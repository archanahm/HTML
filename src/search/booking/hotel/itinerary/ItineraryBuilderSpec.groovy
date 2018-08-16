

package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import org.openqa.selenium.JavascriptExecutor
import spock.lang.Unroll

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.DateUtil

abstract class ItineraryBuilderSpec extends BaseSpec
{

	//public static resultMap = [:]
	public static Map<String, HotelTransferTestResultData>  resultMap = [:]
	public static Map<String, HotelTransferTestResultData>  verifyMap = [:]
	public static Map<String, HotelTransferTestResultData>  verifyCarouselMap = [:]
	//ClientData clientData = new ClientData("client21")
	ClientData clientData = new ClientData("client664")

	DateUtil dateUtil = new DateUtil()
	
	
	def dur=""
	int nights=0
	

	def "01 : Itinerary Builder  "()
	{
		given: "User is able to login and go to transfers view"
		
		
	
		 login(clientData)
		 at HotelSearchPage
		 HotelSearchData hotelData
		 HotelTransferTestResultData resultData
		 	 
		 hotelData = new HotelSearchData("Kingdom-SITUJ15XMLAuto")
		 resultData = new HotelTransferTestResultData("Kingdom-SITUJ15XMLAuto",hotelData)
		 resultMap.put(hotelData.hotelName, resultData)
		 addToItinerary(hotelData, resultData,0)
		 
		 hotelData = new HotelSearchData("Kaohsiung-Taiwan")
		 resultData = new HotelTransferTestResultData("Kaohsiung-Taiwan",hotelData)
		 resultMap.put(hotelData.hotelName, resultData)
		 addToItinerary(hotelData, resultData,1)
		 
		 hotelData = new HotelSearchData("LondonAndmore")
		 resultData = new HotelTransferTestResultData("LondonAndmore",hotelData)
		 resultMap.put(hotelData.hotelName, resultData)
		 addToItinerary(hotelData, resultData,1)
 
 //		Remove item from itinerary builder
 
		 //remove 2nd item from the itinerary builder
		 hotelData = new HotelSearchData("Kaohsiung-Taiwan")
		 resultData = new HotelTransferTestResultData("Kaohsiung-Taiwan",hotelData)
		 verifyMap.put(hotelData.hotelName, resultData)
		 removeItemFromItineraryBuilder(hotelData, resultData)
			

		//code added
		hotelData = new HotelSearchData("LondonAndmore")
		resultData = new HotelTransferTestResultData("LondonAndmore",hotelData)
		addToItineraryMultipleHotels(hotelData, resultData,1)

		//	Itinerary builder - Carousel
		hotelData = new HotelSearchData("CarouselValidations")
		resultData = new HotelTransferTestResultData("CarouselValidations",hotelData)
		verifyCarouselMap.put(hotelData.hotelName, resultData)
		addToItineraryCarousel(hotelData, resultData)
		
	 
	}
	
	private def addToItinerary(HotelSearchData data, HotelTransferTestResultData resultData,int hotelNum)
	{
		
		at HotelSearchPage

		//Enter Hotel Destination
		String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
		println "Cancel Date is:$cancelDate"
		resultData.hotel.searchResults.itineraryBuilder.expectedCancelDate=cancelDate
		enterHotelDestination(data.input.cityAreaHotelTypeText)
		selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)
		
		//Enter Check In and Check Out Dates
		entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
		sleep(3000)
		def checkInDt=getCheckInDate()
		def checkOutDt=getCheckOutDate()
		nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
		if (nights >1)
			dur= checkInDt+" - "+nights+" nights"
		else dur = checkInDt+" - "+nights+" night"
		println("$dur")
		resultData.hotel.searchResults.itineraryBuilder.expectedDurationTxt=dur
		
		clickPaxRoom()
		sleep(3000)
		//Enter Pax
		clickPaxRoom()
		selectNumberOfAdults(data.input.pax.toString(), 0)
		sleep(3000)
	   
		
		//click on Find button
		clickFindButton()
		sleep(7000)
				
		at HotelSearchResultsPage
		
		waitTillAddToItineraryBtnLoads()
		
		scrollToAddToItineraryBtn()
		
		
		resultData.hotel.searchResults.itineraryBuilder.expectedPrice=getItinenaryPrice(0)+" "+clientData.currency
		println("$resultData.hotel.searchResults.itineraryBuilder.expectedPrice")
		if(hotelNum==0){
			clickAddToitinerary(hotelNum)
		}else{
			scrollToSpecificItinryAndClickAddToItinryBtn(data.expected.cityAreaHotelText)
		}
		sleep(3000)
		at ItineraryTravllerDetailsPage
		selectOptionFromManageItinerary(data.input.manageItinryValue)
		sleep(3000)

		//commenting below lines since itinerary builder is removed in 10.3
		at HotelSearchResultsPage

