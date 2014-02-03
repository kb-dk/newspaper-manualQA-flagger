package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.TreeIterator;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.filesystem.transforming.TransformingIteratorForFileSystems;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ManualQAComponentIT  {

    private Logger logger = LoggerFactory.getLogger(ManualQAComponentIT.class);

    private final static String TEST_BATCH_ID = "400022028241";
    private File genericPropertyFile;
    private Properties properties;
    private ResultCollector resultCollector;
    private FlaggingCollector flaggingCollector;


    @BeforeMethod(alwaysRun = true)
    public void loadGeneralConfiguration() throws Exception {
        String pathToProperties = System.getProperty("integration.test.newspaper.properties");
        properties = new Properties();

        genericPropertyFile = new File(pathToProperties);
        properties.load(new FileInputStream(genericPropertyFile));
    }

    /**
     * Test that a reasonable batch can be run against the flagger component without generating any
     * errors or flags when the batch and configuration agree on the setup..
     * @throws Exception
     */
    @Test(groups = "integrationTest")
    public void testConsistentBatch() throws Exception {
        File specificProperties = new File(genericPropertyFile.getParentFile(),
                "newspaper-manualQA-flagger-config/config.properties");
        logger.debug("Doing validation with generic config.properties");
        properties.load(new FileInputStream(specificProperties));
        validateBatch();
        assertTrue(resultCollector.isSuccess(), resultCollector.toReport());
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    /**
     * Test that a the default batch with a configuration inconsistent with the metadata in the batch. This should
     * generate a lot of flags.
     * @throws Exception
     */
    @Test(groups = "integrationTest")
    public void testInconsistentBatch() throws Exception {
        File specificProperties = new File("src/test/config/inconsistent-flagging-config.properties");
        logger.debug("Doing validation with properties from " + specificProperties.getAbsolutePath());
        properties.load(new FileInputStream(specificProperties));
        validateBatch();
        assertTrue(resultCollector.isSuccess(), resultCollector.toReport());
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
        logger.debug(flaggingCollector.toReport());
    }

    public InputStream retrieveBatchStructure() {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("assumed-valid-structure.xml");
    }

    private void validateBatch()  throws Exception  {
        TreeIterator iterator = getIterator();
        EventRunner runner = new EventRunner(iterator);
        resultCollector = new ResultCollector(getClass().getSimpleName(), "v0.1");
        Batch batch = new Batch();

        batch.setBatchID(TEST_BATCH_ID);
        batch.setRoundTripNumber(1);
        InputStream batchXmlStructureStream = retrieveBatchStructure();

        if (batchXmlStructureStream == null) {
            throw new RuntimeException("Failed to resolve batch manifest from data collector");
        }
        Document batchXmlManifest = DOM.streamToDOM(batchXmlStructureStream);

        flaggingCollector = new FlaggingCollector(batch, batchXmlManifest, "0.1", 100);

        EventHandlerFactory eventHandlerFactory = new FlaggerFactory(resultCollector, batch, batchXmlManifest, flaggingCollector, properties);

        runner.runEvents(eventHandlerFactory.createEventHandlers(), resultCollector);


    }

    /**
     * Creates and returns a iteration based on the test batch file structure found in the test/ressources folder.
     *
     * @return A iterator the the test batch
     * @throws URISyntaxException
     */
    public TreeIterator getIterator() throws URISyntaxException {
        File file = getBatchFolder();
        System.out.println(file);
        return new TransformingIteratorForFileSystems(file, "\\.", ".*\\.jp2$", ".md5");
    }

    private File getBatchFolder() {
        String pathToTestBatch = System.getProperty("integration.test.newspaper.testdata");
        return new File(pathToTestBatch, "small-test-batch/B" + TEST_BATCH_ID + "-RT1");
    }
}
