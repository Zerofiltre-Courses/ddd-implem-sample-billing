package tech.zerofiltre.freeland.domain.client.model;


import tech.zerofiltre.freeland.domain.Address;

public class Client {

  private ClientId clientId;
  private Address address;

  public Client(ClientId clientId, Address address) {
    this.clientId = clientId;
    this.address = address;
  }

  public ClientId getClientId() {
    return clientId;
  }


  public Address getAddress() {
    return address;
  }

}
