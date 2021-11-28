package tech.zerofiltre.freeland.application;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ListenToServiceContractStartedTest {

    private ListenToServiceContractStarted listenToServiceContractStarted;

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
        listenToServiceContractStarted = new ListenToServiceContractStarted(paymentProvider, serviceContractProvider, billProvider, agencyProvider);
        serviceContractStarted = new ServiceContractStarted();
        serviceContractStarted = new ServiceContractStarted();
        serviceContractStarted.setServiceContractNumber(10L);
        serviceContractStarted.setRateCurrency(Currency.EUR);
        serviceContractStarted.setRateFrequency(Rate.Frequency.DAILY);
        serviceContractStarted.setRateValue(700f);
    }

    @Test
    @DisplayName("On contract started, schedule billing and payment")
    void consume() throws ScheduleClientBillingException, ScheduleFreelancerPaymentException {
        //ARRANGE
        when(paymentProvider.registerPayment(any())).thenReturn(new Payment());
        when(serviceContractProvider.serviceContractOfId(any())).thenAnswer(invocationOnMock -> {
            ServiceContract result = new ServiceContract();
            result.setRate(new Rate(700f, Currency.EUR, Rate.Frequency.DAILY));
            return Optional.of(result);
        });
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(new Agency()));
        when(billProvider.registerBill(any())).thenReturn(new Bill());

        //ACT
        listenToServiceContractStarted.consume(serviceContractStarted);

        //ASSERT
        verify(paymentProvider, times(1)).registerPayment(any());
        verify(serviceContractProvider, times(2)).serviceContractOfId(any());
        verify(agencyProvider, times(1)).agencyOfId(any());
        verify(billProvider, times(1)).registerBill(any());
    }
}