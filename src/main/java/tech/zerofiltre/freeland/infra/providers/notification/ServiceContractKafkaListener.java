package tech.zerofiltre.freeland.infra.providers.notification;

import lombok.extern.slf4j.*;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;
import tech.zerofiltre.freeland.application.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.bill.*;
import tech.zerofiltre.freeland.domain.payment.*;
import tech.zerofiltre.freeland.domain.serviceContract.*;
import tech.zerofiltre.freeland.infra.providers.notification.mapper.*;
import tech.zerofiltre.freeland.infra.providers.notification.model.*;

@Slf4j
@Component
public class ServiceContractKafkaListener {

    private final ServiceContractStartedListener serviceContractStartedListener;
    private final KafkaServiceContractEventMapper mapper;

    public ServiceContractKafkaListener(PaymentProvider paymentProvider, ServiceContractProvider serviceContractProvider, BillProvider billProvider, AgencyProvider agencyProvider, KafkaServiceContractEventMapper mapper) {
        this.mapper = mapper;
        this.serviceContractStartedListener = new ServiceContractStartedListener(paymentProvider, serviceContractProvider, billProvider, agencyProvider);
    }


    @KafkaListener(topics = "${service-contract.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(KafkaServiceContractEvent message)
            throws ScheduleFreelancerPaymentException, ScheduleClientBillingException {
        serviceContractStartedListener.consume(mapper.fromKafka(message));

    }
}
