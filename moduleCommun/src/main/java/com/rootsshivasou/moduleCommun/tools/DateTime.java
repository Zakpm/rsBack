package com.rootsshivasou.moduleCommun.tools;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Locale;

public class DateTime {
    final public static DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
    public static String format(Date dateToFormat) {
        return df.format(dateToFormat);
    }
}