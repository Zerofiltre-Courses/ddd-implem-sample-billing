package tech.zerofiltre.freeland.infra.providers.notification;

import lombok.extern.slf4j.*;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;
import tech.zerofiltre.freeland.application.*;
import tech.zerofiltre.freeland.domain.agency.model.useCases.*;
import tech.zerofiltre.freeland.domain.bill.useCases.*;
import tech.zerofiltre.freeland.domain.payment.useCases.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.*;

@Slf4j
@Component
public class ServiceContractKafkaListener {

    private ListenToServiceContractStarted listenToServiceContractStarted;

    private final PaymentProvider paymentProvider;
    private final ServiceContractProvider serviceContractProvider;
    private final BillProvider billProvider;
    private final AgencyProvider agencyProvider;

    public ServiceContractKafkaListener(PaymentProvider paymentProvider, ServiceContractProvider serviceContractProvider, BillProvider billProvider, AgencyProvider agencyProvider) {
        this.paymentProvider = paymentProvider;
        this.serviceContractProvider = serviceContractProvider;
        this.billProvider = billProvider;
        this.agencyProvider = agencyProvider;

        this.listenToServiceContractStarted = new ListenToServiceContractStarted(paymentProvider, serviceContractProvider, billProvider, agencyProvider);
    }


    @KafkaListener(topics = "${service-contract.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ServiceContractStarted message)
            throws ScheduleFreelancerPaymentException, ScheduleClientBillingException {
        listenToServiceContractStarted.consume(message);

    }
}
