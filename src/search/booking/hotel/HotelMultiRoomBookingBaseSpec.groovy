package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelBookingData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.hotel.AgentHotelInfoPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 8/23/2017.
 */
class HotelMultiRoomBookingBaseSpec extends SanityHotelBookingBaseSpec {
    int noOfRoomNames;
    int noOfCategoryRooms;

    def searchHotel(CitySearchData data, HotelTransferTestResultData resultData) {
        at HotelSearchResultsPage
        enterHotelDestination(data.input.property.toString())
        selectFromAutoSuggest(data.input.propertySuggest.toString())
        // Below method to select dates and rooms
        searchHotelCommonData(data)
        resultData.hotel.searchResults.hotelsResultStatus = searchResultsReturned()
    }
    def addFirstAvailableRoomItemToItinerary(HotelTransferTestResultData resultData){
        at AgentHotelInfoPage
        for(int i=0;i<getTotalNumOfRooms();i++){
            if(getRoomStatus(i).equals("Available")){
                resultData.hotel.searchResults.cancellationLnkTxt = getCancellationLinkText(i,0)
                resultData.hotel.searchResults.price = getPrice(i+1,0)

                //store price and item name
                clickAddToitinerary()
                break;
            }
        }
    }
    // To get child node results from service call based on parent and child arg nodes
    public List storeResponseXml(Node node, String parent, String child) {

        List list = []

        def isErrorNode = node.depthFirst().findAll {
            it.name() == "Errors"
        }
        if (isErrorNode) {
            println "\nError Response ... "
        }
        def isResponseNode = node.depthFirst().findAll {
            it.name() == "ResponseDetails"
        }
        if (isResponseNode) {
            def itemDetails = node.ResponseDetails.SearchItemInformationResponse.ItemDetails
            def reportsSize = itemDetails.ItemDetail.HotelInformation.Reports.Report.size()
            if (reportsSize == 0) {
                println "\nEmpty Response..."
            } else {
                List<Node> nodes = itemDetails.ItemDetail.HotelInformation.findAll { ele -> return ele }
                println(nodes.get(0).get(parent).getAt(child)[0].text())

                for (Node node1 : nodes.get(0).get(parent).getAt(child)) {
                    list.add(node1.text())
                }
            }

        }
        return list
    }
    public List storeResponsReortHeadersseXml(Node node) {
        def hotelName
        List list = []

        def isErrorNode = node.depthFirst().findAll {
            it.name() == "Errors"
        }
        if (isErrorNode) {
            println "\nError Response ... "
        }
        def isResponseNode = node.depthFirst().findAll {
            it.name() == "ResponseDetails"
        }
        if (isResponseNode) {
            def itemDetails = node.ResponseDetails.SearchItemInformationResponse.ItemDetails
            def reportsSize = itemDetails.ItemDetail.HotelInformation.Reports.Report.size()
            if (reportsSize == 0) {
                println "\nEmpty Response..."
            } else {
                hotelName = itemDetails.ItemDetail.Item.text()
                for(int i=0;i<reportsSize;i++){
                   list.add(itemDetails.ItemDetail.HotelInformation.Reports.Report[i].@Type.toString().toLowerCase())
                }
                }
            list.add(hotelName)
        }
        return list
    }
    public String storeHotelNameResponseXml(Node node) {
        def hotelName

        def isResponseNode = node.depthFirst().findAll {
            it.name() == "ResponseDetails"
        }
        if (isResponseNode) {
            def itemDetails = node.ResponseDetails.SearchItemInformationResponse.ItemDetails
            def reportsSize = itemDetails.ItemDetail.HotelInformation.Reports.Report.size()
            if (reportsSize == 0) {
                println "\nEmpty Response..."
            } else {
                hotelName = itemDetails.ItemDetail.Item.text()
            }
        }
        return hotelName
    }
    public List storeRoomNamesResponseXml(Node node){

        List list = []
        List rooms = []
        String description
        def isErrorNode = node.depthFirst().findAll {
            it.name() == "Errors"
        }
        if (isErrorNode) {
            println "\nError Response ... "
        }
        def isResponseNode = node.depthFirst().findAll {
            it.name() == "ResponseDetails"
        }
        if (isResponseNode) {
            def itemDetails = node.ResponseDetails.SearchHotelPricePaxResponse.HotelDetails
            def reportsSize = itemDetails.Hotel.PaxRoomSearchResults.PaxRoom.RoomCategories.RoomCategory.size()
            for(int i=0;i<reportsSize;i++){
                list.add(itemDetails.Hotel.PaxRoomSearchResults.PaxRoom.RoomCategories.RoomCategory.Description[i].text())
            }

        }
        else{
            println("error response")
        }
        for(int i=0;i<list.size();i++){
            if(!rooms.contains(list[i]))
                rooms.add(list[i])
        }
        return rooms
    }
    def storeHotelDetails(Node node,Node node1,int categoryNum,int  roomNum, HotelTransferTestResultData resultData ){
        at AgentHotelInfoPage
        resultData.hotel.searchResults.hotelInfo = storeHotelInfo(0)
        resultData.hotel.searchResults.reports = storeResponseXml(node,"Reports","Report")
        resultData.hotel.searchResults.facilities = storeResponseXml(node,"Facilities","Facility")
        resultData.hotel.searchResults.reportHeaders = storeResponsReortHeadersseXml(node)
        resultData.hotel.searchResults.hotelName = storeHotelNameResponseXml(node)
        List list = storeRoomNamesResponseXml(node1)
        resultData.hotel.searchResults.expectedRoomNames = list
        resultData.hotel.searchResults.actualRoomNames = getRoomNames()
        recommandedRoomsBlock(resultData)
        resultData.hotel.searchResults.roomDetails = storeRoomDetails(categoryNum,roomNum)

    }
    def recommandedRoomsBlock(HotelTransferTestResultData resultData){
        at AgentHotelInfoPage
        resultData.hotel.searchResults.recommandedRoomExpandStatus = isRoomExpanded(0)
        resultData.hotel.searchResults.recommandedRoomCount = recommendedRoomCount()
        }

