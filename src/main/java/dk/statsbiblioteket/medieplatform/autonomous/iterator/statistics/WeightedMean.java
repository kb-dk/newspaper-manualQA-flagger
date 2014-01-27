package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

/**
 * Defines a weighted count.
 *
 * @see dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics#addRelative(String, WeightedMean)
 */
class WeightedMean {
    final float count;
    final float total;

    public WeightedMean(float count, float total) {
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
        if (count > 0 && total > 0) return Float.toString(count/total);
        else return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightedMean)) return false;

        WeightedMean that = (WeightedMean) o;

        if (Float.compare(that.count, count) != 0) return false;
        if (Float.compare(that.total, total) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (count != +0.0f ? Float.floatToIntBits(count) : 0);
        result = 31 * result + (total != +0.0f ? Float.floatToIntBits(total) : 0);
        return result;
    }
}
