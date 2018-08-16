package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.PaymentPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.payments.MyAccountPage
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 7/26/2017.
 */
class TakeGrossPayLaterTest extends PaymentSpec {
    @Shared
    //Client Login
    ClientData clientData = new ClientData("client697Currency")
   //ClientData clientData = new ClientData("auTakeGrossCredentials")

    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("TakeGrossPayLater", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("TakeGrossPayLater", paymentsData)

    def "TC00: Agent POS Free Cancellation Take Gross and PayLater"() {
        given: "user logeed in"
        resultMap.put(paymentsData.hotelName, resultData)
        login(clientData)
        homeViewProfile(resultData)
        at PaymentPage
        deleteAddrInAccountPage(1)
        driver.navigate().back()
        sleep(1500)
        at HotelSearchResultsPage
        searchForHotelAndAddToItinerary(clientData,paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData, 0)
        bookItem(resultData, 0, 1)
       bookTCOverlay(paymentsData, resultData)
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryOverlay.payLaterStatus = getConfirmBookingPayLaterStatus()
        payLater(resultData, 0)
        ItineraryPageAfterPayment(resultData, 0, 0)
        homeViewProfile(resultData)
        deleteAddr()
        waitTillLoadingIconDisappears()
        addNewAddFromMyAccount(resultData,0)
        at ItineraryTravllerDetailsPage
        clickOnPayButtonInItinerary(0)
        at PaymentsPage
       // clickOnOk()
        resultData.hotel.paymentpage.paymentDispStatus = getPaymentPageDispStatus()
        paymentSummeryBlock(resultData,1,1)
        selectPaymentType(resultData)
        // SelectPaymentType(defaultData.expected.americanExpressType)
        cardDetails(defaultData.expected.americanExpressType,defaultData.expected.amexCardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)
        savedAddr(resultData,0)
        SelectSavedAddress(defaultData.input.addrType1)
        waitTillLoadingIconDisappears()
        makePaymentButton(resultData)
        waitTillLoadingIconDisappears()
        sleep(2000)
        bookingConfirmedOverlay(resultData,0)
        ItineraryPageAfterPayment(resultData,0,0)

    }
   def deleteAddr(){
       at MyAccountPage
       resultData.account.accountPage.addrDetailsInAccountPage = getAddrDetails()
        waitTillLoadingIconDisappears()
       sleep(1000)
       clickOnDeleteAddrButton(0)
       waitTillLoadingIconDisappears()
       at MyAccountPage
       // follwoing method is common for edit or delete addr overlay verification
       resultData.account.accountPage.deleteAddrOverlay =  editOverlayDispStatus()
       resultData.account.accountPage.deleteAddrHeader =  getDeleteAddressHeader()
       resultData.account.accountPage.closeOverlay = getCloseButtonStatus()
       resultData.account.accountPage.subHeaderInOverlay =  getSubHeaderInDeleteOverlay()
       resultData.account.accountPage.addrInOverlay =   getAddrDetailsInDelOverlay()
       clickYesCancelItem()
       at ItineraryTravllerDetailsPage
       waitForAjaxIconToDisappear()
       sleep(1000)
   }

    def "TC01: verifySearchHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        softAssert.assertTrue( resultData.hotel.searchResults.actualSearchResults, "Hotel results page not displayed")
        softAssert.assertTrue(resultData.hotel.searchResults.actualCancellatoinTxtDispStatus,"cancellation link not displayed")
    }

    def "TC02: verifyItineraryPage"() {
        given: "User is at Itinerary page"
        verifyItineraryPage(defaultData, resultData)
    }

    def "TC03: verifyBookAnItem"() {
        given: "User is on itinerary page."
        verifyBookAHotelItem(resultData)
    }

    def "TC04: verifyBookTCOverlay"() {
        given: "user is on T & C Overlay"
        verifyBookTCOverlay(paymentsData, resultData)
    }

    def "TC05: verifyBookedItemAfterPayLater"(){
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)
        verifyCancellationAndAmendCharges(resultData)
    }
    def "TC06: verifyViewProfile"(){
        given: "user is on itinerary page."
        softAssert.assertTrue(resultData.hotel.home.viewProfileOverlay,"View profile overlay is not displayed.")
        softAssert.assertTrue(resultData.account.accountPage.MyProfileStatus,"My Account page is not displayed.")
        softAssert.assertTrue( resultData.account.accountPage.deleteAddrOverlay,"Delete overlay did not display.")
    }
    def "TC07: verifyDeleteFirstAddr"(){
        given: "user is on my account page"
        assertionEquals(resultData.account.accountPage.deleteAddrHeader,defaultData.expected.deleteAddrHeader,"Delete addr header did not display.")
        assertionEquals(resultData.account.accountPage.subHeaderInOverlay,defaultData.expected.subHeaderInOverlay,"Are you sure message not displayed.")
        softAssert.assertTrue(resultData.account.accountPage.MyProfileStatus,"My Account page is not displayed.")

    }

    def "TC08: verifyAddNewAddr"(){
        given: "user is on myAccount page"
        softAssert.assertTrue(resultData.account.accountPage.addrOverlay, "Add addr overlay not displayed.")
        softAssert.assertTrue(resultData.account.accountPage.addedCardStatus, "Address card section not displayed.")
        assertList(resultData.account.accountPage.addrInOverlay,resultData.account.accountPage.addrDetailsInAccountPage,"Addr not matching in delete overlay.")
    }

    def "TC09: verifyPaymentPagePayNow"(){
        given: "user is on payment page"
        softAssert.assertTrue(resultData.hotel.paymentpage.paymentDispStatus, "Ok overlay not displayed")
    }
    def "TC10: verifyPaymentSummeryBlock"(){
        given: "user is on payment page"
        verifyPaymentSummeryBlock(resultData,clientData)
    }

    def "verifyselectPaymentType"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "verfiyCardDetails"(){
        given: "user is on payment page"
        verfiyCardDetails(resultData,defaultData.expected.amexCardNum,defaultData.expected.securityCode)
    }
    def "verifySavedAddr"(){
        given: "user is on payment page"
        softAssert.assertTrue(resultData.hotel.paymentpage.savedAddrStatus,"Saved address not displayed.")
        assertionEquals(resultData.hotel.paymentpage.savedAddrCount.toString(),"1","Added addr Count is not matching.")
        assertList(resultData.account.accountPage.lastCardAddrDetails.sort(),resultData.hotel.paymentpage.savedAddr.sort(),"added card details in account page not matching with saved addr")
    }
    def "verifyMakePaymentButton"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }

    def "verifyBookedConfirmedOverlay"(){
        given: "user is on booking confirmed overlay"
        verifyBookedConfirmedOverlay(paymentsData,resultData)
    }
    def "verifyItineraryPageAfterPayment"() {
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)
    }


}
