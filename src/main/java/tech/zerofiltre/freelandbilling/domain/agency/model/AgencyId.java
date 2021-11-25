package tech.zerofiltre.freelandbilling.domain.agency.model;


import tech.zerofiltre.freelandbilling.domain.CompanyId;

public class AgencyId extends CompanyId {

  public AgencyId(String siren, String name) {
    super(siren, name);
  }
}