		sleep(5000)
		//Capture items added to itinerary builder
		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Collapse
			hideItenaryBuilder()
			sleep(3000)
		}
		at HotelSearchResultsPage
		resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()
		println("$resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder")
	   //capture data for itinerary builder screen display status
	   resultData.hotel.searchResults.itineraryBuilder.actualitineraryBuilderSectionStatus=getItineraryBuilderSectionDisplayStatus()
	   //println("$resultData.hotel.searchResults.itineraryBuilder.actualitineraryBuilderSectionStatus")
	   at ItenaryBuilderPage
	   //collapse
	   hideItenaryBuilder()
	   sleep(5000)
	   at HotelSearchResultsPage
	   //capture collapsed status
	   resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionHiddenStatus=getItineraryBarSectionDisplayStatus()
	   //println("$resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionHiddenStatus")
	  	  	   
	   //at ItenaryBuilderPage
	   //Expand
	   //hideItenaryBuilder()
	   sleep(5000)
		at HotelSearchResultsPage

		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}
		   
	   at HotelSearchResultsPage
	   //capture expanded status
	   resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionOpenStatus=getItineraryBarSectionDisplayStatus()
	   println("Line 154 expanded status: $resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionOpenStatus")
	   
	   //capture hotel name in itinerary builder section
	   resultData.hotel.searchResults.itineraryBuilder.actualHotelName=getHotelNameInTitleCard(data.expected.itinCardIndex.toString().toInteger())
	   //println("$resultData.hotel.searchResults.itineraryBuilder.actualHotelName")
	   //capture hotel rating in itinerary builder section
	   //resultData.hotel.searchResults.itineraryBuilder.actualRating=getRatingForTheHotelItineraryBar(data.expected.itinCardIndex.toString().toInteger())
	   //println("$resultData.hotel.searchResults.itineraryBuilder.actualRating")
		resultData.hotel.searchResults.itineraryBuilder.actualRating=getStarRatingForTheHotelInItineraryBar(data.expected.itinCardIndex.toString().toInteger())
	   //Capture Items
	   resultData.hotel.searchResults.itineraryBuilder.actualItems=getItemsCountItineraryBar()
	   //println("$resultData.hotel.searchResults.itineraryBuilder.actualItems")
	   //Capture existence of Go To Itinerary Icon & Link
	   resultData.hotel.searchResults.itineraryBuilder.actualIconLinkExistence=getItinerarBarSectionGoToItineraryIconLinkExistence()
	   //Capture Go To Itinerary Text
	   resultData.hotel.searchResults.itineraryBuilder.actualGoToItinerarytxt=getGoToItineraryTextItineraryBar()
	   //Capture existence of Close button
	   resultData.hotel.searchResults.itineraryBuilder.actualCloseBtnExistence=getItinerarBarSectionCloseIconLinkExistence()
	   //Capture Close button text
	   //resultData.hotel.searchResults.itineraryBuilder.actualCloseBtnTxt=getCloseBtnTextItineraryBar()
	   //Capature Image Link
	   resultData.hotel.searchResults.itineraryBuilder.actualImgLnkExistence=getItinerarBarSectionImageIconLinkExistence(data.expected.itinCardIndex.toString().toInteger())
	   //Capture Image URL
	   //resultData.hotel.searchResults.itineraryBuilder.actualImgSrcURL=getImageSrcURLItineraryBar(data.expected.itinCardIndex.toString().toInteger())
	   //println("$resultData.hotel.searchResults.itineraryBuilder.actualImgSrcURL")
	   //Capture Itinerary Desc
	   String itineraryDesc=getItinenaryDescreption(data.expected.itinCardIndex.toString().toInteger())
	   println("$itineraryDesc")
	   List<String> tempItineraryDescList=itineraryDesc.tokenize(",")
	   println("$tempItineraryDescList")
	   String roomDescText=tempItineraryDescList.getAt(0)
	   resultData.hotel.searchResults.itineraryBuilder.actualroomdescTxt=roomDescText.trim()
	   println("$resultData.hotel.searchResults.itineraryBuilder.actualroomdescTxt")
	   String ratePlanDesc=tempItineraryDescList.getAt(1)
	   resultData.hotel.searchResults.itineraryBuilder.actualratePlanDescTxt=ratePlanDesc.trim()
	   println("$resultData.hotel.searchResults.itineraryBuilder.actualratePlanDescTxt")
	   String freeCanclTxt=tempItineraryDescList.getAt(2)
	   resultData.hotel.searchResults.itineraryBuilder.actualfreeCanclTxt=freeCanclTxt.trim()
	   println("$resultData.hotel.searchResults.itineraryBuilder.actualfreeCanclTxt")
	   
	   String expectedFreeCanclTxt=data.expected.freecancltxt+" "+resultData.hotel.searchResults.itineraryBuilder.expectedCancelDate.toUpperCase()
	   resultData.hotel.searchResults.itineraryBuilder.expectedFreeCanclTxt=expectedFreeCanclTxt
	   println("$resultData.hotel.searchResults.itineraryBuilder.expectedFreeCanclTxt")
	   //Capture Inventory Availability
	   resultData.hotel.searchResults.itineraryBuilder.actualInvAvailStatus=getAvailabilityItineraryBar(data.expected.itinCardIndex.toString().toInteger())
	   //Capture check in date and nights
		String chkInDateAndNightsPax=getItenaryDuration(data.expected.itinCardIndex.toString().toInteger())

		List<String> tmpList=chkInDateAndNightsPax.tokenize(",")
		String chkInDateAndNights=tmpList.getAt(0).trim()
		resultData.hotel.searchResults.itineraryBuilder.actualDurationtext=chkInDateAndNights
		println("$resultData.hotel.searchResults.itineraryBuilder.actualDurationtext")
	   at ItenaryBuilderPage
	   
	  /*//capture Itinerary reference number only once i.e. for first hotel search only
	   if(data.expected.captureItinRefNum.toString().toBoolean())
	  {
	    //capture new Itinerary reference number
	   String itineraryBuilderTitle = getItenaryBuilderTtile()
	   println("$itineraryBuilderTitle")
	   List<String> tempList=itineraryBuilderTitle.tokenize(" ")
	   //println("$tempList")
	   String itineraryId=tempList.getAt(0)+tempList.getAt(1)
	   resultData.hotel.searchResults.itineraryBuilder.retitineraryId=itineraryId
	   //println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryId")
	   String itineraryName=tempList.getAt(2)
	   resultData.hotel.searchResults.itineraryBuilder.retitineraryName=itineraryName
	   //println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryName")
	   
	  }*/
	   //capture new Itinerary reference number
	   String itineraryBuilderTitle = getItenaryBuilderTtile()
	   println("$itineraryBuilderTitle")
	   List<String> tempList=itineraryBuilderTitle.tokenize(" ")
	   //println("$tempList")
	   String itineraryId=tempList.getAt(0)+tempList.getAt(1)
	   resultData.hotel.searchResults.itineraryBuilder.retitineraryId=itineraryId
	   //println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryId")
	   String itineraryName=tempList.getAt(2)
	   resultData.hotel.searchResults.itineraryBuilder.retitineraryName=itineraryName
	   //println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryName")
	   
	   //capture Room Rate Amount and currency
	   resultData.hotel.searchResults.itineraryBuilder.actualroomRateAmntCurrnTxt=getItenaryPrice(data.expected.itinCardIndex.toString().toInteger())
	   println("$resultData.hotel.searchResults.itineraryBuilder.actualroomRateAmntCurrnTxt")

		at HotelSearchResultsPage
		//Expand
		if(getItineraryBarSectionDisplayStatus()==true){
			at ItenaryBuilderPage
			hideItenaryBuilder()
			sleep(3000)
		}


	   /*at HotelSearchPage
	   //Click on Hotels Tab
	   clickHotelsTab()*/
	   
	   
	   //Click on Go to Itinerary
	   
	   //Click on Close Itinerary - Title Window
	   //closeItenaryBuilder()
	   
	   
	}

	private def addToItineraryMultipleHotels(HotelSearchData data, HotelTransferTestResultData resultData,int hotelNum)
	{

		at HotelSearchPage

		//Enter Hotel Destination
		String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
		println "Cancel Date is:$cancelDate"

		enterHotelDestination(data.input.cityAreaHotelTypeText)
		selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

		//Enter Check In and Check Out Dates
		entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
		sleep(3000)


		clickPaxRoom()
		sleep(3000)
		//Enter Pax
		clickPaxRoom()
		selectNumberOfAdults(data.input.pax.toString(), 0)



		//click on Find button
		clickFindButton()
		sleep(7000)

		at HotelSearchResultsPage

		waitTillAddToItineraryBtnLoads()

		scrollToAddToItineraryBtn()

		for(int i=2;i<=data.input.itemsToBeAddedToItnrBuldr.toString().toInteger();i++)
		{
			at HotelSearchResultsPage
			scrollToSpecificAddToItineraryBtnAndClick(i)
			sleep(5000)
			at ItineraryTravllerDetailsPage
			clickOnBackButtonInItineraryScreen()
			sleep(3000)
		}


		//commenting below lines since itinerary builder is removed in 10.3
		at HotelSearchResultsPage

		sleep(5000)
		//Capture items added to itinerary builder
		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Collapse
			hideItenaryBuilder()
			sleep(3000)
		}

