package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.ParsingEventType;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DarknessHistogramCheckerTest {
    @Test
    public void testHandleAttributeGood() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        String batchID = "40000";
        Batch batch = new Batch(batchID);
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, null, "0.1-SNAPSHOT", 100);
        AttributeParsingEvent event;

        int maxNumberOfDarkImagesAllowed = 2;
        int lowestHistogramIndexNotConsideredBlack = 3;
        int lowestAcceptablePeakPosition = 128;
        int minNumberOfTextLines = 10;
        TreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                maxNumberOfDarkImagesAllowed, lowestHistogramIndexNotConsideredBlack, lowestAcceptablePeakPosition,
                minNumberOfTextLines);

        NodeBeginsParsingEvent beginEvent = createNodeBeginsParsingEvent("/" + batchID + "-99");
        histogramHandler.handleNodeBegin(beginEvent);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.edition.xml", "");
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleGoodHistogram());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0080.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0080.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0081.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0081.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText());
        histogramHandler.handleAttribute(event);

        NodeEndParsingEvent endEvent = createNodeEndParsingEvent("/" + batchID + "-99");
        histogramHandler.handleNodeEnd(endEvent);

        Assert.assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBad() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        String batchID = "40000";
        Batch batch = new Batch(batchID);
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, null, "0.1-SNAPSHOT", 100);
        AttributeParsingEvent event;

        int maxNumberOfDarkImagesAllowed = 1;
        int lowestHistogramIndexNotConsideredBlack = 3;
        int lowestAcceptablePeakPosition = 128;
        int minNumberOfTextLines = 10;
        TreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                maxNumberOfDarkImagesAllowed, lowestHistogramIndexNotConsideredBlack, lowestAcceptablePeakPosition,
                minNumberOfTextLines);

        NodeBeginsParsingEvent beginEvent = createNodeBeginsParsingEvent("/" + batchID + "-99");
        histogramHandler.handleNodeBegin(beginEvent);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.edition.xml", "");
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0080.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0080.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText());
        histogramHandler.handleAttribute(event);

        NodeEndParsingEvent endEvent = createNodeEndParsingEvent("/" + batchID + "-99");
        histogramHandler.handleNodeEnd(endEvent);

        Assert.assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    
    @Test
    public void testHandleAttributeGoodBecauseOfAlto() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        String batchID = "40000";
        Batch batch = new Batch(batchID);
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, null, "0.1-SNAPSHOT", 100);
        AttributeParsingEvent event;

        int maxNumberOfDarkImagesAllowed = 1;
        int lowestHistogramIndexNotConsideredBlack = 3;
        int lowestAcceptablePeakPosition = 128;
        int minNumberOfTextLines = 10;
        TreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                maxNumberOfDarkImagesAllowed, lowestHistogramIndexNotConsideredBlack, lowestAcceptablePeakPosition,
                minNumberOfTextLines);

        // This test should succeed even though there are "too many" dark images - because there are low amounts of text on the
        // pages (according to alto) and so they're disregarded as picture-heavy pages

        NodeBeginsParsingEvent beginEvent = createNodeBeginsParsingEvent("/" + batchID + "-99");
        histogramHandler.handleNodeBegin(beginEvent);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.edition.xml", "");
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.alto.xml",
                HistogramXml.getSampleAltoLoText());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0080.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram());
        histogramHandler.handleAttribute(event);

        event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0080.jp2.alto.xml",
                HistogramXml.getSampleAltoLoText());
        histogramHandler.handleAttribute(event);

        NodeEndParsingEvent endEvent = createNodeEndParsingEvent("/" + batchID + "-99");
        histogramHandler.handleNodeEnd(endEvent);

        Assert.assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }


    private AttributeParsingEvent createAttributeEvent(String name, final String contents) {
        return new AttributeParsingEvent(name) {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream(contents.getBytes());

            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }


        };
    }


    private NodeBeginsParsingEvent createNodeBeginsParsingEvent(String name) {
        return new NodeBeginsParsingEvent(name);
    }


    private NodeEndParsingEvent createNodeEndParsingEvent(String name) {
        return new NodeEndParsingEvent(name);
    }
}
