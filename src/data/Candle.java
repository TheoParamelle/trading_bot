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

/**
 * data.Candle - Created on 15-2-18
 *
 * Stores all information of a single candle in a certain chart.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Candle {

    private String pair;
    private Date date;
    private double high;
    private double low;
    private double open;
    private double close;
    private double volume;

    /**
     * Parses and initializes a candle.
     * @param format The format for a candle as given by the engine in the settings.
     * @param input The string input for this candle's data.
     */
    public Candle(String[] format, String input) {
        String[] values = input.split(",");

        for (int i = 0; i < format.length; i++) {
            String key = format[i];
            String value = values[i];

            switch (key) {
                case "pair":
                    this.pair = value;
                    break;
                case "date":
                    this.date = new Date(Long.parseLong(value));
                    break;
                case "high":
                    this.high = Double.parseDouble(value);
                    break;
                case "low":
                    this.low = Double.parseDouble(value);
                    break;
                case "open":
                    this.open = Double.parseDouble(value);
                    break;
                case "close":
                    this.close = Double.parseDouble(value);
                    break;
                case "volume":
                    this.volume = Double.parseDouble(value);
                    break;
            }
        }
    }

    public String getPair() {
        return this.pair;
    }

    public Date getDate() {
        return this.date;
    }

    public double getHigh() {
        return this.high;
    }

    public double getLow() {
        return this.low;
    }

    public double getOpen() {
        return this.open;
    }

    public double getClose() {
        return this.close;
    }

    public double getVolume() {
        return this.volume;
    }
}
