package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        treeEventHandlers.add(new UnmatchedExcluder(new MissingColorsHistogramChecker(resultCollector, flaggingCollector,
                properties)));
        treeEventHandlers.add(new UnmatchedExcluder(new ChoppyCurveHistogramChecker(resultCollector, flaggingCollector,
                properties)));
        treeEventHandlers.add(new UnmatchedExcluder(new EditionModsHandler(resultCollector, flaggingCollector, batch, properties
        )));
        treeEventHandlers.add(new UnmatchedExcluder(new FilmHandler(resultCollector, flaggingCollector)));
        treeEventHandlers.add(new UnmatchedExcluder(new MixHandler(resultCollector, properties, flaggingCollector)));
        final AltoCache altoCache = new AltoCache();
        treeEventHandlers.add(new UnmatchedExcluder(new AltoWordAccuracyChecker(resultCollector, flaggingCollector,
                altoCache,properties)));
        treeEventHandlers.add(new UnmatchedExcluder(new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,altoCache,
                properties)));
        treeEventHandlers.add(new UnmatchedExcluder(new EndSpikeHistogramChecker(resultCollector, flaggingCollector, properties)));
        return treeEventHandlers;
    }
}
