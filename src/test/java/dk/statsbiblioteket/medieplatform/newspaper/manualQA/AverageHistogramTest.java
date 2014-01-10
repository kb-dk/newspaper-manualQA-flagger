package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import org.testng.Assert;
import org.testng.annotations.Test;

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
        AverageHistogram averageHistogram = new AverageHistogram();
        long[] histogram1 = new long[256];
        for (int i = 0; i < 256; i++) {
            histogram1[i] = 1;
        }
        long[] histogram2 = new long[256];
        for (int i = 0; i < 256; i++) {
            histogram2[i] = 3;
        }


        averageHistogram.addHistogram(histogram1);
        averageHistogram.addHistogram(histogram2);

        long[] average = averageHistogram.getAverageHistogramAsArray();

        for (int i = 0; i < 256; i++) {
            Assert.assertTrue(average[i] == 2);
        }
    }
}
