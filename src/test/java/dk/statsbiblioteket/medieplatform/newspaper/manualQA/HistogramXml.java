package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.util.Strings;

import java.io.IOException;

public class HistogramXml {

    public static String getSampleGoodHistogram() throws IOException {
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream("histogramExample.xml"));
    }
}
