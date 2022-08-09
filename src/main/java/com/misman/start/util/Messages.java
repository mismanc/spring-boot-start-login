package com.misman.start.util;

import com.misman.start.error.ErrorCode;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    public static String getMessageForLocale(ErrorCode errorCode, Locale locale, Object... args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/errorMessage", locale);
        String format = resourceBundle.getString(errorCode.name());
        return MessageFormat.format(format, args);
    }

}
