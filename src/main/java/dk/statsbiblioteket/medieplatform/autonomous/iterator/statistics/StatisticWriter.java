package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

public interface StatisticWriter {
    void addNode(String type, String name);

    void endNode();

    void addStatistic(String name, long metric);

    void addStatistic(String name, GeneralCollector.RelativeCount metric);

    void finish();
}
