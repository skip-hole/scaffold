package com.scaffold.webflux.util;

import com.scaffold.webflux.message.MessageModel;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author hui.zhang
 * @date 2022年09月01日 20:21
 */
@Component
public class MessageUtils {


    public static final String DEV_OPS_REQUEST = "DEV_OPS_REQUEST";
    public static final String DEV_OPS_RESPONSE = "DEV_OPS_RESPONSE";

    public MessageModel assembleMessage(ServerWebExchange exchange) {
        return MessageModel.builder()
                .reqBody(exchange.getAttribute(DEV_OPS_REQUEST))
                .respBody(exchange.getAttribute(DEV_OPS_RESPONSE))
                .build();


    }

    public void pushMessage(MessageModel message) {
        System.out.println("message=" + message);
    }
}
