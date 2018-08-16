package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.helper.TransferTestResultData
import com.kuoni.qa.automation.helper.TransfersTestData
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.TransferSearchResultsPage
import com.kuoni.qa.automation.test.search.booking.transfer.TransfersSpec
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.booking

/**
 * Created by mtmaku on 8/4/2017.
 */
class TestTransferPaymentBookingTest extends TransfersSpec {
    @Shared
    ClientData clientData = new ClientData("prePaidClient")

    DateUtil dateUtil = new DateUtil()

    @Shared
    TransfersTestData transferData = new TransfersTestData("SanityAirportToHotel", "transfers", booking)
    @Shared
    TransferTestResultData resultData = new TransferTestResultData("SanityAirportToHotel", transferData)
    @Shared
    PaymentsTestResultData resultData1 = new PaymentsTestResultData("sanityPayment", defaultData)


    def "TC00: NOVA POS - Transfers Sanity Testing "() {
        given: "User is able to login and present on Hotel tab"

        loginToApplicaiton(clientData, transferData, resultData)// Test case 1,2 are covered
        SearchTransferAtoH(transferData, resultData)
        at TransferSearchResultsPage
        //click on add to Itineary button
        Itinerarybuilder(transferData, resultData)
        GoToitinerary(transferData, resultData)
        sleep(1000)
        Itinerary(clientData,transferData, resultData)
        TravellerDetails(transferData, resultData)
        paymentBooking(transferData, resultData)
        paymentPage_payNow(defaultData, resultData1)
        sleep(1000)
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
       /* at PaymentsPage
        paymentStatus(resultData1,0)*/
        at ItineraryTravllerDetailsPage
        resultData.transfers.itineraryPage.bookingStatus = getBookingStatus(0)
        resultData.transfers.itineraryPage.afterBookingPrice = getItenaryPriceInBookedItem(1)
        then: "user should be able to book item successufully and payment taken"

    }
    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(transferData, resultData)// Test case 1,2 are covered
    }
    def"TC02: Verify Search - Transfer A to A Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifySearchTransferAtoH(transferData, resultData)
    }
    def "TC03: verifyPriceInPaymentPage"(){
        given: "user is in payment page."
        softAssert.assertTrue(resultData1.hotel.paymentpage.totalPay.toString().trim().startsWith(resultData1.hotel.paymentpage.priceInPaymentBlock.toString().trim()),"total pay in payment summery is not matching with seure payment.")
    }

    def "TC04: verifyTransfersBookedItemInItinerary"(){
        given: "User is on Itinerary page."
        assertionEquals(resultData.transfers.searchResults.firstTransfername, resultData.transfers.itineraryPage.actualTransferItineraryName,"Transfer Name not matching with PLP.")
       // assertionEquals(resultData.transfers.itineraryPage.expectedTransferPriceBeforebooking,resultData.transfers.itineraryPage.actualTransferPriceBeforebooking,"Price not matching with PLP" )
        assertionEquals(  resultData.transfers.itineraryPage.bookingStatus,defaultData.expected.bookingConfirmedStatus,"Transfer item not booked.")
        assertionEquals(resultData.transfers.itineraryPage.actualTransferPriceBeforebooking, resultData.transfers.itineraryPage.afterBookingPrice,"Prices are not equal before booking and after booking.")
    }

   /* def "TC:05 verifyPaymentStatus"(){
        given: "user is on itinerary page"
        verifyPaymentStatus(resultData1)

    }*/

}
