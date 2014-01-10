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
        treeEventHandlers.add(new IndividualHistogramHandler(resultCollector, flaggingCollector));

        return treeEventHandlers;
    }
}
