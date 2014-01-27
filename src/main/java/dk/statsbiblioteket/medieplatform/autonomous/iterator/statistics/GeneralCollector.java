package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.util.Map;
import java.util.TreeMap;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;

/**
 * Implementes the framework for collecting and outputting statistics for a single type of treenode.
 * <br>
 * Also includes the functionality for implementing a statemachine pattern for maintaining collectors
 * handling each node type, by requiring concrete collectors to return a collector for handling new nodes.
 * <br>
 * Subclasses should implement the functionality for collecting the concrete statistics
 * and generation children collectors.
 *
 * <p></p>Opportunities for improvement: <ol>
 * <li>The collector statemachine structure is currently hardcoded into the concrete classes. If this
 * could instead be defined pr. configuration, we could define the collector structure dynamically.</li>
 * <li>The collectors are current hardcoded to collect statistics. This could be generalized into
 * exposing a more generic processing option, perhaps through a visitor pattern. This might also lead to the
 * processing functionality to be injected into the statemachine structure, thereby separating the collector
 * construction concerns ans the concrete processing.</li>
 * </ol>
 * This would allow us to define the collectors for the newspaper batch structure pr. configuration, and
 * the different tree processor functionalities with classes for the specific needs.
 * </p>
 *
 *
 */
public abstract class GeneralCollector {
    private final String name;
    private final StatisticWriter writer;
    private final GeneralCollector parent;
    private final Statistics statistics;

    /**
     * Constructor with definition of the general ressources need to collect and writing statistics.
     * @param name The name of the collector
     * @param parent The collector for the parent node.
     * @param writer The concrete writer to output the statistics.
     */
    public GeneralCollector(String name, GeneralCollector parent, StatisticWriter writer) {
        this.name = name;
        this.writer = writer;
        this.parent = parent;
        statistics = new Statistics();
        writer.addNode(getType(), getSimpleName(name));
    }

    /** Used by collectors not writing any output. */
    public GeneralCollector(String name, GeneralCollector parent) {
        this.name = name;
        this.parent = parent;
        writer = null;
        statistics = new Statistics();
    }

    /**
     * @return Used for naming the statistics node in the output.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Used for identifying the statistics node in the output.
     */
    public abstract String getType();

    /**
     * @return Returns the writer which is used to output the statistics..
     */
    protected StatisticWriter getWriter() {
        return writer;
    }

    private void writeStatistics() {
        for (Map.Entry<String, Long> measurement : statistics.countMap.entrySet()) {
            getWriter().addStatistic(measurement.getKey(), measurement.getValue());
        }

        for (Map.Entry<String, RelativeCount> measurement : statistics.relativeCountMap.entrySet()) {
            getWriter().addStatistic(measurement.getKey(), measurement.getValue());
        }
    }

    /**
     * Must be implemented by the concrete subclasses defining the actual statistics collection and
     * which collector to return to handle the new node.
     * @param event The event defining the new node.
     * @return The collector for the new node. This implements a state-machine pattern by returning a specific node
     * collector for each node change event (nodebegin/nodeend).
     */
    public abstract GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event);

    /**
     * Writes statistics and adds statistics to parent.
     * @param event The event identifying the node which has finished.
     * @return The parent collector
     */
    public GeneralCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(name)) {
            writeStatistics();
            if (parent != null) { // Root collector
                parent.getStatistics().addStatistic(statistics);
            }
            getWriter().endNode();
            return parent;
        } else throw new RuntimeException("Unexpected " + event);
    }

    /**
     * Must be implemented by concrete subclasses. Used for collecting any attribute based statistics.
     * @param event
     */
    public abstract void handleAttribute(AttributeParsingEvent event);

    /**
     * Utility method for for accessing the last part of the event name path.
     * @param absoluteName The full name of the event.
     * @return The last part of the absolute name.
     */
    protected String getSimpleName(String absoluteName) {
        return absoluteName.substring(absoluteName.lastIndexOf('/') + 1);
    }

    /**
     * @return The current statistics for this collector.
     */
    protected Statistics getStatistics() {
        return statistics;
    }

    /**
     * Models the collected statistics for this collector.
     */
    protected class Statistics {
        protected Map<String, Long> countMap = new TreeMap();
        protected Map<String, RelativeCount> relativeCountMap = new TreeMap();

        /**
         * Adds a simple statistics. The numbers added here is just added to the current
         * metric with the indicated name.
         * @param name The name of the measurement.
         * @param countToAdd The count for the measurement.
         */
        public void addCount(String name, long countToAdd) {
            long currentCount = 0;
            if (countMap.containsKey(name)) {
                currentCount = countMap.get(name);
            }
            countMap.put(name, currentCount + countToAdd);
        }

        /**
         * Adds a weighted statistics. The numbers added here is added to the current
         * metric with the indicated name together with is weigth.
         * <p>
         * Example: Node1 has OCR for 3 out of 5 words, Node2 has OCR for 45 out of 121 words.
         * This gives a cumulated OCR successrate of 48/126.
         * </p>
         * @param name The name of the measurement.
         * @param countToAdd The relative count for the measurement.
         */
        public void addRelative(String name, RelativeCount countToAdd) {
            RelativeCount currentCount;
            if (relativeCountMap.containsKey(name)) {
                currentCount = relativeCountMap.get(name);
                relativeCountMap.put(name,
                        new RelativeCount(currentCount.count + countToAdd.count, currentCount.total + countToAdd.total));
            } else {
                relativeCountMap.put(name, countToAdd);
            }
        }

        /**
         * Adds one statistics object this this statistics. This means each named measurement is added to this statistics
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
            for (Map.Entry<String, RelativeCount> measurement : statistics.relativeCountMap.entrySet()) {
                addRelative(measurement.getKey(), measurement.getValue());
            }
        }
    }

    /**
     * Defines a weighted count.
     *
     * @see Statistics#addRelative(String, dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.GeneralCollector.RelativeCount)
     */
    protected class RelativeCount {
        final float count;
        final float total;

        public RelativeCount(float count, float total) {
            this.count = count;
            this.total = total;
        }

        @Override
        public String toString() {
            if (count > 0 && total > 0) return Float.toString(count/total);
            else return "";
        }
    }
}
