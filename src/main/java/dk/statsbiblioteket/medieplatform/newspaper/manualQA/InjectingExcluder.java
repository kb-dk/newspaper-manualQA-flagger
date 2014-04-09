package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.ParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.InjectingTreeEventHandler;

import java.util.NoSuchElementException;

public class InjectingExcluder extends InjectingTreeEventHandler {
    private String contains;
    private InjectingTreeEventHandler delegate;

    public InjectingExcluder(String contains, InjectingTreeEventHandler delegate) {
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

    @Override
    public ParsingEvent popInjectedEvent() throws NoSuchElementException {
        return delegate.popInjectedEvent();
    }

    @Override
    public void pushInjectedEvent(ParsingEvent parsingEvent) {
        delegate.pushInjectedEvent(parsingEvent);
    }
}
