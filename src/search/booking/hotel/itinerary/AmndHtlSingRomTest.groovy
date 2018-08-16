package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.ShareItineraryTransfersPage
import com.kuoni.qa.automation.util.DateUtil
import org.testng.Assert
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itineraryTravellerDetail

/**
 * Created by kmahas on 7/4/2016.
 */
class AmndHtlSingRomTest extends TravellerDetailBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")
    //ClientData clientData = new ClientData("client21")
    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("Cambridge-SITUJ18XMLAuto", "itinerarytravellerdetail", itineraryTravellerDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("Cambridge-SITUJ18XMLAuto",hotelSearchData)


    def "TC00: Add To Itinerary, Add, Edit & Verify Traveller Details "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        login(clientData)
        addToItinerary(clientData, hotelSearchData, resultData)

        at ItenaryBuilderPage
        hideItenaryBuilder()

        String expectedPrice=getItenaryPrice(0)
        println("ExpectedPrice: $expectedPrice")
        resultData.hotel.searchResults.itineraryBuilder.expectedPrice=expectedPrice
        //click Go to Itinerary Link
        clickOnGotoItenaryButton()
        sleep(2000)

        at ItineraryTravllerDetailsPage

        //Capture Traveller Label Text
        //String actualTravellerLabelTxt=getTravllerLabelTextInTravellerDetailsPage()
        String actualTravellerLabelTxt=getTravellelersLabelName(0)
        //Validate Itinerary Page Traveller Label Txt
        assertionEquals(actualTravellerLabelTxt, hotelSearchData.expected.travellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")

        //Input Title
        selectTitle(hotelSearchData.expected.title_txt,0)

        //Input First Name
        enterFirstNameAndTabOut(hotelSearchData.expected.firstName,0)

        //Input Last Name
        enterLastNameAndTabOut(hotelSearchData.expected.lastName,0)

        //Input Email Address
        enterEmailAndTabOut(hotelSearchData.expected.emailAddr,0)

        //Input Area Code
        //Commented since 10.3 this field is removed.
        //enterCountryCode(hotelSearchData.expected.defaultcountryCode,0)
        //sleep(1000)
        //Input Telephone number
        enterTelephoneNumberAndTabOut(hotelSearchData.expected.telephone_Num,0)
        boolean actualPhoneNumTickDisplayStatus=getPhoneNumTickMarkDisplayStatus()
        sleep(1000)
        //click Remove button
        clickRemoveButton(0)
        sleep(2000)
        clickRemoveButton(0)
        sleep(2000)
        clickRemoveButton(0)
        sleep(2000)

        //Click on Save Button
        //clickonSaveButton()
        clickonSaveButton(0)
        //sleep(2000)
        waitTillLoadingIconDisappears()
        waitTillTravellerDetailsSaved()

        //Capture the entered details
        //capture entered lead traveller details are displayed correctly
        String expectedleadTravellerName=hotelSearchData.expected.title_txt+" "+hotelSearchData.expected.firstName+" "+hotelSearchData.expected.lastName
        String actualLeadTravellerName=getLeadTravellerName(0)
        //Validate Lead Traveller Name is Saved
        assertionEquals(actualLeadTravellerName,expectedleadTravellerName, "Itinerary Page, Lead Traveller name Actual & Expected don't match")
        //capture entered telephone number are displayed correctly
        String expectedleadTelephoneNum=hotelSearchData.expected.defaultcountryCode+""+hotelSearchData.expected.telephone_Num
        String actualleadTelephoneNum=getLeadTravellerName(1)
        //Validate Telephone number is Saved
        assertionEquals(actualleadTelephoneNum,expectedleadTelephoneNum, "Itinerary Page, Telephone Number Actual & Expected don't match")

        //Capture entered email are displayed correctly
        String actualemailId=getLeadTravellerName(2)
        //Validate Email is Saved
        assertionEquals(actualemailId,hotelSearchData.expected.emailAddr, "Itinerary Page, Email Entered Actual & Expected don't match")

        //Add Travellers 2 - adult To 5 - Adult
        addSaveAndVerifyAdultTravellers(2,5,hotelSearchData)

        //addSavAdultTravellers(2,5,4,hotelSearchData)
        //VerifyAdultTravellers(2,5,4,hotelSearchData)
        //Add Travellers6 - child To 7 - Child
        addSaveAndVerifyChildTravellers(6,9,hotelSearchData,hotelSearchData.input.amendchildren)

        //Book the hotel item

        //click on Book button
        clickOnBookIcon()
        sleep(5000)

        waitTillLoadingIconDisappears()

        //Selecting travellers.

        clickOnTravellerCheckBox(0)
        sleep(1000)
        clickOnTravellerCheckBox(1)
        sleep(1000)
        clickOnTravellerCheckBox(5)
        sleep(1000)
        clickOnTravellerCheckBox(6)
        sleep(1000)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()
        scrollToTopOfThePage()
        //Booking Confirmation Screen Display Status
        boolean actualBookingconfirmaitonDispStatus
        try {
            actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            softAssert.assertFalse(true,"Failed To Confirm Booking")
        }

        assertionEquals(actualBookingconfirmaitonDispStatus,hotelSearchData.expected.dispStatus, "Booking Confirmation Screen display status Actual & Expected don't match")

        //Booking confirmed lightbox displayed

        //Title text-Booking Confirmed
        String actualBookingConfirmedTitleText=getCancellationHeader()
        assertionEquals(actualBookingConfirmedTitleText,hotelSearchData.expected.bookingConfrmTitleTxt, "Booking Confirmation Screen, title text Actual & Expected don't match")

        //read Booking id
        String retrievedbookingID=getBookingIdFromBookingConfirmed(0)
        println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")

        //read Itineary Reference Number & name
        String readitinearyIDAndName=getItinearyID(0)

        //item name
        String readItemName=getConfirmBookedTransferName()

        //item address
        String readItemAddressTxt=getTransferDescBookingConfirmed()

        //Close Booking confirmation
        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //scroll top
        scrollToTopOfThePage()

        //click remove
        clickRemoveIcon(1)
        sleep(6000)

        boolean actualTravellerCannotBeRemovedPopupDispStatus=getTravellerCannotBeDeletedPopupDisplayStatus()
        assertionEquals(actualTravellerCannotBeRemovedPopupDispStatus,hotelSearchData.expected.dispStatus, "Traveller Could not removed popup Screen, display status Actual & Expected don't match")

        //The traveller could not be removed
        String actualTravellerCannotBeRemovedTxt = getTravellerCannotBeDeletedheaderText()
        assertionEquals(actualTravellerCannotBeRemovedTxt,hotelSearchData.expected.travellerCannotBeRemovedTxt, "Traveller Could not removed popup Screen,  text Actual & Expected don't match")

        //Close X function
        boolean actualCloseXDisplayStatus=overlayCloseButton()
        assertionEquals(actualCloseXDisplayStatus,hotelSearchData.expected.dispStatus, "Traveller Could not removed popup Screen, display status Actual & Expected don't match")
        //The traveller could not be removed because they are assigned to the following text should show
        String actualTravellerInnerTxt=getTravellerCannotBeDeletedinsideText()
        assertionEquals(actualTravellerInnerTxt,hotelSearchData.expected.travellerInsideTxt, "Traveller Could not removed popup Screen,  inner text Actual & Expected don't match")

        //Item image

        //Item Name
        String actualItemName=getItemNameInNonAmendablePopup()
        assertionEquals(actualItemName,hotelSearchData.expected.cityAreaHotelText, "Traveller Could not removed popup Screen,  item name text Actual & Expected don't match")

        //Item star rating
        String actualItemStarRating=getItemStarRatingInNonAmendablePopup()
        assertionEquals(actualItemStarRating,hotelSearchData.expected.starRatingTxt, "Traveller Could not removed popup Screen,  item star rating text Actual & Expected don't match")

        String roomdescText=getItemDescriptionInNonAmendablePopup()
        List<String> tempItinryCanclDescList=roomdescText.tokenize(",")

        String roomDscText=tempItinryCanclDescList.getAt(0)
        String actualroomdescTxt=roomDscText.trim()
        //booked room type
        assertionEquals(actualroomdescTxt,hotelSearchData.expected.roomDescTxt, "Traveller Could not removed popup Screen,  booked room type text Actual & Expected don't match")

        //Pax number requested
        String paxCanTxt=tempItinryCanclDescList.getAt(1)
        List<String> paXCanDescList=paxCanTxt.tokenize(".")

        String paxNumTxt=paXCanDescList.getAt(0)
        String actualPaxNum=paxNumTxt.trim()
        // number of pax
        assertionEquals(actualPaxNum,hotelSearchData.expected.paxTxt, "Traveller Could not removed popup Screen,  Pax Num text Actual & Expected don't match")

        //Rate plan - meal basis
        String planMealBasistxt=paXCanDescList.getAt(1)
        String actualratePlan=planMealBasistxt.trim()
        //meal basis
        assertionEquals(actualratePlan,hotelSearchData.expected.mealBasisTxt, "Traveller Could not removed popup Screen,  Meal Basis text Actual & Expected don't match")

        //check in date (ddMMMyy) and stay night
        int nights=0
        String actualCheckInDateDuration=getCheckInDateAndDurationInNonAmendablePopup()
        def checkInDt=dateUtil.addDaysChangeDatetformat(hotelSearchData.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        nights=hotelSearchData.input.checkOutDays.toString().toInteger()-hotelSearchData.input.checkInDays.toString().toInteger()
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        assertionEquals(actualCheckInDateDuration,dur, "Traveller Could not removed popup Screen,  actual check in and duration text Actual & Expected don't match")

        //confirmation status
        String actualStatus=getStatusInNonAmendablePopup()
        resultData.hotel.confirmationPage.actualStatus=actualStatus
        //assertionEquals(actualStatus,hotelSearchData.expected.statusTxt, "Traveller Could not removed popup Screen,  status text Actual & Expected don't match")

        //Commission and % value
        String actualCommissionPercntTxt=getCommissionAndPercentInNonAmendablePopup()
        resultData.hotel.confirmationPage.actualCommissionPercntTxt=actualCommissionPercntTxt
        //assertionEquals(actualCommissionPercntTxt,hotelSearchData.expected.commissionTxt, "Traveller Could not removed popup Screen,  Commission Percent text Actual & Expected don't match")

        //total item rate and currency
        String actualtotalRateAndCurncy= getTotalAmntAndCurrencyInNonAmendablePopup()
        println("TotalAndCurrency:$actualtotalRateAndCurncy")
        resultData.hotel.confirmationPage.actualtotalRateAndCurncy=actualtotalRateAndCurncy
        //assertionEquals(actualtotalRateAndCurncy,expectedPrice, "Traveller Could not removed popup Screen,  Total Amount And Currency text Actual & Expected don't match")

        //Close popup
        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(3000)
        //Amend
        clickOnCancelOrAmendTabButton(1)
        sleep(3000)

        //Amend Booking screen display status
        boolean actualAmendPopupDispStatus=getTravellerCannotBeDeletedPopupDisplayStatus()
        assertionEquals(actualAmendPopupDispStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, display status Actual & Expected don't match")

        //Amend Booking Txt
        String actualAmendTitleTxt=getCancellationHeader()
        assertionEquals(actualAmendTitleTxt,hotelSearchData.expected.amendTitleTxt, "Amend popup Screen,  Commission Percent text Actual & Expected don't match")

        //Close X function
        actualCloseXDisplayStatus=overlayCloseButton()
        assertionEquals(actualCloseXDisplayStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, close button display status Actual & Expected don't match")

        //should open per default
        boolean actualDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
        assertionEquals(actualDatesTabActiveStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Dates tab display status Actual & Expected don't match")

        //should not open per default
        boolean actualOccupancyTabInactStatus=getOccupancyTabActiveOrInactiveStatus()
        assertionEquals(actualOccupancyTabInactStatus,hotelSearchData.expected.notDispStatus, "Amend popup Screen, Occupancy tab  display status Actual & Expected don't match")

        //Click Occupancy tab
        clickOccupancyTab()

        //Occupancy tab opens up
        boolean actualOccupancyTabactStatus=getOccupancyTabActiveOrInactiveStatus()
        assertionEquals(actualOccupancyTabactStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab after click on Occupancy tab display status Actual & Expected don't match")

        //text should show
        String actualOccupancyTxt=getOccupancyInsideTxtInNonAmendablePopup(1)
        assertionEquals(actualOccupancyTxt,hotelSearchData.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking text Actual & Expected don't match")

        //Item Name
        String actualItemNameInOccupancyTab=getItemNameInOccupanceTab(1)
        assertionEquals(actualItemNameInOccupancyTab,hotelSearchData.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text Actual & Expected don't match")

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(1)
        tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")

        String roomnumAndTypeOfRoomText=tempItinryCanclDescList.getAt(0)
        actualroomdescTxt=roomnumAndTypeOfRoomText.trim()
        String expectedRoomNumAndTypeOfRoomTxt="1x "+hotelSearchData.expected.roomDescTxt
        //booked room type
        assertionEquals(actualroomdescTxt,expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text Actual & Expected don't match")

        //meal basis
        String actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()
        assertionEquals(actualmealBasisTxt,hotelSearchData.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, meal basis text Actual & Expected don't match")

        //Free Cancellation Text
        String actualfreeCancelTxt=getFreeCancelTxtInOccupanceTab(1)
        resultData.hotel.confirmationPage.actualfreeCancelTxt=actualfreeCancelTxt
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(hotelSearchData.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), hotelSearchData.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        String expectedFreeCanclTxt=hotelSearchData.expected.freecancltxt+" "+cancelDate.toUpperCase()
        resultData.hotel.confirmationPage.expectedFreeCanclTxt=expectedFreeCanclTxt
        //assertionEquals(actualfreeCancelTxt,expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //check in, number of night
        String actualCheckInAndNumOfNight=getCheckInTxtInOccupanceTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        assertionEquals(actualCheckInAndNumOfNight,dur, "Amend popup Screen,  Occupancy tab,  actual check in and duration text Actual & Expected don't match")

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")

        String actualPaxText=tempPAXAndTravellersList.getAt(0).trim().replaceAll(" ","")
        assertionEquals(actualPaxText,hotelSearchData.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")

        //travellers
        String actualTravellersText=getTravellersInOccupanceTab(1)
        String expectedTravellersTxt=expectedleadTravellerName+", "+hotelSearchData.expected.title_txt+" "+hotelSearchData.expected.travellerfirstName+" "+hotelSearchData.expected.travellerlastName+",  "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.children.getAt(0) + "yrs)"+",  "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.children.getAt(1) + "yrs)"
        assertionEquals(actualTravellersText,expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")

        //Room Amount and Currency
        String actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInOccupancyTab(1)
        assertionEquals(actualTotalRoomAmntAndCurrncyTxt,expectedPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")
        //Commission
        String actualCommissionTxt=getCommissionInOccupancyTab(1)
        assertionEquals(actualCommissionTxt,hotelSearchData.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text Actual & Expected don't match")

        //Change, add or remove occupants availability:
        String actualChangeOccupantsTxt=getChangeOccupantsTxtInOccupancyTab()
        assertionEquals(actualChangeOccupantsTxt,hotelSearchData.expected.changeOccupantsTxt, "Amend popup Screen,  Occupancy tab,  Change, add or remove occupants availability: text Actual & Expected don't match")

        //Occupancy 1
        String actualOccupantListNumTxt=getOccupantListNumInOccupancyTab(1)
        String expectedOccupantListNumTxt=hotelSearchData.expected.occupantTxt+" 1"
        assertionEquals(actualOccupantListNumTxt,expectedOccupantListNumTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Number 1 text Actual & Expected don't match")

        //Title / first name / last name
        String actualOccupantListNameTxt=getOccupantListNameInOccupancyTab(1)
        assertionEquals(actualOccupantListNameTxt,expectedleadTravellerName, "Amend popup Screen,  Occupancy tab,  Occupant List Name 1 text Actual & Expected don't match")

        //Occupancy 2
        actualOccupantListNumTxt=getOccupantListNumInOccupancyTab(2)
        expectedOccupantListNumTxt=hotelSearchData.expected.occupantTxt+" 2"
        assertionEquals(actualOccupantListNumTxt,expectedOccupantListNumTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Number 2 text Actual & Expected don't match")

        //Title / first name / last name
        actualOccupantListNameTxt=getOccupantListNameInOccupancyTab(2)
        String expectedOccupantListNameTxt=hotelSearchData.expected.title_txt+" "+hotelSearchData.expected.travellerfirstName+" "+hotelSearchData.expected.travellerlastName
        assertionEquals(actualOccupantListNameTxt,expectedOccupantListNameTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Name 2 text Actual & Expected don't match")

        //Occupancy 3
        actualOccupantListNumTxt=getOccupantListNumInOccupancyTab(3)
        expectedOccupantListNumTxt=hotelSearchData.expected.occupantTxt+" 3"
        assertionEquals(actualOccupantListNumTxt,expectedOccupantListNumTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Number 3 text Actual & Expected don't match")

        //Title / first name / last name
        actualOccupantListNameTxt=getOccupantListNameInOccupancyTab(3)
        //expectedOccupantListNameTxt= hotelSearchData.expected.thirdTraveller_title_txt+" "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName
        expectedOccupantListNameTxt=hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName
        assertionEquals(actualOccupantListNameTxt,expectedOccupantListNameTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Name 3 text Actual & Expected don't match")

        //Occupancy 4
        actualOccupantListNumTxt=getOccupantListNumInOccupancyTab(4)
        expectedOccupantListNumTxt=hotelSearchData.expected.occupantTxt+" 4"
        assertionEquals(actualOccupantListNumTxt,expectedOccupantListNumTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Number 4 text Actual & Expected don't match")

        //Title / first name / last name
        actualOccupantListNameTxt=getOccupantListNameInOccupancyTab(4)
        //expectedOccupantListNameTxt=hotelSearchData.expected.thirdTraveller_title_txt+" "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName
        expectedOccupantListNameTxt=hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName
        assertionEquals(actualOccupantListNameTxt,expectedOccupantListNameTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Name 4 text Actual & Expected don't match")

        //Please select occupants names:
        String actualPleaseSelectTxt=getPleaseSelectTxtInOccupancyTab()
        assertionEquals(actualPleaseSelectTxt,hotelSearchData.expected.plzSelTxt, "Amend popup Screen,  Occupancy tab,  Please select occupants names: text Actual & Expected don't match")

        //Scroll
        //scrollToBottomOfThePage()

        //tick box should present for each traveller listed in traveller details section
        travellersListCheckBoxExistenceCheck(1,9,hotelSearchData)

        //Get 1st traveller checked status
        boolean chkBoxStatus=getTravellerCheckedStatus(1)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, First Traveller CheckBox display status Actual & Expected don't match")

        chkBoxStatus=getTravellerCheckedStatus(2)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")

        chkBoxStatus=getTravellerCheckedStatus(6)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Sixth Traveller CheckBox display status Actual & Expected don't match")

        chkBoxStatus=getTravellerCheckedStatus(7)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Seventh Traveller CheckBox display status Actual & Expected don't match")

        //lead traveller
        String actualLeadTraveller=getTravellerNameTxt(1)
        String expectedLeadTravellerTxt=hotelSearchData.expected.firstName+" "+hotelSearchData.expected.lastName+" "+"(lead name)"
        assertionEquals(actualLeadTraveller,expectedLeadTravellerTxt, "Amend popup Screen, Occupancy tab, Lead Traveller Text Actual & Expected don't match")

        //2nd to 5th traveller
        verifyAdultTravellers(2,5,hotelSearchData)
        //6th to 9th traveller
        verifyChildTravellers(6,9,hotelSearchData,hotelSearchData.input.amendchildren)

        //infant text should show - if it is available for the room
        String actualInfantLabTxt=getInfantLabelTxt()
        assertionEquals(actualInfantLabTxt,hotelSearchData.expected.infantLabelTxt, "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

        //dropdown list to select number of infant should show
        boolean actualInfantDropdownListDispStatus=getInfantSelectBoxDisplayStatus()
        assertionEquals(actualInfantDropdownListDispStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")

        //list should show 0 - 1 or 0-2
        List<String> actualListInfantValues=getInfantDropDownValuesInOccupancyTab()
        actualListInfantValues=actualListInfantValues.sort()
        println("Actual List Values:$actualListInfantValues")
        List<String> expectedListInfantValues=hotelSearchData.expected.infantDropDownListValues
        expectedListInfantValues=expectedListInfantValues.sort()
        println("Expected List Values:$expectedListInfantValues")
        assertionEquals(actualListInfantValues,hotelSearchData.expected.infantDropDownListValues,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")

        //Confirm Amendment Button display status
        boolean actualConfirmAmendmentButtonStatus=getConfirmAmendButtonDisplayStatus()
        assertionEquals(actualConfirmAmendmentButtonStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button display status Actual & Expected don't match")

        //Confirm Amendment Button Disabled status
        boolean actualConfirmAmendmentDisabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()
        assertionEquals(actualConfirmAmendmentDisabledStatus,hotelSearchData.expected.notDispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Disabled status Actual & Expected don't match")

        //amend child to adult
        //uncheck 6 & 7
        clickTravellersCheckBox(6)
        sleep(3000)
        clickTravellersCheckBox(7)
        sleep(3000)

        //check 4 & 5
        clickTravellersCheckBox(4)
        sleep(3000)
        clickTravellersCheckBox(5)
        sleep(3000)

        //button enabled
        boolean actualConfirmAmendmentEnabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()
        assertionEquals(actualConfirmAmendmentEnabledStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Enabled (amend child to adult) status Actual & Expected don't match")

        clickConfirmAmendmentBtn()
        sleep(2000)
        //user not able to amend correct error message show - no allow child to adult
        String actualErrMsg=getErrorMsgAdultToChild()
        assertionEquals(actualErrMsg,hotelSearchData.expected.ErrorMsgNoChangeFromChildToAdultTxt, "Amend popup Screen, Occupancy tab, Error Message Child To Adult Actual & Expected don't match")


        //amend over max allow
       //uncheck 4 & 5
        clickTravellersCheckBox(4)
        sleep(2000)
        clickTravellersCheckBox(5)
        sleep(2000)
        //click 2nd traveller
        clickTravellersCheckBox(6)
        sleep(2000)
        //check 7th traveller
        clickTravellersCheckBox(7)
        sleep(2000)
        //check 3rd traveller
        clickTravellersCheckBox(3)
        sleep(2000)

        //button enabled
        actualConfirmAmendmentEnabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()
        assertionEquals(actualConfirmAmendmentEnabledStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Enabled (amend over max allow) status Actual & Expected don't match")

        clickConfirmAmendmentBtn()
        sleep(2000)
        //user not able to amend correct error message show - maximum occupancy exceeded
        actualErrMsg=getErrorMsgAdultToChild()
        assertionEquals(actualErrMsg,hotelSearchData.expected.ErrorMsgMaxOccupExceededTxt, "Amend popup Screen, Occupancy tab, Error Message maximum occupancy exceeded  Actual & Expected don't match")

        //Amend Adult to child
        //Uncheck 2nd & 3rd traveller
        clickTravellersCheckBox(2)
        sleep(2000)
        clickTravellersCheckBox(3)
        sleep(2000)
        //check 8th traveller
        clickTravellersCheckBox(8)
        sleep(2000)

        //button enabled
        actualConfirmAmendmentEnabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()
        assertionEquals(actualConfirmAmendmentEnabledStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Enabled (amend child to adult) status Actual & Expected don't match")

        clickConfirmAmendmentBtn()
        sleep(2000)
        //Error Message Adult to Child
        actualErrMsg=getErrorMsgAdultToChild()
        assertionEquals(actualErrMsg,hotelSearchData.expected.ErrorMsgNoChangeFromAdultToChildTxt, "Amend popup Screen, Occupancy tab, Error Message Adult to Child  Actual & Expected don't match")

        //amend back to original assigned

            //Uncheck 8 travellers
                clickTravellersCheckBox(8)
        sleep(2000)

            //check 2
                clickTravellersCheckBox(2)
        sleep(2000)
        //button enabled
        actualConfirmAmendmentEnabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()
        assertionEquals(actualConfirmAmendmentEnabledStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Enabled (amend back to original assigned) status Actual & Expected don't match")

        clickConfirmAmendmentBtn()
        sleep(2000)
        //Error Message amend back to original assigned
        actualErrMsg=getErrorMsgAdultToChild()
        assertionEquals(actualErrMsg,hotelSearchData.expected.ErrorMsgNoChangeTxt, "Amend popup Screen, Occupancy tab, Error Message amend back to original assigned Actual & Expected don't match")

        //amend child with different age

            //Uncheck 7 travellers
                clickTravellersCheckBox(7)
        sleep(2000)
        //Check 8 travellers
        clickTravellersCheckBox(8)
        sleep(2000)
        //button enabled
        actualConfirmAmendmentEnabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()
        assertionEquals(actualConfirmAmendmentEnabledStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Enabled (amend back to original assigned) status Actual & Expected don't match")

        clickConfirmAmendmentBtn()
        sleep(4000)

        //current booking detials - should retain

        //Current booking:  text should show
        actualOccupancyTxt=getOccupancyInsideTxtInNonAmendablePopup(1)
        assertionEquals(actualOccupancyTxt,hotelSearchData.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking after first amendment text Actual & Expected don't match")

        //Item Name
        actualItemNameInOccupancyTab=getItemNameInOccupanceTab(1)
        assertionEquals(actualItemNameInOccupancyTab,hotelSearchData.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text after first amendment Actual & Expected don't match")

        //number of room & type of room
        actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(1)
        tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")

        roomnumAndTypeOfRoomText=tempItinryCanclDescList.getAt(0)
        actualroomdescTxt=roomnumAndTypeOfRoomText.trim()
        expectedRoomNumAndTypeOfRoomTxt="1x "+hotelSearchData.expected.roomDescTxt
        //booked room type
        assertionEquals(actualroomdescTxt,expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()
        assertionEquals(actualmealBasisTxt,hotelSearchData.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        actualfreeCancelTxt=getFreeCancelTxtInOccupanceTab(1)

        cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(hotelSearchData.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), hotelSearchData.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        expectedFreeCanclTxt=hotelSearchData.expected.freecancltxt+" "+cancelDate.toUpperCase()
        assertionEquals(actualfreeCancelTxt,expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in, number of night
        actualCheckInAndNumOfNight=getCheckInTxtInOccupanceTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        assertionEquals(actualCheckInAndNumOfNight,dur, "Amend popup Screen,  Occupancy tab,  actual check in and duration text after first amendment Actual & Expected don't match")

        //number of pax
        actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(1)
        tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")

        actualPaxText=tempPAXAndTravellersList.getAt(0).trim().replaceAll(" ","")
        assertionEquals(actualPaxText,hotelSearchData.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text after first amendment Actual & Expected don't match")

        //travellers
        actualTravellersText=getTravellersInOccupanceTab(1)
        expectedTravellersTxt=expectedleadTravellerName+", "+hotelSearchData.expected.title_txt+" "+hotelSearchData.expected.travellerfirstName+" "+hotelSearchData.expected.travellerlastName+",  "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.children.getAt(0) + "yrs)"+",  "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.children.getAt(1) + "yrs)"
        assertionEquals(actualTravellersText,expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text after first amendment Actual & Expected don't match")

        //Room Amount and Currency
        actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInOccupancyTab(1)
        assertionEquals(actualTotalRoomAmntAndCurrncyTxt,expectedPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text after first amendment Actual & Expected don't match")
        //Commission
        actualCommissionTxt=getCommissionInOccupancyTab(1)
        assertionEquals(actualCommissionTxt,hotelSearchData.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text after first amendment Actual & Expected don't match")

        //change to: text should show
        actualOccupancyTxt=getOccupancyInsideTxtInNonAmendablePopup(2)
        assertionEquals(actualOccupancyTxt,hotelSearchData.expected.changeToTxt, "Amend popup Screen,  Occupancy tab,  Change To text after first amendment Actual & Expected don't match")

        //Item Name
        actualItemNameInOccupancyTab=getItemNameInOccupanceTab(2)
        assertionEquals(actualItemNameInOccupancyTab,hotelSearchData.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, Change to section, item name text after first amendment Actual & Expected don't match")

        //number of room & type of room
        actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(2)
        tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")

        roomnumAndTypeOfRoomText=tempItinryCanclDescList.getAt(0)
        actualroomdescTxt=roomnumAndTypeOfRoomText.trim()
        expectedRoomNumAndTypeOfRoomTxt="1x "+hotelSearchData.expected.roomDescTxt
        //booked room type
        assertionEquals(actualroomdescTxt,expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, Change to section, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()
        assertionEquals(actualmealBasisTxt,hotelSearchData.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, Change to section, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        actualfreeCancelTxt=getFreeCancelTxtInOccupanceTab(2)
        resultData.hotel.amendBooking.actualfreeCancelTxt=actualfreeCancelTxt
        cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(hotelSearchData.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), hotelSearchData.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        expectedFreeCanclTxt=hotelSearchData.expected.freecancltxt+" "+cancelDate.toUpperCase()
        resultData.hotel.amendBooking.expectedFreeCanclTxt=expectedFreeCanclTxt
        //assertionEquals(actualfreeCancelTxt,expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Change to section, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in, number of night
        actualCheckInAndNumOfNight=getCheckInTxtInOccupanceTab(2)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        assertionEquals(actualCheckInAndNumOfNight,dur, "Amend popup Screen,  Occupancy tab, Change to section, actual check in and duration text after first amendment Actual & Expected don't match")

        //number of pax
        actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(2)
        tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")

        actualPaxText=tempPAXAndTravellersList.getAt(0).trim().replaceAll(" ","")
        assertionEquals(actualPaxText,hotelSearchData.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        //travellers
        actualTravellersText=getTravellersInOccupanceTab(2)
        expectedTravellersTxt=expectedleadTravellerName+", "+hotelSearchData.expected.title_txt+" "+hotelSearchData.expected.travellerfirstName+" "+hotelSearchData.expected.travellerlastName+", "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.amendchildren.getAt(0) + "yrs)"+", "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.amendchildren.getAt(2) + "yrs)"
        assertionEquals(actualTravellersText,expectedTravellersTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Travellers text after first amendment Actual & Expected don't match")

        //amount rate change and currency
        String actualpriceDiffAndCurrnTxt=getPriceDiffInOccupancyTab()
        resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt=actualpriceDiffAndCurrnTxt
        println("Actual Price Diff Text:$actualpriceDiffAndCurrnTxt")
        String expectedPriceDiff=getPriceDiffValue()+" GBP"
        resultData.hotel.amendBooking.expectedPriceDiff=expectedPriceDiff
        //assertionEquals(actualpriceDiffAndCurrnTxt,expectedPriceDiff, "Amend popup Screen,  Occupancy tab, Change to section,  Amount Rate change and currency after first amendment Actual & Expected don't match")

        //Room Amount and Currency
        actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInOccupancyTab(2)
        assertionEquals(actualTotalRoomAmntAndCurrncyTxt,expectedPrice, "Amend popup Screen,  Occupancy tab, Change to section,  Total Amount And Currency text after first amendment Actual & Expected don't match")
        //Commission
        actualCommissionTxt=getCommissionInOccupancyTab(2)
        assertionEquals(actualCommissionTxt,hotelSearchData.expected.commissionTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Commission Percent text after first amendment Actual & Expected don't match")
        //confirmation status(inventory) - Commented Status as per discussion with Archana
        //String actualConfirmationStatus=getInventoryStatusInOccupancyTab()
        //assertionEquals(actualConfirmationStatus,hotelSearchData.expected.statusTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Inventory Status after first amendment Actual & Expected don't match")

        //Ok button should be displayed
        boolean actualOkButtonDispStatus=getOkButtonDisplayStatus()
        assertionEquals(actualOkButtonDispStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        boolean actualOkButtonEnableStatus=getOkButtonEnableOrDisableStatus()
        assertionEquals(actualOkButtonEnableStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button enable status Actual & Expected don't match")

        //17 Select to assign

        //Please select occupants names:
        actualPleaseSelectTxt=getPleaseSelectTxtInOccupancyTab()
        assertionEquals(actualPleaseSelectTxt,hotelSearchData.expected.plzSelTxt, "Amend popup Screen,  Occupancy tab,  Please select occupants names: text after first amendment Actual & Expected don't match")

        //tick box should present for each traveller listed in traveller details section
        travellersListCheckBoxExistenceCheck(1,9,hotelSearchData)

        //Get 1st traveller checked status
        chkBoxStatus=getTravellerCheckedStatus(1)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, First Traveller CheckBox display status after first amendment Actual & Expected don't match")

        chkBoxStatus=getTravellerCheckedStatus(2)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status after first amendment Actual & Expected don't match")

        chkBoxStatus=getTravellerCheckedStatus(6)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Sixth Traveller CheckBox display status after first amendment Actual & Expected don't match")

        chkBoxStatus=getTravellerCheckedStatus(8)
        assertionEquals(chkBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Seventh Traveller CheckBox display status after first amendment Actual & Expected don't match")

        //lead traveller
        actualLeadTraveller=getTravellerNameTxt(1)
        expectedLeadTravellerTxt=hotelSearchData.expected.firstName+" "+hotelSearchData.expected.lastName+" "+"(lead name)"
        assertionEquals(actualLeadTraveller,expectedLeadTravellerTxt, "Amend popup Screen, Occupancy tab, Lead Traveller Text after first amendment Actual & Expected don't match")

        //2nd to 5th traveller
        verifyAdultTravellers(2,5,hotelSearchData)
        //6th to 9th traveller
        verifyChildTravellers(6,9,hotelSearchData,hotelSearchData.input.amendchildren)

        //infant text should show - if it is available for the room
        actualInfantLabTxt=getInfantLabelTxt()
        assertionEquals(actualInfantLabTxt,hotelSearchData.expected.infantLabelTxt, "Amend popup Screen, Occupancy tab, Infant Label Text after first amendment Actual & Expected don't match")

        //dropdown list to select number of infant should show
        actualInfantDropdownListDispStatus=getInfantSelectBoxDisplayStatus()
        assertionEquals(actualInfantDropdownListDispStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Drop down Infant display status after first amendment Actual & Expected don't match")

        //list should show 0 - 1 or 0-2
        actualListInfantValues=getInfantDropDownValuesInOccupancyTab()
        actualListInfantValues=actualListInfantValues.sort()
        println("Actual List Values:$actualListInfantValues")
        expectedListInfantValues=hotelSearchData.expected.infantDropDownListValues
        expectedListInfantValues=expectedListInfantValues.sort()
        println("Expected List Values:$expectedListInfantValues")
        assertionEquals(actualListInfantValues,expectedListInfantValues,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")

        //Confirm Amendment Button display status
        actualConfirmAmendmentButtonStatus=getConfirmAmendButtonDisplayStatus()
        assertionEquals(actualConfirmAmendmentButtonStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button display status Actual & Expected don't match")

        //Confirm Amendment Button Disabled status
        actualConfirmAmendmentDisabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()
        assertionEquals(actualConfirmAmendmentDisabledStatus,hotelSearchData.expected.notDispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Disabled status Actual & Expected don't match")


        //18 accepting change

        //click ok button
        clickAmendOKButton()
        sleep(6000)

        //user taken to t & c page
        boolean actualDispStatus=getAboutToBookScreendisplayStatus()
        assertionEquals(actualDispStatus,hotelSearchData.expected.dispStatus, "About To Amend Screen display status Actual & Expected don't match")

        //capture title text
        String actualtitleTxt=getCancellationHeader()
        assertionEquals(actualtitleTxt,hotelSearchData.expected.amendmentTitleTxt, "About to Amend screen, title text Actual & Expected don't match")

        //Capture Close Icon display status
        boolean actualCloseIconDispStatus=getCloseIconDisplayStatus()
        assertionEquals(actualCloseIconDispStatus,hotelSearchData.expected.dispStatus, "About to Amend screen, Close Icon display status Actual & Expected don't match")

        //Capture hotel name in About to Amend Screen
        String actualHotelNameInAbtToAmndScrn=getHotelNameInAboutToBookScrn()
        assertionEquals(actualHotelNameInAbtToAmndScrn,hotelSearchData.expected.cityAreaHotelText, "About to Amend screen, Hotel Name text Actual & Expected don't match")

        //number of room/name of room (descripton)
        //String tmphotelTxt=getTitleSectDescTxt(0)
        String tmphotelTxt=getItemDescPaxAndCityTxtInAboutToBookScreen()
        List<String> htlDesc=tmphotelTxt.tokenize(",")

        String actualnumAndNameofRoomText=htlDesc.getAt(0).trim()
        //expectedRoomNumAndTypeOfRoomTxt="1x "+hotelSearchData.expected.roomDescTxt
        expectedRoomNumAndTypeOfRoomTxt=hotelSearchData.expected.roomDescTxt
        assertionEquals(actualnumAndNameofRoomText,expectedRoomNumAndTypeOfRoomTxt, "About to Amend screen, number of room/name of room (descripton) text Actual & Expected don't match")

        //meal basis
        actualmealBasisTxt=htlDesc.getAt(1).trim()
        assertionEquals(actualmealBasisTxt,hotelSearchData.expected.mealBasisTxt, "About to Amend screen, meal basis text Actual & Expected don't match")

        //number of pax
        //actualPAXAndTravellers=getTitleSectDescTxt(1)
        actualPAXAndTravellers=getTitleSectDescTxt(3)
        //tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")

        //actualPaxText=tempPAXAndTravellersList.getAt(0).trim().replaceAll(" ","")
        actualPaxText=actualPAXAndTravellers.trim().replaceAll("\n","")
        assertionEquals(actualPaxText,hotelSearchData.expected.paxText.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")

        //travellers
        //actualTravellersText=getTravellersInAboutToAmend(1)
        //expectedTravellersTxt=expectedleadTravellerName+", "+hotelSearchData.expected.title_txt+" "+hotelSearchData.expected.travellerfirstName+" "+hotelSearchData.expected.travellerlastName+", "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.amendchildren.getAt(0) + "yrs)"+", "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.amendchildren.getAt(2) + "yrs)"
        //assertionEquals(actualTravellersText,expectedTravellersTxt, "About to Amend screen,  Travellers text  Actual & Expected don't match")

        //check in date, number of night
       //actualCheckInAndNumOfNight=getTitleSectDescTxt(2)
        actualCheckInAndNumOfNight=getTitleSectDescTxt(0).replaceAll("\n","")
        def chkInDate=dateUtil.addDaysChangeDatetformat(hotelSearchData.input.checkInDays.toInteger(), "yyyy-MM-dd")
        String expectedCheckInDateTextAndDate="Check in"+chkInDate

       /* if (nights >1)
            dur= checkInDt+","+nights+" nights"
        else dur = checkInDt+","+nights+" night" */

        //assertionEquals(actualCheckInAndNumOfNight,dur, "About to Amend screen, check in and duration text Actual & Expected don't match")
        assertionEquals(actualCheckInAndNumOfNight,expectedCheckInDateTextAndDate, "About to Amend screen, check in  text and Date Actual & Expected don't match")

        //Number of Nights
        String actualNumOfNight=getTitleSectDescTxt(2).replaceAll("\n","")
        if (nights >1)
            dur= "Nights"+nights
        else dur = "Nights"+nights
        assertionEquals(actualNumOfNight,dur, "About to Amend screen, Number of Nights Actual & Expected don't match")


        String actualInvStatusInAbtToAmendScrn=getPriceInAbouttoBookScrn(0)
        resultData.hotel.amendBooking.actualInvStatusInAbtToAmendScrn=actualInvStatusInAbtToAmendScrn
        //assertionEquals(actualInvStatusInAbtToAmendScrn,hotelSearchData.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")

        //amount rate change and currency
        String actualAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(1)
        resultData.hotel.amendBooking.actualAmntRatechangeAndCurncyAbtToAmendScrn=actualAmntRatechangeAndCurncyAbtToAmendScrn
        //assertionEquals(actualAmntRatechangeAndCurncyAbtToAmendScrn,expectedPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

        //total room amount & currency code
        String actualRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(2)
        resultData.hotel.amendBooking.actualRoomAmntAndCurncyAbtToAmendScrn=actualRoomAmntAndCurncyAbtToAmendScrn
        String expectedRoomAmntAndcurncyAbtToAmendScrn=actualTotalRoomAmntAndCurrncyTxt.replaceAll(" ", "")
        resultData.hotel.amendBooking.expectedRoomAmntAndcurncyAbtToAmendScrn=expectedRoomAmntAndcurncyAbtToAmendScrn
        //assertionEquals(actualRoomAmntAndCurncyAbtToAmendScrn,expectedRoomAmntAndcurncyAbtToAmendScrn, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        //commission & % value
        String actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(3)
        resultData.hotel.amendBooking.actualcommissionPercentAbtToAmendScrn=actualcommissionPercentAbtToAmendScrn
        //assertionEquals(actualcommissionPercentAbtToAmendScrn,hotelSearchData.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")

        //special condition - header text
        String actualSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToAmendScrn(0)
        String expectedSpecialConditionHeaderTxt=hotelSearchData.expected.spclCondtnTxt+checkInDt
        assertionEquals(actualSpecialConditionHeaderTxt,expectedSpecialConditionHeaderTxt, "About to Amend screen, Special condition header text Actual & Expected don't match")

        //Please note text
        String actualPlzNoteTxt=getDescriptionTextInAboutToAmendScrn(0)
        assertionEquals(actualPlzNoteTxt,hotelSearchData.expected.plzNoteTxt, "About to Amend screen,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        //String actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(1)
        String actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)
        assertionEquals(actualCancellationChargeTxt,hotelSearchData.expected.cancelChargeTxt, "About to Amend screen, Cancelation Charge header text Actual & Expected don't match")

        List<String> actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()
        assertionEquals(actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "About to Amend screen, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        String actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(1)
        assertionEquals(actualIfAmendmentsTxt,hotelSearchData.expected.ifAmendmentsTxt, "About to Amend screen,  Amendments text Actual & Expected don't match")

        //All dates text
        String actualDatesTxt=getDescriptionTextInAboutToAmendScrn(2)
        assertionEquals(actualDatesTxt,hotelSearchData.expected.allDatesTxt, "About to Amend screen,  All Dates text Actual & Expected don't match")

        //Total
        String actualTotalTextInAboutToBook=getTotalAndCommissionText(0)
        assertionEquals(actualTotalTextInAboutToBook,hotelSearchData.expected.totalTxt, "About to Amend screen,  total text Actual & Expected don't match")

        //Total Amount and currency
        //String actualTotalAmountAndCurrency=getTotalInConfirmbooking()
        String actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)
        assertionEquals(actualTotalAmountAndCurrency,actualTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        //Your Commission text
        //String actualCommissionTextInAboutToAmend=getTotalAndCommissionText(1)
        String actualCommissionTextInAboutToAmend=getTotalAndCommissionText(2)
        assertionEquals(actualCommissionTextInAboutToAmend,hotelSearchData.expected.cmsnTxt, "About to Amend screen,  Your Commission text Actual & Expected don't match")

        //Your commission amount and currency
        //String actualCommissionValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(1)
        String actualCommissionValueInAboutToAmend=getTotalAndCommissionText(3)
        float compercentage = Float.parseFloat(hotelSearchData.expected.commissionPercent)
        String priceText=actualTotalRoomAmntAndCurrncyTxt.replace(" GBP", "").trim()
        float comAmount=Float.parseFloat(priceText)
        String comValue=getCommissionPercentageValue(comAmount,compercentage)
        String expectedComValue=comValue+" "+clientData.currency
        assertionEquals(actualCommissionValueInAboutToAmend,expectedComValue, "About to Amend screen,  Your Commission Amount and Currency Actual & Expected don't match")

        //< Confirm Amendment > button display status
        boolean actualconfirmButtonDispStatus=getConfirmAmendButtonDispStatusInAboutToAmend()
        assertionEquals(actualconfirmButtonDispStatus,hotelSearchData.expected.dispStatus, "About to Amend screen, Confirm Amendment Button display status Actual & Expected don't match")

        //confirm Amendment enable status
        boolean actualconfirmAmendEnableStatus=getConfirmAmendButtonEnableOrDisableStatusInAboutToAmend()
        assertionEquals(actualconfirmAmendEnableStatus,hotelSearchData.expected.dispStatus, "About to Amend screen, Confirm Amendment Button Enable status Actual & Expected don't match")

        //T&C agreement text
        //String ByClickingFooterTxt=getByClickingFooterTxt(5)
        String ByClickingFooterTxt=getByClickingFooterTxt()
        String actualTermsAndCondtTxt=ByClickingFooterTxt.replace("\n", "")
        assertionEquals(actualTermsAndCondtTxt,hotelSearchData.expected.TermsAndCondtnsFooterTxt, "About to Amend screen, By clicking Footer Text Actual & Expected don't match")

        //click < Confirm Amendment > button
        //clickonConfirmAmendment()
        clickOnConfirmBookingAndPayLater()
        sleep(7000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(6000)

        waitForAjaxIconToDisappear()
        waitTillLoadingIconDisappears()
        scrollToTopOfThePage()
        //Confirm booking page display status
        boolean actualconfirmbookStatus=getBookingConfirmationScreenDisplayStatus()
        assertionEquals(actualconfirmbookStatus,hotelSearchData.expected.dispStatus, "After Amend Booking Confirmation Screen display status Actual & Expected don't match")

        //20  Booking confirming

        //Booking confirmed text
        actualBookingConfirmedTitleText=getCancellationHeader()
        assertionEquals(actualBookingConfirmedTitleText,hotelSearchData.expected.bookingConfrmTitleTxt, "After Amend, Booking Confirmation Screen, title text Actual & Expected don't match")

        //Close X function
        boolean actualCloseBtnDispStatus=overlayCloseButton()
        assertionEquals(actualCloseBtnDispStatus,hotelSearchData.expected.dispStatus, "After Amend Booking Confirmation, Close X function display status Actual & Expected don't match")

        //A confirmation of your booking has been email to:
        String actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
        String emailId=clientData.usernameOrEmail
        //emailId=emailId.toUpperCase()
        //String expectedHeaderSectionText=hotelSearchData.expected.headerSectionTxt+" "+emailId
        String expectedHeaderSectionText=hotelSearchData.expected.headerSectionTxt
        assertionEquals(actualHeaderSectionText,expectedHeaderSectionText, "After Amend Booking Confirmation, Email Text Actual & Expected don't match")

        //brand & icon
        boolean actualBrandAndIconDispStatus=getBrandAndIconDisplayStatus()
        assertionEquals(actualBrandAndIconDispStatus,hotelSearchData.expected.dispStatus, "After Amend Booking Confirmation, Brand and icon display status Actual & Expected don't match")

        //Booking ID: & value
        String actualbookingID=getBookingIdFromBookingConfirmed(0)
        String expectedBookingID=retrievedbookingID
        assertionEquals(actualbookingID,expectedBookingID, "After Amend Booking Confirmation, Booking ID: & Value text Actual & Expected don't match")

        //Itinerary ID,  Itinerary name & value
        String actualitinearyIDAndName=getItinearyID(0)
        String expecteditineraryIdAndName=readitinearyIDAndName
        assertionEquals(actualitinearyIDAndName,expecteditineraryIdAndName, "After Amend Booking Confirmation, Itinerary ID: & Value text Actual & Expected don't match")

        //Departure date: text
        String actualDepDateTxt=getBookingIdFromBookingConfirmed(1)
        assertionEquals(actualDepDateTxt,hotelSearchData.expected.departDateTxt, "After Amend Booking Confirmation, Departure Date text Actual & Expected don't match")

        //Departure Date value text
        String actualDepDateValTxt=getBookingDepartDate()
        String expectedDepDateValTxt=checkInDt.toUpperCase()
        assertionEquals(actualDepDateValTxt,expectedDepDateValTxt, "After Amend Booking Confirmation, Departure Date Value text Actual & Expected don't match")

        //Traveller Details & value
        String actualTravellerDetailsTxt=getBookingIdFromBookingConfirmed(2)
        assertionEquals(actualTravellerDetailsTxt,hotelSearchData.expected.travellerDeetailsTxt, "After Amend Booking Confirmation, Traveller Details text Actual & Expected don't match")

        //Traveller Values
        List<String> tempTravellerList=actualTravellersText.tokenize(",")

        for(int i=0;i<=3;i++){
        String expectedTravellerName=tempTravellerList.getAt(i).trim().replaceAll(" ","")
        String actualTravellerName=getTravellerNamesConfirmationDialog(i).replaceAll(" ","")
        assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
        }
        //item name
        String actualHotelName=getConfirmBookedTransferName()
        String expectedItemName=readItemName
        assertionEquals(actualHotelName,expectedItemName, "After Amend Booking Confirmation, Item Name text Actual & Expected don't match")

        //item address
        String actualHotelAddressTxt=getTransferDescBookingConfirmed()
        String expectedItemAddressTxt=readItemAddressTxt
        assertionEquals(actualHotelAddressTxt,expectedItemAddressTxt, "After Amend Booking Confirmation, Item Address text Actual & Expected don't match")

        //number of room, room type & value
        //String expectedRoomNumAndroomValueTxt=actualnumAndNameofRoomText
        String expectedRoomNumAndroomValueTxt="1x "+actualnumAndNameofRoomText
        String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
        htlDesc=roomAndPax.tokenize(",")

        String actualRoomNumAndTypeTxt=htlDesc.getAt(0).trim()
        assertionEquals(actualRoomNumAndTypeTxt,expectedRoomNumAndroomValueTxt, "After Amend Booking Confirmation, Room Num And Type text Actual & Expected don't match")

        //Capture PAX
        String actualpaxTxt=htlDesc.getAt(1).trim().replaceAll(" ","")
        //String expectedpaxTxt=actualPaxText.replaceAll(" ","")
        String expectedpaxTxt=hotelSearchData.expected.paxTxt.replaceAll(" ","")
        //assertionEquals(actualpaxTxt,expectedpaxTxt, "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
        assertContains(actualpaxTxt,expectedpaxTxt, "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        //Meal basis & value
        String actualMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)
        String expectedMealBasisTxt=actualmealBasisTxt
        assertionEquals(actualMealBasisTxt,expectedMealBasisTxt, "After Amend Booking Confirmation, Meal Basis text Actual & Expected don't match")

        //Check in date, Number of Nights
        String actualCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed().replaceAll(" ","")
        //String expectedCheckInDate=actualCheckInAndNumOfNight.replaceAll(" ","")
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur =checkInDt+", "+nights+" night"
        //assertionEquals(actualCheckInDateNumOfNights,expectedCheckInDate, "After Amend Booking Confirmation, Check in Date and num of nights text Actual & Expected don't match")
        assertionEquals(actualCheckInDateNumOfNights,dur.replaceAll(" ",""), "After Amend Booking Confirmation, Check in Date and num of nights text Actual & Expected don't match")

        //room rate amount and currency
        String actualRoomRateAmountAndCurrency=getPriceBookingConfirmation()
        String expectedRoomRateAmntAndCurrncy=actualTotalAmountAndCurrency.replaceAll(" ", "")
        assertionEquals(actualRoomRateAmountAndCurrency,expectedRoomRateAmntAndCurrncy, "After Amend Booking Confirmation, Room Rate Amnt & Currency text Actual & Expected don't match")

        //commission amount and currency
        String actualCommissionAmountAndCurrency

        try {
            actualCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
        }catch(Exception e){
            actualCommissionAmountAndCurrency="Unable To Read From UI"
        }
        String expectedCommissionAmountAndCurncy="Commission"+actualCommissionValueInAboutToAmend.replace(" ", "")
        assertionEquals(actualCommissionAmountAndCurrency,expectedCommissionAmountAndCurncy, "After Amend Booking Confirmation, Commission Amnt & Currency text Actual & Expected don't match")

        //< PDF voucher > button
        boolean actualPDFVoucherBtnDispStatus

        try {
            //< PDF voucher > button
            actualPDFVoucherBtnDispStatus=getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            actualPDFVoucherBtnDispStatus="false"
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            //softAssert.assertFalse(true,"Failed To Confirm Booking")
        }
        assertionEquals(actualPDFVoucherBtnDispStatus,hotelSearchData.expected.dispStatus, "After Amend Booking Confirmation, PDF Voucher Button Display Status Actual & Expected don't match")

        //click Close X
        coseBookItenary()
        sleep(4000)

       //21 Itinerary - updating

        //Item card
        boolean acutalDisplayStatus=getItemCardDisplayStatus()
        assertionEquals(acutalDisplayStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Card Display Status Actual & Expected don't match")

        //Get Booing ID and number
        //String actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()
        String actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()
        assertionEquals(actualBookingIDinBookedDetailsScrn,expectedBookingID, "After Amend Booking, Itinerary Page, Booked Items, Booking ID: & Value text Actual & Expected don't match")

        //Confirmed tab
        boolean actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()
        assertionEquals(acutalDisplayStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Display Status Actual & Expected don't match")

        //confirm tab text
        String actualConfirmedTabTxt=getStatusInBookedItemsScrn()
        assertionEquals(actualConfirmedTabTxt,hotelSearchData.expected.statusTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Text Actual & Expected don't match")

        //Amend tab
        boolean actualAmendTabDispStatus=getAmendTabDisplayStatus()
        assertionEquals(actualAmendTabDispStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Display Status Actual & Expected don't match")

        //Amend tab text
        String actualAmendTabTxt=getTabTxtInBookedItemsScrn(0)
        assertionEquals(actualAmendTabTxt,hotelSearchData.expected.amendTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Text Actual & Expected don't match")

        //Cancel tab
        boolean actualCancelTabDispStatus=getCancelTabDisplayStatus()
        assertionEquals(actualCancelTabDispStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Display Status Actual & Expected don't match")

        //Cancel tab text
        String actualCancelTabTxt=getTabTxtInBookedItemsScrn(1)
        assertionEquals(actualCancelTabTxt,hotelSearchData.expected.cancelTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Text Actual & Expected don't match")

        //item image
        boolean actualImageStatus=imgDisplayedOnSugestedItem(0)
        assertionEquals(actualImageStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Image Display Status Actual & Expected don't match")

        //item name
        String actualHotelNameTxt=getTransferNameInSuggestedItem(0)
        assertionEquals(actualHotelNameTxt,hotelSearchData.expected.cityAreaHotelText, "After Amend Booking, Itinerary Page, Booked Items, Item Name Text Actual & Expected don't match")

        //item star rating
        String actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)
        assertionEquals(actualStarRatingInBookedHotelItem,hotelSearchData.expected.starRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")

        //room type

        String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        String actualroomdescTxtInBookedItmsScrn=descText.trim()
        assertionEquals(actualroomdescTxtInBookedItmsScrn,hotelSearchData.expected.roomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        String actualPaxNumInBookedItmsScrn=paxNumDetails.trim()
        assertionEquals(actualPaxNumInBookedItmsScrn,hotelSearchData.expected.paxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        String actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()
        assertionEquals(actualratePlanInBookedItmsScrn,hotelSearchData.expected.mealBasisTxt, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")

        //Free cancellation until date
        String actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)
        assertionEquals(actualFreeCnclTxtInbookedItmsScrn,expectedFreeCanclTxt, "After Amend Booking, Itinerary Page, Booked Items, Free Cancellation text Actual & Expected don't match")

        //click on cancellation link
        clickBookedItemCancellationLink(0)

        //cancellation popup opens
        boolean actualCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()
        assertionEquals(actualCancelPopupDisStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup Display Status Actual & Expected don't match")

        //Cancellation policy
        String actualCanclPolcyTxt=getCancellationHeader()
        assertionEquals(actualCanclPolcyTxt,hotelSearchData.expected.cancellationHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Header Text Actual & Expected don't match")

        //Close X
        boolean actualCloseButtonDispStatus=overlayCloseButton()
        assertionEquals(actualCloseButtonDispStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup close button Display Status Actual & Expected don't match")

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        String actualSplConditionTxt=getCancellationItemOverlayHeaders(0)
        assertionEquals(actualSplConditionTxt,expectedSpecialConditionHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Special condition header text Actual & Expected don't match")

        //Please note text
        actualPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)
        assertionEquals(actualPlzNoteTxt,hotelSearchData.expected.plzNoteTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)
        assertionEquals(actualCancellationChargeTxt,hotelSearchData.expected.cancelChargeTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge header text Actual & Expected don't match")

        actualCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()
        assertionEquals(actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(1)
        assertionEquals(actualIfAmendmentsTxt,hotelSearchData.expected.ifAmendmentsTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Amendments text Actual & Expected don't match")

        //All dates text
        actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(2)
        assertionEquals(actualDatesTxt,hotelSearchData.expected.allDatesTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  All Dates text Actual & Expected don't match")

        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        String actualDurationTxt=getItenaryDurationInSuggestedItem(0)
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        assertionEquals(actualDurationTxt,dur, "After Amend Booking, Itinerary Page, Booked Items,  actual check in and duration text Actual & Expected don't match")

        //Voucher Icon
        boolean actualDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(0)
        assertionEquals(actualDownloadVocherIconDispStatus,hotelSearchData.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Download Voucher button Display Status Actual & Expected don't match")

        //Commission and percentage
        String actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)
        assertionEquals(actualcomPercentTxt,hotelSearchData.expected.commissionTxt, "After Amend Booking, Itinerary Page, Booked Items, Commission Percentage text Actual & Expected don't match")

        //Room rate amount and currency
        String actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)
        assertionEquals(actualPriceAndcurrency,actualTotalAmountAndCurrency, "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")

        at ShareItineraryTransfersPage

        //Traveller Details
        String actualtravellerDetails=getTravellerDetails()
        String expectedtravellerDetails="Travellers: "+hotelSearchData.expected.firstName+" "+hotelSearchData.expected.lastName+", "+hotelSearchData.expected.travellerfirstName+" "+hotelSearchData.expected.travellerlastName+", "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.amendchildren.getAt(0) + "yrs)"+", "+hotelSearchData.expected.childFirstName+" "+hotelSearchData.expected.childLastName+" "+ "(" + hotelSearchData.input.amendchildren.getAt(2) + "yrs)"
        assertionEquals(actualtravellerDetails,expectedtravellerDetails, "After Amend Booking, Itinerary Page, Booked Items, Travellers Details Text Actual & Expected don't match")


    }

    def"TC01: Verify Itinerary  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"




    }

    def"TC02: Verify Add Details Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"




    }
    def"TC03: Verify Edit Traveller Details Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"





    }
    def"TC04: Verify Amend Screen Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"

        //status Text
        assertionEquals(resultData.hotel.confirmationPage.actualStatus,hotelSearchData.expected.statusTxt, "Traveller Could not removed popup Screen,  status text Actual & Expected don't match")

        //Cancel Text
        assertionEquals(resultData.hotel.confirmationPage.actualfreeCancelTxt,resultData.hotel.confirmationPage.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //Commisstion Percent Text
        assertionEquals(resultData.hotel.confirmationPage.actualCommissionPercntTxt,hotelSearchData.expected.commissionTxt, "Traveller Could not removed popup Screen,  Commission Percent text Actual & Expected don't match")

        //Amount And Currency Text
        assertionEquals(resultData.hotel.confirmationPage.actualtotalRateAndCurncy,resultData.hotel.searchResults.itineraryBuilder.expectedPrice, "Traveller Could not removed popup Screen,  Total Amount And Currency text Actual & Expected don't match")


        //amend screen, cancel text
        assertionEquals(resultData.hotel.amendBooking.actualfreeCancelTxt, resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Change to section, Free Cancellation text after first amendment Actual & Expected don't match")

        //price diff text
        assertionEquals(resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.expectedPriceDiff, "Amend popup Screen,  Occupancy tab, Change to section,  Amount Rate change and currency after first amendment Actual & Expected don't match")

        //status text
        assertionEquals(resultData.hotel.amendBooking.actualInvStatusInAbtToAmendScrn,hotelSearchData.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")

        //Amount rate change
        assertionEquals(resultData.hotel.amendBooking.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

        //Total room amount
        assertionEquals(resultData.hotel.amendBooking.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedRoomAmntAndcurncyAbtToAmendScrn, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        //commission percent text
        assertionEquals(resultData.hotel.amendBooking.actualcommissionPercentAbtToAmendScrn,hotelSearchData.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")


    }
}

