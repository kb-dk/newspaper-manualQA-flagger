package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;

/**
 * Ignores all event, except nodeEnd events for itself. Insert at maximum statistics
 * depth in tree. May inserted at nodes in a tree where no statistics are needed.
 */
public class SinkCollector extends StatisticCollector {

    public SinkCollector() {
        doNotCount();
        doNotWrite();
    }

    @Override
    protected StatisticCollector createChild(String eventName) {
        return this;
    }

    @Override
    public StatisticCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(getName())) {
            return parent;
        } else {
            return this;
        }
    }
}

