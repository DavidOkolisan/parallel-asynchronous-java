package com.parajava.service;

import com.parajava.domain.checkout.Cart;
import com.parajava.domain.checkout.CartItem;
import com.parajava.domain.checkout.CheckoutResponse;
import com.parajava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.parajava.util.CommonUtil.returnTime;
import static com.parajava.util.CommonUtil.startTimer;
import static com.parajava.util.LoggerUtil.log;

public class CheckoutService {

    private PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkout(Cart cart){
        startTimer();
        List<CartItem> invalidatedList = cart
                .getCartItemList()
//                .stream()
                .parallelStream()
                .map(item -> {
                    boolean isValidPrice = priceValidatorService.isCartItemInvalid(item);
                    item.setExpired(isValidPrice);
                    return item;
                })
                .filter(CartItem::isExpired)
                .collect(Collectors.toList());
        returnTime();
        if(invalidatedList.size()>0){
            return new CheckoutResponse(CheckoutStatus.FAILURE, invalidatedList);
        }
        //double finalPrice = calculateFinalPrice(cart);
        double finalPrice = calculateFinalPrice_reduce(cart);
        log("Checkout completed and the final price is: " + finalPrice);
        return new CheckoutResponse(CheckoutStatus.SUCCESS);
    }

    private double calculateFinalPrice(Cart cart) {

        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private double calculateFinalPrice_reduce(Cart cart) {

        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .reduce(0.0, Double::sum);
    }
}
