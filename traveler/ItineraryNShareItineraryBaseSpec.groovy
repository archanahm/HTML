package com.kuoni.qa.automation.test.traveler

import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.TravelerTestResultData
import com.kuoni.qa.automation.page.ActivityPDPPage
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.hotel.AgentHotelInfoPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.ShareItineraryPage
import com.kuoni.qa.automation.page.transfers.ShareItineraryTransfersPage
import com.kuoni.qa.automation.page.traveller.TravelerViewMyItineraryPage
import com.kuoni.qa.automation.test.search.booking.hotel.itinerary.MultipleModuleBaseSpec
import org.openqa.selenium.support.ui.WebDriverWait

/**
 * Created by mtmaku on 1/27/2017.
 * NOVA POS - Share Itinerary - HH - single room - booked / non-booked /
 */
class ItineraryNShareItineraryBaseSpec extends MultipleModuleBaseSpec {

    /* Search for Hotel and add to Itinerary */
    def searchItemsAndAddToItinerary(HotelSearchData data, HotelTransferTestResultData resultData) {
        fillHotelSearchFormAndSubmit(data.input.cityAreaHotelTypeText.toString(), data.input.cityAreaHotelautoSuggest.toString(), data.input.checkInDays.toString(), data.input.checkOutDays.toString(), data.input.pax.toString(), data.input.children)
        resultData.hotel.searchResults.firstSearch = clickAddToItinerary(0)
        resultData.hotel.searchResults.hotel1Info = storeHotelInfo(0)
        at HotelSearchResultsPage
        clickHotelsTab()
        selectcheckInCheckOutDateCalender(data.input.chkInDays.toString().toInteger(), data.input.chkOutDays.toString().toInteger())
        clickFindButton()
        resultData.hotel.searchResults.secondSearch = clickAddToItinerary(1)
        resultData.hotel.searchResults.hotel2Info = storeHotelInfo(1)
        at ActivityPDPPage
        clickOnHome()
        sleep(2000)
        fillHotelSearchFormAndSubmit(data.input.cityAreaOnRequestItemText.toString(), data.input.cityAreaOnRequestAutoSuggest.toString(), data.input.checkInDays.toString(), data.input.checkOutDays.toString(), data.input.pax.toString(), data.input.children)
        resultData.hotel.searchResults.thirdSearch = clickAddToItinerary(0)

    }
    def searchHotelAndAddToItinerary(String checkInDate, String checkoutDate,HotelTransferTestResultData resultData){
        at HotelSearchResultsPage
        clickHotelsTab()
        sleep(1000)
        selectcheckInCheckOutDateCalender(checkInDate.toInteger(), checkoutDate.toInteger())
        clickFindButton()
        sleep(2000)
        resultData.hotel.searchResults.storeHotelData = clickAddToItinerary(0)
        resultData.hotel.searchResults.actualItemCount = getItemsCountItineraryBar()
    }
    /* Generic method for clicking on Add to itinerary button when search criteria contains paxs (child+infant) */
    def clickAddToItinerary(int index) {
        Map dataMap = [:]
        at HotelSearchResultsPage
        dataMap.actualHotelName = getHotelItemNameInSearchResults()
        scrollToAddToItineraryBtn()
        clickAddToitinerary(index)
        //close infant overlay light box
        try {

            clickOnCloseLightBox()
            sleep(1000)
        }
        catch (Exception e) {
        }
        dataMap.actualItemCount = getItemsCountItineraryBar()
        dataMap
    }
    /* To capture hotel infomation details in PDP page*/
    def storeHotelInfo(int roomNum) {
        at AgentHotelInfoPage
        def dataMap = [:]
        sleep(1000)
        clickExpandRoomButton(roomNum)
        sleep(1000)
        dataMap.hotelAddr = getHotelAddress()
        dataMap.starRating = getStarRatingText()
        dataMap.roomFacilities = getHotelFacilityNames(roomNum)
        clickOnViewMore()
        dataMap.hotelInfoHeader = getHotelInfoHeader()
        dataMap.hotelDataHeaderStrong = hotelDataHeadersStrong()
        dataMap.hotelFacilityHeaders = getHotelRoomData()
        dataMap.hotelFacilityIcons = getHotelFacilities()
        //  resultData.hotel.searchResults.hotelData = getMoreRoomData()
        dataMap.hotelData = getHotelData()
        dataMap.roomInfo =  getRoomInfo(roomNum)
        println("All hotel data:" +dataMap.hotelData )
        dataMap
    }
     /* Edit itinerary Name in itinerary page */
    protected def itineraryNameUpdate(HotelSearchData data, HotelTransferTestResultData resultData) {
        at ItenaryPageItems
        //click on Edit
        scrollToTopOfThePage()
        clickEditIconNextToItineraryHeader()
        sleep(3000)
        waitTillLoadingIconDisappears()
        //Update Name Text
        String todaysDate = dateUtil.addDaysChangeDatetformat(0, "ddMMMyy")
        println("$todaysDate")
        resultData.hotel.itineraryPage.expectedItnrName = todaysDate + "-" + data.expected.itineraryName
        enterItenaryName(resultData.hotel.itineraryPage.expectedItnrName)
        //Click save button
        clickSaveButton()
        sleep(4000)
        waitTillLoadingIconDisappears()
        //capture entered Itinerary Name

    }
    def storeItineraryDetails(){
        def dataMap = [:]
        at ItenaryPageItems
        String edtditineraryPageTitle = getItenaryName()
        // edtditineraryPageTitle = edtditineraryPageTitle.replace(" ","")
        println("itinearyID in Itinerary page" + edtditineraryPageTitle)
        List<String> tList = edtditineraryPageTitle.tokenize(" ")
        String edtitinaryName = tList.getAt(2)
        String itineraryID = tList.getAt(1)
        dataMap.actualSavedItnrID = itineraryID
        dataMap.actualSavedItnrName = edtitinaryName.replaceAll("\nEdit", "")
        dataMap.expectedItnrIDName = edtditineraryPageTitle.replace(" ", "").replaceAll("\nEdit", "").trim()
        dataMap
    }


