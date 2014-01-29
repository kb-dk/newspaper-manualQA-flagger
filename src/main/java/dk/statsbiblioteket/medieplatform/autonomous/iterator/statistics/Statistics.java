package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.util.Map;
import java.util.TreeMap;

/**
 * Models the collected statistics for this collector.
 */
public class Statistics {
    protected Map<String, Long> countMap = new TreeMap();
    protected Map<String, WeightedMean> relativeCountMap = new TreeMap();

    /**
     * Adds a measurement to the current.
     * @param name The name of the measurement.
     * @param countToAdd The count for the measurement.
     */
    public void addCount(String name, Long countToAdd) {
        Long currentCount = 0L;
        if (countMap.containsKey(name)) {
            currentCount = countMap.get(name);
        }
        countMap.put(name, currentCount + countToAdd);
    }

    /**
     * Adds a weighted statistics. The numbers added here is added to the current
     * metric with the indicated name together with is weight.
     * <p>
     * Example: Node1 has OCR for 3 out of 5 words, Node2 has OCR for 45 out of 121 words.
     * This gives a cumulated OCR successrate of 48/126.
     * </p>
     * @param name The name of the measurement.
     * @param countToAdd The relative count for the measurement.
     */
    public void addRelative(String name, WeightedMean countToAdd) {
        WeightedMean currentCount;
        if (relativeCountMap.containsKey(name)) {
            currentCount = relativeCountMap.get(name);
            relativeCountMap.put(name,
                   currentCount.add(countToAdd));
        } else {
            relativeCountMap.put(name, countToAdd);
        }
    }

    /**
     * Adds one statistics object to this statistics. This means each named measurement is added to this statistics
     * object.
     * <p>
     *     Used for adding child statistics to parent statistics.
     * </p>
     * @param statistics The statistics to add to this collector
     */
    public void addStatistic(Statistics statistics) {
        for (Map.Entry<String, Long> measurement : statistics.countMap.entrySet()) {
            addCount(measurement.getKey(), measurement.getValue());
        }
        for (Map.Entry<String, WeightedMean> measurement : statistics.relativeCountMap.entrySet()) {
            addRelative(measurement.getKey(), measurement.getValue());
        }
    }
}