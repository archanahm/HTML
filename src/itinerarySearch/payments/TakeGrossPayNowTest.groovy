package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.HomePage
import com.kuoni.qa.automation.page.PaymentPage
import com.kuoni.qa.automation.page.payments.MyAccountPage
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import org.junit.Ignore
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 6/8/2017.
 */
class TakeGrossPayNowTest extends PaymentSpec {
    @Shared
    //Client Login
  // ClientData clientData = new ClientData("auTakeGrossCredentials")
    ClientData clientData = new ClientData("client697")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("TakeGrossPayNow", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("TakeGrossPayNow", paymentsData)

    def "TC00: Take gross and payNow Book-SecurePayment"() {
        given: "user logeed in"
        resultMap.put(paymentsData.hotelName, resultData)
        login(clientData)
        waitTillLoadingIconDisappears()
        at HomePage
        clickOnViewProfile()
        sleep(5000)
        at PaymentPage
        deleteAddrInAccountPage(1)
        driver.navigate().back()
        sleep(1000)
        searchHotelAndAddToItinerary(paymentsData, resultData)
        TCItineraryPage(paymentsData, resultData, 0)
        bookItem(resultData, 0, 1)
        bookTCOverlay(paymentsData, resultData)
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlock(resultData, 1, 1)
        selectPaymentType(resultData)
        cardDetails(defaultData.expected.masterPaymentType, defaultData.expected.masterCardNum, defaultData.expected.startDate, defaultData.expected.startYear, defaultData.expected.expireDate, defaultData.expected.expireYr, defaultData.expected.cardHolder, defaultData.expected.masterCardSecurity, resultData, 0)
        addNewAddress(resultData, 2)
        returnToItinerary(resultData)
        makePaymentButton(resultData)
        sleep(5000)
        bookingConfirmedOverlay(resultData, 0)
        ItineraryPageAfterPayment(resultData, 0, 0)
        addedAddrSavedToProfile(resultData)
        sleep(1000)
        addNewAddFromMyAccount(resultData,1)
        addNewHotel()
        searchHotelAndAddToItinerary(paymentsData, resultData, paymentsData.input.hotelName1, paymentsData.input.chkInDays.toString(), paymentsData.input.chkoutDays.toString())
        bookSecondItem(resultData)
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlock1(resultData)
        selectPaymentType(resultData)
        at PaymentsPage
        enterCardDetails(defaultData.expected.masterPaymentType.toString(), defaultData.expected.masterCardNum.toString(), defaultData.expected.expireDate.toString(), defaultData.expected.expireYr.toString(), defaultData.expected.cardHolder, defaultData.expected.masterCardSecurity.toString())
        sleep(1000)
        at PaymentsPage
        SelectSavedAddress(defaultData.input.addrType1)
        waitTillLoadingIconDisappears()
     //   savedAddrSection()
        makePaymentButton(resultData)
        waitTillLoadingIconDisappears()
        sleep(2000)
        bookingConfirmedOverlay(resultData)
        ItineraryPageAfterPayment(resultData)

    }
    def savedAddrSection(){
        savedAddr(resultData,2)
        resultData.hotel.paymentpage.savedAddrFirstName =  getSavedAddr(1)
        at PaymentsPage
        SelectSavedAddress(defaultData.input.addrType1)

    }

    protected def bookSecondItem(PaymentsTestResultData resultData) {
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryPage.hotelItem2 = storeItemDetails(0, 1, "hotel")
        resultData.hotel.itineraryPage.cancellationApplyLinkStatus = getItinenaryFreeCnclTxtInSuggestedItem(0)
        sleep(1000)
        clickOnBookIcon()
        waitTillLoadingIconDisappears()
        sleep(1000)
        resultData.hotel.itineraryPage.overlayStatus = overlayCloseButton()
        // resultData.hotel.itineraryPage.lightBox = getTravellerCannotBeDeletedPopupDisplayStatus()

    }

    protected def paymentSummeryBlock1(PaymentsTestResultData resultData) {
        resultData.hotel.paymentpage.cardDetailsSecondItem = paymentCardDetails(0)
        resultData.hotel.paymentpage.totalGrassSecondItem = getTotalGross()
        resultData.hotel.paymentpage.totalNetSecondItem = getTotalNet()
        resultData.hotel.paymentpage.totalPaySecondItem = getGrossORNetORTotalPay(1, 1)
    }

    protected def addedAddrSavedToProfile(PaymentsTestResultData resultData) {
        at HomePage
        clickOnViewProfile()
        at MyAccountPage
       /* resultData.account.accountPage.accountPageDispStatus = getMyProfileStatus()
        resultData.account.accountPage.MyProfileStatus = getMyProfileSectionStatus()
        resultData.account.accountPage.addrSection = getBillingAddrSectionStatus(0)
        resultData.account.accountPage.addrLabel = getAddrLabel(0)
        resultData.account.accountPage.deleteButtonStatus = getDeleteButtonStatus(0)
        resultData.account.accountPage.addrDetails = getAddrDetails()*/
        clickOnEditAddrButton()
        sleep(3000)
        resultData.account.accountPage.editOverlayStatus = editOverlayDispStatus()
        resultData.account.accountPage.closeButtonStatus = getCloseButtonStatus()
        resultData.account.accountPage.editAddrLabels = getEditOverlayLabels()
        resultData.account.accountPage.editOverlayInputValues = getEditOverlayInputValues()
        enterFirstName(defaultData.expected.firstNameUpdate)
        sleep(500)
        //clickOnOk()
        waitForAjaxIconToDisappear()
        sleep(2000)
        resultData.account.accountPage.firstName = getAddrDetailsInAddrCard(1)
        clickOnSaveAddrButton()
        waitTillLoadingIconDisappears()
    }


    protected def addNewHotel() {
        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary("Add a hotel")
    }
    protected def bookingConfirmedOverlay(PaymentsTestResultData resultData){
        at ItineraryTravllerDetailsPage
        sleep(500)
        resultData.hotel.confirmBookingOverlay.bookingIDSecondItem = getBookingIdFromBookingConfirmed(0)
        resultData.hotel.confirmBookingOverlay.depatureDateSecondItem = getBookingDepartDate()
        resultData.hotel.confirmBookingOverlay.hotelNameSecondItem = getHotelNameInBookingConfScrn(1)
        sleep(500)
        resultData.hotel.confirmBookingOverlay.addrSecondItem = getHotelAddressInBookingConfScrn(1)
        resultData.hotel.confirmBookingOverlay.dateSecondItem = getCheckInDateNumOfNightsBookingConfirmed()
        resultData.hotel.confirmBookingOverlay.priceSecondItem = getSubTotalsInBookingConfScreen(1)
        resultData.hotel.confirmBookingOverlay.commissionSecondItem = gettotalCommissionBookingConfirmScrn()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(1000)
    }
    protected def ItineraryPageAfterPayment(PaymentsTestResultData resultData){
        at ItineraryTravllerDetailsPage
        sleep(1000)
        resultData.hotel.itineraryPage.bookedItem1 =  storeItemDetails(1,0,"hotel")
        resultData.hotel.itineraryPage.itemSectionAmendStatus1 = getTabTxtInBookedItemsSection(1,0)
        resultData.hotel.itineraryPage.itemCancelStatus1 = getTabTxtInBookedItemsSection(1,1)
        String travelers = getTravelerNamesMicros(1)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.hotel.itineraryPage.travelerOne = travellerSplit.getAt(0)
        resultData.hotel.itineraryPage.travelerTwo = travellerSplit.getAt(1)
        clickOnItemCancllationLink(1)
        waitTillLoadingIconDisappears()
        sleep(700)
        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(4000)
    }

    /* Validation methods*/

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

    def "TC04: verifyBookTCOverlay"() {
        given: "user is on T & C Overlay"
        verifyBookTCOverlay(paymentsData, resultData)
    }

    def "TC05: verifyPaymentPagePayNow"() {
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }

    protected def "TC06: verifyPaymentSummeryBlock"() {
        given: "User is on payment page."
        softAssert.assertTrue(resultData.hotel.paymentpage.paymentSummeryLabel, "Paymentsummery label not displayed.")
        assertList(resultData.hotel.paymentpage.paymentSummeryLables, defaultData.expected.paymentSummeryBlock, "payment summery labels not displayed.")
        softAssert.assertTrue(resultData.hotel.paymentpage.cardDetails.bookingIdStatus, "booking id is displayed in payment summery card.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().startsWith(resultData.hotel.paymentpage.cardDetails.date.toString()), "date is not displayed in payment summery card.")
        assertionEquals(resultData.hotel.paymentpage.cardDetails.name, resultData.hotel.itineraryPage.hotelItem.expectedHotelName, "hotel name is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(resultData.hotel.paymentpage.cardDetails.pax.toString()), "pax not matched in payment page against itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice.toString().startsWith(resultData.hotel.paymentpage.cardDetails.price), "price is not matched against itinerary page.")
        assertionEquals(resultData.hotel.paymentpage.totalGrass, resultData.hotel.itineraryPage.hotelItem.expectedItemPrice, "total gross is not matching.")
        softAssert.assertTrue(resultData.hotel.paymentpage.totalGrass.toString().equals(resultData.hotel.paymentpage.totalNet.toString()), "total price is not equal to gross price")
        assertionEquals(resultData.hotel.paymentpage.totalPay, resultData.hotel.paymentpage.totalGrass, "total gross price and total price are not equal.")

    }

    def "TC07: verifyselectPaymentType"() {
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }

    def "TC08: verfiyCardDetails"() {
        given: "user is on payment page"
        verfiyCardDetails(resultData, defaultData.expected.masterCardNum, defaultData.expected.masterCardSecurity)
    }

    def "TC09: verifyAddNewAddress"() {
        given: "user is on payment page"
        verifyAddNewAddress(resultData)
    }

    def "TC10: verifyReturnToItinerary"() {
        given: "user is on payment page"
        verifyReturnToItinerary(resultData)
    }

    def "TC11: verifyMakePaymentButton"() {
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }

    def "TC12: verifyBookedConfirmedOverlay"() {
        given: "user is on booking confirmed overlay"
        verifyBookedConfirmedOverlay(paymentsData, resultData)
    }

    def "TC13: verifyItineraryPageAfterPayment"() {
        given: "user is on itinerary page"
        verifyItineraryPageAfterPayment(resultData)
    }

    /*protected def verifyAddedAddrSavedToProfile() {
        given: "User is on Itinerary page."
        softAssert.assertTrue(resultData.account.accountPage.accountPageDispStatus, "My Account page is not displayed.")
        softAssert.assertTrue(resultData.account.accountPage.MyProfileStatus, "MyProfile status is not displayed.")
        softAssert.assertTrue(resultData.account.accountPage.addrSection, "Address card section not displayed.")
        assertionEquals(resultData.account.accountPage.addrLabel, defaultData.expected.addr1, "Address1 Label not displayed.")
        softAssert.assertTrue(resultData.account.accountPage.deleteButtonStatus, "Delete button is not displayed.")
       // assertList(resultData.hotel.paymentpage.txtFieldValues.sort(), resultData.account.accountPage.addrDetails.sort(), "Addr is not matching in payment page.")
        assertionEquals(resultData.hotel.paymentpage.txtFieldValues.sort().get(0).toString().trim(), resultData.account.accountPage.addrDetails.sort().get(0).toString().trim(), "addr not matching")
        assertionEquals(resultData.hotel.paymentpage.txtFieldValues.sort().get(1), resultData.account.accountPage.addrDetails.sort().get(1), "zipcode not matching")
        assertionEquals(resultData.hotel.paymentpage.txtFieldValues.sort().get(2), resultData.account.accountPage.addrDetails.sort().get(3), "city not matching")
        assertionEquals(resultData.hotel.paymentpage.txtFieldValues.sort().get(3), resultData.account.accountPage.addrDetails.sort().get(4), "city not matching")
        softAssert.assertTrue(resultData.account.accountPage.addrDetails.sort().get(5).toString().startsWith(resultData.hotel.paymentpage.txtFieldValues.sort().get(4).toString()) , " payment test name not matching")
        assertionEquals(resultData.hotel.paymentpage.txtFieldValues.sort().get(6), resultData.account.accountPage.addrDetails.sort().get(6), "country not matching")
        assertionEquals( resultData.account.accountPage.addrDetails.sort().get(2), resultData.hotel.paymentpage.txtFieldValues.sort().get(7),"city not matching")
        softAssert.assertTrue(resultData.account.accountPage.editOverlayStatus, "Edit overlay not displayed")
        softAssert.assertTrue(resultData.account.accountPage.closeButtonStatus, "Close button not displayed.")
        assertList(resultData.hotel.paymentpage.addrLabels.sort(),resultData.account.accountPage.editAddrLabels.sort(), "Labels are not matching.")
        assertList(resultData.hotel.paymentpage.txtFieldValues.sort(), resultData.account.accountPage.editOverlayInputValues.sort(), "addr fields are not matching.")
        softAssert.assertTrue(resultData.account.accountPage.firstName.toString().contains(defaultData.expected.firstNameUpdate), "first name is not udpated in account page.")
    }*/

    protected def "TC13: verifyAddNewAddrFromMyAccount"() {
        given: "user is on MyAccount page"
        softAssert.assertTrue(resultData.account.accountPage.addrOverlay, "Add addr overlay not displayed.")
       assertionEquals(resultData.account.accountPage.editAddrLabels.sort().get(0).toString(),resultData.hotel.paymentpage.addrLabels.sort().get(0).toString(),"first name not matching")
       // softAssert.assertEquals(resultData.account.accountPage.editAddrLabels, resultData.hotel.paymentpage.addrLabels, "Labels are not matching.")
        softAssert.assertTrue(resultData.account.accountPage.addedCardStatus, "addr2 is not updated in my account page")
        softAssert.assertTrue(resultData.hotel.itineraryPage.itineraryPageStatus, "Itinerary page not displayed")

    }

    def "TC14: verifySearchSecondHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        verifySearchHotel(paymentsData, resultData)
    }

    def "TC15: verifySecondItemInItineraryPage"() {
        given: "User is at Itinerary page"
        verifyItineraryPage(defaultData, resultData)
    }
     protected def "TC16: verifySecondBookAnItem"(){
        given: "user is on Itinerary page."
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedHotelName,paymentsData.expected.hotelCard1[0],"HotelName not matching as expected.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedStarRating,paymentsData.expected.hotelCard1[1],"Star Rating not matching as expected.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedItemDesc,paymentsData.expected.hotelCard1[2],"Desc not matching as expected.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration.toString().endsWith(paymentsData.expected.hotelCard1[3].toString()),"pax not displayed for Non-booked item.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedItemStatus,paymentsData.expected.hotelCard1[4],"Available not displayed for Non-booked item.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedCommission.toString(),paymentsData.expected.hotelCard1[5].toString(),"Commission not matching as expected.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice,  resultData.hotel.searchResults.itemPriceSecondItem,"price not matched")
        softAssert.assertTrue( resultData.hotel.itineraryPage.cancellationApplyLinkStatus.toString().contains(paymentsData.expected.cancellationLinkStatus),"cancellation charges apply not displayed")
        softAssert.assertTrue(resultData.hotel.itineraryPage.overlayStatus ,"T&C Overlay not displayed.")

    }
   protected def "TC17: verifySecondItemBookingOverlay"(){
       given: "user is on booking overlay"
       softAssert.assertTrue(resultData.hotel.itineraryOverlay.checkBoxCheckedStatus.get(0),"Checkbox 1 is not selected by default")
       softAssert.assertTrue(resultData.hotel.itineraryOverlay.checkBoxCheckedStatus.get(1),"Checkbox 2 is not selected by default")
       softAssert.assertTrue(resultData.hotel.itineraryOverlay.bookNowEnabledStatus,"Book now button is disabled.")
   }

