package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.AltoMocker;
import dk.statsbiblioteket.util.xml.DOM;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class AltoWordAccuracyCheckerTest {

    Properties properties;

    @BeforeMethod
    public void setUp() {
        properties = new Properties();
        properties.setProperty(ConfigConstants.MINIMUM_ALTO_AVERAGE_ACCURACY, "60.0");
        properties.setProperty(ConfigConstants.MINIMUM_ALTO_PERFILE_ACCURACY, "20.0");
        properties.setProperty(ConfigConstants.ALTO_IGNORE_ZERO_ACCURACY, "true");
    }

    /**
     * Test that a single page with accuracy 15.0 generates a flag immediately.
     * @throws Exception
     */
    @Test
    public void testHandleSingleBadPage() throws Exception {
        ResultCollector resultCollector = new ResultCollector("foobar", "1.0");
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        AltoWordAccuracyChecker altoWordAccuracyChecker =
                new AltoWordAccuracyChecker(resultCollector, flaggingCollector, properties);
        AttributeParsingEvent e1 = generateAttributeEvent("foo/bar/film1/edition1/1.alto.xml", "15.0");
        altoWordAccuracyChecker.handleAttribute(e1);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    /**
     * Test that a page with accuracy exactly zero raises no flag.
     * @throws Exception
     */
    @Test
    public void testHandleSinglePageNoText() throws Exception {
        ResultCollector resultCollector = new ResultCollector("foobar", "1.0");
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        AltoWordAccuracyChecker altoWordAccuracyChecker =
                new AltoWordAccuracyChecker(resultCollector, flaggingCollector, properties);
        AttributeParsingEvent e1 = generateAttributeEvent("foo/bar/film1/edition1/1.alto.xml", "0.0");
        altoWordAccuracyChecker.handleAttribute(e1);
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleEditionEndWithFlag() throws Exception {
        ResultCollector resultCollector = new ResultCollector("foobar", "1.0");
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        AltoWordAccuracyChecker altoWordAccuracyChecker =
                 new AltoWordAccuracyChecker(resultCollector, flaggingCollector, properties);
        AttributeParsingEvent e1 = generateAttributeEvent("foo/bar/film1/edition1/1.alto.xml", "50.0");
        AttributeParsingEvent e2 = generateAttributeEvent("foo/bar/film1/edition1/2.alto.xml", "40.0");
        NodeEndParsingEvent e3 = new NodeEndParsingEvent("foo/bar/film1/edition1");
        altoWordAccuracyChecker.handleAttribute(e1);
        altoWordAccuracyChecker.handleAttribute(e2);
        altoWordAccuracyChecker.handleNodeEnd(e3);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleFilmEndWithFlag() throws Exception {
        ResultCollector resultCollector = new ResultCollector("foobar", "1.0");
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        AltoWordAccuracyChecker altoWordAccuracyChecker =
                new AltoWordAccuracyChecker(resultCollector, flaggingCollector, properties);
        AttributeParsingEvent e1 = generateAttributeEvent("foo/bar/film1/edition1/1.alto.xml", "65.0");
        AttributeParsingEvent e2 = generateAttributeEvent("foo/bar/film1/edition1/2.alto.xml", "65.0");
          AttributeParsingEvent e3 = generateAttributeEvent("foo/bar/film1/edition2/1.alto.xml", "40.0");
        AttributeParsingEvent e4 = generateAttributeEvent("foo/bar/film1/edition2/2.alto.xml", "40.0");
        NodeEndParsingEvent e5 = new NodeEndParsingEvent("foo/bar/film1");
        altoWordAccuracyChecker.handleAttribute(e1);
        altoWordAccuracyChecker.handleAttribute(e2);
        altoWordAccuracyChecker.handleAttribute(e3);
        altoWordAccuracyChecker.handleAttribute(e4);
        altoWordAccuracyChecker.handleNodeEnd(e5);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    /**
     * Check that a film with mean accuracy>60.0  generates no flag, including test that page with
     * accuracy=0 is ignored in the calculation.
     * @throws Exception
     */
     @Test
    public void testHandleFilmEndWithoutFlag() throws Exception {
        ResultCollector resultCollector = new ResultCollector("foobar", "1.0");
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        AltoWordAccuracyChecker altoWordAccuracyChecker =
                new AltoWordAccuracyChecker(resultCollector, flaggingCollector, properties);
        AttributeParsingEvent e1 = generateAttributeEvent("foo/bar/film1/edition1/1.alto.xml", "65.0");
        AttributeParsingEvent e2 = generateAttributeEvent("foo/bar/film1/edition1/2.alto.xml", "65.0");
          AttributeParsingEvent e3 = generateAttributeEvent("foo/bar/film1/edition2/1.alto.xml", "58.0");
        AttributeParsingEvent e4 = generateAttributeEvent("foo/bar/film1/edition2/2.alto.xml", "58.0");
         AttributeParsingEvent e5 = generateAttributeEvent("foo/bar/film1/edition2/3.alto.xml", "0.0");
         NodeEndParsingEvent e6 = new NodeEndParsingEvent("foo/bar/film1");
        altoWordAccuracyChecker.handleAttribute(e1);
        altoWordAccuracyChecker.handleAttribute(e2);
        altoWordAccuracyChecker.handleAttribute(e3);
        altoWordAccuracyChecker.handleAttribute(e4);
        altoWordAccuracyChecker.handleAttribute(e5);
        altoWordAccuracyChecker.handleNodeEnd(e6);
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleEditionEndWithoutFlag() throws Exception {
        ResultCollector resultCollector = new ResultCollector("foobar", "1.0");
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        AltoWordAccuracyChecker altoWordAccuracyChecker =
                new AltoWordAccuracyChecker(resultCollector, flaggingCollector, properties);
        AttributeParsingEvent e1 = generateAttributeEvent("foo/bar/film1/edition1/1.alto.xml", "50.0");
        AttributeParsingEvent e2 = generateAttributeEvent("foo/bar/film1/edition1/2.alto.xml", "80.0");
        NodeEndParsingEvent e3 = new NodeEndParsingEvent("foo/bar/film1/edition1");
        altoWordAccuracyChecker.handleAttribute(e1);
        altoWordAccuracyChecker.handleAttribute(e2);
        altoWordAccuracyChecker.handleNodeEnd(e3);
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testRunningAverage() {
        AltoWordAccuracyChecker.RunningAverage runningAverage = new AltoWordAccuracyChecker.RunningAverage();
        runningAverage.addValue(3.0);
        runningAverage.addValue(5.0);
        runningAverage.addValue(6.0);
        //Running average is 4 2/3, so
        assertTrue(runningAverage.getCurrentValue() > 4.6666);
        assertTrue(runningAverage.getCurrentValue() < 4.6667);
    }


    private AttributeParsingEvent generateAttributeEvent(String name, String accuracy) {
        final String alto = AltoMocker.getAlto(accuracy);
        return new AttributeParsingEvent(name) {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream(alto.getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                throw new RuntimeException("not implemented");
            }
        };
    }
}
