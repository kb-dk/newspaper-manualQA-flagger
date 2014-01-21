package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.*;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.TreeIterator;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.filesystem.transforming.TransformingIteratorForFileSystems;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: csr
 * Date: 1/17/14
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManualQAComponentIT  {

    private final static String TEST_BATCH_ID = "400022028241";


    /**
     * Test that a reasonable batch can be run against the flagger component without generating any
     * errors, but generating some flags.
     * @throws Exception
     */
    @Test(groups = "integrationTest")
    public void testMetadataGood() throws Exception {
        String pathToProperties = System.getProperty("integration.test.newspaper.properties");
        Properties properties = new Properties();

        File genericProperties = new File(pathToProperties);
        File specificProperties = new File(genericProperties.getParentFile(),
                "newspaper-manualQA-flagger-config/integration.test.newspaper.properties");

        properties.load(new FileInputStream(specificProperties));

        TreeIterator iterator = getIterator();
        EventRunner runner = new EventRunner(iterator);
        ResultCollector resultCollector = new ResultCollector(getClass().getSimpleName(), "v0.1");
        Batch batch = new Batch();

        batch.setBatchID(TEST_BATCH_ID);
        batch.setRoundTripNumber(1);
        InputStream batchXmlStructureStream = retrieveBatchStructure(batch);

        if (batchXmlStructureStream == null) {
            throw new RuntimeException("Failed to resolve batch manifest from data collector");
        }
        Document batchXmlManifest = DOM.streamToDOM(batchXmlStructureStream);

        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, batchXmlManifest, "0.1");

        EventHandlerFactory eventHandlerFactory = new FlaggerFactory(resultCollector, batch, batchXmlManifest, flaggingCollector, properties);

        runner.runEvents(eventHandlerFactory.createEventHandlers(), resultCollector);


        System.out.println(resultCollector.toReport());
        System.out.println(flaggingCollector.toReport());
        assertTrue(resultCollector.isSuccess());
        assertTrue(flaggingCollector.hasFlags());
    }

    public InputStream retrieveBatchStructure(Batch batch) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("assumed-valid-structure.xml");
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
        return new File(pathToTestBatch);
    }

}
