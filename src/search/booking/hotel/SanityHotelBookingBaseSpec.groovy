package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.test.BaseSpec

/**
 * Created by mtmaku on 7/18/2017.
 */
abstract  class SanityHotelBookingBaseSpec extends BaseSpec{
    //enter checkin and checkout date and select no of rooms
    def searchHotelCommonData(CitySearchData data){
        entercheckInCheckOutDate(data.input.checkInDaysToAdd.toString().toInteger(), data.input.checkOutDaysToAdd.toString().toInteger())
        clickPaxRoom()
        sleep(1000)
        clickPaxNumOfRooms(data.input.noOfRooms.toString())
        clickFindButton()
        sleep(4000)
    }
    def searchHotelCommonDataForSingleRoom(CitySearchData data){
        entercheckInCheckOutDate(data.input.checkInDaysToAdd.toString().toInteger(), data.input.checkOutDaysToAdd.toString().toInteger())
        clickPaxRoom()
        sleep(1000)
        clickFindButton()
        sleep(4000)
    }
    //search hotel for multi room hotel
    def searchHotelAndAddToItinerary(CitySearchData data, HotelTransferTestResultData resultData) {
        login(client)
        at HotelSearchPage
        enterHotelDestination(data.input.city2Text.toString())
        selectFromAutoSuggest(data.input.city2TypeText.toString())
        searchHotelCommonData(data)
        at HotelSearchResultsPage
        resultData.hotel.searchResults.hotelsResultStatus = searchResultsReturned()
        if(searchResultsReturned()) {
            sleep(1000)
            addFirstAvailableItemToItinerary(resultData)
        }
        else {
            enterHotelDestination(data.input.cityText)
            selectFromAutoSuggest(data.input.cityTypeText)
            searchHotelCommonData(data)
            addFirstAvailableItemToItinerary(resultData)
        }
    }
    def searchHotelSingleRoomAndAddToItinerary(CitySearchData data, HotelTransferTestResultData resultData) {
        login(client)
        at HotelSearchPage
        enterHotelDestination(data.input.city2Text.toString())
        selectFromAutoSuggest(data.input.city2TypeText.toString())
        searchHotelCommonDataForSingleRoom(data)
        at HotelSearchResultsPage
        resultData.hotel.searchResults.hotelsResultStatus = searchResultsReturned()
        if(searchResultsReturned()) {
            sleep(1000)
            addFirstAvailableItemToItinerary(resultData)
        }
        else {
            enterHotelDestination(data.input.cityText)
            selectFromAutoSuggest(data.input.cityTypeText)
            searchHotelCommonData(data)
            addFirstAvailableItemToItinerary(resultData)
        }
    }


