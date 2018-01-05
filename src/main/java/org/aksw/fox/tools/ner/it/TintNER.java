package org.aksw.fox.tools.ner.it;

import org.aksw.fox.data.Entity;
import org.aksw.fox.data.EntityClassMap;
import org.aksw.fox.tools.ner.AbstractNER;
import org.aksw.fox.utils.FoxConst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Varma on 05-01-2018
 * @project fox
 */

public class TintNER extends AbstractNER {

    // Entity types check and mapping .. done
    public static final Map<String, String> ENTITY_MAP = new HashMap<>();

    static {
        ENTITY_MAP.put("PER", EntityClassMap.P);
        ENTITY_MAP.put("LOC", EntityClassMap.L);
        ENTITY_MAP.put("ORG", EntityClassMap.O);
    }

    @Override
    public List<Entity> retrieve(String input) {

        LOG.info("retrieve ...");

        // TODO Implement retrieve method
        return null;
    }

    public static void main(final String[] a) {
        LOG.info(new TintNER().retrieve(FoxConst.NER_IT_EXAMPLE_1));
    }
}
