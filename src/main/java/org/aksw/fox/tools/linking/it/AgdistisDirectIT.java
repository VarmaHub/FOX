package org.aksw.fox.tools.linking.it;

import org.aksw.agdistis.algorithm.NEDAlgo_HITS;
import org.aksw.fox.tools.linking.common.Agdistis;

import java.io.IOException;

/**
 * This class uses the Agdistis lib.
 */

public class AgdistisDirectIT extends Agdistis {

    final String file = "agdistisIT.properties";
    NEDAlgo_HITS agdistis = null;

    public AgdistisDirectIT() {
        try {
            agdistis = new NEDAlgo_HITS(file);
        } catch (final IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    protected String send(final String text) {
        return standardAG(text, agdistis);
    }
}
