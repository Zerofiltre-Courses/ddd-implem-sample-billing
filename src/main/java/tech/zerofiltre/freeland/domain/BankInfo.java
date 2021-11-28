package tech.zerofiltre.freeland.domain;

public class BankInfo {

  private final String iban;
  private final String bic;
  private final String accountOwner;
  private final String bankName;

  public BankInfo(String iban, String bic, String accountOwner, String bankName) {
    this.iban = iban;
    this.bic = bic;
    this.accountOwner = accountOwner;
    this.bankName = bankName;
  }

  public String getIban() {
    return iban;
  }

  public String getBic() {
    return bic;
  }

  public String getAccountOwner() {
    return accountOwner;
  }

  public String getBankName() {
    return bankName;
  }
}
