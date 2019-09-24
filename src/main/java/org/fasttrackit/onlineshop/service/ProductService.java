package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.expection.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ProductService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    //IoC -Inversion of Control
    private  final ProductRepository productRepository;

    //dependency injection
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(SaveProductRequest request) {
        LOGGER.info("Creating product: {}", request);

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setImagePath(request.getImagePath());

        return productRepository.save(product);





    }

    public Product getProduct(long id) {
        LOGGER.info("Retrieving product {}", id);
        //lambda expressions
       return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product" + id + "not found."));


    }
    public Product updateProduct(long id, SaveProductRequest request) {
        LOGGER.info("Updating product {}: {}", id, request);
        Product product = getProduct(id);
        //BeanUtils pt copierea proprietatilor dintr-o parte in a alta
        BeanUtils.copyProperties(request,product);
        //save face update la id, daca nu este id il creeaza
        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        LOGGER.info("Deleting product {}", id);
        productRepository.deleteById(id);
    }
}
