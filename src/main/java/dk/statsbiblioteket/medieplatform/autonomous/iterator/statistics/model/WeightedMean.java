package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.model;

import java.math.BigDecimal;

/**
 * Defines a weighted count.
 *
 * @see Statistics#addRelative(String, WeightedMean)
 */
public class WeightedMean extends Number {
    public static final int OUTPUT_NUMBER_OF_DECIMALS = 1;
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
                count + weightedMean2Add.count, total + weightedMean2Add.total
        );
    }

    @Override
    public String toString() {
        if (total > 0) {
            BigDecimal bd = new BigDecimal(Double.toString(count/total));
            bd = bd.setScale(OUTPUT_NUMBER_OF_DECIMALS, BigDecimal.ROUND_HALF_UP);
            return bd.toString();
        }
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
