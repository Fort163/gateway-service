package com.quick.recording.gateway.service.filter;

import com.quick.recording.gateway.service.util.Constant;
import com.quick.recording.gateway.service.util.ServerWebExchangeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {

    private final ServerWebExchangeUtil serverWebExchangeUtil;
    private final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Autowired
    public TrackingFilter(ServerWebExchangeUtil serverWebExchangeUtil) {
        this.serverWebExchangeUtil = serverWebExchangeUtil;

    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> strings = request.getHeaders().get(Constant.TRACK_ID_HEADER_NAME);
        String trackId;
        if(Objects.isNull(strings) || strings.isEmpty()){
            trackId = UUID.randomUUID().toString();
            exchange = serverWebExchangeUtil.addHeader(
                    exchange,
                    Constant.TRACK_ID_HEADER_NAME,
                    trackId);
            logger.info("Create trackId - {}", trackId);
        }
        else {
            trackId = strings.get(0);
        }
        logger.info("Request with path {} , have trackId - {}",request.getURI(),trackId);
        return chain.filter(exchange);
    }

}