    def addFirstAvailableItemToItinerary(HotelTransferTestResultData resultData){
        at HotelSearchResultsPage
        for(int i=0;i<getNumberOfHotelsInSearchResults();i++){
            if(getStatusOfTheRoom(i,i).equals("Available")){
                resultData.hotel.searchResults.hotelName = getHotelName(i)
                resultData.hotel.searchResults.price = getGammaPrice(i,i)
                //store price and item name
                clickAddToitinerary(i)
                break;
            }
        }
    }
    //enter traveler details when user selects multiroom
    def enterTravelerDetails(CitySearchData data,int num, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        waitForAjaxIconToDisappear()
        enterTravellerDetails(data.input.title_txt, data.input.firstName,data.input.lastName, data.input.emailAddr,data.input.telephone_Num, 0)
        addNumberTravellers(data.input.title_txt, data.input.travellerfirstName, data.input.travellerlastName, 3)
        sleep(2000)
        clickonSaveButton(0)
        waitTillLoadingIconDisappears()
        resultData.hotel.itineraryPage.hotelName = getHotelNameOnsuggstedItem(num)
        resultData.hotel.itineraryPage.price = getItenaryPriceInBookedItem(num + 1)
        resultData.hotel.itineraryPage.cancellationLink = getCancellationText()
        sleep(6000)
    }
    //follwoing method is used to book multiroom hotel item when client is pre-paid
    def bookMultiRoomItem(){
        at ItineraryTravllerDetailsPage
        waitForAjaxIconToDisappear()
        clickOnTravellerCheckBox(0)
        clickOnTravellerCheckBox(1)
        clickOnTravellerCheckBox(6)
        sleep(1000)
        clickOnTravellerCheckBox(7)
        waitForAjaxIconToDisappear()

    }
    //follwoing method is used to book multiroom hotel item when client is credit
    def bookAnItem(CitySearchData data,int num, HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        clickOnBookIcon()
       bookMultiRoomItem()
        clickConfirmBookingPayNow()
        waitForAjaxIconToDisappear()
            waitUntillConfrimOverlayAppears()
            sleep(6000)
            clickOnCloseButtonSuggestedItemsCancelpopup()
            sleep(3000)
            waitForAjaxIconToDisappear()
            if (getBookingStatus(num).equals("Pending")) {
                driver.navigate().refresh()
                sleep(3000)
                waitForAjaxIconToDisappear()
            }
            resultData.hotel.itineraryPage.bookingStatus = getBookingStatus(num)
            resultData.hotel.itineraryPage.afterBookingPrice = getItenaryPriceInBookedItem(num + 1)
            if (getBookingStatus(num).equals("Confirmed")) {
                //store voucher status
                sleep(500)
                resultData.hotel.itineraryPage.voucherStatus = voucherStatus(num)
                clickOnVoucher(num)
                resultData.hotel.itineraryPage.voucherOverlayStatus = getTravellerCannotBeDeletedPopupDisplayStatus()
                resultData.hotel.itineraryPage.downLoadVoucherStatus = getDownloadVoucherInOverlayStatus()
                clickOnDownLoadVoucherInOverlay()
                sleep(2000)
                resultData.hotel.itineraryPage.downLoadVoucherStatus1 = getDownloadVoucherInOverlayStatus()
                clickOnCloseButtonSuggestedItemsCancelpopup()

        }
    }
    def bookSingleRoomItem(CitySearchData data,int num,HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        waitForAjaxIconToDisappear()
        enterTravellerDetails(data.input.title_txt, data.input.firstName,data.input.lastName, data.input.emailAddr,data.input.telephone_Num, 0)
        addNumberTravellers(data.input.title_txt, data.input.travellerfirstName, data.input.travellerlastName, 1)
        clickonSaveButton(0)
        waitForAjaxIconToDisappear()
        resultData.hotel.itineraryPage.hotelName = getHotelNameOnsuggstedItem(num)
        resultData.hotel.itineraryPage.price = getItenaryPriceInBookedItem(num + 1)
        resultData.hotel.itineraryPage.cancellationLink = getCancellationText()
        clickOnItemCancllationLink(num)
        resultData.hotel.itineraryPage.cancellationChargesBeforeBooking = getCancellationChargesInOverlay()
        try{
            resultData.hotel.itineraryPage.amendCharges = getAmendChargesTxtInOverlay()
        }catch(Exception e){
            resultData.hotel.itineraryPage.amendCharges = "Unable To Read From UI"
        }
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitTillLoadingIconDisappears()
        sleep(1000)
        clickOnBookIcon()
        waitForAjaxIconToDisappear()
        clickConfirmBookingPayNow()
        waitForAjaxIconToDisappear()
        waitUntillConfrimOverlayAppears()
        sleep(3000)
        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(3000)
        waitForAjaxIconToDisappear()
        if(getBookingStatus(num).equals("Pending")) {
            driver.navigate().refresh()
            sleep(3000)
            waitForAjaxIconToDisappear()
        }
        resultData.hotel.itineraryPage.bookingStatus = getBookingStatus(num)
        resultData.hotel.itineraryPage.afterBookingPrice = getItenaryPriceInBookedItem(num + 1)
        if(getBookingStatus(num).equals("Confirmed")){
            //store voucher status
            sleep(500)
            resultData.hotel.itineraryPage.voucherStatus = voucherStatus(num)
            clickOnVoucher(num)
            resultData.hotel.itineraryPage.voucherOverlayStatus = getTravellerCannotBeDeletedPopupDisplayStatus()
            sleep(1000)
            resultData.hotel.itineraryPage.downLoadVoucherStatus = getDownloadVoucherInOverlayStatus()
            clickOnDownLoadVoucherInOverlay()
            sleep(2000)
            resultData.hotel.itineraryPage.downLoadVoucherStatus1 = getDownloadVoucherInOverlayStatus()
            clickOnCloseButtonSuggestedItemsCancelpopup()
        }
    }

