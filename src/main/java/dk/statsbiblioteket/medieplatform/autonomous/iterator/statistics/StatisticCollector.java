package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.model.Statistics;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.writer.StatisticWriter;

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
 */
public abstract class StatisticCollector {
    private String name;
    private Properties properties;
    StatisticWriter writer;
    protected StatisticCollector parent;
    private final Statistics statistics;
    private final String myType;
    private boolean doNotCount = false;
    private boolean doNotWrite = false;

    public StatisticCollector() {
        statistics = new Statistics();
        String myClassName = getClass().getSimpleName();
        myType = myClassName.substring(0, myClassName.indexOf("Collector"));
    }

    /**
     * Injects the relevant dependencies into this collector.
     * @return The name of these statistics.
     */
    public void initialize(String name, StatisticCollector parentCollector, StatisticWriter writer, Properties properties) {
        this.name = name;
        this.parent = parentCollector;
        this.writer = writer;
        this.properties = properties;
        if (shouldWrite()) writer.addNode(myType, getSimpleName(name));
        if (shouldCount() && parentCollector != null) parentCollector.getStatistics().addCount(myType + 's', 1L);
    }

    /**
     * @return Used for naming the statistics node in the output.
     */
    public String getName() {
        return name;
    }

    /**
     * Defines how collectors should be created based on the incoming event.
     * @param eventName Used for determining how the collector should be constructed and return.
     * @return The collector to handle the new node. The returned collector may be a existing collector, f.ex. itself.
     */
    protected abstract StatisticCollector createChild(String eventName);

    /**
     * Will cause the collector to signal it should't be counted
     * @return this, with the doNotCount
     */
    public StatisticCollector doNotCount() {
        doNotCount = true;
        return this;
    }

    public boolean shouldCount() {
        return !doNotCount;
    }

    /**
     * Will cause the collector to not write any statistics. This includes supressing including an output node for
     * itself.
     * @return this, with the doNotCount
     */
    public StatisticCollector doNotWrite() {
        doNotWrite = true;
        return this;
    }

    public boolean shouldWrite() {
        return !doNotWrite;
    }

    /**
     * @return Returns the statistics object used by this collector. May be overridden by concrete subclasses.
     */
    public Statistics getStatistics() {
        return statistics;
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
        StatisticCollector childCollector = createChild(nameParts[nameParts.length-1]);
        if (childCollector == null) throw new RuntimeException("Unexpected event: " + event);
        if (childCollector != this) childCollector.initialize(event.getName(), this, writer, properties);
        return childCollector;
    }

    /**
     * Writes statistics and adds statistics to parent.
     * @param event The event identifying the node which has finished.
     * @return The parent collector
     */
    public StatisticCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(name)) {
            if (shouldWrite()) getStatistics().writeStatistics(writer);
            if (parent != null) parent.addStatistics(getStatistics());
            if (shouldWrite()) writer.endNode();
            return parent;
        } else throw new RuntimeException("Unexpected " + event);
    }

    /**
     * May be implemented by concrete subclasses whishing to collecting attribute based getStatistics().
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
    public void addStatistics(Statistics statisticsToAdd) {
        getStatistics().addStatistic(statisticsToAdd);
    }
}
