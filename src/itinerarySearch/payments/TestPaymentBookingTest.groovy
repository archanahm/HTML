package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.test.search.booking.hotel.SanityHotelBookingBaseSpec
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 7/24/2017.
 */
class TestPaymentBookingTest extends PaymentSpec {
    @Shared
    ClientData client = new ClientData("prePaidClient")
    @Shared
    CitySearchData citySearchData = new CitySearchData("sanityData")
    @Shared
    public static Map<String,HotelTransferTestResultData> resultMap = [:]
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData(citySearchData)
    @Shared
    PaymentsTestResultData resultData1 = new PaymentsTestResultData("sanityPayment", defaultData)

    def "TC00 Snaity Hotel Booking Single Room"() {
        resultMap.put(citySearchData.city,resultData)
        given: "that the travel agent is on home page"
        when: "search for hotel"
        searchHotelSingleRoomAndAddToItinerary(citySearchData,resultData)
        then: "search results returned."
        when: "user clicks book an item"
        bookItem(citySearchData,0, resultData)
        then: "Hotel item should book successfully."
        paymentPage_payNow(defaultData, resultData1)
        sleep(1000)
        paymentSummeryBlock(resultData1,1,1)
        cardDetails(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData1,0)
        sleep(2000)
        when: "user clicks on payNow button through saved addr option"
        at PaymentsPage
        SelectSavedAddress(defaultData.input.addrType1.toString())
        makePaymentButton(resultData1)
        sleep(2000)
        at ItineraryTravllerDetailsPage
        waitUntillConfrimOverlayAppears()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitForAjaxIconToDisappear()
        sleep(1000)
        at PaymentsPage
        paymentStatus(resultData1,0)
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryPage.bookingStatus = getBookingStatus(0)
        resultData.hotel.itineraryPage.afterBookingPrice = getItenaryPriceInBookedItem(1)
        then: "user should be able to book item successufully and payment taken"

    }
    def "TC01: verifySearchHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        verifySearchHotel(resultData)
    }
    def "TC02: verifyPriceInPaymentPage"() {
        given: "user is in payment page."
        softAssert.assertTrue(resultData1.hotel.paymentpage.totalPay.toString().trim().startsWith(resultData1.hotel.paymentpage.priceInPaymentBlock.toString().trim()),"total pay in payment summery is not matching with seure payment.")
    }
    def "TC:03 verifyHotelBookedItemInItinerary"(){
        given: "User is on Itinerary page."
        assertionEquals(resultData.hotel.searchResults.hotelName,resultData.hotel.itineraryPage.itemName,"HotelName not matching with PLP.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.price.toString().contains(resultData.hotel.searchResults.price.toString()),"Price not matching with PLP" )
        assertionEquals(resultData.hotel.itineraryPage.bookingStatus,"Confirmed","Hotel item not booked.")
        assertionEquals(resultData.hotel.itineraryPage.price,resultData.hotel.itineraryPage.afterBookingPrice,"Prices are not equal before booking and after booking.")
    }
    def "TC:04 verifyPaymentStatus"(){
        given: "user is on itinerary page"

        verifyPaymentStatus(resultData1)

    }


}
