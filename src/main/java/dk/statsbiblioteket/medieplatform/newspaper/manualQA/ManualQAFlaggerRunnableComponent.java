package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;

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
import dk.statsbiblioteket.medieplatform.autonomous.SBOIBasedAbstractRunnableComponent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.Files;
import dk.statsbiblioteket.util.xml.DOM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class ManualQAFlaggerRunnableComponent extends SBOIBasedAbstractRunnableComponent {

    private Logger log = LoggerFactory.getLogger(getClass());
    private Properties properties;
    private final static String OUTPUT_FILE_SUFFIX = ".manualqainput.xml";
    
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

        int maxFlags = 0;
        try {
            maxFlags = Integer.parseInt(properties.getProperty(dk.statsbiblioteket.medieplatform.newspaper.manualQA.ConfigConstants.MAX_FLAGS));
        } catch (NumberFormatException e) {
            final String message = "Property " + dk.statsbiblioteket.medieplatform.newspaper.manualQA.ConfigConstants.MAX_FLAGS + " is not set" +
                    " to an integer value.";
            log.error(message);
            throw new RuntimeException(message, e);
        }
        FlaggingCollector flaggingCollector = new FlaggingCollector(
                batch, batchXmlStructure, getComponentVersion(), maxFlags);
        EventHandlerFactory eventHandlerFactory = new FlaggerFactory(
                resultCollector, batch, flaggingCollector, properties);
        List<TreeEventHandler> eventHandlers = eventHandlerFactory.createEventHandlers();
        EventRunner eventRunner = new EventRunner(createIterator(batch));
        eventRunner.runEvents(eventHandlers, resultCollector);
        saveFlaggingCollector(flaggingCollector);
        log.info("Done validating '{}', success: {}", batch.getFullID(), resultCollector.isSuccess());

    }

    private void saveFlaggingCollector(FlaggingCollector flaggingCollector) throws Exception {
        String report = flaggingCollector.toReport();
        String fullID = flaggingCollector.getBatch().getFullID();
        
        saveOutputToRepository(fullID, report);
        saveOutputOnFilesystem(fullID, report);
    }
    
    private void saveOutputToRepository(String fullBatchID, String report) 
            throws IOException, BackendInvalidCredsException, BackendMethodFailedException, BackendInvalidResourceException {
        EnhancedFedora fedora = createFedoraClient();
        String pid;

        String path = "path:" + fullBatchID;
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
                pid, "MANUAL_QA_FLAGS", report, null, "added data for MANUAL QA");
    }
    
    /**
     * Saves the report to the file system, if a destination directory is specified in the configuration 
     */
    private void saveOutputOnFilesystem(String fullBatchID, String report) throws IOException {
        String outputDir = getProperties().getProperty(
                dk.statsbiblioteket.medieplatform.newspaper.manualQA.ConfigConstants.MANUAL_QA_INPUTFILES_DIR);
        if(outputDir != null) {
            String filename = fullBatchID + OUTPUT_FILE_SUFFIX;
            File outputfile = new File(outputDir, filename);
            Files.saveString(report, outputfile);
        } else {
            log.warn("No directory for writing manual QA output was set in the configuration, was this intentional? "
                    + "QA flags for batch '" + fullBatchID + "' not written to disk");
        }
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
