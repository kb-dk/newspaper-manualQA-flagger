package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.model;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.model.WeightedMean;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class WeightedMeanTest {
    @Test
    public void addTest() {
        WeightedMean wm = new WeightedMean(1,2);
        assertEquals(wm.add(new WeightedMean(1,2)), new WeightedMean(2,4));

        assertEquals(wm.add(new WeightedMean(7,8)), new WeightedMean(8,10));

    }
}
