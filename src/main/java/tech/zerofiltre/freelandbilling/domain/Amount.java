package tech.zerofiltre.freelandbilling.domain;

public class Amount {

  private final float value;
  private final Currency currency;

  public Amount(float value, Currency currency) {
    this.value = value;
    this.currency = currency;
  }

  public float getValue() {
    return value;
  }

  public Currency getCurrency() {
    return currency;
  }
}
