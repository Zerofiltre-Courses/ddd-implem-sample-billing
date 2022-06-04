package tech.zerofiltre.freeland.domain;

public class Amount {

  private final float value;
  private final Rate.Currency currency;

  public Amount(float value, Rate.Currency currency) {
    this.value = value;
    this.currency = currency;
  }

  public float getValue() {
    return value;
  }

  public Rate.Currency getCurrency() {
    return currency;
  }

  @Override
  public String toString() {
    return value+" "+currency;
  }
}
