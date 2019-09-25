package org.fasttrackit.onlineshop;


import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.expection.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceIntegrationTests {
    @Autowired
    private ProductService productService;

    @Test
    public void testCreateProduct_whenValidRequest_thenReturnCreatedProduct() {
            createProduct();

    }



    @Test(expected = TransactionSystemException.class)
    public void testCreateProduct_whenInvalidRequest_thenThrowException() {
        SaveProductRequest request = new SaveProductRequest();
        //we're not setting any values on the request
        // because we want to send an invalid request

        productService.createProduct(request);

    }
    @Test
    public void testGetProduct_whenExistingEntity_thenReturnProduct() {
        Product createdProduct = createProduct();

        Product retrievedProduct = productService.getProduct(createdProduct.getId());

        assertThat(retrievedProduct,notNullValue());
        assertThat(retrievedProduct.getId(),is(createdProduct.getId()));
        assertThat(retrievedProduct.getName(),is(createdProduct.getName()));



    }
    @Test(expected = ResourceNotFoundException.class)
    public void testGetProduct_whenNonExistingEntity_thenThrowNotFoundException() {
        productService.getProduct(999999L);
    }
    @Test
    public void testUpdateProduct_whenValidRequest_thenReturnUpdatedProduct() {
        Product createdProduct = createProduct();
        SaveProductRequest request = new SaveProductRequest();
        request.setName(createdProduct.getName()+ "Updated");
        request.setPrice(createdProduct.getPrice()+ 10);
        request.setQuantity(createdProduct.getQuantity()+10);

        Product updatedProduct = productService.updateProduct(createdProduct.getId(), request);

        assertThat(updatedProduct, notNullValue());
        assertThat(updatedProduct.getId(),is(updatedProduct));
        assertThat(updatedProduct.getName(),is(updatedProduct));
        assertThat(updatedProduct.getPrice(),is (updatedProduct));
        assertThat(updatedProduct.getQuantity(),is (updatedProduct));
        assertThat(updatedProduct.getDescription(),is (updatedProduct));


    }


    private Product createProduct() {
        SaveProductRequest request = new SaveProductRequest();
        request.setName("Computer");
        request.setDescription("Some description");
        request.setPrice(2000);
        request.setQuantity(100);

        Product product = productService.createProduct(request);

        assertThat(product, notNullValue());
        assertThat(product.getId(),notNullValue());
        assertThat(product.getId(),greaterThan((0L)));
        assertThat(product.getName(), is(request.getName()));
        assertThat(product.getDescription(), is(request.getDescription()));
        assertThat(product.getQuantity(), is(request.getQuantity()));
        assertThat(product.getPrice(), is(request.getPrice()));
        return product;
    }



}
