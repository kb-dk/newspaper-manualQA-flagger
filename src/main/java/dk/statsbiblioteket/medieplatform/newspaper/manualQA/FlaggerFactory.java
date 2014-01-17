package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class FlaggerFactory implements EventHandlerFactory {
    // How much a value is allowed to deviate from the average of its two neighbours (in pct, >0) before
    // it is considered an irregularity (only happens if both neighbours are either higher or lower)
    private static final double CHOPPY_CHECK_THRESHOLD = 0.5;
    // The maximum number of peaks/valleys allowed before flagged as an error
    private static final int CHOPPY_CHECK_MAX_IRREGULARITIES = 4;

    private final ResultCollector resultCollector;
    private final Batch batch;
    private final Document batchXmlStructure;
    private FlaggingCollector flaggingCollector;

    public FlaggerFactory(ResultCollector resultCollector, Batch batch, Document batchXmlStructure,
                          FlaggingCollector flaggingCollector) {

        this.resultCollector = resultCollector;
        this.batch = batch;
        this.batchXmlStructure = batchXmlStructure;
        this.flaggingCollector = flaggingCollector;
    }

    @Override
    public List<TreeEventHandler> createEventHandlers() {
        ArrayList<TreeEventHandler> treeEventHandlers = new ArrayList<>();
        treeEventHandlers.add(new MissingColorsHistogramChecker(resultCollector, flaggingCollector, 0, 10));
        treeEventHandlers.add(new ChoppyCurveHistogramChecker(resultCollector, flaggingCollector,
                CHOPPY_CHECK_THRESHOLD, CHOPPY_CHECK_MAX_IRREGULARITIES));
        treeEventHandlers.add(new EditionModsHandler(resultCollector, flaggingCollector, batch));
        treeEventHandlers.add(new FilmHandler(resultCollector, flaggingCollector));
        return treeEventHandlers;
    }
}
