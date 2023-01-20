package org.techytax.helper;

import lombok.extern.slf4j.Slf4j;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigInteger.ZERO;

@Component
@Slf4j
public
class ActivaHelper {

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

	Map<BalanceType, FiscalBalance> handleActiva(String username) {
		bookYear = DateHelper.getYear(new Date()) - 1;
		BigInteger totalActiva = ZERO;
		BigInteger newValue = handleActivumWithDepreciation(username, BalanceType.MACHINERY);
		totalActiva = totalActiva.add(newValue);
		newValue = handleActivumWithDepreciation(username, BalanceType.OFFICE);
		totalActiva = totalActiva.add(newValue);
		newValue = handleActivumWithDepreciation(username, BalanceType.CAR);
		totalActiva = totalActiva.add(newValue);
		newValue = handleBalanceType(username, BalanceType.CURRENT_ASSETS);
		totalActiva = totalActiva.add(newValue);
		newValue = handleBalanceType(username, BalanceType.VAT_TO_BE_PAID);
		BigInteger pensionValue = handleBalanceType(username, BalanceType.PENSION);
		BigInteger nonCurrentAssetsValue = totalActiva.subtract(pensionValue).subtract(newValue);
		handleBalanceTypeWithEndValue(username, nonCurrentAssetsValue);

		return balanceMap;
	}

	private BigInteger handleActivumWithDepreciation(String username, BalanceType balanceType) {
		Collection<Activum> newActiva = activumRepository.findActivums(username, balanceType, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
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
		BookValue previousBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(username, balanceType, bookYear - 1);
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

	private BigInteger handleBalanceType(String username, BalanceType balanceType) {
		FiscalBalance fiscalBalance = new FiscalBalance();
		BookValue previousBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(username, balanceType, bookYear - 1);
		if (previousBookValue == null) {
			fiscalBalance.setBeginSaldo(ZERO);
		} else {
			fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
		}
		BookValue newBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(username, balanceType, bookYear);
		if (newBookValue == null) {
			fiscalBalance.setEndSaldo(ZERO);
		} else {
			fiscalBalance.setEndSaldo(newBookValue.getSaldo());
		}
		balanceMap.put(balanceType, fiscalBalance);
		return fiscalBalance.getEndSaldo();
	}

	private BigInteger handleBalanceTypeWithEndValue(String username, BigInteger endValue) {
		FiscalBalance fiscalBalance = new FiscalBalance();
		BookValue previousBookValue = bookRepository.findBookValueByUserAndBalanceTypeAndBookYear(username, BalanceType.NON_CURRENT_ASSETS, bookYear - 1);
		if (previousBookValue == null) {
			fiscalBalance.setBeginSaldo(ZERO);
		} else {
			fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
		}
		fiscalBalance.setEndSaldo(endValue);
		balanceMap.put(BalanceType.NON_CURRENT_ASSETS, fiscalBalance);
		return endValue;
	}

	BigInteger getOfficeBottomValue(String username) {
		Office office = activumRepository.findOffice(username);
		if (office != null)
			return office.getTerrainValue();
		else
			return ZERO;
	}

	public BigInteger getVatCorrectionForPrivateUsageBusinessCar(String username) {
		Collection<BusinessCar> carList = activumRepository.findBusinessCars(username, YEAR_START, YEAR_END);
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
