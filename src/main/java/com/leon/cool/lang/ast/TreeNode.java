package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Dumpable;
import com.leon.cool.lang.tree.TreeElement;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.util.Pos;

/**
 * Copyright leon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-8
 */
public abstract class TreeNode implements Dumpable, TreeElement {
    public Type typeInfo;
    public Pos starPos;
    public Pos endPos;

    @Override
    public void dump() {
        System.out.println(this.toString());
    }
}

