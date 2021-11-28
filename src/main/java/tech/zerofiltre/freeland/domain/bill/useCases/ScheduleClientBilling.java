package tech.zerofiltre.freeland.domain.bill.useCases;

import java.time.LocalDateTime;
import java.util.Date;
import tech.zerofiltre.freeland.domain.Amount;
import tech.zerofiltre.freeland.domain.Rate;
import tech.zerofiltre.freeland.domain.agency.model.AgencyId;
import tech.zerofiltre.freeland.domain.agency.model.useCases.AgencyProvider;
import tech.zerofiltre.freeland.domain.bill.model.Bill;
import tech.zerofiltre.freeland.domain.client.model.ClientId;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.ServiceContractProvider;

public class ScheduleClientBilling {

  private final BillProvider billProvider;
  private final AgencyProvider agencyProvider;
  private final ServiceContractProvider serviceContractProvider;

  public ScheduleClientBilling(BillProvider billProvider,
      AgencyProvider agencyProvider,
      ServiceContractProvider serviceContractProvider) {
    this.billProvider = billProvider;
    this.agencyProvider = agencyProvider;
    this.serviceContractProvider = serviceContractProvider;
  }

  public Bill execute(ClientId clientId, AgencyId agencyId, ServiceContractId serviceContractId)
      throws ScheduleClientBillingException {
    Bill billToSend = new Bill();

    billToSend.setClientId(clientId);
    agencyProvider.agencyOfId(agencyId)
        .orElseThrow(() -> new ScheduleClientBillingException("There is no agency for: " + agencyId));

    Amount amount = serviceContractProvider.serviceContractOfId(serviceContractId)
        .map(serviceContract ->
            calculateAmount(serviceContract.getRate())
        ).orElseThrow(
            () -> new ScheduleClientBillingException("There is no Service contract for: " + serviceContractId));

    billToSend.setAmount(amount);
    billToSend.setAgencyId(agencyId);
    billToSend = billProvider.registerBill(billToSend);
    billToSend.setIssuedDate(new Date());
    billToSend.setExecutionPeriod(LocalDateTime.now().minusMonths(1));
    sendBill(billToSend);
    return billToSend;
  }

  private Amount calculateAmount(Rate rate) {
    float value;
    switch (rate.getFrequency()) {
      case DAILY:
        value = rate.getValue() * 20;
        break;
      case MONTHLY:
        value = rate.getValue();
        break;
      default:
        value = rate.getValue() / 12;
        break;
    }
    return new Amount(value, rate.getCurrency());
  }

  private void sendBill(Bill billToSend) {
    System.out.println("Bill" + billToSend.getBillId() + " sent to Client");
  }

}
