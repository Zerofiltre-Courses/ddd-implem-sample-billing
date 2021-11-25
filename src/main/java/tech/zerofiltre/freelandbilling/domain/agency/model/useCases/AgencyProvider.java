package tech.zerofiltre.freelandbilling.domain.agency.model.useCases;

import java.util.Optional;
import tech.zerofiltre.freelandbilling.domain.agency.model.Agency;
import tech.zerofiltre.freelandbilling.domain.agency.model.AgencyId;

public interface AgencyProvider {

  Optional<Agency> agencyOfId(AgencyId agencyId);

}
