package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.AbstractRunnableComponent;
import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.util.xml.DOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ManualQAFlaggerRunnableComponent extends AbstractRunnableComponent {

    private Logger log = LoggerFactory.getLogger(getClass());


    protected ManualQAFlaggerRunnableComponent(Properties properties) {
        super(properties);
    }

    @Override
    public String getEventID() {
        return "Manual_QA_Flagged";
    }

    @Override
    public void doWorkOnBatch(Batch batch, ResultCollector resultCollector) throws Exception {
        log.info("Starting validation of '{}'", batch.getFullID());
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch);

        InputStream batchXmlStructureStream = retrieveBatchStructure(batch);
        if (batchXmlStructureStream == null) {
            throw new RuntimeException("Failed to resolve batch manifest from data collector");
        }

        Document batchXmlStructure = DOM.streamToDOM(batchXmlStructureStream);


        FlaggerFactory factory = new FlaggerFactory(resultCollector, batch, batchXmlStructure, flaggingCollector);
        EventHandlerFactory eventHandlerFactory = factory;
        List<TreeEventHandler> eventHandlers = eventHandlerFactory.createEventHandlers();
        EventRunner eventRunner = new EventRunner(createIterator(batch));
        eventRunner.runEvents(eventHandlers, resultCollector);

        saveFlaggingCollector(flaggingCollector);
        log.info("Done validating '{}', success: {}", batch.getFullID(), resultCollector.isSuccess());

    }

    private void saveFlaggingCollector(FlaggingCollector flaggingCollector) {
        //TODO
    }

}
