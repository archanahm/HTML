package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by kmahas on 10/20/2017.
 *
 * NOVA POS GC hotel - Item search/Multi Module-FreeCancellation-outside-MINPayDay-PayLater-Transfer and Activity-PayNOWHotel-check PDP all section  - itineary page - Book-SecurePayment -TakeGross-
 *
 */
class MuModPayNowPayLaterTest extends PaymentSpec {
    //Client Login
    @Shared
    ClientData clientData = new ClientData("client697")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("MuModPayNowPayLaterTest", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("MuModPayNowPayLaterTest", paymentsData)

    def "TC00: Multi Module Pay Now Pay Later "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(paymentsData.hotelName, resultData)
        loginToApplicaiton(clientData, paymentsData, resultData)
        searchForHotelAndAddToItinerary(clientData, paymentsData, resultData)
        searchForTransferAndAddToItinerary(paymentsData, resultData)
        searchForActivityAndAddToItinerary(paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData, 0)
        NonBookedItemSection(paymentsData, resultData, 2, 0, 1)
        bookAHotel(paymentsData, resultData, 2)
        BookingTAndCoverlay(paymentsData, resultData, true)
        paymentPagePayLater(resultData, 0)
        bookedItemSectionAfterPayLater(clientData, resultData, 0, 2)


        bookAnActivity(paymentsData,resultData,1)
        BookingTAndCoverlaySightSeeingMultiModule(paymentsData, resultData)
        paymentPagePayLaterActivity(paymentsData, resultData,0)
        Bookeditemsectionafterpaylateractivity(clientData,resultData,1,0)
        bookATransfer(paymentsData,resultData,0)
        BookingTAndCoverlayTransfersMultiModule(paymentsData, resultData)
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlockTransfers(resultData,2,1)
        selectPaymentType(resultData)
        cardDetailsTransfers(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)

        savedAddress(resultData)
        makePaymentButton(resultData)
        bookingConfirmedOverlayTransfers(paymentsData,resultData,0)
        ItineraryPageAfterPaymentMultiModule(clientData,resultData,2,0,1)


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
        verifyBookingTAndCoverlay(paymentsData, resultData,true)
    }
    def "TC09: Verify Payment page_PayLater Actual & Expected Results"(){
        given: "user is on payment page"
        verifyBookingonfirmedOverlay(paymentsData,resultData)
    }

    def "TC10: Verify Booked item section after pay later Actual & Expected Results"(){
        given: "user is on payment page"
        verifybookedItemSectionAfterPayLater(paymentsData,resultData)
    }

    def"TC11: Verify Book an activity from non booked item section Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifybookanActivity(paymentsData, resultData)
    }

    def"TC12: Verify Booking T & C overlay Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifyBookingTAndCoverlaySightSeeingMultiModule(paymentsData, resultData)
    }

    def"TC13: Verify Payment page_PayLater Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifybookingConfirmedOverlayActivity(paymentsData, resultData)
    }

    def "TC14: Verify Booked item section after pay later activity Actual & Expected Results"() {
        given: "user is on itinerary page"
        verifyBookeditemsectionafterpaylateractivity(paymentsData,resultData)
    }

    def"TC15: Verify Book an transfer from non booked item section Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifybookanTransfer(paymentsData, resultData)
    }
    def"TC16: Verify Booking T & C overlay Actual & Expected Results"(){

        given: "User is Itinerary Page"
        verifyBookingTAndCoverlayTransfersMultiModule(paymentsData, resultData)
    }
    def "TC17: Verify Payment page_PayNow Actual & Expected Results"(){
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }

    def "TC18: Verify Payment summary block Actual & Expected Results"(){
        given: "user is on payment page"
        verifypaymentSummeryBlockTransfer(paymentsData, resultData)
    }
    def "TC19: Verify Select Payment type Actual & Expected Results"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "TC20: Verify Card Details Actual & Expected Results"(){
        given: "user is on payment page"
        verfiyCardDetailsTransfers(resultData,defaultData.expected.cardNum,defaultData.expected.securityCode)
    }

    def "TC21: Verify saved address Actual & Expected Results"(){
        given: "user is on payment page"
        verifysavedAddress(resultData,paymentsData)
    }
    def "TC22: Verify Make payment button Actual & Expected Results"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }
    def "TC23: Verify booking confirmed overlay Actual & Expected Results"(){
        given: "user is on payment page"
        verifybookingConfirmedOverlayTransfers(paymentsData,resultData)
    }
    def "TC24: Verify Itinerary page after payment Actual & Expected Results"() {
        given: "user is on itinerary page"
        verifyItineraryPageAfterPaymentMultiModule(paymentsData,resultData)
    }
}