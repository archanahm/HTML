package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelBookingData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.helper.SearchHotelPricePaxHelper
import com.kuoni.qa.automation.helper.SearchItemInformationNewHelper
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.util.ConfigProperties
import com.kuoni.qa.automation.util.TestConf
import groovy.xml.XmlUtil
import org.testng.asserts.SoftAssert
import spock.lang.Shared


/**
 * Created by mtmaku on 8/23/2017.
 */
class HotelMultiRoomTest extends HotelMultiRoomBookingBaseSpec {

    static final String SHEET_NAME = "xmle2e_SearchItemInfo_New"
    static final String EXCEL_SHEET = new File(ConfigProperties.getValue("excelSheet"))
    static final String URL = ConfigProperties.getValue("AuthenticationURL")
    static SearchItemInformationNewHelper helper = new SearchItemInformationNewHelper(EXCEL_SHEET)
    static final String SITEID = ConfigProperties.getValue("SiteId")
    static final String FITSiteIdHeaderKey = ConfigProperties.getValue("FITSiteIdHeaderKey")
    SearchHotelPricePaxHelper excelMap = new SearchHotelPricePaxHelper(EXCEL_SHEET)
    int rowNum
    static String sheet, testDesc
    SoftAssert softAssert = new SoftAssert()
    private static def xmlParser = new XmlParser()

    static final String SHEETNAME_994 = "xmle2e_SearchHotelPricePax_New"


    @Shared
    //Client Login
    ClientData clientData = new ClientData("client664")
    @Shared
    CitySearchData hotelData = new CitySearchData("Monarch Skyline -SITUJ20XMLAuto", "booking", TestConf.booking)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData(hotelData)

    def "Agent POS FreeCancellation-within-MINPayDay Book-SecurePayment"() {
        given: "user logeed in"
        //resultMap.put(hotelData.dataName, resultData)
        login(clientData)
        searchHotel(hotelData, resultData)


        def fileContents = helper.getInputXml(SHEET_NAME, rowNum)
        println "Test Running...${testDesc}"
        println "Request Xml...\n${fileContents}"
        Node node = helper.postHttpsXMLRequestWithHeaderNode(URL, 'application/xml', fileContents, 'SearchItemInfo', FITSiteIdHeaderKey, SITEID)
        String responseBody = XmlUtil.serialize(node)
        println "Response Xml...\n${responseBody}"



        def fileContent = excelMap.getCancellationDeadlineHoursXml(SHEETNAME_994, 53)
        println "Request Xml...\n${fileContent}"
        Node node1 = excelMap.postHttpsXMLRequestWithHeaderNode(URL, 'application/xml', fileContent, 'SearchExcludeChargeableItemsNewTest', FITSiteIdHeaderKey, SITEID)
        String response = XmlUtil.serialize(node1)
        println "Response Xml Hotel PDP...\n${response}"

        List list = storeRoomNamesResponseXml(node1)
        println "room names:" + list

        storeHotelDetails(node, node1, 1, 0, resultData)
        addFirstAvailableRoomItemToItinerary(resultData)
        at ItineraryTravllerDetailsPage
        enterTravelerDetails(hotelData, 0, resultData)
        resultData.hotel.itineraryPage.travellerDetails = getTravellers()
        resultData.hotel.itineraryPage.hotelItem = storeItemDetails(0, 1, "hotel")

        at ItineraryTravllerDetailsPage

        clickOnBookIcon()
        waitTillLoadingIconDisappears()
        resultData.hotel.itineraryOverlay.hotelName = getHotelNameInAboutToBookScrn()
        resultData.hotel.itineraryOverlay.price = getPriceInAbouttoBookScrn(0)
        bookMultiRoomItem()
        clickConfirmBookingPayNow()
        waitUntillConfrimOverlayAppears()
        resultData.hotel.itineraryOverlay.bookedOverlay = isBookingConfirmationPageDisplayed()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitTillLoadingIconDisappears()

        if (getBookingStatus(0).equals("Pending")) {
            driver.navigate().refresh()
            sleep(3000)
            waitForAjaxIconToDisappear()
        }
        resultData.hotel.itineraryPage.bookedItem = storeItemDetails(0, 0, "hotel")
        resultData.hotel.itineraryPage.itemSectionAmendStatus = getTabTxtInBookedItemsSection(0,0)
        resultData.hotel.itineraryPage.itemCancelStatus = getTabTxtInBookedItemsSection(0,1)
        where:

        rowNum | testDesc
        6      | "Item Level with Item Type=Hotel, Item Destination=city + ItemCode and with SYNCHRONOUS request "
        // 53 | "Verify <ExcludeChargeableItems>/Hours - <CancellationDeadlineHours>48</CancellationDeadlineHours>"*/
    }


