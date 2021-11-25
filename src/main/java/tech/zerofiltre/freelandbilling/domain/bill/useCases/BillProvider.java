package tech.zerofiltre.freelandbilling.domain.bill.useCases;

import tech.zerofiltre.freelandbilling.domain.bill.model.Bill;

public interface BillProvider {

  Bill billOfId(String billId);

  Bill registerBill(Bill bill);

}
