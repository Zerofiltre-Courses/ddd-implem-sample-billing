package tech.zerofiltre.freeland.application;

import tech.zerofiltre.freeland.domain.Rate;
import tech.zerofiltre.freeland.domain.agency.model.AgencyId;
import tech.zerofiltre.freeland.domain.agency.model.useCases.AgencyProvider;
import tech.zerofiltre.freeland.domain.bill.useCases.BillProvider;
import tech.zerofiltre.freeland.domain.bill.useCases.ScheduleClientBilling;
import tech.zerofiltre.freeland.domain.bill.useCases.ScheduleClientBillingException;
import tech.zerofiltre.freeland.domain.client.model.ClientId;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import tech.zerofiltre.freeland.domain.payment.useCases.PaymentProvider;
import tech.zerofiltre.freeland.domain.payment.useCases.ScheduleFreelancerPayment;
import tech.zerofiltre.freeland.domain.payment.useCases.ScheduleFreelancerPaymentException;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractStarted;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.*;

public class ListenToServiceContractStarted {

  private ScheduleFreelancerPayment scheduleFreelancerPayment;
  private ScheduleClientBilling scheduleClientBilling;

  private final PaymentProvider paymentProvider;
  private final ServiceContractProvider serviceContractProvider;
  private final BillProvider billProvider;
  private final AgencyProvider agencyProvider;

  public ListenToServiceContractStarted(PaymentProvider paymentProvider, ServiceContractProvider serviceContractProvider,
                                        BillProvider billProvider, AgencyProvider agencyProvider) {

    this.paymentProvider = paymentProvider;
    this.serviceContractProvider = serviceContractProvider;
    this.billProvider = billProvider;
    this.agencyProvider = agencyProvider;

    this.scheduleFreelancerPayment = new ScheduleFreelancerPayment(paymentProvider, serviceContractProvider);
    this.scheduleClientBilling = new ScheduleClientBilling(billProvider, agencyProvider, serviceContractProvider);
  }

  public void consume(ServiceContractStarted message)
      throws ScheduleFreelancerPaymentException, ScheduleClientBillingException {

    FreelancerId freelancerId = new FreelancerId(message.getFreelancerSiren(), message.getFreelancerName());

    ServiceContractId serviceContractId = new ServiceContractId(message.getServiceContractNumber());
    Rate serviceContractRate = new Rate(message.getRateValue(), message.getRateCurrency(), message.getRateFrequency());

    AgencyId agencyId = new AgencyId(message.getAgencySiren(), message.getAgencyName());
    ClientId clientId = new ClientId(message.getClientSiren(), message.getClientName());

    scheduleFreelancerPayment
        .execute(serviceContractId, freelancerId, serviceContractRate, message.getServiceFeesRate());
    scheduleClientBilling.execute(clientId, agencyId, serviceContractId);

  }
}
