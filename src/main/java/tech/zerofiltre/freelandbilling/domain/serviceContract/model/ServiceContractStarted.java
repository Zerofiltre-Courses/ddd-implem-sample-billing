package tech.zerofiltre.freelandbilling.domain.serviceContract.model;

import java.util.Date;
import tech.zerofiltre.freelandbilling.domain.Rate;
import tech.zerofiltre.freelandbilling.domain.agency.model.AgencyId;
import tech.zerofiltre.freelandbilling.domain.client.model.ClientId;
import tech.zerofiltre.freelandbilling.domain.freelancer.model.FreelancerId;


public class ServiceContractStarted extends ServiceContractEvent {

  private final ServiceContractId serviceContractId;
  private final ClientId clientId;
  private final FreelancerId freelancerId;
  private final AgencyId agencyId;
  private final Rate rate;
  private final float serviceFeesRate;
  private final Date startDate;


  public ServiceContractStarted(
      ServiceContractId serviceContractId, ClientId clientId,
      FreelancerId freelancerId, AgencyId agencyId, Rate rate, float serviceFeesRate, Date startDate) {
    this.serviceContractId = serviceContractId;
    this.clientId = clientId;
    this.freelancerId = freelancerId;
    this.agencyId = agencyId;
    this.rate = rate;
    this.serviceFeesRate = serviceFeesRate;
    this.startDate = startDate;
  }

  public ClientId getClientId() {
    return clientId;
  }

  public FreelancerId getFreelancerId() {
    return freelancerId;
  }

  public AgencyId getAgencyId() {
    return agencyId;
  }

  public Rate getRate() {
    return rate;
  }

  public float getServiceFeesRate() {
    return serviceFeesRate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public ServiceContractId getServiceContractId() {
    return serviceContractId;
  }
}
