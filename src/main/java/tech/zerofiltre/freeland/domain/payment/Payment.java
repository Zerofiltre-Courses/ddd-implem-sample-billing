package tech.zerofiltre.freeland.domain.payment;

import java.time.LocalDateTime;
import java.util.Date;
import tech.zerofiltre.freeland.domain.Amount;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;

public class Payment {

  private Long paymentId;
  private FreelancerId freelancerId;
  private Amount amount;
  private Date executionDate;
  private LocalDateTime executionPeriod;
  private ServiceContractId serviceContractId;

  public ServiceContractId getServiceContractId() {
    return serviceContractId;
  }

  public void setServiceContractId(
      ServiceContractId serviceContractId) {
    this.serviceContractId = serviceContractId;
  }


  public Long getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

  public FreelancerId getFreelancerId() {
    return freelancerId;
  }

  public void setFreelancerId(FreelancerId freelancerId) {
    this.freelancerId = freelancerId;
  }


  public Amount getAmount() {
    return amount;
  }

  public void setAmount(Amount amount) {
    this.amount = amount;
  }


  public Date getExecutionDate() {
    return executionDate;
  }

  public void setExecutionDate(Date executionDate) {
    this.executionDate = executionDate;
  }

  public LocalDateTime getExecutionPeriod() {
    return executionPeriod;
  }

  public void setExecutionPeriod(LocalDateTime executionPeriod) {
    this.executionPeriod = executionPeriod;
  }
}
