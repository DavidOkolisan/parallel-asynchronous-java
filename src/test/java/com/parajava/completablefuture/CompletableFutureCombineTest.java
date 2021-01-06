package com.parajava.completablefuture;

import com.parajava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.parajava.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureCombineTest {

    CompletableFutureCombine cfhw = new CompletableFutureCombine();
    HelloWorldService hws = new HelloWorldService();

    @Test
    void helloWorldSyncVersion() {
        String result = cfhw.helloWorldSyncVersion();

        assertEquals("hello world!", result);
    }

    @Test
    void helloWorldAsyncVersion() {
        String result = cfhw.helloWorldAsyncVersion();

        assertEquals("HELLO WORLD!", result);

    }


    @Test
    void helloWorldAsyncVersionWith3Calls() {
        String result = cfhw.helloWorldAsyncVersionWith3Calls();

        assertEquals("HELLO WORLD! + THIRD ASYNC CALL", result);
    }

    @Test
    void helloWorldAsyncVersionWith4Calls() {
        String result = cfhw.helloWorldAsyncVersionWith4Calls();

        assertEquals("HELLO WORLD! + THIRD ASYNC CALL + FOURTH ASYNC CALL!", result);
    }

    @Test
    void helloWorldWithThenCompose() {
        CompletableFuture<String> cf = cfhw.helloWorldWithThenCompose();

        cf
            .thenAccept(s -> {
                assertEquals("hello world!", s);
            })
            .join();
    }

    @Test
    void helloWorldAsyncVersionWithCustomThreadPool() {
        String result = cfhw.helloWorldAsyncVersionWithCustomThreadPool();
        assertEquals("HELLO WORLD! + THIRD ASYNC CALL", result);
    }

    @Test
    void helloWorldAsyncVersionWithLogAsync() {
        String result = cfhw.helloWorldAsyncVersionWithLogAsync();
        assertEquals("HELLO WORLD! + THIRD ASYNC CALL", result);
    }
}