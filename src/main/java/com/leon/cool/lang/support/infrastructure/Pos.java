package com.leon.cool.lang.support.infrastructure;

/**
 * Copyright leon
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-11-1
 */
public class Pos {
    public final int column;
    public final int row;

    public static final Pos constPos = new Pos(-1, -1);

    public Pos(int column, int row) {
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
