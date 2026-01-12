package org.techytax.helper;

import org.springframework.stereotype.Component;
import org.techytax.domain.BookValue;
import org.techytax.domain.Cost;
import org.techytax.domain.FiscalBalance;
import org.techytax.domain.FiscalPeriod;
import org.techytax.util.DateHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static org.techytax.domain.BalanceType.NON_CURRENT_ASSETS;
import static org.techytax.domain.BalanceType.PENSION;
import static org.techytax.domain.BalanceType.VAT_TO_BE_PAID;
import static org.techytax.helper.AmountHelper.roundDownToInteger;

@Component
public class PassivaHelper {
//  private void handleNonCurrentAssets(BigInteger fiscalPension, BigDecimal vatDebt) throws Exception {
//    BookValue currentBookValue = bookValueDao.getBookValue(NON_CURRENT_ASSETS, bookYear);
//    BookValue previousBookValue = bookValueDao.getBookValue(NON_CURRENT_ASSETS, bookYear - 1);
//    BigInteger newSaldo = overview.getBookTotalEnd();
//    newSaldo = newSaldo.subtract(fiscalPension);
//    newSaldo = newSaldo.subtract(roundDownToInteger(vatDebt));
//    if (currentBookValue == null) {
//      BookValue newValue = new BookValue(NON_CURRENT_ASSETS, bookYear, newSaldo);
//      newValue.setUsername(UserCredentialManager.getUsername());
//      bookValueDao.persistEntity(newValue);
//    } else {
//      currentBookValue.setSaldo(newSaldo);
//    }
//    if (previousBookValue != null || currentBookValue != null) {
//      FiscalBalance fiscalBalance = new FiscalBalance();
//      if (previousBookValue != null) {
//        fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
//      }
//      fiscalBalance.setEndSaldo(newSaldo);
//      passivaMap.put(NON_CURRENT_ASSETS, fiscalBalance);
//    }
//  }
//
//  private BigDecimal handleVatToBePaid() throws Exception {
//    BookValue currentBookValue = bookValueDao.getBookValue(VAT_TO_BE_PAID, bookYear);
//    BookValue previousBookValue = bookValueDao.getBookValue(VAT_TO_BE_PAID, bookYear - 1);
//    BigDecimal newSaldo = getVatDebtFromPreviousYear(DateHelper.getLastVatPeriodPreviousYear());
//    if (newSaldo != null && newSaldo.compareTo(BigDecimal.ZERO) == 1) {
//
//      if (currentBookValue == null) {
//        BookValue newValue = new BookValue(VAT_TO_BE_PAID, bookYear, newSaldo.toBigInteger());
//        newValue.setUsername(UserCredentialManager.getUsername());
//        bookValueDao.persistEntity(newValue);
//      } else {
//        currentBookValue.setSaldo(newSaldo.toBigInteger());
//      }
//    }
//    if (previousBookValue != null || currentBookValue != null) {
//      FiscalBalance fiscalBalance = new FiscalBalance();
//      fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
//      fiscalBalance.setEndSaldo(newSaldo.toBigInteger());
//      passivaMap.put(VAT_TO_BE_PAID, fiscalBalance);
//    }
//    return newSaldo;
//  }
//
//  private BigDecimal getVatDebtFromPreviousYear(FiscalPeriod period) {
//    List<Cost> costs = costDao.getInvoicesSent(period);
//    BigDecimal vatBalance = ZERO;
//    for (Cost cost : costs) {
//      vatBalance = vatBalance.add(cost.getVat());
//    }
//    return vatBalance;
//  }
//
//  private BigInteger handleFiscalPension() throws Exception {
//    BookValue currentBookValue = bookValueDao.getBookValue(PENSION, bookYear);
//    BookValue previousBookValue = bookValueDao.getBookValue(PENSION, bookYear - 1);
//
//    BigInteger newSaldo = BigInteger.ZERO;
//    if (currentBookValue == null) {
//      if (previousBookValue != null) {
//        newSaldo = previousBookValue.getSaldo();
//        BookValue newValue = new BookValue(PENSION, bookYear, newSaldo);
//        newValue.setUsername(UserCredentialManager.getUsername());
//        bookValueDao.persistEntity(newValue);
//      }
//    } else {
//      newSaldo = currentBookValue.getSaldo();
//    }
//    if (previousBookValue != null || currentBookValue != null) {
//      FiscalBalance fiscalBalance = new FiscalBalance();
//      fiscalBalance.setBeginSaldo(previousBookValue.getSaldo());
//      fiscalBalance.setEndSaldo(newSaldo);
//      passivaMap.put(PENSION, fiscalBalance);
//    }
//    return newSaldo;
//  }

}
