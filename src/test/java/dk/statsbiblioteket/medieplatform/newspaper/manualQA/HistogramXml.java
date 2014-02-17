package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.util.Strings;

import java.io.IOException;

public class HistogramXml {

    public static String getSampleGoodHistogram() throws IOException {
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "histogramExample.xml"));
    }

    public static String getSampleBadHistogram() throws IOException {
        // The "BadBadHistogram.xml" is an actual histogram from 9* with possible post-processing artifacts
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "BadBadHistogram.xml"));
//                "histogramExampleBad.xml"));
    }

    public static String getSampleBadDarknessHistogram() throws IOException {
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "histogramExampleBadDarkness.xml"));
    }

    public static String getSampleAltoHiText() throws IOException {
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "altoExampleHiTextAmount.xml"));
    }

    public static String getSampleAltoLoText() throws IOException {
        return Strings.flush(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "altoExampleLoTextAmount.xml"));
    }
}
