package tech.zerofiltre.freelandbilling.domain.bill.useCases;

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
import tech.zerofiltre.freelandbilling.domain.Address;
import tech.zerofiltre.freelandbilling.domain.Amount;
import tech.zerofiltre.freelandbilling.domain.BankInfo;
import tech.zerofiltre.freelandbilling.domain.Currency;
import tech.zerofiltre.freelandbilling.domain.Rate;
import tech.zerofiltre.freelandbilling.domain.Rate.Frequency;
import tech.zerofiltre.freelandbilling.domain.agency.model.Agency;
import tech.zerofiltre.freelandbilling.domain.agency.model.AgencyId;
import tech.zerofiltre.freelandbilling.domain.agency.model.useCases.AgencyProvider;
import tech.zerofiltre.freelandbilling.domain.bill.model.Bill;
import tech.zerofiltre.freelandbilling.domain.client.model.Client;
import tech.zerofiltre.freelandbilling.domain.client.model.ClientId;
import tech.zerofiltre.freelandbilling.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freelandbilling.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freelandbilling.domain.serviceContract.useCases.ServiceContractProvider;

@ExtendWith(SpringExtension.class)
class ScheduleClientBillingTest {

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

  Client client = new Client();
  Agency agency = new Agency();
  ServiceContract serviceContract = new ServiceContract();
  Amount amount = new Amount(12000f, Currency.EUR);
  Rate rate = new Rate(600.0f, Currency.EUR, Frequency.DAILY);
  ClientId clientId = new ClientId(CLIENT_SIREN, CLIENT_NAME);
  AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
  Address clientAddress = new Address("3", "Metz", "75012", "Rue du Cathédrale", "France");
  Address agencyAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");
  ServiceContractId serviceContractId = new ServiceContractId(10L);
  BankInfo bankInfo = new BankInfo(AGENCY_IBAN, BIC, ACCOUNT_OWNER, BANK_NAME);


  ScheduleClientBilling scheduleClientBilling;

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
    client.setClientId(clientId);
    client.setAddress(clientAddress);
    agency.setAgencyId(agencyId);
    agency.setAddress(agencyAddress);
    agency.setBankInfo(bankInfo);
    serviceContract.setServiceContractId(serviceContractId);
    serviceContract.setRate(rate);
    scheduleClientBilling = new ScheduleClientBilling(billProvider, agencyProvider, serviceContractProvider);
  }


  @Test
  void execute() throws ScheduleClientBillingException {
    //ARRANGE
    when(billProvider.registerBill(any())).thenAnswer(invocationOnMock -> {
      Bill result = invocationOnMock.getArgument(0);
      result.setBillId(BILL_ID);
      return result;
    });
    when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(agency));
    when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.of(serviceContract));

    //ACT
    Bill registeredBill = scheduleClientBilling.execute(client, agencyId, serviceContractId);

    //ASSERT
    assertThat(registeredBill.getBillId()).isNotNull();

    assertThat(registeredBill.getClient()).isNotNull();
    ClientId registeredClientId = registeredBill.getClient().getClientId();
    assertThat(registeredClientId).isNotNull();
    assertThat(registeredClientId.getName()).isEqualTo(clientId.getName());
    assertThat(registeredClientId.getSiren()).isEqualTo(clientId.getSiren());

    assertThat(registeredBill.getAgencyId()).isNotNull();
    assertThat(registeredBill.getAgencyId().getSiren()).isEqualTo(agencyId.getSiren());
    assertThat(registeredBill.getAgencyId().getName()).isEqualTo(agencyId.getName());

    assertThat(registeredBill.getAmount()).isNotNull();
    assertThat(registeredBill.getAmount().getValue()).isEqualTo(amount.getValue());
    assertThat(registeredBill.getAmount().getCurrency()).isEqualTo(amount.getCurrency());

    assertThat(registeredBill.getIssuedDate()).isBeforeOrEqualTo(new Date());
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
        scheduleClientBilling.execute(client, agencyId, serviceContractId)
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
        scheduleClientBilling.execute(client, agencyId, serviceContractId)
    );

  }

}