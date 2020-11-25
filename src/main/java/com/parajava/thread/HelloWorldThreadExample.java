package com.parajava.thread;

import javax.sound.midi.Soundbank;
import java.net.StandardSocketOptions;

import static com.parajava.util.CommonUtil.delay;
import static com.parajava.util.CommonUtil.stopWatch;

public class HelloWorldThreadExample {
    private static String result = "";

    private static void hello(){
        delay(700);
        result = result.concat("Hello");
    }
    private static void world(){
        delay(600);
        result = result.concat("World");
    }

    public static void main(String[] args) throws InterruptedException {

        Thread helloThread = new Thread(()-> hello());
        Thread worldThread = new Thread(()-> world());

        //Starting the thread
        stopWatch.start();
        helloThread.start();
        worldThread.start();

        //Joining the thread (Waiting for the threads to finish)
        helloThread.join();
        worldThread.join();
        stopWatch.stop();

        System.out.println("Total time taken : " + stopWatch.getTime());
        System.out.println("Result is : " + result);

    }
}
