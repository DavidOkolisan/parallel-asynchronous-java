package com.parajava.completablefuture;

import com.parajava.domain.Product;
import com.parajava.service.InventoryService;
import com.parajava.service.ProductInfoService;
import com.parajava.service.ReviewService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceWithCompletableFutureTest {
    private ProductInfoService pis = new ProductInfoService();
    private ReviewService rs = new ReviewService();
    private InventoryService is = new InventoryService();
    private ProductServiceWithCompletableFuture pscf = new ProductServiceWithCompletableFuture(pis,rs,is);

    @Test
    void retrieveProductDetailsTest() {
        String pid = "pid123";

        Product product = pscf.retrieveProductDetails(pid);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsNonBlockingTest() {
        String pid = "pid123";
        Product product = pscf.retrieveProductDetailsNonBlocking(pid)
                            .join();

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventoryTest() {
        String pid = "pid123";

        Product product = pscf.retrieveProductDetailsWithInventory(pid);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        // Test inventory
        product.getProductInfo().getProductOptions()
                .forEach(productOption -> {
                    assertNotNull(productOption.getInventory());
                });
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventoryAndCompletableFutureTest() {
        String pid = "pid123";

        Product product = pscf.retrieveProductDetailsWithInventoryAndCompletableFuture(pid);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        // Test inventory
        product.getProductInfo().getProductOptions()
                .forEach(productOption -> {
                    assertNotNull(productOption.getInventory());
                });
        assertNotNull(product.getReview());
    }
}