    /* Enter traveler details in itinerary page*/
    protected
    def travellerDetails(HotelSearchData data, int adultStartindx, int adultendindx, int childstrtindx, int childendindx,def children) {

        at ItineraryTravllerDetailsPage
        //Input Title
        selectTitle(data.expected.title_txt, 0)
        //Input First Name
        enterFirstName(data.expected.firstName, 0)
        //Input Last Name
        enterLastName(data.expected.lastName, 0)
        //Input Email Address
        enterEmail(data.expected.emailAddr, 0)
        //Input Area Code
        //enterCountryCode(data.expected.countryCode, 0)
        sleep(1000)
        enterTelephoneNumber(data.expected.telephone_Num, 0)
        sleep(1000)
        waitTillLoadingIconDisappears()
        sleep(2000)
        selectTitle(data.expected.title_txt, 1)
        //Input First Name
        enterFirstName(data.expected.travellerfirstName, 1)
        //Input Last Name
        enterLastName(data.expected.travellerlastName, 1)

        //Adding child
        ClickRadioButtonAdultOrChild(4)
        sleep(2000)
      //  selectTitle(data.expected.thirdTraveller_title_txt, 2)
        enterFirstName(data.expected.childFirstName,2)
        enterLastName(data.expected.childLastName,2)
        enterChildAge(children.getAt(0).toString(),1)
        sleep(1000)
        clickonSaveButton()
        sleep(1000)
      /*  //Add Travellers 2 - adult
      //  addSavAdultTravellers(adultStartindx, adultendindx,3, data)
     //   VerifyAdultTravellers(adultStartindx,adultendindx,data)
        if (childstrtindx > 0) {
            //Add Travellers5 - child To 1 - Child
            addSaveAndVerifyChildTravellers(childstrtindx, childendindx,data, children)
        }
        sleep(3000)
        WebDriverWait*/
        sleep(1000)
    }
    /* Following method is to capture entered traveler details*/
    def storeTravelerDetails(HotelSearchData data, HotelTransferTestResultData resultData) {
        at ItineraryTravllerDetailsPage

        resultData.hotel.itineraryPage.expectedTravlerCount = itineraryPageTravlerCount()
        resultData.hotel.itineraryPage.expectedTravlerCount = resultData.hotel.itineraryPage.expectedTravlerCount.toString().toInteger()
        println("travelersize" + resultData.hotel.itineraryPage.expectedTravlerCount)
        resultData.hotel.itineraryPage.expectedleadTravellerName = data.expected.firstName + " " + data.expected.lastName
        resultData.hotel.itineraryPage.actualLeadTravellerName = getLeadTravellerName(0)
        resultData.hotel.itineraryPage.actualLeadTravellerName = resultData.hotel.itineraryPage.actualLeadTravellerName.toString().replace("Mr", "").trim()
        //Capture lead traveller - telephone number
        resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum = data.expected.countryCode + " " + data.expected.telephone_Num
        resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum = getLeadTravellerName(1)
        //Caputre lead traveller - email address
        resultData.hotel.itineraryPage.expectedleadTravellerEmail = data.expected.emailAddr
        resultData.hotel.itineraryPage.actualLeadTravellerEmail = getLeadTravellerName(2)
        //capture traveller 2 details
        resultData.hotel.itineraryPage.expectedTraveler2Details = getLeadTravellerName(3)
        println("traveler2 details" + resultData.hotel.itineraryPage.expectedTraveler2Details)
        //capture traveler 3 details
        resultData.hotel.itineraryPage.expectedTraveler3Details = getLeadTravellerName(4)
    }

    protected def bookHotelItem(int index) {
        at ItineraryTravllerDetailsPage
        sleep(500)
        clickOnBookingIcon(index)
        sleep(5000)
        waitTillLoadingIconDisappears()
        //Selecting travellers 1,2,3
       /* clickOnTravellerCheckBox(0)
        sleep(3000)
        clickOnTravellerCheckBox(1)
        sleep(3000)
        clickOnTravellerCheckBox(2)
        sleep(3000)*/
        scrollToRemarksAndClick(1)
        sleep(1000)
        clickWilArrivWithOutVochrCheckBox()
        sleep(3000)
        //Click on Confirm Booking
        clickConfirmBooking()
        //wait for confirmation page
        waitTillConformationPage()
        //Addition of Assetion to check error msg when user click's on confirm booking
        softAssert.assertTrue(checkBookingErrorDispStatus(),"Error occured, User is not able to book an item.")
        sleep(5000)
        scrollToTopOfThePage()
        //click on Close lightbox X function
        coseBookItenary()
        sleep(3000)
    }

    /* Generic method to store Hotel Card Details in itinerary page */
    def storeItemDetails(int hotelDetailsIndex, int itemNum,String itemName) {
        at ItineraryTravllerDetailsPage
        Map dataMap = [:]
        dataMap.expectedHotelName = getHotelNameOnsuggstedItem(hotelDetailsIndex)
        dataMap.expectedImgURL = getImageURLInSuggestedItem(hotelDetailsIndex)
        dataMap.expectedItemCancellationLinkText = getItinenaryFreeCnclTxtInSuggestedItem(hotelDetailsIndex)
        clickOnItemCancllationLink(hotelDetailsIndex)
        sleep(1000)
        dataMap.expectedItemCancellationText = getCancellationItemOverlayHeaders(0)
        clickOnCloseButtonSuggestedItemsCancelpopup()
        sleep(1000)
        dataMap.expectedItemDuration = getItenaryDurationInSuggestedItem(hotelDetailsIndex)
        if("hotel".equals(itemName)){
            dataMap.expectedStarRating = getStarRatingSuggestedItem(hotelDetailsIndex)
            dataMap.expectedStarRating = dataMap.expectedStarRating.toString().replace("Stars", "").trim()
            dataMap.expectedItemDesc = getItinenaryDescreptionInSuggestedItem(hotelDetailsIndex)
        }
        if(("transfers").equals(itemName)){
            dataMap.expectedTransferDesc = getTransfersDesc(hotelDetailsIndex-1)
            dataMap.expectedIconsText = getTransfersIconsText(hotelDetailsIndex-1)
            dataMap.expectedItemDesc = getItinenaryDescreptionInSuggestedItem(hotelDetailsIndex)
        }
        if(("activity").equals(itemName)){
            dataMap.expectedActivityIconsText = getActivityIconsText(hotelDetailsIndex)
        }
        switch (itemNum) {
            case 0:
                dataMap.expectedItemStatus = getBookedItemStatus(hotelDetailsIndex)
                dataMap.expectedBookedItemBookingID = getBookingId(hotelDetailsIndex)
                dataMap.expectedItemPrice = getItenaryPriceInBookedItem(hotelDetailsIndex + 1)
                dataMap.expectedItemTravelerMicro = getTravelerNamesMicros(hotelDetailsIndex)
                dataMap.expectedItemTravelerMicro = dataMap.expectedItemTravelerMicro.toString().replaceAll("\\s+", " ")
                dataMap.expectedItemRemarkComment = getRemarksInBookedSection(hotelDetailsIndex)
                dataMap.expectedItemVoucher = verifyVoucherPresent(hotelDetailsIndex)
                dataMap.expectedCheckInDate = getCheckinDate(hotelDetailsIndex)
                break;

            case 1:
                dataMap.expectedItemPrice = getItenaryPriceInBookedItem(hotelDetailsIndex + 1)
                dataMap.expectedItemStatus = getRoomStatusfromSuggestedItem(hotelDetailsIndex)
                break;

            case 2:
                //Non GTA ITems
                break;
            default: break;
        }


        dataMap
    }
    def totalBookedItemsPrice(int index){
        def dataMap = [:]
        dataMap.expectedItemSubTotalLabel = getTotalLabelTxt(index)
        dataMap.expectedItemSubTotal = getItenaryPriceInSuggestedItem(index)
        List list = dataMap.expectedItemSubTotal.toString().split("\\s+")
        dataMap.expectedItemSubTotal = list.get(0)
        dataMap.expectedItemCurrency = list.get(1)

        dataMap
    }


