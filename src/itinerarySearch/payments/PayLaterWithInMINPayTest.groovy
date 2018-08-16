package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import org.spockframework.util.Assert
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 6/5/2017.
 */
class PayLaterWithInMINPayTest extends PaymentSpec{

    @Shared
    //Client Login
    ClientData clientData = new ClientData("client695")
  //  ClientData clientData = new ClientData("auTakeNetCredentials")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("PaylaterOutsideMinPay", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("PaylaterOutsideMinPay", paymentsData)

    def "TC00: Agent POS Free Cancellation PayLater -WithInPay Book-SecurePayment"() {
        given: "user logeed in"
        resultMap.put(paymentsData.hotelName, resultData)
        login(clientData)
        searchHotelAndAddToItinerary(paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData, 0)
        bookItem(resultData, 0, 1)
        bookTCOverlay(paymentsData, resultData)
        at ItineraryTravllerDetailsPage
        selectRemarks(paymentsData.input.remarks1)
        selectRemarks(paymentsData.input.remarks2)
        selectRemarks(paymentsData.input.note1)
        resultData.hotel.itineraryOverlay.payLaterStatus = getConfirmBookingPayLaterStatus()
        clickOnTravellerCheckBox(1)
        clickConfirmBookingPayNow()
        sleep(1000)
        resultData.hotel.itineraryOverlay.travelerErrorMsg = getErrorRoomOccupancy()
        clickOnTravellerCheckBox(1)
        payLater(resultData,0)
        ItineraryPageAfterPayment(resultData,0,0)
        clickOnPayButtonInItinerary(0)
        at PaymentsPage
       // clickOnOk()
        resultData.hotel.paymentpage.paymentDispStatus = getPaymentPageDispStatus()
        paymentSummeryBlock(resultData,2,1)
        selectPaymentType(resultData)
        // SelectPaymentType(defaultData.expected.americanExpressType)
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
        if(resultData.hotel.itineraryOverlay.payLaterStatusInAboutToBookScrn.toString().equals("true"))
            Assert.fail("PayLater option displayed for CancellationApplyOutsideMinPay scenario.")
        assertionEquals(resultData.hotel.itineraryOverlay.travelerErrorMsg,defaultData.expected.travelerErrorMsg,"Error msg not displayed when user clicks on paynow button")
    }
    def "TC05: verifyPayment_Paylater"(){
        given: "user is on About to book overlay"
        assertionEquals(resultData.hotel.confirmBookingOverlay.header,defaultData.expected.confirmStatus,"Booking confirmed header not displayed.")
        assertionEquals(resultData.hotel.itineraryPage.payLaterPaymentStatus,defaultData.expected.payLaterStatus,"Payment not taken not displayed for Paylater flow.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.payButtonStatus.toString().equals("true"))
    }
    def "TC06: verifyItineraryPageAfterPayLater"(){
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)
        verifyCancellationAndAmendCharges(resultData)
    }
    def "TC07: paymentPageByClickingOnPay"(){
        given: "user is on itinerary page"
        softAssert.assertTrue(resultData.hotel.paymentpage.paymentDispStatus,"paymentpage not displayed.")
    }
    protected def "TC08: verifyPaymentSummeryBlock"(PaymentsTestResultData resultData){
        softAssert.assertTrue( resultData.hotel.paymentpage.paymentSummeryLabel,"Paymentsummery label not displayed." )
        assertList(resultData.hotel.paymentpage.paymentSummeryLables,defaultData.expected.paymentSummeryBlock,"payment summery labels not displayed.")
        softAssert.assertFalse(resultData.hotel.paymentpage.cardDetails.bookingIdStatus,"booking id is not displayed in payment summery card.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().startsWith(resultData.hotel.paymentpage.cardDetails.date.toString()),"date is not displayed in payment summery card.")
        assertionEquals(resultData.hotel.paymentpage.cardDetails.name,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"hotel name is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(resultData.hotel.paymentpage.cardDetails.pax.toString()),"pax not matched in payment page against itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice.toString().startsWith(resultData.hotel.paymentpage.cardDetails.price),"price is not matched against itinerary page.")
        assertionEquals(resultData.hotel.paymentpage.totalGrass,resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,"total gross is not matching.")
        softAssert.assertTrue(resultData.hotel.paymentpage.totalGrass> resultData.hotel.paymentpage.totalNet,"net price is not less then gross price")
        assertionEquals(resultData.hotel.paymentpage.totalPay,resultData.hotel.paymentpage.totalNet,"total net price and total price are not equal.")

    }
    def "TC09: verifyPaymentSummeryBlock"(){
        given: "user is on payment page"
        verifyPaymentSummeryBlock(resultData,clientData)
    }

    def "TC10: verifyselectPaymentType"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "TC11: verfiyCardDetails"(){
        given: "user is on payment page"
        verfiyCardDetails(resultData,defaultData.expected.cardNum,defaultData.expected.securityCode)
    }
    def "TC12: verifyMakePaymentButton"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }

    def "TC13: verifyBookedConfirmedOverlay"(){
        given: "user is on booking confirmed overlay"
        verifyBookedConfirmedOverlay(paymentsData,resultData)
    }
    def "TC14: verifyItineraryPageAfterPayment"(){
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)
        verifyCancellationAndAmendCharges(resultData)
    }
}
