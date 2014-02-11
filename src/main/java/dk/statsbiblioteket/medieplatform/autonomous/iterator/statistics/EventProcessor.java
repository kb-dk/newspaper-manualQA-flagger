package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;

/**
 * Wraps a collector based state machine into a DefaultTreeEventHandler. The hub delegates events to a collector
 * which is updated based on the reference to the new collector provided in the handle result.
 */
public class EventProcessor extends DefaultTreeEventHandler {
    protected StatisticCollector collector;
    private boolean bootstrapping = true;

    /**
     * @param rootCollector The initial collector to use for the first event.
     */
    public EventProcessor(StatisticCollector rootCollector) {
        this.collector = rootCollector;
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        if (bootstrapping) bootstrapping = false;
        else collector = collector.handleNodeBegin(event);
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        collector = collector.handleNodeEnd(event);
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        collector.handleAttribute(event);
    }
}
