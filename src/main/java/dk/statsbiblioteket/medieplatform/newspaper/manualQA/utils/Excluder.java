package dk.statsbiblioteket.medieplatform.newspaper.manualQA.utils;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;

/**
 * The role of this class is to ensure that files in the UNMATCHED folder are not subjected to manual-QA-flagging
 */
public class Excluder extends DefaultTreeEventHandler {
    private String contains;
    private TreeEventHandler delegate;

    public Excluder(String contains, TreeEventHandler delegate) {
        this.contains = contains;
        this.delegate = delegate;
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        if (event.getName().contains(contains)) {
            return;
        }
        delegate.handleNodeBegin(event);
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().contains(contains)) {
            return;
        }
        delegate.handleNodeEnd(event);
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        if (event.getName().contains(contains)) {
            return;
        }
        delegate.handleAttribute(event);
    }

    @Override
    public void handleFinish() {
        delegate.handleFinish();
    }
}
