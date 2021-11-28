package tech.zerofiltre.freeland.domain.serviceContract.useCases;

import java.util.Optional;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;

public interface ServiceContractProvider {

  Optional<ServiceContract> serviceContractOfId(ServiceContractId serviceContractId);

}
