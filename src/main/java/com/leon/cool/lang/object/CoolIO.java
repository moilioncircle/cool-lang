package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
 * @author leon on 15-10-21
 */
public class CoolIO extends CoolObject {
    public CoolIO() {
        this.type = TypeFactory.objectType("IO");
    }

    public CoolObject out_string(CoolString x) {
        System.out.println(x);
        return this;
    }

    public CoolObject out_int(CoolInt x) {
        System.out.println(x.val);
        return this;
    }

    public CoolString in_string() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String str = reader.readLine();
            return new CoolString(str, str.length());
        } catch (Exception e) {
            Utils.error("unexpected.error");
        }
        return new CoolString();
    }

    public CoolInt in_int() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String str = reader.readLine();
            return new CoolInt(Integer.parseInt(str));
        } catch (Exception e) {
            Utils.error("unexpected.error");
        }
        return new CoolInt();
    }

    public CoolIO copy() {
        return new CoolIO();
    }
}
