package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.util.Map;

/**
 * Provides functionality for manipulating the model in a statistics object. The processing is done by implementing
 * methods for changing the statistics maps.
 *
 * @see dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics
 */
public interface StatisticsConverter {
    void processCounts(Map<String, Long> countMap);
    void processMeans(Map<String, WeightedMean> relativeCountMap);
    void processSubstatistics(Map<String, Statistics> substatisticsMap);
}
