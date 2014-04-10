package dk.statsbiblioteket.medieplatform.newspaper.manualQA.utils;

public class HistogramUtils {
    public static int findBrightestColor(long[] values, long maxValueToDeemAColorMissing) {
        int i;
        int brightestColor;// Find brightest color
        for (i = values.length - 1; i > -1; i--) {
            if (values[i] > maxValueToDeemAColorMissing) {
                break;
            }
        }
        if (i > -1) {
            brightestColor = i;
        } else {
            brightestColor = values.length;
        }
        return brightestColor;
    }

    public static int findDarkestColor(long[] values, int startingColor, long maxValueToDeemAColorMissing) {
        int i;
        int darkestColor;// Find darkest color
        //TODO start from some other value, see endspike
        for (i = startingColor; i < values.length; i++) {
            if (values[i] > maxValueToDeemAColorMissing) {
                break;
            }
        }
        if (i < values.length) {
            darkestColor = i;
        } else {
            darkestColor = -1;
        }
        return darkestColor;
    }
}
