package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.TransferTestResultData
import com.kuoni.qa.automation.page.ActivityPDPPage
import com.kuoni.qa.automation.page.ActivitySearchPage
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.activity.ActivitySearchResultsPage
import com.kuoni.qa.automation.page.activity.ActivitySearchToolPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.TransferSearchPage
import com.kuoni.qa.automation.page.transfers.TransferSearchResultsPage
import org.testng.Assert

import java.text.DecimalFormat


/**
 * Created by kmahas on 10/7/2016.
 */
abstract class MultipleModuleBaseSpec extends AmndOccpBaseSpec{

    protected def loginToApplicaiton(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData){
        login(clientData)
        at HotelSearchPage
        resultData.hotel.search.actualFindButtonStatus=getFindButtonDisabled()
    }

    protected def addItemsToItinerary(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        clickTransfersTab()
        at TransferSearchPage
        enterPickupInput(data.input.pickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(data.input.selectPickUp)
        enterDropoffInput(data.input.dropOff)
        sleep(1000)
        selectFromAutoSuggestDropOff(data.input.selectDropOff)
        sleep(1000)
        selectDateCalender(data.input.checkInDays.toInteger())
        sleep(1000)
        selectTime(data.input.hours,data.input.minutes)
        sleep(1000)
        scrollToTopOfThePage()
        enterPaxInput(data.input.pax,"0")
        clickFindButton()
        sleep(5000)

        at TransferSearchResultsPage

        if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
            clickShowCarTypes(data.input.itemRecordIndex)
            sleep(2000)
        }

        //Search Results returned
        resultData.hotel.searchResults.actualSearchResultsReturned=searchResultsReturned()
        //First Transfer Name
        //resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(0)
        resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
        //click on add to Itineary button
        //clickOnAddToItenary(0)
        clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
        sleep(5000)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)


        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at TransferSearchResultsPage

        //transfer itineary name
        resultData.hotel.searchResults.actualTransferItineraryName=getTransferItineraryItem(0)

        //Itinerary ID
        resultData.hotel.searchResults.actualItineraryId=gettheItenaryId()
        println("Itinerary id: $resultData.hotel.searchResults.actualItineraryId")

        //Item #2
        //Click on Hotels Tab
        clickonHotelsTab()
        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
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

        selectNumberOfAdults(data.input.hotelPax.toString(), 0)
        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage

        waitTillAddToItineraryBtnLoads()

        //search results returned
        resultData.hotel.searchResults.actualSearchResults=searchResultsReturned()
        //User able to add 2nd available room to itinerary
        resultData.hotel.searchResults.secondAvailableroom=getHotelNameBasedOnIndex(1)

        //click on add to Itineary button
        scrollToSpecificAddToItineraryBtnAndClick(2)
        sleep(3000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Hotel Item is added to itinerary
        //resultData.hotel.searchResults.actualHotelNameAdded=getHotelNameInTitleCard(1)
        resultData.hotel.searchResults.actualHotelNameAdded=getHotelNameInTitleCard(2)
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }

        at ActivitySearchToolPage

        clickActivitySearchTab()

        enterActivityDestination(data.input.activityTypeText)
        sleep(2000)
        selectFromAutoSuggest(data.input.activityAutoSuggest)

        //select Date
        clickOnIndiDate()
        sleep(3000)
        selectDayInCalender(data.input.checkInDays.toInteger()+1)

        selectPax()

        //select no.of adults
        selectNoAdults(data.input.hotelPax.toString())
        sleep(1000)
        clickFindButton()
        sleep(7000)

        at ActivityPDPPage

        //search results returned
        //resultData.hotel.activity.actualSearchResults=
        //activity name
        resultData.hotel.activity.activityName=getActivityNameOnPDP()

