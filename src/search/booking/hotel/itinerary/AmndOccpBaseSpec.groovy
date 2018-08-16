package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.ShareItineraryTransfersPage
import org.testng.Assert

/**
 * Created by kmahas on 7/18/2016.
 */
abstract class AmndOccpBaseSpec extends TravellerDetailBaseSpec{


    protected def addToItinerary(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
        sleep(3000)
        clickPaxRoom()
        sleep(3000)

            clickPaxNumOfRooms(data.input.noOfRooms.toString())
            sleep(3000)
            //Enter Pax
            clickPaxRoom()


            selectNumberOfAdults(data.input.pax.toString(), 0)

            if(data.input.children.size()==0){
                selectNumberOfChildren(data.input.children.size().toString() , 0)
                sleep(1000)
            }

            if(data.input.children.size()>0){
                selectNumberOfChildren(data.input.children.size().toString() , 0)
                sleep(1000)
                for(int i=0;i<data.input.children.size();i++){
                    enterChildrenAge(data.input.children.getAt(i).toString(),"0", i)
                    sleep(1000)
                }
            }

        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage

        waitTillAddToItineraryBtnLoads()

        //item should return in search results
        resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults=getItemAvailabilityInSearchResults(data.expected.cityAreaHotelText)

        scrollToSpecificItnryAndClickMoreRoomTypesBtn(data.expected.cityAreaHotelText)
        sleep(7000)

        //More room section expanded
        resultData.hotel.searchResults.itineraryBuilder.actualMoreRoomSectionExpandedStatus=getMoreRoomSectionExpandOrCollapseStatus(data.expected.cityAreaHotelText)

        scrollToSpecificItnryAndClickFreeCancellationTxtInMoreRoomTypes(data.expected.cityAreaHotelText,data.expected.roomDescTxt)
        sleep(7000)
        resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt=getCancellationChargeRelatedText()
        clickCloseCloseLightbox()
        sleep(2000)

        //click <Add to itinerary> button against Standard Triple hotel item: Howard Prince -SITUJ07XMLAuto
        scrollToSpecificItnryAndClickAddToItinryBtnInMoreRoomTypes(data.expected.cityAreaHotelText,data.expected.roomDescTxt)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Capture items added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

        //Capture item is added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname=getHotelNameInTitleCard(0)

    }

    protected def createNewItinerary(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
        sleep(3000)
        clickPaxRoom()
        sleep(3000)

        clickPaxNumOfRooms(data.input.noOfRooms.toString())
        sleep(3000)
        //Enter Pax
        clickPaxRoom()


        selectNumberOfAdults(data.input.pax.toString(), 0)

        if(data.input.children.size()==0){
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
        }

        if(data.input.children.size()>0){
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
            for(int i=0;i<data.input.children.size();i++){
                enterChildrenAge(data.input.children.getAt(i).toString(),"0", i)
                sleep(1000)
            }
        }

        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)


        at HotelSearchResultsPage
        if(getCotsRequestedPopupDisplayStatus()==true){
            clickCloseLightbox()
            sleep(4000)
        }


        waitTillAddToItineraryBtnLoads()

        //item should return in search results
        resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults=getItemAvailabilityInSearchResults(data.expected.cityAreaHotelText)

        //scrollToSpecificItnryAndClickMoreRoomTypesBtn(data.expected.cityAreaHotelText)
        sleep(7000)

        //More room section expanded
        //resultData.hotel.searchResults.itineraryBuilder.actualMoreRoomSectionExpandedStatus=getMoreRoomSectionExpandOrCollapseStatus(data.expected.cityAreaHotelText)

        scrollToSpecificItnryAndClickFreeCancellationTxtInMoreRoomTypes(data.expected.cityAreaHotelText,data.expected.firstRoomDescTxt)
        sleep(7000)
        resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt=getCancellationChargeRelatedText()
        clickCloseCloseLightbox()
        sleep(2000)

        //click <Add to itinerary> button against Standard Triple hotel item: Howard Prince -SITUJ07XMLAuto
        scrollToSpecificItnryAndClickAddToItinryBtnInMoreRoomTypes(data.expected.cityAreaHotelText,data.expected.firstRoomDescTxt)
        sleep(5000)

        //commented following code since application flow changed in 10.3
        /*if(getCotsRequestedPopupDisplayStatus()==true){
            clickCloseLightbox()
            sleep(4000)
        }*/

        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Capture items added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

        //Capture item is added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname=getHotelNameInTitleCard(0)

        //Go To Itinerary
        goToItinerary(data, resultData)

        //Enter Traveller Details
        travellerDetails(data, resultData,2,2,3,3,data.input.children)

        //Book Hotel Item
        at ItineraryTravllerDetailsPage

        //click on Book button
        clickOnBookIcon()
        sleep(5000)
        waitTillLoadingIconDisappears()
        int maxTravellers
        if(data.input.infant==true){
            maxTravellers=data.input.pax+(data.input.children.size()-1)
        }else{
            maxTravellers=data.input.pax+data.input.children.size()
        }
        //Selecting all travellers
        for(int i=0;i<maxTravellers;i++) {

            if(getTravellerCheckBoxCheckedStatus(i)==false)
            {
                clickOnTravellerCheckBox(i)
                sleep(2000)
            }
        }

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Screen Display Status
        resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

        //Booking confirmed lightbox displayed

        //Title text-Booking Confirmed
        resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()


        //Traveller Values
        String trvlrValues
        List expectedTravellerName = []
        List actualtravellerName = []
        int travellers=data.input.pax+data.input.children.size()
        if(data.input.infant==true) {
            trvlrValues = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName +", "+ data.expected.childFirstName + " " + data.expected.childLastName + "" + "(" + data.input.children.getAt(0).toString() + "yrs)"+"+1 infant"
            List<String> tempInfantList = trvlrValues.tokenize("+")
            String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = tmpTravellers.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i < travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            }

            for (int i = 0; i < travellers - 1; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim())

            }
            expectedTravellerName.add("+" + tempInfantList.getAt(1).trim())


        }else {
                trvlrValues = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
                //println("Debug Travlr Valu:$trvlrValues")
                List<String> tempTravellerList = trvlrValues.tokenize(",")
                //println("Debug tokenized Travlr Valu:$tempTravellerList")


                for (int i = 0; i <= 2; i++) {
                    expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))
                    actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
                    //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
                }
        }

        resultData.hotel.confirmationPage.travellers.put("actTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expTravellers", expectedTravellerName)


        //read Booking id
        resultData.hotel.itineraryPage.firstBookingID=getBookingIdFromBookingConfirmed(0)
        println("Booking ID: $resultData.hotel.itineraryPage.firstBookingID")

        //read Itineary Reference Number & name
        resultData.hotel.itineraryPage.readitinearyIDAndName=getItinearyID(0)

        //item name
        //resultData.hotel.itineraryPage.readItemName=getConfirmBookedTransferName()
        resultData.hotel.itineraryPage.readFirstItemName=getConfirmBookedTransferName()

        //item address
        //resultData.hotel.itineraryPage.readItemAddressTxt=getTransferDescBookingConfirmed()
        resultData.hotel.itineraryPage.readFirstItemAddressTxt=getTransferDescBookingConfirmed()

        try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = "Unable To Read From UI PDF ICON"
        }

        /*try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            Assert.assertFalse(true,"Failed To Confirm Booking")
        }*/

        //Close Booking confirmation
        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //Click <Add a hotel> function button
        //clickOnAddHotelIcon()
        //sleep(4000)
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(4000)
        at HotelSearchResultsPage
        if(getItineraryBarExpandOrCollapseStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage



        //capture expanded status
        resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionOpenStatus=getItineraryBarExpandOrCollapseStatus()

    }

    protected def createNewItineraryMultiRoom(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
        sleep(3000)
        clickPaxRoom()
        sleep(3000)

        clickPaxNumOfRooms(data.input.noOfRooms.toString())
        sleep(3000)
        selectNumberOfAdults(data.input.pax.toString(), 1)

        if(data.input.children.size()==0){
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
        }

        selectNumberOfAdults(data.input.pax.toString(), 0)
        if(data.input.children.size()==0){
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
        }

        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage

        waitTillAddToItineraryBtnLoads()

        //item should return in search results
        resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults=getItemAvailabilityInSearchResults(data.expected.cityAreaHotelText)

        //scrollToSpecificItineraryAndClickFreeCancellationLink(data.expected.cityAreaHotelText)
        scrollToSpecificItinryAndClickFreeCancellationLink(data.expected.cityAreaHotelText)
        sleep(7000)

        resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt=getCancellationChargeRelatedText()

        clickCloseCloseLightbox()
        sleep(2000)

        //scrollToSpecificItnryAndClickAddToItinryBtnInMoreRoomTypes(data.expected.cityAreaHotelText,data.expected.firstRoomDescTxt)
        scrollToSpecificItnryAndClickAddToItinryBtn(data.expected.cityAreaHotelText)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Capture items added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

        //Capture First item is added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard=getHotelNameInTitleCard(0)

        //Capture Second item is added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard=getHotelNameInTitleCard(1)

        //Go To Itinerary
        goToItinerary(data, resultData)
        //Enter Traveller Details
        travellerDetails(data, resultData,2,2,0,0,data.input.children)

        //Book Hotel Item
        at ItineraryTravllerDetailsPage

        //click on Book button
        clickOnBookIcon()
        sleep(5000)
        waitTillLoadingIconDisappears()
        clickOnTravellerCheckBox(0)
        sleep(3000)
        clickOnTravellerCheckBox(3)
        sleep(3000)
        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Screen Display Status
        resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

        //Title text-Booking Confirmed
        resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()

        //Traveller Values
        String trvlrValues
        List expectedTravellerName = []
        List actualtravellerName = []
        int travellers=data.input.pax+data.input.children.size()

            trvlrValues = resultData.hotel.itineraryPage.expectedleadTravellerName + " (Lead Name), " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName
            //println("Debug Travlr Valu:$trvlrValues")
            List<String> tempTravellerList = trvlrValues.tokenize(",")
            //println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i <= 1; i++) {
                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))
                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
                //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
            }


        resultData.hotel.confirmationPage.travellers.put("actTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expTravellers", expectedTravellerName)


        //read First Booking id
        resultData.hotel.itineraryPage.firstBookingID=getBookingIdFromBookingConfirmed(0)
        println("First Booking ID: $resultData.hotel.itineraryPage.firstBookingID")

        //read Itineary Reference Number & name
        resultData.hotel.itineraryPage.readitinearyIDAndName=getItinearyID(0)

        //item name
        resultData.hotel.itineraryPage.readFirstItemName=getConfirmBookedTransferName(0)
        //item name
        resultData.hotel.itineraryPage.readSecondItemName=getConfirmBookedTransferName(1)

        //item address
        //resultData.hotel.itineraryPage.readItemAddressTxt=getTransferDescBookingConfirmed()
        resultData.hotel.itineraryPage.readFirstItemAddressTxt=getTransferDescBookingConfirmed()

            try{
                //< PDF voucher > button
                resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()

            }catch(Exception e){
                //< PDF voucher > button
                resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = "Unable To Read From UI"

            }


        /*try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            Assert.assertFalse(true,"Failed To Confirm Booking")
        }*/


        //Close Booking confirmation
        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //Get Booking ID for first item
        String complTxt=getBookingIdFromBookedItems(0)

        List<String> bookingIdTxt=complTxt.tokenize("(")

        String firstBookingId=bookingIdTxt.getAt(0)
        resultData.hotel.itineraryPage.firstBookingID=firstBookingId

        //Get Booking ID for second item
        String completeTxt=getBookingIdFromBookedItems(0)

        List<String> bkingIdTxt=completeTxt.tokenize("(")

        String secondItemBookingId=bkingIdTxt.getAt(0)
        resultData.hotel.itineraryPage.scndbookingID=secondItemBookingId
    }

    protected def addSecondItem(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //collapse
            hideItenaryBuilder()
            sleep(3000)
        }


        at HotelSearchPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        int chkInDays=data.input.checkInDays.toString().toInteger()+5
        println("check in days:$chkInDays")
        int chkOutDays=data.input.checkOutDays.toString().toInteger()+7
        println("check out days:$chkOutDays")
        entercheckInCheckOutDate(chkInDays,chkOutDays)
        clickPaxRoom()
        sleep(9000)

        clickPaxRoom()
        //sleep(3000)

        clickPaxNumOfRooms(data.input.noOfRooms.toString())
        sleep(3000)


        //Enter Pax
        clickPaxRoom()
        sleep(3000)

        selectNumberOfAdults(data.input.pax.toString(), 0)
        sleep(3000)

        if(data.input.children.size()==0){
            //at ItineraryTravllerDetailsPage
            //scrollToBottomOfThePage()
            at HotelSearchPage
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
        }

        if(data.input.children.size()>0){
            //at ItineraryTravllerDetailsPage
            //scrollToBottomOfThePage()
            at HotelSearchPage
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
            for(int i=0;i<data.input.children.size();i++){
                enterChildrenAge(data.input.children.getAt(i).toString(),"0", i)
                sleep(1000)
            }
        }

        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage
        if(getCotsRequestedPopupDisplayStatus()==true){
            clickCloseLightbox()
            sleep(4000)
        }


        waitTillAddToItineraryBtnLoads()

        //item should return in search results
        resultData.hotel.searchResults.itineraryBuilder.actualSecondItemInSearchResults=getItemAvailabilityInSearchResults(data.expected.cityAreaHotelTxt)

        //click <Add to itinerary> button against Standard Triple hotel item: Howard Prince -SITUJ07XMLAuto
        //scrollToSpecificItnryAndClickAddToItinryBtn(data.expected.cityAreaHotelTxt)
        scrollToSpecificItinryAndClickAddToItinryBtn(data.expected.cityAreaHotelTxt)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        at HotelSearchResultsPage

       /* if(getCotsRequestedPopupDisplayStatus()==true){
            clickCloseLightbox()
            sleep(2000)
        }*/
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Capture items added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

        //Capture item is added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actItinryHotelname=getHotelNameInTitleCard(1)

        //Go To Itinerary
        at ItenaryBuilderPage
        //hideItenaryBuilder()


            resultData.hotel.searchResults.itineraryBuilder.expPrice=getItenaryPrice(1)
            println("ExpectedPrice: $resultData.hotel.searchResults.itineraryBuilder.expPrice")


        //click Go to Itinerary Link
        clickOnGotoItenaryButton()
        sleep(2000)

        at ItineraryTravllerDetailsPage

        //Capture Traveller Label Text
        resultData.hotel.searchResults.itineraryBuilder.actTravellerLabelTxt=getHeaderTxtInBookedItemsListScrn(0)
        //resultData.hotel.searchResults.itineraryBuilder.actTravellerLabelTxt=getTravellelersLabelName(1)

        //Book Hotel Item
        at ItineraryTravllerDetailsPage

        //click on Book button
        clickOnBookIcon()
        sleep(5000)
        waitTillLoadingIconDisappears()
        int maxTravellers
        if(data.input.infant==true){
            maxTravellers=data.input.pax+(data.input.children.size()-1)
        }else{
            maxTravellers=data.input.pax+data.input.children.size()
        }

        //Selecting all travellers
        for(int i=0;i<maxTravellers;i++) {
            if(getTravellerCheckBoxCheckedStatus(i)==false){
                clickOnTravellerCheckBox(i)
                sleep(3000)
            }

        }

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Screen Display Status
        resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

        //Booking confirmed lightbox displayed

        //Title text-Booking Confirmed
        resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()


        //Traveller Values
        String trvlrValues
        List expectedTravellerName = []
        List actualtravellerName = []
        int travellers=data.input.pax+data.input.children.size()
        if(data.input.infant==true) {
            trvlrValues = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + data.expected.childFirstName + " " + data.expected.childLastName + "" + "(" + data.input.children.getAt(0).toString() + "yrs)"+"+1 infant"
            List<String> tempInfantList = trvlrValues.tokenize("+")
            String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = tmpTravellers.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i < travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            }

            for (int i = 0; i < travellers - 1; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim())

            }
            expectedTravellerName.add("+" + tempInfantList.getAt(1).trim())


        }else {
            trvlrValues = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
            //println("Debug Travlr Valu:$trvlrValues")
            List<String> tempTravellerList = trvlrValues.tokenize(",")
            //println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i <= 2; i++) {
                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))
                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
                //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
            }
        }
        resultData.hotel.confirmationPage.travellers.put("actTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expTravellers", expectedTravellerName)


        //read Booking id
        resultData.hotel.itineraryPage.scndbookingID=getBookingIdFromBookingConfirmed(0)
        println("Second Item Booking ID: $resultData.hotel.itineraryPage.scndbookingID")

        //read Itineary Reference Number & name
        resultData.hotel.itineraryPage.readitinearyIDAndName=getItinearyID(0)

        //item name
        //resultData.hotel.itineraryPage.readItemName=getConfirmBookedTransferName()
        resultData.hotel.itineraryPage.readSecondItemName=getConfirmBookedTransferName()

        //item address
        //resultData.hotel.itineraryPage.readItemAddressTxt=getTransferDescBookingConfirmed()
        resultData.hotel.itineraryPage.readSecondItemAddressTxt=getTransferDescBookingConfirmed()

        try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = "Unable To Read From UI"
        }
       /*
        try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            Assert.assertFalse(true,"Failed To Confirm Booking")
        }*/

        //Close Booking confirmation
        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)


    }

    protected def addItinerary(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
        sleep(3000)
        clickPaxRoom()
        sleep(3000)

        clickPaxNumOfRooms(data.input.noOfRooms.toString())
        sleep(3000)
        //Enter Pax
        clickPaxRoom()


        selectNumberOfAdults(data.input.pax.toString(), 0)

        if(data.input.children.size()==0){
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
        }

        if(data.input.children.size()>0){
            selectNumberOfChildren(data.input.children.size().toString() , 0)
            sleep(1000)
            for(int i=0;i<data.input.children.size();i++){
                enterChildrenAge(data.input.children.getAt(i).toString(),"0", i)
                sleep(1000)
            }
        }

        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage

        waitTillAddToItineraryBtnLoads()

        //item should return in search results
        resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults=getHotelItemAvailabilityInSearchResults(data.expected.cityAreaHotelText)

        clickAddToitinerary(0)
        sleep(7000)

        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Capture items added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

        //Capture item is added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname=getHotelNameInTitleCard(0)

    }


    protected def addToItineraryMultiRoom(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
        sleep(3000)
        clickPaxRoom()
        sleep(3000)

           clickPaxNumOfRooms(data.input.noOfRooms.toString())
            sleep(3000)
            selectNumberOfAdults(data.input.pax.toString(), 1)

            selectNumberOfChildren(data.input.children_FirstRoom.size().toString() , 1)


            enterChildrenAge(data.input.children_FirstRoom.getAt(0).toString(),"1", 0)
            sleep(1000)
            enterChildrenAge(data.input.children_FirstRoom.getAt(1).toString(),"1", 1)
            sleep(1000)

            selectNumberOfAdults(data.input.pax.toString(), 0)
            selectNumberOfChildren(data.input.children_SecondRoom.size().toString() , 0)
            enterChildrenAge(data.input.children_SecondRoom.getAt(0).toString(),"0", 0)
            sleep(1000)
            enterChildrenAge(data.input.children_SecondRoom.getAt(1).toString(),"0", 1)
            sleep(1000)


        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage
        if(getCotsRequestedPopupDisplayStatus()==true){
            clickCloseLightbox()
            sleep(4000)
        }

        waitTillAddToItineraryBtnLoads()

        //scrollToSpecificItineraryAndClickFreeCancellationLink(data.expected.cityAreaHotelText)
        scrollToSpecificItnryAndClickFreeCancelBasedOnRoomTypes(data.expected.firstRoomDescTxt,1)
        sleep(7000)

        resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt=getCancellationChargeRelatedText()

        clickCloseCloseLightbox()
        sleep(2000)

        //scrollToSpecificItnryAndClickAddToItinryBtnBasedOnRoomTypes(data.expected.firstRoomDescTxt,2)
        scrollToSpecificItnryAndClickAddToItinryBtnBasedOnRoomTypes(data.expected.firstRoomDescTxt,1)
        sleep(5000)

        //closeLightBox()
        //commenting since application flow got changed in 10.3
        //clickCloseLightbox()
        //sleep(2000)

        at ItineraryTravllerDetailsPage

        clickOnBackButtonInItineraryScreen()
        sleep(5000)

        at HotelSearchResultsPage

        scrollToSpecificItnryAndClickAddToItinryBtnBasedOnRoomTypes(data.expected.secndRoomDescTxt,1)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Capture items added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

            //Capture First item is added to itinerary builder
            resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard=getHotelNameInTitleCard(0)

            //Capture Second item is added to itinerary builder
            resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard=getHotelNameInTitleCard(1)


    }


    protected def "goToItinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItenaryBuilderPage
        //hideItenaryBuilder()

        if(data.input.multiroom==true){
            resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice=getItenaryPrice(0)
            println("Expected First Item Price: $resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice")

            resultData.hotel.searchResults.itineraryBuilder.expectedScndItemPrice=getItenaryPrice(1)
            println("Expected Second Item Price: $resultData.hotel.searchResults.itineraryBuilder.expectedScndItemPrice")

        }else{
            resultData.hotel.searchResults.itineraryBuilder.expectedPrice=getItenaryPrice(0)
            println("ExpectedPrice: $resultData.hotel.searchResults.itineraryBuilder.expectedPrice")
        }

        //click Go to Itinerary Link
        clickOnGotoItenaryButton()
        sleep(2000)

        at ItineraryTravllerDetailsPage

        //Capture Traveller Label Text
        //resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravllerLabelTextInTravellerDetailsPage()
        //modifed for AmndSingRoomAddRemAdltTest in 10.3 - 28th April ; changed index to 0
        resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravellelersLabelName(0)
    }

    protected def "travellerDetails"(HotelSearchData data, HotelTransferTestResultData resultData,int adultStartindx, int adultendindx, int childstrtindx, int childendindx,def children){

        at ItineraryTravllerDetailsPage

        //Input Title
        selectTitle(data.expected.title_txt,0)
        //Input First Name
        enterFirstName(data.expected.firstName,0)
        //Input Last Name
        enterLastName(data.expected.lastName,0)
        //Input Email Address
        enterEmail(data.expected.emailAddr,0)
        /*Country code removed in 10.3. Hence commenting code
        //Input Area Code
        enterCountryCode(data.expected.countryCode,0)
        sleep(1000)
        */
        //Input Telephone number
        enterTelephoneNumber(data.expected.telephone_Num,0)
        sleep(1000)
        int otherTravellers=getNoOfTravellersOtherThanLeadTraveller()
        for(int i=0;i<otherTravellers;i++){
            clickRemoveButton(0)
            sleep(2000)
        }

        sleep(2000)
        //Click on Save Button
        //clickonSaveButton()
        clickonSaveButton(0)
        waitTillLoadingIconDisappears()

        sleep(3000)

        //Capture lead traveller details
        resultData.hotel.itineraryPage.expectedleadTravellerName=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
        resultData.hotel.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)

        //Capture lead traveller - telephone number
        resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum=data.expected.defaultcountryCode+""+data.expected.telephone_Num
        resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum=getLeadTravellerName(1)

        //Caputre lead traveller - email address
        resultData.hotel.itineraryPage.expectedleadTravellerEmail=data.expected.emailAddr
        resultData.hotel.itineraryPage.actualLeadTravellerEmail=getLeadTravellerName(2)

        //Add Travellers 2 - adult To 4 - Adult
        addSaveAndVerifyAdultTravellers(adultStartindx,adultendindx,data)

        if(childstrtindx>0)
        {
            //Add Travellers5 - child To 6 - Child
            addSaveAndVerifyChildTravellers(childstrtindx,childendindx,data,children)
        }



    }

    protected def "bookHotelItem"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //click on Book button
        clickOnBookIcon()
        waitTillLoadingIconDisappears()
        sleep(5000)
        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(data.input.children.size()>0){
            if(getTravellerCheckBoxCheckedStatus(3)==false) {
                clickOnTravellerCheckBox(3)
            }
            sleep(3000)
        }

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        try{
        //Booking Confirmation Screen Display Status
        resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Complete Booking")
            softAssert.assertFalse(true,"Failed To Complete Booking")
        }
        //Booking confirmed lightbox displayed

        //Title text-Booking Confirmed
        resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()


        //capture traveller details
        resultData.hotel.confirmationPage.actualfirstTravellerName=getTravellerNamesConfirmationDialog(0)
        resultData.hotel.confirmationPage.actualsecondTravellerName=getTravellerNamesConfirmationDialog(1)
        resultData.hotel.itineraryPage.expectedscndTravellerName=data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName
        if(data.input.children.size()>0){
            //capture child traveller details
            resultData.hotel.confirmationPage.actualThirdTravellerName=getTravellerNamesConfirmationDialog(2)
            resultData.hotel.confirmationPage.expectedThirdTravellerName=data.expected.childFirstName+" "+data.expected.childLastName+"" + "(" + data.input.children.getAt(0).toString() + "yrs)"
        }

        //read Booking id
        resultData.hotel.itineraryPage.retrievedbookingID=getBookingIdFromBookingConfirmed(0)
        println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")

        //read Itineary Reference Number & name
        resultData.hotel.itineraryPage.readitinearyIDAndName=getItinearyID(0)

        //item name
        resultData.hotel.itineraryPage.readItemName=getConfirmBookedTransferName()

        //item address
        resultData.hotel.itineraryPage.readItemAddressTxt=getTransferDescBookingConfirmed()

        try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = "Unable To Read PDF Voucher button From UI"
        }

        /*try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            Assert.assertFalse(true,"Failed To Confirm Booking")
        }*/

        //Close Booking confirmation
        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

    }

    protected def "bookMultipleHotelItem"(HotelSearchData data, HotelTransferTestResultData resultData, def childrenroom1, def childrenroom2){

        at ItineraryTravllerDetailsPage

        //click on Book button
        clickOnBookIcon()
        sleep(5000)
        waitTillLoadingIconDisappears()
        //First Room
        int countChkBox=0
        int firstRoomTravellers=data.input.pax+data.input.children_FirstRoom.size()

            for (int j = 1; j <= firstRoomTravellers-1; j++) {
                countChkBox = countChkBox + 1
                clickTravellerCheckBoxInAboutToBookScrn(1, countChkBox)
                sleep(2000)
            }
        //Second Room
        int scndRoomTravellers=data.input.pax+data.input.children_SecondRoom.size()

        for (int j = 1; j <= scndRoomTravellers; j++) {
            countChkBox = countChkBox + 1
            clickTravellerCheckBoxInAboutToBookScrn(2, countChkBox)
            sleep(2000)
        }


        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Screen Display Status
        resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

        //Booking confirmed lightbox displayed

        //Title text-Booking Confirmed
        resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()


        //capture traveller details

        //Traveller Values
        String trvlrValues=resultData.hotel.itineraryPage.expectedleadTravellerName+"  (Lead Name)"+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + childrenroom1.getAt(0).toString() + "yrs)"+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + childrenroom2.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + childrenroom2.getAt(1).toString() + "yrs)"
        //println("Debug Travlr Valu:$trvlrValues")
        List<String> tempTravellerList=trvlrValues.tokenize(",")
        //println("Debug tokenized Travlr Valu:$tempTravellerList")
        List expectedTravellerName=[]
        List actualtravellerName = []

        for(int i=0;i<=6;i++){
            expectedTravellerName.add(tempTravellerList.getAt(i).trim())
            actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
        }

        resultData.hotel.confirmationPage.travellers.put("actTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expTravellers", expectedTravellerName)


        //read Booking id
        resultData.hotel.itineraryPage.retrievedbookingID=getBookingIdFromBookingConfirmed(0)
        println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")

        //read Itineary Reference Number & name
        resultData.hotel.itineraryPage.readitinearyIDAndName=getItinearyID(0)

        //item name
        resultData.hotel.itineraryPage.readItemName=getConfirmBookedTransferName()

        //item address
        resultData.hotel.itineraryPage.readItemAddressTxt=getTransferDescBookingConfirmed()

        try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = "Unable To Read From UI PDF ICON"
        }

    /*    try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            Assert.assertFalse(true,"Failed To Confirm Booking")
        }*/

        //Close Booking confirmation
        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

    }


    protected def "bookHotelItemWithAllTravelers"(HotelSearchData data, HotelTransferTestResultData resultData,def children){

        at ItineraryTravllerDetailsPage

        //click on Book button
        clickOnBookIcon()
        sleep(5000)
        waitTillLoadingIconDisappears()
        int maxTravellers=data.input.pax+data.input.children.size()
        //boolean checkBoxStatus
        //Selecting all travellers
        for(int i=0;i<maxTravellers;i++) {
            //sleep(3000)
            if(getTravellerCheckBoxCheckedStatus(i)==false)
            {
                clickOnTravellerCheckBox(i)
                sleep(2000)
            }

        }

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Screen Display Status
        resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

        //Booking confirmed lightbox displayed

        //Title text-Booking Confirmed
        resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()


        //Traveller Values
        String trvlrValues=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+"" + "(" + children.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+ "" + "(" + children.getAt(1).toString() + "yrs)"
        //println("Debug Travlr Valu:$trvlrValues")
        List<String> tempTravellerList=trvlrValues.tokenize(",")
        //println("Debug tokenized Travlr Valu:$tempTravellerList")
        List expectedTravellerName=[]
        List actualtravellerName = []

        for(int i=0;i<=3;i++){
            expectedTravellerName.add(tempTravellerList.getAt(i).trim())
            actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
        }

        resultData.hotel.confirmationPage.travellers.put("actTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expTravellers", expectedTravellerName)


        //read Booking id
        resultData.hotel.itineraryPage.retrievedbookingID=getBookingIdFromBookingConfirmed(0)
        println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")

        //read Itineary Reference Number & name
        resultData.hotel.itineraryPage.readitinearyIDAndName=getItinearyID(0)

        //item name
        resultData.hotel.itineraryPage.readItemName=getConfirmBookedTransferName()

        //item address
        resultData.hotel.itineraryPage.readItemAddressTxt=getTransferDescBookingConfirmed()

        try {
            //< PDF voucher > button
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            resultData.hotel.itineraryPage.actualPDFVoucherBtnDispStatus = false
            //softAssert.assertFalse(true,"Failed To Confirm Booking")
        }

        //Close Booking confirmation
        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

    }

    protected def "amendOccupancy"(HotelSearchData data, HotelTransferTestResultData resultData, int index){

        at ItineraryTravllerDetailsPage

        scrollToBottomOfThePage()
        //Amend
        clickOnCancelOrAmendTabButton(index)

        //Amend Booking screen display status
        resultData.hotel.amendBooking.actualAmendPopupDispStatus=getTravellerCannotBeDeletedPopupDisplayStatus()
        //Amend Booking Txt
        resultData.hotel.amendBooking.actualAmendTitleTxt=getCancellationHeader()
        //Close X function
        resultData.hotel.amendBooking.actualCloseXDisplayStatus=overlayCloseButton()
        //should open per default
        resultData.hotel.amendBooking.actualDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
        //should not open per default
        resultData.hotel.amendBooking.actualOccupancyTabInactStatus=getOccupancyTabActiveOrInactiveStatus()

        //Click Occupancy tab
        clickOccupancyTab()
        sleep(3000)
        //Occupancy tab opens up
        resultData.hotel.amendBooking.actualOccupancyTabactStatus=getOccupancyTabActiveOrInactiveStatus()

    }

    protected def "abtToAmndDtAddASpecialRemarkorComment"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Click Dates tab
        clickDatesTab()
        sleep(3000)
        //click on Check-in field
        clickonAmendModalCheckinDate()
        scrollToBottomOfThePage()

        //click check in : today + 32 days
        selectAmendCheckInDateCalender(data.input.checkInDays.toInteger()+2)
        sleep(3000)

        //click find button
        clickAmendFindButton()

        //click ok button
        clickAmendOKButton()
        sleep(3000)

        //Add Comment or Remark Text
        resultData.hotel.itineraryPage.actualSpecialRemarkOrCommentTxt=getSpecialRemarkOrCommentTxt()
        scrollToRemarksTxt()

        //click on downside arrow
        clickExpandRemarksTandC()
        sleep(2000)

        clickWilArrivWithOutVochrCheckBox()
        sleep(2000)

        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()

        //Please Note txt
        resultData.hotel.amendBooking.actualPlzNoteTxtInBookingConfScrn=getPleaseNoteTxtInBookingConfirmedScrn()

        //Remarks
        resultData.hotel.amendBooking.actualRemarksTxtInBookingConfScrn=getRemarksTxtInBookingConfirmedScrn(2)

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //Booked Item Section, Should display "Please note: Will arrive without voucher"
        resultData.hotel.amendBooking.actualPlzNoteAndRemarksTxtInBkdItmsSec=getRemarksInBookeddetailsScrn()
        resultData.hotel.amendBooking.expectedPlzNoteAndRemarksTxtInBkdItmsSec=data.expected.pleaseNoteTxt+" "+data.expected.chkBoxWilArriveWithOutVoucherTxt

        sleep(3000)
    }
    protected def "abtToAmndOcupAddASpecialRemarkorComment"(HotelSearchData data, HotelTransferTestResultData resultData){

       at ItineraryTravllerDetailsPage

        scrollToBottomOfThePage()
        //Amend
        clickOnCancelOrAmendTabButton(1)
        sleep(3000)
        //Click Occupancy tab
        clickOccupancyTab()
        sleep(3000)
        //Infant 2
        selectNumberOfInfants(2)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)
        //click ok button
        clickAmendOKButton()
        sleep(3000)

        //Add Comment or Remark Text
        resultData.hotel.itineraryPage.actualSpecialRemarkOrCommentTxt=getSpecialRemarkOrCommentTxt()
        scrollToRemarksTxt()

        //click on downside arrow
        clickExpandRemarksTandC()
        sleep(2000)

        clickWilArrivWithOutVochrCheckBox()
        sleep(2000)

        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()

        //Please Note txt
        resultData.hotel.amendBooking.actualPlzNoteTxtInBookingConfScrn=getPleaseNoteTxtInBookingConfirmedScrn()

        //Remarks
        resultData.hotel.amendBooking.actualRemarksTxtInBookingConfScrn=getRemarksTxtInBookingConfirmedScrn(2)

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //Booked Item Section, Should display "Please note: Will arrive without voucher"
        resultData.hotel.amendBooking.actualPlzNoteAndRemarksTxtInBkdItmsSec=getRemarksInBookeddetailsScrn()
        resultData.hotel.amendBooking.expectedPlzNoteAndRemarksTxtInBkdItmsSec=data.expected.pleaseNoteTxt+" "+data.expected.chkBoxWilArriveWithOutVoucherTxt

        sleep(3000)
    }

    protected def "amendOccupancyDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData, int index){

        at ItineraryTravllerDetailsPage

        scrollToBottomOfThePage()
        //Amend
        clickOnCancelOrAmendTabButton(index)

        //Amend Booking screen display status
        resultData.hotel.amendBooking.actualAmendPopupDispStatus=getTravellerCannotBeDeletedPopupDisplayStatus()
        //Amend Booking Txt
        resultData.hotel.amendBooking.actualAmendTitleTxt=getCancellationHeader()
        //Close X function
        resultData.hotel.amendBooking.actualCloseXDisplayStatus=overlayCloseButton()
        //should open per default
        resultData.hotel.amendBooking.actualDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
        //should not open per default
        resultData.hotel.amendBooking.actualOccupancyTabInactStatus=getOccupancyTabActiveOrInactiveStatus()
    }

    protected def "amndOccupDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData, int index){

        at ItineraryTravllerDetailsPage

        scrollToBottomOfThePage()
        //Amend
        clickOnCancelOrAmendTabButton(index)

        //Amend Booking screen display status
        resultData.hotel.amendBooking.actAmendPopupDispStatus=getTravellerCannotBeDeletedPopupDisplayStatus()
        //Amend Booking Txt
        resultData.hotel.amendBooking.actAmendTitleTxt=getCancellationHeader()
        //Close X function
        resultData.hotel.amendBooking.actCloseXDisplayStatus=overlayCloseButton()
        //should open per default
        resultData.hotel.amendBooking.actDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
        //should not open per default
        resultData.hotel.amendBooking.actOccupancyTabInactStatus=getOccupancyTabActiveOrInactiveStatus()
    }

    protected def "amendOccupancyNADatesTab"(HotelSearchData data, HotelTransferTestResultData resultData, int index){

        at ItineraryTravllerDetailsPage

        scrollToBottomOfThePage()
        //Amend
        clickonAmendDisabledTab()

        try {
            //captrue tool tip
            resultData.hotel.amendBooking.actualToolTip = getAmendMouseHoverTxt()
        }catch(Exception e){
            resultData.hotel.amendBooking.actualToolTip = "Unable To Read Tool Tip From UI"
        }
    }




    protected def "currentDetails"(HotelSearchData data, HotelTransferTestResultData resultData){
        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //text should show
        resultData.hotel.amendBooking.actualOccupTxt=getOccupancyInsideTxtInNonAmendablePopup(1)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInOccupancyTab=getItemNameInOccupanceTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        //booked room type
        resultData.hotel.amendBooking.actualroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt
        }else{
            resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt
        }


        //meal basis
        resultData.hotel.amendBooking.actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actualfreeCancelTxt=getFreeCancelTxtInOccupanceTab(1)
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.expectedFreeCanclTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInAndNumOfNight=getCheckInTxtInOccupanceTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.expectedCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()

        if(data.input.children.size()>0 && (data.input.child==true)){
            //travellers
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInOccupanceTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName +", " + " "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(0).toString() + "yrs)"

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //travellers
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInOccupanceTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+" "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(0).toString() + "yrs)"+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(1).toString() + "yrs)"

        }
        else if(data.input.multiroom==true) {
            //travellers
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInOccupanceTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.thirdTraveller_title_txt+" "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_SecondRoom.getAt(0).toString() + "yrs)"+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_SecondRoom.getAt(1).toString() + "yrs)"

        }
        else {
            //travellers
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInOccupanceTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName
        }
        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInOccupancyTab(1)

        //Commission
        resultData.hotel.amendBooking.actualCommissionTxt=getCommissionInOccupancyTab(1)

    }
    protected def "currentDetailsDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //text should show
        resultData.hotel.amendBooking.actualOccupTxt=getOccupancyInsideTxtInDatesTab(1)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInOccupancyTab=getItemNameInDatesTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        //booked room type
        resultData.hotel.amendBooking.actualroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt
        //meal basis
        resultData.hotel.amendBooking.actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        //resultData.hotel.amendBooking.actualfreeCancelTxt=getFreeCancelTxtInDatesTab(1)
        resultData.hotel.amendBooking.actfreeCancelText=getCancelTxtInDatesTab()
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.expFreeCancelTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()
        resultData.hotel.amendBooking.expectedFreeCanclTxt=resultData.hotel.amendBooking.expFreeCancelTxt
        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInAndNumOfNight=getCheckInTxtInDatesTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.expectedCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInDatesTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()

        if(data.input.multiroom==true) {
            //travellers
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)" + " +1 infant"
        }else if(data.input.infant==true){
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+" +1 infant"

        }
        else{
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"

        }
        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInDatesTab(1)

        //Commission
        resultData.hotel.amendBooking.actualCommissionTxt=getCommissionInDatesTab(1)

    }

    protected def "currentDetailsDatesTabSecondHotel"(HotelSearchData data, HotelTransferTestResultData resultData){
        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //text should show
        resultData.hotel.amendBooking.actualOccupTxt=getOccupancyInsideTxtInDatesTab(1)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInOccupancyTab=getItemNameInDatesTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        //booked room type
        resultData.hotel.amendBooking.actualroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt
        //meal basis
        resultData.hotel.amendBooking.actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actualfreeCancelTxt=getFreeCancelTxtInDatesTab(1)
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.expectedFreeCanclTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInAndNumOfNight=getCheckInTxtInDatesTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.expectedCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInDatesTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()

        if(data.input.multiroom==true) {
            //travellers
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)" + " +1 infant"
        }else if(data.input.infant==true){
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+" +1 infant"

        }
        else{
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"

        }
        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInDatesTab(1)

        //Commission
        resultData.hotel.amendBooking.actualCommissionTxt=getCommissionInDatesTab(1)

    }


    protected def "currntDetailsDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //text should show
        resultData.hotel.amendBooking.actualOccupTxt=getOccupancyInsideTxtInDatesTab(1)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInOccupancyTab=getItemNameInDatesTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        //booked room type
        resultData.hotel.amendBooking.actualroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt
        //meal basis
        resultData.hotel.amendBooking.actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actualfreeCancelTxt=getFreeCancelTxtInDatesTab(1)
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.expectedFreeCanclTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInAndNumOfNight=getCheckInTxtInDatesTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.expectedCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInDatesTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()


            //travellers
            resultData.hotel.amendBooking.actualTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName

        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInDatesTab(1)

        //Commission
        resultData.hotel.amendBooking.actualCommissionTxt=getCommissionInDatesTab(1)

    }

    protected def "curntDetailsDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        def checkInDate
        int totalNights
        int checkOutDays

            checkInDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 5, "ddMMMyy").toUpperCase()
            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays = data.input.checkOutDays.toString().toInteger() + 7.toInteger()
            totalNights = checkOutDays - (data.input.checkInDays.toString().toInteger() + 5)
            println("Number Of Nights in Dates Tab:$totalNights")


        at ItineraryTravllerDetailsPage

        //text should show
        resultData.hotel.amendBooking.actOccupTxt=getOccupancyInsideTxtInDatesTab(1)

        //Item Name
        resultData.hotel.amendBooking.actItemNameInOccupancyTab=getItemNameInDatesTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        //booked room type
        resultData.hotel.amendBooking.actroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt
        //meal basis
        resultData.hotel.amendBooking.actmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actfreeCancelTxt=getFreeCancelTxtInDatesTab(1)
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.expFreeCanclTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actCheckInAndNumOfNight=getCheckInTxtInDatesTab(1)
        if (totalNights >1)
            dur= "Check in: "+checkInDate+", "+totalNights+" nights"
        else dur = "Check in: "+checkInDate+", "+totalNights+" night"
        resultData.hotel.amendBooking.expCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInDatesTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actPaxText=tempPAXAndTravellersList.getAt(0).trim()

        if(data.input.multiroom==true) {
            //travellers
            resultData.hotel.amendBooking.actTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)" + " +1 infant"
        }else if(data.input.infant==true){
            //travellers
            resultData.hotel.amendBooking.actTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)" + " +1 infant"

        }
        else{
            resultData.hotel.amendBooking.actTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expectedTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"

        }
        //Room Amount and Currency
        resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInDatesTab(1)

        //Commission
        resultData.hotel.amendBooking.actCommissionTxt=getCommissionInDatesTab(1)

    }
    protected def "curntDetailsInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        def checkInDate
        int totalNights


        checkInDate=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        totalNights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()


        at ItineraryTravllerDetailsPage

        //text should show
        resultData.hotel.amendBooking.actOccupTxt=getOccupancyInsideTxtInDatesTab(1)

        //Item Name
        resultData.hotel.amendBooking.actItemNameInOccupancyTab=getItemNameInDatesTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        //booked room type
        resultData.hotel.amendBooking.actroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt
        //meal basis
        resultData.hotel.amendBooking.actmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actfreeCancelTxt=getFreeCancelTxtInDatesTab(1)
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.expFreeCanclTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actCheckInAndNumOfNight=getCheckInTxtInDatesTab(1)
        if (totalNights >1)
            dur= "Check in: "+checkInDate+", "+totalNights+" nights"
        else dur = "Check in: "+checkInDate+", "+totalNights+" night"
        resultData.hotel.amendBooking.expCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInDatesTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actPaxText=tempPAXAndTravellersList.getAt(0).trim()


            //travellers
            resultData.hotel.amendBooking.actTravellersText = getTravellersInDatesTab(1)
            resultData.hotel.amendBooking.expTravellersText = data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName

        //Room Amount and Currency
        resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInDatesTab(1)

        //Commission
        resultData.hotel.amendBooking.actCommissionTxt=getCommissionInDatesTab(1)

    }


    protected def "checkIfDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "ddMMMyy")

        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actualCheckAvailTxt=getOccupancyInsideTxtInDatesTab(2)

        //Check-in text should show
        resultData.hotel.amendBooking.actualCheckInTxt=getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per confirmed
        resultData.hotel.amendBooking.actualCheckInValue=amendModalCheckinDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckInValue=chkInDt.toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actualCheckOutTxt=getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per confirmed
        resultData.hotel.amendBooking.actualCheckOutValue=amendModalCheckOutDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckOutValue=chkOutDt.toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actualFindBtnStatus=getFindButtonEnableOrDisabledStatus()

        //click on Check-in field
        clickonAmendModalCheckinDate()

        //a calendar opens
        resultData.hotel.amendBooking.actualCalendarOpenStatus=calendersDisplayed()
        scrollToBottomOfThePage()
        //calendar has check in date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expectedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "d")
        //calendar has check out date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expectedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "d")

        //click check in : today + 10 days
        selectAmendCheckInDateCalender(data.expected.chkInDays)
        sleep(3000)
        //today + 10 days highlighted as check in date
        resultData.hotel.amendBooking.actUpdatedCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays, "d")

        //today + 11 days highlighted as check out date
        resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays+1, "d")

        //click find button
        clickAmendFindButton()

        //Amend Booking - Dates tab should remains open
        resultData.hotel.amendBooking.actDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
    }

    protected def "checkIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "ddMMMyy")

        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actualCheckAvailTxt=getOccupancyInsideTxtInDatesTab(2)

        //Check-in text should show
        resultData.hotel.amendBooking.actualCheckInTxt=getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per confirmed
        resultData.hotel.amendBooking.actualCheckInValue=amendModalCheckinDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckInValue=chkInDt.toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actualCheckOutTxt=getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per confirmed
        resultData.hotel.amendBooking.actualCheckOutValue=amendModalCheckOutDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckOutValue=chkOutDt.toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actualFindBtnStatus=getFindButtonEnableOrDisabledStatus()

        //click find button
        clickAmendFindButton()

        resultData.hotel.amendBooking.actualAmendDateNoChangeErrTxt=getamendDateNoChangeError()

        //click on Check-out field
        clickonAmendModalCheckOutDate()
        //a calendar opens
        resultData.hotel.amendBooking.actualCalendarOpenStatus=calendersDisplayed()

        scrollToBottomOfThePage()
        //calendar has check in date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckInDaySelected=getCheckInDaySelected()
        //resultData.hotel.amendBooking.expectedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "dd")
        resultData.hotel.amendBooking.expectedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "d")
        //calendar has check out date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckOutDaySelected=getCheckOutDaySelected()
        //resultData.hotel.amendBooking.expectedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "dd")
        resultData.hotel.amendBooking.expectedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "d")
        sleep(3000)
        //click: Check-out: today + 32 days
        selectAmendCheckOutDateCalender(data.input.checkInDays.toInteger()+2)
        sleep(3000)

        //today + 11 days highlighted as check out date
        resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+2, "dd")

        //click find button
        clickAmendFindButton()

        //Amend Booking - Dates tab should remains open
        resultData.hotel.amendBooking.actDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
    }

    protected def "chkIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "ddMMMyy")

        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actualCheckAvailTxt=getOccupancyInsideTxtInDatesTab(2)

        //Check-in text should show
        resultData.hotel.amendBooking.actualCheckInTxt=getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per confirmed
        resultData.hotel.amendBooking.actualCheckInValue=amendModalCheckinDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckInValue=chkInDt.toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actualCheckOutTxt=getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per confirmed
        resultData.hotel.amendBooking.actualCheckOutValue=amendModalCheckOutDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckOutValue=chkOutDt.toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actualFindBtnStatus=getFindButtonEnableOrDisabledStatus()

        //click on Check-in field
        clickonAmendModalCheckinDate()
        sleep(2000)
        //a calendar opens
        resultData.hotel.amendBooking.actualCalendarOpenStatus=calendersDisplayed()

        scrollToBottomOfThePage()
        //moveCursorToAnElement()
        //calendar has check in date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expectedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "d")
        //calendar has check out date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expectedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "d")
        sleep(3000)

        //click check in field
        clickonAmendModalCheckinDate()
        sleep(2000)
        scrollToBottomOfThePage()
        //click check in : today + 60 days
        enterCheckinDateValue(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy"))
        sleep(5000)
        selectAmendCheckInDateCalender(data.input.checkInDays.toInteger()+20)
        sleep(3000)
        //calendar has check in date highlighted per updated
        resultData.hotel.amendBooking.actualUpdatedCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expectedUpdatedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "d")
        //calendar has check out date highlighted per updated
        resultData.hotel.amendBooking.actualUpdatedCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expectedUpdatedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+21, "d")

        //click on Check-out field
        clickonAmendModalCheckOutDate()
        sleep(2000)
        //click: Check-out: today + 63 days
        selectAmendCheckOutDateCalender(data.input.checkOutDays.toInteger()+22)
        sleep(3000)

        //today + 63 days highlighted as check out date
        resultData.hotel.amendBooking.actualUpdatedCheckOutDaySel=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expectedUpdatedCheckOutDaySel=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+22, "dd")

        //click find button
        clickAmendFindButton()

        //Amend Booking - Dates tab should remains open
        resultData.hotel.amendBooking.actDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
    }

    protected def "chkIfAllowedDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "ddMMMyy")

        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actualCheckAvailTxt=getOccupancyInsideTxtInDatesTab(2)

        //Check-in text should show
        resultData.hotel.amendBooking.actualCheckInTxt=getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per confirmed
        resultData.hotel.amendBooking.actualCheckInValue=amendModalCheckinDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckInValue=chkInDt.toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actualCheckOutTxt=getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per confirmed
        resultData.hotel.amendBooking.actualCheckOutValue=amendModalCheckOutDateValue().toUpperCase()
        resultData.hotel.amendBooking.expectedCheckOutValue=chkOutDt.toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actualFindBtnStatus=getFindButtonEnableOrDisabledStatus()

        //click on Check-in field
        clickonAmendModalCheckinDate()
        sleep(2000)
        //a calendar opens
        resultData.hotel.amendBooking.actualCalendarOpenStatus=calendersDisplayed()

        scrollToBottomOfThePage()
        //moveCursorToAnElement()
        //calendar has check in date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expectedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "d")
        //calendar has check out date highlighted per confimed
        resultData.hotel.amendBooking.actualCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expectedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "d")
        sleep(3000)

        //click check in field
        clickonAmendModalCheckinDate()
        sleep(2000)
        scrollToBottomOfThePage()
        //click check in : today + 60 days
        selectAmendCheckInDateCalender(data.input.checkInDays.toInteger()+20)
        sleep(3000)
        //calendar has check in date highlighted per updated
        resultData.hotel.amendBooking.actualUpdatedCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expectedUpdatedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "d")
        //calendar has check out date highlighted per updated
        resultData.hotel.amendBooking.actualUpdatedCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expectedUpdatedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+21, "d")

        //click find button
        clickAmendFindButton()

        //Amend Booking - Dates tab should remains open
        resultData.hotel.amendBooking.actDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
    }


    protected def "checkingIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+7, "ddMMMyy")

        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actCheckAvailTxt=getOccupancyInsideTxtInDatesTab(2)

        //Check-in text should show
        resultData.hotel.amendBooking.actCheckInTxt=getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per confirmed
        resultData.hotel.amendBooking.actChekInVal=amendModalCheckinDateValue().toUpperCase()
        resultData.hotel.amendBooking.expCheckInValue=chkInDt.toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actCheckOutTxt=getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per confirmed
        resultData.hotel.amendBooking.actChekOutVal=amendModalCheckOutDateValue().toUpperCase()
        resultData.hotel.amendBooking.expCheckOutValue=chkOutDt.toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actFindBtnStatus=getFindButtonEnableOrDisabledStatus()

        //click on Check-in field
        clickonAmendModalCheckinDate()
        sleep(3000)
        //a calendar opens
        resultData.hotel.amendBooking.actCalendarOpenStatus=calendersDisplayed()

        scrollToBottomOfThePage()
        //moveCursorToAnElement()
        //calendar has check in date highlighted per confimed
        resultData.hotel.amendBooking.actCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "d")
        //calendar has check out date highlighted per confimed
        resultData.hotel.amendBooking.actCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+7, "d")
        sleep(3000)

        //click check in field
        clickonAmendModalCheckinDate()
        sleep(2000)
        scrollToBottomOfThePage()
        //click check in : today + 35 days
        enterCheckinDateValue(dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy"))
        sleep(5000)
        selectAmendCheckInDateCalender(data.expected.chkInDays.toInteger())
        sleep(5000)
        //calendar has check in date highlighted per updated
        resultData.hotel.amendBooking.actUpdatedCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "d")
        //calendar has check out date highlighted per updated
        resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+1, "d")

        //click on Check-out field
        clickonAmendModalCheckOutDate()
        sleep(2000)
        //click: Check-out: today + 37 days
        selectAmendCheckOutDateCalender(data.expected.chkInDays.toInteger()+2)
        sleep(3000)

        //today + 37 days highlighted as check out date
        resultData.hotel.amendBooking.actUpdatedCheckOutDaySel=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckOutDaySel=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+2, "dd")

        //click find button
        clickAmendFindButton()

        //Amend Booking - Dates tab should remains open
        resultData.hotel.amendBooking.acDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
    }

    protected def "chkingIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "ddMMMyy")

        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actCheckAvailTxt=getOccupancyInsideTxtInDatesTab(2)

        //Check-in text should show
        resultData.hotel.amendBooking.actCheckInTxt=getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per confirmed
        resultData.hotel.amendBooking.actChekInVal=amendModalCheckinDateValue().toUpperCase()
        resultData.hotel.amendBooking.expCheckInValue=chkInDt.toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actCheckOutTxt=getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per confirmed
        resultData.hotel.amendBooking.actChekOutVal=amendModalCheckOutDateValue().toUpperCase()
        resultData.hotel.amendBooking.expCheckOutValue=chkOutDt.toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actFindBtnStatus=getFindButtonEnableOrDisabledStatus()

        //click on Check-in field
        clickonAmendModalCheckinDate()
        sleep(2000)
        //a calendar opens
        resultData.hotel.amendBooking.actCalendarOpenStatus=calendersDisplayed()

        scrollToBottomOfThePage()
        //moveCursorToAnElement()
        //calendar has check in date highlighted per confimed
        resultData.hotel.amendBooking.actCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "d")
        //calendar has check out date highlighted per confimed
        resultData.hotel.amendBooking.actCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "d")
        sleep(3000)

        //click check in field
        clickonAmendModalCheckinDate()
        sleep(2000)
        scrollToBottomOfThePage()
        //click check in : today + 15 days
        enterCheckinDateValue(dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy"))
        sleep(5000)
        selectAmendCheckInDateCalender(data.expected.chkInDays.toInteger())
        sleep(5000)
        //calendar has check in date highlighted per updated
        resultData.hotel.amendBooking.actUpdatedCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "d")
        //calendar has check out date highlighted per updated
        resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+1, "d")

        //click find button
        clickAmendFindButton()

        //Amend Booking - Dates tab should remains open
        resultData.hotel.amendBooking.acDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
    }


    protected def "checkIfAllowInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        def chkInDt
        int totalNights
        int chkOutDays
        def chkOutDt

            chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger() + 7, "ddMMMyy")
            chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 5, "ddMMMyy").toUpperCase()
            println("checkInDate in Dates Tab:$chkInDt")
            chkOutDays = data.input.checkOutDays.toString().toInteger() + 7.toInteger()
            totalNights = chkOutDays - (data.input.checkInDays.toString().toInteger() + 5)
            println("Number Of Nights in Dates Tab:$totalNights")

        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actCheckAvailTxt=getOccupancyInsideTxtInDatesTab(2)

        //Check-in text should show
        resultData.hotel.amendBooking.actCheckInTxt=getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per confirmed
        resultData.hotel.amendBooking.actChkInVal=amendModalCheckinDateValue().toUpperCase()
        resultData.hotel.amendBooking.expChkInVal=chkInDt.toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actCheckOutTxt=getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per confirmed
        resultData.hotel.amendBooking.actChkOutVal=amendModalCheckOutDateValue().toUpperCase()
        resultData.hotel.amendBooking.expChkOutVal=chkOutDt.toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actFindBtnStatus=getFindButtonEnableOrDisabledStatus()

         //click on Check-out field
        clickonAmendModalCheckOutDate()
        //a calendar opens
        resultData.hotel.amendBooking.actCalendarOpenStatus=calendersDisplayed()

        scrollToBottomOfThePage()
        //calendar has check in date highlighted per confimed
        resultData.hotel.amendBooking.actCheckInDaySelected=getCheckInDaySelected()
        resultData.hotel.amendBooking.expCheckInDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "dd")
        //calendar has check out date highlighted per confimed
        resultData.hotel.amendBooking.actCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+7, "dd")
        sleep(3000)
        //click: Check-out: today + 37 days
        selectAmendCheckOutDateCalender(data.input.checkInDays.toInteger()+7)
        sleep(3000)

        //today + 37 days highlighted as check out date
        resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected=getCheckOutDaySelected()
        resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+7, "dd")

        //date (today + 38 days ) no longer highlighted
        resultData.hotel.amendBooking.actUpdatedCheckOutDayNotSelected=dateUtil.addDaysChangeDatetformat((data.input.checkInDays.toInteger()+8),"dd")
        resultData.hotel.amendBooking.expUpdatedCheckOutDayNotSelected=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+8, "dd")


        //click find button
        clickAmendFindButton()

        //Amend Booking - Dates tab should remains open
        resultData.hotel.amendBooking.actDatesTabActiveStatus=getDatesTabActiveOrInactiveStatus()
    }

    protected def "checkAvailabilityDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData) {
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger(), "ddMMMyy")

        int nights = data.input.checkOutDays.toString().toInteger() - data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actCheckAvailTxt = getOccupancyInsideTxtInDatesTab(3)

        //Check-in text should show
        resultData.hotel.amendBooking.actCheckInTxt = getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per changed
        resultData.hotel.amendBooking.actCheckInValue = amendModalCheckinDateValue().toUpperCase()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.expUpdtdCheckInDaySelected = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays, "ddMMMyy").toUpperCase()
        }else if(data.input.infant==true) {
            resultData.hotel.amendBooking.expUpdtdCheckInDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
        }
        else{
            resultData.hotel.amendBooking.expUpdtdCheckInDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        }
        //Check-Out text should show
        resultData.hotel.amendBooking.actCheckOutTxt = getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per changed
        resultData.hotel.amendBooking.actCheckOutValue = amendModalCheckOutDateValue().toUpperCase()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.expUpdtdCheckOutDaySelected = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays + 1, "ddMMMyy").toUpperCase()
        }else if(data.input.infant==true) {
            resultData.hotel.amendBooking.expUpdtdCheckOutDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+22, "ddMMMyy").toUpperCase()
        }
        else{
            resultData.hotel.amendBooking.expUpdtdCheckOutDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 2, "ddMMMyy").toUpperCase()
        }
        //Find function should be enabled
        resultData.hotel.amendBooking.actFindBtnStatus = getFindButtonEnableOrDisabledStatus()

    }

    protected def "chkAvailabilityDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData) {
        //def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        def chkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy")
        def chkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+20, "ddMMMyy")

        int nights = data.input.checkOutDays.toString().toInteger()+20 - data.input.checkInDays.toString().toInteger()+20
        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actCheckAvailTxt = getOccupancyInsideTxtInDatesTab(3)

        //Check-in text should show
        resultData.hotel.amendBooking.actCheckInTxt = getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per changed
        resultData.hotel.amendBooking.actCheckInValue = amendModalCheckinDateValue().toUpperCase()

        resultData.hotel.amendBooking.expUpdtdCheckInDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()

        //Check-Out text should show
        resultData.hotel.amendBooking.actCheckOutTxt = getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per changed
        resultData.hotel.amendBooking.actCheckOutValue = amendModalCheckOutDateValue().toUpperCase()

        resultData.hotel.amendBooking.expUpdtdCheckOutDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+20, "ddMMMyy").toUpperCase()

        //Find function should be enabled
        resultData.hotel.amendBooking.actFindBtnStatus = getFindButtonEnableOrDisabledStatus()

    }


    protected def "checkAvailabilityInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData) {

        at ItineraryTravllerDetailsPage

        //check availability text should show
        resultData.hotel.amendBooking.actCheckAvailText = getOccupancyInsideTxtInDatesTab(3)

        //Check-in text should show
        resultData.hotel.amendBooking.actCheckInText = getCheckInCheckOutLabelTxtInDatesTab(0)

        //check in date should show per changed
        resultData.hotel.amendBooking.actChkInValues = amendModalCheckinDateValue().toUpperCase()
        if(data.input.infant==true) {
            resultData.hotel.amendBooking.expUpdatdChkInDaySelected = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
        }else if(data.input.multiroom==true){
            resultData.hotel.amendBooking.expUpdatdChkInDaySelected = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
        }
        else{
            resultData.hotel.amendBooking.expUpdatdChkInDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 5, "ddMMMyy").toUpperCase()
        }

        //Check-Out text should show
        resultData.hotel.amendBooking.actCheckOutTxt = getCheckInCheckOutLabelTxtInDatesTab(1)

        //check out date should show per changed
        resultData.hotel.amendBooking.actChkOutValues = amendModalCheckOutDateValue().toUpperCase()
        if(data.input.infant==true) {
            resultData.hotel.amendBooking.expUpdatdChkOutDaySelected = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+2, "ddMMMyy").toUpperCase()
        }else if(data.input.multiroom==true){
            resultData.hotel.amendBooking.expUpdatdChkOutDaySelected = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+1, "ddMMMyy").toUpperCase()
        }
        else{
            resultData.hotel.amendBooking.expUpdatdChkOutDaySelected = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 7, "ddMMMyy").toUpperCase()
        }


        //Find function should be enabled
        resultData.hotel.amendBooking.actFindButnStatus = getFindButtonEnableOrDisabledStatus()

    }


    protected def "changeAddAndRemove"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Change, add or remove occupants availability:
        resultData.hotel.amendBooking.actualChangeOccupantsTxt=getChangeOccupantsTxtInOccupancyTab()

        //Occupancy 1
        resultData.hotel.amendBooking.actualOccupantListNumTxt=getOccupantListNumInOccupancyTab(1)
        resultData.hotel.amendBooking.expectedOccupantListNumTxt=data.expected.occupantTxt+" 1"

        //Title / first name / last name
        resultData.hotel.amendBooking.actualOccupantListNameTxt=getOccupantListNameInOccupancyTab(1)

        //Occupancy 2
        resultData.hotel.amendBooking.actualOccupantListNumTxt=getOccupantListNumInOccupancyTab(2)
        resultData.hotel.amendBooking.expectedOccupantListNumTxt=data.expected.occupantTxt+" 2"

        //Title / first name / last name
        resultData.hotel.amendBooking.actualOccupantNameTxt=getOccupantListNameInOccupancyTab(2)
        resultData.hotel.amendBooking.expectedOccupantNameTxt=data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName

        if(data.input.children.size()>0) {
            //Occupancy 3
            resultData.hotel.amendBooking.actOccupantListNumTxt = getOccupantListNumInOccupancyTab(3)
            resultData.hotel.amendBooking.expOccupantListNumTxt = data.expected.occupantTxt + " 3"

            //Title / first name / last name
            resultData.hotel.amendBooking.actOccupantNameTxt = getOccupantListNameInOccupancyTab(3)
            resultData.hotel.amendBooking.expOccupantNameTxt = data.expected.childFirstName + " " + data.expected.childLastName
        }
    }

    protected def "selectToAssign"(HotelSearchData data, HotelTransferTestResultData resultData,int adultStartindx, int adultendindx, int childstrtindx, int childendindx,def children){

        at ItineraryTravllerDetailsPage

        //Please select occupants names:
        resultData.hotel.amendBooking.actualPleaseSelectTxt=getPleaseSelectTxtInOccupancyTab()
        if(data.input.multiroom==true) {

            //tick box should present for each traveller listed in traveller details section
            travellersListCheckBoxExistenceCheck(adultStartindx,childendindx,data)

            //Get 1st traveller checked status
            travellersListCheckBoxChkdOrUnchkdStatus(adultStartindx,childendindx,data)



        }else{
            //tick box should present for each traveller listed in traveller details section
            travellersListCheckBoxExistenceCheck(1,childendindx,data)

            //Get 1st traveller checked status
            resultData.hotel.amendBooking.actualFirstTrvlchkBoxStatus=getTravellerCheckedStatus(1)

            //Get 2nd traveller checked status
            resultData.hotel.amendBooking.actualSecondTrvlchkBoxStatus=getTravellerCheckedStatus(2)
        }



        if(data.input.children.size()>0 && (data.input.child==true)){
            //Get 4th traveller checked status
            resultData.hotel.amendBooking.actualThirdTrvlchkBoxStatus=getTravellerCheckedStatus(4)
        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Get 3rd traveller checked status
            resultData.hotel.amendBooking.actualThirdTrvlchkBoxStatus=getTravellerCheckedStatus(3)
            //Get 4th traveller checked status
            resultData.hotel.amendBooking.actualFourthTrvlchkBoxStatus=getTravellerCheckedStatus(4)
        }

        if(!(data.input.multiroom==true)) {
            //lead traveller
            resultData.hotel.amendBooking.actualLeadTravlr = getTravellerNameTxt(1)
            resultData.hotel.amendBooking.expectedLeadTravlrTxt = data.expected.firstName + " " + data.expected.lastName + " " + "(lead name)"
        }
        //2nd to 5th traveller
        verifyAdultTravellers(adultStartindx,adultendindx,data)
        //6th to 9th traveller
        verifyChildTravellers(childstrtindx,childendindx,data,children)

        //infant text should show - if it is available for the room
        resultData.hotel.amendBooking.actualInfantLabTxt=getInfantLabelTxt()

        //dropdown list to select number of infant should show
        resultData.hotel.amendBooking.actualInfantDropdownListDispStatus=getInfantSelectBoxDisplayStatus()

        //list should show 0 - 1 or 0-2
        resultData.hotel.amendBooking.actualListInfantValues=getInfantDropDownValuesInOccupancyTab()

        //Confirm Amendment Button display status
        resultData.hotel.amendBooking.actualConfirmAmendmentButtonStatus=getConfirmAmendButtonDisplayStatus()

        //Confirm Amendment Button Disabled status
        resultData.hotel.amendBooking.actualConfirmAmendmentDisabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()

    }

    protected def "adding1adultpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //check 3rd traveller
        clickTravellersCheckBox(3)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)

    }

    protected def "adding1childpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //check 3rd traveller
        clickTravellersCheckBox(3)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)

    }

    protected def "add2infant"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Infant 2
        selectNumberOfInfants(2)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)

    }
    protected def "add2Child"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Infant 2
        clickTravellersCheckBox(7)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)

    }
    protected def "remove2child"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //uncheck 2nd traveller from room 2
        clickTravellersCheckBox(7)
        sleep(5000)
        clickConfirmAmendmentBtn()
        sleep(4000)

    }

    protected def "currentBookingDetailsShouldRetain"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //current booking detials - should retain

        //Current booking:  text should show
        resultData.hotel.amendBooking.actualOccupTxt=getOccupancyInsideTxtInNonAmendablePopup(1)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInOccupancyTab=getItemNameInOccupanceTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")

        resultData.hotel.amendBooking.actualroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt

        //meal basis
        resultData.hotel.amendBooking.actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actualfreeCancelTxt=getFreeCancelTxtInOccupanceTab(1)

        def cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.expectedFreeCanclTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInAndNumOfNight=getCheckInTxtInOccupanceTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.expectedCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actPaxTxt=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        resultData.hotel.amendBooking.actualTravellersText=getTravellersInOccupanceTab(1)

        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInOccupancyTab(1)

        //Commission
        resultData.hotel.amendBooking.actualCommissionTxt=getCommissionInOccupancyTab(1)

    }

    protected def "changeTo"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //change to: text should show
        resultData.hotel.amendBooking.actualOccupancyTxt=getOccupancyInsideTxtInNonAmendablePopup(2)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInOccupTab=getItemNameInOccupanceTab(2)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(2)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt=tempItinryCanclDescList.getAt(0).trim()

        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt
        }else{
            resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt
        }
        //meal basis
        resultData.hotel.amendBooking.actualmealBasisText=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        //resultData.hotel.amendBooking.actualfreeCancellationTxt=getFreeCancelTxtInOccupanceTab(2)
        resultData.hotel.amendBooking.actualfreeCancellationTxt=getFreeCancelTxtInOccupanceTab(1)

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInPlusNumOfNight=getCheckInTxtInOccupanceTab(2)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.expCheckInPlusNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(2)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actualPaxTxt=tempPAXAndTravellersList.getAt(0).trim()




        if(data.input.children.size()>0 && (data.input.child==true)){
            //travellers
            resultData.hotel.amendBooking.actualTravellersTxt=getTravellersInOccupanceTab(2)
            resultData.hotel.amendBooking.expTravellersTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+""+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.addchildren.getAt(0).toString() + "yrs)"+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.addchildren.getAt(1).toString() + "yrs)"

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //travellers
            resultData.hotel.amendBooking.actualTravellersTxt=getTravellersInOccupanceTab(2)
            resultData.hotel.amendBooking.expTravellersTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+""+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(0).toString() + "yrs)"+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(1).toString() + "yrs)"+" +2 "+data.expected.infantLabelTxt+"s"

        }
        else if(data.input.multiroom==true) {
            //travellers
            resultData.hotel.amendBooking.actualTravellersTxt = getTravellersInOccupanceTab(1)
            resultData.hotel.amendBooking.expTravellersTxt = data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+""+data.expected.travellerlastName+", "+" "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_SecondRoom.getAt(0).toString() + "yrs)"+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_SecondRoom.getAt(1).toString() + "yrs)"

        }
        else{
            //travellers
            resultData.hotel.amendBooking.actualTravellersTxt=getTravellersInOccupanceTab(2)
            resultData.hotel.amendBooking.expTravellersTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName

        }

        //amount rate change and currency
        try{
            resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt=getPriceDiffInOccupancyTab()
            println("Actual Price Diff Text:$resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt")
            resultData.hotel.amendBooking.expectedPriceDiff=getPriceDiffValue()+" GBP"

        }catch(Exception e){
            resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt="Unable To Read From UI"
            resultData.hotel.amendBooking.expectedPriceDiff="Unable To Read From UI"

        }

        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText=getTotalRoomAmntAndCurrencyInOccupancyTab(2)
        try{
            resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt=getPositiveChangeAmountValue()+" GBP"
        }catch(Exception e){
            resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt="Unable To Read Total Room Amount And Currency Text From UI"
        }


        //Commission
        resultData.hotel.amendBooking.actualCommissionText=getCommissionInOccupancyTab(2)

        //confirmation status(inventory)
        resultData.hotel.amendBooking.actualConfirmationStatus=getInventoryStatusInOccupancyTab()

        //Ok button should be displayed
        resultData.hotel.amendBooking.actualOkBtnDispStatus=getOkButtonDisplayStatus()

        //Ok button should be enabled
        resultData.hotel.amendBooking.actualOkBtnEnableStatus=getOkButtonEnableOrDisableStatus()
    }

    protected def "changeToInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt
        int chkOutDays
        def checkOutDt
        int nights
        if(data.input.multiroom==true) {

            checkInDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays, "ddMMMyy").toUpperCase()
            println("checkInDate:$checkInDt")
            chkOutDays = data.expected.chkInDays + 1.toInteger()
            println("chk Out Days: $chkOutDays")
            checkOutDt = dateUtil.addDaysChangeDatetformat(chkOutDays, "ddMMMyy").toUpperCase()
            println("checkOutDate:$checkInDt")
            nights = chkOutDays - data.expected.chkInDays
            println("Number Of Nights:$nights")
        }else if(data.input.infant==true) {
            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+22, "ddMMMyy").toUpperCase()

            nights=(data.input.checkOutDays.toString().toInteger()+22)-(data.input.checkInDays.toString().toInteger()+20)

        }
        else{
            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+2, "ddMMMyy").toUpperCase()

            nights=data.input.checkOutDays.toString().toInteger()+1-data.input.checkInDays.toString().toInteger()

        }


        at ItineraryTravllerDetailsPage

        //change to: text should show
        resultData.hotel.amendBooking.actualChangeToTxt=getOccupancyInsideTxtInDatesTab(2)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInDatesTab=getItemNameInDatesTab(2)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(2)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.actualmealBasisText=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actualfreeCancellationTxt=getFreeCancelTextChangeToInDatesTab()

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInPlusNumOfNight=getCheckInTxtInDatesTab(2)
        //Check in: 29AUG16 Check out: 30AUG16, 1 night
        if (nights >1)
            dur= "Check in: "+checkInDt+" "+"Check out: "+checkOutDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+" "+"Check out: "+checkOutDt+", "+nights+" night"

        resultData.hotel.amendBooking.expChkInChkOutPlusNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXChangeToInDatesTab()
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actualPaxTxt=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        resultData.hotel.amendBooking.actualTravellersTxt = getTravellersInChangeToDatesTab()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.expTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)" + " +1 infant"
        }else if(data.input.infant==true){

            resultData.hotel.amendBooking.expTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + "" + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+" +1 infant"

        }
        else{
            resultData.hotel.amendBooking.expTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + "" + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
        }
        //amount rate change and currency
        resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt=getPriceDiffInDatesTab()
        println("Actual Price Diff Text:$resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt")
        resultData.hotel.amendBooking.expectedPriceDiff=getPriceDiffValueInDatesTab()+" GBP"


        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText=getTotalRoomAmntAndCurrencyInDatesTab(2)
        resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt=getPositiveChangeAmountValueInDatesTab()+" GBP"
        resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText=resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText

        //Commission
        resultData.hotel.amendBooking.actualCommissionText=getCommissionInDatesTab(2)

        //confirmation status(inventory)
        resultData.hotel.amendBooking.actualConfirmationStatus=getInventoryStatusInDatesTab()

        //Ok button should be displayed
        resultData.hotel.amendBooking.actualOkBtnDispStatus=getOkButtonDisplayStatusInDatesTab()

        //Ok button should be enabled
        resultData.hotel.amendBooking.actualOkBtnEnableStatus=getOkButtonEnableOrDisableStatusInDatesTab()

    }

    protected def "changeToInDateTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt
        int chkOutDays
        def checkOutDt
        int nights

            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkOutDays.toInteger()+20, "ddMMMyy").toUpperCase()

            nights=(data.input.checkOutDays.toString().toInteger()+20)-(data.input.checkInDays.toString().toInteger()+20)



        at ItineraryTravllerDetailsPage

        //change to: text should show
        resultData.hotel.amendBooking.actualChangeToTxt=getOccupancyInsideTxtInDatesTab(2)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInDatesTab=getItemNameInDatesTab(2)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(2)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.actualmealBasisText=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actualfreeCancellationTxt=getFreeCancelTextChangeToInDatesTab()

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInPlusNumOfNight=getCheckInTxtInDatesTab(2)
        //Check in: 29AUG16 Check out: 30AUG16, 1 night
        if (nights >1)
            dur= "Check in: "+checkInDt+" "+"Check out: "+checkOutDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+" "+"Check out: "+checkOutDt+", "+nights+" night"

        resultData.hotel.amendBooking.expChkInChkOutPlusNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXChangeToInDatesTab()
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actualPaxTxt=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        resultData.hotel.amendBooking.actualTravellersTxt = getTravellersInChangeToDatesTab()

            resultData.hotel.amendBooking.expTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName

        //amount rate change and currency
        resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt=getPriceDiffInDatesTab()
        println("Actual Price Diff Text:$resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt")
        resultData.hotel.amendBooking.expectedPriceDiff=getPriceDiffValueInDatesTab()+" GBP"


        //Room Amount and Currency
        resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText=getTotalRoomAmntAndCurrencyInDatesTab(2)
        resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt=getPositiveChangeAmountValueInDatesTab()+" GBP"

        //Commission
        resultData.hotel.amendBooking.actualCommissionText=getCommissionInDatesTab(2)

        //confirmation status(inventory)
        resultData.hotel.amendBooking.actualConfirmationStatus=getInventoryStatusInDatesTab()

        //Ok button should be displayed
        resultData.hotel.amendBooking.actualOkBtnDispStatus=getOkButtonDisplayStatusInDatesTab()

        //Ok button should be enabled
        resultData.hotel.amendBooking.actualOkBtnEnableStatus=getOkButtonEnableOrDisableStatusInDatesTab()

    }


    protected def "changeToDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDate
        def checkOutDt
        int totalNights
        int checkOutDays
        if(data.input.infant==true) {
            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+2, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+2.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())
        }else{
            checkInDate=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+7, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.input.checkOutDays.toString().toInteger()+6.toInteger()
            totalNights=checkOutDays-(data.input.checkInDays.toString().toInteger()+5)
            println("Number Of Nights in Dates Tab:$totalNights")
        }

        at ItineraryTravllerDetailsPage

        //change to: text should show
        resultData.hotel.amendBooking.actChangeToTxt=getOccupancyInsideTxtInDatesTab(2)

        //Item Name
        resultData.hotel.amendBooking.actItemNameInDatesTab=getItemNameInDatesTab(2)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(2)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        resultData.hotel.amendBooking.actNumAndtypeOfRoomTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.actmealBasisText=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actfreeCancelTxt=getFreeCancelTextChangeToInDatesTab()
        String canclDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$canclDate"
        resultData.hotel.amendBooking.expFreeCancelText=data.expected.freecancltxt+" "+canclDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actChkInAndNumOfNight=getCheckInTxtInDatesTab(2)
        //Check in: 29AUG16 Check out: 30AUG16, 1 night
        if (totalNights >1)
            dur= "Check in: "+checkInDate+" "+"Check out: "+checkOutDt+", "+totalNights+" nights"
        else dur = "Check in: "+checkInDate+" "+"Check out: "+checkOutDt+", "+totalNights+" night"

        resultData.hotel.amendBooking.expChkInChkOutAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXChangeToInDatesTab()
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actPaxTxt=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        resultData.hotel.amendBooking.actTravellersTxt = getTravellersInChangeToDatesTab()
        if(data.input.infant==true){
            resultData.hotel.amendBooking.expTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)" + " +1 infant"
        }else{
            resultData.hotel.amendBooking.expTravellersTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + "" + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
        }

        //amount rate change and currency
        resultData.hotel.amendBooking.actpriceDiffAndCurrnTxt=getPriceDiffInDatesTab()
        println("Actual Price Diff Text:$resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt")
        resultData.hotel.amendBooking.expPriceDiff=getPriceDiffValueInDatesTab()+" GBP"+" (price difference)"


        //Room Amount and Currency
        resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText=getTotalRoomAmntAndCurrencyInDatesTab(2)
        resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt=getPositiveChangeAmountValueInDatesTab()+" GBP"

        //Commission
        resultData.hotel.amendBooking.actCommissionText=getCommissionInDatesTab(2)

        //confirmation status(inventory)
        resultData.hotel.amendBooking.actConfirmationStatus=getInventoryStatusInDatesTab()

        //Ok button should be displayed
        resultData.hotel.amendBooking.actOkBtnDispStatus=getOkButtonDisplayStatusInDatesTab()

        //Ok button should be enabled
        resultData.hotel.amendBooking.actOkBtnEnableStatus=getOkButtonEnableOrDisableStatusInDatesTab()

    }

    protected def "chngToDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDate
        def checkOutDt
        int totalNights
        int checkOutDays

            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+1, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+1.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())


        at ItineraryTravllerDetailsPage

        //change to: text should show
        resultData.hotel.amendBooking.actChangeToTxt=getOccupancyInsideTxtInDatesTab(2)

        //Item Name
        resultData.hotel.amendBooking.actItemNameInDatesTab=getItemNameInDatesTab(2)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInDatesTab(2)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        resultData.hotel.amendBooking.actNumAndtypeOfRoomTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.actmealBasisText=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.actfreeCancelTxt=getFreeCancelTextChangeToInDatesTab()
        String canclDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+5, "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$canclDate"
        resultData.hotel.amendBooking.expFreeCancelText=data.expected.freecancltxt+" "+canclDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.actChkInAndNumOfNight=getCheckInTxtInDatesTab(2)
        //Check in: 29AUG16 Check out: 30AUG16, 1 night
        if (totalNights >1)
            dur= "Check in: "+checkInDate+" "+"Check out: "+checkOutDt+", "+totalNights+" nights"
        else dur = "Check in: "+checkInDate+" "+"Check out: "+checkOutDt+", "+totalNights+" night"

        resultData.hotel.amendBooking.expChkInChkOutAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXChangeToInDatesTab()
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actPaxTxt=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        resultData.hotel.amendBooking.actTravellersTxt = getTravellersInChangeToDatesTab()

        resultData.hotel.amendBooking.expTravllersTxt = data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName


        //amount rate change and currency
        resultData.hotel.amendBooking.actpriceDiffAndCurrnTxt=getPriceDiffInDatesTab()
        println("Actual Price Diff Text:$resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt")
        resultData.hotel.amendBooking.expPriceDiff=getPriceDiffValueInDatesTab()+" GBP"+" (price difference)"


        //Room Amount and Currency
        resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText=getTotalRoomAmntAndCurrencyInDatesTab(2)
        resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt=getPositiveChangeAmountValueInDatesTab()+" GBP"

        //Commission
        resultData.hotel.amendBooking.actCommissionText=getCommissionInDatesTab(2)

        //confirmation status(inventory)
        resultData.hotel.amendBooking.actConfirmationStatus=getInventoryStatusInDatesTab()

        //Ok button should be displayed
        resultData.hotel.amendBooking.actOkBtnDispStatus=getOkButtonDisplayStatusInDatesTab()

        //Ok button should be enabled
        resultData.hotel.amendBooking.actOkBtnEnableStatus=getOkButtonEnableOrDisableStatusInDatesTab()

    }


    protected def "selectToAssignAfterFirstAmend"(HotelSearchData data, HotelTransferTestResultData resultData,int adultStartindx, int adultendindx, int childstrtindx, int childendindx, def children){

        at ItineraryTravllerDetailsPage

        //Please select occupants names:
        resultData.hotel.amendBooking.actualPleaseSelectTxt=getPleaseSelectTxtInOccupancyTab()

        if(data.input.multiroom==true) {

            //tick box should present for each traveller listed in traveller details section
            travellersListCheckBoxExistenceCheck(adultStartindx,childendindx,data)

            childendindx=childendindx-1
            //Get travellers checked status
            travellersListCheckBoxChkdOrUnchkdStatus(adultStartindx,childendindx,data)



        }else {

            //tick box should present for each traveller listed in traveller details section
            travellersListCheckBoxExistenceCheck(1, childendindx, data)

            //Get 1st traveller checked status
            resultData.hotel.amendBooking.actualFirstTrvlrchkBoxStatus = getTravellerCheckedStatus(1)

            //Get 2nd traveller checked status
            resultData.hotel.amendBooking.actualSecondTrvlrchkBoxStatus = getTravellerCheckedStatus(2)

            //Get 3rd traveller checked status
            resultData.hotel.amendBooking.actualThirdTrvlchkBoxStatus = getTravellerCheckedStatus(3)
        }
        if(data.input.children.size()>0 && (data.input.child==true)){
            //Get 4th traveller checked status
            resultData.hotel.amendBooking.actualFourthTrvlchkBoxStatus=getTravellerCheckedStatus(4)
        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Get 4th traveller checked status
            resultData.hotel.amendBooking.actualFourthTrvlchkBoxStatus=getTravellerCheckedStatus(4)
        }

        if(!(data.input.multiroom==true)) {
            //lead traveller
            resultData.hotel.amendBooking.actualLeadTravlr = getTravellerNameTxt(1)
            resultData.hotel.amendBooking.expectedLeadTravlrTxt = data.expected.firstName + " " + data.expected.lastName + " " + "(lead name)"
        }
        //2nd to 5th traveller
        verifyAdultTravellers(adultStartindx,adultendindx,data)
        //6th to 9th traveller
        verifyChildTravellers(childstrtindx,childendindx,data,children)

        //infant text should show - if it is available for the room
        resultData.hotel.amendBooking.actInfntLabTxt=getInfantLabelTxt()

        //infant value displaying = 2
        if(data.input.children.size()>0 && (data.input.infant==true)){
            resultData.hotel.amendBooking.actualSelInfantVal=getInfantDropDownSelectedValueInOccupancyTab()
        }
        //dropdown list to select number of infant should show
        resultData.hotel.amendBooking.actualInfantDropdownListDispStatus=getInfantSelectBoxDisplayStatus()

        //list should show 0 - 1 or 0-2
        resultData.hotel.amendBooking.actualListInfantValues=getInfantDropDownValuesInOccupancyTab()

        //Confirm Amendment Button display status
        resultData.hotel.amendBooking.actualConfirmAmendmentButtonStatus=getConfirmAmendButtonDisplayStatus()

        //Confirm Amendment Button Disabled status
        resultData.hotel.amendBooking.actualConfirmAmendmentDisabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()

    }

    protected def "addinganotheradditionaladultpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //check 4th traveller
        clickTravellersCheckBox(4)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)

    }
    protected def "changeToAfterSecondAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //change to: text should show
        resultData.hotel.amendBooking.actualOccupancyTxt=getOccupancyInsideTxtInNonAmendablePopup(2)

        //Item Name
        resultData.hotel.amendBooking.actualItemNameInOccupTab=getItemNameInOccupanceTab(2)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(2)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt=tempItinryCanclDescList.getAt(0).trim()
        resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt

        //meal basis
        resultData.hotel.amendBooking.actualmealBasisText=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        //resultData.hotel.amendBooking.actualfreeCancellationTxt=getFreeCancelTxtInOccupanceTab(2)
        resultData.hotel.amendBooking.actualfreeCancellationTxt=getFreeCancelTxtInOccupanceTab(1)

        //check in, number of night
        resultData.hotel.amendBooking.actualCheckInPlusNumOfNight=getCheckInTxtInOccupanceTab(2)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.expCheckInPlusNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(2)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.actPaxText=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        resultData.hotel.amendBooking.actTravlrTxt=getTravellersInOccupanceTab(2)
        resultData.hotel.amendBooking.expTravlrTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName

        //amount rate change and currency
        resultData.hotel.amendBooking.actpriceDiffAndCurrnTxt=getPriceDiffInOccupancyTab()
        println("Actual Price Diff Text:$resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt")
        resultData.hotel.amendBooking.expPriceDiff=getPriceDiffValue()+" GBP"+" (price difference)"

        //Room Amount and Currency
        resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText=getTotalRoomAmntAndCurrencyInOccupancyTab(2)
        try{
            resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt=getPositiveChangeAmountValue()+" GBP"
        }catch(Exception e){
            resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt="Unable To Read From UI Total Room Amount And Currenct Text"
        }


        //Commission
        resultData.hotel.amendBooking.actualCommissionText=getCommissionInOccupancyTab(2)

        //confirmation status(inventory)
        resultData.hotel.amendBooking.actualConfirmationStatus=getInventoryStatusInOccupancyTab()

        //Ok button should be displayed
        resultData.hotel.amendBooking.actualOkBtnDispStatus=getOkButtonDisplayStatus()

        //Ok button should be enabled
        resultData.hotel.amendBooking.actualOkBtnEnableStatus=getOkButtonEnableOrDisableStatus()
    }
    protected def "selectToAssignAfterSecondAmend"(HotelSearchData data, HotelTransferTestResultData resultData,int adultStartindx, int adultendindx, int childstrtindx, int childendindx,def children){

        at ItineraryTravllerDetailsPage

        //Please select occupants names:
        resultData.hotel.amendBooking.actualPleaseSelectTxt=getPleaseSelectTxtInOccupancyTab()

        //tick box should present for each traveller listed in traveller details section
        travellersListCheckBoxExistenceCheck(1,childendindx,data)
        if(data.input.multiroom==true){
            //Get 1st traveller checked status
            resultData.hotel.amendBooking.actFirstTrvlrchkBoxStatus=getTravellerCheckedStatus(1)

            //Get 2nd traveller checked status
            resultData.hotel.amendBooking.actSecondTrvlrchkBoxStatus=getTravellerCheckedStatus(2)

            //Get 3rd traveller checked status
            resultData.hotel.amendBooking.actThirdTrvlchkBoxStatus=getTravellerCheckedStatus(3)

        }else{
            //Get 1st traveller checked status
            resultData.hotel.amendBooking.actFirstTrvlrchkBoxStatus=getTravellerCheckedStatus(1)

            //Get 2nd traveller checked status
            resultData.hotel.amendBooking.actSecondTrvlrchkBoxStatus=getTravellerCheckedStatus(2)

            //Get 3rd traveller checked status
            resultData.hotel.amendBooking.actThirdTrvlchkBoxStatus=getTravellerCheckedStatus(3)

            //Get 4th traveller checked status
            resultData.hotel.amendBooking.actFourthTrvlchkBoxStatus=getTravellerCheckedStatus(4)
        }



        //lead traveller
        resultData.hotel.amendBooking.actualLeadTravlr=getTravellerNameTxt(1)
        resultData.hotel.amendBooking.expectedLeadTravlrTxt=data.expected.firstName+" "+data.expected.lastName+" "+"(lead name)"

        //2nd to 5th traveller
        verifyAdultTravellers(adultStartindx,adultendindx,data)
        //6th to 9th traveller
        verifyChildTravellers(childstrtindx,childendindx,data,children)

        //infant text should show - if it is available for the room
        resultData.hotel.amendBooking.actualInfantLabTxt=getInfantLabelTxt()
        //infant value displaying = 2
        if(data.input.children.size()>0 && (data.input.infant==true)){
            resultData.hotel.amendBooking.actSelectedInfantValue=getInfantDropDownSelectedValueInOccupancyTab()
        }

        //dropdown list to select number of infant should show
        resultData.hotel.amendBooking.actualInfantDropdownListDispStatus=getInfantSelectBoxDisplayStatus()

        //list should show 0-1
        resultData.hotel.amendBooking.actListInfantValues=getInfantDropDownValuesInOccupancyTab()

        //Confirm Amendment Button display status
        resultData.hotel.amendBooking.actualConfirmAmendmentButtonStatus=getConfirmAmendButtonDisplayStatus()

        //Confirm Amendment Button Disabled status
        resultData.hotel.amendBooking.actualConfirmAmendmentDisabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()

    }

    protected def "acceptingChange"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //click ok button
        clickAmendOKButton()
        sleep(3000)

        //user taken to t & c page
        resultData.hotel.amendBooking.actualTAndCDispStatus=getAboutToBookScreendisplayStatus()

    }

    protected def "acceptingChanges"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //click ok button
        clickAmendOKButton()
        sleep(3000)

        //user taken to t & c page
        resultData.hotel.amendBooking.actTAndCDispStatus=getAboutToBookScreendisplayStatus()

    }


    protected def "TAndCpagereconfirmation"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()

        at ItineraryTravllerDetailsPage

        //capture title text
        resultData.hotel.amendBooking.occupTab.actualtitleTxt=getCancellationHeader()

        //Capture Close Icon display status
        resultData.hotel.amendBooking.occupTab.actualCloseIconDispStatus=getCloseIconDisplayStatus()

        //Capture hotel name in About to Amend Screen
        resultData.hotel.amendBooking.occupTab.actualHotelNameInAbtToAmndScrn=getHotelNameInAboutToBookScrn()

        //number of room/name of room (descripton)
        //String tmphotelTxt=getTitleSectDescTxt(0)
        String tmphotelTxt=getItemDescPaxAndCityTxtInAboutToBookScreen()
        List<String> htlDesc=tmphotelTxt.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText=htlDesc.getAt(0).trim()
        if(data.input.multiroom==true) {
            //resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt
            resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt=data.expected.secndRoomDescTxt
        }else{
            //resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt
            resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt=data.expected.roomDescTxt
        }


        //meal basis
        resultData.hotel.amendBooking.occupTab.actualmealBasisTxt=htlDesc.getAt(1).trim()

        //number of pax
        //String actualPAXAndTravellers=getTitleSectDescTxt(1)
        String actualPAXAndTravellers=getTitleSectDescTxt(3)
        //List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")

        //resultData.hotel.amendBooking.occupTab.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()
        resultData.hotel.amendBooking.occupTab.actualPaxText=actualPAXAndTravellers.trim().replaceAll("\n","")

        //travellers

        if(data.input.multiroom==true) {
            //travellers
            //resultData.hotel.amendBooking.occupTab.actualTravellersText=getTravellersInAboutToAmend(1)
            //resultData.hotel.amendBooking.occupTab.expTravellersTxt = data.expected.title_txt+" "+data.expected.travellerfirstName+"4 "+data.expected.travellerlastName+"4"+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+"5 "+data.expected.travellerlastName+"5"+", "+data.expected.thirdTraveller_title_txt+" "+data.expected.childFirstName+"6 "+data.expected.childLastName+"6"+" " + "(" +data.input.children_SecondRoom.getAt(0).toString() + "yrs)"

        }else{
            //travellers
            //resultData.hotel.amendBooking.occupTab.actualTravellersText=getTravellersInAboutToAmend(1)
        }


        //check in date, number of night
        resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight=getTitleSectDescTxt(0).replaceAll("\n","")
        def chkInDate=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd")
        String expectedCheckInDateTextAndDate="Check in"+chkInDate
        //resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight=getTitleSectDescTxt(2)
        /*if (nights >1)
            dur= checkInDt+","+nights+" nights"
        else dur = checkInDt+","+nights+" night"
        resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNight=dur*/
        resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNight=expectedCheckInDateTextAndDate

        //Number of Nights
        resultData.hotel.amendBooking.occupTab.actualNumOfNight=getTitleSectDescTxt(2).replaceAll("\n","")
        if (nights >1)
            dur= "Nights"+nights
        else dur = "Nights"+nights
        resultData.hotel.amendBooking.occupTab.expectedNumOfNight=dur
        //status
        resultData.hotel.amendBooking.occupTab.actualInvStatusInAbtToAmendScrn=getPriceInAbouttoBookScrn(0)

        //amount rate change and currency
        resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //total room amount & currency code
        resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(2)
        //String expectedRoomAmntAndcurncyAbtToAmendScrn=actualTotalRoomAmntAndCurrncyTxt.replaceAll(" ", "")
        //total room amount & currency code


        //commission & % value
        //resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(3)
        //resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(2)
        resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //special condition - header text
        resultData.hotel.amendBooking.occupTab.actualSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToAmendScrn(0)
        resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDt

        //Please note text
        resultData.hotel.amendBooking.occupTab.actualPlzNoteTxt=getDescriptionTextInAboutToAmendScrn(0)

        //Cancellation Charge text
        //resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(1)
        resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)
        //Cancellation charge descriptive text
        List<String> actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()
        resultData.hotel.amendBooking.occupTab.actualCancellationChrgTxt=actualCancellationChrgTxt

        //If Amendments text
        resultData.hotel.amendBooking.occupTab.actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(1)

        //All dates text
        resultData.hotel.amendBooking.occupTab.actualDatesTxt=getDescriptionTextInAboutToAmendScrn(2)

        //Total
        //resultData.hotel.amendBooking.occupTab.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)
        resultData.hotel.amendBooking.occupTab.actualTotalTextInAboutToBook=getTotalAndCommissionTextInAboutTo(0)
        //Total Amount and currency
        //resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency=getTotalInConfirmbooking(1)
        resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)


        //Your Commission text
        //resultData.hotel.amendBooking.occupTab.actualCommissionTextInAboutToAmend=getTotalAndCommissionText(1)
        //resultData.hotel.amendBooking.occupTab.actualCommissionTextInAboutToAmend=getTotalAndCommissionTextInAboutTo(2)
        resultData.hotel.amendBooking.occupTab.actualCommissionTextInAboutToAmend=getTotalAndCommissionTextInAboutTo(1)


        //Your commission amount and currency
        //resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(1)
        //resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getYourCommissionAmountAndCurrencyTextInAboutToBookScreen(1)
        resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getYourCommissionAmountAndCurrencyTextInAboutToBookScreen(0)
        float compercentage = Float.parseFloat(data.expected.commissionPercent)
        String priceText=resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency.replace(" GBP", "").trim()
        float comAmount=Float.parseFloat(priceText)
        String comValue=getCommissionPercentageValue(comAmount,compercentage)
        String expectedComValue=comValue+" "+clientData.currency
        resultData.hotel.amendBooking.occupTab.expCommissionValueInAboutToAmend=expectedComValue

        //< Confirm Amendment > button display status
        resultData.hotel.amendBooking.occupTab.actualconfirmButtonDispStatus=getConfirmAmendButtonDispStatusInAboutToAmend()

        //confirm Amendment enable status
        resultData.hotel.amendBooking.occupTab.actualconfirmAmendEnableStatus=getConfirmAmendButtonEnableOrDisableStatusInAboutToAmend()

        //T&C agreement text
        //String ByClickingFooterTxt=getByClickingFooterTxt(3)
        //String ByClickingFooterTxt=getByClickingFooterTxt(5)
        String ByClickingFooterTxt=getByClickingFooterTxt()
        resultData.hotel.amendBooking.occupTab.actualTermsAndCondtTxt=ByClickingFooterTxt.replace("\n", "")

        //click < Confirm Amendment > button
        //clickonConfirmAmendment()
        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()
        scrollToTopOfThePage()
        //Confirm booking page display status
        resultData.hotel.amendBooking.occupTab.actualconfirmbookStatus=getBookingConfirmationScreenDisplayStatus()


    }

    protected def "TAndCpagereconfirmationFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData, def childrenroom){

        def checkInDt
        def checkOutDateTxt
        def checkInDateText
        int chkOutDays
        int nights
        if(data.input.multiroom==true) {
            checkInDt=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays, "ddMMMyy").toUpperCase()
            println("checkInDate in Dates Tab Fun:$checkInDt")
            chkOutDays=data.expected.chkInDays+1.toInteger()
            nights=chkOutDays-data.expected.chkInDays
            println("Number Of Nights in Dates Tab Fun:$nights")
        }else if(data.input.infant==true) {
            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            nights=(data.input.checkOutDays.toString().toInteger()+22)-(data.input.checkInDays.toString().toInteger()+20)
        }
        else{
            checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkInDateText=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd")
            println("checkInDate in Dates Tab:$checkInDt")
            chkOutDays=data.input.checkOutDays.toString().toInteger()+1.toInteger()
            nights=chkOutDays-data.input.checkInDays.toString().toInteger()
            println("Number Of Nights in Dates Tab:$nights")
            checkOutDateTxt=dateUtil.addDaysChangeDatetformat(chkOutDays, "yyyy-MM-dd")
        }

        at ItineraryTravllerDetailsPage

        //capture title text
        resultData.hotel.amendBooking.occupTab.actualtitleTxt=getCancellationHeader()

        //Capture Close Icon display status
        resultData.hotel.amendBooking.occupTab.actualCloseIconDispStatus=getCloseIconDisplayStatus()

        //Capture hotel name in About to Amend Screen
        resultData.hotel.amendBooking.occupTab.actualHotelNameInAbtToAmndScrn=getHotelNameInAboutToBookScrn()

        //number of room/name of room (descripton)
        //String tmphotelTxt=getTitleSectDescTxt(0)
        String tmphotelTxt=getItemDescPaxAndCityTxtInAboutToBookScreen()
        List<String> htlDesc=tmphotelTxt.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText=htlDesc.getAt(0).trim()
        //resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt
        resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt=data.expected.firstRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.occupTab.actualmealBasisTxt=htlDesc.getAt(1).trim()

        //number of pax
        //String actualPAXAndTravellers=getTitleSectDescTxt(1)
        //List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        //resultData.hotel.amendBooking.occupTab.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()


        //travellers
       //resultData.hotel.amendBooking.occupTab.actualTravellersText=getTravellersInAboutToAmend(1)
       //resultData.hotel.amendBooking.occupTab.expTravellersTxt = data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+"2 "+data.expected.travellerlastName+"2"+", "+data.expected.thirdTraveller_title_txt+" "+data.expected.childFirstName+"3 "+data.expected.childLastName+"3"+" " + "(" + childrenroom.getAt(0).toString() + "yrs)"+" +1 infant"


        //check in date, number of night
        resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight=getTitleSectDescTxt(0).replaceAll("\n","")
        /*if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"*/
        resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNight="Check in"+checkInDateText

        //check out Date
        resultData.hotel.amendBooking.occupTab.actualCheckOutDateAndText=getTitleSectDescTxt(1).replaceAll("\n","")

        resultData.hotel.amendBooking.occupTab.expectedCheckOutDateAndText="Check out"+checkOutDateTxt

        //No. of Nights
        resultData.hotel.amendBooking.occupTab.actualNumOfNight=getTitleSectDescTxt(2).replaceAll("\n","")
        resultData.hotel.amendBooking.occupTab.expectedNumOfNight="Nights"+nights

        //No. of Pax
        String actualPAXAndTravellers=getTitleSectDescTxt(3)
        resultData.hotel.amendBooking.occupTab.actualPaxText=actualPAXAndTravellers.replaceAll("\n","")

        //status
        //resultData.hotel.amendBooking.occupTab.actualInvStatusInAbtToAmendScrn=getPriceInAbouttoBookScrn(0)

        //amount rate change and currency
        //resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(1)
        //resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(0)

        //total room amount & currency code
        //resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(2)
        resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(0)
        //String expectedRoomAmntAndcurncyAbtToAmendScrn=actualTotalRoomAmntAndCurrncyTxt.replaceAll(" ", "")
        //total room amount & currency code


        //commission & % value
        //resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(3)
        resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //special condition - header text
        resultData.hotel.amendBooking.occupTab.actualSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToAmendScrn(0)
        resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDt

        //Please note text
        resultData.hotel.amendBooking.occupTab.actualPlzNoteTxt=getDescriptionTextInAboutToAmendScrn(0)

        //Cancellation Charge text
        //resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(2)
        resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)

        //Cancellation charge descriptive text
        List<String> actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()
        resultData.hotel.amendBooking.occupTab.actualCancellationChrgTxt=actualCancellationChrgTxt

        //If Amendments text & All dates text
        if(data.input.multiroom==true || data.input.infant==true) {
            resultData.hotel.amendBooking.occupTab.actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(2)
            //All dates text
            resultData.hotel.amendBooking.occupTab.actualDatesTxt=getDescriptionTextInAboutToAmendScrn(3)
        }else{
            resultData.hotel.amendBooking.occupTab.actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(1)
            resultData.hotel.amendBooking.occupTab.actualDatesTxt=getDescriptionTextInAboutToAmendScrn(2)
        }

        //Total
        resultData.hotel.amendBooking.occupTab.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)

        //Total Amount and currency
        //resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency=getTotalInConfirmbooking(1)
        resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)

        //Your Commission text
        resultData.hotel.amendBooking.occupTab.actualCommissionTextInAboutToAmend=getTotalAndCommissionText(2)

        //Your commission amount and currency
        //resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(1)
        //resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getYourCommissionAmountAndCurrencyTextInAboutToBookScreen(1)
        resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getYourCommissionAmountAndCurrencyTextInAboutToBookScreen(0)
        float compercentage = Float.parseFloat(data.expected.commissionPercent)
        String priceText=resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency.replace(" GBP", "").trim()
        float comAmount=Float.parseFloat(priceText)
        String comValue=getCommissionPercentageValue(comAmount,compercentage)
        String expectedComValue=comValue+" "+clientData.currency
        resultData.hotel.amendBooking.occupTab.expCommissionValueInAboutToAmend=expectedComValue

        //< Confirm Amendment > button display status
        resultData.hotel.amendBooking.occupTab.actualconfirmButtonDispStatus=getConfirmAmendButtonDispStatusInAboutToAmend()

        //confirm Amendment enable status
        resultData.hotel.amendBooking.occupTab.actualconfirmAmendEnableStatus=getConfirmAmendButtonEnableOrDisableStatusInAboutToAmend()

        //T&C agreement text
        //String ByClickingFooterTxt=getByClickingFooterTxt(3)
        //String ByClickingFooterTxt=getByClickingFooterTxt(5)
        String ByClickingFooterTxt=getByClickingFooterTxt()
        resultData.hotel.amendBooking.occupTab.actualTermsAndCondtTxt=ByClickingFooterTxt.replace("\n", "")

        //click < Confirm Amendment > button
        //clickonConfirmAmendment()
        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()
        scrollToTopOfThePage()
        //Confirm booking page display status
        resultData.hotel.amendBooking.occupTab.actualconfirmbookStatus=getBookingConfirmationScreenDisplayStatus()


    }

    protected def "TAndCpgereconfirmationFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData, def childrenroom){

        def checkInDt
        int chkOutDays
        int nights

            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            nights=(data.input.checkOutDays.toString().toInteger()+20)-(data.input.checkInDays.toString().toInteger()+20)


        at ItineraryTravllerDetailsPage

        //capture title text
        resultData.hotel.amendBooking.occupTab.actualtitleTxt=getCancellationHeader()

        //Capture Close Icon display status
        resultData.hotel.amendBooking.occupTab.actualCloseIconDispStatus=getCloseIconDisplayStatus()

        //Capture hotel name in About to Amend Screen
        resultData.hotel.amendBooking.occupTab.actualHotelNameInAbtToAmndScrn=getHotelNameInAboutToBookScrn()

        //number of room/name of room (descripton)
        String tmphotelTxt=getTitleSectDescTxt(0)
        List<String> htlDesc=tmphotelTxt.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText=htlDesc.getAt(0).trim()
        resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.occupTab.actualmealBasisTxt=htlDesc.getAt(1).trim()

        //number of pax
        String actualPAXAndTravellers=getTitleSectDescTxt(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")

        resultData.hotel.amendBooking.occupTab.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()

        //travellers

        resultData.hotel.amendBooking.occupTab.actualTravellersText=getTravellersInAboutToAmend(1)
        resultData.hotel.amendBooking.occupTab.expTravellersTxt = data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName

        //check in date, number of night
        resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight=getTitleSectDescTxt(2)
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNight=dur

        //status
        resultData.hotel.amendBooking.occupTab.actualInvStatusInAbtToAmendScrn=getPriceInAbouttoBookScrn(0)

        //amount rate change and currency
        resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //total room amount & currency code
        resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(2)
        //String expectedRoomAmntAndcurncyAbtToAmendScrn=actualTotalRoomAmntAndCurrncyTxt.replaceAll(" ", "")
        //total room amount & currency code


        //commission & % value
        //resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(3)
        resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(2)

        //special condition - header text
        resultData.hotel.amendBooking.occupTab.actualSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToAmendScrn(0)
        resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDt

        //Please note text
        resultData.hotel.amendBooking.occupTab.actualPlzNoteTxt=getDescriptionTextInAboutToAmendScrn(0)

        //Cancellation Charge text
        //resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(2)
        resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)

        //Cancellation charge descriptive text
        List<String> actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()
        resultData.hotel.amendBooking.occupTab.actualCancellationChrgTxt=actualCancellationChrgTxt

        //If Amendments text & All dates text

            resultData.hotel.amendBooking.occupTab.actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(1)
            //All dates text
            resultData.hotel.amendBooking.occupTab.actualDatesTxt=getDescriptionTextInAboutToAmendScrn(2)


        //Total
        resultData.hotel.amendBooking.occupTab.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)

        //Total Amount and currency
        //resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency=getTotalInConfirmbooking(1)
        resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)


        //Your Commission text
        resultData.hotel.amendBooking.occupTab.actualCommissionTextInAboutToAmend=getTotalAndCommissionText(2)

        //Your commission amount and currency
        //resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(1)
        resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend=getYourCommissionAmountAndCurrencyTextInAboutToBookScreen(1)
        float compercentage = Float.parseFloat(data.expected.commissionPercent)
        String priceText=resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency.replace(" GBP", "").trim()
        float comAmount=Float.parseFloat(priceText)
        String comValue=getCommissionPercentageValue(comAmount,compercentage)
        String expectedComValue=comValue+" "+clientData.currency
        resultData.hotel.amendBooking.occupTab.expCommissionValueInAboutToAmend=expectedComValue

        //< Confirm Amendment > button display status
        resultData.hotel.amendBooking.occupTab.actualconfirmButtonDispStatus=getConfirmAmendButtonDispStatusInAboutToAmend()

        //confirm Amendment enable status
        resultData.hotel.amendBooking.occupTab.actualconfirmAmendEnableStatus=getConfirmAmendButtonEnableOrDisableStatusInAboutToAmend()

        //T&C agreement text
        //String ByClickingFooterTxt=getByClickingFooterTxt(3)
        //String ByClickingFooterTxt=getByClickingFooterTxt(5)
        String ByClickingFooterTxt=getByClickingFooterTxt()
        resultData.hotel.amendBooking.occupTab.actualTermsAndCondtTxt=ByClickingFooterTxt.replace("\n", "")

        //click < Confirm Amendment > button
        //clickonConfirmAmendment()
        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()
        scrollToTopOfThePage()
        //Confirm booking page display status
        resultData.hotel.amendBooking.occupTab.actualconfirmbookStatus=getBookingConfirmationScreenDisplayStatus()


    }


    protected def "TAndCpgreconfrmtnFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData, def childrenroom){

        def checkInDate
        def checkOutDt
        def checkOutDateTxt
        def checkInDateText
        int totalNights
        int checkOutDays
        if(data.input.infant==true) {
            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+2, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+2.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())
        }else {

            checkInDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 5, "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 7, "ddMMMyy").toUpperCase()
            checkInDateText=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+ 5, "yyyy-MM-dd")
            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays = data.input.checkOutDays.toString().toInteger() + 6.toInteger()
            totalNights = checkOutDays - (data.input.checkInDays.toString().toInteger() + 5)
            println("Number Of Nights in Dates Tab:$totalNights")
            checkOutDateTxt=dateUtil.addDaysChangeDatetformat(checkOutDays, "yyyy-MM-dd")
        }
        at ItineraryTravllerDetailsPage

        //capture title text
        resultData.hotel.amendBooking.occupTab.acttitleTxt=getCancellationHeader()

        //Capture Close Icon display status
        resultData.hotel.amendBooking.occupTab.actCloseIconDispStatus=getCloseIconDisplayStatus()

        //Capture hotel name in About to Amend Screen
        resultData.hotel.amendBooking.occupTab.actHotelNameInAbtToAmndScrn=getHotelNameInAboutToBookScrn()

        //number of room/name of room (descripton)
        //String tmphotelTxt=getTitleSectDescTxt(0)
        String tmphotelTxt=getItemDescPaxAndCityTxtInAboutToBookScreen()
        List<String> htlDesc=tmphotelTxt.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actnumAndNameofRoomText=htlDesc.getAt(0).trim()
        //resultData.hotel.amendBooking.occupTab.expRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt
        resultData.hotel.amendBooking.occupTab.expRoomNumAndTypeOfRoomTxt=data.expected.secndRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.occupTab.actmealBasisTxt=htlDesc.getAt(1).trim()

        //number of pax
        //String actPAXAndTravellers=getTitleSectDescTxt(1)
        //List<String> tempPAXAndTravellersList=actPAXAndTravellers.tokenize("-")

        //resultData.hotel.amendBooking.occupTab.actPaxText=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        /*
        resultData.hotel.amendBooking.occupTab.actTravellersText=getTravellersInAboutToAmend(1)
        if(data.input.infant==true){
            resultData.hotel.amendBooking.occupTab.expTravlrsTxt = data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + "2 " + data.expected.travellerlastName + "2" + ", " + data.expected.thirdTraveller_title_txt + " " + data.expected.childFirstName + "3 " + data.expected.childLastName + "3" + " " + "(" + data.input.children.getAt(0).toString() + "yrs)" + " +1 infant"
        }else{
            resultData.hotel.amendBooking.occupTab.expTravlrsTxt = data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+""+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + childrenroom.getAt(0).toString() + "yrs)"

        }
        */
        //check in date, number of night
        /*resultData.hotel.amendBooking.occupTab.actCheckInAndNumOfNights=getTitleSectDescTxt(2)
        if (totalNights >1)
            dur= checkInDate+", "+totalNights+" nights"
        else dur = checkInDate+", "+totalNights+" night"
        resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNights=dur*/

        /********************/

        //check in date,
        resultData.hotel.amendBooking.occupTab.actCheckInAndNumOfNights=getTitleSectDescTxt(0).replaceAll("\n","")
        /*if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"*/
        resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNights="Check in"+checkInDateText

        //check out Date
        resultData.hotel.amendBooking.occupTab.actCheckOutDateAndText=getTitleSectDescTxt(1).replaceAll("\n","")

        resultData.hotel.amendBooking.occupTab.expCheckOutDateAndText="Check out"+checkOutDateTxt

        //No. of Nights
        resultData.hotel.amendBooking.occupTab.actNumOfNight=getTitleSectDescTxt(2).replaceAll("\n","")
        resultData.hotel.amendBooking.occupTab.expNumOfNight="Nights"+nights

        //No. of Pax
        String actualPAXAndTravellers=getTitleSectDescTxt(3)
        resultData.hotel.amendBooking.occupTab.actPaxText=actualPAXAndTravellers.replaceAll("\n","")

        /*************************/

       //status
        //resultData.hotel.amendBooking.occupTab.actInvStatusInAbtToAmendScrn=getPriceInAbouttoBookScrn(0)

        //amount rate change and currency
        //resultData.hotel.amendBooking.occupTab.actAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //total room amount & currency code
        //resultData.hotel.amendBooking.occupTab.actRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(2)
        resultData.hotel.amendBooking.occupTab.actRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(0)
        //String expectedRoomAmntAndcurncyAbtToAmendScrn=actualTotalRoomAmntAndCurrncyTxt.replaceAll(" ", "")
        //total room amount & currency code


        //commission & % value
        //resultData.hotel.amendBooking.occupTab.actcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(3)
        resultData.hotel.amendBooking.occupTab.actcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //special condition - header text
        resultData.hotel.amendBooking.occupTab.actSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToAmendScrn(0)
        resultData.hotel.amendBooking.occupTab.expSpConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDate

        //Please note text
        resultData.hotel.amendBooking.occupTab.actPlzNoteTxt=getDescriptionTextInAboutToAmendScrn(0)

        //Cancellation Charge text
        //resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(2)
        resultData.hotel.amendBooking.occupTab.actCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)

        //Cancellation charge descriptive text
        List<String> actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()
        resultData.hotel.amendBooking.occupTab.actCancellationChrgTxt=actualCancellationChrgTxt
        if(data.input.multiroom==true || data.input.infant==true) {
            resultData.hotel.amendBooking.occupTab.actIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(2)
            //All dates text
            resultData.hotel.amendBooking.occupTab.actDatesTxt=getDescriptionTextInAboutToAmendScrn(3)
        }else {

            resultData.hotel.amendBooking.occupTab.actIfAmendmentsTxt = getDescriptionTextInAboutToAmendScrn(1)
            resultData.hotel.amendBooking.occupTab.actDatesTxt = getDescriptionTextInAboutToAmendScrn(2)
        }

        //Total
        resultData.hotel.amendBooking.occupTab.actTotalTextInAboutToBook=getTotalAndCommissionText(0)

        //Total Amount and currency
        //resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency=getTotalInConfirmbooking(1)
        resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency=getTotalInConfirmbooking(0)


        //Your Commission text
        resultData.hotel.amendBooking.occupTab.actCommissionTextInAboutToAmend=getTotalAndCommissionText(2)

        //Your commission amount and currency
        //resultData.hotel.amendBooking.occupTab.actComsnValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(1)
        resultData.hotel.amendBooking.occupTab.actComsnValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(0)
        float compercentage = Float.parseFloat(data.expected.commissionPercent)
        String priceText=resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency.replace(" GBP", "").trim()
        float comAmount=Float.parseFloat(priceText)
        String comValue=getCommissionPercentageValue(comAmount,compercentage)
        String expectedComValue=comValue+" "+clientData.currency
        resultData.hotel.amendBooking.occupTab.expComsnValueInAboutToAmend=expectedComValue

        //< Confirm Amendment > button display status
        resultData.hotel.amendBooking.occupTab.actconfirmButtonDispStatus=getConfirmAmendButtonDispStatusInAboutToAmend()

        //confirm Amendment enable status
        resultData.hotel.amendBooking.occupTab.actconfirmAmendEnableStatus=getConfirmAmendButtonEnableOrDisableStatusInAboutToAmend()

        //T&C agreement text
        //String ByClickingFooterTxt=getByClickingFooterTxt(3)
        //String ByClickingFooterTxt=getByClickingFooterTxt(5)
        String ByClickingFooterTxt=getByClickingFooterTxt()
        resultData.hotel.amendBooking.occupTab.actTermsAndCondtTxt=ByClickingFooterTxt.replace("\n", "")

        //click < Confirm Amendment > button
        //clickonConfirmAmendment()
        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()
        scrollToTopOfThePage()
        //Confirm booking page display status
        resultData.hotel.amendBooking.occupTab.actconfirmbookStatus=getBookingConfirmationScreenDisplayStatus()


    }

    protected def "TAndCpgreconfrmatnFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData, def childrenroom){

        def checkInDate
        def checkOutDt
        int totalNights
        int checkOutDays

            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+1, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+1.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())

        at ItineraryTravllerDetailsPage

        //capture title text
        resultData.hotel.amendBooking.occupTab.acttitleTxt=getCancellationHeader()

        //Capture Close Icon display status
        resultData.hotel.amendBooking.occupTab.actCloseIconDispStatus=getCloseIconDisplayStatus()

        //Capture hotel name in About to Amend Screen
        resultData.hotel.amendBooking.occupTab.actHotelNameInAbtToAmndScrn=getHotelNameInAboutToBookScrn()

        //number of room/name of room (descripton)
        String tmphotelTxt=getTitleSectDescTxt(0)
        List<String> htlDesc=tmphotelTxt.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actnumAndNameofRoomText=htlDesc.getAt(0).trim()
        resultData.hotel.amendBooking.occupTab.expRoomNumAndTypeOfRoomTxt="1x "+data.expected.secndRoomDescTxt

        //meal basis
        resultData.hotel.amendBooking.occupTab.actmealBasisTxt=htlDesc.getAt(1).trim()

        //number of pax
        String actPAXAndTravellers=getTitleSectDescTxt(1)
        List<String> tempPAXAndTravellersList=actPAXAndTravellers.tokenize("-")

        resultData.hotel.amendBooking.occupTab.actPaxText=tempPAXAndTravellersList.getAt(0).trim()

        //travellers

        resultData.hotel.amendBooking.occupTab.actTravellersText=getTravellersInAboutToAmend(1)
        resultData.hotel.amendBooking.occupTab.expTravlrsTxt = data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName


        //check in date, number of night
        resultData.hotel.amendBooking.occupTab.actCheckInAndNumOfNights=getTitleSectDescTxt(2)
        if (totalNights >1)
            dur= checkInDate+", "+totalNights+" nights"
        else dur = checkInDate+", "+totalNights+" night"
        resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNights=dur

        //status
        resultData.hotel.amendBooking.occupTab.actInvStatusInAbtToAmendScrn=getPriceInAbouttoBookScrn(0)

        //amount rate change and currency
        resultData.hotel.amendBooking.occupTab.actAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //total room amount & currency code
        resultData.hotel.amendBooking.occupTab.actRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(2)
        //String expectedRoomAmntAndcurncyAbtToAmendScrn=actualTotalRoomAmntAndCurrncyTxt.replaceAll(" ", "")
        //total room amount & currency code


        //commission & % value
        //resultData.hotel.amendBooking.occupTab.actcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(3)
        resultData.hotel.amendBooking.occupTab.actcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(2)

        //special condition - header text
        resultData.hotel.amendBooking.occupTab.actSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToAmendScrn(0)
        resultData.hotel.amendBooking.occupTab.expSpConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDate

        //Please note text
        resultData.hotel.amendBooking.occupTab.actPlzNoteTxt=getDescriptionTextInAboutToAmendScrn(0)

        //Cancellation Charge text
        //resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(2)
        resultData.hotel.amendBooking.occupTab.actCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)

        //Cancellation charge descriptive text
        List<String> actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()
        resultData.hotel.amendBooking.occupTab.actCancellationChrgTxt=actualCancellationChrgTxt


            resultData.hotel.amendBooking.occupTab.actIfAmendmentsTxt = getDescriptionTextInAboutToAmendScrn(1)
            resultData.hotel.amendBooking.occupTab.actDatesTxt = getDescriptionTextInAboutToAmendScrn(2)


        //Total
        resultData.hotel.amendBooking.occupTab.actTotalTextInAboutToBook=getTotalAndCommissionText(0)

        //Total Amount and currency
        //resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency=getTotalInConfirmbooking(1)
        resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency=getTotalInConfirmbooking(0)

        //Your Commission text
        resultData.hotel.amendBooking.occupTab.actCommissionTextInAboutToAmend=getTotalAndCommissionText(2)

        //Your commission amount and currency
        resultData.hotel.amendBooking.occupTab.actComsnValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(1)
        float compercentage = Float.parseFloat(data.expected.commissionPercent)
        String priceText=resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency.replace(" GBP", "").trim()
        float comAmount=Float.parseFloat(priceText)
        String comValue=getCommissionPercentageValue(comAmount,compercentage)
        String expectedComValue=comValue+" "+clientData.currency
        resultData.hotel.amendBooking.occupTab.expComsnValueInAboutToAmend=expectedComValue

        //< Confirm Amendment > button display status
        resultData.hotel.amendBooking.occupTab.actconfirmButtonDispStatus=getConfirmAmendButtonDispStatusInAboutToAmend()

        //confirm Amendment enable status
        resultData.hotel.amendBooking.occupTab.actconfirmAmendEnableStatus=getConfirmAmendButtonEnableOrDisableStatusInAboutToAmend()

        //T&C agreement text
        //String ByClickingFooterTxt=getByClickingFooterTxt(3)
        //String ByClickingFooterTxt=getByClickingFooterTxt(5)
        String ByClickingFooterTxt=getByClickingFooterTxt()
        resultData.hotel.amendBooking.occupTab.actTermsAndCondtTxt=ByClickingFooterTxt.replace("\n", "")

        //click < Confirm Amendment > button
        //clickonConfirmAmendment()
        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()
        scrollToTopOfThePage()
        //Confirm booking page display status
        resultData.hotel.amendBooking.occupTab.actconfirmbookStatus=getBookingConfirmationScreenDisplayStatus()


    }

    protected def "bookingConfirming"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()


        at ItineraryTravllerDetailsPage

        //Booking confirmed text
        resultData.hotel.amendBooking.occupTab.actualBookingConfirmedTitleText=getCancellationHeader()

        //Close X function
        resultData.hotel.amendBooking.occupTab.actualCloseBtnDispStatus=overlayCloseButton()

        //A confirmation of your booking has been email to:
        try{
            resultData.hotel.amendBooking.occupTab.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
        }catch(Exception e){
            resultData.hotel.amendBooking.occupTab.actualHeaderSectionText="Unable To Read Header Section From UI"
        }

        String emailId=clientData.usernameOrEmail
        //emailId=emailId.toUpperCase()
        //resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
        resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText=data.expected.headerSectionTxt

        //brand & icon
        resultData.hotel.amendBooking.occupTab.actualBrandAndIconDispStatus=getBrandAndIconDisplayStatus()

        //Booking ID: & value
        resultData.hotel.amendBooking.occupTab.actualbookingID=getBookingIdFromBookingConfirmed(0)
        resultData.hotel.amendBooking.occupTab.expectedBookingID=resultData.hotel.itineraryPage.retrievedbookingID

        //Itinerary ID,  Itinerary name & value
        resultData.hotel.amendBooking.occupTab.actualitinearyIDAndName=getItinearyID(0)

        //Departure date: text
        resultData.hotel.amendBooking.occupTab.actualDepDateTxt=getBookingIdFromBookingConfirmed(1)

        //Departure Date value text
        resultData.hotel.amendBooking.occupTab.actualDepDateValTxt=getBookingDepartDate()
        resultData.hotel.amendBooking.occupTab.expectedDepDateValTxt=checkInDt.toUpperCase()

        //Traveller Details & value
        resultData.hotel.amendBooking.occupTab.actualTravellerDetailsTxt=getBookingIdFromBookingConfirmed(2)

        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Traveller Values
            //String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
            String trvlrValues=data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+ ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(1).toString() + "yrs)"+"+2 infants"
            sleep(3000)
            //println("Debug Travlr Valu:$trvlrValues")

            List<String> tempInfantList=trvlrValues.tokenize("+")
            String tmpTravellers=tempInfantList.getAt(0).trim()
            List<String> tempTravellerList=tmpTravellers.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")
            List expectedTravellerName=[]
            List actualtravellerName = []

            for(int i=0;i<=4;i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
            }

            for(int i=0;i<=3;i++){

                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))

            }
            expectedTravellerName.add("+"+tempInfantList.getAt(1).trim().replaceAll(" ",""))

            resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerName)
            resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellerName)

        }
        else if(data.input.multiroom==true) {
            //Traveller Values
            String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
            //println("Debug Travlr Valu:$trvlrValues")
            List<String> tempTravellerList=trvlrValues.tokenize(",")
            //println("Debug tokenized Travlr Valu:$tempTravellerList")
            List expectedTravellerName=[]
            List actualtravellerName = []

            for(int i=0;i<=2;i++){
                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))
                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
                //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
            }

            resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerName)
            resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellerName)
        }
        else{

            //Traveller Values
            //String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
            String trvlrValues=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
            //println("Debug Travlr Valu:$trvlrValues")
            List<String> tempTravellerList=trvlrValues.tokenize(",")
            //println("Debug tokenized Travlr Valu:$tempTravellerList")
            List expectedTravellerName=[]
            List actualtravellerName = []

            for(int i=0;i<=3;i++){
                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))
                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
                //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
            }

            resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerName)
            resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellerName)
        }
        //item name
        resultData.hotel.amendBooking.occupTab.actualHotelName=getConfirmBookedTransferName()
        resultData.hotel.amendBooking.occupTab.expectedItemName=resultData.hotel.itineraryPage.readItemName

        //item address
        resultData.hotel.amendBooking.occupTab.actualHotelAddressTxt=getTransferDescBookingConfirmed()
        resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt=resultData.hotel.itineraryPage.readItemAddressTxt

        //number of room, room type & value
        //resultData.hotel.amendBooking.occupTab.expectedRoomNumAndroomValueTxt=resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText
        resultData.hotel.amendBooking.occupTab.expectedRoomNumAndroomValueTxt="1x "+resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText
        String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
        List<String> htlDesc=roomAndPax.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actualRoomNumAndTypeTxt=htlDesc.getAt(0).trim()

        //Capture PAX
        resultData.hotel.amendBooking.occupTab.actpaxTxt=htlDesc.getAt(1).trim()
        //4PAX  (+2 infants accommodated in cot)
        if(data.input.children.size()>0 && (data.input.infant==true)){
            resultData.hotel.amendBooking.occupTab.exppaxTxt=resultData.hotel.amendBooking.occupTab.actualPaxText+" (+"+data.expected.infantDropDownListValues.getAt(2)+" infants accommodated in cot)"
        }

        //String expectedpaxTxt=actualPaxText

        //Meal basis & value
        resultData.hotel.amendBooking.occupTab.actualMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

        //Check in date, Number of Nights
        resultData.hotel.amendBooking.occupTab.actualCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed().replaceAll(" ","")
        //resultData.hotel.amendBooking.occupTab.expectedCheckInDate=resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight.replaceAll(" ","")
        if (nights >1)
            dur= checkInDt+","+nights+"nights"
        else dur = checkInDt+","+nights+"night"
        resultData.hotel.amendBooking.occupTab.expectedCheckInDate=dur

        //room rate amount and currency
        resultData.hotel.amendBooking.occupTab.actualRoomRateAmountAndCurrency=getPriceBookingConfirmation()
        resultData.hotel.amendBooking.occupTab.expectedRoomRateAmntAndCurrncy=resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency.replaceAll(" ", "")

        //commission amount and currency
        resultData.hotel.amendBooking.occupTab.actualCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
        resultData.hotel.amendBooking.occupTab.expectedCommissionAmountAndCurncy="Commission"+resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend.replace(" ", "")

        try {
            //< PDF voucher > button
            resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus =false
            //softAssert.assertFalse(true,"Failed To Confirm Booking")
        }
        //click Close X
        coseBookItenary()
        sleep(4000)


    }

    protected def "bookingConfirmingFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData,int travellers,int bookingIdIndx){

        def checkInDt
        int chkOutDays
        int nights
        if(data.input.multiroom==true) {
            checkInDt=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays, "ddMMMyy").toUpperCase()
            //println("checkInDate in Dates Tab Fun:$checkInDt")
            chkOutDays=data.expected.chkInDays+1.toInteger()
            nights=chkOutDays-data.expected.chkInDays
            //println("Number Of Nights in Dates Tab Fun:$nights")
        }else if(data.input.infant==true) {
            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            nights=(data.input.checkOutDays.toString().toInteger()+22)-(data.input.checkInDays.toString().toInteger()+20)
        }
        else{
            checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
            println("checkInDate in Dates Tab:$checkInDt")
            chkOutDays=data.input.checkOutDays.toString().toInteger()+1.toInteger()
            nights=chkOutDays-data.input.checkInDays.toString().toInteger()
            println("Number Of Nights in Dates Tab:$nights")
        }

        at ItineraryTravllerDetailsPage

        //Booking confirmed text
        resultData.hotel.amendBooking.occupTab.actualBookingConfirmedTitleText=getCancellationHeader()

        //Close X function
        resultData.hotel.amendBooking.occupTab.actualCloseBtnDispStatus=overlayCloseButton()

        //A confirmation of your booking has been email to:
        try{
            resultData.hotel.amendBooking.occupTab.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
        }catch(Exception e){
            resultData.hotel.amendBooking.occupTab.actualHeaderSectionText="Unable To Read Header Text From UI"
        }

        String emailId=clientData.usernameOrEmail
        //emailId=emailId.toUpperCase()
        //resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
        resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText=data.expected.headerSectionTxt

        //brand & icon
        resultData.hotel.amendBooking.occupTab.actualBrandAndIconDispStatus=getBrandAndIconDisplayStatus()

        //Booking ID: & value
        resultData.hotel.amendBooking.occupTab.actualbookingID = getBookingIdFromBookingConfirmed(bookingIdIndx)

        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.occupTab.expectedBookingID =resultData.hotel.itineraryPage.retrievedbookingID

        }
        else{
            if(bookingIdIndx==0){
                resultData.hotel.amendBooking.occupTab.expectedBookingID = resultData.hotel.itineraryPage.firstBookingID

            }else if(bookingIdIndx==1){
                resultData.hotel.amendBooking.occupTab.expectedBookingID = resultData.hotel.itineraryPage.scndbookingID

            }
        }
        //Itinerary ID,  Itinerary name & value
        resultData.hotel.amendBooking.occupTab.actualitinearyIDAndName=getItinearyID(0)

        //Departure date: text
        resultData.hotel.amendBooking.occupTab.actualDepDateTxt=getBookingIdFromBookingConfirmed(1)

        //Departure Date value text
        resultData.hotel.amendBooking.occupTab.actualDepDateValTxt=getBookingDepartDate()
        resultData.hotel.amendBooking.occupTab.expectedDepDateValTxt=checkInDt.toUpperCase()

        //Traveller Details & value
        resultData.hotel.amendBooking.occupTab.actualTravellerDetailsTxt=getBookingIdFromBookingConfirmed(2)


            //Traveller Values
            //String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
        //String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
        String trvlrValues=data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
            //println("Debug Travlr Valu:$trvlrValues")
        List expectedTravellerName = []
        List actualtravellerName = []
        if(data.input.multiroom==true) {
            List<String> tempInfantList = trvlrValues.tokenize("+")
            String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = tmpTravellers.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i <= travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
            }

            for (int i = 0; i <= travellers - 1; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))

            }
            expectedTravellerName.add("+" + tempInfantList.getAt(1).trim().replaceAll(" ",""))
        } else if(data.input.infant==true) {
            //trvlrValues = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + "2 " + data.expected.travellerlastName + "2" + ", " + data.expected.thirdTraveller_title_txt + " " + data.expected.childFirstName + "3 " + data.expected.childLastName + "3" + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+"+1 infant"
            trvlrValues=data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+"+1 infant"
            List<String> tempInfantList = trvlrValues.tokenize("+")
            String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = tmpTravellers.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i < travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
            }

            for (int i = 0; i < travellers - 1; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))

            }
            expectedTravellerName.add("+" + tempInfantList.getAt(1).trim().replaceAll(" ",""))


        }
        else{
            //List<String> tempInfantList = trvlrValues.tokenize("+")
            //String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = trvlrValues.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i <= travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
            }

            for (int i = 0; i <= travellers; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))

            }
        }
            resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerName)
            resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellerName)

        //item name
        resultData.hotel.amendBooking.occupTab.actualHotelName=getConfirmBookedTransferName()
        //item address
        resultData.hotel.amendBooking.occupTab.actualHotelAddressTxt=getTransferDescBookingConfirmed()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.occupTab.expectedItemName=resultData.hotel.itineraryPage.readItemName
            resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt=resultData.hotel.itineraryPage.readItemAddressTxt

        }else{
            if(bookingIdIndx==0){
                resultData.hotel.amendBooking.occupTab.expectedItemName=resultData.hotel.itineraryPage.readFirstItemName
                resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt=resultData.hotel.itineraryPage.readFirstItemAddressTxt

            }else if(bookingIdIndx==1){
                resultData.hotel.amendBooking.occupTab.expectedItemName=resultData.hotel.itineraryPage.readSecondItemName
                resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt=resultData.hotel.itineraryPage.readSecondItemAddressTxt

            }
        }


        //number of room, room type & value
        //resultData.hotel.amendBooking.occupTab.expectedRoomNumAndroomValueTxt=resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText
        resultData.hotel.amendBooking.occupTab.expectedRoomNumAndroomValueTxt="1x "+resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText
        String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
        List<String> htlDesc=roomAndPax.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actualRoomNumAndTypeTxt=htlDesc.getAt(0).trim()

        //Capture PAX
        resultData.hotel.amendBooking.occupTab.actpaxTxt=htlDesc.getAt(1).trim()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.occupTab.exppaxTxt = resultData.hotel.amendBooking.occupTab.actualPaxText + " (+" + data.expected.infantDropDownListVal_FirstRoom.getAt(1) + " infant accommodated in cot)"
        }else if(data.input.infant==true) {
            resultData.hotel.amendBooking.occupTab.exppaxTxt = resultData.hotel.amendBooking.occupTab.actualPaxText + " (+" + data.expected.infantDropDownListValues.getAt(1) + " infant accommodated in cot)"
        }
        else{
            //resultData.hotel.amendBooking.occupTab.exppaxTxt = resultData.hotel.amendBooking.occupTab.actualPaxText
            resultData.hotel.amendBooking.occupTab.exppaxTxt = data.expected.modifiedpaxTxt.replaceAll(" ","")
        }

        //String expectedpaxTxt=actualPaxText

        //Meal basis & value
        resultData.hotel.amendBooking.occupTab.actualMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

        //Check in date, Number of Nights
        resultData.hotel.amendBooking.occupTab.actualCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed()
        //resultData.hotel.amendBooking.occupTab.expectedCheckInDate=resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.occupTab.expectedCheckInDate=dur

        //room rate amount and currency
        resultData.hotel.amendBooking.occupTab.actualRoomRateAmountAndCurrency=getPriceBookingConfirmation()
        resultData.hotel.amendBooking.occupTab.expectedRoomRateAmntAndCurrncy=resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency.replaceAll(" ", "")

        //commission amount and currency
        resultData.hotel.amendBooking.occupTab.actualCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
        resultData.hotel.amendBooking.occupTab.expectedCommissionAmountAndCurncy="Commission"+resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend.replace(" ", "")

        try {
            //< PDF voucher > button
            resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus =false
            //softAssert.assertFalse(true,"Failed To Confirm Booking")
        }
        //click Close X
        coseBookItenary()
        sleep(4000)


    }

    protected def "bkngConfirmingFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData,int travellers,int bookingIdIndx){

        def checkInDt
        int chkOutDays
        int nights

            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            nights=(data.input.checkOutDays.toString().toInteger()+20)-(data.input.checkInDays.toString().toInteger()+20)


        at ItineraryTravllerDetailsPage

        //Booking confirmed text
        resultData.hotel.amendBooking.occupTab.actualBookingConfirmedTitleText=getCancellationHeader()

        //Close X function
        resultData.hotel.amendBooking.occupTab.actualCloseBtnDispStatus=overlayCloseButton()

        //A confirmation of your booking has been email to:
        try{
            resultData.hotel.amendBooking.occupTab.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
        }catch(Exception e){
            resultData.hotel.amendBooking.occupTab.actualHeaderSectionText="Unable To Read Header Text From UI"
        }

        String emailId=clientData.usernameOrEmail
        //emailId=emailId.toUpperCase()
        //resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
        resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText=data.expected.headerSectionTxt

        //brand & icon
        resultData.hotel.amendBooking.occupTab.actualBrandAndIconDispStatus=getBrandAndIconDisplayStatus()

        //Booking ID: & value
        resultData.hotel.amendBooking.occupTab.actualbookingID = getBookingIdFromBookingConfirmed(bookingIdIndx)


            if(bookingIdIndx==0){
                resultData.hotel.amendBooking.occupTab.expectedBookingID = resultData.hotel.itineraryPage.firstBookingID

            }else if(bookingIdIndx==1){
                resultData.hotel.amendBooking.occupTab.expectedBookingID = resultData.hotel.itineraryPage.scndbookingID

            }

        //Itinerary ID,  Itinerary name & value
        resultData.hotel.amendBooking.occupTab.actualitinearyIDAndName=getItinearyID(0)

        //Departure date: text
        resultData.hotel.amendBooking.occupTab.actualDepDateTxt=getBookingIdFromBookingConfirmed(1)

        //Departure Date value text
        resultData.hotel.amendBooking.occupTab.actualDepDateValTxt=getBookingDepartDate()
        resultData.hotel.amendBooking.occupTab.expectedDepDateValTxt=checkInDt.toUpperCase()

        //Traveller Details & value
        resultData.hotel.amendBooking.occupTab.actualTravellerDetailsTxt=getBookingIdFromBookingConfirmed(2)


        //Traveller Values
        String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
        //println("Debug Travlr Valu:$trvlrValues")
        List expectedTravellerName = []
        List actualtravellerName = []

            List<String> tempTravellerList = trvlrValues.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i < travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            }

            for (int i = 0; i < travellers; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim())

            }

        resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellerName)

        //item name
        resultData.hotel.amendBooking.occupTab.actualHotelName=getConfirmBookedTransferName()
        //item address
        resultData.hotel.amendBooking.occupTab.actualHotelAddressTxt=getTransferDescBookingConfirmed()

            if(bookingIdIndx==0){
                resultData.hotel.amendBooking.occupTab.expectedItemName=resultData.hotel.itineraryPage.readFirstItemName
                resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt=resultData.hotel.itineraryPage.readFirstItemAddressTxt

            }else if(bookingIdIndx==1){
                resultData.hotel.amendBooking.occupTab.expectedItemName=resultData.hotel.itineraryPage.readSecondItemName
                resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt=resultData.hotel.itineraryPage.readSecondItemAddressTxt

            }



        //number of room, room type & value
        resultData.hotel.amendBooking.occupTab.expectedRoomNumAndroomValueTxt=resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText
        String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
        List<String> htlDesc=roomAndPax.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actualRoomNumAndTypeTxt=htlDesc.getAt(0).trim()

        //Capture PAX
        resultData.hotel.amendBooking.occupTab.actpaxTxt=htlDesc.getAt(1).trim()

            resultData.hotel.amendBooking.occupTab.exppaxTxt = resultData.hotel.amendBooking.occupTab.actualPaxText


        //String expectedpaxTxt=actualPaxText

        //Meal basis & value
        resultData.hotel.amendBooking.occupTab.actualMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

        //Check in date, Number of Nights
        resultData.hotel.amendBooking.occupTab.actualCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed()
        resultData.hotel.amendBooking.occupTab.expectedCheckInDate=resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight

        //room rate amount and currency
        resultData.hotel.amendBooking.occupTab.actualRoomRateAmountAndCurrency=getPriceBookingConfirmation()
        resultData.hotel.amendBooking.occupTab.expectedRoomRateAmntAndCurrncy=resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency.replaceAll(" ", "")

        //commission amount and currency
        try{
            resultData.hotel.amendBooking.occupTab.actualCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
        }catch(Exception e){
            resultData.hotel.amendBooking.occupTab.actualCommissionAmountAndCurrency="Unable To Read From UI"
        }

        resultData.hotel.amendBooking.occupTab.expectedCommissionAmountAndCurncy="Commission"+resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend.replace(" ", "")

        try {
            //< PDF voucher > button
            resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus =false
                   // softAssert.assertFalse(true,"Failed To Confirm Booking")
        }
        //click Close X
        coseBookItenary()
        sleep(4000)


    }

    protected def "bkngConfrmngFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData,int travellers,int bookingIdIndx){

        def checkInDate
        def checkOutDt
        int totalNights
        int checkOutDays
        if(data.input.infant==true) {
            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+2, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+2.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())
        }else {
            checkInDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 5, "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 7, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays = data.input.checkOutDays.toString().toInteger() + 6.toInteger()
            totalNights = checkOutDays - (data.input.checkInDays.toString().toInteger() + 5)
            println("Number Of Nights in Dates Tab:$totalNights")
        }
        at ItineraryTravllerDetailsPage

        //Booking confirmed text
        resultData.hotel.amendBooking.occupTab.actBookingConfirmedTitleText=getCancellationHeader()

        //Close X function
        resultData.hotel.amendBooking.occupTab.actCloseBtnDispStatus=overlayCloseButton()

        //A confirmation of your booking has been email to:
        resultData.hotel.amendBooking.occupTab.actHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
        String emailId=clientData.usernameOrEmail
        //emailId=emailId.toUpperCase()
        //resultData.hotel.amendBooking.occupTab.expHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
        resultData.hotel.amendBooking.occupTab.expHeaderSectionText=data.expected.headerSectionTxt

        //brand & icon
        resultData.hotel.amendBooking.occupTab.actBrandAndIconDispStatus=getBrandAndIconDisplayStatus()

        //Booking ID: & value
        resultData.hotel.amendBooking.occupTab.actBkngId = "Booking ID: " + getBookingIdFromBookingConfirmed(0)


            if(bookingIdIndx==0){
                resultData.hotel.amendBooking.occupTab.expBkngID = "Booking ID: " + resultData.hotel.itineraryPage.firstBookingID

            }else if(bookingIdIndx==1){
                resultData.hotel.amendBooking.occupTab.expBkngID = "Booking ID: " + resultData.hotel.itineraryPage.scndbookingID

            }

        //Itinerary ID,  Itinerary name & value
        resultData.hotel.amendBooking.occupTab.actitinearyIDAndName=getItinearyID(0)

        //Departure date: text
        resultData.hotel.amendBooking.occupTab.actDepDateTxt=getBookingIdFromBookingConfirmed(1)

        //Departure Date value text
        resultData.hotel.amendBooking.occupTab.actDepDateValTxt=getBookingDepartDate()
        resultData.hotel.amendBooking.occupTab.expDepDateValTxt=checkInDate.toUpperCase()

        //Traveller Details & value
        resultData.hotel.amendBooking.occupTab.actTravellerDetailsTxt=getBookingIdFromBookingConfirmed(2)


        //Traveller Values
        //String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
        String trvlrValues=data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
        //println("Debug Travlr Valu:$trvlrValues")
        List expectedTravellerName = []
        List actualtravellerName = []
        if(data.input.multiroom==true) {
            List<String> tempInfantList = trvlrValues.tokenize("+")
            String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = tmpTravellers.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i <= travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            }

            for (int i = 0; i <= travellers - 1; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim())

            }
            expectedTravellerName.add("+" + tempInfantList.getAt(1).trim())
        }else if(data.input.infant==true) {
            //trvlrValues = resultData.hotel.itineraryPage.expectedleadTravellerName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + "2 " + data.expected.travellerlastName + "2" + ", " + data.expected.thirdTraveller_title_txt + " " + data.expected.childFirstName + "3 " + data.expected.childLastName + "3" + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+"+1 infant"
            trvlrValues=data.expected.title_txt + " " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.title_txt + " " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + " " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+"+1 infant"
            List<String> tempInfantList = trvlrValues.tokenize("+")
            String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = tmpTravellers.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i < travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            }

            for (int i = 0; i < travellers - 1; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim())

            }
            expectedTravellerName.add("+" + tempInfantList.getAt(1).trim())


        }
        else{
            //List<String> tempInfantList = trvlrValues.tokenize("+")
            //String tmpTravellers = tempInfantList.getAt(0).trim()

            List<String> tempTravellerList = trvlrValues.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")


            for (int i = 0; i <= travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
            }

            for (int i = 0; i <= travellers; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim().replaceAll(" ",""))

            }
        }
        resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellerName)

        //item name
        resultData.hotel.amendBooking.occupTab.actHotelName=getConfirmBookedTransferName()
        //item address
        resultData.hotel.amendBooking.occupTab.actHotelAddressTxt=getTransferDescBookingConfirmed()

            if(bookingIdIndx==0){
                resultData.hotel.amendBooking.occupTab.expItemName=resultData.hotel.itineraryPage.readFirstItemName
                resultData.hotel.amendBooking.occupTab.expItemAddressTxt=resultData.hotel.itineraryPage.readFirstItemAddressTxt

            }else if(bookingIdIndx==1){
                resultData.hotel.amendBooking.occupTab.expItemName=resultData.hotel.itineraryPage.readSecondItemName
                resultData.hotel.amendBooking.occupTab.expItemAddressTxt=resultData.hotel.itineraryPage.readSecondItemAddressTxt

            }

        //number of room, room type & value
        resultData.hotel.amendBooking.occupTab.expRoomNumAndroomValueTxt=resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText
        String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
        List<String> htlDesc=roomAndPax.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actRoomNumAndTypeTxt=htlDesc.getAt(0).trim()

        //Capture PAX
        resultData.hotel.amendBooking.occupTab.actpaxTxt=htlDesc.getAt(1).trim()
        if(data.input.infant==true) {
            resultData.hotel.amendBooking.occupTab.exppaxTxt = resultData.hotel.amendBooking.occupTab.actualPaxText + " (+" + data.expected.infantDropDownListValues.getAt(1) + " infant accommodated in cot)"
        }
        else {
            //resultData.hotel.amendBooking.occupTab.exppaxTxt = resultData.hotel.amendBooking.occupTab.actualPaxText
            resultData.hotel.amendBooking.occupTab.exppaxTxt = data.expected.modifiedpaxTxt.replaceAll(" ","")

        }
        //String expectedpaxTxt=actualPaxText

        //Meal basis & value
        resultData.hotel.amendBooking.occupTab.actMealBasisText=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

        //Check in date, Number of Nights
        resultData.hotel.amendBooking.occupTab.actCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed()
        //resultData.hotel.amendBooking.occupTab.expCheckInDate=resultData.hotel.amendBooking.occupTab.actCheckInAndNumOfNights
        if (totalNights >1)
            dur= checkInDate+", "+totalNights+" nights"
        else dur = checkInDate+", "+totalNights+" night"
        resultData.hotel.amendBooking.occupTab.expCheckInDate=dur

        //room rate amount and currency
        resultData.hotel.amendBooking.occupTab.actRoomRateAmountAndCurrency=getPriceBookingConfirmation()
        resultData.hotel.amendBooking.occupTab.expRoomRateAmntAndCurrncy=resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency.replaceAll(" ", "")

        //commission amount and currency
        resultData.hotel.amendBooking.occupTab.actCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
        resultData.hotel.amendBooking.occupTab.expCommissionAmountAndCurncy="Commission"+resultData.hotel.amendBooking.occupTab.actComsnValueInAboutToAmend.replace(" ", "")

        try {
            //< PDF voucher > button
            resultData.hotel.amendBooking.occupTab.actPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            resultData.hotel.amendBooking.occupTab.actPDFVoucherBtnDispStatus = false
            //softAssert.assertFalse(true,"Failed To Confirm Booking")
        }
        //click Close X
        coseBookItenary()
        sleep(4000)


    }

    protected def "bkngConfFromDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData,int travellers,int bookingIdIndx){

        def checkInDate
        def checkOutDt
        int totalNights
        int checkOutDays

            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+1, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+1.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())

        at ItineraryTravllerDetailsPage

        //Booking confirmed text
        resultData.hotel.amendBooking.occupTab.actBookingConfirmedTitleText=getCancellationHeader()

        //Close X function
        resultData.hotel.amendBooking.occupTab.actCloseBtnDispStatus=overlayCloseButton()

        //A confirmation of your booking has been email to:
        try{
            resultData.hotel.amendBooking.occupTab.actHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
        }catch(Exception e){
            resultData.hotel.amendBooking.occupTab.actHeaderSectionText="Unable To Read Header Text From UI"
        }

        String emailId=clientData.usernameOrEmail
        //emailId=emailId.toUpperCase()
        //resultData.hotel.amendBooking.occupTab.expHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
        resultData.hotel.amendBooking.occupTab.expHeaderSectionText=data.expected.headerSectionTxt

        //brand & icon
        resultData.hotel.amendBooking.occupTab.actBrandAndIconDispStatus=getBrandAndIconDisplayStatus()

        //Booking ID: & value
        resultData.hotel.amendBooking.occupTab.actBkngId = getBookingIdFromBookingConfirmed(0)


        if(bookingIdIndx==0){
            resultData.hotel.amendBooking.occupTab.expBkngID = resultData.hotel.itineraryPage.firstBookingID

        }else if(bookingIdIndx==1){
            resultData.hotel.amendBooking.occupTab.expBkngID =  resultData.hotel.itineraryPage.scndbookingID

        }

        //Itinerary ID,  Itinerary name & value
        resultData.hotel.amendBooking.occupTab.actitinearyIDAndName=getItinearyID(0)

        //Departure date: text
        resultData.hotel.amendBooking.occupTab.actDepDateTxt=getBookingIdFromBookingConfirmed(1)

        //Departure Date value text
        resultData.hotel.amendBooking.occupTab.actDepDateValTxt=getBookingDepartDate()
        resultData.hotel.amendBooking.occupTab.expDepDateValTxt=checkInDate.toUpperCase()

        //Traveller Details & value
        resultData.hotel.amendBooking.occupTab.actTravellerDetailsTxt=getBookingIdFromBookingConfirmed(2)


        //Traveller Values
        String trvlrValues=resultData.hotel.amendBooking.occupTab.actTravellersText
        //println("Debug Travlr Valu:$trvlrValues")
        List expectedTravellerName = []
        List actualtravellerName = []

            //List<String> tempInfantList = trvlrValues.tokenize("+")
            //String tmpTravellers = tempInfantList.getAt(0).trim()
            List<String> tempTravellerList = trvlrValues.tokenize(",")
            println("Debug tokenized Travlr Valu:$tempTravellerList")

            for (int i = 0; i < travellers; i++) {

                actualtravellerName.add(getTravellerNamesConfirmationDialog(i))
            }

            for (int i = 0; i < travellers; i++) {

                expectedTravellerName.add(tempTravellerList.getAt(i).trim())
            }
        resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerName)
        resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellerName)

        //item name
        resultData.hotel.amendBooking.occupTab.actHotelName=getConfirmBookedTransferName()
        //item address
        resultData.hotel.amendBooking.occupTab.actHotelAddressTxt=getTransferDescBookingConfirmed()

        if(bookingIdIndx==0){
            resultData.hotel.amendBooking.occupTab.expItemName=resultData.hotel.itineraryPage.readFirstItemName
            resultData.hotel.amendBooking.occupTab.expItemAddressTxt=resultData.hotel.itineraryPage.readFirstItemAddressTxt

        }else if(bookingIdIndx==1){
            resultData.hotel.amendBooking.occupTab.expItemName=resultData.hotel.itineraryPage.readSecondItemName
            resultData.hotel.amendBooking.occupTab.expItemAddressTxt=resultData.hotel.itineraryPage.readFirstItemAddressTxt

        }

        //number of room, room type & value
        resultData.hotel.amendBooking.occupTab.expRoomNumAndroomValueTxt=resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText
        String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
        List<String> htlDesc=roomAndPax.tokenize(",")

        resultData.hotel.amendBooking.occupTab.actRoomNumAndTypeTxt=htlDesc.getAt(0).trim()

        //Capture PAX
        resultData.hotel.amendBooking.occupTab.actpaxTxt=htlDesc.getAt(1).trim()

        resultData.hotel.amendBooking.occupTab.exppaxTxt = resultData.hotel.amendBooking.occupTab.actualPaxText

        //String expectedpaxTxt=actualPaxText

        //Meal basis & value
        resultData.hotel.amendBooking.occupTab.actMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

        //Check in date, Number of Nights
        resultData.hotel.amendBooking.occupTab.actCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed()
        resultData.hotel.amendBooking.occupTab.expCheckInDate=resultData.hotel.amendBooking.occupTab.actCheckInAndNumOfNights

        //room rate amount and currency
        resultData.hotel.amendBooking.occupTab.actRoomRateAmountAndCurrency=getPriceBookingConfirmation()
        resultData.hotel.amendBooking.occupTab.expRoomRateAmntAndCurrncy=resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency.replaceAll(" ", "")

        //commission amount and currency
        resultData.hotel.amendBooking.occupTab.actCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
        resultData.hotel.amendBooking.occupTab.expCommissionAmountAndCurncy="Commission"+resultData.hotel.amendBooking.occupTab.actComsnValueInAboutToAmend.replace(" ", "")

        try {
            //< PDF voucher > button
            resultData.hotel.amendBooking.occupTab.actPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            resultData.hotel.amendBooking.occupTab.actPDFVoucherBtnDispStatus =false
            //softAssert.assertFalse(true,"Failed To Confirm Booking")
        }
        //click Close X
        coseBookItenary()
        sleep(4000)


    }


    protected def "itinenaryUpdating"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()

        at ItineraryTravllerDetailsPage

        //Item card
        resultData.hotel.itineraryPage.bookedItems.acutalDisplayStatus=getItemCardDisplayStatus()

        //Get Booing ID and number
        resultData.hotel.itineraryPage.bookedItems.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

        //Confirmed tab
        resultData.hotel.itineraryPage.bookedItems.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()

        //confirm tab text
        resultData.hotel.itineraryPage.bookedItems.actualConfirmedTabTxt=getStatusInBookedItemsScrn()

        //Amend tab
        resultData.hotel.itineraryPage.bookedItems.actualAmendTabDispStatus=getAmendTabDisplayStatus()

        //Amend tab text
        resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt=getTabTxtInBookedItemsScrn(0)

        //Cancel tab
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabDispStatus=getCancelTabDisplayStatus()

        //Cancel tab text
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabTxt=getTabTxtInBookedItemsScrn(1)

        //item image
        resultData.hotel.itineraryPage.bookedItems.actualImageStatus=imgDisplayedOnSugestedItem(0)

        //item name
        resultData.hotel.itineraryPage.bookedItems.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

        //item star rating
        resultData.hotel.itineraryPage.bookedItems.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)+"Stars"

        //room type
        String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn=descText.trim()


        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()
        if(data.input.multiroom==true){
            resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn=data.expected.modifiedpaxTxt+" (+1 infant accommodated in cot)"
        }
        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        resultData.hotel.itineraryPage.bookedItems.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()

        //Free cancellation until date
        resultData.hotel.itineraryPage.bookedItems.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)

        //click on cancellation link
        clickBookedItemCancellationLink(0)

        //cancellation popup opens
        resultData.hotel.itineraryPage.bookedItems.actualCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

        //Cancellation policy
        resultData.hotel.itineraryPage.bookedItems.actualCanclPolcyTxt=getCancellationHeader()

        //Close X
        resultData.hotel.itineraryPage.bookedItems.actualCloseButtonDispStatus=overlayCloseButton()

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        resultData.hotel.itineraryPage.bookedItems.actualSplConditionTxt=getCancellationItemOverlayHeaders(0)

        //Please note text
        resultData.hotel.itineraryPage.bookedItems.actualPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)

        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Cancellation Charge text
            //resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(2)
            resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)
            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(2)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(3)

        }else if(data.input.multiroom==true){
            //Cancellation Charge text
            //resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(2)
            resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)
            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(2)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(3)

        }
        else{
            //Cancellation Charge text
            //resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(1)
            resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)

            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(1)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(2)

        }

        resultData.hotel.itineraryPage.bookedItems.actualCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()



        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        resultData.hotel.itineraryPage.bookedItems.actualDurationTxt=getItenaryDurationInSuggestedItem(0)
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.itineraryPage.bookedItems.expdurationTxt=dur

        //Voucher Icon
        resultData.hotel.itineraryPage.bookedItems.actualDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(0)

        //Commission and percentage
        resultData.hotel.itineraryPage.bookedItems.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)

        //Room rate amount and currency
        resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)

        at ShareItineraryTransfersPage


        if(data.input.children.size()>0 && (data.input.child==true)){
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.addchildren.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+ " " + "(" + data.input.addchildren.getAt(1).toString() + "yrs)"

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+ " " + "(" + data.input.children.getAt(1).toString() + "yrs)"+" +"+data.expected.infantDropDownListValues.getAt(2)+" "+data.expected.infantLabelTxt+"s"

        }
        else if(data.input.multiroom==true) {
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)"+" +1 infant"

        }
        else{
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName

        }

    }

    protected def "itinenaryUpdatingDates"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays, "ddMMMyy").toUpperCase()
        println("checkInDate:$checkInDt")
        int chkOutDays=data.expected.chkInDays+1.toInteger()
        int nights=chkOutDays-data.expected.chkInDays
        at ItineraryTravllerDetailsPage

        //Item card
        resultData.hotel.itineraryPage.bookedItems.acutalDisplayStatus=getItemCardDisplayStatus()

        //Get Booing ID and number
        resultData.hotel.itineraryPage.bookedItems.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

        //Confirmed tab
        resultData.hotel.itineraryPage.bookedItems.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()

        //confirm tab text
        resultData.hotel.itineraryPage.bookedItems.actualConfirmedTabTxt=getStatusInBookedItemsScrn()

        //Amend tab
        resultData.hotel.itineraryPage.bookedItems.actualAmendTabDispStatus=getAmendTabDisplayStatus()

        //Amend tab text
        if(data.input.multiroom==true) {
            resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt = getDisabledAmendTabTxtInBookedItemsScrn(0)
        }else{
            resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt = getTabTxtInBookedItemsScrn(2)
        }
        //Cancel tab
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabDispStatus=getCancelTabDisplayStatus()

        //Cancel tab text
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabTxt=getTabTxtInBookedItemsScrn(0)

        //item image
        resultData.hotel.itineraryPage.bookedItems.actualImageStatus=imgDisplayedOnSugestedItem(0)

        //item name
        resultData.hotel.itineraryPage.bookedItems.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

        //item star rating
        resultData.hotel.itineraryPage.bookedItems.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)+"Stars"

        //room type
        String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn=descText.trim()


        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()
        if(data.input.multiroom==true){
            resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn=data.expected.modifiedpaxTxt+" (+1 infant accommodated in cot)"
        }
        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        resultData.hotel.itineraryPage.bookedItems.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()

        //Free cancellation until date
        resultData.hotel.itineraryPage.bookedItems.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)

        //click on cancellation link
        clickBookedItemCancellationLink(0)

        //cancellation popup opens
        resultData.hotel.itineraryPage.bookedItems.actualCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

        //Cancellation policy
        resultData.hotel.itineraryPage.bookedItems.actualCanclPolcyTxt=getCancellationHeader()

        //Close X
        resultData.hotel.itineraryPage.bookedItems.actualCloseButtonDispStatus=overlayCloseButton()

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        resultData.hotel.itineraryPage.bookedItems.actualSplConditionTxt=getCancellationItemOverlayHeaders(0)

        //Please note text
        resultData.hotel.itineraryPage.bookedItems.actualPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)

        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Cancellation Charge text
            resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(2)
            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(2)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(3)

        }else if(data.input.multiroom==true){
            //Cancellation Charge text
            //resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(2)
            resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)
            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(2)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(3)

        }
        else{
            //Cancellation Charge text
            resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(1)
            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(1)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(2)

        }

        resultData.hotel.itineraryPage.bookedItems.actualCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()



        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        resultData.hotel.itineraryPage.bookedItems.actualDurationTxt=getItenaryDurationInSuggestedItem(0)
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.itineraryPage.bookedItems.expdurationTxt=dur

        //Voucher Icon
        resultData.hotel.itineraryPage.bookedItems.actualDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(0)

        //Commission and percentage
        resultData.hotel.itineraryPage.bookedItems.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)

        //Room rate amount and currency
        resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)

        at ShareItineraryTransfersPage


        if(data.input.children.size()>0 && (data.input.child==true)){
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.addchildren.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+ " " + "(" + data.input.addchildren.getAt(1).toString() + "yrs)"

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+ " " + "(" + data.input.children.getAt(1).toString() + "yrs)"+" +"+data.expected.infantDropDownListValues.getAt(2)+" "+data.expected.infantLabelTxt+"s"

        }
        else if(data.input.multiroom==true) {
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)"+" +1 infant"

        }
        else{
            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName

        }

    }
    protected def "itinenaryUpdatingDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){


        def checkInDt
        int chkOutDays
        int nights
        if(data.input.infant==true) {
            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            nights=(data.input.checkOutDays.toString().toInteger()+22)-(data.input.checkInDays.toString().toInteger()+20)
        }else {
            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
            println("checkInDate in Dates Tab:$checkInDt")
            chkOutDays = data.input.checkOutDays.toString().toInteger() + 1.toInteger()
            nights = chkOutDays - data.input.checkInDays.toString().toInteger()
            println("Number Of Nights in Dates Tab:$nights")
        }

        at ItineraryTravllerDetailsPage

        //Item card
        resultData.hotel.itineraryPage.bookedItems.acutalDisplayStatus=getItemCardDisplayStatus()

        //Get Booing ID and number
        resultData.hotel.itineraryPage.bookedItems.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

        //Confirmed tab
        resultData.hotel.itineraryPage.bookedItems.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()

        //confirm tab text
        resultData.hotel.itineraryPage.bookedItems.actualConfirmedTabTxt=getStatusInBookedItemsScrn()

        //Amend tab
        resultData.hotel.itineraryPage.bookedItems.actualAmendTabDispStatus=getAmendTabDisplayStatus()

        //Amend tab text
        if(data.input.multiroom==true) {
            resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt = getDisabledAmendTabTxtInBookedItemsScrn(0)
        }else{
            resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt = getTabTxtInBookedItemsScrn(0)
        }
        //Cancel tab
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabDispStatus=getCancelTabDisplayStatus()

        //Cancel tab text
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabTxt=getTabTxtInBookedItemsScrn(1)

        //item image
        resultData.hotel.itineraryPage.bookedItems.actualImageStatus=imgDisplayedOnSugestedItem(0)

        //item name
        resultData.hotel.itineraryPage.bookedItems.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

        //item star rating
        resultData.hotel.itineraryPage.bookedItems.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)+"Stars"

        //room type
        String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn=descText.trim()


        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()
        if(data.input.multiroom==true || data.input.infant==true){
            resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn=data.expected.modifiedpaxTxt+" (+1 infant accommodated in cot)"
        }
        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        resultData.hotel.itineraryPage.bookedItems.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()

        //Free cancellation until date
        resultData.hotel.itineraryPage.bookedItems.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)
        String canclDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")

        if(data.input.infant==true) {
            resultData.hotel.itineraryPage.bookedItems.expFreeCnclTxtInbookedItmsScrn=data.expected.freecancltxt+" "+canclDate.toUpperCase()
        }else{
            resultData.hotel.itineraryPage.bookedItems.expFreeCnclTxtInbookedItmsScrn=resultData.hotel.amendBooking.expectedFreeCanclTxt
            resultData.hotel.itineraryPage.bookedItems.expFreeCnclTxtInbookedItmsScrn=data.expected.freecancltxt+" "+canclDate.toUpperCase()
        }
        //click on cancellation link
        clickBookedItemCancellationLink(0)

        //cancellation popup opens
        resultData.hotel.itineraryPage.bookedItems.actualCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

        //Cancellation policy
        resultData.hotel.itineraryPage.bookedItems.actualCanclPolcyTxt=getCancellationHeader()

        //Close X
        resultData.hotel.itineraryPage.bookedItems.actualCloseButtonDispStatus=overlayCloseButton()

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        resultData.hotel.itineraryPage.bookedItems.actualSplConditionTxt=getCancellationItemOverlayHeaders(0)

        //Please note text
        resultData.hotel.itineraryPage.bookedItems.actualPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)


        if(data.input.infant==true){
            try{
                //Cancellation Charge text
                resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)

            }catch(Exception e){
                resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt="Unable To Read Cancellation Charge Text"
            }
           try{
               //If Amendments text
               resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt = getCancelPopupInsideTextInSuggestedItems(2)

           } catch(Exception e){
               resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt = "Unable To Read If amendments text"
           }
            try{
                //All dates text
                resultData.hotel.itineraryPage.bookedItems.actualDatesTxt = getCancelPopupInsideTextInSuggestedItems(3)

            }catch(Exception e){
                resultData.hotel.itineraryPage.bookedItems.actualDatesTxt = "Unable To read All Dates text"
            }

        }else {
                //Cancellation Charge text
                 resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)

                 //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt = getCancelPopupInsideTextInSuggestedItems(1)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt = getCancelPopupInsideTextInSuggestedItems(2)
        }

        resultData.hotel.itineraryPage.bookedItems.actualCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()

        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        resultData.hotel.itineraryPage.bookedItems.actualDurationTxt=getItenaryDurationInSuggestedItem(0)
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.itineraryPage.bookedItems.expdurationTxt=dur

        //Voucher Icon
        resultData.hotel.itineraryPage.bookedItems.actualDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(0)

        //Commission and percentage
        resultData.hotel.itineraryPage.bookedItems.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)

        //Room rate amount and currency
        //resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)
        resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency=getSuggestedItemsItenaryPrice(0)
        at ShareItineraryTransfersPage

            //Traveller Details
            resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()
        if(data.input.infant==true){
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails = "Travellers: " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + data.expected.childFirstName + " " + data.expected.childLastName  + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+" +1 infant"
        }else {
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails = "Travellers: " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + data.expected.childFirstName + " " + data.expected.childLastName  + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
        }


    }
    protected def "itinaryUpdtngDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){


        def checkInDt
        int chkOutDays
        int nights

            checkInDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "ddMMMyy").toUpperCase()
            nights=(data.input.checkOutDays.toString().toInteger()+20)-(data.input.checkInDays.toString().toInteger()+20)


        at ItineraryTravllerDetailsPage

        //Item card
        resultData.hotel.itineraryPage.bookedItems.acutalDisplayStatus=getItemCardDisplayStatus()

        //Get Booing ID and number
        resultData.hotel.itineraryPage.bookedItems.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

        //Confirmed tab
        resultData.hotel.itineraryPage.bookedItems.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()

        //confirm tab text
        resultData.hotel.itineraryPage.bookedItems.actualConfirmedTabTxt=getStatusInBookedItemsScrn()

        //Amend tab
        resultData.hotel.itineraryPage.bookedItems.actualAmendTabDispStatus=getAmendTabDisplayStatus()

        //Amend tab text

        resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt = getTabTxtInBookedItemsScrn(0)

        //Cancel tab
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabDispStatus=getCancelTabDisplayStatus()

        //Cancel tab text
        resultData.hotel.itineraryPage.bookedItems.actualCancelTabTxt=getTabTxtInBookedItemsScrn(1)

        //item image
        resultData.hotel.itineraryPage.bookedItems.actualImageStatus=imgDisplayedOnSugestedItem(0)

        //item name
        resultData.hotel.itineraryPage.bookedItems.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

        //item star rating
        resultData.hotel.itineraryPage.bookedItems.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)+"Stars"

        //room type
        String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn=descText.trim()


        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()

            resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn=data.expected.modifiedpaxTxt

        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        resultData.hotel.itineraryPage.bookedItems.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()

        //Free cancellation until date
        resultData.hotel.itineraryPage.bookedItems.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)
        String canclDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger()+20, "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")


            resultData.hotel.itineraryPage.bookedItems.expFreeCnclTxtInbookedItmsScrn=resultData.hotel.amendBooking.expectedFreeCanclTxt

        //click on cancellation link
        clickBookedItemCancellationLink(0)

        //cancellation popup opens
        resultData.hotel.itineraryPage.bookedItems.actualCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

        //Cancellation policy
        resultData.hotel.itineraryPage.bookedItems.actualCanclPolcyTxt=getCancellationHeader()

        //Close X
        resultData.hotel.itineraryPage.bookedItems.actualCloseButtonDispStatus=overlayCloseButton()

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        resultData.hotel.itineraryPage.bookedItems.actualSplConditionTxt=getCancellationItemOverlayHeaders(0)

        //Please note text
        resultData.hotel.itineraryPage.bookedItems.actualPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)



            //Cancellation Charge text
            resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)

            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt = getCancelPopupInsideTextInSuggestedItems(1)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actualDatesTxt = getCancelPopupInsideTextInSuggestedItems(2)


        resultData.hotel.itineraryPage.bookedItems.actualCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()

        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        resultData.hotel.itineraryPage.bookedItems.actualDurationTxt=getItenaryDurationInSuggestedItem(0)
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.itineraryPage.bookedItems.expdurationTxt=dur

        //Voucher Icon
        resultData.hotel.itineraryPage.bookedItems.actualDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(0)

        //Commission and percentage
        resultData.hotel.itineraryPage.bookedItems.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)

        //Room rate amount and currency
        resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)

        at ShareItineraryTransfersPage

        //Traveller Details
        resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails=getTravellerDetails()

            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails = "Travellers: " + data.expected.firstName + " " + data.expected.lastName



    }

    protected def "itinryUpdatingDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDate
        def checkOutDt
        int totalNights
        int checkOutDays
        if(data.input.infant==true) {
            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+2, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+2.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())
        }else {
            checkInDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 5, "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger() + 7, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays = data.input.checkOutDays.toString().toInteger() + 6.toInteger()
            totalNights = checkOutDays - (data.input.checkInDays.toString().toInteger() + 5)
            println("Number Of Nights in Dates Tab:$totalNights")
        }

        at ItineraryTravllerDetailsPage

        //Item card
        resultData.hotel.itineraryPage.bookedItems.actDisplayStatus=getItemCardDisplayStatus()

        //Get Booing ID and number
        resultData.hotel.itineraryPage.bookedItems.actBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

        //Confirmed tab
        resultData.hotel.itineraryPage.bookedItems.actconfirmedTabDispStatus=getConfirmedTabDisplayStatus()

        //confirm tab text
        resultData.hotel.itineraryPage.bookedItems.actConfirmedTabTxt=getStatusInBookedItemsScrn()

        //Amend tab
        resultData.hotel.itineraryPage.bookedItems.actAmendTabDispStatus=getAmendTabDisplayStatus()

        //Amend tab text

            resultData.hotel.itineraryPage.bookedItems.actAmendTabTxt = getTabTxtInBookedItemsScrn(0)

        //Cancel tab
        resultData.hotel.itineraryPage.bookedItems.actCancelTabDispStatus=getCancelTabDisplayStatus()

        //Cancel tab text
        resultData.hotel.itineraryPage.bookedItems.actCancelTabTxt=getTabTxtInBookedItemsScrn(1)

        //item image
        resultData.hotel.itineraryPage.bookedItems.actImageStatus=imgDisplayedOnSugestedItem(0)

        //item name
        resultData.hotel.itineraryPage.bookedItems.actHotelNameTxt=getTransferNameInSuggestedItem(1)

        //item star rating
        resultData.hotel.itineraryPage.bookedItems.actStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(1)+"Stars"

        //room type
        String descComplTxt=getItinenaryDescreptionInSuggestedItem(1)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actroomdescTxtInBookedItmsScrn=descText.trim()


        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actPaxNumInBookedItmsScrn=paxNumDetails.trim()

        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        resultData.hotel.itineraryPage.bookedItems.actratePlanInBookedItmsScrn=ratePlantxtDetails.trim()

        //Free cancellation until date
        resultData.hotel.itineraryPage.bookedItems.actFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(1)

        //click on cancellation link
        clickBookedItemCancellationLink(1)

        //cancellation popup opens
        resultData.hotel.itineraryPage.bookedItems.actCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

        //Cancellation policy
        resultData.hotel.itineraryPage.bookedItems.actCanclPolcyTxt=getCancellationHeader()

        //Close X
        resultData.hotel.itineraryPage.bookedItems.actCloseButtonDispStatus=overlayCloseButton()

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        resultData.hotel.itineraryPage.bookedItems.actSplConditionTxt=getCancellationItemOverlayHeaders(0)

        //Please note text
        resultData.hotel.itineraryPage.bookedItems.actPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)


        //Cancellation Charge text
        resultData.hotel.itineraryPage.bookedItems.actCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)
        if(data.input.infant==true){

            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actIfAmendmentsTxt = getCancelPopupInsideTextInSuggestedItems(2)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actDatesTxt = getCancelPopupInsideTextInSuggestedItems(3)

        }else {

            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actIfAmendmentsTxt = getCancelPopupInsideTextInSuggestedItems(1)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actDatesTxt = getCancelPopupInsideTextInSuggestedItems(2)
        }

        resultData.hotel.itineraryPage.bookedItems.actCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()

        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        resultData.hotel.itineraryPage.bookedItems.actDurationTxt=getItenaryDurationInSuggestedItem(1)
        if (totalNights >1)
            dur= checkInDate+", "+totalNights+" nights"
        else dur = checkInDate+", "+totalNights+" night"
        resultData.hotel.itineraryPage.bookedItems.expdurTxt=dur

        //Voucher Icon
        resultData.hotel.itineraryPage.bookedItems.actDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(1)

        //Commission and percentage
        resultData.hotel.itineraryPage.bookedItems.actcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(1)

        //Room rate amount and currency
        //resultData.hotel.itineraryPage.bookedItems.actPriceAndcurrency=getItenaryPriceInSuggestedItem(1)
        resultData.hotel.itineraryPage.bookedItems.actPriceAndcurrency=getSuggestedItemsItenaryPrice(1)

        at ShareItineraryTransfersPage

        //Traveller Details
        resultData.hotel.itineraryPage.bookedItems.acttravellerDetails=getTravellerDetails(1)
        if(data.input.infant==true){
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails = "Travellers: " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"+" +1 infant"
        }else {
            resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails = "Travellers: " + data.expected.firstName + " " + data.expected.lastName + ", " + data.expected.travellerfirstName + " " + data.expected.travellerlastName + ", " + data.expected.childFirstName + " " + data.expected.childLastName + " " + "(" + data.input.children.getAt(0).toString() + "yrs)"
        }

    }

    protected def "itinryUpdngDatesTab"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDate
        def checkOutDt
        int totalNights
        int checkOutDays

            checkInDate=dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger(), "ddMMMyy").toUpperCase()
            checkOutDt = dateUtil.addDaysChangeDatetformat(data.expected.chkInDays.toInteger()+1, "ddMMMyy").toUpperCase()

            println("checkInDate in Dates Tab:$checkInDate")
            checkOutDays=data.expected.chkInDays.toInteger()+1.toInteger()
            totalNights=checkOutDays-(data.expected.chkInDays.toInteger())


        at ItineraryTravllerDetailsPage

        //Item card
        resultData.hotel.itineraryPage.bookedItems.actDisplayStatus=getItemCardDisplayStatus()

        //Get Booing ID and number
        resultData.hotel.itineraryPage.bookedItems.actBookingIDinBookedDetailsScrn=getBookingIDinBookeddetailsScrn()

        //Confirmed tab
        resultData.hotel.itineraryPage.bookedItems.actconfirmedTabDispStatus=getConfirmedTabDisplayStatus()

        //confirm tab text
        resultData.hotel.itineraryPage.bookedItems.actConfirmedTabTxt=getStatusInBookedItemsScrn()

        //Amend tab
        resultData.hotel.itineraryPage.bookedItems.actAmendTabDispStatus=getAmendTabDisplayStatus()

        //Amend tab text

        resultData.hotel.itineraryPage.bookedItems.actAmendTabTxt = getTabTxtInBookedItemsScrn(0)

        //Cancel tab
        resultData.hotel.itineraryPage.bookedItems.actCancelTabDispStatus=getCancelTabDisplayStatus()

        //Cancel tab text
        resultData.hotel.itineraryPage.bookedItems.actCancelTabTxt=getTabTxtInBookedItemsScrn(1)

        //item image
        resultData.hotel.itineraryPage.bookedItems.actImageStatus=imgDisplayedOnSugestedItem(0)

        //item name
        resultData.hotel.itineraryPage.bookedItems.actHotelNameTxt=getTransferNameInSuggestedItem(1)

        //item star rating
        resultData.hotel.itineraryPage.bookedItems.actStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(1)+"Stars"

        //room type
        String descComplTxt=getItinenaryDescreptionInSuggestedItem(1)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actroomdescTxtInBookedItmsScrn=descText.trim()


        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.actPaxNumInBookedItmsScrn=paxNumDetails.trim()

        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        resultData.hotel.itineraryPage.bookedItems.actratePlanInBookedItmsScrn=ratePlantxtDetails.trim()

        //Free cancellation until date
        resultData.hotel.itineraryPage.bookedItems.actFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(1)

        //click on cancellation link
        clickBookedItemCancellationLink(1)

        //cancellation popup opens
        resultData.hotel.itineraryPage.bookedItems.actCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

        //Cancellation policy
        resultData.hotel.itineraryPage.bookedItems.actCanclPolcyTxt=getCancellationHeader()

        //Close X
        resultData.hotel.itineraryPage.bookedItems.actCloseButtonDispStatus=overlayCloseButton()

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        resultData.hotel.itineraryPage.bookedItems.actSplConditionTxt=getCancellationItemOverlayHeaders(0)

        //Please note text
        resultData.hotel.itineraryPage.bookedItems.actPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)


        //Cancellation Charge text
        resultData.hotel.itineraryPage.bookedItems.actCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)


            //If Amendments text
            resultData.hotel.itineraryPage.bookedItems.actIfAmendmentsTxt = getCancelPopupInsideTextInSuggestedItems(1)
            //All dates text
            resultData.hotel.itineraryPage.bookedItems.actDatesTxt = getCancelPopupInsideTextInSuggestedItems(2)


        resultData.hotel.itineraryPage.bookedItems.actCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()

        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        resultData.hotel.itineraryPage.bookedItems.actDurationTxt=getItenaryDurationInSuggestedItem(1)
        if (totalNights >1)
            dur= checkInDate+", "+totalNights+" nights"
        else dur = checkInDate+", "+totalNights+" night"
        resultData.hotel.itineraryPage.bookedItems.expdurTxt=dur

        //Voucher Icon
        resultData.hotel.itineraryPage.bookedItems.actDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(1)

        //Commission and percentage
        resultData.hotel.itineraryPage.bookedItems.actcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(1)

        //Room rate amount and currency
        resultData.hotel.itineraryPage.bookedItems.actPriceAndcurrency=getItenaryPriceInSuggestedItem(1)

        at ShareItineraryTransfersPage

        //Traveller Details
        resultData.hotel.itineraryPage.bookedItems.acttravellerDetails=getTravellerDetails(1)

        resultData.hotel.itineraryPage.bookedItems.exptravlrDetails = "Travellers: " + data.expected.travellerfirstName + " " + data.expected.travellerlastName


    }


    protected def "currentDetailsSecond"(HotelSearchData data, HotelTransferTestResultData resultData){
        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //text should show
        resultData.hotel.amendBooking.occupTab.currntDetails.actualOccupTxt=getOccupancyInsideTxtInNonAmendablePopup(1)

        //Item Name
        resultData.hotel.amendBooking.occupTab.currntDetails.actualItemNameInOccupancyTab=getItemNameInOccupanceTab(1)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(1)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        //booked room type
        resultData.hotel.amendBooking.occupTab.currntDetails.actualroomdescTxt=tempItinryCanclDescList.getAt(0).trim()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.occupTab.currntDetails.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt

        }else{
            resultData.hotel.amendBooking.occupTab.currntDetails.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt

        }

        //meal basis
        resultData.hotel.amendBooking.occupTab.currntDetails.actualmealBasisTxt=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        resultData.hotel.amendBooking.occupTab.currntDetails.actualfreeCancelTxt=getFreeCancelTxtInOccupanceTab(1)
        String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
        println "Cancel Date is:$cancelDate"
        resultData.hotel.amendBooking.occupTab.currntDetails.expectedFreeCanclTxt=data.expected.freecancltxt+" "+cancelDate.toUpperCase()

        //check in, number of night
        resultData.hotel.amendBooking.occupTab.currntDetails.actualCheckInAndNumOfNight=getCheckInTxtInOccupanceTab(1)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.occupTab.currntDetails.expectedCheckInAndNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.occupTab.currntDetails.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()

        //travellers
        //resultData.hotel.amendBooking.occupTab.currntDetails.actualTravellersText=getTravellersInOccupanceTab(1)
        String travellersTextAndPax=getTravellersInOccupanceTab(1)
        //List<String> tempTrvlrsTxt=travellersTextAndPax.tokenize("PAX - ")

        resultData.hotel.amendBooking.occupTab.currntDetails.actualTravellersText=travellersTextAndPax
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.occupTab.currntDetails.expectedTravellersTxt=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+"2 "+data.expected.travellerlastName+"2"+", "+data.expected.thirdTraveller_title_txt+" "+data.expected.childFirstName+"3 "+data.expected.childLastName+"3"+" " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)"+" +1 infant"

        }else{
            //resultData.hotel.amendBooking.occupTab.currntDetails.expectedTravellersTxt=resultData.hotel.amendBooking.occupTab.actualTravellersText
            resultData.hotel.amendBooking.occupTab.currntDetails.expectedTravellersTxt=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName

        }

        //Room Amount and Currency
        resultData.hotel.amendBooking.occupTab.currntDetails.actualTotalRoomAmntAndCurrncyTxt=getTotalRoomAmntAndCurrencyInOccupancyTab(1)

        //Commission
        resultData.hotel.amendBooking.occupTab.currntDetails.actualCommissionTxt=getCommissionInOccupancyTab(1)

    }

    protected def "changeAddAndRemoveLess"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Change, add or remove occupants availability:
        resultData.hotel.amendBooking.occupTab.actualChangeOccupantsTxt=getChangeOccupantsTxtInOccupancyTab()
        String trvlrValues
        if(data.input.children.size()>0 && (data.input.child==true)){
            trvlrValues=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+" "+data.expected.childFirstName+" "+data.expected.childLastName+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName
        }
        else if(data.input.children.size()>0 && (data.input.infant==true)) {
            //travellers
            //trvlrValues=resultData.hotel.amendBooking.actualTravellersText
            //trvlrValues=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+"2 "+data.expected.travellerlastName+"2"+", "+data.expected.thirdTraveller_title_txt+" "+data.expected.childFirstName+"3 "+data.expected.childLastName+"3"+", "+data.expected.thirdTraveller_title_txt+ " "+data.expected.childFirstName+"4 "+data.expected.childLastName+"4"+" +2 "+data.expected.infantLabelTxt+"s"
            trvlrValues=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+" "+data.expected.childFirstName+" "+data.expected.childLastName+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName
        }
        else if(data.input.multiroom==true) {
            //travellers
            trvlrValues = data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+" "+data.expected.childFirstName+" "+data.expected.childLastName+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName

        }

        else{
            // Occupancy & Traveller Values
            //trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
            trvlrValues=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
        }


        List<String> tempTravellerList=trvlrValues.tokenize(",")


        List expectedOccupantName=[]
        List actualOccupantName = []
        List actualOccupantListNum=[]
        List expectedOccupantListNum=[]

        for(int i=1;i<=4;i++){
            expectedOccupantName.add(tempTravellerList.getAt(i-1).trim())
            actualOccupantName.add(getOccupantListNameInOccupancyTab(i))
            actualOccupantListNum.add(getOccupantListNumInOccupancyTab(i))
            expectedOccupantListNum.add(data.expected.occupantTxt+" "+i)
        }

        resultData.hotel.amendBooking.occupTab.put("actualOCupNames", actualOccupantName)
        resultData.hotel.amendBooking.occupTab.put("expectedOcupnames", expectedOccupantName)

        resultData.hotel.amendBooking.occupTab.put("actualOCupList", actualOccupantListNum)
        resultData.hotel.amendBooking.occupTab.put("expectedOcupList", expectedOccupantListNum)


    }

    protected def "changeAddRemoveMore"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Change, add or remove occupants availability:
        resultData.hotel.amendBooking.occupTab.actualChangeOccupantsTxt=getChangeOccupantsTxtInOccupancyTab()

        //Traveller Values
        String trvlrValues
        if(data.input.multiroom==true) {

            trvlrValues=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+" "+data.expected.childFirstName+" "+data.expected.childLastName+" +1 "+data.expected.infantLabelTxt

        }else{
            //String trvlrValues=resultData.hotel.amendBooking.occupTab.actualTravellersText
            trvlrValues=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+" "+data.expected.childFirstName+" "+data.expected.childLastName+", "+ " "+data.expected.childFirstName+" "+data.expected.childLastName+" +2 "+data.expected.infantLabelTxt+"s"
            //println("Debug Travlr Valu:$trvlrValues")
        }



        List<String> tempInfantList=trvlrValues.tokenize("+")
        String tmpTravellers=tempInfantList.getAt(0).trim()
        List<String> tempTravellerList=tmpTravellers.tokenize(",")
        println("Debug tokenized Travlr Valu:$tempTravellerList")


        List expectedOccupantName=[]
        List actualOccupantName = []
        List actualOccupantListNum=[]
        List expectedOccupantListNum=[]

        if(data.input.multiroom==true) {
            for(int i=1;i<=3;i++){
                expectedOccupantName.add(tempTravellerList.getAt(i-1).trim())
                actualOccupantName.add(getOccupantListNameInOccupancyTab(i))
                actualOccupantListNum.add(getOccupantListNumInOccupancyTab(i))
                expectedOccupantListNum.add(data.expected.occupantTxt+" "+i)
            }
            actualOccupantName.add(getInfantsInOccupancyTab(1))
            expectedOccupantName.add("+"+tempInfantList.getAt(1).trim())
        }else{
            for(int i=1;i<=4;i++){
                expectedOccupantName.add(tempTravellerList.getAt(i-1).trim())
                actualOccupantName.add(getOccupantListNameInOccupancyTab(i))
                actualOccupantListNum.add(getOccupantListNumInOccupancyTab(i))
                expectedOccupantListNum.add(data.expected.occupantTxt+" "+i)
            }
            actualOccupantName.add(getInfantsInOccupancyTab(1))
            expectedOccupantName.add("+"+tempInfantList.getAt(1).trim())
        }


        resultData.hotel.amendBooking.occupTab.put("actualOCupNames", actualOccupantName)
        resultData.hotel.amendBooking.occupTab.put("expectedOcupnames", expectedOccupantName)

        resultData.hotel.amendBooking.occupTab.put("actualOCupList", actualOccupantListNum)
        resultData.hotel.amendBooking.occupTab.put("expectedOcupList", expectedOccupantListNum)


    }

    protected def "removeanadultpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Uncheck 3rd traveller
        clickTravellersCheckBox(3)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)
    }
    protected def "removeanchildpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Uncheck 4th traveller
        clickTravellersCheckBox(4)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)
    }

    protected def "removeanInfant"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Infant 2
        selectNumberOfInfants(1)
        sleep(2000)
        clickConfirmAmendmentBtn()
        sleep(4000)
    }

    protected def "changeToBeforeThirdAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
        at ItineraryTravllerDetailsPage

        //change to: text should show
        resultData.hotel.amendBooking.occupTab.changeTo.actualOccupancyTxt=getOccupancyInsideTxtInNonAmendablePopup(2)

        //Item Name
        resultData.hotel.amendBooking.occupTab.changeTo.actualItemNameInOccupTab=getItemNameInOccupanceTab(2)

        //number of room & type of room
        String actualRoomTypeAndRoom=getRoomNumAndRoomTypeInOccupanceTab(2)
        List<String> tempItinryCanclDescList=actualRoomTypeAndRoom.tokenize(",")
        resultData.hotel.amendBooking.occupTab.changeTo.actualNumAndtypeOfRoomTxt=tempItinryCanclDescList.getAt(0).trim()
        if(data.input.multiroom==true) {
            resultData.hotel.amendBooking.occupTab.changeTo.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt

        }else{
            resultData.hotel.amendBooking.occupTab.changeTo.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt

        }

        //meal basis
        resultData.hotel.amendBooking.occupTab.changeTo.actualmealBasisText=tempItinryCanclDescList.getAt(1).trim()

        //Free Cancellation Text
        //resultData.hotel.amendBooking.occupTab.changeTo.actualfreeCancellationTxt=getFreeCancelTxtInOccupanceTab(2)
        resultData.hotel.amendBooking.occupTab.changeTo.actualfreeCancellationTxt=getFreeCancelTxtInOccupanceTab(1)

        //check in, number of night
        resultData.hotel.amendBooking.occupTab.changeTo.actualCheckInPlusNumOfNight=getCheckInTxtInOccupanceTab(2)
        if (nights >1)
            dur= "Check in: "+checkInDt+", "+nights+" nights"
        else dur = "Check in: "+checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.occupTab.changeTo.expCheckInPlusNumOfNight=dur

        //number of pax
        String actualPAXAndTravellers=getPAXAndTravellersInOccupanceTab(2)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")
        resultData.hotel.amendBooking.occupTab.changeTo.actPaxText=tempPAXAndTravellersList.getAt(0).trim()



        if(data.input.children.size()>0 && (data.input.child==true)){
            //travellers
            resultData.hotel.amendBooking.occupTab.changeTo.actTravlrTxt=getTravellersInOccupanceTab(2)
            resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.addchildren.getAt(0).toString() + "yrs)"

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //travellers
            resultData.hotel.amendBooking.occupTab.changeTo.actTravlrTxt=getTravellersInOccupanceTab(2)
            resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(1).toString() + "yrs)"+" +1 "+data.expected.infantLabelTxt

        }else if(data.input.multiroom==true) {
            //travellers
            resultData.hotel.amendBooking.occupTab.changeTo.actTravlrTxt=getTravellersInOccupanceTab(2)
            resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.thirdTraveller_title_txt+" "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)"+", "+data.expected.thirdTraveller_title_txt+ " "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_SecondRoom.getAt(1).toString() + "yrs)"+" +1 "+data.expected.infantLabelTxt

        }
        else{
            //travellers
            resultData.hotel.amendBooking.occupTab.changeTo.actTravlrTxt=getTravellersInOccupanceTab(2)
            resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt=resultData.hotel.itineraryPage.expectedleadTravellerName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.title_txt+" "+data.expected.travellerfirstName+" "+data.expected.travellerlastName

        }


        //amount rate change and currency
        resultData.hotel.amendBooking.occupTab.changeTo.actpriceDiffAndCurrnTxt=getPriceDiffInOccupancyTab()
        println("Actual Price Diff Text:$resultData.hotel.amendBooking.occupTab.changeTo.actpriceDiffAndCurrnTxt")
        resultData.hotel.amendBooking.occupTab.changeTo.expPriceDiff=getPriceDiffValue()+" GBP"

        //Room Amount and Currency
        resultData.hotel.amendBooking.occupTab.changeTo.actTotalRoomAmntAndCurrncyText=getTotalRoomAmntAndCurrencyInOccupancyTab(2)
        resultData.hotel.amendBooking.occupTab.changeTo.expTotalRoomAmntAndCurrncyTxt=getPositiveChangeAmountValue()+" GBP"

        //Commission
        resultData.hotel.amendBooking.occupTab.changeTo.actualCommissionText=getCommissionInOccupancyTab(2)

        //confirmation status(inventory)
        resultData.hotel.amendBooking.occupTab.changeTo.actualConfirmationStatus=getInventoryStatusInOccupancyTab()

        //Ok button should be displayed
        resultData.hotel.amendBooking.occupTab.changeTo.actualOkBtnDispStatus=getOkButtonDisplayStatus()

        //Ok button should be enabled
        resultData.hotel.amendBooking.occupTab.changeTo.actualOkBtnEnableStatus=getOkButtonEnableOrDisableStatus()
    }

    protected def "selectToAssignBeforeThirdAmend"(HotelSearchData data, HotelTransferTestResultData resultData,int adultStartindx, int adultendindx, int childstrtindx, int childendindx, def children){

        at ItineraryTravllerDetailsPage

        //Please select occupants names:
        resultData.hotel.amendBooking.occupTab.plzSelect.actualPleaseSelectTxt=getPleaseSelectTxtInOccupancyTab()

        //tick box should present for each traveller listed in traveller details section
        travellersListCheckBoxExistenceCheck(1,childendindx,data)

        //Get 1st traveller checked status
        resultData.hotel.amendBooking.occupTab.plzSelect.actFirstTrvlrchkBoxStatus=getTravellerCheckedStatus(1)

        //Get 2nd traveller checked status
        resultData.hotel.amendBooking.occupTab.plzSelect.actSecondTrvlrchkBoxStatus=getTravellerCheckedStatus(2)


        if(data.input.children.size()>0 && (data.input.child==true)){
            //Get 3rd traveller checked status
            resultData.hotel.amendBooking.occupTab.plzSelect.actThirdTrvlchkBoxStatus=getTravellerCheckedStatus(3)

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Get 3rd traveller checked status
            resultData.hotel.amendBooking.occupTab.plzSelect.actThirdTrvlchkBoxStatus=getTravellerCheckedStatus(3)

            //Get 4th traveller checked status
            resultData.hotel.amendBooking.occupTab.plzSelect.actFourthTrvlchkBoxStatus=getTravellerCheckedStatus(4)
        }else if(data.input.multiroom==true) {
            //Get 3rd traveller checked status
            resultData.hotel.amendBooking.occupTab.plzSelect.actThirdTrvlchkBoxStatus=getTravellerCheckedStatus(3)

            //Get 7th traveller checked status
            resultData.hotel.amendBooking.occupTab.plzSelect.actSeventhTrvlchkBoxStatus=getTravellerCheckedStatus(7)

        }
        else{
            //Get 4th traveller checked status
            resultData.hotel.amendBooking.occupTab.plzSelect.actFourthTrvlchkBoxStatus=getTravellerCheckedStatus(4)

        }



        //lead traveller
        resultData.hotel.amendBooking.occupTab.plzSelect.actualLeadTravlr=getTravellerNameTxt(1)
        resultData.hotel.amendBooking.occupTab.plzSelect.expectedLeadTravlrTxt=data.expected.firstName+" "+data.expected.lastName+" "+"(lead name)"

        //2nd to 5th traveller
        verifyAdultTravellers(adultStartindx,adultendindx,data)
        //6th to 9th traveller
        verifyChildTravellers(childstrtindx,childendindx,data,children)

        //infant text should show - if it is available for the room
        resultData.hotel.amendBooking.occupTab.plzSelect.actualInfantLabTxt=getInfantLabelTxt()

        //infant value displaying = 1
        if(data.input.children.size()>0 && (data.input.infant==true)){
            resultData.hotel.amendBooking.actualSelectedInfantValue=getInfantDropDownSelectedValueInOccupancyTab()
        }

        //dropdown list to select number of infant should show
        resultData.hotel.amendBooking.occupTab.plzSelect.actualInfantDropdownListDispStatus=getInfantSelectBoxDisplayStatus()

        //list should show 0-1
        resultData.hotel.amendBooking.occupTab.plzSelect.actListInfantValues=getInfantDropDownValuesInOccupancyTab()

        //Confirm Amendment Button display status
        resultData.hotel.amendBooking.occupTab.plzSelect.actualConfirmAmendmentButtonStatus=getConfirmAmendButtonDisplayStatus()

        //Confirm Amendment Button Disabled status
        resultData.hotel.amendBooking.occupTab.plzSelect.actualConfirmAmendmentDisabledStatus=getConfirmAmendmentButtonEnableOrDisableStatus()

    }

    protected def "TAndCpagereconfirmationAfterReduceAmend"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()

        at ItineraryTravllerDetailsPage

        //capture title text
        resultData.hotel.amendBooking.aboutToAmend.actualtitleTxt=getCancellationHeader()

        //Capture Close Icon display status
        resultData.hotel.amendBooking.aboutToAmend.actualCloseIconDispStatus=getCloseIconDisplayStatus()

        //Capture hotel name in About to Amend Screen
        resultData.hotel.amendBooking.aboutToAmend.actualHotelNameInAbtToAmndScrn=getHotelNameInAboutToBookScrn()

        //number of room/name of room (descripton)
        //String tmphotelTxt=getTitleSectDescTxt(0)
        String tmphotelTxt=getItemDescPaxAndCityTxtInAboutToBookScreen()
        List<String> htlDesc=tmphotelTxt.tokenize(",")

        resultData.hotel.amendBooking.aboutToAmend.actualnumAndNameofRoomText=htlDesc.getAt(0).trim()
        if(data.input.multiroom==true) {
            //resultData.hotel.amendBooking.aboutToAmend.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.firstRoomDescTxt
            resultData.hotel.amendBooking.aboutToAmend.expectedRoomNumAndTypeOfRoomTxt=data.expected.firstRoomDescTxt

        }else{
            //resultData.hotel.amendBooking.aboutToAmend.expectedRoomNumAndTypeOfRoomTxt="1x "+data.expected.roomDescTxt
            resultData.hotel.amendBooking.aboutToAmend.expectedRoomNumAndTypeOfRoomTxt=data.expected.roomDescTxt

        }

        //meal basis
        resultData.hotel.amendBooking.aboutToAmend.actualmealBasisTxt=htlDesc.getAt(1).trim()

        //number of pax
        /*String actualPAXAndTravellers=getTitleSectDescTxt(1)
        List<String> tempPAXAndTravellersList=actualPAXAndTravellers.tokenize("-")

        resultData.hotel.amendBooking.aboutToAmend.actualPaxText=tempPAXAndTravellersList.getAt(0).trim()*/
        resultData.hotel.amendBooking.aboutToAmend.actualPaxText=getTitleSectDescTxt(3).replaceAll("\n","")

        //travellers
        //resultData.hotel.amendBooking.aboutToAmend.actualTravellersText=getTravellersInAboutToAmend(1)

        //check in date, number of night
        //resultData.hotel.amendBooking.aboutToAmend.actualCheckInAndNumOfNight=getTitleSectDescTxt(2).replaceAll(" ","")
        resultData.hotel.amendBooking.aboutToAmend.actualCheckInAndNumOfNight=getTitleSectDescTxt(2).replaceAll("\n","")
        /*if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.aboutToAmend.expCheckInAndNumOfNight=dur.replaceAll(" ","")*/
        resultData.hotel.amendBooking.aboutToAmend.expCheckInAndNumOfNight="Nights"+nights

        //status
        resultData.hotel.amendBooking.aboutToAmend.actualInvStatusInAbtToAmendScrn=getPriceInAbouttoBookScrn(0)

        //amount rate change and currency
        resultData.hotel.amendBooking.aboutToAmend.actualAmntRatechangeAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(1)

        //total room amount & currency code
        resultData.hotel.amendBooking.aboutToAmend.actualRoomAmntAndCurncyAbtToAmendScrn=getPriceInAbouttoBookScrn(2)

        //commission & % value
        resultData.hotel.amendBooking.aboutToAmend.actualcommissionPercentAbtToAmendScrn=getPriceInAbouttoBookScrn(3)

        //special condition - header text
        resultData.hotel.amendBooking.aboutToAmend.actualSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToAmendScrn(0)
        resultData.hotel.amendBooking.aboutToAmend.expectedSpecialConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDt

        //Please note text
        resultData.hotel.amendBooking.aboutToAmend.actualPlzNoteTxt=getDescriptionTextInAboutToAmendScrn(0)


        //Cancellation charge descriptive text
        List<String> actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()
        resultData.hotel.amendBooking.aboutToAmend.actualCancellationChrgTxt=actualCancellationChrgTxt


        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Cancellation Charge text
            //resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(2)
            resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)
            //If Amendments text
            resultData.hotel.amendBooking.aboutToAmend.actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(2)

            //All dates text
            resultData.hotel.amendBooking.aboutToAmend.actualDatesTxt=getDescriptionTextInAboutToAmendScrn(3)

        }else if(data.input.multiroom==true){
            //Cancellation Charge text
            //resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(2)
            resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)
            //If Amendments text
            resultData.hotel.amendBooking.aboutToAmend.actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(2)

            //All dates text
            resultData.hotel.amendBooking.aboutToAmend.actualDatesTxt=getDescriptionTextInAboutToAmendScrn(3)

        }
        else{
            //Cancellation Charge text
            //resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmendScrn(1)
            resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt=getOverlayHeadersTextInAboutToAmndScrn(0)
            //If Amendments text
            resultData.hotel.amendBooking.aboutToAmend.actualIfAmendmentsTxt=getDescriptionTextInAboutToAmendScrn(1)

            //All dates text
            resultData.hotel.amendBooking.aboutToAmend.actualDatesTxt=getDescriptionTextInAboutToAmendScrn(2)
        }




        //Total
        //resultData.hotel.amendBooking.aboutToAmend.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)
        resultData.hotel.amendBooking.aboutToAmend.actualTotalTextInAboutToBook=getTotalAndCommissionTextInAboutTo(0)
        //Total Amount and currency
        //resultData.hotel.amendBooking.aboutToAmend.actualTotalAmountAndCurrency=getTotalInConfirmbooking(1)
        resultData.hotel.amendBooking.aboutToAmend.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)

        //Your Commission text
        //resultData.hotel.amendBooking.aboutToAmend.actualCommissionTextInAboutToAmend=getTotalAndCommissionText(1)
        //resultData.hotel.amendBooking.aboutToAmend.actualCommissionTextInAboutToAmend=getTotalAndCommissionTextInAboutTo(2)
        resultData.hotel.amendBooking.aboutToAmend.actualCommissionTextInAboutToAmend=getTotalAndCommissionTextInAboutTo(1)


        //Your commission amount and currency
        //resultData.hotel.amendBooking.aboutToAmend.actualCommissionValueInAboutToAmend=getCommissionValueAndCurrencyInAboutToBookScrn(1)
        //resultData.hotel.amendBooking.aboutToAmend.actualCommissionValueInAboutToAmend=getYourCommissionAmountAndCurrencyTextInAboutToBookScreen(1)
        resultData.hotel.amendBooking.aboutToAmend.actualCommissionValueInAboutToAmend=getYourCommissionAmountAndCurrencyTextInAboutToBookScreen(0)
        float compercentage = Float.parseFloat(data.expected.commissionPercent)
        String priceText=resultData.hotel.amendBooking.aboutToAmend.actualTotalAmountAndCurrency.replace(" GBP", "").trim()
        float comAmount=Float.parseFloat(priceText)
        String comValue=getCommissionPercentageValue(comAmount,compercentage)
        String expectedComValue=comValue+" "+clientData.currency
        resultData.hotel.amendBooking.aboutToAmend.expCommissionValueInAboutToAmend=expectedComValue

        //< Confirm Amendment > button display status
        resultData.hotel.amendBooking.aboutToAmend.actualconfirmButtonDispStatus=getConfirmAmendButtonDispStatusInAboutToAmend()

        //confirm Amendment enable status
        resultData.hotel.amendBooking.aboutToAmend.actualconfirmAmendEnableStatus=getConfirmAmendButtonEnableOrDisableStatusInAboutToAmend()

        //T&C agreement text
        //String ByClickingFooterTxt=getByClickingFooterTxt(3)
        //String ByClickingFooterTxt=getByClickingFooterTxt(5)
        String ByClickingFooterTxt=getByClickingFooterTxt()
        resultData.hotel.amendBooking.aboutToAmend.actualTermsAndCondtTxt=ByClickingFooterTxt.replace("\n", "")

        //click < Confirm Amendment > button
        //clickonConfirmAmendment()
        clickOnConfirmBookingAndPayLater()
        sleep(3000)
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitForAjaxIconToDisappear()
        scrollToTopOfThePage()
        //Confirm booking page display status
        resultData.hotel.amendBooking.aboutToAmend.actualconfirmbookStatus=getBookingConfirmationScreenDisplayStatus()


    }

    protected def "bookingConfirmingAfterReduceAmend"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()

        at ItineraryTravllerDetailsPage

        //Booking confirmed text
        resultData.hotel.amendBooking.bookingConf.actualBookingConfirmedTitleText=getCancellationHeader()

        //Close X function
        resultData.hotel.amendBooking.bookingConf.actualCloseBtnDispStatus=overlayCloseButton()

        //A confirmation of your booking has been email to:
        try{
            resultData.hotel.amendBooking.bookingConf.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
        }catch(Exception e){
            resultData.hotel.amendBooking.bookingConf.actualHeaderSectionText="Unable To Read Header Text From UI"
        }

        String emailId=clientData.usernameOrEmail
        //emailId=emailId.toUpperCase()
        //resultData.hotel.amendBooking.bookingConf.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
        resultData.hotel.amendBooking.bookingConf.expectedHeaderSectionText=data.expected.headerSectionTxt

        //brand & icon
        resultData.hotel.amendBooking.bookingConf.actualBrandAndIconDispStatus=getBrandAndIconDisplayStatus()

        //Booking ID: & value
        resultData.hotel.amendBooking.bookingConf.actualbookingID="Booking ID: "+getBookingIdFromBookingConfirmed(0)
        resultData.hotel.amendBooking.bookingConf.expectedBookingID="Booking ID: "+resultData.hotel.itineraryPage.retrievedbookingID

        //Itinerary ID,  Itinerary name & value
        resultData.hotel.amendBooking.bookingConf.actualitinearyIDAndName=getItinearyID(0)

        //Departure date: text
        resultData.hotel.amendBooking.bookingConf.actualDepDateTxt=getBookingIdFromBookingConfirmed(1)

        //Departure Date value text
        resultData.hotel.amendBooking.bookingConf.actualDepDateValTxt=getBookingDepartDate()
        resultData.hotel.amendBooking.bookingConf.expectedDepDateValTxt=checkInDt.toUpperCase()

        //Traveller Details & value
        resultData.hotel.amendBooking.bookingConf.actualTravellerDetailsTxt=getBookingIdFromBookingConfirmed(2)

        //Traveller Values
        //String trvlrValues=resultData.hotel.amendBooking.aboutToAmend.actualTravellersText
        String trvlrValues=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
        //println("Debug Travlr Valu:$trvlrValues")
        List<String> tmpTravellerList=trvlrValues.tokenize(",")
        //println("Debug tokenized Travlr Valu:$tmpTravellerList")
        List expectedTravellerName=[]
        List actualtravellerName = []

        for(int i=0;i<=2;i++){
            expectedTravellerName.add(tmpTravellerList.getAt(i).trim().replaceAll(" ",""))

            actualtravellerName.add(getTravellerNamesConfirmationDialog(i).replaceAll(" ",""))
            //assertionEquals(actualTravellerName,expectedTravellerName, "After Amend Booking Confirmation, $i th traveller value text Actual & Expected don't match")
        }

        resultData.hotel.amendBooking.bookingConf.put("actualTravellers", actualtravellerName)
        resultData.hotel.amendBooking.bookingConf.put("expectedTravellers", expectedTravellerName)

        //item name
        resultData.hotel.amendBooking.bookingConf.actualHotelName=getConfirmBookedTransferName()
        resultData.hotel.amendBooking.bookingConf.expectedItemName=resultData.hotel.itineraryPage.readItemName

        //item address
        resultData.hotel.amendBooking.bookingConf.actualHotelAddressTxt=getTransferDescBookingConfirmed()
        resultData.hotel.amendBooking.bookingConf.expectedItemAddressTxt=resultData.hotel.itineraryPage.readItemAddressTxt

        //number of room, room type & value
        //resultData.hotel.amendBooking.bookingConf.expectedRoomNumAndroomValueTxt=resultData.hotel.amendBooking.aboutToAmend.actualnumAndNameofRoomText
        resultData.hotel.amendBooking.bookingConf.expectedRoomNumAndroomValueTxt="1x "+resultData.hotel.amendBooking.aboutToAmend.actualnumAndNameofRoomText
        String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
        List<String> htlDesc=roomAndPax.tokenize(",")

        resultData.hotel.amendBooking.bookingConf.actualRoomNumAndTypeTxt=htlDesc.getAt(0).trim()

        //Capture PAX
        resultData.hotel.amendBooking.bookingConf.actualpaxTxt=htlDesc.getAt(1).trim()
        //String expectedpaxTxt=actualPaxText
        //4PAX  (+1 infant accommodated in cot)
        if(((data.input.children.size()>0) && (data.input.infant==true)) ){
            resultData.hotel.amendBooking.occupTab.exppaxText=resultData.hotel.amendBooking.occupTab.actualPaxText+" (+"+data.expected.infantDropDownListValues.getAt(1)+" infant accommodated in cot)"
        }
        if((data.input.multiroom==true)){
            resultData.hotel.amendBooking.occupTab.exppaxText=data.expected.scndAmndPaxTxt+" (+"+data.expected.infantDropDownListValues.getAt(1)+" infant accommodated in cot)"
        }


        //Meal basis & value
        resultData.hotel.amendBooking.bookingConf.actualMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

        //Check in date, Number of Nights
        resultData.hotel.amendBooking.bookingConf.actualCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed()
        //resultData.hotel.amendBooking.bookingConf.expectedCheckInDate=resultData.hotel.amendBooking.aboutToAmend.actualCheckInAndNumOfNight
        if (nights >1)
            dur= checkInDt+","+nights+" nights"
        else dur = checkInDt+","+nights+" night"
        resultData.hotel.amendBooking.bookingConf.expectedCheckInDate=dur
        //room rate amount and currency
        resultData.hotel.amendBooking.bookingConf.actualRoomRateAmountAndCurrency=getPriceBookingConfirmation()
        resultData.hotel.amendBooking.bookingConf.expectedRoomRateAmntAndCurrncy=resultData.hotel.amendBooking.aboutToAmend.actualTotalAmountAndCurrency.replaceAll(" ", "")

        //commission amount and currency
        resultData.hotel.amendBooking.bookingConf.actualCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
        resultData.hotel.amendBooking.bookingConf.expectedCommissionAmountAndCurncy="Commission"+resultData.hotel.amendBooking.aboutToAmend.actualCommissionValueInAboutToAmend.replace(" ", "")

        try {
            //< PDF voucher > button
            resultData.hotel.amendBooking.bookingConf.actualPDFVoucherBtnDispStatus = getPDFVoucherBtnDisplayStatus()
        }catch(Exception e)
        {
            //Assert.assertFalse(true,"Failed To Confirm Booking")
            resultData.hotel.amendBooking.bookingConf.actualPDFVoucherBtnDispStatus =false
                   // softAssert.assertFalse(true,"Failed To Confirm Booking")
        }

        //click Close X
        coseBookItenary()
        sleep(4000)


    }

    protected def "itinenaryUpdatingAfterReduceAmend"(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){

        def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        int nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()

        at ItineraryTravllerDetailsPage

        //Item card
        resultData.hotel.amendBooking.itineraryAfterReduce.acutalDisplayStatus=getItemCardDisplayStatus()

        //Get Booing ID and number
        resultData.hotel.amendBooking.itineraryAfterReduce.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

        //Confirmed tab
        resultData.hotel.amendBooking.itineraryAfterReduce.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()

        //confirm tab text
        resultData.hotel.amendBooking.itineraryAfterReduce.actualConfirmedTabTxt=getStatusInBookedItemsScrn()

        //Amend tab
        resultData.hotel.amendBooking.itineraryAfterReduce.actualAmendTabDispStatus=getAmendTabDisplayStatus()

        //Amend tab text
        resultData.hotel.amendBooking.itineraryAfterReduce.actualAmendTabTxt=getTabTxtInBookedItemsScrn(0)

        //Cancel tab
        resultData.hotel.amendBooking.itineraryAfterReduce.actualCancelTabDispStatus=getCancelTabDisplayStatus()

        //Cancel tab text
        resultData.hotel.amendBooking.itineraryAfterReduce.actualCancelTabTxt=getTabTxtInBookedItemsScrn(1)

        //item image
        resultData.hotel.amendBooking.itineraryAfterReduce.actualImageStatus=imgDisplayedOnSugestedItem(0)

        //item name
        resultData.hotel.amendBooking.itineraryAfterReduce.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

        //item star rating
        resultData.hotel.amendBooking.itineraryAfterReduce.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)+"Stars"

        //room type
        String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
        List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

        String descText=tmpItineraryDescList.getAt(0)
        resultData.hotel.amendBooking.itineraryAfterReduce.actualroomdescTxtInBookedItmsScrn=descText.trim()


        //Pax number requested
        String tempTxt=tmpItineraryDescList.getAt(1)
        List<String> tmpDescList=tempTxt.tokenize(".")

        String paxNumDetails=tmpDescList.getAt(0)
        resultData.hotel.amendBooking.itineraryAfterReduce.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()

        //Rate plan - meal basis
        String ratePlantxtDetails=tmpDescList.getAt(1)
        resultData.hotel.amendBooking.itineraryAfterReduce.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()

        //Free cancellation until date
        resultData.hotel.amendBooking.itineraryAfterReduce.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)

        //click on cancellation link
        clickBookedItemCancellationLink(0)

        //cancellation popup opens
        resultData.hotel.amendBooking.itineraryAfterReduce.actualCancelPopupDisStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

        //Cancellation policy
        resultData.hotel.amendBooking.itineraryAfterReduce.actualCanclPolcyTxt=getCancellationHeader()

        //Close X
        resultData.hotel.amendBooking.itineraryAfterReduce.actualCloseButtonDispStatus=overlayCloseButton()

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        resultData.hotel.amendBooking.itineraryAfterReduce.actualSplConditionTxt=getCancellationItemOverlayHeaders(0)

        //Please note text
        resultData.hotel.amendBooking.itineraryAfterReduce.actualPlzNoteTxt=getCancelPopupInsideTextInSuggestedItems(0)


        resultData.hotel.amendBooking.itineraryAfterReduce.actualCancellationChrgTxt=getCancellationChargeTxtInBookedItemsCancelPopupScrn()



        if((data.input.children.size()>0 && (data.input.infant==true))||(data.input.multiroom==true)){
            //Cancellation Charge text
            //resultData.hotel.amendBooking.itineraryAfterReduce.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(2)
            resultData.hotel.amendBooking.itineraryAfterReduce.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)

            //If Amendments text
            resultData.hotel.amendBooking.itineraryAfterReduce.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(2)

            //All dates text
            resultData.hotel.amendBooking.itineraryAfterReduce.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(3)

        }else{
            //Cancellation Charge text
            //resultData.hotel.amendBooking.itineraryAfterReduce.actualCancellationChargeTxt=getCancellationItemOverlayHeaders(1)
            resultData.hotel.amendBooking.itineraryAfterReduce.actualCancellationChargeTxt=getCancellationAndAmendmentItemOverlayHeaders(0)

            //If Amendments text
            resultData.hotel.amendBooking.itineraryAfterReduce.actualIfAmendmentsTxt=getCancelPopupInsideTextInSuggestedItems(1)

            //All dates text
            resultData.hotel.amendBooking.itineraryAfterReduce.actualDatesTxt=getCancelPopupInsideTextInSuggestedItems(2)

        }

        //click on close
        clickOnCloseButtonSuggestedItemsCancelpopup()

        //check in date and number of nights
        resultData.hotel.amendBooking.itineraryAfterReduce.actualDurationTxt=getItenaryDurationInSuggestedItem(0).replaceAll(" ","")
        if (nights >1)
            dur= checkInDt+", "+nights+" nights"
        else dur = checkInDt+", "+nights+" night"
        resultData.hotel.amendBooking.itineraryAfterReduce.expdurationTxt=dur.replaceAll(" ","")

        //Voucher Icon
        resultData.hotel.amendBooking.itineraryAfterReduce.actualDownloadVocherIconDispStatus=getDownloadVocherIconDisplayStatus(0)

        //Commission and percentage
        resultData.hotel.amendBooking.itineraryAfterReduce.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)

        //Room rate amount and currency
        resultData.hotel.amendBooking.itineraryAfterReduce.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)

        at ShareItineraryTransfersPage



        if(data.input.children.size()>0 && (data.input.child==true)){
            //Traveller Details
            resultData.hotel.amendBooking.itineraryAfterReduce.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.amendBooking.itineraryAfterReduce.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.addchildren.getAt(0).toString() + "yrs)"

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Traveller Details
            resultData.hotel.amendBooking.itineraryAfterReduce.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.amendBooking.itineraryAfterReduce.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+ " " + "(" + data.input.children.getAt(1).toString() + "yrs)"+" +"+data.expected.infantDropDownListValues.getAt(1)+" "+data.expected.infantLabelTxt

        } else if(data.input.multiroom==true){
            //Traveller Details
            resultData.hotel.amendBooking.itineraryAfterReduce.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.amendBooking.itineraryAfterReduce.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+" "+data.expected.childLastName+" " + "(" + data.input.children_FirstRoom.getAt(0).toString() + "yrs)"+", "+data.expected.childFirstName+" "+data.expected.childLastName+ " " + "(" + data.input.children_SecondRoom.getAt(1).toString() + "yrs)"+" +"+data.expected.infantDropDownListValues.getAt(1)+" "+data.expected.infantLabelTxt

        }
        else{
            //Traveller Details
            resultData.hotel.amendBooking.itineraryAfterReduce.actualtravellerDetails=getTravellerDetails()
            resultData.hotel.amendBooking.itineraryAfterReduce.expectedtravellerDetails="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName+", "+data.expected.travellerfirstName+" "+data.expected.travellerlastName

        }


    }


    protected def "VerifyCreatenewitinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item should return in search results
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults,data.expected.dispStatus,"Hotel Search Results Screen, Item Existence in Search Results Actual & Expected don't match")

        //More room section expanded
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualMoreRoomSectionExpandedStatus,data.expected.dispStatus,"Hotel Search Results Screen, More Room Types Expanded Status Actual & Expected don't match")

        //Validate Itinerary builder items added count
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,data.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")

       //Capture item is added to itinerary builder
         assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Items added Actual & Expected don't match")

    }

    protected def "VerifyCreatenewitinry"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item should return in search results
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults,data.expected.dispStatus,"Hotel Search Results Screen, Item Existence in Search Results Actual & Expected don't match")

        //More room section expanded
        //assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualMoreRoomSectionExpandedStatus,data.expected.dispStatus,"Hotel Search Results Screen, More Room Types Expanded Status Actual & Expected don't match")

        //Validate Itinerary builder items added count
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,data.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")

        //Capture item is added to itinerary builder
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Items added Actual & Expected don't match")

        VerifyGoToItinerary(data, resultData)
        VerifyTravellerDetails(data, resultData)
        VerifyBookHotelItemWithAllTravelers(data, resultData)

        //capture expanded status
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionOpenStatus,data.expected.notDispStatus,"Hotel Search Results Screen, Itinerary Builder Expanded Status Actual & Expected don't match")
    }


    protected def "VerifycreateNewItineraryMultiRoom"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item should return in search results
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults,data.expected.dispStatus,"Hotel Search Results Screen, Item Existence in Search Results Actual & Expected don't match")

        //Validate Itinerary builder items added count
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,data.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")

        //Validate First item is added to itinerary builder
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section First Item added Actual & Expected don't match")

        //Validate Second item is added to itinerary builder
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Second Item added Actual & Expected don't match")

        VerifyGoToItinerary(data, resultData)
        VerifyTravellerDetails(data, resultData)
        VerifyBookHotelItemWithAllTravelers(data, resultData)

        //item name
        assertionEquals(resultData.hotel.itineraryPage.readFirstItemName,data.expected.cityAreaHotelText,"Booking Confirmation Screen, First Item Name Actual & Expected don't match")

        assertionEquals(resultData.hotel.itineraryPage.readSecondItemName,data.expected.cityAreaHotelText,"Booking Confirmation Screen, Second Item Name Actual & Expected don't match")


    }


    protected def "VerifyaddSecondItem"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item should return in search results
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualSecondItemInSearchResults,data.expected.dispStatus,"Hotel Search Results Screen, Item Existence in Search Results Actual & Expected don't match")

        //Validate Itinerary builder items added count
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actCountOfItemsItineraryBuilder,data.expected.itinrybuldrUpdatedSecCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")

        //Capture item is added to itinerary builder
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actItinryHotelname,data.expected.cityAreaHotelTxt,"Hotel Search Results Screen Itinerary Builder Section Items added Actual & Expected don't match")

        //Validate Itinerary Page Traveller Label Txt
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actTravellerLabelTxt, data.expected.travlrDetailTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")


        VerifyBookHotelItemWithAllTravelers(data, resultData)

      }


    protected def "Verifyadditinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item should return in search results
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItemExistenceInSearchResults,data.expected.dispStatus,"Hotel Search Results Screen, Item Existence in Search Results Actual & Expected don't match")

        //Validate Itinerary builder items added count
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,data.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")

        //Capture item is added to itinerary builder
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Items added Actual & Expected don't match")

    }

    protected def "VerifyCreatenewitinerarymultiroom"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate Itinerary builder items added count
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,data.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")

        //Capture item is added to itinerary builder
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Items added Actual & Expected don't match")

        //Capture item is added to itinerary builder
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Items added Actual & Expected don't match")


    }


    protected def "VerifyGoToItinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate Itinerary Page Traveller Label Txt
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt, data.expected.travellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")

    }

    protected def "VerifyTravellerDetails"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate Lead Traveller Name - 1st Traveller
        assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerName,resultData.hotel.itineraryPage.expectedleadTravellerName,"Itinerary page, Traveller Details - First or Lead Traveller Name Details actual & expected don't match")

        //Validate Lead Traveller - 1st Traveller - Telephone Number
        assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum,resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum,"Itinerary page, Traveller Details - First or Lead Traveller Telephone Number Details actual & expected don't match")

        //Validate Lead Traveller - 1st Traveller - Email Address
        assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerEmail,resultData.hotel.itineraryPage.expectedleadTravellerEmail,"Itinerary page, Traveller Details - First or Lead Traveller Email Address Details actual & expected don't match")

    }




    protected def "VerifyBookingConfirmDetails"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking Confirmation Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus,data.expected.dispStatus, "Booking Confirmation Screen display status Actual & Expected don't match")

        //Title text-Booking Confirmed
        assertionEquals(resultData.hotel.itineraryPage.actualBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt, "Booking Confirmation Screen, title text Actual & Expected don't match")

        //First Traveller Name
        assertionEquals(resultData.hotel.confirmationPage.actualfirstTravellerName,resultData.hotel.itineraryPage.expectedleadTravellerName,"Booking Confirmation Screen, First Traveller Name actual & expected don't match")
        //Second Traveller name
        assertionEquals(resultData.hotel.confirmationPage.actualsecondTravellerName,resultData.hotel.itineraryPage.expectedscndTravellerName,"Booking Confirmation Screen, Second Traveller Name actual & expected don't match")

        if(data.input.children.size()>0){
            //capture child traveller details
            assertionEquals(resultData.hotel.confirmationPage.actualThirdTravellerName,resultData.hotel.confirmationPage.expectedThirdTravellerName,"Booking Confirmation Screen, Child Traveller Name actual & expected don't match")
        }

    }

    protected def "VerifyBookHotelItemWithAllTravelers"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking Confirmation Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus,data.expected.dispStatus, "Booking Confirmation Screen display status Actual & Expected don't match")

        //Title text-Booking Confirmed
        //assertionEquals(resultData.hotel.itineraryPage.actualBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt, "Booking Confirmation Screen, title text Actual & Expected don't match")

        List actualTravellerData=resultData.hotel.confirmationPage.travellers.get("actTravellers")
        List expectedTravelerData=resultData.hotel.confirmationPage.travellers.get("expTravellers")
        //Validate  travellers
        assertionListEquals(actualTravellerData,expectedTravelerData,"Booking Confirmation, List Of Travellers  actual & expected don't match")


    }

    protected def "VerifyAmendOccupancy"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Amend Booking screen display status
        assertionEquals(resultData.hotel.amendBooking.actualAmendPopupDispStatus,data.expected.dispStatus, "Amend popup Screen, display status Actual & Expected don't match")

        //Amend Booking Txt
        assertionEquals(resultData.hotel.amendBooking.actualAmendTitleTxt,data.expected.amendTitleTxt, "Amend popup Screen,  Commission Percent text Actual & Expected don't match")

        //Close X function
        assertionEquals(resultData.hotel.amendBooking.actualCloseXDisplayStatus,data.expected.dispStatus, "Amend popup Screen, close button display status Actual & Expected don't match")

        //Date tab should open per default
        assertionEquals(resultData.hotel.amendBooking.actualDatesTabActiveStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab display status Actual & Expected don't match")

        //Occupancy tab should not open per default
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTabInactStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab  display status Actual & Expected don't match")

       //Occupancy tab opens up
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTabactStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab after click on Occupancy tab display status Actual & Expected don't match")
    }
    protected def "VerifyAmendOccupancyDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Amend Booking screen display status
        assertionEquals(resultData.hotel.amendBooking.actualAmendPopupDispStatus,data.expected.dispStatus, "Amend popup Screen, display status Actual & Expected don't match")

        //Amend Booking Txt
        assertionEquals(resultData.hotel.amendBooking.actualAmendTitleTxt,data.expected.amendTitleTxt, "Amend popup Screen,  Commission Percent text Actual & Expected don't match")

        //Close X function
        assertionEquals(resultData.hotel.amendBooking.actualCloseXDisplayStatus,data.expected.dispStatus, "Amend popup Screen, close button display status Actual & Expected don't match")

        //Date tab should open per default
        assertionEquals(resultData.hotel.amendBooking.actualDatesTabActiveStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab display status Actual & Expected don't match")

        //Occupancy tab should not open per default
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTabInactStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab  display status Actual & Expected don't match")

    }

    protected def "VerifyamndOccupDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Amend Booking screen display status
        assertionEquals(resultData.hotel.amendBooking.actAmendPopupDispStatus,data.expected.dispStatus, "Amend popup Screen, display status Actual & Expected don't match")

        //Amend Booking Txt
        assertionEquals(resultData.hotel.amendBooking.actAmendTitleTxt,data.expected.amendTitleTxt, "Amend popup Screen,  Commission Percent text Actual & Expected don't match")

        //Close X function
        assertionEquals(resultData.hotel.amendBooking.actCloseXDisplayStatus,data.expected.dispStatus, "Amend popup Screen, close button display status Actual & Expected don't match")

        //Date tab should open per default
        assertionEquals(resultData.hotel.amendBooking.actDatesTabActiveStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab display status Actual & Expected don't match")

        //Occupancy tab should not open per default
        assertionEquals(resultData.hotel.amendBooking.actOccupancyTabInactStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab  display status Actual & Expected don't match")

    }


    protected def "VerifyNameChange"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Amend Booking screen display status
        assertionEquals(resultData.hotel.amendBooking.actualAmendPopupDispStatus,data.expected.dispStatus, "Amend popup Screen, display status Actual & Expected don't match")

        //Amend Booking Txt
        assertionEquals(resultData.hotel.amendBooking.actualAmendTitleTxt,data.expected.amendTitleTxt, "Amend popup Screen,  Commission Percent text Actual & Expected don't match")

        //Close X function
        assertionEquals(resultData.hotel.amendBooking.actualCloseXDisplayStatus,data.expected.dispStatus, "Amend popup Screen, close button display status Actual & Expected don't match")

        //Date tab should open per default
        assertionEquals(resultData.hotel.amendBooking.actualDatesTabActiveStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab display status Actual & Expected don't match")

        //Occupancy tab should not open per default
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTabInactStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab  display status Actual & Expected don't match")

        //Occupancy tab opens up
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTabactStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab after click on Occupancy tab display status Actual & Expected don't match")

    }

    protected def "VerifyaboutToAmendAddASpecialRemarkorComment"(HotelSearchData data, HotelTransferTestResultData resultData){

        //About to Amend Screen, special remarks text
        assertionEquals(resultData.hotel.itineraryPage.actualSpecialRemarkOrCommentTxt,data.expected.remarkTxt, "About To Amend Screen, Add a special remark or comment Text Actual & Expected don't match")

        //Confirmation Screen, Should display "Please note: Will arrive without voucher"
        //Please Note txt
        assertionEquals(resultData.hotel.amendBooking.actualPlzNoteTxtInBookingConfScrn,data.expected.pleaseNoteTxt, "Confirmation Screen, Please Note Text Actual & Expected don't match")

        //Remarks
        assertionEquals(resultData.hotel.amendBooking.actualRemarksTxtInBookingConfScrn,data.expected.chkBoxWilArriveWithOutVoucherTxt, "Confirmation Screen, Remarks Text Actual & Expected don't match")

        //Booked Item Section, Should display "Please note: Will arrive without voucher"
        assertionEquals(resultData.hotel.amendBooking.actualPlzNoteAndRemarksTxtInBkdItmsSec,resultData.hotel.amendBooking.expectedPlzNoteAndRemarksTxtInBkdItmsSec, "Booked Items Section, Please Note Text & Remarks Text Actual & Expected don't match")

    }


    protected def "VerifyamendOccupancyNADatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){


        //ToolTip
        //assertionEquals(resultData.hotel.amendBooking.actualToolTip,data.expected.mouseHoverTxt, "Amend popup Screen, Tool tip Actual & Expected don't match")


    }


    protected def "VerifycurrentDetails"(HotelSearchData data, HotelTransferTestResultData resultData){

        //text should show
        assertionEquals(resultData.hotel.amendBooking.actualOccupTxt,data.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking text Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInOccupancyTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualroomdescTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text Actual & Expected don't match")

        //meal basis
        assertionEquals( resultData.hotel.amendBooking.actualmealBasisTxt,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, meal basis text Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actualfreeCancelTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInAndNumOfNight,resultData.hotel.amendBooking.expectedCheckInAndNumOfNight, "Amend popup Screen,  Occupancy tab,  actual check in and duration text Actual & Expected don't match")

        if(data.input.children.size()>0 && (data.input.child==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
            //travellers
            assertionEquals(resultData.hotel.amendBooking.actualTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")

        }else if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
            //travellers
            assertionEquals(resultData.hotel.amendBooking.actualTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")

        }else if(data.input.multiroom==true){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
            //travellers
            assertionEquals(resultData.hotel.amendBooking.actualTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")

        }

        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
            //travellers
            assertionEquals(resultData.hotel.amendBooking.actualTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")

        }

        if(data.input.multiroom==true){
            //Room Amount and Currency
            assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedScndItemPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

        }else{
            //Room Amount and Currency
            assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

        }

        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionTxt,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text Actual & Expected don't match")

    }
    protected def "VerifycurrentDetailsDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //text should show
        assertionEquals(resultData.hotel.amendBooking.actualOccupTxt,data.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking text Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInOccupancyTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualroomdescTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text Actual & Expected don't match")

        //meal basis
        //assertionEquals( resultData.hotel.amendBooking.actualmealBasisTxt,data.expected.mealBasisText, "Amend popup Screen,  Occupancy tab, meal basis text Actual & Expected don't match")

        //Free Cancellation Text
        //assertionEquals(resultData.hotel.amendBooking.actfreeCancelText,resultData.hotel.amendBooking.expFreeCancelTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInAndNumOfNight,resultData.hotel.amendBooking.expectedCheckInAndNumOfNight, "Amend popup Screen,  Occupancy tab,  actual check in and duration text Actual & Expected don't match")

            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
            //travellers
            assertionEquals(resultData.hotel.amendBooking.actualTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")
            //Room Amount and Currency
              if(data.input.multiroom==true){

                  assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

              }else{
                  assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

              }



        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionTxt,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text Actual & Expected don't match")

    }

    protected def "VerifycurrentDetailsDatesTabSecondHotel"(HotelSearchData data, HotelTransferTestResultData resultData){

        //text should show
        assertionEquals(resultData.hotel.amendBooking.actualOccupTxt,data.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking text Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInOccupancyTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualroomdescTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text Actual & Expected don't match")

        //meal basis
        assertionEquals( resultData.hotel.amendBooking.actualmealBasisTxt,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, meal basis text Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actualfreeCancelTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInAndNumOfNight,resultData.hotel.amendBooking.expectedCheckInAndNumOfNight, "Amend popup Screen,  Occupancy tab,  actual check in and duration text Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.actualPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
        //travellers
        assertionEquals(resultData.hotel.amendBooking.actualTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")
        //Room Amount and Currency
        if(data.input.multiroom==true){

            assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

        }else{
            assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

        }



        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionTxt,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text Actual & Expected don't match")

    }


    protected def "VerifycurntDetailsDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //text should show
        assertionEquals(resultData.hotel.amendBooking.actOccupTxt,data.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking text Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.actItemNameInOccupancyTab,data.expected.cityAreaHotelTxt, "Amend popup Screen,  Occupancy tab, item name text Actual & Expected don't match")

        //booked room type
        //assertionEquals(resultData.hotel.amendBooking.actroomdescTxt,resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text Actual & Expected don't match")

        //meal basis
       // assertionEquals(resultData.hotel.amendBooking.actmealBasisTxt,data.expected.mealBasisText, "Amend popup Screen,  Occupancy tab, meal basis text Actual & Expected don't match")

        //Free Cancellation Text
        //assertionEquals(resultData.hotel.amendBooking.actfreeCancelTxt,resultData.hotel.amendBooking.expFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actCheckInAndNumOfNight,resultData.hotel.amendBooking.expCheckInAndNumOfNight, "Amend popup Screen,  Occupancy tab,  actual check in and duration text Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.actPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
        //travellers
        assertionEquals(resultData.hotel.amendBooking.actTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")
        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")
        //Commission
        assertionEquals(resultData.hotel.amendBooking.actCommissionTxt,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text Actual & Expected don't match")

    }

    protected def "VerifycurntDetailsInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //text should show
        assertionEquals(resultData.hotel.amendBooking.actOccupTxt,data.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking text Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.actItemNameInOccupancyTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actroomdescTxt,resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text Actual & Expected don't match")

        //meal basis
       // assertionEquals(resultData.hotel.amendBooking.actmealBasisTxt,data.expected.mealBasisText, "Amend popup Screen,  Occupancy tab, meal basis text Actual & Expected don't match")

        //Free Cancellation Text
        //assertionEquals(resultData.hotel.amendBooking.actfreeCancelTxt,resultData.hotel.amendBooking.expFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //check in, number of night
        //assertionEquals(resultData.hotel.amendBooking.actCheckInAndNumOfNight.replaceAll(" ",""),resultData.hotel.amendBooking.expCheckInAndNumOfNight.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual check in and duration text Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.actPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
        //travellers
        assertionEquals(resultData.hotel.amendBooking.actTravellersText,resultData.hotel.amendBooking.expTravellersText, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")
        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedScndItemPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")
        //Commission
        assertionEquals(resultData.hotel.amendBooking.actCommissionTxt,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text Actual & Expected don't match")

    }

    protected def "VerifyCheckIfDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInValue,resultData.hotel.amendBooking.expectedCheckInValue,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutValue,resultData.hotel.amendBooking.expectedCheckOutValue,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")

        //a calendar opens
        assertionEquals(resultData.hotel.amendBooking.actualCalendarOpenStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Calendar open status Actual & Expected don't match")

        //calendar has check in date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInDaySelected,resultData.hotel.amendBooking.expectedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Selected Actual & Expected don't match")

        //calendar has check out date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutDaySelected,resultData.hotel.amendBooking.expectedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Selected Actual & Expected don't match")

        //today + 10 days highlighted as check in date
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckInDaySelected,resultData.hotel.amendBooking.expUpdatedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Updated Selected Actual & Expected don't match")

        //today + 11 days highlighted as check out date
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected,resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected Actual & Expected don't match")

        //Amend Booking - Dates tab should remains open
        assertionEquals(resultData.hotel.amendBooking.actDatesTabActiveStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Open status Actual & Expected don't match")

    }

    protected def "VerifyCheckIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInValue,resultData.hotel.amendBooking.expectedCheckInValue,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutValue,resultData.hotel.amendBooking.expectedCheckOutValue,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")
        //user presented message
        assertionEquals(resultData.hotel.amendBooking.actualAmendDateNoChangeErrTxt,data.expected.amndDateNotChangdErrTxt,"Amend popup Screen,  Dates tab, Amend Dates Not changed Erro Text Actual & Expected don't match")

        //a calendar opens
        assertionEquals(resultData.hotel.amendBooking.actualCalendarOpenStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Calendar open status Actual & Expected don't match")

        //calendar has check in date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInDaySelected,resultData.hotel.amendBooking.expectedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Selected Actual & Expected don't match")

        //calendar has check out date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutDaySelected,resultData.hotel.amendBooking.expectedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Selected Actual & Expected don't match")

        //today + 32 days highlighted as check out date
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected,resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected Actual & Expected don't match")

        //Amend Booking - Dates tab should remains open
        assertionEquals(resultData.hotel.amendBooking.actDatesTabActiveStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Open status Actual & Expected don't match")

    }

    protected def "VerifyChkIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInValue,resultData.hotel.amendBooking.expectedCheckInValue,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutValue,resultData.hotel.amendBooking.expectedCheckOutValue,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")

        //a calendar opens
        assertionEquals(resultData.hotel.amendBooking.actualCalendarOpenStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Calendar open status Actual & Expected don't match")

        //calendar has check in date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInDaySelected,resultData.hotel.amendBooking.expectedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Selected Actual & Expected don't match")

        //calendar has check out date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutDaySelected,resultData.hotel.amendBooking.expectedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Selected Actual & Expected don't match")

        //calendar has check in date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actualUpdatedCheckInDaySelected,resultData.hotel.amendBooking.expectedUpdatedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Updated Selected values Actual & Expected don't match")
        //calendar has check out date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actualUpdatedCheckOutDaySelected,resultData.hotel.amendBooking.expectedUpdatedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected values Actual & Expected don't match")

        //today + 63 days highlighted as check out date
        assertionEquals(resultData.hotel.amendBooking.actualUpdatedCheckOutDaySel,resultData.hotel.amendBooking.expectedUpdatedCheckOutDaySel,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected for 63 days Actual & Expected don't match")

        //Amend Booking - Dates tab should remains open
        assertionEquals(resultData.hotel.amendBooking.actDatesTabActiveStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Open status Actual & Expected don't match")

    }

    protected def "VerifyChkIfAllowedDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInValue,resultData.hotel.amendBooking.expectedCheckInValue,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutValue,resultData.hotel.amendBooking.expectedCheckOutValue,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")

        //a calendar opens
        assertionEquals(resultData.hotel.amendBooking.actualCalendarOpenStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Calendar open status Actual & Expected don't match")

        //calendar has check in date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckInDaySelected,resultData.hotel.amendBooking.expectedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Selected Actual & Expected don't match")

        //calendar has check out date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actualCheckOutDaySelected,resultData.hotel.amendBooking.expectedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Selected Actual & Expected don't match")

        //calendar has check in date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actualUpdatedCheckInDaySelected,resultData.hotel.amendBooking.expectedUpdatedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Updated Selected values Actual & Expected don't match")
        //calendar has check out date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actualUpdatedCheckOutDaySelected,resultData.hotel.amendBooking.expectedUpdatedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected values Actual & Expected don't match")

        //Amend Booking - Dates tab should remains open
        assertionEquals(resultData.hotel.amendBooking.actDatesTabActiveStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Open status Actual & Expected don't match")

    }

    protected def "VerifycheckingIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actChekInVal,resultData.hotel.amendBooking.expCheckInValue,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actChekOutVal,resultData.hotel.amendBooking.expCheckOutValue,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")

        //a calendar opens
        assertionEquals(resultData.hotel.amendBooking.actCalendarOpenStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Calendar open status Actual & Expected don't match")

        //calendar has check in date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actCheckInDaySelected,resultData.hotel.amendBooking.expCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Selected Actual & Expected don't match")

        //calendar has check out date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actCheckOutDaySelected,resultData.hotel.amendBooking.expCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Selected Actual & Expected don't match")

        //calendar has check in date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckInDaySelected,resultData.hotel.amendBooking.expUpdatedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Updated Selected values Actual & Expected don't match")
        //calendar has check out date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected,resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected values Actual & Expected don't match")

        //today + 63 days highlighted as check out date
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckOutDaySel,resultData.hotel.amendBooking.expUpdatedCheckOutDaySel,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected for 63 days Actual & Expected don't match")

        //Amend Booking - Dates tab should remains open
        assertionEquals(resultData.hotel.amendBooking.acDatesTabActiveStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Open status Actual & Expected don't match")

    }

    protected def "VerifychkingIfAllowDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actChekInVal,resultData.hotel.amendBooking.expCheckInValue,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actChekOutVal,resultData.hotel.amendBooking.expCheckOutValue,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")

        //a calendar opens
        assertionEquals(resultData.hotel.amendBooking.actCalendarOpenStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Calendar open status Actual & Expected don't match")

        //calendar has check in date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actCheckInDaySelected,resultData.hotel.amendBooking.expCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Selected Actual & Expected don't match")

        //calendar has check out date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actCheckOutDaySelected,resultData.hotel.amendBooking.expCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Selected Actual & Expected don't match")

        //calendar has check in date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckInDaySelected,resultData.hotel.amendBooking.expUpdatedCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Updated Selected values Actual & Expected don't match")
        //calendar has check out date highlighted per updated
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected,resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected values Actual & Expected don't match")

        //Amend Booking - Dates tab should remains open
        assertionEquals(resultData.hotel.amendBooking.acDatesTabActiveStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Open status Actual & Expected don't match")

    }

    protected def "VerifycheckIfAllowInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actChkInVal,resultData.hotel.amendBooking.expChkInVal,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per confirmed
        assertionEquals(resultData.hotel.amendBooking.actChkOutVal,resultData.hotel.amendBooking.expChkOutVal,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")

        //a calendar opens
        assertionEquals(resultData.hotel.amendBooking.actCalendarOpenStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Calendar open status Actual & Expected don't match")

        //calendar has check in date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actCheckInDaySelected,resultData.hotel.amendBooking.expCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Day Selected Actual & Expected don't match")

        //calendar has check out date highlighted per confimed
        assertionEquals(resultData.hotel.amendBooking.actCheckOutDaySelected,resultData.hotel.amendBooking.expCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Selected Actual & Expected don't match")

        //today + 37 days highlighted as check out date
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckOutDaySelected,resultData.hotel.amendBooking.expUpdatedCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Selected Actual & Expected don't match")

        //date (today + 38 days ) no longer highlighted
        assertionEquals(resultData.hotel.amendBooking.actUpdatedCheckOutDayNotSelected,resultData.hotel.amendBooking.expUpdatedCheckOutDayNotSelected,"Amend popup Screen,  Dates tab, Check Out Day Updated Not Selected Actual & Expected don't match")

        //Amend Booking - Dates tab should remains open
        assertionEquals(resultData.hotel.amendBooking.actDatesTabActiveStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, Open status Actual & Expected don't match")

    }


    protected def "VerifyCheckAvailabilityDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckAvailTxt,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckInTxt,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per changed
        assertionEquals(resultData.hotel.amendBooking.actCheckInValue,resultData.hotel.amendBooking.expUpdtdCheckInDaySelected,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per changed
        assertionEquals(resultData.hotel.amendBooking.actCheckOutValue,resultData.hotel.amendBooking.expUpdtdCheckOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actFindBtnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")


    }

    protected def "VerifyCheckAvailabilityInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        // check availability text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckAvailText,data.expected.chkAvailTxt, "Amend popup Screen,  Dates tab, Check Availability text Actual & Expected don't match")

        //Check-in text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckInText,data.expected.checkInTxt,"Amend popup Screen,  Dates tab, Check In text Actual & Expected don't match")

        //check in date should show per changed
        assertionEquals(resultData.hotel.amendBooking.actChkInValues,resultData.hotel.amendBooking.expUpdatdChkInDaySelected,"Amend popup Screen,  Dates tab, Check In Date Populated Actual & Expected don't match")

        //Check-Out text should show
        assertionEquals(resultData.hotel.amendBooking.actCheckOutTxt,data.expected.checkOutTxt,"Amend popup Screen,  Dates tab, Check Out text Actual & Expected don't match")

        //check out date should show per changed
        assertionEquals(resultData.hotel.amendBooking.actChkOutValues,resultData.hotel.amendBooking.expUpdatdChkOutDaySelected,"Amend popup Screen,  Dates tab, Check Out Date Populated Actual & Expected don't match")

        //Find function should be enabled
        assertionEquals(resultData.hotel.amendBooking.actFindButnStatus,data.expected.dispStatus,"Amend popup Screen,  Dates tab, find button enable status Actual & Expected don't match")


    }


    protected def "VerifyChangeAddAndRemove"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Change, add or remove occupants availability:
        assertionEquals(resultData.hotel.amendBooking.actualChangeOccupantsTxt,data.expected.changeOccupantsTxt, "Amend popup Screen,  Occupancy tab,  Change, add or remove occupants availability: text Actual & Expected don't match")

        //Occupancy 1
        assertionEquals(resultData.hotel.amendBooking.actualOccupantListNumTxt,resultData.hotel.amendBooking.expectedOccupantListNumTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Number 1 text Actual & Expected don't match")

        //Title / first name / last name
        assertionEquals(resultData.hotel.amendBooking.actualOccupantListNameTxt,resultData.hotel.itineraryPage.expectedleadTravellerName, "Amend popup Screen,  Occupancy tab,  Occupant List Name 1 text Actual & Expected don't match")

        //Occupancy 2
        assertionEquals(resultData.hotel.amendBooking.actualOccupantListNumTxt,resultData.hotel.amendBooking.expectedOccupantListNumTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Number 2 text Actual & Expected don't match")

        //Title / first name / last name
        assertionEquals(resultData.hotel.amendBooking.actualOccupantNameTxt,resultData.hotel.amendBooking.expectedOccupantNameTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Name 2 text Actual & Expected don't match")


        if(data.input.children.size()>0) {
            //Occupancy 3
            assertionEquals(resultData.hotel.amendBooking.actOccupantListNumTxt,resultData.hotel.amendBooking.expOccupantListNumTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Number 2 text Actual & Expected don't match")

            //Title / first name / last name
            assertionEquals(resultData.hotel.amendBooking.actOccupantNameTxt,resultData.hotel.amendBooking.expOccupantNameTxt, "Amend popup Screen,  Occupancy tab,  Occupant List Name 2 text Actual & Expected don't match")

        }
    }

    protected def "VerifySelectToAssign"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Please select occupants names:
        assertionEquals(resultData.hotel.amendBooking.actualPleaseSelectTxt,data.expected.plzSelTxt, "Amend popup Screen,  Occupancy tab,  Please select occupants names: text Actual & Expected don't match")

        if(!(data.input.multiroom==true)){
            //Get 1st traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualFirstTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, First Traveller CheckBox display status Actual & Expected don't match")
            //Get 2nd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualSecondTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")

        }

        if(data.input.children.size()>0 && (data.input.child==true)){
            //Get 4th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Child Traveller CheckBox display status Actual & Expected don't match")

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Child 3rd Traveller CheckBox display status Actual & Expected don't match")
            //Get 4th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualFourthTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Child 4th Traveller CheckBox display status Actual & Expected don't match")
        }

        if(!(data.input.multiroom==true)) {
            //lead traveller
            assertionEquals(resultData.hotel.amendBooking.actualLeadTravlr, resultData.hotel.amendBooking.expectedLeadTravlrTxt, "Amend popup Screen, Occupancy tab, Lead Traveller Text Actual & Expected don't match")
        }

        if(data.input.children.size()>0 && (data.input.infant==true)){
            //infant text should show - if it is available for the room
            assertionEquals(resultData.hotel.amendBooking.actualInfantLabTxt,data.expected.infantLabelTxt+"s", "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

        }

        else{
            //infant text should show - if it is available for the room
            assertionEquals(resultData.hotel.amendBooking.actualInfantLabTxt,data.expected.infantLabelTxt, "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

        }

        //dropdown list to select number of infant should show
        assertionEquals(resultData.hotel.amendBooking.actualInfantDropdownListDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")
        //list should show 0 - 1
        assertionListEquals(resultData.hotel.amendBooking.actualListInfantValues,data.expected.infantDropDownListValues,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")
        //Confirm Amendment Button display status
        assertionEquals(resultData.hotel.amendBooking.actualConfirmAmendmentButtonStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button display status Actual & Expected don't match")

        //Confirm Amendment Button Disabled status
        assertionEquals(resultData.hotel.amendBooking.actualConfirmAmendmentDisabledStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Disabled status Actual & Expected don't match")


    }

    protected def "Verifyadding1adultpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        //page refreshed to show all the details. this validation is covered in test cases 10 and 11
    }

    protected def "Verifyadding1childpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        //page refreshed to show all the details. this validation is covered in test cases 10 and 11
    }

    protected def "Verifyadd2Infant"(HotelSearchData data, HotelTransferTestResultData resultData){

        //page refreshed to show all the details. this validation is covered in test cases 10 and 11
    }


    protected def "VerifycurrentBookingDetailsShouldRetain"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Current booking:  text should show
        assertionEquals(resultData.hotel.amendBooking.actualOccupTxt,data.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking after first amendment text Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInOccupancyTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text after first amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualroomdescTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.actualmealBasisTxt,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actualfreeCancelTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInAndNumOfNight,resultData.hotel.amendBooking.expectedCheckInAndNumOfNight, "Amend popup Screen,  Occupancy tab,  actual check in and duration text after first amendment Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.child==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actPaxTxt.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text after first amendment Actual & Expected don't match")

        }else if(data.input.children.size()>0 && (data.input.infant==true)){
             //number of pax
            assertionEquals(resultData.hotel.amendBooking.actPaxTxt.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text after first amendment Actual & Expected don't match")

        }
        else if(data.input.multiroom==true) {
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actPaxTxt.replaceAll(" ",""), data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
        }

            else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actPaxTxt.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text after first amendment Actual & Expected don't match")

        }


        //travellers
        assertionEquals(resultData.hotel.amendBooking.actualTravellersText,resultData.hotel.amendBooking.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text after first amendment Actual & Expected don't match")

        if(data.input.multiroom==true){
            //Room Amount and Currency
            assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedScndItemPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

        }else{
            //Room Amount and Currency
            assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.searchResults.itineraryBuilder.expectedPrice, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text after first amendment Actual & Expected don't match")

        }


        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionTxt,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text after first amendment Actual & Expected don't match")

    }

    protected def "VerifyChangeTo"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTxt,data.expected.changeToTxt, "Amend popup Screen,  Occupancy tab,  Change To text after first amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInOccupTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, Change to section, item name text after first amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, Change to section, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.actualmealBasisText,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, Change to section, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actualfreeCancellationTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Change to section, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInPlusNumOfNight,resultData.hotel.amendBooking.expCheckInPlusNumOfNight, "Amend popup Screen,  Occupancy tab, Change to section, actual check in and duration text after first amendment Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.child==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxTxt.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text after first amendment Actual & Expected don't match")

        }else if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxTxt.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        }
        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxTxt.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        }

        //travellers
        assertionEquals(resultData.hotel.amendBooking.actualTravellersTxt.replaceAll(" ",""),resultData.hotel.amendBooking.expTravellersTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,  Travellers text after first amendment Actual & Expected don't match")

        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.expectedPriceDiff, "Amend popup Screen,  Occupancy tab, Change to section,  Amount Rate change and currency after first amendment Actual & Expected don't match")

        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Total Amount And Currency text after first amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Commission Percent text after first amendment Actual & Expected don't match")

        //confirmation status(inventory)
        //assertionEquals(resultData.hotel.amendBooking.actualConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Inventory Status after first amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button enable status Actual & Expected don't match")

    }

    protected def "VerifyChangeToModified"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTxt,data.expected.changeToTxt, "Amend popup Screen,  Occupancy tab,  Change To text after first amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInOccupTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, Change to section, item name text after first amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, Change to section, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.actualmealBasisText,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, Change to section, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        //assertionEquals(resultData.hotel.amendBooking.actualfreeCancellationTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Change to section, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInPlusNumOfNight,resultData.hotel.amendBooking.expCheckInPlusNumOfNight, "Amend popup Screen,  Occupancy tab, Change to section, actual check in and duration text after first amendment Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.child==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxTxt.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text after first amendment Actual & Expected don't match")

        }else if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxTxt.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        }
        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.actualPaxTxt.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        }

        //travellers
        assertionEquals(resultData.hotel.amendBooking.actualTravellersTxt.replaceAll(" ",""),resultData.hotel.amendBooking.expTravellersTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,  Travellers text after first amendment Actual & Expected don't match")

        //amount rate change and currency
        //assertionEquals(resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.expectedPriceDiff, "Amend popup Screen,  Occupancy tab, Change to section,  Amount Rate change and currency after first amendment Actual & Expected don't match")

        //Room Amount and Currency
        //assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Total Amount And Currency text after first amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Commission Percent text after first amendment Actual & Expected don't match")

        //confirmation status(inventory)
        //assertionEquals(resultData.hotel.amendBooking.actualConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Inventory Status after first amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button enable status Actual & Expected don't match")

    }


    protected def "VerifyChangeToInDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.actualChangeToTxt,data.expected.changeToTxt, "Amend popup Screen,  Dates tab,  Change To text after first amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInDatesTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Dates tab, Change to section, item name text after first amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Dates tab, Change to section, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.actualmealBasisText,data.expected.mealBasisTxt, "Amend popup Screen,  Dates tab, Change to section, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actualfreeCancellationTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Dates tab, Change to section, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in check out, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInPlusNumOfNight,resultData.hotel.amendBooking.expChkInChkOutPlusNumOfNight, "Amend popup Screen,  Dates tab, Change to section, actual check in and duration text after first amendment Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.actualPaxTxt.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Dates tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        //travellers
        assertionEquals(resultData.hotel.amendBooking.actualTravellersTxt.replaceAll(" ",""),resultData.hotel.amendBooking.expTravellersTxt.replaceAll(" ",""), "Amend popup Screen,  Dates tab, Change to section,  Travellers text after first amendment Actual & Expected don't match")

        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.actualpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.expectedPriceDiff, "Amend popup Screen,  Dates tab, Change to section,  Amount Rate change and currency after first amendment Actual & Expected don't match")

        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Dates tab, Change to section,  Total Amount And Currency text after first amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Dates tab, Change to section,  Commission Percent text after first amendment Actual & Expected don't match")

        //confirmation status(inventory)
        //assertionEquals(resultData.hotel.amendBooking.actualConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Dates tab, Change to section,  Inventory Status after first amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab, Change to section, Ok button enable status Actual & Expected don't match")

    }

    protected def "VerifyChangeToDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.actChangeToTxt,data.expected.changeToTxt, "Amend popup Screen,  Dates tab,  Change To text after first amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.actItemNameInDatesTab,data.expected.cityAreaHotelTxt, "Amend popup Screen,  Dates tab, Change to section, item name text after first amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Dates tab, Change to section, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.actmealBasisText,data.expected.mealBasisText, "Amend popup Screen,  Dates tab, Change to section, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actfreeCancelTxt,resultData.hotel.amendBooking.expFreeCancelText, "Amend popup Screen,  Dates tab, Change to section, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in check out, number of night
        assertionEquals(resultData.hotel.amendBooking.actChkInAndNumOfNight,resultData.hotel.amendBooking.expChkInChkOutAndNumOfNight, "Amend popup Screen,  Dates tab, Change to section, actual check in and duration text after first amendment Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.actPaxTxt.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Dates tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        //travellers
        assertionEquals(resultData.hotel.amendBooking.actTravellersTxt,resultData.hotel.amendBooking.expTravellersTxt, "Amend popup Screen,  Dates tab, Change to section,  Travellers text after first amendment Actual & Expected don't match")

        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.actpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.expPriceDiff, "Amend popup Screen,  Dates tab, Change to section,  Amount Rate change and currency after first amendment Actual & Expected don't match")

        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Dates tab, Change to section,  Total Amount And Currency text after first amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.actCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Dates tab, Change to section,  Commission Percent text after first amendment Actual & Expected don't match")

        //confirmation status(inventory)
        assertionEquals(resultData.hotel.amendBooking.actConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Dates tab, Change to section,  Inventory Status after first amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.actOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.actOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab, Change to section, Ok button enable status Actual & Expected don't match")

    }

    protected def "VerifychngToDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.actChangeToTxt,data.expected.changeToTxt, "Amend popup Screen,  Dates tab,  Change To text after first amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.actItemNameInDatesTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Dates tab, Change to section, item name text after first amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Dates tab, Change to section, room num and type text after first amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.actmealBasisText,data.expected.mealBasisText, "Amend popup Screen,  Dates tab, Change to section, meal basis text after first amendment Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actfreeCancelTxt,resultData.hotel.amendBooking.expFreeCancelText, "Amend popup Screen,  Dates tab, Change to section, Free Cancellation text after first amendment Actual & Expected don't match")

        //check in check out, number of night
        assertionEquals(resultData.hotel.amendBooking.actChkInAndNumOfNight,resultData.hotel.amendBooking.expChkInChkOutAndNumOfNight, "Amend popup Screen,  Dates tab, Change to section, actual check in and duration text after first amendment Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.actPaxTxt,data.expected.modifiedpaxTxt, "Amend popup Screen,  Dates tab, Change to section,   actual Pax text after first amendment Actual & Expected don't match")

        //travellers
        assertionEquals(resultData.hotel.amendBooking.actTravellersTxt,resultData.hotel.amendBooking.expTravllersTxt, "Amend popup Screen,  Dates tab, Change to section,  Travellers text after first amendment Actual & Expected don't match")

        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.actpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.expPriceDiff, "Amend popup Screen,  Dates tab, Change to section,  Amount Rate change and currency after first amendment Actual & Expected don't match")

        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Dates tab, Change to section,  Total Amount And Currency text after first amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.actCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Dates tab, Change to section,  Commission Percent text after first amendment Actual & Expected don't match")

        //confirmation status(inventory)
        assertionEquals(resultData.hotel.amendBooking.actConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Dates tab, Change to section,  Inventory Status after first amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.actOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.actOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Dates tab, Change to section, Ok button enable status Actual & Expected don't match")

    }


    protected def "VerifySelectToAssignAfterFirstAmend"(HotelSearchData data, HotelTransferTestResultData resultData){
        //Please select occupants names:
        assertionEquals(resultData.hotel.amendBooking.actualPleaseSelectTxt,data.expected.plzSelTxt, "Amend popup Screen,  Occupancy tab,  Please select occupants names: text Actual & Expected don't match")
        if(!(data.input.multiroom==true)){
            //Get 1st traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualFirstTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, First Traveller CheckBox display status Actual & Expected don't match")
            //Get 2nd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualSecondTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")

        }

        if(data.input.children.size()>0 && (data.input.child==true)){
            //Get 4th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualFourthTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Child 3rd Traveller CheckBox display status Actual & Expected don't match")
            //Get 4th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actualFourthTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Child 4th Traveller CheckBox display status Actual & Expected don't match")
        }
        if(!(data.input.multiroom==true)) {
            //lead traveller
            assertionEquals(resultData.hotel.amendBooking.actualLeadTravlr, resultData.hotel.amendBooking.expectedLeadTravlrTxt, "Amend popup Screen, Occupancy tab, Lead Traveller Text Actual & Expected don't match")
        }
        //infant value displaying = 2
        if(data.input.children.size()>0 && (data.input.infant==true)){
            //infant value displaying = 2
            assertionEquals(resultData.hotel.amendBooking.actualSelInfantVal,data.expected.infantDropDownListValues.getAt(2), "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")
            //infant text should show - if it is available for the room
            assertionEquals(resultData.hotel.amendBooking.actInfntLabTxt,data.expected.infantLabelTxt, "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

        }else{
            //infant text should show - if it is available for the room
            assertionEquals(resultData.hotel.amendBooking.actInfntLabTxt,data.expected.infantLabelTxt, "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

        }

        //dropdown list to select number of infant should show
        assertionEquals(resultData.hotel.amendBooking.actualInfantDropdownListDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")
        //list should show 0 - 1
        assertionListEquals(resultData.hotel.amendBooking.actualListInfantValues,data.expected.infantDropDownListValues,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")
        //Confirm Amendment Button display status
        assertionEquals(resultData.hotel.amendBooking.actualConfirmAmendmentButtonStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button display status Actual & Expected don't match")

        //Confirm Amendment Button Disabled status
        assertionEquals(resultData.hotel.amendBooking.actualConfirmAmendmentDisabledStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Disabled status Actual & Expected don't match")


    }

    protected def "VerifyaddinganotheradditionaladultpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        //page refreshed to show all the details. this validation is covered in test cases 14 and 15
    }
    protected def "VerifyChangeToAfterSecondAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.actualOccupancyTxt,data.expected.changeToTxt, "Amend popup Screen,  Occupancy tab,  Change To text after second amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.actualItemNameInOccupTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, Change to section, item name text after second amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.actualNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, Change to section, room num and type text after second amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.actualmealBasisText,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, Change to section, meal basis text after second amendment Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.actualfreeCancellationTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Change to section, Free Cancellation text after second amendment Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.actualCheckInPlusNumOfNight,resultData.hotel.amendBooking.expCheckInPlusNumOfNight, "Amend popup Screen,  Occupancy tab, Change to section, actual check in and duration text after second amendment Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.actPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after second amendment Actual & Expected don't match")

        //travellers
        assertionEquals(resultData.hotel.amendBooking.actTravlrTxt,resultData.hotel.amendBooking.expTravlrTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Travellers text after second amendment Actual & Expected don't match")

        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.actpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.expPriceDiff, "Amend popup Screen,  Occupancy tab, Change to section,  Amount Rate change and currency after second amendment Actual & Expected don't match")

        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Total Amount And Currency text after second amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.actualCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Commission Percent text after second amendment Actual & Expected don't match")

        //confirmation status(inventory)
        //assertionEquals(resultData.hotel.amendBooking.actualConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Inventory Status after second amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.actualOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button enable status Actual & Expected don't match")

    }

    protected def "VerifyselectToAssignAfterSecondAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Please select occupants names:
        assertionEquals(resultData.hotel.amendBooking.actualPleaseSelectTxt,data.expected.plzSelTxt, "Amend popup Screen,  Occupancy tab,  Please select occupants names: text Actual & Expected don't match")

        if(data.input.multiroom==true){
            //Get 1st traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actFirstTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, First Traveller CheckBox display status Actual & Expected don't match")
            //Get 2nd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actSecondTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")

        }else{
            //Get 1st traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actFirstTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, First Traveller CheckBox display status Actual & Expected don't match")
            //Get 2nd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actSecondTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")
            //Get 4th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.actFourthTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")

        }


        //lead traveller
        assertionEquals(resultData.hotel.amendBooking.actualLeadTravlr,resultData.hotel.amendBooking.expectedLeadTravlrTxt, "Amend popup Screen, Occupancy tab, Lead Traveller Text Actual & Expected don't match")

        if(data.input.children.size()>0 && (data.input.infant==true)) {
            //infant text should show - if it is available for the room
            assertionEquals(resultData.hotel.amendBooking.actualInfantLabTxt,data.expected.infantLabelTxt+"s", "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")
            //infant value displaying = 2
            assertionEquals(resultData.hotel.amendBooking.actSelectedInfantValue,data.expected.infantDropDownListValues.getAt(2), "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")

        }else{
            //infant text should show - if it is available for the room
            assertionEquals(resultData.hotel.amendBooking.actualInfantLabTxt,data.expected.infantLabelTxt, "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

        }


        //dropdown list to select number of infant should show
        assertionEquals(resultData.hotel.amendBooking.actualInfantDropdownListDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")
        //list should show 0-1
        if(data.input.multiroom==true){
            assertionEquals(resultData.hotel.amendBooking.actListInfantValues,data.expected.infantDropDownListVal_FirstRoom,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")

        }else{
            assertionEquals(resultData.hotel.amendBooking.actListInfantValues,data.expected.infantDropDownListValues,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")

        }

        //Confirm Amendment Button display status
        assertionEquals(resultData.hotel.amendBooking.actualConfirmAmendmentButtonStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button display status Actual & Expected don't match")

        //Confirm Amendment Button Disabled status
        assertionEquals(resultData.hotel.amendBooking.actualConfirmAmendmentDisabledStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Disabled status Actual & Expected don't match")

    }

    protected def "VerifyacceptingChange"(HotelSearchData data, HotelTransferTestResultData resultData){

        //user taken to t & c page
        assertionEquals(resultData.hotel.amendBooking.actualTAndCDispStatus,data.expected.dispStatus, "About To Amend Screen display status Actual & Expected don't match")

    }

    protected def "VerifyacceptingChanges"(HotelSearchData data, HotelTransferTestResultData resultData){

        //user taken to t & c page
        assertionEquals(resultData.hotel.amendBooking.actTAndCDispStatus,data.expected.dispStatus, "About To Amend Screen display status Actual & Expected don't match")

    }

    protected def "VerifyTAndCpagereconfirmation"(HotelSearchData data, HotelTransferTestResultData resultData){

        //capture title text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualtitleTxt,data.expected.amendmentTitleTxt, "About to Amend screen, title text Actual & Expected don't match")

        //Capture Close Icon display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCloseIconDispStatus,data.expected.dispStatus, "About to Amend screen, Close Icon display status Actual & Expected don't match")

        //Capture hotel name in About to Amend Screen
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHotelNameInAbtToAmndScrn,data.expected.cityAreaHotelText, "About to Amend screen, Hotel Name text Actual & Expected don't match")

        //number of room/name of room (descripton)
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText,resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt, "About to Amend screen, number of room/name of room (descripton) text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualmealBasisTxt,data.expected.mealBasisTxt, "About to Amend screen, meal basis text Actual & Expected don't match")


       if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text  actual Pax text after first amendment Actual & Expected don't match")

        }
       else if(data.input.multiroom==true) {
           //number of pax
           assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""), data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
       }

        else{
            //number of pax
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")
           assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""),data.expected.scndAmndPaxText.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")

        }


        /*
        if(data.input.children.size()>0) {
            //travellers
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellersText.replaceAll(" ",""),resultData.hotel.amendBooking.expTravellersTxt.replaceAll(" ",""), "About to Amend screen,  Travellers text  Actual & Expected don't match")

        }else if(data.input.multiroom==true) {

            //travellers
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellersText,resultData.hotel.amendBooking.occupTab.expTravellersTxt,"About to Amend screen,  Travellers text  Actual & Expected don't match")

        }
        else {
            //travellers
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellersText,resultData.hotel.amendBooking.expTravlrTxt, "About to Amend screen,  Travellers text  Actual & Expected don't match")

        }

        */

        //check out Date
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCheckOutDateAndText,resultData.hotel.amendBooking.occupTab.expectedCheckOutDateAndText, "About to Amend screen, check out date text Actual & Expected don't match")

        //check in date, number of night
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNight.replaceAll(" ",""), "About to Amend screen, check in and duration text Actual & Expected don't match")

        assertionEquals(resultData.hotel.amendBooking.occupTab.actualNumOfNight.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expectedNumOfNight.replaceAll(" ",""), "About to Amend screen, Nights text Actual & Expected don't match")



        //status
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualInvStatusInAbtToAmendScrn,data.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")

        if(data.input.children.size()>0) {
            //amount rate change and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

            //total room amount & currency code
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        }else if(data.input.multiroom==true) {
            //amount rate change and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

            //total room amount & currency code
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        }
        else{
            //amount rate change and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

            //total room amount & currency code
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        }


        //commission & % value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn,data.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")

        //special condition - header text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualSpecialConditionHeaderTxt,resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt, "About to Amend screen, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualPlzNoteTxt,data.expected.plzNoteTxt, "About to Amend screen,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "About to Amend screen, Cancelation Charge header text Actual & Expected don't match")

        //Cancellation charge descriptive text - need to modify
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "About to Amend screen, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "About to Amend screen,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualDatesTxt,data.expected.allDatesTxt, "About to Amend screen,  All Dates text Actual & Expected don't match")

        //Total
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalTextInAboutToBook,data.expected.totalTxt, "About to Amend screen,  total text Actual & Expected don't match")


        if(data.input.children.size()>0) {
            //Total Amount and currency
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        }else if(data.input.multiroom==true){
            //Total Amount and currency
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        }
        else{
            //Total Amount and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        }

        //Your Commission text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCommissionTextInAboutToAmend,data.expected.cmsnTxt, "About to Amend screen,  Your Commission text Actual & Expected don't match")

        //Your commission amount and currency
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend,resultData.hotel.amendBooking.occupTab.expCommissionValueInAboutToAmend, "About to Amend screen,  Your Commission Amount and Currency Actual & Expected don't match")

        //< Confirm Amendment > button display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualconfirmButtonDispStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button display status Actual & Expected don't match")

        //confirm Amendment enable status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualconfirmAmendEnableStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button Enable status Actual & Expected don't match")

        //T&C agreement text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualTermsAndCondtTxt,data.expected.TermsAndCondtnsFooterTxt, "About to Amend screen, By clicking Footer Text Actual & Expected don't match")

        //Confirm booking page display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualconfirmbookStatus,data.expected.dispStatus, "After Amend Booking Confirmation Screen display status Actual & Expected don't match")



    }

    protected def "VerifyTAndCpagereconfirmationModified"(HotelSearchData data, HotelTransferTestResultData resultData){

        //capture title text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualtitleTxt,data.expected.amendmentTitleTxt, "About to Amend screen, title text Actual & Expected don't match")

        //Capture Close Icon display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCloseIconDispStatus,data.expected.dispStatus, "About to Amend screen, Close Icon display status Actual & Expected don't match")

        //Capture hotel name in About to Amend Screen
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHotelNameInAbtToAmndScrn,data.expected.cityAreaHotelText, "About to Amend screen, Hotel Name text Actual & Expected don't match")

        //number of room/name of room (descripton)
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualnumAndNameofRoomText,resultData.hotel.amendBooking.occupTab.expectedRoomNumAndTypeOfRoomTxt, "About to Amend screen, number of room/name of room (descripton) text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualmealBasisTxt,data.expected.mealBasisTxt, "About to Amend screen, meal basis text Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text  actual Pax text after first amendment Actual & Expected don't match")

        }
        else if(data.input.multiroom==true) {
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""), data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")
        }

        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")

        }


        if(data.input.children.size()>0) {
            //travellers
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellersText.replaceAll(" ",""),resultData.hotel.amendBooking.expTravellersTxt.replaceAll(" ",""), "About to Amend screen,  Travellers text  Actual & Expected don't match")

        }else if(data.input.multiroom==true) {

            //travellers
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellersText,resultData.hotel.amendBooking.occupTab.expTravellersTxt,"About to Amend screen,  Travellers text  Actual & Expected don't match")

        }
        else {
            //travellers
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellersText,resultData.hotel.amendBooking.expTravlrTxt, "About to Amend screen,  Travellers text  Actual & Expected don't match")

        }


        //check in date, number of night
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCheckInAndNumOfNight.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNight.replaceAll(" ",""), "About to Amend screen, check in and duration text Actual & Expected don't match")

        //status
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualInvStatusInAbtToAmendScrn,data.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")

        if(data.input.children.size()>0) {
            //amount rate change and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

            //total room amount & currency code
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        }else if(data.input.multiroom==true) {
            //amount rate change and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

            //total room amount & currency code
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        }
        else{
            //amount rate change and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

            //total room amount & currency code
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        }


        //commission & % value
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualcommissionPercentAbtToAmendScrn,data.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")

        //special condition - header text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualSpecialConditionHeaderTxt,resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt, "About to Amend screen, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualPlzNoteTxt,data.expected.plzNoteTxt, "About to Amend screen,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "About to Amend screen, Cancelation Charge header text Actual & Expected don't match")

        //Cancellation charge descriptive text - need to modify
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "About to Amend screen, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "About to Amend screen,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualDatesTxt,data.expected.allDatesTxt, "About to Amend screen,  All Dates text Actual & Expected don't match")

        //Total
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalTextInAboutToBook,data.expected.totalTxt, "About to Amend screen,  total text Actual & Expected don't match")


        if(data.input.children.size()>0) {
            //Total Amount and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.expectedTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        }else if(data.input.multiroom==true){
            //Total Amount and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.actualTotalRoomAmntAndCurrncyText, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        }
        else{
            //Total Amount and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        }

        //Your Commission text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCommissionTextInAboutToAmend,data.expected.cmsnTxt, "About to Amend screen,  Your Commission text Actual & Expected don't match")

        //Your commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCommissionValueInAboutToAmend,resultData.hotel.amendBooking.occupTab.expCommissionValueInAboutToAmend, "About to Amend screen,  Your Commission Amount and Currency Actual & Expected don't match")

        //< Confirm Amendment > button display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualconfirmButtonDispStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button display status Actual & Expected don't match")

        //confirm Amendment enable status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualconfirmAmendEnableStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button Enable status Actual & Expected don't match")

        //T&C agreement text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualTermsAndCondtTxt,data.expected.TermsAndCondtnsFooterTxt, "About to Amend screen, By clicking Footer Text Actual & Expected don't match")

        //Confirm booking page display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualconfirmbookStatus,data.expected.dispStatus, "After Amend Booking Confirmation Screen display status Actual & Expected don't match")



    }


    protected def "VerifyTAndCpgreconfrmtnFromDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //capture title text
        assertionEquals(resultData.hotel.amendBooking.occupTab.acttitleTxt,data.expected.amendmentTitleTxt, "About to Amend screen, title text Actual & Expected don't match")

        //Capture Close Icon display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCloseIconDispStatus,data.expected.dispStatus, "About to Amend screen, Close Icon display status Actual & Expected don't match")

        //Capture hotel name in About to Amend Screen
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHotelNameInAbtToAmndScrn,data.expected.cityAreaHotelTxt, "About to Amend screen, Hotel Name text Actual & Expected don't match")

        //number of room/name of room (descripton)
        assertionEquals(resultData.hotel.amendBooking.occupTab.actnumAndNameofRoomText,resultData.hotel.amendBooking.occupTab.expRoomNumAndTypeOfRoomTxt, "About to Amend screen, number of room/name of room (descripton) text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.occupTab.actmealBasisTxt,data.expected.mealBasisText, "About to Amend screen, meal basis text Actual & Expected don't match")

            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""),data.expected.scndAmndPaxText.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")

            //travellers
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actTravellersText,resultData.hotel.amendBooking.occupTab.expTravlrsTxt, "About to Amend screen,  Travellers text  Actual & Expected don't match")

        //check in date, number of night
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCheckInAndNumOfNights.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNights.replaceAll(" ",""), "About to Amend screen, check in and duration text Actual & Expected don't match")

        //check out Date
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCheckOutDateAndText.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expCheckOutDateAndText.replaceAll(" ",""), "About to Amend screen, check out Date text Actual & Expected don't match")

        //No. of Nights
        assertionEquals(resultData.hotel.amendBooking.occupTab.actNumOfNight.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expNumOfNight.replaceAll(" ",""), "About to Amend screen, No Of Nights Text Actual & Expected don't match")

        //status
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actInvStatusInAbtToAmendScrn,data.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")


            //amount rate change and currency
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

            //total room amount & currency code
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        //commission & % value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actcommissionPercentAbtToAmendScrn,data.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")

        //special condition - header text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actSpecialConditionHeaderTxt,resultData.hotel.amendBooking.occupTab.expSpConditionHeaderTxt, "About to Amend screen, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actPlzNoteTxt,data.expected.plzNoteTxt, "About to Amend screen,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCancellationChargeTxt,data.expected.cancelChargeTxt, "About to Amend screen, Cancelation Charge header text Actual & Expected don't match")

        //Cancellation charge descriptive text - need to modify
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "About to Amend screen, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "About to Amend screen,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actDatesTxt,data.expected.allDatesTxt, "About to Amend screen,  All Dates text Actual & Expected don't match")

        //Total
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTotalTextInAboutToBook,data.expected.totalTxt, "About to Amend screen,  total text Actual & Expected don't match")

            //Total Amount and currency
            assertionEquals(resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency,resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        //Your Commission text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCommissionTextInAboutToAmend,data.expected.cmsnTxt, "About to Amend screen,  Your Commission text Actual & Expected don't match")

        //Your commission amount and currency
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actComsnValueInAboutToAmend,resultData.hotel.amendBooking.occupTab.expComsnValueInAboutToAmend, "About to Amend screen,  Your Commission Amount and Currency Actual & Expected don't match")

        //< Confirm Amendment > button display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actconfirmButtonDispStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button display status Actual & Expected don't match")

        //confirm Amendment enable status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actconfirmAmendEnableStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button Enable status Actual & Expected don't match")

        //T&C agreement text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTermsAndCondtTxt,data.expected.TermsAndCondtnsFooterTxt, "About to Amend screen, By clicking Footer Text Actual & Expected don't match")

        //Confirm booking page display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actconfirmbookStatus,data.expected.dispStatus, "After Amend Booking Confirmation Screen display status Actual & Expected don't match")



    }

    protected def "VerifyTAndCpgreconfrmatnFromDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //capture title text
        assertionEquals(resultData.hotel.amendBooking.occupTab.acttitleTxt,data.expected.amendmentTitleTxt, "About to Amend screen, title text Actual & Expected don't match")

        //Capture Close Icon display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCloseIconDispStatus,data.expected.dispStatus, "About to Amend screen, Close Icon display status Actual & Expected don't match")

        //Capture hotel name in About to Amend Screen
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHotelNameInAbtToAmndScrn,data.expected.cityAreaHotelText, "About to Amend screen, Hotel Name text Actual & Expected don't match")

        //number of room/name of room (descripton)
        assertionEquals(resultData.hotel.amendBooking.occupTab.actnumAndNameofRoomText,resultData.hotel.amendBooking.occupTab.expRoomNumAndTypeOfRoomTxt, "About to Amend screen, number of room/name of room (descripton) text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.occupTab.actmealBasisTxt,data.expected.mealBasisTxt, "About to Amend screen, meal basis text Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")

        //travellers
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTravellersText,resultData.hotel.amendBooking.occupTab.expTravlrsTxt, "About to Amend screen,  Travellers text  Actual & Expected don't match")

        //check in date, number of night
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCheckInAndNumOfNights.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expCheckInAndNumOfNights.replaceAll(" ",""), "About to Amend screen, check in and duration text Actual & Expected don't match")

        //status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actInvStatusInAbtToAmendScrn,data.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")


        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

        //total room amount & currency code
        assertionEquals(resultData.hotel.amendBooking.occupTab.actRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        //commission & % value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actcommissionPercentAbtToAmendScrn,data.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")

        //special condition - header text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actSpecialConditionHeaderTxt,resultData.hotel.amendBooking.occupTab.expSpConditionHeaderTxt, "About to Amend screen, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actPlzNoteTxt,data.expected.plzNoteTxt, "About to Amend screen,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCancellationChargeTxt,data.expected.cancelChargeTxt, "About to Amend screen, Cancelation Charge header text Actual & Expected don't match")

        //Cancellation charge descriptive text - need to modify
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "About to Amend screen, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "About to Amend screen,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actDatesTxt,data.expected.allDatesTxt, "About to Amend screen,  All Dates text Actual & Expected don't match")

        //Total
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTotalTextInAboutToBook,data.expected.totalTxt, "About to Amend screen,  total text Actual & Expected don't match")

        //Total Amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTotalAmountAndCurrency,resultData.hotel.amendBooking.actTotalRoomAmntAndCurrncyText, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        //Your Commission text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCommissionTextInAboutToAmend,data.expected.cmsnTxt, "About to Amend screen,  Your Commission text Actual & Expected don't match")

        //Your commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actComsnValueInAboutToAmend,resultData.hotel.amendBooking.occupTab.expComsnValueInAboutToAmend, "About to Amend screen,  Your Commission Amount and Currency Actual & Expected don't match")

        //< Confirm Amendment > button display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actconfirmButtonDispStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button display status Actual & Expected don't match")

        //confirm Amendment enable status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actconfirmAmendEnableStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button Enable status Actual & Expected don't match")

        //T&C agreement text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTermsAndCondtTxt,data.expected.TermsAndCondtnsFooterTxt, "About to Amend screen, By clicking Footer Text Actual & Expected don't match")

        //Confirm booking page display status
        assertionEquals(resultData.hotel.amendBooking.occupTab.actconfirmbookStatus,data.expected.dispStatus, "After Amend Booking Confirmation Screen display status Actual & Expected don't match")



    }


    protected def "VerifybookingConfirming"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking confirmed text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt, "After Amend, Booking Confirmation Screen, title text Actual & Expected don't match")
        //Close X function
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCloseBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Close X function display status Actual & Expected don't match")
        //A confirmation of your booking has been email to:
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHeaderSectionText,resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText, "After Amend Booking Confirmation, Email Text Actual & Expected don't match")

        //brand & icon
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualBrandAndIconDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Brand and icon display status Actual & Expected don't match")
        //Booking ID: & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualbookingID,resultData.hotel.amendBooking.occupTab.expectedBookingID, "After Amend Booking Confirmation, Booking ID: & Value text Actual & Expected don't match")
        //Itinerary ID,  Itinerary name & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualitinearyIDAndName,resultData.hotel.itineraryPage.readitinearyIDAndName, "After Amend Booking Confirmation, Itinerary ID: & Value text Actual & Expected don't match")

        //Departure date: text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualDepDateTxt,data.expected.departDateTxt, "After Amend Booking Confirmation, Departure Date text Actual & Expected don't match")
        //Departure Date value text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualDepDateValTxt,resultData.hotel.amendBooking.occupTab.expectedDepDateValTxt, "After Amend Booking Confirmation, Departure Date Value text Actual & Expected don't match")

        //Traveller Details & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellerDetailsTxt,data.expected.travellerDeetailsTxt, "After Amend Booking Confirmation, Traveller Details text Actual & Expected don't match")

        List actualTravellerData=resultData.hotel.confirmationPage.travellers.get("actualTravellers")
        List expectedTravelerData=resultData.hotel.confirmationPage.travellers.get("expectedTravellers")
        //Validate  travellers
        assertionListEquals(actualTravellerData,expectedTravelerData,"After Amend Booking Confirmation, List Of Travellers  actual & expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHotelName,resultData.hotel.amendBooking.occupTab.expectedItemName, "After Amend Booking Confirmation, Item Name text Actual & Expected don't match")

        //item address
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHotelAddressTxt,resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt, "After Amend Booking Confirmation, Item Address text Actual & Expected don't match")

        //number of room, room type & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomNumAndTypeTxt,resultData.hotel.amendBooking.occupTab.expectedRoomNumAndroomValueTxt, "After Amend Booking Confirmation, Room Num And Type text Actual & Expected don't match")

        //Capture PAX
        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Capture PAX
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
            assertContains(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        }else{
            //Capture PAX
            //assertionEquals(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
            //assertContains(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.actualPaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
            assertContains(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
        }

        //Meal basis & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualMealBasisTxt,resultData.hotel.amendBooking.occupTab.actualmealBasisTxt, "After Amend Booking Confirmation, Meal Basis text Actual & Expected don't match")

        //Check in date, Number of Nights
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCheckInDateNumOfNights,resultData.hotel.amendBooking.occupTab.expectedCheckInDate, "After Amend Booking Confirmation, Check in Date and num of nights text Actual & Expected don't match")

        //room rate amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomRateAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expectedRoomRateAmntAndCurrncy, "After Amend Booking Confirmation, Room Rate Amnt & Currency text Actual & Expected don't match")

        //commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCommissionAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expectedCommissionAmountAndCurncy, "After Amend Booking Confirmation, Commission Amnt & Currency text Actual & Expected don't match")

        //< PDF voucher > button
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, PDF Voucher Button Display Status Actual & Expected don't match")

    }

    protected def "VerifybkngConfrmngFromDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking confirmed text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt, "After Amend, Booking Confirmation Screen, title text Actual & Expected don't match")
        //Close X function
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCloseBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Close X function display status Actual & Expected don't match")
        //A confirmation of your booking has been email to:
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHeaderSectionText,resultData.hotel.amendBooking.occupTab.expHeaderSectionText, "After Amend Booking Confirmation, Email Text Actual & Expected don't match")

        //brand & icon
        assertionEquals(resultData.hotel.amendBooking.occupTab.actBrandAndIconDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Brand and icon display status Actual & Expected don't match")

        //Booking ID: & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actBkngId, resultData.hotel.amendBooking.occupTab.expBkngID, "After Amend Booking Confirmation, Booking ID: & Value text Actual & Expected don't match")

         //Itinerary ID,  Itinerary name & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actitinearyIDAndName,resultData.hotel.itineraryPage.readitinearyIDAndName, "After Amend Booking Confirmation, Itinerary ID: & Value text Actual & Expected don't match")

        //Departure date: text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actDepDateTxt,data.expected.departDateTxt, "After Amend Booking Confirmation, Departure Date text Actual & Expected don't match")
        //Departure Date value text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actDepDateValTxt,resultData.hotel.amendBooking.occupTab.expDepDateValTxt, "After Amend Booking Confirmation, Departure Date Value text Actual & Expected don't match")

        //Traveller Details & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTravellerDetailsTxt,data.expected.travellerDeetailsTxt, "After Amend Booking Confirmation, Traveller Details text Actual & Expected don't match")

        List actualTravellerData=resultData.hotel.confirmationPage.travellers.get("actualTravellers")
        List expectedTravelerData=resultData.hotel.confirmationPage.travellers.get("expectedTravellers")
        //Validate  travellers
       // assertionListEquals(actualTravellerData,expectedTravelerData,"After Amend Booking Confirmation, List Of Travellers  actual & expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHotelName,resultData.hotel.amendBooking.occupTab.expItemName, "After Amend Booking Confirmation, Item Name text Actual & Expected don't match")

        //item address
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHotelAddressTxt,resultData.hotel.amendBooking.occupTab.expItemAddressTxt, "After Amend Booking Confirmation, Item Address text Actual & Expected don't match")

        //number of room, room type & value
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actRoomNumAndTypeTxt,resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt, "After Amend Booking Confirmation, Room Num And Type text Actual & Expected don't match")

        //Capture PAX
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
        assertContains(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        //Meal basis & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actMealBasisText,resultData.hotel.amendBooking.occupTab.actmealBasisTxt, "After Amend Booking Confirmation, Meal Basis text Actual & Expected don't match")

        //Check in date, Number of Nights
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCheckInDateNumOfNights.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expCheckInDate.replaceAll(" ",""), "After Amend Booking Confirmation, Check in Date and num of nights text Actual & Expected don't match")

        //room rate amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actRoomRateAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expRoomRateAmntAndCurrncy, "After Amend Booking Confirmation, Room Rate Amnt & Currency text Actual & Expected don't match")

        //commission amount and currency
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actCommissionAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expCommissionAmountAndCurncy, "After Amend Booking Confirmation, Commission Amnt & Currency text Actual & Expected don't match")

        //< PDF voucher > button
        assertionEquals(resultData.hotel.amendBooking.occupTab.actPDFVoucherBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, PDF Voucher Button Display Status Actual & Expected don't match")

    }

    protected def "VerifybkngConfFromDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking confirmed text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt, "After Amend, Booking Confirmation Screen, title text Actual & Expected don't match")
        //Close X function
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCloseBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Close X function display status Actual & Expected don't match")
        //A confirmation of your booking has been email to:
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHeaderSectionText,resultData.hotel.amendBooking.occupTab.expHeaderSectionText, "After Amend Booking Confirmation, Email Text Actual & Expected don't match")

        //brand & icon
        assertionEquals(resultData.hotel.amendBooking.occupTab.actBrandAndIconDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Brand and icon display status Actual & Expected don't match")

        //Booking ID: & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actBkngId.replaceAll(" ",""), resultData.hotel.amendBooking.occupTab.expBkngID.replaceAll(" ",""), "After Amend Booking Confirmation, Booking ID: & Value text Actual & Expected don't match")

        //Itinerary ID,  Itinerary name & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actitinearyIDAndName,resultData.hotel.itineraryPage.readitinearyIDAndName, "After Amend Booking Confirmation, Itinerary ID: & Value text Actual & Expected don't match")

        //Departure date: text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actDepDateTxt,data.expected.departDateTxt, "After Amend Booking Confirmation, Departure Date text Actual & Expected don't match")
        //Departure Date value text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actDepDateValTxt,resultData.hotel.amendBooking.occupTab.expDepDateValTxt, "After Amend Booking Confirmation, Departure Date Value text Actual & Expected don't match")

        //Traveller Details & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actTravellerDetailsTxt,data.expected.travellerDeetailsTxt, "After Amend Booking Confirmation, Traveller Details text Actual & Expected don't match")

        List actualTravellerData=resultData.hotel.confirmationPage.travellers.get("actualTravellers")
        List expectedTravelerData=resultData.hotel.confirmationPage.travellers.get("expectedTravellers")
        //Validate  travellers
        assertionListEquals(actualTravellerData,expectedTravelerData,"After Amend Booking Confirmation, List Of Travellers  actual & expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHotelName,resultData.hotel.amendBooking.occupTab.expItemName, "After Amend Booking Confirmation, Item Name text Actual & Expected don't match")

        //item address
        assertionEquals(resultData.hotel.amendBooking.occupTab.actHotelAddressTxt,resultData.hotel.amendBooking.occupTab.expItemAddressTxt, "After Amend Booking Confirmation, Item Address text Actual & Expected don't match")

        //number of room, room type & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actRoomNumAndTypeTxt,resultData.hotel.amendBooking.expRoomNumAndTypeOfRoomTxt, "After Amend Booking Confirmation, Room Num And Type text Actual & Expected don't match")

        //Capture PAX
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
        assertContains(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        //Meal basis & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actMealBasisTxt,resultData.hotel.amendBooking.occupTab.actualmealBasisTxt, "After Amend Booking Confirmation, Meal Basis text Actual & Expected don't match")

        //Check in date, Number of Nights
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCheckInDateNumOfNights.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expCheckInDate.replaceAll(" ",""), "After Amend Booking Confirmation, Check in Date and num of nights text Actual & Expected don't match")

        //room rate amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actRoomRateAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expRoomRateAmntAndCurrncy, "After Amend Booking Confirmation, Room Rate Amnt & Currency text Actual & Expected don't match")

        //commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actCommissionAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expCommissionAmountAndCurncy, "After Amend Booking Confirmation, Commission Amnt & Currency text Actual & Expected don't match")

        //< PDF voucher > button
        assertionEquals(resultData.hotel.amendBooking.occupTab.actPDFVoucherBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, PDF Voucher Button Display Status Actual & Expected don't match")

    }


    protected def "VerifybookingConfirmingDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking confirmed text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt, "After Amend, Booking Confirmation Screen, title text Actual & Expected don't match")
        //Close X function
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCloseBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Close X function display status Actual & Expected don't match")
        //A confirmation of your booking has been email to:
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHeaderSectionText,resultData.hotel.amendBooking.occupTab.expectedHeaderSectionText, "After Amend Booking Confirmation, Email Text Actual & Expected don't match")

        //brand & icon
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualBrandAndIconDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Brand and icon display status Actual & Expected don't match")

        //Booking ID: & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualbookingID.replaceAll(" ",""), resultData.hotel.amendBooking.occupTab.expectedBookingID.replaceAll(" ",""), "After Amend Booking Confirmation, Booking ID: & Value text Actual & Expected don't match")

        //Itinerary ID,  Itinerary name & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualitinearyIDAndName,resultData.hotel.itineraryPage.readitinearyIDAndName, "After Amend Booking Confirmation, Itinerary ID: & Value text Actual & Expected don't match")

        //Departure date: text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualDepDateTxt,data.expected.departDateTxt, "After Amend Booking Confirmation, Departure Date text Actual & Expected don't match")
        //Departure Date value text
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualDepDateValTxt,resultData.hotel.amendBooking.occupTab.expectedDepDateValTxt, "After Amend Booking Confirmation, Departure Date Value text Actual & Expected don't match")

        //Traveller Details & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualTravellerDetailsTxt,data.expected.travellerDeetailsTxt, "After Amend Booking Confirmation, Traveller Details text Actual & Expected don't match")

        List actualTravellerData=resultData.hotel.confirmationPage.travellers.get("actualTravellers")
        List expectedTravelerData=resultData.hotel.confirmationPage.travellers.get("expectedTravellers")
        //Validate  travellers
        //assertionListEquals(actualTravellerData,expectedTravelerData,"After Amend Booking Confirmation, List Of Travellers  actual & expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHotelName,resultData.hotel.amendBooking.occupTab.expectedItemName, "After Amend Booking Confirmation, Item Name text Actual & Expected don't match")

        //item address
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualHotelAddressTxt,resultData.hotel.amendBooking.occupTab.expectedItemAddressTxt, "After Amend Booking Confirmation, Item Address text Actual & Expected don't match")

        //number of room, room type & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomNumAndTypeTxt,resultData.hotel.amendBooking.occupTab.expectedRoomNumAndroomValueTxt, "After Amend Booking Confirmation, Room Num And Type text Actual & Expected don't match")

        //Capture PAX
        //assertionEquals(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
        assertContains(resultData.hotel.amendBooking.occupTab.actpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        //Meal basis & value
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualMealBasisTxt,resultData.hotel.amendBooking.occupTab.actualmealBasisTxt, "After Amend Booking Confirmation, Meal Basis text Actual & Expected don't match")

        //Check in date, Number of Nights
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCheckInDateNumOfNights.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.expectedCheckInDate.replaceAll(" ",""), "After Amend Booking Confirmation, Check in Date and num of nights text Actual & Expected don't match")

        //room rate amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualRoomRateAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expectedRoomRateAmntAndCurrncy, "After Amend Booking Confirmation, Room Rate Amnt & Currency text Actual & Expected don't match")

        //commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualCommissionAmountAndCurrency,resultData.hotel.amendBooking.occupTab.expectedCommissionAmountAndCurncy, "After Amend Booking Confirmation, Commission Amnt & Currency text Actual & Expected don't match")

        //< PDF voucher > button
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualPDFVoucherBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, PDF Voucher Button Display Status Actual & Expected don't match")

    }

    protected def "VerifyitineraryUpdating"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Item card
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.acutalDisplayStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Card Display Status Actual & Expected don't match")

        //Get Booing ID and number
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualBookingIDinBookedDetailsScrn,resultData.hotel.amendBooking.occupTab.expectedBookingID, "After Amend Booking, Itinerary Page, Booked Items, Booking ID: & Value text Actual & Expected don't match")

        //Confirmed tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualconfirmedTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Display Status Actual & Expected don't match")

        //confirm tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualConfirmedTabTxt,data.expected.statusTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Text Actual & Expected don't match")

        //Amend tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualAmendTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Display Status Actual & Expected don't match")

        //Amend tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt,data.expected.amendTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Text Actual & Expected don't match")

        //Cancel tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Display Status Actual & Expected don't match")

        //Cancel tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelTabTxt,data.expected.cancelTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Text Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualImageStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Image Display Status Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualHotelNameTxt,data.expected.cityAreaHotelText, "After Amend Booking, Itinerary Page, Booked Items, Item Name Text Actual & Expected don't match")

        //item star rating
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualStarRatingInBookedHotelItem,data.expected.starRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")


        //room type
        if(data.input.multiroom==true){
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn,data.expected.firstRoomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        }else{
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn,data.expected.roomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        }

        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxTxt.replaceAll(" ",""), "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }else if(data.input.multiroom==true){
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn,resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }

        else{
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn,data.expected.scndAmndPaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }

        //Rate plan - meal basis
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualratePlanInBookedItmsScrn,data.expected.mealBasisTxt, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")

        //Free cancellation until date
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualFreeCnclTxtInbookedItmsScrn,resultData.hotel.amendBooking.expectedFreeCanclTxt, "After Amend Booking, Itinerary Page, Booked Items, Free Cancellation text Actual & Expected don't match")

        //cancellation popup opens
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelPopupDisStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup Display Status Actual & Expected don't match")

        //Cancellation policy
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCanclPolcyTxt,data.expected.cancellationHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Header Text Actual & Expected don't match")

        //Close X
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCloseButtonDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup close button Display Status Actual & Expected don't match")

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualSplConditionTxt,resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPlzNoteTxt,data.expected.plzNoteTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge header text Actual & Expected don't match")

        //assertionEquals(resultData.hotel.itineraryPage.bookedItems. actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDatesTxt,data.expected.allDatesTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  All Dates text Actual & Expected don't match")

        //check in date and number of nights
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDurationTxt,resultData.hotel.itineraryPage.bookedItems.expdurationTxt, "After Amend Booking, Itinerary Page, Booked Items,  actual check in and duration text Actual & Expected don't match")

        //Voucher Icon
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDownloadVocherIconDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Download Voucher button Display Status Actual & Expected don't match")
        //Commission and percentage
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualcomPercentTxt,data.expected.commissionTxt, "After Amend Booking, Itinerary Page, Booked Items, Commission Percentage text Actual & Expected don't match")

        //Room rate amount and currency
        if(data.input.multiroom==true){
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency,resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice, "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")
        }else{
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency,resultData.hotel.amendBooking.occupTab.actualTotalAmountAndCurrency, "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")
        }


        //Traveller Details
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails,resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails, "After Amend Booking, Itinerary Page, Booked Items, Travellers Details Text Actual & Expected don't match")

    }

    protected def "VerifyitineraryUpdatingDates"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Item card
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.acutalDisplayStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Card Display Status Actual & Expected don't match")

        //Get Booing ID and number
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualBookingIDinBookedDetailsScrn,resultData.hotel.amendBooking.occupTab.expectedBookingID, "After Amend Booking, Itinerary Page, Booked Items, Booking ID: & Value text Actual & Expected don't match")

        //Confirmed tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualconfirmedTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Display Status Actual & Expected don't match")

        //confirm tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualConfirmedTabTxt,data.expected.statusTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Text Actual & Expected don't match")

        //Amend tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualAmendTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Display Status Actual & Expected don't match")

        //Amend tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt,data.expected.amendTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Text Actual & Expected don't match")

        //Cancel tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Display Status Actual & Expected don't match")

        //Cancel tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelTabTxt,data.expected.cancelTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Text Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualImageStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Image Display Status Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualHotelNameTxt,data.expected.cityAreaHotelText, "After Amend Booking, Itinerary Page, Booked Items, Item Name Text Actual & Expected don't match")

        //item star rating
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualStarRatingInBookedHotelItem,data.expected.starRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")


        //room type
        if(data.input.multiroom==true){
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn,data.expected.firstRoomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        }else{
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn,data.expected.roomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        }

        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn,resultData.hotel.amendBooking.occupTab.exppaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }else if(data.input.multiroom==true){
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn,resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }

        else{
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn,data.expected.scndAmndPaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }

        //Rate plan - meal basis
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualratePlanInBookedItmsScrn,data.expected.mealBasisTxt, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")

        //Free cancellation until date
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualFreeCnclTxtInbookedItmsScrn,resultData.hotel.amendBooking.expectedFreeCanclTxt, "After Amend Booking, Itinerary Page, Booked Items, Free Cancellation text Actual & Expected don't match")

        //cancellation popup opens
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelPopupDisStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup Display Status Actual & Expected don't match")

        //Cancellation policy
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCanclPolcyTxt,data.expected.cancellationHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Header Text Actual & Expected don't match")

        //Close X
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCloseButtonDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup close button Display Status Actual & Expected don't match")

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualSplConditionTxt,resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPlzNoteTxt,data.expected.plzNoteTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge header text Actual & Expected don't match")

        //assertionEquals(resultData.hotel.itineraryPage.bookedItems. actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDatesTxt,data.expected.allDatesTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  All Dates text Actual & Expected don't match")

        //check in date and number of nights
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDurationTxt,resultData.hotel.itineraryPage.bookedItems.expdurationTxt, "After Amend Booking, Itinerary Page, Booked Items,  actual check in and duration text Actual & Expected don't match")

        //Voucher Icon
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDownloadVocherIconDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Download Voucher button Display Status Actual & Expected don't match")
        //Commission and percentage
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualcomPercentTxt,data.expected.commissionTxt, "After Amend Booking, Itinerary Page, Booked Items, Commission Percentage text Actual & Expected don't match")

        //Room rate amount and currency
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency,resultData.hotel.amendBooking.occupTab.actualRoomRateAmountAndCurrency, "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")

        //Traveller Details
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails,resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails, "After Amend Booking, Itinerary Page, Booked Items, Travellers Details Text Actual & Expected don't match")

    }
    protected def "VerifyitineraryUpdatingDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Item card
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.acutalDisplayStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Card Display Status Actual & Expected don't match")

        //Get Booing ID and number
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualBookingIDinBookedDetailsScrn,resultData.hotel.amendBooking.occupTab.expectedBookingID, "After Amend Booking, Itinerary Page, Booked Items, Booking ID: & Value text Actual & Expected don't match")

        //Confirmed tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualconfirmedTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Display Status Actual & Expected don't match")

        //confirm tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualConfirmedTabTxt,data.expected.statusTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Text Actual & Expected don't match")

        //Amend tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualAmendTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Display Status Actual & Expected don't match")

        //Amend tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualAmendTabTxt,data.expected.amendTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Text Actual & Expected don't match")

        //Cancel tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Display Status Actual & Expected don't match")

        //Cancel tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelTabTxt,data.expected.cancelTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Text Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualImageStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Image Display Status Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualHotelNameTxt,data.expected.cityAreaHotelText, "After Amend Booking, Itinerary Page, Booked Items, Item Name Text Actual & Expected don't match")

        //item star rating
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualStarRatingInBookedHotelItem,data.expected.starRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")


        //room type
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualroomdescTxtInBookedItmsScrn,data.expected.firstRoomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        if(data.input.infant==true) {
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn,resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")


        }else{
            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPaxNumInBookedItmsScrn,data.expected.scndAmndPaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }


        //Rate plan - meal basis
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualratePlanInBookedItmsScrn,data.expected.mealBasisTxt, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")

        //Free cancellation until date
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualFreeCnclTxtInbookedItmsScrn,resultData.hotel.itineraryPage.bookedItems.expFreeCnclTxtInbookedItmsScrn, "After Amend Booking, Itinerary Page, Booked Items, Free Cancellation text Actual & Expected don't match")

        //cancellation popup opens
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancelPopupDisStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup Display Status Actual & Expected don't match")

        //Cancellation policy
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCanclPolcyTxt,data.expected.cancellationHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Header Text Actual & Expected don't match")

        //Close X
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCloseButtonDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup close button Display Status Actual & Expected don't match")

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualSplConditionTxt,resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPlzNoteTxt,data.expected.plzNoteTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge header text Actual & Expected don't match")

        //assertionEquals(resultData.hotel.itineraryPage.bookedItems. actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDatesTxt,data.expected.allDatesTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  All Dates text Actual & Expected don't match")

        //check in date and number of nights
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDurationTxt.replaceAll(" ",""),resultData.hotel.itineraryPage.bookedItems.expdurationTxt.replaceAll(" ",""), "After Amend Booking, Itinerary Page, Booked Items,  actual check in and duration text Actual & Expected don't match")

        //Voucher Icon
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualDownloadVocherIconDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Download Voucher button Display Status Actual & Expected don't match")
        //Commission and percentage
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualcomPercentTxt,data.expected.commissionTxt, "After Amend Booking, Itinerary Page, Booked Items, Commission Percentage text Actual & Expected don't match")

        //Room rate amount and currency
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.actualRoomRateAmountAndCurrency.replaceAll(" ",""), "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")

        //Traveller Details
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actualtravellerDetails,resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails, "After Amend Booking, Itinerary Page, Booked Items, Travellers Details Text Actual & Expected don't match")

    }

    protected def "VerifyitinryUpdatingDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Item card
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDisplayStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Card Display Status Actual & Expected don't match")

        //Get Booing ID and number
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actBookingIDinBookedDetailsScrn,resultData.hotel.amendBooking.occupTab.expectedBookingID, "After Amend Booking, Itinerary Page, Booked Items, Booking ID: & Value text Actual & Expected don't match")

        //Confirmed tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actconfirmedTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Display Status Actual & Expected don't match")

        //confirm tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actConfirmedTabTxt,data.expected.statusTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Text Actual & Expected don't match")

        //Amend tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actAmendTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Display Status Actual & Expected don't match")

        //Amend tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actAmendTabTxt,data.expected.amendTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Text Actual & Expected don't match")

        //Cancel tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancelTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Display Status Actual & Expected don't match")

        //Cancel tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancelTabTxt,data.expected.cancelTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Text Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actImageStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Image Display Status Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actHotelNameTxt,data.expected.cityAreaHotelTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Name Text Actual & Expected don't match")



        //room type
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actroomdescTxtInBookedItmsScrn,data.expected.secndRoomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")
        if(data.input.infant==true) {
            //item star rating
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actStarRatingInBookedHotelItem,data.expected.scndStarRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")

            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPaxNumInBookedItmsScrn,resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")
            //Rate plan - meal basis
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actratePlanInBookedItmsScrn,data.expected.mealBasisTxt, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")


        }else {

            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPaxNumInBookedItmsScrn, data.expected.scndAmndPaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")
            //item star rating
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actStarRatingInBookedHotelItem,data.expected.starRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")
            //Rate plan - meal basis
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actratePlanInBookedItmsScrn,data.expected.mealBasisText, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")

        }

        //Free cancellation until date
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actFreeCnclTxtInbookedItmsScrn,resultData.hotel.amendBooking.expFreeCanclTxt, "After Amend Booking, Itinerary Page, Booked Items, Free Cancellation text Actual & Expected don't match")

        //cancellation popup opens
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancelPopupDisStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup Display Status Actual & Expected don't match")

        //Cancellation policy
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCanclPolcyTxt,data.expected.cancellationHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Header Text Actual & Expected don't match")

        //Close X
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCloseButtonDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup close button Display Status Actual & Expected don't match")

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actSplConditionTxt,resultData.hotel.amendBooking.occupTab.expSpConditionHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPlzNoteTxt,data.expected.plzNoteTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancellationChargeTxt,data.expected.cancelChargeTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge header text Actual & Expected don't match")

        //assertionEquals(resultData.hotel.itineraryPage.bookedItems. actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDatesTxt,data.expected.allDatesTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  All Dates text Actual & Expected don't match")

        //check in date and number of nights
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDurationTxt,resultData.hotel.itineraryPage.bookedItems.expdurTxt, "After Amend Booking, Itinerary Page, Booked Items,  actual check in and duration text Actual & Expected don't match")

        //Voucher Icon
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDownloadVocherIconDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Download Voucher button Display Status Actual & Expected don't match")
        //Commission and percentage
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actcomPercentTxt,data.expected.commissionTxt, "After Amend Booking, Itinerary Page, Booked Items, Commission Percentage text Actual & Expected don't match")

        //Room rate amount and currency
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPriceAndcurrency.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.actRoomRateAmountAndCurrency.replaceAll(" ",""), "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")

        //Traveller Details
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.acttravellerDetails,resultData.hotel.itineraryPage.bookedItems.expectedtravellerDetails, "After Amend Booking, Itinerary Page, Booked Items, Travellers Details Text Actual & Expected don't match")

    }

    protected def "VerifyitinryUpdngDatesTab"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Item card
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDisplayStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Card Display Status Actual & Expected don't match")

        //Get Booing ID and number
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actBookingIDinBookedDetailsScrn,resultData.hotel.amendBooking.occupTab.expectedBookingID, "After Amend Booking, Itinerary Page, Booked Items, Booking ID: & Value text Actual & Expected don't match")

        //Confirmed tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actconfirmedTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Display Status Actual & Expected don't match")

        //confirm tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actConfirmedTabTxt,data.expected.statusTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Text Actual & Expected don't match")

        //Amend tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actAmendTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Display Status Actual & Expected don't match")

        //Amend tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actAmendTabTxt,data.expected.amendTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Text Actual & Expected don't match")

        //Cancel tab
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancelTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Display Status Actual & Expected don't match")

        //Cancel tab text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancelTabTxt,data.expected.cancelTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Text Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actImageStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Image Display Status Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actHotelNameTxt,data.expected.cityAreaHotelText, "After Amend Booking, Itinerary Page, Booked Items, Item Name Text Actual & Expected don't match")



        //room type
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actroomdescTxtInBookedItmsScrn,data.expected.secndRoomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")
        if(data.input.infant==true) {
            //item star rating
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actStarRatingInBookedHotelItem,data.expected.scndStarRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")

            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPaxNumInBookedItmsScrn,resultData.hotel.itineraryPage.bookedItems.expectedPaxNumInBookedItmsScrn, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")
            //Rate plan - meal basis
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actratePlanInBookedItmsScrn,data.expected.mealBasisTxt, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")


        }else {

            //Pax number requested
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPaxNumInBookedItmsScrn, data.expected.scndAmndPaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")
            //item star rating
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actStarRatingInBookedHotelItem,data.expected.starRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")
            //Rate plan - meal basis
            assertionEquals(resultData.hotel.itineraryPage.bookedItems.actratePlanInBookedItmsScrn,data.expected.mealBasisText, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")

        }

        //Free cancellation until date
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actFreeCnclTxtInbookedItmsScrn,resultData.hotel.amendBooking.expFreeCanclTxt, "After Amend Booking, Itinerary Page, Booked Items, Free Cancellation text Actual & Expected don't match")

        //cancellation popup opens
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancelPopupDisStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup Display Status Actual & Expected don't match")

        //Cancellation policy
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCanclPolcyTxt,data.expected.cancellationHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Header Text Actual & Expected don't match")

        //Close X
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCloseButtonDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup close button Display Status Actual & Expected don't match")

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actSplConditionTxt,resultData.hotel.amendBooking.occupTab.expSpConditionHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPlzNoteTxt,data.expected.plzNoteTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actCancellationChargeTxt,data.expected.cancelChargeTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge header text Actual & Expected don't match")

        //assertionEquals(resultData.hotel.itineraryPage.bookedItems. actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDatesTxt,data.expected.allDatesTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  All Dates text Actual & Expected don't match")

        //check in date and number of nights
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDurationTxt,resultData.hotel.itineraryPage.bookedItems.expdurTxt, "After Amend Booking, Itinerary Page, Booked Items,  actual check in and duration text Actual & Expected don't match")

        //Voucher Icon
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actDownloadVocherIconDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Download Voucher button Display Status Actual & Expected don't match")
        //Commission and percentage
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actcomPercentTxt,data.expected.commissionTxt, "After Amend Booking, Itinerary Page, Booked Items, Commission Percentage text Actual & Expected don't match")

        //Room rate amount and currency
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.actPriceAndcurrency,resultData.hotel.amendBooking.occupTab.actRoomRateAmountAndCurrency, "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")

        //Traveller Details
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.acttravellerDetails,resultData.hotel.itineraryPage.bookedItems.exptravlrDetails, "After Amend Booking, Itinerary Page, Booked Items, Travellers Details Text Actual & Expected don't match")

    }


    protected def "VerifycurrentDetailsSecond"(HotelSearchData data, HotelTransferTestResultData resultData){

        //text should show
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualOccupTxt,data.expected.occupancyTxt, "Amend popup Screen,  Occupancy tab, current booking text Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualItemNameInOccupancyTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, item name text Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualroomdescTxt,resultData.hotel.amendBooking.occupTab.currntDetails.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, room num and type text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualmealBasisTxt,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, meal basis text Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualfreeCancelTxt,resultData.hotel.amendBooking.occupTab.currntDetails.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Free Cancellation text Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualCheckInAndNumOfNight,resultData.hotel.amendBooking.occupTab.currntDetails.expectedCheckInAndNumOfNight, "Amend popup Screen,  Occupancy tab,  actual check in and duration text Actual & Expected don't match")

        if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")

        } else if(data.input.multiroom==true) {
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")

        }
        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  actual Pax text Actual & Expected don't match")

        }
        if(data.input.multiroom==true) {
            //travellers
            assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualTravellersText,resultData.hotel.amendBooking.occupTab.currntDetails.expectedTravellersTxt, "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")

        }else{
            //travellers
            assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualTravellersText.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.currntDetails.expectedTravellersTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab,  Travellers text Actual & Expected don't match")

        }

        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualTotalRoomAmntAndCurrncyTxt,resultData.hotel.itineraryPage.bookedItems.actualPriceAndcurrency, "Amend popup Screen,  Occupancy tab,  Total Amount And Currency text Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.occupTab.currntDetails.actualCommissionTxt,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab,  Commission Percent text Actual & Expected don't match")

    }


    protected def "VerifyChangeAddAndRemoveLess"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Change, add or remove occupants availability:
        assertionEquals(resultData.hotel.amendBooking.occupTab.actualChangeOccupantsTxt,data.expected.changeOccupantsTxt, "Amend popup Screen,  Occupancy tab,  Change, add or remove occupants availability: text Actual & Expected don't match")

        //Occupancy
        List actualOcupsNumData=resultData.hotel.amendBooking.occupTab.get("actualOCupList")
        List expectedOcupsNumData=resultData.hotel.amendBooking.occupTab.get("expectedOcupList")
        assertionListEquals(actualOcupsNumData,expectedOcupsNumData,"Amend popup Screen,  Occupancy tab,  Occupant List Number actual & expected don't match")


        //Title / first name / last name
        List actualOcupsData=resultData.hotel.amendBooking.occupTab.get("actualOCupNames")
        List expectedOcupsData=resultData.hotel.amendBooking.occupTab.get("expectedOcupnames")

        assertionListEquals(actualOcupsData,expectedOcupsData,"Amend popup Screen,  Occupancy tab,  Occupant List Name actual & expected don't match")



    }

    protected def "VerifyremoveanadultpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        //page refreshed to show all the details. this validation is covered in test cases 26 and 27
    }

    protected def "VerifyremoveanchildpaxtoroomAllowed"(HotelSearchData data, HotelTransferTestResultData resultData){

        //page refreshed to show all the details. this validation is covered in test cases 26 and 27
    }
    protected def "Verifyremove1Infant"(HotelSearchData data, HotelTransferTestResultData resultData){

        //page refreshed to show all the details. this validation is covered in test cases 22 and 23
    }

    protected def "VerifychangeToBeforeThirdAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualOccupancyTxt,data.expected.changeToTxt, "Amend popup Screen,  Occupancy tab,  Change To text after second amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualItemNameInOccupTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, Change to section, item name text after second amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.occupTab.changeTo.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, Change to section, room num and type text after second amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualmealBasisText,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, Change to section, meal basis text after second amendment Actual & Expected don't match")

        //Free Cancellation Text
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualfreeCancellationTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Change to section, Free Cancellation text after second amendment Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualCheckInPlusNumOfNight,resultData.hotel.amendBooking.occupTab.changeTo.expCheckInPlusNumOfNight, "Amend popup Screen,  Occupancy tab, Change to section, actual check in and duration text after second amendment Actual & Expected don't match")


       if(data.input.children.size()>0 && (data.input.infant==true)){

           //number of pax
           assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after second amendment Actual & Expected don't match")

       } else if(data.input.multiroom==true) {
           //number of pax
           assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after second amendment Actual & Expected don't match")

       }
        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after second amendment Actual & Expected don't match")

        }


        //travellers
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actTravlrTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,  Travellers text after second amendment Actual & Expected don't match")

        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.occupTab.changeTo.expPriceDiff, "Amend popup Screen,  Occupancy tab, Change to section,  Amount Rate change and currency after second amendment Actual & Expected don't match")

        //Room Amount and Currency
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.occupTab.changeTo.expTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Total Amount And Currency text after second amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Commission Percent text after second amendment Actual & Expected don't match")

        //confirmation status(inventory)
        //assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Inventory Status after second amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button enable status Actual & Expected don't match")

    }

    protected def "VerifychangeToBeforeThirdAmendModified"(HotelSearchData data, HotelTransferTestResultData resultData){

        //change to: text should show
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualOccupancyTxt,data.expected.changeToTxt, "Amend popup Screen,  Occupancy tab,  Change To text after second amendment Actual & Expected don't match")

        //Item Name
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualItemNameInOccupTab,data.expected.cityAreaHotelText, "Amend popup Screen,  Occupancy tab, Change to section, item name text after second amendment Actual & Expected don't match")

        //booked room type
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualNumAndtypeOfRoomTxt,resultData.hotel.amendBooking.occupTab.changeTo.expectedRoomNumAndTypeOfRoomTxt, "Amend popup Screen,  Occupancy tab, Change to section, room num and type text after second amendment Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualmealBasisText,data.expected.mealBasisTxt, "Amend popup Screen,  Occupancy tab, Change to section, meal basis text after second amendment Actual & Expected don't match")

        //Free Cancellation Text
        //assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualfreeCancellationTxt,resultData.hotel.amendBooking.expectedFreeCanclTxt, "Amend popup Screen,  Occupancy tab, Change to section, Free Cancellation text after second amendment Actual & Expected don't match")

        //check in, number of night
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualCheckInPlusNumOfNight,resultData.hotel.amendBooking.occupTab.changeTo.expCheckInPlusNumOfNight, "Amend popup Screen,  Occupancy tab, Change to section, actual check in and duration text after second amendment Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.infant==true)){

            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after second amendment Actual & Expected don't match")

        } else if(data.input.multiroom==true) {
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actPaxText.replaceAll(" ",""),data.expected.scndAmndPaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after second amendment Actual & Expected don't match")

        }
        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,   actual Pax text after second amendment Actual & Expected don't match")

        }


        //travellers
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actTravlrTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt.replaceAll(" ",""), "Amend popup Screen,  Occupancy tab, Change to section,  Travellers text after second amendment Actual & Expected don't match")

        //amount rate change and currency
        //assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actpriceDiffAndCurrnTxt,resultData.hotel.amendBooking.occupTab.changeTo.expPriceDiff, "Amend popup Screen,  Occupancy tab, Change to section,  Amount Rate change and currency after second amendment Actual & Expected don't match")

        //Room Amount and Currency
        //assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actTotalRoomAmntAndCurrncyText,resultData.hotel.amendBooking.occupTab.changeTo.expTotalRoomAmntAndCurrncyTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Total Amount And Currency text after second amendment Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualCommissionText,data.expected.commissionTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Commission Percent text after second amendment Actual & Expected don't match")

        //confirmation status(inventory)
        //assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualConfirmationStatus,data.expected.statusTxt, "Amend popup Screen,  Occupancy tab, Change to section,  Inventory Status after second amendment Actual & Expected don't match")

        //Ok button should be displayed
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualOkBtnDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button display status Actual & Expected don't match")

        //Ok button should be enabled
        assertionEquals(resultData.hotel.amendBooking.occupTab.changeTo.actualOkBtnEnableStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Change to section, Ok button enable status Actual & Expected don't match")

    }


    protected def "VerifyselectToAssignBeforeThirdAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Please select occupants names:
        assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actualPleaseSelectTxt,data.expected.plzSelTxt, "Amend popup Screen,  Occupancy tab,  Please select occupants names: text Actual & Expected don't match")

        //Get 1st traveller checked status
        assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actFirstTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, First Traveller CheckBox display status Actual & Expected don't match")
        //Get 2nd traveller checked status
        assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actSecondTrvlrchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Second Traveller CheckBox display status Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.child==true)){
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Third Traveller CheckBox display status Actual & Expected don't match")

        }
        else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Third Traveller CheckBox display status Actual & Expected don't match")

            //Get 4th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actFourthTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Fourth Traveller CheckBox display status Actual & Expected don't match")

        }else if(data.input.multiroom==true) {
            //Get 3rd traveller checked status
            assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actThirdTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Third Traveller CheckBox display status Actual & Expected don't match")

            //Get 7th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actSeventhTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Seventh Traveller CheckBox display status Actual & Expected don't match")

        }
        else{
            //Get 4th traveller checked status
            assertionEquals(resultData.hotel.amendBooking.occupTab.plzSelect.actFourthTrvlchkBoxStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Fourth Traveller CheckBox display status Actual & Expected don't match")

        }


        //lead traveller
        assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actualLeadTravlr, resultData.hotel.amendBooking.occupTab.plzSelect.expectedLeadTravlrTxt, "Amend popup Screen, Occupancy tab, Lead Traveller Text Actual & Expected don't match")

        //infant value displaying = 1
        if(data.input.children.size()>0 && (data.input.infant==true)){

            //infant text should show - if it is available for the room
            assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actualInfantLabTxt,data.expected.infantLabelTxt+"s", "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

            //infant value displaying = 1
            assertionEquals(resultData.hotel.amendBooking.actualSelectedInfantValue,data.expected.infantDropDownListValues.getAt(1), "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")
        }else{
            //infant text should show - if it is available for the room
            assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actualInfantLabTxt,data.expected.infantLabelTxt, "Amend popup Screen, Occupancy tab, Infant Label Text Actual & Expected don't match")

        }

        //dropdown list to select number of infant should show
        assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actualInfantDropdownListDispStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Drop down Infant display status Actual & Expected don't match")
        //list should show 0-1
        if(data.input.multiroom==true){
            assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actListInfantValues,data.expected.infantDropDownListVal_FirstRoom,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")

        }else{
            assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actListInfantValues,data.expected.infantDropDownListValues,"Amend popup Screen, Occupancy tab, Drop down Infant List Values Actual & Expected don't match")

        }
        //Confirm Amendment Button display status
        assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actualConfirmAmendmentButtonStatus,data.expected.dispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button display status Actual & Expected don't match")

        //Confirm Amendment Button Disabled status
        assertionEquals( resultData.hotel.amendBooking.occupTab.plzSelect.actualConfirmAmendmentDisabledStatus,data.expected.notDispStatus, "Amend popup Screen, Occupancy tab, Confirm Amendment Button Disabled status Actual & Expected don't match")


    }

    protected def "VerifyTAndCpagereconfirmationAfterReduceAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        //capture title text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualtitleTxt,data.expected.amendmentTitleTxt, "About to Amend screen, title text Actual & Expected don't match")

        //Capture Close Icon display status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCloseIconDispStatus,data.expected.dispStatus, "About to Amend screen, Close Icon display status Actual & Expected don't match")

        //Capture hotel name in About to Amend Screen
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualHotelNameInAbtToAmndScrn,data.expected.cityAreaHotelText, "About to Amend screen, Hotel Name text Actual & Expected don't match")

        //number of room/name of room (descripton)
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualnumAndNameofRoomText,resultData.hotel.amendBooking.aboutToAmend.expectedRoomNumAndTypeOfRoomTxt, "About to Amend screen, number of room/name of room (descripton) text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualmealBasisTxt,data.expected.mealBasisTxt, "About to Amend screen, meal basis text Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text  actual Pax text after first amendment Actual & Expected don't match")

        }else if(data.input.multiroom==true) {
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPaxText,data.expected.scndAmndPaxTxt, "About to Amend screen,  Pax text  actual Pax text after first amendment Actual & Expected don't match")

        }
        else{
            //number of pax
            //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")
            assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPaxText.replaceAll(" ",""),data.expected.modifiedpaxText.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")

        }


        //travellers
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTravellersText,resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt, "About to Amend screen,  Travellers text  Actual & Expected don't match")

        //check in date, number of night
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCheckInAndNumOfNight.replaceAll(" ",""),resultData.hotel.amendBooking.aboutToAmend.expCheckInAndNumOfNight.replaceAll(" ",""), "About to Amend screen, check in and duration text Actual & Expected don't match")

        //status
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualInvStatusInAbtToAmendScrn,data.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")

        //amount rate change and currency
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.occupTab.changeTo.expPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

        //total room amount & currency code
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.occupTab.changeTo.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        //commission & % value
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualcommissionPercentAbtToAmendScrn,data.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")

        //special condition - header text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualSpecialConditionHeaderTxt,resultData.hotel.amendBooking.aboutToAmend.expectedSpecialConditionHeaderTxt, "About to Amend screen, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPlzNoteTxt,data.expected.plzNoteTxt, "About to Amend screen,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "About to Amend screen, Cancelation Charge header text Actual & Expected don't match")

        //Cancellation charge descriptive text - need to modify
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "About to Amend screen, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "About to Amend screen,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualDatesTxt,data.expected.allDatesTxt, "About to Amend screen,  All Dates text Actual & Expected don't match")

        //Total
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTotalTextInAboutToBook,data.expected.totalTxt, "About to Amend screen,  total text Actual & Expected don't match")

        //Total Amount and currency
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.occupTab.changeTo.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        //Your Commission text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCommissionTextInAboutToAmend,data.expected.cmsnTxt, "About to Amend screen,  Your Commission text Actual & Expected don't match")

        //Your commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCommissionValueInAboutToAmend,resultData.hotel.amendBooking.aboutToAmend.expCommissionValueInAboutToAmend, "About to Amend screen,  Your Commission Amount and Currency Actual & Expected don't match")

        //< Confirm Amendment > button display status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualconfirmButtonDispStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button display status Actual & Expected don't match")

        //confirm Amendment enable status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualconfirmAmendEnableStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button Enable status Actual & Expected don't match")

        //T&C agreement text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTermsAndCondtTxt,data.expected.TermsAndCondtnsFooterTxt, "About to Amend screen, By clicking Footer Text Actual & Expected don't match")

        //Confirm booking page display status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualconfirmbookStatus,data.expected.dispStatus, "After Amend Booking Confirmation Screen display status Actual & Expected don't match")



    }

    protected def "VerifyTAndCpagereconfirmationAfterReduceAmendModified"(HotelSearchData data, HotelTransferTestResultData resultData){

        //capture title text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualtitleTxt,data.expected.amendmentTitleTxt, "About to Amend screen, title text Actual & Expected don't match")

        //Capture Close Icon display status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCloseIconDispStatus,data.expected.dispStatus, "About to Amend screen, Close Icon display status Actual & Expected don't match")

        //Capture hotel name in About to Amend Screen
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualHotelNameInAbtToAmndScrn,data.expected.cityAreaHotelText, "About to Amend screen, Hotel Name text Actual & Expected don't match")

        //number of room/name of room (descripton)
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualnumAndNameofRoomText,resultData.hotel.amendBooking.aboutToAmend.expectedRoomNumAndTypeOfRoomTxt, "About to Amend screen, number of room/name of room (descripton) text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualmealBasisTxt,data.expected.mealBasisTxt, "About to Amend screen, meal basis text Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.infant==true)){
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPaxText.replaceAll(" ",""),data.expected.paxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text  actual Pax text after first amendment Actual & Expected don't match")

        }else if(data.input.multiroom==true) {
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPaxText,data.expected.scndAmndPaxTxt, "About to Amend screen,  Pax text  actual Pax text after first amendment Actual & Expected don't match")

        }
        else{
            //number of pax
            assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPaxText.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "About to Amend screen,  Pax text Actual & Expected don't match")

        }


        //travellers
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTravellersText,resultData.hotel.amendBooking.occupTab.changeTo.expTravlrTxt, "About to Amend screen,  Travellers text  Actual & Expected don't match")

        //check in date, number of night
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCheckInAndNumOfNight.replaceAll(" ",""),resultData.hotel.amendBooking.aboutToAmend.expCheckInAndNumOfNight.replaceAll(" ",""), "About to Amend screen, check in and duration text Actual & Expected don't match")

        //status
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualInvStatusInAbtToAmendScrn,data.expected.statusTxt, "About to Amend screen,  Status text Actual & Expected don't match")

        //amount rate change and currency
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualAmntRatechangeAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.occupTab.changeTo.expPriceDiff, "About to Amend screen,  Amount Rate change and currency Actual & Expected don't match")

        //total room amount & currency code
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualRoomAmntAndCurncyAbtToAmendScrn,resultData.hotel.amendBooking.occupTab.changeTo.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  total room amount & currency code Actual & Expected don't match")

        //commission & % value
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualcommissionPercentAbtToAmendScrn,data.expected.commissionTxt, "About to Amend screen,  Commission Percent text Actual & Expected don't match")

        //special condition - header text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualSpecialConditionHeaderTxt,resultData.hotel.amendBooking.aboutToAmend.expectedSpecialConditionHeaderTxt, "About to Amend screen, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualPlzNoteTxt,data.expected.plzNoteTxt, "About to Amend screen,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "About to Amend screen, Cancelation Charge header text Actual & Expected don't match")

        //Cancellation charge descriptive text - need to modify
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt, "About to Amend screen, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "About to Amend screen,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualDatesTxt,data.expected.allDatesTxt, "About to Amend screen,  All Dates text Actual & Expected don't match")

        //Total
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTotalTextInAboutToBook,data.expected.totalTxt, "About to Amend screen,  total text Actual & Expected don't match")

        //Total Amount and currency
        //assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTotalAmountAndCurrency,resultData.hotel.amendBooking.occupTab.changeTo.expTotalRoomAmntAndCurrncyTxt, "About to Amend screen,  Amount And Currency text Actual & Expected don't match")

        //Your Commission text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCommissionTextInAboutToAmend,data.expected.cmsnTxt, "About to Amend screen,  Your Commission text Actual & Expected don't match")

        //Your commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualCommissionValueInAboutToAmend,resultData.hotel.amendBooking.aboutToAmend.expCommissionValueInAboutToAmend, "About to Amend screen,  Your Commission Amount and Currency Actual & Expected don't match")

        //< Confirm Amendment > button display status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualconfirmButtonDispStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button display status Actual & Expected don't match")

        //confirm Amendment enable status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualconfirmAmendEnableStatus,data.expected.dispStatus, "About to Amend screen, Confirm Amendment Button Enable status Actual & Expected don't match")

        //T&C agreement text
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualTermsAndCondtTxt,data.expected.TermsAndCondtnsFooterTxt, "About to Amend screen, By clicking Footer Text Actual & Expected don't match")

        //Confirm booking page display status
        assertionEquals(resultData.hotel.amendBooking.aboutToAmend.actualconfirmbookStatus,data.expected.dispStatus, "After Amend Booking Confirmation Screen display status Actual & Expected don't match")



    }


    protected def "VerifybookingConfirmingAfterReduceAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking confirmed text
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt, "After Amend, Booking Confirmation Screen, title text Actual & Expected don't match")
        //Close X function
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualCloseBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Close X function display status Actual & Expected don't match")
        //A confirmation of your booking has been email to:
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualHeaderSectionText,resultData.hotel.amendBooking.bookingConf.expectedHeaderSectionText, "After Amend Booking Confirmation, Email Text Actual & Expected don't match")

        //brand & icon
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualBrandAndIconDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, Brand and icon display status Actual & Expected don't match")
        //Booking ID: & value
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualbookingID,resultData.hotel.amendBooking.bookingConf.expectedBookingID, "After Amend Booking Confirmation, Booking ID: & Value text Actual & Expected don't match")
        //Itinerary ID,  Itinerary name & value
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualitinearyIDAndName,resultData.hotel.itineraryPage.readitinearyIDAndName, "After Amend Booking Confirmation, Itinerary ID: & Value text Actual & Expected don't match")

        //Departure date: text
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualDepDateTxt,data.expected.departDateTxt, "After Amend Booking Confirmation, Departure Date text Actual & Expected don't match")
        //Departure Date value text
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualDepDateValTxt,resultData.hotel.amendBooking.bookingConf.expectedDepDateValTxt, "After Amend Booking Confirmation, Departure Date Value text Actual & Expected don't match")

        //Traveller Details & value
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualTravellerDetailsTxt,data.expected.travellerDeetailsTxt, "After Amend Booking Confirmation, Traveller Details text Actual & Expected don't match")

        List actualTravellerData=resultData.hotel.amendBooking.bookingConf.get("actualTravellers")
        List expectedTravelerData=resultData.hotel.amendBooking.bookingConf.get("expectedTravellers")
        //Validate  travellers
        assertionListEquals(actualTravellerData,expectedTravelerData,"After Amend Booking Confirmation, List Of Travellers  actual & expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualHotelName,resultData.hotel.amendBooking.bookingConf.expectedItemName, "After Amend Booking Confirmation, Item Name text Actual & Expected don't match")

        //item address
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualHotelAddressTxt,resultData.hotel.amendBooking.bookingConf.expectedItemAddressTxt, "After Amend Booking Confirmation, Item Address text Actual & Expected don't match")

        //number of room, room type & value
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualRoomNumAndTypeTxt,resultData.hotel.amendBooking.bookingConf.expectedRoomNumAndroomValueTxt, "After Amend Booking Confirmation, Room Num And Type text Actual & Expected don't match")


        if(data.input.children.size()>0 && (data.input.infant==true)){
            //Capture PAX
            //assertionEquals(resultData.hotel.amendBooking.bookingConf.actualpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
            assertContains(resultData.hotel.amendBooking.bookingConf.actualpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        }
        else if(data.input.multiroom==true) {
            //Capture PAX
            //assertionEquals(resultData.hotel.amendBooking.bookingConf.actualpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
            assertContains(resultData.hotel.amendBooking.bookingConf.actualpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        }
        else{
            //Capture PAX
            //assertionEquals(resultData.hotel.amendBooking.bookingConf.actualpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.aboutToAmend.actualPaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
            //assertContains(resultData.hotel.amendBooking.bookingConf.actualpaxTxt.replaceAll(" ",""),resultData.hotel.amendBooking.aboutToAmend.actualPaxText.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")
            assertContains(resultData.hotel.amendBooking.bookingConf.actualpaxTxt.replaceAll(" ",""),data.expected.modifiedpaxTxt.replaceAll(" ",""), "After Amend Booking Confirmation, PAX text Actual & Expected don't match")

        }

        //Meal basis & value
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualMealBasisTxt,resultData.hotel.amendBooking.aboutToAmend.actualmealBasisTxt, "After Amend Booking Confirmation, Meal Basis text Actual & Expected don't match")

        //Check in date, Number of Nights
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualCheckInDateNumOfNights.replaceAll(" ",""),resultData.hotel.amendBooking.bookingConf.expectedCheckInDate.replaceAll(" ",""), "After Amend Booking Confirmation, Check in Date and num of nights text Actual & Expected don't match")

        //room rate amount and currency
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualRoomRateAmountAndCurrency,resultData.hotel.amendBooking.bookingConf.expectedRoomRateAmntAndCurrncy, "After Amend Booking Confirmation, Room Rate Amnt & Currency text Actual & Expected don't match")

        //commission amount and currency
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualCommissionAmountAndCurrency,resultData.hotel.amendBooking.bookingConf.expectedCommissionAmountAndCurncy, "After Amend Booking Confirmation, Commission Amnt & Currency text Actual & Expected don't match")

        //< PDF voucher > button
        assertionEquals(resultData.hotel.amendBooking.bookingConf.actualPDFVoucherBtnDispStatus,data.expected.dispStatus, "After Amend Booking Confirmation, PDF Voucher Button Display Status Actual & Expected don't match")

    }

    protected def "VerifyitinenaryUpdatingAfterReduceAmend"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Item card
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.acutalDisplayStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Card Display Status Actual & Expected don't match")

        //Get Booing ID and number
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualBookingIDinBookedDetailsScrn,resultData.hotel.amendBooking.occupTab.expectedBookingID, "After Amend Booking, Itinerary Page, Booked Items, Booking ID: & Value text Actual & Expected don't match")

        //Confirmed tab
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualconfirmedTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Display Status Actual & Expected don't match")

        //confirm tab text
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualConfirmedTabTxt,data.expected.statusTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Confirm Tab Text Actual & Expected don't match")

        //Amend tab
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualAmendTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Display Status Actual & Expected don't match")

        //Amend tab text
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualAmendTabTxt,data.expected.amendTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Amend Tab Text Actual & Expected don't match")

        //Cancel tab
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualCancelTabDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Display Status Actual & Expected don't match")

        //Cancel tab text
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualCancelTabTxt,data.expected.cancelTabTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancel Tab Text Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualImageStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Item Image Display Status Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualHotelNameTxt,data.expected.cityAreaHotelText, "After Amend Booking, Itinerary Page, Booked Items, Item Name Text Actual & Expected don't match")

        //item star rating
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualStarRatingInBookedHotelItem,data.expected.starRatingTxt, "After Amend Booking, Itinerary Page, Booked Items, Item Star Rating Text Actual & Expected don't match")

        //room type
        if(data.input.multiroom==true){
            assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualroomdescTxtInBookedItmsScrn,data.expected.firstRoomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        }else{
            assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualroomdescTxtInBookedItmsScrn,data.expected.roomDescTxt, "After Amend Booking, Itinerary Page, Booked Items, Room Type Text Actual & Expected don't match")

        }

        if(data.input.children.size()>0 && (data.input.child==true)){
            //Pax number requested
            assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualPaxNumInBookedItmsScrn,data.expected.modifiedpaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }else if(data.input.children.size()>0 && (data.input.infant==true)){
            //Pax number requested
            assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualPaxNumInBookedItmsScrn.replaceAll(" ",""),resultData.hotel.amendBooking.occupTab.exppaxText.replaceAll(" ",""), "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }else if(data.input.multiroom==true){
            //Pax number requested
            assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualPaxNumInBookedItmsScrn,resultData.hotel.amendBooking.occupTab.exppaxText, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }
        else{
            //Pax number requested
            assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualPaxNumInBookedItmsScrn,data.expected.modifiedpaxTxt, "After Amend Booking, Itinerary Page, Booked Items, PAX Text Actual & Expected don't match")

        }


        //Rate plan - meal basis
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualratePlanInBookedItmsScrn,data.expected.mealBasisTxt, "After Amend Booking, Itinerary Page, Booked Items, Meal Basis Text Actual & Expected don't match")

        //Free cancellation until date
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualFreeCnclTxtInbookedItmsScrn,resultData.hotel.amendBooking.expectedFreeCanclTxt, "After Amend Booking, Itinerary Page, Booked Items, Free Cancellation text Actual & Expected don't match")

        //cancellation popup opens
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualCancelPopupDisStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup Display Status Actual & Expected don't match")

        //Cancellation policy
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualCanclPolcyTxt,data.expected.cancellationHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Header Text Actual & Expected don't match")

        //Close X
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualCloseButtonDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup close button Display Status Actual & Expected don't match")

        //Special conditions from DD/MMM/YY (e.g. 19NOV15)
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualSplConditionTxt,resultData.hotel.amendBooking.occupTab.expectedSpecialConditionHeaderTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Special condition header text Actual & Expected don't match")

        //Please note text
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualPlzNoteTxt,data.expected.plzNoteTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Please Note text Actual & Expected don't match")

        //Cancellation Charge text
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualCancellationChargeTxt,data.expected.cancelChargeTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge header text Actual & Expected don't match")

        //assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualCancellationChrgTxt,resultData.hotel.amendBooking.aboutToAmend.actualCancellationChrgTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup, Cancelation Charge Descriptive text Actual & Expected don't match")

        //If Amendments text
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualIfAmendmentsTxt,data.expected.ifAmendmentsTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  Amendments text Actual & Expected don't match")

        //All dates text
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualDatesTxt,data.expected.allDatesTxt, "After Amend Booking, Itinerary Page, Booked Items, Cancellation Popup,  All Dates text Actual & Expected don't match")

        //check in date and number of nights
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualDurationTxt.replaceAll(" ",""),resultData.hotel.amendBooking.itineraryAfterReduce.expdurationTxt.replaceAll(" ",""), "After Amend Booking, Itinerary Page, Booked Items,  actual check in and duration text Actual & Expected don't match")

        //Voucher Icon
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualDownloadVocherIconDispStatus,data.expected.dispStatus, "After Amend Booking, Itinerary Page, Booked Items, Download Voucher button Display Status Actual & Expected don't match")
        //Commission and percentage
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualcomPercentTxt,data.expected.commissionTxt, "After Amend Booking, Itinerary Page, Booked Items, Commission Percentage text Actual & Expected don't match")

        //Room rate amount and currency
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualPriceAndcurrency,resultData.hotel.amendBooking.aboutToAmend.actualTotalAmountAndCurrency, "After Amend Booking, Itinerary Page, Booked Items, Room Rate Amount And currency Actual & Expected don't match")

        //Traveller Details
        assertionEquals(resultData.hotel.amendBooking.itineraryAfterReduce.actualtravellerDetails,resultData.hotel.amendBooking.itineraryAfterReduce.expectedtravellerDetails, "After Amend Booking, Itinerary Page, Booked Items, Travellers Details Text Actual & Expected don't match")

    }

}
