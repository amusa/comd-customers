/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comd.customer.api.services;

import com.comd.customer.lib.v1.response.CustomerListResponse;
import com.comd.customer.lib.v1.response.CustomerResponse;
import com.sap.conn.jco.JCoException;

/**
 *
 * @author maliska
 */
public interface CustomerService {

    CustomerResponse customer(String customerNumber) throws JCoException;

    CustomerListResponse customers() throws JCoException;

}
