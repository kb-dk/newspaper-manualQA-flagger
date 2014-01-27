package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.util.Map;

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
public abstract class StatisticCollector {
    protected String name;
    protected StatisticWriter writer;
    protected StatisticCollector parent;
    private final Statistics statistics;

    public StatisticCollector() {
        statistics = new Statistics();
    }

    protected void initialize(String name, StatisticCollector parentCollector, StatisticWriter writer) {
        this.name = name;
        this.parent = parentCollector;
        this.writer = writer;
        if (writeNode() && writer != null) { // Does the concrete collector participate in the statistics output.
            writer.addNode(getType(), getSimpleName(name));
        }
        // Should this node be included in the statistics. NA for parentless root node.
        if (parent != null && getStatisticsName() != null) {
            parent.getStatistics().addCount(getStatisticsName(), 1L);
        }
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
    protected abstract String getType();

    /**
     * @return Used for element name to use in the statistics. Default implementatios is to append 'es'
     * to the type, but this may be overridden by subclasses. If null is returned no statistics are added
     * for this node.
     */
    public String getStatisticsName() {
        if (getType() != null) {
            return getType() + "s";
        } else {
            return null;
        }
    }

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

        for (Map.Entry<String, WeightedMean> measurement : statistics.relativeCountMap.entrySet()) {
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
    public StatisticCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        String[] nameParts = event.getName().split("/");
        StatisticCollector newCollector = createChild(nameParts[nameParts.length-1]);
        if (newCollector == null) {
            throw new RuntimeException("Unexpected event: " + event);
        }
        if (newCollector != this) {
            newCollector.initialize(event.getName(), this, writer);
        }
        return newCollector;
    }

        /**
         * Writes statistics and adds statistics to parent.
         * @param event The event identifying the node which has finished.
         * @return The parent collector
         */
    public StatisticCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(name)) {
            writeStatistics();
            if (parent != null) { // Root collector
                parent.getStatistics().addStatistic(statistics);
            }
            if (writeNode() && getWriter() != null) {
                getWriter().endNode();
            }
            return parent;
        } else throw new RuntimeException("Unexpected " + event);
    }

    /**
     * May be implemented by concrete subclasses whishing to collecting attribute based statistics.
     */
    public void handleAttribute(AttributeParsingEvent event){}

    /**
     * Utility method for for accessing the last part of the event name path.
     * @param absoluteName The full name of the event.
     * @return The last part of the absolute name.
     */
    protected static String getSimpleName(String absoluteName) {
        return absoluteName.substring(absoluteName.lastIndexOf('/') + 1);
    }

    /**
     * @return The current statistics for this collector.
     */
    protected Statistics getStatistics() {
        return statistics;
    }

    /**
     * Indicates whether a mode for this collector should be included in the out. Default is true, but may be overridden
     * by subclasses.
     */
    protected boolean writeNode() {
        return true;
    }

    protected abstract StatisticCollector createChild(String eventName);
}
