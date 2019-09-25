package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Customer;
import org.fasttrackit.onlineshop.expection.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.CustomerRepository;

import org.fasttrackit.onlineshop.transfer.customer.SaveCustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public class CustomerService {

    private static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    //IoC -Inversion of Control
    private final CustomerRepository customerRepository;

    //dependency injection
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(SaveCustomerRequest request) {
        LOGGER.info("Creating customer: {}", request);

        Customer customer = new Customer();
       customer.setFirstName(request.getFirstName());
       customer.setLastName(request.getLastName());
        return customerRepository.save(customer);


    }

    public Customer getCustomer(long id) {
        LOGGER.info("Retrieving customer {}", id);
        //lambda expressions
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer" + id + "not found."));


    }






    public Customer updateCustomer(long id, SaveCustomerRequest request) {
        LOGGER.info("Updating customer {}: {}", id, request);
        Customer customer = getCustomer(id);
        //BeanUtils pt copierea proprietatilor dintr-o parte in a alta
        BeanUtils.copyProperties(request, customer);
        //save face update la id, daca nu este id il creeaza
        return customerRepository.save(customer);
    }




}
