package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.statistics.BatchCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps a collector based statemachine into a DefaultTreeEventHandler.
 */
public class StatisticManager extends DefaultTreeEventHandler {
    private static Logger log = LoggerFactory.getLogger(StatisticManager.class);
    private final StatisticWriter writer;
    private StatisticCollector collector;

    /**
     * @param writer Defines how the statistics should be written.
     */
    public StatisticManager(StatisticWriter writer) {
        this.writer = writer;

    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        if (collector == null) {
            collector = new BatchCollector(event.getName(), writer);
        } else {
            collector = collector.handleNodeBegin(event);
        }
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        collector = collector.handleNodeEnd(event);
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        collector.handleAttribute(event);
    }

    @Override
    public void handleFinish() {
        super.handleFinish();
        writer.finish();
    }
}
