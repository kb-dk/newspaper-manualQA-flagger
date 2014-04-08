package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

public class FlaggerFactory implements EventHandlerFactory {
    private final ResultCollector resultCollector;
    private final Batch batch;
    private FlaggingCollector flaggingCollector;
    private Properties properties;

    public FlaggerFactory(ResultCollector resultCollector, Batch batch,
                          FlaggingCollector flaggingCollector, Properties properties) {
        this.resultCollector = resultCollector;
        this.batch = batch;
        this.flaggingCollector = flaggingCollector;
        this.properties = properties;
    }

    @Override
    public List<TreeEventHandler> createEventHandlers() {
        ArrayList<TreeEventHandler> treeEventHandlers = new ArrayList<>();
        final AltoCache altoCache = new AltoCache();
        // Booleans below are deliberately set to false only if specifically set like that in the config file
        boolean mixHandlerOn = properties.getProperty(ConfigConstants.MIX_HANDLER_ON) != "false";
        boolean altoWordAccuracyCheckerOn = properties.getProperty(ConfigConstants.ALTO_WORD_ACCURACY_CHECKER_ON) != "false";
        boolean darknessHistogramCheckerOn = Boolean.parseBoolean(properties.getProperty(
                ConfigConstants.DARKNESS_HISTOGRAM_CHECKER_ON));

        treeEventHandlers.add(new UnmatchedExcluder(new MissingColorsHistogramChecker(resultCollector, flaggingCollector,
                properties)));
        treeEventHandlers.add(new UnmatchedExcluder(new ChoppyCurveHistogramChecker(resultCollector, flaggingCollector,
                properties)));
        treeEventHandlers.add(new UnmatchedExcluder(new EditionModsHandler(resultCollector, flaggingCollector, batch, properties
        )));
        treeEventHandlers.add(new UnmatchedExcluder(new FilmHandler(resultCollector, flaggingCollector)));

        if (mixHandlerOn) {
            treeEventHandlers.add(new UnmatchedExcluder(new MixHandler(resultCollector, properties, flaggingCollector)));
        }

        if (altoWordAccuracyCheckerOn) {
            treeEventHandlers.add(new UnmatchedExcluder(new AltoWordAccuracyChecker(resultCollector, flaggingCollector,
                    altoCache, properties)));
        }

        if (darknessHistogramCheckerOn) {
            treeEventHandlers.add(new UnmatchedExcluder(new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                    altoCache, properties)));
        }

        treeEventHandlers.add(new UnmatchedExcluder(new EndSpikeHistogramChecker(resultCollector, flaggingCollector,
                properties)));
        return treeEventHandlers;
    }
}
