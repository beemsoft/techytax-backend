package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.techytax.domain.Activum;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BookValue;
import org.techytax.domain.Cost;
import org.techytax.domain.CostConstants;
import org.techytax.domain.FiscalPeriod;
import org.techytax.domain.VatDeclarationData;
import org.techytax.domain.VatPeriodType;
import org.techytax.domain.VatType;
import org.techytax.domain.fiscal.FiscalOverview;
import org.techytax.domain.fiscal.VatReport;
import org.techytax.helper.FiscalOverviewHelper;
import org.techytax.model.security.User;
import org.techytax.repository.ActivumRepository;
import org.techytax.repository.BookRepository;
import org.techytax.repository.CostRepository;
import org.techytax.saas.domain.Registration;
import org.techytax.saas.repository.RegistrationRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;
import org.techytax.util.DateHelper;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.math.BigDecimal.ZERO;
import static org.techytax.helper.AmountHelper.roundDownToInteger;

@RestController
public class FiscalRestController {

    public static final String ZERO_PREFIX_PHONE_NUMBER = "0";
    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;

    private FiscalOverviewHelper fiscalOverviewHelper;
    private CostRepository costRepository;
    private ActivumRepository activumRepository;
    private BookRepository bookRepository;
    private RegistrationRepository registrationRepository;
    private UserRepository userRepository;
    private RestTemplate restTemplate;

