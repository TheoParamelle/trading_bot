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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import data.Candle;
import indicators.IndicatorEMA;
import indicators.IndicatorRSI;
import move.Move;
import move.MoveType;
import move.Order;

/**
 * bot.BotStarter - Created on 15-2-18
 * <p>
 * Magic happens here. You should edit this file, or more specifically
 * the doMove() method to make your bot do more than random moves.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotStarter {

    /**
     * Simple Moving Average Crossover
     *
     * @param state The current state of the game
     * @return A Move object
     */
    Move makeTrades(BotState state) {
        double threshold = 0.01;  // 1% threshold for buying or selling
        return this.simpleMovingAverageCrossover(state, threshold);

//        String symbol = "ETH";
//        return BuyAndHODL(state, symbol);
    }

    /**
     * Buys given symbol at the first request and holds it.
     *
     * @param state  Current state of the bot.
     * @param symbol Symbol to buy.
     * @return A move for this round, might be no move at all.
     */
    private Move buyAndHold(BotState state, String symbol) {
        ArrayList<Order> orders = new ArrayList<>();

        state.getCharts().forEach((pair, chart) -> {
            if (!pair.equals("USDT_" + symbol)) {
                return;
            }

            double dollars = state.getStacks().get("USDT");
            double price = chart.getCandleAt(state.getDate()).getClose();
            double amount = this.getAmount(MoveType.BUY, pair, state.getStacks(), price);

            if (amount > 0 && dollars > 0) {
                orders.add(new Order(MoveType.BUY, pair, amount));
            }
        });

        return new Move(orders);
    }

    /**
     * This is an example trading strategy that works decently well:
     * the Simple Moving Average Crossover. We compare a leading and lagging SMA for
     * all the available charts and buy if above, and sell if below given treshold.
     *
     * @param state     Current state of the bot.
     * @param threshold Threshold for a buy or sell order.
     * @return A move for this round, might be no move at all.
     */
    private Move simpleMovingAverageCrossover(BotState state, double threshold) {
        ArrayList<Order> orders = new ArrayList<>();

        state.getCharts().forEach((pair, chart) -> {
            double SMA2 = chart.SMA(2, state.getDate(), state.getCandleInterval());
            double SMA10 = chart.SMA(10, state.getDate(), state.getCandleInterval());
            Candle candle = chart.getCandleAt(state.getDate());
            double price = candle.getClose();

            double diff = (SMA2 - SMA10) / price;  // get relative difference

            Date date = new Date(state.getDate().getTime() * 1000);
            state.setStartDate(date);

            List<Double> closingPrices = chart.getCandles().values().stream()
                    .map(Candle::getClose)
                    .collect(Collectors.toList()
                    );

            IndicatorRSI indicatorRSI = new IndicatorRSI(closingPrices, 14);
            IndicatorEMA indicatorEMA = new IndicatorEMA(closingPrices);

            long elapsedTime = date.getTime() - state.getStartDate().getTime();
            int elapsedDays = (int) (elapsedTime / 1000 / 60 / 60 / 24);

            double ema12 = indicatorEMA.getEma12(elapsedDays);
            double ema26 = indicatorEMA.getEma26(elapsedDays);
            double macd = ema12 - ema26;

            if (macd >= 0) {
                MoveType moveType = MoveType.BUY;
                double amount = this.getAmount(moveType, pair, state.getStacks(), price) * 0.25D;

                if (amount <= 0) {
                    return;
                }

                orders.add(new Order(moveType, pair, amount));
                return;
            }

            MoveType moveType;
            int check = indicatorRSI.check(price);
            if ((moveType = this.useRSI(check)) == null) {
                if (diff > threshold
                        || ((date.getDay() == 4 || date.getDay() == 1) && date.getHours() <= 8 && date.getHours() >= 5)
                        && date.getDate() <= 10) {  // Buy if above treshold
                    moveType = MoveType.BUY;
                } else if (diff < -threshold
                        && date.getDate() > 10 || ((date.getDay() == 4 || date.getDay() == 1) && date.getHours() >= 21)) {  // Sell if below treshold (diff < -0.01)
                    moveType = MoveType.SELL;
                } else {
                    return;
                }
            }

            // Get half of current stack
            double amount = this.getMultiplier(check).apply(this.getAmount(moveType, pair, state.getStacks(), price));

            if (amount <= 0) {
                return;
            }

            orders.add(new Order(moveType, pair, amount));
        });

        return new Move(orders);
    }

    private Function<Double, Double> getMultiplier(int check) {
        // All
        if (check == -2 || check == 2) {
            return integer -> integer * 1.0D;
        }

        // A half
        if (check == 1 || check == -1) {
            return integer -> integer * 0.5D;
        }

        // Normal
        return integer -> integer * 0.2D;
    }

    private MoveType useRSI(int check) {
        // Buy
        if (check == 2 || check == 1) {
            return MoveType.BUY;
        }

        // Sell
        if (check == -2 || check == -1) {
            return MoveType.SELL;
        }
        return null;
    }

    /**
     * Gets the correct amount from the bot's stack for an order
     *
     * @param type   Buy or Sell.
     * @param pair   Pair that that the order will be on.
     * @param stacks The bot's stacks.
     * @param price  Price for the given pair.
     * @return An amount from the bot's stack
     */
    private double getAmount(MoveType type, String pair, HashMap<String, Double> stacks, double price) {
        String[] split = pair.split("_");

        double amount;
        if (type == MoveType.SELL) {
            amount = stacks.get(split[1]);  // Second part of pair for selling
        } else {
            amount = stacks.get(split[0]) / price;  // First part of pair for buying
        }

        if (amount < 0.00001) {  // We don't want to order on very small amounts.
            return 0;
        }

        return amount;
    }

    public static void main(String[] args) {
        BotParser parser = new BotParser(new BotStarter());
        parser.run();
    }
}
