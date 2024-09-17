package com.dev.book_service.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Tracer tracer = GlobalOpenTelemetry.getTracer("my-application");
                Span currentSpan = tracer.spanBuilder("feign-request").startSpan();
                try {
                    SpanContext spanContext = currentSpan.getSpanContext();
                    requestTemplate.header("traceparent", "00-" + spanContext.getTraceId() + "-" + spanContext.getSpanId() + "-01");
                } finally {
                    currentSpan.end();
                }
            }
        };
    }
}


