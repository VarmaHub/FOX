package org.aksw.fox.tools.linking.it;

import org.aksw.fox.tools.linking.common.Agdistis;
import org.aksw.fox.utils.CfgManager;

/**
 * This class uses the Agdistis web service.
 */

public class AgdistisIT extends Agdistis {
    public AgdistisIT() {
        super(CfgManager.getCfg(AgdistisIT.class));
    }
}
