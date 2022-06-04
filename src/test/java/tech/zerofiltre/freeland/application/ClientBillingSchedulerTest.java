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
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ClientBillingSchedulerTest {

    public static final String CLIENT_NAME = "client_name";
    public static final String CLIENT_SIREN = "client_siren";
    public static final String AGENCY_SIREN = "agency_siren";
    public static final String AGENCY_NAME = "agency_name";
    public static final String FREELANCER_SIREN = "freelancer_siren";
    public static final String FREELANCER_NAME = "freelancer_name";
    public static final String SERVICE_CONTRACT_ID = "4545785426";
    public static final String BILL_ID = "bill_id";
    public static final String AGENCY_IBAN = "agencyIban";
    public static final String BIC = "bic";
    public static final String ACCOUNT_OWNER = "accountOwner";
    public static final String BANK_NAME = "bankName";

    Client client;
    Agency agency;
    ServiceContract serviceContract;
    Amount amount = new Amount(12000f, Rate.Currency.EUR);
    Rate rate = new Rate(600.0f, Rate.Currency.EUR, Rate.Frequency.DAILY);
    ClientId clientId = new ClientId(CLIENT_SIREN, CLIENT_NAME);
    AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
    Address clientAddress = new Address("3", "Metz", "75012", "Rue du Cathédrale", "France");
    Address agencyAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");
    ServiceContractId serviceContractId = new ServiceContractId(10L);
    BankInfo bankInfo = new BankInfo(AGENCY_IBAN, BIC, ACCOUNT_OWNER, BANK_NAME);


    ClientBillingScheduler clientBillingScheduler;

    @Mock
    BillProvider billProvider;
    @Mock
    AgencyProvider agencyProvider;
    @Mock
    ServiceContractProvider serviceContractProvider;


    @BeforeEach
    void init() {
//    bill.setBillId(BILL_ID);
//    bill.setAmount(amount);
//    bill.setClientId(clientId);
//    bill.setServiceContractId(SERVICE_CONTRACT_ID);
//    bill.setAgencyId(agencyId);
//    bill.setIssuedDate(new Date());
//    bill.setExecutionPeriod(LocalDateTime.now());
        client = new Client(clientId, clientAddress);
        agency = new Agency(agencyId, bankInfo, agencyAddress);
        serviceContract = ServiceContract.builder()
                .serviceContractId(serviceContractId)
                .rate(rate)
                .serviceContractProvider(serviceContractProvider)
                .build();
        clientBillingScheduler = new ClientBillingScheduler(billProvider, agencyProvider, serviceContractProvider);
    }


    @Test
    void execute() throws ScheduleClientBillingException {
        //ARRANGE
        when(billProvider.registerBill(any())).thenAnswer(invocationOnMock -> {
            Bill result = invocationOnMock.getArgument(0);
            return Bill.builder().copy(result).billId(BILL_ID).build();
        });
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(agency));
        when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.of(serviceContract));

        //ACT
        Bill registeredBill = clientBillingScheduler.execute(clientId, agencyId, serviceContractId);

        //ASSERT
        assertThat(registeredBill.getBillId()).isNotNull();

        assertThat(registeredBill.getClientId()).isNotNull();
        ClientId registeredClientId = registeredBill.getClientId();
        assertThat(registeredClientId).isNotNull();
        assertThat(registeredClientId.getName()).isEqualTo(clientId.getName());
        assertThat(registeredClientId.getSiren()).isEqualTo(clientId.getSiren());

        assertThat(registeredBill.getAgencyId()).isNotNull();
        assertThat(registeredBill.getAgencyId().getSiren()).isEqualTo(agencyId.getSiren());
        assertThat(registeredBill.getAgencyId().getName()).isEqualTo(agencyId.getName());

        assertThat(registeredBill.getAmount()).isNotNull();
        assertThat(registeredBill.getAmount().getValue()).isEqualTo(amount.getValue());
        assertThat(registeredBill.getAmount().getCurrency()).isEqualTo(amount.getCurrency());

        assertThat(registeredBill.getIssuedDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(registeredBill.isPaid()).isFalse();

        assertThat(registeredBill.getExecutionPeriod().getMonthValue())
                .isEqualTo(LocalDateTime.now().getMonth().minus(1).getValue());


    }

    @Test
    @DisplayName("Schedule billing throws a ScheduleBillingException if the agency is missing")
    void execute_throwsScheduleBillingExceptionIsMissing() {
        //ARRANGE
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.empty());
        when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.of(serviceContract));

        //ACT & ASSERT
        assertThatExceptionOfType(ScheduleClientBillingException.class).isThrownBy(() ->
                clientBillingScheduler.execute(clientId, agencyId, serviceContractId)
        );

    }

    @Test
    @DisplayName("Schedule billing throws a ScheduleBillingException if the service contract is missing")
    void execute_throwsScheduleBillingExceptionIfServiceContractIsMissing() {
        //ARRANGE
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(agency));
        when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatExceptionOfType(ScheduleClientBillingException.class).isThrownBy(() ->
                clientBillingScheduler.execute(clientId, agencyId, serviceContractId)
        );

    }

}