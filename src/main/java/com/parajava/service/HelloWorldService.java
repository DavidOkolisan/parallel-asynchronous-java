package com.parajava.service;

import java.util.concurrent.CompletableFuture;

import static com.parajava.util.CommonUtil.delay;
import static com.parajava.util.LoggerUtil.log;

public class HelloWorldService {

    public String helloWorld() {
        delay(1000);
        log("helloWorld() method called.");
        return "hello world";
    }

    public String hello() {
        delay(1000);
        log("hello() method called.");
        return "hello";
    }

    public String world() {
        delay(1000);
        log("world() method called.");
        return " world!";
    }

    public CompletableFuture<String> worldFuture(String input) {
        return CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return input + " world!";
        });
    }

}
