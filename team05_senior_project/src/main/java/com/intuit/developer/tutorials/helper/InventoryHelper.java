package com.intuit.developer.tutorials.helper;

import com.intuit.ipp.data.Item;
import com.intuit.ipp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryHelper {
    @Autowired
    public QBOServiceHelper helper;

    public List<Item> getAllInventory(String realmId, String accessToken) throws Exception {
        DataService ds = helper.getDataService(realmId, accessToken);

        List<Item> items = (List<Item>) ds.executeQuery("select * from Item").getEntities();
        return items;
    }
}