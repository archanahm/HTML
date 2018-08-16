package com.kuoni.qa.automation.test.traveler

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.TravelerTestResultData
import com.kuoni.qa.automation.page.ActivityPDPPage
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.hotel.AgentHotelInfoPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.traveller.TravelerViewMyItineraryPage
import com.kuoni.qa.automation.test.search.booking.hotel.itinerary.MultipleModuleBaseSpec
import com.kuoni.qa.automation.page.transfers.ShareItineraryTransfersPage
import com.kuoni.qa.automation.page.transfers.ShareItineraryPage
import geb.waiting.WaitTimeoutException

import static org.junit.Assert.assertNull

/**
 * Created by mtmaku on 11/21/2016.
 */
abstract class TravelerSiteBaseSpec extends ItineraryNShareItineraryBaseSpec {

    protected def printItineraryDetails(HotelTransferTestResultData resultData){
        println("Itinerary ID"+resultData.hotel.itineraryPage.expectedItnrIDName)
    }
    protected def travelerHeader(HotelSearchData data,HotelTransferTestResultData resultData){
        at TravelerViewMyItineraryPage
        def dataMap = [:]
        dataMap.welcomeHeader = getWelcomeMessage()
        dataMap.welcomeHeader = dataMap.welcomeHeader.replaceAll("\\n", " ");
      //  dataMap.welcomeHeader2 = getWelcomeMessage2()
        clickOnLaungDropDown(1)
        sleep(1000)
        dataMap.languageOptions = getLanguageOptions()
        dataMap.lightBoxHeader = viewMyItineraryLabel(data.expected.travelerHeaderText)
        dataMap.itineraryIDPlaceHolderTxt = getItineraryTextFieldPlaceholder(0)
        dataMap.leadNamePlaceHolderTxt = getLeadNameTextFieldPlaceHolder()
        //view itinerary button
        dataMap.itineraryButton = verifyIfViewItineraryButton()
        dataMap.footerContent1 = getFooterText(0)
        dataMap.footerContent2 = getFooterText(0)
        clickOnRegisterHere()
        sleep(1000)
        dataMap.registerLightBoxContent = RegisterLightBoxLabel()
        dataMap.registerLighBoxContent2 = getRegisterLightBoxContent2()
        dataMap.registerBoxEmailPlaceHolder = getRegisterBoxEmailPlaceHolder()
        clickOnClose()
        sleep(500)
        dataMap.RegisterLinkTxt = getRegisterHereTxt()
        dataMap.LoginLinkTxt = getLoginHereTxt()
        dataMap.LoginLinkTxt = dataMap.LoginLinkTxt.toString().trim()
        enterItineraryId(resultData.hotel.shareItineraryPage.overlayIID)
        enterSurname("fir-s't tLN la-s't")
        clickOnViewItineraryButton()
        sleep(2000)
        dataMap
    }




    protected sampleTravelerSiteTC(HotelTransferTestResultData resultData, HotelSearchData data, TravelerTestResultData tData) {

        at TravelerViewMyItineraryPage
        enterItineraryId(resultData.hotel.shareItineraryPage.overlayIID)
        enterSurname("fir-s't tLN la-s't")
        clickOnViewItineraryButton()
        sleep(2000)
        clickOnCollapseSeeMoreDetailsLink(0)
        sleep(2000)
        // clickOnViewMore(1)
        //dataMap.hotelFacilitySection = verifyHotelFacilitiesIconSection(0)
        dataMap.hotelFacilities = getTravelerHotelFacilities(0)
    }

    protected def travelerTripOverViewDetails() {
        def dataMap = [:]
        dataMap.travelerItineraryID = gettravelerItineraryID()
        String trimSpaces = dataMap.travelerItineraryID.toString().replaceAll("\\(", " ").replaceAll("\\)", " ").replaceAll("\\s+", " ")
        String[] splitDetails = trimSpaces.split("\\s+")
        dataMap.travelerItineraryID = splitDetails[0]
        dataMap.travelerItineraryName = splitDetails[1]
        dataMap.travelerStartDate = splitDetails[2]
        dataMap.endDate = splitDetails[4]
        dataMap.travelerPPLCountNLabel = getHeaderTravelerLabels(0)
        String[] splitPeopleDetails = dataMap.travelerPPLCountNLabel.toString().split(":")
        dataMap.travellingLabel = splitPeopleDetails[0] + "" + ":"
        List<String> ppl = splitPeopleDetails[1].tokenize(" ")
        dataMap.travelerPeopleCount = ppl.get(0).toInteger()
        dataMap.travelerPeopleLabel = ppl.get(1)
        dataMap.travelerLeadNameLabel = getHeaderTravelerLabels(3)
        dataMap.travelerLeadName = getHeaderTravelerDetails(2)
        dataMap.travelerLeadName =  dataMap.travelerLeadName.toString().trim()
        dataMap.travelerTwoLabel = getHeaderTravelerLabels(4)
        dataMap.travelerTwoName = getHeaderTravelerDetails(3)
        dataMap.travelerThreeeLabel = getHeaderTravelerLabels(5)
        dataMap.travelerThreeName = getHeaderTravelerDetails(4)
        dataMap.tCurrenyNPrice = getCurrencyNPriceInHeader()
        String[] splitText = dataMap.tCurrenyNPrice.toString().split("\\s+")
        dataMap.travelerCurrency = splitText[0]
        dataMap.travelerTotalPrice = splitText[1]
        dataMap.travelerPhoneNumLabel = getHeaderTravelerLabels(2)
        dataMap.travelerPhoneNum = getHeaderTravelerDetails(1)
        dataMap.travelerEmailAddrLabel = getHeaderTravelerLabels(1)
        dataMap.travelerEmailAddr = getHeaderTravelerDetails(0)



        dataMap
    }

