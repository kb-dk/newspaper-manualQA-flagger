package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

/**
 * Defines the statistics write operations which can be performed.
 */
public interface StatisticWriter {
    /**
     * Adds a node element.
     * @param type The type of element.
     * @param name The name attribute.
     */
    void addNode(String type, String name);

    /**
     * Finishes the current node.
     */
    void endNode();

    /**
     * Adds a simple measurement, counted N.
     * @param name The name of the measurement.
     * @param metric The N measurement.
     */
    void addStatistic(String name, long metric);

    /**
     * Adds a relativ measurement, eg. N out of M.
     * @param name The name of the measurement.
     * @param metric The N/M measurement.
     */
    void addStatistic(String name, WeightedMean metric);

    void finish();
}
