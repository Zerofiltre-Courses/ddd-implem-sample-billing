package tech.zerofiltre.freelandbilling.domain.client.model;


import tech.zerofiltre.freelandbilling.domain.CompanyId;

public class ClientId extends CompanyId {

  public ClientId(String siren, String name) {
    super(siren, name);
  }
}
