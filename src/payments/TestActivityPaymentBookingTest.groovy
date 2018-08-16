package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.hotel.ItineraryPageBooking
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.test.search.booking.activity.ActivityBaseSpec
import spock.lang.Shared

/**
 * Created by mtmaku on 8/7/2017.
 */
class TestActivityPaymentBookingTest extends ActivityBaseSpec {
    String activityName = ""
    String price = ""
    @Shared
    ClientData client = new ClientData("prePaidClient")
    @Shared
    ActivitiesSearchToolData activitiesSearchToolData = new ActivitiesSearchToolData("SanityTestDataCity1")
    @Shared
    PaymentsTestResultData resultData1 = new PaymentsTestResultData("sanityPayment", defaultData)
    @Shared
    CitySearchData citySearchData = new CitySearchData("sanityData")
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData(citySearchData)

    def "TC00: Launch the URL and Search For SightSeeing , addfirst Available Item Book and Cancel "() {
        given: "The agent logged in"
        loginToApplicaiton(client)
        when: "Agent search for on Activity in City"
        searchForActivity(activitiesSearchToolData)
        then: "activites PLP Page displayed"
        addFirstAvailableItem(activitiesSearchToolData)
        //println("itemName"+itemName)
        //println("price"+itemPrice[0].toString())
        when: "user clicks on book an item"
        resultData.activity.searchResults.itemName = itemName
        resultData.activity.searchResults.price = itemPrice[0].toString()
        bookItem(citySearchData,0,resultData)
        then:"Terms and conditions page displayed"
        when: "Click on confirm booking button."
        paymentPage_payNow(defaultData, resultData1)
        sleep(1000)
        then: "Payment page displayed."
        paymentSummeryBlock(resultData1,1,1)
        cardDetails(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData1,0)
        sleep(2000)
        when: "user clicks on payNow button through saved addr option"
        at PaymentsPage
        SelectSavedAddress(defaultData.input.addrType1.toString())
        makePaymentButton(resultData1)
        sleep(6000)
        at ItineraryTravllerDetailsPage
        waitUntillConfrimOverlayAppears()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitForAjaxIconToDisappear()
        sleep(1000)
        //at PaymentsPage
        //paymentStatus(resultData1,0)
        at ItineraryTravllerDetailsPage
        resultData.activity.itineraryPage.bookingStatus = getBookingStatus(0)
        resultData.activity.itineraryPage.afterBookingPrice = getItenaryPriceInBookedItem(1)
        then: "user should be able to book item successufully and payment taken"

    }

    def "TC01: verifyPriceInPaymentPage"(){
        given: "user is in payment page."
        softAssert.assertTrue(resultData1.hotel.paymentpage.totalPay.toString().trim().startsWith(resultData1.hotel.paymentpage.priceInPaymentBlock.toString().trim()),"total pay in payment summery is not matching with seure payment.")
    }

    def "TC:02 verifyHotelBookedItemInItinerary"(){
        given: "User is on Itinerary page."
        // itemName is Activity Name in Activity search Results page
        assertionEquals(resultData.activity.searchResults.itemName.toString(),resultData.hotel.itineraryPage.itemName.toString(),"HotelName not matching with PLP.")
        //Item price in activity search results page
        softAssert.assertTrue(resultData.hotel.itineraryPage.price.toString().contains(resultData.activity.searchResults.price.toString()),"Price not matching with PLP" )
        assertionEquals( resultData.activity.itineraryPage.bookingStatus,"Confirmed","Hotel item not booked.")
        assertionEquals(resultData.hotel.itineraryPage.price, resultData.activity.itineraryPage.afterBookingPrice,"Prices are not equal before booking and after booking.")
    }
   /* def "TC:03 verifyPaymentStatus"(){
        given: "user is on itinerary page"
        verifyPaymentStatus(resultData1)

    }
*/
}