package tech.zerofiltre.freelandbilling.domain.bill.model;

import java.time.LocalDateTime;
import java.util.Date;
import tech.zerofiltre.freelandbilling.domain.Amount;
import tech.zerofiltre.freelandbilling.domain.agency.model.AgencyId;
import tech.zerofiltre.freelandbilling.domain.client.model.Client;

public class Bill {

  private String billId;
  private Amount amount;
  private boolean paymentStatus = false;
  private Date issuedDate;
  private String serviceContractId;
  private LocalDateTime executionPeriod;
  private Client client;
  private AgencyId agencyId;

  public String getBillId() {
    return billId;
  }

  public void setBillId(String billId) {
    this.billId = billId;
  }

  public Amount getAmount() {
    return amount;
  }

  public void setAmount(Amount amount) {
    this.amount = amount;
  }

  public boolean isPaid() {
    return paymentStatus;
  }

  public void setPaymentStatus(boolean paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public Date getIssuedDate() {
    return issuedDate;
  }

  public void setIssuedDate(Date issuedDate) {
    this.issuedDate = issuedDate;
  }

  public String getServiceContractId() {
    return serviceContractId;
  }

  public void setServiceContractId(String serviceContractId) {
    this.serviceContractId = serviceContractId;
  }

  public LocalDateTime getExecutionPeriod() {
    return executionPeriod;
  }

  public void setExecutionPeriod(LocalDateTime executionPeriod) {
    this.executionPeriod = executionPeriod;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public AgencyId getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(AgencyId agencyId) {
    this.agencyId = agencyId;
  }
}
