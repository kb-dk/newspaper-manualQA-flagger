package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

public class HistogramAverageHandler extends DefaultTreeEventHandler {

    private final ResultCollector resultCollector;
    private final FlaggingCollector flaggingCollector;
    private Batch batch;
    private AverageHistogram filmAverageHistogram = new AverageHistogram();
    private AverageHistogram batchAverageHistogram = new AverageHistogram();


    public HistogramAverageHandler(ResultCollector resultCollector, FlaggingCollector flaggingCollector, Batch batch) {
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        this.batch = batch;
    }


    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".histogram.xml")) {
                filmAverageHistogram.addHistogram(parseHistogram(event, resultCollector));
                batchAverageHistogram.addHistogram(parseHistogram(event, resultCollector));
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "Exception", "component", e.getMessage());
        }
    }


    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        try {
            if (event.getName().matches("/" + batch.getBatchID() + "-" + "[0-9]{2}$")) {
                // We have now entered a film
                filmAverageHistogram.resetAverageHistogram();
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "Exception", "component", e.getMessage());
        }
    }


    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        try {
            if (event.getName().matches("/" + batch.getBatchID() + "-" + "[0-9]{2}$")) {
                // We have now left a film
                // TODO output average to somewhere...
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "Exception", "component", e.getMessage());
        }
    }


    /**
     * Called when the batch is finished
     */
    @Override
    public void handleFinish() {
        // TODO output average to somewhere...
    }

}
