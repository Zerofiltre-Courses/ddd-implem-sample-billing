package tech.zerofiltre.freeland.domain.bill.model;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.bill.*;
import tech.zerofiltre.freeland.domain.client.model.*;

import java.time.*;

public class Bill {

    private final BillProvider billProvider;
    private String billId;
    private Amount amount;
    private boolean paymentStatus = false;
    private LocalDateTime issuedDate;
    private String serviceContractId;
    private LocalDateTime executionPeriod;
    private ClientId clientId;
    private AgencyId agencyId;

    private Bill(BillBuilder billBuilder) {
        this.billId = billBuilder.billId;
        this.amount = billBuilder.amount;
        this.paymentStatus = billBuilder.paymentStatus;
        this.issuedDate = billBuilder.issuedDate;
        this.serviceContractId = billBuilder.serviceContractId;
        this.executionPeriod = billBuilder.executionPeriod;
        this.clientId = billBuilder.clientId;
        this.agencyId = billBuilder.agencyId;
        this.billProvider = billBuilder.billProvider;
    }

    public Bill send(){
        billId = billProvider.registerBill(this).billId;
        System.out.println("Bill of amount " + amount + " sent to Client "+ clientId);
        return this;
    }

    public static BillBuilder builder() {
        return new BillBuilder();
    }

    public String getBillId() {
        return billId;
    }

    public Amount getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paymentStatus;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public String getServiceContractId() {
        return serviceContractId;
    }

    public LocalDateTime getExecutionPeriod() {
        return executionPeriod;
    }

    public ClientId getClientId() {
        return clientId;
    }

    public AgencyId getAgencyId() {
        return agencyId;
    }

    public static class BillBuilder {
        private BillProvider billProvider;
        private String billId;
        private Amount amount;
        private boolean paymentStatus = false;
        private LocalDateTime issuedDate;
        private String serviceContractId;
        private LocalDateTime executionPeriod;
        private ClientId clientId;
        private AgencyId agencyId;

        public BillBuilder copy(Bill bill){
            billId = bill.billId;
            billProvider = bill.billProvider;
            amount = bill.amount;
            paymentStatus = bill.paymentStatus;
            issuedDate = bill.issuedDate;
            serviceContractId = bill.serviceContractId;
            executionPeriod = bill.executionPeriod;
            clientId = bill.clientId;
            agencyId = bill.agencyId;
            return this;
        }

        public BillBuilder billId(String billId) {
            this.billId = billId;
            return this;
        }

        public BillBuilder amount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public BillBuilder paymentStatus(boolean paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public BillBuilder issuedDate(LocalDateTime issuedDate) {
            this.issuedDate = issuedDate;
            return this;
        }

        public BillBuilder serviceContractId(String serviceContractId) {
            this.serviceContractId = serviceContractId;
            return this;
        }

        public BillBuilder executionPeriod(LocalDateTime executionPeriod) {
            this.executionPeriod = executionPeriod;
            return this;
        }

        public BillBuilder clientId(ClientId clientId) {
            this.clientId = clientId;
            return this;
        }

        public BillBuilder agencyId(AgencyId agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public BillBuilder billProvider(BillProvider billProvider){
            this.billProvider = billProvider;
            return this;
        }

        public Bill build() {
            return new Bill(this);
        }
    }
}
