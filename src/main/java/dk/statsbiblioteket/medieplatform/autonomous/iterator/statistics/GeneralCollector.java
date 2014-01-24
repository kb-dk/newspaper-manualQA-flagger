package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.util.Map;
import java.util.TreeMap;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;

public abstract class GeneralCollector {
    private final String name;
    private final StatisticWriter writer;
    private final GeneralCollector parent;
    private final Statistics statistics;

    public GeneralCollector(String name, GeneralCollector parent, StatisticWriter writer) {
        this.name = name;
        this.writer = writer;
        this.parent = parent;
        statistics = new Statistics();
        writer.addNode(getType(), getSimpleName(name));
    }

    /** Sink node constructor */
    public GeneralCollector(String name, GeneralCollector parent) {
        this.name = name;
        this.parent = parent;
        writer = null;
        statistics = new Statistics();
    }

    public String getName() {
        return name;
    }

    public abstract String getType();

    protected StatisticWriter getWriter() {
        return writer;
    }

    private void writeStatistics() {
        for (Map.Entry<String, Long> measurement : statistics.countMap.entrySet()) {
            getWriter().addStatistic(measurement.getKey(), measurement.getValue());
        }

        for (Map.Entry<String, RelativeCount> measurement : statistics.relativeCountMap.entrySet()) {
            getWriter().addStatistic(measurement.getKey(), measurement.getValue());
        }
    }

    public abstract GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event);

    public GeneralCollector handleNodeEnd(NodeEndParsingEvent event) {
        if (event.getName().equals(name)) {
            writeStatistics();
            if (parent != null) { // Root collector
                parent.getStatistics().addStatistic(statistics);
            }
            getWriter().endNode();
            return parent;
        } else throw new RuntimeException("Unexpected " + event);
    }

    public abstract void handleAttribute(AttributeParsingEvent event);

    protected String getSimpleName(String absoluteName) {
        return absoluteName.substring(absoluteName.lastIndexOf('/') + 1);
    }

    protected Statistics getStatistics() {
        return statistics;
    }

    protected class Statistics {
        protected Map<String, Long> countMap = new TreeMap();
        protected Map<String, RelativeCount> relativeCountMap = new TreeMap();

        public void addCount(String name, long countToAdd) {
            long currentCount = 0;
            if (countMap.containsKey(name)) {
                currentCount = countMap.get(name);
            }
            countMap.put(name, currentCount + countToAdd);
        }

        public void addRelative(String name, RelativeCount countToAdd) {
            RelativeCount currentCount;
            if (relativeCountMap.containsKey(name)) {
                currentCount = relativeCountMap.get(name);
                relativeCountMap.put(name,
                        new RelativeCount(currentCount.count + countToAdd.count, currentCount.total + countToAdd.total));
            } else {
                relativeCountMap.put(name, countToAdd);
            }
        }

        public void addStatistic(Statistics statistics) {
            for (Map.Entry<String, Long> measurement : statistics.countMap.entrySet()) {
                addCount(measurement.getKey(), measurement.getValue());
            }
            for (Map.Entry<String, RelativeCount> measurement : statistics.relativeCountMap.entrySet()) {
                addRelative(measurement.getKey(), measurement.getValue());
            }
        }
    }


    protected class RelativeCount {
        final float count;
        final float total;

        public RelativeCount(float count, float total) {
            this.count = count;
            this.total = total;
        }

        @Override
        public String toString() {
            if (count > 0 && total > 0) return Float.toString(count/total);
            else return "";
        }
    }
}
