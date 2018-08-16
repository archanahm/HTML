package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.PaymentPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import org.testng.Assert
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.paymentDetails

/**
 * Created by mtmaku on 9/19/2017.
 */
    class PyaNowMultiRoomsTest extends PaymentSpec {
    @Shared
    //Client Login
    ClientData clientData = new ClientData("client697")
   // ClientData clientData = new ClientData("auTakeGrossCredentialss")
    @Shared
    public static Map<String, PaymentsTestResultData> resultMap = [:]
    @Shared
    PaymentsTestData paymentsData = new PaymentsTestData("PayNowMultiRoom", "payments", paymentDetails)
    @Shared
    PaymentsTestResultData resultData = new PaymentsTestResultData("PayNowMultiRoom", paymentsData)
    @Shared
    CitySearchData citySearchData = new CitySearchData("sanityData")
    @Shared
    HotelTransferTestResultData resultData1 = new HotelTransferTestResultData(citySearchData)

    def "TC00: Agent POS Free Cancellation Take Gross and PayLater"() {
        given: "user logeed in"
        resultMap.put(paymentsData.hotelName, resultData)
        login(clientData)
        at HotelSearchResultsPage
        searchHotelForMultiRoomAndSubmit(paymentsData.input.area.toString(), paymentsData.input.areaAutoSuggest.toString(), paymentsData.input.checkInDays.toString(), paymentsData.input.checkOutDays.toString(), paymentsData.input.noOfAdults.toString(), paymentsData.input.child,paymentsData.input.rooms.toString())
        //  search Results returned
        if(searchResultsReturned()) {
            resultData.hotel.searchResults.actualSearchResults = searchResultsReturned()
        }
        else{
            Assert.fail("Search Results not returned")
        }
        int index = getAddToItineraryCancellationLinkIndex(paymentsData.expected.hotelCancelTxt, paymentsData.expected.statusTxt)
        resultData.hotel.searchResults.hotelItem = storeHotelItemDetails(index,0)
        resultData.hotel.searchResults.cancellationLinkStatus1 = getCancellationLinkText(index, 0)
        resultData.hotel.searchResults.cancellationLinkStatus2 = getCancellationLinkText(index, 1)
        clickAddToItinry(paymentsData.expected.hotelCancelTxt, paymentsData.expected.statusTxt)
        itineraryNameUpdate(paymentsData)
        enterAagentRef(defaultData)
        enterTravelerDetails(citySearchData,0,resultData1)
        resultData.hotel.itineraryPage.actualSavedAgentRefName=getSavedAgentRefName()
        String itineraryId = getItineraryId()
        resultData.hotel.itineraryPage.itineraryID = itineraryId.split(" ").getAt(1)
        resultData.hotel.itineraryPage.itineraryName = itineraryId.split(" ").getAt(2)

        sleep(2000)
        resultData.hotel.itineraryPage.travellerDetails = getTravellers()
        resultData.hotel.itineraryPage.hotelItem = storeItemDetails(0,1,"hotel")
        resultData.hotel.itineraryPage.cancellationLinkStatus = getItinenaryFreeCnclTxtInSuggestedItem(0)
        resultData.hotel.itineraryPage.hotelItem2 = storeItemDetails(1,1,"hotel")
        resultData.hotel.itineraryPage.cancellationLinkStatus2 = getItinenaryFreeCnclTxtInSuggestedItem(1)
        clickOnBookIcon()
        waitTillLoadingIconDisappears()
        bookTCOverlay(paymentsData, resultData)
        bookMultiRoomItem()
        paymentPage_payNow(defaultData, resultData)
        paymentSummeryBlock(resultData,1,1)
        selectPaymentType(resultData)
        cardDetails(defaultData.expected.paymentType,defaultData.expected.cardNum,defaultData.expected.startDate,defaultData.expected.startYear,defaultData.expected.expireDate,defaultData.expected.expireYr,defaultData.expected.cardHolder,defaultData.expected.securityCode,resultData,0)
        savedAddress(resultData)
        makePaymentButton(resultData)
        bookingConfirmedOverlay(resultData,0)
        ItineraryPageAfterPayment(resultData,0,0)
        resultData.hotel.itineraryPage.bookedItem2 =  storeItemDetails(1,0,"hotel")
        resultData.hotel.itineraryPage.item2Details = storeItineraryPageItemDetails(1,0)
    }
    def "TC01: verifySearchHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        softAssert.assertTrue( resultData.hotel.searchResults.actualSearchResults, "Hotel results page not displayed")
        //Validation of free cancellation exists for 1st room s
        softAssert.assertTrue(resultData.hotel.searchResults.cancellationLinkStatus1.toString().contains(paymentsData.expected.hotelCancelTxt),"Free cancellation did not display")
        //Validation of free cancellation exists for 2nd room
        softAssert.assertTrue(resultData.hotel.searchResults.cancellationLinkStatus2.toString().contains(paymentsData.expected.hotelCancelTxt),"Free cancellation did not display")
    }
    def "TC02: verifyItineraryPage"() {
        given: "User is at Itinerary page"

        assertionEquals(resultData.hotel.itineraryPage.actualSavedAgentRefName,defaultData.input.agentRef,"Itinerary Page, Agent Reference Saved Details Actual & Expected Don't Match")

        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(0),citySearchData.input.title_txt+" "+citySearchData.input.firstName+" "+citySearchData.input.lastName,"traveler 1 is not saved")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(1),defaultData.expected.traveler1[2],"Email addr not saved.")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(2),defaultData.expected.traveler1[1],"Phone number not saved.")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(3).toString(),citySearchData.input.title_txt+" "+citySearchData.input.travellerfirstName+" "+citySearchData.input.travellerlastName.toString(),"traveler two not displayed")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(4).toString(),citySearchData.input.title_txt+" "+citySearchData.input.travellerfirstName+" "+citySearchData.input.travellerlastName.toString(),"traveler three not entered")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(5).toString(),citySearchData.input.title_txt+" "+citySearchData.input.travellerfirstName+" "+citySearchData.input.travellerlastName.toString(),"traveler 4 not saved")

    }

    def "TC03: verifyBookAnItem"(){
            //Hotel name
            assertionEquals(resultData.hotel.searchResults.hotelItem.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName, "HotelName not matching as expected.")
            //star rating
            assertionEquals(resultData.hotel.searchResults.hotelItem.starRating,resultData.hotel.itineraryPage.hotelItem.expectedStarRating,"Hotel Rating did not match as expected.")
            // room type ,
            softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(resultData.hotel.searchResults.hotelItem.roomName.toString()),"\n Room descritption did not match")
            //Pax
            softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(resultData.hotel.searchResults.hotelItem.adults.toString()),"\n Adults not matched.")
            //available text
            assertionEquals(resultData.hotel.searchResults.hotelItem.status,resultData.hotel.itineraryPage.hotelItem.expectedItemStatus,"Avaliable status not matched.")
            //commission
            assertionEquals(resultData.hotel.searchResults.hotelItem.commission.toString().tokenize(" ").get(1).toString(),resultData.hotel.itineraryPage.hotelItem.expectedCommission.toString().tokenize(" ").get(0).toString(),"Commission is not matching.")
            //check in date , number of nights
            assertionEquals(resultData.hotel.itineraryPage.expectedCheckInNights.toString().replaceAll(" ","").toUpperCase(),resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().replaceAll(" ","").toUpperCase(),"CheckinDate and nights did not match")
            //Price & Currency
            assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,resultData.hotel.searchResults.hotelItem.priceAndCurrency,"Price And Currency Text Actual And Expected Don't Match")
            //Cancellation Text
            assertionEquals(resultData.hotel.searchResults.hotelItem.CancellationLinkTxt,resultData.hotel.itineraryPage.hotelItem.expectedItemCancellationLinkText,"Cancellation link did not match.")

    }

    def "TC04: verifyBookAnItemForRoom2"(){
        //Hotel name
        assertionEquals(resultData.hotel.searchResults.hotelItem.hotelName,resultData.hotel.itineraryPage.hotelItem2.expectedHotelName, "HotelName not matching as expected.")
        //star rating
        assertionEquals(resultData.hotel.searchResults.hotelItem.starRating,resultData.hotel.itineraryPage.hotelItem2.expectedStarRating,"Hotel Rating did not match as expected.")
        // room type ,
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemDesc.toString().contains(resultData.hotel.searchResults.hotelItem.roomName.toString()),"\n Room descritption did not match")
        //Pax
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemDesc.toString().contains(resultData.hotel.searchResults.hotelItem.adults.toString()),"\n Adults not matched.")
        //available text
        assertionEquals(resultData.hotel.searchResults.hotelItem2.status,resultData.hotel.itineraryPage.hotelItem.expectedItemStatus,"Avaliable status not matched.")
        //commission
        assertionEquals(resultData.hotel.searchResults.hotelItem.commission.toString().tokenize(" ").get(1).toString(),resultData.hotel.itineraryPage.hotelItem2.expectedCommission.toString().tokenize(" ").get(0).toString(),"Commission is not matching.")
        //check in date , number of nights
        assertionEquals(resultData.hotel.itineraryPage.expectedCheckInNights.toString().replaceAll(" ","").toUpperCase(),resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration.toString().replaceAll(" ","").toUpperCase(),"CheckinDate and nights did not match")
        //Price & Currency
        assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice,resultData.hotel.searchResults.hotelItem.priceAndCurrency,"Price And Currency Text Actual And Expected Don't Match")
        //Cancellation Text
        assertionEquals(resultData.hotel.searchResults.hotelItem.CancellationLinkTxt,resultData.hotel.itineraryPage.hotelItem2.expectedItemCancellationLinkText,"Cancellation link did not match.")
        //Verfication of booking T & C overlay
        softAssert.assertFalse(resultData.hotel.itineraryOverlay.hotelName.toString().isEmpty(),"Booking T&C overlay not displayed.")
    }

    def "TC05: verifyBookTCOverlay"() {
        given: "user is on T & C Overlay"
        // 1st Hotel card hotelName validation
        assertionEquals(resultData.hotel.itineraryOverlay.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"Hotel name not displayed in overlay." )
        //Validation of traveler details check box status before selecting
        resultData.hotel.itineraryOverlay.checkBoxCheckedStatus.each {
            softAssert.assertTrue(it.equals(false),"checkbox ${it} is selected by default for multiroom travelers ")
        }
        //Validation of traveler details for 1st hotel card
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(0).toString().startsWith(citySearchData.input.firstName.toString()),"traveler1 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(1).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler2 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(2).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler3 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(3).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler4 is not dsiplayed in T & C Overlay")
        //Validation of traveler details for 2nd hotel card
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(4).toString().startsWith(citySearchData.input.firstName),"traveler1 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(5).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler2 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(6).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler3 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(7).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler4 is not dsiplayed in T & C Overlay")
        //Special remarks section valdiations for 1st hotel card
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.expandStatus,"Clicking on remarks link not expanded")
        softAssert.assertFalse(resultData.hotel.itineraryOverlay.colapseStatus,"Clicking on remarks link not collapsed.")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.bookNowEnabledStatus,"Book now button is disabled.")
        //Special remarks section valdiations for 2nd hotel card
        assertionEquals(resultData.hotel.itineraryOverlay.hotelName2,resultData.hotel.itineraryPage.hotelItem2.expectedHotelName, "Second Hotel card hotel name not matching.")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.expandStatus2,"user is unable to expand Add special remark section")
        softAssert.assertFalse( resultData.hotel.itineraryOverlay.colapseStatus2,"User is unable to collapse special remarks section")

    }
    def "TC06: verifyPaymentPagePayNow"(){
        given: "user is on payment page"
        verifyPaymentPagePayNow(resultData)
    }
    def "TC07: verifyPaymentSummeryBlock"(){
        given: "user is on payment page"
        verifyPaymentSummeryBlock(resultData,clientData)
    }

    def "TC08: verifyselectPaymentType"(){
        given: "user is on payment page"
        verifyselectPaymentType(resultData)
    }
    def "TC09: verfiyCardDetails"(){
        given: "user is on payment page"
        verfiyCardDetails(resultData,defaultData.expected.cardNum,defaultData.expected.securityCode)
    }
    def "TC10: verifySavedAddr"(){
        softAssert.assertTrue(resultData.hotel.paymentpage.savedAddressTxtdispStatus, "Saved addr text not displayed")
        //Radio button display status
        softAssert.assertTrue(resultData.hotel.paymentpage.savedAddressRadioBtnDispStatus,"radio button for saved addr not displayed")

    }
    def "TC11: verifyMakePaymentButton"(){
        given: "user is on payment page"
        verifyMakePaymentButton(resultData)
    }
    def "TC12: verifyBookingConfirmedOverlay"(){
        given: "user is on booking confirmation page"
        softAssert.assertNotNull(resultData.hotel.confirmBookingOverlay.header,"Booking confirmed header not displayed.")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.logo,"Traveler Cube Logo is not displayed." )
      //  assertionEquals(resultData.hotel.confirmBookingOverlay.bookingID,resultData.hotel.itineraryPage.bookedItem.expectedBookedItemBookingID,"Booking id is not matching.") //as mutliroom would be in pending state in confirmation overlay
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryID.toString()),"Itinerary ID not matching")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryName.toString()),"Itinerary name is not matching")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.travelers.getAt(0).toString().contains(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString()),"traveler1 not matching with itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.travelers.getAt(1),resultData.hotel.itineraryPage.travellerDetails.getAt(3),"traveler2 not matching with itinerary")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().contains(resultData.hotel.confirmBookingOverlay.depatureDate),"depature date is not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"Hotel name not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.addr,paymentsData.expected.hotelAddr,"hotel addr is not matching.")
        String pax = resultData.hotel.confirmBookingOverlay.paxDesc.replaceAll("\\n"," ").split("S").getAt(1)
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(pax),"pax are not matching against itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.date,resultData.hotel.itineraryPage.hotelItem.expectedItemDuration,"date is not matching")
        assertionEquals(resultData.hotel.confirmBookingOverlay.price,resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,"price is not matching with itinerary")
        Float commission = resultData.hotel.confirmBookingOverlay.price.toString().split(" ").getAt(0).toString().toFloat()
        commission = (commission*12)/100
        Float commissionOverlay = resultData.hotel.confirmBookingOverlay.commission.toString().split(" ").getAt(1).toString().toFloat()
        assertionEquals(commissionOverlay,commission,"commission is not matching.")

        assertionEquals(resultData.hotel.confirmBookingOverlay.hotelName2,resultData.hotel.itineraryPage.hotelItem2.expectedHotelName,"Hotel name not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.addr2,paymentsData.expected.hotelAddr,"hotel addr is not matching.")
        String pax1 = resultData.hotel.confirmBookingOverlay.paxDesc2.replaceAll("\\n"," ").split("S").getAt(1)
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem2.expectedItemDesc.toString().contains(pax),"pax are not matching against itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.date2,resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration,"date is not matching")
        assertionEquals(resultData.hotel.confirmBookingOverlay.price2,resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice,"price is not matching with itinerary")

    }

        def "TC11: verifyItineraryPageAfterPayment"(){
            given: "user is on itinerary page"
            verifyItineraryPageAfterPayment(resultData)
            softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem2.expectedBookedItemBookingID.toString().contains(resultData.hotel.confirmBookingOverlay.bookingID),"booking id not matched" )
            assertionEquals(resultData.hotel.itineraryPage.bookedItem2.expectedItemStatus,defaultData.expected.bookingConfirmedStatus,"Confirmed status not displayed.")
            assertionEquals(resultData.hotel.itineraryPage.item2Details.itemSectionAmendStatus,defaultData.expected.amendStatus,"Amend Status not displayed.")
            assertionEquals(resultData.hotel.itineraryPage.item2Details.itemCancelStatus,defaultData.expected.cancelStatus,"Cancel status not displayed for booked items")
            assertionEquals(resultData.hotel.itineraryPage.bookedItem2.expectedHotelName,resultData.hotel.itineraryPage.hotelItem2.expectedHotelName,"HotelName not matching after payment.")
            assertionEquals(resultData.hotel.itineraryPage.bookedItem2.expectedStarRating,resultData.hotel.itineraryPage.hotelItem2.expectedStarRating,"Star Rating not matching after payment.")
            assertionEquals(resultData.hotel.itineraryPage.bookedItem2.expectedItemDesc,resultData.hotel.itineraryPage.hotelItem2.expectedItemDesc,"Desc not matching")
            softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem2.expectedItemDuration.toString().contains(resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration),"Duration is not matching after payment.")
            softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem2.expectedItemDuration.toString().startsWith(resultData.hotel.itineraryPage.hotelItem2.expectedItemDuration),"date not matching after payment.")
            softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(4).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler 1 is not matching with traveler details section")
            softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(5).toString().endsWith(citySearchData.input.travellerlastName.toString()),"traveler 2 is not matching with traveler details section")
            assertionEquals(resultData.hotel.itineraryPage.hotelItem2.expectedItemPrice,resultData.hotel.itineraryPage.bookedItem2.expectedItemPrice,"price is not matching after payment.")
            assertionEquals(resultData.hotel.itineraryPage.hotelItem2.totalPrice,resultData.hotel.itineraryPage.bookedItem2.totalPrice,"total price is not matching after booking")

        }

}