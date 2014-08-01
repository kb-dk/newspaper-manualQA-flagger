package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.AltoCache;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.HistogramCache;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.utils.Excluder;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.utils.InjectingExcluder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlaggerFactory implements EventHandlerFactory {
    private static final String UNMATCHED = "UNMATCHED";
    private static final String WORKSHIFT_ISO_TARGET = "WORKSHIFT-ISO-TARGET";
    private final ResultCollector resultCollector;
    private final Batch batch;
    private FlaggingCollector flaggingCollector;
    private Properties properties;

    public FlaggerFactory(ResultCollector resultCollector, Batch batch, FlaggingCollector flaggingCollector,
                          Properties properties) {
        this.resultCollector = resultCollector;
        this.batch = batch;
        this.flaggingCollector = flaggingCollector;
        this.properties = properties;
    }

    @Override
    public List<TreeEventHandler> createEventHandlers() {
        ArrayList<TreeEventHandler> treeEventHandlers = new ArrayList<>();
        final AltoCache altoCache = new AltoCache();
        final HistogramCache histogramCache = new HistogramCache();
        // Booleans below are deliberately set to false only if specifically set like that in the config file
        boolean mixHandlerOn = !properties.getProperty(ConfigConstants.MIX_HANDLER_ON, "true").equals("false");
        boolean altoWordAccuracyCheckerOn = !properties.getProperty(
                ConfigConstants.ALTO_WORD_ACCURACY_CHECKER_ON, "true").equals("false");
        boolean darknessHistogramCheckerOn = !properties.getProperty(
                ConfigConstants.DARKNESS_HISTOGRAM_CHECKER_ON, "true").equals("false");
        treeEventHandlers.add(new InjectingExcluder(UNMATCHED, 
                new InjectingExcluder(WORKSHIFT_ISO_TARGET,  new HistogramAverageHandler(resultCollector, histogramCache,batch))));
        
        treeEventHandlers.add(
                new Excluder(
                        UNMATCHED,
                        new MissingColorsHistogramChecker(
                                resultCollector,
                                flaggingCollector,
                                histogramCache,
                                properties)));


        treeEventHandlers.add(
                new Excluder(
                        UNMATCHED,
                        new ChoppyCurveHistogramChecker(
                                resultCollector,
                                flaggingCollector,
                                histogramCache,
                                properties)));

        treeEventHandlers.add(
                new Excluder(
                        UNMATCHED,
                        new EditionModsHandler(
                                resultCollector,
                                flaggingCollector,
                                batch,
                                properties)));

        treeEventHandlers.add(new Excluder(UNMATCHED, new FilmHandler(resultCollector, flaggingCollector)));



        if (mixHandlerOn) {
            treeEventHandlers.add(
                    new Excluder(UNMATCHED,
                            new MixHandler(
                                    resultCollector,
                                    properties,
                                    flaggingCollector)));
        }

        if (altoWordAccuracyCheckerOn) {
            treeEventHandlers.add(
                    new Excluder(UNMATCHED,
                            new AltoWordAccuracyChecker(
                                    resultCollector,
                                    flaggingCollector,
                                    altoCache,
                                    properties)));
        }
        if (darknessHistogramCheckerOn) {
            treeEventHandlers.add(
                    new Excluder(UNMATCHED,
                            new DarknessHistogramChecker(
                                    resultCollector,
                                    flaggingCollector,
                                    histogramCache,
                                    batch,
                                    altoCache,
                                    properties)));
        }


        treeEventHandlers.add(
                new Excluder(
                        UNMATCHED,
                        new EndSpikeHistogramChecker(
                                resultCollector,
                                flaggingCollector, histogramCache,
                                properties)));
        return treeEventHandlers;
    }
}
