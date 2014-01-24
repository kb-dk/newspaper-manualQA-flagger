package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;

/**
 * Ignores all event, except nodeEnd events for itself. Insert at maximum statistics
 * depth in tree.
 */
public class SinkCollector extends GeneralCollector {
    private final GeneralCollector parent;
    public SinkCollector(String name, GeneralCollector parentCollector) {
        super(name, parentCollector);
        parent = parentCollector;
    }

    public GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        return this;
    }

    public GeneralCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(getName())) {
            return parent;
        } else return this;
    }

    public void handleAttribute(AttributeParsingEvent event){}

    @Override
    public String getType() {
        return null;
    }
}
