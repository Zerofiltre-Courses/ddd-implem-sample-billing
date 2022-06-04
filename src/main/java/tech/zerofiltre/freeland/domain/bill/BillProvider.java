package tech.zerofiltre.freeland.domain.bill;

import tech.zerofiltre.freeland.domain.bill.model.*;

public interface BillProvider {

  Bill billOfId(String billId);

  Bill registerBill(Bill bill);

}
