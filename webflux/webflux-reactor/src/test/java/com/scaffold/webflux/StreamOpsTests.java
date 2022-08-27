package com.scaffold.webflux;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hui.zhang
 * @date 2022年08月27日 19:54
 */
public class StreamOpsTests {


    @Test
    public void streamMap() {

        Flux<Integer> ints = Flux.range(1, 4);
        Flux<Integer> mapped = ints.map(i -> i * 2);
        mapped
                .log()
                .subscribe();


    }

    @Test
    public void streamFilter() {

        Flux<Integer> ints = Flux.range(1, 4);
        Flux<Integer> filtered = ints.filter(i -> i % 2 == 0);
        filtered
                .log()
                .subscribe();


    }

    @Test
    public void streamBuffer() {

        Flux<Integer> ints = Flux.range(1, 40);
        Flux<List<Integer>> buffered = ints.buffer(3);
        buffered
                .log()
                .subscribe();


    }

    @Test
    public void streamRetry() {

        Mono<String> client = Mono.fromSupplier(() -> {
            double num = Math.random();
            if (num > 0.01) {
                throw new Error("Network issue");
            }
            return "https://www.baidu.com";
        });
        client
                .log()
                .retry(3)
                .subscribe();


    }

    @Test
    public void streamRetryFlux() throws InterruptedException {

        Flux<Long> flux = Flux.generate(AtomicLong::new, (state, sink) -> {
            long i = state.getAndIncrement();
            sink.next(i);
            if (i == 10) sink.error(new RuntimeException("i don't like 10"));
            return state;
        }, (state) -> System.out.println("i'm done"));
        flux
                .log()
                .retry(1)
                .subscribe();
        Thread.sleep(10000);


    }
}
