package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.statistics.BatchCollector;

public class StatisticCollector extends DefaultTreeEventHandler {
    private final StatisticWriter writer;

    private GeneralCollector collector;

    public StatisticCollector(StatisticWriter writer) {
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

    protected GeneralCollector getCollector() {
        return collector;
    }
}
