package tech.zerofiltre.freeland.domain.payment.useCases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.zerofiltre.freeland.domain.Currency;
import tech.zerofiltre.freeland.domain.Rate;
import tech.zerofiltre.freeland.domain.Rate.Frequency;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import tech.zerofiltre.freeland.domain.payment.Payment;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.ServiceContractProvider;

@ExtendWith(SpringExtension.class)
class ScheduleFreelancerPaymentTest {

  public static final String AGENCY_SIREN = "agency_siren";
  public static final String AGENCY_NAME = "agency_name";
  public static final String CLIENT_NAME = "client_name";
  public static final String CLIENT_SIREN = "client_siren";
  public static final String FREELANCER_SIREN = "freelancer_siren";
  public static final String FREELANCER_NAME = "freelancer_name";
  public static final long CONTRACT_NUMBER = 10L;
  public static final float SERVICE_FEES_RATE = 0.05f;

  ServiceContract serviceContract = new ServiceContract();
  ServiceContractId serviceContractId = new ServiceContractId(CONTRACT_NUMBER);
  Rate serviceContractRate = new Rate(700f, Currency.EUR, Frequency.DAILY);
  FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);


  ScheduleFreelancerPayment scheduleFreelancerPayment;

  @Mock
  PaymentProvider paymentProvider;

  @Mock
  ServiceContractProvider serviceContractProvider;


  @BeforeEach
  void init() {
    scheduleFreelancerPayment = new ScheduleFreelancerPayment(paymentProvider, serviceContractProvider);
    serviceContract.setServiceContractId(serviceContractId);
    serviceContract.setRate(serviceContractRate);


  }

  @Test
  @DisplayName("Schedule freelancer payment is executes properly")
  void execute() throws ScheduleFreelancerPaymentException {

    //ARRANGE
    when(paymentProvider.registerPayment(any())).thenAnswer(invocationOnMock -> {
      Payment payment = invocationOnMock.getArgument(0);
      payment.setPaymentId(1855L);
      return payment;
    });

    when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.of(new ServiceContract()));

    //ACT
    Payment payment = scheduleFreelancerPayment
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
    assertThat(payment.getAmount().getCurrency()).isEqualTo(Currency.EUR);
    assertThat(payment.getAmount().getValue()).isEqualTo(7049);

    assertThat(payment.getExecutionDate()).isBeforeOrEqualTo(new Date());
    assertThat(payment.getExecutionPeriod().getMonthValue())
        .isEqualTo(LocalDateTime.now().getMonth().minus(1).getValue());


  }

  @Test
  @DisplayName("ScheduleBilling throws ScheduleBillingException if the Service Contract is missing")
  void execute_mustThrowScheduleBillingExceptionIfTheServiceContractIsMissing() {
    //ARRANGE
    when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.empty());

    //ACT & ASSERT
    assertThatExceptionOfType(ScheduleFreelancerPaymentException.class).isThrownBy(() -> scheduleFreelancerPayment
        .execute(serviceContractId, freelancerId, serviceContractRate, SERVICE_FEES_RATE));

  }
}