    /* Following method is to calculate sub total of booked and non-booked items in itinerary page */
   def addItemsPrice() {
        Map dataMap = [:]
        Map hotel1DataMap = totalBookedItemsPrice(0)
        Map hotel2DataMap = totalBookedItemsPrice(1)
        Float totalPrice = Float.parseFloat(hotel1DataMap.expectedItemSubTotal.toString()) + Float.parseFloat(hotel2DataMap.expectedItemSubTotal.toString())
        dataMap.hotelItemsTotalPrice = totalPrice.toString()
        dataMap
    }

    def storeItemsInItinerary(HotelTransferTestResultData resultData) {
        resultData.hotel.itineraryPage.itineraryDetails = storeItineraryDetails()
        resultData.hotel.itineraryPage.bookedItem = storeItemDetails(0, 0,"hotel")
        resultData.hotel.itineraryPage.bookedItem1 = storeItemDetails(1,0,"hotel")
        resultData.hotel.itineraryPage.NonBookedItem = storeItemDetails(2, 1,"hotel")
        resultData.hotel.itineraryPage.hotelItemPrice = addItemsPrice()

    }

    /* click on share itinerary button in itinerary page */
    protected def "clickOnShareItinerary"(HotelTransferTestResultData resultData) {
        at ItenaryPage
        clickShareItineraryButtonAgentPos()
        sleep(3000)
       /* at ShareItineraryTransfersPage
        resultData.hotel.shareItineraryPage.actualEmailID = getEmail()
        println "share itinerary email" + resultData.hotel.shareItineraryPage.actualEmailID*/
    }

    /* Capture share itinerary header details in share itinerary page */

    protected def "ShareItineraryHeader"(HotelTransferTestResultData resultData) {
        at ShareItineraryPage
        sleep(2000)
        resultData.hotel.shareItineraryPage.backButtonStatus = verifyBackArrowDisplayed()
        resultData.hotel.shareItineraryPage.actualItineraryID = getItinearyID(0)
        List<String> list = getTravelerNameInHeader()
        resultData.hotel.shareItineraryPage.actualLeadName = list.getAt(0).trim()
        resultData.hotel.shareItineraryPage.peopleCount = Integer.valueOf(list.getAt(1))
        resultData.hotel.shareItineraryPage.peopleLabel = list.get(2)
        resultData.hotel.shareItineraryPage.shareURLButton = verifyShareURLButton()
        resultData.hotel.shareItineraryPage.saveeButton = verifySaveButton()
        resultData.hotel.shareItineraryPage.introductionText1 = getShareItinearyIntroductionText(0)
        resultData.hotel.shareItineraryPage.introductionText2 = getShareItinearyIntroductionText(1)
        println "share itinerary ID and Name" + resultData.hotel.shareItineraryPage.actualItineraryID


    }

    protected def "ShareItinerarySection1"(HotelSearchData data, HotelTransferTestResultData resultData) {
        at ShareItineraryPage
        resultData.hotel.shareItineraryPage.iconStatus1 = verifySectionIcon(0)
        resultData.hotel.shareItineraryPage.section1Header = verifySection1TitleDisplayed()
        resultData.hotel.shareItineraryPage.checkBoxLabelSelectAll = getLabelText(0)
        resultData.hotel.shareItineraryPage.checkBoxLabelBooked = getLabelText(1)
        resultData.hotel.shareItineraryPage.checkBoxLabelNonBooked = getLabelText(2)
        resultData.hotel.shareItineraryPage.checkBoxLabelNonTravelCube = getLabelText(3)
        resultData.hotel.shareItineraryPage.actualChkBoxClrSelectAll = getCheckboxColorNText(0)
        resultData.hotel.shareItineraryPage.actualChkBoxClrSelected = getCheckboxColorNText(1)
        resultData.hotel.shareItineraryPage.actualChkBoxNonSelected = getCheckboxColorNText(2)
        resultData.hotel.shareItineraryPage.actualChkBoxNonTCube = getCheckboxColorNText(3)
        resultData.hotel.shareItineraryPage.chkBox1TickmarkColor = verifySection1CheckboxTickmark(0, data.expected.chkBox3Color)
        resultData.hotel.shareItineraryPage.chkBox2TickmarkColor = verifySection1CheckboxTickmark(1, data.expected.chkBox3Color)
        resultData.hotel.shareItineraryPage.chkBox3TickmarkColor = verifySection1CheckboxTickmark(2, data.expected.chkBox3Color)
        resultData.hotel.shareItineraryPage.chkBox4TickmarkColor = verifySection1CheckboxTickmark(3, data.expected.chkBox3Color)
    }

