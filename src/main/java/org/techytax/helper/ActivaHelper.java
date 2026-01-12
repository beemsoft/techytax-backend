package org.techytax.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.techytax.domain.Activum;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BookValue;
import org.techytax.domain.BusinessCar;
import org.techytax.domain.FiscalBalance;
import org.techytax.domain.Office;
import org.techytax.repository.ActivumRepository;
import org.techytax.repository.BookRepository;
import org.techytax.util.DateHelper;

import org.techytax.model.security.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigInteger.ZERO;

@Component
public class ActivaHelper {

	private static final Logger log = LoggerFactory.getLogger(ActivaHelper.class);

	public static final LocalDate YEAR_START = LocalDate.now().minusYears(1).withDayOfYear(1);
	public static final LocalDate YEAR_END = LocalDate.now().withDayOfYear(1).minusDays(1);

	private final BookRepository bookRepository;

	private final ActivumRepository activumRepository;

	private int bookYear;
	private final Map<BalanceType, FiscalBalance> balanceMap = new HashMap<>();

	ActivaHelper(BookRepository bookRepository, ActivumRepository activumRepository) {
		this.bookRepository = bookRepository;
		this.activumRepository = activumRepository;
	}

	Map<BalanceType, FiscalBalance> handleActiva(User user) {
		bookYear = DateHelper.getYear(new Date()) - 1;
		BigInteger totalActiva = ZERO;
		BigInteger newValue = handleActivumWithDepreciation(user, BalanceType.MACHINERY);
		totalActiva = totalActiva.add(newValue);
		newValue = handleActivumWithDepreciation(user, BalanceType.OFFICE);
		totalActiva = totalActiva.add(newValue);
		newValue = handleActivumWithDepreciation(user, BalanceType.CAR);
		totalActiva = totalActiva.add(newValue);
		newValue = handleBalanceType(user, BalanceType.CURRENT_ASSETS);
		totalActiva = totalActiva.add(newValue);
		newValue = handleBalanceType(user, BalanceType.VAT_TO_BE_PAID);
		BigInteger pensionValue = handleBalanceType(user, BalanceType.PENSION);
		BigInteger nonCurrentAssetsValue = totalActiva.subtract(pensionValue).subtract(newValue);
		handleBalanceTypeWithEndValue(user, nonCurrentAssetsValue);

		return balanceMap;
	}

	private BigInteger handleActivumWithDepreciation(User user, BalanceType balanceType) {
		Collection<Activum> newActiva = activumRepository.findActivums(user, balanceType, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
		BigDecimal totalCost = BigDecimal.ZERO;
		BigInteger totalValue = BigInteger.ZERO;
		BigInteger totalRemainingValue = ZERO;
		for (Activum activum : newActiva) {
			if (activum.getPurchasePrice() == null) {
				throw new RuntimeException("No purchase price for: " + activum.getDescription());
			}
			totalCost = totalCost.add(activum.getPurchasePrice());
			totalValue = totalValue.add(DepreciationHelper.getValueAtEndOfFiscalYear(activum));
			totalRemainingValue = totalRemainingValue.add(activum.getRemainingValue());
		}

		FiscalBalance fiscalBalance = new FiscalBalance();
		BookValue previousBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(user, balanceType, bookYear - 1);
		if (previousBookValue == null) {
			fiscalBalance.setBeginSaldo(ZERO);
		} else {
			fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
		}
		fiscalBalance.setEndSaldo(totalValue);
		fiscalBalance.setTotalPurchaseCost(totalCost);
		fiscalBalance.setTotalRemainingValue(totalRemainingValue);
		balanceMap.put(balanceType, fiscalBalance);
		return totalValue;
	}

	private BigInteger handleBalanceType(User user, BalanceType balanceType) {
		FiscalBalance fiscalBalance = new FiscalBalance();
		BookValue previousBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(user, balanceType, bookYear - 1);
		if (previousBookValue == null) {
			fiscalBalance.setBeginSaldo(ZERO);
		} else {
			fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
		}
		BookValue newBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(user, balanceType, bookYear);
		if (newBookValue == null) {
			fiscalBalance.setEndSaldo(ZERO);
		} else {
			fiscalBalance.setEndSaldo(newBookValue.getSaldo());
		}
		balanceMap.put(balanceType, fiscalBalance);
		return fiscalBalance.getEndSaldo();
	}

	private BigInteger handleBalanceTypeWithEndValue(User user, BigInteger endValue) {
		FiscalBalance fiscalBalance = new FiscalBalance();
		BookValue previousBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(user, BalanceType.NON_CURRENT_ASSETS, bookYear - 1);
		if (previousBookValue == null) {
			fiscalBalance.setBeginSaldo(ZERO);
		} else {
			fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
		}
		fiscalBalance.setEndSaldo(endValue);
		balanceMap.put(BalanceType.NON_CURRENT_ASSETS, fiscalBalance);
		return endValue;
	}

	BigInteger getOfficeBottomValue(User user) {
		Office office = activumRepository.findOffice(user);
		if (office != null)
			return office.getTerrainValue();
		else
			return ZERO;
	}

	public BigInteger getVatCorrectionForPrivateUsageBusinessCar(User user) {
		Collection<BusinessCar> carList = activumRepository.findBusinessCars(user, YEAR_START, YEAR_END);
		BigInteger vatCorrectionForPrivateUsage = BigInteger.ZERO;
		for (BusinessCar activum: carList) {
			BigDecimal privateUsage = activum.getVatCorrectionForPrivateUsage();
			double proportion = 1.0d;
			if (activum.getEndDate() != null) {
				proportion = activum.getEndDate().getMonthValue() / 12.0d;
			}
			if (activum.getStartDate().getYear() == LocalDate.now().getYear() - 1) {
				proportion = 1.0d - activum.getStartDate().getMonthValue() / 12.0d;
			}
			privateUsage = privateUsage.multiply(BigDecimal.valueOf(proportion));
			log.info("Private usage for {} is {}", activum.getDescription(), privateUsage);
			vatCorrectionForPrivateUsage = vatCorrectionForPrivateUsage.add(privateUsage.toBigInteger());
		}
	    return vatCorrectionForPrivateUsage;
	}
}