    def "TC01 verify Hotel Information"() {
        given: "User is on Hotel PDP page."
        verifyHotelDeatails(resultData)
    }

    def "TC02: verify Facilities List in PDP page"() {
        assertionEquals(resultData.hotel.searchResults.hotelInfo.hotelFacilityIcons.sort().get(0), resultData.hotel.searchResults.facilities.sort().get(2), "Baby sitting not matching.")
        assertionEquals(resultData.hotel.searchResults.hotelInfo.hotelFacilityIcons.sort().get(1), resultData.hotel.searchResults.facilities.sort().get(3), "Business center not matching")
        assertionEquals(resultData.hotel.searchResults.hotelInfo.hotelFacilityIcons.sort().get(2), resultData.hotel.searchResults.facilities.sort().get(6), "Disabled Facilities not matching.")
    }

    def "TC03: verfify room details of PDP against Itinerary page"() {
        given: "user is on Itinerary page."
        //Hotel name
        assertionEquals(resultData.hotel.searchResults.hotelInfo.hotelName, resultData.hotel.itineraryPage.hotelItem.expectedHotelName, "HotelName not matching as expected.")
        //Price & Currency
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice.toString().contains(resultData.hotel.searchResults.price.toString()), "Price And Currency Text Actual And Expected Don't Match")

        //Cancellation Text
        assertionEquals(resultData.hotel.searchResults.cancellationLnkTxt, resultData.hotel.itineraryPage.hotelItem.expectedItemCancellationLinkText, "Cancellation link did not match.")
    }

    def "TC04: verify added traveler details"() {
        given: "user is on itinerary page"

        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(0), hotelData.input.title_txt + " " + hotelData.input.firstName + " " + hotelData.input.lastName, "traveler 1 is not saved")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.get(1).toString().contains(hotelData.input.telephone_Num), "Phone number not saved.")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(2), hotelData.input.emailAddr, "email not mataching.")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(3).toString(), hotelData.input.title_txt + " " + hotelData.input.travellerfirstName + " " + hotelData.input.travellerlastName.toString(), "traveler two not displayed")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(4).toString(), hotelData.input.title_txt + " " + hotelData.input.travellerfirstName + " " + hotelData.input.travellerlastName.toString(), "traveler three not entered")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(5).toString(), hotelData.input.title_txt + " " + hotelData.input.travellerfirstName + " " + hotelData.input.travellerlastName.toString(), "traveler 4 not saved")

    }

    def "TC05: verify bookAnItem"() {
        given: "user is on about to book screen"
        assertionEquals(resultData.hotel.itineraryOverlay.hotelName, resultData.hotel.itineraryPage.hotelItem.expectedHotelName, "HotelName not matching as expected.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice, resultData.hotel.itineraryOverlay.price, "price in about to book screen is not matching against hotel item in itinerary page")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.bookedOverlay, "booked overlay did not display")
    }

    def "TC06: verifyItineraryPageAfterPayment"() {
        given: "user is on itinerary page"
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedItemStatus,hotelData.expected.bookingConfirmedStatus,"Confirmed status not displayed.")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedHotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"HotelName not matching after payment.")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedStarRating,resultData.hotel.itineraryPage.hotelItem.expectedStarRating,"Star Rating not matching after payment.")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedItemDesc,resultData.hotel.itineraryPage.hotelItem.expectedItemDesc,"Desc not matching")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem.expectedItemDuration.toString().contains(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration),"Duration is not matching after payment.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem.expectedItemDuration.toString().startsWith(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration),"date not matching after payment.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,resultData.hotel.itineraryPage.bookedItem.expectedItemPrice,"price is not matching after payment.")
        softAssert.assertNotNull(resultData.hotel.itineraryPage.itemSectionAmendStatus,"amend button not displayed")
        softAssert.assertNotNull( resultData.hotel.itineraryPage.itemCancelStatus,"cancel button not displayed")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem.expectedItemVoucher,"voucher not displayed")

    }
}





