package indicators;

import java.util.List;

public class IndicatorEMA {

    private final static double EMA_12_ALPHA = 2.0D / (1.0D + 12.0D);
    private final static double EMA_26_ALPHA = 2.0D / (1.0D + 26.0D);

    private final List<Double> prices;

    public IndicatorEMA(List<Double> prices) {
        this.prices = prices;
    }

    public double getEma12(int day) {
        if (day < 11) {
            return 0.0D;
        }
        double ema12 = 0.0D;
        for (int i = day - 10; i <= day; i++) {
            ema12 = IndicatorEMA.EMA_12_ALPHA * this.prices.get(i) + (1.0D - IndicatorEMA.EMA_12_ALPHA) * ema12;
        }
        return ema12;
    }

    public double getEma26(int day) {
        if (day < 25) {
            return 0.0D;
        }
        double ema26 = 0.0D;
        for (int i = day - 24; i <= day; i++) {
            ema26 = IndicatorEMA.EMA_26_ALPHA * this.prices.get(i) + (1.0D - IndicatorEMA.EMA_26_ALPHA) * ema26;
        }
        return ema26;
    }

    public List<Double> getPrices() {
        return this.prices;
    }
}
