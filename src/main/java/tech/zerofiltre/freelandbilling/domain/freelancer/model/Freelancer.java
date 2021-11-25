package tech.zerofiltre.freelandbilling.domain.freelancer.model;

import tech.zerofiltre.freelandbilling.domain.Amount;
import tech.zerofiltre.freelandbilling.domain.BankInfo;

public class Freelancer {

  private FreelancerId freelancerId;
  private BankInfo bankInfo;
  private Amount salary;


  public Amount getSalary() {
    return salary;
  }

  public void setSalary(Amount salary) {
    this.salary = salary;
  }


  public FreelancerId getFreelancerId() {
    return freelancerId;
  }

  public void setFreelancerId(FreelancerId freelancerId) {
    this.freelancerId = freelancerId;
  }

  public BankInfo getBankInfo() {
    return bankInfo;
  }

  public void setBankInfo(BankInfo bankInfo) {
    this.bankInfo = bankInfo;
  }
}
