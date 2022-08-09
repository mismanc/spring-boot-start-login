package com.misman.start.error;

import com.misman.start.util.Messages;

import java.util.Locale;

public class BadRequestException extends RuntimeException {

    private final ErrorCode errorCode;
    private Object[] args;
    private final Locale locale;

    private final String DEFAULT_EN = "en-US";

    public BadRequestException() {
        this.errorCode = ErrorCode.UNDEFINED;
        this.args = new Object[]{getMessage()};
        this.locale = new Locale("tr");
    }

    public BadRequestException(ErrorCode errorCode, String langKey, Object... args) {
        String[] langAndCountry = langKey.split("-");
        this.locale = langAndCountry.length > 1 ? new Locale(langAndCountry[0], langAndCountry[1]) : new Locale(langAndCountry[0]);
        this.errorCode = errorCode;
        this.args = args;
    }

    public BadRequestException(ErrorCode errorCode) {
        String[] langAndCountry = DEFAULT_EN.split("-");
        this.locale = langAndCountry.length > 1 ? new Locale(langAndCountry[0], langAndCountry[1]) : new Locale(langAndCountry[0]);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public BadRequestException args(Object... args) {
        this.args = args;
        return this;
    }


    @Override
    public String getLocalizedMessage() {
        return Messages.getMessageForLocale(errorCode, locale);
    }

}
