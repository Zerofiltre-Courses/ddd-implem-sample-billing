package tech.zerofiltre.freelandbilling.domain.payment.useCases;

public class ScheduleBillingException extends Exception {

  public ScheduleBillingException(String message) {
    super(message);
  }
}
