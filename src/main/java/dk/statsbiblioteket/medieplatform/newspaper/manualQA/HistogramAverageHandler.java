package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.HistogramCache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class HistogramAverageHandler implements TreeEventHandler {

    private HistogramCache cache;
    private final ResultCollector resultCollector;
    private final Pattern regex;
    private AverageHistogram filmAverageHistogram;


    public HistogramAverageHandler(ResultCollector resultCollector, HistogramCache cache, Batch batch) {
        this.resultCollector = resultCollector;
        this.cache = cache;
        regex = Pattern.compile(".*/" + batch.getBatchID() + "-" + "[0-9]{2}$");
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event, EventRunner runner) {
        try {
            if (event.getName().endsWith(".film.xml")){
                filmAverageHistogram = new AverageHistogram(event.getName());
            } else  if (event.getName().endsWith("jp2.histogram.xml")) {
                long[] histogram = cache.getHistogram(event).values();
                if (filmAverageHistogram != null) {
                    filmAverageHistogram.addHistogram(histogram);
                }
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event, EventRunner runner) {
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event, EventRunner runner) {
        final String name = event.getName();
        try {
            if (regex.matcher(name).matches()) {
                filmAverageHistogram.close();
                // We have now left a film
                runner.pushEvent(new AttributeParsingEvent(makeName(filmAverageHistogram)) {

                    private final String averageHistogram
                            = new Histogram(filmAverageHistogram.getAverageHistogramAsArray()).toXml();

                    @Override
                    public InputStream getData() throws IOException {
                        return new ByteArrayInputStream(averageHistogram.getBytes());
                    }

                    @Override
                    public String getChecksum() throws IOException {
                        //TODO
                        return null;
                    }
                });
            }

        } catch (Exception e) {
            resultCollector.addFailure(name, "exception", getComponent(), e.getMessage());
        }
    }

    private String makeName(AverageHistogram filmAverageHistogram) {
        if (filmAverageHistogram.getName().endsWith(".film.xml")){
            return filmAverageHistogram.getName().replace(".film.xml",".film.histogram.xml");
        }
        return filmAverageHistogram.getName();
    }

    private String getComponent() {
        return getClass().getSimpleName();
    }


    /**
     * Called when the batch is finished
     */
    @Override
    public void handleFinish(EventRunner runner) {
        // TODO output average to somewhere...
    }

}
