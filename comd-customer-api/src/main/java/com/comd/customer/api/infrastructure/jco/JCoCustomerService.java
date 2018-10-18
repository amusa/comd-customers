/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comd.customer.api.infrastructure.jco;

import com.comd.customer.api.services.CustomerService;
import com.comd.customer.lib.v1.response.Address;
import com.comd.customer.lib.v1.response.Customer;
import com.comd.customer.lib.v1.response.CustomerListResponse;
import com.comd.customer.lib.v1.response.CustomerResponse;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author maliska
 */
@ApplicationScoped
public class JCoCustomerService implements CustomerService {

    private static final Logger logger = Logger.getLogger(JCoCustomerService.class.getName());

    @Inject
    @ConfigProperty(name = "SAP_RFC_DESTINATION")
    private String sapRfcDestination;

    @Inject
    @ConfigProperty(name = "JCO_ASHOST")
    private String jcoHost;

    @Inject
    @ConfigProperty(name = "JCO_SYSNR")
    private String jcoSysNr;

    @Inject
    @ConfigProperty(name = "JCO_CLIENT")
    private String jcoClient;

    @Inject
    @ConfigProperty(name = "JCO_USER")
    private String jcoUser;

    @Inject
    @ConfigProperty(name = "JCO_PASSWD")
    private String jcoPassword;

    @Inject
    @ConfigProperty(name = "JCO_LANG")
    private String jcoLang;

    @Inject
    @ConfigProperty(name = "JCO_POOL_CAPACITY")
    private String jcoPoolCapacity;

    @Inject
    @ConfigProperty(name = "JCO_PEAK_LIMIT")
    private String jcoPeakLimit;

    @PostConstruct
    public void init() {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, jcoHost);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, jcoSysNr);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, jcoClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, jcoUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, jcoPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, jcoLang);
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, jcoPoolCapacity);
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, jcoPeakLimit);
        createDestinationDataFile(sapRfcDestination, connectProperties);

        logger.log(Level.INFO, "Service initialized... {0}", connectProperties);
    }

    private void createDestinationDataFile(String destinationName, Properties connectProperties) {
        File destCfg = new File(destinationName + ".jcoDestination");

        try {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "SAP jco destination config");
            fos.close();
        } catch (IOException e) {
            logger.log(Level.INFO, "Unable to create the destination files");
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }

    @Override
    public CustomerResponse customer(String customerNumber) throws JCoException {

        JCoDestination destination = JCoDestinationManager.getDestination(sapRfcDestination);
        JCoFunction function = destination.getRepository().getFunction("BAPI_CUSTOMER_GETDETAIL2");
        if (function == null) {
            throw new RuntimeException("BAPI_CUSTOMER_GETDETAIL2 not found in SAP.");
        }

        function.getImportParameterList().setValue("CUSTOMERNO", customerNumber);

        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
            return null; //TODO:fix
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");

//        if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
//            throw new RuntimeException(returnStructure.getString("MESSAGE"));
//        }
        JCoStructure customerStructure = function.getExportParameterList().getStructure("CUSTOMERADDRESS");

        Customer customer = new Customer();
        customer.setNumber(customerStructure.getString("CUSTOMER"));
        customer.setName(customerStructure.getString("NAME"));

        Address address = new Address(
                customerStructure.getString("STREET"),
                customerStructure.getString("CITY"),
                customerStructure.getString("REGION"),
                customerStructure.getString("COUNTRY")
        );
        customer.setAddress(address);

        return new CustomerResponse(customer);
    }

    @Override
    public CustomerListResponse customers() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(sapRfcDestination);
        JCoFunction getCustomerListFunction = destination.getRepository().getFunction("ZCUSTOMER_GETLIST");
        if (getCustomerListFunction == null) {
            throw new RuntimeException("ZCUSTOMER_GETLIST not found in SAP.");
        }

        getCustomerListFunction.getImportParameterList().setValue("CUSTOMER_TYPE", "C");

        try {
            getCustomerListFunction.execute(destination);
        } catch (AbapException e) {
            logger.log(Level.INFO, "--- Error occurred ---\n{0}", e.getMessage());
            throw new RuntimeException(e);
        }

        JCoTable customerDataTable = getCustomerListFunction.getTableParameterList().getTable("CUSTOMERDATA");

        List<Customer> customerList = new ArrayList<>();

        for (int i = 0; i < customerDataTable.getNumRows(); i++, customerDataTable.nextRow()) {
            Customer customer = new Customer();
            customer.setNumber(customerDataTable.getString("CUSTOMER"));
            customer.setName(customerDataTable.getString("NAME"));

            Address address = new Address(
                    customerDataTable.getString("STREET"),
                    customerDataTable.getString("CITY"),
                    customerDataTable.getString("REGION"),
                    customerDataTable.getString("COUNTRY")
            );
            customer.setAddress(address);

            customerList.add(customer);
        }

        return new CustomerListResponse(customerList);
    }

}
