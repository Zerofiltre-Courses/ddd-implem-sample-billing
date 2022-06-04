package tech.zerofiltre.freeland.domain.client;

import java.util.Optional;
import tech.zerofiltre.freeland.domain.client.model.Client;
import tech.zerofiltre.freeland.domain.client.model.ClientId;

public interface ClientProvider {

  Optional<Client> clientOfId(ClientId clientId);

  Client registerClient(Client client);

}
