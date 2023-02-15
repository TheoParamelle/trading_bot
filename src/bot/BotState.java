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

package bot;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

import data.Candle;
import data.Chart;

/**
 * bot.BotState - Created on 15-2-18
 *
 * Stores the complete state of the bot as it currently is,
 * Contains settings given by the engine, but also stuff that changes
 * each update, such as the new candles and the stacks.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotState {

    // Constants (from settings)
    private int MAX_TIMEBANK;
    private int TIME_PER_MOVE;
    private int CANDLE_INTERVAL;
    private int CANDLES_TOTAL;
    private int CANDLES_GIVEN;
    private int INITIAL_STACK;
    private String[] CANDLE_FORMAT;
    private double TRANSACTION_FEE;

    // Variables (from updates)
    private int timebank;
    private Date date;
    private Date startDate;
    private HashMap<String, Double> stacks;
    private HashMap<String, Chart> charts;

    BotState() {
        this.stacks = new HashMap<>();
        this.charts = new HashMap<>();
    }

    public void updateChart(String pair, String candleString) {
        if (!this.charts.containsKey(pair)) {
            this.charts.put(pair, new Chart());
        }

        Candle candle = new Candle(this.CANDLE_FORMAT, candleString);
        this.charts.get(pair).addCandle(candle);
    }

    public void updateStack(String symbol, double amount) {
        this.stacks.put(symbol, amount);
    }

    public void setTimebank(int value) {
        this.timebank = value;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStartDate(Date startDate) {
        if (this.startDate != null) {
            return;
        }
        this.startDate = startDate;
        this.startDate.setTime(this.startDate.getTime() - Duration.ofDays(25).toMillis());
    }

    public void setMaxTimebank(int value) {
        this.MAX_TIMEBANK = value;
    }

    public void setTimePerMove(int value) {
        this.TIME_PER_MOVE = value;
    }

    public void setCandleInterval(int value) {
        this.CANDLE_INTERVAL = value;
    }

    public void setCandlesTotal(int value) {
        this.CANDLES_TOTAL = value;
    }

    public void setCandlesGiven(int value) {
        this.CANDLES_GIVEN = value;
    }

    public void setInitialStack(int value) {
        this.INITIAL_STACK = value;
    }

    public void setCandleFormat(String[] value) {
        this.CANDLE_FORMAT = value;
    }

    public void setTransactionFee(double value) {
        this.TRANSACTION_FEE = value;
    }

    public HashMap<String, Chart> getCharts() {
        return this.charts;
    }

    public HashMap<String, Double> getStacks() {
        return this.stacks;
    }

    public int getTimebank() {
        return this.timebank;
    }

    public Date getDate() {
        return this.date;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public int getMaxTimebank() {
        return this.MAX_TIMEBANK;
    }

    public int getTimePerMove() {
        return this.TIME_PER_MOVE;
    }

    public int getCandleInterval() {
        return this.CANDLE_INTERVAL;
    }

    public int getCandlesTotal() {
        return this.CANDLES_TOTAL;
    }

    public int getCandlesGiven() {
        return this.CANDLES_GIVEN;
    }

    public int getInitialStack() {
        return this.INITIAL_STACK;
    }

    public String[] getCandleFormat() {
        return this.CANDLE_FORMAT;
    }

    public double getTransactionFee() {
        return this.TRANSACTION_FEE;
    }
}
