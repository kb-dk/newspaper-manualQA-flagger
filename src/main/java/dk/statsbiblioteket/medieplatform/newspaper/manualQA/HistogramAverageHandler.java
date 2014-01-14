package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.InjectingTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HistogramAverageHandler extends InjectingTreeEventHandler {

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
                long[] histogram = new Histogram(event.getData()).values();
                filmAverageHistogram.addHistogram(histogram);
                batchAverageHistogram.addHistogram(histogram);
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
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
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }


    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        try {
            if (event.getName().matches("/" + batch.getBatchID() + "-" + "[0-9]{2}$")) {
                // We have now left a film
                pushInjectedEvent(new AttributeParsingEvent(event.getName()+".film.histogram.xml") {
                    @Override
                    public InputStream getData() throws IOException {
                        try {
                            return new ByteArrayInputStream(new Histogram(
                                    filmAverageHistogram.getAverageHistogramAsArray()).toXml().getBytes());
                        } catch (JAXBException e) {
                            throw new IOException(e);
                        }
                    }

                    @Override
                    public String getChecksum() throws IOException {
                        //TODO
                        return null;
                    }
                });

            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }

    private String getComponent() {
        return getClass().getSimpleName();
    }


    /**
     * Called when the batch is finished
     */
    @Override
    public void handleFinish() {
        // TODO output average to somewhere...
    }

}
