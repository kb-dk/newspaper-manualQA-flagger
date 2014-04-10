package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.InjectingTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.HistogramCache;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class HistogramAverageHandler extends InjectingTreeEventHandler {

    private HistogramCache cache;
    private final ResultCollector resultCollector;
    private final Pattern regex;
    private AverageHistogram filmAverageHistogram = new AverageHistogram();
    private AverageHistogram batchAverageHistogram = new AverageHistogram();


    public HistogramAverageHandler(ResultCollector resultCollector, HistogramCache cache, Batch batch) {
        this.resultCollector = resultCollector;
        this.cache = cache;
        regex = Pattern.compile(".*/" + batch.getBatchID() + "-" + "[0-9]{2}$");
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith("jp2.histogram.xml")) {
                long[] histogram = cache.getHistogram(event).values();
                filmAverageHistogram.addHistogram(histogram);
                batchAverageHistogram.addHistogram(histogram);
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }


    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        final String name = event.getName();
        try {
            if (regex.matcher(name).matches()) {
                // We have now entered a film
                filmAverageHistogram.resetAverageHistogram();
            }
        } catch (Exception e) {
            resultCollector.addFailure(name, "exception", getComponent(), e.getMessage());
        }
    }


    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        final String name = event.getName();
        try {
            if (regex.matcher(name).matches()) {
                // We have now left a film
                pushInjectedEvent(new AttributeParsingEvent(name +".film.histogram.xml") {
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

            } else {
                //System.out.println(name);
            }

        } catch (Exception e) {
            resultCollector.addFailure(name, "exception", getComponent(), e.getMessage());
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
