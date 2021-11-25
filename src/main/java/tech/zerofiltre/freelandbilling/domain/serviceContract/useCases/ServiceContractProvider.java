package tech.zerofiltre.freelandbilling.domain.serviceContract.useCases;

import java.util.Optional;
import tech.zerofiltre.freelandbilling.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freelandbilling.domain.serviceContract.model.ServiceContractId;

public interface ServiceContractProvider {

  Optional<ServiceContract> serviceContractOfId(ServiceContractId serviceContractId);

}
