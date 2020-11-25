package com.parajava.service;

import com.parajava.domain.Review;

import static com.parajava.util.CommonUtil.delay;

public class ReviewService {

    public Review retrieveReviews(String productId) {
        delay(1000);
        return new Review(200, 4.5);
    }
}
