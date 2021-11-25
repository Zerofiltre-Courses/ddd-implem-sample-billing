package tech.zerofiltre.freelandbilling.domain.payment.useCases;

import java.util.Optional;
import tech.zerofiltre.freelandbilling.domain.payment.Payment;

public interface PaymentProvider {

  Optional<Payment> paymentOfId(String paymentId);

  Payment registerPayment(Payment payment);

}
