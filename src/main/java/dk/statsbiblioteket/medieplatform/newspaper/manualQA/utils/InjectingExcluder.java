package dk.statsbiblioteket.medieplatform.newspaper.manualQA.utils;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;

/**
 * An excluder for the injecting tree event handlers. Nessesary because the framework uses instance of to
 * determine if an eventhandler is injecting...
 */
public class InjectingExcluder implements TreeEventHandler {
    private String contains;
    private TreeEventHandler delegate;

    public InjectingExcluder(String contains, TreeEventHandler delegate) {
        this.contains = contains;
        this.delegate = delegate;
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event, EventRunner runner) {
        if (event.getName().contains(contains)) {
            return;
        }
        delegate.handleNodeBegin(event,runner);
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event, EventRunner runner) {
        if (event.getName().contains(contains)) {
            return;
        }
        delegate.handleNodeEnd(event, runner);
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event, EventRunner runner) {
        if (event.getName().contains(contains)) {
            return;
        }
        delegate.handleAttribute(event, runner);
    }

    @Override
    public void handleFinish(EventRunner runner) {
        delegate.handleFinish(runner);
    }


}
