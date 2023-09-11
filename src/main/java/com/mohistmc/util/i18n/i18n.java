package com.mohistmc.util.i18n;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import com.mohistmc.configuration.MohistConfigUtil;

public class i18n {


    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new UTF8Control());

    public static String get(String key) {
        return rb.getString(key);
    }

    public static String get(String key, Object... f) {
        return new MessageFormat(get(key)).format(f);
    }

    public static String getLocale(int key) {
        File f = new File("mohist-config", "mohist.yml");
        String locale = MohistConfigUtil.getString(f, "lang:", "xx");
        if (locale.length() == 5) {
            if (key == 1) return locale.substring(0, 2);
            if (key == 2) return locale.substring(3, 5);
        }
        return "xx";
    }

    public static String getLanguage() {
        return getLocale(1);
    }

    public static String getCountry() {
        return getLocale(2);
    }

    public static String getLocale() {
        return rb.getLocale().toString();
    }

    public static boolean isLang(String lang) {
        return Locale.getDefault().getCountry().equals(lang) || isTimezone("Asia/Shanghai") || rb.getLocale().getCountry().equals(lang);
    }

    public static boolean isTimezone(String timezone) {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getID().equals(timezone);
    }
}
