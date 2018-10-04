/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comd.customer.lib.v1.response;

/**
 *
 * @author maliska
 */
public class CustomerResponse {

    private Customer customer;

    public CustomerResponse() {
    }

    public CustomerResponse(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
