package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.InputStream;

public class HistogramTest {
    @Test
    public void testToXml() throws Exception {
        InputStream sampleHistogram = Thread.currentThread()
                                            .getContextClassLoader()
                                            .getResourceAsStream("histogramExample.xml");

        Histogram histogramObject = new Histogram(sampleHistogram);

        InputStream sampleHistogramAgain = Thread.currentThread()
                                                 .getContextClassLoader()
                                                 .getResourceAsStream("histogramExample.xml");

        Document control = XMLUnit.buildControlDocument(new InputSource(sampleHistogramAgain));
        Document test = XMLUnit.buildTestDocument(histogramObject.toXml());
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(control, test);

        Assert.assertTrue(diff.similar(), diff.toString());
        Assert.assertTrue(diff.identical(), diff.toString());

    }
}
