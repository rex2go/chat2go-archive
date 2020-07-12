package eu.rex2go.chat2go.util;

import java.util.Random;

public class MathUtil {

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static int getSeconds(String timeString) {
        int seconds = 0;
        String mode = "s";
        String store = "";

        for (int i = timeString.length() - 1; i >= 0; i--) {
            char c = timeString.charAt(i);

            if (Character.isDigit(c)) {
                store = c + store;
            } else {
                if (!store.equalsIgnoreCase("")) {
                    if (mode.equalsIgnoreCase("s")) {
                        seconds += Integer.parseInt(store);
                    } else if (mode.equalsIgnoreCase("m")) {
                        seconds += Integer.parseInt(store) * 60;
                    } else if (mode.equalsIgnoreCase("h")) {
                        seconds += Integer.parseInt(store) * 60 * 60;
                    } else {
                        seconds += Integer.parseInt(store) * 60 * 60 * 24;
                    }
                    store = "";
                }

                if (c == 's' || c == 'S') {
                    mode = "s";
                } else if (c == 'm' || c == 'M') {
                    mode = "m";
                } else if (c == 'h' || c == 'H') {
                    mode = "h";
                } else if (c == 'd' || c == 'D') {
                    mode = "d";
                }
            }
        }

        if (!store.equalsIgnoreCase("")) {
            if (mode.equalsIgnoreCase("s")) {
                seconds += Integer.parseInt(store);
            } else if (mode.equalsIgnoreCase("m")) {
                seconds += Integer.parseInt(store) * 60;
            } else if (mode.equalsIgnoreCase("h")) {
                seconds += Integer.parseInt(store) * 60 * 60;
            } else {
                seconds += Integer.parseInt(store) * 60 * 60 * 24;
            }
        }

        return seconds;
    }

}
