package tech.zerofiltre.freeland.infra.providers.dummy;

import org.springframework.stereotype.*;
import tech.zerofiltre.freeland.domain.bill.*;
import tech.zerofiltre.freeland.domain.bill.model.*;

@Component
public class DummyBillProvider implements BillProvider {
    @Override
    public Bill billOfId(String billId) {
        return Bill.builder().billId(billId).build();
    }

    @Override
    public Bill registerBill(Bill bill) {
        return bill;
    }
}
