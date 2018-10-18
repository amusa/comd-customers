/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comd.customer.lib.v1.response;

import java.util.List;

/**
 *
 * @author maliska
 */
public class CustomerListResponse {

    public CustomerListResponse() {
    }

    public CustomerListResponse(List<Customer> customerList) {
        this.customerList = customerList;
    }

    private List<Customer> customerList;

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

}
