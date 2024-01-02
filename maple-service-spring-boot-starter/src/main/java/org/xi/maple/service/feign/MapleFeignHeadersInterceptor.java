package org.xi.maple.service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xi.maple.common.constant.MapleHttpHeaders;

import java.util.function.Consumer;

public class MapleFeignHeadersInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        Consumer<String> consumer = s -> {
            String header = attributes.getRequest().getHeader(s);
            if (header != null) {
                requestTemplate.header(s, header);
            }
        };
        consumer.accept(MapleHttpHeaders.INSTANCE);
        consumer.accept(MapleHttpHeaders.TAG);
    }
}