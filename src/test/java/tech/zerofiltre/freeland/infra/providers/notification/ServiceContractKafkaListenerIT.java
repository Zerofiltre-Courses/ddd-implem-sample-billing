package tech.zerofiltre.freeland.infra.providers.notification;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.*;
import org.springframework.kafka.core.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.support.*;
import org.testcontainers.containers.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.utility.*;
import tech.zerofiltre.freeland.domain.Currency;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.agency.model.useCases.*;
import tech.zerofiltre.freeland.domain.bill.model.*;
import tech.zerofiltre.freeland.domain.bill.useCases.*;
import tech.zerofiltre.freeland.domain.payment.*;
import tech.zerofiltre.freeland.domain.payment.useCases.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.*;

import java.util.*;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@Testcontainers
@ContextConfiguration(initializers = ServiceContractKafkaListenerIT.BrokerPropertiesOverrideInitializer.class)
class ServiceContractKafkaListenerIT {


    @MockBean
    private PaymentProvider paymentProvider;
    @MockBean
    private ServiceContractProvider serviceContractProvider;
    @MockBean
    private BillProvider billProvider;
    @MockBean
    private AgencyProvider agencyProvider;

    @Autowired
    KafkaTemplate<String, ServiceContractEvent> kafkaTemplate;


    @Container
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
    private ServiceContractStarted serviceContractStarted;

    @BeforeEach
    void init() {
        serviceContractStarted = new ServiceContractStarted();
        serviceContractStarted.setServiceContractNumber(10L);
        serviceContractStarted.setRateCurrency(Currency.EUR);
        serviceContractStarted.setRateFrequency(Rate.Frequency.DAILY);
        serviceContractStarted.setRateValue(700f);
    }

    @Test
    @DisplayName("On message received, appropriate providers are called")
    void listener() throws InterruptedException {
        when(paymentProvider.registerPayment(any())).thenReturn(new Payment());
        when(serviceContractProvider.serviceContractOfId(any())).thenAnswer(invocationOnMock -> {
            ServiceContract result = new ServiceContract();
            result.setRate(new Rate(700f, Currency.EUR, Rate.Frequency.DAILY));
            return Optional.of(result);
        });
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(new Agency()));
        when(billProvider.registerBill(any())).thenReturn(new Bill());

        kafkaTemplate.send("container-test-topic", serviceContractStarted);
        TimeUnit.MILLISECONDS.sleep(5000);

        verify(paymentProvider, times(1)).registerPayment(any());
        verify(serviceContractProvider, times(2)).serviceContractOfId(any());
        verify(agencyProvider, times(1)).agencyOfId(any());
        verify(billProvider, times(1)).registerBill(any());

    }

    static class BrokerPropertiesOverrideInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    configurableApplicationContext,
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers());

            TestPropertySourceUtils.addPropertiesFilesToEnvironment(
                    configurableApplicationContext, "classpath:application.properties");
        }
    }
}