package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;

/**
 * Ignores all event, except nodeEnd events for itself. Insert at maximum statistics
 * depth in tree. May inserted at nodes in a tree where no statistics are needed.
 */
public class SinkCollector extends StatisticCollector {
    @Override
    protected StatisticCollector createChild(String eventName) {
        return this;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    protected void initialize(String name, StatisticCollector parentCollector, StatisticWriter writer, Properties properties) {
        this.name = name;
        this.parent = parentCollector;
    }

    @Override
    public StatisticCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        return super.handleNodeBegin(event);
    }

    @Override
    public StatisticCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(name)) {
            return parent;
        } else {
            return this;
        }
    }
}

