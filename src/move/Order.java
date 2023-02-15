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

package move;

/**
 * move.Order - Created on 15-2-18
 *
 * Stores a single order for a certain trading pair and is able
 * to convert it to a string that is part of a Move string.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Order {

    private MoveType type;
    private String pair;
    private double amount;

    public Order(MoveType type, String pair, double amount) {
        this.type = type;
        this.pair = pair;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.type, this.pair, this.amount);
    }
}
