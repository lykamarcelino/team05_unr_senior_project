package com.intuit.developer.tutorials.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.ipp.data.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.ipp.services.DataService;

import com.intuit.developer.tutorials.objects.InventoryClass;

@CrossOrigin
		(origins = "http://localhost:3000")

@Controller
public class inventoryController {

	@Autowired
	OAuth2PlatformClientFactory factory;

	@Autowired
	recordsHelper recordsH;

	@Autowired
	public QBOServiceHelper helper;

	private static final Logger logger = Logger.getLogger(inventoryController.class);

	@RequestMapping(value = "/getInventory", method = RequestMethod.GET)
	public @ResponseBody InventoryClass inventoryDisplay() {

		InventoryClass inventory = new InventoryClass();

		String realmId = oauthController.realmIdHolder;
		String accessToken = oauthController.accessTokenHolder;

		try {
			//get DataService
			DataService service = helper.getDataService(realmId, accessToken);
			List<Item> items = recordsH.getItems(service);

			String names = new String();
			List<String> namesArray = new ArrayList<String>();
			for (Item i : items) {
				names = i.getFullyQualifiedName();
				namesArray.add(names);
			}
			inventory.setNamesArray(namesArray);

			String itemName = new String();
			List<String> itemArray = new ArrayList<String>();
			for (Item i : items) {
				itemName = i.getItemCategoryType();
				itemArray.add(itemName);
			}
			inventory.setItemsArray(itemArray);

			String desc = new String();
			List<String> descArray = new ArrayList<String>();
			for (Item i : items) {
				desc = i.getDescription();
				descArray.add(desc);
			}
			inventory.setDescArray(descArray);

			String unitPrice = new String();
			List<String> unitArray = new ArrayList<String>();
			for (Item i : items) {
				unitPrice = i.getUnitPrice().toString();
				unitArray.add(unitPrice);
			}
			inventory.setPriceArray(unitArray);

			BigDecimal qty;
			List<BigDecimal> qtyArray = new ArrayList<BigDecimal>();
			for (Item i : items) {
				qty = i.getQtyOnHand();
				qtyArray.add(qty);
			}
			inventory.setQtyArray(qtyArray);

		} catch (Exception e) {
			System.out.println("Error creating inventory response");
		}
		return inventory;
	}
}