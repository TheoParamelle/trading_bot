/*
 *  Copyright 2018 riddles.io (developers@riddles.io)
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 *      For the full copyright and license information, please view the LICENSE
 *      file that was distributed with this source code.
 */

package data;

import java.util.Date;
import java.util.HashMap;

/**
 * data.Chart - Created on 15-2-18
 *
 * A Chart of a certain trading pair. Contains and manages the candles
 * that are in the chart. Additionally used for calculations on the chart.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Chart {

    // Stores candles in a Map with the candle's timestamp as key
    // for easy retrieval.
    private HashMap<Long, Candle> candles;

    public Chart() {
        this.candles = new HashMap<>();
    }

    public void addCandle(Candle candle) {
        this.candles.put(candle.getDate().getTime(), candle);
    }

    public Candle getCandleAt(Date date) {
        return this.candles.get(date.getTime());
    }

    public HashMap<Long, Candle> getCandles() {
        return this.candles;
    }

    /**
     * Calculates the Simple Moving Average (SMA) for given date.
     * @param amount Amount of candles to use in SMA, higher numbers flatten out.
     *               the chart, while lower numbers are closer to actual chart.
     * @param currentDate Date to calculate SMA for
     * @param interval Time between each candle, from settings.
     * @return SMA value.
     */
    public double SMA(int amount, Date currentDate, int interval) {
        double sum = 0;
        int count = 0;

        for (int i = 0; i < amount; i++) {
            Date date = new Date(currentDate.getTime() - (interval * i));  // Previous candle
            Candle candle = getCandleAt(date);

            if (candle != null) {
                sum += getCandleAt(date).getClose();  // Sums closing price of candle
                count++;
            }
        }

        return sum / count;
    }
}