    /* Gereric method to validate section 2 and section 3 functionality in share itinerary */
    protected def getSectionData(int sectionNum) {
        /* Generic method to validate Section Details */
        def dataMap = [:]
        def buttonLabels = []
        def buttonFunctionality = []
        def buttonDefaultStatus = []
        // ClickOnselectAll()
        dataMap.iconStatus = verifySectionIcon(sectionNum - 1)
        dataMap.sectionHeader = getSectionHeader(sectionNum - 2)
        dataMap.sectionHeaderSubTitle = getSubTitle(sectionNum - 2)
        switch (sectionNum) {

            case 2:
                for (int i = 0; i <= 3; i++) {
                    buttonLabels.add(getButtonLabelText(i))
                    buttonDefaultStatus.add(getToggleButtonDefaultStatus(i))
                    sleep(2000)
                    buttonFunctionality.add(getToggleButtonFuntionalityStatus(i))
                }

                break;
            case 3:
                for (int i = 4; i <= 6; i++) {

                    buttonLabels.add(getButtonLabelText(i))
                    buttonDefaultStatus.add(getToggleButtonDefaultStatus(i))
                    buttonFunctionality.add(getToggleButtonFuntionalityStatus(i))
                }
                break;
            case 4:
                //Non-GTA Items
                break;

            default: break;
        }
        dataMap.buttonLabel = buttonLabels
        dataMap.buttonDefaultValue = buttonDefaultStatus
        dataMap.buttonFunctionlity = buttonFunctionality
        println("buttonLabelstatus" + dataMap.buttonLabel)
        println("button verification" + dataMap)
        dataMap

    }
    /* Generic method to capture hotel card details for booked and non-booked items in share itinerary */
    def shareItineraryItems(int hotelDetailsIndex, int sectionNum,String itemName) {
        /* Generic method to store Hotel Card Details */
        at ShareItineraryPage
        def dataMap = [:]
        dataMap.actualHotelName = getHotelItemName(hotelDetailsIndex)
        dataMap.actualImgURL = getImageUrlOnSharePage(hotelDetailsIndex)
        dataMap.actualStayNights = stayNights(hotelDetailsIndex)
        dataMap.actualCommission = verifyCommission(hotelDetailsIndex)
        if(("hotel").equals(itemName)){
            dataMap.actualStarRating = getStarRating(hotelDetailsIndex)
            dataMap.actualItemDesc =   getHotelNTransferPaxDetails(hotelDetailsIndex)
            dataMap.hotelStatus = getStatus(hotelDetailsIndex)
        }
        if(("transfers").equals(itemName)){
            dataMap.actualTransferItemDesc =   getHotelNTransferPaxDetails(hotelDetailsIndex)
            dataMap.transferStatus = getAvailabilityStatus(hotelDetailsIndex)
        }
        if(("activity").equals(itemName)){
            dataMap.activityStatus = getAvailabilityStatus(hotelDetailsIndex)
        }
        switch (sectionNum) {
            case 2:
                dataMap.date = getBookingDate(hotelDetailsIndex)
                dataMap.date = dataMap.date.toString().toUpperCase()
                dataMap.bookingID = getPassiveItemBookingID(hotelDetailsIndex)
                resetToggleBookedSection()
                sleep(1000)
                dataMap.actualTravelerMicro = getTravellerDetails(hotelDetailsIndex)
                dataMap.actualTravelerMicro = dataMap.actualTravelerMicro.toString().replaceAll("\\s+", " ")
                dataMap.actualItemRemarkComment = getPassiveItemComments(hotelDetailsIndex)
                dataMap.actualItemVoucher = verifyIfVoucherIsDisplayed(hotelDetailsIndex)
                break;
            case 3:
                resetToggleNonBookedSection()
                sleep(3000)
                dataMap.checkDateNotToDisplay = verifyNonBookedItemCheckDateNotTODisplay(hotelDetailsIndex)
                dataMap.actualSINonBookedItemBookingID = verifyNonBookedItemBookingIDNotToDisplay(hotelDetailsIndex)
                dataMap.nonBookedItemTravelerDetails = verifyNonBookedItemTravellerDetailsNotToDisplay(hotelDetailsIndex)
                dataMap.nonBookedItemComments = verifyNonBookedItemCommentsNotToDisplay(hotelDetailsIndex)
                break;
        }
        dataMap.cancellationLinkText = getCancellationLinkText(hotelDetailsIndex)
        sleep(1000)
         clickFreeCancellationLink(hotelDetailsIndex)
        sleep(2000)
        dataMap.CancellationHeader = cancellationPolicyHeader(0)
        coseBookItenary()
        sleep(1000)
        dataMap.actualItemPrice = getIndividualPrice(hotelDetailsIndex)
        dataMap.actualCurrency = getCurrencyType(hotelDetailsIndex)
        dataMap.actualItemPrice = dataMap.actualItemPrice + " " + dataMap.actualCurrency
        dataMap
    }
    def storeShareItineraryPriceDetails(HotelTransferTestResultData resultData){
        at ShareItineraryPage
        resultData.hotel.shareItineraryPage.shareItiTotalAmount = storeTotalLabelNAmount()
        resultData.hotel.shareItineraryPage.nonBookedItemLabel =getBookedItemLabel(1)
        resultData.hotel.shareItineraryPage.nonBookedItemPrice = getLabelPrice(1)
        resultData.hotel.shareItineraryPage.shareURLButton = verifyShareURLButton()

    }

