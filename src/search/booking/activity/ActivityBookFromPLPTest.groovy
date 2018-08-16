package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import spock.lang.IgnoreIf
import spock.lang.Shared

/**
 * Created by ArchanaHM on 03/01/2018.
 */

class ActivityBookFromPLPTest extends ActivityBookBaseSpec{


    @Shared
    ClientData client = new ClientData("client697")
    @Shared
    ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData("TSCitySearch")
    @Shared
    public static Map<String,HotelTransferTestResultData> resultMap = [:]
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData(activitiesSearchToolData)


    def "TC0 Activity Booking Functionality  "() {
        resultMap.put(activitiesSearchToolData.itemName,resultData)
        login(client)
        given: " Nova Home Page displayed"
        searchForActvity(activitiesSearchToolData,resultData)
        when: "search results returned."
        and : "user add first avilable item to itinerary"
        getFirstAvailableItemPLP(activitiesSearchToolData,resultData)
        getDeatilsOfItemMoreLangugae(activitiesSearchToolData,resultData)
        getDetailsFromCancellationOverlay(activitiesSearchToolData,resultData)
        navigateToPDPPage(activitiesSearchToolData,resultData)
        getDetailsDiffItemsPDP(activitiesSearchToolData,resultData)
        then: "Activity item should book successfully."

    }


    def "TC1: verify Activity Tab "() {
        given: " Activity Tab is displayed"
        verifyActivityTabDefaultValues(activitiesSearchToolData,resultData)
    }

/*    def "TC1: verify Details of Activity in PLP Page  "() {
        given: " Activity PLP Page is displayed"
        verifyActivityPLPPageDetails(activitiesSearchToolData,resultData)
    }*/

    def "TC2: verify details in PDP page"(){
        given: "Activity PDP Page displayed"
        VerifyActivityDetailsPDP(activitiesSearchToolData,resultData)
    }
}
