package org.aksw.fox.nertools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import org.aksw.fox.data.Entity;
import org.aksw.fox.data.EntityClassMap;
import org.aksw.fox.utils.FoxCfg;
import org.aksw.fox.utils.FoxTextUtil;
import org.apache.log4j.PropertyConfigurator;

public class NEROpenNLP extends AbstractNER {

    private final String[] modelPath = { "data/openNLP/en-ner-person.bin", "data/openNLP/en-ner-location.bin", "data/openNLP/en-ner-organization.bin" };

    private final TokenNameFinderModel[] tokenNameFinderModels = new TokenNameFinderModel[modelPath.length];

    /**
     * 
     */
    public NEROpenNLP() {
        InputStream[] modelIn = new InputStream[3];
        for (int i = 0; i < tokenNameFinderModels.length; i++) {
            try {

                modelIn[i] = new FileInputStream(modelPath[i]);
                if (modelIn[i] != null)
                    tokenNameFinderModels[i] = new TokenNameFinderModel(modelIn[i]);

            } catch (IOException e) {
                logger.error("\n", e);
            } finally {

                try {
                    if (modelIn[i] != null)
                        modelIn[i].close();
                } catch (IOException e) {
                    logger.error("\n", e);
                }
            }
        }
    }

    // TODO: do parallel for each model
    @Override
    public Set<Entity> retrieve(String input) {
        logger.info("retrieve ...");

        Set<Entity> set = new HashSet<>();
        String[] sentences = FoxTextUtil.getSentences(input);

        for (int i = 0; i < tokenNameFinderModels.length; i++) {
            if (tokenNameFinderModels[i] != null) {
                NameFinderME nameFinder = new NameFinderME(tokenNameFinderModels[i]);
                for (String sentence : sentences) {
                    String[] tokens = FoxTextUtil.getSentenceToken(sentence);
                    if (tokens[tokens.length - 1].trim().isEmpty())
                        tokens[tokens.length - 1] = ".";

                    // if (logger.isDebugEnabled())
                    // for (String t : tokens)
                    // logger.debug("token: " + t);

                    Span[] nameSpans = nameFinder.find(tokens);
                    double[] probs = nameFinder.probs(nameSpans);
                    for (int ii = 0; ii < nameSpans.length; ii++) {
                        Span span = nameSpans[ii];

                        String word = "";
                        for (int j = 0; j < span.getEnd() - span.getStart(); j++)
                            word += tokens[span.getStart() + j] + " ";
                        word = word.trim();

                        float p = Entity.DEFAULT_RELEVANCE;
                        if (FoxCfg.get("openNLPDefaultRelevance") != null && !Boolean.valueOf(FoxCfg.get("openNLPDefaultRelevance")))
                            p = Double.valueOf(probs[ii]).floatValue();
                        String cl = EntityClassMap.openNLP(span.getType());
                        if (cl != EntityClassMap.getNullCategory())
                            set.add(getEntiy(word, cl, p, getToolName()));
                    }
                }
                nameFinder.clearAdaptiveData();
            }
        }
        return post(set);
    }

    public static void main(String[] a) {
        PropertyConfigurator.configure("log4j.properties");
        for (Entity e : new NEROpenNLP().retrieve("Stanford University is located in California. It is a great university."))
            NEROpenNLP.logger.info(e);
    }
}
