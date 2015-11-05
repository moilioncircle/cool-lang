package com.leon.cool.lang.object;

import com.leon.cool.lang.support._;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by leon on 15-10-21.
 */
public class CoolIO extends CoolObject {
    public CoolIO() {
        this.type = t.objectType("IO");
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
            _.error("error read input");
        }
        return new CoolString();
    }

    public CoolInt in_int() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String str = reader.readLine();
            return new CoolInt(Integer.parseInt(str));
        } catch (Exception e) {
            _.error("error read input");
        }
        return new CoolInt();
    }

    public CoolIO copy() {
        return new CoolIO();
    }
}
