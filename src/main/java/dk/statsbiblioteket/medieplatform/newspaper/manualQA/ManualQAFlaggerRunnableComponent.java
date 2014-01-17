package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.doms.central.connectors.BackendInvalidCredsException;
import dk.statsbiblioteket.doms.central.connectors.BackendInvalidResourceException;
import dk.statsbiblioteket.doms.central.connectors.BackendMethodFailedException;
import dk.statsbiblioteket.doms.central.connectors.EnhancedFedora;
import dk.statsbiblioteket.doms.central.connectors.EnhancedFedoraImpl;
import dk.statsbiblioteket.doms.central.connectors.fedora.pidGenerator.PIDGeneratorException;
import dk.statsbiblioteket.doms.webservices.authentication.Credentials;
import dk.statsbiblioteket.medieplatform.autonomous.AbstractRunnableComponent;
import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ConfigConstants;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ManualQAFlaggerRunnableComponent extends AbstractRunnableComponent {

    private Logger log = LoggerFactory.getLogger(getClass());
    private Properties properties;

    protected ManualQAFlaggerRunnableComponent(Properties properties) {
        super(properties);
        this.properties = properties;
    }

    @Override
    public String getEventID() {
        return "Manual_QA_Flagged";
    }

    @Override
    public void doWorkOnBatch(Batch batch, ResultCollector resultCollector) throws Exception {
        log.info("Starting validation of '{}'", batch.getFullID());

        InputStream batchXmlStructureStream = retrieveBatchStructure(batch);
        if (batchXmlStructureStream == null) {
            throw new RuntimeException("Failed to resolve batch manifest from data collector");
        }

        Document batchXmlStructure = DOM.streamToDOM(batchXmlStructureStream);


        FlaggingCollector flaggingCollector = new FlaggingCollector(
                batch, batchXmlStructure, getComponentVersion());

        EventHandlerFactory eventHandlerFactory = new FlaggerFactory(
                resultCollector, batch, batchXmlStructure, flaggingCollector, properties);
        List<TreeEventHandler> eventHandlers = eventHandlerFactory.createEventHandlers();
        EventRunner eventRunner = new EventRunner(createIterator(batch));
        eventRunner.runEvents(eventHandlers, resultCollector);
        saveFlaggingCollector(flaggingCollector);
        log.info("Done validating '{}', success: {}", batch.getFullID(), resultCollector.isSuccess());

    }

    private void saveFlaggingCollector(FlaggingCollector flaggingCollector) throws
                                                                            IOException,
                                                                            BackendInvalidCredsException,
                                                                            BackendMethodFailedException,
                                                                            BackendInvalidResourceException {
        EnhancedFedora fedora = createFedoraClient();
        String pid;

        String path = "path:" + flaggingCollector.getBatch().getFullID();
        List<String> hits = fedora.findObjectFromDCIdentifier(path);
        if (hits.isEmpty()) {
            throw new BackendInvalidResourceException("Failed to look up doms object for DC identifier '" + path + "'");
        } else {
            if (hits.size() > 1) {
                log.warn("Found multipe pids for dc identifier '" + path + "', using the first one '" + hits.get(0) + "'");
            }
            pid = hits.get(0);
        }

        fedora.modifyDatastreamByValue(
                pid, "MANUAL_QA_FLAGS", flaggingCollector.toReport(), null, "added data for MANUAL QA");
    }

    /**
     * Get the fedora client
     *
     * @return the fedora client
     * @throws java.io.IOException
     */
    protected EnhancedFedora createFedoraClient() throws IOException {
        try {
            String username = getProperties().getProperty(ConfigConstants.DOMS_USERNAME);
            String password = getProperties().getProperty(ConfigConstants.DOMS_PASSWORD);
            String domsUrl = getProperties().getProperty(ConfigConstants.DOMS_URL);
            return new EnhancedFedoraImpl(
                    new Credentials(username, password), domsUrl, null, null);
        } catch (JAXBException | PIDGeneratorException e) {
            throw new IOException(e);
        }
    }


}
