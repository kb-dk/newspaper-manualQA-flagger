package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.Pair;

/**
 * Alle histogrammerne havde en meget stor spike i 0 eller 2. (pga. mærkelig scanner). Det er ikke undgåeligt. Dette er
 * ikke en spike i kurvemæssig forstand. Det er en enkelt farve, så står op som et søm i grafen. Det samme kunne være
 * tilfældet i den anden ende. Hvis denne spike er for stor, tænker her 2% af det samlede antal pixel, skal billedet
 * flages.
 */
public class EndSpikeHistogramChecker extends DefaultTreeEventHandler {

    private dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector flaggingCollector;
    private ResultCollector resultCollector;
    private double threshold;

    public EndSpikeHistogramChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector,
                                    double threshold) {
        this.flaggingCollector = flaggingCollector;
        this.resultCollector = resultCollector;
        this.threshold = threshold;
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".film.histogram.xml")) {
                Histogram histogram = new Histogram(event.getData());
                Pair<Spike, Long> spikeAndTotal = findSpike(histogram);
                Spike spike = spikeAndTotal.getLeft();
                Long total = spikeAndTotal.getRight();
                double amount = (spike.getValue() + 0.0) / total;
                if (amount > threshold) {
                    flaggingCollector.addFlag(
                            event,
                            "jp2file",
                            getComponent(),
                            "Found suspicious color spike for color '" + spike.getColor() + "'. " + amount * 100 + "% of the graph is contained in this spike.");
                }
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "Exception", "component", e.getMessage());
        }

    }

    private Pair<Spike, Long> findSpike(Histogram histogram) {
        long total = 0;
        Spike highest = null;
        long[] values = histogram.values();
        for (int i = 0; i < values.length; i++) {
            long value = values[i];
            Spike spike = new Spike(i, value);
            if (highest == null || highest.compareTo(spike) < 0) {
                highest = spike;
            }
            total += value;
        }
        return new Pair<>(highest, total);
    }

    private String getComponent() {
        return null;
    }

    /**
     * Note: this class has a natural ordering that is inconsistent with equals
     */
    private class Spike implements Comparable<Spike> {

        private int color;
        private long value;

        private Spike(int color, long value) {
            this.color = color;
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public long getValue() {
            return value;
        }

        @Override
        public int compareTo(Spike that) {
            return new Long(getValue()).compareTo(that.getValue());
        }
    }
}
