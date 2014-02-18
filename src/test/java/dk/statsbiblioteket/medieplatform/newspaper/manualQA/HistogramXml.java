package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.util.Strings;

import java.io.IOException;

public class HistogramXml {

    public static String getSampleGoodHistogram() throws IOException {
        return getXml("histogramExample.xml");
    }

    public static String getSampleBadHistogram() throws IOException {
        // The "BadBadHistogram.xml" is an actual histogram from 9* with possible post-processing artifacts
        return getXml("BadBadHistogram.xml");
//                "histogramExampleBad.xml"
    }

    public static String getSampleBadDarknessHistogram() throws IOException {
        return getXml("histogramExampleBadDarkness.xml");
    }

    public static String getSampleAltoHiText() throws IOException {
        return getXml("altoExampleHiTextAmount.xml");
    }

    public static String getSampleAltoLoText() throws IOException {
        return getXml("altoExampleLoTextAmount.xml");
    }

    private static String getXml(String file) throws IOException {
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream(file));
    }
}
