package tech.zerofiltre.freeland.domain.payment;

import java.util.Optional;
import tech.zerofiltre.freeland.domain.payment.model.Payment;

public interface PaymentProvider {

  Optional<Payment> paymentOfId(String paymentId);

  Payment registerPayment(Payment payment);

}