    @Autowired
    public FiscalRestController(
      JwtTokenUtil jwtTokenUtil,
      FiscalOverviewHelper fiscalOverviewHelper,
      CostRepository costRepository,
      ActivumRepository activumRepository,
      BookRepository bookRepository,
      RegistrationRepository registrationRepository,
      UserRepository userRepository,
      RestTemplate restTemplate
    ) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.fiscalOverviewHelper = fiscalOverviewHelper;
        this.costRepository = costRepository;
        this.activumRepository = activumRepository;
        this.bookRepository = bookRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "auth/fiscal-overview", method = RequestMethod.GET)
    public FiscalOverview getFiscalOverview(HttpServletRequest request) throws Exception {
        User user = getUser(request);
        return fiscalOverviewHelper.createFiscalOverview(user);
    }

    @RequestMapping(value = "auth/fiscal-overview", method = RequestMethod.POST)
    public void sendFiscalData(HttpServletRequest request, @RequestBody VatReport vatReport) throws Exception {
        User user = getUser(request);
        if (vatReport.getInvestments() != null && !vatReport.getInvestments().isEmpty()) {
            saveActiva(vatReport.getInvestments(), user);
        }
        saveCosts(vatReport, user);
        addBookValues(vatReport, user);
    }

    private void saveActiva(ArrayList<Activum> activa, User user) {
        for (Activum activum : activa) {
            activum.setUser(user);
            activumRepository.save(activum);
        }
    }

    private void saveCosts(@RequestBody VatReport vatReport, User user) {
        if (vatReport.getTotalCarCosts().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.BUSINESS_CAR);
            cost.setAmount(vatReport.getTotalCarCosts());
            cost.setDescription("Total car costs");
            costRepository.save(cost);
        }
        if (vatReport.getTotalTransportCosts().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.TRAVEL_WITH_PUBLIC_TRANSPORT);
            cost.setAmount(vatReport.getTotalTransportCosts());
            cost.setDescription("Total transport costs");
            costRepository.save(cost);
        }
        if (vatReport.getTotalOfficeCosts().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.SETTLEMENT);
            cost.setAmount(vatReport.getTotalOfficeCosts());
            cost.setDescription("Total office costs");
            costRepository.save(cost);
        }
        if (vatReport.getTotalOtherCosts().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.EXPENSE_CURRENT_ACCOUNT);
            cost.setAmount(vatReport.getTotalOtherCosts());
            cost.setDescription("Total other costs");
            costRepository.save(cost);
        }
        if (vatReport.getTotalFoodCosts().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.BUSINESS_FOOD);
            cost.setAmount(vatReport.getTotalFoodCosts());
            cost.setDescription("Total food costs");
            costRepository.save(cost);
        }
        if (vatReport.getTotalVatOut().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.EXPENSE_CURRENT_ACCOUNT);
            cost.setVat(vatReport.getTotalVatOut());
            cost.setDescription("Total VAT out");
            costRepository.save(cost);
        }
        if (vatReport.getTotalVatIn().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.INCOME_CURRENT_ACCOUNT);
            cost.setVat(vatReport.getTotalVatIn());
            cost.setDescription("Total VAT in");
            costRepository.save(cost);
        }
        if (vatReport.getSentInvoices().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.INVOICE_PAID);
            cost.setAmount(vatReport.getSentInvoices());
            cost.setDescription("Total paid invoices");
            costRepository.save(cost);
        }
        if (vatReport.getVatSaldo().compareTo(ZERO) > 0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setDate(vatReport.getLatestTransactionDate());
            cost.setCostType(CostConstants.VAT);
            cost.setAmount(vatReport.getVatSaldo());
            cost.setDescription("VAT saldo");
            costRepository.save(cost);
        }
    }

    private VatDeclarationData createVatDeclarationData(User user, VatReport vatReport) throws Exception {
        Registration registration = registrationRepository.findByUser(user).stream().findFirst().get();
        VatDeclarationData data = new VatDeclarationData();
        FiscalPeriod period = DateHelper.getLatestVatPeriod(registration.getFiscalData().getDeclarationPeriod());
        data.setStartDate(period.getBeginDate());
        data.setEndDate(period.getEndDate());
        data.setFiscalNumber(registration.getFiscalData().getVatNumber());
        data.setInitials(registration.getPersonalData().getInitials());
        data.setSurname(registration.getPersonalData().getSurname());
        data.setPhoneNumber(ZERO_PREFIX_PHONE_NUMBER +registration.getPersonalData().getPhoneNumber());
        data.setTaxedTurnoverSuppliesServicesGeneralTariff(roundDownToInteger(vatReport.getSentInvoices()));
        BigInteger tax = roundDownToInteger(new BigDecimal(data.getTaxedTurnoverSuppliesServicesGeneralTariff())
                .multiply(BigDecimal.valueOf(VatType.HIGH.getValue())));
        if (VatPeriodType.PER_QUARTER.equals(registration.getFiscalData().getDeclarationPeriod())) {
            data.setValueAddedTaxOnInput(roundDownToInteger(vatReport.getTotalVatOut()));
            data.setValueAddedTaxPrivateUse(vatReport.getVatCorrectionForPrivateUsage());
            data.setValueAddedTaxSuppliesServicesGeneralTariff(tax);
            BigInteger owed = data.getValueAddedTaxSuppliesServicesGeneralTariff();
            if (data.getValueAddedTaxPrivateUse() != null) {
                owed = owed.add(data.getValueAddedTaxPrivateUse());
            } else {
                data.setValueAddedTaxPrivateUse(BigInteger.ZERO);
            }
            data.setValueAddedTaxOwed(owed);
            BigInteger owedToBePaidBack = owed.subtract(data.getValueAddedTaxOnInput());
            data.setValueAddedTaxOwedToBePaidBack(owedToBePaidBack);
        } else {
            data.setValueAddedTaxOnInput(BigInteger.ZERO);
            data.setValueAddedTaxPrivateUse(BigInteger.ZERO);
            data.setValueAddedTaxSuppliesServicesGeneralTariff(BigInteger.ZERO);
            data.setValueAddedTaxOwedToBePaidBack(BigInteger.ZERO);
        }
        return data;
    }

    private void addBookValues(VatReport vatReport, User user) {
        if (vatReport.getLatestTransactionDate().getYear() < LocalDate.now().getYear()) {
            // TODO: fix NPE vat saldo null for KOR
            if (vatReport.getVatSaldo().compareTo(ZERO) > 0) {
                BookValue bookValue = new BookValue();
                bookValue.setUser(user);
                bookValue.setBalanceType(BalanceType.VAT_TO_BE_PAID);
                bookValue.setSaldo(vatReport.getVatSaldo().toBigInteger());
                bookValue.setBookYear(LocalDate.now().getYear() - 1);
                bookRepository.save(bookValue);
            }
        }
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
