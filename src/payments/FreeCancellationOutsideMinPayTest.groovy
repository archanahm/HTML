package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 5/30/2017.
 */
class FreeCancellationOutsideMinPayTest extends PaymentSpec{
    @Shared
    //Client Login
    ClientData clientData = new ClientData("client695")
    //ClientData clientData = new ClientData("auTakeNetCredentials")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("FreeCancellationOutsideMinPay", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("FreeCancellationOutsideMinPay", paymentsData)

    def "TC00: Agent POS FreeCancellation-Outside- MinPay Book-SecurePayment"() {
        given: "user logeed in"
        resultMap.put(paymentsData.hotelName, resultData)
        login(clientData)
        searchHotelAndAddToItinerary(paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData,0)
        bookItem(resultData, 0,1)
        bookTCOverlay(paymentsData, resultData)
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryOverlay.payLaterStatus = getConfirmBookingPayLaterStatus()
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlock(resultData,2,1)
        selectPaymentType(resultData)
        cardDetails(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)
        SelectSavedAddress(defaultData.input.addrType1)
        makePaymentButton(resultData)
        waitTillLoadingIconDisappears()
        sleep(5000)
        bookingConfirmedOverlay(resultData,0)
        ItineraryPageAfterPayment(resultData,0,0)
    }
    def "TC01: verifySearchHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        verifySearchHotel(paymentsData, resultData)
    }

    def "TC02: verifyItineraryPage"() {
        given: "User is at Itinerary page"
        verifyItineraryPage(defaultData, resultData)
    }
    def "TC03: verifyBookAnItem"() {
        given: "User is on itinerary page."
        verifyBookAnItem(paymentsData, resultData, 0)
    }
    def "TC04: verifyBookTCOverlay"(){
        given: "user is on T & C Overlay"
        verifyBookTCOverlay(paymentsData,resultData)
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.payLaterStatus,"PaymentLater button is not displayed.")
    }
    def "TC05: verifyPaymentPagePayNow"(){
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }
    def "TC06: verifyPaymentSummeryBlock"(){
        given: "user is on payment page"
        verifyPaymentSummeryBlock(resultData,clientData)
    }

    def "TC07: verifyselectPaymentType"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "TC08: verfiyCardDetails"(){
        given: "user is on payment page"
        verfiyCardDetails(resultData,defaultData.expected.cardNum,defaultData.expected.securityCode)
    }
    def "TC09: verifyMakePaymentButton"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }

    def "TC10: verifyBookedConfirmedOverlay"(){
        given: "user is on booking confirmed overlay"
        verifyBookedConfirmedOverlay(paymentsData,resultData)
    }
    def "TC11: verifyItineraryPageAfterPayment"() {
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)
    }


}
