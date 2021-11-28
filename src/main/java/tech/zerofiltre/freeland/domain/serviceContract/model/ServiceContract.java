package tech.zerofiltre.freeland.domain.serviceContract.model;

import tech.zerofiltre.freeland.domain.Rate;

public class ServiceContract {

  private ServiceContractId serviceContractId;
  private Rate rate;

  public ServiceContractId getServiceContractId() {
    return serviceContractId;
  }

  public void setServiceContractId(
      ServiceContractId serviceContractId) {
    this.serviceContractId = serviceContractId;
  }

  public Rate getRate() {
    return rate;
  }

  public void setRate(Rate rate) {
    this.rate = rate;
  }
}