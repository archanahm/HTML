package com.kuoni.qa.automation.test.search.booking.transfer

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.TransferTestResultData
import com.kuoni.qa.automation.helper.TransfersTestData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.TransferSearchPage
import com.kuoni.qa.automation.page.transfers.TransferSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.test.payments.PaymentSpec
import com.kuoni.qa.automation.util.TestConf

import org.testng.Assert

/**
 * Created by kmahas on 4/14/2017.
 */
abstract class TransfersSpec extends PaymentSpec{

    protected def loginToApplicaiton(ClientData clientData, TransfersTestData data, TransferTestResultData resultData){
        login(clientData)
        at HotelSearchPage
        resultData.transfers.search.actualFindButtonStatus=getFindButtonDisabled()
    }

    protected def SearchTransferAtoA(transferData, resultData){
        at HotelSearchPage
        clickTransfersTab()
        at TransferSearchPage
        enterPickupInput(transferData.input.pickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(transferData.input.selectPickUp)
        enterDropoffInput(transferData.input.dropOff)
        sleep(1000)
        selectFromAutoSuggestDropOff(transferData.input.selectDropOff)
        sleep(1000)
        selectDateCalender(transferData.input.checkInDays.toInteger())
        sleep(1000)
        selectTime(transferData.input.hours,transferData.input.minutes)
        enterPaxInput(transferData.input.pax,"0")
        sleep(2000)

        clickFindButton()
        sleep(5000)

        at TransferSearchResultsPage
        //Search Results returned
        resultData.transfers.searchResults.actualSearchResultsReturned=searchResultsReturned()
        //First Transfer Name
        resultData.transfers.searchResults.firstTransfername=getTransferVehicleName(0)
    }

    protected def SearchTransferAtoH(transferData, resultData){
        at HotelSearchPage
        clickTransfersTab()
        at TransferSearchPage
        enterPickupInput(transferData.input.firstpickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(transferData.input.firstselectPickUp)
        enterDropoffInput(transferData.input.firstdropOff)
        sleep(1000)
        selectFromAutoSuggestDropOff(transferData.input.firstselectDropOff)
        sleep(1000)
        selectDateCalender(transferData.input.checkInDays.toInteger())
        sleep(1000)
        selectTime(transferData.input.hours,transferData.input.minutes)
        sleep(1000)
        scrollToTopOfThePage()
        enterPaxInput(transferData.input.pax,"0")
        sleep(2000)

        clickFindButton()
        sleep(5000)

        at TransferSearchResultsPage

        if(searchResultsReturned()==false){

            at TransferSearchPage
            enterPickupInput(transferData.input.secondpickup)
            sleep(1000)
            selectFromAutoSuggestPickUp(transferData.input.secondselectPickUp)
            enterDropoffInput(transferData.input.seconddropOff)
            sleep(1000)
            selectFromAutoSuggestDropOff(transferData.input.secondselectDropOff)
            sleep(1000)
            selectDateCalender(transferData.input.checkInDays.toInteger())
            sleep(1000)
            selectTime(transferData.input.hours,transferData.input.minutes)
            enterPaxInput(transferData.input.pax,"0")
            sleep(2000)

            clickFindButton()
            sleep(5000)
        }

        at TransferSearchResultsPage
            if(searchResultsReturned()==false)
            {
                Assert.assertFalse(true,"0 Search Results Returned")
            }
        if(getShowCarTypesExpandOrCollapseStatus(0)==false){
            clickShowCarTypes(0)
            sleep(2000)
        }

        //Search Results returned
        resultData.transfers.searchResults.actualSearchResultsReturned=searchResultsReturned()
        //First Transfer Name
        //resultData.transfers.searchResults.firstTransfername=getTransferVehicleName(0)
        resultData.transfers.searchResults.firstTransfername=getTransferVehicleName(0,0)

        //First Transfer Item Price
        //resultData.transfers.searchResults.firstTransferPrice=getPriceAndCurrencyTransferItem(0)
        resultData.transfers.searchResults.firstTransferPrice=getPriceTransferItem(0,0)

    }

    protected def Itinerarybuilder(transferData, resultData){

        at TransferSearchResultsPage
        //click on add to Itineary button
        //clickOnAddToItenary(0)
        clickOnAddToItenary(0,0)
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
        resultData.transfers.searchResults.actualTransferItineraryName=getTransferItineraryItem(0)
    }

    protected def GoToitinerary(transferData, resultData){

        at ItenaryBuilderPage
        //hideItenaryBuilder()

        //capture new Itinerary reference number
        String itineraryBuilderTitle = getItenaryBuilderTtile()
        //println("$itineraryBuilderTitle")
        List<String> tempList=itineraryBuilderTitle.tokenize(" ")
        //println("$tempList")
        String itineraryId=tempList.getAt(0)+tempList.getAt(1)
        resultData.transfers.searchResults.retitineraryId=itineraryId
        println("Retrieved Itinerary ID: $resultData.transfers.searchResults.retitineraryId")

        //click Go to Itinerary Link
        clickOnGotoItenaryButton()
        sleep(5000)

        at ItineraryTravllerDetailsPage

        //itineary page display with selected item
        resultData.transfers.itineraryPage.actualTransferItineraryName=getTransferNameInSuggestedItem(0)

    }

    protected def Itinerary(ClientData clientData,transferData, resultData){

        //Edit Itinerary name
        at ItineraryTravllerDetailsPage
        //click on Edit
        scrollToTopOfThePage()
        clickEditIconNextToItineraryHeader()
        sleep(5000)
        waitTillLoadingIconDisappears()

        enterItenaryName(transferData.expected.itineraryName)

        //Click save button
        clickSaveButton()
        sleep(5000)
        waitTillLoadingIconDisappears()
        //capture entered Itinerary Name
        //Capture Itinerary Page - header
        String edtditineraryPageTitle = getItenaryName()
        //println("$itineraryPageTitle")
        List<String> tList=edtditineraryPageTitle.tokenize(" ")
        String edtitinaryName=tList.getAt(2)
        resultData.transfers.itineraryPage.actualSavedItnrName=edtitinaryName.replaceAll("\nEdit", "")

        //add agent refrence name and click on save
        //add agent refrence name and
        clickOnAddAgentRefButton()
        sleep(3000)
        enterAgentRef(transferData.expected.agentRef)
        sleep(2000)
        // clik on save
        clickOnSaveOrCancelBtnsAgentRef(1)
        sleep(5000)

        //user should be able to do so.
        resultData.transfers.itineraryPage.actualSavedAgentRefName=getSavedAgentRefName()
        // Save button should be removed .
        resultData.transfers.itineraryPage.actualSaveBtnDispStatus=verifySaveButtonInAgentRefSecDispStatus()
        //Edit button should be displayed
        resultData.transfers.itineraryPage.actualEditBtnDispStatus=verifyEditButtonDisplayed(0)

        //Transfer Price
        resultData.transfers.itineraryPage.actualTransferPriceBeforebooking=getNonBookedItemsItenaryPrice(0)
        resultData.transfers.itineraryPage.expectedTransferPriceBeforebooking=resultData.transfers.searchResults.firstTransferPrice.replaceAll(",", "")
        resultData.transfers.itineraryPage.expectedTransferPriceBeforebooking=resultData.transfers.itineraryPage.expectedTransferPriceBeforebooking+" "+clientData.currency

    }

    protected def Traveller1Details(transferData, resultData){

        //Title drop down should contain below details

    }

    protected def TravellerDetails(transferData, resultData){

        //Enter traveller details:
        //Traveller 1 (lead name) - Mr XXXX XXXX
        //Traveller 2 (Adult) - Mr XXXX XXXX
        at ItineraryTravllerDetailsPage
        //Input Title
        selectTitle(transferData.expected.transferstitle,0)
        //Input First Name
        enterFirstName(transferData.expected.fNameFirst,0)
        //Input Last Name
        enterLastName(transferData.expected.LNameFirst,0)
        //Input Email Address

        enterEmail(transferData.expected.emailAddr,0)
        sleep(1000)
        enterTelephoneNumber(transferData.expected.phoneNum,0)
        sleep(1000)
        //Add Travellers and click on save
        addNumberTravellers(transferData.expected.transferstitle, transferData.expected.fNameSecond, transferData.expected.LNameSecond, 1)

        clickonSaveButton(0)
        sleep(2000)
        waitTillLoadingIconDisappears()

        //capture entered lead traveller details are displayed correctly
        resultData.transfers.itineraryPage.expectedleadTravellerName=transferData.expected.transferstitle+" "+transferData.expected.fNameFirst+" "+transferData.expected.LNameFirst
        println("$resultData.transfers.itineraryPage.expectedleadTravellerName")
        //resultData.transfers.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)
        resultData.transfers.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)

        //email
        resultData.transfers.itineraryPage.actualemailId=getLeadTravellerName(2)
        //capture entered first traveller details are displayed correctly
        resultData.transfers.itineraryPage.expectedFirstTravellerName=transferData.expected.transferstitle+" "+transferData.expected.fNameSecond+" "+transferData.expected.LNameSecond
        resultData.transfers.itineraryPage.actualFirstTravellerName=getLeadTravellerName(3)


    }

    protected def Booking(transferData, resultData){

        at ItineraryTravllerDetailsPage

        //CLICK ON BOOK
        clickBookButton(0)
        sleep(5000)

        //Input Flight Number
        enterPickUpFlightNumber(transferData.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(transferData.expected.arrivingText)
        sleep(5000)
        //Auto Suggest select
        selectArrivalAutoSuggest(transferData.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(transferData.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        //enterPickUpArrivalMins(transferData.timeOfArrival_mins)
        enterArrivalMins(transferData.expected.timeOfArrival_mins)
        sleep(1000)
        //clickConfirmBooking()
        clickOnConfirmBookingAndPayNow()
        sleep(5000)

        //wait for confirmation page
        waitTillConformationPage()
        sleep(5000)

        waitTillLoadingIconDisappears()
        scrollToTopOfThePage()
        //Booking Confirmation Screen Display Status
        resultData.transfers.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

        //Title text-Booking Confirmed
        resultData.transfers.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()

        //click on Close lightbox X function
        coseBookItenary()
        sleep(7000)

        pageRefresh()

    }
    protected def paymentBooking(transferData, resultData) {

        at ItineraryTravllerDetailsPage
        //Transfer Name
        resultData.transfers.itineraryPage.actualTransferItineraryName=getTransferNameInSuggestedItem(0)
        // Transfer name in itinerary page
        resultData.transfers.itineraryPage.actualTransferItineraryName=getTransferNameInSuggestedItem(0)

        //CLICK ON BOOK
        clickBookButton(0)
        sleep(5000)

        //Input Flight Number
        enterPickUpFlightNumber(transferData.expected.flightNumber)

        //Input Arriving From
        enterArrivalFrom(transferData.expected.arrivingText)
        sleep(3000)
        //Auto Suggest select
        selectArrivalAutoSuggest(transferData.expected.arrivingFrom)
        //Input time of arrival - hrs
        enterPickUpArrivalTime(transferData.expected.timeOfArrival_Hrs)
        //Input time of arrival - mins
        //enterPickUpArrivalMins(transferData.timeOfArrival_mins)
        enterArrivalMins(transferData.expected.timeOfArrival_mins)
        sleep(1000)
    }

        protected def BookedItemslist(ClientData clientData,transferData, resultData){
        at ItineraryTravllerDetailsPage

        //Booking Status:
        resultData.transfers.itineraryPage.actualBookingStatus=getStatusInBookedItemsScrn(1)

        //Transfer Price
        resultData.transfers.itineraryPage.actualTransferPriceAfterbooking=getNonBookedItemsItenaryPrice(0)
        resultData.transfers.itineraryPage.expectedTransferPriceAfterbooking=resultData.transfers.searchResults.firstTransferPrice.replaceAll(",", "")
        resultData.transfers.itineraryPage.expectedTransferPriceAfterbooking=resultData.transfers.itineraryPage.expectedTransferPriceAfterbooking+" "+clientData.currency
        //Voucher Display Status
        resultData.transfers.itineraryPage.actualVoucherDisplayStatus=voucherStatus(0)

        //Click on Vocher
        clickOnVoucher(0)
        sleep(1000)

        //Voucher Overlay Disp Status
        resultData.transfers.itineraryPage.actualVoucherOverLayDisplayStatus=getVoucherOverLayDispStatus()

        //Download Voucher - Enabled
        resultData.transfers.itineraryPage.actualDownloadVoucherBtnStatus=getDownloadVoucherInOverlayStatus()

        //click on download voucher
        clickOnDownLoadVoucherInOverlay()
        sleep(1000)

        //download voucher overlay should be retained. It should be displayed
        resultData.transfers.itineraryPage.actualVoucherOverLayDisplayStatusAftrClkOnDownloadBtn=getVoucherOverLayDispStatus()

        //close voucher overlay
        closeDwldVocherOverlay()
        sleep(1000)

    }

    protected def ItineraryPDF(transferData, resultData){
        at ItineraryTravllerDetailsPage

        //Click on Manage Itinerary -> Itinerary PDF
        selectFromManageItinerary(transferData.input.manageItinryValue)
        sleep(3000)

        //First Get All the current Active Window Id's
        Set <String> set = driver.getWindowHandles();
        //Using Iterator capture the window Id's from the Set Collection
        Iterator<String> itr = set.iterator();
        //Get the parent window id
        String parentWinId = itr.next();
        //Get the Child Window id
        String firstChildWinId = itr.next();

        //Switch the driver control to first child window and verify title
        driver.switchTo().window(firstChildWinId);

        println("First Child window URL Is Empty:"+driver.getCurrentUrl().isEmpty())


        //New window Itinerary PDF should be opened
        resultData.transfers.itineraryPage.actualItineraryPDFDispStatus=driver.getCurrentUrl().isEmpty()

        driver.switchTo().window(firstChildWinId).close();
        driver.switchTo().window(parentWinId);
    }

    protected def CancelItem(transferData, resultData){
        at ItineraryTravllerDetailsPage

        //select above confirmed (1st item) and click on Cancel button
        clickOnCancelOrAmendTabButton(1)

        //Cancel item lightbox display status
        resultData.transfers.removeItemPage.actualCancelItemDispStatus=getCancelItemDisplayStatus()

        //lightbox - Title text , Text - Cancel item
        resultData.transfers.removeItemPage.actualCancelItemTxt=getCancellationHeader()

        //click Yes
        clickYesOnRemoveItenary()
        sleep(7000)
        waitTillLoadingIconDisappears()

        sleep(3000)
        //title text - Unavailable and Cancelled items
        resultData.transfers.removeItemPage.cancelledItems.actualCancelledItmsTxt=getHeaderTxtInBookedItemsListScrn(0)

        //Status - Cancelled - text
        resultData.transfers.removeItemPage.cancelledItems.actualStatusTxt=getStatusInBookedItemsScrn(1)

    }

    protected def Bookanitemwithoutsave(transferData, resultData){

    }

    protected def EditAgentrefrence(transferData, resultData){

    }

    protected def EditTravellerdetails(transferData, resultData){

    }

    protected def BookingTAndC(transferData, resultData){

    }

    protected def Departuretimeerrormessage(transferData, resultData){

    }

    protected def "VerifyloginToApplicaiton"(TransfersTestData data, TransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.transfers.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }
    protected def "VerifySearchTransferAtoA"(TransfersTestData data, TransferTestResultData resultData){

        //Search Results returned
        assertionEquals(resultData.transfers.searchResults.actualSearchResultsReturned,data.expected.dispStatus,"Search Results Returned Actual & Expected don't match")



    }

    protected def "VerifySearchTransferAtoH"(TransfersTestData data, TransferTestResultData resultData){

        //Search Results returned
        assertionEquals(resultData.transfers.searchResults.actualSearchResultsReturned,data.expected.dispStatus,"Search Results Returned Actual & Expected don't match")


    }

    protected def "VerifyItinerarybuilder"(TransfersTestData data, TransferTestResultData resultData){

        //user able to do so
        assertionEquals(resultData.transfers.searchResults.actualTransferItineraryName,resultData.transfers.searchResults.firstTransfername,"Item Added to Itinerary builder Actual & Expected don't match")


    }
    protected def "VerifyGoToitinerary"(TransfersTestData data, TransferTestResultData resultData){

        //itineary page display with selected item
        assertionEquals(resultData.transfers.itineraryPage.actualTransferItineraryName,resultData.transfers.searchResults.firstTransfername,"Itinerary Page displayed with Selected Item Actual & Expected don't match")



    }
    protected def "VerifyItinerary"(TransfersTestData data, TransferTestResultData resultData){

        //User is able to edit and save Itinerary Name
        assertionEquals(resultData.transfers.itineraryPage.actualSavedItnrName,data.expected.itineraryName,"Itinerary Name Edit Actual & Expected don't match")

        //user should be able to do so.
        assertionEquals(resultData.transfers.itineraryPage.actualSavedAgentRefName,data.expected.agentRef,"Itinerary Screen Agent Reference Name displayed Actual & Expected don't match")
        // Save button should be removed .
        assertionEquals(resultData.transfers.itineraryPage.actualSaveBtnDispStatus,data.expected.notDispStatus.toBoolean(),"Itinerary Screen Agent Reference Section Save button displayed Actual & Expected don't match")

        //Edit button should be displayed
        assertionEquals(resultData.transfers.itineraryPage.actualEditBtnDispStatus,data.expected.dispStatus.toBoolean(),"Itinerary Screen Agent Reference Section Edit Button displayed Actual & Expected don't match")

        //Transfer Price
        assertionEquals(resultData.transfers.itineraryPage.actualTransferPriceBeforebooking,resultData.transfers.itineraryPage.expectedTransferPriceBeforebooking,"Itinerary Screen, Transfers Price Before Booking Actual & Expected don't match")

    }
    protected def "VerifyTraveller1Details"(TransfersTestData data, TransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.transfers.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }

    protected def "VerifyTravellerDetails"(TransfersTestData data, TransferTestResultData resultData){

        //Validate Entered Traveller Details - for lead traveller
        assertionEquals(resultData.transfers.itineraryPage.actualLeadTravellerName,resultData.transfers.itineraryPage.expectedleadTravellerName,"Itinerary Screen Traveller Details - Lead Traveller Expected & Actual Traveller details don't match")
        //validate email
        assertionEquals(resultData.transfers.itineraryPage.actualemailId,data.expected.emailAddr,"Itinerary Screen Traveller Email Details Expected & Actual don't match")
        //Validate Entered Traveller Details - for another traveller
        assertionEquals(resultData.transfers.itineraryPage.actualFirstTravellerName,resultData.transfers.itineraryPage.expectedFirstTravellerName,"Itinerary Screen Traveller Details - First Traveller Expected & Actual details don't match")


    }

    protected def "VerifyBookanitemwithoutsave"(TransfersTestData data, TransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.transfers.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }

    protected def "VerifyEditAgentrefrence"(TransfersTestData data, TransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.transfers.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }

    protected def "VerifyEditTravellerdetails"(TransfersTestData data, TransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.transfers.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }

    protected def "VerifyBookingTAndC"(TransfersTestData data, TransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.transfers.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }

    protected def "VerifyBooking"(TransfersTestData data, TransferTestResultData resultData){

        //Booking Confirmation Screen Display Status
        assertionEquals(resultData.transfers.itineraryPage.actualBookingconfirmaitonDispStatus,data.expected.dispStatus,"Booking Confirmation Screen Display Status Actual & Expected don't match")

        //Title Text - Booking confirmed
        assertionEquals(resultData.transfers.itineraryPage.actualBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt,"Booking Confirmation Screen, Title Text actual & expected don't match")


    }

    protected def "VerifyBookedItemslist"(TransfersTestData data, TransferTestResultData resultData){

        //Booking status
        assertionEquals(resultData.transfers.itineraryPage.actualBookingStatus,data.expected.bookingconfStatus,"Itinerary Screen, Booking status Actual & Expected don't match")

        //Transfer Price
        assertionEquals(resultData.transfers.itineraryPage.actualTransferPriceAfterbooking,resultData.transfers.itineraryPage.expectedTransferPriceAfterbooking,"Itinerary Screen, Transfers Price After Booking Actual & Expected don't match")

        //Voucher Display Status
        assertionEquals(resultData.transfers.itineraryPage.actualVoucherDisplayStatus,data.expected.dispStatus,"Itinerary screen, voucher displayed Status Actual & Expected don't match")

        //Voucher Overlay Disp Status
        assertionEquals(resultData.transfers.itineraryPage.actualVoucherOverLayDisplayStatus,data.expected.dispStatus,"Itinerary screen, Voucher Overlay Display Status Actual & Expected don't match")

        //Download Voucher - Enabled
        assertionEquals(resultData.transfers.itineraryPage.actualDownloadVoucherBtnStatus,data.expected.dispStatus,"Itinerary screen, Download Voucher Button Enabled Status Actual & Expected don't match")

        //download voucher overlay should be retained. It should be displayed
        assertionEquals(resultData.transfers.itineraryPage.actualVoucherOverLayDisplayStatusAftrClkOnDownloadBtn,data.expected.dispStatus,"Itinerary screen, voucher overlay display Status after click on Download voucher button Actual & Expected don't match")



    }
    protected def "VerifyItineraryPDF"(TransfersTestData data, TransferTestResultData resultData){

        //Itinerary PDF Display status
        assertionEquals(resultData.transfers.itineraryPage.actualItineraryPDFDispStatus,data.expected.notDispStatus,"Itinerary Screen, Itinerary PDF display status Actual & Expected don't match")


    }

    protected def "VerifyCancelItem"(TransfersTestData data, TransferTestResultData resultData){

        //Cancel item lightbox display status
        assertionEquals(resultData.transfers.removeItemPage.actualCancelItemDispStatus,data.expected.dispStatus,"Itinerary Screen, Cancel Item Overlay Display Status Actual & Expected don't match")

        //lightbox - Title text , Text - Cancel item
        assertionEquals(resultData.transfers.removeItemPage.actualCancelItemTxt,data.expected.CancelItemTxt,"Itinerary Screen, Cancel Item Text Actual & Expected don't match")

        //title text - Unavailable and Cancelled items
        assertionEquals(resultData.transfers.removeItemPage.cancelledItems.actualCancelledItmsTxt,data.expected.headerUnavAndCanclTxt,"Itinerary Screen, Header Text Unavailable and Cancelled Items Actual & Expected don't match")

        //Status - Cancelled - text
        assertionEquals(resultData.transfers.removeItemPage.cancelledItems.actualStatusTxt,data.expected.cancelStatusTxt,"Itinerary Screen, Cancelled Status Text Actual & Expected don't match")



    }

    protected def "VerifyDeparturetimeerrormessage"(TransfersTestData data, TransferTestResultData resultData){

        //Find Button should be disabled
        assertionEquals(resultData.transfers.search.actualFindButtonStatus,data.expected.dispStatus,"Login Successful Actual & Expected don't match")


    }
}
