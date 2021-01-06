package com.parajava.completablefuture;

import com.parajava.domain.Product;
import com.parajava.domain.ProductInfo;
import com.parajava.domain.ProductOption;
import com.parajava.service.InventoryService;
import com.parajava.service.ProductInfoService;
import com.parajava.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceWithCompletableFutureExceptionTest {

    @Mock
    private ProductInfoService productInfoService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private InventoryService inventoryService;


    @InjectMocks
    ProductServiceWithCompletableFuture pswcf;

    @Test
    void retrieveProductDetailsWithInventoryAndCompletableFuture() {
        String productID="ABC123";
        when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
        when(reviewService.retrieveReviews(any())).thenThrow(new RuntimeException("Exception during reviews pull."));
        when(inventoryService.retrieveInventory(any())).thenCallRealMethod();

        Product product = pswcf.retrieveProductDetailsWithInventoryAndCompletableFuture(productID);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        // Test inventory
        product.getProductInfo().getProductOptions()
                .forEach(productOption -> {
                    assertNotNull(productOption.getInventory());
                });
        assertNotNull(product.getReview());
        assertEquals(0, product.getReview().getNoOfReviews());
    }

    @Test
    void retrieveProductDetailsWithInventoryAndCompletableFutureProductInfoError() {
        String productID="ABC123";
        when(productInfoService.retrieveProductInfo(any()))
                .thenThrow(new RuntimeException("Exception during reviews call."));
        when(reviewService.retrieveReviews(any())).thenCallRealMethod();

        Assertions.assertThrows(RuntimeException.class, ()->pswcf.retrieveProductDetailsWithInventoryAndCompletableFuture(productID));
    }

    @Test
    void updateInventoryWithCompletableFutureInventoryError() {
        String productID="ABC123";
        when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
        when(inventoryService.retrieveInventory(any()))
                .thenThrow(new RuntimeException("Exception during inventory call."));
        ProductInfo productInfo = productInfoService.retrieveProductInfo(productID);
        List<ProductOption> productOptions = pswcf.updateInventoryWithCompletableFuture(productInfo);

        assertNotNull(productOptions);
        assertTrue(productOptions.size()>0);
        productOptions.forEach(p -> {
            assertNotNull(p.getInventory());
            assertEquals(1, p.getInventory().getCount());
        });

    }
}