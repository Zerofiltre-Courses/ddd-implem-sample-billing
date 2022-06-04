package tech.zerofiltre.freeland.domain.serviceContract.model;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.serviceContract.*;

import java.util.*;

public class ServiceContract {

    private ServiceContractId serviceContractId;
    private Rate rate;
    private ServiceContractProvider serviceContractProvider;


    private ServiceContract(ServiceContractBuilder serviceContractBuilder) {
        this.serviceContractId = serviceContractBuilder.serviceContractId;
        this.rate = serviceContractBuilder.rate;

        this.serviceContractProvider = serviceContractBuilder.serviceContractProvider;
    }

    public Optional<ServiceContract> of(ServiceContractId serviceContractId) {
        Optional<ServiceContract> result = serviceContractProvider.serviceContractOfId(serviceContractId);
        result.ifPresent(serviceContract -> serviceContract.serviceContractProvider = this.serviceContractProvider);
        return result;
    }

    public static ServiceContractBuilder builder() {
        return new ServiceContract.ServiceContractBuilder();
    }

    public ServiceContractId getServiceContractId() {
        return serviceContractId;
    }

    public Rate getRate() {
        return rate;
    }

    public ServiceContractProvider getServiceContractProvider() {
        return serviceContractProvider;
    }

    public static class ServiceContractBuilder {
        private ServiceContractProvider serviceContractProvider;
        private ServiceContractId serviceContractId;
        private Rate rate;

        public ServiceContractBuilder copy(ServiceContract serviceContract) {
            this.rate = serviceContract.rate;
            this.serviceContractId = serviceContract.serviceContractId;
            return this;
        }

        public ServiceContractBuilder serviceContractProvider(ServiceContractProvider serviceContractProvider) {
            this.serviceContractProvider = serviceContractProvider;
            return this;
        }

        public ServiceContractBuilder serviceContractId(ServiceContractId serviceContractId) {
            this.serviceContractId = serviceContractId;
            return this;
        }

        public ServiceContractBuilder rate(Rate rate) {
            this.rate = rate;
            return this;
        }

        public ServiceContract build() {
            return new ServiceContract(this);
        }

    }
}
