package dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.Histogram;
import dk.statsbiblioteket.util.caching.TimeSensitiveCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * A simple utility cache to ensure that the Histogram objects are not retrieved and parsed more than once
 */
public class HistogramCache {

    static Logger log = LoggerFactory.getLogger(HistogramCache.class);

    public TimeSensitiveCache<String, Histogram> histogramCache = new TimeSensitiveCache<>(100, true, 10);

    public HistogramCache() {
    }

    public synchronized Histogram getHistogram(AttributeParsingEvent event) throws IOException, JAXBException {
        Histogram histogram = histogramCache.get(event.getName());
        if (histogram == null) {
            histogram = new Histogram(event.getData());
            histogramCache.put(event.getName(), histogram);
        }
        return histogram;
    }

}
