package com.parajava.completablefuture;

import com.parajava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    CompletableFutureHelloWorld cfhw = new CompletableFutureHelloWorld();
    HelloWorldService hws = new HelloWorldService();

    @Test
    void helloWorldTest() {
        CompletableFuture<String> cf = cfhw.helloWorld(hws);

        cf.thenAccept(s -> {
            assertEquals("HELLO WORLD", s);
        })
        //join method is required in order to successfully execute this test in contrary this code won't be complete since it's completable future
        .join();
    }

    @Test
    void helloWorldWithSizeTest() {
        CompletableFuture<String> cf = cfhw.helloWorldWithSize(hws);

        cf.thenAccept(s -> {
            assertEquals("11 - HELLO WORLD", s);
        })
                //join method is required in order to successfully execute this test in contrary this code won't be complete since it's completable future
                .join();
    }
}