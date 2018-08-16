package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 5/22/2017.
 */
class CancellationApplyWithInMINPayTest extends PaymentSpec {
    @Shared
    //Client Login
     ClientData clientData = new ClientData("client696")
    //ClientData clientData = new ClientData("auTakeNetCredentials")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("CancellationApplyWithInMINPay", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("CancellationApplyWithInMINPay", paymentsData)

    def "TC00: Agent POS CancellationApply -within-MINPayDay Book-SecurePayment"() {
        given: "user logeed in"
        resultMap.put(paymentsData.hotelName, resultData)
        login(clientData)
        searchHotelAndAddToItinerary(paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData,0)
        bookItem(resultData, 0,1)
        bookTCOverlay(paymentsData, resultData)
        at ItineraryTravllerDetailsPage
        selectRemarks(paymentsData.input.note1)
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlock(resultData,2,1)
        at PaymentsPage
        clickOnReturnToItineraryPage()
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryPage.itineraryStatus = verifyItineraryPageDisplayed()
        bookItem(resultData, 0,1)
        paymentPage_payNow(defaultData, resultData)
        at PaymentsPage
        selectPaymentType(resultData)
        SelectPaymentType(defaultData.expected.masterPaymentType)
        sleep(500)
        validateErrorMessages(resultData)
        cardDetails(defaultData.expected.masterPaymentType,defaultData.expected.masterCardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.masterCardSecurity,resultData,1)
        SelectAddress(defaultData.input.addrType)
        waitTillLoadingIconDisappears()
        scrollToBottomOfThePage()
        clickAddrfileds()
        addNewAddress(resultData,2)
        makePaymentButton(resultData)
        waitTillLoadingIconDisappears()
        sleep(2000)
        bookingConfirmedOverlay(resultData,0)
        ItineraryPageAfterPayment(resultData,0,0)
    }

   def validateErrorMessages(PaymentsTestResultData resultData){
       at PaymentsPage
       enterCardNumber(defaultData.expected.wrongCardNum)
       scrollToBottomOfThePage()
      enterSecurityCode(defaultData.expected.wrongSecurityCode)
       clickOutside()
       sleep(1000)
       resultData.hotel.paymentpage.cardNumError = validCardNumberWarning()
       sleep(500)
       resultData.hotel.paymentpage.securityCodeError = validSecurityCodeWarning()
   }
    def "TC01: verifySearchHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        verifySearchHotel(paymentsData, resultData)
    }

    def "TC02: verifyItineraryPage"() {
        given: "User is at Itinerary page"
        verifyItineraryPage(defaultData, resultData)
    }
    def "TC03: verifyBookAnItem"(){
        given: "User is on itinerary page."
        verifyBookAnItem(paymentsData, resultData, 0)
    }
    def "TC04: verifyBookTCOverlay"(){
        given: "user is on T & C Overlay"
        verifyBookTCOverlay(paymentsData,resultData)
    }
    def "TC05: verifyPaymentPagePayNow"(){
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }
    def "TC06: verifyPaymentSummeryBlock"(){
        given: "user is on payment page"
        verifyPaymentSummeryBlock(resultData,clientData)
    }

    def "TC07: verifyReturnToItinerary"(){
        given: "User is on payment page"
        softAssert.assertTrue( resultData.hotel.itineraryPage.itineraryStatus,"Itinerary page is not displayed.")
    }

    def "TC08: verifyselectPaymentType"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }

    def "TC09: verfiyCardDetails"(){
        given: "user is on payment page"
        assertionEquals(resultData.hotel.paymentpage.cardNumError,defaultData.expected.cardErrorMsg,"Error msg not displayed when user enter wrong card num")
       assertionEquals(resultData.hotel.paymentpage.securityCodeError,defaultData.expected.securityCodeErrorMsg,"Error msg not displayed when user enter wrong security code")
        verfiyCardDetails(resultData,defaultData.expected.masterCardNum,defaultData.expected.masterCardSecurity)
    }
    def "TC10: verifyAddressDetails"(){
        given: "user is on payment page"
        at PaymentsPage
        List list = getAddrErrorMessages()
        for(String errorMsg : list){
            softAssert.assertTrue(errorMsg.contains(defaultData.expected.addressErrorMsg))
        }
        verifyAddNewAddress(resultData)
    }
    def "TC11: verifyMakePaymentButton"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }
    def "TC12: verifyBookedConfirmedOverlay"(){
        given: "user is on booking confirmed overlay"
        verifyBookedConfirmedOverlay(paymentsData,resultData)
    }
    def "TC13: verifyItineraryPageAfterPayment"(){
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)


    }

}
