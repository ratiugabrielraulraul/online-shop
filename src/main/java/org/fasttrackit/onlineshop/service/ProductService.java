package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.expection.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.product.GetProductRequest;
import org.fasttrackit.onlineshop.transfer.product.ProductResponse;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service

public class ProductService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    //IoC -Inversion of Control
    private final ProductRepository productRepository;

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
    @Transactional
    public Page<ProductResponse> getProducts(GetProductRequest request, Pageable pageable) {
        LOGGER.info("Retrieving products {}", request);

       Page<Product> products;

        // != diferit de null
        if (request != null && request.getPartialName() != null && request.getMinimumQuantity() != null) {
            products=productRepository.findByNameContainingAndQuantityGreaterThanEqual(request.getPartialName(), request.getMinimumQuantity(), pageable);

        } else if (request != null && request.getPartialName() != null) {
            products= productRepository.findByNameContaining(request.getPartialName(), pageable);
        } else {
            products= productRepository.findAll(pageable);
        }

        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products.getContent()) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setPrice(product.getPrice());
            productResponse.setDescription(product.getDescription());
            productResponse.setQuantity(product.getQuantity());
            productResponse.setImagePath(product.getImagePath());

            productResponses.add(productResponse);

        }
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    public Product updateProduct(long id, SaveProductRequest request) {
        LOGGER.info("Updating product {}: {}", id, request);
        Product product = getProduct(id);
        //BeanUtils pt copierea proprietatilor dintr-o parte in a alta
        BeanUtils.copyProperties(request, product);
        //save face update la id, daca nu este id il creeaza
        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        LOGGER.info("Deleting product {}", id);
        productRepository.deleteById(id);
    }


}