    protected def "TC18: verifyPaymentSummeryBlockSecondItem"(){
        given: "User is on payment page."
        softAssert.assertTrue( resultData.hotel.paymentpage.paymentSummeryLabel,"Paymentsummery label not displayed." )
        assertList(resultData.hotel.paymentpage.paymentSummeryLables,defaultData.expected.paymentSummeryBlock,"payment summery labels not displayed.")
        softAssert.assertTrue(resultData.hotel.paymentpage.cardDetailsSecondItem.bookingIdStatus,"booking id is displayed in payment summery card.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration.toString().startsWith(resultData.hotel.paymentpage.cardDetailsSecondItem.date.toString()),"date is not displayed in payment summery card.")
        assertionEquals(resultData.hotel.paymentpage.cardDetailsSecondItem.name,resultData.hotel.itineraryPage.hotelItem2.expectedHotelName,"hotel name is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemDesc.toString().contains(resultData.hotel.paymentpage.cardDetailsSecondItem.pax.toString()),"pax not matched in payment page against itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice.toString().startsWith(resultData.hotel.paymentpage.cardDetailsSecondItem.price),"price is not matched against itinerary page.")
        assertionEquals(resultData.hotel.paymentpage.totalGrassSecondItem,resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice,"total gross is not matching.")
        softAssert.assertTrue(resultData.hotel.paymentpage.totalGrassSecondItem.toString().equals(resultData.hotel.paymentpage.totalNetSecondItem.toString()),"total price is not equal to gross price")
        assertionEquals(resultData.hotel.paymentpage.totalPaySecondItem,resultData.hotel.paymentpage.totalGrassSecondItem,"total gross price and total price are not equal.")
    }

    def "TC19: verifyselectPaymentTypeWhileAddingSecondItem"() {
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }

   /* def "verifySavedAddrSection"(){
        assertionEquals(resultData.hotel.paymentpage.savedAddrCount,paymentsData.expected.savedAddrCount.toString().toInteger(),"Added addr Count is not matching.")
        assertList(resultData.hotel.paymentpage.savedAddr,resultData.account.accountPage.lastCardAddrDetails,"3rd Card details are not matching")
        assertionEquals(resultData.hotel.paymentpage.savedAddrFirstName.get(0),resultData.account.accountPage.firstName,"Updated name not matching against paymentpage.")

    }*/

    def "TC20: verifyBookingConfirmationOverlaySecondItem"(){
        given: "user is on booking confirmed overlay"

        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration.toString().contains(resultData.hotel.confirmBookingOverlay.depatureDateSecondItem.toString()),"depature date is not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.hotelNameSecondItem,resultData.hotel.itineraryPage.hotelItem2.expectedHotelName,"Hotel name not matching.")

        assertionEquals(resultData.hotel.confirmBookingOverlay.addrSecondItem,paymentsData.expected.hotelAddr1,"hotel addr is not matching.")
        String pax = resultData.hotel.confirmBookingOverlay.paxDesc.replaceAll("\\n"," ").split("S").getAt(1)
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(pax),"pax are not matching against itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.dateSecondItem,resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration,"date is not matching")
        assertionEquals(resultData.hotel.confirmBookingOverlay.priceSecondItem,resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice,"price is not matching with itinerary")
        Float commission = resultData.hotel.confirmBookingOverlay.priceSecondItem.toString().split(" ").getAt(0).toString().toFloat()
        commission = (commission*12)/100
        Float commissionOverlay = resultData.hotel.confirmBookingOverlay.commissionSecondItem.toString().split(" ").getAt(1).toString().toFloat()
        assertionEquals(commissionOverlay,commission,"commission is not matching.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.itineraryPageStatus,"Itinerary page not displayed")
    }

    def "TC21: verfiyBookedSecondItemAfterPayment"(){
        given: "user is on itinerary page."

        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem1.expectedBookedItemBookingID.toString().contains(resultData.hotel.confirmBookingOverlay.bookingIDSecondItem.toString()),"booking id not matched" )
        assertionEquals(resultData.hotel.itineraryPage.bookedItem1.expectedItemStatus,defaultData.expected.bookingConfirmedStatus,"Confirmed status not displayed.")
        assertionEquals(resultData.hotel.itineraryPage.itemSectionAmendStatus1,defaultData.expected.amendStatus,"Amend Status not displayed.")
        assertionEquals(resultData.hotel.itineraryPage.itemCancelStatus1,defaultData.expected.cancelStatus,"Cancel status not displayed for booked items")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem1.expectedHotelName,resultData.hotel.itineraryPage.hotelItem2.expectedHotelName,"HotelName not matching after payment.")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem1.expectedStarRating,resultData.hotel.itineraryPage.hotelItem2.expectedStarRating,"Star Rating not matching after payment.")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem1.expectedItemDesc,resultData.hotel.itineraryPage.hotelItem2.expectedItemDesc,"Desc not matching")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem1.expectedItemDuration.toString().contains(resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration),"Duration is not matching after payment.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem1.expectedItemDuration.toString().startsWith(resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration),"date not matching after payment.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.hotel.itineraryPage.travelerOne.toString()),"traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.hotel.itineraryPage.travelerTwo.toString()),"traveler 2 is not matching with traveler details section")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice,resultData.hotel.itineraryPage.bookedItem1.expectedItemPrice,"price is not matching after payment.")
    }

}
