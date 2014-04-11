package dk.statsbiblioteket.medieplatform.newspaper.manualQA.utils;

/**
 * Utility class for simple histogram operations
 */
public class HistogramUtils {

    /**
     * Find the highest color that is greater than #maxValueToDeemAColorMissing
     * @param values the values of the histogram
     * @param maxValueToDeemAColorMissing the max value
     * @return the highest color. Return -1 if no bright color is found
     */
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

    /**
     * Find the lowest color that is still more than #maxValueToDeemAColorMissing, starting from #startingColor
     * @param values the values of the histogram
     * @param startingColor the starting color
     * @param maxValueToDeemAColorMissing the max value
     * @return the darkest color. Return -1 if no dark color is found
     */
    public static int findDarkestColor(long[] values, int startingColor, long maxValueToDeemAColorMissing) {
        int i;
        int darkestColor;// Find darkest color
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