    def storeTravelerHotelCardDetails(int index){
        at TravelerViewMyItineraryPage
       def dataMap = [:]
        dataMap.comment = getHotelItemComment()
        dataMap.viewMoreText = getViewMoreLinkText(index)
        clickOnViewMore(index)
        sleep(1500)
        dataMap.imgURL = getHotelImg(index)
        dataMap.mapStatus = verifyMapdisplayedStatus()
        dataMap.hotelInfo1 = getTravelerHotelInfoStrong()
        dataMap.hotelInfo2 = getTravelerListHotelInfoText2(index+1)
        dataMap.hotelInfo3 = getTravlerHotelInfoHeaders()
        dataMap.hotelFacilities =  getTravelerHotelFacilities(index+1)
        dataMap.viewMoreFunctionality = verifyViewMoreStatus()
        dataMap.hotelFacilitySection = verifyHotelFacilitiesIconSection()
        clickOnViewMore(index)
        sleep(1000)
        dataMap.viewLessFunctionality = verifyViewMoreStatus()
        sleep(1000)
        clickOnRoomViewMoreLink(index)
        sleep(1000)
        dataMap.roomFacilitiesExpandStatus = verifyRoomViewMoreExpandStatus()
        dataMap.roomDesc = getRoomInfoText(index)
        dataMap.roomFacilities = getTravlerRoomFacilitiesInfo(index)
        sleep(500)
        clickOnRoomViewMoreLink(index)
        sleep(1000)
        dataMap.roomFacilitiesCollapseStatus = verifyRoomViewMoreExpandStatus()
        dataMap.seeFewDetailsLink = verifyClickOnFewDetailsLink(index+1)
        dataMap.fewDetailsLinkText = getFewDetailsLinkText(index+1)
        clickOnFewDetailsLink(index+1)
        dataMap
    }
    def storeItemsInItinerary(HotelTransferTestResultData resultData) {
        resultData.hotel.itineraryPage.itineraryDetails = storeItineraryDetails()
        resultData.hotel.itineraryPage.itineraryTotalPrice = totalBookedItemsPrice(0)
    }
    def storeTravelerSiteDetails(TravelerTestResultData tData){
        dataMap.hotel1Card = travelerHotelCardDetails(0,0)
        tData.traveler.viewMyItinerary.hotel1Info = storeTravelerSiteHotelInfo(0)
    }

    def storeTravelerSiteHotelInfo(int hotelNum){
        def dataMap = [:]
        at TravelerViewMyItineraryPage
        dataMap.hotelHeader = getTravelerHotelInfoHeader(hotelNum)
        clickOnViewMore(hotelNum)
        dataMap.hotelInfoHeaders = getTravlerHotelInfoStrong()
        dataMap.hotelFacilityHeaders = getTravlerHotelInfoHeaders()
        dataMap.hotelInfo = getTravelerListHotelInfoText2(hotelNum)
        dataMap.hotelFacilityIcons = getTravelerHotelFacilities(hotelNum)
        dataMap.roomFacilities = getTravlerRoomFacilitiesInfo(hotelNum)
    }
    protected def storeTravelerValidations(HotelSearchData data, HotelTransferTestResultData resultData,TravelerTestResultData tData){
        tData.traveler.viewMyItinerary.viewMyItineraryLogin = travelerHeader(data,resultData)
    }

    /* Start of Test case Validations */


protected def "VerifyViewMyItineraryLogin"(HotelSearchData data,TravelerTestResultData tData){
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.welcomeHeader,data.expected.welcomeMsg,"welcome msg is not as expected")
  //  assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.welcomeHeader2,data.expected.welocmeMsg2,"Login into view.. Msg is not as expected")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.languageOptions.get(3),data.expected.lang1,"English lang is not present in drop down")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.languageOptions.get(4),data.expected.lang2,"French Lang is not present in drop down")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.languageOptions.get(5),data.expected.lang3,"Jerman Lang is not present in drop down")
    softAssert.assertTrue(tData.traveler.viewMyItinerary.viewMyItineraryLogin.lightBoxHeader,"view my itinerary header is not present in traveler site")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.itineraryIDPlaceHolderTxt,data.expected.placeHolderTextField1,"Itinerary ID place holder is not as expected")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.leadNamePlaceHolderTxt,data.expected.placeHolderTextField2,"Lead Passenger place holder is not as expected")
    softAssert.assertTrue(tData.traveler.viewMyItinerary.viewMyItineraryLogin.itineraryButton,"Itineary button is not present")
    softAssert.assertTrue(tData.traveler.viewMyItinerary.viewMyItineraryLogin.footerContent1.toString().startsWith(data.expected.footerContent1),"Are you the lead .. msg not displayed as expected")
    softAssert.assertTrue(tData.traveler.viewMyItinerary.viewMyItineraryLogin.footerContent2.toString().contains(data.expected.footerContent2),"Have an account? msg not displayed as expected")
    softAssert.assertTrue(tData.traveler.viewMyItinerary.viewMyItineraryLogin.registerLightBoxContent,"Register for a profile.. not displayed as expected")
    assertionEquals( tData.traveler.viewMyItinerary.viewMyItineraryLogin.registerLighBoxContent2,data.expected.registerLightBoxContent2,"Enter the.. not displayed as expected")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.registerBoxEmailPlaceHolder,data.expected.registerLightBoxEmail,"email addr place holder is not matching as expected result")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.RegisterLinkTxt,data.expected.registerLink,"register text and link not present")
    assertionEquals(tData.traveler.viewMyItinerary.viewMyItineraryLogin.LoginLinkTxt,data.expected.loginLink,"login text and link not present")

}


}








