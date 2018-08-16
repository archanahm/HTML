package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
//import org.apache.xpath.operations.String
import org.spockframework.util.Assert
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 6/2/2017.
 */
class CancellationApplyOutsideMINPayTest extends PaymentSpec {
    @Shared
    //Client Login
    ClientData clientData = new ClientData("client695Currency")
    //ClientData clientData = new ClientData("auTakeNetCredentials")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("CancellationApplyOutsideMinPay", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("CancellationApplyOutsideMinPay", paymentsData)

    def "TC00: Agent POS CancellationApply-Outside-MINPayDay Book-SecurePayment"() {
        given: "user logeed in"
        resultMap.put(paymentsData.hotelName, resultData)
        login(clientData)
        searchHotelAndAddToItinerary(paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData,0)
        bookItem(resultData, 0,1)
        bookTCOverlay(paymentsData, resultData)
        at ItineraryTravllerDetailsPage
        selectRemarks(paymentsData.input.remarks1)
        selectRemarks(paymentsData.input.note1)
        resultData.hotel.itineraryOverlay.payLaterStatusInAboutToBookScrn = getConfirmBookingPayLaterStatus()
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlock(resultData,2,1)
        selectPaymentType(resultData)
        //SelectPaymentType(defaultData.expected.americanExpressType)
        cardDetails(defaultData.expected.americanExpressType,defaultData.expected.amexCardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)
        SelectSavedAddress(defaultData.input.addrType1)
        waitTillLoadingIconDisappears()
        makePaymentButton(resultData)
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
        if(resultData.hotel.itineraryOverlay.payLaterStatusInAboutToBookScrn.toString().equals("true"))
        Assert.fail("PayLater option displayed for CancellationApplyOutsideMinPay scenario.")
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
        verfiyCardDetails(resultData,defaultData.expected.amexCardNum,defaultData.expected.securityCode)
    }
    def "TC09: verifyMakePaymentButton"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }

    def "TC10: verifyBookedConfirmedOverlay"(){
        given: "user is on booking confirmed overlay"
        verifyBookedConfirmedOverlay(paymentsData,resultData)
    }
    def "TC11: verifyItineraryPageAfterPayment"(){
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)
        verifyCancellationAndAmendCharges(resultData)
    }
}
