package tech.zerofiltre.freeland.domain.agency;

import tech.zerofiltre.freeland.domain.agency.model.*;

import java.util.*;

public interface AgencyProvider {

    Optional<Agency> agencyOfId(AgencyId agencyId);

}
