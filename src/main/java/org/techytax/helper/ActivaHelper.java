package org.techytax.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.Activum;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BookValue;
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
class ActivaHelper {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ActivumRepository activumRepository;

	private int bookYear;
	private Map<BalanceType, FiscalBalance> activaMap;

	Map<BalanceType, FiscalBalance> handleActiva(String username) {
		bookYear = DateHelper.getYear(new Date()) - 1;
		activaMap = new HashMap<>();

		handleActivum(username, BalanceType.MACHINERY);
		handleActivum(username, BalanceType.OFFICE);
		handleActivum(username, BalanceType.CAR);

		return activaMap;
	}

	private void handleActivum(String username, BalanceType balanceType) {
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
		activaMap.put(balanceType, fiscalBalance);
	}

	BigInteger getOfficeBottomValue(String username) {
		Office office = activumRepository.findOffice(username);
		return office.getTerrainValue();
	}
}
