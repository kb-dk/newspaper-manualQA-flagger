package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.model;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.model.Statistics;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class StatisticsTest {
    @Test
    public void subStatisticsAdditionTest() {
        Statistics rootStatistics = new Statistics();
        assertEquals(rootStatistics.substatisticsMap.size(), 0);

        Statistics firstSubstatistics = new Statistics();
        firstSubstatistics.addCount("Books", 1L);
        firstSubstatistics.addCount("Travel", 1L);
        rootStatistics.addSubstatistic("Sections", firstSubstatistics);
        assertEquals(rootStatistics.substatisticsMap.size(), 1);
        assertEquals(rootStatistics.substatisticsMap.get("Sections").countMap.size(), 2);
        assertEquals(rootStatistics.substatisticsMap.get("Sections").countMap.get("Books"), new Long(1));

        Statistics secondSubstatistics = new Statistics();
        secondSubstatistics.addCount("Books", 1L);
        secondSubstatistics.addCount("Cats", 1L);
        rootStatistics.addSubstatistic("Sections", secondSubstatistics);
        assertEquals(rootStatistics.substatisticsMap.size(), 1);
        assertEquals(rootStatistics.substatisticsMap.get("Sections").countMap.size(), 3);
        assertEquals(rootStatistics.substatisticsMap.get("Sections").countMap.get("Books"), new Long(2));
        assertEquals(rootStatistics.substatisticsMap.get("Sections").countMap.get("Travel"), new Long(1));
        assertEquals(rootStatistics.substatisticsMap.get("Sections").countMap.get("Cats"), new Long(1));
    }
}
