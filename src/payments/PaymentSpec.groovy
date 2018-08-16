package com.kuoni.qa.automation.test.payments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PaymentsTestData
import com.kuoni.qa.automation.helper.PaymentsTestResultData
import com.kuoni.qa.automation.page.ActivityPDPPage
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.HomePage
import com.kuoni.qa.automation.page.activity.ActivitySearchResultsPage
import com.kuoni.qa.automation.page.activity.ActivitySearchToolPage
import com.kuoni.qa.automation.page.payments.MyAccountPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.payments.PaymentsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.TransferSearchPage
import com.kuoni.qa.automation.page.transfers.TransferSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.test.search.booking.hotel.SanityHotelBookingBaseSpec
import org.testng.Assert
import spock.lang.Shared
import com.kuoni.qa.automation.util.DateUtil

import java.math.RoundingMode
import java.text.DecimalFormat

import static com.kuoni.qa.automation.util.TestConf.clientData
import static com.kuoni.qa.automation.util.TestConf.paymentDetails


/**
 * Created by mtmaku on 4/12/2017.
 */
abstract class PaymentSpec extends SanityHotelBookingBaseSpec{

    int itemIndex = 0
    public static  int  size = 0
    DateUtil dateUtil = new DateUtil()
    @Shared
    public PaymentsTestData defaultData = new PaymentsTestData("default", "payments",paymentDetails)

    protected def loginToApplicaiton(ClientData clientData, PaymentsTestData data, PaymentsTestResultData resultData){
        login(clientData)
        at HotelSearchPage
        resultData.hotel.search.actualFindButtonStatus=getFindButtonDisabled()
    }

    //Genric method to get checkIndate and nights (Expected Data)
    protected String getCheckInDateNights(String checkInDays, String checkoutDays){
        String  dur = ""

        def checkInDt  = dateUtil.addDaysChangeDatetformat(checkInDays.toInteger(), "ddMMMyy").toLowerCase()

        int nights=checkoutDays.toInteger()-checkInDays.toInteger()
        if (nights >1)
            dur = checkInDt+","+nights+" nights"
        else
            dur = checkInDt+","+nights+" night"
        return dur
    }

    //search for hotel and click on add to itinerary page
    def searchHotelAndAddToItinerary(PaymentsTestData data,PaymentsTestResultData resultData){
        Map dataMap = [:]
        fillHotelSearchFormAndSubmit(data.input.property.toString(), data.input.propertySuggest.toString(), data.input.checkInDays.toString(), data.input.checkOutDays.toString(), data.input.adults.toString(), data.input.child)
        //  search Results returned
        resultData.hotel.searchResults.actualSearchResults = searchResultsReturned()
        at HotelSearchResultsPage
        sleep(1000)
        // store itemCancellationLinkStatus and price of the passed hotel item.
        resultData.hotel.searchResults.freeCancellationStatus = getFreeCancellationLinkStatus(data.input.hotelName)
        resultData.hotel.searchResults.itemPrice = scrollToSpecificItemAndGetPrice(data.input.hotelName)
        //click on add to itinerary
        scrollToSpecificItnryAndClickAddToItinryBtn(data.input.hotelName)
        resultData.hotel.searchResults.itineraryBuilderStatus = verifyItenaryBuilderDisplayed()

    }

    //Search hotel based on passed hotelName arg and add to itinerary - Genralized with hotelName arg
    def searchHotelAndAddToItinerary(PaymentsTestData data,PaymentsTestResultData resultData,String hotelName,String checkInDays,String checkOutDays){

        fillHotelSearchFormAndSubmit(data.input.property.toString(), data.input.propertySuggest.toString(),checkInDays,checkOutDays, data.input.adults.toString(), data.input.child)
        at HotelSearchResultsPage
        sleep(1000)
        //search results returned.
        resultData.hotel.searchResults.actualSearchResults = searchResultsReturned()
        sleep(1000)
        // store itemCancellationLinkStatus and price of the passed hotel item.
        resultData.hotel.searchResults.itemPriceSecondItem = scrollToSpecificItemAndGetPrice(hotelName)
        resultData.hotel.searchResults.freeCancellationStatus = getFreeCancellationLinkStatus(hotelName)
       // click on add to itinerary based on passed hotel item
        scrollToSpecificItnryAndClickAddToItinryBtn(hotelName)

        resultData.hotel.searchResults.itineraryBuilderStatus = verifyItenaryBuilderDisplayed()

    }

    //Used for search for hotel and always click on first item in search results
    def searchForHotelAndAddToItinerary(ClientData clientData,PaymentsTestData data,PaymentsTestResultData resultData){

        fillHotelSearchFormAndSubmit(data.input.area.toString(), data.input.areaAutoSuggest.toString(), data.input.checkInDays.toString(), data.input.checkOutDays.toString(), data.input.adults.toString(), data.input.child)

        at HotelSearchResultsPage
        waitTillAddToItineraryBtnLoads()
        sleep(1000)
        //search results returned
        resultData.hotel.searchResults.actualSearchResults=searchResultsReturned()
        //click on first item with Free Cancellation until

        int firstItemIndex=clickAddToItinry(data.expected.hotelCancelTxt,data.expected.statusTxt)
        itemIndex = firstItemIndex
        println("Item Index Retrieved From Search Results:$firstItemIndex")

        sleep(3000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)
        at HotelSearchResultsPage
        //code to retrieve hotel related items if required in future
        resultData.hotel.searchResults.hotelItem = storeHotelItemDetails(itemIndex,0)
        resultData.hotel.searchResults.hotelItem.priceAndCurrency=resultData.hotel.searchResults.hotelItem.priceAndCurrency+" "+clientData.currency
        //to Expand
        if(getItineraryBarExpandOrCollapseStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }
        at HotelSearchResultsPage

        //capture hotel item added to itinerary
        resultData.hotel.searchResults.itineraryBuilder.actualHotelNameInItinryBuilder=getHotelNameInTitleCard(0)
        resultData.hotel.searchResults.actualHotelName=getHotelNameBasedOnIndex(firstItemIndex)


        //Free cacellation should be present
        resultData.hotel.searchResults.actualCancellatoinTxtDispStatus=getCancellationText(resultData.hotel.searchResults.actualHotelName,data.expected.hotelCancelTxt)
        println("Actual Status: $resultData.hotel.searchResults.actualCancellatoinTxtDispStatus")
        if(getItineraryBarExpandOrCollapseStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }

        at ItenaryBuilderPage
        //capture new Itinerary reference number
        String itineraryBuilderTitle = getItenaryBuilderTtile()
        //println("$itineraryBuilderTitle")
        List<String> tempList=itineraryBuilderTitle.tokenize(" ")
        //println("$tempList")
        String itineraryId=tempList.getAt(0)+tempList.getAt(1)
        resultData.hotel.searchResults.retitineraryId=itineraryId
        println("Retrieved Itinerary ID: $resultData.hotel.searchResults.retitineraryId")
        //Following method included by madhavi as per R13C2 UI changes
        //clickItineraryBuilderToggle()
        clickOnGotoItenaryButton()
        sleep(5000)

        at ItineraryTravllerDetailsPage
        //Itinerary page should be displayed.
        String travlrLablTxt=getTravellelersLabelName(0)
        resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt=travlrLablTxt.trim()
        resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt=data.expected.travlrLabelText.toString()+" (lead)"


    }

