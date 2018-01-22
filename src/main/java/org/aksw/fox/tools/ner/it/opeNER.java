package org.aksw.fox.tools.ner.it;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.aksw.fox.data.Entity;
import org.aksw.fox.data.EntityClassMap;
import org.aksw.fox.tools.ner.common.OpenNLPCommon;
import org.aksw.fox.utils.FoxCfg;
import org.aksw.fox.utils.FoxConst;
import org.aksw.fox.utils.FoxTextUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Varma on 08-01-2018
 * @project Fox_Italian
 * OpeNER is a language analysis toolchain helping (academic) researchers and companies make sense
 * out of “natural language analysis”.
 */
public class opeNER extends OpenNLPCommon {

    protected TokenNameFinderModel[] tokenNameFinderModels;

    // Italian Language Model
    static final String[] modelPath = {"data/opeNER-it/ixa-opener-it.bin"};

    public opeNER() {
        this(modelPath);
    }

    public opeNER(final String[] modelPath) {
        super(modelPath);
        // http://www.opener-project.eu/getting-started/#named-entity-resolution
        entityClasses.put("PER", EntityClassMap.P);
        entityClasses.put("LOC", EntityClassMap.L);
        entityClasses.put("ORG", EntityClassMap.O);
        entityClasses.put("GPE", EntityClassMap.N);

        tokenNameFinderModels = new TokenNameFinderModel[modelPath.length];
        final InputStream[] modelIn = new InputStream[modelPath.length];

        for (int i = 0; i < tokenNameFinderModels.length; i++) {
            try {
                modelIn[i] = new FileInputStream(modelPath[i]);
                if (modelIn[i] != null) {
                    tokenNameFinderModels[i] = new TokenNameFinderModel(modelIn[i]);
                }
            } catch (IOException e) {
               // LOG.error("\n", e);
            }
            finally {
                try {
                    if (modelIn[i] != null) {
                        modelIn[i].close();
                    }
                } catch (final IOException e) {
                    //LOG.error("\n", e);
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public List<Entity> retrieve(String input) {
        LOG.info("retrieve ...");

        final List<Entity> list = new ArrayList<>();
        final String[] sentences = FoxTextUtil.getSentences(input);

        for (int i = 0; i < tokenNameFinderModels.length; i++) {
            if (tokenNameFinderModels[i] != null) {
                final NameFinderME nameFinder = new NameFinderME(tokenNameFinderModels[i]);
                for (final String sentence : sentences) {
                    final String[] tokens = FoxTextUtil.getSentenceToken(sentence);

                    if ((tokens.length > 0) && tokens[tokens.length - 1].trim().isEmpty()) {
                        tokens[tokens.length - 1] = ".";
                    }

                    // if (logger.isDebugEnabled())
                    // for (String t : tokens)
                    // logger.debug("token: " + t);

                    final Span[] nameSpans = nameFinder.find(tokens);
                    final double[] probs = nameFinder.probs(nameSpans);
                    for (int ii = 0; ii < nameSpans.length; ii++) {
                        final Span span = nameSpans[ii];

                        String word = "";
                        for (int j = 0; j < (span.getEnd() - span.getStart()); j++) {
                            word += tokens[span.getStart() + j] + " ";
                        }
                        word = word.trim();

                        float p = Entity.DEFAULT_RELEVANCE;
                        if ((FoxCfg.get("openNLPDefaultRelevance") != null)
                                && !Boolean.valueOf(FoxCfg.get("openNLPDefaultRelevance"))) {
                            p = Double.valueOf(probs[ii]).floatValue();
                        }
                        final String cl = mapTypeToSupportedType(span.getType());
                        if (cl != EntityClassMap.getNullCategory()) {
                            list.add(getEntity(word, cl, p, getToolName()));
                        }
                    }
                }
                nameFinder.clearAdaptiveData();
            }
        }
        // TRACE
        if (LOG.isTraceEnabled()) {
            LOG.trace(list);
        } // TRACE
        return list;
    }

    public static void main(final String[] a) {
        LOG.info(new opeNER().retrieve(FoxConst.NER_IT_EXAMPLE_1));
    }
}
