package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

/**
 * Defines a weighted count.
 *
 * @see dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics#addRelative(String, WeightedMean)
 */
class WeightedMean extends Number {
    final double count;
    final double total;

    public WeightedMean(double count, double total) {
        this.count = count;
        this.total = total;
    }

    /**
     * Returns a new WeightedMean the sum of this WeightedMean and the supplied one.
     * @param weightedMean2Add The WeightedMean to add.
     * @return The sum.
     */
    public WeightedMean add(WeightedMean weightedMean2Add) {
        return new WeightedMean(
                count + weightedMean2Add.count, total + + weightedMean2Add.total
        );
    }

    @Override
    public String toString() {
        if (count > 0 && total > 0) return Double.toString(count/total);
        else return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightedMean)) return false;

        WeightedMean that = (WeightedMean) o;

        if (Double.compare(that.count, count) != 0) return false;
        if (Double.compare(that.total, total) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long result = (count != +0.0f ? Double.doubleToLongBits(count) : 0);
        result = 31 * result + (total != +0.0f ? Double.doubleToLongBits(total) : 0);
        return (int)result;
    }

    @Override
    public int intValue() {
        return (int)doubleValue();
    }

    @Override
    public long longValue() {
        return (long)doubleValue();
    }

    @Override
    public float floatValue() {
        return (float)doubleValue();
    }

    @Override
    public double doubleValue() {
        return count/total;
    }
}
