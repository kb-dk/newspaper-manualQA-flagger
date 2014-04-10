package dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.util.caching.TimeSensitiveCache;
import dk.statsbiblioteket.util.xml.DOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.IOException;

public class AltoCache {

    static Logger log = LoggerFactory.getLogger(AltoCache.class);

    public  TimeSensitiveCache<String,Document> altocache = new TimeSensitiveCache<>(100,true,10);

    public AltoCache() {
    }

    public synchronized  Document getAlto(AttributeParsingEvent event) throws IOException {
        Document alto = altocache.get(event.getName());
        if (alto == null){
            alto = DOM.streamToDOM(event.getData(), true);
            altocache.put(event.getName(),alto);
        }
        return alto;
    }


}
