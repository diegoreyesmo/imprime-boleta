package diegoreyesmo.imprimeboleta;

import java.text.DecimalFormat;

public class Util {

    private static DecimalFormat decimalFormat = new DecimalFormat("$ ###,###.##");

    public static int parseInt(String value) {
        if (value == null || value.isEmpty()) return 0;
        return Integer.parseInt(value.replace("$ ", "").replace(",", "").replace(".", ""));
    }

    public static String formatInt(int value) {
        return decimalFormat.format(value);
    }
}