    def storeShareItineraryValidations(HotelTransferTestResultData resultData) {
        at ShareItineraryPage
        ClickOnselectAll()
        sleep(1000)
        resultData.hotel.shareItineraryPage.section2Data = getSectionData(2)
        clickSaveButton()
        resultData.hotel.shareItineraryPage.section3Data = getSectionData(3)
        resultData.hotel.shareItineraryPage.saveButtonText =  getSaveNUpateButtonText(2)
        resultData.hotel.shareItineraryPage.BookedItem = shareItineraryItems(0, 2,"hotel")
        /*resultData.hotel.shareItineraryPage.BookedItem.actualStarRating = getStarRating(0)
        resultData.hotel.shareItineraryPage.BookedItem.actualItemDesc =   getHotelNTransferPaxDetails(0)
        resultData.hotel.shareItineraryPage.BookedItem.hotelStatus = getStatus(0)*/
        resultData.hotel.shareItineraryPage.BookedItem1 = shareItineraryItems(1, 2,"hotel")
       /* resultData.hotel.shareItineraryPage.BookedItem1.actualStarRating = getStarRating(1)
        resultData.hotel.shareItineraryPage.BookedItem1.actualItemDesc =   getHotelNTransferPaxDetails(1)
        resultData.hotel.shareItineraryPage.BookedItem1.hotelStatus = getStatus(1)*/
        resultData.hotel.shareItineraryPage.NonBookedItem = shareItineraryItems(2, 3,"hotel")
        /*resultData.hotel.shareItineraryPage.NonBookedItem.actualStarRating = getStarRating(2)
        resultData.hotel.shareItineraryPage.NonBookedItem.actualItemDesc =   getHotelNTransferPaxDetails(2)
        resultData.hotel.shareItineraryPage.NonBookedItem.hotelStatus = getStatus(2)*/
        sleep(1000)
        resultData.hotel.shareItineraryPage.totalAmount2 = storeTotalLabelNAmount()

        sleep(1000)
    }

    protected def storeTotalLabelNAmount(HotelTransferTestResultData resultData) {

        def dataMap = [:]
        at ShareItineraryPage
        String total = getCurrencyAndTotalText()
        String[] totalText = total.split("\\s+")
        dataMap.totalLabel = totalText[0].toString().trim()
        dataMap.currency = totalText[1].toString().trim()
        dataMap.totalAmount = getAmount()  //total amount
        dataMap.labelTotalAmount = getTotalPrice(0) //booked items total
        // booked items label n currency
        String bookedItmesLabel = getBookedItemLabel(0)
        String[] split = bookedItmesLabel.split("\\s+")
        dataMap.labelTotal = split[0]+" "+split[1]
        dataMap.currencyBooked = split[2]

        dataMap
    }


    /* Generic method to assert two lists and display failed steps individually in report */
    protected void assertList(List expected, List actual, String message) {
        int i=0,j=0;
        boolean flag = true;
        if (actual == null || expected == null) {
            softAssert.assertTrue(false, "Unable to compare as one of the value is null $expected(expected) != $actual(actual)\n")
        } else {
            try {

                for ( ; i < expected.size(); i++) {

                    if(!actual.contains(expected[i])){
                        softAssert.fail(message+":"+expected[i]+":not present in actual list \n")
                    }
                }
            }

            catch (Exception e ) {
                print("Failed to assert got exception caused by: " + e.getCause())
                softAssert.assertNull(e, "\n $message" +expected.get(i) != actual.get(j))
            }

        }
    }

    protected
    def "ShareItineraryOverlay"(HotelSearchData data, HotelTransferTestResultData resultData, TravelerTestResultData tData) {
        at ShareItineraryPage
        clickShareURLButton(1)
        sleep(1000)
        resultData.hotel.shareItineraryPage.shareItineraryHeaderText = getShareItineraryOverlayHeaderText()
        resultData.hotel.shareItineraryPage.closeButton = verifyLightBoxCloseButton()
        resultData.hotel.shareItineraryPage.shareItineraryText = getShareItineraryTextNOpenNewWindowTxt(0)
        resultData.hotel.shareItineraryPage.travelerLink = getTravelerDetailsInOverlay(0)
        resultData.hotel.shareItineraryPage.overlayItineraryID = getTravelerDetailsInOverlay(1)
        String[] split = resultData.hotel.shareItineraryPage.overlayItineraryID.toString().split(":")
        resultData.hotel.shareItineraryPage.overlayIID = split[1]
        resultData.hotel.shareItineraryPage.leadTravelerName = getTravelerDetailsInOverlay(2)
        resultData.hotel.shareItineraryPage.leadTravelerName = resultData.hotel.shareItineraryPage.leadTravelerName.toString().replace("&nbsp;", " ")
        resultData.hotel.shareItineraryPage.openNewWindowLinkText = getShareItineraryTextNOpenNewWindowTxt(1)
        resultData.hotel.shareItineraryPage.copyLinkButton = verifyCopyLinkButton()
        resultData.hotel.shareItineraryPage.expectedID = data.expected.itineraryIDText + resultData.hotel.itineraryPage.itineraryDetails.actualSavedItnrID
        // verify new window link step need to be added
        clickOnNewWindowLink()
        navigate()
        at TravelerViewMyItineraryPage
        resultData.hotel.shareItineraryPage.travelerSite=verifyTermsAndConditonsLink()
    }


    /* Start of Test case Validations */

    protected def "VerifyaddItemsToItinerary"(HotelSearchData data, HotelTransferTestResultData resultData) {

        //Hotel Search Results Returned and Hotel name on PDP is validated
        assertionEquals(resultData.hotel.searchResults.firstSearch.actualHotelName, data.expected.cityAreaHotelText, "Transfer Search Results Returned Actual & Expected don't match")

        //Validate Itinenary count in itinenary builder after adding one hotel

        assertionEquals(resultData.hotel.searchResults.firstSearch.actualItemCount, data.expected.itineraryBarCount, "Transfer Name Added To Itinerary Actual & Expected don't match")
        //Hotel Search Results Returned and Hotel name on PDP is validated
        assertionEquals(resultData.hotel.searchResults.secondSearch.actualHotelName, data.expected.cityAreaHotelText, "Transfer Name Added To Itinerary Actual & Expected don't match")
        //Validate Itinenary count in itinenary builder after adding 2 hotels
        assertionEquals(resultData.hotel.searchResults.secondSearch.actualItemCount, data.expected.itineraryBarTotalCount, "Transfer Name Added To Itinerary Actual & Expected don't match")
    }
    protected def "VerifyitineraryNameUpdate"(HotelSearchData data, HotelTransferTestResultData resultData){

        //Validate Itinerary Page Traveller Label Txt
        assertionEquals(resultData.hotel.itineraryPage.itineraryDetails.actualSavedItnrName,resultData.hotel.itineraryPage.expectedItnrName, "Itinerary Page, Itinerary Name Update Actual & Expected don't match")

    }


    protected def "verifyBookingItem"(HotelSearchData data, HotelTransferTestResultData resultData) {
        assertionEquals(resultData.hotel.itineraryPage.bookedItem.expectedItemStatus, data.expected.bookingStatus, "Item 1 is not booked")
    }

