package com.parajava.service;


import com.parajava.domain.checkout.CartItem;

import static com.parajava.util.CommonUtil.delay;
import static com.parajava.util.LoggerUtil.log;

public class PriceValidatorService {

    public boolean isCartItemInvalid(CartItem cartItem) {
        log("isCartItemInvalid(): " + cartItem);
        int cartId = cartItem.getItemId();
        delay(500);
        if (cartId == 7 || cartId == 9 || cartId == 11) {
            return true;
        }
        return false;
    }
}
