package tech.zerofiltre.freelandbilling.domain.freelancer.model;

import tech.zerofiltre.freelandbilling.domain.CompanyId;

public class FreelancerId extends CompanyId {

  public FreelancerId(String siren, String name) {
    super(siren, name);
  }
}
