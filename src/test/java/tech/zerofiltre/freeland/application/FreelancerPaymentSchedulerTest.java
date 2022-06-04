package tech.zerofiltre.freeland.application;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.payment.*;
import tech.zerofiltre.freeland.domain.payment.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class FreelancerPaymentSchedulerTest {

    public static final String AGENCY_SIREN = "agency_siren";
    public static final String AGENCY_NAME = "agency_name";
    public static final String CLIENT_NAME = "client_name";
    public static final String CLIENT_SIREN = "client_siren";
    public static final String FREELANCER_SIREN = "freelancer_siren";
    public static final String FREELANCER_NAME = "freelancer_name";
    public static final long CONTRACT_NUMBER = 10L;
    public static final float SERVICE_FEES_RATE = 0.05f;

    ServiceContract serviceContract;
    ServiceContractId serviceContractId = new ServiceContractId(CONTRACT_NUMBER);
    Rate serviceContractRate = new Rate(700f, Rate.Currency.EUR, Rate.Frequency.DAILY);
    FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);


    FreelancerPaymentScheduler freelancerPaymentScheduler;

    @Mock
    PaymentProvider paymentProvider;

    @Mock
    ServiceContractProvider serviceContractProvider;


    @BeforeEach
    void init() {
        freelancerPaymentScheduler = new FreelancerPaymentScheduler(paymentProvider, serviceContractProvider);
        serviceContract = ServiceContract.builder().serviceContractId(serviceContractId)
                .rate(serviceContractRate)
                .build();
    }

    @Test
    @DisplayName("Schedule freelancer payment executes properly")
    void execute() throws ScheduleFreelancerPaymentException {

        //ARRANGE
        when(paymentProvider.registerPayment(any())).thenAnswer(invocationOnMock -> {
            Payment payment = invocationOnMock.getArgument(0);
            return Payment.builder().copy(payment).paymentId(1855L).build();
        });

        when(serviceContractProvider.serviceContractOfId(any()))
                .thenReturn(Optional.of(ServiceContract.builder().build()));

        //ACT
        Payment payment = freelancerPaymentScheduler
                .execute(serviceContractId, freelancerId, serviceContractRate, SERVICE_FEES_RATE);

        //ASSERT
        assertThat(payment).isNotNull();
        assertThat(payment.getPaymentId()).isNotNull();

        assertThat(payment.getServiceContractId()).isNotNull();
        assertThat(payment.getServiceContractId().getContractNumber()).isEqualTo(CONTRACT_NUMBER);

        assertThat(payment.getFreelancerId()).isNotNull();
        assertThat(payment.getFreelancerId().getName()).isEqualTo(freelancerId.getName());
        assertThat(payment.getFreelancerId().getSiren()).isEqualTo(freelancerId.getSiren());

        assertThat(payment.getAmount()).isNotNull();
        assertThat(payment.getAmount().getCurrency()).isEqualTo(Rate.Currency.EUR);
        assertThat(payment.getAmount().getValue()).isEqualTo(7049);

        assertThat(payment.getExecutionDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(payment.getExecutionPeriod().getMonthValue())
                .isEqualTo(LocalDateTime.now().getMonth().minus(1).getValue());


    }

    @Test
    @DisplayName("ScheduleBilling throws ScheduleBillingException if the Service Contract is missing")
    void execute_mustThrowScheduleBillingExceptionIfTheServiceContractIsMissing() {
        //ARRANGE
        when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatExceptionOfType(ScheduleFreelancerPaymentException.class).isThrownBy(() -> freelancerPaymentScheduler
                .execute(serviceContractId, freelancerId, serviceContractRate, SERVICE_FEES_RATE));

    }
}