    def amendAnItem(int num,HotelTransferTestResultData resultData,CitySearchData data){
        at ItineraryTravllerDetailsPage
        clickAmendBookingButton(num)
       waitTillLoadingIconDisappears()
        sleep(1000)
        selectAmendCheckOutDateCalender(data.input.checkInDaysToAdd.toString().toInteger()+3)
        clickAmendFindButton()
        sleep(1000)
        resultData.hotel.itineraryPage.amendedNights  = getAmendOverlayBlockText()
       // String[] text = info.split(",")
        //resultData.hotel.itineraryPage.amendedNights = text[1]
        clickAmendOKButton()
        sleep(6000)
        scrollToBottomOfThePage()
        sleep(1000)
        clickonConfirmAmendment()
        waitForAjaxIconToDisappear()
        waitUntillConfrimOverlayAppears()
        sleep(2000)
        clickOnCloseButtonSuggestedItemsCancelpopup()
        waitForAjaxIconToDisappear()
        resultData.hotel.itineraryPage.afterAmend = verifyPax()
    }
    def cancelAnItem(int num,HotelTransferTestResultData resultData){
        at ItineraryTravllerDetailsPage
        clickOnCancelForPendingItem(num)
        ClickonYesToCancel()
        sleep(1000)
        waitForAjaxIconToDisappear()
        sleep(4000)
        resultData.hotel.itineraryPage.cancelStatus = getBookingStatus(num)
    }
    //follwoing method used for booking single item when client is credit
    def bookItem(CitySearchData data,int num,HotelTransferTestResultData resultData) {
        at ItineraryTravllerDetailsPage
        waitForAjaxIconToDisappear()
        enterTravellerDetails(data.input.title_txt, data.input.firstName, data.input.lastName, data.input.emailAddr, data.input.telephone_Num, 0)
        addNumberTravellers(data.input.title_txt, data.input.travellerfirstName, data.input.travellerlastName, 1)
        clickonSaveButton(0)
        waitForAjaxIconToDisappear()
        sleep(1000)
        resultData.hotel.itineraryPage.itemName = getHotelNameOnsuggstedItem(num)
        resultData.hotel.itineraryPage.price = getItenaryPriceInBookedItem(num + 1)
        sleep(6000)
        clickOnBookIcon()
        waitForAjaxIconToDisappear()
        sleep(2000)

    }

    //verifications started
    protected def verifySearchHotel(HotelTransferTestResultData resultData) {
        softAssert.assertTrue(resultData.hotel.searchResults.hotelsResultStatus,"search results not returned")
    }
    protected def verifyBookedItem(HotelTransferTestResultData resultData){
        assertionEquals(resultData.hotel.searchResults.hotelName,resultData.hotel.itineraryPage.hotelName,"HotelName not matching with PLP.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.price.toString().contains(resultData.hotel.searchResults.price.toString()),"Price not matching with PLP" )
        assertionEquals(resultData.hotel.itineraryPage.bookingStatus,"Confirmed","Hotel item not booked.")
        assertionEquals(resultData.hotel.itineraryPage.price,resultData.hotel.itineraryPage.afterBookingPrice,"Prices are not equal before booking and after booking.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.voucherStatus,"Voucher not available for booked item.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.voucherOverlayStatus,"Clicking on voucher did not display overlay.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.downLoadVoucherStatus,"Download voucher button not enabled.")
        softAssert.assertTrue(resultData.hotel.itineraryPage.downLoadVoucherStatus1,"voucher did not download.")

    }
    protected def verifyAmendedItem(HotelTransferTestResultData resultData){
        softAssert.assertTrue(resultData.hotel.itineraryPage.amendedNights.toString().trim().contains(resultData.hotel.itineraryPage.afterAmend.toString().trim()),"Amemendment did not happen.")
    }

    protected def verifyCancelledItem(HotelTransferTestResultData resultData){
        assertionEquals(resultData.hotel.itineraryPage.cancelStatus,"Cancelled","Item is not cancelled")
    }
}


