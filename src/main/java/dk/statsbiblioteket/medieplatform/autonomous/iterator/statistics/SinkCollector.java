package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;

/**
 * Ignores all event, except nodeEnd events for itself. Insert at maximum statistics
 * depth in tree. May inserted at nodes in a tree where no statistics are needed.
 */
public class SinkCollector extends GeneralCollector {
    private final GeneralCollector parent;
    public SinkCollector(String name, GeneralCollector parentCollector) {
        super(name, parentCollector);
        parent = parentCollector;
    }

    /**
     * Subnodes are ignored.
     * @return Itself
     */
    @Override
    public GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        return this;
    }

    /**
     * @param event The event identifying the node which has finished.
     * @return The parent collector if the node finishing has the same name as this collector, else itself.
     */
    @Override
    public GeneralCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(getName())) {
            return parent;
        } else return this;
    }

    /**
     * No statistics, all is sinked.
     */
    @Override
    public void handleAttribute(AttributeParsingEvent event){}

    @Override
    public String getType() {
        return null;
    }
}
