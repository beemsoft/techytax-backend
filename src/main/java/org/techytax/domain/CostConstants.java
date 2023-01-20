package org.techytax.domain;

public interface CostConstants {

	@Deprecated
	CostType UNDETERMINED = new CostType(0);

	CostType INCOME_CURRENT_ACCOUNT = new CostType(1);
	CostType EXPENSE_CURRENT_ACCOUNT = new CostType(2);

	CostType DEPOSIT = new CostType(4);
	CostType WITHDRAWAL = new CostType(5);

	CostType EXPENSE_OTHER_ACCOUNT = new CostType(7);
	CostType TRAVEL_WITH_PUBLIC_TRANSPORT_OTHER_ACCOUNT = new CostType(8);
	CostType TRAVEL_WITH_PUBLIC_TRANSPORT = new CostType(9);
	CostType TO_SAVINGS_ACCOUNT = new CostType(10);
	CostType VAT = new CostType(12);
	CostType BUSINESS_FOOD = new CostType(13);
	CostType BUSINESS_CAR = new CostType(14);
	CostType FROM_SAVINGS_ACCOUNT = new CostType(15);
	CostType INVESTMENT = new CostType(16);

	CostType TO_PRIVATE_ACCOUNT = new CostType(17);
	CostType FROM_PRIVATE_ACCOUNT = new CostType(18);

	CostType BUSINESS_CAR_OTHER_ACCOUNT = new CostType(19);
	CostType BUSINESS_FOOD_OTHER_ACCOUNT = new CostType(20);

	CostType INVESTMENT_OTHER_ACCOUNT = new CostType(26);
	CostType ADVERTORIAL = new CostType(27);
	CostType ADVERTORIAL_NO_VAT = new CostType(28);
	CostType INCOME_TAX = new CostType(29);
	CostType INCOME_TAX_PAID_BACK = new CostType(30);
	CostType ROAD_TAX = new CostType(31);
	CostType EXPENSE_CREDIT_CARD = new CostType(32);
	CostType INTEREST = new CostType(33);

	CostType VAT_CORRECTION = new CostType(35);

	@Deprecated  // This is automatically derived from Business car data
	CostType VAT_CORRECTION_CAR_PRIVATE = new CostType(36);
	CostType INVOICE_PAID = new CostType(38);
	CostType INVOICE_SENT = new CostType(39);
	CostType REPURCHASES = new CostType(40);
	CostType SETTLEMENT = new CostType(41);
	CostType SETTLEMENT_INTEREST = new CostType(42);

	CostType SETTLEMENT_OTHER_ACCOUNT = new CostType(44);
	CostType SETTLEMENT_DISCOUNT = new CostType(45);
	CostType INCOME_CURRENT_ACCOUNT_IGNORE = new CostType(46);
	CostType EXPENSE_INSIDE_EU = new CostType(47);

	float FOOD_TAXFREE_PERCENTAGE = 0.735f;

	int INVESTMENT_MINIMUM_AMOUNT = 450;


}
