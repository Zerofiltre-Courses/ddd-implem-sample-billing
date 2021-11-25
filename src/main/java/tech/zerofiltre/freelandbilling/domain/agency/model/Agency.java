package tech.zerofiltre.freelandbilling.domain.agency.model;

import tech.zerofiltre.freelandbilling.domain.Address;
import tech.zerofiltre.freelandbilling.domain.BankInfo;

public class Agency {

  private AgencyId agencyId;
  private BankInfo bankInfo;
  private Address address;

  public AgencyId getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(AgencyId agencyId) {
    this.agencyId = agencyId;
  }

  public BankInfo getBankInfo() {
    return bankInfo;
  }

  public void setBankInfo(BankInfo bankInfo) {
    this.bankInfo = bankInfo;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
