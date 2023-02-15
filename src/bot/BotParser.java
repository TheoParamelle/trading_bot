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

import java.util.Date;
import java.util.Scanner;

import move.Move;

/**
 * bot.BotParser - Created on 15-2-18
 *
 * Class that will keep reading output from the engine.
 * Will either update the bot state or get actions.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotParser {

    private Scanner scanner;
    private BotStarter bot;
    private BotState currentState;

    BotParser(BotStarter bot) {
        this.scanner = new Scanner(System.in);
        this.bot = bot;
        this.currentState = new BotState();
    }

    /**
     * Keeps consuming all input over the stdin channel
     */
    public void run() {
        while (this.scanner.hasNextLine()) {
            String line = this.scanner.nextLine();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(" ");
            switch (parts[0]) {
                case "settings":
                    this.parseSettings(parts[1], parts[2]);
                    break;
                case "update":
                    if ("game".equals(parts[1])) {
                        this.parseGameData(parts[2], parts[3]);
                    }
                    break;
                case "action":
                    this.currentState.setTimebank(Integer.parseInt(parts[2]));

                    Move move = this.bot.makeTrades(this.currentState);

                    System.out.println(move.toString());
                    break;
                default:
                    System.err.println("Unknown command");
            }
        }
    }

    /**
     * Parses all the game settings given by the engine
     * @param key Type of setting given
     * @param value Value
     */
    private void parseSettings(String key, String value) {
        try {
            switch (key) {
                case "timebank":
                    int time = Integer.parseInt(value);
                    this.currentState.setMaxTimebank(time);
                    this.currentState.setTimebank(time);
                    break;
                case "time_per_move":
                    this.currentState.setTimePerMove(Integer.parseInt(value));
                    break;
                case "player_names":
                case "your_bot":
                    // We don't need to store this
                    break;
                case "candle_interval":
                    this.currentState.setCandleInterval(Integer.parseInt(value));
                    break;
                case "candle_format":
                    this.currentState.setCandleFormat(value.split(","));
                    break;
                case "candles_total":
                    this.currentState.setCandlesTotal(Integer.parseInt(value));
                    break;
                case "candles_given":
                    this.currentState.setCandlesGiven(Integer.parseInt(value));
                    break;
                case "initial_stack":
                    this.currentState.setInitialStack(Integer.parseInt(value));
                    break;
                case "transaction_fee_percent":
                    this.currentState.setTransactionFee(Double.parseDouble(value));
                    break;
                default:
                    System.err.println(String.format(
                            "Cannot parse settings input with key '%s'", key));
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Cannot parse settings value '%s' for key '%s'", value, key));
            e.printStackTrace();
        }
    }

    /**
     * Parse data about the game given by the engine
     * @param key Type of game data given
     * @param value Value
     */
    private void parseGameData(String key, String value) {
        try {
            switch (key) {
                case "next_candles": // Updates both the date and the charts
                    String[] chartStrings = value.split(";");
                    String dateString = chartStrings[0].split(",")[1]; // get date from first candle

                    this.currentState.setDate(new Date(Long.parseLong(dateString)));

                    for (String candleString : chartStrings) {
                        String[] split = candleString.trim().split(",");
                        this.currentState.updateChart(split[0], candleString); // split[0] is the pair
                    }
                    break;
                case "stacks":
                    for (String stack : value.split(",")) {
                        String[] split = stack.trim().split(":");
                        this.currentState.updateStack(split[0], Double.parseDouble(split[1]));
                    }
                    break;
                default:
                    System.err.println(String.format(
                            "Cannot parse game data input with key '%s'", key));
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Cannot parse game data value '%s' for key '%s'", value, key));
            e.printStackTrace();
        }
    }
}
