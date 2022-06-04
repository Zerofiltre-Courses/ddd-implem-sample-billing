package tech.zerofiltre.freeland.domain.freelancer.model;

import tech.zerofiltre.freeland.domain.*;

public class Freelancer {

    private FreelancerId freelancerId;
    private BankInfo bankInfo;
    private Amount salary;


    public Amount getSalary() {
        return salary;
    }


    public FreelancerId getFreelancerId() {
        return freelancerId;
    }


    public BankInfo getBankInfo() {
        return bankInfo;
    }

    @Override
    public String toString() {
        return "Freelancer{" +
                "freelancerId=" + freelancerId +
                ", bankInfo=" + "****" + bankInfo.getIban().substring(4) +
                ", salary=" + salary +
                '}';
    }
}