    protected def "VerifyShareItinerary"(HotelSearchData data, HotelTransferTestResultData resultData) {
        assertionEquals(resultData.hotel.shareItineraryPage.actualEmailID, data.expected.emailAddr, "Email ID in shareItinerary,Itinerary are as expected ")
    }

    protected def "VerifyShareItineraryHeader"(HotelSearchData data, HotelTransferTestResultData resultData) {
        //assertTrue need to be verified
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.backButtonStatus, "Back button verified")
        assertionEquals(resultData.hotel.itineraryPage.itineraryDetails.expectedItnrIDName, resultData.hotel.shareItineraryPage.actualItineraryID, "ItineraryIDName in shareItinerary,Itinerary are matches")
        assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerName, resultData.hotel.shareItineraryPage.actualLeadName, "Lead Name's in shareItinerary,Itinerary are matches")
        assertionEquals(resultData.hotel.itineraryPage.expectedTravlerCount, resultData.hotel.shareItineraryPage.peopleCount, "People count in shareItinerary,Itinerary are matches")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.shareURLButton, "ShareURL Button exists")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.saveeButton, "Save Button exists")
        assertionEquals(resultData.hotel.shareItineraryPage.introductionText1, data.expected.introductionTxt1, "Introduction Text1 in shareItinerary,Itinerary are matches ")
        assertionEquals(resultData.hotel.shareItineraryPage.introductionText2, data.expected.introductionTxt2, "Introduction Text1 in shareItinerary,Itinerary are matches ")
        assertionEquals(resultData.hotel.shareItineraryPage.saveButtonText,data.expected.saveButtonText,"button text not changed to save n update")
    }

    protected def "VerifyShareItinerarySection1"(HotelSearchData data, HotelTransferTestResultData resultData) {
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.iconStatus1, "Section1 icon not displayed")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section1Header, "Section 1 Header text not present")
        assertionEquals(resultData.hotel.shareItineraryPage.checkBoxLabelSelectAll, data.expected.chkBoxSelectAll, "CheckBox selectAll Label Returned Actual & Expected don't match")
        assertionEquals(resultData.hotel.shareItineraryPage.checkBoxLabelBooked, data.expected.chkBoxSelected, "Checkbox Selected Label Returned Actual & Expected don't match")
        assertionEquals(resultData.hotel.shareItineraryPage.checkBoxLabelNonBooked, data.expected.chkBoxNotSelected, "Checkbox Non-Selected Label Returned Actual & Expected don't match")
        assertionEquals(resultData.hotel.shareItineraryPage.checkBoxLabelNonTravelCube, data.expected.chkBoxNonTravelerCube, "Checkbox Non-TravelerCube Label Returned Actual & Expected don't match")
        assertionEquals(resultData.hotel.shareItineraryPage.actualChkBoxClrSelectAll, data.expected.chkBox1Color, "Checkbox 1 color did not match")
        assertionEquals(resultData.hotel.shareItineraryPage.actualChkBoxClrSelected, data.expected.ChkBox2Color, "Checkbox 2 color did not match")
        assertionEquals(resultData.hotel.shareItineraryPage.actualChkBoxNonSelected, data.expected.chkBox3Color, "Checkbox 3 color did not match")
        assertionEquals(resultData.hotel.shareItineraryPage.actualChkBoxNonTCube, data.expected.chkBox4Color, "Checkbox 4 color did not match")
        /*softAssert.assertFalse(resultData.hotel.shareItineraryPage.chkBox1TickmarkColor, "Checkbox 1 tickmark present")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.chkBox2TickmarkColor, "Checkbox 2 tickmark not present")
        softAssert.assertFalse(resultData.hotel.shareItineraryPage.chkBox3TickmarkColor, "Checkbox 3 tickmark present")
        softAssert.assertFalse(resultData.hotel.shareItineraryPage.chkBox4TickmarkColor, "Checkbox 4 tickmark present")*/


    }

    protected def "VerifyShareItinerarySection2"(HotelSearchData data, HotelTransferTestResultData resultData) {

        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.iconStatus, "Section2 icon not displayed")
        assertionEquals(resultData.hotel.shareItineraryPage.section2Data.sectionHeader, data.expected.section2Header, "Section2 Title not displayed")
        assertionEquals(resultData.hotel.shareItineraryPage.section2Data.sectionHeaderSubTitle, data.expected.sectionSubTitle, "Section2 Sub Title not displayed")
    }


    protected
    def "VerifyShareItinerarySection2ToggleButton1"(HotelSearchData data, HotelTransferTestResultData resultData) {

        assertionEquals(resultData.hotel.shareItineraryPage.section2Data.buttonLabel.get(0), data.expected.buttonLabelPrice, "Toggle Button 1 Text is not as expected")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonDefaultValue.get(0), "Button 1 Default value not set to YES")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonFunctionlity.get(0), "Toggle Button1 is not functional")

    }

    protected
    def "VerifyShareItinerarySection2ToggleButton2"(HotelSearchData data, HotelTransferTestResultData resultData) {
        assertionEquals(resultData.hotel.shareItineraryPage.section2Data.buttonLabel.get(1), data.expected.buttonLabelCancellation, "Toggle Button 2 Text is not as expected")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonDefaultValue.get(1), "Button 2 Default value not set to YES")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonFunctionlity.get(1), "Toggle Button2 is not functional")

    }

    protected
    def "VerifyShareItinerarySection2ToggleButton3"(HotelSearchData data, HotelTransferTestResultData resultData) {
        assertionEquals(resultData.hotel.shareItineraryPage.section2Data.buttonLabel.get(2), data.expected.buttonLabelTotalPrice, "Toggle Button 3 Text is not as expected")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonDefaultValue.get(2), "Button 3 Default value not set to YES")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonFunctionlity.get(2), "Toggle Button3 is not functional")

    }

    protected
    def "VerifyShareItinerarySection2ToggleButton4"(HotelSearchData data, HotelTransferTestResultData resultData) {
        assertionEquals(resultData.hotel.shareItineraryPage.section2Data.buttonLabel.get(3), data.expected.buttonLabelVoucher, "Toggle Button 4 Text is not as expected")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonDefaultValue.get(3), "Button 4 Default value not set to YES")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section2Data.buttonFunctionlity.get(3), "Toggle Button4 is not functional")

    }

    protected
    def "VerifyBookedHotelItemAgainstItinerary"(HotelSearchData data, HotelTransferTestResultData resultData) {

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.date, resultData.hotel.itineraryPage.bookedItem.expectedCheckInDate, "Booking date not displayed as expected")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.bookingID, resultData.hotel.itineraryPage.bookedItem.expectedBookedItemBookingID, "BookingID did not match against itinerary page")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualImgURL, resultData.hotel.itineraryPage.bookedItem.expectedImgURL, "Hotel Image did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualHotelName, resultData.hotel.itineraryPage.bookedItem.expectedHotelName, "HotelName did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualStarRating, resultData.hotel.itineraryPage.bookedItem.expectedStarRating, "Hotel Rating did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualItemDesc, resultData.hotel.itineraryPage.bookedItem.expectedItemDesc, "Pax Details did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.cancellationLinkText, resultData.hotel.itineraryPage.bookedItem.expectedItemCancellationLinkText, "Cancellation Link Text did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.CancellationHeader, resultData.hotel.itineraryPage.bookedItem.expectedItemCancellationText, "Cancellation Header Text in Cancellation Overlay did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.hotelStatus, data.expected.hotelStatus, "Available status not displayed in Share Itinerary page:")

        softAssert.assertTrue(resultData.hotel.shareItineraryPage.BookedItem.actualCommission, "Commission Displayed in Share Itinerary page:")

        //assertionEquals(resultData.hotel.shareItineraryPage.actualSICommission, resultData.hotel.itineraryPage.expectedBookedItemCommission, "Commission did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualStayNights, resultData.hotel.itineraryPage.bookedItem.expectedItemDuration, "Stay Nights text did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualItemPrice, resultData.hotel.itineraryPage.bookedItem.expectedItemPrice, "Booked Item price did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualTravelerMicro, resultData.hotel.itineraryPage.bookedItem.expectedItemTravelerMicro, "Traveler Details did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem.actualItemRemarkComment, resultData.hotel.itineraryPage.bookedItem.expectedItemRemarkComment, "Please Note: Additonal Comments did not match")

        softAssert.assertTrue(resultData.hotel.shareItineraryPage.BookedItem.actualItemVoucher, "Vochuer Image not displayed in Share Itinerary Page")
    }
    protected
    def verifyShareItineraryOnRequestItem(HotelSearchData data, HotelTransferTestResultData resultData) {

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.date, resultData.hotel.itineraryPage.bookedItem1.expectedCheckInDate, "Booking date not displayed as expected")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.bookingID, resultData.hotel.itineraryPage.bookedItem1.expectedBookedItemBookingID, "BookingID did not match against itinerary page")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualImgURL, resultData.hotel.itineraryPage.bookedItem1.expectedImgURL, "Hotel Image did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualHotelName, resultData.hotel.itineraryPage.bookedItem1.expectedHotelName, "HotelName did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualStarRating, resultData.hotel.itineraryPage.bookedItem1.expectedStarRating, "Hotel Rating did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualItemDesc, resultData.hotel.itineraryPage.bookedItem1.expectedItemDesc, "Pax Details did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.cancellationLinkText, resultData.hotel.itineraryPage.bookedItem1.expectedItemCancellationLinkText, "Cancellation Link Text did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.CancellationHeader, resultData.hotel.itineraryPage.bookedItem1.expectedItemCancellationText, "Cancellation Header Text in Cancellation Overlay did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.hotelStatus, data.expected.onRequestStatus, "Available status not displayed in Share Itinerary page:")

        softAssert.assertTrue(resultData.hotel.shareItineraryPage.BookedItem1.actualCommission, "Commission Displayed in Share Itinerary page:")

        //assertionEquals(resultData.hotel.shareItineraryPage.actualSICommission, resultData.hotel.itineraryPage.expectedBookedItemCommission, "Commission did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualStayNights, resultData.hotel.itineraryPage.bookedItem1.expectedItemDuration, "Stay Nights text did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualItemPrice, resultData.hotel.itineraryPage.bookedItem1.expectedItemPrice, "Booked Item price did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualTravelerMicro, resultData.hotel.itineraryPage.bookedItem1.expectedItemTravelerMicro, "Traveler Details did not match")

        assertionEquals(resultData.hotel.shareItineraryPage.BookedItem1.actualItemRemarkComment, resultData.hotel.itineraryPage.bookedItem1.expectedItemRemarkComment, "Please Note: Additonal Comments did not match")

        softAssert.assertFalse(resultData.hotel.shareItineraryPage.BookedItem1.actualItemVoucher, "Vochuer Image not displayed in Share Itinerary Page")
    }
    def "VerifyShareItinerarySection3"(HotelSearchData data, HotelTransferTestResultData resultData) {

        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section3Data.iconStatus, "Section2 icon not displayed")
        assertionEquals(resultData.hotel.shareItineraryPage.section3Data.sectionHeader, data.expected.section3Header, "Section2 Title not displayed")
        assertionEquals(resultData.hotel.shareItineraryPage.section3Data.sectionHeaderSubTitle, data.expected.sectionSubTitle, "Section2 Sub Title not displayed")
    }

    def "VerifySection3ToggleButton1"(HotelSearchData data, HotelTransferTestResultData resultData) {

        assertionEquals(resultData.hotel.shareItineraryPage.section3Data.buttonLabel.get(0), data.expected.buttonLabelPrice, "Non-booked Item:Toggle Button 1 Text is not as expected")
        softAssert.assertFalse(resultData.hotel.shareItineraryPage.section3Data.buttonDefaultValue.get(0), "Non-booked Item: Button 1 Default value not set to NO")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section3Data.buttonFunctionlity.get(0), "Non-booked Item: Toggle Button 1 is not functional")

    }

    def "VerifySection3ToggleButton2"(HotelSearchData data, HotelTransferTestResultData resultData) {

        assertionEquals(resultData.hotel.shareItineraryPage.section3Data.buttonLabel.get(1), data.expected.buttonLabelCancellation, "Non-booked Item:Toggle Button 2 Text is not as expected")
        softAssert.assertFalse(resultData.hotel.shareItineraryPage.section3Data.buttonDefaultValue.get(1), "Non-booked Item: Button 2 Default value not set to No")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section3Data.buttonFunctionlity.get(1), "Non-booked Item: Toggle Button 2 is not functional")
    }

    def "VerifySection3ToggleButton3"(HotelSearchData data, HotelTransferTestResultData resultData) {
        assertionEquals(resultData.hotel.shareItineraryPage.section3Data.buttonLabel.get(2), data.expected.buttonLabelTotalPrice, "Non-booked Item:Toggle Button 3 Text is not as expected")
        softAssert.assertFalse(resultData.hotel.shareItineraryPage.section3Data.buttonDefaultValue.get(2), "Non-booked Item: Button 3 Default value not set to YES")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.section3Data.buttonFunctionlity.get(2), "Non-booked Item: Toggle Button 3 is not functional")

    }


    def "VerifyNonBookedItemCardAgainstItinerary"(HotelSearchData data, HotelTransferTestResultData resultData) {

        softAssert.assertFalse(resultData.hotel.shareItineraryPage.NonBookedItem.checkDateNotToDisplay, "CheckinDate displayed for Non-BookedItem")
        softAssert.assertFalse(resultData.hotel.shareItineraryPage.NonBookedItem.actualSINonBookedItemBookingID, "Booking ID displayed for Non-Booked Item:")

        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.actualImgURL, resultData.hotel.itineraryPage.NonBookedItem.expectedImgURL, "NonBookedItem:Hotel Image not matching against itinerary:")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.actualHotelName, resultData.hotel.itineraryPage.NonBookedItem.expectedHotelName, "NonBookedItem: Hotel Name not matching against itinerary:")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.actualStarRating, resultData.hotel.itineraryPage.NonBookedItem.expectedStarRating, "NonBookedItem: Star Rating not matching against itinerary")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.actualItemDesc, resultData.hotel.itineraryPage.NonBookedItem.expectedItemDesc, "NonBookedItem: Pax Details not matching against itinerary:")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.cancellationLinkText, resultData.hotel.itineraryPage.NonBookedItem.expectedItemCancellationLinkText, "NonBookedItem: CancellationLNKTXT not matching:")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.CancellationHeader, resultData.hotel.itineraryPage.NonBookedItem.expectedItemCancellationText, "Non Booked Item: Cancellation Overlay not matching:")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.actualStayNights, resultData.hotel.itineraryPage.NonBookedItem.expectedItemDuration, "Non Booked Item: Stay Nights not matching:")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.hotelStatus, resultData.hotel.itineraryPage.NonBookedItem.expectedItemStatus, "Non Booked Item: Available Status not matching:")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.NonBookedItem.actualCommission, "Non Booked Item: Commission Displayed:")
        assertionEquals(resultData.hotel.shareItineraryPage.NonBookedItem.actualItemPrice, resultData.hotel.itineraryPage.NonBookedItem.expectedItemPrice, "Non Booked Item: prices are not matching:")

        softAssert.assertFalse(resultData.hotel.shareItineraryPage.NonBookedItem.nonBookedItemTravelerDetails, "NonBookedItem: Traveler Details displayed:")
        softAssert.assertFalse(resultData.hotel.shareItineraryPage.NonBookedItem.nonBookedItemComments, "NonBookedItem: Remarks displayed:")

    }

    protected def "VerifyShareItiBookingTotalRate"(HotelSearchData data, HotelTransferTestResultData resultData) {

        assertionEquals(resultData.hotel.shareItineraryPage.toalAmount1.totalLabel, data.expected.totalTxt, "Total Label not displayed for Booked Items")
        assertionEquals(resultData.hotel.shareItineraryPage.toalAmount1.currency, resultData.hotel.itineraryPage.hotelItemPrice.expectedItemCurrency, "Total Amount for Booked Items not matching against Itinerary")
        assertionEquals(resultData.hotel.shareItineraryPage.toalAmount1.totalAmount, resultData.hotel.itineraryPage.hotelItemPrice.expectedItemSubTotal, "Currency for Booked Items not matching against Itinerary")
    }

    protected def "VerifyBNBTotalRate"(HotelSearchData data, HotelTransferTestResultData resultData) {
        assertionEquals(resultData.hotel.shareItineraryPage.totalAmount2.totalLabel, data.expected.totalTxt, "Total Label not displayed for Booked Items")
        assertionEquals(resultData.hotel.shareItineraryPage.totalAmount2.currency, resultData.hotel.itineraryPage.hotelItemPrice.expectedItemCurrency, "Total Amount for Booked Items not matching against Itinerary")
        assertionEquals(resultData.hotel.shareItineraryPage.totalAmount2.totalAmount, resultData.hotel.itineraryPage.hotelItemPrice.hotelItemsTotalPrice, "Currency for Booked Items not matching against Itinerary")
    }

    protected
    def "VerifyShareItineraryOverlay"(HotelSearchData data, HotelTransferTestResultData resultData, TravelerTestResultData tData) {
        assertionEquals(resultData.hotel.shareItineraryPage.shareItineraryHeaderText, data.expected.shareItineraryHeaderText, "Share Itinerary Header Text not matching with excepted result.")
        assertionEquals(resultData.hotel.shareItineraryPage.shareItineraryText, data.expected.shareItineraryText, "Share Itinearary Overlay Text not matching with expected Text.")
        assertionEquals(resultData.hotel.shareItineraryPage.travelerLink, data.expected.travelerLinkText, "Traveler Site Link Text not matching with expected Text.")
        assertionEquals(resultData.hotel.shareItineraryPage.overlayItineraryID, resultData.hotel.shareItineraryPage.expectedID, "ItineraryID not matching as expected.")
        assertionEquals(resultData.hotel.shareItineraryPage.leadTravelerName, data.expected.itineraryLeadText, "LeadName not displayed as expected.")
        assertionEquals(resultData.hotel.shareItineraryPage.openNewWindowLinkText, data.expected.expectedOpenWindowLinkText, "Link Text not displayed as expected.")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.copyLinkButton, "CopyLink Button not displayed.")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.closeButton, "Close Button is not displayed.")
        softAssert.assertTrue(resultData.hotel.shareItineraryPage.travelerSite, "User Could not be able to redirect to traveler site")

    }


}
