package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DoubleUtil {

    /**
     * 将数据保留两位小数
     */
    public static String getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String s = dFormat.format(num);
        return '.' == s.charAt(0) ? "0" + s : s;
    }

    public static String getTwoDecimal(float num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String s = dFormat.format(num);
        return '.' == s.charAt(0) ? "0" + s : s;
    }

    /**
     * 将Float保留两位小数
     * @param ft
     * @return
     */
    public static float getTwoDecimalFloat(float ft) {
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd = new BigDecimal((double) ft);
        bd = bd.setScale(scale, roundingMode);
        return Float.parseFloat(getTwoDecimal(bd.floatValue()));
    }

}
