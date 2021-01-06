package com.parajava.completablefuture;

import com.parajava.domain.*;
import com.parajava.service.InventoryService;
import com.parajava.service.ProductInfoService;
import com.parajava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.parajava.util.CommonUtil.stopWatch;
import static com.parajava.util.LoggerUtil.log;

public class ProductServiceWithCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;
    private InventoryService inventoryService;

    public ProductServiceWithCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceWithCompletableFuture(ProductInfoService productInfoService,
                                               ReviewService reviewService,
                                               InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    // Example for client usage when response is required
    public Product retrieveProductDetails(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfo =
                CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> review =
                CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfo
                .thenCombine(review, (productInfo1, review1) -> new Product(productId, productInfo1, review1))
                .join();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    // Example for server usage in order to avoid blocking client
    public CompletableFuture<Product> retrieveProductDetailsNonBlocking(String productId) {

        CompletableFuture<ProductInfo> productInfo =
                CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> review =
                CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        return productInfo
                .thenCombine(review, (productInfo1, review1) -> new Product(productId, productInfo1, review1));
    }

    // Example for client including inventory without completable future
    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfo =
                CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo1 -> {
                    productInfo1.setProductOptions(updateInventory(productInfo1));
                    return productInfo1;
                });
        CompletableFuture<Review> review =
                CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfo
                .thenCombine(review, (productInfo1, review1) -> new Product(productId, productInfo1, review1))
                .join();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    // Since retrieve inventory has delay, it will introduce additional delay
    // for each productOption
    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        List<ProductOption> po = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    Inventory inventory = inventoryService.retrieveInventory(productOption);
                    productOption.setInventory(inventory);
                    return productOption;
                })
                .collect(Collectors.toList());
        return po;
    }

    // Example for client including inventory without completable future
    public Product retrieveProductDetailsWithInventoryAndCompletableFuture(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfo =
                CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                        .thenApply(productInfo1 -> {
                            productInfo1.setProductOptions(updateInventoryWithCompletableFuture(productInfo1));
                            return productInfo1;
                        });
        CompletableFuture<Review> review =
                CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally((e) -> {
                    log("Handle exception in review: " + e.getMessage());
                    return Review.builder().noOfReviews(0).overallRating(0.0).build();
                });

        Product product = productInfo
                .thenCombine(review, (productInfo1, review1) -> new Product(productId, productInfo1, review1))
                .whenComplete(((product1, e) -> {
                    log("When complete called: " + product1 + " with exception - " + e);
                }))
                .join();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    // Resolving delay introduced by retrieveInventory method for each productOption
    public List<ProductOption> updateInventoryWithCompletableFuture(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> po = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    return CompletableFuture.supplyAsync(()->
                            inventoryService
                                    .retrieveInventory(productOption))
                                    .exceptionally((e) -> {
                                        log("Handle exception in inventory: " + e.getMessage());
                                        return Inventory.builder()
                                                .count(1).build();
                                    })
                                    .thenApply(inventory -> {
                                        productOption.setInventory(inventory);
                                        return productOption;
                                    });


                })
                .collect(Collectors.toList());
        return po.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceWithCompletableFuture productService = new ProductServiceWithCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
