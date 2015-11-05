package com.leon.cool.lang.parser;

/**
 * Created by leon on 15-11-4.
 */
public class Main {
    public static void main(String[] args) {
        int i = 0;
        do {
            i++;
            System.out.println(i);
            if (i == 10) {
                continue;
            }
        } while (i < 10);
    }
}
