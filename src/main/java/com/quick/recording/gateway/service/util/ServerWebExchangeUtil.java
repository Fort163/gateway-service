package com.quick.recording.gateway.service.util;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class ServerWebExchangeUtil {

    public ServerWebExchange addHeader(ServerWebExchange exchange,
                                              String headerName,
                                              String... headerValue){
        return exchange.mutate().request(
                exchange.getRequest().mutate().header(headerName, headerValue).build()
        ).build();
    }

}
