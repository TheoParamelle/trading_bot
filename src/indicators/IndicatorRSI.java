package indicators;

import java.util.List;

public class IndicatorRSI {

    public static final int POSITIVE_MIN = 20; // Achète tout
    public static final int POSITIVE_MAX = 35; // Achète à moitié
    public static final int NEGATIVE_MIN = 60; // Vend à moitié
    public static final int NEGATIVE_MAX = 70; // Vend tout

    private final int period;
    private double avgUp;
    private double avgDwn;
    private double prevClose;

    public IndicatorRSI(List<Double> closingPrice, int period) {
        this.avgUp = 0;
        this.avgDwn = 0;
        this.period = period;
        this.init(closingPrice);
    }

    public void init(List<Double> closingPrices) {
        this.prevClose = closingPrices.get(0);
        for (int i = 1; i < this.period + 1; i++) {
            double change = closingPrices.get(i) - this.prevClose;
            if (change > 0) {
                this.avgUp += change;
            } else {
                this.avgDwn += Math.abs(change);
            }
        }

        //Initial SMA values
        this.avgUp = this.avgUp / (double) this.period;
        this.avgDwn = this.avgDwn / (double) this.period;

        //Dont use latest unclosed value
        for (int i = this.period + 1; i < closingPrices.size() - 1; i++) {
            this.update(closingPrices.get(i));
        }
    }

    public double get() {
        return 100 - 100.0 / (1 + this.avgUp / this.avgDwn);
    }

    public double getTemp(double newPrice) {
        double change = newPrice - this.prevClose;
        double tempUp;
        double tempDwn;
        if (change > 0) {
            tempUp = (this.avgUp * (this.period - 1) + change) / (double) this.period;
            tempDwn = (this.avgDwn * (this.period - 1)) / (double) this.period;
        } else {
            tempDwn = (this.avgDwn * (this.period - 1) + Math.abs(change)) / (double) this.period;
            tempUp = (this.avgUp * (this.period - 1)) / (double) this.period;
        }
        return 100 - 100.0 / (1 + tempUp / tempDwn);
    }

    public void update(double newPrice) {
        double change = newPrice - this.prevClose;
        if (change > 0) {
            this.avgUp = (this.avgUp * (this.period - 1) + change) / (double) this.period;
            this.avgDwn = (this.avgDwn * (this.period - 1)) / (double) this.period;
        } else {
            this.avgUp = (this.avgUp * (this.period - 1)) / (double) this.period;
            this.avgDwn = (this.avgDwn * (this.period - 1) + Math.abs(change)) / (double) this.period;
        }
        this.prevClose = newPrice;
    }

    public int check(double newPrice) {
        double temp = this.getTemp(newPrice);
        if (temp < IndicatorRSI.POSITIVE_MIN) {
            return 2;
        }
        if (temp < IndicatorRSI.POSITIVE_MAX) {
            return 1;
        }
        if (temp > IndicatorRSI.NEGATIVE_MAX) {
            return -2;
        }
        if (temp > IndicatorRSI.NEGATIVE_MIN) {
            return -1;
        }
        return 0;
    }
}
