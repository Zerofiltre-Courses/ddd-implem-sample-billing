package tech.zerofiltre.freelandbilling.domain.client.model;


import tech.zerofiltre.freelandbilling.domain.Address;

public class Client {

  private ClientId clientId;
  private Address address;

  public ClientId getClientId() {
    return clientId;
  }

  public void setClientId(ClientId clientId) {
    this.clientId = clientId;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
