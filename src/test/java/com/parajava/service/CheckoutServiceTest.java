package com.parajava.service;

import com.parajava.domain.checkout.Cart;
import com.parajava.domain.checkout.CheckoutResponse;
import com.parajava.domain.checkout.CheckoutStatus;
import com.parajava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @Test
    void numberOfCores() {
        System.out.println("no of cores: " + Runtime.getRuntime().availableProcessors());
    }

    @Test
    void parallelism() {
        // Fork-join example
        System.out.println("Fork-join parallelism: " + ForkJoinPool.getCommonPoolParallelism());
    }

    @Test
    void checkout6Items() {
        //given
        Cart cart= DataSet.createCart(6);

        //when
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        //then
        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout13Items() {
        //given
        Cart cart= DataSet.createCart( 13 );

        //when
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        //then
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout25Items() {
        //given
        Cart cart= DataSet.createCart( 25 );

        //when
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        //then
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void test100ItemInParallel() {
        // Test custom parallelism (be sure that checkout methodu includes parallelstream API)
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");
        //Results
        // with default parallelism: 7599
        // with custom parallelism(100): 613

        //given
        Cart cart= DataSet.createCart( 100 );

        //when
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        //then
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }
}