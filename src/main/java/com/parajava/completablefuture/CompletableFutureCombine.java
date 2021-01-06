package com.parajava.completablefuture;

import com.parajava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.parajava.util.CommonUtil.*;
import static com.parajava.util.LoggerUtil.log;

/**
 * CompletableFuture example with using two async calls and combine method
 */
public class CompletableFutureCombine {

    static HelloWorldService hws = new HelloWorldService();

    public static void main(String[] args) {
        helloWorldAsyncVersion();

        log("DONE!");
    }

    public String helloWorldSyncVersion() {
        String hello = hws.hello();
        String world = hws.world();
        return hello + world;
    }

    public static String helloWorldAsyncVersion() {
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());

        // Then combine takes CompletableFuture as first parameter
        // and function as second parameter with results from previous two CompletableFutures a
        // as input parameters
        String result = hello
                .thenCombine(world, (h,w) -> h+w)
                .thenApply(String::toUpperCase)
                .join();

        returnTime();

        return result;
    }

    public static String helloWorldAsyncVersionWith3Calls() {
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());

        //Adding local third CompletableFuture
        CompletableFuture<String> thirdAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Third async call";
        });

        // Then combine takes CompletableFuture as first parameter
        // and function as second parameter with results from previous two CompletableFutures a
        // as input parameters
        String result = hello
                .thenCombine(world, (h,w) -> h+w)
                //adding additional cf
                .thenCombine(thirdAsyncCall, (previous, t)->previous+t)
                .thenApply(String::toUpperCase)
                .join();

        returnTime();

        return result;
    }

    public static String helloWorldAsyncVersionWith4Calls() {
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());

        //Adding local third CompletableFuture
        CompletableFuture<String> thirdAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Third async call";
        });

        //Adding local fourth CompletableFuture
        CompletableFuture<String> fourthAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Fourth async call!";
        });

        String result = hello
                .thenCombine(world, (h,w) -> h+w)
                .thenCombine(thirdAsyncCall, (previous, t)->previous+t)
                .thenCombine(fourthAsyncCall, (previous, f)->previous+f)
                .thenApply(String::toUpperCase)
                .join();

        returnTime();

        return result;
    }

    // Example of using thenCompose with one dependent task
    // hello method invocation in our example
    public static CompletableFuture<String> helloWorldWithThenCompose(){
        return CompletableFuture.supplyAsync(hws::hello)
                    .thenCompose(previous->hws.worldFuture(previous));
    }

    public static String helloWorldAsyncVersionWithLogAsync() {
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> thirdAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Third async call";
        });

        String result = hello
                .thenCombineAsync(world, (h,w) -> {
                    log("Then combine invoked ");
                    return h+w;
                })
                .thenCombineAsync(thirdAsyncCall, (previous, t)->{
                    log("Then combine invoked with thirdAsyncCall ");
                    return previous+t;
                })
                .thenApplyAsync(s -> {
                    log("thenApply invoked ");
                    return s.toUpperCase();
                })
                .join();

        returnTime();

        return result;
    }

    public static String helloWorldAsyncVersionWithCustomThreadPool() {
        startTimer();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world(), executorService);
        CompletableFuture<String> thirdAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Third async call";
        }, executorService);

        // Logs added in order to be able to see
        // how thread are working in APIs
        String result = hello
                .thenCombine(world, (h,w) -> {
                    log("Then combine invoked ");
                    return h+w;
                })
                .thenCombine(thirdAsyncCall, (previous, t)->{
                    log("Then combine invoked with thirdAsyncCall ");
                    return previous+t;
                })
                .thenApply(s -> {
                    log("thenApply invoked ");
                    return s.toUpperCase();
                })
                .join();

        returnTime();

        return result;
    }
}
