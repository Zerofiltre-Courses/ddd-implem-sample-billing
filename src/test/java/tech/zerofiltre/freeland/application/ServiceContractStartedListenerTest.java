package tech.zerofiltre.freeland.application;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.bill.*;
import tech.zerofiltre.freeland.domain.bill.model.*;
import tech.zerofiltre.freeland.domain.payment.*;
import tech.zerofiltre.freeland.domain.payment.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ServiceContractStartedListenerTest {

    private ServiceContractStartedListener serviceContractStartedListener;

    private ServiceContractStarted serviceContractStarted;

    @Mock
    private PaymentProvider paymentProvider;
    @Mock
    private ServiceContractProvider serviceContractProvider;
    @Mock
    private BillProvider billProvider;
    @Mock
    private AgencyProvider agencyProvider;

    @BeforeEach
    void init() {
        serviceContractStartedListener = new ServiceContractStartedListener(paymentProvider, serviceContractProvider, billProvider, agencyProvider);
        serviceContractStarted = new ServiceContractStarted(
                10L,
                null,
                null,
                null,
                null,
                null,
                null,
                700f,
                Rate.Frequency.DAILY,
                Rate.Currency.EUR,
                0f,
                null
        );
    }

    @Test
    @DisplayName("On contract started, schedule billing and payment")
    void consume() throws ScheduleClientBillingException, ScheduleFreelancerPaymentException {
        //ARRANGE
        when(paymentProvider.registerPayment(any())).thenReturn(Payment.builder().build());
        when(serviceContractProvider.serviceContractOfId(any())).thenAnswer(invocationOnMock -> Optional.of(
                ServiceContract.builder()
                        .rate(new Rate(700f, Rate.Currency.EUR, Rate.Frequency.DAILY))
                        .build()
        ));
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(new Agency(null, null, null)));
        when(billProvider.registerBill(any())).thenReturn(Bill.builder().build());

        //ACT
        serviceContractStartedListener.consume(serviceContractStarted);

        //ASSERT
        verify(paymentProvider, times(1)).registerPayment(any());
        verify(serviceContractProvider, times(2)).serviceContractOfId(any());
        verify(agencyProvider, times(1)).agencyOfId(any());
        verify(billProvider, times(1)).registerBill(any());
    }
}