/*
		at HotelSearchResultsPage
		//Expand
		if(getItineraryBarSectionDisplayStatus()==true){
			at ItenaryBuilderPage
			hideItenaryBuilder()
			sleep(3000)
		}

*/



	}

	private def removeItemFromItineraryBuilder(HotelSearchData data, HotelTransferTestResultData resultData)
	{
				
		at HotelSearchPage
		//Enter Hotel Destination
		String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
		println "Cancel Date is:$cancelDate"
		resultData.hotel.removeItemPage.expectedCancelDate=cancelDate
		
		def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
		
		nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
		if (nights >1)
			dur= checkInDt+", "+nights+" nights"
		else dur = checkInDt+", "+nights+" night"
		println("$dur")
		resultData.hotel.removeItemPage.expectedDurationTxt=dur
		sleep(5000)
		//at ItenaryBuilderPage

		at HotelSearchResultsPage
		if(getItineraryBarSectionDisplayStatus()==true) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}
		at ItenaryBuilderPage
		//hideItenaryBuilder()
		sleep(3000)
		resultData.hotel.removeItemPage.expectedPrice=getItenaryPrice(data.expected.itinCardIndex.toString().toInteger())
		
		//Remove Itinerary
		clickOnRemoveItenary(data.expected.itinCardIndex.toInteger())
		waitTillLoadingIconDisappears()
		sleep(7000)
		at HotelSearchResultsPage
		
		//Capture Remove ItineraryConfirmation Screen Display Status
		boolean displayflag=getRemoveItemConfirmationScreenDisplayStatus()
		resultData.hotel.removeItemPage.actualRemoveItemScreenDispStatus=displayflag
		println("$resultData.hotel.removeItemPage.actualRemoveItemScreenDispStatus")
		
		//Capture Close X icon display status in Remove ItineraryConfirmation Screen
		boolean closeIconDisp=getCloseIconDisplayStatus()
		resultData.hotel.removeItemPage.actualCloseIconDispStatus=closeIconDisp
		println("$resultData.hotel.removeItemPage.actualCloseIconDispStatus")
		//Capture Header 'Are You Sure' text in Remove ItineraryConfirmation Screen
		resultData.hotel.removeItemPage.actualConfirmText=getConfirmTextInRemoveItemConfScreen()
		println("$resultData.hotel.removeItemPage.actualConfirmText")
		//Capture Hotel Image display status in Remove ItineraryConfirmation Screen
		resultData.hotel.removeItemPage.actualHotelImageIconStatus=getRemoveItemConfScreenImageIconLinkExistence()
		println("$resultData.hotel.removeItemPage.actualHotelImageIconStatus")
		//Capture Hotel Image Link in Remove ItineraryConfirmation Screen
		resultData.hotel.removeItemPage.actualHotelImageURL=getImageSrcURLRemoveItemConfScreen()
		println("$resultData.hotel.removeItemPage.actualHotelImageURL")
		
		//Capture Hotel name in Remove ItineraryConfirmation Screen
		resultData.hotel.removeItemPage.actualHotelName=getHotelNameInRemoveItemConfScreen()
		println("$resultData.hotel.removeItemPage.actualHotelName")
		
		//Capture Hotel Star Rating in Remove Itineraryconfirmation Screen
		resultData.hotel.removeItemPage.actualHotelStarRating=getRatingForRemoveItemConfScreen()
		
		//Capture Itinerary Desc
		String itineraryDesc=getItinenaryDescreptionInRemoveItemConfScrn()
		List<String> tempItineraryDescList=itineraryDesc.tokenize(".")

		List<String> roomDescPaxText=tempItineraryDescList.getAt(0).tokenize(",")
		String roomDescText=roomDescPaxText.getAt(0)
		resultData.hotel.removeItemPage.actualroomdescTxt=roomDescText.trim()
		println("$resultData.hotel.removeItemPage.actualroomdescTxt")
		String ratePlanDesc=tempItineraryDescList.getAt(1)
		resultData.hotel.removeItemPage.actualratePlanDescTxt=ratePlanDesc.trim()
		println("$resultData.hotel.removeItemPage.actualratePlanDescTxt")
		String freeCanclTxt=tempItineraryDescList.getAt(2)
		resultData.hotel.removeItemPage.actualfreeCanclTxt=freeCanclTxt.trim()
		println("$resultData.hotel.removeItemPage.actualratePlanDescTxt")
		
		String expectedFreeCanclTxt=data.expected.freecancltxt+" "+resultData.hotel.removeItemPage.expectedCancelDate.toUpperCase()
		resultData.hotel.removeItemPage.expectedFreeCanclTxt=expectedFreeCanclTxt
		println("$resultData.hotel.removeItemPage.expectedFreeCanclTxt")
		//Capture Itinerary Inventory status
		resultData.hotel.removeItemPage.actualStatus=getAvailabilityInRemoveItemConfScrn()
		//Capture check in date and nights
		resultData.hotel.removeItemPage.actualDuration=getItenaryDurationInRemoveItemConfScrn()
		//Capture room rate and currency
		//resultData.hotel.removeItemPage.actualPriceAndCurrency=getItenaryPriceInRemoveItemConfScrn()
		resultData.hotel.removeItemPage.actualPriceAndCurrency=getItenaryPriceInRemoveItemConfScrn(0)
		//Capture No Button displayed or not
		resultData.hotel.removeItemPage.actualNoButtonDisplayStatus=getNoButtonDisplayStatusInRemoveItemConfScrn()
		//Capture Yes Button displayed or not
		resultData.hotel.removeItemPage.actualYesButtonDisplayStatus=getYesButtonDisplayStatusInRemoveItemConfScrn()
		
		at ItenaryBuilderPage
	   //Close Click on X to close the screen
		clickCloseItenaryButton()
		sleep(5000)
		
		at HotelSearchResultsPage
		
		resultData.hotel.removeItemPage.actualitineraryBuilderSectionStatus=getItineraryBuilderDisplayStatus()
		println("$resultData.hotel.removeItemPage.actualitineraryBuilderSectionStatus")
		
		//click on X
		at ItenaryBuilderPage
		
		//Remove Itinerary
		clickOnRemoveItenary(data.expected.itinCardIndex.toInteger())
		sleep(5000)
		at HotelSearchResultsPage
		
		//Capture Remove ItineraryConfirmation Screen Display Status
		resultData.hotel.removeItemPage.actualRemoveItemScreenStatus=getRemoveItemConfirmationScreenDisplayStatus()
		println("$resultData.hotel.removeItemPage.actualRemoveItemScreenStatus")
		
		at ItenaryBuilderPage
		//Click No Button
		clickNoOnRemoveItenary()
		sleep(5000)
		at HotelSearchResultsPage		
		//capture Itinerary Builder screen display status
		resultData.hotel.removeItemPage.actualitineraryBuilderSecStatus=getItineraryBuilderDisplayStatus()
		println("$resultData.hotel.removeItemPage.actualitineraryBuilderSecStatus")
		
		at ItenaryBuilderPage
		//Remove Itinerary
		clickOnRemoveItenary(data.expected.itinCardIndex.toInteger())
		sleep(5000)
		at HotelSearchResultsPage
		
		//Capture Remove ItineraryConfirmation Screen Display Status
		resultData.hotel.removeItemPage.actualRemoveItemScrnDispStatus=getRemoveItemConfirmationScreenDisplayStatus()
		println("$resultData.hotel.removeItemPage.actualRemoveItemScrnDispStatus")
		
		//click Yes button
		at ItenaryBuilderPage
		clickOnConfirmRemoveItenary()
		sleep(5000)
		at HotelSearchResultsPage
		//capture Itinerary Builder screen display status
		resultData.hotel.removeItemPage.actualitineraryBuildrSecStatus=getItineraryBuilderDisplayStatus()
		println("$resultData.hotel.removeItemPage.actualitineraryBuildrSecStatus")
		
		
		at ItenaryBuilderPage
		//collapse
		//hideItenaryBuilder()
		sleep(5000)
		at HotelSearchResultsPage
		//capture collapsed status
		resultData.hotel.removeItemPage.actualItineraryBuilderSectionHiddenStatus=getItineraryBarSectionDisplayStatus()
		//println("$resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionHiddenStatus")
					
		at ItenaryBuilderPage
		//Expand
		hideItenaryBuilder()
		sleep(3000)
			
		at HotelSearchResultsPage
		//capture expanded status
		resultData.hotel.removeItemPage.actualItineraryBuilderSectionOpenStatus=getItineraryBarSectionDisplayStatus()
		//println("$resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionOpenStatus")
		
		//Capture Items
		resultData.hotel.removeItemPage.actualItems=getItemsCountItineraryBar()
		
		
		//Capture existence of Go To Itinerary Icon & Link
		//resultData.hotel.removeItemPage.actualIconLinkExistence=getItinerarBarSectionGoToItineraryIconLinkExistence()
		//Capture Go To Itinerary Text
		resultData.hotel.removeItemPage.actualGoToItinerarytxt=getGoToItineraryTextItineraryBar()
		//Capture existence of Close button
		resultData.hotel.removeItemPage.actualCloseBtnExistence=getItinerarBarSectionCloseIconLinkExistence()
		//Capture Close button text
		//resultData.hotel.removeItemPage.actualCloseBtnTxt=getCloseBtnTextItineraryBar()
		
		at ItenaryBuilderPage
		
		//capture new Itinerary reference number
		String itineraryBuilderTitle = getItenaryBuilderTtile()
		println("$itineraryBuilderTitle")
		List<String> tempList=itineraryBuilderTitle.tokenize(" ")
		//println("$tempList")
		String itineraryId=tempList.getAt(0)+tempList.getAt(1)
		resultData.hotel.removeItemPage.retitineraryId=itineraryId
		//println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryId")
		String itineraryName=tempList.getAt(2)
		resultData.hotel.removeItemPage.retitineraryName=itineraryName
		//println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryName")
		
	}
	
	private def addToItineraryCarousel(HotelSearchData data, HotelTransferTestResultData resultData)
	{


		at HotelSearchResultsPage

		/*for(int i=2;i<=data.input.itemsToBeAddedToItnrBuldr.toString().toInteger();i++)
		{
			scrollToSpecificAddToItineraryBtnAndClick(i)
			sleep(5000)
		}*/
		if(getItineraryBarSectionDisplayStatus()==true) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}

		//capture Left button display status in carousel section
		resultData.hotel.carouselVerify.actualCarouselLeftBtnDispStatus=getCarouselLeftButtonDisplayStatusItnrBuldr()
		
		//click left arrow
		clickCarouselLeftButtonItnrBuldr()
		sleep(3000)
		
		//capture the count - instead of shift positions
		resultData.hotel.carouselVerify.actualCountOfItems=getListCountInCourselSection()
		
		//click till the first item is shown
		clickLeftCarouselBtnTillFirstItemShown()
		
		//capture left button display status
		resultData.hotel.carouselVerify.actualCarouselLeftBtnStatus=getCarouselLeftButtonDisplayStatusItnrBuldr()
		
		//Click right arrow
		clickCarouselRightButtonItnrBuldr()
		
		//capture the count - instead of shift positions
		resultData.hotel.carouselVerify.actualCountItems=getListCountInCourselSection()
		
		//click till the last item is shown
		clickRightCarouselBtnTillLastItemShown()
		
		//capture right button display status
		resultData.hotel.carouselVerify.actualCarouselRightBtnStatus=getCarouselRightButtonDisplayStatusItnrBuldr()
		
		//Delete the last item
		at ItenaryBuilderPage
		
		clickOnRemoveItenary(data.expected.itinCardIndex.toInteger())
		sleep(5000)
		clickOnConfirmRemoveItenary()
		sleep(5000)
		
		at HotelSearchResultsPage
		//Capture the count
		resultData.hotel.carouselVerify.actualCountItemsAftrDel=getListCountInCourselSection()
		println("After Delete Item Count is: $resultData.hotel.carouselVerify.actualCountItemsAftrDel")
		
		/*
		//Delete All the items - Application Issue is there to click on last item remove icon
		for(int i=;i>=0;i--)
		{
			clickOnRemoveItenary(i)
			sleep(4000)
			clickOnConfirmRemoveItenary()
			sleep(5000)
		}*/
		
		//Delete All the items - work around code to delete all items bcz of applicaiton issue
		
		//click till the first item is shown
		//clickLeftCarouselBtnTillFirstItemShown()
		
		
		for(int j=resultData.hotel.carouselVerify.actualCountItemsAftrDel.toInteger();j>0;j--)
		{	
			at HotelSearchResultsPage
			int tempcount=getListCountInCourselSection().toInteger()
			println("Iteration:$j Count: $tempcount")

			if(getItineraryBarSectionDisplayStatus()==false) {
				at ItenaryBuilderPage
				//Expand
				hideItenaryBuilder()
				sleep(3000)
			}
			at HotelSearchResultsPage
			resultData.hotel.carouselVerify.actualRemoveIconStatus=getRemoveItinenaryIconDispStatus(j)
			println("Index: $j, Icon Status: $resultData.hotel.carouselVerify.actualRemoveIconStatus")
			
			resultData.hotel.carouselVerify.put("index"+j, resultData.hotel.carouselVerify.actualRemoveIconStatus)
			if(tempcount<=4)
			{
				at HotelSearchResultsPage
				if(getItineraryBarSectionDisplayStatus()==true) {
					at ItenaryBuilderPage
					//Expand
					hideItenaryBuilder()
					sleep(3000)
				}

			}

			if(tempcount>4)
			{	at HotelSearchResultsPage
				if(getItineraryBarSectionDisplayStatus()==true) {
				at ItenaryBuilderPage
				//Expand
				hideItenaryBuilder()
				sleep(3000)
				}
				at HotelSearchResultsPage
				clickLeftCarouselBtnTillFirstItemShown()
			}

			at ItenaryBuilderPage
			//println("For Iteration: $j")
			clickOnRemoveItenary(0)
			waitTillLoadingIconDisappears()
			sleep(4000)
			clickOnConfirmRemoveItenary()
			sleep(5000)
			waitTillLoadingIconDisappears()

			
		}
		at HotelSearchResultsPage
		try{
		//Get count after deleting all the items
		resultData.hotel.carouselVerify.actualCountItemsAftrAllDel=getCountOfItemsInItineraryBuilder()
		
		//Capture the text
		resultData.hotel.carouselVerify.actualDummyCardText=getDummyCardTextInItnrBuldr()
		}catch(Exception e){
			//Get count after deleting all the items
			resultData.hotel.carouselVerify.actualCountItemsAftrAllDel="Unable To Read From UI"

			//Capture the text
			resultData.hotel.carouselVerify.actualDummyCardText="Unable To Read From UI"
		}
		at ItenaryPageItems
		scrollToTopOfThePage()
		sleep(2000)
		at HotelSearchPage
		//Click on Hotels Tab
		//clickHotelsTab()
		//Enter Hotel Destination
		enterHotelDestination(data.input.cityAreaHotelTypeText)
		sleep(3000)
		selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)
		sleep(3000)
		//Enter Check In and Check Out Dates
		entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
		sleep(3000)
		
		clickPaxRoom()
		sleep(3000)
		//Enter Pax
		clickPaxRoom()
		selectNumberOfAdults(data.input.pax.toString(), 0)
		
	   	//click on Find button
		clickFindButton()
		sleep(7000)
		at HotelSearchResultsPage
		
		waitTillAddToItineraryBtnLoads()
		
		scrollToAddToItineraryBtn()
		
		clickAddToitinerary(0)
		sleep(4000)

		at ItineraryTravllerDetailsPage
		selectOptionFromManageItinerary(data.input.manageItinryValue)
		sleep(3000)

		at HotelSearchResultsPage

		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}
		at HotelSearchResultsPage
		//Capture display status for dummy card
		resultData.hotel.carouselVerify.actualDummyCardDispStatus=isDummyCardDisplayed()
		
		//Capture display status for the hotel added
		//capture hotel name in itinerary builder section
		resultData.hotel.carouselVerify.actualHotelName=getHotelNameInTitleCard(data.expected.itnCrdInx.toString().toInteger())
		
		at ItenaryBuilderPage
		//Click on Go to Itinerary Link
		clickOnGotoItenaryButton()
		sleep(2000)
		
		at ItineraryTravllerDetailsPage
		selectOptionFromManageItinerary(data.input.manageItinryValue)
		sleep(3000)

		at HotelSearchResultsPage
		resultData.hotel.carouselVerify.actualitineraryBuilderSectionStatus=getItineraryBuilderSectionDisplayStatus()
		
		//at ItenaryBuilderPage
		
		//closeItenaryBuilder()
		
		//at HotelSearchPage
		
	}
	
		
	@Unroll
	def "02 : Validate Search Results  #resultDat.hotelName"(){
		
		HotelTransferTestResultData resultData = resultDat
		HotelSearchData hotelData=resultData.hotelData
		//HotelTransferTestResultData resultData = resultMap.get(hotelData.hotelName)
		
		when: "Expected and actual Search Results are available"
		
		then: "Assert the Search Results"
		
		//Validate Itinerary builder section display status
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualitineraryBuilderSectionStatus,hotelData.expected.itinerarybulderSectionStatus,"Hotel Search Results Screen Itinerary Builder Section display status Actual & Expected don't match")
		//Validate Itinerary builder items added count
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,hotelData.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")
		
		//Validate Close Icon functionality - collapse
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionHiddenStatus,hotelData.expected.itinerarybuilderSectionHidden,"Close Icon (Collapse) in Itinerary Builder Section doesn't function as expected")
		//Validate Close Icon functionality - expand
		//assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryBuilderSectionOpenStatus,hotelData.expected.itinerarybulderSectionStatus,"Close Icon (Expand) in Itinerary Builder Section doesn't function as expected")
		//Validate Reference number
		softAssert.assertNotNull(resultData.hotel.searchResults.itineraryBuilder.retitineraryId, "Hotel Search Results Screen Itinerary Builder Section, Itinerary ID/Reference Number is null")
		//Validate Itinerary Name
		softAssert.assertNotNull(resultData.hotel.searchResults.itineraryBuilder.retitineraryName, "Hotel Search Results Screen Itinerary Builder Section, Itinerary Name is null")
		//Validate Items
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItems,hotelData.expected.ItemsTextAndCount,"Hotel Search Results Screen Itinerary Builder Section, Items Count Actual & Expected don't match")
		//Validate Go To Itinerary Icon Existence
		//assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualIconLinkExistence,hotelData.expected.goItineraryIcon,"Hotel Search Results Screen Itinerary Builder Section, Go To Itinerary Icon does not exist")
		//Validate Go To Itinerary Text
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualGoToItinerarytxt,hotelData.expected.gotItineraryText,"Hotel Search Results Screen Itinerary Builder Section, Go To Itinerary Text Actual & Expected don't match")
		//Validate Close Button Existence
		//assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCloseBtnExistence,hotelData.expected.goItineraryIcon,"Hotel Search Results Screen Itinerary Builder Section, Close Button does not exist")
		//Validate Close Button text
		//assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCloseBtnTxt,hotelData.expected.closeBtnText,"Hotel Search Results Screen Itinerary Builder Section, Close Text Actual & Expected don't match")
		//Validate Image Link Existence
		//assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualImgLnkExistence,hotelData.expected.goItineraryIcon,"Hotel Search Results Screen Itinerary Builder Section, Image Link does not exist")
		//Validate Hotel name
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualHotelName,hotelData.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section, Hotel Name Actual & Expected don't match")
		
		//Validate Hotel Rating
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualRating,hotelData.expected.starRating,"Hotel Search Results Screen Itinerary Builder Section, Hotel Rating Actual & Expected don't match")
		//Validate Room Desc
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualroomdescTxt,hotelData.expected.roomDesc,"Hotel Search Results Screen Itinerary Builder Section, Hotel Room Desc Actual & Expected don't match")
		//Validate Rate plan - meal basis
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualratePlanDescTxt,hotelData.expected.ratePlanDesc,"Hotel Search Results Screen Itinerary Builder Section, Hotel Rate Plan Desc Actual & Expected don't match")
		//Validate Free Cancel Text with Date		
		//assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualfreeCanclTxt,resultData.hotel.searchResults.itineraryBuilder.expectedFreeCanclTxt,"Hotel Search Results Screen Itinerary Builder Section, Hotel Free Cancel Text with Date Actual & Expected don't match")
		//Validate Inventory Availability
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualInvAvailStatus,hotelData.expected.invAvailability,"Hotel Search Results Screen Itinerary Builder Section, Hotel Inventory Availability Actual & Expected don't match")
		//Validate Check in date & nights
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualDurationtext,resultData.hotel.searchResults.itineraryBuilder.expectedDurationTxt,"Hotel Search Results Screen Itinerary Builder Section, Hotel check in date & nights Actual & Expected don't match")
		//Validate Room Rate and currency
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualroomRateAmntCurrnTxt,resultData.hotel.searchResults.itineraryBuilder.expectedPrice,"Hotel Search Results Screen Itinerary Builder Section, Hotel Room Rate & Currency Actual & Expected don't match")
	
		
		
		where :
		resultDat << resultMap.values()
	}
	
		
 	def "03: Validate Remove Item "()
	 {
		HotelTransferTestResultData resultData=resultDat
		HotelSearchData hotelData=resultData.hotelData
	
		when: "Expected and actual Search Results are available"
		
		then: "Assert the Search Results"
		
		//Validate Remove Item confirmation screen display status
		assertionEquals(resultData.hotel.removeItemPage.actualRemoveItemScreenDispStatus,hotelData.expected.itinerarybulderSectionStatus,"Remove Item Confirmation Screen display status Actual & Expected don't match")
		//Validate Close X function display status
		//assertionEquals(resultData.hotel.removeItemPage.actualCloseIconDispStatus,hotelData.expected.itinerarybulderSectionStatus,"Remove Item Confirmation Screen, Close Icon display status Actual & Expected don't match")
		//Validate Are You Sure Text
		assertionEquals(resultData.hotel.removeItemPage.actualConfirmText,hotelData.expected.removeItemConfirmScrntxt,"Remove Item Confirmation Screen, Are You Sure Text displayed Actual & Expected don't match")
		//Validate Hotel Image Link
		softAssert.assertNotNull(resultData.hotel.removeItemPage.actualHotelImageURL, "Remove Item Confirmation Screen, Hotel Image Link is null")
		//Validate Hotel Image display status
		assertionEquals(resultData.hotel.removeItemPage.actualHotelImageIconStatus,hotelData.expected.itinerarybulderSectionStatus,"Remove Item Confirmation Screen, Hotel Image display status Actual & Expected don't match")
		//Validate Hotel Name
		assertionEquals(resultData.hotel.removeItemPage.actualHotelName,hotelData.expected.cityAreaHotelText,"Remove Item Confirmation Screen, Hotel Name display status Actual & Expected don't match")
		//Validate Hotel Star Rating
		assertionEquals(resultData.hotel.removeItemPage.actualHotelStarRating,hotelData.expected.starRating,"Remove Item Confirmation Screen, Hotel Star Rating Actual & Expected don't match")
		//Validate Room Desc
		assertionEquals(resultData.hotel.removeItemPage.actualroomdescTxt,hotelData.expected.roomDesc,"Remove Item Confirmation Screen, Hotel Room Desc Actual & Expected don't match")
		//Validate Rate plan - meal basis
		assertionEquals(resultData.hotel.removeItemPage.actualratePlanDescTxt,hotelData.expected.ratePlanDesc,"Remove Item Confirmation Screen, Hotel Rate Plan Desc Actual & Expected don't match")
		//Validate Free Cancel Text with Date		
		assertionEquals(resultData.hotel.removeItemPage.actualfreeCanclTxt,resultData.hotel.removeItemPage.expectedFreeCanclTxt,"Remove Item Confirmation Screen, Hotel Free Cancel Text with Date Actual & Expected don't match")
		//Validate Inventory Status
		assertionEquals(resultData.hotel.removeItemPage.actualStatus,hotelData.expected.invAvailability,"Remove Item Confirmation Screen, Inventory Status Actual & Expected don't match")
		
		//Validate checkin date and nights
		assertionEquals(resultData.hotel.removeItemPage.actualDuration,resultData.hotel.removeItemPage.expectedDurationTxt,"Remove Item Confirmation Screen, Check in Date and nights Actual & Expected don't match")
		//Validate Amount And Price
		assertionEquals(resultData.hotel.removeItemPage.actualPriceAndCurrency,resultData.hotel.removeItemPage.expectedPrice,"Remove Item Confirmation Screen, Room Rate And Currency Actual & Expected don't match")
		
		//Validate No Button Display status
		assertionEquals(resultData.hotel.removeItemPage.actualNoButtonDisplayStatus,hotelData.expected.itinerarybulderSectionStatus,"Remove Item Confirmation Screen, No Button Display status Actual & Expected don't match")
		//Validate Yes Button Display status
		assertionEquals(resultData.hotel.removeItemPage.actualYesButtonDisplayStatus,hotelData.expected.itinerarybulderSectionStatus,"Remove Item Confirmation Screen, Yes Button Display status Actual & Expected don't match")
		
		//Validate User taken back to Itinerary builder screen
		assertionEquals(resultData.hotel.removeItemPage.actualitineraryBuilderSectionStatus,hotelData.expected.itinerarybulderSectionStatus,"Hotel Search Results Screen Itinerary Builder Section display status Actual & Expected don't match")
		
		
		//Validate Remove Item Confirmation screen display status
		assertionEquals(resultData.hotel.removeItemPage.actualRemoveItemScreenStatus,hotelData.expected.itinerarybulderSectionStatus,"Remove Item Confirmation Screen display status Actual & Expected don't match")
		
		//Validate Itinerary builder screen
		assertionEquals(resultData.hotel.removeItemPage.actualitineraryBuilderSecStatus,hotelData.expected.itinerarybulderSectionStatus,"Hotel Search Results Screen Itinerary Builder Section display status Actual & Expected don't match")
		
		//Validate Remove Item confirmation screen
		assertionEquals(resultData.hotel.removeItemPage.actualRemoveItemScrnDispStatus,hotelData.expected.itinerarybulderSectionStatus,"Remove Item Confirmation Screen display status Actual & Expected don't match")
		
		//Validate Itinerary builder screen
		assertionEquals(resultData.hotel.removeItemPage.actualitineraryBuildrSecStatus,hotelData.expected.itinerarybulderSectionStatus,"Hotel Search Results Screen Itinerary Builder Section display status Actual & Expected don't match")
		
		//Validate Close Icon functionality - collapse
		assertionEquals(resultData.hotel.removeItemPage.actualItineraryBuilderSectionHiddenStatus,hotelData.expected.itinerarybuilderSectionHidden,"Close Icon (Collapse) in Itinerary Builder Section doesn't function as expected")
		//Validate Close Icon functionality - expand
		//assertionEquals(resultData.hotel.removeItemPage.actualItineraryBuilderSectionOpenStatus,hotelData.expected.itinerarybulderSectionStatus,"Close Icon (Expand) in Itinerary Builder Section doesn't function as expected")
		
		//Validate Reference number
		softAssert.assertNotNull(resultData.hotel.removeItemPage.retitineraryId, "Hotel Search Results Screen Itinerary Builder Section, Itinerary ID/Reference Number is null")
		//Validate Itinerary Name
		softAssert.assertNotNull(resultData.hotel.removeItemPage.retitineraryName, "Hotel Search Results Screen Itinerary Builder Section, Itinerary Name is null")
		//Validate Items
		assertionEquals(resultData.hotel.removeItemPage.actualItems,hotelData.expected.ItemsTextAndCount,"Hotel Search Results Screen Itinerary Builder Section, Items Count Actual & Expected don't match")
		//Validate Go To Itinerary Icon Existence
		//assertionEquals(resultData.hotel.removeItemPage.actualIconLinkExistence,hotelData.expected.goItineraryIcon,"Hotel Search Results Screen Itinerary Builder Section, Go To Itinerary Icon does not exist")
		//Validate Go To Itinerary Text
		assertionEquals(resultData.hotel.removeItemPage.actualGoToItinerarytxt,hotelData.expected.gotItineraryText,"Hotel Search Results Screen Itinerary Builder Section, Go To Itinerary Text Actual & Expected don't match")
		//Validate Close Button Existence
		//assertionEquals(resultData.hotel.removeItemPage.actualCloseBtnExistence,hotelData.expected.goItineraryIcon,"Hotel Search Results Screen Itinerary Builder Section, Close Button does not exist")
		//Validate Close Button text
		//assertionEquals(resultData.hotel.removeItemPage.actualCloseBtnTxt,hotelData.expected.closeBtnText,"Hotel Search Results Screen Itinerary Builder Section, Close Text Actual & Expected don't match")
		//
		
		
		where :
		resultDat << verifyMap.values()
		
		
	 }
	
	def "04: Verify Itinerary Carousel"()
	{
		HotelTransferTestResultData resultData=resultDat
		HotelSearchData hotelData=resultData.hotelData
	
		when: "Expected and actual Search Results are available"
		
		then: "Assert the Carousel"
		 
		//Validate Left button display status in Carousel Section
		assertionEquals(resultData.hotel.carouselVerify.actualCarouselLeftBtnDispStatus,hotelData.expected.CarouselButtonDispStatus,"Carousel Section, Left button display status in Carousel Section Actual & Expected don't match")
		//Validate item count
		assertionEquals(resultData.hotel.carouselVerify.actualCountOfItems.toInteger(),hotelData.expected.expectedItemCount.toInteger(),"Carousel Section, Item count Actual & Expected don't match")
		
		//Validate left button display status
		assertionEquals(resultData.hotel.carouselVerify.actualCarouselLeftBtnStatus,hotelData.expected.CarouselButonNoDispStatus,"Carousel Section, Left button display status Actual & Expected don't match")
		
		//Validate items count
		assertionEquals(resultData.hotel.carouselVerify.actualCountItems.toInteger(),hotelData.expected.expectedItemCount.toInteger(),"Carousel Section, Item count Actual & Expected don't match")
		//Validate right button display status
		assertionEquals(resultData.hotel.carouselVerify.actualCarouselRightBtnStatus,hotelData.expected.CarouselButonNoDispStatus,"Carousel Section, Right button display status Actual & Expected don't match")
		
		//Validate count after delete
		assertionEquals(resultData.hotel.carouselVerify.actualCountItemsAftrDel.toInteger(),hotelData.expected.expectedItemCountAfterDelete.toInteger(),"Carousel Section, Item count After Delete Actual & Expected don't match")
		
		//Validate count after deleting all the items
		//assertionEquals(resultData.hotel.carouselVerify.actualCountItemsAftrAllDel,hotelData.expected.expectedItemCountAfterAllDelete,"Carousel Section, Item count After All items Delete Actual & Expected don't match")
		
		for(int j=resultData.hotel.carouselVerify.actualCountItemsAftrDel.toInteger();j>0;j--)
		{
			boolean actualremoveIconDispStatus=resultData.hotel.carouselVerify.get("index"+j)
			assertionEquals(actualremoveIconDispStatus,hotelData.expected.CarouselButtonDispStatus,"Carousel Section, Remove Itinerary Icon is not displayed for itinerary card item $j ")
			
		}
		
		
		//Validate text "Add items to build your itinerary"
		//assertionEquals(resultData.hotel.carouselVerify.actualDummyCardText,hotelData.expected.expectedDummyCardText,"Carousel Section, Dummy Card Text  Actual & Expected don't match")
		
		//Validate dummy card display status
		assertionEquals(resultData.hotel.carouselVerify.actualDummyCardDispStatus,hotelData.expected.CarouselButonNoDispStatus,"Carousel Section, Dummy Card Displays status  Actual & Expected don't match")
		
		//Validate hotel name
		assertionEquals(resultData.hotel.carouselVerify.actualHotelName,hotelData.expected.cityAreaHotelText,"Carousel Section, Hotel Name Displayed Actual & Expected don't match")
		
		//Validate Itinerary builder section display
		assertionEquals(resultData.hotel.carouselVerify.actualitineraryBuilderSectionStatus,hotelData.expected.CarouselButtonDispStatus,"Itinerary Builder Section display status Actual & Expected don't match")
		
		where :
		resultDat << verifyCarouselMap.values()
	}
	 
	 		
	protected static loadData(List<String> paths) {
		List<HotelSearchData> dataList = paths.collect { path ->
			new HotelSearchData(path)
		}
		return dataList
	}
	
	protected static List<HotelSearchData> getHotelSearchData() {
		
		println(resultMap.values())
		return resultMap.values()
	}
}
