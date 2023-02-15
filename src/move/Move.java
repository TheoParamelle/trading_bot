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

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * move.Move - Created on 15-2-18
 *
 * Stores a Move, with 0 or multiple orders, and is able to convert it to
 * a string to send to the engine.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Move {

    private ArrayList<Order> orders;

    public Move(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        if (this.orders.isEmpty()) {
            return MoveType.PASS.toString();
        }

        return this.orders.stream().map(Order::toString).collect(Collectors.joining(";"));
    }
}
