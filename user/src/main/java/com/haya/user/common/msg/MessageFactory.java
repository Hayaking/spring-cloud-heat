package com.haya.user.common.msg;

public class MessageFactory {

    public static <T> Message message(boolean state, T body) {
        return Message.builder().state( true ).body( body ).build();
    }

    public static <T> Message message(boolean state, int code, T body) {
        return Message.builder().state( state ).code( code ).body( body ).build();
    }

    public static <T> Message message(boolean state) {
        return Message.builder().state( state ).build();
    }
}
