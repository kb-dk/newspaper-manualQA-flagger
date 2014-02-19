package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.util.Strings;

import java.io.IOException;

public class HistogramXml {
    private static final String GOOD_HIST = "histogramExample.xml";

    // The "BadBadHistogram.xml" is an actual histogram from 9* with possible post-processing artifacts
    // Formerly "histogramExampleBad.xml"
    private static final String BAD_BAD_HIST = "BadBadHistogram.xml";

    private static final String BAD_DARKNESS_HIST = "histogramExampleBadDarkness.xml";
    private static final String ALTO_HI = "altoExampleHiTextAmount.xml";
    private static final String ALTO_LO = "altoExampleLoTextAmount.xml";


    public static String getSampleGoodHistogram() throws IOException {
        return getXml(GOOD_HIST);
    }

    public static String getSampleBadHistogram() throws IOException {
        return getXml(BAD_BAD_HIST);
    }

    public static String getSampleBadDarknessHistogram() throws IOException {
        return getXml(BAD_DARKNESS_HIST);
    }

    public static String getSampleAltoHiText() throws IOException {
        return getXml(ALTO_HI);
    }

    public static String getSampleAltoLoText() throws IOException {
        return getXml(ALTO_LO);
    }

    private static String getXml(String file) throws IOException {
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream(file));
    }
}