    def protected verifyHotelDeatails(HotelTransferTestResultData resultData){
        String hotelName =   resultData.hotel.searchResults.hotelName.toString()
        List data = hotelName.tokenize(" ")
        String hotel = data.getAt(0)+" "+data.getAt(1)
        softAssert.assertTrue(resultData.hotel.searchResults.hotelInfo.hotelName.toString().toUpperCase().startsWith(hotel),"Hotel name not matching.")
        assertList(resultData.hotel.searchResults.hotelInfo.reportHeaders.sort(),resultData.hotel.searchResults.reportHeaders.sort(),"Report headers are not matching.")
        assertList(resultData.hotel.searchResults.hotelInfo.reportsTxt.sort(), resultData.hotel.searchResults.reports.sort(),"reports are not matching.")
        // Facilites list validation included in specific test class ex: HotelMultiRoomTest.

        softAssert.assertTrue(resultData.hotel.searchResults.recommandedRoomExpandStatus,"Bydefault Recommanded room not expanded.")
        softAssert.assertTrue( resultData.hotel.searchResults.recommandedRoomCount.toString().toInteger()>=0,"Rooms not displayed in Recommanded section")
        assertList( resultData.hotel.searchResults.expectedRoomNames.sort(),resultData.hotel.searchResults.actualRoomNames.sort(),"RoomNames not matching against service res")
        at AgentHotelInfoPage
        int size = getNumberOfRoomNames()
        for(int i=1;i<size;i++){
            int roomSize = getNumberOfRoomsPerCategory(i)
            for(int j=0;j<roomSize;j++){
                softAssert.assertNotNull(getToolTipForPax(i,j),"Pax tooltip not displayed for room:"+j+"categoryNum:"+i+"\n")
                softAssert.assertNotNull(getNumberOfPax(i,j),"Pax not displayed for room"+j+"categoryNum:"+i+"\n")
                softAssert.assertNotNull(getCancellationLinkText(i,j),"item cancellation link not displayed:"+j+"categoryNum:"+i+"\n")
                softAssert.assertNotNull(getPrice(i+1,j),"price not displayed:"+j+"categoryNum:"+i+"\n")
                softAssert.assertNotNull(QuantityCount(i+1,j),"Quantity dropdown not displayed"+j+"categoryNum:"+i+"\n")
                softAssert.assertNotNull(getRoomStatus(i,j),"Room status not displayed:"+j+"categoryNum:"+i+"\n")
                softAssert.assertNotNull(getCommission(i,j),"commssion not displayed:"+j+"categoryNum:"+i+"\n")
                softAssert.assertTrue(getToggleStatus(i,j),"Toggle button by default not collapsed:"+j+"categoryNum:"+i+"\n")
                clickMoreRoomInfoButton(i+1,j)
                sleep(1000)
                softAssert.assertTrue(verifyPriceBreakdownTableDisplayed(i+1,j),"Price Break down table not displayed."+j+"categoryNum:"+i+"\n")
                softAssert.assertTrue(getItineraryButtonStatus(i,j),"Add to itinerary button not displayed."+j+"categoryNum:"+i+"\n")
            }
        }

    }



}
