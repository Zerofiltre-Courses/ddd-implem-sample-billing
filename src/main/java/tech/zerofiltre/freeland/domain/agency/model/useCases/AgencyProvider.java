package tech.zerofiltre.freeland.domain.agency.model.useCases;

import java.util.Optional;
import tech.zerofiltre.freeland.domain.agency.model.Agency;
import tech.zerofiltre.freeland.domain.agency.model.AgencyId;

public interface AgencyProvider {

  Optional<Agency> agencyOfId(AgencyId agencyId);

}
