package com.parajava.exceptionhandling;

import com.parajava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.parajava.util.CommonUtil.*;
import static com.parajava.util.LoggerUtil.log;

public class CompletableFutureExceptionHandler {

    private HelloWorldService hws;

    public CompletableFutureExceptionHandler(HelloWorldService hws) {
        this.hws = hws;
    }

    public String helloWorldHandle() {
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> thirdAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Third async call";
        });

        String result = hello
                .handle((res, e) -> {
                    if(e != null) {
                        log("Exception after hello() method called: " + e.getMessage());
                        return "";
                    }
                    return res;
                })
                .thenCombine(world, (h,w) -> h+w)
                .handle((res, e) -> {
                    if(e != null) {
                        log("Exception after world() method called: " + e.getMessage());
                        return "";
                    }
                    return res;
                })
                .thenCombine(thirdAsyncCall, (previous, t)->previous+t)
                .thenApply(String::toUpperCase)
                .join();

        returnTime();

        return result;
    }

    public String helloWorldExceptionally() {
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> thirdAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Third async call";
        });

        String result = hello
                .exceptionally((e) -> {
                    log("Exception after hello() method called: " + e.getMessage());
                    return "";
                })
                .thenCombine(world, (h,w) -> h+w)
                .exceptionally((e) -> {
                    log("Exception after world() method called: " + e.getMessage());
                    return "";
                })
                .thenCombine(thirdAsyncCall, (previous, t)->previous+t)
                .thenApply(String::toUpperCase)
                .join();

        returnTime();

        return result;
    }



    public String helloWorldWhenComplete() {
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> thirdAsyncCall = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " + Third async call";
        });

        String result = hello
                .whenComplete((res, e) -> {
                    if(e != null) log("Exception after hello() method called: " + e.getMessage());
                })
                .thenCombine(world, (h,w) -> h+w)
                .whenComplete((res, e) -> {
                    if(e != null) log("Exception after world() method called: " + e.getMessage());
                })
                .exceptionally((e) -> {
                    // Exceptionally was added in order to recover from exception/failure
                    log("Exception after world() method called: " + e.getMessage());
                    return "";
                })
                .thenCombine(thirdAsyncCall, (previous, t)->previous+t)
                .thenApply(String::toUpperCase)
                .join();

        returnTime();

        return result;
    }


}