        clickOnAddToItinerary(0)
        sleep(3000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)


        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at ActivityPDPPage
        //resultData.hotel.activity.actualActivityName=verifyActivityNameOnBuilder(2)
        resultData.hotel.activity.actualActivityName=verifyActivityNameOnBuilder(3)
        at HotelSearchResultsPage
        clickTransfersTab()

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //collapse
            hideItenaryBuilder()
            sleep(3000)
        }

        at TransferSearchPage

        enterPickupInput(data.input.dropOff)
        sleep(1000)
        selectFromAutoSuggestPickUp(data.input.selectDropOff)
        enterDropoffInput(data.input.pickup)
        sleep(1000)
        selectFromAutoSuggestDropOff(data.input.selectPickUp)
        sleep(1000)
        selectDateCalender(data.input.checkInDays.toInteger()+2)
        sleep(3000)
        scrollToTopOfThePage()
        //at TransferSearchResultsPage
        //scrollToBottomOfThePage()
        at TransferSearchPage
        selectTime(data.input.transferHours,data.input.transfermins)
        sleep(1000)
        scrollToTopOfThePage()
        enterPaxInput(data.input.hotelPax,"0")
        clickFindButton()
        sleep(5000)

        at TransferSearchResultsPage
        if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
            clickShowCarTypes(data.input.itemRecordIndex)
            sleep(2000)
        }

        //Search Results returned
        resultData.hotel.searchResults.actSearchResultsReturned=searchResultsReturned()
        //First Transfer Name
        resultData.hotel.searchResults.firstTransfer=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
        //resultData.hotel.searchResults.firstTransfer=getTransferVehicleName(0)

        //scrollToAddToItineraryBtn(0)

        //click on add to Itineary button
        //clickOnAddToItenary(0)
        clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
        sleep(5000)
        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(4000)
        }
        at TransferSearchResultsPage
        sleep(3000)
        //transfer itineary name
        //resultData.hotel.searchResults.actTransferItineraryName=getTransferItineraryItem(0)
        resultData.hotel.searchResults.actTransferItineraryName=getTransferItineraryItem(4)



    }

    protected def addItemsToItinry(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        clickTransfersTab()
        at TransferSearchPage
        enterPickupInput(data.input.pickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(data.input.selectPickUp)
        enterDropoffInput(data.input.dropOff)
        sleep(1000)
        selectFromAutoSuggestDropOff(data.input.selectDropOff)
        sleep(1000)
        selectDateCalender(data.input.checkInDays.toInteger())
        sleep(1000)
        selectTime(data.input.hours,data.input.minutes)
        sleep(1000)
        scrollToTopOfThePage()
        enterPaxInput(data.input.pax,"0")
        clickFindButton()
        sleep(7000)
        waitTillLoadingIconDisappears()

        at TransferSearchResultsPage

        if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
            clickShowCarTypes(data.input.itemRecordIndex)
            sleep(2000)
        }

        //Search Results returned
        resultData.hotel.searchResults.actualSearchResultsReturned=searchResultsReturned()
        //First Transfer Name
        //resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(0)
        resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
        //click on add to Itineary button
        //clickOnAddToItenary(0)
        clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at TransferSearchResultsPage
        //transfer itineary name
        resultData.hotel.searchResults.actualTransferItineraryName=getTransferItineraryItem(0)

        //Itinerary ID
        resultData.hotel.searchResults.actualItineraryId=gettheItenaryId()
        println("Itinerary id: $resultData.hotel.searchResults.actualItineraryId")
        sleep(3000)
        //Item #2
        //Click on Hotels Tab
        clickonHotelsTab()
        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        entercheckInCheckOutDate(data.input.chkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
        sleep(3000)
        clickPaxRoom()
        sleep(3000)

        clickPaxNumOfRooms(data.input.noOfRooms.toString())
        sleep(3000)
        //Enter Pax
        clickPaxRoom()

        selectNumberOfAdults(data.input.hotelPax.toString(), 0)
        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage

        waitTillAddToItineraryBtnLoads()

        //search results returned
        resultData.hotel.searchResults.actualSearchResults=getHotelItemAvailabilityInSearchResults(data.input.hotelText)
        //User able to add 1st available room to itinerary
        resultData.hotel.searchResults.firstAvailableroom=getHotelItemNameInSearchResults()

        scrollToAddToItineraryBtn()
        //click on add to Itineary button
        clickAddToitinerary(0)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)
        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Hotel Item is added to itinerary
        resultData.hotel.searchResults.actualHotelNameAdded=getHotelNameInTitleCard(1)
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        scrollToTopOfThePage()
        at ActivitySearchToolPage

        clickActivitySearchTab()

        enterActivityDestination(data.input.activityTypeText)
        sleep(3000)
        selectFromAutoSuggest(data.input.activityAutoSuggest)

        //select Date
        clickOnIndiDate()
        sleep(5000)
        //selectDayInCalender(data.input.chkInDays.toInteger()+1)
        selectDayInCalender(data.input.activitychkInDays.toInteger()+1)
        //selectDayInCalender(30)

        selectPax()

        //select no.of adults
        selectNoAdults(data.input.hotelPax.toString())

        sleep(1000)
        clickFindButton()
        sleep(7000)

        //search results returned
        at ActivitySearchResultsPage

        //search results returned
        resultData.hotel.activity.actualSearchResults=getActivityItemAvailabilityInSearchResults(data.input.activitySelect)

        scrollToSpecificActivityAndClickAddToItinryBtn(data.input.activitySelect)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }

        at ActivityPDPPage
        resultData.hotel.activity.actualActivityName=verifyActivityNameOnBuilder(2)



    }

    protected def addItemsToItinery(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

        at HotelSearchPage
        clickTransfersTab()
        at TransferSearchPage
        enterPickupInput(data.input.pickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(data.input.selectPickUp)
        enterDropoffInput(data.input.dropOff)
        sleep(1000)
        selectFromAutoSuggestDropOff(data.input.selectDropOff)
        sleep(1000)
        selectDateCalender(data.input.checkInDays.toInteger())
        sleep(1000)
        selectTime(data.input.hours,data.input.minutes)
        sleep(1000)
        scrollToTopOfThePage()
        enterPaxInput(data.input.pax,"0")
        sleep(2000)
        selectNumberOfChildren(data.input.children.size().toString() , 0)
        sleep(2000)
        enterChildrenAge(data.input.children.getAt(0).toString(),"0",0)
        sleep(2000)
        clickFindButton()
        sleep(5000)

        at TransferSearchResultsPage

        if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
            clickShowCarTypes(data.input.itemRecordIndex)
            sleep(2000)
        }

        //Search Results returned
        resultData.hotel.searchResults.actualSearchResultsReturned=searchResultsReturned()
        //First Transfer Name
        //resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(0)
        resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
        //click on add to Itineary button
        //clickOnAddToItenary(0)
        clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at TransferSearchResultsPage
        //transfer itineary name
        resultData.hotel.searchResults.actualTransferItineraryName=getTransferItineraryItem(0)

        //Itinerary ID
        resultData.hotel.searchResults.actualItineraryId=gettheItenaryId()
        println("Itinerary id: $resultData.hotel.searchResults.actualItineraryId")
        sleep(3000)
        //at HotelSearchPage
        //Item #2
        //Click on Transfers Tab
        //clickTransfersTab()
        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //collapse
            hideItenaryBuilder()
            sleep(3000)
        }

        at TransferSearchPage

        enterPickupInput(data.input.pickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(data.input.selectPickUp)
        enterDropoffInput(data.input.dropOff)
        sleep(1000)
        selectFromAutoSuggestDropOff(data.input.selectDropOff)
        sleep(1000)
        selectDateCalender(data.input.checkInDays.toInteger())
        sleep(1000)
        //at TransferSearchResultsPage
        //scrollToBottomOfThePage()
        //at TransferSearchPage
        //selectTime(data.input.hours,data.input.minutes)
        //sleep(1000)
        scrollToTopOfThePage()
        enterPaxInput(data.input.pax,"0")

        clickFindButton()
        sleep(5000)

        at TransferSearchResultsPage

        if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
            clickShowCarTypes(data.input.itemRecordIndex)
            sleep(2000)
        }

        //Search Results returned
        resultData.hotel.searchResults.actualSearchResultsReturnedSecondItem=searchResultsReturned()
        //First Transfer Name
        //resultData.hotel.searchResults.firstTransfernameSecondItem=getTransferVehicleName(0)
        resultData.hotel.searchResults.firstTransfernameSecondItem=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
        //click on add to Itineary button
        //scrollToAddToItineraryBtn(0)
        //clickOnAddToItenary(0)
        clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)


        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at TransferSearchResultsPage
        //transfer itineary name
        resultData.hotel.searchResults.actualTransferItineraryNameSecondItem=getTransferItineraryItem(1)

        //Click on Hotels Tab
        clickonHotelsTab()
        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Enter Hotel Destination
        enterHotelDestination(data.input.cityAreaHotelTypeText)
        selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

        //Enter Check In and Check Out Dates
        entercheckInCheckOutDate(data.input.chkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
        sleep(3000)
        clickPaxRoom()
        sleep(3000)

        clickPaxNumOfRooms(data.input.noOfRooms.toString())
        sleep(3000)
        //Enter Pax
        clickPaxRoom()

        selectNumberOfAdults(data.input.hotelPax.toString(), 1)

        selectNumberOfChildren(data.input.children.size().toString() , 1)

        enterChildrenAge(data.input.children.getAt(0).toString(),"1", 0)

        sleep(1000)

        //at ItineraryTravllerDetailsPage
        //scrollToBottomOfThePage()
        at HotelSearchResultsPage
        selectNumberOfAdults(data.input.hotelPax.toString(), 0)

        clickPaxRoom()
        //click on Find button
        clickFindButton()
        sleep(7000)

        at HotelSearchResultsPage

        waitTillAddToItineraryBtnLoads()

        //search results returned
        resultData.hotel.searchResults.actualSearchResultsThirdItem=getHotelItemAvailabilityInSearchResults(data.input.hotelText)
        //User able to add 1st available room to itinerary
        resultData.hotel.searchResults.firstAvailableroomThirdItem=getHotelItemNameInSearchResults()

        scrollToAddToItineraryBtn()
        //click on add to Itineary button
        clickAddToitinerary(0)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at HotelSearchResultsPage

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        //Hotel Item is added to itinerary
        resultData.hotel.searchResults.actualHotelNameAddedThirdItem=getHotelNameInTitleCard(2)

        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage
        scrollToTopOfThePage()
        at ActivitySearchToolPage

        clickActivitySearchTab()

        enterActivityDestination(data.input.activityTypeText)
        sleep(2000)
        selectFromAutoSuggest(data.input.activityAutoSuggest)

        //select Date
        clickOnIndiDate()
        sleep(5000)
        //selectDayInCalender(data.input.chkInDays.toInteger()+1)
        selectDayInCalender(data.input.activitychkInDays.toInteger())
        //selectDayInCalender(30)

        selectPax()

        //select no.of adults
        selectNoAdults(data.input.activityPax.toString())
        sleep(3000)
        //select no.of children
        selectNoOfChild(data.input.children.size().toString())
        sleep(2000)
        //Enter children Age
        //enterAgeOfChild(0,data.input.children.getAt(0).toString())
        enterChildrenAge(data.input.children.getAt(0).toString(),"0",0)
        sleep(2000)
        clickFindButton()
        sleep(7000)

        //search results returned
        at ActivitySearchResultsPage

        //search results returned
        resultData.hotel.activity.actualSearchResults=getActivityAvailabilityInSearchResults(data.input.activitySelect)
        at HotelSearchResultsPage
        scrollToAddToItineraryBtn()
        at ActivitySearchResultsPage
        //scrollToSpecificActivityAndClickAddToItinryBtn(data.input.activitySelect)
        clickAddToitinerary(0)
        sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at HotelSearchResultsPage


        if(getItineraryBarSectionDisplayStatus()==false) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }

        at ActivityPDPPage
        resultData.hotel.activity.actualActivityName=verifyActivityNameOnBuilder(4)
         at HotelSearchResultsPage

         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at HotelSearchPage
         clickTransfersTab()
         at TransferSearchPage
         enterPickupInput(data.input.pickupitem5)
         sleep(1000)
         selectFromAutoSuggestPickUp(data.input.selectPickUpitem5)
         enterDropoffInput(data.input.dropOffitem5)
         sleep(1000)
         selectFromAutoSuggestDropOff(data.input.selectDropOffitem5)
         sleep(1000)
         selectDateCalender(data.input.checkInDaysitem5.toInteger())
         sleep(1000)
        scrollToTopOfThePage()
         //at ItineraryTravllerDetailsPage
         //scrollToBottomOfThePage()
         at TransferSearchPage
         selectTime(data.input.hoursitem5,data.input.minutesitem5)
        sleep(1000)

        scrollToTopOfThePage()
         enterPaxInput(data.input.paxitem5,"0")
            sleep(2000)
         selectNumberOfChildren(data.input.children.size().toString() , 0)
        sleep(2000)
         enterChildrenAge(data.input.children.getAt(0).toString(),"0",0)
        sleep(2000)
         clickFindButton()
         sleep(5000)

         at TransferSearchResultsPage
        if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
            clickShowCarTypes(data.input.itemRecordIndex)
            sleep(2000)
        }

         //Search Results returned
         resultData.hotel.searchResults.actualSearchResultsReturnedFifthItem=searchResultsReturned()
         //First Transfer Name
         //resultData.hotel.searchResults.firstTransfernameFifthItem=getTransferVehicleName(1)
        resultData.hotel.searchResults.firstTransfernameFifthItem=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
         //click on add to Itineary button
         //clickOnAddToItenary(0)
        //scrollToBottomOfThePage()
        //scrollToAddToItineraryBtn(1)
        //clickOnAddToItenary(1)
        clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
         sleep(5000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at TransferSearchResultsPage
         //transfer itineary name
         resultData.hotel.searchResults.actualTransferItineraryNamefifthItem=getTransferItineraryItem(5)

     }

    protected def "travellerDetails"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Input Title
        selectTitle(data.expected.title_txt,0)
        //Input First Name
        enterFirstName(data.expected.firstName,0)
        //Input Last Name
        enterLastName(data.expected.lastName,0)
        //Input Email Address
        enterEmail(data.expected.emailAddr,0)
        //Input Area Code
        //enterCountryCode(data.expected.countryCode,0)
        //sleep(1000)
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
        sleep(7000)

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


    }

    protected def addremainingTravellers(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //Add Travellers


            clickAddTravellersButton()
            sleep(5000)
            //Input Title
            selectTitle(data.expected.title_txt,0)
            //Input First Name
            //String travellerFName=hotelSearchData.expected.travellerfirstName+i
            String travellerFName=data.expected.travellerfirstName
            enterEditedFirstNameAndTabOut(travellerFName)
            //Input Last Name
            //String travellerLName=hotelSearchData.expected.travellerlastName+i
            String travellerLName=data.expected.travellerlastName
            enterEditedLastNameAndTabOut(travellerLName)
            sleep(3000)
            //Click on Save Button
            clickonSaveButton()

            sleep(6000)
            waitTillLoadingIconDisappears()
            waitTillTravellerDetailsSaved()

            //Capture the entered edit details
            //capture entered second traveller details are displayed correctly
            resultData.hotel.itineraryPage.expectedSecondTravellerName=data.expected.title_txt+" "+travellerFName+" "+travellerLName
            resultData.hotel.itineraryPage.actualSecondTravellerName=getTravellerNameInItineraryPage(1)

            sleep(2000)

            //3rd Traveller Details
            clickAddTravellersButton()
            sleep(5000)
            //Input Title
            selectTitle(data.expected.title_txt,0)
            //Input First Name
        //String travellerFName=hotelSearchData.expected.travellerfirstName+i
        String thirdtravellerFName=data.expected.travellerfirstName+"Third"
        enterEditedFirstNameAndTabOut(thirdtravellerFName)
        //Input Last Name
        //String travellerLName=hotelSearchData.expected.travellerlastName+i
        String thirdtravellerLName=data.expected.travellerlastName+"Third"
        enterEditedLastNameAndTabOut(thirdtravellerLName)
        sleep(3000)
        //Click on Save Button
        clickonSaveButton()

        sleep(6000)
        waitTillLoadingIconDisappears()
        waitTillTravellerDetailsSaved()

        //Capture the entered details
        //capture entered third traveller details are displayed correctly
        resultData.hotel.itineraryPage.expectedThirdTravellerName=data.expected.title_txt+" "+thirdtravellerFName+" "+thirdtravellerLName
        resultData.hotel.itineraryPage.actualThirdTravellerName=getTravellerNameInItineraryPage(2)

        scrollToTopOfThePage()


        //Fourth traveller
        clickAddTravellersButton()
        sleep(5000)
        //Input Title
        selectTitle(data.expected.title_txt,0)
        //Input First Name
        //String travellerFName=hotelSearchData.expected.travellerfirstName+i
        String fourthtravellerFName=data.expected.travellerfirstName+"Fourth"
        enterEditedFirstNameAndTabOut(fourthtravellerFName)
        //Input Last Name
        //String travellerLName=hotelSearchData.expected.travellerlastName+i
        String fourthtravellerLName=data.expected.travellerlastName+"Fourth"
        enterEditedLastNameAndTabOut(fourthtravellerLName)
        sleep(3000)
        //Click on Save Button
        clickonSaveButton()

        sleep(6000)
        waitTillLoadingIconDisappears()
        waitTillTravellerDetailsSaved()

        //Capture the entered details
        //capture entered Fourth traveller details are displayed correctly
        resultData.hotel.itineraryPage.expectedFourthTravellerName=data.expected.title_txt+" "+fourthtravellerFName+" "+fourthtravellerLName
        resultData.hotel.itineraryPage.actualFourthTravellerName=getTravellerNameInItineraryPage(3)

        scrollToTopOfThePage()


        //Fifth traveller
        clickAddTravellersButton()
        sleep(5000)

        //Select radio button child
        ClickRadioButtonAdultOrChild(2)
        sleep(2000)
        //Input Title
        //selectTitle(data.expected.title_txt,0)
        //Input First Name
        //String travellerFName=hotelSearchData.expected.travellerfirstName+i
        String fifthhtravellerFName=data.expected.childFirstName+"Fifth"
        enterEditedFirstNameAndTabOut(fifthhtravellerFName)
        //Input Last Name
        //String travellerLName=hotelSearchData.expected.travellerlastName+i
        String fifthtravellerLName=data.expected.childLastName+"Fifth"
        enterEditedLastNameAndTabOut(fifthtravellerLName)
        sleep(3000)
       //Enter Age
        enterEditedChildAgeAndTabOut(data.input.children.getAt(0).toString())
        sleep(3000)

        //Click on Save Button
        clickonSaveButton()
        sleep(6000)
        waitTillLoadingIconDisappears()
        waitTillTravellerDetailsSaved()
        sleep(2000)
        //clickOnExpandTraveller()

        //Capture the entered details
        //capture entered Fifth traveller details are displayed correctly
        //resultData.hotel.itineraryPage.expectedFifthTravellerName=data.expected.title_txt+" "+fifthhtravellerFName+" "+fifthtravellerLName+" "+"("+data.input.children.getAt(0).toString()+"yrs)"
        resultData.hotel.itineraryPage.expectedFifthTravellerName=fifthhtravellerFName+" "+fifthtravellerLName+" "+"("+data.input.children.getAt(0).toString()+"yrs)"
        resultData.hotel.itineraryPage.actualFifthTravellerName=getTravellerNameInItineraryPage(4)


    }

    protected def Item4Hotel(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData){
         at HotelSearchResultsPage

         if(getItineraryBarSectionDisplayStatus()==true) {
             at ItenaryBuilderPage
             //Expand
             hideItenaryBuilder()
             sleep(3000)
         }
         at HotelSearchResultsPage
         //Enter Hotel Destination
         enterHotelDestination(data.input.item4cityAreaHotelTypeText)
         selectFromAutoSuggest(data.input.item4cityAreaHotelautoSuggest)

         //Enter Check In and Check Out Dates
         entercheckInCheckOutDate(data.input.chkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
         sleep(3000)
         clickPaxRoom()
         sleep(3000)

         clickPaxNumOfRooms(data.input.noOfRooms.toString())
         sleep(3000)
         //Enter Pax
         clickPaxRoom()

         selectNumberOfAdults(data.input.hotelPax.toString(), 0)
         clickPaxRoom()
         //click on Find button
         clickFindButton()
         sleep(7000)

         at HotelSearchResultsPage

         waitTillAddToItineraryBtnLoads()

         //search results returned
         resultData.hotel.searchResults.actualItem4SearchResults=getHotelItemAvailabilityInSearchResults(data.input.item4hotelText)
         //User able to add 1st available room to itinerary
         resultData.hotel.searchResults.firstAvailableroomItem4=getHotelItemNameInSearchResults()

         scrollToAddToItineraryBtn()
         //click on add to Itineary button
         clickAddToitinerary(0)
         sleep(5000)

        at ItineraryTravllerDetailsPage
        selectOptionFromManageItinerary(data.input.manageItineraryValue)
        sleep(3000)
        at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Expand
             hideItenaryBuilder()
             sleep(3000)
         }
        at HotelSearchResultsPage
         //Hotel Item is added to itinerary
         resultData.hotel.searchResults.actualHotelNameAddedItem4=getHotelNameInTitleCard(3)
         if(getItineraryBarSectionDisplayStatus()==true) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
     }

     protected def addItemsToItineraryMultiRoom(ClientData clientData, HotelSearchData data, HotelTransferTestResultData resultData) {

         at HotelSearchPage
         clickTransfersTab()
         at TransferSearchPage
         enterPickupInput(data.input.pickup)
         sleep(1000)
         selectFromAutoSuggestPickUp(data.input.selectPickUp)
         enterDropoffInput(data.input.dropOff)
         sleep(1000)
         selectFromAutoSuggestDropOff(data.input.selectDropOff)
         sleep(1000)
         selectDateCalender(data.input.checkInDays.toInteger())
         sleep(1000)
         selectTime(data.input.hours,data.input.minutes)
         sleep(1000)
         scrollToTopOfThePage()
         enterPaxInput(data.input.pax,"0")
         clickFindButton()
         sleep(5000)

         at TransferSearchResultsPage

         if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
             clickShowCarTypes(data.input.itemRecordIndex)
             sleep(2000)
         }

         try{
             //First Transfer Name

             resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
         }catch(Exception e)
         {

             Assert.assertFalse(true,"Unable to fetch Transfer Name from UI")
         }

         //Search Results returned
         resultData.hotel.searchResults.actualSearchResultsReturned=searchResultsReturned()

         //resultData.hotel.searchResults.firstTransfername=getTransferVehicleName(0)
         //click on add to Itineary button
         //clickOnAddToItenary(0)
         clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
         sleep(7000)
         at ItineraryTravllerDetailsPage
         clickOnBackButtonInItineraryScreen()
         sleep(3000)

         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at TransferSearchResultsPage
         //transfer itineary name
         resultData.hotel.searchResults.actualTransferItineraryName=getTransferItineraryItem(0)

         //Itinerary ID
         resultData.hotel.searchResults.actualItineraryId=gettheItenaryId()
         println("Itinerary id: $resultData.hotel.searchResults.actualItineraryId")

         at ItenaryBuilderPage
         //hideItenaryBuilder()
         resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice=getItenaryPrice(0)

         at HotelSearchResultsPage

         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at TransferSearchResultsPage
         //Item #2
         //scrollToAddToItineraryBtn(0)
         if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
             clickShowCarTypes(data.input.itemRecordIndex)
             sleep(2000)
         }
         //click on add to Itineary button
         //clickOnAddToItenary(0)
         // sleep(7000)

         clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
         sleep(7000)
         at ItineraryTravllerDetailsPage
         clickOnBackButtonInItineraryScreen()
         sleep(3000)
         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at TransferSearchResultsPage
         //transfer itineary name
         //resultData.hotel.searchResults.actualAddedTransferItineraryName=getTransferItineraryItem(1)
         resultData.hotel.searchResults.actualAddedTransferItineraryName=getTransferItineraryItem(2)

         //Item # 3(multi Rooms)
         //Click on Hotels Tab
         clickonHotelsTab()
         at HotelSearchResultsPage

         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at HotelSearchResultsPage
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
         //clickPaxRoom()

         selectNumberOfAdults(data.input.hotelPax.toString(), 1)
         //scrollToBottomOfThePage()
         selectNumberOfAdults(data.input.hotelPax.toString(), 0)
         clickPaxRoom()
         //click on Find button
         clickFindButton()
         sleep(7000)

         at HotelSearchResultsPage

         waitTillAddToItineraryBtnLoads()

         //search results returned
         resultData.hotel.searchResults.actualSearchResults=getHotelItemAvailabilityInSearchResults(data.input.cityAreaHotelTypeText)

         //User able to add recommended room to itinerary
         resultData.hotel.searchResults.recommendedroom=getHotelItemNameInSearchResults()

         //click on add to Itineary button
         scrollToBottomOfThePage()

         clickAddToitinerary(0)
         sleep(7000)

         at ItineraryTravllerDetailsPage
         clickOnBackButtonInItineraryScreen()
         sleep(3000)
         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at HotelSearchResultsPage
         //Hotel Item is added to itinerary
         //resultData.hotel.searchResults.actualHotelNameAdded=getHotelNameInTitleCard(2)
         resultData.hotel.searchResults.actualHotelNameAdded=getHotelNameInTitleCard(4)

         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }

         at ActivitySearchToolPage

         //#Item 4

         clickActivitySearchTab()

         enterActivityDestination(data.input.activityTypeText)
         sleep(2000)
         selectFromAutoSuggest(data.input.activityAutoSuggest)

         //select Date
         clickOnIndiDate()
         sleep(3000)
         selectDayInCalender(data.input.checkInDays.toInteger()+1)

         selectPax()

         //select no.of adults
         selectNoAdults(data.input.pax.toString())
         sleep(1000)
         clickFindButton()
         sleep(7000)

         //search results returned
         at HotelSearchResultsPage

         //search results returned
         resultData.hotel.activity.actualSearchResults=getHotelItemAvailabilityInSearchResults(data.input.activityTypeText)

         at ActivityPDPPage
         //activity name
         resultData.hotel.activity.activityName=getActivityNameOnPDP()

         clickOnAddToItinerary(0)
         sleep(3000)

         at ItineraryTravllerDetailsPage
         clickOnBackButtonInItineraryScreen()
         sleep(3000)

         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at ActivityPDPPage
         //resultData.hotel.activity.actualActivityName=verifyActivityNameOnBuilder(4)
         resultData.hotel.activity.actualActivityName=verifyActivityNameOnBuilder(6)
         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         //#Item 5

         at ActivitySearchToolPage

         //clickActivitySearchTab()

         enterActivityDestination(data.input.activityTypeText_Second)
         sleep(2000)
         selectFromAutoSuggest(data.input.activityAutoSuggest_Second)

         //select Date
         clickOnIndiDate()
         sleep(3000)
         selectDayInCalender(data.input.checkInDays.toInteger()+3)

         selectPax()
         sleep(3000)
         //select no.of adults
         selectNoAdults(data.input.pax.toString())
         sleep(1000)
         clickFindButton()
         sleep(7000)

         //search results returned
         at HotelSearchResultsPage

         //search results returned
         resultData.hotel.activity.actualSearchResults_Second=getHotelItemAvailabilityInSearchResults(data.input.activityTypeText_Second)

         at ActivityPDPPage
         //activity name
         resultData.hotel.activity.activityName_Second=getActivityNameOnPDP()

         clickOnAddToItinerary(0)
         sleep(3000)

         at ItineraryTravllerDetailsPage
         clickOnBackButtonInItineraryScreen()
         sleep(3000)

         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at ActivityPDPPage
         //resultData.hotel.activity.actualActivityName_Second=verifyActivityNameOnBuilder(5)
         resultData.hotel.activity.actualActivityName_Second=verifyActivityNameOnBuilder(7)

         //#Item 6
         at HotelSearchResultsPage
         clickTransfersTab()
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at TransferSearchPage
         enterPickupInput(data.input.dropOff)
         sleep(1000)
         selectFromAutoSuggestPickUp(data.input.selectDropOff)
         enterDropoffInput(data.input.pickup)
         sleep(1000)
         selectFromAutoSuggestDropOff(data.input.selectPickUp)
         sleep(1000)
         selectDateCalender(data.input.checkOutDays.toInteger())
         sleep(1000)
         selectTime(data.input.transferHours,data.input.transfermins)
         sleep(1000)
         //scrollToTopOfThePage()
         enterPaxInput(data.input.hotelPax,"0")
         clickFindButton()
         sleep(5000)

         at TransferSearchResultsPage
         if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
             clickShowCarTypes(data.input.itemRecordIndex)
             sleep(2000)
         }

         //Search Results returned
         resultData.hotel.searchResults.searchResults=searchResultsReturned()
        //First Transfer Name
         resultData.hotel.searchResults.Transfername=getTransferVehicleName(data.input.itemRecordIndex,data.input.transferIndex)
         //First Transfer Name
         //resultData.hotel.searchResults.Transfername=getTransferVehicleName(0)
         //click on add to Itineary button
         //scrollToAddToItineraryBtn(0)
         //clickOnAddToItenary(0)
         clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
         sleep(7000)
         at ItineraryTravllerDetailsPage
         clickOnBackButtonInItineraryScreen()
         sleep(3000)

         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at TransferSearchResultsPage
         if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
             clickShowCarTypes(data.input.itemRecordIndex)
             sleep(2000)
         }
         //transfer itineary name
         //resultData.hotel.searchResults.actualTransferName=getTransferItineraryItem(6)
         resultData.hotel.searchResults.actualTransferName=getTransferItineraryItem(8)

         //Item #7
         //click on add to Itineary button
         //scrollToAddToItineraryBtn(0)
         //clickOnAddToItenary(0)
         clickOnAddToItenary(data.input.itemRecordIndex,data.input.transferIndex)
         sleep(7000)
         at ItineraryTravllerDetailsPage
         clickOnBackButtonInItineraryScreen()
         sleep(3000)

         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==false) {
             at ItenaryBuilderPage
             //Collapse
             hideItenaryBuilder()
             sleep(3000)
         }
         at TransferSearchResultsPage

         //transfer itineary name
         //resultData.hotel.searchResults.actualAddTransferItineraryName=getTransferItineraryItem(7)
         resultData.hotel.searchResults.actualAddTransferItineraryName=getTransferItineraryItem(9)
         sleep(3000)



     }

    protected def "goToItinery"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItenaryBuilderPage
        //hideItenaryBuilder()

        //click Go to Itinerary Link
        clickOnGotoItenaryButton()
        sleep(5000)

        at ItineraryTravllerDetailsPage

        //Capture Traveller Label Text
        //resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravllerLabelTextInTravellerDetailsPage()
        //resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravellelersLabelName(1)
        String travlrLablTxt=getTravellelersLabelName(0)
        resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=travlrLablTxt.trim()
        resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt=data.expected.travlrLabelText.toString()+" (lead)"
    }

     protected def "goToItinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

         at HotelSearchResultsPage
         if(getItineraryBarSectionDisplayStatus()==true) {
             at ItenaryBuilderPage
             //Expand
             hideItenaryBuilder()
             sleep(3000)
         }

         at ItenaryBuilderPage
         //hideItenaryBuilder()

         resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice=getItenaryPrice(0)

             //click Go to Itinerary Link
         clickOnGotoItenaryButton()
         sleep(5000)

         at ItineraryTravllerDetailsPage

         //Capture Traveller Label Text
         //resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravllerLabelTextInTravellerDetailsPage()
         String travlrLablTxt=getTravellelersLabelName(0)
         resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=travlrLablTxt.trim()
         resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt=data.expected.travlrLabelText.toString()+" (lead)"

     }

     protected def "goToItinry"(HotelSearchData data, HotelTransferTestResultData resultData){

         at ItenaryBuilderPage
         //click Go to Itinerary Link
         clickOnGotoItenaryButton()
         sleep(5000)

         at ItineraryTravllerDetailsPage

         //Capture Traveller Label Text
         //resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravllerLabelTextInTravellerDetailsPage()
         //resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravellerDetailsLabels(0)
         resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=getTravellelersLabelName(0)
         resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt=data.expected.travlrLabelText.toString()+" (lead)"
     }

     protected def "itineraryNameUpdate"(HotelSearchData data, HotelTransferTestResultData resultData){

         at ItenaryPageItems
         //click on Edit
         scrollToTopOfThePage()
         clickEditIconNextToItineraryHeader()
         sleep(3000)
         waitTillLoadingIconDisappears()
         //Update Name Text
         String todaysDate= dateUtil.addDaysChangeDatetformat(0, "ddMMMyy")
         println("$todaysDate")
         resultData.hotel.itineraryPage.expectedItnrName=todaysDate+"-"+data.expected.itineraryName
         enterItenaryName(resultData.hotel.itineraryPage.expectedItnrName)

         //Click save button
         clickSaveButton()
         sleep(4000)
         waitTillLoadingIconDisappears()
         //capture entered Itinerary Name
         //Capture Itinerary Page - header
         String edtditineraryPageTitle = getItenaryName()
         //println("$itineraryPageTitle")
         List<String> tList=edtditineraryPageTitle.tokenize(" ")
         String edtitinaryName=tList.getAt(2)
         resultData.hotel.itineraryPage.actualSavedItnrName=edtitinaryName.replaceAll("\nEdit", "")



     }

     protected def "bookItem"(HotelSearchData data, HotelTransferTestResultData resultData,int bkdItmTransfrIndx,int bkdItmscndTransfrIndx,int bkdItmHotelIndx,int bkdItmActIndx, int bkdItmlastTransfrIndx){
         at ItineraryTravllerDetailsPage
        //first item - book
         //first item is not displayed as first
         clickOnBookingIcon(0)

         //clickOnBookingIcon(1)
         sleep(5000)

         waitTillLoadingIconDisappears()
         if(getTravellerCheckBoxCheckedStatus(0)==false) {
             //Selecting travellers 1,2
             clickOnTravellerCheckBox(0)
             sleep(3000)
         }
         if(getTravellerCheckBoxCheckedStatus(1)==false) {
             clickOnTravellerCheckBox(1)
             sleep(3000)
         }
         //Input Flight Number
         enterPickUpFlightNumber(data.expected.flightNumber)

         //Input Arriving From
         enterArrivalFrom(data.expected.arrivingText)
         sleep(3000)
         //Auto Suggest select
         selectArrivalAutoSuggest(data.expected.arrivingFrom)
         //Input time of arrival - hrs
         enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
         //Input time of arrival - mins
         enterArrivalMins(data.expected.timeOfArrival_mins)

         //Click on Confirm Booking
         //clickConfirmBooking()
         clickOnConfirmBookingAndPayNow()

         //wait for confirmation page
         waitTillConformationPage()
         sleep(5000)
         scrollToTopOfThePage()

         //Booking Confirmation First Item Screen Display Status
         resultData.hotel.itineraryPage.actualFirstItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

         //click on Close lightbox X function
         coseBookItenary()
         sleep(7000)


         //Click on cancellation charge link
         //clickOnCancellationChargesLink(0)
         clickOnCancellationChargesLink(4)
         sleep(3000)

         //Cancellation policy text
         resultData.hotel.itineraryPage.retrieveFirstTransferCancellationPolicyText=getCancellationPolicyPopupInnerTxt()

         //click on close
         clickOnCloseLightBox()

         //first item - 2nd part Book
         clickOnBookingIcon(0)
         sleep(5000)

         waitTillLoadingIconDisappears()

         if(getTravellerCheckBoxCheckedStatus(0)==false) {
             //Selecting travellers 1,2
             clickOnTravellerCheckBox(0)
             sleep(3000)
         }
         if(getTravellerCheckBoxCheckedStatus(1)==false) {
             clickOnTravellerCheckBox(1)
             sleep(3000)
         }

         //Input Flight Number
         enterPickUpFlightNumber(data.expected.flightNumber)

         //Input Arriving From
         enterArrivalFrom(data.expected.arrivingText)
         sleep(3000)
         //Auto Suggest select
         selectArrivalAutoSuggest(data.expected.arrivingFrom)
         //Input time of arrival - hrs
         enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
         //Input time of arrival - mins
         enterArrivalMins(data.expected.timeOfArrival_mins)

         //Click on Confirm Booking
         //clickConfirmBooking()
         clickOnConfirmBookingAndPayNow()
         //wait for confirmation page
         waitTillConformationPage()
         sleep(5000)
         scrollToTopOfThePage()

         //Booking Confirmation First Item 2nd Part Screen Display Status
         resultData.hotel.itineraryPage.actualFirstItemScndPartBookingConfrm=getBookingConfirmationScreenDisplayStatus()

         //click on Close lightbox X function
         coseBookItenary()
         sleep(7000)


         //Click on cancellation charge link
         //clickOnCancellationChargesLink(0)
         clickOnCancellationChargesLink(3)
         sleep(3000)

         //Cancellation policy text
         resultData.hotel.itineraryPage.retrieveFirstTransferScndPartCancellationPolicyText=getCancellationPolicyPopupInnerTxt()

         //click on close
         clickOnCloseLightBox()

         //2nd Item - Book
         clickOnBookingIcon(0)
         sleep(5000)

         waitTillLoadingIconDisappears()

         if(getTravellerCheckBoxCheckedStatus(0)==false) {
             //Selecting travellers 1,2
             clickOnTravellerCheckBox(0)
             sleep(3000)
         }
         if(getTravellerCheckBoxCheckedStatus(1)==false) {
             clickOnTravellerCheckBox(1)
             sleep(3000)
         }

         //Click on Confirm Booking
         //clickConfirmBooking()
         clickOnConfirmBookingAndPayNow()
         //wait for confirmation page
         waitTillConformationPage()
         sleep(5000)
         scrollToTopOfThePage()

         //Booking Confirmation Second Item Screen Display Status
         resultData.hotel.itineraryPage.actualSecondItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

         //click on Close lightbox X function
         coseBookItenary()
         sleep(7000)

         //Click on cancellation charge link
         clickOnCancellationChargesLink(4)
         sleep(3000)

         //Cancellation policy text
         resultData.hotel.itineraryPage.retrieveSecondHotelCancellationPolicyText=getCancellationPolicyPopupInnerTxt()

         //click on close
         clickOnCloseLightBox()
         sleep(2000)

         //third item - book
         clickOnBookingIcon(0)
         sleep(5000)

         waitTillLoadingIconDisappears()

         if(getTravellerCheckBoxCheckedStatus(0)==false) {
             //Selecting travellers 1,2
             clickOnTravellerCheckBox(0)
             sleep(3000)
         }
         if(getTravellerCheckBoxCheckedStatus(1)==false) {
             clickOnTravellerCheckBox(1)
             sleep(3000)
         }
         //select language
         selectLanguage(data.expected.language)
         sleep(1000)

         //Click on Confirm Booking
         //clickConfirmBooking()
         clickOnConfirmBookingAndPayNow()
         //wait for confirmation page
         waitTillConformationPage()
         sleep(5000)
         scrollToTopOfThePage()

         //Booking Confirmation Third Item Screen Display Status
         resultData.hotel.itineraryPage.actualThirdItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()
         //Read itinerary id

         String actualItineraryId=getItinearyIdInBookingConf(0)
         resultData.hotel.itineraryPage.retrievedItineraryIdName=actualItineraryId
         println("Itinerary Id: $actualItineraryId")
         List<String> tempList=actualItineraryId.tokenize(" ")
         println("tokenized id: $tempList")
         resultData.hotel.itineraryPage.actualItineraryId=tempList.getAt(1).trim()
         println("Actual Itinerary ID $resultData.hotel.itineraryPage.actualItineraryId")
         //click on Close lightbox X function
         coseBookItenary()
         sleep(7000)

         //Fourth item - book
         sleep(5000)
         scrollToTopOfThePage()
         clickOnBookingIcon(0)
          sleep(5000)

          waitTillLoadingIconDisappears()

         if(getTravellerCheckBoxCheckedStatus(0)==false) {
             //Selecting travellers 1,2
             clickOnTravellerCheckBox(0)
             sleep(3000)
         }
         if(getTravellerCheckBoxCheckedStatus(1)==false) {
             clickOnTravellerCheckBox(1)
             sleep(3000)
         }
          //Input Flight Number
          enterFlightNumber(data.expected.newFlightNum)

          //Input Departing To
          enterAndSelectDepartingTo(data.expected.arrivingText,data.expected.arrivingFrom)
          //Input time of arrival - hrs
          enterDEpartureTime(data.expected.newtimeOfArrival_Hrs)
          //Input time of arrival - mins
          enterDEpartureTimeMins(data.expected.timeOfArrival_mins)

          //Click on Confirm Booking
          //clickConfirmBooking()
         clickOnConfirmBookingAndPayNow()

          //wait for confirmation page
          waitTillConformationPage()
          sleep(5000)
          scrollToTopOfThePage()

          //Booking Confirmation Fourth Item Screen Display Status
          resultData.hotel.itineraryPage.actualFourthItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

          //click on Close lightbox X function
          coseBookItenary()
          sleep(7000)

         //Click on cancellation charge link
         clickOnCancellationChargesLink(4)
         sleep(3000)

         //Cancellation policy text
         resultData.transfer.itineraryPage.retrieveFourthTransferCancellationPolicyText=getCancellationPolicyPopupInnerTxt()

         //click on close
         clickOnCloseLightBox()
         sleep(3000)


         //Retrieve Booking id
         //resultData.hotel.itineraryPage.retrieveThirdBookingId=getBookingIDBookedDetailsScrn(3)
         resultData.hotel.itineraryPage.retrieveThirdBookingId= getBookingIDBookedDetailsScrn(bkdItmActIndx+1)
         //Retrieve Third Image Src URL
         //resultData.hotel.itineraryPage.retrieveThirdBookingItemImageSrcURL=getItemsImageURLInItinerary(3)
         resultData.hotel.itineraryPage.retrieveThirdBookingItemImageSrcURL=getItemsImageURLInItinerary(bkdItmActIndx)

         //Retrieve Third item Name
         resultData.hotel.itineraryPage.retrieveThirdItemName=getTransferNameInSuggestedItem(bkdItmActIndx)
         //Cancellation Text
         //resultData.hotel.itineraryPage.retrieveThirditemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(3)
         resultData.hotel.itineraryPage.retrieveThirditemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(bkdItmActIndx)
         //Duration Icon
         //resultData.hotel.itineraryPage.retrieveThirditemDurationIconStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("time",3)
         resultData.hotel.itineraryPage.retrieveThirditemDurationIconStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("time",bkdItmActIndx)
         //Duration Value
         resultData.hotel.itineraryPage.retrieveThirditemDurationValue=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",3)

         //Language Icon
         resultData.hotel.itineraryPage.retrieveThirditemLangIconStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("language",3)
         //Language Value
         resultData.hotel.itineraryPage.retrieveThirditemLangValue=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",3)

         //service date & Pax
         resultData.hotel.itineraryPage.retrieveThirditemServiceDateAndPax=getDateTimePaxDetails(3)

         //Commission and %
         resultData.hotel.itineraryPage.retrieveThirditemCommissionAndPercent=getCommisionAndPercentageInBookeddetailsScrn(3)

         //Amount & Currency
         resultData.hotel.itineraryPage.retrieveThirditemAmountAndCurrency=getItenaryPriceInBookedItem(4)
         //Traveller name list
         resultData.hotel.itineraryPage.retrieveThirditemTravellers=getTravelerNamesMicros(2)


         //Retrieve First Item Details

         //Retrieve Booking id
         resultData.hotel.itineraryPage.retrieveFirstBookingId=getBookingIDBookedDetailsScrn(1)
         //Retrieve First Image Src URL
         resultData.hotel.itineraryPage.retrieveFirstBookingItemImageSrcURL=getItemsImageURLInItinerary(0)
         //Retrieve First item type of car
         resultData.hotel.itineraryPage.retrieveFirstItemCarType=getTransferNameInSuggestedItem(0)
         //Retrieve First Pax
         resultData.hotel.itineraryPage.retrieveFirstTransferitemPax=getTransfersItemsPax(0)
         //Retrieve First Item title
         resultData.hotel.itineraryPage.retrieveFirstTransferitemTitle=getTransfersItemTitle(0)
         //Cancellation Text
         resultData.hotel.itineraryPage.retrieveFirstTransferitemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(0)
         //Cancellation Link
         resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLink=getCancellationLinkExistenceInItinerary(0)
         //Cancellation Link Icon
         resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(0)

//duration icon
         resultData.hotel.itineraryPage.retrieveFirstItemDurationIconDisplayStatus=verifyItineryItemIconsDisplayed("time",0)
         //duration value
         resultData.hotel.itineraryPage.retrieveFirstItemDurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",0)

         //luggage icon
         resultData.hotel.itineraryPage.retrieveFirstItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplayed("luggage",0)
         //luggage value
         resultData.hotel.itineraryPage.retrieveFirstItemLuggageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("luggage",0)

         //pax icon
         resultData.hotel.itineraryPage.retrieveFirstItemPaxIconDisplayStatus=verifyItineryItemIconsDisplayed("pax",0)
         //Pax value
         resultData.hotel.itineraryPage.retrieveFirstItempaxIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("pax",0)

         try{
             //supporting language icon
             resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplayed("language",0)
             //Language value
             resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",0)

         }catch(Exception e){
             //supporting language icon
             resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconDisplayStatus="Unable To Read Icon Disp Status From UI Before Cancel"
             //Language value
             resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconText="Unable To Read Icon Text From UI Before Cancel"

         }

         //service date
         //service time
         resultData.hotel.itineraryPage.retrieveFirstItemServiceDateTime=getCheckInDetails(0)

         //commission and % value
         resultData.hotel.itineraryPage.retrieveFirstItemCommissionPercentAndValue=getCommisionAndPercentageInBookeddetailsScrn(0)
         //total service item amount and currency
         resultData.hotel.itineraryPage.retrieveFirstItemTotalAmountAndCurncy=getSuggestedItemsItenaryPrice(0)

         //Traveller name list
         resultData.hotel.itineraryPage.retrieveFirstItemTravellerNameList=getTravelerNamesMicros(0)

         //pick up details
         resultData.hotel.itineraryPage.retrieveFirstItemPickUpDetails=getPickUpAndDropOffDetails_BookedItems(0)
         //drop off details
         resultData.hotel.itineraryPage.retrieveFirstItemDropOffDetails=getPickUpAndDropOffDetails_BookedItems(1)

//Retrieve Second Item Details

         //Retrieve Booking id
         //resultData.hotel.itineraryPage.retrieveSecondBookingId=getBookingIDBookedDetailsScrn(2)
         resultData.hotel.itineraryPage.retrieveSecondBookingId=getBookingIDBookedDetailsScrn(bkdItmHotelIndx+1)
        //Retrieve Second Image Src URL
         //resultData.hotel.itineraryPage.retrieveSecondBookingItemImageSrcURL=getItemsImageURLInItinerary(1)
         resultData.hotel.itineraryPage.retrieveSecondBookingItemImageSrcURL=getItemsImageURLInItinerary(bkdItmHotelIndx)
        //Retrieve Second item Name
         //resultData.hotel.itineraryPage.retrieveSecondItemName=getTransferNameInSuggestedItem(1)
         resultData.hotel.itineraryPage.retrieveSecondItemName=getTransferNameInSuggestedItem(bkdItmHotelIndx)
         //Retrieve hotel star rating
         resultData.hotel.itineraryPage.retrieveSecondItemHotelStarRatng=getRatingForTheHotelInSuggestedItem(0)

         //String roomdescComplTxt_first=getHotelRoomTypePaxMealBasisDetails(1)
         String roomdescComplTxt_first=getHotelRoomTypePaxMealBasisDetails(bkdItmHotelIndx)
         List<String> tempItineraryDescList_first=roomdescComplTxt_first.tokenize(",")

         String roomDescText_first=tempItineraryDescList_first.getAt(0)
         //room type name
         resultData.hotel.itineraryPage.retrieveSecondItemroomdescTxt=roomDescText_first.trim()
         //Pax number
         String tmpTxt_first=tempItineraryDescList_first.getAt(1)
         List<String> tempDescList_first=tmpTxt_first.tokenize(".")

         String paxNum_first=tempDescList_first.getAt(0)
         resultData.hotel.itineraryPage.retrieveSecondItemPaxNum=paxNum_first.trim()

         //Rate plan - meal basis
         String ratePlantxt_first=tempDescList_first.getAt(1)
         resultData.hotel.itineraryPage.retrieveSecondItemMealbasis=ratePlantxt_first.trim()

         //Cancellation Text
         //resultData.hotel.itineraryPage.retrieveSeconditemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(1)
         resultData.hotel.itineraryPage.retrieveSeconditemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(bkdItmHotelIndx)
         //Cancellation Link
         //resultData.hotel.itineraryPage.retrieveSeconditemCancelLink=getCancellationLinkExistenceInItinerary(1)
         resultData.hotel.itineraryPage.retrieveSeconditemCancelLink=getCancellationLinkExistenceInItinerary(bkdItmHotelIndx)
         //Cancellation Link Icon
         //resultData.hotel.itineraryPage.retrieveSeconditemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(1)
         resultData.hotel.itineraryPage.retrieveSeconditemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(bkdItmHotelIndx)

         //check in date - nights
         //resultData.hotel.itineraryPage.retrieveSecondItemChkInDateNights=getCheckInDetails(1)
         resultData.hotel.itineraryPage.retrieveSecondItemChkInDateNights=getCheckInDetails(bkdItmHotelIndx)

         //commission and % value
         //resultData.hotel.itineraryPage.retrieveSecondItemCommissionPercentAndValue=getCommisionAndPercentageInBookeddetailsScrn(1)
         resultData.hotel.itineraryPage.retrieveSecondItemCommissionPercentAndValue=getCommisionAndPercentageInBookeddetailsScrn(bkdItmHotelIndx)
         //total service item amount and currency
         //resultData.hotel.itineraryPage.retrieveSecondItemTotalAmountAndCurncy=getItenaryPriceInBookedItem(2)
         resultData.hotel.itineraryPage.retrieveSecondItemTotalAmountAndCurncy=getItenaryPriceInBookedItem(bkdItmHotelIndx+1)

         //Traveller name list
         //resultData.hotel.itineraryPage.retrieveSecondItemTravellerNameList=getTravelerNamesMicros(1)
         resultData.hotel.itineraryPage.retrieveSecondItemTravellerNameList=getTravelerNamesMicros(bkdItmHotelIndx)

         //Add a Remark or Comment Text
         //resultData.hotel.itineraryPage.retrieveSecondItemAddRemarkOrCommentText=getHotelAddRemarkOrCommentText(1)
         resultData.hotel.itineraryPage.retrieveSecondItemAddRemarkOrCommentText=getHotelAddRemarkOrCommentText(bkdItmHotelIndx)


       //Retrive Fourth Item Details
         //Retrieve Booking id
         //resultData.transfer.itineraryPage.retrieveFourthBookingId=getBookingIDBookedDetailsScrn(4)
         resultData.transfer.itineraryPage.retrieveFourthBookingId=getBookingIDBookedDetailsScrn(bkdItmlastTransfrIndx+1)
         //Retrieve Fourth Image Src URL
         //resultData.transfer.itineraryPage.retrieveFourthBookingItemImageSrcURL=getItemsImageURLInItinerary(3)
         resultData.transfer.itineraryPage.retrieveFourthBookingItemImageSrcURL=getItemsImageURLInItinerary(bkdItmlastTransfrIndx)
         //Retrieve Fourth item type of car
         //resultData.transfer.itineraryPage.retrieveFourthItemCarType=getTransferNameInSuggestedItem(3)
         resultData.transfer.itineraryPage.retrieveFourthItemCarType=getTransferNameInSuggestedItem(bkdItmlastTransfrIndx)
         //Retrieve Fourth Pax
         //resultData.transfer.itineraryPage.retrieveFourthTransferitemPax=getTransfersItemsPax(3)
         resultData.transfer.itineraryPage.retrieveFourthTransferitemPax=getTransfersItemsPax(bkdItmlastTransfrIndx)

         //Retrieve Fourth Item title
         //resultData.transfer.itineraryPage.retrieveFourthTransferitemTitle=getTransfersItemTitle(3)
         resultData.transfer.itineraryPage.retrieveFourthTransferitemTitle=getTransfersItemTitle(bkdItmlastTransfrIndx)
         //Cancellation Text
         //resultData.transfer.itineraryPage.retrieveFourthTransferitemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(3)
         resultData.transfer.itineraryPage.retrieveFourthTransferitemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(bkdItmlastTransfrIndx)
         //Cancellation Link
         //resultData.transfer.itineraryPage.retrieveFourthTransferitemCancelLink=getCancellationLinkExistenceInItinerary(3)
         resultData.transfer.itineraryPage.retrieveFourthTransferitemCancelLink=getCancellationLinkExistenceInItinerary(bkdItmlastTransfrIndx)
         //Cancellation Link Icon
         //resultData.transfer.itineraryPage.retrieveFourthTransferitemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(3)
         resultData.transfer.itineraryPage.retrieveFourthTransferitemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(bkdItmlastTransfrIndx)
        //duration icon
         //resultData.hotel.itineraryPage.retrieveFourthItemDurationIconDisplayStatus=verifyItineryItemIconsDisplayed("time",2)
         resultData.hotel.itineraryPage.retrieveFourthItemDurationIconDisplayStatus=verifyItineryItemIconsDisplay("time",bkdItmlastTransfrIndx)
         //duration value
         //resultData.hotel.itineraryPage.retrieveFourthItemDurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",3)
         resultData.hotel.itineraryPage.retrieveFourthItemDurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",bkdItmlastTransfrIndx)

         //luggage icon
         //resultData.hotel.itineraryPage.retrieveFourthItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplayed("luggage",1)
         resultData.hotel.itineraryPage.retrieveFourthItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplay("luggage",bkdItmlastTransfrIndx)
         //luggage value
         //resultData.hotel.itineraryPage.retrieveFourthItemLuggageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("luggage",3)
         resultData.hotel.itineraryPage.retrieveFourthItemLuggageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("luggage",bkdItmlastTransfrIndx)

         //pax icon
         //resultData.hotel.itineraryPage.retrieveFourthItemPaxIconDisplayStatus=verifyItineryItemIconsDisplayed("pax",1)
         resultData.hotel.itineraryPage.retrieveFourthItemPaxIconDisplayStatus=verifyItineryItemIconsDisplay("pax",bkdItmlastTransfrIndx)
         //Pax value
         //resultData.hotel.itineraryPage.retrieveFourthItempaxIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("pax",3)
         resultData.hotel.itineraryPage.retrieveFourthItempaxIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("pax",bkdItmlastTransfrIndx)

         try{
             //supporting language icon
             //resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplayed("language",2)
             resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplay("language",bkdItmlastTransfrIndx)
             //Language value
             //resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",3)
             resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",bkdItmlastTransfrIndx)

         }catch(Exception e){
             //supporting language icon
             resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconDisplayStatus="Unable To Read Icon Disp Status From UI Before Cancel"
             //Language value
             resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconText="Unable To Read Icon Text From UI Before Cancel"

         }

         //service date
         //service time
         //resultData.hotel.itineraryPage.retrieveFourthItemServiceDateTime=getCheckInDetails(3)
         resultData.hotel.itineraryPage.retrieveFourthItemServiceDateTime=getCheckInDetails(bkdItmlastTransfrIndx)

         //commission and % value
         //resultData.hotel.itineraryPage.retrieveFourthItemCommissionPercentAndValue=getCommisionAndPercentageInBookeddetailsScrn(3)
         resultData.hotel.itineraryPage.retrieveFourthItemCommissionPercentAndValue=getCommisionAndPercentageInBookeddetailsScrn(bkdItmlastTransfrIndx)
         //total service item amount and currency
         //resultData.hotel.itineraryPage.retrieveFourthItemTotalAmountAndCurncy=getSuggestedItemsItenaryPrice(3)
         resultData.hotel.itineraryPage.retrieveFourthItemTotalAmountAndCurncy=getSuggestedItemsItenaryPrice(bkdItmlastTransfrIndx)

         //Traveller name list
         //resultData.hotel.itineraryPage.retrieveFourthItemTravellerNameList=getTravelerNamesMicros(3)
         resultData.hotel.itineraryPage.retrieveFourthItemTravellerNameList=getTravelerNamesMicros(bkdItmlastTransfrIndx)

         //pick up details
         resultData.hotel.itineraryPage.retrieveFourthItemPickUpDetails=getPickUpAndDropOffDetails_BookedItems(5)
         //drop off details
         resultData.hotel.itineraryPage.retrieveFourthItemDropOffDetails=getPickUpAndDropOffDetails_BookedItems(6)

     }

    protected def "bookItems"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2 & 5
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(4)==false) {
            clickOnTravellerCheckBox(4)
            sleep(3000)
        }
        //Input Flight Number
        enterPickUpFlightNumber(data.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(data.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        enterArrivalMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation First Item Screen Display Status
        resultData.hotel.itineraryPage.actualFirstItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //book to confirm item # 2
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(2)==false) {
            //Selecting travellers 3,4
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==false) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }
        //Input Flight Number
        enterPickUpFlightNumber(data.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(data.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        enterArrivalMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Second Item Screen Display Status
        resultData.hotel.itineraryPage.actualSecondItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)
        //book to confirm item # 3
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2 & 5 - room 1
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(4)==false) {
            clickOnTravellerCheckBox(4)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(7)==false) {
            //Selecting travellers 3,4 - room 2
            clickOnTravellerCheckBox(7)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(8)==false) {
            clickOnTravellerCheckBox(8)
            sleep(3000)
        }

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Third Item Screen Display Status
        resultData.hotel.itineraryPage.actualThirdItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //book to confirm item # 4
         clickOnBookingIcon(0)
         sleep(5000)

         waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2,3,4 & 5
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==false) {
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==false) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(4)==false) {
            clickOnTravellerCheckBox(4)
            sleep(3000)
        }
        //select language
        selectLanguage(data.expected.language)
        sleep(1000)

        //Click on Confirm Booking
        // clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

         //wait for confirmation page
         waitTillConformationPage()
         sleep(5000)
         scrollToTopOfThePage()

         //Booking Confirmation Fourth Item Screen Display Status
         resultData.hotel.itineraryPage.actualFourthItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

         //click on Close lightbox X function
         coseBookItenary()
         sleep(7000)
        //book to confirm item # 5 - Part 1
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2,3,4 & 5
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==false) {
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }


        //Input Flight Number
        enterFlightNumber(data.expected.newFlightNum)

        //Input Departing To
        enterAndSelectDepartingTo(data.expected.arrivingText,data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterDEpartureTime(data.expected.newtimeOfArrival_Hrs)
        //Input time of arrival - mins
        enterDEpartureTimeMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Fourth Item Screen Display Status
        resultData.hotel.itineraryPage.actualFifthItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)
        //book to confirm item # 5 - Part 2
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()


        if(getTravellerCheckBoxCheckedStatus(3)==false) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(4)==false) {
            clickOnTravellerCheckBox(4)
            sleep(3000)
        }

        //Input Flight Number
        enterFlightNumber(data.expected.newFlightNum)

        //Input Departing To
        enterAndSelectDepartingTo(data.expected.arrivingText,data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterDEpartureTime(data.expected.newtimeOfArrival_Hrs)
        //Input time of arrival - mins
        enterDEpartureTimeMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Fourth Item Screen Display Status
        resultData.hotel.itineraryPage.actualFifthItemScndPartBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)


        //Read Item 1 Details

        //item type of car
        resultData.hotel.itineraryPage.retrievedFirstItemTypeOfCar=getTransferNameInSuggestedItem(0)
        //item #1 - image
        resultData.hotel.itineraryPage.retrievedFirstItemImageSrcURL=getItemsImageURLInItinerary(0)
        //item #1 - Pax
        resultData.hotel.itineraryPage.retrievedFirstItemPax=getTransfersItemsPax(0)
        //Item #1 - title
        resultData.hotel.itineraryPage.retrievedFirstItemTitle=getTransfersItemTitle(0)
        //Item #1 - Traveller Details
        resultData.hotel.itineraryPage.retrievedItem1TravellerNameDetails=getTravlrDetailsInBookedItems(0)


        //duration icon
        resultData.hotel.itineraryPage.retrievedItem1DurationIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("time",0)
        //duration value
        resultData.hotel.itineraryPage.retrievedItem1DurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",0)

        //luggage icon
        resultData.hotel.itineraryPage.retrievedItem1LuggageIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("luggage",0)
        //luggage value
        resultData.hotel.itineraryPage.retrievedItem1LuggageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("luggage",0)

        //pax icon
        resultData.hotel.itineraryPage.retrievedItem1PaxIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("pax",0)
        //Pax value
        resultData.hotel.itineraryPage.retrievedItem1paxIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("pax",0)

        try{
            //supporting language icon
            resultData.hotel.itineraryPage.retrievedItem1LanguageIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("language",0)
            //Language value
            resultData.hotel.itineraryPage.retrievedItem1LanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",0)

        }catch(Exception e){
            //supporting language icon
            resultData.hotel.itineraryPage.retrievedItem1LanguageIconDisplayStatus="Unable To Read From UI"
            //Language value
            resultData.hotel.itineraryPage.retrievedItem1LanguageIconText="Unable To Read From UI"

        }


        //Service Date & time
        resultData.hotel.itineraryPage.retrievedItem1ServiceDatetime=getDateTimePaxDetails(0)

        //Commission % & Value
        resultData.hotel.itineraryPage.retrievedItem1CommissionPercentValue=getCommisionAndPercentageInBookeddetailsScrn(0)

        //Amount And currency
        resultData.hotel.itineraryPage.retrievedItem1AmountAndCurrency=getItenaryPriceInBookedItem(1)

        //Read Item 2 Details

        //Item #2 - Traveller Details
        resultData.hotel.itineraryPage.retrievedItem2TravellerNameDetails=getTravlrDetailsInBookedItems(1)
        //item #2 - Pax
        resultData.hotel.itineraryPage.retrievedSecondItemPax=getTransfersItemsPax(1)

        //Read Item 3 Details - Room 1
        //item #3 - image
        resultData.hotel.itineraryPage.retrievedItem3Room1ImageSrcURL=getItemsImageURLInItinerary(2)
        //item name
        resultData.hotel.itineraryPage.retrievedItem3Room1Name=getTransferNameInSuggestedItem(2)

        //item - hotel star rating
        resultData.hotel.itineraryPage.retrievedItem3Room1HotelStarRating=getRatingForTheHotelInSuggestedItem(0)

        // booked room type text

        String roomdescComplTxt_first=getHotelRoomTypePaxMealBasisDetails(2)
        List<String> tempItineraryDescList_first=roomdescComplTxt_first.tokenize(",")

        String bookedRoomTypeTxt=tempItineraryDescList_first.getAt(0)
        resultData.hotel.itineraryPage.retrievedItem3Room1BookedRoomTypeTxt=bookedRoomTypeTxt.trim()

        //Pax number
        String tmpTxt_first=tempItineraryDescList_first.getAt(1)
        List<String> tempDescList_first=tmpTxt_first.tokenize(".")

        String paxNum_first=tempDescList_first.getAt(0)
        //Item#3 - pax
        resultData.hotel.itineraryPage.retrievedItem3Room1Pax=paxNum_first.trim()

        //meal basis
        String mealBasisTxt=tempDescList_first.getAt(1)
        resultData.hotel.itineraryPage.retrievedItem3Room1MealBasisTxt=mealBasisTxt.trim()

        //check in date & stay nights
        resultData.hotel.itineraryPage.retrievedItem3Room1CheckInDateAndStayNightsTxt=getDateTimePaxDetails(2).trim()
       // item status - Available or On request - this will not be displayed when item is in confirmed state 10.3

        //commission and % value
        resultData.hotel.itineraryPage.retrievedItem3Room1CommissionPercentValue=getCommisionAndPercentageInBookeddetailsScrn(2)

        //total service item amount and currency
        resultData.hotel.itineraryPage.retrievedItem3Room1AmountAndCurrency=getItenaryPriceInBookedItem(3)

        //Item #3 - Traveller Details
        resultData.hotel.itineraryPage.retrievedItem3Room1TravellerNameDetails=getTravlrDetailsInBookedItems(2)


        //Read Item 3 Details - Room 2
        //Item #3 - Traveller Details
        resultData.hotel.itineraryPage.retrievedItem3Room2TravellerNameDetails=getTravlrDetailsInBookedItems(3)
        //Item#3 - pax
        String roomdescComplTxt_Second=getHotelRoomTypePaxMealBasisDetails(3)
        List<String> tempItineraryDescList_second=roomdescComplTxt_Second.tokenize(",")

        //Pax number
        String tmpTxt_second=tempItineraryDescList_second.getAt(1)
        List<String> tempDescList_second=tmpTxt_second.tokenize(".")

        String paxNum_second=tempDescList_second.getAt(0)
        resultData.hotel.itineraryPage.retrievedItem3Room2Pax=paxNum_second.trim()

        //Read Item 4 Details
        //item #4 - image
        resultData.hotel.itineraryPage.retrievedFourthItemImageSrcURL=getItemsImageURLInItinerary(4)

        //item #4 - item name
        resultData.hotel.itineraryPage.retrievedFourthItemName=getTransferNameInSuggestedItem(4)

        //Item #4 - Traveller Details
        resultData.hotel.itineraryPage.retrievedItem4TravellerNameDetails=getTravlrDetailsInBookedItems(4)

        //duration icon
        resultData.hotel.itineraryPage.retrievedItem4DurationIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("time",2)
        //duration value
        resultData.hotel.itineraryPage.retrievedItem4DurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",4)

        try{
            //supporting language icon
            //resultData.hotel.itineraryPage.retrievedItem4LanguageIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("language",2)
            resultData.hotel.itineraryPage.retrievedItem4LanguageIconDisplayStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("language",4)
        }catch(Exception e) {
            //supporting language icon
            resultData.hotel.itineraryPage.retrievedItem4LanguageIconDisplayStatus = "Unable To Read From UI"
        }
            try{
            //Language value
            resultData.hotel.itineraryPage.retrievedItem4LanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",4)
            }catch(Exception e){
            //Language value
            resultData.hotel.itineraryPage.retrievedItem4LanguageIconText="Unable To Read From UI"

        }

        //Servide date & pax
        String DateAndPax=getDateTimePaxDetails(4)
        List<String> tempDateAndPax=DateAndPax.tokenize(",")
        //service date
        resultData.hotel.itineraryPage.retrievedItem4ServiceDate=tempDateAndPax.getAt(0).trim()
        //number of pax
        resultData.hotel.itineraryPage.retrievedItem4Pax=tempDateAndPax.getAt(1).trim()

        //Commission % & Value
        resultData.hotel.itineraryPage.retrievedItem4CommissionPercentValue=getCommisionAndPercentageInBookeddetailsScrn(4)

        //Amount And currency
        resultData.hotel.itineraryPage.retrievedItem4AmountAndCurrency=getItenaryPriceInBookedItem(5)


        //Read Item 5 Details

        //item type of car
        resultData.hotel.itineraryPage.retrievedFifthItemTypeOfCar=getTransferNameInSuggestedItem(5)
        //item #5 - image
        resultData.hotel.itineraryPage.retrievedFifthItemImageSrcURL=getItemsImageURLInItinerary(5)
        //item #5 - Pax
        resultData.hotel.itineraryPage.retrievedFifthItemPax=getTransfersItemsPax(5)
        //Item #5 - title
        resultData.hotel.itineraryPage.retrievedFifthItemTitle=getTransfersItemTitle(5)
        //Item #5 - Traveller Details
        resultData.hotel.itineraryPage.retrievedItem5TravellerNameDetails=getTravlrDetailsInBookedItems(5)


        //duration icon
        resultData.hotel.itineraryPage.retrievedItem5DurationIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("time",3)
        //duration value
        resultData.hotel.itineraryPage.retrievedItem5DurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",5)

        //luggage icon
        resultData.hotel.itineraryPage.retrievedItem5LuggageIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("luggage",2)
        //luggage value
        resultData.hotel.itineraryPage.retrievedItem5LuggageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("luggage",5)

        //pax icon
        resultData.hotel.itineraryPage.retrievedItem5PaxIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("pax",2)
        //Pax value
        resultData.hotel.itineraryPage.retrievedItem5paxIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("pax",5)

        try{
            //supporting language icon
            //resultData.hotel.itineraryPage.retrievedItem5LanguageIconDisplayStatus=verifyItineryItemIconsDisplayedInCancelItemScreen("language",3)
            resultData.hotel.itineraryPage.retrievedItem5LanguageIconDisplayStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("language",5)
        }catch(Exception e) {
            //supporting language icon
            resultData.hotel.itineraryPage.retrievedItem5LanguageIconDisplayStatus = "Unable To Read From UI"
        }
        try{
            //Language value
            resultData.hotel.itineraryPage.retrievedItem5LanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",5)
        }catch(Exception e) {
        //Language value
            resultData.hotel.itineraryPage.retrievedItem5LanguageIconText="Unable To Read From UI"

        }

        //Service Date & time
        resultData.hotel.itineraryPage.retrievedItem5ServiceDatetime=getDateTimePaxDetails(5)

        //Commission % & Value
        resultData.hotel.itineraryPage.retrievedItem5CommissionPercentValue=getCommisionAndPercentageInBookeddetailsScrn(5)

        //Amount And currency
        resultData.hotel.itineraryPage.retrievedItem5AmountAndCurrency=getItenaryPriceInBookedItem(6)


    }

    protected def "Edittravellername"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        scrollToTopOfThePage()
        //click Edit function against traveller 2
        clickSpecificTravellerEditButton(1)
        sleep(3000)

        //user is able to do so all fields open to edit mode for traveller 1
        resultData.hotel.itineraryPage.EditModeStatusForTraveller=getTravellerCancelButtonDisplayStatus()

        enterFirstName(data.expected.modified_firstName,0)
        sleep(3000)
        //clickonSaveButton()
        clickOnEditSaveOrCancelButton(1)
        sleep(7000)
        waitTillLoadingIconDisappears()
        //user is able to do so user taken to a lightbox of items could not be amended
        resultData.hotel.itineraryPage.itemsCouldnotamendpopupDispStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

    }

    protected def "Itemscouldnotbeamendedbox"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //Thes item(s) could not be amended
        resultData.hotel.itineraryPage.ItemCouldNotBeAmendTxt=getTravellerCannotBeDeletedheaderText()
        //lightbox - Close X function
        resultData.hotel.itineraryPage.lightBoxCloseXFunction=getCloseIconDisplayStatus()

        //You have edited traveller names. Sorry, This change cannot be reflected on the following bookings.
        resultData.hotel.itineraryPage.editTravellersDescTxt=getTravellerCannotBeDeletedinsideText()
    }

    protected def "Itemscouldnotbeamended"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //Thes item(s) could not be amended
        resultData.hotel.itineraryPage.ItemCouldNotBeAmendTxt=getTravellerCannotBeDeletedheaderText()

        //Section 1 - cannot be changed
        resultData.hotel.itineraryPage.ItemSection1Text=getTravellerCannotBeDeletedinsideText(0)

        //Section 2 - to be applied to booking individually
        resultData.hotel.itineraryPage.ItemSection2Text=getTravellerCannotBeDeletedinsideText(1)
        //lightbox - Close X function
        resultData.hotel.itineraryPage.lightBoxCloseXFunction=getCloseIconDisplayStatus()


    }

    protected def "Section1cannotbechanged"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //You have edited traveller names. Sorry, This change cannot be reflected on the following bookings.
        resultData.hotel.itineraryPage.editTravellersDescTxt=getTravellerCannotBeDeletedinsideText(0)
    }

    protected def "Section2tobeappliedtobookingindividually"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //You have edited child age(s). Sorry these need to be applied to the following bookings(s) individually.  Click on Amend for each booking to select the new child age.
        resultData.hotel.itineraryPage.section2editTravellersDescTxt=getTravellerCannotBeDeletedinsideText(1)
    }

    protected def "Itinerarypage"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //click on Close X function or anywhere outside lightbox
        clickCloseOnAmendOverlay()
        //user taken back itinerary page
        //resultData.hotel.itineraryPage.actualHeaderBookedItemText=getHeaderTextInItineraryPage(2)
        resultData.hotel.itineraryPage.actualHeaderBookedItemText=getHeaderTextInItineraryPage(0)
        //check Traveller details section - the amended traveller 2 name is created in traveller detaile section as traveller 6
        //clickOnExpandTraveller()
        sleep(3000)
        //Get 6th traveller name
        resultData.hotel.itineraryPage.actualSixthTravellerName=getTravellerNameInItineraryPage(5)
        //Expected Traveller name
        resultData.hotel.itineraryPage.expectedSixthTravellerName=data.expected.title_txt+" "+data.expected.modified_firstName+" "+data.expected.travellerlastName

        //Item #1 - transer item - traveller name details - should not be changed
        resultData.hotel.itineraryPage.actualItem1TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(0)


        //item #2 - transfer item - traveller name details - remain same
        resultData.hotel.itineraryPage.actualItem2TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(1)

        //item #3 - hotel item - traveller name details - Room 1 - traveller 2 name should be changed as per amendment done
        resultData.hotel.itineraryPage.actualItem3Room1TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(2)

        resultData.hotel.itineraryPage.expectedItem3Room1TravellerNameDetailsAfterEdit=data.expected.firstName+" "+data.expected.lastName+", "+data.expected.modified_firstName+" "+data.expected.travellerlastName+", "+data.expected.childFirstName+"Fifth"+" "+data.expected.childLastName+"Fifth"+" "+"("+data.input.children.getAt(0).toString()+"yrs)"


        //item #3 - hotel item - traveller name details - Room 2 - remain same
        resultData.hotel.itineraryPage.actualItem3Room2TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(3)
        //item #4 - activity item - traveller name details - should not be changed
        resultData.hotel.itineraryPage.actualItem4TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(4)
        //item #5 - transfer item - traveller name details - should not be changed
        resultData.hotel.itineraryPage.actualItem5TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(5)



    }

    protected def "Itinrypage"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //click on Close X function or anywhere outside lightbox
        clickCloseOnAmendOverlay()
        //user taken back itinerary page
        //resultData.hotel.itineraryPage.actualHeaderBookedItemText=getHeaderTextInItineraryPage(2)
        resultData.hotel.itineraryPage.actualHeaderBookedItemText=getHeaderTextInItineraryPage(0)
        //check Traveller details section - the amended traveller 2 name is created in traveller detaile section as traveller 6
        //clickOnExpandTraveller()
        sleep(3000)
        //Get 6th traveller name
        resultData.hotel.itineraryPage.actualSixthTravellerName=getTravellerNameInItineraryPage(5)
        //Expected Traveller name
        resultData.hotel.itineraryPage.expectedSixthTravellerName=data.expected.childFirstName+"Fifth"+" "+data.expected.childLastName+"Fifth"+" "+"("+data.expected.modifiedChildAge+"yrs)"

        //Item #1 - transer item - traveller name details - should not be changed
        resultData.hotel.itineraryPage.actualItem1TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(0)


        //item #2 - transfer item - traveller name details - remain same
        resultData.hotel.itineraryPage.actualItem2TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(1)

        //item #3 - hotel item - traveller name details - Room 1 - should not be changed
        resultData.hotel.itineraryPage.actualItem3Room1TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(2)

        resultData.hotel.itineraryPage.expectedItem3Room1TravellerNameDetailsAfterEdit=resultData.hotel.itineraryPage.retrievedItem3Room1TravellerNameDetails


        //item #3 - hotel item - traveller name details - Room 2 - remain same
        resultData.hotel.itineraryPage.actualItem3Room2TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(3)
        //item #4 - activity item - traveller name details - should not be changed
        resultData.hotel.itineraryPage.actualItem4TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(4)
        //item #5 - transfer item - traveller name details - should not be changed
        resultData.hotel.itineraryPage.actualItem5TravellerNameDetailsAfterEdit=getTravlrDetailsInBookedItems(5)



    }

    protected def "Item1"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //item #1 - transfer item
        boolean itemExistence=false
        String name
        String pax
        for(int i=0;i<getNoOfItemsInNonAmendPopup();i++){
            name=getItemNameInNonAmendPopup(i)
            pax=getItemsPaxInNonAmendPopup(i)
            if((data.expected.firstItemTypeOfCar.toString().equalsIgnoreCase(name))&&(data.expected.firstItemPax.toString().equalsIgnoreCase(pax))){
                itemExistence=true
                break
            }
        }

        resultData.hotel.itineraryPage.Item1DispStatus=itemExistence
        //item #1 - image
        resultData.hotel.itineraryPage.Item1Image=getItemsImageURLInNonAmmend(0)
        //item type of car
        resultData.hotel.itineraryPage.Item1Txt=getItemNameInNonAmendPopup(0)

        //number of pax
        resultData.hotel.itineraryPage.Item1Pax=getItemsPaxInNonAmendPopup(0)

        //item title
        resultData.hotel.itineraryPage.Item1Title=getItemTitleInNonAmendPopup(0)

        //Cancellation fees apply
        resultData.hotel.itineraryPage.Item1CancellationTextDispStatus=getCancellationExistenceInNonAmendPopup(0)

        //duration icon
        resultData.hotel.itineraryPage.Item1DurationIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("time",0)
        //duration value
        resultData.hotel.itineraryPage.Item1DurationIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("time",0)

        //luggage icon
        resultData.hotel.itineraryPage.Item1LuggageIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("luggage",0)
        //luggage value
        resultData.hotel.itineraryPage.Item1LuggageIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("luggage",0)

        //pax icon
        resultData.hotel.itineraryPage.Item1PaxIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("pax",0)
        //Pax value
        resultData.hotel.itineraryPage.Item1paxIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("pax",0)

        try{
            //supporting language icon
            resultData.hotel.itineraryPage.Item1LanguageIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("language",0)
            //Language value
            resultData.hotel.itineraryPage.Item1LanguageIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("language",0)

        }catch(Exception e){
            //supporting language icon
            resultData.hotel.itineraryPage.Item1LanguageIconDisplayStatus="Unable To Read Icon Disp Status From UI"
            //Language value
            resultData.hotel.itineraryPage.Item1LanguageIconText="Unable To Read Icon Text From UI"

        }

        //service date
        //service time
        resultData.hotel.itineraryPage.Item1ServiceDateTime=getCheckInDateAndDurationInNonAmendablePopup(1)

        //item status - Available or On request
        //item status,  will not be shown in the item is booked successfully- 10.3
        //resultData.hotel.itineraryPage.Item1StatusText=getStatusTextInNonAmendablePopupScrn(1)
        //commission and % value
        resultData.hotel.itineraryPage.Item1CommissionPercentAndValue=getCommissionAndPercentInNonAmendablePopup(1)
        //total service item amount and currency
        resultData.hotel.itineraryPage.Item1TotalAmountAndCurncy=getTotalAmntAndCurrencyInNonAmendablePopup(1)

    }

    protected def "Item2"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //item #2 - transfer item
        boolean itemExist=false
        String name

        String pax
        for(int i=0;i<getNoOfItemsInNonAmendPopup();i++){
            name=getItemNameInNonAmendPopup(i)
            pax=getItemsPaxInNonAmendPopup(i)
            if((data.expected.firstItemTypeOfCar.toString().equalsIgnoreCase(name))&&(data.expected.secondItemPax.toString().equalsIgnoreCase(pax))){
                itemExist=true
                break
            }
        }
        resultData.hotel.itineraryPage.Item2DispStatus=itemExist


    }
    protected def "Item3"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //item #3 - hotel item
        boolean itemExistence=false
        String name

        for(int i=0;i<getNoOfItemsInNonAmendPopup();i++){
            name=getItemNameInNonAmendPopup(i)

            if((data.expected.thirdtemType.toString().equalsIgnoreCase(name))){
                itemExistence=true
                break
            }
        }

        resultData.hotel.itineraryPage.Item3DispStatus=itemExistence


    }

    protected def "Items3"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //item #3 - hotel item
        boolean itemHotelRoom1Existence=false
        boolean itemHotelRoom2Existence=false
        String name
        String paxRoom
        String paxRoom1=resultData.hotel.itineraryPage.retrievedItem3Room1Pax
        String paxRoom2=resultData.hotel.itineraryPage.retrievedItem3Room2Pax


        for(int i=0;i<getNoOfItemsInNonAmendPopupBasedOnSection(0);i++){
            name=getItemNameInNonAmendPopupBasedOnSection(0,i)
            paxRoom=getItemsPaxInNonAmendPopupBasedOnSection(0,i)
            if((data.expected.thirdtemType.toString().equalsIgnoreCase(name))&&(paxRoom.contains(paxRoom1))){
                itemHotelRoom1Existence=true
                break
            }

            if((data.expected.thirdtemType.toString().equalsIgnoreCase(name))&&(paxRoom.contains(paxRoom2))){
                itemHotelRoom2Existence=true
                break
            }
        }

        resultData.hotel.itineraryPage.Item3HotelRoom1DispStatus=itemHotelRoom1Existence
        resultData.hotel.itineraryPage.Item3HotelRoom2DispStatus=itemHotelRoom2Existence


    }

    protected def "Item3room1"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        //item #3 - hotel item - room 1
        boolean itemHotelRoom1Existence=false

        String name
        String paxRoom1=resultData.hotel.itineraryPage.retrievedItem3Room1Pax
        String pax

        for(int i=0;i<getNoOfItemsInNonAmendPopupBasedOnSection(1);i++){
            name=getItemNameInNonAmendPopupBasedOnSection(1,i)
            pax=getItemsPaxInNonAmendPopupBasedOnSection(1,i)

            if((data.expected.thirdtemType.toString().equalsIgnoreCase(name))&&(pax.contains(paxRoom1))){
                itemHotelRoom1Existence=true
                break
            }

        }
        //item #3 - hotel item - room 1
        resultData.hotel.itineraryPage.Item3HotelRoom1DispStatusSection2=itemHotelRoom1Existence

        //item #3 - image
        resultData.hotel.itineraryPage.Item3Room1Image=getItemsImageURLInNonAmmend(3)

        //item name
        resultData.hotel.itineraryPage.Item3Room1ItemName=getItemNameInNonAmendPopupBasedOnSection(1,0)

        //item - hotel star rating
        resultData.hotel.itineraryPage.Item3Room1HotelStarRating=getItemStarRatingInNonAmendPopupBasedOnSection(1,0)

        //booked room type text
        String roomdescComplTxt_first=getItemsPaxInNonAmendPopupBasedOnSection(1,0)
        List<String> tempItineraryDescList_first=roomdescComplTxt_first.tokenize(",")

        String bookedRoomTypeTxt=tempItineraryDescList_first.getAt(0)
        resultData.hotel.itineraryPage.Item3Room1BookedRoomTypeTxt=bookedRoomTypeTxt.trim()

        //Pax number
        String tmpTxt_first=tempItineraryDescList_first.getAt(1)
        List<String> tempDescList_first=tmpTxt_first.tokenize(".")

        String paxNum_first=tempDescList_first.getAt(0)
        //Item#3 - pax
        resultData.hotel.itineraryPage.Item3Room1Pax=paxNum_first.trim()

        //meal basis
        String mealBasisTxt=tempDescList_first.getAt(1)
        resultData.hotel.itineraryPage.Item3Room1MealBasisTxt=mealBasisTxt.trim()

        //Cancellation fees apply
        resultData.hotel.itineraryPage.Item3Room1CancellationTextDispStatus=getCancellationExistenceInNonAmendPopup(3)

        //check in date & stay nights
        resultData.hotel.itineraryPage.Item3Room1CheckInDateAndStayNights=getCheckInDateAndDurationInNonAmendablePopup(4)

        //item status - Available or On request - status will not be displayed if booking is confirmed - 10.3
        //commission and % value
        resultData.hotel.itineraryPage.Item3Room1CommissionPercentAndValue=getCommissionAndPercentInNonAmendablePopup(2)
        //total service item amount and currency
        resultData.hotel.itineraryPage.Item3Room1TotalAmountAndCurncy=getTotalAmntAndCurrencyInNonAmendablePopup(2)
    }

    protected def "Item3room2"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //item #3 - hotel item - room 2

        boolean itemHotelRoom2Existence=false
        String name
        String pax
        String paxRoom2=resultData.hotel.itineraryPage.retrievedItem3Room2Pax


        for(int i=0;i<getNoOfItemsInNonAmendPopupBasedOnSection(1);i++){
            name=getItemNameInNonAmendPopupBasedOnSection(1,i)
            pax=getItemsPaxInNonAmendPopupBasedOnSection(1,i)

            if((data.expected.thirdtemType.toString().equalsIgnoreCase(name))&&(pax.contains(paxRoom2))){
                itemHotelRoom2Existence=true
                break
            }
        }


        resultData.hotel.itineraryPage.Item3HotelRoom2DispStatusSection2=itemHotelRoom2Existence


    }

    protected def "Item4"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //item #4 - activity item
        boolean itemExistence=false
        String iname

        for(int i=0;i<getNoOfItemsInNonAmendPopup();i++){
            iname=getItemNameInNonAmendPopup(i)

            if(data.input.activityTypeText.toString().equalsIgnoreCase(iname)){
                itemExistence=true
                break
            }
        }

        resultData.hotel.itineraryPage.Item4DispStatus=itemExistence
        //item image
        resultData.hotel.itineraryPage.Item4Image=getItemsImageURLInNonAmmend(1)

        //item name
        resultData.hotel.itineraryPage.Item4Txt=getItemNameInNonAmendPopup(1)
        //cancellation charge apply
        resultData.hotel.itineraryPage.Item4CancellationTextDispStatus=getCancellationExistenceInNonAmendPopup(1)
        //duration icon
        resultData.hotel.itineraryPage.Item4DurationIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("time",1)
        //duration value
        resultData.hotel.itineraryPage.Item4DurationIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("time",1)

        try{
            //supporting language icon
            resultData.hotel.itineraryPage.Item4LanguageIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("language",1)
            //Language value
            resultData.hotel.itineraryPage.Item4LanguageIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("language",1)

        }catch(Exception e){
            //supporting language icon
            resultData.hotel.itineraryPage.Item4LanguageIconDisplayStatus="Unable To Read Icon Display Status From UI"
            //Language value
            resultData.hotel.itineraryPage.Item4LanguageIconText="Unable To Read Icon Text From UI"

        }

        String DateAndPax=getCheckInDateAndDurationInNonAmendablePopup(2)
        List<String> tempDateAndPax=DateAndPax.tokenize(",")
        //service date
        resultData.hotel.itineraryPage.Item4ServiceDate=tempDateAndPax.getAt(0).trim()
        //number of pax
        resultData.hotel.itineraryPage.Item4Pax=tempDateAndPax.getAt(1).trim()

        //item status - Available or On request
        //item status will not be shown in the item is booked successfully- 10.3
        //resultData.hotel.itineraryPage.Item4StatusText=getStatusTextInNonAmendablePopupScrn(2)
        //commission and % value
        resultData.hotel.itineraryPage.Item4CommissionPercentAndValue=getCommissionAndPercentInNonAmendablePopup(2)
        //amount & currency
        resultData.hotel.itineraryPage.Item4TotalAmountAndCurncy=getTotalAmntAndCurrencyInNonAmendablePopup(2)


    }

    protected def "Item5"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //item #5 - transfer item
        boolean itemExistence=false
        String name
        String pax
        for(int i=0;i<getNoOfItemsInNonAmendPopup();i++){
            name=getItemNameInNonAmendPopup(i)
            pax=getItemsPaxInNonAmendPopup(i)
            if((data.expected.fifthItemType.toString().equalsIgnoreCase(name))&&(data.expected.fifthItemPax.toString().equalsIgnoreCase(pax))){
                itemExistence=true
                break
            }
        }

        resultData.hotel.itineraryPage.Item5DispStatus=itemExistence
        //item #5 - image
        resultData.hotel.itineraryPage.Item5Image=getItemsImageURLInNonAmmend(2)
        //item type of car
        resultData.hotel.itineraryPage.Item5Txt=getItemNameInNonAmendPopup(2)

        //number of pax
        resultData.hotel.itineraryPage.Item5Pax=getItemsPaxInNonAmendPopup(2)

        //item title
        resultData.hotel.itineraryPage.Item5Title=getItemTitleInNonAmendPopup(2)

        //Cancellation fees apply
        resultData.hotel.itineraryPage.Item5CancellationTextDispStatus=getCancellationExistenceInNonAmendPopup(2)

        //duration icon
        resultData.hotel.itineraryPage.Item5DurationIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("time",2)
        //duration value
        resultData.hotel.itineraryPage.Item5DurationIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("time",2)

        //luggage icon
        resultData.hotel.itineraryPage.Item5LuggageIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("luggage",2)
        //luggage value
        resultData.hotel.itineraryPage.Item5LuggageIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("luggage",2)

        //pax icon
        resultData.hotel.itineraryPage.Item5PaxIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("pax",2)
        //Pax value
        resultData.hotel.itineraryPage.Item5paxIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("pax",2)

        try{
            //supporting language icon
            resultData.hotel.itineraryPage.Item5LanguageIconDisplayStatus=verifyItineryItemIconsDisplayedInCouldNotAmendPopupScreen("language",2)
            //Language value
            resultData.hotel.itineraryPage.Item5LanguageIconText=getItinrytemIconsTextDispInCannotAmendPopupScrn("language",2)

        }catch(Exception e){
            //supporting language icon
            resultData.hotel.itineraryPage.Item5LanguageIconDisplayStatus="Unable To Read Icon Disp Status From UI"
            //Language value
            resultData.hotel.itineraryPage.Item5LanguageIconText="Unable To Read Icon Text From UI"

        }

        //service date
        //service time
        resultData.hotel.itineraryPage.Item5ServiceDateTime=getCheckInDateAndDurationInNonAmendablePopup(3)

        //item status - Available or On request
        //item status will not be displayed if booking is successful - sit 10.3
        //resultData.hotel.itineraryPage.Item5StatusText=getStatusTextInNonAmendablePopupScrn(3)
        //commission and % value
        resultData.hotel.itineraryPage.Item5CommissionPercentAndValue=getCommissionAndPercentInNonAmendablePopup(3)
        //total service item amount and currency
        resultData.hotel.itineraryPage.Item5TotalAmountAndCurncy=getTotalAmntAndCurrencyInNonAmendablePopup(3)

    }

    protected def "EditChildAge"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        scrollToTopOfThePage()
        sleep(2000)
        //clickOnExpandTraveller()
        //sleep(2000)
        //click Edit function against traveller 5
        clickSpecificTravellerEditButton(4)
        sleep(3000)

        //user is able to do so all fields open to edit mode for traveller 5
        resultData.hotel.itineraryPage.EditModeStatusForTraveller=getTravellerCancelButtonDisplayStatus()
        //Amend child age from 5 to 15
        //clickAndModifyAge(data.expected.modifiedChildAge)
        enterEditedChildAgeAndTabOut(data.expected.modifiedChildAge)
        sleep(4000)
        //click <Save> function
        //clickonSaveButton()
        clickOnEditSaveOrCancelButton(1)
        sleep(2000)

        //user is able to do so user taken to a lightbox of items could not be amended
        resultData.hotel.itineraryPage.itemsCouldnotamendpopupDispStatus=getTravellerCannotBeDeletedPopupDisplayStatus()

    }

    protected def "bookItemFullCharge"(HotelSearchData data, HotelTransferTestResultData resultData,int bookBtnIndex,int hotelCardIndx,int transferCardIndx, int activityCardIndx){
        at ItineraryTravllerDetailsPage

        clickOnBookingIcon(bookBtnIndex)
        sleep(5000)

        waitTillLoadingIconDisappears()
        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false){
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }


        //Input Flight Number
        enterPickUpFlightNumber(data.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(data.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        enterArrivalMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation First Item Screen Display Status
        resultData.hotel.itineraryPage.actualFirstItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //Click on cancellation charge link
        clickOnCancellationChargesLink(0)
        sleep(3000)

        //Cancellation policy text
        resultData.hotel.itineraryPage.retrieveFirstTransferCancellationPolicyText=getCancellationPolicyPopupInnerTxt()
        //click on close
        clickOnCloseLightBox()


        clickOnBookingIcon(0)
        sleep(7000)
        waitTillLoadingIconDisappears()
        if(getTravellerCheckBoxCheckedStatus(0)==false)
        {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false)
        {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }


        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()
        //Booking Confirmation Second Item Screen Display Status
        resultData.hotel.itineraryPage.actualSecondItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)



        clickOnBookingIcon(0)
        sleep(7000)
        waitTillLoadingIconDisappears()
        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()
        //Booking Confirmation Third Item Screen Display Status
        resultData.hotel.itineraryPage.actualThirdItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()
        String actualItineraryId=getItinearyIdInBookingConf(0)
        resultData.hotel.itineraryPage.retrievedItineraryIdName=actualItineraryId

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        //Click on cancellation charge link
        clickOnCancellationChargesLink(hotelCardIndx)
        sleep(3000)

        //Cancellation policy text
        resultData.hotel.itineraryPage.retrieveSecondHotelCancellationPolicyText=getCancellationPolicyPopupInnerTxt()

        //click on close
        clickOnCloseLightBox()
        sleep(2000)

/*
        //Retrieve Booking Id's
        resultData.hotel.itineraryPage.actualFirstBookingIdValue=getBookingId(0)
        //Retrieve First Image Src URL
        resultData.hotel.itineraryPage.retrieveFirstBookingItemImageSrcURL=getItemsImageURLInItinerary(0)
        //Retrieve First item type of car
        resultData.hotel.itineraryPage.retrieveFirstItemCarType=getTransferNameInSuggestedItem(0)
        //Retrieve First Pax
        resultData.hotel.itineraryPage.retrieveFirstTransferitemPax=getTransfersItemsPax(0)
        //Retrieve First Item title
        resultData.hotel.itineraryPage.retrieveFirstTransferitemTitle=getTransfersItemTitle(0)
        //Cancellation Text
        resultData.hotel.itineraryPage.retrieveFirstTransferitemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(0)
        //Cancellation Link
        resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLink=getCancellationLinkExistenceInItinerary(0)
        //Cancellation Link Icon
        resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(0)

        //duration icon
        resultData.hotel.itineraryPage.retrieveFirstTransferitemDurationIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("time",0)

        //duration value
        resultData.hotel.itineraryPage.retrieveFirstTransferitemDurationValue=verifyDuration(0)

        //luggage icon
        resultData.hotel.itineraryPage.retrieveFirstTransferitemLuggageIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("luggage",0)
        // luggage value
        resultData.hotel.itineraryPage.retrieveFirstTransferitemLuggageValue=verifyDuration(1)
        //pax icon
        resultData.hotel.itineraryPage.retrieveFirstTransferitemPaxIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("pax",0)
        // pax value
        resultData.hotel.itineraryPage.retrieveFirstTransferitemPaxValue=verifyDuration(2)
        //supporting language icon
        resultData.hotel.itineraryPage.retrieveFirstTransferitemLanguageIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("language",0)
        // supporting language value
        resultData.hotel.itineraryPage.retrieveFirstTransferitemLanguageValue=verifyDuration(3)
*/
        //Retrieve First Item details

        //Retrieve First Item Details

        //Retrieve Booking id
        resultData.hotel.itineraryPage.retrieveFirstBookingId=getBookingIDBookedDetailsScrn(transferCardIndx+1)
        //Retrieve First Image Src URL
        resultData.hotel.itineraryPage.retrieveFirstBookingItemImageSrcURL=getItemsImageURLInItinerary(transferCardIndx)
        //Retrieve First item type of car
        resultData.hotel.itineraryPage.retrieveFirstItemCarType=getTransferNameInSuggestedItem(transferCardIndx)
        //Retrieve First Pax
        resultData.hotel.itineraryPage.retrieveFirstTransferitemPax=getTransfersItemsPax(transferCardIndx)
        //Retrieve First Item title
        resultData.hotel.itineraryPage.retrieveFirstTransferitemTitle=getTransfersItemTitle(transferCardIndx)
        //Cancellation Text
        resultData.hotel.itineraryPage.retrieveFirstTransferitemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(transferCardIndx)
        //Cancellation Link
        resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLink=getCancellationLinkExistenceInItinerary(transferCardIndx)
        //Cancellation Link Icon
        resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(transferCardIndx)

//duration icon
        resultData.hotel.itineraryPage.retrieveFirstItemDurationIconDisplayStatus=verifyItineryItemIconsDisplayed("time",transferCardIndx)
        //duration value
        resultData.hotel.itineraryPage.retrieveFirstItemDurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",transferCardIndx)

        //luggage icon
        //resultData.hotel.itineraryPage.retrieveFirstItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplayed("luggage",transferCardIndx)
        resultData.hotel.itineraryPage.retrieveFirstItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplay("luggage",transferCardIndx)
        //luggage value
        resultData.hotel.itineraryPage.retrieveFirstItemLuggageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("luggage",transferCardIndx)

        //pax icon
        //resultData.hotel.itineraryPage.retrieveFirstItemPaxIconDisplayStatus=verifyItineryItemIconsDisplayed("pax",transferCardIndx)
        resultData.hotel.itineraryPage.retrieveFirstItemPaxIconDisplayStatus=verifyItineryItemIconsDisplay("pax",transferCardIndx)
        //Pax value
        resultData.hotel.itineraryPage.retrieveFirstItempaxIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("pax",transferCardIndx)

        try{
            //supporting language icon
            resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplayed("language",transferCardIndx)
            //Language value
            resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",transferCardIndx)

        }catch(Exception e){
            //supporting language icon
            resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconDisplayStatus="Unable To Read Icon Disp Status From UI Before Cancel"
            //Language value
            resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconText="Unable To Read Icon Text From UI Before Cancel"

        }

        //service date
        //service time
        resultData.hotel.itineraryPage.retrieveFirstItemServiceDateTime=getCheckInDetails(transferCardIndx)

        //commission and % value
        resultData.hotel.itineraryPage.retrieveFirstItemCommissionPercentAndValue=getCommisionAndPercentageInBookeddetailsScrn(transferCardIndx)
        //total service item amount and currency
        resultData.hotel.itineraryPage.retrieveFirstItemTotalAmountAndCurncy=getSuggestedItemsItenaryPrice(transferCardIndx)

        //Traveller name list
        resultData.hotel.itineraryPage.retrieveFirstItemTravellerNameList=getTravelerNamesMicros(transferCardIndx)

        //pick up details
        resultData.hotel.itineraryPage.retrieveFirstItemPickUpDetails=getPickUpAndDropOffDetails_BookedItems(0)
        //drop off details
        resultData.hotel.itineraryPage.retrieveFirstItemDropOffDetails=getPickUpAndDropOffDetails_BookedItems(1)


        //First Item Price
        resultData.hotel.itineraryPage.retrieveFirstItemPrice=getIndividualPrice(transferCardIndx)


        //Retrieve Second Item Booking Id's
        resultData.hotel.itineraryPage.actualSecondBookingIdValue=getBookingId(hotelCardIndx)
        //Second Item Price
        resultData.hotel.itineraryPage.retrieveSecondItemPrice=getIndividualPrice(hotelCardIndx)

        //Retrieve Second Item Details

        //Retrieve Booking id
        resultData.hotel.itineraryPage.retrieveSecondBookingId=getBookingIDBookedDetailsScrn(hotelCardIndx+1)
        //Retrieve Second Image Src URL
        resultData.hotel.itineraryPage.retrieveSecondBookingItemImageSrcURL=getItemsImageURLInItinerary(hotelCardIndx)
        //Retrieve Second item Name
        resultData.hotel.itineraryPage.retrieveSecondItemName=getTransferNameInSuggestedItem(hotelCardIndx)
        //Retrieve hotel star rating
        resultData.hotel.itineraryPage.retrieveSecondItemHotelStarRatng=getRatingForTheHotelInSuggestedItem(0)

        String roomdescComplTxt_first=getHotelRoomTypePaxMealBasisDetails(hotelCardIndx)
        List<String> tempItineraryDescList_first=roomdescComplTxt_first.tokenize(",")

        String roomDescText_first=tempItineraryDescList_first.getAt(0)
        //room type name
        resultData.hotel.itineraryPage.retrieveSecondItemroomdescTxt=roomDescText_first.trim()
        //Pax number
        String tmpTxt_first=tempItineraryDescList_first.getAt(1)
        List<String> tempDescList_first=tmpTxt_first.tokenize(".")

        String paxNum_first=tempDescList_first.getAt(0)
        resultData.hotel.itineraryPage.retrieveSecondItemPaxNum=paxNum_first.trim()

        //Rate plan - meal basis
        String ratePlantxt_first=tempDescList_first.getAt(1)
        resultData.hotel.itineraryPage.retrieveSecondItemMealbasis=ratePlantxt_first.trim()

        //Cancellation Text
        resultData.hotel.itineraryPage.retrieveSeconditemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(hotelCardIndx)
        //Cancellation Link
        resultData.hotel.itineraryPage.retrieveSeconditemCancelLink=getCancellationLinkExistenceInItinerary(hotelCardIndx)
        //Cancellation Link Icon
        resultData.hotel.itineraryPage.retrieveSeconditemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(hotelCardIndx)

        //check in date - nights
        resultData.hotel.itineraryPage.retrieveSecondItemChkInDateNights=getCheckInDetails(hotelCardIndx)

        //commission and % value
        resultData.hotel.itineraryPage.retrieveSecondItemCommissionPercentAndValue=getCommisionAndPercentageInBookeddetailsScrn(hotelCardIndx)
        //total service item amount and currency
        resultData.hotel.itineraryPage.retrieveSecondItemTotalAmountAndCurncy=getItenaryPriceInBookedItem(hotelCardIndx+1)

        //Traveller name list
        resultData.hotel.itineraryPage.retrieveSecondItemTravellerNameList=getTravelerNamesMicros(hotelCardIndx)

        //Add a Remark or Comment Text
        resultData.hotel.itineraryPage.retrieveSecondItemAddRemarkOrCommentText=getHotelAddRemarkOrCommentText(hotelCardIndx)





        //Retrieve Third Item Booking Id's
        resultData.hotel.itineraryPage.actualThirdBookingIdValue=getBookingId(activityCardIndx)
        //Third Item Price
        resultData.hotel.itineraryPage.retrieveThirdItemPrice=getIndividualPrice(activityCardIndx)


        //Retrieve Third Item Details

        //Retrieve Booking id
        resultData.hotel.itineraryPage.retrieveThirdBookingId=getBookingIDBookedDetailsScrn(activityCardIndx+1)
        //Retrieve Third Image Src URL
        resultData.hotel.itineraryPage.retrieveThirdBookingItemImageSrcURL=getItemsImageURLInItinerary(activityCardIndx)

        //Retrieve Third item Name
        resultData.hotel.itineraryPage.retrieveThirdItemName=getTransferNameInSuggestedItem(activityCardIndx)
        //Cancellation Text
        resultData.hotel.itineraryPage.retrieveThirditemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(activityCardIndx)
        //Duration Icon
        resultData.hotel.itineraryPage.retrieveThirditemDurationIconStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("time",activityCardIndx)
        //Duration Value
        resultData.hotel.itineraryPage.retrieveThirditemDurationValue=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",activityCardIndx)

        //Language Icon
        resultData.hotel.itineraryPage.retrieveThirditemLangIconStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("language",activityCardIndx)
        //Language Value
        resultData.hotel.itineraryPage.retrieveThirditemLangValue=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",activityCardIndx)

        //service date & Pax
        resultData.hotel.itineraryPage.retrieveThirditemServiceDateAndPax=getDateTimePaxDetails(activityCardIndx)

        //Commission and %
        resultData.hotel.itineraryPage.retrieveThirditemCommissionAndPercent=getCommisionAndPercentageInBookeddetailsScrn(activityCardIndx)

        //Amount & Currency
        resultData.hotel.itineraryPage.retrieveThirditemAmountAndCurrency=getItenaryPriceInBookedItem(activityCardIndx+1)
        //Traveller name list
        resultData.hotel.itineraryPage.retrieveThirditemTravellers=getTravelerNamesMicros(activityCardIndx)

    }

    protected def "Bookitem4"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        clickOnBookingIcon(2)
        //clickOnBookingIcon(1)
        sleep(5000)

        waitTillLoadingIconDisappears()
        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

         //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        try{
            //hotel item status updated to Unavailable
            resultData.hotel.itineraryPage.actualHotelNameInUnavailableSection=getHotelNameInUnavailableItem(1)

        }catch(Exception e){
            resultData.hotel.itineraryPage.actualHotelNameInUnavailableSection="Status Is Not Unavailable"

        }



    }

    protected def "bookItemMultiRoom"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        //Book First Item - Part 1
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==true) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==true) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }
        //Input Flight Number
        enterPickUpFlightNumber(data.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(data.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        enterArrivalMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation First Item Screen Display Status
        resultData.hotel.itineraryPage.actualFirstItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)
        //Book First Item - Part 2
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==true) {
            //Selecting travellers 3,4
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==true) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==false) {
            //Selecting travellers 3,4
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==false) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }

        //Input Flight Number
        enterPickUpFlightNumber(data.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(data.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        enterArrivalMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Second Item Screen Display Status
        resultData.hotel.itineraryPage.actualFirstItemPart2BookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)
        //Book Confirtm - item 2
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==true) {
            //Selecting travellers 3,4
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==true) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==false) {
            //Selecting travellers 3,4
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==false) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }

        //Input Flight Number
        enterPickUpFlightNumber(data.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(data.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        enterArrivalMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Second Item Screen Display Status
        resultData.hotel.itineraryPage.actualSecondItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)
        //Book Confirtm - item 2 part2
        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==true) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==true) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }
        //Input Flight Number
        enterPickUpFlightNumber(data.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(data.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(data.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        enterArrivalMins(data.expected.timeOfArrival_mins)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation First Item Screen Display Status
        resultData.hotel.itineraryPage.actualSecondItemPart2BookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2 for room 1
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(6)==false) {
            //Selecting travellers 3,4 for room 2
            clickOnTravellerCheckBox(6)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(7)==false) {
            clickOnTravellerCheckBox(7)
            sleep(3000)
        }

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Third Item Screen Display Status
        resultData.hotel.itineraryPage.actualThirdItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2,3 & r
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==false) {
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==false) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }

        //select language
        selectLanguage(data.expected.language)
        sleep(1000)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Fourth Item Screen Display Status
        resultData.hotel.itineraryPage.actualFourthItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2,3 & r
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(2)==false) {
            clickOnTravellerCheckBox(2)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(3)==false) {
            clickOnTravellerCheckBox(3)
            sleep(3000)
        }

        //select language
        selectLanguage(data.expected.language)
        sleep(1000)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Fifth Item Screen Display Status
        resultData.hotel.itineraryPage.actualFifthItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)


        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }

        //Input Flight Number
        enterFlightNumber(data.expected.newFlightNum)

        //Input Departing To
        enterAndSelectDepartingTo(data.expected.arrivingText,data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterDEpartureTime(data.expected.newtimeOfArrival_Hrs)
        sleep(2000)
        //Input time of arrival - mins
        enterDEpartureTimeMins(data.expected.timeOfArrival_mins)
        sleep(2000)

        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Sixth Item Screen Display Status
        resultData.hotel.itineraryPage.actualSixthItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        clickOnBookingIcon(0)
        sleep(5000)

        waitTillLoadingIconDisappears()

        if(getTravellerCheckBoxCheckedStatus(0)==false) {
            //Selecting travellers 1,2
            clickOnTravellerCheckBox(0)
            sleep(3000)
        }
        if(getTravellerCheckBoxCheckedStatus(1)==false) {
            clickOnTravellerCheckBox(1)
            sleep(3000)
        }

        //Input Flight Number
        enterFlightNumber(data.expected.newFlightNum)

        //Input Departing To
        enterAndSelectDepartingTo(data.expected.arrivingText,data.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterDEpartureTime(data.expected.newtimeOfArrival_Hrs)
        sleep(3000)
        //Input time of arrival - mins
        enterDEpartureTimeMins(data.expected.timeOfArrival_mins)
        sleep(2000)
        //Click on Confirm Booking
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)
        scrollToTopOfThePage()

        //Booking Confirmation Seventh Item Screen Display Status
        resultData.hotel.itineraryPage.actualSeventhItemBookingConfrm=getBookingConfirmationScreenDisplayStatus()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)



    }

    protected def "closeItinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        scrollToTopOfThePage()
        sleep(3000)
        clickOnCloseItineraryButton()
        sleep(7000)
        at HotelSearchPage
        resultData.hotel.search.actFindButtonStatus=getFindButtonDisabled()
    }

    protected def "Searchtheitinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        at HotelSearchResultsPage
        clickOnSearchToolTab(data.expected.itineraryTab)
        sleep(3000)

        enterYourItinerary(resultData.hotel.itineraryPage.actualItineraryId)
        sleep(3000)
        //click Find button
        clickFindButtonItinerarySearch()
        sleep(7000)

        at ItenaryPageItems
        String edtditineraryPageTitle = getItenaryName()
        //println("$itineraryPageTitle")
        List<String> tList=edtditineraryPageTitle.tokenize(" ")
        String edtitinaryName=tList.getAt(2)
        resultData.hotel.itineraryPage.actItnrName=edtitinaryName.replaceAll("\nEdit", "")

    }

    protected def "cancelitinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        resultData.hotel.itineraryPage.retrievedItineraryIdName=getItenaryName().replaceAll("\nEdit", "").trim()

        //click on Cancel itinerary button
        //clickOnDeleteItenaryCTA()
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)
        //Cancel itinerary lightbox display status
        resultData.hotel.removeItemPage.actualCancelItemDispStatus=verifyRemoveItenary()


    }

    protected def "cancelItem"(HotelSearchData data, HotelTransferTestResultData resultData,int cancelBtnIndex,int Item){

        at ItineraryTravllerDetailsPage
        //click on <Cancel> funciton button against 1st transfer item
        //clickOnCancelOrAmendTabButton(cancelBtnIndex)
        clickCancelOrAmendBtnBasedonItemIndxInBkdItms(0,0)
        sleep(3000)
        if(Item==1){
            //Cancel itinerary lightbox display status
            resultData.hotel.removeItemPage.actualCancelItemDispStatus_FirstItem=getCancelItemDisplayStatus()

        }else if(Item==2){
            //Cancel itinerary lightbox display status
            resultData.hotel.removeItemPage.actualCancelItemDispStatus_SecondItem=getCancelItemDisplayStatus()

        }else if (Item==3){
            //Cancel itinerary lightbox display status
            resultData.hotel.removeItemPage.actualCancelItemDispStatus_ThirdItem=getCancelItemDisplayStatus()

        }


    }

    protected def "Appliedcharges"(HotelSearchData data, HotelTransferTestResultData resultData,int Item){

        at ItenaryPageItems

        sleep(3000)
        if(Item==1){

        }else if(Item==2){

        }else if (Item==3){

        }


    }

    protected def "cancelitineraryMultiRoom"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //click on Cancel itinerary button
        //clickOnDeleteItenaryCTA()
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        //Cancel itinerary lightbox display status
        resultData.hotel.removeItemPage.actualCancelItemDispStatus=verifyRemoveItenary()

        //click on Yes to reconfirm cancelling itinerary
        clickYesCancelItem()
        sleep(7000)
        waitTillLoadingIconDisappears()

        int noOfItems=getNoOfItemsInUnavailableAndCancelItemsSection()
        List expectedStatus=[]
        List actualStatus = []

        for(int i=0;i<=noOfItems-1;i++) {

            actualStatus.add(getStatusDisplayed(i))
            sleep(3000)
            expectedStatus.add(data.expected.itemStatus)
        }



        resultData.hotel.removeItemPage.cancelledItems.put("actualItemStatus", actualStatus)
        resultData.hotel.removeItemPage.cancelledItems.put("expectedItemStatus", expectedStatus)


    }

    protected def "cancelitemlightbox"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //text - Cancel item
        resultData.hotel.removeItemPage.cancelledItems.actualCancelItemHeaderTxt=getCancellationHeader()
        //close X function
        resultData.hotel.removeItemPage.cancelledItems.actualClosebuttonDispStatus=overlayCloseButton()

        //Are you sure you want to cancel the following item?
        resultData.hotel.removeItemPage.cancelledItems.actualAreYouSureText=getTravellerCannotBeDeletedheaderText()

        try{
            //A cancellation charge of XXX.XX GBP applies.
            resultData.hotel.removeItemPage.cancelledItems.actualACancellationChargeText=getCancellationChargeText(0)
            resultData.hotel.removeItemPage.cancelledItems.actualCharge=verifyCancellationChargeText(data.expected.headerTextCancellationCharge)

        }catch(Exception e){
            //A cancellation charge of XXX.XX GBP applies.
            resultData.hotel.removeItemPage.cancelledItems.actualACancellationChargeText="Unable To Read Cancellation Charge Text From UI"
            resultData.hotel.removeItemPage.cancelledItems.actualCharge="Unable To Read Cancellation Charge Header Text From UI"

        }

        println("Charge Text:$resultData.hotel.removeItemPage.cancelledItems.actualCharge")
        //charge amount
        String amount=getCancellationChargeText(1)
        //charge currency
        String currency=getCancellationChargeText(2)
        resultData.hotel.removeItemPage.cancelledItems.actualCancelChargeAmount=amount+" "+currency

        //Item Total Amount And Currency
        resultData.hotel.removeItemPage.cancelledItems.actualItemAmountAndCurrency=getItenaryPriceInCancelItem()

        //item image
        resultData.hotel.removeItemPage.cancelledItems.actualImageDispStatus=imgDisplayedOnSugestedItem(4)

        //item type of car
        resultData.hotel.removeItemPage.cancelledItems.actualItemTypeOfCar=getTransferNameInSuggestedItem(3)

        //number of pax
        //resultData.hotel.removeItemPage.cancelledItems.actualPax=getItinenaryDescreptionInSuggestedItem(4)
        resultData.hotel.removeItemPage.cancelledItems.actualPax=getItineraryDescInRemoveItemScreen(0)
        //item title
        //resultData.hotel.removeItemPage.cancelledItems.actualItemTitle=getItinenaryDescreptionInSuggestedItem(5)
        resultData.hotel.removeItemPage.cancelledItems.actualItemTitle=getItineraryDescInRemoveItemScreen(1)

        //Cancellation fees apply
        resultData.hotel.removeItemPage.cancelledItems.actualCancelFeesApplyTxt=getCancelFeesApplyTxt()

        //duration icon
        resultData.hotel.removeItemPage.cancelledItems.actualDurationIconDisp=verifyItineryItemIconsDisplayedInCancelItemScreen("time",2)
        //duration value
        resultData.hotel.removeItemPage.cancelledItems.actualDurationText=verifyDuration(6)

        //luggage icon
        resultData.hotel.removeItemPage.cancelledItems.actualLuggageIconDisp=verifyItineryItemIconsDisplayedInCancelItemScreen("luggage",1)
        //luggage value
        resultData.hotel.removeItemPage.cancelledItems.actualLuggageText=verifyDuration(7)

        //pax icon
        resultData.hotel.removeItemPage.cancelledItems.actualPaxIconDisp=verifyItineryItemIconsDisplayedInCancelItemScreen("pax",1)
        //pax value
        resultData.hotel.removeItemPage.cancelledItems.actualPaxText=verifyDuration(8)

        try{
            //supporting language icon
            resultData.hotel.removeItemPage.cancelledItems.actualLanguageIconDisp=verifyItineryItemIconsDisplayedInCancelItemScreen("language",2)
            //supporting language value
            resultData.hotel.removeItemPage.cancelledItems.actualLanguageText=verifyDuration(9)

        }catch(Exception e){
            //supporting language icon
            resultData.hotel.removeItemPage.cancelledItems.actualLanguageIconDisp="Langugage Icon Unable To Read From UI"
            //supporting language value
            resultData.hotel.removeItemPage.cancelledItems.actualLanguageText="Language Text Unable to Read From UI"
        }

        //service date & time
        resultData.hotel.removeItemPage.cancelledItems.actualServiceDateAndtime=getItenaryDurationInCancelItem()
        resultData.hotel.removeItemPage.cancelledItems.expectedServiceDateAndtime=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()+" "+data.expected.timeOfArrival_Hrs+":"+data.expected.timeOfArrival_mins

        try{
            //Commission
            resultData.hotel.removeItemPage.cancelledItems.actualCommissionTextAndValue=verifyCancelItemCommission()

        }catch(Exception e){
            //Commission
            resultData.hotel.removeItemPage.cancelledItems.actualCommissionTextAndValue="Unable To Read Commission Text From UI"

        }
        resultData.hotel.removeItemPage.cancelledItems.expectedCommissionTextAndValue=data.expected.firstcmsnTxt+" "+data.expected.firstcommissionPercent+"%"

        //<No>  functional buttons
        resultData.hotel.removeItemPage.cancelledItems.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
        //<Yes> functional buttons
        resultData.hotel.removeItemPage.cancelledItems.actualYesButtonDispStatus=getYesNoDisplayStatus(2)


        //click on close X or anywhere outside the lightbox
        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(3000)
        //the lightbox disappeared
        resultData.hotel.removeItemPage.actualCancelItemPopupDispStatus=getCancelItemDisplayStatus()

        //check the item is not cancelled
        String statusOfItem
        boolean flag=false
        for(int i=0;i<3;i++){
            statusOfItem=getStatusDisplayed(i)
            if(statusOfItem==data.expected.itemStatus){
                flag=true
                break
            }
        }

        resultData.hotel.removeItemPage.actualItemStatus=flag

        //click on <Cancel> funciton button against 1st transfer item
        clickOnCancelOrAmendTabButton(1)
        sleep(3000)
        //Cancel itinerary lightbox display status
        resultData.hotel.removeItemPage.actCancelItemDispStatus_FirstItem=getCancelItemDisplayStatus()

        //click <No> functinal button
        clickNoCancelItem()
        sleep(3000)
        //the lightbox disappeared
        resultData.hotel.removeItemPage.actCancelItemPopupDispStatus=getCancelItemDisplayStatus()

        //check the item is not cancelled
        String statsOfItem
        boolean checkflag=false
        for(int i=0;i<3;i++){
            statsOfItem=getStatusDisplayed(i)
            if(statsOfItem==data.expected.itemStatus){
                checkflag=true
                break
            }
        }

        resultData.hotel.removeItemPage.actItemStatus=checkflag

        //click on <Cancel> funciton button against 1st transfer item
        //clickOnCancelOrAmendTabButton(1)
        clickCancelOrAmendBtnBasedonItemIndxInBkdItms(0,0)
        sleep(3000)
        //Cancel itinerary lightbox display status
        resultData.hotel.removeItemPage.actCancelItmDispStatus_FirstItem=getCancelItemDisplayStatus()


    }

    protected def "cancelled1"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //click on <Yes> functional button
        clickYesCancelItem()
        sleep(7000)
        //waitTillLoadingIconDisappears()
        waitTillItineraryGetsCancelled()


    }

    protected def "cancelitinerarylightbox"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //lightbox title text should show
        resultData.hotel.removeItemPage.actualTitleText=getBookingStatusOnLightBox()
        //text and funciton should show
        resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()
        //This itinerary has booked items.  Deleting this itinerary will cancel these items. text should show
        //resultData.hotel.removeItemPage.actualHeaderText=cancelItineraryHeadingDisplayed(data.expected.headerText,0)
        //A cancellation charge of XXX.XX GBP applies. - should not show
        //resultData.hotel.removeItemPage.actualHeaderNotShowText=cancelItineraryHeadingDisplayed(data.expected.headerTextNotShow)
        //Are you sure you want to delete this itinerary? - Text should show
        //resultData.hotel.removeItemPage.actualHeaderAreYouSureText=cancelItineraryHeadingDisplayed(data.expected.headerTextAreYouSure,1)
        resultData.hotel.removeItemPage.actualHeaderAreYouSureText=cancelItineraryHeadingDisplayed(data.expected.headerTextAreYouSure,0)



    }

    protected def "cancelitinrylightbox"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //lightbox title text should show
        resultData.hotel.removeItemPage.actualTitleText=getBookingStatusOnLightBox()
        //text and funciton should show
        resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()
        //This itinerary has booked items.  Deleting this itinerary will cancel these items. text should show
        resultData.hotel.removeItemPage.actualHeaderText=cancelItineraryHeadingDisplayed(data.expected.headerText)
        //A cancellation charge of XXX.XX GBP applies. - should show Total amount should be item 1 + 2 + 3
        try{
            resultData.hotel.removeItemPage.actualCancellationChargeText=getCancellationChargeTextInCancelItinry()
        }catch(Exception e){
            resultData.hotel.removeItemPage.actualCancellationChargeText="Unable To Read From UI"
        }


        //Expected text
        DecimalFormat df = new DecimalFormat("###0.00");

        float totalPrice=Float.parseFloat(resultData.hotel.itineraryPage.retrieveFirstItemPrice.toString())+Float.parseFloat(resultData.hotel.itineraryPage.retrieveSecondItemPrice.toString())+Float.parseFloat(resultData.hotel.itineraryPage.retrieveThirdItemPrice.toString())
        resultData.hotel.removeItemPage.calculatedCancellationChargePrice=df.format(totalPrice)+" GBP"
        resultData.hotel.removeItemPage.expectedCancellationChargeText=data.expected.headerTextCancellationCharge_part1+df.format(totalPrice)+" GBP"+data.expected.headerTextCancellationCharge_part2
        try {
            //Are you sure you want to delete this itinerary? - Text should show
            //resultData.hotel.removeItemPage.actualHeaderAreYouSureText = getCancellationChargeTextInCancelItinry(2)
            resultData.hotel.removeItemPage.actualHeaderAreYouSureText = getCancellationChargeTextInCancelItinry(0)
        }
        catch(Exception e){
            resultData.hotel.removeItemPage.actualHeaderAreYouSureText = "Unable to Read From UI"
        }



    }

    protected def "cancelitinerarylightboxNonBooked"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItenaryPageItems
        //lightbox title text should show
        resultData.hotel.removeItemPage.actualTitleText=getBookingStatusOnLightBox()
        //text and funciton should show
        resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()
        //Are you sure you want to delete this itinerary? - Text should show

        resultData.hotel.removeItemPage.actualHeaderAreYouSureTextDispStatus=cancelItineraryHeadingDisplayed(data.expected.headerTextAreYouSure)
        //A cancellation charge of XXX.XX GBP applies. - should not show
        resultData.hotel.removeItemPage.actualHeaderNotShowText=cancelItineraryHeadingDisplayed(data.expected.headerTextNotShow)


    }

    protected def "Itinerarydetails"(HotelSearchData data, HotelTransferTestResultData resultData,int statusCode,int testCaseNo){

        at ItineraryTravllerDetailsPage
        if(statusCode==0){
            //Itinerary status should show status - Booked
            //resultData.hotel.removeItemPage.actualItineraryStatusBookedText=getItineraryStatusTxt()
        }else if(statusCode==1){
            //Itinerary status should show status - Partially Booked
            //resultData.hotel.removeItemPage.actualItineraryStatusPartiallyBookedText=getItineraryStatusTxt()
        }else if(statusCode==2){
            //Itinerary status should show status - Quote
            //resultData.hotel.removeItemPage.actualItineraryStatusQuoteText=getItineraryStatusTxt()

        }
        //Itinerary status should show status - Booked
        //resultData.hotel.removeItemPage.actualItineraryStatusText=getItineraryStatusTxt()
        //Itinerary Departure date & value
        //resultData.hotel.removeItemPage.actualDepartureDateTxt=trimAllWhitespaces(getItineraryDepartureDateTxt(),testCaseNo)

        //println("Departure Date Text -$resultData.hotel.removeItemPage.actualDepartureDateTxt")
        //resultData.hotel.removeItemPage.expectedDepartureDateTxt="Departure: "+dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
        //resultData.hotel.removeItemPage.expectedDepartureDateTxt=trimAllWhitespaces(resultData.hotel.removeItemPage.expectedDepartureDateTxt.toString(),testCaseNo)
        //Itinerary ID, Name & value
        //resultData.hotel.removeItemPage.actualItineraryIdNameValueTxt=getItineraryIdNameValueTxt()

        //Lead Name and Number of pax
        //resultData.hotel.removeItemPage.actualItineraryLeadNamePaxText=getItineraryLeadNamePaxTxt()
        //resultData.hotel.removeItemPage.expectedItineraryLeadNamePaxText=data.expected.firstName+" "+data.expected.lastName+" ("+data.input.hotelPax+" PAX"+")"

        //Create Date Text
        //resultData.hotel.removeItemPage.actualItineraryCreateDateTxt=trimAllWhitespaces(getCreateDateTxt(),testCaseNo)
        //resultData.hotel.removeItemPage.expectedItineraryCreateDateTxt="Created "+dateUtil.addDaysChangeDatetformat(0, "ddMMMyy").toUpperCase()
        //resultData.hotel.removeItemPage.expectedItineraryCreateDateTxt=trimAllWhitespaces(resultData.hotel.removeItemPage.expectedItineraryCreateDateTxt.toString(),testCaseNo)
    }

    protected def "functionbuttons"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //<No>  functional buttons
        resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)

        //<Yes> functional buttons
        resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

        clickOnCloseButtonSuggestedItemsCancelpopup()
        //the lightbox disappeared
        resultData.hotel.removeItemPage.actuallightboxDispStatus=getCancelItemDisplayStatus()
        //user taken back to itinerary page
        //check the items are not cancelled

        //click on Cancel itinerary button
        //clickOnDeleteItenaryCTA()
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)

        //Cancel itinerary lightbox display status
        resultData.hotel.removeItemPage.actualCancelItemLightBoxDispStatus=verifyRemoveItenary()

    }

    protected def "functions"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //<No>  functional buttons
        resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)

        //<Yes> functional buttons
        resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)


    }

    protected def "functionbuttonsNonBooked"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //<No>  functional buttons
        resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)

        //<Yes> functional buttons
        resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

        //click on <No> button
        clickNoCancelItem()
        sleep(5000)

        //user taken back to itinerary page
        //resultData.hotel.removeItemPage.actualHeaderNonBookedItemText=getHeaderTextInItineraryPage(2)
        resultData.hotel.removeItemPage.actualHeaderNonBookedItemText=getHeaderTextInItineraryPage(0)

    }

    protected def "NonBookedblock"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage

        //transfer item #1
        resultData.hotel.itineraryPage.actualTransferItem1=getHotelName(0).trim()
        //hotel item #2
        resultData.hotel.itineraryPage.actualHotelItem2=getHotelName(1).trim()
        //activity item #3
        resultData.hotel.itineraryPage.actualActivityItem3=getHotelName(2).trim()

    }

    protected def "Cancelled"(HotelSearchData data, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        clickYesOnRemoveItenary()
        sleep(7000)
        //Traveler Details label text is removed from 10.3. Hence changing it a first traveler label name
        //resultData.hotel.removeItemPage.cancelledItems.actualTravellerLabelTxt=getHeaderTextInItineraryPage(1)
        String travlrLablText=getTravellelersLabelName(0)
        resultData.hotel.removeItemPage.cancelledItems.actualTravellerLabelTxt=travlrLablText
        resultData.hotel.removeItemPage.cancelledItems.expectedTravellerLabelTxt=data.expected.travlrLabelText.toString()+" (lead)"

    }

    protected def "Unavailableblock"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

       sleep(3000)
        //Unavailable and Cancelled Items
        resultData.hotel.removeItemPage.cancelledItems.actualUnavailableAndCanncedItemsText=getHeaderTxtInUnavailableItemsListScrn()
        //section layout, colours
        resultData.hotel.removeItemPage.cancelledItems.actualUnavailableAndCanceledItemColr=getBackgroundColorForUnavailableItems(2)
    }

    protected def "transferitemcard"(HotelSearchData data, HotelTransferTestResultData resultData,int index){

        at ItineraryTravllerDetailsPage
        sleep(3000)
        resultData.transfer.unavailableAndCancelItems.put("item1", getTransferItemCardDetailsInUnavailableAndCancelled(index))
        //Expected Booking ID & Value
        resultData.transfer.unavailableAndCancelItems.expectedFirstBookingIdAndVal="Booking ID: "+resultData.hotel.itineraryPage.actualFirstBookingIdValue


        //Expected Image Src URL
        resultData.transfer.unavailableAndCancelItems.expectedImageURL=resultData.hotel.itineraryPage.retrieveFirstBookingItemImageSrcURL
        //Expected Car type
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCarType=resultData.hotel.itineraryPage.retrieveFirstItemCarType
        //Expected First Pax
        resultData.transfer.unavailableAndCancelItems.expectedFirstItempax=resultData.hotel.itineraryPage.retrieveFirstTransferitemPax
        //Expected Title
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemTitle=resultData.hotel.itineraryPage.retrieveFirstTransferitemTitle
        //Expected Cancellation Text
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancellationText=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancellationText
        //Expected Cancellation Link
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLink=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLink
        //Expected Cancellation Link Icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLinkIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLinkIcon

        //Click on cancellation charge link
        //clickOnCancellationChargesLink(0)
        clickOnCancellationChargesLink(2)

        //click on close
        clickOnCloseLightBox()

        //duration icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemDurationIcon
        //duration value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemDurationValue

        //luggage icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemLuggageIcon
        // luggage value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemLuggageValue
        //pax icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemPaxIcon
        // pax value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemPaxValue
        //supporting language icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLanguageIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemLanguageIcon
        // supporting language value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLangaugeValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemLanguageValue

        //Duration Icon
        resultData.transfer.unavailableAndCancelItems.actualDurationIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("time",1)
        //Duration Value
        resultData.transfer.unavailableAndCancelItems.actualDurationValue=verifyDuration(2)

        //Luggage Icon
        resultData.transfer.unavailableAndCancelItems.actualLuggageIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("luggage",0)
        //Luggage value
        resultData.transfer.unavailableAndCancelItems.actualLuggageValue=verifyDuration(3)
        //pax Icon
        resultData.transfer.unavailableAndCancelItems.actualPaxIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("pax",0)
        //Pax Value
        resultData.transfer.unavailableAndCancelItems.actualPaxValue=verifyDuration(4)
        //Language Icon
        resultData.transfer.unavailableAndCancelItems.actualLanguageIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("language",1)
        //Language Value
        resultData.transfer.unavailableAndCancelItems.actualLanguageValue=verifyDuration(5)

/*


        //Booking ID & value
        resultData.hotel.removeItemPage.cancelledItems.actualBookingIDAndValue=getBookingIDinUnavailableItemsScrn(1)
        resultData.hotel.removeItemPage.cancelledItems.expectedBookingIDAndValue="Booking ID: "+resultData.hotel.itineraryPage.actualFirstBookingIdValue

        //status - Cancelled
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelledStatus=getCancelStatusDisplayed(1)

        //item image
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemImageStatus=imgDisplayedOnSugestedItem(2)

        //item type of car
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemTypeOfCar=verifyCancelledBookingItemPropName(data.expected.firstItemTypeOfCar,0)

        //number of pax
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemPax=verifyCancelledBookedItemRoomAndPax(data.expected.firstItemPax,0)

        //item title
        resultData.hotel.removeItemPage.cancelledItems.actualFirstTitleText=verifyCancelledBookedItemRoomAndPax(data.expected.firstItem_transferTitle_txt,1)

        //Cancellation fees apply - text, link & icon
*/
        //pick up details
        resultData.transfer.unavailableAndCancelItems.Item1actualPickUpDetails=getPickUpAndDropOffDetails_BookedItems(0)
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPickUpDetails=resultData.hotel.itineraryPage.retrieveFirstItemPickUpDetails

        //drop off details
        resultData.transfer.unavailableAndCancelItems.Item1actualDropOffDetails=getPickUpAndDropOffDetails_BookedItems(1)
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDropOffDetails=resultData.hotel.itineraryPage.retrieveFirstItemDropOffDetails


    }

    protected def "item1transferitemcardCancelItinerary"(HotelSearchData data, HotelTransferTestResultData resultData,int index){

        at ItineraryTravllerDetailsPage
        sleep(3000)
        resultData.transfer.unavailableAndCancelItems.put("item1", getTransferItemCardDetailsInUnavailableAndCancelled(index))
        //Expected Booking ID & Value
        resultData.transfer.unavailableAndCancelItems.expectedFirstBookingIdAndVal="Booking ID: "+resultData.hotel.itineraryPage.actualFirstBookingIdValue


        //Expected Image Src URL
        resultData.transfer.unavailableAndCancelItems.expectedImageURL=resultData.hotel.itineraryPage.retrieveFirstBookingItemImageSrcURL
        //Expected Car type
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCarType=resultData.hotel.itineraryPage.retrieveFirstItemCarType
        //Expected First Pax
        resultData.transfer.unavailableAndCancelItems.expectedFirstItempax=resultData.hotel.itineraryPage.retrieveFirstTransferitemPax
        //Expected Title
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemTitle=resultData.hotel.itineraryPage.retrieveFirstTransferitemTitle
        //Expected Cancellation Text
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancellationText=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancellationText
        //Expected Cancellation Link
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLink=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLink
        //Expected Cancellation Link Icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLinkIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLinkIcon

        //Click on cancellation charge link
        clickOnCancellationChargesLink(0)

        //click on close
        clickOnCloseLightBox()

        //duration icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemDurationIcon
        //duration value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemDurationValue

        //luggage icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemLuggageIcon
        // luggage value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemLuggageValue
        //pax icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemPaxIcon
        // pax value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemPaxValue
        //supporting language icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLanguageIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemLanguageIcon
        // supporting language value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLangaugeValue=resultData.hotel.itineraryPage.retrieveFirstTransferitemLanguageValue

        //Duration Icon
        resultData.transfer.unavailableAndCancelItems.actualDurationIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("time",1)
        //Duration Value
        resultData.transfer.unavailableAndCancelItems.actualDurationValue=verifyDuration(0)

        //Luggage Icon
        resultData.transfer.unavailableAndCancelItems.actualLuggageIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("luggage",0)
        //Luggage value
        resultData.transfer.unavailableAndCancelItems.actualLuggageValue=verifyDuration(1)
        //pax Icon
        resultData.transfer.unavailableAndCancelItems.actualPaxIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("pax",0)
        //Pax Value
        resultData.transfer.unavailableAndCancelItems.actualPaxValue=verifyDuration(2)
        //Language Icon
        resultData.transfer.unavailableAndCancelItems.actualLanguageIcon=verifyItineryItemIconsDisplayedInCancelItemScreen("language",1)
        //Language Value
        resultData.transfer.unavailableAndCancelItems.actualLanguageValue=verifyDuration(3)



/*


        //Booking ID & value
        resultData.hotel.removeItemPage.cancelledItems.actualBookingIDAndValue=getBookingIDinUnavailableItemsScrn(1)
        resultData.hotel.removeItemPage.cancelledItems.expectedBookingIDAndValue="Booking ID: "+resultData.hotel.itineraryPage.actualFirstBookingIdValue

        //status - Cancelled
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelledStatus=getCancelStatusDisplayed(1)

        //item image
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemImageStatus=imgDisplayedOnSugestedItem(2)

        //item type of car
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemTypeOfCar=verifyCancelledBookingItemPropName(data.expected.firstItemTypeOfCar,0)

        //number of pax
        resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemPax=verifyCancelledBookedItemRoomAndPax(data.expected.firstItemPax,0)

        //item title
        resultData.hotel.removeItemPage.cancelledItems.actualFirstTitleText=verifyCancelledBookedItemRoomAndPax(data.expected.firstItem_transferTitle_txt,1)

        //Cancellation fees apply - text, link & icon
*/


    }

    protected def "item1transferitemcard"(HotelSearchData data, HotelTransferTestResultData resultData,int index){
        resultData.transfer.unavailableAndCancelItems.put("item1", getTransferItemCardDetailsInUnavailableAndCancelled(index))
        //Expected Booking ID & Value
        resultData.transfer.unavailableAndCancelItems.expectedFirstBookingIdAndVal="Booking ID: "+resultData.hotel.itineraryPage.retrieveFirstBookingId
        //Expected Image Src URL
        resultData.transfer.unavailableAndCancelItems.expectedImageURL=resultData.hotel.itineraryPage.retrieveFirstBookingItemImageSrcURL
        //Expected Car type
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCarType=resultData.hotel.itineraryPage.retrieveFirstItemCarType
        //Expected First Pax
        resultData.transfer.unavailableAndCancelItems.expectedFirstItempax=resultData.hotel.itineraryPage.retrieveFirstTransferitemPax
        //Expected Title
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemTitle=resultData.hotel.itineraryPage.retrieveFirstTransferitemTitle
        //Expected Cancellation Text
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancellationText=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancellationText
        //Expected Cancellation Link
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLink=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLink
        //Expected Cancellation Link Icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLinkIcon=resultData.hotel.itineraryPage.retrieveFirstTransferitemCancelLinkIcon

        //Click on cancellation charge link
        clickOnCancellationChargesLink(0)
        sleep(3000)

        //Cancellation policy text
        resultData.transfer.unavailableAndCancelItems.Item1actualCancellationPolicyText=getCancellationPolicyPopupInnerTxt()
        //Cancellation policy text
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancellationPolicyTxt=resultData.hotel.itineraryPage.retrieveFirstTransferCancellationPolicyText

        //click on close
        clickOnCloseLightBox()

        //duration icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationIcon=resultData.hotel.itineraryPage.retrieveFirstItemDurationIconDisplayStatus
        //duration value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationValue=resultData.hotel.itineraryPage.retrieveFirstItemDurationIconText

        //luggage icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageIcon=resultData.hotel.itineraryPage.retrieveFirstItemLuggageIconDisplayStatus
        //luggage value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageValue=resultData.hotel.itineraryPage.retrieveFirstItemLuggageIconText

        //pax icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxIcon=resultData.hotel.itineraryPage.retrieveFirstItemPaxIconDisplayStatus
        //Pax value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxValue=resultData.hotel.itineraryPage.retrieveFirstItempaxIconText

        //supporting language icon
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLanguageIcon=resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconDisplayStatus
        //Language value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemLangaugeValue=resultData.hotel.itineraryPage.retrieveFirstItemLanguageIconText

        //service date
        //service time
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemServiceDateTimeText=resultData.hotel.itineraryPage.retrieveFirstItemServiceDateTime

        //commission and % value
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemcommisionPercentValue=resultData.hotel.itineraryPage.retrieveFirstItemCommissionPercentAndValue
        //total service item amount and currency
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemTotalServItemAmntCurncy=resultData.hotel.itineraryPage.retrieveFirstItemTotalAmountAndCurncy

        //Traveller name list
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemTravelrNameList=resultData.hotel.itineraryPage.retrieveFirstItemTravellerNameList


        //pick up details
        resultData.transfer.unavailableAndCancelItems.Item1actualPickUpDetails=getPickUpAndDropOffDetails_BookedItems(0)
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemPickUpDetails=resultData.hotel.itineraryPage.retrieveFirstItemPickUpDetails

        //drop off details
        resultData.transfer.unavailableAndCancelItems.Item1actualDropOffDetails=getPickUpAndDropOffDetails_BookedItems(1)
        resultData.transfer.unavailableAndCancelItems.expectedFirstItemDropOffDetails=resultData.hotel.itineraryPage.retrieveFirstItemDropOffDetails

    }
    protected def getTransferItemCardDetailsInUnavailableAndCancelled(int index){
          at ItineraryTravllerDetailsPage
            Map dataMap=[:]
            //Booking ID & value
            String bookingIdAndVal="Booking ID: "+getBookingIDBookedDetailsScrn(index+1)
            dataMap.actualBookingIdAndVal=bookingIdAndVal

            //status - Cancelled
            dataMap.actualTransferItemStatus=getStatusInBookedItemsScrn(index+1)

            //item image
            dataMap.actualItmeImage=getItemsImageURLInItinerary(index)

            //item type of car
            dataMap.actualItemTypeOfCar=getTransferNameInSuggestedItem(index)

            //number of pax
            dataMap.actualTransferItemPax=getTransfersItemsPax(index)

            //item title
            dataMap.actualTransferItemTitle=getTransfersItemTitle(index)

            //Cancellation fees apply - text
            dataMap.actualTransferItemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(index)
            // Cancellation fees applylink
            dataMap.actualTransferItemCancelLink=getCancellationLinkExistenceInItinerary(index)
            // Cancellation fees apply icon
            dataMap.actualTransferItemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(index)

        //duration icon
        dataMap.ItemDurationIconDisplayStatus=verifyItineryItemIconsDisplayInCancelItemScreen("time",index)
        //duration value
        dataMap.ItemDurationIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",index)

        //luggage icon
        dataMap.ItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplayInCancelItemScreen("luggage",index)
        //luggage value
        dataMap.ItemLuggageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("luggage",index)

        //pax icon
        dataMap.ItemPaxIconDisplayStatus=verifyItineryItemIconsDisplayInCancelItemScreen("pax",index)
        //Pax value
        dataMap.ItempaxIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("pax",index)

        try{
            //supporting language icon
            dataMap.ItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplayInCancelItemScreen("language",index)
            //Language value
            dataMap.ItemLanguageIconText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",index)

        }catch(Exception e){
            //supporting language icon
            dataMap.ItemLanguageIconDisplayStatus="Unable To Read Icon Disp Status From UI"
            //Language value
            dataMap.ItemLanguageIconText="Unable To Read Icon Text From UI"

        }

        //service date
        //service time
        dataMap.ItemServiceDateTime=getItenaryDurationInUnavailableItem(index+1)

        //commission and % value
        dataMap.ItemCommissionPercentAndValue=getCommisionAndPercentageInUnavailableItem(index+1)
        //total service item amount and currency
        dataMap.ItemTotalAmountAndCurncy=getItenaryPriceInBookedItem(index+1)

        //Traveller name list
        dataMap.ItemTravellerNameList=getTravellernamesUnavailableItem(index+1)


        return dataMap

    }

    protected def "item2hotelitemcard"(HotelSearchData data, HotelTransferTestResultData resultData,int hotelIndex){
        resultData.hotel.unavailableAndCancelItems.put("item2", getHotelItemCardDetailsInUnavailableAndCancelled(hotelIndex))

        //Expected Booking ID & Value
        resultData.hotel.unavailableAndCancelItems.expectedSecondBookingIdAndVal="Booking ID: "+resultData.hotel.itineraryPage.retrieveSecondBookingId

        //item image
        resultData.hotel.unavailableAndCancelItems.expectedSecondImageURL=resultData.hotel.itineraryPage.retrieveSecondBookingItemImageSrcURL

        //item name and link
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemName=resultData.hotel.itineraryPage.retrieveSecondItemName

        //hotel star rating
        resultData.hotel.unavailableAndCancelItems.actualSecondItemHotelStarRatng=getRatingForTheHotelInSuggestedItem(0)
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemHotelStarRatng=resultData.hotel.itineraryPage.retrieveSecondItemHotelStarRatng

        String roomdescComplTxt_first=getHotelRoomTypePaxMealBasisDetails(hotelIndex)
        List<String> tempItineraryDescList_first=roomdescComplTxt_first.tokenize(",")

        String roomDescText_first=tempItineraryDescList_first.getAt(0)
        //room type name
        resultData.hotel.unavailableAndCancelItems.actualSecondItemroomdescTxt=roomDescText_first.trim()
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemroomdescTxt=resultData.hotel.itineraryPage.retrieveSecondItemroomdescTxt
        //Pax number
        String tmpTxt_first=tempItineraryDescList_first.getAt(1)
        List<String> tempDescList_first=tmpTxt_first.tokenize(".")

        String paxNum_first=tempDescList_first.getAt(0)
        resultData.hotel.unavailableAndCancelItems.actualSecondItemPaxNum=paxNum_first.trim()
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemPaxNum=resultData.hotel.itineraryPage.retrieveSecondItemPaxNum
        //Rate plan - meal basis
        String ratePlantxt_first=tempDescList_first.getAt(1)
        resultData.hotel.unavailableAndCancelItems.actualSecondItemMealbasis=ratePlantxt_first.trim()
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemMealbasis=resultData.hotel.itineraryPage.retrieveSecondItemMealbasis

        //cancellation fee apply - text, link & icon
        //Cancellation Text
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancellationText=resultData.hotel.itineraryPage.retrieveSeconditemCancellationText
        //Cancellation Link
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancelLink=resultData.hotel.itineraryPage.retrieveSeconditemCancelLink
        //Cancellation Link Icon
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancelLinkIcon=resultData.hotel.itineraryPage.retrieveSeconditemCancelLinkIcon


        //Click on cancellation charge link
        clickOnCancellationChargesLink(hotelIndex)
        sleep(3000)

        //Cancellation policy text
        resultData.hotel.unavailableAndCancelItems.Item2actualCancellationPolicyText=getCancellationPolicyPopupInnerTxt()
        //Cancellation policy text
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancellationPolicyText=resultData.hotel.itineraryPage.retrieveSecondHotelCancellationPolicyText

        //click on close
        clickOnCloseLightBox()

        //check in date - stay night(s)
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemChkInDateNights=resultData.hotel.itineraryPage.retrieveSecondItemChkInDateNights

        //commission and %
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemCommissionPercentAndValue=resultData.hotel.itineraryPage.retrieveSecondItemCommissionPercentAndValue
        //        item amount and currency
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemTotalAmountAndCurncy=resultData.hotel.itineraryPage.retrieveSecondItemTotalAmountAndCurncy
        //Traveller name list
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemTravellerNameList=resultData.hotel.itineraryPage.retrieveSecondItemTravellerNameList

        //Add a remark or comment
        //Add a Remark or Comment Text
        resultData.hotel.unavailableAndCancelItems.expectedSecondItemAddRemarkOrCommentText=resultData.hotel.itineraryPage.retrieveSecondItemAddRemarkOrCommentText
        //Edit remark or comment
        //resultData.hotel.unavailableAndCancelItems.actualSecondItemEditRemarkOrCommentDispStatus=getEditIconInBookedDetailsScrnDisplayStatus(1)
        resultData.hotel.unavailableAndCancelItems.actualSecondItemEditRemarkOrCommentDispStatus=getEditIconInBookedDetailsScrnDisplayStatus()



    }

    protected def getHotelItemCardDetailsInUnavailableAndCancelled(int index){

        at ItineraryTravllerDetailsPage
        sleep(3000)

        Map dataMap=[:]
        //Booking ID & value
        String bookingIdAndVal="Booking ID: "+getBookingIDBookedDetailsScrn(index+1)
        dataMap.actualBookingIdAndVal=bookingIdAndVal
        sleep(3000)
        //status - Cancelled
        dataMap.actualHotelItemStatus=getStatusInBookedItemsScrn(index+1)

        //item image
        dataMap.actualItemImage=getItemsImageURLInItinerary(index)

        //item name
        dataMap.actualItemName=getTransferNameInSuggestedItem(index)

        //Cancellation fees apply - text
        dataMap.actualItemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(index)
        // Cancellation fees applylink
        dataMap.actualItemCancelLink=getCancellationLinkExistenceInItinerary(index)
        // Cancellation fees apply icon
        dataMap.actualItemCancelLinkIcon=getCancellationLinkIconExistenceInItinerary(index)

        //Check IN Date Nights
        dataMap.actualCheckInDateNights=getDateTimePaxDetails(index)

        //Commission and %
        dataMap.actualCommissionAndPercent=getCommisionAndPercentageInBookeddetailsScrn(index)

        //Amount & Currency
        dataMap.actualAmountAndCurrency=getItenaryPriceInBookedItem(index+1)

        //Travellers list
        dataMap.actualTravellersList=getTravellernamesUnavailableItem(index+1)

        //Add a remark or comment
        dataMap.actualAddRemarkOrCommentText=getHotelAddRemarkOrCommentText(index)

        return dataMap
    }

    protected def "item3activityitemcard"(HotelSearchData data, HotelTransferTestResultData resultData,int activityIndex){
        resultData.activity.unavailableAndCancelItems.put("item3", getActivityItemCardDetailsInUnavailableAndCancelled(activityIndex))

        //Expected Booking ID & Value
        resultData.activity.unavailableAndCancelItems.expectedThirdBookingIdAndVal="Booking ID: "+resultData.hotel.itineraryPage.retrieveThirdBookingId

        //Expected Image Src URL
        resultData.activity.unavailableAndCancelItems.expectedImageURL=resultData.hotel.itineraryPage.retrieveThirdBookingItemImageSrcURL

        //Expected Item Name
        resultData.activity.unavailableAndCancelItems.expectedThirdItemName=resultData.hotel.itineraryPage.retrieveThirdItemName

        //Expected Cancellation Text
        resultData.activity.unavailableAndCancelItems.expectedThirdItemCancellationText=resultData.hotel.itineraryPage.retrieveThirditemCancellationText

        //Expected Duration Icon
        resultData.activity.unavailableAndCancelItems.expectedThirdItemDurationIconStatus=resultData.hotel.itineraryPage.retrieveThirditemDurationIconStatus

        //Expected Duration Value
        resultData.activity.unavailableAndCancelItems.expectedThirdItemDurationValue=resultData.hotel.itineraryPage.retrieveThirditemDurationValue

        //Expected Language Icon
        resultData.activity.unavailableAndCancelItems.expectedThirdItemLangIconStatus=resultData.hotel.itineraryPage.retrieveThirditemLangIconStatus

        //Expected Language Value
        resultData.activity.unavailableAndCancelItems.expectedThirdItemLangValue=resultData.hotel.itineraryPage.retrieveThirditemLangValue

        //service date & Pax
        resultData.activity.unavailableAndCancelItems.expectedThirdItemDateAndPax=resultData.hotel.itineraryPage.retrieveThirditemServiceDateAndPax

        //Commission and %
        resultData.activity.unavailableAndCancelItems.expectedThirdItemCommissionAndPercent=resultData.hotel.itineraryPage.retrieveThirditemCommissionAndPercent

        //Amount & Currency
        resultData.activity.unavailableAndCancelItems.expectedThirdItemAmountAndCurrency=resultData.hotel.itineraryPage.retrieveThirditemAmountAndCurrency

        //Traveller name list
        resultData.activity.unavailableAndCancelItems.expectedThirdItemTravellers=resultData.hotel.itineraryPage.retrieveThirditemTravellers
    }

    protected def getActivityItemCardDetailsInUnavailableAndCancelled(int index){

        at ItineraryTravllerDetailsPage
        Map dataMap=[:]
        //Booking ID & value
        String bookingIdAndVal="Booking ID: "+getBookingIDBookedDetailsScrn(index+1)
        dataMap.actualBookingIdAndVal=bookingIdAndVal

        //status - Cancelled
        dataMap.actualActivityItemStatus=getStatusInBookedItemsScrn(index+1)

        //item image
        dataMap.actualItemImage=getItemsImageURLInItinerary(index)

        //item name
        dataMap.actualItemName=getTransferNameInSuggestedItem(index)

        //cancellation charge apply
        dataMap.actualItemCancellationText=getItinenaryFreeCnclTxtInSuggestedItem(index)

        //duration icon
        dataMap.actualdurationIconStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("time",index)

        //duration value
        dataMap.actualdurationText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("time",index)

        //language supported item
        dataMap.actualLangIconStatus=getItinrytemIconsDispInUnvailAndCanclItemScrn("language",index)

        //language supported value
        dataMap.actualLangText=getItinrytemIconsTextDispInUnvailAndCanclItemScrn("language",index)

        //Service date & pax
        dataMap.actualDateAndPax=getDateTimePaxDetails(index)

        //Commission and %
        dataMap.actualCommissionAndPercent=getCommisionAndPercentageInBookeddetailsScrn(index)

        //Amount & Currency
        dataMap.actualAmountAndCurrency=getItenaryPriceInBookedItem(index+1)

        //Travellers list
        dataMap.actualTravellersList=getTravellernamesUnavailableItem(index+1)

        return dataMap
    }
    protected def "item4transferitemcard"(HotelSearchData data, HotelTransferTestResultData resultData, int transferIndex){
        resultData.transfer.unavailableAndCancelItems.put("item4", getTransferItemCardDetailsInUnavailableAndCancelled(transferIndex))
        //Expected Booking ID & Value
        resultData.transfer.unavailableAndCancelItems.expectedFourthBookingIdAndVal="Booking ID: "+resultData.transfer.itineraryPage.retrieveFourthBookingId
        //Expected Image Src URL
        resultData.transfer.unavailableAndCancelItems.expectedFourthImageURL=resultData.transfer.itineraryPage.retrieveFourthBookingItemImageSrcURL
        //Expected Car type
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemCarType=resultData.transfer.itineraryPage.retrieveFourthItemCarType
        //Expected  Pax
        resultData.transfer.unavailableAndCancelItems.expectedFourthItempax=resultData.transfer.itineraryPage.retrieveFourthTransferitemPax

        //Expected Title
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemTitle=resultData.transfer.itineraryPage.retrieveFourthTransferitemTitle
        //Expected Cancellation Text
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancellationText=resultData.transfer.itineraryPage.retrieveFourthTransferitemCancellationText
        //Expected Cancellation Link
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancelLink=resultData.transfer.itineraryPage.retrieveFourthTransferitemCancelLink
        //Expected Cancellation Link Icon
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancelLinkIcon=resultData.transfer.itineraryPage.retrieveFourthTransferitemCancelLinkIcon

        //Click on cancellation charge link
        clickOnCancellationChargesLink(3)
        sleep(3000)

        //Cancellation policy text
        resultData.transfer.unavailableAndCancelItems.Item4actualCancellationPolicyText=getCancellationPolicyPopupInnerTxt()
        //Cancellation policy text
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancellationPolicyTxt=resultData.transfer.itineraryPage.retrieveFourthTransferCancellationPolicyText

        //click on close
        clickOnCloseLightBox()


        //duration icon
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemDurationIcon=resultData.hotel.itineraryPage.retrieveFourthItemDurationIconDisplayStatus
        //duration value
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemDurationValue=resultData.hotel.itineraryPage.retrieveFourthItemDurationIconText

        //luggage icon
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemLuggageIcon=resultData.hotel.itineraryPage.retrieveFourthItemLuggageIconDisplayStatus
        //luggage value
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemLuggageValue=resultData.hotel.itineraryPage.retrieveFourthItemLuggageIconText

        //pax icon
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemPaxIcon=resultData.hotel.itineraryPage.retrieveFourthItemPaxIconDisplayStatus
        //Pax value
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemPaxValue=resultData.hotel.itineraryPage.retrieveFourthItempaxIconText

        //supporting language icon
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemLanguageIcon=resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconDisplayStatus
        //Language value
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemLangaugeValue=resultData.hotel.itineraryPage.retrieveFourthItemLanguageIconText

//service date
        //service time
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemServiceDateTimeText=resultData.hotel.itineraryPage.retrieveFourthItemServiceDateTime

        //commission and % value
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemcommisionPercentValue=resultData.hotel.itineraryPage.retrieveFourthItemCommissionPercentAndValue
        //total service item amount and currency
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemTotalServItemAmntCurncy=resultData.hotel.itineraryPage.retrieveFourthItemTotalAmountAndCurncy

        //Traveller name list
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemTravelrNameList=resultData.hotel.itineraryPage.retrieveFourthItemTravellerNameList


        //pick up details
        resultData.transfer.unavailableAndCancelItems.Item4actualPickUpDetails=getPickUpAndDropOffDetails_BookedItems(3)
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemPickUpDetails=resultData.hotel.itineraryPage.retrieveFourthItemPickUpDetails

        //drop off details
        resultData.transfer.unavailableAndCancelItems.Item4actualDropOffDetails=getPickUpAndDropOffDetails_BookedItems(4)
        resultData.transfer.unavailableAndCancelItems.expectedFourthItemDropOffDetails=resultData.hotel.itineraryPage.retrieveFourthItemDropOffDetails




        }

    protected def "Appliedcharge"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Applied Cancellation Charges Sub-Total
        resultData.hotel.itineraryPage.actualAppliedChargeTxtDispStatus=getTotalLabelTextDispStatus(data.expected.appliedChargeTxt)

    }

    protected def "AppliedchargeForFullCharge"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //Applied Cancellation Charges Sub-Total
        resultData.hotel.itineraryPage.actualAppliedChargeTxtDispStatus=getTotalLabelTextDispStatus(data.expected.appliedChargeTxt)

        //it should be same amount of applied charge amount stated on cancel item lightbox in the earlier system flow
        String temptxt=resultData.hotel.removeItemPage.actualCancellationChargeText
        temptxt=temptxt.replaceAll("A cancellation charge of", "")
        println("first cut:$temptxt")
        temptxt=temptxt.replaceAll("applies.","")
        println("final cut:$temptxt")
        resultData.hotel.itineraryPage.expectedTotalAmountAndCurrencyInCancelLightbox=temptxt.trim()
        //Total amount should be item 1 + 2 + 3
        resultData.hotel.itineraryPage.expectedtotalAmountAndCurrency=resultData.hotel.removeItemPage.calculatedCancellationChargePrice
        resultData.hotel.itineraryPage.actualtotalAmountAndCurrency=getItenaryPriceInSuggestedItem(0)

    }

    protected def "addadditionalitem"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage

        //click on <Add a Hotel> function button within itinerary
        //clickOnAddAHotelButton()

        selectOptionFromManageItinerary(data.input.manageItineraryValue)
        sleep(5000)

        at HotelSearchResultsPage
        //user taken to home page
        resultData.hotel.search.actFindBtnStatus=getFindButtonDisabled()
    }

    protected def "NonBookedBlock"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage
        sleep(7000)
        //Non-Booked Block - Should not show
        resultData.hotel.itineraryPage.actualNonBookedTextDispStatus=getTitleDisplayStatus(data.expected.itinerarypageNonBkdTxt,3)

        //transfer item 1 should be removed from itinerary
        resultData.hotel.itineraryPage.actualTransferItem1DispStatus=getHotelNameDisplayStatus(1)
        //hotel item 2 should be removed from itienrary
        resultData.hotel.itineraryPage.actualHotelItem2DispStatus=getHotelNameDisplayStatus(2)
        //activity item 3 should be removed from itinerary
        resultData.hotel.itineraryPage.actualActivityItem3DispStatus=getHotelNameDisplayStatus(3)
        //Unavailable Block - should not show
        resultData.hotel.itineraryPage.actualUnavailableTitleTextDispStatus=getTitleDisplayStatus(data.expected.unavailableBlockTxt,2)

        //hotel item - 4 - should be removed from itinerary
        resultData.hotel.itineraryPage.actualUnavailableHotelItemDispStatus=getHotelNameDisplayStatus(0)
        //Applied Cancellation Charges Sub-Total
        resultData.hotel.itineraryPage.actualAppliedChargeTextDispStatus=getTotalLabelTextDispStatus(data.expected.appliedChargeTxt)

    }

    protected def "returntohome"(HotelSearchData data, HotelTransferTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //click on header home link
        clickOnHome()
        sleep(5000)
        waitTillLoadingIconDisappears()
        at HotelSearchResultsPage
        //user taken to home page
        resultData.hotel.search.actFindBtnStatus=getFindButtonDisabled()

        //Itinerary builder should not opens up
        resultData.hotel.search.actualItineraryBuilderDispStatus=getItineraryBuilderSectionDisplayStatus()

    }

    protected def "VerifyloginToApplicaiton"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.hotel.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }
    protected def "VerifyCloseItinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.hotel.search.actFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }

    protected def "VerifyaddItemsToItinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Transfer Search Results Returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResultsReturned,data.expected.dispStatus,"Transfer Search Results Returned Actual & Expected don't match")

        //select 1st available item to add to itinerary
        assertionEquals(resultData.hotel.searchResults.actualTransferItineraryName,resultData.hotel.searchResults.firstTransfername,"Transfer Name Added To Itinerary Actual & Expected don't match")

        //Hotel Search results returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResults,data.expected.dispStatus,"Hotel Search Results Returned Actual & Expected don't match")

        //User able to add 2nd available room to itinerary
        assertionEquals(resultData.hotel.searchResults.actualHotelNameAdded,resultData.hotel.searchResults.secondAvailableroom,"Hotel Name Added To Itinerary Actual & Expected don't match")

        //Activity Search Results returned

        //User able to add 1st available item to itinerary
        assertionEquals(resultData.hotel.activity.actualActivityName,resultData.hotel.activity.activityName,"Activity Name Added To Itinerary Actual & Expected don't match")

        //Search Results returned
        assertionEquals(resultData.hotel.searchResults.actSearchResultsReturned,data.expected.dispStatus,"Transfer Search Results (Item #4) Returned Actual & Expected don't match")

        //select 1st available item to add to itinerary
        assertionEquals(resultData.hotel.searchResults.actTransferItineraryName,resultData.hotel.searchResults.firstTransfer,"Transfer Name Added To Itinerary (Item #4) Actual & Expected don't match")

    }

    protected def "VerifyaddItemsToItinry"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Transfer Search Results Returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResultsReturned,data.expected.dispStatus,"Transfer Search Results Returned Actual & Expected don't match")

        //select 1st available item to add to itinerary
        assertionEquals(resultData.hotel.searchResults.actualTransferItineraryName,resultData.hotel.searchResults.firstTransfername,"Transfer Name Added To Itinerary Actual & Expected don't match")

        //Hotel Search results returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResults,data.expected.dispStatus,"Hotel Search Results Returned Actual & Expected don't match")

        //User able to add 1st available room to itinerary
        assertionEquals(resultData.hotel.searchResults.actualHotelNameAdded,resultData.hotel.searchResults.firstAvailableroom,"Hotel Name Added To Itinerary Actual & Expected don't match")

        //Activity Search Results returned
        assertionEquals(resultData.hotel.activity.actualSearchResults,data.expected.dispStatus,"Activity Search Results Returned Actual & Expected don't match")

        //User able to add 1st available item to itinerary
        assertionEquals(resultData.hotel.activity.actualActivityName,data.input.activitySelect,"Activity Name Added To Itinerary Actual & Expected don't match")


    }

    protected def "VerifyaddItemsToItinery"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Transfer Search Results Returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResultsReturned,data.expected.dispStatus,"Transfer Search Results Returned for first item Actual & Expected don't match")

        //select 1st available item to add to itinerary
        assertionEquals(resultData.hotel.searchResults.actualTransferItineraryName,resultData.hotel.searchResults.firstTransfername,"Transfer Name Added To Itinerary for first item Actual & Expected don't match")

        //Transfer Search Results Returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResultsReturnedSecondItem,data.expected.dispStatus,"Transfer Search Results Returned for second item Actual & Expected don't match")

        //select 1st available item to add to itinerary
        assertionEquals(resultData.hotel.searchResults.actualTransferItineraryNameSecondItem,resultData.hotel.searchResults.firstTransfernameSecondItem,"Transfer  Name Added To Itinerary for second item Actual & Expected don't match")

        //search results returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResultsThirdItem,data.expected.dispStatus,"Hotel Search Results Returned for third item Actual & Expected don't match")

        //User able to add 1st available room to itinerary
        assertionEquals( resultData.hotel.searchResults.actualHotelNameAddedThirdItem,resultData.hotel.searchResults.firstAvailableroomThirdItem,"Hotel  Name Added To Itinerary for third item Actual & Expected don't match")

        //Activity Search Results returned
        assertionEquals(resultData.hotel.activity.actualSearchResults,data.expected.dispStatus,"Activity Search Results Returned for fourth item Actual & Expected don't match")

        //User able to add 1st available item to itinerary
        assertionEquals(resultData.hotel.activity.actualActivityName,data.input.activitySelect,"Activity Name Added To Itinerary for fourth item Actual & Expected don't match")

        //Transfer Search Results Returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResultsReturnedFifthItem,data.expected.dispStatus,"Transfer Search Results Returned for fifth item Actual & Expected don't match")

        //select 1st available item to add to itinerary
        //assertionEquals(resultData.hotel.searchResults.actualTransferItineraryNamefifthItem,resultData.hotel.searchResults.firstTransfernameFifthItem,"Transfer  Name Added To Itinerary for fifth item Actual & Expected don't match")

        //select 1st available item to add to itinerary
        assertContains(resultData.hotel.searchResults.firstTransfernameFifthItem,resultData.hotel.searchResults.actualTransferItineraryNamefifthItem,"Transfer  Name Added To Itinerary for fifth item Actual & Expected don't match")

    }

    protected def "VerifyItem4Hotel"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Hotel Search results returned
        assertionEquals(resultData.hotel.searchResults.actualItem4SearchResults,data.expected.dispStatus,"Hotel Search Results Returned Actual & Expected don't match")

        //User able to add 1st available room to itinerary
        assertionEquals(resultData.hotel.searchResults.actualHotelNameAddedItem4,resultData.hotel.searchResults.firstAvailableroomItem4,"Hotel Name Added To Itinerary Actual & Expected don't match")



    }

    protected def "VerifyaddItemsToItineraryMultiRoom"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Transfer Search Results Returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResultsReturned,data.expected.dispStatus,"Transfer Search Results Returned Actual & Expected don't match")

        //select 1st available item to add to itinerary
        //assertionEquals(resultData.hotel.searchResults.actualTransferItineraryName,resultData.hotel.searchResults.firstTransfername,"Transfer Name (Item 1) Added To Itinerary Actual & Expected don't match")
        assertContains(resultData.hotel.searchResults.firstTransfername,resultData.hotel.searchResults.actualTransferItineraryName,"Transfer Name (Item 1) Added To Itinerary Actual & Expected don't match")

        //transfer itineary name
        //assertionEquals(resultData.hotel.searchResults.actualAddedTransferItineraryName,resultData.hotel.searchResults.firstTransfername,"Transfer Name (Item 2) Added To Itinerary Actual & Expected don't match")
        assertContains(resultData.hotel.searchResults.firstTransfername,resultData.hotel.searchResults.actualAddedTransferItineraryName,"Transfer Name (Item 2) Added To Itinerary Actual & Expected don't match")

        //Hotel Search results returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResults,data.expected.dispStatus,"Hotel Search Results (Item 3) Returned Actual & Expected don't match")

        //User able to add recommended room to itinerary
        assertionEquals(resultData.hotel.searchResults.actualHotelNameAdded,resultData.hotel.searchResults.recommendedroom,"Hotel Name (Item 3) Added To Itinerary Actual & Expected don't match")

        //Activity Search Results returned
        assertionEquals(resultData.hotel.activity.actualSearchResults,data.expected.dispStatus,"Activity Search Results (Item 4) Returned Actual & Expected don't match")

        //User able to add 1st available item to itinerary
        assertionEquals(resultData.hotel.activity.actualActivityName,resultData.hotel.activity.activityName,"Activity Name (Item 4)Added To Itinerary Actual & Expected don't match")

        //search results returned
        assertionEquals(resultData.hotel.activity.actualSearchResults_Second,data.expected.dispStatus,"Activity Search Results (Item 5) Returned Actual & Expected don't match")

        //User able to add 1st available item to itinerary
        assertionEquals(resultData.hotel.activity.actualActivityName_Second,resultData.hotel.activity.activityName_Second,"Activity Name (Item 5)Added To Itinerary Actual & Expected don't match")

        //Search Results returned
        assertionEquals(resultData.hotel.searchResults.searchResults,data.expected.dispStatus,"Transfer Search Results (Item #6) Returned Actual & Expected don't match")

        //select 1st available item to add to itinerary
        assertionEquals(resultData.hotel.searchResults.actualTransferName,resultData.hotel.searchResults.Transfername,"Transfer Name Added To Itinerary (Item #6) Actual & Expected don't match")

        //transfer itineary name
        assertionEquals(resultData.hotel.searchResults.actualAddTransferItineraryName,resultData.hotel.searchResults.Transfername,"Transfer Name Added To Itinerary (Item #7) Actual & Expected don't match")



    }

    protected def "VerifyGoToItinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate Itinerary Page Traveller Label Txt
        //assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt,data.expected.travellerLabelTxt.toString(), "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt,resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")

    }

    protected def "VerifyBookitem4"(HotelSearchData data, HotelTransferTestResultData resultData){


        //hotel item status updated to Unavailable
        assertionEquals(resultData.hotel.itineraryPage.actualHotelNameInUnavailableSection, data.input.item4hotelText, "Itinerary Page, Unavailable Hotel Name Text added Actual & Expected don't match")


    }

    protected def "VerifyitineraryNameUpdate"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate Itinerary Page Traveller Label Txt
        assertionEquals(resultData.hotel.itineraryPage.actualSavedItnrName,resultData.hotel.itineraryPage.expectedItnrName, "Itinerary Page, Itinerary Name Update Actual & Expected don't match")

    }

    protected def "VerifybookItem"(HotelSearchData data, HotelTransferTestResultData resultData){

    //Booking Confirmation First Item Screen Display Status
    assertionEquals( resultData.hotel.itineraryPage.actualFirstItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation First Item Actual & Expected don't match")

     //Booking Confirmation Second Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualSecondItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Second Item Actual & Expected don't match")

        //Booking Confirmation Third Item Screen Display Status
        assertionEquals( resultData.hotel.itineraryPage.actualThirdItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Third Item Actual & Expected don't match")
        /*
        //Booking Confirmation Fourth Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualFourthItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Third Item Actual & Expected don't match")

        */

    }

    protected def "VerifybookItems"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking Confirmation First Item Screen Display Status
        assertionEquals( resultData.hotel.itineraryPage.actualFirstItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation First Item Actual & Expected don't match")

        //Booking Confirmation Second Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualSecondItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Second Item Actual & Expected don't match")

        //Booking Confirmation Third Item Screen Display Status
        assertionEquals( resultData.hotel.itineraryPage.actualThirdItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Third Item Actual & Expected don't match")

        //Booking Confirmation Fourth Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualFourthItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Fourth Item Actual & Expected don't match")

        //Booking Confirmation Fifth Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualFifthItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Fifth Item Actual & Expected don't match")


    }

    protected def "VerifyItemscouldnotbeamendedbox"(HotelSearchData data, HotelTransferTestResultData resultData){

        //lightbox page title text should show
        assertionEquals(resultData.hotel.itineraryPage.ItemCouldNotBeAmendTxt,data.expected.itemsCouldNotAmendTxt, "lightbox page title text Actual & Expected don't match")

        //lightbox - Close X function
        assertionEquals(resultData.hotel.itineraryPage.lightBoxCloseXFunction,data.expected.dispStatus, "lightbox - Close X function display status Actual & Expected don't match")

        //You have edited traveller names. Sorry, This change cannot be reflected on the following bookings.
        assertionEquals(resultData.hotel.itineraryPage.editTravellersDescTxt,data.expected.editTravellersDescTxt, "lightbox page Descriptive header text Actual & Expected don't match")
    }

    protected def "VerifyItemscouldnotbeamended"(HotelSearchData data, HotelTransferTestResultData resultData){

        //lightbox page title text should show
        assertionEquals(resultData.hotel.itineraryPage.ItemCouldNotBeAmendTxt,data.expected.itemsCouldNotAmendTxt, "lightbox page title text Actual & Expected don't match")

        //Section 1 - cannot be changed
        assertionEquals(resultData.hotel.itineraryPage.ItemSection1Text,data.expected.editTravellersDescTxt, "Section 1 text Actual & Expected don't match")


        //Section 2 - to be applied to booking individually
        assertionEquals(resultData.hotel.itineraryPage.ItemSection2Text,data.expected.editChildAgeTxt, "Section 2 text Actual & Expected don't match")

        //lightbox - Close X function
        assertionEquals(resultData.hotel.itineraryPage.lightBoxCloseXFunction,data.expected.dispStatus, "lightbox - Close X function display status Actual & Expected don't match")


    }

    protected def "VerifySection1cannotbechanged"(HotelSearchData data, HotelTransferTestResultData resultData){

         //You have edited traveller names. Sorry, This change cannot be reflected on the following bookings.
        assertionEquals(resultData.hotel.itineraryPage.editTravellersDescTxt,data.expected.editTravellersDescTxt, "lightbox page Descriptive header text Actual & Expected don't match")
    }

    protected def "VerifySection2tobeappliedtobookingindividually"(HotelSearchData data, HotelTransferTestResultData resultData){

        //You have edited child age(s). Sorry these need to be applied to the following bookings(s) individually.  Click on Amend for each booking to select the new child age.
        assertionEquals(resultData.hotel.itineraryPage.section2editTravellersDescTxt,data.expected.editChildAgeTxt, "lightbox page Descriptive header text Actual & Expected don't match")
    }
    protected def "VerifyItem1"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #1 - transfer item
        assertionEquals(resultData.hotel.itineraryPage.Item1DispStatus,data.expected.dispStatus, "Item 1 inclusion in the lightbox Actual & Expected don't match")

        //item #1 - image
        assertionEquals(resultData.hotel.itineraryPage.Item1Image,resultData.hotel.itineraryPage.retrievedFirstItemImageSrcURL, "Item 1 Image Actual & Expected don't match")

        //item type of car
        assertionEquals(resultData.hotel.itineraryPage.Item1Txt,resultData.hotel.itineraryPage.retrievedFirstItemTypeOfCar, "Item1 type of car Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.itineraryPage.Item1Pax,resultData.hotel.itineraryPage.retrievedFirstItemPax, "Item1 Pax Actual & Expected don't match")

        //item title
        assertionEquals(resultData.hotel.itineraryPage.Item1Title,resultData.hotel.itineraryPage.retrievedFirstItemTitle, "Item1 Title Actual & Expected don't match")

        //Cancellation fees apply
        assertionEquals(resultData.hotel.itineraryPage.Item1CancellationTextDispStatus,data.expected.notDispStatus, "Item1 Cancellation Fees apply Actual & Expected don't match")

        //duration icon
        assertionEquals(resultData.hotel.itineraryPage.Item1DurationIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1DurationIconDisplayStatus, "Item1 Duration Icon Display status Actual & Expected don't match")
        // duration value
        assertionEquals(resultData.hotel.itineraryPage.Item1DurationIconText,resultData.hotel.itineraryPage.retrievedItem1DurationIconText, "Item1 Duration icon text Actual & Expected don't match")

        //luggage icon
        assertionEquals(resultData.hotel.itineraryPage.Item1LuggageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1LuggageIconDisplayStatus, "Item1 Luggage Icon Display status Actual & Expected don't match")
        // luggage value
        assertionEquals(resultData.hotel.itineraryPage.Item1LuggageIconText,resultData.hotel.itineraryPage.retrievedItem1LuggageIconText, "Item1 Luggage Icon Text Actual & Expected don't match")

        //pax icon
        assertionEquals(resultData.hotel.itineraryPage.Item1PaxIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1PaxIconDisplayStatus, "Item1 Pax Icon Display status Actual & Expected don't match")
        // pax value
        assertionEquals(resultData.hotel.itineraryPage.Item1paxIconText,resultData.hotel.itineraryPage.retrievedItem1paxIconText, "Item1 Pax Icon Text Actual & Expected don't match")

        //supporting language icon
        assertionEquals(resultData.hotel.itineraryPage.Item1LanguageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1LanguageIconDisplayStatus, "Item1 Supporting Language Icon Display status Actual & Expected don't match")
        // language value
        assertionEquals(resultData.hotel.itineraryPage.Item1LanguageIconText,resultData.hotel.itineraryPage.retrievedItem1LanguageIconText, "Item1 Supporting Language Text Actual & Expected don't match")

        //service date
        //service time
        assertionEquals(resultData.hotel.itineraryPage.Item1ServiceDateTime,resultData.hotel.itineraryPage.retrievedItem1ServiceDatetime, "Item1 Service Date & Time Text Actual & Expected don't match")

        //item status - Available or On request
        //item status, commision and % value  will not displayed if booking is successful. - 10.3
        //assertionEquals(resultData.hotel.itineraryPage.Item1StatusText,data.expected.statusTxt, "Item1 Status Text Actual & Expected don't match")

        //commission and % value
        assertionEquals(resultData.hotel.itineraryPage.Item1CommissionPercentAndValue,resultData.hotel.itineraryPage.retrievedItem1CommissionPercentValue, "Item1 Commission % Value Text Actual & Expected don't match")

        //total service item amount and currency
        assertionEquals(resultData.hotel.itineraryPage.Item1TotalAmountAndCurncy,resultData.hotel.itineraryPage.retrievedItem1AmountAndCurrency, "Item1 Amount And Currency Text Actual & Expected don't match")



    }

    protected def "VerifyItem1Modified"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #1 - transfer item
        assertionEquals(resultData.hotel.itineraryPage.Item1DispStatus,data.expected.dispStatus, "Item 1 inclusion in the lightbox Actual & Expected don't match")

        //item #1 - image
        assertionEquals(resultData.hotel.itineraryPage.Item1Image,resultData.hotel.itineraryPage.retrievedFirstItemImageSrcURL, "Item 1 Image Actual & Expected don't match")

        //item type of car
        assertionEquals(resultData.hotel.itineraryPage.Item1Txt,resultData.hotel.itineraryPage.retrievedFirstItemTypeOfCar, "Item1 type of car Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.itineraryPage.Item1Pax,resultData.hotel.itineraryPage.retrievedFirstItemPax, "Item1 Pax Actual & Expected don't match")

        //item title
        assertionEquals(resultData.hotel.itineraryPage.Item1Title,resultData.hotel.itineraryPage.retrievedFirstItemTitle, "Item1 Title Actual & Expected don't match")

        //Cancellation fees apply
        assertionEquals(resultData.hotel.itineraryPage.Item1CancellationTextDispStatus,data.expected.notDispStatus, "Item1 Cancellation Fees apply Actual & Expected don't match")

        //duration icon
        assertionEquals(resultData.hotel.itineraryPage.Item1DurationIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1DurationIconDisplayStatus, "Item1 Duration Icon Display status Actual & Expected don't match")
        // duration value
        assertionEquals(resultData.hotel.itineraryPage.Item1DurationIconText,resultData.hotel.itineraryPage.retrievedItem1DurationIconText, "Item1 Duration icon text Actual & Expected don't match")

        //luggage icon
        assertionEquals(resultData.hotel.itineraryPage.Item1LuggageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1LuggageIconDisplayStatus, "Item1 Luggage Icon Display status Actual & Expected don't match")
        // luggage value
        assertionEquals(resultData.hotel.itineraryPage.Item1LuggageIconText,resultData.hotel.itineraryPage.retrievedItem1LuggageIconText, "Item1 Luggage Icon Text Actual & Expected don't match")

        //pax icon
        assertionEquals(resultData.hotel.itineraryPage.Item1PaxIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1PaxIconDisplayStatus, "Item1 Pax Icon Display status Actual & Expected don't match")
        // pax value
        assertionEquals(resultData.hotel.itineraryPage.Item1paxIconText,resultData.hotel.itineraryPage.retrievedItem1paxIconText, "Item1 Pax Icon Text Actual & Expected don't match")

        //supporting language icon
        assertionEquals(resultData.hotel.itineraryPage.Item1LanguageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem1LanguageIconDisplayStatus, "Item1 Supporting Language Icon Display status Actual & Expected don't match")
        // language value
        assertionEquals(resultData.hotel.itineraryPage.Item1LanguageIconText,resultData.hotel.itineraryPage.retrievedItem1LanguageIconText, "Item1 Supporting Language Text Actual & Expected don't match")

        //service date
        //service time
        assertionEquals(resultData.hotel.itineraryPage.Item1ServiceDateTime,resultData.hotel.itineraryPage.retrievedItem1ServiceDatetime, "Item1 Service Date & Time Text Actual & Expected don't match")

        //item status - Available or On request
        //item status, commision and % value  will not displayed if booking is successful. - 10.3
        //assertionEquals(resultData.hotel.itineraryPage.Item1StatusText,data.expected.statusTxt, "Item1 Status Text Actual & Expected don't match")

        //commission and % value
        //assertionEquals(resultData.hotel.itineraryPage.Item1CommissionPercentAndValue,resultData.hotel.itineraryPage.retrievedItem1CommissionPercentValue, "Item1 Commission % Value Text Actual & Expected don't match")

        //total service item amount and currency
        //assertionEquals(resultData.hotel.itineraryPage.Item1TotalAmountAndCurncy,resultData.hotel.itineraryPage.retrievedItem1AmountAndCurrency, "Item1 Amount And Currency Text Actual & Expected don't match")



    }

    protected def "VerifyItem2"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #2 - transfer item
        assertionEquals(resultData.hotel.itineraryPage.Item2DispStatus,data.expected.notDispStatus, "Item 2 inclusion in the lightbox Actual & Expected don't match")
    }

    protected def "VerifyItem3room2"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #3 - hotel item - room 2
        assertionEquals(resultData.hotel.itineraryPage.Item3HotelRoom2DispStatusSection2,data.expected.notDispStatus, "Item 3 Hotel Room 2 inclusion in the lightbox Actual & Expected don't match")
    }

    protected def "VerifyItem3"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #3 - hotel item
        assertionEquals(resultData.hotel.itineraryPage.Item3DispStatus,data.expected.notDispStatus, "Item 3 inclusion in the lightbox Actual & Expected don't match")
    }

    protected def "VerifyItem3room1"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #3 - Hotel item - Room 1
        assertionEquals(resultData.hotel.itineraryPage.Item3HotelRoom1DispStatusSection2,data.expected.dispStatus, "Item 3 Hotel Room 1 inclusion in the lightbox Actual & Expected don't match")

        //item #3 - image
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1Image,resultData.hotel.itineraryPage.retrievedItem3Room1ImageSrcURL, "Item 3 Room 1 Image Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1ItemName,resultData.hotel.itineraryPage.retrievedItem3Room1Name, "Item3 Room 1 Hotel Name Text Actual & Expected don't match")

        //item - hotel star rating
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1HotelStarRating,resultData.hotel.itineraryPage.retrievedItem3Room1HotelStarRating, "Item3 Room 1 Hotel Star Rating Text Actual & Expected don't match")

        //booked room type text
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1BookedRoomTypeTxt,resultData.hotel.itineraryPage.retrievedItem3Room1BookedRoomTypeTxt, "Item3 Room 1 Booked Room Type Text Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1Pax,resultData.hotel.itineraryPage.retrievedItem3Room1Pax, "Item3 Room 1 Pax Text Actual & Expected don't match")

        //meal basis
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1MealBasisTxt,resultData.hotel.itineraryPage.retrievedItem3Room1MealBasisTxt, "Item3 Room 1 Meal Basis Actual & Expected don't match")

        //Cancellation fees apply
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1CancellationTextDispStatus,data.expected.notDispStatus, "Item3 Room 1 Cancellation Fees apply Actual & Expected don't match")

        //check in date & stay nights
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1CheckInDateAndStayNights,resultData.hotel.itineraryPage.retrievedItem3Room1CheckInDateAndStayNightsTxt, "Item3 Room 1 Check in Date and stay nights Actual & Expected don't match")

        //item status - Available or On request - will not be displayed if booking is confirmed - 10.3
        //commission and % value
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1CommissionPercentAndValue,resultData.hotel.itineraryPage.retrievedItem3Room1CommissionPercentValue, "Item3 Room 1 Commission Percentage and Text Actual & Expected don't match")

        //total service item amount and currency
        assertionEquals(resultData.hotel.itineraryPage.Item3Room1TotalAmountAndCurncy,resultData.hotel.itineraryPage.retrievedItem3Room1AmountAndCurrency, "Item3 Room 1 total Amount Actual & Expected don't match")

    }

    protected def "VerifyItems3"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #3 - hotel item - Room 1
        assertionEquals(resultData.hotel.itineraryPage.Item3HotelRoom1DispStatus,data.expected.notDispStatus, "Item 3 Hotel Room 1 inclusion in the lightbox Actual & Expected don't match")

        //item #3 - hotel item - Room 2
        assertionEquals(resultData.hotel.itineraryPage.Item3HotelRoom2DispStatus,data.expected.notDispStatus, "Item 3 Hotel Room 2 inclusion in the lightbox Actual & Expected don't match")

    }

    protected def "VerifyItem4"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #4 - activity item
        assertionEquals(resultData.hotel.itineraryPage.Item4DispStatus,data.expected.dispStatus, "Item 4 inclusion in the lightbox Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.itineraryPage.Item4Image,resultData.hotel.itineraryPage.retrievedFourthItemImageSrcURL, "Item 4 Image Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.Item4Txt,resultData.hotel.itineraryPage.retrievedFourthItemName, "Item 4 Item Name Actual & Expected don't match")

        //cancellation charge apply
        assertionEquals(resultData.hotel.itineraryPage.Item4CancellationTextDispStatus,data.expected.notDispStatus, "Item4 Cancellation Fees apply Actual & Expected don't match")

        //duration icon
        assertionEquals(resultData.hotel.itineraryPage.Item4DurationIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem4DurationIconDisplayStatus, "Item4 Duration Icon Display status Actual & Expected don't match")
        // duration value
        assertionEquals(resultData.hotel.itineraryPage.Item4DurationIconText,resultData.hotel.itineraryPage.retrievedItem4DurationIconText, "Item4 Duration icon text Actual & Expected don't match")

        //supporting language icon
        assertionEquals(resultData.hotel.itineraryPage.Item4LanguageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem4LanguageIconDisplayStatus, "Item4 Supporting Language Icon Display status Actual & Expected don't match")
        // language value
        assertionEquals(resultData.hotel.itineraryPage.Item4LanguageIconText,resultData.hotel.itineraryPage.retrievedItem4LanguageIconText, "Item4 Supporting Language Text Actual & Expected don't match")

        //service date
        assertionEquals(resultData.hotel.itineraryPage.Item4ServiceDate,resultData.hotel.itineraryPage.retrievedItem4ServiceDate, "Item4 Service Date Actual & Expected don't match")
        //number of pax
        assertionEquals(resultData.hotel.itineraryPage.Item4Pax,resultData.hotel.itineraryPage.retrievedItem4Pax, "Item4 Pax Actual & Expected don't match")
        //item status - Available or On request
        //item status, will not displayed if booking is successful - 10.3
        //assertionEquals(resultData.hotel.itineraryPage.Item4StatusText,data.expected.statusTxt, "Item4 Status Text Actual & Expected don't match")
        //commission and % value
        assertionEquals(resultData.hotel.itineraryPage.Item4CommissionPercentAndValue,resultData.hotel.itineraryPage.retrievedItem4CommissionPercentValue, "Item4 Commission % Value Text Actual & Expected don't match")

        //total service item amount and currency
        assertionEquals(resultData.hotel.itineraryPage.Item4TotalAmountAndCurncy,resultData.hotel.itineraryPage.retrievedItem4AmountAndCurrency, "Item4 Amount And Currency Text Actual & Expected don't match")


    }

    protected def "VerifyItem4Modified"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #4 - activity item
        assertionEquals(resultData.hotel.itineraryPage.Item4DispStatus,data.expected.dispStatus, "Item 4 inclusion in the lightbox Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.itineraryPage.Item4Image,resultData.hotel.itineraryPage.retrievedFourthItemImageSrcURL, "Item 4 Image Actual & Expected don't match")

        //item name
        assertionEquals(resultData.hotel.itineraryPage.Item4Txt,resultData.hotel.itineraryPage.retrievedFourthItemName, "Item 4 Item Name Actual & Expected don't match")

        //cancellation charge apply
        assertionEquals(resultData.hotel.itineraryPage.Item4CancellationTextDispStatus,data.expected.notDispStatus, "Item4 Cancellation Fees apply Actual & Expected don't match")

        //duration icon
        assertionEquals(resultData.hotel.itineraryPage.Item4DurationIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem4DurationIconDisplayStatus, "Item4 Duration Icon Display status Actual & Expected don't match")
        // duration value
        assertionEquals(resultData.hotel.itineraryPage.Item4DurationIconText,resultData.hotel.itineraryPage.retrievedItem4DurationIconText, "Item4 Duration icon text Actual & Expected don't match")

        //supporting language icon
        assertionEquals(resultData.hotel.itineraryPage.Item4LanguageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem4LanguageIconDisplayStatus, "Item4 Supporting Language Icon Display status Actual & Expected don't match")
        // language value
        assertionEquals(resultData.hotel.itineraryPage.Item4LanguageIconText,resultData.hotel.itineraryPage.retrievedItem4LanguageIconText, "Item4 Supporting Language Text Actual & Expected don't match")

        //service date
        assertionEquals(resultData.hotel.itineraryPage.Item4ServiceDate,resultData.hotel.itineraryPage.retrievedItem4ServiceDate, "Item4 Service Date Actual & Expected don't match")
        //number of pax
        assertionEquals(resultData.hotel.itineraryPage.Item4Pax,resultData.hotel.itineraryPage.retrievedItem4Pax, "Item4 Pax Actual & Expected don't match")
        //item status - Available or On request
        //item status, will not displayed if booking is successful - 10.3
        //assertionEquals(resultData.hotel.itineraryPage.Item4StatusText,data.expected.statusTxt, "Item4 Status Text Actual & Expected don't match")
        //commission and % value
        //assertionEquals(resultData.hotel.itineraryPage.Item4CommissionPercentAndValue,resultData.hotel.itineraryPage.retrievedItem4CommissionPercentValue, "Item4 Commission % Value Text Actual & Expected don't match")

        //total service item amount and currency
        //assertionEquals(resultData.hotel.itineraryPage.Item4TotalAmountAndCurncy,resultData.hotel.itineraryPage.retrievedItem4AmountAndCurrency, "Item4 Amount And Currency Text Actual & Expected don't match")


    }


    protected def "VerifyItem5"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #5 - transfer item
        assertionEquals(resultData.hotel.itineraryPage.Item5DispStatus,data.expected.dispStatus, "Item 5 inclusion in the lightbox Actual & Expected don't match")

        //item #5 - image
        assertionEquals(resultData.hotel.itineraryPage.Item5Image,resultData.hotel.itineraryPage.retrievedFifthItemImageSrcURL, "Item 5 Image Actual & Expected don't match")

        //item type of car
        assertionEquals(resultData.hotel.itineraryPage.Item5Txt,resultData.hotel.itineraryPage.retrievedFifthItemTypeOfCar, "Item5 type of car Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.itineraryPage.Item5Pax,resultData.hotel.itineraryPage.retrievedFifthItemPax, "Item5 Pax Actual & Expected don't match")

        //item title
        assertionEquals(resultData.hotel.itineraryPage.Item5Title,resultData.hotel.itineraryPage.retrievedFifthItemTitle, "Item5 Title Actual & Expected don't match")

        //Cancellation fees apply
        assertionEquals(resultData.hotel.itineraryPage.Item5CancellationTextDispStatus,data.expected.notDispStatus, "Item5 Cancellation Fees apply Actual & Expected don't match")

        //duration icon
        assertionEquals(resultData.hotel.itineraryPage.Item5DurationIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5DurationIconDisplayStatus, "Item5 Duration Icon Display status Actual & Expected don't match")
        // duration value
        assertionEquals(resultData.hotel.itineraryPage.Item5DurationIconText,resultData.hotel.itineraryPage.retrievedItem5DurationIconText, "Item5 Duration icon text Actual & Expected don't match")

        //luggage icon
        assertionEquals(resultData.hotel.itineraryPage.Item5LuggageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5LuggageIconDisplayStatus, "Item5 Luggage Icon Display status Actual & Expected don't match")
        // luggage value
        assertionEquals(resultData.hotel.itineraryPage.Item5LuggageIconText,resultData.hotel.itineraryPage.retrievedItem5LuggageIconText, "Item5 Luggage Icon Text Actual & Expected don't match")

        //pax icon
        assertionEquals(resultData.hotel.itineraryPage.Item5PaxIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5PaxIconDisplayStatus, "Item5 Pax Icon Display status Actual & Expected don't match")
        // pax value
        assertionEquals(resultData.hotel.itineraryPage.Item5paxIconText,resultData.hotel.itineraryPage.retrievedItem5paxIconText, "Item5 Pax Icon Text Actual & Expected don't match")

        //supporting language icon
        assertionEquals(resultData.hotel.itineraryPage.Item5LanguageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5LanguageIconDisplayStatus, "Item5 Supporting Language Icon Display status Actual & Expected don't match")
        // language value
        assertionEquals(resultData.hotel.itineraryPage.Item5LanguageIconText,resultData.hotel.itineraryPage.retrievedItem5LanguageIconText, "Item5 Supporting Language Text Actual & Expected don't match")

        //service date
        //service time
        assertionEquals(resultData.hotel.itineraryPage.Item5ServiceDateTime,resultData.hotel.itineraryPage.retrievedItem5ServiceDatetime, "Item5 Service Date & Time Text Actual & Expected don't match")

        //item status - Available or On request
        //item status will not be displayed if booking is successful - sit 10.3
        //assertionEquals(resultData.hotel.itineraryPage.Item5StatusText,data.expected.statusTxt, "Item5 Status Text Actual & Expected don't match")

        //commission and % value
        assertionEquals(resultData.hotel.itineraryPage.Item5CommissionPercentAndValue,resultData.hotel.itineraryPage.retrievedItem5CommissionPercentValue, "Item5 Commission % Value Text Actual & Expected don't match")

        //total service item amount and currency
        assertionEquals(resultData.hotel.itineraryPage.Item5TotalAmountAndCurncy,resultData.hotel.itineraryPage.retrievedItem5AmountAndCurrency, "Item5 Amount And Currency Text Actual & Expected don't match")

    }

    protected def "VerifyItem5Modified"(HotelSearchData data, HotelTransferTestResultData resultData){

        //item #5 - transfer item
        assertionEquals(resultData.hotel.itineraryPage.Item5DispStatus,data.expected.dispStatus, "Item 5 inclusion in the lightbox Actual & Expected don't match")

        //item #5 - image
        assertionEquals(resultData.hotel.itineraryPage.Item5Image,resultData.hotel.itineraryPage.retrievedFifthItemImageSrcURL, "Item 5 Image Actual & Expected don't match")

        //item type of car
        assertionEquals(resultData.hotel.itineraryPage.Item5Txt,resultData.hotel.itineraryPage.retrievedFifthItemTypeOfCar, "Item5 type of car Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.itineraryPage.Item5Pax,resultData.hotel.itineraryPage.retrievedFifthItemPax, "Item5 Pax Actual & Expected don't match")

        //item title
        assertionEquals(resultData.hotel.itineraryPage.Item5Title,resultData.hotel.itineraryPage.retrievedFifthItemTitle, "Item5 Title Actual & Expected don't match")

        //Cancellation fees apply
        assertionEquals(resultData.hotel.itineraryPage.Item5CancellationTextDispStatus,data.expected.notDispStatus, "Item5 Cancellation Fees apply Actual & Expected don't match")

        //duration icon
        assertionEquals(resultData.hotel.itineraryPage.Item5DurationIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5DurationIconDisplayStatus, "Item5 Duration Icon Display status Actual & Expected don't match")
        // duration value
        assertionEquals(resultData.hotel.itineraryPage.Item5DurationIconText,resultData.hotel.itineraryPage.retrievedItem5DurationIconText, "Item5 Duration icon text Actual & Expected don't match")

        //luggage icon
        assertionEquals(resultData.hotel.itineraryPage.Item5LuggageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5LuggageIconDisplayStatus, "Item5 Luggage Icon Display status Actual & Expected don't match")
        // luggage value
        assertionEquals(resultData.hotel.itineraryPage.Item5LuggageIconText,resultData.hotel.itineraryPage.retrievedItem5LuggageIconText, "Item5 Luggage Icon Text Actual & Expected don't match")

        //pax icon
        assertionEquals(resultData.hotel.itineraryPage.Item5PaxIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5PaxIconDisplayStatus, "Item5 Pax Icon Display status Actual & Expected don't match")
        // pax value
        assertionEquals(resultData.hotel.itineraryPage.Item5paxIconText,resultData.hotel.itineraryPage.retrievedItem5paxIconText, "Item5 Pax Icon Text Actual & Expected don't match")

        //supporting language icon
        assertionEquals(resultData.hotel.itineraryPage.Item5LanguageIconDisplayStatus,resultData.hotel.itineraryPage.retrievedItem5LanguageIconDisplayStatus, "Item5 Supporting Language Icon Display status Actual & Expected don't match")
        // language value
        assertionEquals(resultData.hotel.itineraryPage.Item5LanguageIconText,resultData.hotel.itineraryPage.retrievedItem5LanguageIconText, "Item5 Supporting Language Text Actual & Expected don't match")

        //service date
        //service time
        assertionEquals(resultData.hotel.itineraryPage.Item5ServiceDateTime,resultData.hotel.itineraryPage.retrievedItem5ServiceDatetime, "Item5 Service Date & Time Text Actual & Expected don't match")

        //item status - Available or On request
        //item status will not be displayed if booking is successful - sit 10.3
        //assertionEquals(resultData.hotel.itineraryPage.Item5StatusText,data.expected.statusTxt, "Item5 Status Text Actual & Expected don't match")

        //commission and % value
        //assertionEquals(resultData.hotel.itineraryPage.Item5CommissionPercentAndValue,resultData.hotel.itineraryPage.retrievedItem5CommissionPercentValue, "Item5 Commission % Value Text Actual & Expected don't match")

        //total service item amount and currency
        //assertionEquals(resultData.hotel.itineraryPage.Item5TotalAmountAndCurncy,resultData.hotel.itineraryPage.retrievedItem5AmountAndCurrency, "Item5 Amount And Currency Text Actual & Expected don't match")

    }


    protected def "VerifyItinerarypage"(HotelSearchData data, HotelTransferTestResultData resultData){


        //user taken back itinerary page
        assertionEquals(resultData.hotel.itineraryPage.actualHeaderBookedItemText,data.expected.itinerarypageBkdTxt, "User taken back to itinerary page Actual & Expected don't match")

        //check Traveller details section -the amended traveller 2 name is created in traveller detaile section as traveller 6
        assertionEquals(resultData.hotel.itineraryPage.actualSixthTravellerName,resultData.hotel.itineraryPage.expectedSixthTravellerName, "Traveller 6 Creation Actual & Expected don't match")

        //Item #1 - transer item - traveller name details - should not be changed
        assertionEquals(resultData.hotel.itineraryPage.actualItem1TravellerNameDetailsAfterEdit,resultData.hotel.itineraryPage.retrievedItem1TravellerNameDetails, "Item #1 - transer item - traveller name details Actual & Expected don't match")

        //item #2 - transfer item - traveller name details - remain same
        assertionEquals(resultData.hotel.itineraryPage.actualItem2TravellerNameDetailsAfterEdit,resultData.hotel.itineraryPage.retrievedItem2TravellerNameDetails, "Item #2 - transer item - traveller name details Actual & Expected don't match")

        //item #3 - hotel item - traveller name details - Room 1 - traveller 2 name should be changed as per amendment done
        assertionEquals(resultData.hotel.itineraryPage.actualItem3Room1TravellerNameDetailsAfterEdit,resultData.hotel.itineraryPage.expectedItem3Room1TravellerNameDetailsAfterEdit, "Item #3 - hotel item Room 1- traveller name details Actual & Expected don't match")

        //item #3 - hotel item - traveller name details - Room 2 - remain same
        assertionEquals(resultData.hotel.itineraryPage.actualItem3Room2TravellerNameDetailsAfterEdit, resultData.hotel.itineraryPage.retrievedItem3Room2TravellerNameDetails, "Item #3 - hotel item Room 2- traveller name details Actual & Expected don't match")

        //item #4 - activity item - traveller name details - should not be changed
        assertionEquals(resultData.hotel.itineraryPage.actualItem4TravellerNameDetailsAfterEdit,resultData.hotel.itineraryPage.retrievedItem4TravellerNameDetails, "Item #4 - activity item - traveller name details Actual & Expected don't match")

        //item #5 - transfer item - traveller name details - should not be changed
        assertionEquals(resultData.hotel.itineraryPage.actualItem5TravellerNameDetailsAfterEdit,resultData.hotel.itineraryPage.retrievedItem5TravellerNameDetails, "Item #5 - transfer item - traveller name details Actual & Expected don't match")

    }

    protected def "VerifyEdit"(HotelSearchData data, HotelTransferTestResultData resultData){

        //user is able to do so all fields open to edit mode for traveller
        assertionEquals(resultData.hotel.itineraryPage.EditModeStatusForTraveller,data.expected.dispStatus, "All Fields In Edit Mode Actual & Expected don't match")

        //user is able to do so user taken to a lightbox of items could not be amended
        assertionEquals(resultData.hotel.itineraryPage.itemsCouldnotamendpopupDispStatus,data.expected.dispStatus, "user taken to a lightbox of items could not be amended Actual & Expected don't match")


    }

    protected def "VerifybookItemFullCharge"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking Confirmation First Item Screen Display Status
        assertionEquals( resultData.hotel.itineraryPage.actualFirstItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation First Item Actual & Expected don't match")

        //Booking Confirmation Second Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualSecondItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Second Item Actual & Expected don't match")

        //Booking Confirmation Third Item Screen Display Status
        assertionEquals( resultData.hotel.itineraryPage.actualThirdItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Third Item Actual & Expected don't match")


    }

    protected def "VerifybookItemMultiRoom"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking Confirmation First Item Screen Display Status
        assertionEquals( resultData.hotel.itineraryPage.actualFirstItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation First Item Actual & Expected don't match")

        //Booking Confirmation Second Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualSecondItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Second Item Actual & Expected don't match")

        //Booking Confirmation Third Item Screen Display Status
        assertionEquals( resultData.hotel.itineraryPage.actualThirdItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Third Item Actual & Expected don't match")
        //Booking Confirmation Fourth Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualFourthItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Fourth Item Actual & Expected don't match")
        //Booking Confirmation Fifth Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualFifthItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Fifth Item Actual & Expected don't match")

        //Booking Confirmation Sixth Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualSixthItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Sixth Item Actual & Expected don't match")

        //Booking Confirmation Seventh Item Screen Display Status
        assertionEquals(resultData.hotel.itineraryPage.actualSeventhItemBookingConfrm,data.expected.dispStatus, "Booking Confirmation Seventh Item Actual & Expected don't match")


    }

    protected def "VerifySearchtheitinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate Itinerary Page Traveller Label Txt
        assertionEquals( resultData.hotel.itineraryPage.actItnrName,resultData.hotel.itineraryPage.expectedItnrName, "Itinerary Page, Itinerary Name Update Actual & Expected don't match")

    }

    protected def "Verifycancelitinerary"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Cancel itinerary lightbox display status
        assertionEquals( resultData.hotel.removeItemPage.actualCancelItemDispStatus,data.expected.dispStatus, "Itinerary Page, re-confirm deleting itinerary lightbox display status Actual & Expected don't match")



    }

    protected def "VerifycancelItem"(HotelSearchData data, HotelTransferTestResultData resultData,int Item){


        if(Item==1){
            //Cancel itinerary lightbox display status
            assertionEquals(resultData.hotel.removeItemPage.actualCancelItemDispStatus_FirstItem,data.expected.dispStatus, "Itinerary Page, First cancel item lightbox display status Actual & Expected don't match")

        }else if(Item==2){
            //Cancel itinerary lightbox display status
            assertionEquals(resultData.hotel.removeItemPage.actualCancelItemDispStatus_SecondItem,data.expected.dispStatus, "Itinerary Page, Second cancel item lightbox display status Actual & Expected don't match")


        }else if (Item==3){
            //Cancel itinerary lightbox display status
            assertionEquals(resultData.hotel.removeItemPage.actualCancelItemDispStatus_ThirdItem,data.expected.dispStatus, "Itinerary Page, Third cancel item lightbox display status Actual & Expected don't match")

        }

    }

    protected def "Verifycancelled1"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validation is done in the step 10

    }
    protected def "Verifytransferitemcard"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Booking ID & value
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualBookingIDAndValue,resultData.hotel.removeItemPage.cancelledItems.expectedBookingIDAndValue, "Itinerary Page, First Unavailable & Cancel Items Booking Id and value Actual & Expected don't match")

        //status - Cancelled
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelledStatus,data.expected.itemStatus, "Itinerary Page, Cancelled Status Test Actual & Expected don't match")

        //item image
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemImageStatus,data.expected.dispStatus, "Itinerary Page, Item Image display status Actual & Expected don't match")

        //item type of car
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemTypeOfCar,data.expected.dispStatus, "Itinerary Page, Item type of car text Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualFirstCancelItemPax,data.expected.dispStatus, "Itinerary Page, Item Pax text Actual & Expected don't match")

        //item title
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualFirstTitleText,data.expected.dispStatus, "Itinerary Page, Item title text Actual & Expected don't match")





    }

    protected def "VerifyAppliedcharges"(HotelSearchData data, HotelTransferTestResultData resultData){



    }

    protected def "VerifycancelitineraryMultiRoom"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Cancel itinerary lightbox display status
        assertionEquals( resultData.hotel.removeItemPage.actualCancelItemDispStatus,data.expected.dispStatus, "Itinerary Page, re-confirm deleting itinerary lightbox display status Actual & Expected don't match")

        //Validate  Item Status
        List actualStatusData=resultData.hotel.removeItemPage.cancelledItems.get("actualItemStatus")
        List expectedStatusData=resultData.hotel.removeItemPage.cancelledItems.get("expectedItemStatus")
        //Validate  Item Status
        assertionListEquals(actualStatusData,expectedStatusData,"Unavailable and Cancelled Items screen, item updated status actual & expected don't match")


    }

    protected def "Verifycancelitemlightbox"(HotelSearchData data, HotelTransferTestResultData resultData){

        //text - Cancel item
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualCancelItemHeaderTxt,data.expected.cancelItemHeaderTxt, "Itinerary Page, cancel item lightbox header text Cancel Item  Actual & Expected don't match")

        //close X function
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualClosebuttonDispStatus,data.expected.dispStatus, "Itinerary Page, close x function  Actual & Expected don't match")
        //Are You Sure ...
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualAreYouSureText,data.expected.headerTextAreYouSure, "Itinerary Page, close x function  Actual & Expected don't match")

        //A cancellation charge of XXX.XX GBP applies.
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualACancellationChargeText,data.expected.headerTextCancellationCharge,"Itinerary Page, Text A cancellation charge of XXX.XX GBP applies.Actual & Expected don't match")

        //Charge amount
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualCancelChargeAmount,resultData.hotel.removeItemPage.cancelledItems.actualItemAmountAndCurrency,"Itinerary Page, Charge amount actual and expected don't match")

        //item image
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualImageDispStatus,data.expected.dispStatus, "Itinerary Page, Item Image Display Status Actual & Expected don't match")

        //item type of car
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualItemTypeOfCar,data.expected.firstItemTypeOfCar, "Itinerary Page, Item Type Of Car Actual & Expected don't match")

        //number of pax
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualPax,data.expected.firstItemPax, "Itinerary Page, Item Pax Actual & Expected don't match")
        //item title
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualItemTitle,data.expected.firstItemTitleTxt, "Itinerary Page, Item Title Text Actual & Expected don't match")

        //Cancellation fees apply
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualCancelFeesApplyTxt,data.expected.fristItemCancelTxt, "Itinerary Page, Cancellation fees apply  Text Actual & Expected don't match")

        //duration icon
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualDurationIconDisp,data.expected.dispStatus, "Itinerary Page, Duration Icon Display status  Actual & Expected don't match")
        //duration value
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualDurationText,data.expected.firstItemDurantionTxt, "Itinerary Page, Duration Text  Actual & Expected don't match")

        //luggage icon
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualLuggageIconDisp,data.expected.dispStatus, "Itinerary Page, Luggage Icon Display status  Actual & Expected don't match")
        //luggage value
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualLuggageText,data.expected.firstItemLuggageText, "Itinerary Page, Luggage Text  Actual & Expected don't match")

        //pax icon
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualPaxIconDisp,data.expected.dispStatus, "Itinerary Page, Pax Icon Display status  Actual & Expected don't match")
        //pax value
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualPaxText,data.expected.firstItemPaxText, "Itinerary Page, Pax Text  Actual & Expected don't match")

        //supporting language icon
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualLanguageIconDisp,data.expected.dispStatus, "Itinerary Page, Language Icon Display status  Actual & Expected don't match")
        //supporting language value
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualLanguageText,data.expected.language, "Itinerary Page, Language Text  Actual & Expected don't match")

        //service date & time
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualServiceDateAndtime,resultData.hotel.removeItemPage.cancelledItems.expectedServiceDateAndtime, "Itinerary Page, Service Date & time Text  Actual & Expected don't match")

        //Commission
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualCommissionTextAndValue,resultData.hotel.removeItemPage.cancelledItems.expectedCommissionTextAndValue, "Itinerary Page, Commission Text  Actual & Expected don't match")

        //total service item amount and currency
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualItemAmountAndCurrency,resultData.hotel.searchResults.itineraryBuilder.expectedFirstItemPrice, "Itinerary Page, total service item amount and currency  Actual & Expected don't match")

        //<No> functional buttons
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualNoButtonDispStatus,data.expected.dispStatus, "Itinerary Page, No Fuctional Button Display Status Actual & Expected don't match")

        //<Yes> functional buttons
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualYesButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Yes Fuctional Button Display Status Actual & Expected don't match")

        //the lightbox disappeared
        assertionEquals(resultData.hotel.removeItemPage.actualCancelItemPopupDispStatus,data.expected.notDispStatus, "Itinerary Page, lightbox display status  Actual & Expected don't match")

        //check the item is not cancelled
        assertionEquals(resultData.hotel.removeItemPage.actualItemStatus,data.expected.notDispStatus, "Itinerary Page, Item Not Cancelled status  Actual & Expected don't match")

        //Cancel itinerary lightbox display status
        assertionEquals(resultData.hotel.removeItemPage.actCancelItemDispStatus_FirstItem,data.expected.dispStatus, "Itinerary Page, Cancel Item Light box Display Status Actual & Expected don't match")

        //the lightbox disappeared
        assertionEquals(resultData.hotel.removeItemPage.actCancelItemPopupDispStatus,data.expected.notDispStatus, "Itinerary Page, lightbox display status after No button click Actual & Expected don't match")
        //check the item is not cancelled
        assertionEquals(resultData.hotel.removeItemPage.actItemStatus,data.expected.notDispStatus, "Itinerary Page, Item Not Cancelled status after No button click  Actual & Expected don't match")

        //Cancel itinerary lightbox display status
        assertionEquals(resultData.hotel.removeItemPage.actCancelItmDispStatus_FirstItem,data.expected.dispStatus, "Itinerary Page, Cancel Item Light box Display Status Actual & Expected don't match")

    }

    protected def "Verifycancelitinerarylightbox"(HotelSearchData data, HotelTransferTestResultData resultData){

        //lightbox title text should show
        assertionEquals(resultData.hotel.removeItemPage.actualTitleText,data.expected.titleText, "Itinerary Page, Cancel Itinerary popup, lightbox title text Actual & Expected don't match")

        //Close X text and funciton should show
        assertionEquals(resultData.hotel.removeItemPage.actualClosebuttonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Close X Function And Text Actual & Expected don't match")

        //This itinerary has booked items.  Deleting this itinerary will cancel these items. text should show
        //assertionEquals(resultData.hotel.removeItemPage.actualHeaderText,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Header Text (This itinerary has..) Actual & Expected don't match")

        //A cancellation charge of XXX.XX GBP applies. - should not show
        //assertionEquals(resultData.hotel.removeItemPage.actualHeaderNotShowText,data.expected.notDispStatus, "Itinerary Page, Cancel Itinerary popup, Header Text (A cancellation charge of..) Actual & Expected don't match")

        //Are you sure you want to delete this itinerary? - Text should show
        assertionEquals(resultData.hotel.removeItemPage.actualHeaderAreYouSureText,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Header Text (Are you sure you want to delete..) Actual & Expected don't match")



    }

    protected def "Verifycancelitinrylightbox"(HotelSearchData data, HotelTransferTestResultData resultData){

        //lightbox title text should show
        assertionEquals(resultData.hotel.removeItemPage.actualTitleText,data.expected.titleText, "Itinerary Page, Cancel Itinerary popup, lightbox title text Actual & Expected don't match")

        //Close X text and funciton should show
        assertionEquals(resultData.hotel.removeItemPage.actualClosebuttonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Close X Function And Text Actual & Expected don't match")

        //This itinerary has booked items.  Deleting this itinerary will cancel these items. text should show
        //assertionEquals(resultData.hotel.removeItemPage.actualHeaderText,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Header Text (This itinerary has..) Actual & Expected don't match")

        //A cancellation charge of XXX.XX GBP applies. - should show Total amount should be item 1 + 2 + 3
        //assertionEquals(resultData.hotel.removeItemPage.actualCancellationChargeText,resultData.hotel.removeItemPage.expectedCancellationChargeText, "Itinerary Page, Cancel Itinerary popup, Header Text (A cancellation charge of..) Actual & Expected don't match")

        //Are you sure you want to delete this itinerary? - Text should show
        assertionEquals(resultData.hotel.removeItemPage.actualHeaderAreYouSureText,data.expected.headerTextAreYouSure, "Itinerary Page, Cancel Itinerary popup, Header Text (Are you sure you want to delete..) Actual & Expected don't match")



    }

    protected def "VerifycancelitinerarylightboxNonBooked"(HotelSearchData data, HotelTransferTestResultData resultData){

        //lightbox title text should show
        assertionEquals(resultData.hotel.removeItemPage.actualTitleText,data.expected.titleText, "Itinerary Page, Cancel Itinerary popup, lightbox title text Actual & Expected don't match")

        //Close X text and funciton should show
        assertionEquals(resultData.hotel.removeItemPage.actualClosebuttonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Close X Function And Text Actual & Expected don't match")

        //Are you sure you want to delete this itinerary? - Text should show
        assertionEquals(resultData.hotel.removeItemPage.actualHeaderAreYouSureTextDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Header Text (Are you sure you want to delete..) Actual & Expected don't match")

        //A cancellation charge of XXX.XX GBP applies. - should not show
        assertionEquals(resultData.hotel.removeItemPage.actualHeaderNotShowText,data.expected.notDispStatus, "Itinerary Page, Cancel Itinerary popup, Header Text (A cancellation charge of..) Actual & Expected don't match")


    }

    protected def "VerifyaddremainingTravellers"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate  Second Traveller Name is Saved
        assertionEquals(resultData.hotel.itineraryPage.actualSecondTravellerName,resultData.hotel.itineraryPage.expectedSecondTravellerName, "Itinerary Page, Second Traveller name details Actual & Expected don't match")

        //Validate  Third Traveller Name is Saved
        assertionEquals(resultData.hotel.itineraryPage.actualThirdTravellerName,resultData.hotel.itineraryPage.expectedThirdTravellerName, "Itinerary Page, Third Traveller name details Actual & Expected don't match")

        //Validate  Fourth Traveller Name is Saved
        assertionEquals(resultData.hotel.itineraryPage.actualFourthTravellerName,resultData.hotel.itineraryPage.expectedFourthTravellerName, "Itinerary Page, Fourth Traveller name details Actual & Expected don't match")

        //Validate  Fifth Traveller Name is Saved
        assertionEquals(resultData.hotel.itineraryPage.actualFifthTravellerName,resultData.hotel.itineraryPage.expectedFifthTravellerName, "Itinerary Page, Fifth Traveller name details Actual & Expected don't match")



    }

    protected def "VerifyItinerarydetails"(HotelSearchData data, HotelTransferTestResultData resultData,int statusCode){
        /*
        if(statusCode==0){
            //Itinerary status should show status - Booked

            assertionEquals(resultData.hotel.removeItemPage.actualItineraryStatusBookedText,data.expected.itineraryStatusText, "Itinerary Page, Cancel Itinerary popup, Itinerary Status Text Actual & Expected don't match")


        }else if(statusCode==1){
            //Itinerary status should show status - Partially Booked

            assertionEquals(resultData.hotel.removeItemPage.actualItineraryStatusPartiallyBookedText,data.expected.itineraryStatusText, "Itinerary Page, Cancel Itinerary popup, Itinerary Status Text Actual & Expected don't match")


        }else if(statusCode==2){
            //Itinerary status should show status - Quote
            assertionEquals(resultData.hotel.removeItemPage.actualItineraryStatusQuoteText,data.expected.itineraryStatusQuoteText, "Itinerary Page, Cancel Itinerary popup, Itinerary Status Text Actual & Expected don't match")



        }


        //Itinerary Departure date & value
        assertionEquals(resultData.hotel.removeItemPage.actualDepartureDateTxt,resultData.hotel.removeItemPage.expectedDepartureDateTxt, "Itinerary Page, Cancel Itinerary popup, Itinerary Date and value Text Actual & Expected don't match")

        //Itinerary ID, Name & value
        assertionEquals(resultData.hotel.removeItemPage.actualItineraryIdNameValueTxt,resultData.hotel.itineraryPage.retrievedItineraryIdName, "Itinerary Page, Cancel Itinerary popup, Itinerary ID name value Text Actual & Expected don't match")

        //Lead Name and Number of pax
        assertionEquals(resultData.hotel.removeItemPage.actualItineraryLeadNamePaxText,resultData.hotel.removeItemPage.expectedItineraryLeadNamePaxText, "Itinerary Page, Cancel Itinerary popup, Lead Name value & Pax Text Actual & Expected don't match")

        //Create Date Text
        assertionEquals(resultData.hotel.removeItemPage.actualItineraryCreateDateTxt,resultData.hotel.removeItemPage.expectedItineraryCreateDateTxt, "Itinerary Page, Cancel Itinerary popup, Creation Date Text Actual & Expected don't match")

        */

    }
    protected def "Verifyfunctionbuttons"(HotelSearchData data, HotelTransferTestResultData resultData){

        //<No>  functional buttons
        assertionEquals(resultData.hotel.removeItemPage.actualNoButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, No button dispaly status Actual & Expected don't match")
        //<Yes> functional buttons
         assertionEquals(resultData.hotel.removeItemPage.actualYesButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Yes button dispaly status Actual & Expected don't match")

        //the lightbox disappeared
        assertionEquals(resultData.hotel.removeItemPage.actuallightboxDispStatus,data.expected.notDispStatus, "Itinerary Page, Cancel Itinerary popup, light box disappear status Actual & Expected don't match")



        //Cancel itinerary lightbox display status
        assertionEquals(resultData.hotel.removeItemPage.actualCancelItemLightBoxDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, light box display status Actual & Expected don't match")



    }

    protected def "Verifyfunctions"(HotelSearchData data, HotelTransferTestResultData resultData){

        //<No>  functional buttons
        assertionEquals(resultData.hotel.removeItemPage.actualNoButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, No button dispaly status Actual & Expected don't match")
        //<Yes> functional buttons
        assertionEquals(resultData.hotel.removeItemPage.actualYesButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Yes button dispaly status Actual & Expected don't match")


    }
    protected def "VerifyfunctionbuttonsNonBooked"(HotelSearchData data, HotelTransferTestResultData resultData){

        //<No>  functional buttons
        assertionEquals(resultData.hotel.removeItemPage.actualNoButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, No button dispaly status Actual & Expected don't match")
        //<Yes> functional buttons
        assertionEquals(resultData.hotel.removeItemPage.actualYesButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Cancel Itinerary popup, Yes button dispaly status Actual & Expected don't match")

        //user taken back to itinerary page
        assertionEquals(resultData.hotel.removeItemPage.actualHeaderNonBookedItemText,data.expected.itinerarypageNonBkdTxt, "Itinerary Page, Non Booked Items Text Actual & Expected don't match")


    }

    protected def "VerifyNonBookedblock"(HotelSearchData data, HotelTransferTestResultData resultData){

        //transfer item #1
        assertionEquals(resultData.hotel.itineraryPage.actualTransferItem1,data.expected.firstItemTypeOfCar, "Itinerary Page, Non Booked Items, Item 1 Actual & Expected don't match")


        //hotel item #2
        assertionEquals(resultData.hotel.itineraryPage.actualHotelItem2,data.input.hotelText, "Itinerary Page, Non Booked Items, Item 2 Actual & Expected don't match")


        //activity item #3
        assertionEquals(resultData.hotel.itineraryPage.actualActivityItem3,data.input.activitySelect, "Itinerary Page, Non Booked Items, Item 3 Actual & Expected don't match")


    }

    protected def "VerifyCancelled"(HotelSearchData data, HotelTransferTestResultData resultData){

        //user taken back to itinerary page
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualTravellerLabelTxt,resultData.hotel.removeItemPage.cancelledItems.expectedTravellerLabelTxt, "Itinerary Page After Cancel Itinerary, Traveller Details Label Text added Actual & Expected don't match")


    }
    protected def "VerifyUnavailableblock"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Unavailable and Cancelled Items
        assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualUnavailableAndCanncedItemsText,data.expected.firstItemUnavailableAndCnclTxt, "Itinerary Page, Unavailable and Cancelled Items Text Actual & Expected don't match")

       //section layout, colours
        //assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualUnavailableAndCanceledItemColr,data.expected.unavailableCancelItemsBgColor, "Itinerary Page, Unavailable and Cancelled Items Background color Actual & Expected don't match")



    }
    protected def "Verifyitem1transferitemcard"(HotelSearchData data, HotelTransferTestResultData resultData){

        Map actualItem1TransferData=resultData.transfer.unavailableAndCancelItems.get("item1")

        //Booking ID & value
        assertionEquals(actualItem1TransferData.actualBookingIdAndVal,resultData.transfer.unavailableAndCancelItems.expectedFirstBookingIdAndVal, "Itinerary Page, Unavailable and Cancelled Items, First Item, Booking Id Value & Text Actual & Expected don't match")

        //status - Cancelled
        assertionEquals(actualItem1TransferData.actualTransferItemStatus,data.expected.itemStatus, "Itinerary Page, Unavailable and Cancelled Items, First Item, Booking Status Text Actual & Expected don't match")

        //item image
        assertionEquals(actualItem1TransferData.actualItmeImage,resultData.transfer.unavailableAndCancelItems.expectedImageURL, "Itinerary Page, Unavailable and Cancelled Items, First Item, Image Src URL  Actual & Expected don't match")

        //item type of car
        assertionEquals(actualItem1TransferData.actualItemTypeOfCar,resultData.transfer.unavailableAndCancelItems.expectedFirstItemCarType, "Itinerary Page, Unavailable and Cancelled Items, First Item, Car Type  Actual & Expected don't match")

        //number of pax
        assertionEquals(actualItem1TransferData.actualTransferItemPax,resultData.transfer.unavailableAndCancelItems.expectedFirstItempax, "Itinerary Page, Unavailable and Cancelled Items, First Item, Pax Number  Actual & Expected don't match")

        //item title
        assertionEquals(actualItem1TransferData.actualTransferItemTitle,resultData.transfer.unavailableAndCancelItems.expectedFirstItemTitle, "Itinerary Page, Unavailable and Cancelled Items, First Item, Item Title Actual & Expected don't match")

        // Cancellation Text
        assertionEquals(actualItem1TransferData.actualTransferItemCancellationText,resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancellationText, "Itinerary Page, Unavailable and Cancelled Items, First Item, Cancellation Text Actual & Expected don't match")

        // Cancellation fees applylink
        assertionEquals(actualItem1TransferData.actualTransferItemCancelLink,resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLink, "Itinerary Page, Unavailable and Cancelled Items, First Item, Cancellation Link Actual & Expected don't match")

        // Cancellation fees apply icon
        assertionEquals(actualItem1TransferData.actualTransferItemCancelLinkIcon,resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancelLinkIcon, "Itinerary Page, Unavailable and Cancelled Items, First Item, Cancellation Link Icon Actual & Expected don't match")

        //Cancellation policy text
        assertionEquals(resultData.transfer.unavailableAndCancelItems.Item1actualCancellationPolicyText,resultData.transfer.unavailableAndCancelItems.expectedFirstItemCancellationPolicyTxt, "Itinerary Page, Unavailable and Cancelled Items, First Item, Cancellation Policy Inner Text Actual & Expected don't match")

        //duration icon
        assertionEquals(actualItem1TransferData.ItemDurationIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationIcon, "Itinerary Page, Unavailable and Cancelled Items, First Item, Duration Icon Display Actual & Expected don't match")

        //duration value
        assertionEquals(actualItem1TransferData.ItemDurationIconText,resultData.transfer.unavailableAndCancelItems.expectedFirstItemDurationValue, "Itinerary Page, Unavailable and Cancelled Items, First Item, Duration Value Text Actual & Expected don't match")

        //Luggage Icon
        assertionEquals(actualItem1TransferData.ItemLuggageIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageIcon, "Itinerary Page, Unavailable and Cancelled Items, First Item, Luggage Icon Actual & Expected don't match")
        //Luggage value
        assertionEquals(actualItem1TransferData.ItemLuggageIconText,resultData.transfer.unavailableAndCancelItems.expectedFirstItemLuggageValue, "Itinerary Page, Unavailable and Cancelled Items, First Item, Luggage Value Text Actual & Expected don't match")

        //pax Icon
        assertionEquals(actualItem1TransferData.ItemPaxIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxIcon, "Itinerary Page, Unavailable and Cancelled Items, First Item, Pax Icon Actual & Expected don't match")
        //Pax Value
        assertionEquals(actualItem1TransferData.ItempaxIconText,resultData.transfer.unavailableAndCancelItems.expectedFirstItemPaxValue, "Itinerary Page, Unavailable and Cancelled Items, First Item, Pax Value Text Actual & Expected don't match")

        //Language Icon
        //assertionEquals(actualItem1TransferData.ItemLanguageIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFirstItemLanguageIcon, "Itinerary Page, Unavailable and Cancelled Items, First Item, Language Icon Actual & Expected don't match")
        //Language Value
        //assertionEquals(actualItem1TransferData.ItemLanguageIconText,resultData.transfer.unavailableAndCancelItems.expectedFirstItemLangaugeValue, "Itinerary Page, Unavailable and Cancelled Items, First Item, Language Value Text Actual & Expected don't match")


        //service date
        //service time
        assertionEquals(actualItem1TransferData.ItemServiceDateTime,resultData.transfer.unavailableAndCancelItems.expectedFirstItemServiceDateTimeText, "Itinerary Page, Unavailable and Cancelled Items, First Item, Service Date & time Actual & Expected don't match")

        //commission and % value
        assertionEquals(actualItem1TransferData.ItemCommissionPercentAndValue,resultData.transfer.unavailableAndCancelItems.expectedFirstItemcommisionPercentValue, "Itinerary Page, Unavailable and Cancelled Items, First Item, Commission Percent Value Actual & Expected don't match")


        //total service item amount and currency
        assertionEquals(actualItem1TransferData.ItemTotalAmountAndCurncy,resultData.transfer.unavailableAndCancelItems.expectedFirstItemTotalServItemAmntCurncy, "Itinerary Page, Unavailable and Cancelled Items, First Item, Total Amount And Currency Actual & Expected don't match")

        //Traveller name list
        assertionEquals(actualItem1TransferData.ItemTravellerNameList,resultData.transfer.unavailableAndCancelItems.expectedFirstItemTravelrNameList, "Itinerary Page, Unavailable and Cancelled Items, First Item, Traveller Name List Actual & Expected don't match")

        //pick up details
        assertionEquals(resultData.transfer.unavailableAndCancelItems.Item1actualPickUpDetails,resultData.transfer.unavailableAndCancelItems.expectedFirstItemPickUpDetails, "Itinerary Page, Unavailable and Cancelled Items, First Item, Pick Up Details Actual & Expected don't match")

        //drop off details
        assertionEquals(resultData.transfer.unavailableAndCancelItems.Item1actualDropOffDetails,resultData.transfer.unavailableAndCancelItems.expectedFirstItemDropOffDetails, "Itinerary Page, Unavailable and Cancelled Items, First Item, Drop Off Details Actual & Expected don't match")




    }
    protected def "Verifyitem2hotelitemcard"(HotelSearchData data, HotelTransferTestResultData resultData){
        Map actualItem2HotelData=resultData.hotel.unavailableAndCancelItems.get("item2")

        //Booking ID & value
        assertionEquals(actualItem2HotelData.actualBookingIdAndVal,resultData.hotel.unavailableAndCancelItems.expectedSecondBookingIdAndVal, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Booking Id Value & Text Actual & Expected don't match")

        //status - Cancelled
        assertionEquals(actualItem2HotelData.actualHotelItemStatus,data.expected.itemStatus, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Booking Status Text Actual & Expected don't match")

        //item image
        assertionEquals(actualItem2HotelData.actualItemImage,resultData.hotel.unavailableAndCancelItems.expectedSecondImageURL, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Image Src URL  Actual & Expected don't match")

        //item Name
        assertionEquals(actualItem2HotelData.actualItemName,resultData.hotel.unavailableAndCancelItems.expectedSecondItemName, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Item Type  Actual & Expected don't match")

        //hotel star rating
        assertionEquals(resultData.hotel.unavailableAndCancelItems.actualSecondItemHotelStarRatng,resultData.hotel.unavailableAndCancelItems.expectedSecondItemHotelStarRatng, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Hotel Star Rating  Actual & Expected don't match")

        //room type name
        assertionEquals(resultData.hotel.unavailableAndCancelItems.actualSecondItemroomdescTxt,resultData.hotel.unavailableAndCancelItems.expectedSecondItemroomdescTxt, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Room Type name  Actual & Expected don't match")

        //Pax number
        assertionEquals(resultData.hotel.unavailableAndCancelItems.actualSecondItemPaxNum,resultData.hotel.unavailableAndCancelItems.expectedSecondItemPaxNum, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Pax Num Actual & Expected don't match")

        //Rate plan - meal basis
        assertionEquals(resultData.hotel.unavailableAndCancelItems.actualSecondItemMealbasis,resultData.hotel.unavailableAndCancelItems.expectedSecondItemMealbasis, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Meal Basis Text Actual & Expected don't match")

        //Cancellation fees apply - text
        assertionEquals(actualItem2HotelData.actualItemCancellationText,resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancellationText, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Cancellation fees apply - text Actual & Expected don't match")

        // Cancellation fees applylink
        assertionEquals(actualItem2HotelData.actualItemCancelLink,resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancelLink, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Cancellation fees apply link Actual & Expected don't match")

        // Cancellation fees apply icon
        assertionEquals(actualItem2HotelData.actualItemCancelLinkIcon,resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancelLinkIcon, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Cancellation fees apply icon Actual & Expected don't match")

        //Cancellation policy text
        assertionEquals(resultData.hotel.unavailableAndCancelItems.Item2actualCancellationPolicyText,resultData.hotel.unavailableAndCancelItems.expectedSecondItemCancellationPolicyText, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Cancellation Policy Inner Text Actual & Expected don't match")

        //check in date - stay night(s)
        assertionEquals(actualItem2HotelData.actualCheckInDateNights,resultData.hotel.unavailableAndCancelItems.expectedSecondItemChkInDateNights, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Check In Date, Nights Text Actual & Expected don't match")

        //commission and %
        assertionEquals(actualItem2HotelData.actualCommissionAndPercent,resultData.hotel.unavailableAndCancelItems.expectedSecondItemCommissionPercentAndValue, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Commission Percent Value Text Actual & Expected don't match")

        // item amount and currency
        assertionEquals(actualItem2HotelData.actualAmountAndCurrency,resultData.hotel.unavailableAndCancelItems.expectedSecondItemTotalAmountAndCurncy, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Total Amnt Currency Value Text Actual & Expected don't match")

        //Traveller name list
        assertionEquals(actualItem2HotelData.actualTravellersList,resultData.hotel.unavailableAndCancelItems.expectedSecondItemTravellerNameList, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Traveller name List Text Actual & Expected don't match")

        //Add a remark or comment
        assertionEquals(actualItem2HotelData.actualAddRemarkOrCommentText,resultData.hotel.unavailableAndCancelItems.expectedSecondItemAddRemarkOrCommentText, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Add Remark or Comment Text Actual & Expected don't match")

        //Edit remark or comment
        assertionEquals(resultData.hotel.unavailableAndCancelItems.actualSecondItemEditRemarkOrCommentDispStatus,data.expected.notDispStatus, "Itinerary Page, Unavailable and Cancelled Items, Second Item, Edit remark or comment Icon Disp Status Actual & Expected don't match")




    }
    protected def "Verifyitem3activityitemcard"(HotelSearchData data, HotelTransferTestResultData resultData){
        Map actualItem3ActivityData=resultData.activity.unavailableAndCancelItems.get("item3")

        //Booking ID & value
        assertionEquals(actualItem3ActivityData.actualBookingIdAndVal,resultData.activity.unavailableAndCancelItems.expectedThirdBookingIdAndVal, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Booking Id Value & Text Actual & Expected don't match")

        //status - Cancelled
        assertionEquals(actualItem3ActivityData.actualActivityItemStatus,data.expected.itemStatus, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Booking Status Text Actual & Expected don't match")

        //item image
        assertionEquals(actualItem3ActivityData.actualItemImage,resultData.activity.unavailableAndCancelItems.expectedImageURL, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Image Src URL  Actual & Expected don't match")

        //item Name
        assertionEquals(actualItem3ActivityData.actualItemName,resultData.activity.unavailableAndCancelItems.expectedThirdItemName, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Item Type  Actual & Expected don't match")

        // Cancellation Text
        assertionEquals(actualItem3ActivityData.actualItemCancellationText,resultData.activity.unavailableAndCancelItems.expectedThirdItemCancellationText, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Cancellation Text Actual & Expected don't match")

        // Duration Icon
        assertionEquals(actualItem3ActivityData.actualdurationIconStatus,resultData.activity.unavailableAndCancelItems.expectedThirdItemDurationIconStatus, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Duration Icon Actual & Expected don't match")

        // Duration Value
        assertionEquals(actualItem3ActivityData.actualdurationText,resultData.activity.unavailableAndCancelItems.expectedThirdItemDurationValue, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Duration Icon Value Actual & Expected don't match")

        // Language Icon
        assertionEquals(actualItem3ActivityData.actualLangIconStatus,resultData.activity.unavailableAndCancelItems.expectedThirdItemLangIconStatus, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Language Icon Actual & Expected don't match")

        // language Value
        //assertionEquals(actualItem3ActivityData.actualLangText,resultData.activity.unavailableAndCancelItems.expectedThirdItemLangValue, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Language Icon Value Actual & Expected don't match")
        assertContains(actualItem3ActivityData.actualLangText,resultData.activity.unavailableAndCancelItems.expectedThirdItemLangValue,"Itinerary Page, Unavailable and Cancelled Items, Third Item, Language Icon Value Actual & Expected don't match")

        //service date & Pax
        assertionEquals(actualItem3ActivityData.actualDateAndPax,resultData.activity.unavailableAndCancelItems.expectedThirdItemDateAndPax, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Service Date & Pax Actual & Expected don't match")

        //Commission and %
        assertionEquals(actualItem3ActivityData.actualCommissionAndPercent,resultData.activity.unavailableAndCancelItems.expectedThirdItemCommissionAndPercent, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Commission And Percent Actual & Expected don't match")

        //Amount & Currency
        assertionEquals(actualItem3ActivityData.actualAmountAndCurrency,resultData.activity.unavailableAndCancelItems.expectedThirdItemAmountAndCurrency, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Amount And Currency Actual & Expected don't match")

        //Traveller name list
        assertionEquals(actualItem3ActivityData.actualTravellersList,resultData.activity.unavailableAndCancelItems.expectedThirdItemTravellers, "Itinerary Page, Unavailable and Cancelled Items, Third Item, Travellers List Actual & Expected don't match")

    }
    protected def "Verifyitem4transferitemcard"(HotelSearchData data, HotelTransferTestResultData resultData){

        Map actualItem1TransferData=resultData.transfer.unavailableAndCancelItems.get("item4")

        //Booking ID & value
        assertionEquals(actualItem1TransferData.actualBookingIdAndVal,resultData.transfer.unavailableAndCancelItems.expectedFourthBookingIdAndVal, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Booking Id Value & Text Actual & Expected don't match")

        //status - Cancelled
        assertionEquals(actualItem1TransferData.actualTransferItemStatus,data.expected.itemStatus, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Booking Status Text Actual & Expected don't match")

        //item image
        assertionEquals(actualItem1TransferData.actualItmeImage,resultData.transfer.unavailableAndCancelItems.expectedFourthImageURL, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Image Src URL  Actual & Expected don't match")

        //item type of car
        assertionEquals(actualItem1TransferData.actualItemTypeOfCar,resultData.transfer.unavailableAndCancelItems.expectedFourthItemCarType, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Car Type  Actual & Expected don't match")

        //number of pax
        assertionEquals(actualItem1TransferData.actualTransferItemPax,resultData.transfer.unavailableAndCancelItems.expectedFourthItempax, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Pax Number  Actual & Expected don't match")

        //item title
        assertionEquals(actualItem1TransferData.actualTransferItemTitle,resultData.transfer.unavailableAndCancelItems.expectedFourthItemTitle, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Item Title Actual & Expected don't match")

        // Cancellation Text
        assertionEquals(actualItem1TransferData.actualTransferItemCancellationText,resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancellationText, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Cancellation Text Actual & Expected don't match")

        // Cancellation fees applylink
        assertionEquals(actualItem1TransferData.actualTransferItemCancelLink,resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancelLink, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Cancellation Link Actual & Expected don't match")

        // Cancellation fees apply icon
        assertionEquals(actualItem1TransferData.actualTransferItemCancelLinkIcon,resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancelLinkIcon, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Cancellation Link Icon Actual & Expected don't match")

        //Cancellation policy text
        assertionEquals(resultData.transfer.unavailableAndCancelItems.Item4actualCancellationPolicyText,resultData.transfer.unavailableAndCancelItems.expectedFourthItemCancellationPolicyTxt, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Cancellation Policy Inner Text Actual & Expected don't match")

//duration icon
        assertionEquals(actualItem1TransferData.ItemDurationIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFourthItemDurationIcon, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Duration Icon Display Actual & Expected don't match")

        //duration value
        assertionEquals(actualItem1TransferData.ItemDurationIconText,resultData.transfer.unavailableAndCancelItems.expectedFourthItemDurationValue, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Duration Value Text Actual & Expected don't match")

        //Luggage Icon
        assertionEquals(actualItem1TransferData.ItemLuggageIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFourthItemLuggageIcon, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Luggage Icon Actual & Expected don't match")
        //Luggage value
        assertionEquals(actualItem1TransferData.ItemLuggageIconText, resultData.transfer.unavailableAndCancelItems.expectedFourthItemLuggageValue, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Luggage Value Text Actual & Expected don't match")

        //pax Icon
        assertionEquals(actualItem1TransferData.ItemPaxIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFourthItemPaxIcon, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Pax Icon Actual & Expected don't match")
        //Pax Value
        assertionEquals(actualItem1TransferData.ItempaxIconText,resultData.transfer.unavailableAndCancelItems.expectedFourthItemPaxValue, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Pax Value Text Actual & Expected don't match")

        //Language Icon
        assertionEquals(actualItem1TransferData.ItemLanguageIconDisplayStatus,resultData.transfer.unavailableAndCancelItems.expectedFourthItemLanguageIcon, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Language Icon Actual & Expected don't match")
        //Language Value
        assertionEquals(actualItem1TransferData.ItemLanguageIconText,resultData.transfer.unavailableAndCancelItems.expectedFourthItemLangaugeValue, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Language Value Text Actual & Expected don't match")

//service date
        //service time
        assertionEquals(actualItem1TransferData.ItemServiceDateTime,resultData.transfer.unavailableAndCancelItems.expectedFourthItemServiceDateTimeText, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Service Date & time Actual & Expected don't match")

        //commission and % value
        assertionEquals(actualItem1TransferData.ItemCommissionPercentAndValue,resultData.transfer.unavailableAndCancelItems.expectedFourthItemcommisionPercentValue, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Commission Percent Value Actual & Expected don't match")


        //total service item amount and currency
        assertionEquals(actualItem1TransferData.ItemTotalAmountAndCurncy,resultData.transfer.unavailableAndCancelItems.expectedFourthItemTotalServItemAmntCurncy, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Total Amount And Currency Actual & Expected don't match")

        //Traveller name list
        assertionEquals(actualItem1TransferData.ItemTravellerNameList,resultData.transfer.unavailableAndCancelItems.expectedFirstItemTravelrNameList, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Traveller Name List Actual & Expected don't match")

        //pick up details
        assertionEquals(resultData.transfer.unavailableAndCancelItems.Item4actualPickUpDetails,resultData.transfer.unavailableAndCancelItems.expectedFourthItemPickUpDetails, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Pick Up Details Actual & Expected don't match")

        //drop off details
        assertionEquals(resultData.transfer.unavailableAndCancelItems.Item4actualDropOffDetails,resultData.transfer.unavailableAndCancelItems.expectedFourthItemDropOffDetails, "Itinerary Page, Unavailable and Cancelled Items, Fourth Item, Drop Off Details Actual & Expected don't match")

    }

    protected def "VerifyAppliedcharge"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Applied Cancellation Charges Sub-Total - should not show
        assertionEquals(resultData.hotel.itineraryPage.actualAppliedChargeTxtDispStatus,data.expected.notDispStatus, "Itinerary page Applied Cancellation Charges Sub-Total text display status Actual & Expected don't match")



    }
    protected def "VerifyAppliedchargeForFullCharge"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Applied Cancellation Charges Sub-Total - should  show
        assertionEquals(resultData.hotel.itineraryPage.actualAppliedChargeTxtDispStatus,data.expected.dispStatus, "Itinerary page Applied Cancellation Charges Sub-Total text display status Actual & Expected don't match")

        //it should be same amount of applied charge amount stated on cancel item lightbox in the earlier system flow
        assertionEquals(resultData.hotel.itineraryPage.actualtotalAmountAndCurrency,resultData.hotel.itineraryPage.expectedTotalAmountAndCurrencyInCancelLightbox, "Itinerary page Applied Cancellation Charges Sub-Total Amount & Currency text Actual & Expected don't match")


        //Total amount should be item 1 + 2 + 3 & currency
        assertionEquals(resultData.hotel.itineraryPage.actualtotalAmountAndCurrency,resultData.hotel.itineraryPage.expectedtotalAmountAndCurrency, "Itinerary page Applied Cancellation Charges Sub-Total Amount & Currency (Total of item 1+ item2 +item3) Actual & Expected don't match")




    }

    protected def "VerifyNonBookedBlock"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Non-Booked Block - Should not show
        assertionEquals(resultData.hotel.itineraryPage.actualNonBookedTextDispStatus,data.expected.notDispStatus, "Itinerary page Non Booked block display status Actual & Expected don't match")
        //transfer item 1 should be removed from itinerary
        assertionEquals(resultData.hotel.itineraryPage.actualTransferItem1DispStatus,data.expected.notDispStatus, "Itinerary page Transfer item 1 display status Actual & Expected don't match")

        //hotel item 2 should be removed from itienrary
        assertionEquals(resultData.hotel.itineraryPage.actualHotelItem2DispStatus,data.expected.notDispStatus, "Itinerary page Hotel item 2 display status Actual & Expected don't match")

        //activity item 3 should be removed from itinerary
        assertionEquals(resultData.hotel.itineraryPage.actualActivityItem3DispStatus,data.expected.notDispStatus, "Itinerary page Activity item 3 display status Actual & Expected don't match")

        //Unavailable Block - should not show
        assertionEquals(resultData.hotel.itineraryPage.actualUnavailableTitleTextDispStatus,data.expected.notDispStatus, "Itinerary page Unavailable Block display status Actual & Expected don't match")

        //hotel item - 4 - should be removed from itinerary
        assertionEquals(resultData.hotel.itineraryPage.actualUnavailableHotelItemDispStatus,data.expected.notDispStatus, "Itinerary page Hotel item 4 display status Actual & Expected don't match")


        //Applied Cancellation Charges Sub-Total - should not show
        assertionEquals(resultData.hotel.itineraryPage.actualAppliedChargeTextDispStatus,data.expected.notDispStatus, "Itinerary page Applied Cancellation Charges Sub-Total text display status Actual & Expected don't match")


    }

    protected def "Verifyaddadditionalitem"(HotelSearchData data, HotelTransferTestResultData resultData){

        //user taken to home page
        assertionEquals(resultData.hotel.search.actFindBtnStatus,data.expected.dispStatus, "Home Page, Find Button Enabled status Actual & Expected don't match")



    }

    protected def "Verifyreturntohome"(HotelSearchData data, HotelTransferTestResultData resultData){

        //user taken to home page
        assertionEquals(resultData.hotel.search.actFindBtnStatus,data.expected.dispStatus, "Home Page, Find Button Enabled status Actual & Expected don't match")

        //Itinerary builder should not opens up
        assertionEquals(resultData.hotel.search.actualItineraryBuilderDispStatus,data.expected.notDispStatus, "Home Page, Itinerary builder section status Actual & Expected don't match")



    }

}
