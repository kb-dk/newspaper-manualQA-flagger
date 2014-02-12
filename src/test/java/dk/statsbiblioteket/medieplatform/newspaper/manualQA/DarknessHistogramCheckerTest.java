package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
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
        Batch batch = new Batch("40000");
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, null, "0.1-SNAPSHOT", 100);

        int maxNumberOfDarkImagesAllowed = 5;
        int lowestHistogramIndexNotConsideredBlack = 3;
        int lowestAcceptablePeakPosition = 128;
        int minNumberOfTextLines = 10;
        TreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                maxNumberOfDarkImagesAllowed, lowestHistogramIndexNotConsideredBlack, lowestAcceptablePeakPosition,
                minNumberOfTextLines);

        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleGoodHistogram());
        histogramHandler.handleAttribute(event);

        Assert.assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }


    // TODO bad test(s)


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
}
