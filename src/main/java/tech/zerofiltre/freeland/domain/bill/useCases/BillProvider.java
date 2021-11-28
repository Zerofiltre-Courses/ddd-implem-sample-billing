package tech.zerofiltre.freeland.domain.bill.useCases;

import tech.zerofiltre.freeland.domain.bill.model.Bill;

public interface BillProvider {

  Bill billOfId(String billId);

  Bill registerBill(Bill bill);

}
