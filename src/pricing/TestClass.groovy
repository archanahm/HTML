package com.kuoni.qa.automation.test.pricing

import com.kuoni.qa.automation.util.CoherenceAPIUtil
import com.kuoni.qa.automation.util.PriceSearchQueryBuilder

/**
 * Created by Joseph Sebastian on 07/10/2015.
 */
class TestClass {

    static PriceSearchQueryBuilder queryBuilder;

    public static void main(String[] args) {
        queryBuilder = new PriceSearchQueryBuilder()
        printQuery()
//        cohLoginTest()
        queryCoh()
    }

    static void printQuery() {
        println queryBuilder.buildJson()
    }

    static void cohLoginTest() {
        println(CoherenceAPIUtil.getJSESSIONID())
        println(CoherenceAPIUtil.getJSESSIONID())
        println(CoherenceAPIUtil.getJSESSIONID())
    }

    static void queryCoh() {
        String query = "{\"searchType\":\"PAX\",\"effectiveDate\":\"1/10/2015\",\"checkIn\":\"1/10/2015\",\"checkOut\":\"2/10/2015\",\"country\":\"GB\",\"currency\":\"GBP\",\"language\":\"en\",\"siteId\":1,\"clientId\":1,\"chargeHours\":null,\"includeCancellationConditions\":true,\"includePriceBreakdown\":true,\"includeLog\":true,\"ignoreAvailability\":false,\"roomRequirements\":[{\"requestIndex\":0,\"occupancy\":2}],\"propertyIds\":[\"17037\"]}"
        CoherenceAPIUtil.search(query)
    }
}
