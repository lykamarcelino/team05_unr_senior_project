package com.intuit.developer.tutorials.helper;
import com.intuit.ipp.services.QueryResult;

import com.intuit.ipp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intuit.ipp.data.CompanyInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class userHelper {
    @Autowired
    public QBOServiceHelper helper;

    public String getUserInfo(String realmId, String accessToken) throws Exception {
        //get DataService
        DataService service = helper.getDataService(realmId, accessToken);

        // get all company info
        String sql = "select * from companyinfo";
        QueryResult queryResult = service.executeQuery(sql);
        CompanyInfo companyInfo = (CompanyInfo) queryResult.getEntities().get(0);

        // process response
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(companyInfo);
    }
}