    //Used for search for transfers and always click on first item in search results
    def searchForTransferAndAddToItinerary(PaymentsTestData data,PaymentsTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //clik on manage Itinerary , select Add transfer link
        selectOptionFromManageItinerary(data.expected.manageItineraryValue)


        at TransferSearchPage
        enterPickupInput(data.input.pickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(data.input.selectPickUp)
        enterDropoffInput(data.input.dropOff)
        sleep(1000)
        selectFromAutoSuggestDropOff(data.input.selectDropOff)
        sleep(1000)
        selectDateCalender(data.input.transferchkinDays.toInteger())
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

        resultData.transfer.searchResults.actualSearchResults=searchResultsReturned()

        //click on first item with Cancellation fees
        int secondItemIndex=clickAddToItinry(data.expected.transfersCancelTxt,data.expected.statusTxt)
        println("Item Index Retrieved From Search Results:$secondItemIndex")

        sleep(3000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)
        at TransferSearchResultsPage
        if(getShowCarTypesExpandOrCollapseStatus(data.input.itemRecordIndex)==false){
            clickShowCarTypes(data.input.itemRecordIndex)
            sleep(2000)
        }
        //resultData.transfer.searchResults.transferItem=storeTransferItemDetails(secondItemIndex)
        resultData.transfer.searchResults.transferItem=storeTransfersItemDetails(data.input.itemRecordIndex,secondItemIndex)

        //service duration icon
        //resultData.transfer.searchResults.transferItem.actualDurationIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("time",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualDurationIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("time",data.input.itemRecordIndex,secondItemIndex)
        //place cursor on top of icon, tooltip show:		Duration"
        //clickTransferItemsIconsDispInSearchResultsScrn("time",secondItemIndex)
        clickTransferItemsIconsDispInSearchResultsScrn("time",data.input.itemRecordIndex,secondItemIndex)
        //resultData.transfer.searchResults.transferItem.actualDurationIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("time",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualDurationIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("time",data.input.itemRecordIndex,secondItemIndex)
        //service duration time & value
        //resultData.transfer.searchResults.transferItem.actualDurationTimeAndValueText=getTransferItemIconsTextInSearchResultsScrn("time",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualDurationTimeAndValueText=getTransferItemIconsTextInSearchResultsScrn("time",data.input.itemRecordIndex,secondItemIndex)


        //"maximum luggage icon
        //resultData.transfer.searchResults.transferItem.actualLuggageIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("luggage",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualLuggageIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("luggage",data.input.itemRecordIndex,secondItemIndex)
        //place cursor on top of icon, tooltip show:		Luggage allowance"
        //clickTransferItemsIconsDispInSearchResultsScrn("luggage",secondItemIndex)
        clickTransferItemsIconsDispInSearchResultsScrn("luggage",data.input.itemRecordIndex,secondItemIndex)
        //resultData.transfer.searchResults.transferItem.actualLuggageIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("luggage",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualLuggageIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("luggage",data.input.itemRecordIndex,secondItemIndex)
        //validate max luggage
        //resultData.transfer.searchResults.transferItem.actualTransferLuggage=getTransferItemIconsTextInSearchResultsScrn("luggage",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualTransferLuggage=getTransferItemIconsTextInSearchResultsScrn("luggage",data.input.itemRecordIndex,secondItemIndex)

        //"maximum passenger allowed icon
        //resultData.transfer.searchResults.transferItem.actualPaxIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("pax",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualPaxIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("pax",data.input.itemRecordIndex,secondItemIndex)
        //place cursor on top of icon, tooltip show:		Maximum passenger allowed"
        //clickTransferItemsIconsDispInSearchResultsScrn("pax",secondItemIndex)
        clickTransferItemsIconsDispInSearchResultsScrn("pax",data.input.itemRecordIndex,secondItemIndex)
        //resultData.transfer.searchResults.transferItem.actualPaxIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("pax",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualPaxIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("pax",data.input.itemRecordIndex,secondItemIndex)
        //validate max passenger text
        //resultData.transfer.searchResults.transferItem.actualTransferLuggage=getTransferItemIconsTextInSearchResultsScrn("pax",secondItemIndex)
        resultData.transfer.searchResults.transferItem.actualTransferLuggage=getTransferItemIconsTextInSearchResultsScrn("pax",data.input.itemRecordIndex,secondItemIndex)


        at HotelSearchResultsPage
        if(getItineraryBarExpandOrCollapseStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }

        at TransferSearchResultsPage

        //capture transfer item added to itinerary
        resultData.transfer.searchResults.itineraryBuilder.actualTransferNameInItineraryBuilder=getTransferItineraryItem(1)
        //resultData.transfer.searchResults.actualTransferName=getTransferVehicleName(secondItemIndex)
        resultData.transfer.searchResults.actualTransferName=getTransferVehicleName(data.input.itemRecordIndex,secondItemIndex)

        //Cancellation fees should be present
        resultData.transfer.searchResults.actualCancellatoinTxtDispStatus=getCancellationText(data.input.itemRecordIndex,secondItemIndex,data.expected.transfersCancelTxt)
        println("Actual Status: $resultData.transfer.searchResults.actualCancellatoinTxtDispStatus")
        at HotelSearchResultsPage
        if(getItineraryBarExpandOrCollapseStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }

        at ItenaryBuilderPage

        clickOnGotoItenaryButton()
        sleep(5000)

        at ItineraryTravllerDetailsPage
        //Itinerary page should be displayed.
        String travlrLablTxt=getTravellelersLabelName(0)
        resultData.transfer.itineraryPage.actualTravellerLabelTxt=travlrLablTxt.trim()
    }


    //Used for search for activity and always click on first item in search results
    def searchForActivityAndAddToItinerary(PaymentsTestData data,PaymentsTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //clik on manage Itinerary , select Add Activity link
        selectOptionFromManageItinerary(data.expected.manageItinryValue)

        at ActivitySearchToolPage

        enterActivityDestination(data.input.activityTypeText)
        sleep(2000)
        selectFromAutoSuggest(data.input.activityAutoSuggest)

        //select Date
        clickOnIndiDate()
        sleep(3000)
        selectDayInCalender(data.input.activitychkInDays.toInteger())

        selectPax()

        //select no.of adults
        selectNoAdults(data.input.activityPax.toString())
        sleep(1000)
        clickFindButton()
        sleep(7000)


        at ActivitySearchResultsPage

        resultData.activity.searchResults.actualSearchResults=searchResult()

        at HotelSearchResultsPage
        if(getItineraryBarExpandOrCollapseStatus()==false) {
            at ItenaryBuilderPage
            //Collapse
            hideItenaryBuilder()
            sleep(3000)
        }
        at ActivitySearchResultsPage
        //click on first item with Cancellation charges
        int thirdItemIndex=clickAddToItinry(data.expected.sightSeeingcancelTxt,data.expected.statusTxt)
        println("Item Index Retrieved From Search Results:$thirdItemIndex")

        sleep(3000)

        at ItineraryTravllerDetailsPage
        clickOnBackButtonInItineraryScreen()
        sleep(3000)

        at ActivitySearchResultsPage

        resultData.activity.searchResults.sightSeeingItem=storeSightSeeingItemDetails(thirdItemIndex)

        //service duration icon
        resultData.activity.searchResults.sightSeeingItem.actualDurationIconDispStatus=getSightSeeingItemsIconsDispInSearchResultsScrn("time",thirdItemIndex)
        //place cursor on top of icon, tooltip show: Duration"
        clickSightSeeingItemsIconsDispInSearchResultsScrn("time",thirdItemIndex)
        resultData.activity.searchResults.sightSeeingItem.actualDurationIconMouseHoverText=getSightSeeingItemIconsMouseHoverTextInSearchResultsScrn("time",thirdItemIndex)
        //service duration time & value
        resultData.activity.searchResults.sightSeeingItem.actualDurationTimeAndValueText=getSightSeeingItemIconsTextInSearchResultsScrn("time",thirdItemIndex)

        //Language duration icon
        resultData.activity.searchResults.sightSeeingItem.actualLanguageIconDispStatus=getSightSeeingItemsIconsDispInSearchResultsScrn("language",thirdItemIndex)
        //place cursor on top of icon, tooltip show: Languages Spoken"
        clickSightSeeingItemsIconsDispInSearchResultsScrn("language",thirdItemIndex)
        resultData.activity.searchResults.sightSeeingItem.actualLanguageIconMouseHoverText=getSightSeeingItemIconsMouseHoverTextInSearchResultsScrn("language",thirdItemIndex)
        //service duration time & value
        //resultData.activity.searchResults.sightSeeingItem.actualLangaugeValueText=getSightSeeingItemIconsTextInSearchResultsScrn("language",thirdItemIndex)
        String actualsightSeeingLangugeItemText=getSightSeeingItemIconsTextInSearchResultsScrn("language",thirdItemIndex)
        List<String> sightSeeingLangItemText=actualsightSeeingLangugeItemText.tokenize(",")
        resultData.activity.searchResults.sightSeeingItem.actualLangaugeValueText=sightSeeingLangItemText.sort().toString()

        at HotelSearchResultsPage
        if(getItineraryBarExpandOrCollapseStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }

        at ActivitySearchResultsPage

        try{
            //capture Activity item added to itinerary
            resultData.activity.searchResults.itineraryBuilder.actualActivityNameInItinryBuilder=verifyActivityNameOnBuilder(2)

        }catch(Exception e){
            resultData.activity.searchResults.itineraryBuilder.actualActivityNameInItinryBuilder="Unable To Read From UI"
        }
        resultData.activity.searchResults.actualActivityName=getActivityName(thirdItemIndex)

        //Cancellation fees should be present
        resultData.activity.searchResults.actualCancellatoinTxtDispStatus=getCancellationText(thirdItemIndex,data.expected.sightSeeingcancelTxt)
        println("Actual Status: $resultData.activity.searchResults.actualCancellatoinTxtDispStatus")
        at HotelSearchResultsPage
        if(getItineraryBarSectionDisplayStatus()==true) {
            at ItenaryBuilderPage
            //Expand
            hideItenaryBuilder()
            sleep(3000)
        }

        at ItenaryBuilderPage

        clickOnGotoItenaryButton()
        sleep(5000)

        at ItineraryTravllerDetailsPage
        //Itinerary page should be displayed.
        String travlrLablTxt=getTravellelersLabelName(0)
        resultData.activity.itineraryPage.actualTravellerLabelTxt=travlrLablTxt.trim()
    }


    //Method not been used as per 10.3 changes
    protected def goToItinerary(){
        at ItenaryBuilderPage
        clickOnGotoItenaryButton()
        sleep(5000)
    }
    //Follwoing method used for updating itinerary name in itinerary page.
    protected def itineraryNameUpdate(PaymentsTestData paymentsData) {
        at ItenaryPageItems
        scrollToTopOfThePage()
        clickEditIconNextToItineraryHeader()
        sleep(3000)
        waitTillLoadingIconDisappears()
        enterItenaryName(paymentsData.input.itineraryName)
        clickSaveButton()
        sleep(4000)
        waitTillLoadingIconDisappears()
    }
    protected def enterAagentRef(PaymentsTestData defaultData){
        at ItineraryTravllerDetailsPage
        sleep(1000)
       // resultData.hotel.itineraryPage.itineraryPageStatus = verifyAddAgentRefButtonStatus()
        clickOnAddAgentRefButton()
        waitTillLoadingIconDisappears()
        sleep(500)
        enterAgentRef(defaultData.input.agentRef)
        sleep(500)
        at ItineraryTravllerDetailsPage
        clickOnSaveOrCancelBtnsAgentRef(1)
        waitTillLoadingIconDisappears()
      //  resultData.hotel.itineraryPage.actualSavedAgentRefName=getSavedAgentRefName()
    }
    //Update agent reference details and traveler1,2 details
    protected def enterAgentRefAndTravelerDetails(PaymentsTestData defaultData,PaymentsTestResultData resultData){
        enterAagentRef(defaultData)
        at ItenaryPageItems
        enterTravellerDetails(defaultData.input.title_txt, defaultData.input.firstName,defaultData.input.lastName, defaultData.input.emailAddr,defaultData.input.telephone_Num, 0)
        addNumberTravellers(defaultData.input.title_txt, defaultData.input.travellerfirstName, defaultData.input.travellerlastName, 1)
        clickonSaveButton(0)
        waitTillLoadingIconDisappears()
        sleep(1000)
    }
    // combined all the above methods required steps as per TC 3
    protected def TCItineraryPage(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int index){
        at ItineraryTravllerDetailsPage
        sleep(4000)
        resultData.hotel.itineraryPage.itineraryPageStatus = verifyAddAgentRefButtonStatus()
        //update itienrary
        itineraryNameUpdate(paymentsData)
        //enter traveler details
        enterAgentRefAndTravelerDetails(defaultData,resultData)
        sleep(500)
        //store agent ref name
        resultData.hotel.itineraryPage.actualSavedAgentRefName=getSavedAgentRefName()
        //resultData.hotel.itineraryPage.itineraryName = getItenaryName()
        sleep(2000)
        resultData.hotel.itineraryPage.travellerDetails = getTravellers()
        sleep(1000)
        String itineraryId = getItineraryId()
        resultData.hotel.itineraryPage.itineraryID = itineraryId.split(" ").getAt(1)
        resultData.hotel.itineraryPage.itineraryName = itineraryId.split(" ").getAt(2)
        resultData.hotel.itineraryPage.expectedItineraryname=paymentsData.input.itineraryName
       // scrollToBottomOfThePage()
        clickOnItemCancllationLink(index)
        waitTillLoadingIconDisappears()
        sleep(1000)
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryPage.cancellationChargesLabel = getCancellationChargesLabelInOverlay()
        resultData.hotel.itineraryPage.cancellationCharges = getCancellationChargesInOverlay()
        try{
            resultData.hotel.itineraryPage.amendChargesLabel = getAmendChargesLabelInOverlay()
        }catch(Exception e){
            resultData.hotel.itineraryPage.amendChargesLabel = "Unable To Read From UI"
        }
        try{
            resultData.hotel.itineraryPage.amendCharges = getAmendChargesTxtInOverlay()
        }catch(Exception e){
            resultData.hotel.itineraryPage.amendCharges = "Unable To Read From UI"
        }

        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitTillLoadingIconDisappears()
        sleep(1000)
        resultData.hotel.itineraryPage.expectedCheckInNights = getCheckInDateNights(paymentsData.input.checkInDays.toString(), paymentsData.input.checkOutDays.toString())
    }

    protected def NonBookedItemSection(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int hotelCardIndx,int tranferCardIndx,int activityCardIndx){

        at ItineraryTravllerDetailsPage

        //NON booked items section should have 3 added items
        resultData.hotel.itineraryPage.actualNonBookedItemsCount=getNoOfItemsInNonBookedItems()

        //Hotel Card Details
        //nonbooked - 1
        //booked - 0
        //item order location -
        resultData.hotel.itineraryPage.hotelItem = storeItemDetails(hotelCardIndx,1,"hotel")
        scrollToTopOfThePage()
        //Transfer Card Details
        //resultData.transfer.itineraryPage.transferItem = storeItemDetails(tranferCardIndx,1,"transfers")
        resultData.transfer.itineraryPage.transferItem = storeItemCardDetails(tranferCardIndx,1,"transfers")
            //PAX
            resultData.transfer.itineraryPage.transferItem.expectedPax=paymentsData.input.pax+"PAX"

        //time
        resultData.transfer.itineraryPage.transferItem.actualItemDurationIconDisplayStatus=verifyItineryItemIconsDisplay("time",tranferCardIndx)
        resultData.transfer.itineraryPage.transferItem.actualTransferTimeSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(tranferCardIndx,0)
        //place cursor on top of icon, tooltip show:		Duration"
        clickTransfrItmIconsDispInNonBkdItem("time",tranferCardIndx)
        resultData.transfer.itineraryPage.transferItem.actualDurationIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("time",tranferCardIndx)

        //max luggage
        resultData.transfer.itineraryPage.transferItem.actualItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplay("luggage",tranferCardIndx)
        resultData.transfer.itineraryPage.transferItem.actualTransferMaxLuggageSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(tranferCardIndx,1)
        //place cursor on top of icon, tooltip show:		Luggage allowance"
        clickTransfrItmIconsDispInNonBkdItem("luggage",tranferCardIndx)
        resultData.transfer.itineraryPage.transferItem.actualLuggageIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("luggage",tranferCardIndx)

        //max number of persons allowed
        resultData.transfer.itineraryPage.transferItem.actualPaxIconDisplayStatus=verifyItineryItemIconsDisplay("adult",tranferCardIndx)
        resultData.transfer.itineraryPage.transferItem.actualTransfermaxPersonsSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(tranferCardIndx,2)
        //place cursor on top of icon, tooltip show:		Maximum passenger allowed"
        clickTransfrItmIconsDispInNonBkdItem("adult",tranferCardIndx)
        resultData.transfer.itineraryPage.transferItem.actualPaxIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("adult",tranferCardIndx)

        //child
        try{
            resultData.transfer.itineraryPage.transferItem.actualChildIconDisplayStatus=verifyItineryItemIconsDisplay("child",tranferCardIndx)
        }catch(Exception e){
            resultData.transfer.itineraryPage.transferItem.actualChildIconDisplayStatus="Unable To Read Child Icon display Status From UI"
        }
        try{
            resultData.transfer.itineraryPage.transferItem.actualChildTxt=getItenarySuggestedItemTimeMaxLuggPersons(tranferCardIndx,3)
        }catch(Exception e){
            resultData.transfer.itineraryPage.transferItem.actualChildTxt="Unable To Read Child Icon Text From UI"
        }
        try{
            //place cursor on top of icon, tooltip show:		Maximum passenger allowed"
            clickTransfrItmIconsDispInNonBkdItem("child",tranferCardIndx)
        }catch(Exception e){

        }
        try{
            resultData.transfer.itineraryPage.transferItem.actualChildIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("child",tranferCardIndx)
        }catch(Exception e){
            resultData.transfer.itineraryPage.transferItem.actualChildIconMouseHoverText="Unable To Read Child Mouse Hover Text From UI"
        }




        scrollToTopOfThePage()
        //Sightseeing Card Details
        resultData.activity.itineraryPage.sightSeeingItem = storeItemDetails(activityCardIndx,1,"activity")

        //time
        resultData.activity.itineraryPage.sightSeeingItem.actualItemDurationIconDisplayStatus=verifyItineryItemIconsDisplay("time",activityCardIndx)
        resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingTimeSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(activityCardIndx,0)
        //place cursor on top of icon, tooltip show:		Duration"
        clickTransfrItmIconsDispInNonBkdItem("time",activityCardIndx)
        resultData.activity.itineraryPage.sightSeeingItem.actualDurationIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("time",activityCardIndx)


        //Languae
        resultData.activity.itineraryPage.sightSeeingItem.actualItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplay("language",activityCardIndx)
        //resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt=getItenarySuggestedItemTimeMaxLuggPersons(activityCardIndx,1)
        String actualsightSeeingLangugeItemTxt=getItenarySuggestedItemTimeMaxLuggPersons(activityCardIndx,1)
        List<String> sightSeeingLangItemTxt=actualsightSeeingLangugeItemTxt.tokenize(",")
        resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt=sightSeeingLangItemTxt.sort().toString()
        //place cursor on top of icon, tooltip show:		Duration"
        clickTransfrItmIconsDispInNonBkdItem("language",activityCardIndx)
        resultData.activity.itineraryPage.sightSeeingItem.actualLanguageIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("language",activityCardIndx)


    }

    protected def bookAHotel(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int bookBtnindex){
        at ItineraryTravllerDetailsPage
        clickOnBookingIcon(bookBtnindex)

        waitTillLoadingIconDisappears()

        //You Are About To book screen
        resultData.hotel.itineraryPage.YouAreAbtToBookScrnTxt=getCancellationHeader()

    }

    protected def bookATransfer(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int bookBtnindex){
        at ItineraryTravllerDetailsPage
        scrollToTopOfThePage()
        clickOnBookingIcon(bookBtnindex)
        sleep(5000)
        waitTillLoadingIconDisappears()

        //You Are About To book screen
        resultData.transfer.itineraryPage.YouAreAbtToBookScrnTxt=getCancellationHeader()

    }

    protected def bookAnActivity(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int bookBtnindex){
        at ItineraryTravllerDetailsPage
        scrollToTopOfThePage()
        clickOnBookingIcon(bookBtnindex)
        sleep(5000)
        waitTillLoadingIconDisappears()

        //You Are About To book screen
        resultData.activity.itineraryPage.YouAreAbtToBookScrnTxt=getCancellationHeader()

    }

    protected def BookingTAndCoverlay(PaymentsTestData paymentsData,PaymentsTestResultData resultData,boolean payLater){

        at ItineraryTravllerDetailsPage
        //verify the details in booking T&C overlay  1. Hotel Name
        resultData.hotel.itineraryOverlay.hotelName =  getHotelNameInAboutToBookScrn()
        //Traveller name and check box

        int maxTravellers=paymentsData.input.adults.toInteger()+paymentsData.input.child.size()
        List expectedStatus=[]
        List actualStatus = []

        for(int i=0;i<maxTravellers;i++) {

            actualStatus.add(getTravellerCheckBoxCheckedStatus(i))
            sleep(3000)
            expectedStatus.add(paymentsData.expected.dispStatus)
        }
        resultData.hotel.itineraryOverlay.checkBoxStatus.put("actualItemStatus", actualStatus)
        resultData.hotel.itineraryOverlay.checkBoxStatus.put("expectedItemStatus", expectedStatus)

        //Traveller Name
        resultData.hotel.itineraryOverlay.travelerName = getTravelerNamesInAboutToBookScrn()
        //println("Traveler Name $resultData.hotel.itineraryOverlay.travelerName")
        resultData.hotel.itineraryOverlay.expectedTravelerName=paymentsData.expected.travelerDetails
        //click on Add Special remark or comment
        scrollToRemarksAndClick(1)
        sleep(3000)
        //user should be able to do so and section should expand
        resultData.hotel.itineraryOverlay.expandStatus = getExpandOrCollapseAddARemarkSectionStatus()
        //println("Expand Status After $resultData.hotel.itineraryOverlay.expandStatus")
        //click on check box next to early arrival from please not section and
        selectRemarks(paymentsData.expected.hotelPleaseNoteText)
        sleep(2000)
        //click on check box next to room with bathtub from If possible , please provide section
        selectRemarks(paymentsData.expected.hotelIfPossibleText)
        sleep(2000)
        //click on Add Special remark or comment  link
        scrollToRemarksAndClick(1)
        sleep(3000)
        //user should be able to do so and section should collapse
        resultData.hotel.itineraryOverlay.colapseStatus = getExpandOrCollapseAddARemarkSectionStatus()
        //println("collapse status status $resultData.hotel.itineraryOverlay.colapseStatus")
        //check for confirm booking and pay now button should be enabled
        resultData.hotel.itineraryOverlay.bookNowEnabledStatus = getConfirmBookingPayNowEnabled()
        //println("book book status $resultData.hotel.itineraryOverlay.bookNowEnabledStatus")

        if(payLater==true){
            resultData.hotel.itineraryOverlay.bookNowPayLaterBtnEnabledStatus=getConfirmBookingPayLaterStatus()
        }

    }
 protected def BookingTAndCoverlayTransfersMultiModule(PaymentsTestData paymentsData,PaymentsTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //verify the details in booking T&C overlay  1. Transfer Name
        resultData.transfer.itineraryOverlay.transferName =  getHotelNameInAboutToBookScrn()
        //Traveller name and check box

        int maxTravellers=paymentsData.input.adults.toInteger()+paymentsData.input.child.size()
        List expectedStatus=[]
        List actualStatus = []

        for(int i=0;i<maxTravellers;i++) {

            actualStatus.add(getTravellerCheckBoxCheckedStatus(i))
            sleep(3000)
            expectedStatus.add(paymentsData.expected.dispStatus)
        }
        resultData.transfer.itineraryOverlay.checkBoxStatus.put("actualItemStatus", actualStatus)
        resultData.transfer.itineraryOverlay.checkBoxStatus.put("expectedItemStatus", expectedStatus)

        //Traveller Name
        resultData.transfer.itineraryOverlay.travelerName = getTravelerNamesInAboutToBookScrn()
        //println("Traveler Name $resultData.hotel.itineraryOverlay.travelerName")
        resultData.transfer.itineraryOverlay.expectedTravelerName=paymentsData.expected.travelerDetails

        //Input Flight Number
        enterPickUpFlightNumber(paymentsData.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(paymentsData.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(paymentsData.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(paymentsData.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        //enterPickUpArrivalMins(transferData.timeOfArrival_mins)
        enterArrivalMins(paymentsData.expected.timeOfArrival_mins)
        sleep(2000)
        //should have header Drop off point details .
        resultData.transfer.itineraryOverlay.headerDropOffTxt=getTransferPickUpDropOffHeaderTxt(1)

        //Address
        resultData.transfer.itineraryOverlay.dropOffAddressTxt=getTransferDropOffAddressTxt().replaceAll(" ","").replaceAll("\n","").toUpperCase()
        resultData.transfer.itineraryOverlay.expecteddropOffAddressTxt=paymentsData.expected.transferHotelAddrTxt.replaceAll(" ","").toUpperCase()
        /*
        //click on Add Special remark or comment
        scrollToRemarksAndClick(1)
        sleep(3000)
        //user should be able to do so and section should expand
        resultData.hotel.itineraryOverlay.expandStatus = getExpandOrCollapseAddARemarkSectionStatus()
        //println("Expand Status After $resultData.hotel.itineraryOverlay.expandStatus")
        //click on check box next to early arrival from please not section and
        selectRemarks(paymentsData.expected.hotelPleaseNoteText)
        sleep(2000)
        //click on check box next to room with bathtub from If possible , please provide section
        selectRemarks(paymentsData.expected.hotelIfPossibleText)
        sleep(2000)
        //click on Add Special remark or comment  link
        scrollToRemarksAndClick(1)
        sleep(3000)
        //user should be able to do so and section should collapse
        resultData.hotel.itineraryOverlay.colapseStatus = getExpandOrCollapseAddARemarkSectionStatus()
        //println("collapse status status $resultData.hotel.itineraryOverlay.colapseStatus")
        //check for confirm booking and pay now button should be enabled
        resultData.hotel.itineraryOverlay.bookNowEnabledStatus = getConfirmBookingPayNowEnabled()
        //println("book book status $resultData.hotel.itineraryOverlay.bookNowEnabledStatus")
        */

    }

    protected def BookingTAndCoverlaySightSeeingMultiModule(PaymentsTestData paymentsData,PaymentsTestResultData resultData){

        at ItineraryTravllerDetailsPage
        //verify the details in booking T&C overlay  1. Activity Name
        resultData.activity.itineraryOverlay.sightSeeingName =  getHotelNameInAboutToBookScrn()
        //Traveller name and check box

        int maxTravellers=paymentsData.input.adults.toInteger()+paymentsData.input.child.size()
        List expectedStatus=[]
        List actualStatus = []

        for(int i=0;i<maxTravellers;i++) {

            actualStatus.add(getTravellerCheckBoxCheckedStatus(i))
            sleep(3000)
            expectedStatus.add(paymentsData.expected.dispStatus)
        }
        resultData.activity.itineraryOverlay.checkBoxStatus.put("actualItemStatus", actualStatus)
        resultData.activity.itineraryOverlay.checkBoxStatus.put("expectedItemStatus", expectedStatus)

        //Traveller Name
        resultData.activity.itineraryOverlay.travelerName = getTravelerNamesInAboutToBookScrn()
        //println("Traveler Name $resultData.hotel.itineraryOverlay.travelerName")
        resultData.activity.itineraryOverlay.expectedTravelerName=paymentsData.expected.travelerDetails

        //Please select language section should be dispalyed and
        //resultData.activity.itineraryOverlay.langSectHeaderTxt=getLanguageHeaderSectionTxt()
        // lauguage tab should have english by default
        //resultData.activity.itineraryOverlay.defaultLangSelectedTxt=getDefualtSelectedLanguage()
        resultData.activity.itineraryOverlay.LangTxt=getLanguageOrDepartPointOrTimeText(0)
        //Departure point should eb displayed and

        // should have section details: Gala hotel and

        // time  tab should have 080:00 by default.
        //resultData.activity.itineraryOverlay.timeTxt=getActivityTimeTxtInAbouttoBookScreen()
        resultData.activity.itineraryOverlay.timeTxt=getActivityTimeSelectedTxtInAbouttoBookScreen()

    }
    //store non-booked item details and then click on book item
    protected def bookItem(PaymentsTestResultData resultData,int index,int itemType){
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryPage.hotelItem = storeItemDetails(index,itemType,"hotel")
        resultData.hotel.itineraryPage.cancellationLinkStatus = getItinenaryFreeCnclTxtInSuggestedItem(index)
        sleep(1000)
        clickOnBookIcon()
        sleep(1000)
        waitForAjaxIconToDisappear()
        resultData.hotel.itineraryPage.overlayStatus = overlayCloseButton()
        // resultData.hotel.itineraryPage.lightBox = getTravellerCannotBeDeletedPopupDisplayStatus()

    }
    protected def getTravelerCheckboxStatus(PaymentsTestData paymentsData){
        def list = []
        int maxTravellers
        if(paymentsData.input.infant==true){
            maxTravellers=paymentsData.input.adults+(paymentsData.input.child.size()-1)
        }else{
            maxTravellers=paymentsData.input.adults.toString().toInteger()+paymentsData.input.child.size()
        }
        //Selecting all travellers
        for(int i=0;i<maxTravellers;i++) {
            list.add(getTravellerCheckBoxCheckedStatus(i))
           /* if(getTravellerCheckBoxCheckedStatus(i)==false)
            {
                clickOnTravellerCheckBox(i)
                sleep(2000)
            }*/
        }
        return list
    }
    protected def clickTravelerIfCheckboxNotSelectedByDefault(PaymentsTestData paymentsData){
        List list = getTravelerCheckboxStatus(paymentsData)
        for(int i=0;i<list.size();i++){
            if(list[i].equals(false)){
                clickOnTravellerCheckBox(i)
                sleep(2000)
            }
        }
    }
    protected def bookTCOverlay(PaymentsTestData data, PaymentsTestResultData resultData){
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryOverlay.hotelName =  getHotelNameInAboutToBookScrn()
        //Traveller name and check box
        resultData.hotel.itineraryOverlay.checkBoxCheckedStatus = getTravelerCheckboxStatus(data)
        resultData.hotel.itineraryOverlay.travelerName = getTravelerNamesInAboutToBookScrn()
       // scrollToRemarksAndClick(1)
        clickOnRemarks(0)
        resultData.hotel.itineraryOverlay.expandStatus = getExpandOrCollapseItemDisplayStatus(0)
        selectRemarks(data.input.remarks)
        selectRemarks(data.input.note)
        clickOnRemarks(0)
        waitTillLoadingIconDisappears()
        sleep(1000)
        resultData.hotel.itineraryOverlay.colapseStatus = getExpandOrCollapseItemDisplayStatus(0)
        resultData.hotel.itineraryOverlay.bookNowEnabledStatus = getConfirmBookingPayNowEnabled()
          int  size =   bookedItemsSize()
        this.size = size
        //can be expanded if rooms are more by storing details in temp map and return to another map
        if(bookedItemsSize()>1){
            resultData.hotel.itineraryOverlay.hotelName2 = getHotelNameInAboutToBookScrn()
           // scrollToRemarksAndClick(2)
            clickOnRemarks(1)
            sleep(1000)
            resultData.hotel.itineraryOverlay.expandStatus2 = getExpandOrCollapseItemDisplayStatus(1)
            selectRemarks(data.input.remarks)
            selectRemarks(data.input.note)
            clickOnRemarks(1)
            waitTillLoadingIconDisappears()
            sleep(1000)
            resultData.hotel.itineraryOverlay.colapseStatus2 = getExpandOrCollapseItemDisplayStatus(1)
        }

    }
    //Click on confirm booking in T&C Overlay and navigates to payment page.
    protected def paymentPage_payNow(PaymentsTestData defaultData,PaymentsTestResultData resultData){
        at ItineraryTravllerDetailsPage
        clickConfirmBookingPayNow()
        waitTillLoadingIconDisappears()
        at PaymentsPage
        sleep(5000)
        /*resultData.hotel.paymentpage.okOverlayStatus = getClickOkStatus()
        clickOnOk()*/
        resultData.hotel.paymentpage.paymentDispStatus = getPaymentPageDispStatus()
        resultData.hotel.paymentpage.logoStatus = getTravelerCubeLogoDispStatus()
        resultData.hotel.paymentpage.mainMenuLinks = getHeaderMainMenuLinks(defaultData.expected.mainMenuLinks)
        resultData.hotel.paymentpage.miniLinks = getMiniMenuLinksStatus(defaultData.expected.miniLinks)
    }
    //click on confrimBooking paylater button in T&c Overlay and storing details after navigating itienrary page.
    protected def payLater(PaymentsTestResultData resultData,int index){
        at ItineraryTravllerDetailsPage
        clickConfirmBookingPayLaterAgentPos()
        waitTillLoadingIconDisappears()
        sleep(2000)
        resultData.hotel.confirmBookingOverlay.header = getCancellationHeader()
        waitForAjaxIconToDisappear()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitTillLoadingIconDisappears()
        sleep(1000)
        returnToItinerary(resultData)
        if(getBookingStatus(index).equals("Pending")) {
            driver.navigate().refresh()
            sleep(3000)
            waitForAjaxIconToDisappear()
        }
        resultData.hotel.itineraryPage.payLaterPaymentStatus = paymentStatus(index)
        resultData.hotel.itineraryPage.payButtonStatus = getPayButtonStatusInItinerary(index)

    }
    //Store payment card details of 1st row in payment summery block
    protected def paymentCardDetails(int index){
        def dataMap = [:]
        dataMap.bookingIdStatus = getItemBookingIdfromPaymentSummaryStatus(index)
        dataMap.date = getItemBookingDatefromPaymentSummary(index)
        dataMap.name = getItemNamefromPaymentSummary(index)
        dataMap.pax = getItemPaxfromPaymentSummary(index)
        dataMap.price = getItemPricefromPaymentSummary(index)
        dataMap
    }
    //store paymentSummeryBlock details
    protected def paymentSummeryBlock(PaymentsTestResultData resultData,int row,int col){
        resultData.hotel.paymentpage.paymentSummeryLabel = verifyPaymentSummaryLabel()
        resultData.hotel.paymentpage.paymentSummeryLables = getPaymentSummeryLabels()
        resultData.hotel.paymentpage.cardDetails = paymentCardDetails(0)
        resultData.hotel.paymentpage.totalGrass = getTotalGross()
        resultData.hotel.paymentpage.totalNet =  getTotalNet()

        resultData.hotel.paymentpage.totalPay = getGrossORNetORTotalPay(row,col)
    }

    protected def paymentPagePayLater(PaymentsTestResultData resultData,int index){

        at ItineraryTravllerDetailsPage

        //click on confirm booking and Pay later
        clickConfirmBookingPayLaterAgentPos()
        sleep(2000)
        waitTillLoadingIconDisappears()

        bookingConfirmedOverlay(resultData,index)

    }
    protected def paymentPagePayLaterTransfers(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int index){

        at ItineraryTravllerDetailsPage

        //click on confirm booking and Pay later
        clickConfirmBookingPayLaterAgentPos()
        sleep(2000)
        waitTillLoadingIconDisappears()

        bookingConfirmedOverlayTransfers(paymentsData,resultData,index)

    }
    protected def paymentPagePayLaterActivity(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int index){

        at ItineraryTravllerDetailsPage

        //click on confirm booking and Pay later
        clickConfirmBookingPayLaterAgentPos()
        sleep(2000)
        waitTillLoadingIconDisappears()

        bookingConfirmedOverlayActivity(paymentsData,resultData,index)

    }

    protected def bookedItemSectionAfterPayLater(ClientData clientData,PaymentsTestResultData resultData,int itemNum, int index){

        //All Details after booking item
        ItineraryPageAfterPaymentHotelMultiModule(clientData,resultData,itemNum,index)

        at ItineraryTravllerDetailsPage

        String itemStatus=getBookingStatus(itemNum)
        if(itemStatus.equals("Pending")) {
            driver.navigate().refresh()
            sleep(3000)
            waitForAjaxIconToDisappear()
        }

        String paymentStatus=getPaymentNotTakenTxt(index)
        if(paymentStatus.contains("Payment Due")) {
            driver.navigate().refresh()
            sleep(3000)
            waitForAjaxIconToDisappear()
        }
        //Check for payment section
        //payment not taken text should be displayed .
        resultData.hotel.itineraryPage.bookedItemPaymentNotTakenTxt=getPaymentNotTakenTxt(index)
        // Pay button should be enabled .
        resultData.hotel.itineraryPage.bookedItemPayButtonEnabledStatus=getPayBtnEnabledStatus(index)

        //Download Voucher Icon should not be displayed
        resultData.hotel.itineraryPage.bookedItemDownloadVoucherIconDispStatus=getDownloadVocherIconDisplayStatus(index)

    }

    protected def paymentpagebyclickingonpay(PaymentsTestData defaultData,PaymentsTestResultData resultData,int itemIndex){
        at ItineraryTravllerDetailsPage
        clickPayButtonInItinerary(itemIndex)
        waitTillLoadingIconDisappears()
        at PaymentsPage
        sleep(5000)
        //resultData.hotel.paymentpage.okOverlayStatus = getClickOkStatus()
        //clickOnOk()
        resultData.hotel.paymentpage.paymentDispStatus = getPaymentPageDispStatus()
        resultData.hotel.paymentpage.logoStatus = getTravelerCubeLogoDispStatus()
        resultData.hotel.paymentpage.mainMenuLinks = getHeaderMainMenuLinks(defaultData.expected.mainMenuLinks)
        resultData.hotel.paymentpage.miniLinks = getMiniMenuLinksStatus(defaultData.expected.miniLinks)
    }



    protected def paymentSummaryBlockExtended(PaymentsTestResultData resultData,boolean payLater){

        at PaymentsPage

        //Payment Summary Block Text
        resultData.hotel.paymentpage.paymentSummaryBlockTxt=getPaymentSummaryBlockTxt()

        //Transfer Display in Payment Summary
        boolean transfersisExist=false
        if(resultData.hotel.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.transfer.searchResults.transferItem.transferName)){
            transfersisExist=true
        }
        resultData.hotel.paymentpage.transferExistence=transfersisExist

        //Activity Display in Payment Summary
        boolean SightseeingisExist=false
        if(resultData.hotel.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.activity.searchResults.sightSeeingItem.sightSeeingName)){
            SightseeingisExist=true
        }
        resultData.hotel.paymentpage.sightSeeingExistence=SightseeingisExist

        if(payLater==true){
            //Booking ID text
            resultData.hotel.paymentpage.bookingIdText=getItemBookingIdfromPaymentSummary(0)
        }


        //Hotel Display in Payment Summary
        /*boolean hotelIsExist=false
        if(resultData.hotel.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.hotel.searchResults.hotelItem.hotelName)){
            hotelIsExist=true
        }
        resultData.hotel.paymentpage.hotelExistence=hotelIsExist */

    }

    protected def paymentSummeryBlockTransfers(PaymentsTestResultData resultData,int row,int col){

        at PaymentsPage

        resultData.transfer.paymentpage.paymentSummeryLabel = verifyPaymentSummaryLabel()
        resultData.transfer.paymentpage.paymentSummeryLables = getPaymentSummeryLabels()
        resultData.transfer.paymentpage.cardDetails = paymentCardDetails(0)
        resultData.transfer.paymentpage.totalGrass = getTotalGross()
        resultData.transfer.paymentpage.totalNet =  getTotalNet()

        resultData.transfer.paymentpage.totalPay = getGrossORNetORTotalPay(row,col)

        //Payment Summary Block Text
        resultData.transfer.paymentpage.paymentSummaryBlockTxt=getPaymentSummaryBlockTxt()

        //Transfer Display in Payment Summary
        boolean transfersisExist=false
        if(resultData.transfer.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.transfer.searchResults.transferItem.transferName)){
            transfersisExist=true
        }
        resultData.transfer.paymentpage.transferExistence=transfersisExist

        //Activity Display in Payment Summary
        boolean SightseeingisExist=false
        if(resultData.transfer.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.activity.searchResults.sightSeeingItem.sightSeeingName)){
            SightseeingisExist=true
        }
        resultData.transfer.paymentpage.sightSeeingExistence=SightseeingisExist

        //Hotel Display in Payment Summary
        boolean hotelIsExist=false
        if(resultData.transfer.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.hotel.searchResults.hotelItem.hotelName)){
            hotelIsExist=true
        }
        resultData.transfer.paymentpage.hotelExistence=hotelIsExist

        clickTotalToPayInformationIconInPaymentSummary()
        //Information icon should be dispalyed and tool tip should be dispalyed with text "Although we are unable to display the net amount due ,you will only be charged The net amount"
        resultData.transfer.paymentpage.totalToPayInformationIconTxt=getTotalToPayInfoIconToolTipInPaymtSummary()
        sleep(2000)
    }

