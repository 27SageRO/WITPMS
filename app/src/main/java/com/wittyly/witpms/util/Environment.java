package com.wittyly.witpms.util;

public class Environment {

    public static final int CLIENT_ID = 2;
    public static final String CLIENT_SECRET = "Y75v5jxzRO8Byd1HE0PEZqUJyix8WXk8C1Zm8S0P";
    public static final String GRANT_TYPE = "password";

    // public static final String  API_BASE_URL        = "http://mobile.wittyly.com/";
    public static final String  API_BASE_URL        = "http://192.168.0.123:8000";
    public static final String  WITTYLY_BASE_URL    = "http://21pos.wittyly.com/";
    public static final String  ATTACHMENT_URL      = WITTYLY_BASE_URL
            + "application/modules/ticket/public/attachments/"; // + {ticker_id}/{ticket_log_id}

}