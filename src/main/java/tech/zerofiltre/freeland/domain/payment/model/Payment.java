package tech.zerofiltre.freeland.domain.payment.model;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.payment.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;

import java.time.*;

public class Payment {

    private long paymentId;
    private FreelancerId freelancerId;
    private Amount amount;
    private LocalDateTime executionDate;
    private LocalDateTime executionPeriod;
    private ServiceContractId serviceContractId;
    private PaymentProvider provider;

    private Payment(PaymentBuilder paymentBuilder) {
        this.paymentId = paymentBuilder.paymentId;
        this.freelancerId = paymentBuilder.freelancerId;
        this.amount = paymentBuilder.amount;
        this.executionDate = paymentBuilder.executionDate;
        this.executionPeriod = paymentBuilder.executionPeriod;
        this.serviceContractId = paymentBuilder.serviceContractId;
        this.provider = paymentBuilder.provider;
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public Payment send() {
        paymentId = provider.registerPayment(this).getPaymentId();
        System.out.println("Payment of amount "+amount+" will be sent to Freelancer " + freelancerId);
        return this;

    }

    public ServiceContractId getServiceContractId() {
        return serviceContractId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public FreelancerId getFreelancerId() {
        return freelancerId;
    }

    public Amount getAmount() {
        return amount;
    }

    public LocalDateTime getExecutionDate() {
        return executionDate;
    }

    public LocalDateTime getExecutionPeriod() {
        return executionPeriod;
    }

    public PaymentProvider getProvider() {
        return provider;
    }

    public static class PaymentBuilder {
        private long paymentId;
        private FreelancerId freelancerId;
        private Amount amount;
        private LocalDateTime executionDate;
        private LocalDateTime executionPeriod;
        private ServiceContractId serviceContractId;
        private PaymentProvider provider;

        public PaymentBuilder copy(Payment payment) {
            this.paymentId = payment.paymentId;
            this.freelancerId = payment.freelancerId;
            this.amount = payment.amount;
            this.executionDate = payment.executionDate;
            this.executionPeriod = payment.executionPeriod;
            this.serviceContractId = payment.serviceContractId;
            this.provider = payment.provider;
            return this;
        }

        public PaymentBuilder paymentId(long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public PaymentBuilder freelancerId(FreelancerId freelancerId) {
            this.freelancerId = freelancerId;
            return this;
        }

        public PaymentBuilder amount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public PaymentBuilder executionDate(LocalDateTime executionDate) {
            this.executionDate = executionDate;
            return this;
        }

        public PaymentBuilder executionPeriod(LocalDateTime executionPeriod) {
            this.executionPeriod = executionPeriod;
            return this;
        }

        public PaymentBuilder serviceContractId(ServiceContractId serviceContractId) {
            this.serviceContractId = serviceContractId;
            return this;
        }

        public PaymentBuilder provider(PaymentProvider provider) {
            this.provider = provider;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }


    }

}