    protected def paymentSummeryBlockActivity(PaymentsTestResultData resultData,int row,int col){

        at PaymentsPage

        resultData.activity.paymentpage.paymentSummeryLabel = verifyPaymentSummaryLabel()
        resultData.activity.paymentpage.paymentSummeryLables = getPaymentSummeryLabels()
        resultData.activity.paymentpage.cardDetails = paymentCardDetails(0)
        resultData.activity.paymentpage.totalGrass = getTotalGross()
        resultData.activity.paymentpage.totalNet =  getTotalNet()

        resultData.activity.paymentpage.totalPay = getGrossORNetORTotalPay(row,col)

        //Payment Summary Block Text
        resultData.activity.paymentpage.paymentSummaryBlockTxt=getPaymentSummaryBlockTxt()

        //Transfer Display in Payment Summary
        boolean transfersisExist=false
        if(resultData.activity.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.transfer.searchResults.transferItem.transferName)){
            transfersisExist=true
        }
        resultData.activity.paymentpage.transferExistence=transfersisExist

        //Activity Display in Payment Summary
        boolean SightseeingisExist=false
        if(resultData.activity.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.activity.searchResults.sightSeeingItem.sightSeeingName)){
            SightseeingisExist=true
        }
        resultData.activity.paymentpage.sightSeeingExistence=SightseeingisExist

        //Hotel Display in Payment Summary
        boolean hotelIsExist=false
        if(resultData.activity.paymentpage.paymentSummaryBlockTxt.toString().contains(resultData.hotel.searchResults.hotelItem.hotelName)){
            hotelIsExist=true
        }
        resultData.activity.paymentpage.hotelExistence=hotelIsExist


    }
    // store payment type details like static headers in payment page.
    protected def selectPaymentType(PaymentsTestResultData resultData){
        resultData.hotel.paymentpage.securePayHeader =  getSecurePaymentHeaderTxt()
        resultData.hotel.paymentpage.securePaySubHeader =  getSecurePaymentSubHeaderTxt()
        resultData.hotel.paymentpage.cardOptionsStatus = getPaymentCardOptionsStatus()
        resultData.hotel.paymentpage.buttonStatus = isMakePaymentNowEnabled()
    }
    //enter card details and store label values
    protected def cardDetails(String paymentType,String cardNum,String startDate,String startYear,String expireDate,String expireYr,String cardHolder,String securityCode,PaymentsTestResultData resultData,int index){
        SelectPaymentType(paymentType)
        at ItineraryTravllerDetailsPage
        waitTillLoadingIconDisappears()
        at PaymentsPage
        resultData.hotel.paymentpage.cardLabels = getCardLabels()
        resultData.hotel.paymentpage.priceInPaymentBlock = getPriceInPaymentPage(0)
        resultData.hotel.paymentpage.indicatesStaticTxt = getIndicatesStaticTxt()
        resultData.hotel.paymentpage.cardImg = getCardImageStatus(index)
        //Follwoing method updated as per new requirement in payment page.
        enterCardDetails(paymentType,cardNum,expireDate,expireYr,cardHolder,securityCode)
        resultData.hotel.paymentpage.enteredFieldsValues = getEnteredTextFieldsTxt()
        //select dropdown are only 2 present in new UI
       // resultData.hotel.paymentpage.startDateSelectTag = getSelectSize(0)
       // resultData.hotel.paymentpage.monthSelectTag = getSelectSize(1)
        resultData.hotel.paymentpage.expireMonthSelectSize =  getSelectSize(0)
        resultData.hotel.paymentpage.expireYearSelectSize =  getSelectSize(1)

        //resultData.hotel.paymentpage.selectOptionDefaultValue = getSelectDefaultValue(4)
       // resultData.hotel.paymentpage.selectMonthDefaultValue = getSelectDefaultValue(5)

        resultData.hotel.paymentpage.expireMonthSelectDefaultValue = getSelectDefaultValue(0)
        resultData.hotel.paymentpage.expireYearSelectDefaultValue = getSelectDefaultValue(1)
        //start date - select option years size
        //resultData.hotel.paymentpage.dateYearOptionsSize = getDateYearOptionsSize()
        //start date select options please -select to Dec month
       // resultData.hotel.paymentpage.startDateOptions = getDateMonthOptions()
        resultData.hotel.paymentpage.expireDateOptions = getMonthOptions()
        //Expire month - select option year size
        resultData.hotel.paymentpage.expireDateYrOptionsSize = getYearOptionsSize()
    }

    protected def cardDetailsTransfers(String paymentType,String cardNum,String startDate,String startYear,String expireDate,String expireYr,String cardHolder,String securityCode,PaymentsTestResultData resultData,int index){
        SelectPaymentType(paymentType)
        at ItineraryTravllerDetailsPage
        waitForAjaxIconToDisappear()
        at PaymentsPage
        resultData.transfer.paymentpage.cardLabels = getCardLabels()
        resultData.transfer.paymentpage.cardImg = getCardImageStatus(index)
        enterCardDetails(paymentType,cardNum,expireDate,expireYr,cardHolder,securityCode)

        resultData.transfer.paymentpage.enteredFieldsValues = getEnteredTextFieldsTxt()

        resultData.transfer.paymentpage.expireMonthSelectSize =  getSelectSize(0)
        resultData.transfer.paymentpage.expireYearSelectSize =  getSelectSize(1)

        resultData.transfer.paymentpage.expireMonthSelectDefaultValue = getSelectDefaultValue(0)
        resultData.transfer.paymentpage.expireYearSelectDefaultValue = getSelectDefaultValue(1)

        resultData.transfer.paymentpage.expireDateOptions = getMonthOptions()
        //Expire month - select option year size
        resultData.transfer.paymentpage.expireDateYrOptionsSize = getYearOptionsSize()

        //Total  To Pay
        resultData.transfer.paymentpage.totalToPayAmountAndCurncy=getTotalToPayInPaymentSummaryPage()

    }

    protected def cardDetailsActivity(String paymentType,String cardNum,String startDate,String startYear,String expireDate,String expireYr,String cardHolder,String securityCode,PaymentsTestResultData resultData,int index){
        SelectPaymentType(paymentType)
        at ItineraryTravllerDetailsPage
        waitForAjaxIconToDisappear()
        at PaymentsPage
        resultData.activity.paymentpage.cardLabels = getCardLabels()
        resultData.activity.paymentpage.cardImg = getCardImageStatus(index)
        enterCardDetails(paymentType,cardNum,expireDate,expireYr,cardHolder,securityCode)
        resultData.activity.paymentpage.enteredFieldsValues = getEnteredTextFieldsTxt()
        resultData.activity.paymentpage.startDateSelectTag = getSelectSize(4)
        resultData.activity.paymentpage.monthSelectTag = getSelectSize(5)
        resultData.activity.paymentpage.selectOptionDefaultValue = getSelectDefaultValue(4)
        resultData.activity.paymentpage.selectMonthDefaultValue = getSelectDefaultValue(5)
        resultData.activity.paymentpage.dateYearOptionsSize = getDateYearOptionsSize()
        resultData.activity.paymentpage.startDateOptions = getDateMonthOptions()
        resultData.activity.paymentpage.expireDateOptions = getMonthOptions()
        resultData.activity.paymentpage.expireDateYrOptionsSize = getYearOptionsSize()
    }


    protected def savedAddress(PaymentsTestResultData resultData){

        at PaymentsPage

        //saved address txt
        resultData.hotel.paymentpage.savedAddressTxtdispStatus=getUseSavedAddressTxtDispStatus()

        //Radio button display status
        resultData.hotel.paymentpage.savedAddressRadioBtnDispStatus=getUseSavedAddressRadioBtnDispStatus()

        //select radio button
        selectFirstSavedAddressRadioBtn()
    }

    protected def addNewAddress(PaymentsTestResultData resultData,int num){
        at PaymentsPage
        sleep(1000)
       scrollToBottomOfThePage()
        resultData.hotel.paymentpage.addrStatus = verifyIfAddressExists()
        resultData.hotel.paymentpage.radioButtonStatus = getAddrCheckboxStatus(num)
        SelectAddress(defaultData.input.addrType)
        waitTillLoadingIconDisappears()
        sleep(5000)
        resultData.hotel.paymentpage.billingAddrSectionStatus = verifyPaymentSection(0)
        sleep(1000)
        resultData.hotel.paymentpage.addrLabels = getAddrLabels()
        enterBillingAddress(defaultData.expected.Addr1.get(0).toString(),defaultData.expected.Addr1.get(1).toString(),defaultData.expected.Addr1.get(2).toString(),defaultData.expected.Addr1.get(3).toString(),defaultData.expected.Addr1.getAt(4).toString(),defaultData.expected.Addr1.get(5).toString(),defaultData.expected.Addr1.get(6).toString(),defaultData.expected.Addr1.get(7).toString())
        resultData.hotel.paymentpage.txtFieldValues = getAddrTxtFieldValues()
    }

    protected def returnToItinerary(PaymentsTestResultData resultData){
        resultData.hotel.paymentpage.returnItineraryStatus = getReturnToItineraryPageStatus()
    }
    //CLick on make payment button and validation of  confirm overlay
    protected def makePaymentButton(PaymentsTestResultData resultData){
        resultData.hotel.paymentpage.paymentStatus = isMakePaymentNowEnabled()
        at PaymentsPage

        clickOnMakePaymentNow()
        waitTillLoadingIconDisappears()
        boolean status = waitTillErrorMsgDisplays()
        if(status){
            Assert.fail("Error msg displayed, could not make payment.")
        }
        at ItineraryTravllerDetailsPage
        waitUntillConfrimOverlayAppears()
        waitForAjaxIconToDisappear()
        waitTillLoadingIconDisappears()
    }

    protected def bookingConfirmedOverlay(PaymentsTestResultData resultData,int index){
        at ItineraryTravllerDetailsPage
        sleep(1000)
        resultData.hotel.confirmBookingOverlay.header = getCancellationHeader()
        resultData.hotel.confirmBookingOverlay.logo = getBrandAndIconDisplayStatus()
        resultData.hotel.confirmBookingOverlay.itineraryID = getItinearyID(index)
        resultData.hotel.confirmBookingOverlay.bookingID = getBookingIdFromBookingConfirmed(index)
        resultData.hotel.confirmBookingOverlay.bookingIDDisplayed = verifyBookingIdDisplayedInBookingConfirmScrn(index)
        resultData.hotel.confirmBookingOverlay.travelers = getLeadTravellerDetailsConfirmationDialog()
        resultData.hotel.confirmBookingOverlay.depatureDate = getBookingDepartDate()
        resultData.hotel.confirmBookingOverlay.hotelName = getHotelNameInBookingConfScrn(index+1)
        sleep(500)
        resultData.hotel.confirmBookingOverlay.addr = getHotelAddressInBookingConfScrn(index+1)
        resultData.hotel.confirmBookingOverlay.paxDesc = getPaxDescInConfirmScreen(index)
        resultData.hotel.confirmBookingOverlay.date = getCheckInDateNumOfNightsBookingConfirmed()
        resultData.hotel.confirmBookingOverlay.price = getSubTotalsInBookingConfScreen(index+1)
        resultData.hotel.confirmBookingOverlay.commission = gettotalCommissionBookingConfirmScrn()
        if(getBookingConfirmationItemsCount()>1){
            resultData.hotel.confirmBookingOverlay.hotelName2 = getHotelNameInBookingConfScrn(2)
            sleep(500)
            resultData.hotel.confirmBookingOverlay.addr2 = getHotelAddressInBookingConfScrn(2)
            resultData.hotel.confirmBookingOverlay.paxDesc2 = getPaxDescInConfirmScreen(1)
            resultData.hotel.confirmBookingOverlay.date2 = getDurationInBookingConfScrn(2)
            resultData.hotel.confirmBookingOverlay.price2 = getSubTotalsInBookingConfScreen(2)
            //resultData.hotel.confirmBookingOverlay.commission2 = gettotalCommissionBookingConfirmScrn()
        }
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitTillLoadingIconDisappears()
        sleep(1000)
    }

    protected def bookingConfirmedOverlayTransfers(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int index){
        at ItineraryTravllerDetailsPage
        sleep(500)
        //Header-booking confirmed overlay should be displayed
        resultData.transfer.confirmBookingOverlay.header = getCancellationHeader()
        //An update of your Itinerary will be sent to you at your registered email address text should be displayed
        resultData.transfer.confirmBookingOverlay.subheaderTxt=getHeaderSectionInBookingConfirmedScrn()
        resultData.transfer.confirmBookingOverlay.logo = getBrandAndIconDisplayStatus()
        resultData.transfer.confirmBookingOverlay.itineraryID = getItinearyID(index)
        resultData.transfer.confirmBookingOverlay.bookingID = getBookingIdFromBookingConfirmed(index)
        resultData.transfer.confirmBookingOverlay.bookingIDDisplayed = verifyBookingIdDisplayedInBookingConfirmScrn(index)
        resultData.transfer.confirmBookingOverlay.travelers = getLeadTravellerDetailsConfirmationDialog()
        resultData.transfer.confirmBookingOverlay.depatureDate = getBookingDepartDate()
        resultData.transfer.confirmBookingOverlay.transferName = getHotelNameInBookingConfScrn(index+1)
        sleep(500)

        //Transfer Description & Pax
        resultData.transfer.confirmBookingOverlay.expectedTransferDescBookingConf_txt=paymentsData.expected.transferDesctxt+",  "+(paymentsData.input.pax.toInteger())+" PAX"
        resultData.transfer.confirmBookingOverlay.actualTransferDescBookingConf_txt=getTransferDescBookingConfirmed()

        //pickup
        resultData.transfer.confirmBookingOverlay.expectedPickupATH = "Pick up: "+"Flight Number:"+" "+paymentsData.expected.flightNumber+". "+"Estimated time of arrival:"+" "+paymentsData.expected.timeOfArrival_Hrs+":"+paymentsData.expected.timeOfArrival_mins
        resultData.transfer.confirmBookingOverlay.actualPickupATH=getPickUpDetailsOnBookingConfirmation().replaceAll(" ","").toUpperCase()
        resultData.transfer.confirmBookingOverlay.expectedPickupATH =resultData.transfer.confirmBookingOverlay.expectedPickupATH.replaceAll(" ","").toUpperCase()
        // dropoff
        resultData.transfer.confirmBookingOverlay.actualDropOffATH=getDropOffDetailsOnBookingConfirmation().replaceAll(" ","").replaceAll(",","").toUpperCase()
        resultData.transfer.confirmBookingOverlay.expectedDropOffATH="Drop off:"+paymentsData.expected.transferHotelAddrTxt
        resultData.transfer.confirmBookingOverlay.expectedDropOffATH=resultData.transfer.confirmBookingOverlay.expectedDropOffATH.replaceAll(" ","").toUpperCase()

        //Transfer Service Date and time
        resultData.transfer.confirmBookingOverlay.actualTransferDateAndTimeInBookingConf=getTransferDateAndTimeInBookingConfirmed()

        //Transfer Price
        resultData.transfer.confirmBookingOverlay.price = getSubTotalsInBookingConfScreen(index+1)
        //Commission
        resultData.transfer.confirmBookingOverlay.commission = gettotalCommissionBookingConfirmScrn()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitTillLoadingIconDisappears()
        sleep(1000)
    }

    protected def bookingConfirmedOverlayActivity(PaymentsTestData paymentsData,PaymentsTestResultData resultData,int index){
        at ItineraryTravllerDetailsPage
        sleep(500)
        //Header-booking confirmed overlay should be displayed
        resultData.activity.confirmBookingOverlay.header = getCancellationHeader()
        //An update of your Itinerary will be sent to you at your registered email address text should be displayed
        resultData.activity.confirmBookingOverlay.subheaderTxt=getHeaderSectionInBookingConfirmedScrn()
        //travelcube icon
        resultData.activity.confirmBookingOverlay.logo = getBrandAndIconDisplayStatus()
        //Itinerary number and name
        resultData.activity.confirmBookingOverlay.itineraryID = getItinearyID(index)
        //booking id text and number
        resultData.activity.confirmBookingOverlay.bookingID = getBookingIdFromBookingConfirmed(index)
        resultData.activity.confirmBookingOverlay.bookingIDDisplayed = verifyBookingIdDisplayedInBookingConfirmScrn(index)
        //Traveller Details
        resultData.activity.confirmBookingOverlay.travelers = getLeadTravellerDetailsConfirmationDialog()
        //Departure date
        resultData.activity.confirmBookingOverlay.depatureDate = getBookingDepartDate()
        //Activity details
        //Activity Name should be displayed
        resultData.activity.confirmBookingOverlay.activityName = getHotelNameInBookingConfScrn(index+1)
        sleep(500)
        //Should display Duration ,
        resultData.activity.confirmBookingOverlay.activityDuration=getDurationPaxinActivityConfirmScreen(0)
        // number of Pax ,
        resultData.activity.confirmBookingOverlay.activityPax=getDurationPaxinActivityConfirmScreen(1).replaceAll(" ","")
        resultData.activity.confirmBookingOverlay.expectedactivityPax=paymentsData.input.activityPax+"PAX"
        // Departure Point : Gala Hotel  same enterd in Booking T & C overlay .
        resultData.activity.confirmBookingOverlay.activityDeparturePointTxt=getActivityDeparturePointDateTimeBookingConfirmed(0)

        //Date & Time
        resultData.activity.confirmBookingOverlay.activityDateAndTimeTxt=getActivityDeparturePointDateTimeBookingConfirmed(1).replaceAll(" ","")
        resultData.activity.confirmBookingOverlay.expectedactivityDateAndTimeTxt=resultData.activity.confirmBookingOverlay.depatureDate+","+resultData.activity.itineraryOverlay.timeTxt

        //Total Amount
        resultData.activity.confirmBookingOverlay.price = getSubTotalsInBookingConfScreen(index+1)
        //Commission
        resultData.activity.confirmBookingOverlay.commission = gettotalCommissionBookingConfirmScrn()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitTillLoadingIconDisappears()
        sleep(1000)
    }

    protected def storeItineraryPageItemDetails(int itemNum, int index){
        Map dataMap = [:]
       dataMap.itemSectionAmendStatus = getTabTxtInBookedItemsSection(itemNum,index)
        dataMap.itemCancelStatus = getTabTxtInBookedItemsSection(itemNum,index+1)
        dataMap.travellers = getTravelerNamesMicros(index)
        String travellers = dataMap.travellers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        dataMap.traveler1 = travellerSplit.getAt(0)
        dataMap.traveler2 = travellerSplit.getAt(1)
        return dataMap
    }


    protected def ItineraryPageAfterPayment(PaymentsTestResultData resultData,int itemNum, int index){
        at ItineraryTravllerDetailsPage
        sleep(1000)
        resultData.hotel.itineraryPage.bookedItemHeader =  getSuggestedItemsHeaderText()
        resultData.hotel.itineraryPage.bookedItem =  storeItemDetails(index,itemNum,"hotel")
        resultData.hotel.itineraryPage.item1Details = storeItineraryPageItemDetails(itemNum,index)
        clickOnItemCancllationLink(index)
        sleep(700)
        resultData.hotel.itineraryPage.cancellationChargesLabel1 = getCancellationChargesLabelInOverlay()
        resultData.hotel.itineraryPage.cancellationCharges1 = getCancellationChargesInOverlay()
        resultData.hotel.itineraryPage.amendChargesLabel1 = getAmendChargesLabelInOverlay()
        resultData.hotel.itineraryPage.amendCharges1 = getAmendChargesTxtInOverlay()
        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }

    protected def ItineraryPageAfterPaymentHotelMultiModule(ClientData clientData,PaymentsTestResultData resultData,int itemNum, int index){
        //itemNum - Item Card Index
        //index - Inside card different items index

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.hotel.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(1)
        //Booked Item section - should have only hotel in the section
            //count the items in Booked Item section
        resultData.hotel.itineraryPage.bookedItemsCount=getNoOfItemsInBookedItemsSection()
            //only hotel in the section
        resultData.hotel.itineraryPage.bookedItemItemName=getItemNameInBookedItemsSection(0)
        //booking Id text and number should be displayed
        resultData.hotel.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(1)
        //confirmed Status
        resultData.hotel.itineraryPage.itemConfirmedStatus=getBookingStatus(0)
            //Amend button text
        resultData.hotel.itineraryPage.itemSectionAmendTxt = getOptionsInBookedItemsSection(itemNum,0)
            //Cancel button text
        resultData.hotel.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(itemNum,1)

        //Hotel Name
        resultData.hotel.itineraryPage.bookedItems.hotelName=getHotelNameOnsuggstedItem(index)

        //Star Rating
        resultData.hotel.itineraryPage.bookedItems.hotelstarRating=getStarRating(index)

        //Room Type, Pax
        resultData.hotel.itineraryPage.bookedItems.hotelroomTypePax=getItinenaryDescreptionInSuggestedItem(index)

        //Checkin Date, No. of Stay Nights
        resultData.hotel.itineraryPage.bookedItems.hotelCheckInDateNoOfNights=getItenaryDurationInSuggestedItem(index)

        //Traveller Details
        String travelers = getTravelerNamesMicros(0)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.hotel.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)
        //total amount
        resultData.hotel.itineraryPage.bookedItems.hotelTotalAmount=getBookedItemAmount(index)+" "+clientData.currency
        //cancellation charges overlay should be dispalyed

        //check for the Cancellation and Amendment Charges section

        //overlay should be dismissed and itinerary page should be displayed.

        clickOnItemCancllationLink(index)
        sleep(2000)
        resultData.hotel.itineraryPage.bookedItems.cancellationChargesTxt = getCancellationPolicyContent()

        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }


    protected def ItinryPageAfterPaymentHotelMultiModule(ClientData clientData,PaymentsTestResultData resultData,int hotelIndex){
        //itemNum - Item Card Index
        //index - Inside card different items index

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.hotel.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(0)
        //Booked Item section - should have only hotel in the section
        //count the items in Booked Item section
        resultData.hotel.itineraryPage.bkdItemsCount=getNoOfItemsInBookedItemsSection()
        //only hotel in the section
        resultData.hotel.itineraryPage.bookedItemItemName=getItemNameInBookedItemsSection(hotelIndex)
        //booking Id text and number should be displayed
        resultData.hotel.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(hotelIndex+1)
        //confirmed Status
        resultData.hotel.itineraryPage.itemConfirmedStatus=getBookingStatus(hotelIndex)
        //Amend button text
        resultData.hotel.itineraryPage.itemSectionAmendTxt = getOptionsInBookedItemsSection(hotelIndex,0)
        //Cancel button text
        resultData.hotel.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(hotelIndex,1)

        //Hotel Name
        resultData.hotel.itineraryPage.bookedItems.hotelName=getHotelNameOnsuggstedItem(hotelIndex)

        //Star Rating
        resultData.hotel.itineraryPage.bookedItems.hotelstarRating=getStarRating(hotelIndex)

        //Room Type, Pax
        resultData.hotel.itineraryPage.bookedItems.hotelroomTypePax=getItinenaryDescreptionInSuggestedItem(hotelIndex)

        //Checkin Date, No. of Stay Nights
        resultData.hotel.itineraryPage.bookedItems.hotelCheckInDateNoOfNights=getItenaryDurationInSuggestedItem(hotelIndex)

        //Traveller Details
        String travelers = getTravelerNamesMicrosInBookedItems(hotelIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.hotel.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)
        //total amount
        resultData.hotel.itineraryPage.bookedItems.hotelTotalAmount=getBookedItemAmount(hotelIndex)+" "+clientData.currency
        //cancellation charges overlay should be dispalyed

        //check for the Cancellation and Amendment Charges section

        //overlay should be dismissed and itinerary page should be displayed.

        clickOnItemCancllationLink(hotelIndex)
        sleep(2000)
        resultData.hotel.itineraryPage.bookedItems.cancellationChargesTxt = getCancellationPolicyContent()

        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }


    protected def ItineraryPageAfterPaymentTransfersMultiModule(ClientData clientData,PaymentsTestResultData resultData,int itemNum, int index){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.transfer.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(1)
        //Booked Item section - should have only hotel in the section
        //count the items in Booked Item section
        resultData.transfer.itineraryPage.bookedItemsCount=getNoOfItemsInBookedItemsSection()
        //only hotel in the section
        resultData.transfer.itineraryPage.bookedHotelItemName=getItemNameInBookedItemsSection(0)
        resultData.transfer.itineraryPage.bookedTransferItemName=getItemNameInBookedItemsSection(1)
        //booking Id text and number should be displayed
        resultData.transfer.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(2)
        //confirmed Status
        resultData.transfer.itineraryPage.itemConfirmedStatus=getBookingStatus(1)

        //Cancel button text
        resultData.transfer.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(itemNum,0)

        //Transfer Name
        resultData.transfer.itineraryPage.bookedItems.transferName=getHotelNameOnsuggstedItem(index)

        //Transfer Pax
        resultData.transfer.itineraryPage.bookedItems.transferPax=getTransferPaxDetailsItinerarypageNonBkd(index)

        //Pick and drop off
        resultData.transfer.itineraryPage.bookedItems.transferPickUpAndDropOff=getTransfersDescItinryPagNonBkd(index)

        //duration with icon and tool tip

        //luggage  icon with tool tip and number

        //Max passanger allowed icon with tool tip and number


        //child icon tool tip and number


        //commission text should not display,


        // total amount and currency type.

        //Traveller Details
        String travelers = getTravelerNamesMicros(1)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.transfer.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.transfer.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)

        //total amount
        resultData.transfer.itineraryPage.bookedItems.transferTotalAmount=getBookedItemAmount(index)+" "+clientData.currency
        //cancellation charges overlay should be dispalyed

        //check for the Cancellation and Amendment Charges section

        //overlay should be dismissed and itinerary page should be displayed.

        clickOnItemCancllationLink(index)
        sleep(2000)
        resultData.transfer.itineraryPage.bookedItems.cancellationChargesTxt = getCancellationPolicyContent()

        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }

    protected def ItinryPageAfterPaymentTransfersMultiModule(ClientData clientData,PaymentsTestResultData resultData,int transferIndex){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.transfer.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(0)
        //Booked Item section - should have only hotel in the section
        //count the items in Booked Item section
        resultData.transfer.itineraryPage.bokedItemsCount=getNoOfItemsInBookedItemsSection()
        //only hotel in the section
        //resultData.transfer.itineraryPage.bookedHotelItemName=getItemNameInBookedItemsSection(0)
        resultData.transfer.itineraryPage.bookedTransferItemName=getItemNameInBookedItemsSection(transferIndex)
        //booking Id text and number should be displayed
        resultData.transfer.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(transferIndex+1)
        //confirmed Status
        resultData.transfer.itineraryPage.itemConfirmedStatus=getBookingStatus(transferIndex)

        //Cancel button text
        resultData.transfer.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(transferIndex,0)

        //Transfer Name
        resultData.transfer.itineraryPage.bookedItems.transferName=getHotelNameOnsuggstedItem(transferIndex)

        //Transfer Pax
        resultData.transfer.itineraryPage.bookedItems.transferPax=getTransferPaxDetailsItinerarypageBkd(transferIndex)

        //Pick and drop off
        resultData.transfer.itineraryPage.bookedItems.transferPickUpAndDropOff=getTransfersDescItinryPagBkd(transferIndex)

        //duration with icon and tool tip

        //luggage  icon with tool tip and number

        //Max passanger allowed icon with tool tip and number


        //child icon tool tip and number


        //commission text should not display,


        // total amount and currency type.

        //Traveller Details
        String travelers = getTravelerNamesMicros(transferIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.transfer.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.transfer.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)

        //total amount
        resultData.transfer.itineraryPage.bookedItems.transferTotalAmount=getBookedItemAmount(transferIndex)+" "+clientData.currency
        //cancellation charges overlay should be dispalyed

        //check for the Cancellation and Amendment Charges section

        //overlay should be dismissed and itinerary page should be displayed.

        clickOnItemCancllationLink(transferIndex)
        sleep(2000)
        resultData.transfer.itineraryPage.bookedItems.cancellationChargesTxt = getCancellationPolicyContent()

        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }

    protected def ItinryPageAfterPaymentTransferMultiModule(ClientData clientData,PaymentsTestResultData resultData,int transfersIndex,int activityIndex){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.transfer.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(1)
        //Booked Item section - should have only hotel in the section
        //count the items in Booked Item section
        resultData.transfer.itineraryPage.bookedItemsCount=getNoOfItemsInBookedItemsSection()
        //only transfer & sightseeing in the section
        resultData.transfer.itineraryPage.bookedTransferItemName=getItemNameInBookedItemsSection(transfersIndex)
        resultData.transfer.itineraryPage.bookedSightSeeingItemName=getItemNameInBookedItemsSection(activityIndex)
        //booking Id text and number should be displayed
        resultData.transfer.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(transfersIndex+1)
        //confirmed Status
        resultData.transfer.itineraryPage.itemConfirmedStatus=getBookingStatus(transfersIndex)

        //Cancel button text
        resultData.transfer.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(transfersIndex,0)

        //Transfer Name
        resultData.transfer.itineraryPage.bookedItems.transferName=getItemNameInBookedItemsSection(transfersIndex)

        //Transfer Pax
        resultData.transfer.itineraryPage.bookedItems.transferPax=getTransferPaxDetailsItinerarypageBkd(transfersIndex)

        //Pick and drop off
        resultData.transfer.itineraryPage.bookedItems.transferPickUpAndDropOff=getTransfersDescItinryPagBkd(transfersIndex)

        //duration with icon and tool tip

        //luggage  icon with tool tip and number

        //Max passanger allowed icon with tool tip and number


        //child icon tool tip and number


        //commission text should not display,


        // total amount and currency type.

        //Traveller Details
        String travelers = getTravelerNamesMicrosInBookedItems(transfersIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.transfer.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.transfer.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)

        //total amount
        resultData.transfer.itineraryPage.bookedItems.transferTotalAmount=getItemAmountInBookedItems(transfersIndex)+" "+clientData.currency




    }


    protected def ItineraryPageAfterPaymentMultiModuleHotel(ClientData clientData,PaymentsTestResultData resultData,int hotelIndex, int transfersIndex,int activityIndex){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.hotel.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(0)
        //3 items card should be displayed
        //count the items in Booked Item section
        resultData.hotel.itineraryPage.bkdItemsCount=getNoOfItemsInBookedItemsSection()
        //three items should be present in booked section
        resultData.hotel.itineraryPage.bookedHotelItemName=getItemNameInBookedItemsSection(hotelIndex)
        resultData.hotel.itineraryPage.bookedTransferItemName=getItemNameInBookedItemsSection(transfersIndex)
        resultData.hotel.itineraryPage.bookedSightSeeingItemName=getItemNameInBookedItemsSection(activityIndex)
        //booking Id text and number should be displayed
        resultData.hotel.itineraryPage.bkdItemBookingId=getBookingIDBookedDetailsScrn(1)
        //confirmed Status
        resultData.hotel.itineraryPage.itemConfirmedStatus=getBookingStatus(hotelIndex)

        //Cancel button text
        resultData.hotel.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(hotelIndex,1)



        // total amount and currency type.

        //Traveller Details
        String travelers = getTravelerNamesMicrosInBookedItems(hotelIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.hotel.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.hotel.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)

        //total amount
        resultData.hotel.itineraryPage.bookedItems.transferTotalAmount=getBookedItemAmount(hotelIndex)+" "+clientData.currency

        //Booked item subtotal
        resultData.hotel.itineraryPage.bookedItems.bookedItemSubtotal=getItenaryPriceInSuggestedItem(0)
        sleep(2000)

    }

    protected def ItineraryPageAfterPaymentMultiModule(ClientData clientData,PaymentsTestResultData resultData,int hotelIndex, int transfersIndex,int activityIndex){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.transfer.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(0)
        //3 items card should be displayed
        //count the items in Booked Item section
        resultData.transfer.itineraryPage.bkdItemsCount=getNoOfItemsInBookedItemsSection()
        //three items should be present in booked section
        resultData.transfer.itineraryPage.bookedHotelItemName=getItemNameInBookedItemsSection(hotelIndex)
        resultData.transfer.itineraryPage.bookedTransferItemName=getItemNameInBookedItemsSection(transfersIndex)
        resultData.transfer.itineraryPage.bookedSightSeeingItemName=getItemNameInBookedItemsSection(activityIndex)
        //booking Id text and number should be displayed
        resultData.transfer.itineraryPage.bkdItemBookingId=getBookingIDBookedDetailsScrn(1)
        //confirmed Status
        resultData.transfer.itineraryPage.itemConfirmedStatus=getBookingStatus(transfersIndex)

        //Cancel button text
        resultData.transfer.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(transfersIndex,0)



        // total amount and currency type.

        //Traveller Details
        String travelers = getTravelerNamesMicrosInBookedItems(transfersIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.transfer.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.transfer.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)

        //total amount
        resultData.transfer.itineraryPage.bookedItems.transferTotalAmount=getBookedItemAmount(transfersIndex)+" "+clientData.currency

        //Booked item subtotal
        resultData.transfer.itineraryPage.bookedItems.bookedItemSubtotal=getItenaryPriceInSuggestedItem(0)
        sleep(2000)

    }



    protected def "cancelAllBookings"(PaymentsTestData data, PaymentsTestResultData resultData){

        at ItineraryTravllerDetailsPage

       //click on manage itinerary and select cancel all bookings
        selectOptionFromManageItinerary(data.input.manageItinryValue)
        sleep(3000)
        //Cancel itinerary lightbox display status
        resultData.transfer.itineraryPage.actualCancelItemDispStatus=verifyRemoveItenary()
        clickYesOnRemoveItenary()
        sleep(7000)
        //Cancel Itinerary should disappear
        resultData.transfer.itineraryPage.actCancelItemDispStatus=verifyRemoveItenary()

    }

    protected def "Unavailableblock"(PaymentsTestData data, PaymentsTestResultData resultData,int hotelIndex,int transferIndex,int sightseeingIndex) {

        at ItineraryTravllerDetailsPage

        sleep(3000)
        //Unavailable and Cancelled Items
        resultData.transfer.removeItemPage.cancelledItems.actualUnavailableAndCanncedItemsText = getHeaderTxtInUnavailableItemsListScrn()

        //Booked Item status should be changed to cancelled
        resultData.transfer.removeItemPage.cancelledItems.actualHotelBookingStatus=getBookingStatus(hotelIndex)
        resultData.transfer.removeItemPage.cancelledItems.actualTransferBookingStatus=getBookingStatus(transferIndex)
        resultData.transfer.removeItemPage.cancelledItems.actualsightSeeingBookingStatus=getBookingStatus(sightseeingIndex)

        //booking id : should be displayed
        resultData.transfer.removeItemPage.cancelledItems.actualAfterCancelHotelBookingId=getBookingIDBookedDetailsScrn(hotelIndex+1)
        resultData.transfer.removeItemPage.cancelledItems.actualAfterCancelTransferBookingId=getBookingIDBookedDetailsScrn(transferIndex+1)
        resultData.transfer.removeItemPage.cancelledItems.actualAfterCancelSightSeeingBookingId=getBookingIDBookedDetailsScrn(sightseeingIndex+1)


        //3. Payment status
        resultData.transfer.removeItemPage.cancelledItems.actualHotelpaymentStatus=getPaymentNotTakenTxt(hotelIndex)
        resultData.transfer.removeItemPage.cancelledItems.actualTransferpaymentStatus=getPaymentNotTakenTxt(transferIndex)
        resultData.transfer.removeItemPage.cancelledItems.actualSightSeeingpaymentStatus=getPaymentNotTakenTxt(sightseeingIndex)
    }

        protected def ItineraryPageAfterPaymentActivityMultiModule(ClientData clientData,PaymentsTestResultData resultData, int hotelIndex, int transferIndex, int activityIndex){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.activity.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(0)
        //Booked Item section - should have only hotel in the section
        //count the items in Booked Item section
        resultData.activity.itineraryPage.bookedItemsCount=getNoOfItemsInBookedItemsSection()
        //only hotel in the section
        resultData.activity.itineraryPage.bookedHotelItemName=getItemNameInBookedItemsSection(hotelIndex)
        resultData.activity.itineraryPage.bookedTransferItemName=getItemNameInBookedItemsSection(transferIndex)
        resultData.activity.itineraryPage.bookedActivityItemName=getItemNameInBookedItemsSection(activityIndex)
        //booking Id text and number should be displayed
        resultData.activity.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(activityIndex+1)
        //confirmed Status
        resultData.activity.itineraryPage.itemConfirmedStatus=getBookingStatus(activityIndex)
        //Cancel button text
        resultData.activity.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(activityIndex,0)

        //Activity Details


        //Sightseeing Card Details
        resultData.activity.itineraryPage.bookedItems = storeItemDetails(activityIndex,0,"activity")

        //time
        resultData.activity.itineraryPage.bookedItems.actualItemDurationIconDisplayStatus=verifyItineryItemIconsDisplay("time",activityIndex)
        resultData.activity.itineraryPage.bookedItems.actualsightSeeingTimeSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(activityIndex,0)
        //place cursor on top of icon, tooltip show:		Duration"
        clickTransfrItmIconsDispInNonBkdItem("time",2)
        resultData.activity.itineraryPage.bookedItems.actualDurationIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("time",activityIndex)


        //Language
        resultData.activity.itineraryPage.bookedItems.actualItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplay("language",activityIndex)
        resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt=getItenarySuggestedItemTimeMaxLuggPersons(activityIndex,1)
        //place cursor on top of icon, tooltip show:		Duration"
        clickTransfrItmIconsDispInNonBkdItem("language",2)
        try{
            resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("language",activityIndex)
        }catch(Exception e){
            resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText="Unable To Read Lanugage Text From UI"
        }


        // commission text should  display
        resultData.activity.itineraryPage.bookedItems.commissionTxt=getCommisionAndPercentageInBookeddetailsScrn(activityIndex)

        //Traveller Details
        String travelers = getTravelerNamesMicros(activityIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.activity.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.activity.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)


        clickOnItemCancllationLink(activityIndex)
        sleep(2000)
        resultData.activity.itineraryPage.bookedItems.cancellationChargesTxt = getCancellationPolicyContent()

        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }

    protected def Bookeditemsectionafterpaylateractivity(ClientData clientData,PaymentsTestResultData resultData, int hotelIndex, int activityIndex){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.activity.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(1)
        //Booked Item section - should have only hotel in the section
        //count the items in Booked Item section
        resultData.activity.itineraryPage.bookedItemsCount=getNoOfItemsInBookedItemsSection()
        //only hotel in the section
        resultData.activity.itineraryPage.bookedHotelItemName=getItemNameInBookedItemsSection(hotelIndex)

        resultData.activity.itineraryPage.bookedActivityItemName=getItemNameInBookedItemsSection(activityIndex)
        //booking Id text and number should be displayed
        resultData.activity.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(activityIndex+1)
        //confirmed Status
        resultData.activity.itineraryPage.itemConfirmedStatus=getBookingStatus(activityIndex)
        //Cancel button text
        resultData.activity.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(activityIndex,0)

        //Activity Details


        //Sightseeing Card Details
        resultData.activity.itineraryPage.bookedItems = storeItemDetails(activityIndex,0,"activity")

        //time
        resultData.activity.itineraryPage.bookedItems.actualItemDurationIconDisplayStatus=verifyItineryItemIconsDisplayInBookedItems("time",activityIndex)
        resultData.activity.itineraryPage.bookedItems.actualsightSeeingTimeSuggestedItem=getItenaryBookedItemTimeMaxLuggPersons(activityIndex,0)
        //place cursor on top of icon, tooltip show:		Duration"
        clickItmIconsDispInBkdItem("time",0)
        resultData.activity.itineraryPage.bookedItems.actualDurationIconMouseHoverText=getsTransfrItmMouseHoverTextInBkdItemsScrn("time",activityIndex)


        //Language
        resultData.activity.itineraryPage.bookedItems.actualItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplayInBookedItems("language",activityIndex)
        resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt=getItenaryBookedItemTimeMaxLuggPersons(activityIndex,1)
        //place cursor on top of icon, tooltip show:		Duration"
        clickItmIconsDispInBkdItem("language",0)
        try{
            resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText=getsTransfrItmMouseHoverTextInBkdItemsScrn("language",activityIndex)
        }catch(Exception e){
            resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText="Unable To Read Lanugage Text From UI"
        }


        // commission text should  display
        resultData.activity.itineraryPage.bookedItems.commissionTxt=getCommisionAndPercentageInBookedItems(activityIndex)

        resultData.activity.itineraryPage.bookedItems.actualPriceAndCurrency=getItenaryPriceInBookedItems(activityIndex)

        //Traveller Details
        String travelers = getTravelerNamesMicrosInBookedItems(activityIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.activity.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.activity.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)

        clickOnItemCancllationLinkInBookedItems(activityIndex)
        sleep(2000)
        resultData.activity.itineraryPage.bookedItems.cancellationChargesTxt = getCancellationPolicyContent()

        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }

    protected def bookedItmSctnAftrPaylatrActivity(ClientData clientData,PaymentsTestResultData resultData, int activityIndex){

        at ItineraryTravllerDetailsPage
        sleep(1000)
        //Booked item section - booked item header should be displayed
        resultData.activity.itineraryPage.bookedItemHeader =  getHeaderTextInItineraryPage(1)
        //Booked Item section - should have only activity in the section
        //count the items in Booked Item section
        resultData.activity.itineraryPage.bookedItemsCount=getNoOfItemsInBookedItemsSection()
        //only activity in the section
        resultData.activity.itineraryPage.bookedActivityItemName=getItemNameInBookedItemsSection(activityIndex)
        //booking Id text and number should be displayed
        resultData.activity.itineraryPage.bookedItemBookingId=getBookingIDBookedDetailsScrn(activityIndex+1)
        //confirmed Status
        resultData.activity.itineraryPage.itemConfirmedStatus=getBookingStatus(activityIndex)
        //Cancel button text
        resultData.activity.itineraryPage.itemCancelTxt = getOptionsInBookedItemsSection(activityIndex,0)

        //Activity Details


        //Sightseeing Card Details
        resultData.activity.itineraryPage.bookedItems = storeItemDetails(activityIndex,0,"activity")

        //Activity Check In Date
        resultData.activity.itineraryPage.bookedItems.actualCheckInDate=getCheckInDateInBookedItems(activityIndex)

        //time
        resultData.activity.itineraryPage.bookedItems.actualItemDurationIconDisplayStatus=verifyItineryItemIconsDisplayInBookedItems("time",activityIndex)
        resultData.activity.itineraryPage.bookedItems.actualsightSeeingTimeSuggestedItem=getItenaryBookedItemTimeMaxLuggPersons(activityIndex,0)
        //place cursor on top of icon, tooltip show:		Duration"
        clickItmIconsDispInBkdItem("time",0)
        resultData.activity.itineraryPage.bookedItems.actualDurationIconMouseHoverText=getsTransfrItmMouseHoverTextInBkdItemsScrn("time",activityIndex)


        //Language
        resultData.activity.itineraryPage.bookedItems.actualItemLanguageIconDisplayStatus=verifyItineryItemIconsDisplayInBookedItems("language",activityIndex)
        resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt=getItenaryBookedItemTimeMaxLuggPersons(activityIndex,1)
        //place cursor on top of icon, tooltip show:		Duration"
        clickItmIconsDispInBkdItem("language",0)
        try{
            resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText=getsTransfrItmMouseHoverTextInBkdItemsScrn("language",activityIndex)
        }catch(Exception e){
            resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText="Unable To Read Lanugage Text From UI"
        }


        // commission text should  display
        resultData.activity.itineraryPage.bookedItems.commissionTxt=getCommisionAndPercentageInBookedItems(activityIndex)

        resultData.activity.itineraryPage.bookedItems.actualPriceAndCurrency=getItenaryPriceInBookedItems(activityIndex)

        //Traveller Details
        String travelers = getTravelerNamesMicrosInBookedItems(activityIndex)
        String travellers = travelers.split(":").getAt(1)
        String[] travellerSplit = travellers.split(",")
        resultData.activity.itineraryPage.bookedItems.traveler1 = travellerSplit.getAt(0)
        resultData.activity.itineraryPage.bookedItems.traveler2 = travellerSplit.getAt(1)

        clickOnItemCancllationLinkInBookedItems(activityIndex)
        sleep(2000)
        resultData.activity.itineraryPage.bookedItems.cancellationChargesTxt = getCancellationPolicyContent()

        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(2000)

    }


    //payment status in itienray page after booking done
    protected def paymentStatus(PaymentsTestResultData resultData,int index){
        at ItineraryTravllerDetailsPage
        resultData.hotel.itineraryPage.paymentStatusInItinerary = getPaymentStatus(index)
        resultData.hotel.itineraryPage.voucherStatus = voucherStatus(index)
    }
    protected def homeViewProfile(PaymentsTestResultData resultData){
        at HomePage
        resultData.hotel.home.viewProfileOverlay = viewProfile()
        clickOnViewProfile()
        at MyAccountPage
        resultData.account.accountPage.MyProfileStatus =  getMyProfileSectionStatus()
    }

        //Store addr details in myaccountpage and Add new addr in my account page
    protected def addNewAddFromMyAccount(PaymentsTestResultData resultData,int num) {
        at MyAccountPage
        sleep(1000)
        clickAddNewAddrButton()
        waitTillLoadingIconDisappears()
        sleep(2000)
        resultData.account.accountPage.addrOverlay = getNewAddrOverlayStatus()
        enterBillingAddressMyAcc(defaultData.expected.Addr2.get(0).toString(), defaultData.expected.Addr2.get(1).toString(), defaultData.expected.Addr2.get(2).toString(), defaultData.expected.Addr2.get(3).toString(), defaultData.expected.Addr2.getAt(4).toString(), defaultData.expected.Addr2.get(5).toString(), defaultData.expected.Addr2.get(6).toString())
        sleep(500)
        clickOnSaveAddrButton()
        waitTillLoadingIconDisappears()
        resultData.account.accountPage.addedCardStatus = getBillingAddrSectionStatus(num)
        sleep(1000)
        resultData.account.accountPage.lastCardAddrDetails = getAddrDetails()
        clickOnRetrunToPrevPage()
       //driver.navigate().back()
        waitTillLoadingIconDisappears()
        sleep(1000)
    }
    // store saved addr details in payment page.
    def savedAddr(PaymentsTestResultData resultData,int num){
        at PaymentsPage
        resultData.hotel.paymentpage.savedAddrStatus = verifyIfAddressPresent(defaultData.input.addrType1)
        resultData.hotel.paymentpage.savedAddrCount = getAddrCount(defaultData.input.addrType1)
        resultData.hotel.paymentpage.savedAddr = getSavedAddr(num)

    }



    /****** Validations *********/

    protected def "VerifyloginToApplicaiton"(PaymentsTestData data, PaymentsTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.hotel.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }

    protected def "VerifysearchForHotelAndAddToItinerary"(PaymentsTestData data, PaymentsTestResultData resultData) {

        //Hotel Search Results Returned
        assertionEquals(resultData.hotel.searchResults.actualSearchResults, data.expected.dispStatus, "Hotel Search Results Returned Actual & Expected don't match")

        //Free cacellation should be present
        assertionEquals(resultData.hotel.searchResults.actualCancellatoinTxtDispStatus, data.expected.dispStatus, "Free Cancellation Displayed Actual & Expected don't match")

        //capture hotel item added to itinerary
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualHotelNameInItinryBuilder, resultData.hotel.searchResults.actualHotelName, "Hotel Added to Itinerary Actual & Expected don't match")

        //Itinerary Page should be displayed
        assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualTravellerLabelTxt, resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")


    }

    protected def "VerifysearchForTransferAndAddToItinerary"(PaymentsTestData data, PaymentsTestResultData resultData) {

        //Transfer Search Results Returned
        assertionEquals(resultData.transfer.searchResults.actualSearchResults, data.expected.dispStatus, "Transfer Search Results Returned Actual & Expected don't match")

        //Cancellation fees apply should be present
        assertionEquals(resultData.transfer.searchResults.actualCancellatoinTxtDispStatus, data.expected.dispStatus, "Cancellation fees Apply Displayed Actual & Expected don't match")

        //capture transfer item added to itinerary
        assertionEquals(resultData.transfer.searchResults.itineraryBuilder.actualTransferNameInItineraryBuilder, resultData.transfer.searchResults.actualTransferName, "Transfer Item added to Itinerary Actual & Expected don't match")

        //Itinerary Page should be displayed
        assertionEquals(resultData.transfer.itineraryPage.actualTravellerLabelTxt, resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")

    }

    protected def "VerifysearchForActivityAndAddToItinerary"(PaymentsTestData data, PaymentsTestResultData resultData) {

        //Transfer Search Results Returned
        assertionEquals(resultData.activity.searchResults.actualSearchResults, data.expected.dispStatus, "Activity Search Results Returned Actual & Expected don't match")

        //Cancellation fees apply should be present
        assertionEquals(resultData.activity.searchResults.actualCancellatoinTxtDispStatus, data.expected.dispStatus, "Cancellation Charges Apply Displayed Actual & Expected don't match")

        //capture Activity item added to itinerary
        assertionEquals(resultData.activity.searchResults.itineraryBuilder.actualActivityNameInItinryBuilder,resultData.activity.searchResults.actualActivityName, "Activity Item Added to Itinerary Actual & Expected don't match")


        //Itinerary Page should be displayed
        assertionEquals(resultData.activity.itineraryPage.actualTravellerLabelTxt, resultData.hotel.searchResults.itineraryBuilder.expectedTravellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")

    }


    protected def verifySearchHotel(PaymentsTestData data, PaymentsTestResultData resultData) {
        //  resultData.hotel.searchResults = searchHotelAndAddToItinerary(data)
        softAssert.assertTrue( resultData.hotel.searchResults.actualSearchResults, "Hotel results page not displayed")
        softAssert.assertTrue(resultData.hotel.searchResults.freeCancellationStatus, "Free cancellation link not displayed")
    }

    protected def verifyNonBookedItemSection(PaymentsTestData paymentsData, PaymentsTestResultData resultData){

         //NON booked items section should have 3 added items
        assertionEquals(resultData.hotel.itineraryPage.actualNonBookedItemsCount,paymentsData.expected.nonBookedItemsCount,"Itinerary Page, Non booked Itmes count Actual & Expected Don't Match")

        //Validate Non Booked Hotel Item Card Details
        verifyBookAHotelItem(resultData)
        //Validate Non Booked Transfer Item Card Details
        verifyNonBookedTransferItem(paymentsData,resultData)
        //Validate Non Booked SightSeeing Item Card Details
        verifyNonBookedSightSeeingItem(resultData)

    }
    protected def verifyItineraryPage(PaymentsTestData defaultData, PaymentsTestResultData resultData){
        //   softAssert.assertTrue(resultData.hotel.itineraryPage.itineraryPageStatus,"Itinerary page not displayed")
        softAssert.assertFalse(resultData.hotel.itineraryPage.itineraryName.toString().isEmpty(),"Itinerary name is not updated.")
        //Validated Edited Itinerary Name - added by Kranthi
        assertionEquals(resultData.hotel.itineraryPage.itineraryName,resultData.hotel.itineraryPage.expectedItineraryname,"Itinerary Page, Edit Itinerary Name Actual & Expected Don't Match")

        //Validate Agent Ref - added by kranthi
        assertionEquals(resultData.hotel.itineraryPage.actualSavedAgentRefName,defaultData.input.agentRef,"Itinerary Page, Agent Reference Saved Details Actual & Expected Don't Match")

        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(0),defaultData.expected.traveler1[0],"traveler 1 is not saved")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(1),defaultData.expected.traveler1[2],"phone num not saved.")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(2),defaultData.expected.traveler1[1],"email not saved.")
        assertionEquals(resultData.hotel.itineraryPage.travellerDetails.get(3).toString(),defaultData.expected.traveler2,"traveler2 not saved.")

    }
    //Excepted data comes from test data file payments
    protected def verifyBookAnItem(PaymentsTestData data,PaymentsTestResultData resultData,int index){

        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedHotelName,data.expected.hotelCard[0],"HotelName not matching as expected.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedStarRating,data.expected.hotelCard[1],"Star Rating not matching as expected.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc,data.expected.hotelCard[2],"Desc not matching as expected.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().endsWith(data.expected.hotelCard[3].toString()),"pax not displayed for Non-booked item.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedItemStatus,data.expected.hotelCard[4],"Available not displayed for Non-booked item.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedCommission.toString(),data.expected.hotelCard[5].toString(),"Commission not matching as expected.")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice, resultData.hotel.searchResults.itemPrice,"price not matched")
        softAssert.assertTrue(resultData.hotel.itineraryPage.cancellationLinkStatus.toString().contains(data.expected.freeCancellationLinkStatus),"Free cancellation link not displayed")
        softAssert.assertTrue(resultData.hotel.itineraryPage.overlayStatus ,"T&C Overlay not displayed.")

    }

    protected def verifyBookAHotelItem(PaymentsTestResultData resultData) {

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

    protected def verifyNonBookedTransferItem(PaymentsTestData data, PaymentsTestResultData resultData) {

        //Transfer name
        assertionEquals(resultData.transfer.itineraryPage.transferItem.expectedHotelName,resultData.transfer.searchResults.transferItem.transferName, "Transfer Name Actual & Expected Don't Match")

        //Pax
        assertionEquals(resultData.transfer.itineraryPage.transferItem.pax.replaceAll(" ",""),resultData.transfer.itineraryPage.transferItem.expectedPax.replaceAll(" ",""), "Transfer Pax Actual & Expected Don't Match")

        //pick up and drop off
        assertionEquals(resultData.transfer.itineraryPage.transferItem.expectedTransferDesc,resultData.transfer.searchResults.transferItem.transferDesc, "Transfer Desc Actual & Expected Don't Match")

        //Duration Icon status
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualItemDurationIconDisplayStatus,resultData.transfer.searchResults.transferItem.actualDurationIconDispStatus, "Transfer Item, Duration Icon Status Actual & Expected Don't Match")

        //Duration Icon Tool tip
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualDurationIconMouseHoverText,resultData.transfer.searchResults.transferItem.actualDurationIconMouseHoverText, "Transfer Item, Duration Tool Tip Actual & Expected Don't Match")

        //Duration Icon succeeding Text
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualTransferTimeSuggestedItem,resultData.transfer.searchResults.transferItem.actualDurationTimeAndValueText, "Transfer Item, Duration Text Actual & Expected Don't Match")


        //max luggage Icon status
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualItemLuggageIconDisplayStatus,resultData.transfer.searchResults.transferItem.actualLuggageIconDispStatus, "Transfer Item, Max Luggage Icon Status Actual & Expected Don't Match")

        //mas luggage Tool tip
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualLuggageIconMouseHoverText,resultData.transfer.searchResults.transferItem.actualLuggageIconMouseHoverText, "Transfer Item, Max Luggage Tool Tip Actual & Expected Don't Match")

        //max luggage Icon succeeding text
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualTransferMaxLuggageSuggestedItem,resultData.transfer.searchResults.transferItem.actualTransferLuggage, "Transfer Item, Max Luggage Text Actual & Expected Don't Match")


        //max Persons allowed Icon status
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualPaxIconDisplayStatus,resultData.transfer.searchResults.transferItem.actualPaxIconDispStatus, "Transfer Item, Max Persons Icon Status Actual & Expected Don't Match")

        //max Persons Tool tip
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualPaxIconMouseHoverText,resultData.transfer.searchResults.transferItem.actualPaxIconMouseHoverText, "Transfer Item, Max Persons Tool Tip Actual & Expected Don't Match")

        //max Persons Icon succeeding text
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualTransfermaxPersonsSuggestedItem,resultData.transfer.searchResults.transferItem.actualTransferLuggage, "Transfer Item, Max Persons Text Actual & Expected Don't Match")


        //child Icon status
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualChildIconDisplayStatus,data.expected.dispStatus, "Transfer Item, Child Icon Status Actual & Expected Don't Match")

        //child icon Tool tip
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualChildIconMouseHoverText,data.expected.childIconToolTip, "Transfer Item, Child Tool Tip Actual & Expected Don't Match")

        //child Icon succeeding text
        assertionEquals(resultData.transfer.itineraryPage.transferItem.actualChildTxt,data.expected.childIconTxt, "Transfer Item, Child Text Actual & Expected Don't Match")


        //Status
        assertionEquals(resultData.transfer.itineraryPage.transferItem.expectedItemStatus,resultData.transfer.searchResults.transferItem.transferStatus, "Transfer Status Actual & Expected Don't Match")

        //Total Amount And Currency Type
        assertionEquals(resultData.transfer.itineraryPage.transferItem.expectedItemPrice.replaceAll(" ",""),resultData.transfer.searchResults.transferItem.transferPrice.replaceAll(" ",""), "Transfer Total Amount And Currency Type Actual & Expected Don't Match")

        //Transfer Cancel Chrg Text
        assertionEquals(resultData.transfer.itineraryPage.transferItem.expectedItemCancellationLinkText,resultData.transfer.searchResults.transferItem.transferCancelTxt, "Transfer Cancel Charge Text Actual & Expected Don't Match")



    }

    protected def verifyNonBookedSightSeeingItem(PaymentsTestResultData resultData) {

        //SightSeeing name
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.expectedHotelName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName, "Sightseeing Name Actual & Expected Don't Match")


        //Duration Icon status
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.actualItemDurationIconDisplayStatus,resultData.activity.searchResults.sightSeeingItem.actualDurationIconDispStatus, "Sightseeing Item, Duration Icon Status Actual & Expected Don't Match")

        //Duration Icon Tool tip
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.actualDurationIconMouseHoverText,resultData.activity.searchResults.sightSeeingItem.actualDurationIconMouseHoverText, "Sightseeing Item, Duration Tool Tip Actual & Expected Don't Match")

        //Duration Icon succeeding Text
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingTimeSuggestedItem,resultData.activity.searchResults.sightSeeingItem.actualDurationTimeAndValueText, "Sightseeing Item, Duration Text Actual & Expected Don't Match")


        //Language Icon status
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.actualItemLanguageIconDisplayStatus,resultData.activity.searchResults.sightSeeingItem.actualLanguageIconDispStatus, "Sightseeing Item, Language Icon Status Actual & Expected Don't Match")

        //Language Icon Tool tip
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.actualLanguageIconMouseHoverText,resultData.activity.searchResults.sightSeeingItem.actualLanguageIconMouseHoverText, "Sightseeing Item, Language Tool Tip Actual & Expected Don't Match")

        //Language Icon succeeding Text
        //assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt,resultData.activity.searchResults.sightSeeingItem.actualLangaugeValueText, "Sightseeing Item, Language Text Actual & Expected Don't Match")
        assertContains(resultData.activity.searchResults.sightSeeingItem.actualLangaugeValueText.replaceAll(",",""),resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt.replaceAll(",",""), "Sightseeing Item, Language Text Actual & Expected Don't Match")
        //assertionListEquals(resultData.activity.searchResults.sightSeeingItem.actualLangaugeValueText,resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt, "Sightseeing Item, Language Text Actual & Expected Don't Match")


        //SightSeeing Status
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.expectedItemStatus,resultData.activity.searchResults.sightSeeingItem.sightSeeingStatus, "Sightseeing Status Text Actual & Expected Don't Match")

        //SightSeeing Total Amount And Currency Text
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.expectedItemPrice,resultData.activity.searchResults.sightSeeingItem.sightSeeingAmntAndCurncy, "Sightseeing Total Amount And Currency Text Actual & Expected Don't Match")

        //SightSeeing Cancel Chrg Text
        assertionEquals(resultData.activity.itineraryPage.sightSeeingItem.expectedItemCancellationLinkText,resultData.activity.searchResults.sightSeeingItem.sightSeeingCancelChrgTxt, "Sightseeing Cancel Charge Text Actual & Expected Don't Match")

    }

    protected def verifybookanhotel(PaymentsTestData data, PaymentsTestResultData resultData){
        //User should be able to do so
        assertionEquals(resultData.hotel.itineraryPage.YouAreAbtToBookScrnTxt,data.expected.aboutToBookScrnTxt, "Hotels, About To Book Screen Header Text Actual & Expected Don't Match")

    }

    protected def verifybookanTransfer(PaymentsTestData data, PaymentsTestResultData resultData){
        //User should be able to do so
        assertionEquals(resultData.transfer.itineraryPage.YouAreAbtToBookScrnTxt,data.expected.aboutToBookScrnTxt, "Transfers, About To Book Screen Header Text Actual & Expected Don't Match")

    }
    protected def verifybookanActivity(PaymentsTestData data, PaymentsTestResultData resultData){
        //User should be able to do so
        assertionEquals(resultData.activity.itineraryPage.YouAreAbtToBookScrnTxt,data.expected.aboutToBookScrnTxt, "Sightseeing, About To Book Screen Header Text Actual & Expected Don't Match")

    }

    protected def verifyBookingTAndCoverlay(PaymentsTestData data, PaymentsTestResultData resultData,boolean payLater){

        //verify the details in booking T&C overlay         1. Hotel Name
        assertionEquals(resultData.hotel.itineraryOverlay.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName, "HotelName Actual & Expected Don't Match")

        //Validate Traveller Name
        assertionListEquals(resultData.hotel.itineraryOverlay.travelerName,resultData.hotel.itineraryOverlay.expectedTravelerName, "Traveller names Actual & Expected Don't Match")


        //Validate  Traveller Check Box Status
        List actualStatusData=resultData.hotel.itineraryOverlay.checkBoxStatus.get("actualItemStatus")
        List expectedStatusData=resultData.hotel.itineraryOverlay.checkBoxStatus.get("expectedItemStatus")

        assertionListEquals(actualStatusData,expectedStatusData,"Traveller Checkbox status actual & expected don't match")

        //user should be able to do so and section should expand
        assertionEquals(resultData.hotel.itineraryOverlay.expandStatus,data.expected.dispStatus, "Add Special Remark Or Comment Section Expand Status Actual & Expected Don't Match")

        //user should be able to do so and section should collapse
        assertionEquals(resultData.hotel.itineraryOverlay.colapseStatus,data.expected.notDispStatus, "Add Special Remark Or Comment Section Collapse Status Actual & Expected Don't Match")

        ////check for confirm booking and pay now button should be enabled
        assertionEquals(resultData.hotel.itineraryOverlay.bookNowEnabledStatus,data.expected.dispStatus, "Confirm Booking & Pay now button Enabled Status Actual & Expected Don't Match")
        if(payLater==true) {
            assertionEquals(resultData.hotel.itineraryOverlay.bookNowPayLaterBtnEnabledStatus,data.expected.dispStatus, "Confirm Booking & pay later button Enabled Status Actual & Expected Don't Match")

        }
    }

    protected def verifyBookingTAndCoverlayTransfersMultiModule(PaymentsTestData data, PaymentsTestResultData resultData){

        //verify the details in booking T&C overlay         1. Transfer Name
        assertContains(resultData.transfer.itineraryOverlay.transferName,resultData.transfer.itineraryPage.transferItem.expectedHotelName, "Transfer Name Actual & Expected Don't Match")

        //Validate Traveller Name
        assertionListEquals(resultData.transfer.itineraryOverlay.travelerName,resultData.transfer.itineraryOverlay.expectedTravelerName, "Traveller names Actual & Expected Don't Match")


        //Validate  Traveller Check Box Status
        List actualStatusData=resultData.transfer.itineraryOverlay.checkBoxStatus.get("actualItemStatus")
        List expectedStatusData=resultData.transfer.itineraryOverlay.checkBoxStatus.get("expectedItemStatus")

        assertionListEquals(actualStatusData,expectedStatusData,"Traveller Checkbox status actual & expected don't match")

        //should have header Drop off point details .
        assertionEquals(resultData.transfer.itineraryOverlay.headerDropOffTxt,data.expected.dropOffheaderTxt, "Transfers Item, Drop Off header text Actual & Expected Don't Match")

        //Address
        //assertionEquals(resultData.transfer.itineraryOverlay.dropOffAddressTxt,resultData.transfer.itineraryOverlay.expecteddropOffAddressTxt, "Transfers Item, Drop Off Address text Actual & Expected Don't Match")
        assertContains(resultData.transfer.itineraryOverlay.dropOffAddressTxt,resultData.transfer.itineraryOverlay.expecteddropOffAddressTxt, "Transfers Item, Drop Off Address text Actual & Expected Don't Match")


        /*
        //user should be able to do so and section should expand
        assertionEquals(resultData.hotel.itineraryOverlay.expandStatus,data.expected.dispStatus, "Add Special Remark Or Comment Section Expand Status Actual & Expected Don't Match")

        //user should be able to do so and section should collapse
        assertionEquals(resultData.hotel.itineraryOverlay.colapseStatus,data.expected.notDispStatus, "Add Special Remark Or Comment Section Collapse Status Actual & Expected Don't Match")

        ////check for confirm booking and pay now button should be enabled
        assertionEquals(resultData.hotel.itineraryOverlay.bookNowEnabledStatus,data.expected.dispStatus, "Confirm Booking & Pay now button Enabled Status Actual & Expected Don't Match")

*/
    }

    protected def verifyBookingTAndCoverlaySightSeeingMultiModule(PaymentsTestData data, PaymentsTestResultData resultData){

        //verify the details in booking T&C overlay         1. Activity Name
        assertContains(resultData.activity.itineraryOverlay.sightSeeingName,resultData.activity.itineraryPage.sightSeeingItem.expectedHotelName, "Sight Seeing Name Actual & Expected Don't Match")

        //Validate Traveller Name
        assertionListEquals(resultData.activity.itineraryOverlay.travelerName,resultData.activity.itineraryOverlay.expectedTravelerName, "Sight Seeing names Actual & Expected Don't Match")


        //Validate  Traveller Check Box Status
        List actualStatusData=resultData.activity.itineraryOverlay.checkBoxStatus.get("actualItemStatus")
        List expectedStatusData=resultData.activity.itineraryOverlay.checkBoxStatus.get("expectedItemStatus")

        assertionListEquals(actualStatusData,expectedStatusData,"Sight Seeing, Traveller Checkbox status actual & expected don't match")

        //Please select language section should be dispalyed and
        //assertionEquals(resultData.activity.itineraryOverlay.langSectHeaderTxt,data.expected.activityLangHeaderTxt,"Activity item, Please select Language Section Actual & Expected don't match")
        // lauguage tab should have english by default
        //assertionEquals(resultData.activity.itineraryOverlay.defaultLangSelectedTxt,data.expected.activityLangDefaulSelTxt,"Activity item, Language selected by default Actual & Expected don't match")
        assertionEquals(resultData.activity.itineraryOverlay.LangTxt,data.expected.activityLangTxt,"Activity item, Language selected by default Actual & Expected don't match")


        //Departure point should eb displayed and

        // should have section details: Gala hotel and

        // time  tab should have 080:00 by default.


    }


    protected def verifypaymentSummaryBlockExtended(PaymentsTestData data, PaymentsTestResultData resultData,boolean payLater){

        //Transfer dispalyed in Payment summary
        assertionEquals(resultData.hotel.paymentpage.transferExistence,data.expected.notDispStatus, "Transfer Display In Payment Summary Block Actual & Expected Don't Match")

        //Activity displayed in Payment summary
        assertionEquals(resultData.hotel.paymentpage.sightSeeingExistence,data.expected.notDispStatus, "Sightseeing Display In Payment Summary Block Actual & Expected Don't Match")

        if(payLater==true){
            //Booking ID text
            assertionEquals(resultData.hotel.paymentpage.bookingIdText,resultData.hotel.confirmBookingOverlay.bookingID, "Hotel Item, Booking ID In Payment Summary Block Actual & Expected Don't Match")

        }



    }

    protected def verifypaymentSummaryBlockExtendedHotel(PaymentsTestData data, PaymentsTestResultData resultData,boolean payLater){

        //Transfer dispalyed in Payment summary
        assertionEquals(resultData.hotel.paymentpage.transferExistence,data.expected.notDispStatus, "Transfer Display In Payment Summary Block Actual & Expected Don't Match")

        //Activity displayed in Payment summary
        //assertionEquals(resultData.hotel.paymentpage.sightSeeingExistence,data.expected.notDispStatus, "Sightseeing Display In Payment Summary Block Actual & Expected Don't Match")

        if(payLater==true){
            //Booking ID text
            assertionEquals(resultData.hotel.paymentpage.bookingIdText,resultData.hotel.confirmBookingOverlay.bookingID, "Hotel Item, Booking ID In Payment Summary Block Actual & Expected Don't Match")

        }



    }

    protected def verifypaymentSummeryBlockTransfers(PaymentsTestData data, PaymentsTestResultData resultData){

        softAssert.assertTrue(resultData.transfer.paymentpage.paymentSummeryLabel,"Paymentsummery label not displayed." )
        assertList(resultData.transfer.paymentpage.paymentSummeryLables,defaultData.expected.paymentSummeryBlock,"payment summery labels not displayed.")
        if(resultData.transfer.paymentpage.cardDetails.bookingIdStatus.toString().equals("false")){
            softAssert.assertFalse(resultData.transfer.paymentpage.cardDetails.bookingIdStatus,"booking id is not displayed in payment summery card.")
        }
        else{
            softAssert.assertTrue(resultData.transfer.paymentpage.cardDetails.bookingIdStatus,"booking id is displayed in payment summery card.")
        }
        softAssert.assertTrue(resultData.transfer.itineraryPage.transferItem.expectedItemDuration.toString().startsWith(resultData.transfer.paymentpage.cardDetails.date.toString()),"date is not displayed in payment summery card.")
        assertionEquals(resultData.transfer.paymentpage.cardDetails.name,resultData.transfer.itineraryPage.transferItem.expectedHotelName,"Transfer name is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.transfer.itineraryPage.transferItem.expectedItemDesc.toString().contains(resultData.transfer.paymentpage.cardDetails.pax.toString()),"pax not matched in payment page against itinerary page.")
        softAssert.assertTrue(resultData.transfer.itineraryPage.transferItem.expectedItemPrice.toString().startsWith(resultData.transfer.paymentpage.cardDetails.price),"price is not matched against itinerary page.")
        //Total Gross
        assertionEquals(resultData.transfer.paymentpage.totalGrass,resultData.transfer.itineraryPage.transferItem.expectedItemPrice,"total gross is not matching.")

        //Total Net
        assertionEquals(resultData.transfer.paymentpage.totalNet,data.expected.transferTotalNet,"total net price is displayed")

        //Total To Pay
        assertionEquals(resultData.transfer.paymentpage.totalPay,resultData.transfer.paymentpage.totalGrass,"total net price and total gross price are not equal.")
        assertionEquals(resultData.transfer.paymentpage.totalToPayInformationIconTxt,data.expected.transferTotalToPaySummary,"Information Icon with Tooltip is not displayed.")

        //Hotel dispalyed in Payment summary
        assertionEquals(resultData.transfer.paymentpage.hotelExistence,data.expected.notDispStatus, "Hotel Display In Payment Summary Block Actual & Expected Don't Match")

        //Activity displayed in Payment summary
        assertionEquals(resultData.transfer.paymentpage.sightSeeingExistence,data.expected.notDispStatus, "Sightseeing Display In Payment Summary Block Actual & Expected Don't Match")

    }


    protected def verifypaymentSummeryBlockTransfer(PaymentsTestData data, PaymentsTestResultData resultData){

        softAssert.assertTrue(resultData.transfer.paymentpage.paymentSummeryLabel,"Paymentsummery label not displayed." )
        assertList(resultData.transfer.paymentpage.paymentSummeryLables,defaultData.expected.paymentSummeryBlock,"payment summery labels not displayed.")
        if(resultData.transfer.paymentpage.cardDetails.bookingIdStatus.toString().equals("false")){
            softAssert.assertFalse(resultData.transfer.paymentpage.cardDetails.bookingIdStatus,"booking id is not displayed in payment summery card.")
        }
        else{
            softAssert.assertTrue(resultData.transfer.paymentpage.cardDetails.bookingIdStatus,"booking id is displayed in payment summery card.")
        }
        softAssert.assertTrue(resultData.transfer.itineraryPage.transferItem.expectedItemDuration.toString().startsWith(resultData.transfer.paymentpage.cardDetails.date.toString()),"date is not displayed in payment summery card.")
        //assertionEquals(resultData.transfer.paymentpage.cardDetails.name,resultData.transfer.itineraryPage.transferItem.expectedHotelName,"Transfer name is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.transfer.itineraryPage.transferItem.expectedItemDesc.toString().contains(resultData.transfer.paymentpage.cardDetails.pax.toString()),"pax not matched in payment page against itinerary page.")
        //softAssert.assertTrue(resultData.transfer.itineraryPage.transferItem.expectedItemPrice.toString().startsWith(resultData.transfer.paymentpage.cardDetails.price),"price is not matched against itinerary page.")
        //Total Gross
        //assertionEquals(resultData.transfer.paymentpage.totalGrass,resultData.transfer.itineraryPage.transferItem.expectedItemPrice,"total gross is not matching.")

        //Total Net
        //assertionEquals(resultData.transfer.paymentpage.totalNet,data.expected.transferTotalNet,"total net price is displayed")

        //Total To Pay
        //assertionEquals(resultData.transfer.paymentpage.totalPay,resultData.transfer.paymentpage.totalGrass,"total net price and total gross price are not equal.")
        //assertionEquals(resultData.transfer.paymentpage.totalToPayInformationIconTxt,data.expected.transferTotalToPaySummary,"Information Icon with Tooltip is not displayed.")

        //Hotel dispalyed in Payment summary
       // assertionEquals(resultData.transfer.paymentpage.hotelExistence,data.expected.notDispStatus, "Hotel Display In Payment Summary Block Actual & Expected Don't Match")

        //Activity displayed in Payment summary
        //assertionEquals(resultData.transfer.paymentpage.sightSeeingExistence,data.expected.notDispStatus, "Sightseeing Display In Payment Summary Block Actual & Expected Don't Match")

    }


    protected def verifypaymentSummeryBlockActivity(PaymentsTestData data, PaymentsTestResultData resultData){

        softAssert.assertTrue(resultData.activity.paymentpage.paymentSummeryLabel,"Paymentsummery label not displayed." )
        assertList(resultData.activity.paymentpage.paymentSummeryLables,defaultData.expected.paymentSummeryBlock,"payment summery labels not displayed.")
        if(resultData.activity.paymentpage.cardDetails.bookingIdStatus.toString().equals("false")){
            softAssert.assertFalse(resultData.activity.paymentpage.cardDetails.bookingIdStatus,"booking id is not displayed in payment summery card.")
        }
        else{
            softAssert.assertTrue(resultData.activity.paymentpage.cardDetails.bookingIdStatus,"booking id is displayed in payment summery card.")
        }
        softAssert.assertTrue(resultData.activity.itineraryPage.sightSeeingItem.expectedItemDuration.toString().startsWith(resultData.activity.paymentpage.cardDetails.date.toString()),"date is not displayed in payment summery card.")
        assertionEquals(resultData.activity.paymentpage.cardDetails.name,resultData.activity.itineraryPage.sightSeeingItem.expectedHotelName,"Sight Seeing is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.activity.itineraryPage.sightSeeingItem.expectedItemDuration.toString().contains(resultData.activity.paymentpage.cardDetails.pax.toString()),"pax not matched in payment page against itinerary page.")
        softAssert.assertTrue(resultData.activity.itineraryPage.sightSeeingItem.expectedItemPrice.toString().startsWith(resultData.activity.paymentpage.cardDetails.price),"price is not matched against itinerary page.")
        //Total Gross
        assertionEquals(resultData.activity.paymentpage.totalGrass,resultData.activity.itineraryPage.sightSeeingItem.expectedItemPrice,"total gross is not matching.")

        //Total Net
        assertionEquals(resultData.activity.paymentpage.totalNet,data.expected.activityTotalNet,"total net price is displayed")

        //Total To Pay
        assertionEquals(resultData.activity.paymentpage.totalPay,resultData.activity.paymentpage.totalGrass,"total net price and total gross price are not equal.")

        //Hotel dispalyed in Payment summary
        assertionEquals(resultData.activity.paymentpage.hotelExistence,data.expected.notDispStatus, "Hotel Display In Payment Summary Block Actual & Expected Don't Match")

        //Transfer displayed in Payment summary
        assertionEquals(resultData.activity.paymentpage.transferExistence,data.expected.notDispStatus, "Transfer Display In Payment Summary Block Actual & Expected Don't Match")

    }


    protected def verifysavedAddress(PaymentsTestResultData resultData,PaymentsTestData data) {

        at PaymentsPage
        //saved address txt
        assertionEquals(resultData.hotel.paymentpage.savedAddressTxtdispStatus,data.expected.dispStatus, "Saved Address Text Actual & Expected Don't Match")

        //Radio button display status
        assertionEquals(resultData.hotel.paymentpage.savedAddressRadioBtnDispStatus,data.expected.dispStatus, "Use Saved Address Radio Button Display Actual & Expected Don't Match")

    }

    protected def verifyBookTCOverlay(PaymentsTestData data,PaymentsTestResultData resultData){
        assertionEquals(resultData.hotel.itineraryOverlay.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"Hotel name not displayed in overlay." )
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.checkBoxCheckedStatus.get(0),"Checkbox 1 is not selected by default")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.checkBoxCheckedStatus.get(1),"Checkbox 2 is not selected by default")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.travelerName.getAt(0).toString().startsWith(defaultData.expected.traveller),"traveler1 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(defaultData.expected.traveler2.toString().endsWith(resultData.hotel.itineraryOverlay.travelerName.getAt(1).toString()),"traveler2 is not dsiplayed in T & C Overlay")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.expandStatus,"Clicking on remarks link not expanded")
        softAssert.assertFalse(resultData.hotel.itineraryOverlay.colapseStatus,"Clicking on remarks link not collapsed.")
        softAssert.assertTrue(resultData.hotel.itineraryOverlay.bookNowEnabledStatus,"Book now button is disabled.")
    }

    protected def verifyPaymentPagePayNow(PaymentsTestResultData resultData){
       // softAssert.assertTrue(resultData.hotel.paymentpage.okOverlayStatus,"Overlay is not displayed in payment page." )
        softAssert.assertTrue( resultData.hotel.paymentpage.paymentDispStatus,"clicking on booknow payment page is not returned")
        softAssert.assertTrue(resultData.hotel.paymentpage.logoStatus,"TravelerCube logo is not displayed in payment page.")
        softAssert.assertTrue(resultData.hotel.paymentpage.mainMenuLinks,"Main menu links not displayed in payment page header.")
        softAssert.assertTrue(resultData.hotel.paymentpage.miniLinks,"Icons not displayed in header in payment page.")
    }




    protected def verifyPaymentSummeryBlock(PaymentsTestResultData resultData, ClientData clientData){
        softAssert.assertTrue( resultData.hotel.paymentpage.paymentSummeryLabel,"Paymentsummery label not displayed." )
        assertList(resultData.hotel.paymentpage.paymentSummeryLables,defaultData.expected.paymentSummeryBlock,"payment summery labels not displayed.")
        if(resultData.hotel.paymentpage.cardDetails.bookingIdStatus.toString().equals("false")){
            softAssert.assertFalse(resultData.hotel.paymentpage.cardDetails.bookingIdStatus,"booking id is not displayed in payment summery card.")
        }
        else{
            softAssert.assertTrue(resultData.hotel.paymentpage.cardDetails.bookingIdStatus,"booking id is displayed in payment summery card.")
        }
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().startsWith(resultData.hotel.paymentpage.cardDetails.date.toString()),"date is not displayed in payment summery card.")
        assertionEquals(resultData.hotel.paymentpage.cardDetails.name,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"hotel name is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(resultData.hotel.paymentpage.cardDetails.pax.toString()),"pax not matched in payment page against itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice.toString().startsWith(resultData.hotel.paymentpage.cardDetails.price),"price is not matched against itinerary page.")
        //If condition validation for multiroom price from itinerary page against payment page
        if(size>1){
            assertionEquals(resultData.hotel.paymentpage.totalGrass,resultData.hotel.itineraryPage.hotelItem.totalPrice,"total price is not matching.")
        }else{

            assertionEquals(resultData.hotel.paymentpage.totalGrass,resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,"total gross is not matching.")
        }

            softAssert.assertTrue(Float.parseFloat(resultData.hotel.paymentpage.totalGrass.replaceAll( clientData.currency, "").toString()) >= Float.parseFloat(resultData.hotel.paymentpage.totalNet.replaceAll( clientData.currency, "").toString()), "net price is not less then gross price")
        assertionEquals(resultData.hotel.paymentpage.totalPay,resultData.hotel.paymentpage.totalNet,"total net price and total price are not equal.")

    }

    protected def verifyPaymentSummeryBlockHotel(PaymentsTestResultData resultData){
        softAssert.assertTrue( resultData.hotel.paymentpage.paymentSummeryLabel,"Paymentsummery label not displayed." )
        assertList(resultData.hotel.paymentpage.paymentSummeryLables,defaultData.expected.paymentSummeryBlock,"payment summery labels not displayed.")
        if(resultData.hotel.paymentpage.cardDetails.bookingIdStatus.toString().equals("false")){
            softAssert.assertFalse(resultData.hotel.paymentpage.cardDetails.bookingIdStatus,"booking id is not displayed in payment summery card.")
        }
        else{
            softAssert.assertTrue(resultData.hotel.paymentpage.cardDetails.bookingIdStatus,"booking id is displayed in payment summery card.")
        }
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().startsWith(resultData.hotel.paymentpage.cardDetails.date.toString()),"date is not displayed in payment summery card.")
        assertionEquals(resultData.hotel.paymentpage.cardDetails.name,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"hotel name is not matched as per itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(resultData.hotel.paymentpage.cardDetails.pax.toString()),"pax not matched in payment page against itinerary page.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice.toString().startsWith(resultData.hotel.paymentpage.cardDetails.price),"price is not matched against itinerary page.")
        //If condition validation for multiroom price from itinerary page against payment page
        if(size>1){
            //assertionEquals(resultData.hotel.paymentpage.totalGrass,resultData.hotel.itineraryPage.hotelItem.totalPrice,"total price is not matching.")
        }else{

            //assertionEquals(resultData.hotel.paymentpage.totalGrass,resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,"total gross is not matching.")
        }

        softAssert.assertTrue(Float.parseFloat(resultData.hotel.paymentpage.totalGrass.replaceAll(" GBP", "").toString()) >= Float.parseFloat(resultData.hotel.paymentpage.totalNet.replaceAll(" GBP", "").toString()), "net price is not less then gross price")
        //assertionEquals(resultData.hotel.paymentpage.totalPay,resultData.hotel.paymentpage.totalNet,"total net price and total price are not equal.")

    }


    def verifyselectPaymentType(PaymentsTestResultData resultData){

        assertionEquals(resultData.hotel.paymentpage.securePayHeader,defaultData.expected.paymentHeader,"securePayment header not displayed.")
        assertionEquals(resultData.hotel.paymentpage.securePaySubHeader,defaultData.expected.paymentSubHeader,"secure payment sub header not displayed.")
        softAssert.assertTrue(resultData.hotel.paymentpage.cardOptionsStatus,"card option not displayed.")
        softAssert.assertFalse(resultData.hotel.paymentpage.buttonStatus,"button is enabled." )
    }

    def verfiyCardDetails(PaymentsTestResultData resultData,String cardNum, String securityCode){
        //Following assertions committed as per new requirement in payment page
        //softAssert.assertTrue(resultData.hotel.paymentpage.cardLabels.getAt(0).toString().trim().contains(defaultData.expected.cardLabels[0]),"payment method label not displayed.")
        //softAssert.assertTrue(resultData.hotel.paymentpage.cardLabels.getAt(0).toString().trim().contains(defaultData.expected.cardLabels[0]),"payment option visa not displayed")
        softAssert.assertTrue(resultData.hotel.paymentpage.cardImg,"payment option image not displayed.")
       // softAssert.assertTrue(resultData.hotel.paymentpage.totalPay.toString().contains(resultData.hotel.paymentpage.cardLabels.getAt(1).toString().split(" ").getAt(1).toString()),"total pay in payment summery is not matching with seure payment.")
        assertionEquals(resultData.hotel.paymentpage.indicatesStaticTxt.toString(),defaultData.expected.cardLabels[1].toString(),"*indicates required field text not displayed")
        softAssert.assertTrue(resultData.hotel.paymentpage.cardLabels.getAt(0).toString().startsWith(defaultData.expected.cardLabels[2].toString()),"card number label not displayed.")
        assertionEquals(resultData.hotel.paymentpage.enteredFieldsValues.getAt(0).toString(),cardNum,"card num not matching with entered value.")
       // softAssert.assertTrue(resultData.hotel.paymentpage.cardLabels.getAt(4).toString().contains(defaultData.expected.cardLabels[3]),"start date label not displayed.")
        softAssert.assertTrue(resultData.hotel.paymentpage.expireMonthSelectSize .toString().toInteger().equals(1),"select block expire month are not equal to 1")

        assertionEquals(resultData.hotel.paymentpage.expireMonthSelectDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")
        softAssert.assertTrue( resultData.hotel.paymentpage.expireYearSelectSize.toString().toInteger().equals(1),"select block expire year are not equal to 1")
        assertionEquals(resultData.hotel.paymentpage.expireYearSelectDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")
      //  assertList(resultData.hotel.paymentpage.startDateOptions,defaultData.expected.dateOptions,"options in UI not matching with expected data")
        //softAssert.assertTrue(resultData.hotel.paymentpage.dateYearOptionsSize.toString().toInteger()>0,"year options not available.")
        assertList(resultData.hotel.paymentpage.expireDateOptions,defaultData.expected.dateOptions,"expire date options in UI not matching with expected data")
        softAssert.assertTrue(resultData.hotel.paymentpage.expireDateYrOptionsSize.toString().toInteger()>0,"expire date year options not available.")

        softAssert.assertTrue(resultData.hotel.paymentpage.cardLabels.getAt(2).toString().contains(defaultData.expected.cardLabels[4]),"exipre month label not displayed.")
        assertionEquals(resultData.hotel.paymentpage.cardLabels.getAt(1).toString(),defaultData.expected.cardLabels[5],"cardholder name label not displayed.")
        assertionEquals(resultData.hotel.paymentpage.enteredFieldsValues.getAt(1).toString(),defaultData.expected.cardHolder,"card holder name not matching.")
        softAssert.assertTrue(resultData.hotel.paymentpage.cardLabels.getAt(5).toString().startsWith(defaultData.expected.cardLabels[6].toString()),"security code label not displayed.")
        assertionEquals(resultData.hotel.paymentpage.enteredFieldsValues.getAt(4).toString(),securityCode,"entered security code not matching in filed.")
    }

    def verfiyCardDetailsTransfers(PaymentsTestResultData resultData,String cardNum, String securityCode){
        //softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(0).toString().trim().contains(defaultData.expected.cardLabels[0]),"payment method label not displayed.")
        //softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(0).toString().trim().contains(defaultData.expected.cardLabels[0]),"payment option visa not displayed")
        softAssert.assertTrue(resultData.transfer.paymentpage.cardImg,"payment option image not displayed.")
        //softAssert.assertTrue(resultData.transfer.paymentpage.totalPay.toString().contains(resultData.transfer.paymentpage.cardLabels.getAt(1).toString().split(" ").getAt(1).toString()),"total pay in payment summery is not matching with seure payment.")
        //assertionEquals(resultData.transfer.paymentpage.cardLabels.getAt(2).toString(),defaultData.expected.cardLabels[1],"*indicates required field text not displayed")
        softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(0).toString().startsWith(defaultData.expected.cardLabels[2].toString()),"card number label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.enteredFieldsValues.getAt(0).toString(),cardNum,"card num not matching with entered value.")
        softAssert.assertTrue(resultData.transfer.paymentpage.expireMonthSelectSize .toString().toInteger().equals(1),"select block expire month are not equal to 1")

        //softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(4).toString().contains(defaultData.expected.cardLabels[3]),"start date label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.expireMonthSelectDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")
        softAssert.assertTrue( resultData.transfer.paymentpage.expireYearSelectSize.toString().toInteger().equals(1),"select block expire year are not equal to 1")
        assertionEquals(resultData.transfer.paymentpage.expireYearSelectDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")


        assertList(resultData.transfer.paymentpage.expireDateOptions,defaultData.expected.dateOptions,"expire date options in UI not matching with expected data")
        softAssert.assertTrue(resultData.transfer.paymentpage.expireDateYrOptionsSize.toString().toInteger()>0,"expire date year options not available.")

        softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(2).toString().contains(defaultData.expected.cardLabels[4]),"exipre month label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.cardLabels.getAt(1).toString(),defaultData.expected.cardLabels[5],"cardholder name label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.enteredFieldsValues.getAt(1).toString(),defaultData.expected.cardHolder,"card holder name not matching.")
        softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(5).toString().startsWith(defaultData.expected.cardLabels[6].toString()),"security code label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.enteredFieldsValues.getAt(4).toString(),securityCode,"entered security code not matching in filed.")


/*
        ///

       // softAssert.assertTrue(resultData.transfer.paymentpage.startDateSelectTag.toString().toInteger().equals(2),"select blocks are not equal to 2")
        //assertionEquals(resultData.transfer.paymentpage.selectOptionDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")
        //softAssert.assertTrue(resultData.transfer.paymentpage.monthSelectTag.toString().toInteger().equals(2),"select blocks are not equal to 2")
        //assertionEquals(resultData.transfer.paymentpage.selectMonthDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")
        assertList(resultData.transfer.paymentpage.startDateOptions,defaultData.expected.dateOptions,"options in UI not matching with expected data")
        softAssert.assertTrue(resultData.transfer.paymentpage.dateYearOptionsSize.toString().toInteger()>0,"year options not available.")
        assertList(resultData.transfer.paymentpage.expireDateOptions,defaultData.expected.dateOptions,"expire date options in UI not matching with expected data")
        softAssert.assertTrue(resultData.transfer.paymentpage.expireDateYrOptionsSize.toString().toInteger()>0,"expire date year options not available.")
        softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(5).toString().contains(defaultData.expected.cardLabels[4]),"exipre month label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.cardLabels.getAt(6).toString(),defaultData.expected.cardLabels[5],"cardholder name label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.enteredFieldsValues.getAt(5).toString(),defaultData.expected.cardHolder,"card holder name not matching.")
        softAssert.assertTrue(resultData.transfer.paymentpage.cardLabels.getAt(7).toString().startsWith(defaultData.expected.cardLabels[6].toString()),"security code label not displayed.")
        assertionEquals(resultData.transfer.paymentpage.enteredFieldsValues.getAt(6).toString(),securityCode,"entered security code not matching in filed.")
        */
    }

    def verfiyCardDetailsActivity(PaymentsTestResultData resultData,String cardNum, String securityCode){
        softAssert.assertTrue(resultData.activity.paymentpage.cardLabels.getAt(0).toString().trim().contains(defaultData.expected.cardLabels[0]),"payment method label not displayed.")
        softAssert.assertTrue(resultData.activity.paymentpage.cardLabels.getAt(0).toString().trim().contains(defaultData.expected.cardLabels[0]),"payment option visa not displayed")
        softAssert.assertTrue(resultData.activity.paymentpage.cardImg,"payment option image not displayed.")
        softAssert.assertTrue(resultData.activity.paymentpage.totalPay.toString().contains(resultData.activity.paymentpage.cardLabels.getAt(1).toString().split(" ").getAt(1).toString()),"total pay in payment summery is not matching with seure payment.")
        assertionEquals(resultData.activity.paymentpage.cardLabels.getAt(2).toString(),defaultData.expected.cardLabels[1],"*indicates required field text not displayed")
        softAssert.assertTrue(resultData.activity.paymentpage.cardLabels.getAt(3).toString().startsWith(defaultData.expected.cardLabels[2].toString()),"card number label not displayed.")
        assertionEquals(resultData.activity.paymentpage.enteredFieldsValues.getAt(0).toString(),cardNum,"card num not matching with entered value.")
        softAssert.assertTrue(resultData.activity.paymentpage.cardLabels.getAt(4).toString().contains(defaultData.expected.cardLabels[3]),"start date label not displayed.")
        softAssert.assertTrue(resultData.activity.paymentpage.startDateSelectTag.toString().toInteger().equals(2),"select blocks are not equal to 2")
        assertionEquals(resultData.activity.paymentpage.selectOptionDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")
        softAssert.assertTrue(resultData.activity.paymentpage.monthSelectTag.toString().toInteger().equals(2),"select blocks are not equal to 2")
        assertionEquals(resultData.activity.paymentpage.selectMonthDefaultValue,defaultData.expected.defaultSelectValue,"please select option is not selected by default.")
        assertList(resultData.activity.paymentpage.startDateOptions,defaultData.expected.dateOptions,"options in UI not matching with expected data")
        softAssert.assertTrue(resultData.activity.paymentpage.dateYearOptionsSize.toString().toInteger()>0,"year options not available.")
        assertList(resultData.activity.paymentpage.expireDateOptions,defaultData.expected.dateOptions,"expire date options in UI not matching with expected data")
        softAssert.assertTrue(resultData.activity.paymentpage.expireDateYrOptionsSize.toString().toInteger()>0,"expire date year options not available.")
        softAssert.assertTrue(resultData.activity.paymentpage.cardLabels.getAt(5).toString().contains(defaultData.expected.cardLabels[4]),"exipre month label not displayed.")
        assertionEquals(resultData.activity.paymentpage.cardLabels.getAt(6).toString(),defaultData.expected.cardLabels[5],"cardholder name label not displayed.")
        assertionEquals(resultData.activity.paymentpage.enteredFieldsValues.getAt(5).toString(),defaultData.expected.cardHolder,"card holder name not matching.")
        softAssert.assertTrue(resultData.activity.paymentpage.cardLabels.getAt(7).toString().startsWith(defaultData.expected.cardLabels[6].toString()),"security code label not displayed.")
        assertionEquals(resultData.activity.paymentpage.enteredFieldsValues.getAt(6).toString(),securityCode,"entered security code not matching in filed.")
    }

    def verifyAddNewAddress(PaymentsTestResultData resultData){
        softAssert.assertTrue(resultData.hotel.paymentpage.addrStatus,"address section not displayed.")
        softAssert.assertTrue(resultData.hotel.paymentpage.radioButtonStatus,"address radio button not avialable.")
        softAssert.assertTrue(resultData.hotel.paymentpage.billingAddrSectionStatus,"billing addr section not displayed.")
        assertList(defaultData.expected.addrLabels,resultData.hotel.paymentpage.addrLabels,"address labels are not matching as expected.")
        assertList(resultData.hotel.paymentpage.txtFieldValues,defaultData.expected.Addr1,"address line text filed values are not matching.")
    }

    def verifyReturnToItinerary(PaymentsTestResultData resultData){
        softAssert.assertTrue(resultData.hotel.paymentpage.returnItineraryStatus,"returnToItinerary button is not available.")
    }
    def verifyMakePaymentButton(PaymentsTestResultData resultData){
        softAssert.assertTrue(resultData.hotel.paymentpage.paymentStatus,"payment button is not enabled.")
    }
    def verifyBookedConfirmedOverlay(PaymentsTestData data,PaymentsTestResultData resultData){
        assertionEquals(resultData.hotel.confirmBookingOverlay.header,defaultData.expected.confirmStatus,"Booking confirmed header not displayed.")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.logo,"Traveler Cube Logo is not displayed." )
        assertionEquals(resultData.hotel.confirmBookingOverlay.bookingID,resultData.hotel.itineraryPage.bookedItem.expectedBookedItemBookingID,"Booking id is not matching.")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryID.toString()),"Itinerary ID not matching")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryName.toString()),"Itinerary name is not matching")
        assertionEquals(resultData.hotel.confirmBookingOverlay.travelers.getAt(0), resultData.hotel.itineraryPage.travellerDetails.getAt(0),"traveler1 not matching with itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.travelers.getAt(1),resultData.hotel.itineraryPage.travellerDetails.getAt(3),"traveler2 not matching with itinerary")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().contains(resultData.hotel.confirmBookingOverlay.depatureDate),"depature date is not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"Hotel name not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.addr,data.expected.hotelAddr,"hotel addr is not matching.")
        String pax = resultData.hotel.confirmBookingOverlay.paxDesc.replaceAll("\\n"," ").split("S").getAt(1)
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(pax),"pax are not matching against itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.date,resultData.hotel.itineraryPage.hotelItem.expectedItemDuration,"date is not matching")
        assertionEquals(resultData.hotel.confirmBookingOverlay.price,resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,"price is not matching with itinerary")
        String commissionGlobal = resultData.hotel.itineraryPage.hotelItem.expectedCommission.toString()
        Double commission = resultData.hotel.confirmBookingOverlay.price.toString().split(" ").getAt(0).toString().toDouble()
        Double  expectedCommission = Double.valueOf(commissionGlobal.replaceAll("[^\\d.]+|\\.(?!\\d)", ""))
        commission =(commission*expectedCommission)/100
       commission = Math.round(commission)
        Double commissionOverlay = Math.round(resultData.hotel.confirmBookingOverlay.commission.toString().split(" ").getAt(1).toString().toDouble())
        assertionEquals(commissionOverlay,commission,"commission is not matching.")
       // softAssert.assertTrue(resultData.hotel.itineraryPage.itineraryPageStatus,"Itinerary page not displayed")
    }

    def verifyBookingonfirmedOverlay(PaymentsTestData data,PaymentsTestResultData resultData){
        assertionEquals(resultData.hotel.confirmBookingOverlay.header,defaultData.expected.confirmStatus,"Booking confirmed header not displayed.")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.logo,"Traveler Cube Logo is not displayed." )
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.bookingIDDisplayed,"Booking Id is not displayed." )
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryID.toString()),"Itinerary ID not matching")
        softAssert.assertTrue(resultData.hotel.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryName.toString()),"Itinerary name is not matching")
        assertionEquals(resultData.hotel.confirmBookingOverlay.travelers.getAt(0), resultData.hotel.itineraryPage.travellerDetails.getAt(0),"traveler1 not matching with itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.travelers.getAt(1),resultData.hotel.itineraryPage.travellerDetails.getAt(3),"traveler2 not matching with itinerary")
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration.toString().contains(resultData.hotel.confirmBookingOverlay.depatureDate),"depature date is not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"Hotel name not matching.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.addr,resultData.hotel.searchResults.hotelItem.address,"Hotel Address Actual & Expected don't match")
        String pax = resultData.hotel.confirmBookingOverlay.paxDesc.replaceAll("\\n"," ").split("S").getAt(1)
        softAssert.assertTrue(resultData.hotel.itineraryPage.hotelItem.expectedItemDesc.toString().contains(pax),"pax are not matching against itinerary page.")
        assertionEquals(resultData.hotel.confirmBookingOverlay.date,resultData.hotel.itineraryPage.hotelItem.expectedItemDuration,"date is not matching")
        assertionEquals(resultData.hotel.confirmBookingOverlay.price,resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,"price is not matching with itinerary")
        Float commission = resultData.hotel.confirmBookingOverlay.price.toString().split(" ").getAt(0).toString().toFloat()
        String commissionGlobal = resultData.hotel.itineraryPage.hotelItem.expectedCommission.toString()
       Float  expectedCommission = Float.valueOf(commissionGlobal.replaceAll("[^\\d.]+|\\.(?!\\d)", ""))
        commission = (commission*expectedCommission)/100
        Float commissionOverlay = resultData.hotel.confirmBookingOverlay.commission.toString().split(" ").getAt(1).toString().toFloat()
        assertionEquals(commissionOverlay,commission,"commission is not matching.")
       // softAssert.assertTrue(resultData.hotel.itineraryPage.itineraryPageStatus,"Itinerary page not displayed")
    }

    def verifybookingConfirmedOverlayTransfers(PaymentsTestData data,PaymentsTestResultData resultData){
        //booking confirmed overlay should be displayed
        assertionEquals(resultData.transfer.confirmBookingOverlay.header,defaultData.expected.confirmStatus,"Booking confirmed header not displayed.")

        //An update of your Itinerary will be sent to you at your registered email address text should be displayed
        assertionEquals(resultData.transfer.confirmBookingOverlay.subheaderTxt,defaultData.expected.subHeaderTxt,"Booking confirmed header not displayed.")
        //travelcube icon
        softAssert.assertTrue(resultData.transfer.confirmBookingOverlay.logo,"Traveler Cube Logo is not displayed." )

        //booking id text and number
        softAssert.assertTrue(resultData.transfer.confirmBookingOverlay.bookingIDDisplayed,"Booking Id is not displayed." )
        //Itinerary number and name
        softAssert.assertTrue(resultData.transfer.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryID.toString()),"Itinerary ID not matching")
        softAssert.assertTrue(resultData.transfer.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryName.toString()),"Itinerary name is not matching")
        //Traveller Details
        assertionEquals(resultData.transfer.confirmBookingOverlay.travelers.getAt(0), resultData.hotel.itineraryPage.travellerDetails.getAt(0),"traveler1 not matching with itinerary page.")
        assertionEquals(resultData.transfer.confirmBookingOverlay.travelers.getAt(1),resultData.hotel.itineraryPage.travellerDetails.getAt(3),"traveler2 not matching with itinerary")
        //Departure date
        softAssert.assertTrue(resultData.transfer.itineraryPage.transferItem.expectedItemDuration.toString().contains(resultData.transfer.confirmBookingOverlay.depatureDate),"depature date is not matching.")
        //Transfer Details
            //Transfer Name
        assertionEquals(resultData.transfer.confirmBookingOverlay.transferName,resultData.transfer.itineraryPage.transferItem.expectedHotelName,"Transfer name not matching.")

            //Transfer Description & Pax
        assertionEquals(resultData.transfer.confirmBookingOverlay.actualTransferDescBookingConf_txt,resultData.transfer.confirmBookingOverlay.expectedTransferDescBookingConf_txt,"Transfer Description & Pax Actual & Expected don't match")

        //pickup
        assertionEquals(resultData.transfer.confirmBookingOverlay.actualPickupATH,resultData.transfer.confirmBookingOverlay.expectedPickupATH,"Transfer Pick Up Actual & Expected don't match")

         // dropoff
        //assertionEquals(resultData.transfer.confirmBookingOverlay.actualDropOffATH,resultData.transfer.confirmBookingOverlay.expectedDropOffATH,"Transfer Drop Off Actual & Expected don't match")
        //assertContains(resultData.transfer.confirmBookingOverlay.expectedDropOffATH,resultData.transfer.confirmBookingOverlay.actualDropOffATH,"Transfer Drop Off Actual & Expected don't match")

        //Transfer Service Date and time
        assertionEquals(resultData.transfer.confirmBookingOverlay.actualTransferDateAndTimeInBookingConf.replaceAll(" ",""),resultData.transfer.itineraryPage.transferItem.expectedItemDuration.replaceAll(" ",""),"Transfer Date & Time Of Service Actual & Expected don't match")

        //Transfer price
        assertionEquals(resultData.transfer.confirmBookingOverlay.price,resultData.transfer.itineraryPage.transferItem.expectedItemPrice,"price is not matching with itinerary")
        //Commissioin
        Float commission = resultData.transfer.confirmBookingOverlay.price.toString().split(" ").getAt(0).toString().toFloat()
        commission = (commission*12)/100
        Float commissionOverlay = resultData.transfer.confirmBookingOverlay.commission.toString().split(" ").getAt(1).toString().toFloat()
        assertionEquals(commissionOverlay,commission,"commission is not matching.")

    }

    def verifybookingConfirmedOverlayActivity(PaymentsTestData data,PaymentsTestResultData resultData){
        //booking confirmed overlay should be displayed
        assertionEquals(resultData.activity.confirmBookingOverlay.header,defaultData.expected.confirmStatus,"Activity, Booking confirmed header not displayed.")

        //An update of your Itinerary will be sent to you at your registered email address text should be displayed
        assertionEquals(resultData.activity.confirmBookingOverlay.subheaderTxt,defaultData.expected.subHeaderTxt,"Activity, Booking confirmed header not displayed.")
        //travelcube icon
        softAssert.assertTrue(resultData.activity.confirmBookingOverlay.logo,"Activity, Traveler Cube Logo is not displayed." )

        //booking id text and number
        softAssert.assertTrue(resultData.activity.confirmBookingOverlay.bookingIDDisplayed,"Booking Id is not displayed." )
        //Itinerary number and name
        softAssert.assertTrue(resultData.activity.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryID.toString()),"Itinerary ID not matching")
        softAssert.assertTrue(resultData.activity.confirmBookingOverlay.itineraryID.toString().contains(resultData.hotel.itineraryPage.itineraryName.toString()),"Itinerary name is not matching")
        //Traveller Details
        assertionEquals(resultData.activity.confirmBookingOverlay.travelers.getAt(0), resultData.hotel.itineraryPage.travellerDetails.getAt(0),"traveler1 not matching with itinerary page.")
        assertionEquals(resultData.activity.confirmBookingOverlay.travelers.getAt(1),resultData.hotel.itineraryPage.travellerDetails.getAt(3),"traveler2 not matching with itinerary")
        //Departure date
        softAssert.assertTrue(resultData.activity.itineraryPage.sightSeeingItem.expectedItemDuration.toString().contains(resultData.activity.confirmBookingOverlay.depatureDate),"depature date is not matching.")
        //Activity Details
        //Activity Name
        assertionEquals(resultData.activity.confirmBookingOverlay.activityName,resultData.activity.itineraryPage.sightSeeingItem.expectedHotelName,"Activity name not matching.")

        //Should display Duration ,

        //Pax
        assertionEquals(resultData.activity.confirmBookingOverlay.activityPax,resultData.activity.confirmBookingOverlay.expectedactivityPax,"Activity, Pax Actual & Expected don't match")

        // Departure Point

       //Activity Date and time
        assertionEquals(resultData.activity.confirmBookingOverlay.activityDateAndTimeTxt,resultData.activity.confirmBookingOverlay.expectedactivityDateAndTimeTxt,"Activity Date & Time Of Service Actual & Expected don't match")

        //Activity price
        assertionEquals(resultData.activity.confirmBookingOverlay.price,resultData.activity.itineraryPage.sightSeeingItem.expectedItemPrice,"Activity, price is not matching with itinerary")
        //Commissioin
        Float commission = resultData.activity.confirmBookingOverlay.price.toString().split(" ").getAt(0).toString().toFloat()
        commission = (commission*12)/100
        Float commissionOverlay = resultData.activity.confirmBookingOverlay.commission.toString().split(" ").getAt(1).toString().toFloat()
        assertionEquals(commissionOverlay,commission,"commission is not matching.")

    }


    def verifyItineraryPageAfterPayment(PaymentsTestResultData resultData){
        assertionEquals(resultData.hotel.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Booked item header not displayed.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem.expectedBookedItemBookingID.toString().contains(resultData.hotel.confirmBookingOverlay.bookingID),"booking id not matched" )
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedItemStatus,defaultData.expected.bookingConfirmedStatus,"Confirmed status not displayed.")
        assertionEquals(resultData.hotel.itineraryPage.item1Details.itemSectionAmendStatus,defaultData.expected.amendStatus,"Amend Status not displayed.")
        assertionEquals(resultData.hotel.itineraryPage.item1Details.itemCancelStatus,defaultData.expected.cancelStatus,"Cancel status not displayed for booked items")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedHotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"HotelName not matching after payment.")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedStarRating,resultData.hotel.itineraryPage.hotelItem.expectedStarRating,"Star Rating not matching after payment.")
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedItemDesc,resultData.hotel.itineraryPage.hotelItem.expectedItemDesc,"Desc not matching")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem.expectedItemDuration.toString().contains(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration),"Duration is not matching after payment.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.bookedItem.expectedItemDuration.toString().startsWith(resultData.hotel.itineraryPage.hotelItem.expectedItemDuration),"date not matching after payment.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.hotel.itineraryPage.item1Details.traveler1.toString()),"traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.hotel.itineraryPage.item1Details.traveler2.toString()),"traveler 2 is not matching with traveler details section")
        assertionEquals(resultData.hotel.itineraryPage.hotelItem.expectedItemPrice,resultData.hotel.itineraryPage.bookedItem.expectedItemPrice,"price is not matching after payment.")

    }

    def verifyItineraryPageAfterPaymentHotelMultiModule(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.hotel.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Hotel Item, Booked item header text not displayed.")
        //Booked Item section - should have only hotel in the section
            //count the items in Booked Item section
        assertionEquals(resultData.hotel.itineraryPage.bookedItemsCount,data.expected.bookedItemsCount,"Hotel Item, Booked item count Actual and Expected don't match")
            //only hotel in the section
        assertionEquals(resultData.hotel.itineraryPage.bookedItemItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.hotel.itineraryPage.bookedItemBookingId,resultData.hotel.confirmBookingOverlay.bookingID ,"Hotel Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.hotel.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Hotel Item, Booked item Confirmed status Actual and Expected don't match")

        //Amend button text
        assertionEquals(resultData.hotel.itineraryPage.itemSectionAmendTxt,data.expected.amndBtntxt ,"Hotel Item, Booked item Amend Text Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.hotel.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Hotel Item, Booked item Cancel Text Actual and Expected don't match")

        //Hotel details

        //hotel name ,
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"HotelName not matching after payment.")
        //star rating,
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelstarRating,resultData.hotel.itineraryPage.hotelItem.expectedStarRating,"Star Rating not matching after payment.")
        //room type , pax,
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelroomTypePax,resultData.hotel.itineraryPage.hotelItem.expectedItemDesc,"Desc not matching")

        //checkin date number of stay nights
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelCheckInDateNoOfNights,resultData.hotel.confirmBookingOverlay.date,"Check In Days No Of Nights Actual And Expected don't matching")

        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.hotel.itineraryPage.bookedItems.traveler1.toString()),"traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.hotel.itineraryPage.bookedItems.traveler2.toString()),"traveler 2 is not matching with traveler details section")

        //total amount
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelTotalAmount,resultData.hotel.confirmBookingOverlay.price,"Hotel Item, Total Amount actual And Expected don't matching")

        //cancellation charges overlay should be dispalyed
        //check for the Cancellation and Amendment Charges section
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.cancellationChargesTxt,resultData.hotel.itineraryPage.hotelItem.cancelchargesTxt,"Hotel Item, Cancellation Charges actual And Expected don't matching")

        //overlay should be dismissed and itinerary page should be displayed.
    }

    def verifyItinryPageAfterPaymentHotelMultiModule(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.hotel.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Hotel Item, Booked item header text not displayed.")
        //Booked Item section - should have only hotel in the section
        //count the items in Booked Item section
        assertionEquals(resultData.hotel.itineraryPage.bkdItemsCount,data.expected.bookedItemsCount+2,"Hotel Item, Booked item count Actual and Expected don't match")
        //only hotel in the section
        assertionEquals(resultData.hotel.itineraryPage.bookedItemItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.hotel.itineraryPage.bookedItemBookingId,resultData.hotel.confirmBookingOverlay.bookingID ,"Hotel Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.hotel.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Hotel Item, Booked item Confirmed status Actual and Expected don't match")

        //Amend button text
        assertionEquals(resultData.hotel.itineraryPage.itemSectionAmendTxt,data.expected.amndBtntxt ,"Hotel Item, Booked item Amend Text Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.hotel.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Hotel Item, Booked item Cancel Text Actual and Expected don't match")

        //Hotel details

        //hotel name ,
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelName,resultData.hotel.itineraryPage.hotelItem.expectedHotelName,"HotelName not matching after payment.")
        //star rating,
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelstarRating,resultData.hotel.itineraryPage.hotelItem.expectedStarRating,"Star Rating not matching after payment.")
        //room type , pax,
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelroomTypePax,resultData.hotel.itineraryPage.hotelItem.expectedItemDesc,"Desc not matching")

        //checkin date number of stay nights
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelCheckInDateNoOfNights,resultData.hotel.confirmBookingOverlay.date,"Check In Days No Of Nights Actual And Expected don't matching")

        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.hotel.itineraryPage.bookedItems.traveler1.toString()),"traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.hotel.itineraryPage.bookedItems.traveler2.toString()),"traveler 2 is not matching with traveler details section")

        //total amount
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.hotelTotalAmount,resultData.hotel.confirmBookingOverlay.price,"Hotel Item, Total Amount actual And Expected don't matching")

        //cancellation charges overlay should be dispalyed
        //check for the Cancellation and Amendment Charges section
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.cancellationChargesTxt,resultData.hotel.itineraryPage.hotelItem.cancelchargesTxt,"Hotel Item, Cancellation Charges actual And Expected don't matching")

        //overlay should be dismissed and itinerary page should be displayed.
    }


    def verifybookedItemSectionAfterPayLater(PaymentsTestData data,PaymentsTestResultData resultData){

        verifyItineraryPageAfterPaymentHotelMultiModule(data,resultData)

        //payment not taken text should be displayed .
        assertionEquals(resultData.hotel.itineraryPage.bookedItemPaymentNotTakenTxt,defaultData.expected.payLaterStatus,"Hotel Item, Payment Not Taken text not displayed.")

        // Pay button should be enabled .
        assertionEquals(resultData.hotel.itineraryPage.bookedItemPayButtonEnabledStatus,data.expected.dispStatus,"Hotel Item, Pay button enable status actual and expected don't match.")

        //Download Voucher Icon should not be displayed
        assertionEquals(resultData.hotel.itineraryPage.bookedItemDownloadVoucherIconDispStatus,data.expected.notDispStatus,"Hotel Item, Download voucher icon display status actual and expected don't match.")


    }

    def verifyItineraryPageAfterPaymentTransfersMultiModule(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.transfer.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Transfer Item, Booked item header text not displayed.")
        //Booked Item section - should have  hotel and transfer  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.transfer.itineraryPage.bookedItemsCount,data.expected.bookedItemsCount+1,"Transfer Item, Booked item count Actual and Expected don't match")
        //should have  hotel and transfer  in this section
        assertionEquals(resultData.transfer.itineraryPage.bookedHotelItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")
        assertionEquals(resultData.transfer.itineraryPage.bookedTransferItemName,resultData.transfer.searchResults.transferItem.transferName,"Transfer Item, Booked item Transfer name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.transfer.itineraryPage.bookedItemBookingId,resultData.transfer.confirmBookingOverlay.bookingID ,"Transfer Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.transfer.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Transfer Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.transfer.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Transfer Item, Booked item Cancel Text Actual and Expected don't match")

        //Transfer details

        //Transfer name ,
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferName,resultData.transfer.itineraryPage.transferItem.expectedHotelName,"Transfer Name not matching after payment.")
        //Pax
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferPax,resultData.transfer.itineraryPage.transferItem.pax,"Transfer Item, Pax not matching after payment.")
        //Pick and drop off
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferPickUpAndDropOff,resultData.transfer.itineraryPage.transferItem.expectedTransferDesc,"Transfer Item, Pick Up And Drop Off not matching after payment.")






        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler1.toString()),"Transfer Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler2.toString()),"Transfer Item, traveler 2 is not matching with traveler details section")

        //total amount
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferTotalAmount,resultData.transfer.confirmBookingOverlay.price,"Transfer Item, Total Amount actual And Expected don't matching")

        //cancellation charges overlay should be dispalyed
        //check for the Cancellation and Amendment Charges section
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.cancellationChargesTxt,resultData.transfer.itineraryPage.transferItem.cancelchargesTxt,"Transfer Item, Cancellation Charges actual And Expected don't matching")

        //overlay should be dismissed and itinerary page should be displayed.
    }


    def verifyItinryPageAfterPaymentTransfersMultiModule(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.transfer.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Transfer Item, Booked item header text not displayed.")
        //Booked Item section - should have  hotel and transfer  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.transfer.itineraryPage.bokedItemsCount,data.expected.bookedItemsCount+2,"Transfer Item, Booked item count Actual and Expected don't match")
        //should have  hotel and transfer  in this section
        //assertionEquals(resultData.transfer.itineraryPage.bookedHotelItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")
        assertionEquals(resultData.transfer.itineraryPage.bookedTransferItemName,resultData.transfer.searchResults.transferItem.transferName,"Transfer Item, Booked item Transfer name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.transfer.itineraryPage.bookedItemBookingId,resultData.transfer.confirmBookingOverlay.bookingID ,"Transfer Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.transfer.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Transfer Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.transfer.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Transfer Item, Booked item Cancel Text Actual and Expected don't match")

        //Transfer details

        //Transfer name ,
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferName,resultData.transfer.itineraryPage.transferItem.expectedHotelName,"Transfer Name not matching after payment.")
        //Pax
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferPax,resultData.transfer.itineraryPage.transferItem.pax,"Transfer Item, Pax not matching after payment.")
        //Pick and drop off
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferPickUpAndDropOff,resultData.transfer.itineraryPage.transferItem.expectedTransferDesc,"Transfer Item, Pick Up And Drop Off not matching after payment.")






        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler1.toString()),"Transfer Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler2.toString()),"Transfer Item, traveler 2 is not matching with traveler details section")

        //total amount
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferTotalAmount,resultData.transfer.confirmBookingOverlay.price,"Transfer Item, Total Amount actual And Expected don't matching")

        //cancellation charges overlay should be dispalyed
        //check for the Cancellation and Amendment Charges section
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.cancellationChargesTxt,resultData.transfer.itineraryPage.transferItem.cancelchargesTxt,"Transfer Item, Cancellation Charges actual And Expected don't matching")

        //overlay should be dismissed and itinerary page should be displayed.
    }


    def verifyItinryPageAfterPaymentTransferMultiModule(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.transfer.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Transfer Item, Booked item header text not displayed.")
        //Booked Item section - should have  hotel and transfer  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.transfer.itineraryPage.bookedItemsCount,data.expected.bookedItemsCount+1,"Transfer Item, Booked item count Actual and Expected don't match")
        //should have  hotel and transfer  in this section
        assertionEquals(resultData.transfer.itineraryPage.bookedSightSeeingItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"SightSeeing Item, Booked item Sightseeing name Actual and Expected don't match")
        assertionEquals(resultData.transfer.itineraryPage.bookedTransferItemName,resultData.transfer.searchResults.transferItem.transferName,"Transfer Item, Booked item Transfer name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.transfer.itineraryPage.bookedItemBookingId,resultData.transfer.confirmBookingOverlay.bookingID ,"Transfer Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.transfer.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Transfer Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.transfer.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Transfer Item, Booked item Cancel Text Actual and Expected don't match")

        //Transfer details

        //Transfer name ,
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferName,resultData.transfer.itineraryPage.transferItem.expectedHotelName,"Transfer Name not matching after payment.")
        //Pax
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferPax,resultData.transfer.itineraryPage.transferItem.pax,"Transfer Item, Pax not matching after payment.")
        //Pick and drop off
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferPickUpAndDropOff,resultData.transfer.itineraryPage.transferItem.expectedTransferDesc,"Transfer Item, Pick Up And Drop Off not matching after payment.")

        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler1.toString()),"Transfer Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler2.toString()),"Transfer Item, traveler 2 is not matching with traveler details section")

        //total amount
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferTotalAmount,resultData.transfer.confirmBookingOverlay.price,"Transfer Item, Total Amount actual And Expected don't matching")


    }


    def verifyItineraryPageAfterPaymentMultiModule(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.transfer.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Booked item header text not displayed.")
        //Booked Item section - should have  hotel and transfer  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.transfer.itineraryPage.bkdItemsCount,data.expected.bookedItemsCount+2,"Booked item count Actual and Expected don't match")
        //should have all three  in this section
        assertionEquals(resultData.transfer.itineraryPage.bookedHotelItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")
        assertionEquals(resultData.transfer.itineraryPage.bookedTransferItemName,resultData.transfer.searchResults.transferItem.transferName,"Transfer Item, Booked item Transfer name Actual and Expected don't match")
        assertionEquals(resultData.transfer.itineraryPage.bookedSightSeeingItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Item, Booked item Activity name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.transfer.itineraryPage.bkdItemBookingId,resultData.transfer.confirmBookingOverlay.bookingID ,"Transfer Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.transfer.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Transfer Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.transfer.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Transfer Item, Booked item Cancel Text Actual and Expected don't match")

        //Transfer details
        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler1.toString()),"Transfer Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler2.toString()),"Transfer Item, traveler 2 is not matching with traveler details section")

        //total amount
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.transferTotalAmount,resultData.transfer.confirmBookingOverlay.price,"Transfer Item, Total Amount actual And Expected don't matching")

        //Booked item subtotal
        assertionEquals(resultData.transfer.itineraryPage.bookedItems.bookedItemSubtotal,resultData.transfer.paymentpage.totalToPayAmountAndCurncy,"Booked Item Sub total Amount actual And Expected don't matching")


    }

    def verifyItineraryPageAfterPaymentMultiModuleHotel(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.hotel.itineraryPage.bookedItemHeader,defaultData.expected.bookedItemsHeader,"Booked item header text not displayed.")
        //Booked Item section - should have  hotel and transfer  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.hotel.itineraryPage.bkdItemsCount,data.expected.bookedItemsCount+2,"Booked item count Actual and Expected don't match")
        //should have all three  in this section
        assertionEquals(resultData.hotel.itineraryPage.bookedHotelItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")
        assertionEquals(resultData.hotel.itineraryPage.bookedTransferItemName,resultData.transfer.searchResults.transferItem.transferName,"Transfer Item, Booked item Transfer name Actual and Expected don't match")
        assertionEquals(resultData.hotel.itineraryPage.bookedSightSeeingItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Item, Booked item Activity name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.hotel.itineraryPage.bkdItemBookingId,resultData.hotel.confirmBookingOverlay.bookingID ,"Transfer Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.hotel.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Transfer Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.hotel.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Transfer Item, Booked item Cancel Text Actual and Expected don't match")

        //Transfer details
        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler1.toString()),"Transfer Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.transfer.itineraryPage.bookedItems.traveler2.toString()),"Transfer Item, traveler 2 is not matching with traveler details section")

        //total amount
        assertionEquals(resultData.hotel.itineraryPage.bookedItems.transferTotalAmount,resultData.hotel.confirmBookingOverlay.price,"Hotel Item, Total Amount actual And Expected don't matching")

        //Booked item subtotal
        //assertionEquals(resultData.hotel.itineraryPage.bookedItems.bookedItemSubtotal,resultData.transfer.paymentpage.totalToPayAmountAndCurncy,"Booked Item Sub total Amount actual And Expected don't matching")


    }


    def verifycancelAllBookings(PaymentsTestData data,PaymentsTestResultData resultData){
        //User should be able to do so and cancel overlay should be displayed .
        assertionEquals(resultData.transfer.itineraryPage.actualCancelItemDispStatus,data.expected.dispStatus,"Cancel Overlay not displayed.")

        //Cancel overlay disappear
        assertionEquals( resultData.transfer.itineraryPage.actCancelItemDispStatus,data.expected.notDispStatus,"Cancel Overlay  displayed.")



    }

    def verifyUnavailableblock(PaymentsTestData defaultData,PaymentsTestData data,PaymentsTestResultData resultData){

        //User should be able to do so and cancel overlay should be displayed .
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualUnavailableAndCanncedItemsText,data.expected.unavailableCancelTxt,"Unavailable & Cancel items header text Actual & Expected don't match")

        //Booked Hotel Item status changed to cancelled
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualHotelBookingStatus,data.expected.statusCancelledTxt,"Hotel Booked Status text Actual & Expected don't match")

        //Booked Transfer Item status changed to cancelled
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualTransferBookingStatus,data.expected.statusCancelledTxt,"Transfer Booked Status text Actual & Expected don't match")

        //Booked SightSeeing Item status changed to cancelled
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualsightSeeingBookingStatus,data.expected.statusCancelledTxt,"SightSeeing Booked Status text Actual & Expected don't match")

        //Hotel booking id : should be displayed
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualAfterCancelHotelBookingId,resultData.hotel.confirmBookingOverlay.bookingID,"Hotel Booking Id text Actual & Expected don't match")

        //Transfer booking id : should be displayed
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualAfterCancelTransferBookingId,resultData.transfer.confirmBookingOverlay.bookingID,"Transfer Booking Id text Actual & Expected don't match")

        //SightSeeing booking id : should be displayed
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualAfterCancelSightSeeingBookingId,resultData.activity.confirmBookingOverlay.bookingID,"SightSeeing Booking Id text Actual & Expected don't match")

        //Hotel Payment status
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualHotelpaymentStatus,defaultData.expected.payLaterStatus,"Hotel Payment Status text Actual & Expected don't match")

        //Transfer Payment status
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualTransferpaymentStatus,defaultData.expected.payLaterStatus,"Transfer Payment Status text Actual & Expected don't match")

        //SightSeeing Payment status
        assertionEquals(resultData.transfer.removeItemPage.cancelledItems.actualSightSeeingpaymentStatus,defaultData.expected.payLaterStatus,"SightSeeing Payment Status text Actual & Expected don't match")


    }

    def verifyItineraryPageAfterPaymentActivityMultiModule(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.activity.itineraryPage.bookedItemHeader,data.expected.bookedItemsHeader,"Transfer Item, Booked item header text not displayed.")
        //Booked Item section - should have  hotel and transfer  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.activity.itineraryPage.bookedItemsCount,data.expected.bookedItemsCount+2,"Transfer Item, Booked item count Actual and Expected don't match")
        //should have  hotel  transfer  activity in this section
        assertionEquals(resultData.activity.itineraryPage.bookedHotelItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")
        assertionEquals(resultData.activity.itineraryPage.bookedTransferItemName,resultData.transfer.searchResults.transferItem.transferName,"Transfer Item, Booked item Transfer name Actual and Expected don't match")
        assertionEquals(resultData.activity.itineraryPage.bookedActivityItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Item, Booked item Activity name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.activity.itineraryPage.bookedItemBookingId,resultData.activity.confirmBookingOverlay.bookingID ,"Activity Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.activity.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Activity Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.activity.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Activity Item, Booked item Cancel Text Actual and Expected don't match")

        //Activity details

        //Activity name ,
        assertionEquals(resultData.activity.itineraryPage.bookedItems.expectedHotelName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Name not matching after payment.")

        //,duration with icon and tool tip
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualItemDurationIconDisplayStatus,resultData.activity.itineraryPage.sightSeeingItem.actualItemDurationIconDisplayStatus,"Activity Item, Duration Icon Display Status")
        //Duration Text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualsightSeeingTimeSuggestedItem,resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingTimeSuggestedItem,"Activity Item, Duration Text Actual & Expected Don't Match")
        //Duration Tool tip text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualDurationIconMouseHoverText,resultData.activity.itineraryPage.sightSeeingItem.actualDurationIconMouseHoverText,"Activity Item, Duration Tool Tip Actual & Expected Don't Match")


        //language icon with tool tip and all the languages
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualItemLanguageIconDisplayStatus,resultData.activity.itineraryPage.sightSeeingItem.actualItemLanguageIconDisplayStatus,"Activity Item, Language Icon Display Status")
        //Language Text
        //assertionEquals(resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt,resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt,"Activity Item, Language Text Actual & Expected Don't Match")
        assertContains(resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt.replaceAll(",",""),resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt.replaceAll(",",""),"Activity Item, Language Text Actual & Expected Don't Match")

        //Language Tool tip text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText,resultData.activity.itineraryPage.sightSeeingItem.actualLanguageIconMouseHoverText,"Activity Item, Language Tool Tip Actual & Expected Don't Match")


        //checkin date and pax
        assertionEquals(resultData.activity.itineraryPage.bookedItems.expectedCheckInDate,resultData.activity.itineraryPage.sightSeeingItem.expectedCheckInDate,"Activity Item, Check In Date & Pax not matching after payment.")

        //Commission and text should display
        assertContains(resultData.activity.itineraryPage.bookedItems.commissionTxt,data.expected.commissionTxt,"Activity Item, Commision Text Display not matching after payment.")

        //Total Amount and currency type
        assertionEquals(resultData.activity.itineraryPage.bookedItems.expectedItemPrice,resultData.activity.itineraryPage.sightSeeingItem.expectedItemPrice,"Activity Item, Price And Currency Text not matching after payment.")


        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.activity.itineraryPage.bookedItems.traveler1.toString()),"Activity Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.activity.itineraryPage.bookedItems.traveler2.toString()),"Activity Item, traveler 2 is not matching with traveler details section")

        //cancellation charges overlay should be dispalyed
        //check for the Cancellation and Amendment Charges section
        assertionEquals(resultData.activity.itineraryPage.bookedItems.cancellationChargesTxt,resultData.activity.itineraryPage.sightSeeingItem.cancelchargesTxt,"Activity Item, Cancellation Charges actual And Expected don't matching")


    }

    def verifyBookeditemsectionafterpaylateractivity(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.activity.itineraryPage.bookedItemHeader,data.expected.bookedItemsHeader,"Transfer Item, Booked item header text not displayed.")
        //Booked Item section - should have  hotel and transfer  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.activity.itineraryPage.bookedItemsCount,data.expected.bookedItemsCount+1,"Transfer Item, Booked item count Actual and Expected don't match")
        //should have  hotel   activity in this section
        assertionEquals(resultData.activity.itineraryPage.bookedHotelItemName,resultData.hotel.searchResults.actualHotelName,"Hotel Item, Booked item Hotel name Actual and Expected don't match")

        assertionEquals(resultData.activity.itineraryPage.bookedActivityItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Item, Booked item Activity name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.activity.itineraryPage.bookedItemBookingId,resultData.activity.confirmBookingOverlay.bookingID ,"Activity Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.activity.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Activity Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.activity.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Activity Item, Booked item Cancel Text Actual and Expected don't match")

        //Activity details

        //Activity name ,
        assertionEquals(resultData.activity.itineraryPage.bookedActivityItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Name not matching after payment.")

        //,duration with icon and tool tip
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualItemDurationIconDisplayStatus,resultData.activity.itineraryPage.sightSeeingItem.actualItemDurationIconDisplayStatus,"Activity Item, Duration Icon Display Status")
        //Duration Text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualsightSeeingTimeSuggestedItem,resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingTimeSuggestedItem,"Activity Item, Duration Text Actual & Expected Don't Match")
        //Duration Tool tip text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualDurationIconMouseHoverText,resultData.activity.itineraryPage.sightSeeingItem.actualDurationIconMouseHoverText,"Activity Item, Duration Tool Tip Actual & Expected Don't Match")


        //language icon with tool tip and all the languages
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualItemLanguageIconDisplayStatus,resultData.activity.itineraryPage.sightSeeingItem.actualItemLanguageIconDisplayStatus,"Activity Item, Language Icon Display Status")
        //Language Text
        //assertionEquals(resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt,resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt,"Activity Item, Language Text Actual & Expected Don't Match")
        assertContains(resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt.replaceAll(",",""),resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt.replaceAll(",",""),"Activity Item, Language Text Actual & Expected Don't Match")
        //assertionListEquals(resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt,resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt,"Activity Item, Language Text Actual & Expected Don't Match")

        //Language Tool tip text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText,resultData.activity.itineraryPage.sightSeeingItem.actualLanguageIconMouseHoverText,"Activity Item, Language Tool Tip Actual & Expected Don't Match")


        //checkin date and pax
        assertionEquals(resultData.activity.itineraryPage.bookedItems.expectedCheckInDate,resultData.activity.itineraryPage.sightSeeingItem.expectedCheckInDate,"Activity Item, Check In Date & Pax not matching after payment.")

        //Commission and text should display
        assertContains(resultData.activity.itineraryPage.bookedItems.commissionTxt,data.expected.commissionTxt,"Activity Item, Commision Text Display not matching after payment.")

        //Total Amount and currency type
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualPriceAndCurrency,resultData.activity.itineraryPage.sightSeeingItem.expectedItemPrice,"Activity Item, Price And Currency Text not matching after payment.")


        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.activity.itineraryPage.bookedItems.traveler1.toString()),"Activity Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.activity.itineraryPage.bookedItems.traveler2.toString()),"Activity Item, traveler 2 is not matching with traveler details section")

        //cancellation charges overlay should be dispalyed
        //check for the Cancellation and Amendment Charges section
        assertionEquals(resultData.activity.itineraryPage.bookedItems.cancellationChargesTxt,resultData.activity.itineraryPage.sightSeeingItem.cancelchargesTxt,"Activity Item, Cancellation Charges actual And Expected don't matching")


    }

    def verifybookedItmSctnAftrPaylatrActivity(PaymentsTestData data,PaymentsTestResultData resultData){
        //booked item header should be displayed,
        assertionEquals(resultData.activity.itineraryPage.bookedItemHeader,data.expected.bookedItemsHeader,"Activity Item, Booked item header text not displayed.")
        //Booked Item section - should have  activity  in this section
        //count the items in Booked Item section
        assertionEquals(resultData.activity.itineraryPage.bookedItemsCount,data.expected.bookedItemsCount,"Activity Item, Booked item count Actual and Expected don't match")
        //should have     activity in this section
        assertionEquals(resultData.activity.itineraryPage.bookedActivityItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Item, Booked item Activity name Actual and Expected don't match")

        //booking Id text and number should be displayed
        assertionEquals(resultData.activity.itineraryPage.bookedItemBookingId,resultData.activity.confirmBookingOverlay.bookingID ,"Activity Item, Booked item Booking Id Actual and Expected don't match")

        //confirmed Status
        assertionEquals(resultData.activity.itineraryPage.itemConfirmedStatus,data.expected.confrmdStatus ,"Activity Item, Booked item Confirmed status Actual and Expected don't match")

        //Cancel button text
        assertionEquals(resultData.activity.itineraryPage.itemCancelTxt,data.expected.cancelBtnTxt ,"Activity Item, Booked item Cancel Text Actual and Expected don't match")

        //Activity details

        //Activity name ,
        assertionEquals(resultData.activity.itineraryPage.bookedActivityItemName,resultData.activity.searchResults.sightSeeingItem.sightSeeingName,"Activity Name not matching after payment.")

        //,duration with icon and tool tip
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualItemDurationIconDisplayStatus,resultData.activity.itineraryPage.sightSeeingItem.actualItemDurationIconDisplayStatus,"Activity Item, Duration Icon Display Status")
        //Duration Text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualsightSeeingTimeSuggestedItem,resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingTimeSuggestedItem,"Activity Item, Duration Text Actual & Expected Don't Match")
        //Duration Tool tip text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualDurationIconMouseHoverText,resultData.activity.itineraryPage.sightSeeingItem.actualDurationIconMouseHoverText,"Activity Item, Duration Tool Tip Actual & Expected Don't Match")


        //language icon with tool tip and all the languages
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualItemLanguageIconDisplayStatus,resultData.activity.itineraryPage.sightSeeingItem.actualItemLanguageIconDisplayStatus,"Activity Item, Language Icon Display Status")
        //Language Text
        //assertionEquals(resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt,resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt,"Activity Item, Language Text Actual & Expected Don't Match")
        assertContains(resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt.replaceAll(",",""),resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt.replaceAll(",",""),"Activity Item, Language Text Actual & Expected Don't Match")
        //assertionListEquals(resultData.activity.itineraryPage.sightSeeingItem.actualsightSeeingLangugeItemTxt,resultData.activity.itineraryPage.bookedItems.actualsightSeeingLangugeItemTxt,"Activity Item, Language Text Actual & Expected Don't Match")

        //Language Tool tip text
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualLanguageIconMouseHoverText,resultData.activity.itineraryPage.sightSeeingItem.actualLanguageIconMouseHoverText,"Activity Item, Language Tool Tip Actual & Expected Don't Match")


        //checkin date and pax
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualCheckInDate,resultData.activity.itineraryPage.sightSeeingItem.expectedCheckInDate,"Activity Item, Check In Date & Pax not matching after payment.")

        //Commission and text should display
        assertContains(resultData.activity.itineraryPage.bookedItems.commissionTxt,data.expected.commissionTxt,"Activity Item, Commision Text Display not matching after payment.")

        //Total Amount and currency type
        assertionEquals(resultData.activity.itineraryPage.bookedItems.actualPriceAndCurrency,resultData.activity.itineraryPage.sightSeeingItem.expectedItemPrice,"Activity Item, Price And Currency Text not matching after payment.")


        //Traveller Details
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(0).toString().endsWith(resultData.activity.itineraryPage.bookedItems.traveler1.toString()),"Activity Item, traveler 1 is not matching with traveler details section")
        softAssert.assertTrue(resultData.hotel.itineraryPage.travellerDetails.getAt(3).toString().endsWith(resultData.activity.itineraryPage.bookedItems.traveler2.toString()),"Activity Item, traveler 2 is not matching with traveler details section")

        //cancellation charges overlay should be dispalyed
        //check for the Cancellation and Amendment Charges section
        assertionEquals(resultData.activity.itineraryPage.bookedItems.cancellationChargesTxt,resultData.activity.itineraryPage.sightSeeingItem.cancelchargesTxt,"Activity Item, Cancellation Charges actual And Expected don't matching")


    }



    def protected verifyPaymentStatus(PaymentsTestResultData resultData){
        assertionEquals(resultData.hotel.itineraryPage.paymentStatusInItinerary,defaultData.expected.paymentStatus,"payment is not successful")
        if(resultData.hotel.itineraryPage.paymentStatusInItinerary.toString().equals(defaultData.expected.paymentStatus)){
            softAssert.assertTrue(resultData.hotel.itineraryPage.voucherStatus,"voucher is not displayed after payment success.")
        }
    }
    def verifyCancellationAndAmendCharges(PaymentsTestResultData resultData){
        assertionEquals(resultData.hotel.itineraryPage.cancellationChargesLabel,resultData.hotel.itineraryPage.cancellationChargesLabel1,"Cacellation charges label not matching.")
        assertList(resultData.hotel.itineraryPage.cancellationCharges,resultData.hotel.itineraryPage.cancellationCharges1,"Cancellation charges amount not matching.")
        assertionEquals(resultData.hotel.itineraryPage.amendChargesLabel,resultData.hotel.itineraryPage.amendChargesLabel1,"Amend charges label not displayed.")
        assertList(resultData.hotel.itineraryPage.amendCharges,resultData.hotel.itineraryPage.amendCharges1,"Amend charges not matching.")
    }

}
