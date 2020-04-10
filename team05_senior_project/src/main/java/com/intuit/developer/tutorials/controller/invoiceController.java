package com.intuit.developer.tutorials.controller;


import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.ipp.data.Invoice;
import com.intuit.ipp.services.DataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.intuit.developer.tutorials.objects.InvoiceClass;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
public class invoiceController {

    @Autowired
    OAuth2PlatformClientFactory factory;

    @Autowired
    recordsHelper recordsH;

    @Autowired
    public QBOServiceHelper helper;

    private static final Logger logger = Logger.getLogger(invoiceController.class);

    @RequestMapping(value = "/getInvoice", method = RequestMethod.GET)
    public @ResponseBody InvoiceClass invoiceDisplay() {
        InvoiceClass newInvoice = new InvoiceClass();

        String realmId = oauthController.realmIdHolder;
        String accessToken = oauthController.accessTokenHolder;
        try {
            //get DataService
            DataService service = helper.getDataService(realmId, accessToken);
            List<Invoice> invoices = recordsH.getInvoices(service);

            List<String> namesArray = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
            List<String> description = invoices.stream().map(invoice -> invoice.getLine().get(0).getDescription()).collect(Collectors.toList());
            List<String> docNumber = invoices.stream().map(invoice -> invoice.getDocNumber()).collect(Collectors.toList());
            List<Date> txnDate = invoices.stream().map(invoice -> invoice.getTxnDate()).collect(Collectors.toList());
            List<BigDecimal> quantity = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
            List<BigDecimal> unitPrice = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

            newInvoice.setItemName(namesArray);
            newInvoice.setDescription(description);
            newInvoice.setDocNumber(docNumber);
            newInvoice.setTxnDate(txnDate);
            newInvoice.setQuantity(quantity);
            newInvoice.setUnitPrice(unitPrice);

        } catch (Exception e) {
            System.out.println("Error creating inventory response");
        }
        return newInvoice;
    }
}