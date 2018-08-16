package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by kmahas on 7/24/2017.
 *
 * NOVA POS GC hotel - Item search/Htel/Transfer/Activity-FreeCancellation-inside-MINPayDay-PayLater-check PDP all section  -  itineary page - Book-SecurePayment -TakeNet
 *
 */
class MuModPayNowTakNetTest extends PaymentSpec {
    //Client Login
    @Shared
    ClientData clientData = new ClientData("client696")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("MuModPayNowTakeNet", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("MuModPayNowTakeNet", paymentsData)

    def "TC00: Multi Module Pay Now TakeNet"(){
        given: "User is able to login and present on Hotel tab"

        resultMap.put(paymentsData.hotelName, resultData)
        loginToApplicaiton(clientData, paymentsData, resultData)
        searchForHotelAndAddToItinerary(clientData,paymentsData, resultData)
        searchForTransferAndAddToItinerary(paymentsData, resultData)
        searchForActivityAndAddToItinerary(paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData,0)
        NonBookedItemSection(paymentsData,resultData,0,1,2)
        bookAHotel(paymentsData,resultData,0)
        BookingTAndCoverlay(paymentsData, resultData,false)
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlock(resultData,2,1)
        paymentSummaryBlockExtended(resultData,false)

        selectPaymentType(resultData)
        cardDetails(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)
        savedAddress(resultData)
        makePaymentButton(resultData)
        bookingConfirmedOverlay(resultData,0)
        ItineraryPageAfterPaymentHotelMultiModule(clientData,resultData,0,2)
        bookATransfer(paymentsData,resultData,0)
        BookingTAndCoverlayTransfersMultiModule(paymentsData, resultData)
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlockTransfers(resultData,2,1)

        selectPaymentType(resultData)
        cardDetailsTransfers(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)
        savedAddress(resultData)
        makePaymentButton(resultData)
        bookingConfirmedOverlayTransfers(paymentsData,resultData,0)
        ItineraryPageAfterPaymentTransfersMultiModule(clientData,resultData,1,2)
        bookAnActivity(paymentsData,resultData,0)
        BookingTAndCoverlaySightSeeingMultiModule(paymentsData, resultData)
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlockActivity(resultData,2,1)

        selectPaymentType(resultData)
        cardDetailsActivity(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)
        savedAddress(resultData)
        makePaymentButton(resultData)
        bookingConfirmedOverlayActivity(paymentsData,resultData,0)
        ItineraryPageAfterPaymentActivityMultiModule(clientData,resultData,0,1,2)
    }
    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(paymentsData, resultData)
    }

    def"TC02: Verify Search For Hotel Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifysearchForHotelAndAddToItinerary(paymentsData, resultData)
    }
    def"TC03: Verify Search For Transfer Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifysearchForTransferAndAddToItinerary(paymentsData, resultData)
    }
    def"TC04: Verify Search For Activity Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifysearchForActivityAndAddToItinerary(paymentsData, resultData)
    }
    def"TC05: Verify Itinerary page Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifyItineraryPage(defaultData, resultData)
    }
    def"TC06: Verify Non booked item section Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifyNonBookedItemSection(paymentsData, resultData)
    }

    def"TC07: Verify book an hotel Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifybookanhotel(paymentsData, resultData)
    }

    def"TC08: Verify Booking T & C overlay Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifyBookingTAndCoverlay(paymentsData, resultData,false)
    }
    def "TC09: Verify Payment page_PayNow Actual & Expected Results"(){
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }

    def "TC10: Verify Payment summary block Actual & Expected Results"(){
        given: "user is on payment page"
        verifyPaymentSummeryBlock(resultData,clientData)
        verifypaymentSummaryBlockExtended(paymentsData, resultData,false)
    }

    def "TC11: Verify Select Payment type Actual & Expected Results"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "TC12: Verify Card Details Actual & Expected Results"(){
        given: "user is on payment page"
        verfiyCardDetails(resultData,defaultData.expected.cardNum,defaultData.expected.securityCode)
    }

    def "TC13: Verify saved address Actual & Expected Results"(){
        given: "user is on payment page"
        verifysavedAddress(resultData,paymentsData)
    }
    def "TC14: Verify Make payment button Actual & Expected Results"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }
    def "TC15: Verify booking confirmed overlay Actual & Expected Results"(){
        given: "user is on booking confirmed overlay"
        verifyBookingonfirmedOverlay(paymentsData,resultData)
    }
    def "TC16: Verify Itinerary page after payment Actual & Expected Results"() {
        given: "user is on itinerary page"
        verifyItineraryPageAfterPaymentHotelMultiModule(paymentsData,resultData)
    }
    def"TC17: Verify Book an transfer from non booked item section Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifybookanTransfer(paymentsData, resultData)
    }
    def"TC18: Verify Booking T & C overlay Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifyBookingTAndCoverlayTransfersMultiModule(paymentsData, resultData)
    }
    def "TC19: Verify Payment page_PayNow Actual & Expected Results"(){
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }

    def "TC20: Verify Payment summary block Actual & Expected Results"(){
        given: "user is on payment page"
        verifypaymentSummeryBlockTransfers(paymentsData, resultData)
    }

    def "TC21: Verify Select Payment type Actual & Expected Results"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "TC22: Verify Card Details Actual & Expected Results"(){
        given: "user is on payment page"
        verfiyCardDetailsTransfers(resultData,defaultData.expected.cardNum,defaultData.expected.securityCode)
    }

    def "TC23: Verify saved address Actual & Expected Results"(){
        given: "user is on payment page"
        verifysavedAddress(resultData,paymentsData)
    }
    def "TC24: Verify Make payment button Actual & Expected Results"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }
    def "TC25: Verify booking confirmed overlay Actual & Expected Results"(){
        given: "user is on payment page"
        verifybookingConfirmedOverlayTransfers(paymentsData,resultData)
    }
    def "TC26: Verify Itinerary page after payment Actual & Expected Results"() {
        given: "user is on itinerary page"
        verifyItineraryPageAfterPaymentTransfersMultiModule(paymentsData,resultData)
    }
    def"TC27: Verify Book an activity from non booked item section Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifybookanActivity(paymentsData, resultData)
    }
    def"TC28: Verify Booking T & C overlay Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifyBookingTAndCoverlaySightSeeingMultiModule(paymentsData, resultData)
    }
    def "TC29: Verify Payment page_PayNow Actual & Expected Results"(){
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }
    def "TC30: Verify Payment summary block Actual & Expected Results"(){
        given: "user is on payment page"
        verifypaymentSummeryBlockActivity(paymentsData, resultData)
    }
    def "TC31: Verify Select Payment type Actual & Expected Results"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "TC32: Verify Card Details Actual & Expected Results"(){
        given: "user is on payment page"
        verfiyCardDetailsActivity(resultData,defaultData.expected.cardNum,defaultData.expected.securityCode)
    }
    def "TC33: Verify saved address Actual & Expected Results"(){
        given: "user is on payment page"
        verifysavedAddress(resultData,paymentsData)
    }
    def "TC34: Verify Make payment button Actual & Expected Results"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }
    def "TC35: Verify booking confirmed overlay Actual & Expected Results"(){
        given: "user is on payment page"
        verifybookingConfirmedOverlayActivity(paymentsData,resultData)
    }
    def "TC36: Verify Itinerary page after payment Actual & Expected Results"() {
        given: "user is on itinerary page"
        verifyItineraryPageAfterPaymentActivityMultiModule(paymentsData,resultData)
    }
}
