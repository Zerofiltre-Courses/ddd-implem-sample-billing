package tech.zerofiltre.freeland.domain.payment.useCases;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import tech.zerofiltre.freeland.domain.Amount;
import tech.zerofiltre.freeland.domain.Rate;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import tech.zerofiltre.freeland.domain.payment.Payment;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.ServiceContractProvider;

public class ScheduleFreelancerPayment {

  private final PaymentProvider paymentProvider;
  private final ServiceContractProvider serviceContractProvider;
  private final static float TAXES_RATE = 0.47f;

  public ScheduleFreelancerPayment(PaymentProvider paymentProvider, ServiceContractProvider serviceContractProvider) {
    this.paymentProvider = paymentProvider;
    this.serviceContractProvider = serviceContractProvider;
  }

  public Payment execute(ServiceContractId serviceContractId, FreelancerId freelancerId,
      Rate serviceContractRate, float serviceFeesRate) throws ScheduleFreelancerPaymentException {

    serviceContractProvider.serviceContractOfId(serviceContractId)
        .orElseThrow(() -> new ScheduleFreelancerPaymentException(
            "The service contract of id: " + serviceContractId.getContractNumber() + "is either missing or terminated"));

    Payment payment = new Payment();
    payment.setServiceContractId(serviceContractId);
    payment.setFreelancerId(freelancerId);
    payment.setAmount(getAmount(serviceContractRate, serviceFeesRate));
    payment.setExecutionDate(new Date());
    payment.setExecutionPeriod(LocalDateTime.now().minusMonths(1));

    sendPayment(payment);
    return paymentProvider.registerPayment(payment);

  }


  private Amount getAmount(Rate serviceContractRate, float serviceFeesRate) {

    DecimalFormat df = new DecimalFormat("0.00");
    float value;
    switch (serviceContractRate.getFrequency()) {
      case DAILY:
        value = serviceContractRate.getValue() * 20;
        break;
      case MONTHLY:
        value = serviceContractRate.getValue();
        break;
      default:
        value = serviceContractRate.getValue() / 12;
    }

    return new Amount(Float.parseFloat(df.format(value * (1 - serviceFeesRate) * (1 - TAXES_RATE))),
        serviceContractRate.getCurrency());
  }

  private void sendPayment(Payment payment) {
    System.out.println("Payment " + payment.getPaymentId() + " sent to Freelancer");
  }


}
