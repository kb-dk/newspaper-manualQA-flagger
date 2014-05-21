package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: jrg
 * Date: 1/8/14
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AverageHistogramTest {

    @Test
    public void testGetAverageHistogramAsArrayGood() throws Exception {
        AverageHistogram averageHistogram = new AverageHistogram("something");
        long[] histogram1 = new long[256];
        Arrays.fill(histogram1, 1);
        long[] histogram2 = new long[256];
        Arrays.fill(histogram2, 3);

        averageHistogram.addHistogram(histogram1);
        averageHistogram.addHistogram(histogram2);

        long[] average = averageHistogram.getAverageHistogramAsArray();

        for (int i = 0; i < 256; i++) {
            Assert.assertTrue(average[i] == 2);
        }
    }
}
