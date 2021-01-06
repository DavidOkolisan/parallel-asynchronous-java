package com.parajava.completablefuture;

import com.parajava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.parajava.util.CommonUtil.delay;
import static com.parajava.util.LoggerUtil.log;

/**
 * CompletableFuture simple example
 * and example with thenApplu and lambda method references
 */
public class CompletableFutureHelloWorld {

    public static void main(String[] args) {
        HelloWorldService hws = new HelloWorldService();

        // Simplest example using completable future
//        CompletableFuture.supplyAsync(()->hws.helloWorld())
//            .thenAccept((result)->{
//                log("Completable future result is: " + result);
//            })
//            ; //.join();

        //Example using thenApply() and lambda method references
        helloWorld(hws);

        log("DONE!");

        // HelloWorld method has delay of 1sec if don't add delay here
        // we won't see result log, only DONE message
        // the second option is using join() method to block main thread
        delay(2000);
    }

    public static CompletableFuture<String> helloWorld(HelloWorldService hws) {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
                // since we have test for this method we don't call
                // thenAcept here, rather in test itself
//                .thenAccept((result)->{
//                    log("Completable future result is: " + result);
//                })
                ; //.join();
    }

    public static CompletableFuture<String> helloWorldWithSize(HelloWorldService hws) {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(result-> {
                    return result.length() + " - " + result.toUpperCase();
                })
                // since we have test for this method we don't call
                // thenAcept here, rather in test itself
//                .thenAccept((result)->{
//                    log("Completable future result is: " + result);
//                })
                ; //.join();
    }

}
