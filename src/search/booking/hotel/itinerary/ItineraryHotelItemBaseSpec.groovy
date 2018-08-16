package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage

import java.text.DecimalFormat

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.DateUtil

import java.text.NumberFormat

abstract class ItineraryHotelItemBaseSpec extends BaseSpec {

	DateUtil dateUtil = new DateUtil()
	def dur=""
	int nights=0


	protected def addToItinerary(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData) {


		at HotelSearchResultsPage
		//Enter Hotel Destination
		enterHotelDestination(data.input.cityAreaHotelTypeText)
		sleep(3000)
		selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)
		sleep(2000)

		//Enter Check In and Check Out Dates
		entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
		sleep(3000)

		clickPaxRoom()
		sleep(3000)

		if(data.input.noOfRooms.toString().toInteger()>1 && (data.input.bookItemMultiRoom)){
			clickPaxNumOfRooms(data.input.noOfRooms.toString())
			sleep(3000)
			selectNumberOfAdults(data.input.pax.toString(), 0)

			selectNumberOfChildren(data.input.children.size().toString() , 0)
			sleep(1000)
			enterChildrenAge(data.input.children.getAt(0).toString(),"0", 0)
			sleep(1000)
			enterChildrenAge(data.input.children.getAt(1).toString(),"0", 1)
			sleep(1000)

			/*selectNumberOfChildren(data.input.children.toString() , 1)
			 sleep(1000)
			 enterChildrenAge(data.input.firstChildAge,"1", 0)
			 sleep(1000)
			 enterChildrenAge(data.input.secondChildAge,"1", 1)
			 sleep(1000)*/

			//Enter Pax
			clickPaxRoom()
		}
		else{
			
			clickPaxNumOfRooms(data.input.noOfRooms.toString())
			sleep(3000)
			//Enter Pax
			clickPaxRoom()


			selectNumberOfAdults(data.input.pax.toString(), 0)

			if(data.input.children.size()==0){
				selectNumberOfChildren(data.input.children.size().toString() , 0)
				sleep(3000)
			}

			if(data.input.children.size()>0){

				selectNumberOfChildren(data.input.children.size().toString() , 0)
				sleep(1000)
				enterChildrenAge(data.input.children.getAt(0).toString(),"0", 0)
				sleep(1000)
				enterChildrenAge(data.input.children.getAt(1).toString(),"1", 1)
				sleep(1000)
			}

		}
		clickPaxRoom()
		//click on Find button
		clickFindButton()
		sleep(7000)

		at HotelSearchResultsPage

		waitTillAddToItineraryBtnLoads()
		if(data.input.noOfRooms.toString().toInteger()>1){
			scrollToSpecificItinryAndClickFreeCancellationLink(data.expected.cityAreaHotelText)
		}else{
			scrollToSpecificItineraryAndClickFreeCancellationLink(data.expected.cityAreaHotelText)
		}

		sleep(7000)

		resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt=getCancellationChargeRelatedText()

		clickCloseCloseLightbox()
		sleep(2000)
		if(data.input.noOfRooms.toString().toInteger()>1){
			scrollToSpecificItnryAndClickAddToItinryBtn(data.expected.cityAreaHotelText)
			sleep(5000)
		}
		else{
			scrollToSpecificItineraryAndClickAddToItineraryBtn(data.expected.cityAreaHotelText)
			sleep(5000)
		}

		//commenting below lines since itinerary builder is removed in 10.3
		/*

		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}
		at HotelSearchResultsPage
		//Capture items added to itinerary builder
		resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

		*/

		/*
		if(data.input.noOfRooms>1){


			//Capture First item is added to itinerary builder
			resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard=getHotelNameInTitleCard(0)

			//Capture Second item is added to itinerary builder
			resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard=getHotelNameInTitleCard(1)

			at ItenaryBuilderPage

			/*for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
			 {	Map priceMap = [:]
			 //Capture Price and currency
			 priceMap.expPriceAndCurrncy=getItenaryPrice(i-1)
			 println("Price Print->$priceMap.expPriceAndCurrncy")
			 resultData.hotel.searchResults.priceAndCurrency.put("room"+i, priceMap)
			 }*/
		/*
			(1..data.input.noOfRooms).each
			{
				Map priceAndCurrencyMap = [:]
				Map priceMap = [:]
				//Capture Price and currency
				priceAndCurrencyMap.expPriceAndCurrncy=getItenaryPrice(it-1)
				println("Price And Currency Print->$priceAndCurrencyMap.expPriceAndCurrncy")
				String tmppriceTxt=getItenaryPrice(it-1)
				List<String> tempPriceAndCurrency=tmppriceTxt.tokenize(" ")
				String onlypriceText=tempPriceAndCurrency.getAt(0)
				priceMap.price=onlypriceText
				println("Price Print->$priceMap.price")
				resultData.hotel.searchResults.priceAndCurrency.put("room"+it, priceAndCurrencyMap)
				resultData.hotel.searchResults.price.put("room"+it, priceMap)
			}
			//Capture price & currency for First item
			resultData.hotel.searchResults.expectedPrice_FirstCard=getItenaryPrice(0)
			//capture only price
			List<String> tempPriceAndCurrency=resultData.hotel.searchResults.expectedPrice_FirstCard.tokenize(" ")
			String priceText=tempPriceAndCurrency.getAt(0)
			resultData.hotel.searchResults.priceText_FirstCard=priceText

			//Capture price & currency for First item
			resultData.hotel.searchResults.expectedPrice_SecondCard=getItenaryPrice(1)
			//capture only price
			List<String> tmpPriceAndCurrency=resultData.hotel.searchResults.expectedPrice_SecondCard.tokenize(" ")
			String priceTxt=tmpPriceAndCurrency.getAt(0)
			resultData.hotel.searchResults.priceText_SecondCard=priceTxt



		}
		*/
			/* //commenting below lines since itinerary builder is removed in 10.3
		else{

			//Capture item is added to itinerary builder
			resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname=getHotelNameInTitleCard(0)
			at ItenaryBuilderPage

			//Capture price & currency
			resultData.hotel.searchResults.expectedPrice=getItenaryPrice(0)
			//capture only price
			List<String> tempPriceAndCurrency=resultData.hotel.searchResults.expectedPrice.tokenize(" ")
			String priceText=tempPriceAndCurrency.getAt(0)
			resultData.hotel.searchResults.priceText=priceText
		}
		*/
		//commenting below lines since itinerary builder is removed in 10.3
		/*
		//hideItenaryBuilder()
		sleep(2000)
		at HotelSearchResultsPage
		scrollToTopOfThePage()
		sleep(3000)
		//clickHotelsTab()
		//sleep(3000)

		if(getItineraryBarSectionDisplayStatus()==true) {
			at ItenaryBuilderPage
			//Collapse
			hideItenaryBuilder()
			sleep(3000)
		}

		*/
		at ItineraryTravllerDetailsPage
		selectOptionFromManageItinerary(data.input.manageItinryValue)
		sleep(7000)

		//commenting below lines since itinerary builder is removed in 10.3
		at HotelSearchResultsPage

		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}
		at HotelSearchResultsPage
		//Capture items added to itinerary builder
		resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()

		if(data.input.noOfRooms>1){

			//Capture First item is added to itinerary builder
			resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard=getHotelNameInTitleCard(0)

			//Capture Second item is added to itinerary builder
			resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard=getHotelNameInTitleCard(1)

			at ItenaryBuilderPage

			/*for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
			 {	Map priceMap = [:]
			 //Capture Price and currency
			 priceMap.expPriceAndCurrncy=getItenaryPrice(i-1)
			 println("Price Print->$priceMap.expPriceAndCurrncy")
			 resultData.hotel.searchResults.priceAndCurrency.put("room"+i, priceMap)
			 }*/

			(1..data.input.noOfRooms).each
					{
						Map priceAndCurrencyMap = [:]
						Map priceMap = [:]
						//Capture Price and currency
						priceAndCurrencyMap.expPriceAndCurrncy=getItenaryPrice(it-1)
						println("Price And Currency Print->$priceAndCurrencyMap.expPriceAndCurrncy")
						String tmppriceTxt=getItenaryPrice(it-1)
						List<String> tempPriceAndCurrency=tmppriceTxt.tokenize(" ")
						String onlypriceText=tempPriceAndCurrency.getAt(0)
						priceMap.price=onlypriceText
						println("Price Print->$priceMap.price")
						resultData.hotel.searchResults.priceAndCurrency.put("room"+it, priceAndCurrencyMap)
						resultData.hotel.searchResults.price.put("room"+it, priceMap)
					}
			//Capture price & currency for First item
			resultData.hotel.searchResults.expectedPrice_FirstCard=getItenaryPrice(0)
			//capture only price
			List<String> tempPriceAndCurrency=resultData.hotel.searchResults.expectedPrice_FirstCard.tokenize(" ")
			String priceText=tempPriceAndCurrency.getAt(0)
			resultData.hotel.searchResults.priceText_FirstCard=priceText

			//Capture price & currency for First item
			resultData.hotel.searchResults.expectedPrice_SecondCard=getItenaryPrice(1)
			//capture only price
			List<String> tmpPriceAndCurrency=resultData.hotel.searchResults.expectedPrice_SecondCard.tokenize(" ")
			String priceTxt=tmpPriceAndCurrency.getAt(0)
			resultData.hotel.searchResults.priceText_SecondCard=priceTxt



		}

    else{

        //Capture item is added to itinerary builder
        resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname=getHotelNameInTitleCard(0)
        at ItenaryBuilderPage

        //Capture price & currency
        resultData.hotel.searchResults.expectedPrice=getItenaryPrice(0)
        //capture only price
        List<String> tempPriceAndCurrency=resultData.hotel.searchResults.expectedPrice.tokenize(" ")
        String priceText=tempPriceAndCurrency.getAt(0)
        resultData.hotel.searchResults.priceText=priceText
    }
		at HotelSearchResultsPage
		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Collapse
			hideItenaryBuilder()
			sleep(3000)
		}
}


protected def addToItineraryNonRecom(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData) {

            at HotelSearchPage
            //Enter Hotel Destination
            enterHotelDestination(data.input.cityAreaHotelTypeText)
			sleep(3000)
            selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)

            //Enter Check In and Check Out Dates
            entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
            sleep(3000)
            clickPaxRoom()
            sleep(3000)

            if(data.input.noOfRooms.toString().toInteger()>1 && (data.input.bookItemMultiRoom)){
                clickPaxNumOfRooms(data.input.noOfRooms.toString())
                sleep(3000)
                selectNumberOfAdults(data.input.pax.toString(), 0)

                selectNumberOfChildren(data.input.children.size().toString() , 0)
                sleep(1000)
                enterChildrenAge(data.input.children.getAt(0).toString(),"0", 0)
                sleep(1000)
                enterChildrenAge(data.input.children.getAt(1).toString(),"0", 1)
                sleep(1000)

                /*selectNumberOfChildren(data.input.children.toString() , 1)
                 sleep(1000)
                 enterChildrenAge(data.input.firstChildAge,"1", 0)
                 sleep(1000)
                 enterChildrenAge(data.input.secondChildAge,"1", 1)
                 sleep(1000)*/
		
					//Enter Pax
					clickPaxRoom()
				}
				else{
					
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
						enterChildrenAge(data.input.children.getAt(0).toString(),"0", 0)
						sleep(1000)
						enterChildrenAge(data.input.children.getAt(1).toString(),"1", 1)
						sleep(1000)
					}
		
				}
				clickPaxRoom()
				//click on Find button
				clickFindButton()
				sleep(7000)
		
				at HotelSearchResultsPage
		
				waitTillAddToItineraryBtnLoads()
		
				//scrollToSpecificItineraryAndClickFreeCancellationLink(data.expected.cityAreaHotelText)
				scrollAndClickFreeCancellationLink(1)
				sleep(7000)
		
				resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt=getCancellationChargeRelatedText()
		
				clickCloseCloseLightbox()
				sleep(2000)
				
				scrollAndClickAddToItinryBtn(1)
				sleep(5000)
				//Adding new code since 10.3 flow changed
				at ItineraryTravllerDetailsPage
				clickOnBackButtonInItineraryScreen()
				sleep(3000)
				at HotelSearchResultsPage
				scrollAndClickAddToItinryBtn(3)
				sleep(5000)
				//Adding new code since 10.3 flow changed
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
				//Capture items added to itinerary builder
				resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()
		
				
		
				if(data.input.noOfRooms>1){
		
		
					//Capture First item is added to itinerary builder
					resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard=getHotelNameInTitleCard(0)
		
					//Capture Second item is added to itinerary builder
					resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard=getHotelNameInTitleCard(1)
		
					at ItenaryBuilderPage
		
					
		
					(1..data.input.noOfRooms).each
					{
						Map priceAndCurrencyMap = [:]
						Map priceMap = [:]
						//Capture Price and currency
						priceAndCurrencyMap.expPriceAndCurrncy=getItenaryPrice(it-1)
						println("Price And Currency Print->$priceAndCurrencyMap.expPriceAndCurrncy")
						String tmppriceTxt=getItenaryPrice(it-1)
						List<String> tempPriceAndCurrency=tmppriceTxt.tokenize(" ")
						String onlypriceText=tempPriceAndCurrency.getAt(0)
						priceMap.price=onlypriceText
						println("Price Print->$priceMap.price")
						resultData.hotel.searchResults.priceAndCurrency.put("room"+it, priceAndCurrencyMap)
						resultData.hotel.searchResults.price.put("room"+it, priceMap)
					}
					//Capture price & currency for First item
					resultData.hotel.searchResults.expectedPrice_FirstCard=getItenaryPrice(0)
					//capture only price
					List<String> tempPriceAndCurrency=resultData.hotel.searchResults.expectedPrice_FirstCard.tokenize(" ")
					String priceText=tempPriceAndCurrency.getAt(0)
					resultData.hotel.searchResults.priceText_FirstCard=priceText
		
					//Capture price & currency for First item
					resultData.hotel.searchResults.expectedPrice_SecondCard=getItenaryPrice(1)
					//capture only price
					List<String> tmpPriceAndCurrency=resultData.hotel.searchResults.expectedPrice_SecondCard.tokenize(" ")
					String priceTxt=tmpPriceAndCurrency.getAt(0)
					resultData.hotel.searchResults.priceText_SecondCard=priceTxt
		
		
		
				}
				else{
		
					//Capture item is added to itinerary builder
					resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname=getHotelNameInTitleCard(0)
					at ItenaryBuilderPage
		
					//Capture price & currency
					resultData.hotel.searchResults.expectedPrice=getItenaryPrice(0)
					//capture only price
					List<String> tempPriceAndCurrency=resultData.hotel.searchResults.expectedPrice.tokenize(" ")
					String priceText=tempPriceAndCurrency.getAt(0)
					resultData.hotel.searchResults.priceText=priceText
				}
		
				
				hideItenaryBuilder()
				sleep(2000)
				at HotelSearchPage
				clickHotelsTab()
				sleep(2000)
	}
	
	
	protected def itineraryBuilder(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData) {


		at ItenaryBuilderPage
		hideItenaryBuilder()

		if(data.input.addItems.toString().toBoolean())
		{
			String firstItemPrice=getItenaryPrice(0).replace(" GBP", "")
			println("Pirce is$firstItemPrice")
			String scndItemPrice=getItenaryPrice(1).replace(" GBP", "")
			String thirdItemPrice=getItenaryPrice(2).replace(" GBP", "")
			String fourthItemPrice=getItenaryPrice(3).replace(" GBP", "")

			float totalPrice=Float.parseFloat(firstItemPrice)+Float.parseFloat(scndItemPrice)+Float.parseFloat(thirdItemPrice)+Float.parseFloat(fourthItemPrice)
			DecimalFormat df = new DecimalFormat("####.00");
			println("Total Price ----->"+df.format(totalPrice))
			resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice=df.format(totalPrice)
			sleep(2000)
			float secondItemPrice=Float.parseFloat(scndItemPrice)
			resultData.hotel.searchResults.itineraryBuilder.secondItemPrice=df.format(secondItemPrice)

		}

		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			String firstItemPrice=getItenaryPrice(0).replace(" GBP", "")
			println("First Item Pirce is$firstItemPrice")
			String scndItemPrice=getItenaryPrice(1).replace(" GBP", "")
			println("Second Item Pirce is$scndItemPrice")

			float totalPrice=Float.parseFloat(firstItemPrice)+Float.parseFloat(scndItemPrice)
			DecimalFormat df = new DecimalFormat("####.00");
			println("Total Price ----->"+df.format(totalPrice))
			resultData.hotel.searchResults.itineraryBuilder.multiRoomTotalPrice=df.format(totalPrice)
			sleep(2000)
		}

		println("$resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice")
		String depDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
		//capture new Itinerary reference number
		String itineraryBuilderTitle = getItenaryBuilderTtile()
		//println("$itineraryBuilderTitle")
		List<String> tempList=itineraryBuilderTitle.tokenize(" ")
		//println("$tempList")
		String itineraryId=tempList.getAt(0)+tempList.getAt(1)
		resultData.hotel.searchResults.itineraryBuilder.retitineraryId=itineraryId
		println("Retrieved Itinerary ID: $resultData.hotel.searchResults.itineraryBuilder.retitineraryId")

		//click Go to Itinerary Link
		clickOnGotoItenaryButton()
		sleep(2000)

		at ItineraryTravllerDetailsPage

		if(data.input.noOfRooms.toString().toInteger()>1){
			//capture hotel name in suggested items for first item
			resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms_firstCard=getTransferNameInSuggestedItem(0)
			//capture hotel name in suggested items for second item
			resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms_secondCard=getTransferNameInSuggestedItem(1)
		}
		else{
			//capture hotel name in suggested items
			resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms=getTransferNameInSuggestedItem(0)
		}
		//Input Title
		selectTitle(data.expected.title_txt,0)
		//Input First Name
		enterFirstName(data.expected.firstName,0)
		//Input Last Name
		enterLastName(data.expected.lastName,0)
		//Input Email Address
		enterEmail(data.expected.emailAddr,0)
		//Input Area Code
		//commented since 10.3 field is removed
		//enterCountryCode(data.expected.countryCode,0)
		//sleep(1000)
		//Input Telephone number
		enterTelephoneNumber(data.expected.telephone_Num,0)
		sleep(1000)

		String scndTrvlrfirstName=data.expected.firstName+"Second"
		String scndTrvlrlastName=data.expected.lastName+"Second"
		//enterAdultTravellerDetails(data.expected.title_txt, scndTrvlrfirstName , scndTrvlrlastName,0)
		fillTravellerDetailsNotSaved(data.expected.title_txt, scndTrvlrfirstName , scndTrvlrlastName,1)
		sleep(3000)

		//Click on Save Button
		//commented for SugstdNonRcmndMultiRoom on 31stMay 2017 @2:47 PM
		/*//clickonSaveButton()
		//clickonSaveButton(0)
		//sleep(2000)
		//waitTillLoadingIconDisappears()*/

		//Add 2nd Traveller details and click on save
		//String scndTrvlrfirstName=data.expected.firstName+"2"
		//String scndTrvlrlastName=data.expected.lastName+"2"

		//enterAdultTravellerDetails(data.expected.title_txt, scndTrvlrfirstName , scndTrvlrlastName,1)
		//sleep(4000)


		//waitTillLoadingIconDisappears()

		//String thirdTrvlrfirstName=data.expected.childFirstName+"3"
		//String thirdTrvlrlastName=data.expected.childLastName+"3"
		String thirdTrvlrfirstName=data.expected.childFirstName+"Third"
		String thirdTrvlrlastName=data.expected.childLastName+"Third"
		String firstChildAge=data.expected.childAgeFirst

		//String fourthTrvlrfirstName=data.expected.childFirstName+"4"
		//String fourthTrvlrlastName=data.expected.childLastName+"4"
		String fourthTrvlrfirstName=data.expected.childFirstName+"Fourth"
		String fourthTrvlrlastName=data.expected.childLastName+"Fourth"
		String secondChildAge=data.expected.childAgeSecond
		
		if(!(data.input.bookItemMultiRoom))
		{
			if(data.input.children.size()>0){

				//Add 3rd Traveller Details - child traveller

				//enterChildTravellerDetails(data.expected.title_txt,thirdTrvlrfirstName,thirdTrvlrlastName,firstChildAge,0)
				fillTravellerDetailsNotSaved(data.expected.title_txt,thirdTrvlrfirstName,thirdTrvlrlastName,3)
				ClickRadioButtonAdultOrChild(2)
				sleep(2000)
				clickAndEnterAge(firstChildAge)

				sleep(4000)
				waitTillLoadingIconDisappears()
				//Capture 3rd traveller details

				//resultData.hotel.itineraryPage.expectedthirdTravellerName=data.expected.title_txt+" "+thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"
				resultData.hotel.itineraryPage.expectedthirdTravellerName=thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"
				println("$resultData.hotel.itineraryPage.expectedthirdTravellerName")
				resultData.hotel.itineraryPage.actualthirdTravellerName=getLeadTravellerName(4)

				//Add 4th Traveller Details - child traveller

				//enterChildTravellerDetails(data.expected.title_txt,fourthTrvlrfirstName,fourthTrvlrlastName,secondChildAge,0)
				fillTravellerDetailsNotSaved(data.expected.title_txt,fourthTrvlrfirstName,fourthTrvlrlastName,4)
				ClickRadioButtonAdultOrChild(2)
				sleep(2000)
				clickAndEnterAge(secondChildAge)
				sleep(2000)
				//Click on Save Button
				clickonSaveButton()
				sleep(3000)

				waitTillLoadingIconDisappears()
				sleep(4000)

				//Capture 4th traveller details

				//resultData.hotel.itineraryPage.expectedfourthTravellerName=data.expected.title_txt+" "+fourthTrvlrfirstName+" "+fourthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
				resultData.hotel.itineraryPage.expectedfourthTravellerName=fourthTrvlrfirstName+" "+fourthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
				println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
				resultData.hotel.itineraryPage.actualfourthTravellerName=getLeadTravellerName(5)
			}

		}
		if(data.input.noOfRooms>1)
		{

			 /*thirdTrvlrfirstName=data.expected.firstName+"3"
			 thirdTrvlrlastName=data.expected.lastName+"3"

			 fourthTrvlrfirstName=data.expected.firstName+"4"
			 fourthTrvlrlastName=data.expected.lastName+"4"*/

			thirdTrvlrfirstName=data.expected.firstName+"Third"
			thirdTrvlrlastName=data.expected.lastName+"Third"

			fourthTrvlrfirstName=data.expected.firstName+"Fourth"
			fourthTrvlrlastName=data.expected.lastName+"Fourth"

			//String fifthTrvlrfirstName=data.expected.childFirstName+"5"
			//String fifthTrvlrlastName=data.expected.childLastName+"5"
			String fifthTrvlrfirstName=data.expected.childFirstName+"Fifth"
			String fifthTrvlrlastName=data.expected.childLastName+"Fifth"
			firstChildAge=data.input.children.getAt(0)
			//String sixthTrvlrfirstName=data.expected.childFirstName+"6"
			//String sixthTrvlrlastName=data.expected.childLastName+"6"
			String sixthTrvlrfirstName=data.expected.childFirstName+"Sixth"
			String sixthTrvlrlastName=data.expected.childLastName+"Sixth"
			secondChildAge=data.input.children.getAt(1)
				//Add 3rd Traveller details and click on save
			fillTravellerDetailsNotSaved(data.expected.title_txt, thirdTrvlrfirstName , thirdTrvlrlastName,2)
			sleep(2000)
				/*enterAdultTravellerDetails(data.expected.title_txt, thirdTrvlrfirstName , thirdTrvlrlastName,0)
				sleep(4000)
				waitTillLoadingIconDisappears()*/
				//Add 4th Traveller details and click on save
			fillTravellerDetailsNotSaved(data.expected.title_txt, fourthTrvlrfirstName , fourthTrvlrlastName,3)
			sleep(2000)
				/*enterAdultTravellerDetails(data.expected.title_txt, fourthTrvlrfirstName , fourthTrvlrlastName,0)
				sleep(4000)
				waitTillLoadingIconDisappears()


				//Capture 3rd traveller details
				resultData.hotel.itineraryPage.expectedthirdTravellerName=data.expected.title_txt+" "+thirdTrvlrfirstName+" "+thirdTrvlrlastName
				resultData.hotel.itineraryPage.actualthirdTravellerName=getLeadTravellerName(4)

				//Capture 4th traveller details
				resultData.hotel.itineraryPage.expectedfourthTravellerName=data.expected.title_txt+" "+fourthTrvlrfirstName+" "+fourthTrvlrlastName
				println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
				resultData.hotel.itineraryPage.actualfourthTravellerName=getLeadTravellerName(5)
				*/

				if(data.input.children.size()>0)
				{
					//Add 5th Traveller Details - child traveller
					clickAddTravellersButton()
					sleep(5000)
 					ClickRadioButtonAdultOrChild(8)
					//fillTravellerDetailsNotSaved(data.expected.title_txt,fifthTrvlrfirstName,fifthTrvlrlastName,4)
					enterFirstName(fifthTrvlrfirstName, 4)
					sleep(1000)
					enterLastName(fifthTrvlrlastName, 4)
					sleep(2000)
					clickAndEnterAge(firstChildAge, 3)
					sleep(3000)

					/*enterChildTravellerDetails(data.expected.title_txt,fifthTrvlrfirstName,fifthTrvlrlastName,firstChildAge,0)
                    sleep(4000)
                    waitTillLoadingIconDisappears()*/
					//Add 6th Traveller Details - child traveller
					//fillTravellerDetailsNotSaved(data.expected.title_txt,sixthTrvlrfirstName,sixthTrvlrlastName,5)
					clickAddTravellersButton()
					sleep(5000)
					ClickRadioButtonAdultOrChild(10)
					sleep(2000)
					enterFirstName(sixthTrvlrfirstName, 5)
					sleep(1000)
					enterLastName(sixthTrvlrlastName, 5)
					sleep(1000)
					clickAndEnterAge(secondChildAge, 4)
					sleep(2000)

				}
			clickonSaveButton(0)
			sleep(3000)

			waitTillLoadingIconDisappears()
			sleep(4000)

			//Capture lead traveller details
			resultData.hotel.itineraryPage.expectedleadTravellerName=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
			resultData.hotel.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)

			//Capture lead traveller - telephone number
			resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum=data.expected.countryCode+""+data.expected.telephone_Num
			resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum=getLeadTravellerName(1)

			//Caputre lead traveller - email address
			resultData.hotel.itineraryPage.expectedleadTravellerEmail=data.expected.emailAddr
			resultData.hotel.itineraryPage.actualLeadTravellerEmail=getLeadTravellerName(2)

			//Capture 2nd traveller details
			resultData.hotel.itineraryPage.expectedscndTravellerName=data.expected.title_txt+" "+scndTrvlrfirstName+" "+scndTrvlrlastName
			resultData.hotel.itineraryPage.actualscndTravellerName=getLeadTravellerName(3)


			//Capture 3rd traveller details
			resultData.hotel.itineraryPage.expectedthirdTravellerName=data.expected.title_txt+" "+thirdTrvlrfirstName+" "+thirdTrvlrlastName
			//resultData.hotel.itineraryPage.expectedthirdTravellerName=thirdTrvlrfirstName+" "+thirdTrvlrlastName
			resultData.hotel.itineraryPage.actualthirdTravellerName=getLeadTravellerName(4)

			//Capture 4th traveller details
			resultData.hotel.itineraryPage.expectedfourthTravellerName=data.expected.title_txt+" "+fourthTrvlrfirstName+" "+fourthTrvlrlastName
			//resultData.hotel.itineraryPage.expectedfourthTravellerName=fourthTrvlrfirstName+" "+fourthTrvlrlastName
			println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
			resultData.hotel.itineraryPage.actualfourthTravellerName=getLeadTravellerName(5)

			//Capture 5th traveller details

			resultData.hotel.itineraryPage.expectedfifthTravellerName=fifthTrvlrfirstName+" "+fifthTrvlrlastName+" "+"("+firstChildAge+"yrs)"
			println("$resultData.hotel.itineraryPage.expectedfifthTravellerName")
			resultData.hotel.itineraryPage.actualfifthTravellerName=getLeadTravellerName(6)

			//Capture 6th traveller details
			resultData.hotel.itineraryPage.expectedsixthTravellerName=sixthTrvlrfirstName+" "+sixthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
			println("$resultData.hotel.itineraryPage.expectedsixthTravellerName")
			resultData.hotel.itineraryPage.actualsixthTravellerName=getLeadTravellerName(7)

			

		}
		if(data.input.noOfRooms==1)		{
			clickonSaveButton(0)
			sleep(3000)

			waitTillLoadingIconDisappears()
			sleep(4000)

			//Capture lead traveller details
			resultData.hotel.itineraryPage.expectedleadTravellerName=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
			resultData.hotel.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)

			//Capture lead traveller - telephone number
			resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum=data.expected.countryCode+""+data.expected.telephone_Num
			resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum=getLeadTravellerName(1)

			//Caputre lead traveller - email address
			resultData.hotel.itineraryPage.expectedleadTravellerEmail=data.expected.emailAddr
			resultData.hotel.itineraryPage.actualLeadTravellerEmail=getLeadTravellerName(2)

			//Capture 2nd traveller details
			resultData.hotel.itineraryPage.expectedscndTravellerName=data.expected.title_txt+" "+scndTrvlrfirstName+" "+scndTrvlrlastName
			resultData.hotel.itineraryPage.actualscndTravellerName=getLeadTravellerName(3)


		}

		//click on Edit
		scrollToTopOfThePage()
		clickEditIconNextToItineraryHeader()
		sleep(3000)
		waitTillLoadingIconDisappears()
		//Update Name Text
		String todaysDate= dateUtil.addDaysChangeDatetformat(0, "ddMMMyy")
		println("$todaysDate")
		resultData.hotel.itineraryPage.expectedItnrName=todaysDate
		enterItenaryName(todaysDate)

		//commented below method since page object changed in 10.3
		//clickSaveButton()
		clickOnSaveOrCancelBtnsEditItinry(0)
		sleep(4000)
		waitTillLoadingIconDisappears()
		//capture entered Itinerary Name
		//Capture Itinerary Page - header
		String edtditineraryPageTitle = getItenaryName()
		//println("$itineraryPageTitle")
		List<String> tList=edtditineraryPageTitle.tokenize(" ")
		String edtitinaryName=tList.getAt(2)
		resultData.hotel.itineraryPage.actualSavedItnrName=edtitinaryName.replaceAll("\nEdit", "")

		//Capture suggested item text
		resultData.hotel.itineraryPage.actualSuggestedItemHeaderTxt=getSuggestedItemsHeaderText()

		scrollToBottomOfThePage()
		//click on suggested items question mark icon
		clickonQstnMarkIcon()

		//capture the text
		resultData.hotel.itineraryPage.actualQstnMarkTxt=getQuestionMarkMouseHoverTxt()

		//Capture the total items in suggested items
		resultData.hotel.itineraryPage.actualCountOfItemsInSuggstedItms=getNoOfItemsInNonBookedItems()

		//capture the total (only if there is multiple items)
		if(data.input.addItems.toString().toBoolean())
		{
			resultData.hotel.itineraryPage.actualTotalLabelText=getTotalLabelTxtInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.actualallItemsTotalAmountAndCurrency=getTotalAmountAndCurrencyInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.expectedallItemsTotalAmountAndCurrency=resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice+" "+clientData.currency
		}

		//capture the total (only if there is multiple items)
		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			resultData.hotel.itineraryPage.actualTotalLabelText=getTotalLabelTxtInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.actualallItemsTotalAmountAndCurrency=getTotalAmountAndCurrencyInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.expectedallItemsTotalAmountAndCurrency=resultData.hotel.searchResults.itineraryBuilder.multiRoomTotalPrice+" "+clientData.currency
		}

		scrollToTopOfThePage()
		//<Book> function button
		resultData.hotel.itineraryPage.actualBookBtnDispStatusInSuggestedItems=getBookBtndisplayStatus()
		def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()

		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			//hotel image link - First Card
			resultData.hotel.itineraryPage.actualImageIconLinkExistence_first=getImageIconLinkExistenceInSuggestedItem(0)

			//hotel image link - Second Card
			resultData.hotel.itineraryPage.actualImageIconLinkExistence_second=getImageIconLinkExistenceInSuggestedItem(1)

			//capture hotel image link URL - first
			resultData.hotel.itineraryPage.actualImageURL_first=getImageSrcURLInSuggestedItem(0)
			//capture hotel image link URL - second
			resultData.hotel.itineraryPage.actualImageURL_first=getImageSrcURLInSuggestedItem(1)

			//hotel  link - first
			resultData.hotel.itineraryPage.actualHotelNameURL_first=getHotelNameSrcURLInSuggestedItem(0)
			println "Hotel Link First is:$resultData.hotel.itineraryPage.actualHotelNameURL_first"
			//hotel  link - second
			resultData.hotel.itineraryPage.actualHotelNameURL_second=getHotelNameSrcURLInSuggestedItem(2)
			println "Hotel Link second is:$resultData.hotel.itineraryPage.actualHotelNameURL_second"

			//hotel star rating
			resultData.hotel.itineraryPage.actualHotelStarRating_first=getRatingForTheHotelInSuggestedItem(0)
			//hotel star rating
			resultData.hotel.itineraryPage.actualHotelStarRating_second=getRatingForTheHotelInSuggestedItem(1)

			//Room - description - First
			String roomdescComplTxt_first=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItineraryDescList_first=roomdescComplTxt_first.tokenize(",")

			String roomDescText_first=tempItineraryDescList_first.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxt_first=roomDescText_first.trim()
			//Pax number requested
			String tmpTxt_first=tempItineraryDescList_first.getAt(1)
			List<String> tempDescList_first=tmpTxt_first.tokenize(".")

			String paxNum_first=tempDescList_first.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNum_first=paxNum_first.trim()

			//Rate plan - meal basis
			String ratePlantxt_first=tempDescList_first.getAt(1)
			resultData.hotel.itineraryPage.actualratePlan_first=ratePlantxt_first.trim()

			//Room - description - Second
			String roomdescComplTxt_second=getItinenaryDescreptionInSuggestedItem(1)
			List<String> tempItineraryDescList_second=roomdescComplTxt_second.tokenize(",")

			String roomDescText_second=tempItineraryDescList_second.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxt_second=roomDescText_second.trim()
			//Pax number requested
			String tmpTxt_second=tempItineraryDescList_second.getAt(1)
			List<String> tempDescList_second=tmpTxt_second.tokenize(".")

			String paxNum_second=tempDescList_second.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNum_second=paxNum_second.trim()

			//Rate plan - meal basis
			String ratePlantxt_second=tempDescList_second.getAt(1)
			resultData.hotel.itineraryPage.actualratePlan_second=ratePlantxt_second.trim()


			//Free cancellation until date
			String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
			println "Cancel Date is:$cancelDate"
			resultData.hotel.itineraryPage.expectedCancelDate=cancelDate
			String expectedFreeCanclTxt=data.expected.freecancltxt+" "+resultData.hotel.itineraryPage.expectedCancelDate.toUpperCase()
			resultData.hotel.itineraryPage.expectedFreeCanclTxt=expectedFreeCanclTxt

			resultData.hotel.itineraryPage.actualFreeCnclTxt_first=getItinenaryFreeCnclTxtInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualFreeCnclTxt_second=getItinenaryFreeCnclTxtInSuggestedItem(1)
			//inventory availability
			resultData.hotel.itineraryPage.actualInventoryStatus_first=getTransferStatusDisplayedSuggestedItem(0)
			resultData.hotel.itineraryPage.actualInventoryStatus_second=getTransferStatusDisplayedSuggestedItem(1)

			//commission and percentage
			String commissionPercentageTxt_first
			String commissionPercentageTxt_second
			try{
				commissionPercentageTxt_first=getCommissionPercentInNonBookedItems(0)
				commissionPercentageTxt_second=getCommissionPercentInNonBookedItems(1)
				println("Commission Percentage Value in Non booked items : $commissionPercentageTxt_first")
				println("Commission Percentage Value in Non booked items : $commissionPercentageTxt_second")
				sleep(2000)

			}catch(Exception e)
			{
				commissionPercentageTxt_first="Unable To Read From UI"
				commissionPercentageTxt_second="Unable To Read From UI"
			}
			resultData.hotel.itineraryPage.actualcomPercentTxt_first=commissionPercentageTxt_first.replace("\n", " ")
			resultData.hotel.itineraryPage.actualcomPercentTxt_second=commissionPercentageTxt_second.replace("\n", " ")
			//requested check in date and nights

			nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
			if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"
			println("$dur")
			resultData.hotel.itineraryPage.expectedDurationTxt=dur

			resultData.hotel.itineraryPage.actualDurationTxt_first=getItenaryDurationInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualDurationTxt_second=getItenaryDurationInSuggestedItem(1)
			//Room rate amound and currency
			//resultData.hotel.itineraryPage.actualPriceAndcurrency_first=getItenaryPriceInSuggestedItem(0)
			//resultData.hotel.itineraryPage.actualPriceAndcurrency_second=getItenaryPriceInSuggestedItem(1)
			resultData.hotel.itineraryPage.actualPriceAndcurrency_first=getNonBookedItemsItenaryPrice(0)
			resultData.hotel.itineraryPage.actualPriceAndcurrency_second=getNonBookedItemsItenaryPrice(1)
		}
		else{
			//hotel image link
			resultData.hotel.itineraryPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(0)

			//capture hotel image link URL
			resultData.hotel.itineraryPage.actualImageURL=getImageSrcURLInSuggestedItem(0)

			//hotel  link
			resultData.hotel.itineraryPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(0)

			//hotel star rating
			resultData.hotel.itineraryPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(0)

			//Room - description
			String roomdescComplTxt=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItineraryDescList=roomdescComplTxt.tokenize(",")

			String roomDescText=tempItineraryDescList.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxt=roomDescText.trim()
			//Pax number requested
			String tmpTxt=tempItineraryDescList.getAt(1)
			List<String> tempDescList=tmpTxt.tokenize(".")

			String paxNum=tempDescList.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlantxt=tempDescList.getAt(1)
			resultData.hotel.itineraryPage.actualratePlan=ratePlantxt.trim()
			//Free cancellation until date
			String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
			println "Cancel Date is:$cancelDate"
			resultData.hotel.itineraryPage.expectedCancelDate=cancelDate
			String expectedFreeCanclTxt=data.expected.freecancltxt+" "+resultData.hotel.itineraryPage.expectedCancelDate.toUpperCase()
			resultData.hotel.itineraryPage.expectedFreeCanclTxt=expectedFreeCanclTxt

			resultData.hotel.itineraryPage.actualFreeCnclTxt=getItinenaryFreeCnclTxtInSuggestedItem(0)
			//inventory availability
			resultData.hotel.itineraryPage.actualInventoryStatus=getTransferStatusDisplayedSuggestedItem(0)

			//commission and percentage
			String commissionPercentageTxt
			try{
				commissionPercentageTxt=verifySuggestedItemCommission(0)
				println("Commission Percentage Value in Non booked items : $commissionPercentageTxt")
				sleep(2000)

			}catch(Exception e)
			{
				commissionPercentageTxt="Unable To Read From UI"
			}
			resultData.hotel.itineraryPage.actualcomPercentTxt=commissionPercentageTxt.replace("\n", " ")

			//requested check in date and nights
			checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()

			nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
			if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"
			println("$dur")
			resultData.hotel.itineraryPage.expectedDurationTxt=dur

			resultData.hotel.itineraryPage.actualDurationTxt=getItenaryDurationInSuggestedItem(0)
			//Room rate amound and currency
			//resultData.hotel.itineraryPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualPriceAndcurrency=getSuggestedItemsItenaryPrice(0)
		}

		if(data.input.addItems.toString().toBoolean())
		{
			//Remove function button display status
			resultData.hotel.removeItemPage.actualRemoveIconDispStatus=getRemoveButtonDisplayStatus(1)
			RemoveItenaryButton(0)
			sleep(3000)

			//Remove item lightbox display status
			resultData.hotel.removeItemPage.actualremoveItemLightBoxDispStatus=getCancelItemDisplayStatus()

			//lightbox - Title text , Text - Remove item
			resultData.hotel.removeItemPage.actualremoveItemTxt=getCancellationHeader()

			//Close lightbox X function
			resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()

			//text - Are you sure you want to remove the following item form this itinerary?
			resultData.hotel.removeItemPage.actualremoveHeaderTxt=getTravellerCannotBeDeletedheaderText()

			//hotel image link
			resultData.hotel.removeItemPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(4)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.actualImageURL=getImageSrcURLInSuggestedItem(4)

			//hotel name
			resultData.hotel.removeItemPage.actualHotelNameTxt=getTransferNameInSuggestedItem(4)

			//hotel  link
			resultData.hotel.removeItemPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(8)

			//hotel star rating
			resultData.hotel.removeItemPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(4)

			//Room - description
			//String roomdescComplText=getItinenaryDescreptionInSuggestedItem(4)
			String roomdescComplText=getItineraryDescInRemoveItemScreen(0)
			List<String> tempItinryDescList=roomdescComplText.tokenize(",")

			String roomDscText=tempItinryDescList.getAt(0)
			resultData.hotel.removeItemPage.actualroomdescTxt=roomDscText.trim()
			//Pax number requested
			String paxTxt=tempItinryDescList.getAt(1)
			List<String> paxDescList=paxTxt.tokenize(".")

			String paxNum=paxDescList.getAt(0)
			resultData.hotel.removeItemPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlanMealBasistxt=paxDescList.getAt(1)
			resultData.hotel.removeItemPage.actualratePlan=ratePlanMealBasistxt.trim()

			//Free cancellation until date
			String completeTxt=getRoomDescPaxRatePlanFreeCnclTxtInCancelItemScrn()
			List<String> tempcompleteTxtList=completeTxt.tokenize(".")

			String freeCanclText=tempcompleteTxtList.getAt(2)
			println("Cancel Free Cancl Txt $freeCanclText")
			resultData.hotel.removeItemPage.actualFreeCnclTxt=freeCanclText.trim()

			//requested check in date and nights
			resultData.hotel.removeItemPage.actualDurationTxt=getItenaryDurationInSuggestedItem(4)

			//Inventory Availability
			resultData.hotel.removeItemPage.actualInvStatusDisplayed=getInvStatusInRemoveItemScrn()

			//Commission and percentage
			resultData.hotel.removeItemPage.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(4)

			//Room rate amount and currency
			//resultData.hotel.removeItemPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(4)
			resultData.hotel.removeItemPage.actualPriceAndcurrency=getCancelledItemAmount(0)+" GBP"
			//display function button No
			resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
			//display function button Yes
			resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

			//Click No
			clickNoOnRemoveItenary()
			sleep(3000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//select above confirmed (1st item) and click on remove button
			RemoveItenaryButton(0)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(6000)

			//capture Item display status after removal
			resultData.hotel.removeItemPage.actualItemDispStatusAfetRemovalInNonBookedItms=getItemDispStatusInNonBookedItems(data.expected.cityAreaHotelText)

			//total amount for all item within the list
			resultData.hotel.removeItemPage.actualTotalLabelText=getTotalLabelTxtInNonBookedItemsScrn()
			resultData.hotel.removeItemPage.actualallItemsTotalAmountAndCurrency=getTotalAmountAndCurrencyInNonBookedItemsScrn()

			float modifiedPrice=Float.parseFloat(resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice.toString())-Float.parseFloat(resultData.hotel.searchResults.priceText.toString())
			DecimalFormat df = new DecimalFormat("####.00");
			println("Total Price ----->"+df.format(modifiedPrice))

			resultData.hotel.removeItemPage.expectedallItemsTotalAmountAndCurrency=df.format(modifiedPrice)+" "+clientData.currency

			scrollToTopOfThePage()
		}


		//click on Book button
		clickOnBookIcon()
		sleep(5000)

		waitTillLoadingIconDisappears()
		//capture booking screen display status
		resultData.hotel.itineraryPage.actualBookingScreenDispStatus=getAboutToBookScreendisplayStatus()

		//capture title text
		resultData.hotel.itineraryPage.actualtitleTxt=getCancellationHeader()

		//Capture Close Icon display status
		resultData.hotel.itineraryPage.actualCloseIconDispStatus=getCloseIconDisplayStatus()

		if(data.input.noOfRooms>1 && (data.input.bookItemMultiRoom))
		{
			multiRoomBooking(clientData,data,resultData)
		}

		else if(data.input.noOfRooms>1 && (data.input.cancelMultiRoom)){
			
			multiRoomCancellation(clientData,data,resultData)
		}
	
		
		else{
			//Capture hotel name in About to Book Screen
			resultData.hotel.itineraryPage.actualHotelNameInAbtToBkScrn=getHotelNameInAboutToBookScrn()

			//Capture hotel desc
			//String tmphotelTxt=getTitleSectDescTxt(0)
			String tmphotelTxt=getItemDescPaxAndCityTxtInAboutToBookScreen()
			List<String> htlDesc=tmphotelTxt.tokenize(",")

			String hotelDescText=htlDesc.getAt(0)
			resultData.hotel.itineraryPage.actualHotelDescTxt=hotelDescText

			//resultData.hotel.itineraryPage.expectedHotelDescTxt="1x "+data.expected.roomDesc
			resultData.hotel.itineraryPage.expectedHotelDescText=data.expected.roomDesc
			//Capture PAX
			//String paxTxt=htlDesc.getAt(1)
			String paxTxt=getTitleSectDescTxt(3).replaceAll("\n","")
			resultData.hotel.itineraryPage.actualPaxTxt=paxTxt.trim()

			//capture meal basis
			//resultData.hotel.itineraryPage.actualMealBasisTxt=getTitleSectDescTxt(1)
			resultData.hotel.itineraryPage.actualMealBasisTxt=htlDesc.getAt(1)

			//check in date, night

			/*if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"
			resultData.hotel.itineraryPage.expectedChkInDateNight=dur*/
			//resultData.hotel.itineraryPage.actualcheckInNightTxt=getTitleSectDescTxt(2)
			resultData.hotel.itineraryPage.expectedChkInDateNight="Check in"+checkInDt
			resultData.hotel.itineraryPage.actualcheckInNightTxt=getTitleSectDescTxt(0).replaceAll("\n","")

			String actualNumOfNight=getTitleSectDescTxt(2).replaceAll("\n","")
			if (nights >1)
				dur= "Nights"+nights
			else dur = "Nights"+nights
			resultData.hotel.itineraryPage.actualNumOfNight=actualNumOfNight
			resultData.hotel.itineraryPage.expectedNumOfNight=dur

			//Room Rate and currency
			resultData.hotel.itineraryPage.actualPriceInAboutToBookScrn=getPriceInAbouttoBookScrn()

			//Book Page PAX Section

			//Title Text
			resultData.hotel.itineraryPage.actualPaxSecTitleTxt=getPaxSectionTitleTxt()

			//1st traveller
			resultData.hotel.itineraryPage.actualfirstTravellerTxt=getTravellerNameInAboutToBookScrn(0)
			resultData.hotel.itineraryPage.expectedfirstTravellerTxt=data.expected.firstName+" "+data.expected.lastName+" (Lead Name)"
			// tick box
			resultData.hotel.itineraryPage.actualfirstTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(1)

			//2nd traveller
			resultData.hotel.itineraryPage.actualsecondTravellerTxt=getTravellerNameInAboutToBookScrn(1)
			resultData.hotel.itineraryPage.expectedsecondTravellerTxt=scndTrvlrfirstName+" "+scndTrvlrlastName
			// tick box
			resultData.hotel.itineraryPage.actualsecondTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(2)

			if(data.input.children.size()>0){
				//3rd traveller
				resultData.hotel.itineraryPage.actualthirdTravellerTxt=getTravellerNameInAboutToBookScrn(2)
				resultData.hotel.itineraryPage.expectedthirdTravellerTxt=thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"
				// tick box - 3rd traveller
				resultData.hotel.itineraryPage.actualthirdTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(3)

				//4th traveller
				resultData.hotel.itineraryPage.actualfourthTravellerTxt=getTravellerNameInAboutToBookScrn(3)
				resultData.hotel.itineraryPage.expectedfourthTravellerTxt=fourthTrvlrfirstName+" "+fourthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
				// tick box - 4th traveller
				resultData.hotel.itineraryPage.actualfourthTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(4)
			}

			//Selecting travellers. Note: this step is not there in test case - but we cannot click on confirm booking without selecting travellers
			//Commenting since 10.3 by default travellers are selected.
			/*clickOnTravellerCheckBox(0)
			sleep(3000)
			clickOnTravellerCheckBox(1)
			sleep(3000)

			if(data.input.children.size()>0){
				clickOnTravellerCheckBox(2)
				sleep(3000)
				clickOnTravellerCheckBox(3)
				sleep(3000)
			}
			*/
			//Special condition

			//special condition - header text
			resultData.hotel.itineraryPage.actualSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToBookItems(0)
			resultData.hotel.itineraryPage.expectedSpecialConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDt

			//Description text
			resultData.hotel.itineraryPage.actualDescText=getTextInAboutToBookItems(0)

			//Cancel charge - header text
			//resultData.hotel.itineraryPage.actualSpecialcancelchrgHeaderTxt=getOverlayHeadersTextInAboutToBookItems(1)
			resultData.hotel.itineraryPage.actualSpecialcancelchrgHeaderTxt=getOverlayHeadersTextInAboutToBookItemScrn(0)


			if(data.input.ammendmentCharge.toString().toBoolean())
			{
				//Amendment Title text, Name change, early Departure text
				String ammendmentTitleTxt
				String nameChangeTxt
				String ammendmentTxt
				String earlyDepTxt
				String nameChangeDate
				String ammendmentsDate
				try{
					ammendmentTitleTxt=getOverlayHeadersTextInAboutToBookItems(2)
					println("Ammendment Title : $ammendmentTitleTxt")
					sleep(2000)
				}catch(Exception e)
				{
					ammendmentTitleTxt="Unable To Read From UI"
				}
				try{
					nameChangeTxt=getAmmendmentChargeTextAndDateTxt(2,1,1)
					sleep(2000)
					println("Actual NameChangeText$nameChangeTxt")
				}
				catch(Exception e)
				{
					nameChangeTxt="Unable To Read From UI"
				}
				try{
					ammendmentTxt=getAmmendmentChargeTextAndDateTxt(2,1,2)
					sleep(2000)
					println("Actual Ammendment Text$ammendmentTxt")
				}catch(Exception e)
				{
					ammendmentTxt="Unable To Read From UI"

				}
				try{earlyDepTxt=getAmmendmentChargeTextAndDateTxt(2,1,3)
					sleep(2000)
					println("ActualEarly Dep Text$earlyDepTxt")
				}catch(Exception e)
				{
					earlyDepTxt="Unable To Read From UI"
				}
				try{
					nameChangeDate=getAmmendmentChargeTextAndDateTxt(2,2,1)
					sleep(2000)
					println("Actual Name Change Date$nameChangeDate")
				}catch(Exception e)
				{
					nameChangeDate="Unable To Read From UI"
				}
				try{
					ammendmentsDate=getAmmendmentChargeTextAndDateTxt(2,2,2)
					sleep(2000)
					println("Actual ammendments Date $ammendmentsDate")

				}
				catch(Exception e)
				{
					ammendmentsDate="Unable To Read From UI"
				}
				resultData.hotel.itineraryPage.actualAmmendmentTitletxt=ammendmentTitleTxt
				resultData.hotel.itineraryPage.actualnameChangeTxt=nameChangeTxt
				resultData.hotel.itineraryPage.actualammendmentTxt=ammendmentTxt
				resultData.hotel.itineraryPage.actualearlyDepTxt=earlyDepTxt
				resultData.hotel.itineraryPage.actualnameChangeDate=nameChangeDate

				String actualNameAndAmmendmentChangeDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.ammendmentOrNameChangeDays.toInteger(), "ddMMMyy")

				resultData.hotel.itineraryPage.expectedNameAndAmmendmentChangeDate="After "+actualNameAndAmmendmentChangeDate.toUpperCase()
				resultData.hotel.itineraryPage.actualammendmentsDate=ammendmentsDate
			}


			resultData.hotel.itineraryPage.actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()

			/*
			 //Date First Row
			 resultData.hotel.itineraryPage.actualDateFirstRow=getDateAmountCurrency(0)
			 resultData.hotel.itineraryPage.ExpectedDateFirstRow=cancelDate.toUpperCase()+" or earlier"
			 //Date Second Row
			 resultData.hotel.itineraryPage.actualDateSecondRow=getDateAmountCurrency(1)
			 resultData.hotel.itineraryPage.ExpectedDateSecondRow=checkInDt+" onwards"
			 //Amount First Row
			 resultData.hotel.itineraryPage.actualAmountFirstRow=getDateAmountCurrency(2)
			 println("$resultData.hotel.itineraryPage.actualAmountFirstRow")
			 //Amount Second Row
			 resultData.hotel.itineraryPage.actualAmountSecondRow=getDateAmountCurrency(3)
			 println("$resultData.hotel.itineraryPage.actualAmountSecondRow")
			 float fpercentage = Float.parseFloat(data.expected.cancel_percentage)
			 float Amount=Float.parseFloat(resultData.hotel.searchResults.priceText.toString())
			 String percentValue=getCommissionPercentageValue(Amount,fpercentage)
			 resultData.hotel.itineraryPage.expectedPercentValue=percentValue+clientData.currency
			 */
			//Ammendment Charge
			resultData.hotel.itineraryPage.actualAmmendmentCharge=getTextInAboutToBookItems(1)

			//General Terms & Conditions Text
			resultData.hotel.itineraryPage.actualTermsAndConditionsText=getTextInAboutToBookItems(2)

			//Terms & Condtions Link
			resultData.hotel.itineraryPage.actualTAndCDisplayStatus=getTAndCLinkInAboutToBookScreendisplayStatus()

			//By Click on T & c
			//String ByclickonTAndC=getByClickingFooterTxt(0)
			String ByclickonTAndC=getByClickingFooterTxt()
			resultData.hotel.itineraryPage.actualTermsAndCondtTxt=ByclickonTAndC.replace("\n", "")
			//Add Comment or Remark Text
			resultData.hotel.itineraryPage.actualSpecialRemarkOrCommentTxt=getSpecialRemarkOrCommentTxt()
			scrollToRemarksTxt()

			//click on downside arrow
			clickExpandRemarksTandC()
			sleep(2000)
			//capture Expand status
			resultData.hotel.itineraryPage.actualExpandedStatus=getExpandOrCollapseItemDisplayStatus()
			//click on upside arrow
			clickExpandRemarksTandC()
			sleep(2000)
			//capture Collapsed status
			resultData.hotel.itineraryPage.actualCollapsedStatus=getExpandOrCollapseItemDisplayStatus()
			//click on downside arrow
			clickExpandRemarksTandC()
			sleep(4000)

			//title text, please note text
			resultData.hotel.itineraryPage.actualPleaseNoteTxt=getPleaseNotOrProvideTxt(0)

			//Will arrive without voucher check box display status
			resultData.hotel.itineraryPage.actualwilArrivWithoutVochrchkBoxDispStatus=getWilArrivWithOutVochrCheckBoxDisplayStatus()

			//Will arrive without voucher text
			resultData.hotel.itineraryPage.actualwilArrivWithoutVochrTxt=getCheckBoxTextInAddSpecialRemarkSection(0)

			//Previous night is booked for early morning arrival display status
			//resultData.hotel.itineraryPage.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus(0)

			//Previous night is booked for early morning arrival text
			resultData.hotel.itineraryPage.actualPreviousNightTxt=getCheckBoxTextInAddSpecialRemarkSection(1)

			//Late check out check box display status
			//resultData.hotel.itineraryPage.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus(0)

			//Late check out Text
			resultData.hotel.itineraryPage.actualLateCheckOutTxt=getCheckBoxTextInAddSpecialRemarkSection(2)

			//Late Arrival check box display status
			//resultData.hotel.itineraryPage.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus(0)

			//Late Arrival text box
			resultData.hotel.itineraryPage.actualLateArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(3)

			//Passengers Are HoneyMooners check box Display Status
			//resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus(0)

			//Passengers Are HoneyMooners Text
			resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersTxt=getCheckBoxTextInAddSpecialRemarkSection(4)

			//Early Arrival Disp Status
			//resultData.hotel.itineraryPage.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus(0)

			//Early Arrival Text
			resultData.hotel.itineraryPage.actualEarlyArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(5)

			//tick the check box Will arrive without voucher
			clickWilArrivWithOutVochrCheckBox()
			sleep(2000)

			//titleText - if possible please provide
			resultData.hotel.itineraryPage.actualIfPsblePlzProvTxt=getPleaseNotOrProvideTxt(1)

			//Twin Room check box display status
			//resultData.hotel.itineraryPage.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus(0)

			//Twin Room Text Box
			resultData.hotel.itineraryPage.actualTwinRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(7)

			//Smoking Room Check Box display status
			//resultData.hotel.itineraryPage.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus(0)
			//Smoking Room Check Box Text
			resultData.hotel.itineraryPage.actualSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(8)

			//Inter Connecting Rooms check box display status
			//resultData.hotel.itineraryPage.actualInterConnectingRoomCheckboxDispStatus=getInterConnectingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualInterConnectingRoomCheckboxDispStatus=getICRoomCheckBoxDisplayStatus(0)
			//Inter connecting Rooms text
			resultData.hotel.itineraryPage.actualIntrConnectngRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(9)

			//Adjoining Rooms check box display status
			//resultData.hotel.itineraryPage.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus(0)
			//Adjoining Rooms check box text
			resultData.hotel.itineraryPage.actualAdjoiningRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(10)

			//Quiet Rooms check box display status
			//resultData.hotel.itineraryPage.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus(0)
			//Quiet Rooms check box text
			resultData.hotel.itineraryPage.actualQuietRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(11)

			//Non-Smoking Rooms check box display status
			//resultData.hotel.itineraryPage.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus(0)
			//Non-Smoking Rooms text
			resultData.hotel.itineraryPage.actualNonSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(12)

			//Room on lowest floor display status
			//resultData.hotel.itineraryPage.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus(0)
			//Room on lowest floor text
			resultData.hotel.itineraryPage.actualRoomOnLowestFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(13)

			//Room on high floor checkbox display status
			//resultData.hotel.itineraryPage.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus(0)
			//Room on high floor text
			resultData.hotel.itineraryPage.actualRoomOnHighFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(14)

			//Double Room check box display status
			//resultData.hotel.itineraryPage.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus(0)
			//Double Room text
			resultData.hotel.itineraryPage.actualDoubleRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(15)

			//Room with bathtub
			//resultData.hotel.itineraryPage.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus(0)
			//Room with bathtub
			resultData.hotel.itineraryPage.actualRoomwithBathTubtxt=getCheckBoxTextInAddSpecialRemarkSection(16)

			//Arrival Flight Number check box display status
			//resultData.hotel.itineraryPage.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus(0)
			//Arrival Flight Number text
			resultData.hotel.itineraryPage.actualArrivalFlightNumtxt=getCheckBoxTextInAddSpecialRemarkSection(6)

			//Arrival Flight Number text box existence
			//resultData.hotel.itineraryPage.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus(0)

			//at Hrs
			//at text
			resultData.hotel.itineraryPage.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(1)
			//Hrs text
			resultData.hotel.itineraryPage.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(2)
			//Hrs select drop box display status
			resultData.hotel.itineraryPage.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus()

			//mins
			//mins text
			resultData.hotel.itineraryPage.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(3)
			//mins select drop box display status
			resultData.hotel.itineraryPage.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus()
			//On Date
			//on text
			resultData.hotel.itineraryPage.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(4)
			//on Day select drop box display status
			resultData.hotel.itineraryPage.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus()
			//on Month select drop box display status
			resultData.hotel.itineraryPage.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus()

			//Book Page Footer

			//Total
			resultData.hotel.itineraryPage.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)
			//Total Amount and currency
			resultData.hotel.itineraryPage.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)

			//Your Commission text
			resultData.hotel.itineraryPage.actualCommissionTextInAboutToBook=getTotalAndCommissionText(2)
			//Your commission amount and currency
			//resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getCommissionValueAndCurrencyInAboutToBookScrn(1)
			resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getTotalAndCommissionText(3)
			float compercentage = Float.parseFloat(data.expected.commissionPercent)
			float comAmount=Float.parseFloat(resultData.hotel.searchResults.priceText.toString().replaceAll(",",""))
			String comValue=getCommissionPercentageValue(comAmount,compercentage)
			resultData.hotel.itineraryPage.expectedComValue=comValue+" "+clientData.currency

			//Click on Confirm Booking
			//clickConfirmBooking()
			clickOnConfirmBookingAndPayNow()

			//wait for confirmation page
			waitTillConformationPage()
			sleep(5000)
			
			waitForAjaxIconToDisappear()
			scrollToTopOfThePage()
			//Booking Confirmation Screen Display Status
			resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

			//Booking confirmed lightbox displayed

			//Title text-Booking Confirmed
			resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()

			//Header Section
			try{
				resultData.hotel.itineraryPage.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
			}catch(Exception e){
				resultData.hotel.itineraryPage.actualHeaderSectionText="Unable To Read Header Section Text in Booking conf screen UI"
			}

			String emailId=clientData.usernameOrEmail
			//emailId=emailId.toUpperCase()
			//resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
			resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt

			//read Booking id
			resultData.hotel.itineraryPage.retrievedbookingID=getBookingIdFromBookingConfirmed(0)
			println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")

			//read Itineary Reference Number & name
			resultData.hotel.itineraryPage.actualreaditinearyIDAndName=getItinearyID(0)
			resultData.hotel.itineraryPage.expecteditinearyIDAndName=resultData.hotel.searchResults.itineraryBuilder.retitineraryId+resultData.hotel.itineraryPage.expectedItnrName

			//Capture Departure Date

			resultData.hotel.confirmationPage.actualConfimrationDialogDepDate=getBookingDepartDate()
			resultData.hotel.confirmationPage.expectedConfimrationDialogDepDate=depDate.toUpperCase()

			//capture traveller details
			resultData.hotel.confirmationPage.actualfirstTravellerName=getTravellerNamesConfirmationDialog(0)
			resultData.hotel.confirmationPage.actualsecondTravellerName=getTravellerNamesConfirmationDialog(1)

			if(data.input.children.size()>0){
				resultData.hotel.confirmationPage.actualthirdTravellerName=getTravellerNamesConfirmationDialog(2)
				resultData.hotel.confirmationPage.actualfourthTravellerName=getTravellerNamesConfirmationDialog(3)
			}
			//Hotel Name
			resultData.hotel.confirmationPage.actualHotelName=getConfirmBookedTransferName()

			//Hotel Address
			resultData.hotel.confirmationPage.actualHotelAddressTxt=getTransferDescBookingConfirmed()

			//number of room/name of room (descripton)
			String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
			htlDesc=roomAndPax.tokenize(",")

			hotelDescText=htlDesc.getAt(0)
			resultData.hotel.confirmationPage.actualHotelDescTxt=hotelDescText

			//Capture PAX
			paxTxt=htlDesc.getAt(1)
			resultData.hotel.confirmationPage.actualPaxTxt=paxTxt.trim()

			//Meal Basis
			resultData.hotel.confirmationPage.actualMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

			//Check in date, Number of Nights
			resultData.hotel.confirmationPage.actualCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed()

			if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"

			resultData.hotel.confirmationPage.expectedCheckInDateNumOfNights=dur
			//Please Note txt
			resultData.hotel.confirmationPage.actualPlzNoteTxtInBookingConfScrn=getPleaseNoteTxtInBookingConfirmedScrn()

			//Remarks
			resultData.hotel.confirmationPage.expectedRemarksTxtInBookingConfScrn=getRemarksTxtInBookingConfirmedScrn(2)
			//Room Rate Amount and currency
			resultData.hotel.confirmationPage.actualRoomRateAmountAndCurrency=getPriceBookingConfirmation()
			resultData.hotel.confirmationPage.expectedRoomRateAmountCurency=resultData.hotel.searchResults.expectedPrice.replaceAll(" ", "")

			//Commission Amount and Currency
			resultData.hotel.confirmationPage.actualCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
			resultData.hotel.confirmationPage.expectedCommissionAmountAndCurrency=data.expected.comTxt+resultData.hotel.itineraryPage.expectedComValue.replaceAll(" ", "")

			//Close lightbox X function
			resultData.hotel.confirmationPage.actualCloseLightboxDispStatus=getCloseIconDisplayStatus()

			//click on Close lightbox X function
			coseBookItenary()
			sleep(7000)

			//scrollToBottomOfThePage()
			//Get Header Text
			//resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInBookedItemsListScrn(1)
			//resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInBookedItemsListScrn(2)
			resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInBookedItemsListScrn(0)
			//Get Booing ID and number
			resultData.hotel.itineraryPage.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

			//Confirmed tab
			resultData.hotel.itineraryPage.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()
			//confirm tab text
			resultData.hotel.itineraryPage.actualStatusTabDispStatus=getStatusInBookedItemsScrn()

			if((data.input.bookItem.toString().toBoolean()) || (data.input.addItems.toString().toBoolean()))
			{
				//Amend tab
				resultData.hotel.itineraryPage.actualAmendTabDispStatus=getAmendTabDisplayStatus()
				//Amend tab text
				resultData.hotel.itineraryPage.actualAmendTabTxt=getTabTxtInBookedItemsScrn(0)
				//Cancel tab
				resultData.hotel.itineraryPage.actualCancelTabDispStatus=getCancelTabDisplayStatus()
				//Cancel tab text
				resultData.hotel.itineraryPage.actualCancelTabTxt=getTabTxtInBookedItemsScrn(1)
			}
			else{

				//Amend tab
				//resultData.hotel.itineraryPage.actualAmendTabDispStatus=getAmendTabDisplayStatusInBookedItmsSec()
				resultData.hotel.itineraryPage.actualAmendTabDispStatus=getAmendTabDisplayStatusInBookedItmsSec(0)
				if(data.input.unavailableItem.toString().toBoolean()){
					try{
						//Amend tab text
						resultData.hotel.itineraryPage.actualAmendTabTxt=getAmmendTabTxtInBookedItemsScrn()

					}catch(Exception e){
						resultData.hotel.itineraryPage.actualAmendTabTxt="Unable To Read From UI - Amend Tab  Text"
					}
					try {
						//Cancel tab text
						resultData.hotel.itineraryPage.actualCancelTabTxt = getTabTxtInBookedItemsSection(0)
					}catch(Exception e){
						resultData.hotel.itineraryPage.actualCancelTabTxt ="Unable To Read From UI - Cancel Tab Text"
					}
				}
				else{
					//Amend tab text
					resultData.hotel.itineraryPage.actualAmendTabTxt=getTabTxtInBookedItemsSection(0)
					//Cancel tab text
					resultData.hotel.itineraryPage.actualCancelTabTxt=getTabTxtInBookedItemsSection(1)
				}
				try {
					//Cancel tab
					resultData.hotel.itineraryPage.actualCancelTabDispStatus = getCancelTabDisplayStatusInBookedItmsScrn()
				}catch(Exception e){
					resultData.hotel.itineraryPage.actualCancelTabDispStatus ="Unable To Read From UI - Cancel Tab"
				}

			}

			//Hotel Image Display status
			resultData.hotel.itineraryPage.actualHotelImageDispStatus=getHotelImageDisplayStatus()

			//Capture Image Src URL
			resultData.hotel.itineraryPage.actualImageSrcURLinBookedItem=getImageSrcURLInBookedItem(0)
			println("$resultData.hotel.itineraryPage.actualImageSrcURLinBookedItem")

			//Hotel Name
			resultData.hotel.itineraryPage.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

			//Caputre Hotel name Src URL
			resultData.hotel.itineraryPage.actualHotelNameLinkTxt=getHotelNameSrcURLInSuggestedItem(0)
			println("$resultData.hotel.itineraryPage.actualHotelNameLinkTxt")

			//Hotel Star Rating
			resultData.hotel.itineraryPage.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)

			//room description

			String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

			String descText=tmpItineraryDescList.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxtInBookedItmsScrn=descText.trim()
			//Pax number requested
			String tempTxt=tmpItineraryDescList.getAt(1)
			List<String> tmpDescList=tempTxt.tokenize(".")

			String paxNumDetails=tmpDescList.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()

			//Rate plan - meal basis
			String ratePlantxtDetails=tmpDescList.getAt(1)
			resultData.hotel.itineraryPage.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()
			//Free cancellation until date
			resultData.hotel.itineraryPage.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)

			//scrollToTopOfThePage()
			//scrollToBottomOfThePage()
			if(data.input.cancelItem.toString().toBoolean())
			{
				//Inventory Status
				String invStatus
				try{
					//commented below line since page obect changed in 10.3
					//invStatus=getRoomStatusfromSuggestedItem(0)
					invStatus=getTransferStatusDisplayedSuggestedItem(0)
					println("Ammendment Title : $invStatus")
					sleep(2000)
				}catch(Exception e)
				{
					invStatus="Unable To Read From UI"
				}
				resultData.hotel.itineraryPage.actualInventoryStatusInBookedItems=invStatus
			}
			//Requested Check in and nights
			resultData.hotel.itineraryPage.actualDurationTxtInBookedItmsScrn=getItenaryDurationInSuggestedItem(0)

			//commission and percentage
			resultData.hotel.itineraryPage.actualCommissionAndPercentatgeInBookedItmsScrn=getCommisionAndPercentageInBookeddetailsScrn(0)

			//Room rate amound and currency
			//resultData.hotel.itineraryPage.actualPriceAndcurrencyInBookedItmsScrn=getItenaryPriceInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualPriceAndcurrencyInBookedItmsScrn=getSuggestedItemsItenaryPrice(0)


		}
		if(data.input.bookItem.toString().toBoolean())
		{

			//Add a remark or content
			resultData.hotel.itineraryPage.actualRemarksInBookedItmsScrn=getRemarksInBookeddetailsScrn()
			resultData.hotel.itineraryPage.expectedRemarksInBookedItmsScrn=data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1

			//Edit Icon display status
			resultData.hotel.itineraryPage.actualEditIconInBookedDetailScrnDispStatus=getEditIconInBookedDetailsScrnDisplayStatus()
			sleep(2000)
			scrollToTopOfThePage()
			//click on Edit icon
			clickEditRemarksButton(0)
			sleep(5000)
			//Add comment or special request lighbox display
			resultData.hotel.itineraryPage.actualHeaderTxtInAddCmntOrSpeclReqScrnDispStatus=getAddCmntOrSpclReqDisplayStatus()

			//Add comment or special request lighbox text
			resultData.hotel.itineraryPage.actualHeaderTxtInAddCmntOrSpeclReqScrn=getBookingStatusOnLightBox()

			//Select from the choices below or write your own request - text
			resultData.hotel.itineraryPage.actualHeaderTxtSelChoicesBelowInAddCmntOrSpeclReqScrn=getTravellerCannotBeDeletedheaderText().trim()

			//Please Note: text
			resultData.hotel.itineraryPage.actualHeaderTxtPlzNoteInAddcmntOrSclReqScrn=getHeaderTxtInAddCmntrSpclReqScrn(0)

			//Will arrive without voucher- Should display with box ticked.
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualwilArrivWithoutVochrCheckBoxStatus=getWilArrivWithOutVochrCheckBoxCheckedStatus()
			//Will arrive without voucher text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualwilArrivWithoutVochrTxt=getCheckBoxTextInAddSpecialRemarkSection(0)

			//Click
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualCheckBoxTickStatusAfterClick=clickWilArrivWithOutVochrCheckBox()

			//Previous night is booked for early morning arrival display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus()

			//Previous night is booked for early morning arrival text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPreviousNightTxt=getCheckBoxTextInAddSpecialRemarkSection(1)

			//Late check out check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus()

			//Late check out Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateCheckOutTxt=getCheckBoxTextInAddSpecialRemarkSection(2)

			//Late Arrival check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus()

			//Late Arrival text box
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(3)


			//Passengers Are HoneyMooners check box Display Status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus()

			//Passengers Are HoneyMooners Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPassengersAreHoneyMoonersTxt=getCheckBoxTextInAddSpecialRemarkSection(4)

			//Early Arrival Disp Status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus()

			//Early Arrival Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualEarlyArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(5)

			//If possible, please provide: text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualHeaderTxtIfPssblPlzProvideTxt=getHeaderTxtInAddCmntrSpclReqScrn(1)

			//Twin Room check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus()

			//Twin Room Text Box
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualTwinRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(6)

			//Smoking Room Check Box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus()
			//Smoking Room Check Box Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(7)

			//Inter Connecting Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualInterConnectingRoomCheckboxDispStatus=getInterConnectingRoomCheckBoxDisplayStatus()
			//Inter connecting Rooms text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualIntrConnectngRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(8)

			//Adjoining Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus()
			//Adjoining Rooms check box text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualAdjoiningRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(9)


			//Quiet Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus()
			//Quiet Rooms check box text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualQuietRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(10)

			//Non-Smoking Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus()
			//Non-Smoking Rooms text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualNonSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(11)


			//Room on lowest floor display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus()
			//Room on lowest floor text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnLowestFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(12)


			//Room on high floor checkbox display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus()
			//Room on high floor text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnHighFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(13)

			//Double Room check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus()
			//Double Room text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualDoubleRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(14)

			//Room with bathtub
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus()
			//Room with bathtub
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomwithBathTubtxt=getCheckBoxTextInAddSpecialRemarkSection(15)

			//Arrival Flight Number check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus()
			//Arrival Flight Number text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumtxt=getCheckBoxTextInAddSpecialRemarkSection(16)

			//Arrival Flight Number text box existence
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus()

			//Hrs
			//at text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(0)
			//Hrs text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(1)
			//Hrs select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus()


			//Mins
			//mins text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(2)
			//mins select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus()

			//On Date
			//on text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(3)
			//on Day select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus()
			//on Month select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus()

			//Click Smoking Room Check Box
			clickSmokingRoomCheckBox()
			sleep(3000)

			//capture Smoking Room check box ticked or not
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingRoomCheckBoxCheckedStatus=getSmokingRoomCheckBoxCheckedStatus()

			//Click on Save button
			scrollAndclickOnSaveBtn()
			sleep(5000)

			//Confirmed booking after Edit remarks

			//Hotel Image Display status
			resultData.hotel.itineraryPage.actualHotelImageDispStatusInConfBkngAftrEdtRmrks=getHotelImageDisplayStatus()

			//Capture Image Src URL
			resultData.hotel.itineraryPage.actualImageSrcURLinBookedItemInConfBkngAftrEdtRmrks=getImageSrcURLInBookedItem(0)
			println("$resultData.hotel.itineraryPage.actualImageSrcURLinBookedItemInConfBkngAftrEdtRmrks")

			//Hotel Name
			resultData.hotel.itineraryPage.actualHotelNameTxtInConfBkngAftrEdtRmrks=getTransferNameInSuggestedItem(0)

			//Caputre Hotel name Src URL
			resultData.hotel.itineraryPage.actualHotelNameLinkTxtInConfBkngAftrEdtRmrks=getHotelNameSrcURLInSuggestedItem(0)
			println("$resultData.hotel.itineraryPage.actualHotelNameLinkTxtInConfBkngAftrEdtRmrks")

			//Hotel Star Rating
			resultData.hotel.itineraryPage.actualStarRatingInBookedHotelItemInConfBkngAftrEdtRmrks=getRatingForTheHotelInSuggestedItem(0)

			//room description

			String complTxt=getItinenaryDescreptionInSuggestedItem(0)
			List<String> itineraryDescList=complTxt.tokenize(",")

			String descTxt=itineraryDescList.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxtInBookedItmsScrnInConfBkngAftrEdtRmrks=descTxt.trim()
			//Pax number requested
			String tmpText=itineraryDescList.getAt(1)
			List<String> descList=tmpText.tokenize(".")

			String paxNo=descList.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNumInBookedItmsScrnInConfBkngAftrEdtRmrks=paxNo.trim()

			//Rate plan - meal basis
			String ratePlanDetails=descList.getAt(1)
			resultData.hotel.itineraryPage.actualratePlanInBookedItmsScrnInConfBkngAftrEdtRmrks=ratePlanDetails.trim()

			//Free cancellation until date
			resultData.hotel.itineraryPage.actualFreeCnclTxtInConfBkngAftrEdtRmrks=getItinenaryFreeCnclTxtInSuggestedItem(0)


			//Requested Check in and nights
			resultData.hotel.itineraryPage.actualDurationTxtInConfBkngAftrEdtRmrks=getItenaryDurationInSuggestedItem(0)

			//commission and percentage
			resultData.hotel.itineraryPage.actualCommissionAndPercentatgeInConfBkngAftrEdtRmrks=getCommisionAndPercentageInBookeddetailsScrn(0)

			//Room rate amound and currency
			resultData.hotel.itineraryPage.actualPriceAndcurrencyInConfBkngAftrEdtRmrks=getItenaryPriceInSuggestedItem(0)




		}

		if(data.input.cancelItem.toString().toBoolean())
		{
			sleep(3000)
			scrollToBottomOfThePage()

			//select above confirmed (1st item) and click on Cancel tab
			//clickOnCancelOrAmendTabButton(1)
			clickOnCancelOrAmendTabButton(2)

			//Cancel item lightbox display status
			resultData.hotel.removeItemPage.actualCancelItemDispStatus=getCancelItemDisplayStatus()

			//lightbox - Title text , Text - Cancel item
			resultData.hotel.removeItemPage.actualCancelItemTxt=getCancellationHeader()

			//Close lightbox X function
			resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()

			//hotel image link
			resultData.hotel.removeItemPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(1)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.actualImageURL=getImageSrcURLInSuggestedItem(1)

			//hotel name
			resultData.hotel.removeItemPage.actualHotelNameTxt=getTransferNameInSuggestedItem(1)

			//hotel  link
			resultData.hotel.removeItemPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(2)

			//hotel star rating
			resultData.hotel.removeItemPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(1)

			//Room - description
			//String roomdescComplText=getItinenaryDescreptionInSuggestedItem(0)
			String roomdescComplText=getItineraryDescInRemoveItemScreen(0)
			List<String> tempItinryDescList=roomdescComplText.tokenize(",")

			String roomDscText=tempItinryDescList.getAt(0)
			resultData.hotel.removeItemPage.actualroomdescTxt=roomDscText.trim()
			//Pax number requested
			String parTxt=tempItinryDescList.getAt(1)
			List<String> parDescList=parTxt.tokenize(".")

			String paxNum=parDescList.getAt(0)
			resultData.hotel.removeItemPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlanMealBasistxt=parDescList.getAt(1)
			resultData.hotel.removeItemPage.actualratePlan=ratePlanMealBasistxt.trim()

			//Free cancellation until date
			String completeTxt=getRoomDescPaxRatePlanFreeCnclTxtInCancelItemScrn()
			List<String> tempcompleteTxtList=completeTxt.tokenize(".")

			String freeCanclText=tempcompleteTxtList.getAt(2)
			println("Cancel Free Cancl Txt $freeCanclText")
			resultData.hotel.removeItemPage.actualFreeCnclTxt=freeCanclText.trim()

			//requested check in date and nights
			resultData.hotel.removeItemPage.actualDurationTxt=getItenaryDurationInSuggestedItem(1)

			//Commission and percentage
			resultData.hotel.removeItemPage.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(1)

			//Room rate amount and currency
			//resultData.hotel.removeItemPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(1)
			resultData.hotel.removeItemPage.actualPriceAndcurrency=getCancelledItemAmount(0)+" GBP"
			//display function button No
			resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
			//display function button Yes
			resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

			//Click No
			clickNoOnRemoveItenary()
			sleep(3000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//select above confirmed (1st item) and click on Cancel tab
			//clickOnCancelOrAmendTabButton(1)
			clickOnCancelOrAmendTabButton(2)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()

			waitForAjaxIconToDisappear()
			sleep(6000)

			//title text - Unavailable and Cancelled items
			//resultData.hotel.removeItemPage.cancelledItems.actualCancelledItmsTxt=getHeaderTxtInBookedItemsListScrn(2)
			resultData.hotel.removeItemPage.cancelledItems.actualCancelledItmsTxt=getHeaderTxtInBookedItemsListScrn(0)

			//Get Booing ID and number
			resultData.hotel.removeItemPage.cancelledItems.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

			//Status - cancelled - text
			//Commented since status is not shown after booking in 10.3
			//resultData.hotel.removeItemPage.cancelledItems.actualStatus=getStatusDisplayed().trim()


			//hotel image link
			resultData.hotel.removeItemPage.cancelledItems.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(0)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.cancelledItems.actualImageURL=getImageSrcURLInSuggestedItem(0)

			//hotel name
			resultData.hotel.removeItemPage.cancelledItems.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

			//hotel  link
			resultData.hotel.removeItemPage.cancelledItems.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(0)

			//hotel star rating
			resultData.hotel.removeItemPage.cancelledItems.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(0)

			//Room - description
			String roomdescCanclText=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItinryCanclDescList=roomdescCanclText.tokenize(",")

			String roomDscCanclText=tempItinryCanclDescList.getAt(0)
			resultData.hotel.removeItemPage.cancelledItems.actualroomdescTxt=roomDscCanclText.trim()
			//Pax number requested
			String paxCanTxt=tempItinryCanclDescList.getAt(1)
			List<String> paXCanDescList=paxCanTxt.tokenize(".")

			String CancpaxNum=paXCanDescList.getAt(0)
			resultData.hotel.removeItemPage.cancelledItems.actualPaxNum=CancpaxNum.trim()

			//Rate plan - meal basis
			String canclratePlanMealBasistxt=paXCanDescList.getAt(1)
			resultData.hotel.removeItemPage.cancelledItems.actualratePlan=canclratePlanMealBasistxt.trim()

			//Free cancellation until date
			resultData.hotel.removeItemPage.cancelledItems.actualFreeCnclTxt=getItinenaryFreeCnclTxtInSuggestedItem(0)

			//requested check in date and nights
			resultData.hotel.removeItemPage.cancelledItems.actualDurationTxt=getItenaryDurationInSuggestedItem(0)

			//Commission and percentage
			resultData.hotel.removeItemPage.cancelledItems.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)

			//Room rate amount and currency
			//resultData.hotel.removeItemPage.cancelledItems.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)
			resultData.hotel.removeItemPage.cancelledItems.actualPriceAndcurrency=getSuggestedItemsItenaryPrice(0)

			//Travellers
			resultData.hotel.removeItemPage.cancelledItems.actualTravellerDetailsTxt=getTravellernamesCancelledBookedItem(0)
			resultData.hotel.removeItemPage.cancelledItems.expectedTravellerDetailsTxt="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+scndTrvlrfirstName+" "+scndTrvlrlastName


		}



		if(data.input.unavailableItem.toString().toBoolean())
		{
			sleep(4000)
			waitTillLoadingIconDisappears()
			scrollToBottomOfThePage()

			//CLICK ON REMOVE TAB
			//clickOnCancelOrAmendTabButton(1)
			RemoveItenaryButton(0)

			sleep(3000)

			//Remove item lightbox display status
			resultData.hotel.removeItemPage.actualremoveItemLightBoxDispStatus=getCancelItemDisplayStatus()

			//lightbox - Title text , Text - Remove item
			resultData.hotel.removeItemPage.actualremoveItemTxt=getCancellationHeader()

			//Close lightbox X function
			resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()

			//text - Are you sure you want to remove the following item form this itinerary?
			resultData.hotel.removeItemPage.actualremoveHeaderTxt=getTravellerCannotBeDeletedheaderText()

			//hotel image link
			resultData.hotel.removeItemPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(1)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.actualImageURL=getImageSrcURLInSuggestedItem(1)

			//hotel name
			resultData.hotel.removeItemPage.actualHotelNameTxt=getTransferNameInSuggestedItem(1)

			//hotel  link
			resultData.hotel.removeItemPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(2)

			//hotel star rating
			resultData.hotel.removeItemPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(1)

			//Room - description
			String roomdescComplText=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItinryDescList=roomdescComplText.tokenize(",")

			String roomDscText=tempItinryDescList.getAt(0)
			resultData.hotel.removeItemPage.actualroomdescTxt=roomDscText.trim()
			//Pax number requested
			String parTxt=tempItinryDescList.getAt(1)
			List<String> parDescList=parTxt.tokenize(".")

			String paxNum=parDescList.getAt(0)
			resultData.hotel.removeItemPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlanMealBasistxt=parDescList.getAt(1)
			resultData.hotel.removeItemPage.actualratePlan=ratePlanMealBasistxt.trim()

			//Free cancellation until date
			String completeTxt=getRoomDescPaxRatePlanFreeCnclTxtInCancelItemScrn()
			List<String> tempcompleteTxtList=completeTxt.tokenize(".")
			String freeCanclText
			try{
				freeCanclText=tempcompleteTxtList.getAt(2)
				println("Cancel Free Cancl Txt $freeCanclText")
				resultData.hotel.removeItemPage.actualFreeCnclTxt=freeCanclText.trim()
			}catch(Exception e){
				resultData.hotel.removeItemPage.actualFreeCnclTxt="Unable To Read From UI"
			}



			//requested check in date and nights
			resultData.hotel.removeItemPage.actualDurationTxt=getItenaryDurationInSuggestedItem(1)

			//Commission and percentage
			resultData.hotel.removeItemPage.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(1)

			try{
				//Room rate amount and currency
				//resultData.hotel.removeItemPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(1)
				resultData.hotel.removeItemPage.actualPriceAndcurrency=getItenaryPriceInCancelItem()

			}catch(Exception e){
				//Room rate amount and currency
				resultData.hotel.removeItemPage.actualPriceAndcurrency="Unable To Read Text From UI"

			}

			//display function button No
			resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
			//display function button Yes
			resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

			//Click No
			clickNoOnRemoveItenary()
			sleep(3000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//select above confirmed (1st item) and click on Cancel tab
			//clickOnCancelOrAmendTabButton(1)
			//clickOnCancelOrAmendTabButton(2)
			RemoveItenaryButton(0)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(6000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//Should not have suggest item list displayed
			//resultData.hotel.removeItemPage.actualUnavailableAndCancelItemSecDispStatus=getUnavailableAndCancelItemDisplayStatus()
		}


		if(!(data.input.unavailableItem.toString().toBoolean()))
		{
			//Add a remark or content
			resultData.hotel.itineraryPage.actualRemarksInConfBkngAftrEdtRmrks=getRemarksInBookeddetailsScrn().replaceAll(":","")

			if(data.input.children.size()>0){
				resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=data.expected.IfPsblePlzProvTxt+" "+data.expected.chkBoxTxt_8+" "+data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
				resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks.replaceAll(":","")
			}
			else
				resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
			    resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks.replaceAll(":","")
		}

		if(data.input.addItems.toString().toBoolean())
		{
			//click on Book button
			clickOnBookIcon()
			sleep(5000)

			//Selecting travellers. Note: this step is not there in test case - but we cannot click on confirm booking without selecting travellers
			clickOnTravellerCheckBox(0)
			sleep(3000)
			clickOnTravellerCheckBox(1)
			sleep(3000)
			scrollToRemarksTxt()

			//click on downside arrow
			clickExpandRemarksTandC()
			sleep(2000)

			//tick the check box Will arrive without voucher
			clickWilArrivWithOutVochrCheckBox()
			sleep(2000)

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
			//select above confirmed (1st item) and click on Cancel tab
			clickOnCancelOrAmendTabButton(3)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(6000)


			//click on Book button
			clickOnBookIcon()
			sleep(5000)

			//Selecting travellers. Note: this step is not there in test case - but we cannot click on confirm booking without selecting travellers
			clickOnTravellerCheckBox(0)
			sleep(3000)
			clickOnTravellerCheckBox(1)
			sleep(3000)

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

			//select above confirmed (1st item) and click on Cancel tab
			clickOnCancelOrAmendTabButton(3)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(6000)

			//Capture Booked Items title text
			resultData.hotel.removeItemPage.actualBookedItemsHeaderTxt=getHeaderTxtInBookedItemsListScrn(2)

			//Capture no. of items under Booked Items
			resultData.hotel.removeItemPage.actualBookedItemsCount=getNoOfItemsInBookedItemsSection()
			//Total label text
			resultData.hotel.removeItemPage.actualTotalLabelTextInBookedItemsSec=getTotalLabelTxtInNonBookedItemsScrn()

			//Total / Room rate amount and currency
			resultData.hotel.removeItemPage.actualallItemsTotalAmountAndCurrencyInBookedItemsSec=getTotalAmountAndCurrencyInNonBookedItemsScrn()

			resultData.hotel.removeItemPage.expectedItemsTotalAmountAndCurrency=resultData.hotel.searchResults.itineraryBuilder.secondItemPrice+" "+clientData.currency

			scrollToTopOfThePage()

			//Capture Unavailable and cancel item list
			resultData.hotel.removeItemPage.actualUnavailableAndCancelItemsHeaderTxt=getHeaderTxtInBookedItemsListScrn(4)

			//Capture no. of items under Unavailable and cancel item list
			resultData.hotel.removeItemPage.actualUnavailableAndCancelItemsCount=getNoOfItemsInUnavailableAndCancelItemsSection()

			//should not shown total amount under Unavailable and cancel item list
			resultData.hotel.removeItemPage.actualTotalDispStatus=getTotalDisplayStatus()

			//Should not have suggest item / Non booked item list displayed
			resultData.hotel.removeItemPage.actualNonBookedItemOrSuggestedItmsSecDispStatus=getNonBookedItemsOrSuggestedItemsSecDispStatus()
		}

	}

	protected def itineraryBuild(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData) {

		sleep(3000)
		at HotelSearchResultsPage
		if(getItineraryBarExpandOrCollapseStatus()==true) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}
		at ItenaryBuilderPage
		//hideItenaryBuilder()
		sleep(7000)

		if(data.input.addItems.toString().toBoolean())
		{
			String firstItemPrice=getItenaryPrice(0).replaceAll("\n","").replaceAll("GBP", "").trim()
			sleep(2000)
			println("Pirce is$firstItemPrice")
			String scndItemPrice=getItenaryPrice(1).replaceAll("\n","").replaceAll("GBP", "").trim()
			sleep(2000)
			String thirdItemPrice=getItenaryPrice(2).replaceAll("\n","").replaceAll("GBP", "").trim()
			sleep(2000)
			String fourthItemPrice=getItenaryPrice(3).replaceAll("\n","").replaceAll("GBP", "").trim()
			sleep(2000)

			//float totalPrice=Float.parseFloat(firstItemPrice)+Float.parseFloat(scndItemPrice)+Float.parseFloat(thirdItemPrice)+Float.parseFloat(fourthItemPrice)
			DecimalFormat df = new DecimalFormat("####.00");
			//println("Total Price ----->"+df.format(totalPrice))
			//resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice=df.format(totalPrice)
			sleep(2000)
			float secondItemPrice=Float.parseFloat(scndItemPrice)
			resultData.hotel.searchResults.itineraryBuilder.secondItemPrice=df.format(secondItemPrice)

			float thrdItemPrice=Float.parseFloat(thirdItemPrice)
			resultData.hotel.searchResults.itineraryBuilder.thirdItemPrice=df.format(thrdItemPrice)


		}

		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			String firstItemPrice=getItenaryPrice(0).replace(" GBP", "").replaceAll(",","").replace(" JPY", "")
			println("First Item Pirce is$firstItemPrice")
			String scndItemPrice=getItenaryPrice(1).replace(" GBP", "").replaceAll(",","").replace(" JPY", "")
			println("Second Item Pirce is$scndItemPrice")

			float totalPrice=Float.parseFloat(firstItemPrice)+Float.parseFloat(scndItemPrice)
			DecimalFormat df = new DecimalFormat("####.00");
			println("Total Price ----->"+df.format(totalPrice))
			resultData.hotel.searchResults.itineraryBuilder.multiRoomTotalPrice=df.format(totalPrice)
			sleep(2000)
		}

		println("$resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice")
		String depDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
		//capture new Itinerary reference number
		String itineraryBuilderTitle = getItenaryBuilderTtile()
		//println("$itineraryBuilderTitle")
		List<String> tempList=itineraryBuilderTitle.tokenize(" ")
		//println("$tempList")
		String itineraryId=tempList.getAt(0)+tempList.getAt(1)
		resultData.hotel.searchResults.itineraryBuilder.retitineraryId=itineraryId
		println("Retrieved Itinerary ID: $resultData.hotel.searchResults.itineraryBuilder.retitineraryId")

		//click Go to Itinerary Link
		clickOnGotoItenaryButton()
		sleep(2000)

		at ItineraryTravllerDetailsPage

		if(data.input.noOfRooms.toString().toInteger()>1){
			//capture hotel name in suggested items for first item
			resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms_firstCard=getTransferNameInSuggestedItem(0)
			//capture hotel name in suggested items for second item
			resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms_secondCard=getTransferNameInSuggestedItem(1)
		}
		else{
			//capture hotel name in suggested items
			resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms=getTransferNameInSuggestedItem(0)
		}
		//Input Title
		selectTitle(data.expected.title_txt,0)
		//Input First Name
		enterFirstName(data.expected.firstName,0)
		//Input Last Name
		enterLastName(data.expected.lastName,0)
		//Input Email Address
		enterEmail(data.expected.emailAddr,0)
		//Input Area Code
		//commented since 10.3 field is removed
		//enterCountryCode(data.expected.countryCode,0)
		//sleep(1000)
		//Input Telephone number
		enterTelephoneNumber(data.expected.telephone_Num,0)
		sleep(1000)

		String scndTrvlrfirstName=data.expected.firstName+"Second"
		String scndTrvlrlastName=data.expected.lastName+"Second"
		//enterAdultTravellerDetails(data.expected.title_txt, scndTrvlrfirstName , scndTrvlrlastName,0)
		fillTravellerDetailsNotSaved(data.expected.title_txt, scndTrvlrfirstName , scndTrvlrlastName,1)
		sleep(3000)



		//Add 2nd Traveller details and click on save
		//String scndTrvlrfirstName=data.expected.firstName+"2"
		//String scndTrvlrlastName=data.expected.lastName+"2"

		//enterAdultTravellerDetails(data.expected.title_txt, scndTrvlrfirstName , scndTrvlrlastName,1)
		//sleep(4000)


		//waitTillLoadingIconDisappears()

		//String thirdTrvlrfirstName=data.expected.childFirstName+"3"
		//String thirdTrvlrlastName=data.expected.childLastName+"3"
		String thirdTrvlrfirstName=data.expected.childFirstName+"Third"
		String thirdTrvlrlastName=data.expected.childLastName+"Third"
		String firstChildAge=data.expected.childAgeFirst

		//String fourthTrvlrfirstName=data.expected.childFirstName+"4"
		//String fourthTrvlrlastName=data.expected.childLastName+"4"
		String fourthTrvlrfirstName=data.expected.childFirstName+"Fourth"
		String fourthTrvlrlastName=data.expected.childLastName+"Fourth"
		String secondChildAge=data.expected.childAgeSecond

		if(!(data.input.bookItemMultiRoom))
		{
			if(data.input.children.size()>0){

				//Add 3rd Traveller Details - child traveller

				//enterChildTravellerDetails(data.expected.title_txt,thirdTrvlrfirstName,thirdTrvlrlastName,firstChildAge,0)
				//fillTravellerDetailsNotSaved(data.expected.title_txt,thirdTrvlrfirstName,thirdTrvlrlastName,3)
				enterFirstName(thirdTrvlrfirstName, 2)
				enterLastName(thirdTrvlrlastName, 2)

				ClickRadioButtonAdultOrChild(4)
				sleep(2000)
				//clickAndEnterAge(firstChildAge)
				enterChildAge(firstChildAge,1)


				//waitTillLoadingIconDisappears()
				//Capture 3rd traveller details

				//Add 4th Traveller Details - child traveller

				//enterChildTravellerDetails(data.expected.title_txt,fourthTrvlrfirstName,fourthTrvlrlastName,secondChildAge,0)
				//fillTravellerDetailsNotSaved(data.expected.title_txt,fourthTrvlrfirstName,fourthTrvlrlastName,4)
				enterFirstName(fourthTrvlrfirstName, 3)
				enterLastName(fourthTrvlrlastName, 3)

				ClickRadioButtonAdultOrChild(6)
				//sleep(2000)
				//clickAndEnterAge(secondChildAge)
				enterChildAge(secondChildAge,2)
				sleep(3000)
				//Click on Save Button
				clickonSaveButton(0)
				sleep(3000)

				waitTillLoadingIconDisappears()
				sleep(4000)

				//Capture 3rd traveller details
				//resultData.hotel.itineraryPage.expectedthirdTravellerName=data.expected.title_txt+" "+thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"
				resultData.hotel.itineraryPage.expectedthirdTravellerName=thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"
				println("$resultData.hotel.itineraryPage.expectedthirdTravellerName")
				resultData.hotel.itineraryPage.actualthirdTravellerName=getLeadTravellerName(4)


				//Capture 4th traveller details

				//resultData.hotel.itineraryPage.expectedfourthTravellerName=data.expected.title_txt+" "+fourthTrvlrfirstName+" "+fourthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
				resultData.hotel.itineraryPage.expectedfourthTravellerName=fourthTrvlrfirstName+" "+fourthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
				println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
				resultData.hotel.itineraryPage.actualfourthTravellerName=getLeadTravellerName(5)
			}

		}
		if(data.input.noOfRooms>1) {

			/*thirdTrvlrfirstName=data.expected.firstName+"3"
            thirdTrvlrlastName=data.expected.lastName+"3"

            fourthTrvlrfirstName=data.expected.firstName+"4"
            fourthTrvlrlastName=data.expected.lastName+"4"*/

			thirdTrvlrfirstName = data.expected.firstName + "Third"
			thirdTrvlrlastName = data.expected.lastName + "Third"

			fourthTrvlrfirstName = data.expected.firstName + "Fourth"
			fourthTrvlrlastName = data.expected.lastName + "Fourth"

			//String fifthTrvlrfirstName=data.expected.childFirstName+"5"
			//String fifthTrvlrlastName=data.expected.childLastName+"5"
			String fifthTrvlrfirstName = data.expected.childFirstName + "Fifth"
			String fifthTrvlrlastName = data.expected.childLastName + "Fifth"
			firstChildAge = data.input.children.getAt(0)
			//String sixthTrvlrfirstName=data.expected.childFirstName+"6"
			//String sixthTrvlrlastName=data.expected.childLastName+"6"
			String sixthTrvlrfirstName = data.expected.childFirstName + "Sixth"
			String sixthTrvlrlastName = data.expected.childLastName + "Sixth"
			secondChildAge = data.input.children.getAt(1)
			//Add 3rd Traveller details and click on save

			//Add 3rd Traveller details and click on save
			fillTravellerDetailsNotSaved(data.expected.title_txt, thirdTrvlrfirstName, thirdTrvlrlastName, 2)
			sleep(2000)
			/*enterAdultTravellerDetails(data.expected.title_txt, thirdTrvlrfirstName , thirdTrvlrlastName,0)
            sleep(4000)
            waitTillLoadingIconDisappears()*/
			//Add 4th Traveller details and click on save
			fillTravellerDetailsNotSaved(data.expected.title_txt, fourthTrvlrfirstName, fourthTrvlrlastName, 3)
			sleep(2000)
			/*enterAdultTravellerDetails(data.expected.title_txt, fourthTrvlrfirstName , fourthTrvlrlastName,0)
            sleep(4000)
            waitTillLoadingIconDisappears()


            //Capture 3rd traveller details
            resultData.hotel.itineraryPage.expectedthirdTravellerName=data.expected.title_txt+" "+thirdTrvlrfirstName+" "+thirdTrvlrlastName
            resultData.hotel.itineraryPage.actualthirdTravellerName=getLeadTravellerName(4)

            //Capture 4th traveller details
            resultData.hotel.itineraryPage.expectedfourthTravellerName=data.expected.title_txt+" "+fourthTrvlrfirstName+" "+fourthTrvlrlastName
            println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
            resultData.hotel.itineraryPage.actualfourthTravellerName=getLeadTravellerName(5)
            */

			if (data.input.children.size() > 0) {
				//Add 5th Traveller Details - child traveller
				//clickAddTravellersButton()
				//sleep(2000)
				ClickRadioButtonAdultOrChild(8)
				//fillTravellerDetailsNotSaved(data.expected.title_txt,fifthTrvlrfirstName,fifthTrvlrlastName,4)
				enterFirstName(fifthTrvlrfirstName, 4)
				sleep(1000)
				enterLastName(fifthTrvlrlastName, 4)
				sleep(2000)
				clickAndEnterAge(firstChildAge, 3)
				sleep(3000)

				/*enterChildTravellerDetails(data.expected.title_txt,fifthTrvlrfirstName,fifthTrvlrlastName,firstChildAge,0)
                sleep(4000)
                waitTillLoadingIconDisappears()*/
				//Add 6th Traveller Details - child traveller
				//fillTravellerDetailsNotSaved(data.expected.title_txt,sixthTrvlrfirstName,sixthTrvlrlastName,5)
				//clickAddTravellersButton()
				//sleep(2000)
				ClickRadioButtonAdultOrChild(10)
				sleep(2000)
				enterFirstName(sixthTrvlrfirstName, 5)
				sleep(1000)
				enterLastName(sixthTrvlrlastName, 5)
				sleep(1000)
				clickAndEnterAge(secondChildAge, 4)
				sleep(2000)

				/*enterChildTravellerDetails(data.expected.title_txt,sixthTrvlrfirstName,sixthTrvlrlastName,secondChildAge,0)
                sleep(4000)
                waitTillLoadingIconDisappears()

                sleep(2000)*/

				/*//Capture 5th traveller details

                resultData.hotel.itineraryPage.expectedfifthTravellerName=data.expected.title_txt+" "+fifthTrvlrfirstName+" "+fifthTrvlrlastName+" "+"("+firstChildAge+"yrs)"
                println("$resultData.hotel.itineraryPage.expectedfifthTravellerName")
                resultData.hotel.itineraryPage.actualfifthTravellerName=getLeadTravellerName(6)

                //Capture 6th traveller details
                resultData.hotel.itineraryPage.expectedsixthTravellerName=data.expected.title_txt+" "+sixthTrvlrfirstName+" "+sixthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
                println("$resultData.hotel.itineraryPage.expectedsixthTravellerName")
                resultData.hotel.itineraryPage.actualsixthTravellerName=getLeadTravellerName(7)
                    */
			}
			//Click on Save Button
			//clickonSaveButton()
			clickonSaveButton(0)
			sleep(3000)

			waitTillLoadingIconDisappears()
			sleep(4000)
			/*if(getNoOfTravellers() > 4){
				clickOnExpandTraveller()
			sleep(2000)
			}*/
			//Capture 3rd traveller details
			resultData.hotel.itineraryPage.expectedthirdTravellerName=data.expected.title_txt+" "+thirdTrvlrfirstName+" "+thirdTrvlrlastName
			//resultData.hotel.itineraryPage.expectedthirdTravellerName=thirdTrvlrfirstName+" "+thirdTrvlrlastName
			resultData.hotel.itineraryPage.actualthirdTravellerName=getLeadTravellerName(4)

			//Capture 4th traveller details
			resultData.hotel.itineraryPage.expectedfourthTravellerName=data.expected.title_txt+" "+fourthTrvlrfirstName+" "+fourthTrvlrlastName
			//resultData.hotel.itineraryPage.expectedfourthTravellerName=fourthTrvlrfirstName+" "+fourthTrvlrlastName
			println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
			resultData.hotel.itineraryPage.actualfourthTravellerName=getLeadTravellerName(5)

			if (data.input.children.size() > 0) {
				//Capture 5th traveller details

				resultData.hotel.itineraryPage.expectedfifthTravellerName = fifthTrvlrfirstName + " " + fifthTrvlrlastName + " " + "(" + firstChildAge + "yrs)"
				println("$resultData.hotel.itineraryPage.expectedfifthTravellerName")
				resultData.hotel.itineraryPage.actualfifthTravellerName = getLeadTravellerName(6)

				//Capture 6th traveller details
				resultData.hotel.itineraryPage.expectedsixthTravellerName = sixthTrvlrfirstName + " " + sixthTrvlrlastName + " " + "(" + secondChildAge + "yrs)"
				println("$resultData.hotel.itineraryPage.expectedsixthTravellerName")
				resultData.hotel.itineraryPage.actualsixthTravellerName = getLeadTravellerName(7)
			}

		}

		//Capture lead traveller details
		resultData.hotel.itineraryPage.expectedleadTravellerName=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
		resultData.hotel.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)

		//Capture lead traveller - telephone number
		resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum=data.expected.countryCode+""+data.expected.telephone_Num
		resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum=getLeadTravellerName(1)

		//Caputre lead traveller - email address
		resultData.hotel.itineraryPage.expectedleadTravellerEmail=data.expected.emailAddr
		resultData.hotel.itineraryPage.actualLeadTravellerEmail=getLeadTravellerName(2)

		//Capture 2nd traveller details
		resultData.hotel.itineraryPage.expectedscndTravellerName=data.expected.title_txt+" "+scndTrvlrfirstName+" "+scndTrvlrlastName
		resultData.hotel.itineraryPage.actualscndTravellerName=getLeadTravellerName(3)

		//click on Edit
		scrollToTopOfThePage()
		clickEditIconNextToItineraryHeader()
		sleep(3000)
		waitTillLoadingIconDisappears()
		//Update Name Text
		String todaysDate= dateUtil.addDaysChangeDatetformat(0, "ddMMMyy")
		println("$todaysDate")
		resultData.hotel.itineraryPage.expectedItnrName=todaysDate
		enterItenaryName(todaysDate)

		//Click save button
		//commented below method since page object changed in 10.3
		//clickSaveButton()
		clickOnSaveOrCancelBtnsEditItinry(0)
		sleep(4000)
		waitTillLoadingIconDisappears()
		//capture entered Itinerary Name
		//Capture Itinerary Page - header
		String edtditineraryPageTitle = getItenaryName()
		//println("$itineraryPageTitle")
		List<String> tList=edtditineraryPageTitle.tokenize(" ")
		String edtitinaryName=tList.getAt(2)
		resultData.hotel.itineraryPage.actualSavedItnrName=edtitinaryName.replaceAll("\nEdit", "")

		//Capture suggested item text
		resultData.hotel.itineraryPage.actualSuggestedItemHeaderTxt=getSuggestedItemsHeaderText()

		scrollToBottomOfThePage()
		//click on suggested items question mark icon
		clickonQstnMarkIcon()

		//capture the text
		resultData.hotel.itineraryPage.actualQstnMarkTxt=getQuestionMarkMouseHoverTxt()

		//Capture the total items in suggested items
		resultData.hotel.itineraryPage.actualCountOfItemsInSuggstedItms=getNoOfItemsInNonBookedItems()

		//capture the total (only if there is multiple items)
		if(data.input.addItems.toString().toBoolean())
		{
			resultData.hotel.itineraryPage.actualTotalLabelText=getTotalLabelTxtInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.actualallItemsTotalAmountAndCurrency=getTotalAmountAndCurrencyInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.expectedallItemsTotalAmountAndCurrency=resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice+" "+clientData.currency
		}

		//capture the total (only if there is multiple items)
		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			resultData.hotel.itineraryPage.actualTotalLabelText=getTotalLabelTxtInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.actualallItemsTotalAmountAndCurrency=getTotalAmountAndCurrencyInNonBookedItemsScrn()
			resultData.hotel.itineraryPage.expectedallItemsTotalAmountAndCurrency=resultData.hotel.searchResults.itineraryBuilder.multiRoomTotalPrice+" "+clientData.currency
		}

		scrollToTopOfThePage()
		//<Book> function button
		resultData.hotel.itineraryPage.actualBookBtnDispStatusInSuggestedItems=getBookBtndisplayStatus()
		def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()

		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			//hotel image link - First Card
			resultData.hotel.itineraryPage.actualImageIconLinkExistence_first=getImageIconLinkExistenceInSuggestedItem(0)

			//hotel image link - Second Card
			resultData.hotel.itineraryPage.actualImageIconLinkExistence_second=getImageIconLinkExistenceInSuggestedItem(1)

			//capture hotel image link URL - first
			resultData.hotel.itineraryPage.actualImageURL_first=getImageSrcURLInSuggestedItem(0)
			//capture hotel image link URL - second
			resultData.hotel.itineraryPage.actualImageURL_first=getImageSrcURLInSuggestedItem(1)

			//hotel  link - first
			resultData.hotel.itineraryPage.actualHotelNameURL_first=getHotelNameSrcURLInSuggestedItem(0)
			println "Hotel Link First is:$resultData.hotel.itineraryPage.actualHotelNameURL_first"
			//hotel  link - second
			resultData.hotel.itineraryPage.actualHotelNameURL_second=getHotelNameSrcURLInSuggestedItem(2)
			println "Hotel Link second is:$resultData.hotel.itineraryPage.actualHotelNameURL_second"

			//hotel star rating
			resultData.hotel.itineraryPage.actualHotelStarRating_first=getRatingForTheHotelInSuggestedItem(0)
			//hotel star rating
			resultData.hotel.itineraryPage.actualHotelStarRating_second=getRatingForTheHotelInSuggestedItem(1)

			//Room - description - First
			String roomdescComplTxt_first=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItineraryDescList_first=roomdescComplTxt_first.tokenize(",")

			String roomDescText_first=tempItineraryDescList_first.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxt_first=roomDescText_first.trim()
			//Pax number requested
			String tmpTxt_first=tempItineraryDescList_first.getAt(1)
			List<String> tempDescList_first=tmpTxt_first.tokenize(".")

			String paxNum_first=tempDescList_first.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNum_first=paxNum_first.trim()

			//Rate plan - meal basis
			String ratePlantxt_first=tempDescList_first.getAt(1)
			resultData.hotel.itineraryPage.actualratePlan_first=ratePlantxt_first.trim()

			//Room - description - Second
			String roomdescComplTxt_second=getItinenaryDescreptionInSuggestedItem(1)
			List<String> tempItineraryDescList_second=roomdescComplTxt_second.tokenize(",")

			String roomDescText_second=tempItineraryDescList_second.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxt_second=roomDescText_second.trim()
			//Pax number requested
			String tmpTxt_second=tempItineraryDescList_second.getAt(1)
			List<String> tempDescList_second=tmpTxt_second.tokenize(".")

			String paxNum_second=tempDescList_second.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNum_second=paxNum_second.trim()

			//Rate plan - meal basis
			String ratePlantxt_second=tempDescList_second.getAt(1)
			resultData.hotel.itineraryPage.actualratePlan_second=ratePlantxt_second.trim()


			//Free cancellation until date
			String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
			println "Cancel Date is:$cancelDate"
			resultData.hotel.itineraryPage.expectedCancelDate=cancelDate
			String expectedFreeCanclTxt=data.expected.freecancltxt+" "+resultData.hotel.itineraryPage.expectedCancelDate.toUpperCase()
			resultData.hotel.itineraryPage.expectedFreeCanclTxt=expectedFreeCanclTxt

			resultData.hotel.itineraryPage.actualFreeCnclTxt_first=getItinenaryFreeCnclTxtInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualFreeCnclTxt_second=getItinenaryFreeCnclTxtInSuggestedItem(1)
			//inventory availability
			resultData.hotel.itineraryPage.actualInventoryStatus_first=getTransferStatusDisplayedSuggestedItem(0)
			resultData.hotel.itineraryPage.actualInventoryStatus_second=getTransferStatusDisplayedSuggestedItem(1)

			//commission and percentage
			String commissionPercentageTxt_first
			String commissionPercentageTxt_second
			try{
				commissionPercentageTxt_first=getCommissionPercentInNonBookedItems(0)
				commissionPercentageTxt_second=getCommissionPercentInNonBookedItems(1)
				println("Commission Percentage Value in Non booked items : $commissionPercentageTxt_first")
				println("Commission Percentage Value in Non booked items : $commissionPercentageTxt_second")
				sleep(2000)

			}catch(Exception e)
			{
				commissionPercentageTxt_first="Unable To Read From UI"
				commissionPercentageTxt_second="Unable To Read From UI"
			}
			resultData.hotel.itineraryPage.actualcomPercentTxt_first=commissionPercentageTxt_first.replace("\n", " ")
			resultData.hotel.itineraryPage.actualcomPercentTxt_second=commissionPercentageTxt_second.replace("\n", " ")
			//requested check in date and nights

			nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
			if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"
			println("$dur")
			resultData.hotel.itineraryPage.expectedDurationTxt=dur

			resultData.hotel.itineraryPage.actualDurationTxt_first=getItenaryDurationInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualDurationTxt_second=getItenaryDurationInSuggestedItem(1)
			//Room rate amound and currency
			//resultData.hotel.itineraryPage.actualPriceAndcurrency_first=getItenaryPriceInSuggestedItem(0)
			//resultData.hotel.itineraryPage.actualPriceAndcurrency_second=getItenaryPriceInSuggestedItem(1)
			resultData.hotel.itineraryPage.actualPriceAndcurrency_first=getNonBookedItemsItenaryPrice(0)
			resultData.hotel.itineraryPage.actualPriceAndcurrency_second=getNonBookedItemsItenaryPrice(1)
		}
		else{
			//hotel image link
			resultData.hotel.itineraryPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(0)

			//capture hotel image link URL
			resultData.hotel.itineraryPage.actualImageURL=getImageSrcURLInSuggestedItem(0)

			//hotel  link
			resultData.hotel.itineraryPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(0)

			//hotel star rating
			resultData.hotel.itineraryPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(0)

			//Room - description
			String roomdescComplTxt=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItineraryDescList=roomdescComplTxt.tokenize(",")

			String roomDescText=tempItineraryDescList.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxt=roomDescText.trim()
			//Pax number requested
			String tmpTxt=tempItineraryDescList.getAt(1)
			List<String> tempDescList=tmpTxt.tokenize(".")

			String paxNum=tempDescList.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlantxt=tempDescList.getAt(1)
			resultData.hotel.itineraryPage.actualratePlan=ratePlantxt.trim()
			//Free cancellation until date
			String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.cancelDays.toInteger(), "ddMMMyy")
			println "Cancel Date is:$cancelDate"
			resultData.hotel.itineraryPage.expectedCancelDate=cancelDate
			String expectedFreeCanclTxt=data.expected.freecancltxt+" "+resultData.hotel.itineraryPage.expectedCancelDate.toUpperCase()
			resultData.hotel.itineraryPage.expectedFreeCanclTxt=expectedFreeCanclTxt

			resultData.hotel.itineraryPage.actualFreeCnclTxt=getItinenaryFreeCnclTxtInSuggestedItem(0)
			//inventory availability
			resultData.hotel.itineraryPage.actualInventoryStatus=getTransferStatusDisplayedSuggestedItem(0)

			//commission and percentage
			String commissionPercentageTxt
			try{
				commissionPercentageTxt=verifySuggestedItemCommission(0)
				println("Commission Percentage Value in Non booked items : $commissionPercentageTxt")
				sleep(2000)

			}catch(Exception e)
			{
				commissionPercentageTxt="Unable To Read From UI"
			}
			resultData.hotel.itineraryPage.actualcomPercentTxt=commissionPercentageTxt.replace("\n", " ")

			//requested check in date and nights
			checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()

			nights=data.input.checkOutDays.toString().toInteger()-data.input.checkInDays.toString().toInteger()
			if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"
			println("$dur")
			resultData.hotel.itineraryPage.expectedDurationTxt=dur

			resultData.hotel.itineraryPage.actualDurationTxt=getItenaryDurationInSuggestedItem(0)
			//Room rate amound and currency
			//resultData.hotel.itineraryPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualPriceAndcurrency=getSuggestedItemsItenaryPrice(0)
		}

		if(data.input.addItems.toString().toBoolean())
		{
			//Remove function button display status
			resultData.hotel.removeItemPage.actualRemoveIconDispStatus=getRemoveButtonDisplayStatus(1)
			RemoveItenaryButton(0)
			sleep(3000)

			//Remove item lightbox display status
			resultData.hotel.removeItemPage.actualremoveItemLightBoxDispStatus=getCancelItemDisplayStatus()

			//lightbox - Title text , Text - Remove item
			resultData.hotel.removeItemPage.actualremoveItemTxt=getCancellationHeader()

			//Close lightbox X function
			resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()

			//text - Are you sure you want to remove the following item form this itinerary?
			resultData.hotel.removeItemPage.actualremoveHeaderTxt=getTravellerCannotBeDeletedheaderText()

			//hotel image link
			resultData.hotel.removeItemPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(4)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.actualImageURL=getImageSrcURLInSuggestedItem(4)

			//hotel name
			resultData.hotel.removeItemPage.actualHotelNameTxt=getTransferNameInSuggestedItem(4)

			//hotel  link
			resultData.hotel.removeItemPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(8)

			//hotel star rating
			resultData.hotel.removeItemPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(4)

			//Room - description
			//String roomdescComplText=getItinenaryDescreptionInSuggestedItem(4)
			String roomdescComplText=getItineraryDescInRemoveItemScreen(0)
			List<String> tempItinryDescList=roomdescComplText.tokenize(",")

			String roomDscText=tempItinryDescList.getAt(0)
			resultData.hotel.removeItemPage.actualroomdescTxt=roomDscText.trim()
			//Pax number requested
			String paxTxt=tempItinryDescList.getAt(1)
			List<String> paxDescList=paxTxt.tokenize(".")

			String paxNum=paxDescList.getAt(0)
			resultData.hotel.removeItemPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlanMealBasistxt=paxDescList.getAt(1)
			resultData.hotel.removeItemPage.actualratePlan=ratePlanMealBasistxt.trim()

			//Free cancellation until date
			String completeTxt=getRoomDescPaxRatePlanFreeCnclTxtInCancelItemScrn()
			List<String> tempcompleteTxtList=completeTxt.tokenize(".")

			String freeCanclText=tempcompleteTxtList.getAt(2)
			println("Cancel Free Cancl Txt $freeCanclText")
			resultData.hotel.removeItemPage.actualFreeCnclTxt=freeCanclText.trim()

			//requested check in date and nights
			resultData.hotel.removeItemPage.actualDurationTxt=getItenaryDurationInSuggestedItem(4)

			//Inventory Availability
			resultData.hotel.removeItemPage.actualInvStatusDisplayed=getInvStatusInRemoveItemScrn()

			//Commission and percentage
			resultData.hotel.removeItemPage.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(4)

			//Room rate amount and currency
			//resultData.hotel.removeItemPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(4)
			resultData.hotel.removeItemPage.actualPriceAndcurrency=getCancelledItemAmount(0)+" GBP"
			//display function button No
			resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
			//display function button Yes
			resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

			//Click No
			clickNoOnRemoveItenary()
			sleep(3000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//select above confirmed (1st item) and click on remove button
			RemoveItenaryButton(0)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(6000)
			waitTillLoadingIconDisappears()

			//capture Item display status after removal
			resultData.hotel.removeItemPage.actualItemDispStatusAfetRemovalInNonBookedItms=getItemDispStatusInNonBookedItems(data.expected.cityAreaHotelText)

			//total amount for all item within the list
			resultData.hotel.removeItemPage.actualTotalLabelText=getTotalLabelTxtInNonBookedItemsScrn()
			resultData.hotel.removeItemPage.actualallItemsTotalAmountAndCurrency=getTotalAmountAndCurrencyInNonBookedItemsScrn()

			/*
			float modifiedPrice=Float.parseFloat(resultData.hotel.searchResults.itineraryBuilder.retrievedTotalPrice.toString())-Float.parseFloat(resultData.hotel.searchResults.priceText.toString())
			DecimalFormat df = new DecimalFormat("####.00");
			println("Total Price ----->"+df.format(modifiedPrice))

			resultData.hotel.removeItemPage.expectedallItemsTotalAmountAndCurrency=df.format(modifiedPrice)+" "+clientData.currency
			*/
			scrollToTopOfThePage()
		}


		//click on Book button
		clickOnBookIcon()
		sleep(3000)
		waitTillLoadingIconDisappears()
		sleep(7000)
		//capture booking screen display status
		resultData.hotel.itineraryPage.actualBookingScreenDispStatus=getAboutToBookScreendisplayStatus()

		//capture title text
		resultData.hotel.itineraryPage.actualtitleTxt=getCancellationHeader()

		//Capture Close Icon display status
		resultData.hotel.itineraryPage.actualCloseIconDispStatus=getCloseIconDisplayStatus()

		sleep(3000)

		if(data.input.noOfRooms>1 && (data.input.bookItemMultiRoom))
		{
			multiRoomBooking(clientData,data,resultData)
		}

		else if(data.input.noOfRooms>1 && (data.input.cancelMultiRoom)){

			multiRoomCancellation(clientData,data,resultData)
		}


		else{
			//Capture hotel name in About to Book Screen
			resultData.hotel.itineraryPage.actualHotelNameInAbtToBkScrn=getHotelNameInAboutToBookScrn()

			//Capture hotel desc
			//String tmphotelTxt=getTitleSectDescTxt(0)
			String tmphotelTxt=getItemDescPaxAndCityTxtInAboutToBookScreen()
			List<String> htlDesc=tmphotelTxt.tokenize(",")

			String hotelDescText=htlDesc.getAt(0)
			resultData.hotel.itineraryPage.actualHotelDescTxt=hotelDescText

			//resultData.hotel.itineraryPage.expectedHotelDescTxt="1x "+data.expected.roomDesc
			resultData.hotel.itineraryPage.expectedHotelDescText=data.expected.roomDesc
			//Capture PAX
			//String paxTxt=htlDesc.getAt(1)
			String paxTxt=getTitleSectDescTxt(3).trim().replaceAll("\n","")
			resultData.hotel.itineraryPage.actualPaxTxt=paxTxt.trim()

			//capture meal basis
			//resultData.hotel.itineraryPage.actualMealBasisTxt=getTitleSectDescTxt(1)
			resultData.hotel.itineraryPage.actualMealBasisTxt=htlDesc.getAt(1)

			//check in date, night

			if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"

			//resultData.hotel.itineraryPage.expectedChkInDateNight=dur
			resultData.hotel.itineraryPage.expectedChkInDateNight="Check in"+checkInDt
			//resultData.hotel.itineraryPage.actualcheckInNightTxt=getTitleSectDescTxt(2)
			resultData.hotel.itineraryPage.actualcheckInNightTxt=getTitleSectDescTxt(0).replaceAll("\n","")

			String actualNumOfNight=getTitleSectDescTxt(2).replaceAll("\n","")
			if (nights >1)
				dur= "Nights"+nights
			else dur = "Nights"+nights
			resultData.hotel.itineraryPage.actualNumOfNight=actualNumOfNight
			resultData.hotel.itineraryPage.expectedNumOfNight=dur

			//Room Rate and currency
			resultData.hotel.itineraryPage.actualPriceInAboutToBookScrn=getPriceInAbouttoBookScrn()

			//Book Page PAX Section

			//Title Text
			resultData.hotel.itineraryPage.actualPaxSecTitleTxt=getPaxSectionTitleTxt()

			//1st traveller
			resultData.hotel.itineraryPage.actualfirstTravellerTxt=getTravellerNameInAboutToBookScrn(0)
			resultData.hotel.itineraryPage.expectedfirstTravellerTxt=data.expected.firstName+" "+data.expected.lastName+" (Lead Name)"
			// tick box
			resultData.hotel.itineraryPage.actualfirstTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(1)

			//2nd traveller
			resultData.hotel.itineraryPage.actualsecondTravellerTxt=getTravellerNameInAboutToBookScrn(1)
			resultData.hotel.itineraryPage.expectedsecondTravellerTxt=scndTrvlrfirstName+" "+scndTrvlrlastName
			// tick box
			resultData.hotel.itineraryPage.actualsecondTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(2)

			if(data.input.children.size()>0){
				//3rd traveller
				resultData.hotel.itineraryPage.actualthirdTravellerTxt=getTravellerNameInAboutToBookScrn(2)
				resultData.hotel.itineraryPage.expectedthirdTravellerTxt=thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"
				// tick box - 3rd traveller
				resultData.hotel.itineraryPage.actualthirdTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(3)

				//4th traveller
				resultData.hotel.itineraryPage.actualfourthTravellerTxt=getTravellerNameInAboutToBookScrn(3)
				resultData.hotel.itineraryPage.expectedfourthTravellerTxt=fourthTrvlrfirstName+" "+fourthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
				// tick box - 4th traveller
				resultData.hotel.itineraryPage.actualfourthTravellerCheckBoxDispStatus=getCheckBoxDisplayStatus(4)
			}

			//Selecting travellers. Note: this step is not there in test case - but we cannot click on confirm booking without selecting travellers
			//Commenting since 10.3 by default travellers are selected.
			/*clickOnTravellerCheckBox(0)
			sleep(3000)
			clickOnTravellerCheckBox(1)
			sleep(3000)

			if(data.input.children.size()>0){
				clickOnTravellerCheckBox(2)
				sleep(3000)
				clickOnTravellerCheckBox(3)
				sleep(3000)
			}
			*/
			//Special condition

			//special condition - header text
			resultData.hotel.itineraryPage.actualSpecialConditionHeaderTxt=getOverlayHeadersTextInAboutToBookItems(0)
			resultData.hotel.itineraryPage.expectedSpecialConditionHeaderTxt=data.expected.spclCondtnTxt+checkInDt

			//Description text
			resultData.hotel.itineraryPage.actualDescText=getTextInAboutToBookItems(0)

			//Cancel charge - header text
			//resultData.hotel.itineraryPage.actualSpecialcancelchrgHeaderTxt=getOverlayHeadersTextInAboutToBookItems(1)
			resultData.hotel.itineraryPage.actualSpecialcancelchrgHeaderTxt=getOverlayHeadersTextInAboutToBookItemScrn(0)


			if(data.input.ammendmentCharge.toString().toBoolean())
			{
				//Amendment Title text, Name change, early Departure text
				String ammendmentTitleTxt
				String nameChangeTxt
				String ammendmentTxt
				String earlyDepTxt
				String nameChangeDate
				String ammendmentsDate
				try{
					ammendmentTitleTxt=getOverlayHeadersTextInAboutToBookItems(2)
					println("Ammendment Title : $ammendmentTitleTxt")
					sleep(2000)
				}catch(Exception e)
				{
					ammendmentTitleTxt="Unable To Read From UI"
				}
				try{
					nameChangeTxt=getAmmendmentChargeTextAndDateTxt(2,1,1)
					sleep(2000)
					println("Actual NameChangeText$nameChangeTxt")
				}
				catch(Exception e)
				{
					nameChangeTxt="Unable To Read From UI"
				}
				try{
					ammendmentTxt=getAmmendmentChargeTextAndDateTxt(2,1,2)
					sleep(2000)
					println("Actual Ammendment Text$ammendmentTxt")
				}catch(Exception e)
				{
					ammendmentTxt="Unable To Read From UI"

				}
				try{earlyDepTxt=getAmmendmentChargeTextAndDateTxt(2,1,3)
					sleep(2000)
					println("ActualEarly Dep Text$earlyDepTxt")
				}catch(Exception e)
				{
					earlyDepTxt="Unable To Read From UI"
				}
				try{
					nameChangeDate=getAmmendmentChargeTextAndDateTxt(2,2,1)
					sleep(2000)
					println("Actual Name Change Date$nameChangeDate")
				}catch(Exception e)
				{
					nameChangeDate="Unable To Read From UI"
				}
				try{
					ammendmentsDate=getAmmendmentChargeTextAndDateTxt(2,2,2)
					sleep(2000)
					println("Actual ammendments Date $ammendmentsDate")

				}
				catch(Exception e)
				{
					ammendmentsDate="Unable To Read From UI"
				}
				resultData.hotel.itineraryPage.actualAmmendmentTitletxt=ammendmentTitleTxt
				resultData.hotel.itineraryPage.actualnameChangeTxt=nameChangeTxt
				resultData.hotel.itineraryPage.actualammendmentTxt=ammendmentTxt
				resultData.hotel.itineraryPage.actualearlyDepTxt=earlyDepTxt
				resultData.hotel.itineraryPage.actualnameChangeDate=nameChangeDate

				String actualNameAndAmmendmentChangeDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.input.ammendmentOrNameChangeDays.toInteger(), "ddMMMyy")

				resultData.hotel.itineraryPage.expectedNameAndAmmendmentChangeDate="After "+actualNameAndAmmendmentChangeDate.toUpperCase()
				resultData.hotel.itineraryPage.actualammendmentsDate=ammendmentsDate
			}


			resultData.hotel.itineraryPage.actualCancellationChrgTxt=getCancellationChargeTxtInAboutToBookScrn()

			/*
			 //Date First Row
			 resultData.hotel.itineraryPage.actualDateFirstRow=getDateAmountCurrency(0)
			 resultData.hotel.itineraryPage.ExpectedDateFirstRow=cancelDate.toUpperCase()+" or earlier"
			 //Date Second Row
			 resultData.hotel.itineraryPage.actualDateSecondRow=getDateAmountCurrency(1)
			 resultData.hotel.itineraryPage.ExpectedDateSecondRow=checkInDt+" onwards"
			 //Amount First Row
			 resultData.hotel.itineraryPage.actualAmountFirstRow=getDateAmountCurrency(2)
			 println("$resultData.hotel.itineraryPage.actualAmountFirstRow")
			 //Amount Second Row
			 resultData.hotel.itineraryPage.actualAmountSecondRow=getDateAmountCurrency(3)
			 println("$resultData.hotel.itineraryPage.actualAmountSecondRow")
			 float fpercentage = Float.parseFloat(data.expected.cancel_percentage)
			 float Amount=Float.parseFloat(resultData.hotel.searchResults.priceText.toString())
			 String percentValue=getCommissionPercentageValue(Amount,fpercentage)
			 resultData.hotel.itineraryPage.expectedPercentValue=percentValue+clientData.currency
			 */
			//Ammendment Charge
			resultData.hotel.itineraryPage.actualAmmendmentCharge=getTextInAboutToBookItems(1)

			//General Terms & Conditions Text
			resultData.hotel.itineraryPage.actualTermsAndConditionsText=getTextInAboutToBookItems(2)

			//Terms & Condtions Link
			resultData.hotel.itineraryPage.actualTAndCDisplayStatus=getTAndCLinkInAboutToBookScreendisplayStatus()

			//By Click on T & c
			//String ByclickonTAndC=getByClickingFooterTxt(0)
			String ByclickonTAndC=getByClickingFooterTxt()
			resultData.hotel.itineraryPage.actualTermsAndCondtTxt=ByclickonTAndC.replace("\n", "")
			//Add Comment or Remark Text
			resultData.hotel.itineraryPage.actualSpecialRemarkOrCommentTxt=getSpecialRemarkOrCommentTxt()
			scrollToRemarksTxt()

			//click on downside arrow
			clickExpandRemarksTandC()
			sleep(2000)
			//capture Expand status
			resultData.hotel.itineraryPage.actualExpandedStatus=getExpandOrCollapseItemDisplayStatus()
			//click on upside arrow
			clickExpandRemarksTandC()
			sleep(2000)
			//capture Collapsed status
			resultData.hotel.itineraryPage.actualCollapsedStatus=getExpandOrCollapseItemDisplayStatus()
			//click on downside arrow
			clickExpandRemarksTandC()
			sleep(4000)

			//title text, please note text
			resultData.hotel.itineraryPage.actualPleaseNoteTxt=getPleaseNotOrProvideTxt(0)

			//Will arrive without voucher check box display status
			resultData.hotel.itineraryPage.actualwilArrivWithoutVochrchkBoxDispStatus=getWilArrivWithOutVochrCheckBoxDisplayStatus()

			//Will arrive without voucher text
			resultData.hotel.itineraryPage.actualwilArrivWithoutVochrTxt=getCheckBoxTextInAddSpecialRemarkSection(0)

			//Previous night is booked for early morning arrival display status
			//resultData.hotel.itineraryPage.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus(0)

			//Previous night is booked for early morning arrival text
			resultData.hotel.itineraryPage.actualPreviousNightTxt=getCheckBoxTextInAddSpecialRemarkSection(1)

			//Late check out check box display status
			//resultData.hotel.itineraryPage.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus(0)

			//Late check out Text
			resultData.hotel.itineraryPage.actualLateCheckOutTxt=getCheckBoxTextInAddSpecialRemarkSection(2)

			//Late Arrival check box display status
			//resultData.hotel.itineraryPage.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus(0)

			//Late Arrival text box
			resultData.hotel.itineraryPage.actualLateArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(3)

			//Passengers Are HoneyMooners check box Display Status
			//resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus(0)

			//Passengers Are HoneyMooners Text
			resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersTxt=getCheckBoxTextInAddSpecialRemarkSection(4)

			//Early Arrival Disp Status
			//resultData.hotel.itineraryPage.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus(0)

			//Early Arrival Text
			resultData.hotel.itineraryPage.actualEarlyArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(5)

			//tick the check box Will arrive without voucher
			clickWilArrivWithOutVochrCheckBox()
			sleep(2000)

			//titleText - if possible please provide
			resultData.hotel.itineraryPage.actualIfPsblePlzProvTxt=getPleaseNotOrProvideTxt(1)

			//Twin Room check box display status
			resultData.hotel.itineraryPage.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus(0)

			//Twin Room Text Box
			resultData.hotel.itineraryPage.actualTwinRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(7)

			//Smoking Room Check Box display status
			//resultData.hotel.itineraryPage.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus(0)
			//Smoking Room Check Box Text
			resultData.hotel.itineraryPage.actualSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(8)

			//Inter Connecting Rooms check box display status
			//resultData.hotel.itineraryPage.actualInterConnectingRoomCheckboxDispStatus=getInterConnectingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualInterConnectingRoomCheckboxDispStatus=getICRoomCheckBoxDisplayStatus(0)
			//Inter connecting Rooms text
			resultData.hotel.itineraryPage.actualIntrConnectngRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(9)

			//Adjoining Rooms check box display status
			//resultData.hotel.itineraryPage.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus(0)
			//Adjoining Rooms check box text
			resultData.hotel.itineraryPage.actualAdjoiningRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(10)

			//Quiet Rooms check box display status
			//resultData.hotel.itineraryPage.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus(0)
			//Quiet Rooms check box text
			resultData.hotel.itineraryPage.actualQuietRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(11)

			//Non-Smoking Rooms check box display status
			//resultData.hotel.itineraryPage.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus(0)
			//Non-Smoking Rooms text
			resultData.hotel.itineraryPage.actualNonSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(12)

			//Room on lowest floor display status
			//resultData.hotel.itineraryPage.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus(0)
			//Room on lowest floor text
			resultData.hotel.itineraryPage.actualRoomOnLowestFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(13)

			//Room on high floor checkbox display status
			//resultData.hotel.itineraryPage.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus(0)
			//Room on high floor text
			resultData.hotel.itineraryPage.actualRoomOnHighFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(14)

			//Double Room check box display status
			//resultData.hotel.itineraryPage.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus(0)
			//Double Room text
			resultData.hotel.itineraryPage.actualDoubleRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(15)

			//Room with bathtub
			//resultData.hotel.itineraryPage.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus(0)
			//Room with bathtub
			resultData.hotel.itineraryPage.actualRoomwithBathTubtxt=getCheckBoxTextInAddSpecialRemarkSection(16)

			//Arrival Flight Number check box display status
			//resultData.hotel.itineraryPage.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus(0)
			//Arrival Flight Number text
			resultData.hotel.itineraryPage.actualArrivalFlightNumtxt=getCheckBoxTextInAddSpecialRemarkSection(6)

			//Arrival Flight Number text box existence
			//resultData.hotel.itineraryPage.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus()
			resultData.hotel.itineraryPage.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus(0)

			//at Hrs
			//at text
			resultData.hotel.itineraryPage.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(1)
			//Hrs text
			resultData.hotel.itineraryPage.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(2)
			//Hrs select drop box display status
			resultData.hotel.itineraryPage.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus()

			//mins
			//mins text
			resultData.hotel.itineraryPage.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(3)
			//mins select drop box display status
			resultData.hotel.itineraryPage.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus()
			//On Date
			//on text
			resultData.hotel.itineraryPage.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(4)
			//on Day select drop box display status
			resultData.hotel.itineraryPage.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus()
			//on Month select drop box display status
			resultData.hotel.itineraryPage.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus()

			//Book Page Footer

			//Total
			resultData.hotel.itineraryPage.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)
			//Total Amount and currency
			resultData.hotel.itineraryPage.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)

			//Your Commission text
			resultData.hotel.itineraryPage.actualCommissionTextInAboutToBook=getTotalAndCommissionText(2)
			//Your commission amount and currency
			//resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getCommissionValueAndCurrencyInAboutToBookScrn(1)
			resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getTotalAndCommissionText(3)
			float compercentage = Float.parseFloat(data.expected.commissionPercent)
			float comAmount=Float.parseFloat(resultData.hotel.searchResults.priceText.toString().replaceAll(",",""))
			String comValue=getCommissionPercentageValue(comAmount,compercentage)
			resultData.hotel.itineraryPage.expectedComValue=comValue+" "+clientData.currency

			//Click on Confirm Booking
			//clickConfirmBooking()
			clickOnConfirmBookingAndPayNow()

			//wait for confirmation page
			waitTillConformationPage()
			sleep(5000)

			waitForAjaxIconToDisappear()
			scrollToTopOfThePage()
			//Booking Confirmation Screen Display Status
			resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus=getBookingConfirmationScreenDisplayStatus()

			//Booking confirmed lightbox displayed

			//Title text-Booking Confirmed
			resultData.hotel.itineraryPage.actualBookingConfirmedTitleText=getCancellationHeader()

			//Header Section
			try{
				resultData.hotel.itineraryPage.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
			}catch(Exception e){
				resultData.hotel.itineraryPage.actualHeaderSectionText="Unable To Read Header Section Text in Booking conf screen UI"
			}

			String emailId=clientData.usernameOrEmail
			//emailId=emailId.toUpperCase()
			//resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
			resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt

			//read Booking id
			resultData.hotel.itineraryPage.retrievedbookingID=getBookingIdFromBookingConfirmed(0)
			println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")

			//read Itineary Reference Number & name
			resultData.hotel.itineraryPage.actualreaditinearyIDAndName=getItinearyID(0)
			resultData.hotel.itineraryPage.expecteditinearyIDAndName=resultData.hotel.searchResults.itineraryBuilder.retitineraryId+resultData.hotel.itineraryPage.expectedItnrName

			//Capture Departure Date

			resultData.hotel.confirmationPage.actualConfimrationDialogDepDate=getBookingDepartDate()
			resultData.hotel.confirmationPage.expectedConfimrationDialogDepDate=depDate.toUpperCase()

			//capture traveller details
			resultData.hotel.confirmationPage.actualfirstTravellerName=getTravellerNamesConfirmationDialog(0)
			resultData.hotel.confirmationPage.actualsecondTravellerName=getTravellerNamesConfirmationDialog(1)

			if(data.input.children.size()>0){
				resultData.hotel.confirmationPage.actualthirdTravellerName=getTravellerNamesConfirmationDialog(2)
				resultData.hotel.confirmationPage.actualfourthTravellerName=getTravellerNamesConfirmationDialog(3)
			}
			//Hotel Name
			resultData.hotel.confirmationPage.actualHotelName=getConfirmBookedTransferName()

			//Hotel Address
			resultData.hotel.confirmationPage.actualHotelAddressTxt=getTransferDescBookingConfirmed()

			//number of room/name of room (descripton)
			String roomAndPax=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(0)
			htlDesc=roomAndPax.tokenize(",")

			hotelDescText=htlDesc.getAt(0)
			resultData.hotel.confirmationPage.actualHotelDescTxt=hotelDescText
			resultData.hotel.confirmationPage.expHotelDescTxt="1x "+data.expected.roomDesc
			//Capture PAX
			paxTxt=htlDesc.getAt(1)
			resultData.hotel.confirmationPage.actualPaxTxt=paxTxt.trim()

			//Meal Basis
			resultData.hotel.confirmationPage.actualMealBasisTxt=getRoomDescPaxInfoMealBasisInBookingConfirmedScrn(1)

			//Check in date, Number of Nights
			resultData.hotel.confirmationPage.actualCheckInDateNumOfNights=getCheckInDateNumOfNightsBookingConfirmed()

			if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"

			resultData.hotel.confirmationPage.expectedCheckInDateNumOfNights=dur
			//Please Note txt
			resultData.hotel.confirmationPage.actualPlzNoteTxtInBookingConfScrn=getPleaseNoteTxtInBookingConfirmedScrn()

			//Remarks
			resultData.hotel.confirmationPage.expectedRemarksTxtInBookingConfScrn=getRemarksTxtInBookingConfirmedScrn(2)
			//Room Rate Amount and currency
			resultData.hotel.confirmationPage.actualRoomRateAmountAndCurrency=getPriceBookingConfirmation()
			resultData.hotel.confirmationPage.expectedRoomRateAmountCurency=resultData.hotel.searchResults.expectedPrice.replaceAll(" ", "")

			//Commission Amount and Currency
			resultData.hotel.confirmationPage.actualCommissionAmountAndCurrency=getCommisionTextAmountAndCurrency()
			resultData.hotel.confirmationPage.expectedCommissionAmountAndCurrency=data.expected.comTxt+resultData.hotel.itineraryPage.expectedComValue.replaceAll(" ", "")

			//Close lightbox X function
			resultData.hotel.confirmationPage.actualCloseLightboxDispStatus=getCloseIconDisplayStatus()

			//click on Close lightbox X function
			coseBookItenary()
			sleep(7000)

			//scrollToBottomOfThePage()
			//Get Header Text
			//resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInBookedItemsListScrn(1)
			//resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInBookedItemsListScrn(2)
			resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInBookedItemsListScrn(0)
			//Get Booing ID and number
			resultData.hotel.itineraryPage.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

			//Confirmed tab
			resultData.hotel.itineraryPage.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus()
			//confirm tab text
			resultData.hotel.itineraryPage.actualStatusTabDispStatus=getStatusInBookedItemsScrn()

			if((data.input.bookItem.toString().toBoolean()) || (data.input.addItems.toString().toBoolean()))
			{
				//Amend tab
				resultData.hotel.itineraryPage.actualAmendTabDispStatus=getAmendTabDisplayStatus()
				//Amend tab text
				resultData.hotel.itineraryPage.actualAmendTabTxt=getTabTxtInBookedItemsScrn(0)
				//Cancel tab
				resultData.hotel.itineraryPage.actualCancelTabDispStatus=getCancelTabDisplayStatus()
				//Cancel tab text
				resultData.hotel.itineraryPage.actualCancelTabTxt=getTabTxtInBookedItemsScrn(1)
			}
			else{

				//Amend tab
				resultData.hotel.itineraryPage.actualAmendTabDispStatus=getAmendTabDisplayStatusInBookedItmsSec()
				//Amend tab text
				resultData.hotel.itineraryPage.actualAmendTabTxt=getAmmendTabTxtInBookedItemsScrn()
				//Cancel tab
				resultData.hotel.itineraryPage.actualCancelTabDispStatus=getCancelTabDisplayStatusInBookedItmsScrn()
				//Cancel tab text
				resultData.hotel.itineraryPage.actualCancelTabTxt=getTabTxtInBookedItemsSection(0)
			}

			//Hotel Image Display status
			resultData.hotel.itineraryPage.actualHotelImageDispStatus=getHotelImageDisplayStatus()

			//Capture Image Src URL
			resultData.hotel.itineraryPage.actualImageSrcURLinBookedItem=getImageSrcURLInBookedItem(0)
			println("$resultData.hotel.itineraryPage.actualImageSrcURLinBookedItem")

			//Hotel Name
			resultData.hotel.itineraryPage.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

			//Caputre Hotel name Src URL
			resultData.hotel.itineraryPage.actualHotelNameLinkTxt=getHotelNameSrcURLInSuggestedItem(0)
			println("$resultData.hotel.itineraryPage.actualHotelNameLinkTxt")

			//Hotel Star Rating
			resultData.hotel.itineraryPage.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(0)

			//room description

			String descComplTxt=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tmpItineraryDescList=descComplTxt.tokenize(",")

			String descText=tmpItineraryDescList.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxtInBookedItmsScrn=descText.trim()
			//Pax number requested
			String tempTxt=tmpItineraryDescList.getAt(1)
			List<String> tmpDescList=tempTxt.tokenize(".")

			String paxNumDetails=tmpDescList.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()

			//Rate plan - meal basis
			String ratePlantxtDetails=tmpDescList.getAt(1)
			resultData.hotel.itineraryPage.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()
			//Free cancellation until date
			resultData.hotel.itineraryPage.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(0)

			//scrollToTopOfThePage()
			//scrollToBottomOfThePage()
			if(data.input.cancelItem.toString().toBoolean())
			{
				//Inventory Status
				String invStatus
				try{
					invStatus=getRoomStatusfromSuggestedItem(0)
					println("Ammendment Title : $invStatus")
					sleep(2000)
				}catch(Exception e)
				{
					invStatus="Unable To Read From UI"
				}
				resultData.hotel.itineraryPage.actualInventoryStatusInBookedItems=invStatus
			}
			//Requested Check in and nights
			resultData.hotel.itineraryPage.actualDurationTxtInBookedItmsScrn=getItenaryDurationInSuggestedItem(0)

			//commission and percentage
			resultData.hotel.itineraryPage.actualCommissionAndPercentatgeInBookedItmsScrn=getCommisionAndPercentageInBookeddetailsScrn(0)

			//Room rate amound and currency
			//resultData.hotel.itineraryPage.actualPriceAndcurrencyInBookedItmsScrn=getItenaryPriceInSuggestedItem(0)
			resultData.hotel.itineraryPage.actualPriceAndcurrencyInBookedItmsScrn=getSuggestedItemsItenaryPrice(0)


		}
		if(data.input.bookItem.toString().toBoolean())
		{

			//Add a remark or content
			resultData.hotel.itineraryPage.actualRemarksInBookedItmsScrn=getRemarksInBookeddetailsScrn().replaceAll(":","")
			resultData.hotel.itineraryPage.expectedRemarksInBookedItmsScrn=data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
			resultData.hotel.itineraryPage.expectedRemarksInBookedItmsScrn=resultData.hotel.itineraryPage.expectedRemarksInBookedItmsScrn.replaceAll(":","")

			//Edit Icon display status
			resultData.hotel.itineraryPage.actualEditIconInBookedDetailScrnDispStatus=getEditIconInBookedDetailsScrnDisplayStatus()
			sleep(2000)
			scrollToTopOfThePage()
			//click on Edit icon
			clickEditRemarksButton(0)
			waitTillLoadingIconDisappears()
			sleep(7000)
			//Add comment or special request lighbox display
			resultData.hotel.itineraryPage.actualHeaderTxtInAddCmntOrSpeclReqScrnDispStatus=getAddCmntOrSpclReqDisplayStatus()

			//Add comment or special request lighbox text
			resultData.hotel.itineraryPage.actualHeaderTxtInAddCmntOrSpeclReqScrn=getBookingStatusOnLightBox()

			//Select from the choices below or write your own request - text
			resultData.hotel.itineraryPage.actualHeaderTxtSelChoicesBelowInAddCmntOrSpeclReqScrn=getTravellerCannotBeDeletedheaderText().trim()

			//Please Note: text
			resultData.hotel.itineraryPage.actualHeaderTxtPlzNoteInAddcmntOrSclReqScrn=getHeaderTxtInAddCmntrSpclReqScrn(0)

			//Will arrive without voucher- Should display with box ticked.
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualwilArrivWithoutVochrCheckBoxStatus=getWilArrivWithOutVochrCheckBoxCheckedStatus()
			//Will arrive without voucher text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualwilArrivWithoutVochrTxt=getCheckBoxTextInAddSpecialRemarkSection(0)

			//Click
			//resultData.hotel.itineraryPage.addComntOrSpclReq.actualCheckBoxTickStatusAfterClick=clickWilArrivWithOutVochrCheckBox()
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualCheckBoxTickStatusAfterClick=getWilArrivWithOutVochrCheckBoxCheckedStatus()
			//Previous night is booked for early morning arrival display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus()

			//Previous night is booked for early morning arrival text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPreviousNightTxt=getCheckBoxTextInAddSpecialRemarkSection(1)

			//Late check out check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus()

			//Late check out Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateCheckOutTxt=getCheckBoxTextInAddSpecialRemarkSection(2)

			//Late Arrival check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus()

			//Late Arrival text box
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(3)


			//Passengers Are HoneyMooners check box Display Status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus()

			//Passengers Are HoneyMooners Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualPassengersAreHoneyMoonersTxt=getCheckBoxTextInAddSpecialRemarkSection(4)

			//Early Arrival Disp Status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus()

			//Early Arrival Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualEarlyArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(5)

			//If possible, please provide: text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualHeaderTxtIfPssblPlzProvideTxt=getHeaderTxtInAddCmntrSpclReqScrn(1)

			//Twin Room check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus()

			//Twin Room Text Box
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualTwinRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(7)

			//Smoking Room Check Box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus()
			//Smoking Room Check Box Text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(8)

			//Inter Connecting Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualInterConnectingRoomCheckboxDispStatus=getInterConnectingRoomCheckBoxDisplayStatus()
			//Inter connecting Rooms text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualIntrConnectngRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(9)

			//Adjoining Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus()
			//Adjoining Rooms check box text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualAdjoiningRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(10)


			//Quiet Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus()
			//Quiet Rooms check box text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualQuietRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(11)

			//Non-Smoking Rooms check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus()
			//Non-Smoking Rooms text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualNonSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(12)


			//Room on lowest floor display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus()
			//Room on lowest floor text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnLowestFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(13)


			//Room on high floor checkbox display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus()
			//Room on high floor text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnHighFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(14)

			//Double Room check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus()
			//Double Room text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualDoubleRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(15)

			//Room with bathtub
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus()
			//Room with bathtub
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomwithBathTubtxt=getCheckBoxTextInAddSpecialRemarkSection(16)

			//Arrival Flight Number check box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus()
			//Arrival Flight Number text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumtxt=getCheckBoxTextInAddSpecialRemarkSection(6)

			//Arrival Flight Number text box existence
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus()

			//Hrs
			//at text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(1)
			//Hrs text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(2)
			//Hrs select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus()


			//Mins
			//mins text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(3)
			//mins select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus()

			//On Date
			//on text
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(4)
			//on Day select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus()
			//on Month select drop box display status
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus()

			//Click Smoking Room Check Box
			clickSmokingRoomCheckBox()
			sleep(3000)

			//capture Smoking Room check box ticked or not
			resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingRoomCheckBoxCheckedStatus=getSmokingRoomCheckBoxCheckedStatus()

			//Click on Save button
			scrollAndclickOnSaveBtn()
			sleep(5000)
			waitTillLoadingIconDisappears()
			//Confirmed booking after Edit remarks

			//Hotel Image Display status
			resultData.hotel.itineraryPage.actualHotelImageDispStatusInConfBkngAftrEdtRmrks=getHotelImageDisplayStatus()

			//Capture Image Src URL
			resultData.hotel.itineraryPage.actualImageSrcURLinBookedItemInConfBkngAftrEdtRmrks=getImageSrcURLInBookedItem(0)
			println("$resultData.hotel.itineraryPage.actualImageSrcURLinBookedItemInConfBkngAftrEdtRmrks")

			//Hotel Name
			resultData.hotel.itineraryPage.actualHotelNameTxtInConfBkngAftrEdtRmrks=getTransferNameInSuggestedItem(0)

			//Caputre Hotel name Src URL
			resultData.hotel.itineraryPage.actualHotelNameLinkTxtInConfBkngAftrEdtRmrks=getHotelNameSrcURLInSuggestedItem(0)
			println("$resultData.hotel.itineraryPage.actualHotelNameLinkTxtInConfBkngAftrEdtRmrks")

			//Hotel Star Rating
			resultData.hotel.itineraryPage.actualStarRatingInBookedHotelItemInConfBkngAftrEdtRmrks=getRatingForTheHotelInSuggestedItem(0)

			//room description

			String complTxt=getItinenaryDescreptionInSuggestedItem(0)
			List<String> itineraryDescList=complTxt.tokenize(",")

			String descTxt=itineraryDescList.getAt(0)
			resultData.hotel.itineraryPage.actualroomdescTxtInBookedItmsScrnInConfBkngAftrEdtRmrks=descTxt.trim()
			//Pax number requested
			String tmpText=itineraryDescList.getAt(1)
			List<String> descList=tmpText.tokenize(".")

			String paxNo=descList.getAt(0)
			resultData.hotel.itineraryPage.actualPaxNumInBookedItmsScrnInConfBkngAftrEdtRmrks=paxNo.trim()

			//Rate plan - meal basis
			String ratePlanDetails=descList.getAt(1)
			resultData.hotel.itineraryPage.actualratePlanInBookedItmsScrnInConfBkngAftrEdtRmrks=ratePlanDetails.trim()

			//Free cancellation until date
			resultData.hotel.itineraryPage.actualFreeCnclTxtInConfBkngAftrEdtRmrks=getItinenaryFreeCnclTxtInSuggestedItem(0)


			//Requested Check in and nights
			resultData.hotel.itineraryPage.actualDurationTxtInConfBkngAftrEdtRmrks=getItenaryDurationInSuggestedItem(0)

			//commission and percentage
			resultData.hotel.itineraryPage.actualCommissionAndPercentatgeInConfBkngAftrEdtRmrks=getCommisionAndPercentageInBookeddetailsScrn(0)

			//Room rate amound and currency
			resultData.hotel.itineraryPage.actualPriceAndcurrencyInConfBkngAftrEdtRmrks=getItenaryPriceInSuggestedItem(0)




		}

		if(data.input.cancelItem.toString().toBoolean())
		{
			sleep(3000)
			scrollToBottomOfThePage()

			//select above confirmed (1st item) and click on Cancel tab
			clickOnCancelOrAmendTabButton(1)

			//Cancel item lightbox display status
			resultData.hotel.removeItemPage.actualCancelItemDispStatus=getCancelItemDisplayStatus()

			//lightbox - Title text , Text - Cancel item
			resultData.hotel.removeItemPage.actualCancelItemTxt=getCancellationHeader()

			//Close lightbox X function
			resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()

			//hotel image link
			resultData.hotel.removeItemPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(1)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.actualImageURL=getImageSrcURLInSuggestedItem(1)

			//hotel name
			resultData.hotel.removeItemPage.actualHotelNameTxt=getTransferNameInSuggestedItem(1)

			//hotel  link
			resultData.hotel.removeItemPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(2)

			//hotel star rating
			resultData.hotel.removeItemPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(1)

			//Room - description
			String roomdescComplText=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItinryDescList=roomdescComplText.tokenize(",")

			String roomDscText=tempItinryDescList.getAt(0)
			resultData.hotel.removeItemPage.actualroomdescTxt=roomDscText.trim()
			//Pax number requested
			String parTxt=tempItinryDescList.getAt(1)
			List<String> parDescList=parTxt.tokenize(".")

			String paxNum=parDescList.getAt(0)
			resultData.hotel.removeItemPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlanMealBasistxt=parDescList.getAt(1)
			resultData.hotel.removeItemPage.actualratePlan=ratePlanMealBasistxt.trim()

			//Free cancellation until date
			String completeTxt=getRoomDescPaxRatePlanFreeCnclTxtInCancelItemScrn()
			List<String> tempcompleteTxtList=completeTxt.tokenize(".")

			String freeCanclText=tempcompleteTxtList.getAt(2)
			println("Cancel Free Cancl Txt $freeCanclText")
			resultData.hotel.removeItemPage.actualFreeCnclTxt=freeCanclText.trim()

			//requested check in date and nights
			resultData.hotel.removeItemPage.actualDurationTxt=getItenaryDurationInSuggestedItem(1)

			//Commission and percentage
			resultData.hotel.removeItemPage.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(1)

			//Room rate amount and currency
			//resultData.hotel.removeItemPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(1)
			resultData.hotel.removeItemPage.actualPriceAndcurrency=getCancelledItemAmount(0)+" GBP"
			//display function button No
			resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
			//display function button Yes
			resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

			//Click No
			clickNoOnRemoveItenary()
			sleep(3000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//select above confirmed (1st item) and click on Cancel tab
			clickOnCancelOrAmendTabButton(1)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(6000)

			//title text - Unavailable and Cancelled items
			resultData.hotel.removeItemPage.cancelledItems.actualCancelledItmsTxt=getHeaderTxtInBookedItemsListScrn(2)

			//Get Booing ID and number
			resultData.hotel.removeItemPage.cancelledItems.actualBookingIDinBookedDetailsScrn="Booking ID: "+getBookingIDinBookeddetailsScrn()

			//Status - cancelled - text
			//Commented since status is not shown after booking in 10.3
			//resultData.hotel.removeItemPage.cancelledItems.actualStatus=getStatusDisplayed().trim()


			//hotel image link
			resultData.hotel.removeItemPage.cancelledItems.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(0)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.cancelledItems.actualImageURL=getImageSrcURLInSuggestedItem(0)

			//hotel name
			resultData.hotel.removeItemPage.cancelledItems.actualHotelNameTxt=getTransferNameInSuggestedItem(0)

			//hotel  link
			resultData.hotel.removeItemPage.cancelledItems.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(0)

			//hotel star rating
			resultData.hotel.removeItemPage.cancelledItems.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(0)

			//Room - description
			String roomdescCanclText=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItinryCanclDescList=roomdescCanclText.tokenize(",")

			String roomDscCanclText=tempItinryCanclDescList.getAt(0)
			resultData.hotel.removeItemPage.cancelledItems.actualroomdescTxt=roomDscCanclText.trim()
			//Pax number requested
			String paxCanTxt=tempItinryCanclDescList.getAt(1)
			List<String> paXCanDescList=paxCanTxt.tokenize(".")

			String CancpaxNum=paXCanDescList.getAt(0)
			resultData.hotel.removeItemPage.cancelledItems.actualPaxNum=CancpaxNum.trim()

			//Rate plan - meal basis
			String canclratePlanMealBasistxt=paXCanDescList.getAt(1)
			resultData.hotel.removeItemPage.cancelledItems.actualratePlan=canclratePlanMealBasistxt.trim()

			//Free cancellation until date
			resultData.hotel.removeItemPage.cancelledItems.actualFreeCnclTxt=getItinenaryFreeCnclTxtInSuggestedItem(0)

			//requested check in date and nights
			resultData.hotel.removeItemPage.cancelledItems.actualDurationTxt=getItenaryDurationInSuggestedItem(0)

			//Commission and percentage
			resultData.hotel.removeItemPage.cancelledItems.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(0)

			//Room rate amount and currency
			//resultData.hotel.removeItemPage.cancelledItems.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(0)
			resultData.hotel.removeItemPage.cancelledItems.actualPriceAndcurrency=getSuggestedItemsItenaryPrice(0)

			//Travellers
			resultData.hotel.removeItemPage.cancelledItems.actualTravellerDetailsTxt=getTravellernamesCancelledBookedItem(0)
			resultData.hotel.removeItemPage.cancelledItems.expectedTravellerDetailsTxt="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+scndTrvlrfirstName+" "+scndTrvlrlastName


		}



		if(data.input.unavailableItem.toString().toBoolean())
		{
			sleep(3000)
			scrollToBottomOfThePage()

			//CLICK ON REMOVE TAB
			clickOnCancelOrAmendTabButton(1)

			sleep(3000)

			//Remove item lightbox display status
			resultData.hotel.removeItemPage.actualremoveItemLightBoxDispStatus=getCancelItemDisplayStatus()

			//lightbox - Title text , Text - Remove item
			resultData.hotel.removeItemPage.actualremoveItemTxt=getCancellationHeader()

			//Close lightbox X function
			resultData.hotel.removeItemPage.actualClosebuttonDispStatus=overlayCloseButton()

			//text - Are you sure you want to remove the following item form this itinerary?
			resultData.hotel.removeItemPage.actualremoveHeaderTxt=getTravellerCannotBeDeletedheaderText()

			//hotel image link
			resultData.hotel.removeItemPage.actualImageIconLinkExistence=getImageIconLinkExistenceInSuggestedItem(1)

			//capture hotel image link URL
			resultData.hotel.removeItemPage.actualImageURL=getImageSrcURLInSuggestedItem(1)

			//hotel name
			resultData.hotel.removeItemPage.actualHotelNameTxt=getTransferNameInSuggestedItem(1)

			//hotel  link
			resultData.hotel.removeItemPage.actualHotelNameURL=getHotelNameSrcURLInSuggestedItem(2)

			//hotel star rating
			resultData.hotel.removeItemPage.actualHotelStarRating=getRatingForTheHotelInSuggestedItem(1)

			//Room - description
			String roomdescComplText=getItinenaryDescreptionInSuggestedItem(0)
			List<String> tempItinryDescList=roomdescComplText.tokenize(",")

			String roomDscText=tempItinryDescList.getAt(0)
			resultData.hotel.removeItemPage.actualroomdescTxt=roomDscText.trim()
			//Pax number requested
			String parTxt=tempItinryDescList.getAt(1)
			List<String> parDescList=parTxt.tokenize(".")

			String paxNum=parDescList.getAt(0)
			resultData.hotel.removeItemPage.actualPaxNum=paxNum.trim()

			//Rate plan - meal basis
			String ratePlanMealBasistxt=parDescList.getAt(1)
			resultData.hotel.removeItemPage.actualratePlan=ratePlanMealBasistxt.trim()

			//Free cancellation until date
			String completeTxt=getRoomDescPaxRatePlanFreeCnclTxtInCancelItemScrn()
			List<String> tempcompleteTxtList=completeTxt.tokenize(".")
			String freeCanclText
			try{
				freeCanclText=tempcompleteTxtList.getAt(2)
				println("Cancel Free Cancl Txt $freeCanclText")
				resultData.hotel.removeItemPage.actualFreeCnclTxt=freeCanclText.trim()
			}catch(Exception e){
				resultData.hotel.removeItemPage.actualFreeCnclTxt="Unable To Read From UI"
			}



			//requested check in date and nights
			resultData.hotel.removeItemPage.actualDurationTxt=getItenaryDurationInSuggestedItem(1)

			//Commission and percentage
			resultData.hotel.removeItemPage.actualcomPercentTxt=getCommisionAndPercentageInBookeddetailsScrn(1)

			try{
				//Room rate amount and currency
				resultData.hotel.removeItemPage.actualPriceAndcurrency=getItenaryPriceInSuggestedItem(1)

			}catch(Exception e){
				//Room rate amount and currency
				resultData.hotel.removeItemPage.actualPriceAndcurrency="Unable To Read Text From UI"

			}

			//display function button No
			resultData.hotel.removeItemPage.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
			//display function button Yes
			resultData.hotel.removeItemPage.actualYesButtonDispStatus=getYesNoDisplayStatus(2)

			//Click No
			clickNoOnRemoveItenary()
			sleep(3000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//select above confirmed (1st item) and click on Cancel tab
			clickOnCancelOrAmendTabButton(1)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(6000)

			//remove item box disappear
			resultData.hotel.removeItemPage.actualremoveItemDispStatus=getCancelItemDisplayStatus()

			//Should not have suggest item list displayed
			//resultData.hotel.removeItemPage.actualUnavailableAndCancelItemSecDispStatus=getUnavailableAndCancelItemDisplayStatus()
		}


		if(!(data.input.unavailableItem.toString().toBoolean()))
		{
			//Add a remark or content
			resultData.hotel.itineraryPage.actualRemarksInConfBkngAftrEdtRmrks=getRemarksInBookeddetailsScrn().replaceAll(":","")

			if(data.input.children.size()>0){
				resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=data.expected.IfPsblePlzProvTxt+" "+data.expected.chkBoxTxt_8+" "+data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
				resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks.replaceAll(":","")
			}
			else
				resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
			    resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks=resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks.replaceAll(":","")
		}

		if(data.input.addItems.toString().toBoolean())
		{
			scrollToBottomOfThePage()
			//click on Book button
			clickOnBookIcon()
			sleep(5000)

			//Selecting travellers. Note: this step is not there in test case - but we cannot click on confirm booking without selecting travellers
			clickOnTravellerCheckBox(0)
			sleep(3000)
			clickOnTravellerCheckBox(1)
			sleep(3000)
			scrollToRemarksTxt()

			//click on downside arrow
			clickExpandRemarksTandC()
			sleep(2000)

			//tick the check box Will arrive without voucher
			clickWilArrivWithOutVochrCheckBox()
			sleep(2000)

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
			//select above confirmed (1st item) and click on Cancel tab
			clickOnCancelOrAmendTabButton(3)
			sleep(3000)
			//click Yes
			clickYesOnRemoveItenary()
			sleep(7000)
			waitForAjaxIconToDisappear()

			//click on Book button
			clickOnBookIcon()
			sleep(5000)

			//Selecting travellers. Note: this step is not there in test case - but we cannot click on confirm booking without selecting travellers
			clickOnTravellerCheckBox(0)
			sleep(3000)
			clickOnTravellerCheckBox(1)
			sleep(3000)

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

			//select above confirmed (1st item) and click on Cancel tab
			//clickOnCancelOrAmendTabButton(3)
			clickOnCancelOrAmendTabButton(2)
			sleep(3000)
			//click Yes
			//clickYesOnRemoveItenary()
			clickNoOnRemoveItenary()
			sleep(6000)

			//Capture Booked Items title text
			//resultData.hotel.removeItemPage.actualBookedItemsHeaderTxt=getHeaderTxtInBookedItemsListScrn(2)
			resultData.hotel.removeItemPage.actualBookedItemsHeaderTxt=getHeaderTxtInBookedItemsListScrn(0)

			//Capture no. of items under Booked Items
			resultData.hotel.removeItemPage.actualBookedItemsCount=getNoOfItemsInBookedItemsSection()
			//Total label text
			resultData.hotel.removeItemPage.actualTotalLabelTextInBookedItemsSec=getTotalLabelTxtInNonBookedItemsScrn()

			//Total / Room rate amount and currency
			resultData.hotel.removeItemPage.actualallItemsTotalAmountAndCurrencyInBookedItemsSec=getTotalAmountAndCurrencyInNonBookedItemsScrn()

			//resultData.hotel.removeItemPage.expectedItemsTotalAmountAndCurrency=resultData.hotel.searchResults.itineraryBuilder.secondItemPrice+" "+clientData.currency
			resultData.hotel.removeItemPage.expectedItemsTotalAmountAndCurrency=resultData.hotel.searchResults.itineraryBuilder.thirdItemPrice+" "+clientData.currency

			scrollToTopOfThePage()

			//Capture Unavailable and cancel item list
			//resultData.hotel.removeItemPage.actualUnavailableAndCancelItemsHeaderTxt=getHeaderTxtInBookedItemsListScrn(4)
			resultData.hotel.removeItemPage.actualUnavailableAndCancelItemsHeaderTxt=getHeaderTxtInBookedItemsListScrn(2)

			//Capture no. of items under Unavailable and cancel item list
			resultData.hotel.removeItemPage.actualUnavailableAndCancelItemsCount=getNoOfItemsInUnavailableAndCancelItemsSection()

			//should not shown total amount under Unavailable and cancel item list
			resultData.hotel.removeItemPage.actualTotalDispStatus=getTotalDisplayStatus()

			//Should not have suggest item / Non booked item list displayed
			resultData.hotel.removeItemPage.actualNonBookedItemOrSuggestedItmsSecDispStatus=getNonBookedItemsOrSuggestedItemsSecDispStatus()
		}

	}


	protected def multiRoomBooking(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){
		
		String depDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
		at ItineraryTravllerDetailsPage
		def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
		int countChkBox=0
		for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
		{
			Map roomDataMap = [:]
			Map roomExpectedData = data.expected.get("room" + i)
			
			//Capture hotel name in About to Book Screen
			roomDataMap.actualHotelName  = getHotelNameInAboutToBookScrn(i - 1)

			//String tmphotelTxt=getTitleSectDescText(i-1)
			String tmphotelTxt=getTitleSectDescTextInMultiRoom(i-1)

			//Capture hotel desc
			//List<String> htlDesc=tmphotelTxt.tokenize("\n")
			//println("Complete Desc is-> $htlDesc")
			//String hotelDescPaxText=htlDesc.getAt(0)
			//List<String> hotelDesPax=hotelDescPaxText.tokenize(",")
			//String actualhotelDesc=hotelDesPax.getAt(0)
			//roomDataMap.actualhotelDesc=actualhotelDesc

			List<String> hotelDesPax=tmphotelTxt.tokenize(",")
			String actualhotelDesc=hotelDesPax.getAt(0)
			roomDataMap.actualhotelDesc=actualhotelDesc

			//roomDataMap.expectedhotelDesc="1x "+roomExpectedData.roomDesc
			roomDataMap.expectedhotelDesc=roomExpectedData.roomDesc
			println("$roomDataMap.actualhotelDesc")
			//Capture PAX
			//String actualpaxTxt=hotelDesPax.getAt(1)
			//actualpaxTxt=actualpaxTxt.trim()
			//roomDataMap.actualpaxTxt=actualpaxTxt
			//println("$roomDataMap.actualpaxTxt")

			String actualpaxTxt=getTitleSectDescTxtInMultiRoom(i-1,3)
			actualpaxTxt=actualpaxTxt.trim().replaceAll("\n","")
			roomDataMap.actualpaxTxt=actualpaxTxt
			println("$roomDataMap.actualpaxTxt")
			//Capture Meal Basis Text
			//String actualMealBasisTxt=htlDesc.getAt(1)
			String actualMealBasisTxt=hotelDesPax.getAt(1)
			roomDataMap.actualMealBasisTxt=actualMealBasisTxt
			println("$roomDataMap.actualMealBasisTxt")
			//check in date, night
			//String actualCheckInDtNightsTxt=htlDesc.getAt(2)
			String actualCheckInDtNightsTxt=getTitleSectDescTxtInMultiRoom(i-1,0)
			roomDataMap.actualCheckInDtNightsTxt=actualCheckInDtNightsTxt.replaceAll("\n","")
			println("$roomDataMap.actualCheckInDtNightsTxt")
			/*if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"
			roomDataMap.expectedDuration=dur*/

			String expectedCheckInDateTextAndDate="Check in"+checkInDt
			roomDataMap.expectedCheckInDateTextAndDate=expectedCheckInDateTextAndDate
			//Number of Nights
			String actualNumOfNight=getTitleSectDescTxtInMultiRoom(i-1,2).replaceAll("\n","")
			roomDataMap.actualNumOfNight=actualNumOfNight
			/*if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"*/

			if (nights >1)
				dur= "Nights"+nights
			else dur = "Nights"+nights
			roomDataMap.expectedDuration=dur

			//price and commission
			String tmpPriceAndComsn=getPriceAndCommisstionPercentInAbouttoBookScrn(i-1)

			//Capture price and currency
			List<String> htlPriceAndComsn=tmpPriceAndComsn.tokenize("\n")
			String actualPriceAndComsn=htlPriceAndComsn.getAt(0).trim()
			roomDataMap.actualPriceAndComsn=actualPriceAndComsn
			//Capture Commission
			String actualCommission
			try{
				actualCommission=htlPriceAndComsn.getAt(1).trim()
				roomDataMap.actualCommission=actualCommission
			}catch(Exception e){
				roomDataMap.actualCommission="Unable To Read From UI"
			}


			//Book Page PAX Section

			//Title Text
			roomDataMap.actualPaxSecTitleTxt=getPaxSectionTitleTxt(i-1)
			List travellerDataList = []
			List travellerChkBoxDispStatus=[]
			//Capture travellers
			for(int j=0;j<data.expected.totalNoOfTravellers;j++)
			{
				travellerDataList.add(getTravellerNameInAboutToBookScrn(i-1,j))
				travellerChkBoxDispStatus.add(getCheckBoxDisplayStatusInAboutToBookScrn(i,j+1))
			}
			println("$travellerDataList")
			println("$travellerChkBoxDispStatus")
			
			List expectedTravellers=[]
			List expectedTravellerChkBoxDispStatus = []
			//Capture adults dynamically
			
			for(int k=1;k<=data.input.totalAdults;k++){
				if(k==1) expectedTravellers.add(data.expected.firstName+" "+data.expected.lastName+" (Lead Name)")
				//else expectedTravellers.add(data.expected.firstName+" "+data.expected.lastName)
				else if(k==2) expectedTravellers.add(data.expected.firstName+"Second "+data.expected.lastName+"Second")
				else if(k==3) expectedTravellers.add(data.expected.firstName+"Third "+data.expected.lastName+"Third")
				else if(k==4) expectedTravellers.add(data.expected.firstName+"Fourth "+data.expected.lastName+"Fourth")


			}
			int childIndx=0
			for(int k=data.input.totalAdults+1;k<=(data.input.totalAdults+data.input.children.size());k++)
			{
				//expectedTravellers.add(data.expected.childFirstName+" "+data.expected.childLastName+" "+"("+data.input.children.getAt(childIndx++)+"yrs)")
				if(k==5) expectedTravellers.add(data.expected.childFirstName+"Fifth "+data.expected.childLastName+"Fifth "+"("+data.input.children.getAt(childIndx++)+"yrs)")
				else if(k==6) expectedTravellers.add(data.expected.childFirstName+"Sixth "+data.expected.childLastName+"Sixth "+"("+data.input.children.getAt(childIndx++)+"yrs)")


			}
			//println("Room Expected  Travllers->$expectedTravellers")

			
			for(int k=1;k<=(data.input.totalAdults+data.input.children.size());k++){
				
				expectedTravellerChkBoxDispStatus.add(data.expected.yesDispStatus)
				
				
			}
				
			//println("Room Expected check box disp status->$expectedTravellerChkBoxDispStatus")
			List<String> roomPax=roomExpectedData.totalPax.tokenize(" ")
			int pax=roomPax.getAt(0).toInteger()
			//println("Tracking Retrievd pax-> $pax")
			for(int j=1;j<=pax;j++){
				countChkBox=countChkBox+1
				clickTravellerCheckBoxInAboutToBookScrn(i,countChkBox)
				sleep(2000)
			}
			//Special condition
			
			//special condition - header text
			//roomDataMap.actualSpecialConditionHeaderTxt=getOverlayHeaderTextInAboutToBookItems(i)
			//roomDataMap.expectedSpecialConditionHeeaderTxt=data.expected.spclCondtnTxt+checkInDt
			roomDataMap.actualSpecialConditionHeaderTxt=getTermsAndConditionsTxtInAboutToBookItemScrn()
			roomDataMap.expectedSpecialConditionHeeaderTxt=data.expected.spclCondtnTxt
			
			//special condition - please note text
			//roomDataMap.actualSpecialConditionPlzNoteTxt=getPleaseNoteTextInAboutToBookItems(i)
			roomDataMap.actualSpecialConditionPlzNoteTxt=getPleaseNoteTextInAboutToBookItems(1)
			
			//Cancellation Charge Header Text
			//roomDataMap.actualCancellationChrgHeaderTxt=getCancelChrgHeaderTextInAboutToBookItems(i)
			//roomDataMap.actualCancellationChrgHeaderTxt=getOverlayHeadersTextInAboutToAmndScrn(i-1)
			roomDataMap.actualCancellationChrgHeaderTxt=getCancelChargersHeaderTextInAboutToScrn(i-1)

			//Ammendment Charge
			//roomDataMap.actualAmmendmentCharge=getIfAmendmentstextInAboutToBookItems(i)
			roomDataMap.actualAmmendmentCharge=getIfAmendmentstextInAboutToBookItems(1)

			//General Terms & Conditions Text
			//roomDataMap.actualTermsAndConditionsText=getAllDatesTextInAboutToBookItems(i)
			roomDataMap.actualTermsAndConditionsText=getAllDatesTextInAboutToBookItems(1)

			//Terms & Condtions Link
			//roomDataMap.actualTAndCDisplayStatus=getTCLinkInAboutToBookScreendisplayStatus(i)
			roomDataMap.actualTAndCDisplayStatus=getTCLinkInAboutToBookScreendisplayStatus(1)

			//By Click on T & c
			//String ByclickonTAndC=getByClickingFooterTxt(0)
			String ByclickonTAndC=getByClickingFooterTxt()
			resultData.hotel.itineraryPage.actualTermsAndCondtTxt=ByclickonTAndC.replace("\n", "")
			
			//Add Comment or Remark Text
			roomDataMap.actualSpecialRemarkOrCommentTxt=getSpecialRemarkOrCommentTxt(i)
			
			//click on downside arrow
			scrollToRemarksAndClick(i)
			
			sleep(2000)
			//capture Expand status
			//roomDataMap.actualExpandedStatus=getExpandOrCollapseItemDisplayStatus(i)
			roomDataMap.actualExpandedStatus=getExpandOrCollapseItemDisplayStatus(i-1)
			//click on upside arrow
			scrollToRemarksAndClick(i)
			sleep(2000)
			//capture Collapsed status
			//roomDataMap.actualCollapsedStatus=getExpandOrCollapseItemDisplayStatus(i)
			roomDataMap.actualCollapsedStatus=getExpandOrCollapseItemDisplayStatus(i-1)
			//click on downside arrow
			scrollToRemarksAndClick(i)
			sleep(4000)
			
			//title text, please note text
			roomDataMap.actualPleaseNoteTxt=getPleaseNoteTxt(i)
			
			//Will arrive without voucher check box display status
			roomDataMap.actualwilArrivWithoutVochrchkBoxDispStatus=getWilArrivWithOutVochrCheckBoxDisplayStatus(i-1)
			//Will arrive without voucher text
			roomDataMap.actualwilArrivWithoutVochrTxt=getCheckBoxTextInAddSpecialRemark(i,1)

			//Previous night is booked for early morning arrival display status
			roomDataMap.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus(i-1)
			//Previous night is booked for early morning arrival text
			roomDataMap.actualPreviousNightTxt=getCheckBoxTextInAddSpecialRemark(i,2)

			//Late check out check box display status
			roomDataMap.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus(i-1)
			//Late check out Text
			roomDataMap.actualLateCheckOutTxt=getCheckBoxTextInAddSpecialRemark(i,3)

			//Late Arrival check box display status
			roomDataMap.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus(i-1)
			//Late Arrival text box
			roomDataMap.actualLateArrivalTxt=getCheckBoxTextInAddSpecialRemark(i,4)

			//Passengers Are HoneyMooners check box Display Status
			roomDataMap.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus(i-1)
			//Passengers Are HoneyMooners Text
			roomDataMap.actualPassengersAreHoneyMoonersTxt=getCheckBoxTextInAddSpecialRemark(i,5)
			
			//Early Arrival Disp Status
			roomDataMap.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus(i-1)
			//Early Arrival Text
			roomDataMap.actualEarlyArrivalTxt=getCheckBoxTextInAddSpecialRemark(i,6)

			//tick the check box Will arrive without voucher
			clickWilArrivWithOutVochrCheckBox(i-1)
			sleep(2000)

			//titleText - if possible please provide
			roomDataMap.actualIfPsblePlzProvTxt=getPleaseProvideTxt(i)

			//Twin Room check box display status
			roomDataMap.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus(i-1)
			//Twin Room Text Box
			roomDataMap.actualTwinRoomtxt=getCheckBoxTextInAddSpecialRemark(i,8)

			//Smoking Room Check Box display status
			roomDataMap.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus(i-1)
			//Smoking Room Check Box Text
			roomDataMap.actualSmokingRoomtxt=getCheckBoxTextInAddSpecialRemark(i,9)

			//Inter Connecting Rooms check box display status
			roomDataMap.actualICRChkBoxDisp=getICRoomCheckBoxDisplayStatus(i-1)
			//Inter connecting Rooms text
			roomDataMap.actualICRoomtxt=getCheckBoxTextInAddSpecialRemark(i,10)
			
			//Adjoining Rooms check box display status
			roomDataMap.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus(i-1)
			//Adjoining Rooms check box text
			roomDataMap.actualAdjoiningRoomtxt=getCheckBoxTextInAddSpecialRemark(i,11)

			//Quiet Rooms check box display status
			roomDataMap.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus(i-1)
			//Quiet Rooms check box text
			roomDataMap.actualQuietRoomtxt=getCheckBoxTextInAddSpecialRemark(i,12)

			//Non-Smoking Rooms check box display status
			roomDataMap.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus(i-1)
			//Non-Smoking Rooms text
			roomDataMap.actualNonSmokingRoomtxt=getCheckBoxTextInAddSpecialRemark(i,13)

			//Room on lowest floor display status
			roomDataMap.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus(i-1)
			//Room on lowest floor text
			roomDataMap.actualRoomOnLowestFloorRoomtxt=getCheckBoxTextInAddSpecialRemark(i,14)

			//Room on high floor checkbox display status
			roomDataMap.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus(i-1)
			//Room on high floor text
			roomDataMap.actualRoomOnHighFloorRoomtxt=getCheckBoxTextInAddSpecialRemark(i,15)

			//Double Room check box display status
			roomDataMap.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus(i-1)
			//Double Room text
			roomDataMap.actualDoubleRoomtxt=getCheckBoxTextInAddSpecialRemark(i,16)

			//Room with bathtub
			roomDataMap.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus(i-1)
			//Room with bathtub
			roomDataMap.actualRoomwithBathTubtxt=getCheckBoxTextInAddSpecialRemark(i,17)

			//Arrival Flight Number check box display status
			roomDataMap.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus(i-1)
			//Arrival Flight Number text
			roomDataMap.actualArrivalFlightNumtxt=getCheckBoxTextInAddSpecialRemark(i,7)
						
			//Arrival Flight Number text box existence
			roomDataMap.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus(i-1)

			//at Hrs
			//at text
			//roomDataMap.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,2)
			roomDataMap.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,2)
			//Hrs text
			//roomDataMap.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,3)
			roomDataMap.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,3)
			//Hrs select drop box display status
			//roomDataMap.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus(i-1)
			roomDataMap.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus(i)
			
			//mins
			//mins text
			//roomDataMap.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,4)
			roomDataMap.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,4)
			//mins select drop box display status
			//roomDataMap.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus(i-1)
			roomDataMap.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus(i)
			
			//On Date
			//on text
			//roomDataMap.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,5)
			roomDataMap.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,5)
			//on Day select drop box display status
			//roomDataMap.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus(i-1)
			roomDataMap.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus(i)
			//on Month select drop box display status
			//roomDataMap.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus(i-1)
			roomDataMap.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus(i)
			
			
			resultData.hotel.itineraryPage.multiRoomBooking.travellers.put("room"+i, travellerDataList)
			resultData.hotel.itineraryPage.multiRoomBooking.travellers.chkBoxStatus.put("room"+i, travellerChkBoxDispStatus)
			resultData.hotel.itineraryPage.multiRoomBooking.put("room"+i, roomDataMap)
			resultData.hotel.itineraryPage.multiRoomBooking.expectedTravellers.put("room"+i, expectedTravellers)
			resultData.hotel.itineraryPage.multiRoomBooking.travellers.expectedChkBoxStatus.put("room"+i, expectedTravellerChkBoxDispStatus)

		}
				

			//Book Page Footer
			
						//Total
						resultData.hotel.itineraryPage.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)
						//Total Amount and currency
						resultData.hotel.itineraryPage.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)
			
						//Your Commission text
						resultData.hotel.itineraryPage.actualCommissionTextInAboutToBook=getTotalAndCommissionText(2)
						//Your commission amount and currency
						//resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getCommissionValueAndCurrencyInAboutToBookScrn(1)
						resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getTotalAndCommissionText(3)
						float compercentage = Float.parseFloat(data.expected.commissionPercent)
						float totalPrice=Float.parseFloat(resultData.hotel.searchResults.priceText_FirstCard.toString().replaceAll(",",""))+Float.parseFloat(resultData.hotel.searchResults.priceText_SecondCard.toString().replaceAll(",",""))
						DecimalFormat df = new DecimalFormat("####.00");
						resultData.hotel.itineraryPage.totalExpectedPrice=df.format(totalPrice)+" "+clientData.currency
						println("Bfore Booking total tprice: $totalPrice")
						
			
			
						String comValue=getCommissionPercentageValue(totalPrice,compercentage)
						resultData.hotel.itineraryPage.expectedComValue=comValue+" "+clientData.currency
			
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
						
						try{
							//Header Section
							resultData.hotel.itineraryPage.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()
						}catch(Exception e){
							//Header Section
							resultData.hotel.itineraryPage.actualHeaderSectionText="Unable To Read Header Section Text From UI"
						}

						String emailId=clientData.usernameOrEmail
						//emailId=emailId.toUpperCase()
						//resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
						resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt
			
						//read Booking id
						//resultData.hotel.itineraryPage.retrievedbookingID=getBookingIdFromBookingConfirmed(0)
						//println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")
			
						//read Itineary Reference Number & name
						resultData.hotel.itineraryPage.actualreaditinearyIDAndName=getItinearyID(0)
						resultData.hotel.itineraryPage.expecteditinearyIDAndName=resultData.hotel.searchResults.itineraryBuilder.retitineraryId+resultData.hotel.itineraryPage.expectedItnrName
			
						//Capture Departure Date
			
						resultData.hotel.confirmationPage.actualConfimrationDialogDepDate=getBookingDepartDate()
						resultData.hotel.confirmationPage.expectedConfimrationDialogDepDate=depDate.toUpperCase()
						//capture traveller details
						List expectedTravellersConfScreen=[]
						List actualtravellerDataListConfScreen = []
					
						//Capture adults dynamically
						
						for(int k=1;k<=data.input.totalAdults;k++){
							if(k==1) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+" (Lead Name)")
							//else expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+k+" "+data.expected.lastName+k)
							else if(k==2) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+"Second "+data.expected.lastName+"Second")
							else if(k==3) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+"Third "+data.expected.lastName+"Third")
							else if(k==4) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+"Fourth "+data.expected.lastName+"Fourth")


						}
						int childIndx=0
						for(int k=data.input.totalAdults+1;k<=(data.input.totalAdults+data.input.children.size());k++)
						{
							//expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.childFirstName+k+" "+data.expected.childLastName+k+" "+"("+data.input.children.getAt(childIndx++)+"yrs)")
							//expectedTravellersConfScreen.add(data.expected.childFirstName+k+" "+data.expected.childLastName+k+" "+"("+data.input.children.getAt(childIndx++)+"yrs)")
							if(k==5) expectedTravellersConfScreen.add(data.expected.childFirstName+"Fifth "+data.expected.childLastName+"Fifth "+"("+data.input.children.getAt(childIndx++)+"yrs)")
							else if(k==6) expectedTravellersConfScreen.add(data.expected.childFirstName+"Sixth "+data.expected.childLastName+"Sixth "+"("+data.input.children.getAt(childIndx++)+"yrs)")


						}
						
						println("Booking Confirmation screen exp travellers:$expectedTravellersConfScreen")
						
						//Capture actual travellers
						for(int j=0;j<data.expected.totalNoOfTravellers;j++)
						{
							actualtravellerDataListConfScreen.add(getTravellerNamesConfirmationDialog(j))
							
						}
						
						println("Booking Confirmation screen actual travellers:$actualtravellerDataListConfScreen")
					
						resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerDataListConfScreen)
						resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellersConfScreen)
						
						
						for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
						{
							Map roomDataMap = [:]
							Map roomExpectedData = data.expected.get("room" + i)
							Map onlyPrice=resultData.hotel.searchResults.price.get("room"+i)
							//Hotel Name
							roomDataMap.actualHotelName=getHotelNameInBookingConfScrn(i)
							
							//Hotel Address
							roomDataMap.actualHotelAddressTxt=getHotelAddressInBookingConfScrn(i)
							
							//Hotel Details
							String tempTxt=getHotelDetailsInBookingConfScrn(i)
							//Capture hotel desc
							List<String> htlDesc=tempTxt.tokenize("\n")
							String hotelDescPaxText=htlDesc.getAt(0)
							List<String> hotelDesPax=hotelDescPaxText.tokenize(",")
							String actualhotelDesc=hotelDesPax.getAt(0)
							roomDataMap.actualhotelDesc=actualhotelDesc
							roomDataMap.expectedhotelDesc="1x "+roomExpectedData.roomDesc
							//Capture PAX
							String actualpaxTxt=hotelDesPax.getAt(1)
							actualpaxTxt=actualpaxTxt.trim()
							roomDataMap.actualpaxTxt=actualpaxTxt
							println("$roomDataMap.actualpaxTxt")
							//Capture Meal Basis Text
							String actualMealBasisTxt=htlDesc.getAt(1)
							roomDataMap.actualMealBasisTxt=actualMealBasisTxt
							println("$roomDataMap.actualMealBasisTxt")
							
							
							//Check in date, Number of Nights
							roomDataMap.actualCheckInDateNumOfNights=getDurationInBookingConfScrn(i)
				
							if (nights >1)
								dur= checkInDt+", "+nights+" nights"
							else dur = checkInDt+", "+nights+" night"
				
							roomDataMap.expectedCheckInDateNumOfNights=dur
							//Please Note txt
							roomDataMap.actualPlzNoteTxtInBookingConfScrn=getPleaseNoteTxtInBookingConfirmedScrn(i)
							
							//Remarks
							roomDataMap.actualRemarksTxtInBookingConfScrn=getRemarksTxtInBookingConfirmScrn(i)
							
							//Price and currency
							roomDataMap.actualSubPriceAndCurrency=getSubTotalsInBookingConfScreen(i)
							
							//Commission Actual
							roomDataMap.actualSubCommissionAndCurrency=getSubCommissionsInBookingConfScreen(i)
							//Commission Expected
							compercentage = Float.parseFloat(data.expected.commissionPercent)
						    totalPrice=Float.parseFloat(onlyPrice.price)
							comValue=getCommissionPercentageValue(totalPrice,compercentage)
							roomDataMap.expectedComValue=data.expected.comTxt+" "+comValue+" "+clientData.currency
							
							
							resultData.hotel.confirmationPage.multiroomBooking.put("room"+i, roomDataMap)
						}
						
						//Price and currency
						resultData.hotel.confirmationPage.actualTotalPriceAndCurrency=gettotalPriceAndCurencyBookingConfirmScrn()
						resultData.hotel.confirmationPage.expectedTotalPriceAndCurrency=resultData.hotel.itineraryPage.totalExpectedPrice
						//Commission and currency
						//Commission Amount and Currency
						resultData.hotel.confirmationPage.actualCommissionAmountAndCurrency=gettotalCommissionBookingConfirmScrn()
						resultData.hotel.confirmationPage.expectedCommissionAmountAndCurrency=data.expected.comTxt+resultData.hotel.itineraryPage.expectedComValue.replaceAll(" ", "")
			
						
						//Close lightbox X function
						resultData.hotel.confirmationPage.actualCloseLightboxDispStatus=getCloseIconDisplayStatus()
			
						//click on Close lightbox X function
						coseBookItenary()
						sleep(7000)
			
						//Get Header Text
						resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInItemsListScrn(1)
						
						//read Booking id
						resultData.hotel.itineraryPage.retrievedbookingID=getBookingIDBookedDetailsScrn(1)
						println("New Code Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")
					
						for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)	{
							
							Map roomDataMap = [:]
							//Get Booing ID and number
							//roomDataMap.actualBookingID=getBookingIDinBookeddetailsScrn(i)
							roomDataMap.actualBookingID=getBookingIDBookedDetailsScrn(i)
							println("Booking Id Actual->$roomDataMap.actualBookingID")					
							//roomDataMap.expectedBookingID="Booking ID: "+resultData.hotel.itineraryPage.retrievedbookingID+"  "+"("+i+" "+"of"+" "+data.input.noOfRooms.toString().toInteger()+")"
							roomDataMap.expectedBookingID=resultData.hotel.itineraryPage.retrievedbookingID
							println("Booking Id expected->$roomDataMap.expectedBookingID")
							
							//Confirmed tab
							roomDataMap.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus(i)
							//confirm tab text
							roomDataMap.actualStatusTabDispStatus=getStatusInBookedItemsScrn(i)
							
							//Amend tab
							roomDataMap.actualAmendTabDispStatus=getAmendTabDisplayStatus(i)
							//Amend tab text
							roomDataMap.actualAmendTabTxt=getAmendTabTxtInBookedItemsScrn(i)
							
							//Cancel tab
							roomDataMap.actualCancelTabDispStatus=getCancelTabDisplayStatus(i)
							//Cancel tab text
							roomDataMap.actualCancelTabTxt=getCancelTabTxtInBookedItemsScrn(i)
							
							//Hotel Image Display status
							roomDataMap.actualHotelImageDispStatus=getHotelImageDisplayStatus(i)
				
							//Capture Image Src URL
							roomDataMap.actualImageSrcURLinBookedItem=getImageSrcURLInBookedItem(i-1)
							println("$roomDataMap.actualImageSrcURLinBookedItem")
							
							//Hotel Name
							roomDataMap.actualHotelNameTxt=getTransferNameInSuggestedItem(i-1)
							//Caputre Hotel name Src URL
							try{
								roomDataMap.actualHotelNameLinkTxt=getHotelNameSrcURLInBookedItems(i)
								println("$roomDataMap.actualHotelNameLinkTxt")
							}catch(Exception e){
								roomDataMap.actualHotelNameLinkTxt="Unable to read Hotel Name Src URL in Booked Items"
							}


							
							//Hotel Star Rating
							roomDataMap.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(i-1)
							
							String descComplTxt=getItinenaryDescreptionInSuggestedItem(i-1)
							List<String> tmpItineraryDescList=descComplTxt.tokenize(",")
							//room description
							String descText=tmpItineraryDescList.getAt(0)
							roomDataMap.actualroomdescTxtInBookedItmsScrn=descText.trim()
							//Pax number requested
							String tempTxt=tmpItineraryDescList.getAt(1)
							List<String> tmpDescList=tempTxt.tokenize(".")
				
							String paxNumDetails=tmpDescList.getAt(0)
							roomDataMap.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()
				
							//Rate plan - meal basis
							String ratePlantxtDetails=tmpDescList.getAt(1)
							roomDataMap.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()
							
							//Free cancellation until date
							roomDataMap.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(i-1)
							
							//Requested Check in and nights
							roomDataMap.actualDurationTxtInBookedItmsScrn=getItenaryDurationInSuggestedItem(i-1)
							if (nights >1)
							dur= checkInDt+", "+nights+" nights"
							else dur = checkInDt+", "+nights+" night"
			
							roomDataMap.expectedCheckInDateNumOfNights=dur
							
							//commission and percentage
							roomDataMap.actualCommissionAndPercentatgeInBookedItmsScrn=getCommisionAndPercentageInBookeddetailsScrn(i-1)
							
							//Room rate amound and currency
							//roomDataMap.actualPriceAndcurrencyInBookedItmsScrn=getItenaryPriceInSuggestedItem(i-1)
							roomDataMap.actualPriceAndcurrencyInBookedItmsScrn=getItenaryPriceInBookedItem(i)
							//Add a remark or content
							roomDataMap.actualRemarksInBookedItmsScrn=getRemarksInBookeddetailsScrn(i).replaceAll(":","")
							roomDataMap.expectedRemarksInBookedItmsScrn=data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
							roomDataMap.expectedRemarksInBookedItmsScrn=roomDataMap.expectedRemarksInBookedItmsScrn.replaceAll(":","")

							//Edit Icon display status
							roomDataMap.actualEditIconInBookedDetailScrnDispStatus=getEditIconInBookedDetailsScrnDisplayStatus(i)
				
							//click on Edit icon
							clickEditRemarksButton(i-1)
							sleep(4000)
							
							//Add comment or special request lighbox display
							roomDataMap.actualHeaderTxtInAddCmntOrSpeclReqScrnDispStatus=getAddCmntOrSpclReqDisplayStatus()
							//Add comment or special request lighbox text
							roomDataMap.actualHeaderTxtInAddCmntOrSpeclReqScrn=getBookingStatusOnLightBox()
														
							//Select from the choices below or write your own request - text
							roomDataMap.actualHeaderTxtSelChoicesBelowInAddCmntOrSpeclReqScrn=getTravellerCannotBeDeletedheaderText().trim()
				
							//Please Note: text
							roomDataMap.actualHeaderTxtPlzNoteInAddcmntOrSclReqScrn=getHeaderTxtInAddCmntrSpclReqScrn(0)
						
							//Will arrive without voucher- Should display with box ticked.
							roomDataMap.actualwilArrivWithoutVochrCheckBoxStatus=getWilArrivWithOutVochrCheckBoxCheckedStatus()
							//roomDataMap.actualwilArrivWithoutVochrCheckBoxStatus=getWillArrivWithOutVochrChkBoxEnabledOrDisabledStatus()
							//Will arrive without voucher text
							roomDataMap.actualwilArrivWithoutVochrTxt=getCheckBoxTextInAddSpecialRemarkSection(0)
				
							//Click
							//roomDataMap.actualCheckBoxTickStatusAfterClick=clickWilArrivWithOutVochrCheckBox()
							//roomDataMap.actualCheckBoxTickStatusAfterClick=clickWilArrivWithOutVochrCheckBox(i-1)
							//Previous night is booked for early morning arrival display status
							roomDataMap.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus()
							//Previous night is booked for early morning arrival text
							roomDataMap.actualPreviousNightTxt=getCheckBoxTextInAddSpecialRemarkSection(1)
														
							//Late check out check box display status
							roomDataMap.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus()
							//Late check out Text
							roomDataMap.actualLateCheckOutTxt=getCheckBoxTextInAddSpecialRemarkSection(2)
							
							//Late Arrival check box display status
							roomDataMap.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus()
							//Late Arrival text box
							roomDataMap.actualLateArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(3)
							
							//Passengers Are HoneyMooners check box Display Status
							roomDataMap.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus()
							//Passengers Are HoneyMooners Text
							roomDataMap.actualPassengersAreHoneyMoonersTxt=getCheckBoxTextInAddSpecialRemarkSection(4)
				
							//Early Arrival Disp Status
							roomDataMap.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus()
							//Early Arrival Text
							roomDataMap.actualEarlyArrivalTxt=getCheckBoxTextInAddSpecialRemarkSection(5)
				
							//If possible, please provide: text
							roomDataMap.actualHeaderTxtIfPssblPlzProvideTxt=getHeaderTxtInAddCmntrSpclReqScrn(1)
							
							//Twin Room check box display status
							roomDataMap.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus()
							//Twin Room Text Box
							roomDataMap.actualTwinRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(7)
				
							//Smoking Room Check Box display status
							roomDataMap.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus()
							//Smoking Room Check Box Text
							roomDataMap.actualSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(8)
							
							//Inter Connecting Rooms check box display status
							roomDataMap.actualInterConnectingRoomCheckboxDispStatus=getInterConnectingRoomCheckBoxDisplayStatus()
							//Inter connecting Rooms text
							roomDataMap.actualIntrConnectngRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(9)
							
							//Adjoining Rooms check box display status
							roomDataMap.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus()
							//Adjoining Rooms check box text
							roomDataMap.actualAdjoiningRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(10)
							
							//Quiet Rooms check box display status
							roomDataMap.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus()
							//Quiet Rooms check box text
							roomDataMap.actualQuietRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(11)
				
							//Non-Smoking Rooms check box display status
							roomDataMap.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus()
							//Non-Smoking Rooms text
						    roomDataMap.actualNonSmokingRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(12)
							
							//Room on lowest floor display status
							roomDataMap.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus()
							//Room on lowest floor text
							roomDataMap.actualRoomOnLowestFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(13)
							
							//Room on high floor checkbox display status
							roomDataMap.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus()
							//Room on high floor text
							roomDataMap.actualRoomOnHighFloorRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(14)
							
							//Double Room check box display status
							roomDataMap.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus()
							//Double Room text
							roomDataMap.actualDoubleRoomtxt=getCheckBoxTextInAddSpecialRemarkSection(15)
				
							//Room with bathtub
							roomDataMap.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus()
							//Room with bathtub
							roomDataMap.actualRoomwithBathTubtxt=getCheckBoxTextInAddSpecialRemarkSection(16)
							
							//Arrival Flight Number check box display status
							roomDataMap.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus()
							//Arrival Flight Number text
							roomDataMap.actualArrivalFlightNumtxt=getCheckBoxTextInAddSpecialRemarkSection(6)
				
							//Arrival Flight Number text box existence
							roomDataMap.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus()
				
							//Hrs
							//at text
							roomDataMap.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(1)
							//Hrs text
							roomDataMap.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(2)
							//Hrs select drop box display status
							roomDataMap.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus()
				
				
							//Mins
							//mins text
							roomDataMap.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(3)
							//mins select drop box display status
							roomDataMap.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus()
				
							//On Date
							//on text
							roomDataMap.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInAddSpecialRemarkSection(4)
							//on Day select drop box display status
							roomDataMap.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus()
							//on Month select drop box display status
							roomDataMap.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus()
							
							//Click Smoking Room Check Box
							clickSmokingRoomCheckBox()
							sleep(3000)
							
							//capture Smoking Room check box ticked or not
							roomDataMap.actualSmokingRoomCheckBoxCheckedStatus=getSmokingRoomCheckBoxCheckedStatus()
							
							//Click on Save button
							scrollAndclickOnSaveBtn()
							sleep(9000)
							waitForAjaxIconToDisappear()
							
							//Confirmed booking after Edit remarks
							
							//Hotel Image Display status
							roomDataMap.actualHotelImgDispStatus=getHotelImageDisplayStatus(i)
				
							//Capture Image Src URL
							roomDataMap.actualImageSrcURLinBookedItem=getImageSrcURLInBookedItem(i-1)
							println("$roomDataMap.actualImageSrcURLinBookedItem")
							
							//Hotel Name
							roomDataMap.actualHtlNameTxt=getTransferNameInSuggestedItem(i-1)
							//Caputre Hotel name Src URL
							try{
								roomDataMap.actualHotelNameLinkTxt=getHotelNameSrcURLInBookedItems(i)
								println("$roomDataMap.actualHotelNameLinkTxt")
							}catch(Exception e){
								roomDataMap.actualHotelNameLinkTxt="Unable to Read Hotel Name Src URL From UI"
							}

							
							//Hotel Star Rating
							roomDataMap.actualStarRatingBookedHotelItem=getRatingForTheHotelInSuggestedItem(i-1)
							
							//room description
											
							descComplTxt=getItinenaryDescreptionInSuggestedItem(i-1)
							tmpItineraryDescList=descComplTxt.tokenize(",")
							//room description
							descText=tmpItineraryDescList.getAt(0)
							roomDataMap.actualroomdescTextBookedItmsScrn=descText.trim()
							//Pax number requested
							tempTxt=tmpItineraryDescList.getAt(1)
							tmpDescList=tempTxt.tokenize(".")
				
							paxNumDetails=tmpDescList.getAt(0)
							roomDataMap.actualPaxNumBookedItmsScrn=paxNumDetails.trim()
				
							//Rate plan - meal basis
							ratePlantxtDetails=tmpDescList.getAt(1)
							roomDataMap.actualratePlanBookedItmsScrn=ratePlantxtDetails.trim()
							
							//Free cancellation until date
							roomDataMap.actualFreeCancelTxtbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(i-1)
							
							//Requested Check in and nights
							roomDataMap.actualDurationTextBookedItmsScrn=getItenaryDurationInSuggestedItem(i-1)
														
							//commission and percentage
							roomDataMap.actualCommissionAndPercentatgeBookedItmsScrn=getCommisionAndPercentageInBookeddetailsScrn(i-1)
							
							//Room rate amound and currency
							//roomDataMap.actualPriceAndcurrencyBookedItmsScrn=getItenaryPriceInSuggestedItem(i-1)
							//Room rate amound and currency
							roomDataMap.actualPriceAndcurrencyBookedItmsScrn=getItenaryPriceInBookedItem(i)

							//Add a remark or content
							roomDataMap.actualRemarksBookedItmsScrn=getRemarksInBookeddetailsScrn(i)
							roomDataMap.expectedRemarksBookedItmsScrn=data.expected.IfPsblePlzProvTxt+" "+data.expected.chkBoxTxt_8+" "+data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
							
												
							
							resultData.hotel.itineraryPage.multiRoomBooking.bookedItems.put("room"+i, roomDataMap)
						}
					
			
	}
	
	protected def multiRoomCancellation(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData){
		
		String depDate = dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy")
		at ItineraryTravllerDetailsPage
		def checkInDt=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "ddMMMyy").toUpperCase()
		//def checkInDts=dateUtil.addDaysChangeDatetformat(data.input.checkInDays.toInteger(), "yyyy-MM-dd")
		int countChkBox=0
		for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
		{
			Map roomDataMap = [:]
			Map roomExpectedData = data.expected.get("room" + i)
			
			//Capture hotel name in About to Book Screen
			roomDataMap.actualHotelName  = getHotelNameInAboutToBookScrn(i - 1)
			//String tmphotelTxt=getTitleSectDescText(i-1)
			String tmphotelTxt=getTitleSectDescTextInMultiRoom(i-1)

			//Capture hotel desc
			//List<String> htlDesc=tmphotelTxt.tokenize("\n")
			//println("Complete Desc is-> $htlDesc")
			//String hotelDescPaxText=htlDesc.getAt(0)
			//List<String> hotelDesPax=hotelDescPaxText.tokenize(",")
			List<String> hotelDesPax=tmphotelTxt.tokenize(",")
			String actualhotelDesc=hotelDesPax.getAt(0)
			roomDataMap.actualhotelDesc=actualhotelDesc
			//roomDataMap.expectedhotelDesc="1x "+roomExpectedData.roomDesc
			roomDataMap.expectedhotelDesc=roomExpectedData.roomDesc
			println("$roomDataMap.actualhotelDesc")
			//Capture PAX
			//String actualpaxTxt=hotelDesPax.getAt(1)
			String actualpaxTxt=getTitleSectDescTxtInMultiRoom(i-1,3)
			actualpaxTxt=actualpaxTxt.trim().replaceAll("\n","")
			roomDataMap.actualpaxTxt=actualpaxTxt
			println("$roomDataMap.actualpaxTxt")
			//Capture Meal Basis Text
			//String actualMealBasisTxt=htlDesc.getAt(1)
			String actualMealBasisTxt=hotelDesPax.getAt(1)

			roomDataMap.actualMealBasisTxt=actualMealBasisTxt
			println("$roomDataMap.actualMealBasisTxt")
			//check in date, night
			//String actualCheckInDtNightsTxt=htlDesc.getAt(2)
			String actualCheckInDtNightsTxt=getTitleSectDescTxtInMultiRoom(i-1,0)
			roomDataMap.actualCheckInDtNightsTxt=actualCheckInDtNightsTxt.trim().replaceAll("\n","")
			println("$roomDataMap.actualCheckInDtNightsTxt")

			String expectedCheckInDateTextAndDate="Check in"+checkInDt
			roomDataMap.expectedCheckInDateTextAndDate=expectedCheckInDateTextAndDate
			//Number of Nights
			String actualNumOfNight=getTitleSectDescTxtInMultiRoom(i-1,2).replaceAll("\n","")
			roomDataMap.actualNumOfNight=actualNumOfNight
			/*if (nights >1)
				dur= checkInDt+", "+nights+" nights"
			else dur = checkInDt+", "+nights+" night"*/

			if (nights >1)
				dur= "Nights"+nights
			else dur = "Nights"+nights
			roomDataMap.expectedDuration=dur

			//price and commission
			String tmpPriceAndComsn=getPriceAndCommisstionPercentInAbouttoBookScrn(i-1)

			//Capture price and currency
			List<String> htlPriceAndComsn=tmpPriceAndComsn.tokenize("\n")
			String actualPriceAndComsn=htlPriceAndComsn.getAt(0).trim()
			roomDataMap.actualPriceAndComsn=actualPriceAndComsn
			//Capture Commission
			String actualCommission
			try{
				actualCommission=htlPriceAndComsn.getAt(1).trim()
				roomDataMap.actualCommission=actualCommission
			}catch(Exception e){
				roomDataMap.actualCommission="Unable To Read From UI"
			}


			//Book Page PAX Section

			//Title Text
			roomDataMap.actualPaxSecTitleTxt=getPaxSectionTitleTxt(i-1)
			List travellerDataList = []
			List travellerChkBoxDispStatus=[]
			//Capture travellers
			for(int j=0;j<data.expected.totalNoOfTravellers;j++)
			{
				travellerDataList.add(getTravellerNameInAboutToBookScrn(i-1,j))
				travellerChkBoxDispStatus.add(getCheckBoxDisplayStatusInAboutToBookScrn(i,j+1))
			}
			println("$travellerDataList")
			println("$travellerChkBoxDispStatus")
			
			List expectedTravellers=[]
			List expectedTravellerChkBoxDispStatus = []
			//Capture adults dynamically
			
			for(int k=1;k<=data.input.totalAdults;k++){
				if(k==1) expectedTravellers.add(data.expected.firstName+" "+data.expected.lastName+" (Lead Name)")
				//else expectedTravellers.add(data.expected.firstName+k+" "+data.expected.lastName+k)

				else if(k==2) expectedTravellers.add(data.expected.firstName+"Second "+data.expected.lastName+"Second")
				else if(k==3) expectedTravellers.add(data.expected.firstName+"Third "+data.expected.lastName+"Third")
				else if(k==4) expectedTravellers.add(data.expected.firstName+"Fourth "+data.expected.lastName+"Fourth")

			}
			int childIndx=0
			for(int k=data.input.totalAdults+1;k<=(data.input.totalAdults+data.input.children.size());k++)
			{
				expectedTravellers.add(data.expected.childFirstName+" "+data.expected.childLastName+" "+"("+data.input.children.getAt(childIndx++)+"yrs)")
				
				
			}
			//println("Room Expected  Travllers->$expectedTravellers")

			
			for(int k=1;k<=(data.input.totalAdults+data.input.children.size());k++){
				
				expectedTravellerChkBoxDispStatus.add(data.expected.yesDispStatus)
				
				
			}
				
			//println("Room Expected check box disp status->$expectedTravellerChkBoxDispStatus")
			//List<String> roomPax=roomExpectedData.totalPax.tokenize(" ")
			List<String> roomPax=roomExpectedData.totalPaxTxt.tokenize("x")
			//int pax=roomPax.getAt(0).toInteger()
			int pax=roomPax.getAt(1).toInteger()
			println("Tracking Retrievd pax-> $pax")
			for(int j=1;j<=pax;j++){
				countChkBox=countChkBox+1
				clickTravellerCheckBoxInAboutToBookScrn(i,countChkBox)
				sleep(2000)
			}
			//Special condition
			
			//special condition - header text
			//roomDataMap.actualSpecialConditionHeaderTxt=getOverlayHeaderTextInAboutToBookItems(i)
			//roomDataMap.expectedSpecialConditionHeeaderTxt=data.expected.spclCondtnTxt+checkInDt
			roomDataMap.actualSpecialConditionHeaderTxt=getTermsAndConditionsTxtInAboutToBookItemScrn()
			roomDataMap.expectedSpecialConditionHeeaderTxt=data.expected.spclCondtnTxt
			
			//special condition - please note text
			//roomDataMap.actualSpecialConditionPlzNoteTxt=getPleaseNoteTextInAboutToBookItems(i)
			roomDataMap.actualSpecialConditionPlzNoteTxt=getPleaseNoteTextInAboutToBookItems(1)
			
			//Cancellation Charge Header Text
			//roomDataMap.actualCancellationChrgHeaderTxt=getCancelChrgHeaderTextInAboutToBookItems(i)
			//roomDataMap.actualCancellationChrgHeaderTxt=getOverlayHeadersTextInAboutToAmndScrn(i-1)
			roomDataMap.actualCancellationChrgHeaderTxt=getCancelChargersHeaderTextInAboutToScrn(i-1)
			//Ammendment Charge
			//roomDataMap.actualAmmendmentCharge=getIfAmendmentstextInAboutToBookItems(i)
			roomDataMap.actualAmmendmentCharge=getIfAmendmentstextInAboutToBookItems(1)

			//General Terms & Conditions Text
			//roomDataMap.actualTermsAndConditionsText=getAllDatesTextInAboutToBookItems(i)
			roomDataMap.actualTermsAndConditionsText=getAllDatesTextInAboutToBookItems(1)

			//Terms & Condtions Link
			//roomDataMap.actualTAndCDisplayStatus=getTCLinkInAboutToBookScreendisplayStatus(i)
			roomDataMap.actualTAndCDisplayStatus=getTCLinkInAboutToBookScreendisplayStatus(1)

			//By Click on T & c
			//String ByclickonTAndC=getByClickingFooterTxt(0)
			String ByclickonTAndC=getByClickingFooterTxt()
			resultData.hotel.itineraryPage.actualTermsAndCondtTxt=ByclickonTAndC.replace("\n", "")
			
			//Add Comment or Remark Text
			roomDataMap.actualSpecialRemarkOrCommentTxt=getSpecialRemarkOrCommentTxt(i)
			
			//click on downside arrow
			scrollToRemarksAndClick(i)
			
			sleep(2000)
			//capture Expand status
			//roomDataMap.actualExpandedStatus=getExpandOrCollapseItemDisplayStatus(i)
			roomDataMap.actualExpandedStatus=getExpandOrCollapseItemDisplayStatus(i-1)
			//click on upside arrow
			scrollToRemarksAndClick(i)
			sleep(2000)
			//capture Collapsed status
			//roomDataMap.actualCollapsedStatus=getExpandOrCollapseItemDisplayStatus(i)
			roomDataMap.actualCollapsedStatus=getExpandOrCollapseItemDisplayStatus(i-1)
			//click on downside arrow
			scrollToRemarksAndClick(i)
			sleep(4000)
			
			//title text, please note text
			roomDataMap.actualPleaseNoteTxt=getPleaseNoteTxt(i)
			
			//Will arrive without voucher check box display status
			roomDataMap.actualwilArrivWithoutVochrchkBoxDispStatus=getWilArrivWithOutVochrCheckBoxDisplayStatus(i-1)
			//Will arrive without voucher text
			roomDataMap.actualwilArrivWithoutVochrTxt=getCheckBoxTextInAddSpecialRemark(i,1)
			
			//Previous night is booked for early morning arrival display status
			roomDataMap.actualPreviousNightBookChkBoxDispStatus=getPreviousNightBookCheckBoxDisplayStatus(i-1)
			//Previous night is booked for early morning arrival text
			roomDataMap.actualPreviousNightTxt=getCheckBoxTextInAddSpecialRemark(i,2)

			//Late check out check box display status
			roomDataMap.actualLateChkOutDispStatus=getLateCheckOutCheckBoxDisplayStatus(i-1)
			//Late check out Text
			roomDataMap.actualLateCheckOutTxt=getCheckBoxTextInAddSpecialRemark(i,3)

			//Late Arrival check box display status
			roomDataMap.actualLateArrivalDispStatus=getLateArrivalCheckBoxDisplayStatus(i-1)
			//Late Arrival text box
			roomDataMap.actualLateArrivalTxt=getCheckBoxTextInAddSpecialRemark(i,4)

			//Passengers Are HoneyMooners check box Display Status
			roomDataMap.actualPassengersAreHoneyMoonersDispStatus=getpassengersAreHoneymonnersCheckBoxDisplayStatus(i-1)
			//Passengers Are HoneyMooners Text
			roomDataMap.actualPassengersAreHoneyMoonersTxt=getCheckBoxTextInAddSpecialRemark(i,5)
			
			//Early Arrival Disp Status
			roomDataMap.actualEarlyArrivalCheckBoxDispStatus=getEarlyArrivalCheckBoxDisplayStatus(i-1)
			//Early Arrival Text
			roomDataMap.actualEarlyArrivalTxt=getCheckBoxTextInAddSpecialRemark(i,6)

			//tick the check box Will arrive without voucher
			clickWilArrivWithOutVochrCheckBox(i-1)
			sleep(2000)

			//titleText - if possible please provide
			roomDataMap.actualIfPsblePlzProvTxt=getPleaseProvideTxt(i)

			//Twin Room check box display status
			roomDataMap.actualTwinRoomCheckBoxDispStatus=getTwinRoomCheckBoxDisplayStatus(i-1)
			//Twin Room Text Box
			roomDataMap.actualTwinRoomtxt=getCheckBoxTextInAddSpecialRemark(i,8)

			//Smoking Room Check Box display status
			roomDataMap.actualSmokingroomchkBoxDispStatus=getSmokingRoomCheckBoxDisplayStatus(i-1)
			//Smoking Room Check Box Text
			roomDataMap.actualSmokingRoomtxt=getCheckBoxTextInAddSpecialRemark(i,9)

			//Inter Connecting Rooms check box display status
			roomDataMap.actualICRChkBoxDisp=getICRoomCheckBoxDisplayStatus(i-1)
			//Inter connecting Rooms text
			roomDataMap.actualICRoomtxt=getCheckBoxTextInAddSpecialRemark(i,10)
			
			//Adjoining Rooms check box display status
			roomDataMap.actualAdjoiningRoomCheckboxDispStatus=getAdjoingRoomCheckBoxDisplayStatus(i-1)
			//Adjoining Rooms check box text
			roomDataMap.actualAdjoiningRoomtxt=getCheckBoxTextInAddSpecialRemark(i,11)

			//Quiet Rooms check box display status
			roomDataMap.actualQuietRoomCheckBoxDispStatus=getQuietRoomCheckBoxDisplayStatus(i-1)
			//Quiet Rooms check box text
			roomDataMap.actualQuietRoomtxt=getCheckBoxTextInAddSpecialRemark(i,12)

			//Non-Smoking Rooms check box display status
			roomDataMap.actualNonSmokingRoomCheckBoxDispStatus=getNonSmokingRoomCheckBoxDisplayStatus(i-1)
			//Non-Smoking Rooms text
			roomDataMap.actualNonSmokingRoomtxt=getCheckBoxTextInAddSpecialRemark(i,13)

			//Room on lowest floor display status
			roomDataMap.actualRoomOnLowestFlrCheckBoxDispStatus=getRoomOnLowestFlrRoomCheckBoxDisplayStatus(i-1)
			//Room on lowest floor text
			roomDataMap.actualRoomOnLowestFloorRoomtxt=getCheckBoxTextInAddSpecialRemark(i,14)

			//Room on high floor checkbox display status
			roomDataMap.actualRoomOnHighFloorcheckBoxDispStatus=getRoomOnHighestFloorCheckBoxDisplayStatus(i-1)
			//Room on high floor text
			roomDataMap.actualRoomOnHighFloorRoomtxt=getCheckBoxTextInAddSpecialRemark(i,15)

			//Double Room check box display status
			roomDataMap.actualDoubleRoomcheckBoxDispStatus=getDoubleRoomCheckBoxDisplayStatus(i-1)
			//Double Room text
			roomDataMap.actualDoubleRoomtxt=getCheckBoxTextInAddSpecialRemark(i,16)

			//Room with bathtub
			roomDataMap.actualRoomWithBathtubcheckBoxDispStatus=getRoomWithBathTubCheckBoxDisplayStatus(i-1)
			//Room with bathtub
			roomDataMap.actualRoomwithBathTubtxt=getCheckBoxTextInAddSpecialRemark(i,17)

			//Arrival Flight Number check box display status
			roomDataMap.actualArrivalFlightNumDispStatus=getArrivalFlightNumCheckBoxDisplayStatus(i-1)
			//Arrival Flight Number text
			roomDataMap.actualArrivalFlightNumtxt=getCheckBoxTextInAddSpecialRemark(i,7)
						
			//Arrival Flight Number text box existence
			roomDataMap.actualArrivalFlightNumTxtBoxDispStatus=getArrivalFlightNumTextBoxDisplayStatus(i-1)

			//at Hrs
			//at text
			//roomDataMap.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,2)
			roomDataMap.actualAtTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,2)
			//Hrs text
			//roomDataMap.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,3)
			roomDataMap.actualHrsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,3)
			//Hrs select drop box display status
			//roomDataMap.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus(i-1)
			roomDataMap.actualHrsSelectDropBoxinAddRemrksOrCmntScrn=getHrsSelectBoxDisplayStatus(i)
			//mins
			//mins text
			//roomDataMap.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,4)
			roomDataMap.actualminsTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,4)
			//mins select drop box display status
			//roomDataMap.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus(i-1)
			roomDataMap.actualMinsDispStatusinAddRemrksOrCmntScrn=getMinsSelectBoxDisplayStatus(i)
			
			//On Date
			//on text
			//roomDataMap.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(i,5)
			roomDataMap.actualOnTextinAddRemrksOrCmntScrn=getSelectBoxTextInplzProvideSec(1,5)
			//on Day select drop box display status
			//roomDataMap.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus(i-1)
			roomDataMap.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn=getDaySelectBoxDisplayStatus(i)
			//on Month select drop box display status
			//roomDataMap.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus(i-1)
			roomDataMap.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn=getMonthSelectBoxDisplayStatus(i)
		
			
			
			resultData.hotel.itineraryPage.multiRoomBooking.travellers.put("room"+i, travellerDataList)
			resultData.hotel.itineraryPage.multiRoomBooking.travellers.chkBoxStatus.put("room"+i, travellerChkBoxDispStatus)
			resultData.hotel.itineraryPage.multiRoomBooking.put("room"+i, roomDataMap)
			resultData.hotel.itineraryPage.multiRoomBooking.expectedTravellers.put("room"+i, expectedTravellers)
			resultData.hotel.itineraryPage.multiRoomBooking.travellers.expectedChkBoxStatus.put("room"+i, expectedTravellerChkBoxDispStatus)

		}
				

			//Book Page Footer
			
						//Total
						resultData.hotel.itineraryPage.actualTotalTextInAboutToBook=getTotalAndCommissionText(0)
						//Total Amount and currency
						resultData.hotel.itineraryPage.actualTotalAmountAndCurrency=getTotalInConfirmbooking(0)
			
						//Your Commission text
						resultData.hotel.itineraryPage.actualCommissionTextInAboutToBook=getTotalAndCommissionText(2)
						//Your commission amount and currency
						//resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getCommissionValueAndCurrencyInAboutToBookScrn(1)
						resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook=getTotalAndCommissionText(3)
						float compercentage = Float.parseFloat(data.expected.commissionPercent)
						float totalPrice=Float.parseFloat(resultData.hotel.searchResults.priceText_FirstCard)+Float.parseFloat(resultData.hotel.searchResults.priceText_SecondCard)
						DecimalFormat df = new DecimalFormat("####.00");
						resultData.hotel.itineraryPage.totalExpectedPrice=df.format(totalPrice)+" "+clientData.currency
						println("Bfore Booking total tprice: $totalPrice")
						
			
			
						String comValue=getCommissionPercentageValue(totalPrice,compercentage)
						resultData.hotel.itineraryPage.expectedComValue=comValue+" "+clientData.currency
			
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
						
						try{
							//Header Section
							resultData.hotel.itineraryPage.actualHeaderSectionText=getHeaderSectionInBookingConfirmedScrn()

						}catch(Exception e){
							//Header Section
							resultData.hotel.itineraryPage.actualHeaderSectionText="Unable To Read Header Text From UI"

						}
						String emailId=clientData.usernameOrEmail
						//emailId=emailId.toUpperCase()
						//resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt+" "+emailId
						resultData.hotel.itineraryPage.expectedHeaderSectionText=data.expected.headerSectionTxt
			
						//read Booking id
						//resultData.hotel.itineraryPage.retrievedbookingID=getBookingIdFromBookingConfirmed(0)
						//println("Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")
			
						//read Itineary Reference Number & name
						resultData.hotel.itineraryPage.actualreaditinearyIDAndName=getItinearyID(0)
						resultData.hotel.itineraryPage.expecteditinearyIDAndName=resultData.hotel.searchResults.itineraryBuilder.retitineraryId+resultData.hotel.itineraryPage.expectedItnrName
			
						//Capture Departure Date
			
						resultData.hotel.confirmationPage.actualConfimrationDialogDepDate=getBookingDepartDate()
						resultData.hotel.confirmationPage.expectedConfimrationDialogDepDate=depDate.toUpperCase()
						//capture traveller details
						List expectedTravellersConfScreen=[]
						List actualtravellerDataListConfScreen = []
					
						//Capture adults dynamically
						
						for(int k=1;k<=data.input.totalAdults;k++){
							if(k==1) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName+" (Lead Name)")
							//else expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName)
							else if(k==2) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+"Second "+data.expected.lastName+"Second")
							else if(k==3) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+"Third "+data.expected.lastName+"Third")
							else if(k==4) expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.firstName+"Fourth "+data.expected.lastName+"Fourth")
			
						}
						int childIndx=0
						for(int k=data.input.totalAdults+1;k<=(data.input.totalAdults+data.input.children.size());k++)
						{
							//expectedTravellersConfScreen.add(data.expected.title_txt+" "+data.expected.childFirstName+k+" "+data.expected.childLastName+k+" "+"("+data.input.children.getAt(childIndx++)+"yrs)")
							expectedTravellersConfScreen.add(data.expected.childFirstName+" "+data.expected.childLastName+" "+"("+data.input.children.getAt(childIndx++)+"yrs)")

						}
						
						println("Booking Confirmation screen exp travellers:$expectedTravellersConfScreen")
						
						//Capture actual travellers
						for(int j=0;j<data.expected.totalNoOfTravellers;j++)
						{
							actualtravellerDataListConfScreen.add(getTravellerNamesConfirmationDialog(j))
							
						}
						
						println("Booking Confirmation screen actual travellers:$actualtravellerDataListConfScreen")
					
						resultData.hotel.confirmationPage.travellers.put("actualTravellers", actualtravellerDataListConfScreen)
						resultData.hotel.confirmationPage.travellers.put("expectedTravellers", expectedTravellersConfScreen)
						
						
						for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
						{
							Map roomDataMap = [:]
							Map roomExpectedData = data.expected.get("room" + i)
							Map onlyPrice=resultData.hotel.searchResults.price.get("room"+i)
							//Hotel Name
							roomDataMap.actualHotelName=getHotelNameInBookingConfScrn(i)
							
							//Hotel Address
							roomDataMap.actualHotelAddressTxt=getHotelAddressInBookingConfScrn(i)
							
							//Hotel Details
							String tempTxt=getHotelDetailsInBookingConfScrn(i)
							//Capture hotel desc
							List<String> htlDesc=tempTxt.tokenize("\n")
							String hotelDescPaxText=htlDesc.getAt(0)
							List<String> hotelDesPax=hotelDescPaxText.tokenize(",")
							String actualhotelDesc=hotelDesPax.getAt(0)
							roomDataMap.actualhotelDesc=actualhotelDesc
							roomDataMap.expectedhotelDesc="1x "+roomExpectedData.roomDesc
							//Capture PAX
							String actualpaxTxt=hotelDesPax.getAt(1)
							actualpaxTxt=actualpaxTxt.trim()
							roomDataMap.actualpaxTxt=actualpaxTxt
							println("$roomDataMap.actualpaxTxt")
							//Capture Meal Basis Text
							String actualMealBasisTxt=htlDesc.getAt(1)
							roomDataMap.actualMealBasisTxt=actualMealBasisTxt
							println("$roomDataMap.actualMealBasisTxt")
							
							
							//Check in date, Number of Nights
							roomDataMap.actualCheckInDateNumOfNights=getDurationInBookingConfScrn(i)
				
							if (nights >1)
								dur= checkInDt+", "+nights+" nights"
							else dur = checkInDt+", "+nights+" night"
				
							roomDataMap.expectedCheckInDateNumOfNights=dur
							//Please Note txt
							roomDataMap.actualPlzNoteTxtInBookingConfScrn=getPleaseNoteTxtInBookingConfirmedScrn(i)
							
							//Remarks
							roomDataMap.actualRemarksTxtInBookingConfScrn=getRemarksTxtInBookingConfirmScrn(i)
							
							//Price and currency
							roomDataMap.actualSubPriceAndCurrency=getSubTotalsInBookingConfScreen(i)
							
							//Commission Actual
							roomDataMap.actualSubCommissionAndCurrency=getSubCommissionsInBookingConfScreen(i)
							//Commission Expected
							compercentage = Float.parseFloat(data.expected.commissionPercent)
							totalPrice=Float.parseFloat(onlyPrice.price)
							comValue=getCommissionPercentageValue(totalPrice,compercentage)
							roomDataMap.expectedComValue=data.expected.comTxt+" "+comValue+" "+clientData.currency
							
							
							resultData.hotel.confirmationPage.multiroomBooking.put("room"+i, roomDataMap)
						}
						
						//Price and currency
						resultData.hotel.confirmationPage.actualTotalPriceAndCurrency=gettotalPriceAndCurencyBookingConfirmScrn()
						resultData.hotel.confirmationPage.expectedTotalPriceAndCurrency=resultData.hotel.itineraryPage.totalExpectedPrice
						//Commission and currency
						//Commission Amount and Currency
						//resultData.hotel.confirmationPage.actualCommissionAmountAndCurrency=gettotalCommissionBookingConfirmScrn()
						resultData.hotel.confirmationPage.actualCommissionAmountAndCurrency=getGrandtotalCommissionBookingConfirmScrnMultiRoom().replaceAll(" ", "")
						resultData.hotel.confirmationPage.expectedCommissionAmountAndCurrency=data.expected.comTxt+resultData.hotel.itineraryPage.expectedComValue.replaceAll(" ", "")
			
						
						//Close lightbox X function
						resultData.hotel.confirmationPage.actualCloseLightboxDispStatus=getCloseIconDisplayStatus()
			
						//click on Close lightbox X function
						coseBookItenary()
						sleep(7000)
			
						//Get Header Text
						resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt=getHeaderTxtInItemsListScrn(1)
						
						
						//read Booking id
						resultData.hotel.itineraryPage.retrievedbookingID=getBookingIDBookedDetailsScrn(1)
						println("New Code Booking ID: $resultData.hotel.itineraryPage.retrievedbookingID")
					
						for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)	{
							
							Map roomDataMap = [:]
							//Get Booing ID and number
							//roomDataMap.actualBookingID=getBookingIDinBookeddetailsScrn(i)
							roomDataMap.actualBookingID=getBookingIDBookedDetailsScrn(i)
							println("Booking Id Actual->$roomDataMap.actualBookingID")
							//roomDataMap.expectedBookingID="Booking ID: "+resultData.hotel.itineraryPage.retrievedbookingID+"  "+"("+i+" "+"of"+" "+data.input.noOfRooms.toString().toInteger()+")"
							roomDataMap.expectedBookingID=resultData.hotel.itineraryPage.retrievedbookingID
							println("Booking Id expected->$roomDataMap.expectedBookingID")
							
							//Confirmed tab
							roomDataMap.actualconfirmedTabDispStatus=getConfirmedTabDisplayStatus(i)
							//confirm tab text
							roomDataMap.actualStatusTabDispStatus=getStatusInBookedItemsScrn(i)
							
							//Amend tab
							roomDataMap.actualAmendTabDispStatus=getAmendTabDisplayStatus(i)
							//Amend tab text
							roomDataMap.actualAmendTabTxt=getAmendTabTxtInBookedItemsScrn(i)
							
							//Cancel tab
							roomDataMap.actualCancelTabDispStatus=getCancelTabDisplayStatus(i)
							//Cancel tab text
							roomDataMap.actualCancelTabTxt=getCancelTabTxtInBookedItemsScrn(i)
							
							//Hotel Image Display status
							roomDataMap.actualHotelImageDispStatus=getHotelImageDisplayStatus(i)
				
							//Capture Image Src URL
							roomDataMap.actualImageSrcURLinBookedItem=getImageSrcURLInBookedItem(i-1)
							println("$roomDataMap.actualImageSrcURLinBookedItem")
							
							//Hotel Name
							roomDataMap.actualHotelNameTxt=getTransferNameInSuggestedItem(i-1)
							//Caputre Hotel name Src URL
							try{
								roomDataMap.actualHotelNameLinkTxt=getHotelNameSrcURLInBookedItems(i)
								println("$roomDataMap.actualHotelNameLinkTxt")
							}catch(Exception e){
								roomDataMap.actualHotelNameLinkTxt="Unable to Read Hotel Name Src URL From UI"
							}

							
							//Hotel Star Rating
							roomDataMap.actualStarRatingInBookedHotelItem=getRatingForTheHotelInSuggestedItem(i-1)
							
							String descComplTxt=getItinenaryDescreptionInSuggestedItem(i-1)
							List<String> tmpItineraryDescList=descComplTxt.tokenize(",")
							//room description
							String descText=tmpItineraryDescList.getAt(0)
							roomDataMap.actualroomdescTxtInBookedItmsScrn=descText.trim()
							//Pax number requested
							String tempTxt=tmpItineraryDescList.getAt(1)
							List<String> tmpDescList=tempTxt.tokenize(".")
				
							String paxNumDetails=tmpDescList.getAt(0)
							roomDataMap.actualPaxNumInBookedItmsScrn=paxNumDetails.trim()
				
							//Rate plan - meal basis
							String ratePlantxtDetails=tmpDescList.getAt(1)
							roomDataMap.actualratePlanInBookedItmsScrn=ratePlantxtDetails.trim()
							
							//Free cancellation until date
							roomDataMap.actualFreeCnclTxtInbookedItmsScrn=getItinenaryFreeCnclTxtInSuggestedItem(i-1)
							
							//Requested Check in and nights
							roomDataMap.actualDurationTxtInBookedItmsScrn=getItenaryDurationInSuggestedItem(i-1)
							if (nights >1)
							dur= checkInDt+", "+nights+" nights"
							else dur = checkInDt+", "+nights+" night"
			
							roomDataMap.expectedCheckInDateNumOfNights=dur
							
							//commission and percentage
							roomDataMap.actualCommissionAndPercentatgeInBookedItmsScrn=getCommisionAndPercentageInBookeddetailsScrn(i-1)
							
							//Room rate amound and currency
							//roomDataMap.actualPriceAndcurrencyInBookedItmsScrn=getItenaryPriceInSuggestedItem(i-1)
							roomDataMap.actualPriceAndcurrencyInBookedItmsScrn=getItenaryPriceInBookedItem(i)
							//Add a remark or content
							roomDataMap.actualRemarksInBookedItmsScrn=getRemarksInBookeddetailsScrn(i).replaceAll(":","")
							roomDataMap.expectedRemarksInBookedItmsScrn=data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
							roomDataMap.expectedRemarksInBookedItmsScrn=roomDataMap.expectedRemarksInBookedItmsScrn.replaceAll(":","")
							//Edit Icon display status
							roomDataMap.actualEditIconInBookedDetailScrnDispStatus=getEditIconInBookedDetailsScrnDisplayStatus(i)
																
							
							resultData.hotel.itineraryPage.multiRoomBooking.bookedItems.put("room"+i, roomDataMap)
						}
						int travlrCount=1
						for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)	{
						 
						 Map roomDataCancelMap = [:]
						 Map roomExpectedData = data.expected.get("room" + i)
						
						 //roomDataCancelMap.expectedBookingID="Booking ID: "+resultData.hotel.itineraryPage.retrievedbookingID
							roomDataCancelMap.expectedBookingID=resultData.hotel.itineraryPage.retrievedbookingID
						 
						  sleep(3000)
						 scrollToBottomOfThePage()
						 //select above confirmed (1st item) and click on Cancel tab
						 clickOnCancelTabButton(1)
							waitTillLoadingIconDisappears()
							sleep(3000)
			 
						 //Cancel item lightbox display status
						 roomDataCancelMap.actualCancelItemDispStatus=getCancelItemDisplayStatus()
			 
						 //lightbox - Title text , Text - Cancel item
						 roomDataCancelMap.actualCancelItemTxt=getCancellationHeader()
						 
						 //Close lightbox X function
						 roomDataCancelMap.actualClosebuttonDispStatus=overlayCloseButton()
						 
						//hotel image link
						roomDataCancelMap.actualImageIconLinkExistence=getImageIconLinkExistenceInCancelItem()
			
						//capture hotel image link URL
						roomDataCancelMap.actualImageURL=getImageSrcURLInCancelItem()
			
						//hotel name
						roomDataCancelMap.actualHotelNameTxt=getHotelNameInCancelItem()
							try{
								//hotel  link
								roomDataCancelMap.actualHotelNameURL=getHotelNameSrcURLInCancelItems()
							}catch(Exception e){
								//hotel  link
								roomDataCancelMap.actualHotelNameURL="Unable to Read Hotel Name Src URL in Cancel Item  From UI"
							}

			
						//hotel star rating
						roomDataCancelMap.actualHotelStarRating=getStarsRatingInCancelledItem()
			
						//Room - description
						String roomdescComplText=getItinenaryDescreptionInCancelItem()
						List<String> tempItinryDescList=roomdescComplText.tokenize(",")
			
						String roomDscText=tempItinryDescList.getAt(0)
						roomDataCancelMap.actualroomdescTxt=roomDscText.trim()
						//Pax number requested
						String parTxt=tempItinryDescList.getAt(1)
						List<String> parDescList=parTxt.tokenize(".")
			
						String paxNum=parDescList.getAt(0)
						roomDataCancelMap.actualPaxNum=paxNum.trim()
			
						//Rate plan - meal basis
						String ratePlanMealBasistxt=parDescList.getAt(1)
						roomDataCancelMap.actualratePlan=ratePlanMealBasistxt.trim()
			
						//Free cancellation until date
						String completeTxt=getRoomDescPaxRatePlanFreeCnclTxtInCancelItemScrn()
						List<String> tempcompleteTxtList=completeTxt.tokenize(".")
			
						String freeCanclText=tempcompleteTxtList.getAt(2)
						println("Cancel Free Cancl Txt $freeCanclText")
						roomDataCancelMap.actualFreeCnclTxt=freeCanclText.trim()
			
						//requested check in date and nights
						roomDataCancelMap.actualDurationTxt=getItenaryDurationInCancelItem()
			
						//Commission and percentage
						roomDataCancelMap.actualcomPercentTxt=getCommisionAndPercentageInCancelScrn()
			
						//Room rate amount and currency
						roomDataCancelMap.actualPriceAndcurrency=getItenaryPriceInCancelItem()
			
						//display function button No
						roomDataCancelMap.actualNoButtonDispStatus=getYesNoDisplayStatus(1)
						//display function button Yes
						roomDataCancelMap.actualYesButtonDispStatus=getYesNoDisplayStatus(2)
			
						//Click No
						clickNoOnRemoveItenary()
						sleep(3000)
			
						//remove item box disappear
						roomDataCancelMap.actualremoveItemDispStatus=getCancelItemDisplayStatus()
			
						//select above confirmed (1st item) and click on Cancel tab
						clickOnCancelTabButton(1)
						sleep(3000)
						//click Yes
						clickYesOnRemoveItenary()

						waitTillLoadingIconDisappears()
						sleep(5000)
						
						//title text - Unavailable and Cancelled items
						roomDataCancelMap.actualCancelledItmsTxt=getHeaderTxtInUnavailableItemsListScrn()
			
						//Get Booing ID and number
						roomDataCancelMap.actualBookingIDinBookedDetailsScrn=getBookingIDinUnavailableItemsScrn(i)
			
						//Status - cancelled - text
						roomDataCancelMap.actualStatus=getCancelStatusDisplayed(i).trim()
			
			
						//hotel image link
						roomDataCancelMap.actualImageIconLinkExistence=getImageIconLinkExistenceInUnavailableItem(i)
			
						//capture hotel image link URL
						roomDataCancelMap.actualImageURL=getImageSrcURLInUnavailableItem(i)
			
						//hotel name
						roomDataCancelMap.actualHotelNameTxt=getHotelNameInUnavailableItem(i)
							try{
								//hotel  link
								roomDataCancelMap.actualHotelNameURL=getHotelNameSrcURLInUnavailableItems(i)
							}catch(Exception e){
								//hotel  link
								roomDataCancelMap.actualHotelNameURL="Unable To Read Hotel Name Src URL From UI"
							}

			
						//hotel star rating
						roomDataCancelMap.actualHotelStarRating=getStarsRatingInUnavailableItem(i)
			
						//Room - description
						String roomdescCanclText=getItinenaryDescreptionInUnavailableItem(i)
						List<String> tempItinryCanclDescList=roomdescCanclText.tokenize(",")
			
						String roomDscCanclText=tempItinryCanclDescList.getAt(0)
						roomDataCancelMap.actualroomdescTxt=roomDscCanclText.trim()
						//Pax number requested
						String paxCanTxt=tempItinryCanclDescList.getAt(1)
						List<String> paXCanDescList=paxCanTxt.tokenize(".")
			
						String CancpaxNum=paXCanDescList.getAt(0)
						roomDataCancelMap.actualPaxNum=CancpaxNum.trim()
			
						//Rate plan - meal basis
						String canclratePlanMealBasistxt=paXCanDescList.getAt(1)
						roomDataCancelMap.actualratePlan=canclratePlanMealBasistxt.trim()
			
						//Free cancellation until date
						roomDataCancelMap.actualFreeCnclTxt=getItinenaryFreeCancelnInUnavailableItem(i)
			
						//requested check in date and nights
						roomDataCancelMap.actualDurationTxt=getItenaryDurationInUnavailableItem(i)
			
						//Commission and percentage
						roomDataCancelMap.actualcomPercentTxt=getCommisionAndPercentageInUnavailableItem(i)
			
						//Room rate amount and currency
						roomDataCancelMap.actualPriceAndcurrency=getItenaryPriceInUnavailableItem(i)
			
						//Travellers
						//roomDataCancelMap.actualTravellerDetailsTxt=getTravellernamesUnavailableItem(i)
							roomDataCancelMap.actualTravellerDetailsTxt=getTravellernamesUnavailableItem(2)
							//roomDataCancelMap.expectedTravellerDetailsTxt="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+scndTrvlrfirstName+" "+scndTrvlrlastName
						List<String> roomPax=roomExpectedData.totalPax.tokenize(" ")
						int pax=roomPax.getAt(0).toInteger()
						println("Tracking Retrievd pax-> $pax")
						
							travlrCount=travlrCount+1
							if(i==1)
								roomDataCancelMap.expectedTravellerDetailsTxt="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.firstName+"Second"+" "+data.expected.lastName+"Second"
							//roomDataCancelMap.expectedTravellerDetailsTxt="Travellers: "+data.expected.firstName+" "+data.expected.lastName+", "+data.expected.firstName+travlrCount+" "+data.expected.lastName+travlrCount

							else
							//roomDataCancelMap.expectedTravellerDetailsTxt="Travellers: "+data.expected.firstName+travlrCount+" "+data.expected.lastName+travlrCount+", "+data.expected.firstName+(travlrCount+1)+" "+data.expected.lastName+(travlrCount+1)
								roomDataCancelMap.expectedTravellerDetailsTxt="Travellers: "+data.expected.firstName+"Third"+" "+data.expected.lastName+"Third"+", "+data.expected.firstName+"Fourth"+" "+data.expected.lastName+"Fourth"
						
						
						
						roomDataCancelMap.actualRemarksUnavailableItms=getRemarksInUnavailableItem(i)
						roomDataCancelMap.expectedRemarksUnavailableItms=data.expected.pleaseNoteInAbtToBookItemsTxt+" "+data.expected.chkBoxTxt_1
						
						
						 
						 resultData.hotel.removeItemPage.multiRoomBooking.cancelItems.put("room"+i, roomDataCancelMap)
					 }
						
						
					
	}
	
	
	protected def verifyresults(HotelSearchData data, HotelTransferTestResultData resultData)
	{

		/* commented since itinerary builder is removed in 10.3
		//Validate Itinerary builder items added count
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,data.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")
		*/
		if(data.input.noOfRooms.toString().toInteger()>1){
			//Validate First Itinerary builder item added
			assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_FirstTitleCard,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section First Item Name added Actual & Expected don't match")
			//Validate Second Itinerary builder item added
			assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname_SecondTitleCard,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Second Item Name added Actual & Expected don't match")

			//Validate selected item displayed First Card
			assertionEquals(resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms_firstCard,data.expected.cityAreaHotelText,"Itinerary Page, Suggested Items hotel name (First) Actual & Expected don't match")
			//Validate selected item displayed Second Card
			assertionEquals(resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms_secondCard,data.expected.cityAreaHotelText,"Itinerary Page, Suggested Items hotel name (Second) Actual & Expected don't match")

		}
		else{
			//Validate Itinerary builder item added
			assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualItineraryHotelname,data.expected.cityAreaHotelText,"Hotel Search Results Screen Itinerary Builder Section Item Name added Actual & Expected don't match")

			//Validate selected item displayed
			assertionEquals(resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms,data.expected.cityAreaHotelText,"Itinerary Page, Suggested Items hotel name Actual & Expected don't match")

		}
		if(!(data.input.bookItemMultiRoom))
		{
			//Validate Lead Traveller Name - 1st Traveller
			assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerName,resultData.hotel.itineraryPage.expectedleadTravellerName,"Itinerary page, Traveller Details - First or Lead Traveller Name Details actual & expected don't match")

			//Validate Lead Traveller - 1st Traveller - Telephone Number
			assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum,resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum,"Itinerary page, Traveller Details - First or Lead Traveller Telephone Number Details actual & expected don't match")

			//Validate Lead Traveller - 1st Traveller - Email Address
			assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerEmail,resultData.hotel.itineraryPage.expectedleadTravellerEmail,"Itinerary page, Traveller Details - First or Lead Traveller Email Address Details actual & expected don't match")

			//Validate 2nd Traveller Details
			assertionEquals(resultData.hotel.itineraryPage.actualscndTravellerName,resultData.hotel.itineraryPage.expectedscndTravellerName,"Itinerary page, Traveller Details - Second Traveller Details actual & expected don't match")

			if(data.input.children.size()>0){
				//Validate 3rd Traveller Details
				assertionEquals(resultData.hotel.itineraryPage.actualthirdTravellerName,resultData.hotel.itineraryPage.expectedthirdTravellerName,"Itinerary page, Traveller Details - Third Traveller Details actual & expected don't match")

				//Validate 4th Traveller Details
				assertionEquals(resultData.hotel.itineraryPage.actualfourthTravellerName,resultData.hotel.itineraryPage.expectedfourthTravellerName,"Itinerary page, Traveller Details - Fourth Traveller Details actual & expected don't match")
			}
		}
		if((data.input.noOfRooms.toString().toInteger()>1) && (data.input.bookItemMultiRoom)){

			//Validate Lead Traveller Name - 1st Traveller
			assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerName,resultData.hotel.itineraryPage.expectedleadTravellerName,"Itinerary page, Traveller Details - First or Lead Traveller Name Details actual & expected don't match")

			//Validate Lead Traveller - 1st Traveller - Telephone Number
			assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum,resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum,"Itinerary page, Traveller Details - First or Lead Traveller Telephone Number Details actual & expected don't match")

			//Validate Lead Traveller - 1st Traveller - Email Address
			assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerEmail,resultData.hotel.itineraryPage.expectedleadTravellerEmail,"Itinerary page, Traveller Details - First or Lead Traveller Email Address Details actual & expected don't match")

			//Validate 2nd Traveller Details
			assertionEquals(resultData.hotel.itineraryPage.actualscndTravellerName,resultData.hotel.itineraryPage.expectedscndTravellerName,"Itinerary page, Traveller Details - Second Traveller Details actual & expected don't match")

			//Validate 3rd Traveller Details
			assertionEquals(resultData.hotel.itineraryPage.actualthirdTravellerName,resultData.hotel.itineraryPage.expectedthirdTravellerName,"Itinerary page, Traveller Details - Third Traveller Details actual & expected don't match")

			//Validate 4th Traveller Details
			assertionEquals(resultData.hotel.itineraryPage.actualfourthTravellerName,resultData.hotel.itineraryPage.expectedfourthTravellerName,"Itinerary page, Traveller Details - Fourth Traveller Details actual & expected don't match")


			//Validate 5th Traveller Details
			assertionEquals(resultData.hotel.itineraryPage.actualfifthTravellerName,resultData.hotel.itineraryPage.expectedfifthTravellerName,"Itinerary page, Traveller Details - Fifth Traveller Details actual & expected don't match")

			//Validate 6th traveller details
			assertionEquals(resultData.hotel.itineraryPage.actualsixthTravellerName,resultData.hotel.itineraryPage.expectedsixthTravellerName,"Itinerary page, Traveller Details - Sixth Traveller Details actual & expected don't match")

		}

		//Validate saved itinerary name
		assertionEquals(resultData.hotel.itineraryPage.actualSavedItnrName,resultData.hotel.itineraryPage.expectedItnrName,"Itinerary page, Saved Itinerary Name actual & expected don't match")

		//Validate Suggested Items Header text
		assertionEquals(resultData.hotel.itineraryPage.actualSuggestedItemHeaderTxt,data.expected.suggestedItemHeaderTxt,"Itinerary page, Suggested Item header text actual & expected don't match")

		//Validate Question Mark Hover Text
		assertionEquals(resultData.hotel.itineraryPage.actualQstnMarkTxt,data.expected.qstnMarkHoverTxt,"Itinerary page, Suggested Item Question Mark Mouse Hover text actual & expected don't match")

		//Validate No of Items in Suggested Items
		assertionEquals(resultData.hotel.itineraryPage.actualCountOfItemsInSuggstedItms.toString().toInteger(),data.expected.itmsCountinSuggstedItm.toString().toInteger(),"Itinerary page, Suggested Item No Of Items count actual & expected don't match")

		if(data.input.addItems.toString().toBoolean())
		{
			//validate Total label text
			assertionEquals(resultData.hotel.itineraryPage.actualTotalLabelText,data.expected.totalText,"Itinerary page, Non Booked Items Total Text actual & expected don't match")

			//validate total price & currency
			//assertionEquals(resultData.hotel.itineraryPage.actualallItemsTotalAmountAndCurrency,resultData.hotel.itineraryPage.expectedallItemsTotalAmountAndCurrency,"Itinerary page, Non Booked Items Total Amount & Currency actual & expected don't match")

		}

		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			//validate Total label text
			assertionEquals(resultData.hotel.itineraryPage.actualTotalLabelText,data.expected.totalText,"Itinerary page, Non Booked Items Total Text actual & expected don't match")

			//validate total price & currency
			//assertionEquals(resultData.hotel.itineraryPage.actualallItemsTotalAmountAndCurrency,resultData.hotel.itineraryPage.expectedallItemsTotalAmountAndCurrency,"Itinerary page, Non Booked Items Total Amount & Currency actual & expected don't match")

		}


		//Validate <Book> function button
		assertionEquals(resultData.hotel.itineraryPage.actualBookBtnDispStatusInSuggestedItems,data.expected.itinerarybulderSectionStatus,"Itinerary page, Suggested Item Book Button Display Status actual & expected don't match")

		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			//hotel image link - first
			//assertionEquals(resultData.hotel.itineraryPage.actualImageIconLinkExistence_first,data.expected.itinerarybulderSectionStatus,"Itinerary page, Suggested Item Image Link (First) actual & expected don't match")
			//hotel image link - second
			//assertionEquals(resultData.hotel.itineraryPage.actualImageIconLinkExistence_second,data.expected.itinerarybulderSectionStatus,"Itinerary page, Suggested Item Image Link (Second) actual & expected don't match")

			//hotel star rating - first
			assertionEquals(resultData.hotel.itineraryPage.actualHotelStarRating_first,data.expected.starRating,"Itinerary page, Suggested Item Star Rating (First Item) actual & expected don't match")
			//hotel star rating - second
			assertionEquals(resultData.hotel.itineraryPage.actualHotelStarRating_second,data.expected.starRating,"Itinerary page, Suggested Item Star Rating (Second Item) actual & expected don't match")

			//Room - description - first
			assertionEquals(resultData.hotel.itineraryPage.actualroomdescTxt_first,data.expected.room1.roomDesc,"Itinerary page, Suggested Item Room Desc (First Item )actual & expected don't match")
			//Room - description - second
			assertionEquals(resultData.hotel.itineraryPage.actualroomdescTxt_second,data.expected.room2.roomDesc,"Itinerary page, Suggested Item Room Desc (Second Item) actual & expected don't match")

			//Pax number requested - first
			assertionEquals(resultData.hotel.itineraryPage.actualPaxNum_first,data.expected.room1.totalPax,"Itinerary page, Suggested Item Pax (First Item) actual & expected don't match")
			//Pax number requested - second
			assertionEquals(resultData.hotel.itineraryPage.actualPaxNum_second,data.expected.room2.totalPax,"Itinerary page, Suggested Item Pax (Second Item) actual & expected don't match")

			//Rate plan - meal basis - first
			assertionEquals(resultData.hotel.itineraryPage.actualratePlan_first,data.expected.ratePlan,"Itinerary page, Suggested Item Rate Plan (First Item) actual & expected don't match")
			//Rate plan - meal basis - second
			assertionEquals(resultData.hotel.itineraryPage.actualratePlan_second,data.expected.ratePlan,"Itinerary page, Suggested Item Rate Plan (Second Item) actual & expected don't match")

			//Free cancellation until date - first
			assertionEquals(resultData.hotel.itineraryPage.actualFreeCnclTxt_first,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Itinerary page, Suggested Item Free Cancelation Text (First Item) actual & expected don't match")
			//Free cancellation until date - second
			assertionEquals(resultData.hotel.itineraryPage.actualFreeCnclTxt_second,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Itinerary page, Suggested Item Free Cancelation Text (Second Item) actual & expected don't match")

			//inventory availability - first
			assertionEquals(resultData.hotel.itineraryPage.actualInventoryStatus_first,data.expected.invntryStatus,"Itinerary page, Suggested Item Inventory Status (First Item) actual & expected don't match")
			//inventory availability - second
			assertionEquals(resultData.hotel.itineraryPage.actualInventoryStatus_second,data.expected.invntryStatus,"Itinerary page, Suggested Item Inventory Status (Second Item) actual & expected don't match")

			//commission and percentage - first
			assertionEquals(resultData.hotel.itineraryPage.actualcomPercentTxt_first,data.expected.commissionTxt,"Itinerary Page, Non Booked Item Commission Percent (First Item) actual & expected don't match")
			//commission and percentage - second
			assertionEquals(resultData.hotel.itineraryPage.actualcomPercentTxt_second,data.expected.commissionTxt,"Itinerary Page, Non Booked Item Commission Percent (Second Item) actual & expected don't match")

			//requested check in date and nights - first
			assertionEquals(resultData.hotel.itineraryPage.actualDurationTxt_first,resultData.hotel.itineraryPage.expectedDurationTxt,"Itinerary page, Suggested Item Duration (First Item) actual & expected don't match")
			//requested check in date and nights - second
			assertionEquals(resultData.hotel.itineraryPage.actualDurationTxt_second,resultData.hotel.itineraryPage.expectedDurationTxt,"Itinerary page, Suggested Item Duration (Second Item) actual & expected don't match")

			//Room rate amount and currency - first
			assertionEquals(resultData.hotel.itineraryPage.actualPriceAndcurrency_first,resultData.hotel.searchResults.expectedPrice_FirstCard,"Itinerary page, Suggested Item Price & currency (First Item) actual & expected don't match")
			//Room rate amount and currency - second
			assertionEquals(resultData.hotel.itineraryPage.actualPriceAndcurrency_second,resultData.hotel.searchResults.expectedPrice_SecondCard,"Itinerary page, Suggested Item Price & currency (Second Item) actual & expected don't match")


		}
		else{
			//hotel image link
			//assertionEquals(resultData.hotel.itineraryPage.actualImageIconLinkExistence,data.expected.itinerarybulderSectionStatus,"Itinerary page, Suggested Item Image Link actual & expected don't match")

			//hotel star rating
			assertionEquals(resultData.hotel.itineraryPage.actualHotelStarRating,data.expected.starRating,"Itinerary page, Suggested Item Star Rating actual & expected don't match")

			//Room - description
			assertionEquals(resultData.hotel.itineraryPage.actualroomdescTxt,data.expected.roomDesc,"Itinerary page, Suggested Item Room Desc actual & expected don't match")

			//Pax number requested
			assertionEquals(resultData.hotel.itineraryPage.actualPaxNum,data.expected.totalPax,"Itinerary page, Suggested Item Pax actual & expected don't match")

			//Rate plan - meal basis
			assertionEquals(resultData.hotel.itineraryPage.actualratePlan,data.expected.ratePlan,"Itinerary page, Suggested Item Rate Plan actual & expected don't match")

			//Free cancellation until date
			assertionEquals(resultData.hotel.itineraryPage.actualFreeCnclTxt,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Itinerary page, Suggested Item Free Cancelation Text actual & expected don't match")

			//inventory availability
			assertionEquals(resultData.hotel.itineraryPage.actualInventoryStatus,data.expected.invntryStatus,"Itinerary page, Suggested Item Inventory Status actual & expected don't match")

			//commission and percentage
			assertionEquals(resultData.hotel.itineraryPage.actualcomPercentTxt,data.expected.commissionTxt,"Itinerary Page, Non Booked Item Commission Percent actual & expected don't match")

			//requested check in date and nights
			assertionEquals(resultData.hotel.itineraryPage.actualDurationTxt,resultData.hotel.itineraryPage.expectedDurationTxt,"Itinerary page, Suggested Item Duration actual & expected don't match")

			//Room rate amound and currency
			assertionEquals(resultData.hotel.itineraryPage.actualPriceAndcurrency,resultData.hotel.searchResults.expectedPrice,"Itinerary page, Suggested Item Price & currency actual & expected don't match")
		}

	}

	protected def verifyAboutToBookScreenresults(HotelSearchData data, HotelTransferTestResultData resultData)
	{
		//Booking Status
		assertionEquals(resultData.hotel.itineraryPage.actualBookingScreenDispStatus,data.expected.itinerarybulderSectionStatus,"About to Book Screen display status actual & expected don't match")

		//title text
		assertionEquals(resultData.hotel.itineraryPage.actualtitleTxt,data.expected.headertitletxt,"About to Book Screen Title text actual & expected don't match")

		//close icon display status
		assertionEquals(resultData.hotel.itineraryPage.actualCloseIconDispStatus,data.expected.itinerarybulderSectionStatus,"About to Book Screen Close Icon Display status actual & expected don't match")

		if(data.input.noOfRooms.toString().toInteger()>1)
		{
			
			for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
			{
				Map roomDataMap = resultData.hotel.itineraryPage.multiRoomBooking.get("room"+i)
				Map roomExpectedData = data.expected.get("room" + i)
				Map priceAndCurrencyDataMap=resultData.hotel.searchResults.priceAndCurrency.get("room"+i)
				List actualTravellerData=resultData.hotel.itineraryPage.multiRoomBooking.travellers.get("room"+i)
				List actualTravellerChkBoxDispStatus=resultData.hotel.itineraryPage.multiRoomBooking.travellers.chkBoxStatus.get("room"+i)
				List expectedChkBoxDispStatus=resultData.hotel.itineraryPage.multiRoomBooking.travellers.expectedChkBoxStatus.get("room"+i)
				List expectedTravellersList=resultData.hotel.itineraryPage.multiRoomBooking.expectedTravellers.get("room"+i)

				//Validate Hotel Name
				assertionEquals(roomDataMap.actualHotelName,data.expected.cityAreaHotelText,"About to Book Screen hotel name for room $i Actual & Expected don't match")
				//Validate Room Description
				assertionEquals(roomDataMap.actualhotelDesc,roomDataMap.expectedhotelDesc,"About to Book Screen Hotel Desc for room $i Actual & Expected don't match")
				//Validate PAX
				//assertionEquals(roomDataMap.actualpaxTxt,roomExpectedData.totalPax,"About to Book Screen Pax  for room $i Actual & Expected don't match")
				assertContains(roomDataMap.actualpaxTxt,roomExpectedData.totalPaxTxt,"About to Book Screen Pax  for room $i Actual & Expected don't match")
				//Validate Meal Basis Text
				assertionEquals(roomDataMap.actualMealBasisTxt.toString().trim(),data.expected.ratePlan.toString().trim(),"About to Book Screen Meal Basis for room $i Actual & Expected don't match")
				//Validate check in date, night
				//assertionEquals(roomDataMap.actualCheckInDtNightsTxt,roomDataMap.expectedDuration,"About to Book Screen Check in Date, Nights for room $i Actual & Expected don't match")
				assertionEquals(roomDataMap.actualCheckInDtNightsTxt,roomDataMap.expectedCheckInDateTextAndDate,"About to Book Screen Check in Date, for room $i Actual & Expected don't match")

				assertionEquals(roomDataMap.actualNumOfNight,roomDataMap.expectedDuration,"About to Book Screen, Nights, for room $i Actual & Expected don't match")


				//Room rate amount and currency
				assertionEquals(roomDataMap.actualPriceAndComsn,priceAndCurrencyDataMap.expPriceAndCurrncy,"About to Book Screen Price & currency for room $i actual & expected don't match")

				//title text
				assertionEquals(roomDataMap.actualPaxSecTitleTxt,data.expected.paxSecTitletxt,"About to Book Screen Pax Section Title Text for room $i actual & expected don't match")
				
				//Validate  travellers					
				assertionListEquals(actualTravellerData,expectedTravellersList,"About to Book Screen, List Of Travellers for room $i actual & expected don't match")
					
				//Validate travellers check box display status individually
				assertionListEquals(actualTravellerChkBoxDispStatus,expectedChkBoxDispStatus,"About to Book Screen, List Of Travellers Check Box Display Status for room $i actual & expected don't match")
					
				
				//Special condition
				//special condition title text
				assertionEquals(roomDataMap.actualSpecialConditionHeaderTxt,roomDataMap.expectedSpecialConditionHeeaderTxt,"About to Book Special condition header text for room $i actual & expected don't match")
	
				//Pleaes Note Description text
				assertionEquals(roomDataMap.actualSpecialConditionPlzNoteTxt,data.expected.plzNoteTxt,"About to Book Special condition Please Note text for room $i actual & expected don't match")
	
				//CancelCharge - header text
				assertionEquals(roomDataMap.actualCancellationChrgHeaderTxt,data.expected.canclChrgTxt,"About to Book Cancellation Charge Header text for room $i actual & expected don't match")
	
				//Ammendment charge text
				assertionEquals(roomDataMap.actualAmmendmentCharge,data.expected.ammendmentChrgTxt,"About to Book Ammendment Charge Text for room $i actual & expected don't match")
	
				//General Terms & conditions Text
				assertionEquals(roomDataMap.actualTermsAndConditionsText,data.expected.termsAndconditonsTxt,"About to Book General Terms & Conditions Text for room $i actual & expected don't match")
				//T & C Hyperlink
				assertionEquals(roomDataMap.actualTAndCDisplayStatus,data.expected.itinerarybulderSectionStatus,"About to Book General Terms & Conditions Link Displayed for room $i actual & expected don't match")
	
				//T & C Text
				assertionEquals(resultData.hotel.itineraryPage.actualTermsAndCondtTxt.toString().trim(),data.expected.TermsAndCondtnsFooterTxt.toString().trim(),"About to Book General Terms & Conditions Footer Text actual & expected don't match")
	
				//Add a special remark or comment text
				assertionEquals(roomDataMap.actualSpecialRemarkOrCommentTxt,data.expected.remarkTxt,"About to Book Add a special remark or comment text for room $i actual & expected don't match")
	
				//section opens up
				assertionEquals(roomDataMap.actualExpandedStatus,data.expected.itinerarybulderSectionStatus,"About to Book Add a special remark or comment Expand status for room $i actual & expected don't match")
				//section closes up
				assertionEquals(roomDataMap.actualCollapsedStatus,data.expected.notDispStatus,"About to Book Add a special remark or comment Collapse status for room $i actual & expected don't match")
							
				//Title text, please note
				assertionEquals(roomDataMap.actualPleaseNoteTxt,data.expected.pleaseNoteInAbtToBookItemsTxt,"About to Book Add a special remark or comment Pleaes Note text for room $i actual & expected don't match")
	
				//Will Arrive Without Voucher CheckBox Display Status
				assertionEquals(roomDataMap.actualwilArrivWithoutVochrchkBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Will Arrive Without Voucher Check box display status for room $i actual & expected don't match")
				
				//Will Arrive Without Voucher Text
				assertionEquals(roomDataMap.actualwilArrivWithoutVochrTxt,data.expected.chkBoxTxt_1,"About to Book, Add a special remark or comment section, Will Arrive Without Voucher Check box Text for room $i actual & expected don't match")
				
				//Previous night is booked for early morning arrival display status
				assertionEquals(roomDataMap.actualPreviousNightBookChkBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Previous night is booked for early morning arrival Check box display status for room $i actual & expected don't match")
	
				//Previous night text
				assertionEquals(roomDataMap.actualPreviousNightTxt,data.expected.chkBoxTxt_2,"About to Book, Add a special remark or comment section, Previous night is booked for early morning arrival Check box text for room $i actual & expected don't match")
	
				//Late check out checkbox display status
				assertionEquals(roomDataMap.actualLateChkOutDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Late Check OUt checkbox display status  for room $i actual & expected don't match")
	
				//Late check out text
				assertionEquals(roomDataMap.actualLateCheckOutTxt,data.expected.chkBoxTxt_3,"About to Book, Add a special remark or comment section, Late Check out Check box text for room $i actual & expected don't match")
	
				//Late Arrival Checkbox display status
				assertionEquals(roomDataMap.actualLateArrivalDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Late Arrival checkbox display status for room $i actual & expected don't match")
	
				//Late Arrival check box text
				assertionEquals(roomDataMap.actualLateArrivalTxt,data.expected.chkBoxTxt_4,"About to Book, Add a special remark or comment section, Late Arrival Check box text for room $i actual & expected don't match")
	
				//Passengers Are HoneyMooners checkbox display status
				assertionEquals(roomDataMap.actualPassengersAreHoneyMoonersDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Passengers Are Honeymooners checkbox display status for room $i actual & expected don't match")
	
				//Passengers Are HoneyMooners Text
				assertionEquals(roomDataMap.actualPassengersAreHoneyMoonersTxt,data.expected.chkBoxTxt_5,"About to Book, Add a special remark or comment section, Passengers Are Honeymooners checkbox text for room $i actual & expected don't match")
	
				//Early Arrival Display Status
				assertionEquals(roomDataMap.actualEarlyArrivalCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Early Arrival checkbox display status for room $i actual & expected don't match")
	
				//EarlyArrival check box text
				assertionEquals(roomDataMap.actualEarlyArrivalTxt,data.expected.chkBoxTxt_6,"About to Book, Add a special remark or comment section, Early Arrival checkbox text for room $i actual & expected don't match")
	
				//title text - if possible please provide
				assertionEquals(roomDataMap.actualIfPsblePlzProvTxt,data.expected.IfPsblePlzProvTxt,"About to Book, Add a special remark or comment section, If possible, please provide: text for room $i actual & expected don't match")
				
				//Twin Room - Check box display status
				assertionEquals(roomDataMap.actualTwinRoomCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Twin Room Check Box display status for room $i actual & expected don't match")
	
				//Twin Room - check
				assertionEquals(roomDataMap.actualTwinRoomtxt,data.expected.chkBoxTxt_7,"About to Book, Add a special remark or comment section, Twin Room checkbox text for room $i actual & expected don't match")
	
				//Smoking Rook - check box display status
				assertionEquals(roomDataMap.actualSmokingroomchkBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Smoking Room Check Box display status for room $i actual & expected don't match")
	
				//Smoking Room - check box text
				assertionEquals(roomDataMap.actualSmokingRoomtxt,data.expected.chkBoxTxt_8,"About to Book, Add a special remark or comment section, Smoking Room checkbox text for room $i actual & expected don't match")
	
				//Inter Connecting Room - Check box display status
				assertionEquals(roomDataMap.actualICRChkBoxDisp,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Inter Connecting Room Check Box display for room $i status actual & expected don't match")
	
				//Inter Connecting Room - Check box text
				assertionEquals(roomDataMap.actualICRoomtxt,data.expected.chkBoxTxt_9,"About to Book, Add a special remark or comment section, InterConnecting Room checkbox text for room $i actual & expected don't match")
	
				//Adjoining Room - Check box display status
				assertionEquals(roomDataMap.actualAdjoiningRoomCheckboxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Adjoining Room Check Box display status for room $i actual & expected don't match")
				//Adjoining Room - check box text
				assertionEquals(roomDataMap.actualAdjoiningRoomtxt,data.expected.chkBoxTxt_10,"About to Book, Add a special remark or comment section, Adjoining Room checkbox text for room $i actual & expected don't match")
	
				//quiet Rom - check box display status
				assertionEquals(roomDataMap.actualQuietRoomCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Quiet Room Check Box display status for room $i actual & expected don't match")
				//quiet room - check box text
				assertionEquals(roomDataMap.actualQuietRoomtxt,data.expected.chkBoxTxt_11,"About to Book, Add a special remark or comment section, Quiet Room checkbox text for room $i actual & expected don't match")
	
				//Non Smoking check box display status
				assertionEquals(roomDataMap.actualNonSmokingRoomCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Non Smoking Room Check Box display status for room $i actual & expected don't match")
				//Non smoking check box text
				assertionEquals(roomDataMap.actualNonSmokingRoomtxt,data.expected.chkBoxTxt_12,"About to Book, Add a special remark or comment section, Non Smoking Room checkbox text for room $i actual & expected don't match")
	
				//Room on lowest floor - check box display status
				assertionEquals(roomDataMap.actualRoomOnLowestFlrCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Room on Lowest floor Available Check Box display status  for room $i actual & expected don't match")
				//Room on lowest floor text
				assertionEquals(roomDataMap.actualRoomOnLowestFloorRoomtxt,data.expected.chkBoxTxt_13,"About to Book, Add a special remark or comment section, Room on Lowest floor checkbox text for room $i actual & expected don't match")
	
				//Room on high floor check box display status
				assertionEquals(roomDataMap.actualRoomOnHighFloorcheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Room on High floor Available Check Box display status for room $i actual & expected don't match")
				//Room on high floor text box
				assertionEquals(roomDataMap.actualRoomOnHighFloorRoomtxt,data.expected.chkBoxTxt_14,"About to Book, Add a special remark or comment section, Room on High floor checkbox text for room $i actual & expected don't match")
	
				//Double Room check box display status
				assertionEquals(roomDataMap.actualDoubleRoomcheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Double Room Check Box display status for room $i actual & expected don't match")
				//double room text
				assertionEquals(roomDataMap.actualDoubleRoomtxt,data.expected.chkBoxTxt_15,"About to Book, Add a special remark or comment section, Double Room checkbox text for room $i actual & expected don't match")
	
				//room with bath tub checkbox display status
				assertionEquals(roomDataMap.actualRoomWithBathtubcheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Room with bathtub Check Box display status for room $i actual & expected don't match")
				//room with bath tub text
				assertionEquals(roomDataMap.actualRoomwithBathTubtxt,data.expected.chkBoxTxt_16,"About to Book, Add a special remark or comment section, Room with bath tub checkbox text for room $i actual & expected don't match")
	
				//Arrival Flight Num check box display status
				assertionEquals(roomDataMap.actualArrivalFlightNumDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Arrival flight number Check Box display status for room $i actual & expected don't match")
				//Arrival flight num text
				assertionEquals(roomDataMap.actualArrivalFlightNumtxt,data.expected.chkBoxTxt_17,"About to Book, Add a special remark or comment section, Arrival flight number checkbox text for room $i actual & expected don't match")
			
				
				//Arrival Flight number text box display status
				assertionEquals(roomDataMap.actualArrivalFlightNumTxtBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Arrival flight number Text Box display status for room $i actual & expected don't match")
	
				//at Hrs
				//at text
				assertionEquals(roomDataMap.actualAtTextinAddRemrksOrCmntScrn,data.expected.atTxt,"About to Book, Add a special remark or comment section, at  Text for room $i actual & expected don't match")
				//hrs text
				assertionEquals(roomDataMap.actualHrsTextinAddRemrksOrCmntScrn,data.expected.hrsTxt,"About to Book, Add a special remark or comment section, Hrs Text for room $i actual & expected don't match")
				//Hrs select drop box display status
				assertionEquals(roomDataMap.actualHrsSelectDropBoxinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Hrs Select Drop Box display status for room $i actual & expected don't match")
	
				//mins
				//mins text
				assertionEquals(roomDataMap.actualminsTextinAddRemrksOrCmntScrn,data.expected.minsTxt,"About to Book, Add a special remark or comment section, Mins Text actual & expected don't match")
				//mins select drop box display status
				assertionEquals(roomDataMap.actualMinsDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Mins Select Drop Box display status for room $i actual & expected don't match")
	
				//On Date
				//On text
				assertionEquals(roomDataMap.actualOnTextinAddRemrksOrCmntScrn,data.expected.onTxt,"About to Book, Add a special remark or comment section, on Text for room $i actual & expected don't match")
				//on Day select drop box display status
				assertionEquals(roomDataMap.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, on Day select drop box display status for room $i actual & expected don't match")
				//on Month select drop box display status
				assertionEquals(roomDataMap.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, on Month select drop box display status for room $i actual & expected don't match")
	
				
			}
			
			//Total Text
			assertionEquals(resultData.hotel.itineraryPage.actualTotalTextInAboutToBook,data.expected.totalText,"About to Book, Total Text actual & expected don't match")
			//total amount and currency
			assertionEquals(resultData.hotel.itineraryPage.actualTotalAmountAndCurrency,resultData.hotel.itineraryPage.totalExpectedPrice,"About to Book Screen, Amount & currency actual & expected don't match")

			//Your Commission text
			assertionEquals(resultData.hotel.itineraryPage.actualCommissionTextInAboutToBook,data.expected.commissiontext,"About to Book, Commission Text actual & expected don't match")
			//Your commission amount and currency
			assertionEquals(resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook, resultData.hotel.itineraryPage.expectedComValue,"About to Book, Commission Amount & Currency Text actual & expected don't match")
			
			
		}
		else{

			//Hotel Name
			assertionEquals(resultData.hotel.itineraryPage.actualHotelNameInAbtToBkScrn,data.expected.cityAreaHotelText,"About to Book Screen hotel name Actual & Expected don't match")

			//Hotel number of room/name of room (descripton)
			assertionEquals(resultData.hotel.itineraryPage.actualHotelDescTxt,resultData.hotel.itineraryPage.expectedHotelDescText,"About to Book Screen Hotel Desc Actual & Expected don't match")

			//PAX
			//assertionEquals(resultData.hotel.itineraryPage.actualPaxTxt,data.expected.totalPax,"About to Book Screen Pax actual & expected don't match")
			assertContains(resultData.hotel.itineraryPage.actualPaxTxt,data.expected.totalPaxTxt,"About to Book Screen Pax actual & expected don't match")

			//Meal basis
			assertionEquals(resultData.hotel.itineraryPage.actualMealBasisTxt.toString().trim(),data.expected.ratePlan.toString().trim(),"About to Book Screen Meal Basis actual & expected don't match")

			//Check in Date, Nights
			assertionEquals(resultData.hotel.itineraryPage.actualcheckInNightTxt,resultData.hotel.itineraryPage.expectedChkInDateNight,"About to Book Screen Check in Date, Nights actual & expected don't match")

			//No.of Nights
			assertionEquals(resultData.hotel.itineraryPage.actualNumOfNight,resultData.hotel.itineraryPage.expectedNumOfNight,"About to Book Screen, Nights, actual & expected don't match")

			//Room rate amound and currency
			assertionEquals(resultData.hotel.itineraryPage.actualPriceInAboutToBookScrn,resultData.hotel.searchResults.expectedPrice,"About to Book Screen Price & currency actual & expected don't match")

			//Book Page Pax Section

			//title text
			assertionEquals(resultData.hotel.itineraryPage.actualPaxSecTitleTxt,data.expected.paxSecTitletxt,"About to Book Screen Pax Section Titel Text actual & expected don't match")

			//Lead traveller
			assertionEquals(resultData.hotel.itineraryPage.actualfirstTravellerTxt,resultData.hotel.itineraryPage.expectedfirstTravellerTxt,"About to Book Screen First Traveller Name Text actual & expected don't match")

			//tick box - first traveller
			assertionEquals(resultData.hotel.itineraryPage.actualfirstTravellerCheckBoxDispStatus,data.expected.itinerarybulderSectionStatus,"About to Book Screen First Traveller check box actual & expected don't match")

			//second traveller
			assertionEquals(resultData.hotel.itineraryPage.actualsecondTravellerTxt,resultData.hotel.itineraryPage.expectedsecondTravellerTxt,"About to Book Screen Second Traveller Name actual & expected don't match")

			//tick box - second traveller
			assertionEquals(resultData.hotel.itineraryPage.actualsecondTravellerCheckBoxDispStatus,data.expected.itinerarybulderSectionStatus,"About to Book Screen second Traveller check box actual & expected don't match")

			if(data.input.children.size()>0){
				//third traveller
				assertionEquals(resultData.hotel.itineraryPage.actualthirdTravellerTxt,resultData.hotel.itineraryPage.expectedthirdTravellerTxt,"About to Book Screen Third Traveller Name actual & expected don't match")

				//tick box - third traveller
				assertionEquals(resultData.hotel.itineraryPage.actualthirdTravellerCheckBoxDispStatus,data.expected.itinerarybulderSectionStatus,"About to Book Screen third Traveller check box actual & expected don't match")

				//fourth traveller
				assertionEquals(resultData.hotel.itineraryPage.actualfourthTravellerTxt,resultData.hotel.itineraryPage.expectedfourthTravellerTxt,"About to Book Screen Fourth Traveller Name actual & expected don't match")

				//tick box - fourth traveller
				assertionEquals(resultData.hotel.itineraryPage.actualfourthTravellerCheckBoxDispStatus,data.expected.itinerarybulderSectionStatus,"About to Book Screen Fourth Traveller check box actual & expected don't match")
			}

			//Special condition
			//special condition title text
			assertionEquals(resultData.hotel.itineraryPage.actualSpecialConditionHeaderTxt,resultData.hotel.itineraryPage.expectedSpecialConditionHeaderTxt,"About to Book Special condition header text actual & expected don't match")

			//Description text
			assertionEquals(resultData.hotel.itineraryPage.actualDescText,data.expected.plzNoteTxt,"About to Book Special condition Please Note text actual & expected don't match")

			//CancelCharge - header text
			assertionEquals(resultData.hotel.itineraryPage.actualSpecialcancelchrgHeaderTxt,data.expected.canclChrgTxt,"About to Book Cancellation Charge Header text actual & expected don't match")

			if(data.input.ammendmentCharge.toString().toBoolean())
			{
				//Ammednment Title Text
				assertionEquals(resultData.hotel.itineraryPage.actualAmmendmentTitletxt,data.expected.ammendmentTitleTxt,"About to Book Ammendment Header text actual & expected don't match")

				//Name Change Text
				assertionEquals(resultData.hotel.itineraryPage.actualnameChangeTxt,data.expected.nameChangeTxt,"About to Book Ammendment Section, Name Change Text actual & expected don't match")

				//Name Change Date
				assertionEquals(resultData.hotel.itineraryPage.actualnameChangeDate,resultData.hotel.itineraryPage.expectedNameAndAmmendmentChangeDate,"About to Book Ammendment Section, Name Change Date Text actual & expected don't match")

				//Ammendment Text
				assertionEquals(resultData.hotel.itineraryPage.actualammendmentTxt,data.expected.ammendmentTxt,"About to Book Ammendment Section, ammendment Text actual & expected don't match")
				//Ammendment Change Date
				assertionEquals(resultData.hotel.itineraryPage.actualammendmentsDate,resultData.hotel.itineraryPage.expectedNameAndAmmendmentChangeDate,"About to Book Ammendment Section, Ammendment Date Text actual & expected don't match")


				//Early Dep Text
				assertionEquals(resultData.hotel.itineraryPage.actualearlyDepTxt,data.expected.earlyDepTxt,"About to Book Ammendment Section, Early Departure Text actual & expected don't match")
			}

			/*
			 //Date
			 assertionEquals(resultData.hotel.itineraryPage.actualDateFirstRow,resultData.hotel.itineraryPage.ExpectedDateFirstRow,"About to Book Cancellation First Row Date Text")
			 //Date
			 assertionEquals(resultData.hotel.itineraryPage.actualDateSecondRow,resultData.hotel.itineraryPage.ExpectedDateSecondRow,"About to Book Cancellation Second Row Date Text")
			 //Amount First Row
			 assertionEquals(resultData.hotel.itineraryPage.actualAmountFirstRow,data.expected.amountFirstRow,"About to Book Cancellation First Row Text")
			 //Amount Second Row
			 assertionEquals(resultData.hotel.itineraryPage.actualAmountSecondRow,resultData.hotel.itineraryPage.expectedPercentValue,"About to Book Cancellation Second Row Text")
			 */

			//Cancellation charge text
			assertionEquals(resultData.hotel.itineraryPage.actualCancellationChrgTxt,resultData.hotel.searchResults.itineraryBuilder.cancellationChargeRelatedTxt,"About to Book Cancellation Charges Text actual & expected don't match")


			//Ammendment charge text
			assertionEquals(resultData.hotel.itineraryPage.actualAmmendmentCharge,data.expected.ammendmentChrgTxt,"About to Book Ammendment Charge Text actual & expected don't match")

			//General Terms & conditions Text
			assertionEquals(resultData.hotel.itineraryPage.actualTermsAndConditionsText,data.expected.termsAndconditonsTxt,"About to Book General Terms & Conditions Text actual & expected don't match")
			//T & C Hyperlink
			assertionEquals(resultData.hotel.itineraryPage.actualTAndCDisplayStatus,data.expected.itinerarybulderSectionStatus,"About to Book General Terms & Conditions Link Displayed actual & expected don't match")

			//T & C Text
			assertionEquals(resultData.hotel.itineraryPage.actualTermsAndCondtTxt,data.expected.TermsAndCondtnsFooterTxt,"About to Book General Terms & Conditions Footer Text actual & expected don't match")

			//Add a special remark or comment text
			assertionEquals(resultData.hotel.itineraryPage.actualSpecialRemarkOrCommentTxt,data.expected.remarkTxt,"About to Book Add a special remark or comment text actual & expected don't match")

			//section opens up
			assertionEquals(resultData.hotel.itineraryPage.actualExpandedStatus,data.expected.itinerarybulderSectionStatus,"About to Book Add a special remark or comment Expand status actual & expected don't match")
			//section closes up
			assertionEquals(resultData.hotel.itineraryPage.actualCollapsedStatus,data.expected.notDispStatus,"About to Book Add a special remark or comment Collapse status actual & expected don't match")
			//Title text, please note
			assertionEquals(resultData.hotel.itineraryPage.actualPleaseNoteTxt,data.expected.pleaseNoteInAbtToBookItemsTxt,"About to Book Add a special remark or comment Pleaes Note text actual & expected don't match")

			//Will Arrive Without Voucher CheckBox Display Status
			assertionEquals(resultData.hotel.itineraryPage.actualwilArrivWithoutVochrchkBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Will Arrive Without Voucher Check box display status actual & expected don't match")

			//Will Arrive Without Voucher Text
			assertionEquals(resultData.hotel.itineraryPage.actualwilArrivWithoutVochrTxt,data.expected.chkBoxTxt_1,"About to Book, Add a special remark or comment section, Will Arrive Without Voucher Check box Text actual & expected don't match")

			//Previous night display status
			assertionEquals(resultData.hotel.itineraryPage.actualPreviousNightBookChkBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Previous night is booked for early morning arrival Check box display status actual & expected don't match")

			//Previous night text
			assertionEquals(resultData.hotel.itineraryPage.actualPreviousNightTxt,data.expected.chkBoxTxt_2,"About to Book, Add a special remark or comment section, Previous night is booked for early morning arrival Check box text actual & expected don't match")

			//Late check out checkbox display status
			assertionEquals(resultData.hotel.itineraryPage.actualLateChkOutDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Late Check OUt checkbox display status  actual & expected don't match")

			//Late check out text
			assertionEquals( resultData.hotel.itineraryPage.actualLateCheckOutTxt,data.expected.chkBoxTxt_3,"About to Book, Add a special remark or comment section, Late Check out Check box text actual & expected don't match")

			//Late Arrival Checkbox display status
			assertionEquals(resultData.hotel.itineraryPage.actualLateArrivalDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Late Arrival checkbox display status  actual & expected don't match")

			//Late Arrival check box text
			assertionEquals(resultData.hotel.itineraryPage.actualLateArrivalTxt,data.expected.chkBoxTxt_4,"About to Book, Add a special remark or comment section, Late Arrival Check box text actual & expected don't match")

			//Passengers Are HoneyMooners checkbox display status
			assertionEquals(resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Passengers Are Honeymooners checkbox display status  actual & expected don't match")

			//Passengers Are HoneyMooners Text
			assertionEquals(resultData.hotel.itineraryPage.actualPassengersAreHoneyMoonersTxt,data.expected.chkBoxTxt_5,"About to Book, Add a special remark or comment section, Passengers Are Honeymooners checkbox text actual & expected don't match")

			//Early Arrival Display Status
			assertionEquals(resultData.hotel.itineraryPage.actualEarlyArrivalCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Early Arrival checkbox display status actual & expected don't match")

			//EarlyArrival check box text
			assertionEquals(resultData.hotel.itineraryPage.actualEarlyArrivalTxt,data.expected.chkBoxTxt_6,"About to Book, Add a special remark or comment section, Early Arrival checkbox text actual & expected don't match")

			//title text - if possible please provide
			assertionEquals(resultData.hotel.itineraryPage.actualIfPsblePlzProvTxt,data.expected.IfPsblePlzProvTxt,"About to Book, Add a special remark or comment section, If possible, please provide: text actual & expected don't match")

			//Twin Room - Check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualTwinRoomCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Twin Room Check Box display status actual & expected don't match")

			//Twin Room - check
			assertionEquals(resultData.hotel.itineraryPage.actualTwinRoomtxt,data.expected.chkBoxTxt_7,"About to Book, Add a special remark or comment section, Twin Room checkbox text actual & expected don't match")

			//Smoking Rook - check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualSmokingroomchkBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Smoking Room Check Box display status actual & expected don't match")

			//Smoking Room - check box text
			assertionEquals(resultData.hotel.itineraryPage.actualSmokingRoomtxt,data.expected.chkBoxTxt_8,"About to Book, Add a special remark or comment section, Smoking Room checkbox text actual & expected don't match")

			//Inter Connecting Room - Check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualInterConnectingRoomCheckboxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Inter Connecting Room Check Box display status actual & expected don't match")

			//Inter Connecting Room - Check box text
			assertionEquals(resultData.hotel.itineraryPage.actualIntrConnectngRoomtxt,data.expected.chkBoxTxt_9,"About to Book, Add a special remark or comment section, InterConnecting Room checkbox text actual & expected don't match")

			//Adjoining Room - Check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualAdjoiningRoomCheckboxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Adjoining Room Check Box display status actual & expected don't match")
			//Adjoining Room - check box text
			assertionEquals(resultData.hotel.itineraryPage.actualAdjoiningRoomtxt,data.expected.chkBoxTxt_10,"About to Book, Add a special remark or comment section, Adjoining Room checkbox text actual & expected don't match")

			//quiet Rom - check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualQuietRoomCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Quiet Room Check Box display status actual & expected don't match")
			//quiet room - check box text
			assertionEquals(resultData.hotel.itineraryPage.actualQuietRoomtxt,data.expected.chkBoxTxt_11,"About to Book, Add a special remark or comment section, Quiet Room checkbox text actual & expected don't match")

			//Non Smoking check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualNonSmokingRoomCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Non Smoking Room Check Box display status actual & expected don't match")
			//Non smoking check box text
			assertionEquals( resultData.hotel.itineraryPage.actualNonSmokingRoomtxt,data.expected.chkBoxTxt_12,"About to Book, Add a special remark or comment section, Non Smoking Room checkbox text actual & expected don't match")

			//Room on lowest floor - check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualRoomOnLowestFlrCheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Room on Lowest floor Available Check Box display status actual & expected don't match")
			//Room on lowest floor text
			assertionEquals(resultData.hotel.itineraryPage.actualRoomOnLowestFloorRoomtxt,data.expected.chkBoxTxt_13,"About to Book, Add a special remark or comment section, Room on Lowest floor checkbox text actual & expected don't match")

			//Room on high floor check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualRoomOnHighFloorcheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Room on High floor Available Check Box display status actual & expected don't match")
			//Room on high floor text box
			assertionEquals(resultData.hotel.itineraryPage.actualRoomOnHighFloorRoomtxt,data.expected.chkBoxTxt_14,"About to Book, Add a special remark or comment section, Room on High floor checkbox text actual & expected don't match")

			//Double Room check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualDoubleRoomcheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Double Room Check Box display status actual & expected don't match")
			//double room text
			assertionEquals( resultData.hotel.itineraryPage.actualDoubleRoomtxt,data.expected.chkBoxTxt_15,"About to Book, Add a special remark or comment section, Double Room checkbox text actual & expected don't match")

			//room with bath tub checkbox display status
			assertionEquals(resultData.hotel.itineraryPage.actualRoomWithBathtubcheckBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Room with bathtub Check Box display status actual & expected don't match")
			//room with bath tub text
			assertionEquals(  resultData.hotel.itineraryPage.actualRoomwithBathTubtxt,data.expected.chkBoxTxt_16,"About to Book, Add a special remark or comment section, Room with bath tub checkbox text actual & expected don't match")

			//Arrival Flight Num check box display status
			assertionEquals(resultData.hotel.itineraryPage.actualArrivalFlightNumDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Arrival flight number Check Box display status actual & expected don't match")
			//Arrival flight num text
			assertionEquals(resultData.hotel.itineraryPage.actualArrivalFlightNumtxt,data.expected.chkBoxTxt_17,"About to Book, Add a special remark or comment section, Arrival flight number checkbox text actual & expected don't match")

			//Arrival Flight number text box display status
			assertionEquals(resultData.hotel.itineraryPage.actualArrivalFlightNumTxtBoxDispStatus,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Arrival flight number Text Box display status actual & expected don't match")

			//at Hrs
			//at text
			assertionEquals(resultData.hotel.itineraryPage.actualAtTextinAddRemrksOrCmntScrn,data.expected.atTxt,"About to Book, Add a special remark or comment section, at  Text actual & expected don't match")
			//hrs text
			assertionEquals(resultData.hotel.itineraryPage.actualHrsTextinAddRemrksOrCmntScrn,data.expected.hrsTxt,"About to Book, Add a special remark or comment section, Hrs Text actual & expected don't match")
			//Hrs select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.actualHrsSelectDropBoxinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Hrs Select Drop Box display status actual & expected don't match")

			//mins
			//mins text
			assertionEquals(resultData.hotel.itineraryPage.actualminsTextinAddRemrksOrCmntScrn,data.expected.minsTxt,"About to Book, Add a special remark or comment section, Mins Text actual & expected don't match")
			//mins select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.actualMinsDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, Mins Select Drop Box display status actual & expected don't match")

			//On Date
			//On text
			assertionEquals(resultData.hotel.itineraryPage.actualOnTextinAddRemrksOrCmntScrn,data.expected.onTxt,"About to Book, Add a special remark or comment section, on Text actual & expected don't match")
			//on Day select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, on Day select drop box display status actual & expected don't match")
			//on Month select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"About to Book, Add a special remark or comment section, on Month select drop box display status actual & expected don't match")

			//Total Text
			assertionEquals(resultData.hotel.itineraryPage.actualTotalTextInAboutToBook,data.expected.totalText,"About to Book, Total Text actual & expected don't match")
			//total amount and currency
			assertionEquals(resultData.hotel.itineraryPage.actualTotalAmountAndCurrency,resultData.hotel.searchResults.expectedPrice,"About to Book Screen, Amount & currency actual & expected don't match")

			//Your Commission text
			assertionEquals(resultData.hotel.itineraryPage.actualCommissionTextInAboutToBook,data.expected.commissiontext,"About to Book, Commission Text actual & expected don't match")
			//Your commission amount and currency
			assertionEquals(resultData.hotel.itineraryPage.actualCommissionValueInAboutToBook, resultData.hotel.itineraryPage.expectedComValue,"About to Book, Commission Amount & Currency Text actual & expected don't match")
		}

	}

	protected def verifyBookingConfirmationScreenresults(HotelSearchData data, HotelTransferTestResultData resultData)
	{
		//Booking Confirmation Display Status
		assertionEquals(resultData.hotel.itineraryPage.actualBookingconfirmaitonDispStatus,data.expected.yesDispStatus,"Booking Confirmation Screen display status actual & expected don't match")

		//Title Text - Booking confirmed
		//assertionEquals(resultData.hotel.itineraryPage.actualBookingConfirmedTitleText,data.expected.bookingConfrmTitleTxt,"Booking Confirmation Screen, Title Text actual & expected don't match")

		//Header Section
		assertionEquals(resultData.hotel.itineraryPage.actualHeaderSectionText,resultData.hotel.itineraryPage.expectedHeaderSectionText,"Booking Confirmation Screen, Header Text actual & expected don't match")

		//Itinerary Reference Number & Name
		assertionEquals(resultData.hotel.itineraryPage.actualreaditinearyIDAndName,resultData.hotel.itineraryPage.expecteditinearyIDAndName,"Booking Confirmation Screen, Itinerary ID Text actual & expected don't match")

		//Departure Date
		assertionEquals(resultData.hotel.confirmationPage.actualConfimrationDialogDepDate,resultData.hotel.confirmationPage.expectedConfimrationDialogDepDate,"Booking Confirmation Screen, Departure Date actual & expected don't match")

		
		if(data.input.noOfRooms.toString().toInteger()>1 )
		{
			List actualTravellerData=resultData.hotel.confirmationPage.travellers.get("actualTravellers")
			List expectedTravelerData=resultData.hotel.confirmationPage.travellers.get("expectedTravellers")
			
			
			//Validate  travellers
			//assertionListEquals(actualTravellerData,expectedTravelerData,"Booking Confirmation Screen, List Of Travellers  actual & expected don't match")
			for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
			{
				Map roomDataMap = resultData.hotel.confirmationPage.multiroomBooking.get("room"+i)
				Map roomExpectedData = data.expected.get("room" + i)
				Map priceDataMap=resultData.hotel.searchResults.priceAndCurrency.get("room"+i)
				
				//Hotel Name
				assertionEquals(roomDataMap.actualHotelName,data.expected.cityAreaHotelText,"Booking Confirmation Screen, Hotel Name for room $i actual & expected don't match")
		
				//Hotel Address
				assertionEquals(roomDataMap.actualHotelAddressTxt,data.expected.hotelAddrsTxt,"Booking Confirmation Screen, Hotel Name for room $i actual & expected don't match")
		
				//number of room/name of room (descripton)
				assertionEquals(roomDataMap.actualhotelDesc,roomDataMap.expectedhotelDesc,"Booking Confirmation Screen, number of room/name of room (descripton) for room $i actual & expected don't match")
		
				//PAX
				//assertionEquals(roomDataMap.actualpaxTxt,roomExpectedData.totalPax,"Booking Confirmation Screen, PAX for room $i actual & expected don't match")
				assertContains(roomDataMap.actualpaxTxt,roomExpectedData.totalPax,"Booking Confirmation Screen, PAX for room $i actual & expected don't match")


				//Meal Basis
				assertionEquals(roomDataMap.actualMealBasisTxt,data.expected.ratePlan,"Booking Confirmation Screen, Meal Basis for room $i  actual & expected don't match")
		
				//Check in date, Number of Nights
				assertionEquals(roomDataMap.actualCheckInDateNumOfNights,roomDataMap.expectedCheckInDateNumOfNights,"Booking Confirmation Screen, Check In Date Number of Nights for room $i actual & expected don't match")
		
				//Please Note txt
				assertionEquals(roomDataMap.actualPlzNoteTxtInBookingConfScrn,data.expected.pleaseNoteInAbtToBookItemsTxt,"Booking Confirmation Screen, Please Note Text for room $i actual & expected don't match")
		
				//Remarks
				assertionEquals(roomDataMap.actualRemarksTxtInBookingConfScrn,data.expected.chkBoxTxt_1,"Booking Confirmation Screen, Will arrive without voucher for room $i  actual & expected don't match")
				
				//Price and currency
				assertionEquals(roomDataMap.actualSubPriceAndCurrency,priceDataMap.expPriceAndCurrncy,"Booking Confirmation Screen, Price and currency for room $i  actual & expected don't match")
				
				//Commision and currency
				assertionEquals(roomDataMap.actualSubCommissionAndCurrency,roomDataMap.expectedComValue,"Booking Confirmation Screen, Commission and currency for room $i  actual & expected don't match")
				
				
			}
			//Room Rate Amount and currency
			assertionEquals(resultData.hotel.confirmationPage.actualTotalPriceAndCurrency,resultData.hotel.confirmationPage.expectedTotalPriceAndCurrency,"Booking Confirmation Screen, Room Rate Amount & Currency actual & expected don't match")
	
			//Commission Amount and currency
			//assertionEquals(resultData.hotel.confirmationPage.actualCommissionAmountAndCurrency,resultData.hotel.confirmationPage.expectedCommissionAmountAndCurrency,"Booking Confirmation Screen, Commission Amount & Currency actual & expected don't match")
			
			//close lightbox x function display status
			assertionEquals(resultData.hotel.confirmationPage.actualCloseLightboxDispStatus,data.expected.yesDispStatus,"Booking Confirmation Screen, Close lightbox X function actual & expected don't match")
			
		}
		else{
		//First Traveller Name
		assertionEquals(resultData.hotel.confirmationPage.actualfirstTravellerName,resultData.hotel.itineraryPage.expectedleadTravellerName,"Booking Confirmation Screen, First Traveller Name actual & expected don't match")
		//Second Traveller name
		assertionEquals(resultData.hotel.confirmationPage.actualsecondTravellerName,resultData.hotel.itineraryPage.expectedscndTravellerName,"Booking Confirmation Screen, Second Traveller Name actual & expected don't match")

		if(data.input.children.size()>0){
			//third Traveller name
			assertionEquals(resultData.hotel.confirmationPage.actualthirdTravellerName.replaceAll(" ",""),resultData.hotel.itineraryPage.expectedthirdTravellerName.replaceAll(" ",""),"Booking Confirmation Screen, Thrid Traveller Name actual & expected don't match")
			//Fourth Traveller name
			assertionEquals(resultData.hotel.confirmationPage.actualfourthTravellerName.replaceAll(" ",""),resultData.hotel.itineraryPage.expectedfourthTravellerName.replaceAll(" ",""),"Booking Confirmation Screen, Fourth Traveller Name actual & expected don't match")
		}

		//Hotel Name
		assertionEquals(resultData.hotel.confirmationPage.actualHotelName,data.expected.cityAreaHotelText,"Booking Confirmation Screen, Hotel Name actual & expected don't match")

		//Hotel Address
		assertionEquals(resultData.hotel.confirmationPage.actualHotelAddressTxt,data.expected.hotelAddrsTxt,"Booking Confirmation Screen, Hotel Name actual & expected don't match")

		//number of room/name of room (descripton)
		//assertionEquals(resultData.hotel.confirmationPage.actualHotelDescTxt,resultData.hotel.confirmationPage.expHotelDescTxt,"Booking Confirmation Screen, number of room/name of room (descripton) actual & expected don't match")

		//PAX
		//assertionEquals(resultData.hotel.confirmationPage.actualPaxTxt,data.expected.totalPax,"Booking Confirmation Screen, PAX actual & expected don't match")
		assertContains(resultData.hotel.confirmationPage.actualPaxTxt,data.expected.totalPax,"Booking Confirmation Screen, PAX actual & expected don't match")

		//Meal Basis
		assertionEquals(resultData.hotel.confirmationPage.actualMealBasisTxt,data.expected.ratePlan,"Booking Confirmation Screen, Meal Basis actual & expected don't match")

		//Check in date, Number of Nights
		assertionEquals(resultData.hotel.confirmationPage.actualCheckInDateNumOfNights,resultData.hotel.confirmationPage.expectedCheckInDateNumOfNights,"Booking Confirmation Screen, Check In Date Number of Nights actual & expected don't match")

		//Please Note txt
		assertionEquals(resultData.hotel.confirmationPage.actualPlzNoteTxtInBookingConfScrn,data.expected.pleaseNoteInAbtToBookItemsTxt,"Booking Confirmation Screen, Please Note Text actual & expected don't match")

		//Remarks
		assertionEquals(resultData.hotel.confirmationPage.expectedRemarksTxtInBookingConfScrn,data.expected.chkBoxTxt_1,"Booking Confirmation Screen, Will arrive without voucher actual & expected don't match")
		//Room Rate Amount and currency
		assertionEquals(resultData.hotel.confirmationPage.actualRoomRateAmountAndCurrency,resultData.hotel.confirmationPage.expectedRoomRateAmountCurency,"Booking Confirmation Screen, Room Rate Amount & Currency actual & expected don't match")

		//Commission Amount and currency
		//assertionEquals(resultData.hotel.confirmationPage.actualCommissionAmountAndCurrency,resultData.hotel.confirmationPage.expectedCommissionAmountAndCurrency,"Booking Confirmation Screen, Commission Amount & Currency actual & expected don't match")

		//close lightbox x function display status
		assertionEquals(resultData.hotel.confirmationPage.actualCloseLightboxDispStatus,data.expected.yesDispStatus,"Booking Confirmation Screen, Close lightbox X function actual & expected don't match")
		}

	}
	protected def verifyBookedItemsScreenresults(HotelSearchData data, HotelTransferTestResultData resultData)
	{

		//Title Text - Booked Items
		assertionEquals(resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt,data.expected.bookedItemHeaderTxt,"Booked Items Screen, Title text booked items actual & expected don't match")

		if(data.input.noOfRooms.toString().toInteger()>1 && (data.input.bookItemMultiRoom))
		{
			
			for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
			{
				Map roomDataMap = resultData.hotel.itineraryPage.multiRoomBooking.bookedItems.get("room"+i)
				Map roomExpectedData = data.expected.get("room" + i)
				Map priceDataMap=resultData.hotel.searchResults.priceAndCurrency.get("room"+i)
				//Booking ID & Number
				assertionEquals(roomDataMap.actualBookingID,roomDataMap.expectedBookingID,"Booked Items Screen, Booking ID and number for room $i actual & expected don't match")
		
				//Confirmed tab
				assertionEquals(roomDataMap.actualconfirmedTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Confirmed or Pending Tab display status for room $i actual & expected don't match")
				//Confirmed tab text
				assertionEquals(roomDataMap.actualStatusTabDispStatus,data.expected.statusTabTxt,"Booked Items Screen, Confirmed Or Pending Tab Text status for room $i actual & expected don't match")
		
				//Amend tab
				assertionEquals(roomDataMap.actualAmendTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Amend Tab display status for room$i actual & expected don't match")
				//Amend tab text
				assertionEquals(roomDataMap.actualAmendTabTxt,data.expected.amendTabTxt,"Booked Items Screen, Amend Tab Text for room$i actual & expected don't match")
		
				//Cancel tab
				assertionEquals(roomDataMap.actualCancelTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Cancel Tab display status for room $i actual & expected don't match")
				//Cancel tab text
				assertionEquals(roomDataMap.actualCancelTabTxt,data.expected.cancelTabTxt,"Booked Items Screen, Cancel Tab Text for room $i actual & expected don't match")
				//Hotel Image Display Status
				assertionEquals(roomDataMap.actualHotelImageDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Hotel Image display status for room $i actual & expected don't match")

				//Hotel Name
				assertionEquals(roomDataMap.actualHotelNameTxt,data.expected.cityAreaHotelText,"Booked Items Screen, Booked Items Section - Hotel Name Text for room $i actual & expected don't match")
		
				//Hotel Star Rating
				assertionEquals(roomDataMap.actualStarRatingInBookedHotelItem,data.expected.starRating,"Booked Items Screen, Booked Items Section - Hotel Star Rating Text for room $i actual & expected don't match")
				
				//Hotel Room Desc
				assertionEquals(roomDataMap.actualroomdescTxtInBookedItmsScrn,roomExpectedData.roomDesc,"Booked Items Screen, Booked Items Section - Room Desc Text for room $i actual & expected don't match")
		
				//PAX number
				assertionEquals(roomDataMap.actualPaxNumInBookedItmsScrn,roomExpectedData.totalPax,"Booked Items Screen, Booked Items Section - PAX Number Text for room $i actual & expected don't match")
		
				//Rate Plan
				assertionEquals(roomDataMap.actualratePlanInBookedItmsScrn,data.expected.ratePlan,"Booked Items Screen, Booked Items Section - Rate Plan/Meal Basis Text for room $i actual & expected don't match")
		
				//Free Cancel Text
				assertionEquals(roomDataMap.actualFreeCnclTxtInbookedItmsScrn,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Booked Items Screen, Booked Items Section - Free Cancel Text for room $i actual & expected don't match")
		
				//Requested Check in and nights
				assertionEquals(roomDataMap.actualDurationTxtInBookedItmsScrn,roomDataMap.expectedCheckInDateNumOfNights,"Booked Items Screen, Booked Items Section - Requested check in Date & nights Text for room$i actual & expected don't match")
		
				//Commission and percentage
				assertionEquals(roomDataMap.actualCommissionAndPercentatgeInBookedItmsScrn,data.expected.commissionTxt,"Booked Items Screen, Booked Items Section - Commission And Percentage text for room$i actual & expected don't match")
		
				//Room Rate Amount and currency
				assertionEquals(roomDataMap.actualPriceAndcurrencyInBookedItmsScrn,priceDataMap.expPriceAndCurrncy,"Booked Items Screen, Booked Items Section - Room Rate And Currency Text room$i actual & expected don't match")
		
				//Add Remarks or Comment
				assertionEquals(roomDataMap.actualRemarksInBookedItmsScrn,roomDataMap.expectedRemarksInBookedItmsScrn,"Booked Items Screen, Booked Items Section - Please Note Text and special request text for room$i actual & expected don't match")
	
				//Edit icon display status
				assertionEquals(roomDataMap.actualEditIconInBookedDetailScrnDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Edit Icon display status for room$i actual & expected don't match")
	
				//Add comment or special request lighbox display
				assertionEquals(roomDataMap.actualHeaderTxtInAddCmntOrSpeclReqScrnDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request lighbox display status for room $i actual & expected don't match")
				//Add comment or special request lighbox text
				assertionEquals(roomDataMap.actualHeaderTxtInAddCmntOrSpeclReqScrn,data.expected.addCmntOrSpclReqTxt,"Booked Items Screen, Booked Items Section - Add comment or special request text for room $i actual & expected don't match")
	
				
				//Select from the choices below or write your own request - text
				assertionEquals(roomDataMap.actualHeaderTxtSelChoicesBelowInAddCmntOrSpeclReqScrn,data.expected.selectChoicesTxt,"Booked Items Screen, Booked Items Section - Select from the choices below or write your own request text actual & expected don't match")
				
				//Please Note text
				assertionEquals(roomDataMap.actualHeaderTxtPlzNoteInAddcmntOrSclReqScrn,data.expected.pleaseNoteInAbtToBookItemsTxt,"Booked Items Screen, Booked Items Section, Add comment or special request screen - Please Note text for room $i actual & expected don't match")
	
				//Will arrive without voucher- Should display with box ticked.
				assertionEquals(roomDataMap.actualwilArrivWithoutVochrCheckBoxStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - With Or Without Vouched Tick Box Selected status for room $i actual & expected don't match")
	
				//Will arrive without voucehr - text
				assertionEquals(roomDataMap.actualwilArrivWithoutVochrTxt,data.expected.chkBoxTxt_1,"Booked Items Screen, Booked Items Section - Add comment or special request screen - With Or Without Voucher Text for room $i actual & expected  don't match")
	
				//Untick validation
				//assertionEquals(roomDataMap.actualCheckBoxTickStatusAfterClick,data.expected.notDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - With Or Without Voucher Untick status for room $i actual & expected don't match")
	
				//Previous night is booked for early morning arrival display status
				assertionEquals(roomDataMap.actualPreviousNightBookChkBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Previous night is booked for early morning arrival check box display status for room $i actual & expected don't match")
	     		//Previous night is booked for early morning arrival text
				assertionEquals(roomDataMap.actualPreviousNightTxt,data.expected.chkBoxTxt_2,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Previous night is booked for early morning arrival text for room $i actual & expected don't match")
	
				//Late check out check box display status
				assertionEquals(roomDataMap.actualLateChkOutDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late check out check box display status for room $i actual & expected don't match")
				//Late check out Text
				assertionEquals(roomDataMap.actualLateCheckOutTxt,data.expected.chkBoxTxt_3,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late check out text for room $i actual & expected don't match")
	
				//Late Arrival check box display status
				assertionEquals(roomDataMap.actualLateArrivalDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late Arrival check box display status for room $i actual & expected don't match")
				//Late Arrival text box
				assertionEquals(roomDataMap.actualLateArrivalTxt,data.expected.chkBoxTxt_4,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late Arrival text for room $i actual & expected don't match")
	
				//Passengers Are HoneyMooners check box Display Status
				assertionEquals(roomDataMap.actualPassengersAreHoneyMoonersDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Passengers Are HoneyMooners check box Display status for room $i actual & expected don't match")
				//Passengers Are HoneyMooners Text
				assertionEquals(roomDataMap.actualPassengersAreHoneyMoonersTxt,data.expected.chkBoxTxt_5,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Passengers Are HoneyMooners text for room $i actual & expected don't match")
				
				//Early Arrival Disp Status
				assertionEquals(roomDataMap.actualEarlyArrivalCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Early Arrival Disp Status for room $i actual & expected don't match")
				//Early Arrival Text
				assertionEquals(roomDataMap.actualEarlyArrivalTxt,data.expected.chkBoxTxt_6,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Early Arrival text for room $i actual & expected don't match")
	
				//If possible, please provide: text
				assertionEquals(roomDataMap.actualHeaderTxtIfPssblPlzProvideTxt,data.expected.IfPsblePlzProvTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - If possible, please provide text for room $i actual & expected don't match")
	
				//Twin Room check box display status
				assertionEquals(roomDataMap.actualTwinRoomCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Twin Room check box display Status for room $i actual & expected don't match")
				//Twin Room Text Box
				assertionEquals(roomDataMap.actualTwinRoomtxt,data.expected.chkBoxTxt_7,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Twin Room text for room $i actual & expected don't match")
	
				//Smoking Room Check Box display status
				assertionEquals(roomDataMap.actualSmokingroomchkBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Smoking Room Check Box display Status for room $i actual & expected don't match")
				//Smoking Room Check Box Text
				assertionEquals(roomDataMap.actualSmokingRoomtxt,data.expected.chkBoxTxt_8,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Smoking Room  text for room $i actual & expected don't match")
	
				//Inter Connecting Rooms check box display status
				assertionEquals(roomDataMap.actualInterConnectingRoomCheckboxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Inter Connecting Rooms check box display Status for room $i actual & expected don't match")
				//Inter connecting Rooms text
				assertionEquals(roomDataMap.actualIntrConnectngRoomtxt,data.expected.chkBoxTxt_9,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Inter connecting Rooms text for room $i actual & expected don't match")
	
				//Adjoining Rooms check box display status
				assertionEquals(roomDataMap.actualAdjoiningRoomCheckboxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Adjoining Rooms check box display Status for room $i actual & expected don't match")
				//Adjoining Rooms check box text
				assertionEquals(roomDataMap.actualAdjoiningRoomtxt,data.expected.chkBoxTxt_10,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Adjoining Rooms check box text for room $i actual & expected don't match")
	
				//Quiet Rooms check box display status
				assertionEquals(roomDataMap.actualQuietRoomCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Quiet Rooms check box  display Status for room $i actual & expected don't match")
				//Quiet Rooms check box text
				assertionEquals(roomDataMap.actualQuietRoomtxt,data.expected.chkBoxTxt_11,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Quiet Rooms check  box text for room $i actual & expected don't match")
	
				//Non-Smoking Rooms check box display status
				assertionEquals(roomDataMap.actualNonSmokingRoomCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Non-Smoking Rooms check box display Status for room $i actual & expected don't match")
				//Non-Smoking Rooms text
				assertionEquals(roomDataMap.actualNonSmokingRoomtxt,data.expected.chkBoxTxt_12,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Non-Smoking Rooms check  box text actual & expected don't match")
	
				//Room on lowest floor display status
				assertionEquals(roomDataMap.actualRoomOnLowestFlrCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on lowest floor display Status for room $i actual & expected don't match")
				//Room on lowest floor text
				assertionEquals(roomDataMap.actualRoomOnLowestFloorRoomtxt,data.expected.chkBoxTxt_13,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on lowest floor check  box text for room $i actual & expected don't match")
	
				//Room on high floor checkbox display status
				assertionEquals(roomDataMap.actualRoomOnHighFloorcheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on high floor checkbox display Status for room $i actual & expected don't match")
				//Room on high floor text
				assertionEquals(roomDataMap.actualRoomOnHighFloorRoomtxt,data.expected.chkBoxTxt_14,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on high floor check  box text for room $i actual & expected don't match")
	
				//Double Room check box display status
				assertionEquals(roomDataMap.actualDoubleRoomcheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Double Room check display Status for room $i actual & expected don't match")
				//Double Room text
				assertionEquals(roomDataMap.actualDoubleRoomtxt,data.expected.chkBoxTxt_15,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Double Room check  box text for room $i actual & expected don't match")
	
				//Room with bathtub check box display status
				assertionEquals(roomDataMap.actualRoomWithBathtubcheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room with bathtub display Status for room $i actual & expected don't match")
				//Room with bathtub text
				assertionEquals(roomDataMap.actualRoomwithBathTubtxt,data.expected.chkBoxTxt_16,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room with bath tub text for room $i actual & expected don't match")
	
				//Arrival Flight Number check box display status
				assertionEquals(roomDataMap.actualArrivalFlightNumDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Arrival Flight Number check box display Status for room $i actual & expected don't match")
				//Arrival Flight Number text
				assertionEquals(roomDataMap.actualArrivalFlightNumtxt,data.expected.chkBoxTxt_17,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Arrival Flight Number text for room $i actual & expected don't match")
	
				//Arrival Flight Number text box existence
				assertionEquals(roomDataMap.actualArrivalFlightNumTxtBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Arrival Flight Number text box display Status for room $i actual & expected don't match")
	
				// Hrs
				//at text
				assertionEquals(roomDataMap.actualAtTextinAddRemrksOrCmntScrn,data.expected.atTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - at text for room $i actual & expected don't match")
				//Hrs text
				assertionEquals(roomDataMap.actualHrsTextinAddRemrksOrCmntScrn,data.expected.hrsTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Hrs text for room $i actual & expected don't match")
				//Hrs select drop box display status
				assertionEquals(roomDataMap.actualHrsSelectDropBoxinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Hrs select drop box display Status for room $i actual & expected don't match")
	
				//Mins
				//mins text
				assertionEquals(roomDataMap.actualminsTextinAddRemrksOrCmntScrn,data.expected.minsTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Mins text for room $i actual & expected don't match")
				//mins select drop box display status
				assertionEquals(roomDataMap.actualMinsDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - mins select drop box display Status for room $i actual & expected don't match")
	
				//On Date
				//on text
				assertionEquals(roomDataMap.actualOnTextinAddRemrksOrCmntScrn,data.expected.onTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - On text for room $i actual & expected don't match")
				//on Day select drop box display status
				assertionEquals(roomDataMap.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - on Day select drop box display Status for room $i actual & expected don't match")
				//on Month select drop box display status
				assertionEquals(roomDataMap.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - on Month select drop box display Status for room $i actual & expected don't match")
	
				//Smoking Room check box ticked or not
				assertionEquals(roomDataMap.actualSmokingRoomCheckBoxCheckedStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Smoking Room check box display Status after tick for room $i actual & expected don't match")
	
				//Hotel Image Display Status
				assertionEquals(roomDataMap.actualHotelImgDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Hotel Image display status after edit for room $i actual & expected don't match")

				//Hotel Name
				assertionEquals(roomDataMap.actualHtlNameTxt,data.expected.cityAreaHotelText,"Booked Items Screen, Booked Items Section - Hotel Name Text after edit for room $i actual & expected don't match")
		
				//Hotel Star Rating
				assertionEquals(roomDataMap.actualStarRatingBookedHotelItem,data.expected.starRating,"Booked Items Screen, Booked Items Section - Hotel Star Rating Text after edit for room $i actual & expected don't match")
				
				//Hotel Room Desc
				assertionEquals(roomDataMap.actualroomdescTextBookedItmsScrn,roomExpectedData.roomDesc,"Booked Items Screen, Booked Items Section - Room Desc Text after edit for room $i actual & expected don't match")
		
				//PAX number
				assertionEquals(roomDataMap.actualPaxNumBookedItmsScrn,roomExpectedData.totalPax,"Booked Items Screen, Booked Items Section - PAX Number Text after edit for room $i actual & expected don't match")
		
				//Rate Plan
				assertionEquals(roomDataMap.actualratePlanBookedItmsScrn,data.expected.ratePlan,"Booked Items Screen, Booked Items Section - Rate Plan/Meal Basis Text after edit for room $i actual & expected don't match")
		
				//Free Cancel Text
				assertionEquals(roomDataMap.actualFreeCancelTxtbookedItmsScrn,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Booked Items Screen, Booked Items Section - Free Cancel Text after edit for room $i actual & expected don't match")
		
				//Requested Check in and nights
				assertionEquals(roomDataMap.actualDurationTextBookedItmsScrn,roomDataMap.expectedCheckInDateNumOfNights,"Booked Items Screen, Booked Items Section - Requested check in Date & nights Text after edit for room$i actual & expected don't match")
		
				//Commission and percentage
				assertionEquals(roomDataMap.actualCommissionAndPercentatgeBookedItmsScrn,data.expected.commissionTxt,"Booked Items Screen, Booked Items Section - Commission And Percentage text after edit for room$i actual & expected don't match")
		
				//Room Rate Amount and currency
				assertionEquals(roomDataMap.actualPriceAndcurrencyBookedItmsScrn,priceDataMap.expPriceAndCurrncy,"Booked Items Screen, Booked Items Section - Room Rate And Currency Text after edit room$i actual & expected don't match")
		
				//Add Remarks or Comment
				assertionEquals(roomDataMap.actualRemarksBookedItmsScrn,roomDataMap.expectedRemarksBookedItmsScrn,"Booked Items Screen, Booked Items Section - Please Note Text and special request text after edit for room$i actual & expected don't match")
	
				
			}
		}
		
		else{
		
		//Booking ID & Number
		assertionEquals(resultData.hotel.itineraryPage.actualBookingIDinBookedDetailsScrn,resultData.hotel.itineraryPage.retrievedbookingID,"Booked Items Screen, Booking ID and number actual & expected don't match")

		//Confirmed tab
		assertionEquals(resultData.hotel.itineraryPage.actualconfirmedTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Confirmed or Pending Tab display status actual & expected don't match")
		//Confirmed tab text
		assertionEquals(resultData.hotel.itineraryPage.actualStatusTabDispStatus,data.expected.statusTabTxt,"Booked Items Screen, Confirmed Or Pending Tab Text status actual & expected don't match")

		//Amend tab
		assertionEquals(resultData.hotel.itineraryPage.actualAmendTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Amend Tab display status actual & expected don't match")
		//Amend tab text
		assertionEquals(resultData.hotel.itineraryPage.actualAmendTabTxt,data.expected.amendTabTxt,"Booked Items Screen, Amend Tab Text actual & expected don't match")

		//Cancel tab
		assertionEquals(resultData.hotel.itineraryPage.actualCancelTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Cancel Tab display status actual & expected don't match")
		//Cancel tab text
		assertionEquals(resultData.hotel.itineraryPage.actualCancelTabTxt,data.expected.cancelTabTxt,"Booked Items Screen, Cancel Tab Text actual & expected don't match")
		//Hotel Image Display Status
		assertionEquals(resultData.hotel.itineraryPage.actualHotelImageDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Hotel Image display status actual & expected don't match")

		//Hotel Name
		assertionEquals(resultData.hotel.itineraryPage.actualHotelNameTxt,data.expected.cityAreaHotelText,"Booked Items Screen, Booked Items Section - Hotel Name Text actual & expected don't match")

		//Hotel Star Rating
		assertionEquals(resultData.hotel.itineraryPage.actualStarRatingInBookedHotelItem,data.expected.starRating,"Booked Items Screen, Booked Items Section - Hotel Star Rating Text actual & expected don't match")

		//Hotel Room Desc
		assertionEquals(resultData.hotel.itineraryPage.actualroomdescTxtInBookedItmsScrn,data.expected.roomDesc,"Booked Items Screen, Booked Items Section - Room Desc Text actual & expected don't match")

		//PAX number
		assertionEquals(resultData.hotel.itineraryPage.actualPaxNumInBookedItmsScrn,data.expected.totalPax,"Booked Items Screen, Booked Items Section - PAX Number Text actual & expected don't match")

		//Rate Plan
		assertionEquals(resultData.hotel.itineraryPage.actualratePlanInBookedItmsScrn,data.expected.ratePlan,"Booked Items Screen, Booked Items Section - Rate Plan/Meal Basis Text actual & expected don't match")


			if(!(data.input.unavailableItem.toString().toBoolean()))	{
				//Free Cancel Text
				assertionEquals(resultData.hotel.itineraryPage.actualFreeCnclTxtInbookedItmsScrn,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Booked Items Screen, Booked Items Section - Free Cancel Text actual & expected don't match")

			}

		if(data.input.cancelItem.toString().toBoolean())
		{
			//Inventory Status
			assertionEquals(resultData.hotel.itineraryPage.actualInventoryStatusInBookedItems,data.expected.invntryStatus,"Booked Items Screen, Booked Items Section - Inventory Status Text actual & expected don't match")
		}
		//Requested Check in and nights
		assertionEquals(resultData.hotel.itineraryPage.actualDurationTxtInBookedItmsScrn,resultData.hotel.itineraryPage.expectedDurationTxt,"Booked Items Screen, Booked Items Section - Requested check in Date & nights Text actual & expected don't match")

		//Commission and percentage
		assertionEquals(resultData.hotel.itineraryPage.actualCommissionAndPercentatgeInBookedItmsScrn,data.expected.commissionTxt,"Booked Items Screen, Booked Items Section - Commission And Percentage text actual & expected don't match")

		//Room Rate Amount and currency
		assertionEquals(resultData.hotel.itineraryPage.actualPriceAndcurrencyInBookedItmsScrn,resultData.hotel.searchResults.expectedPrice,"Booked Items Screen, Booked Items Section - Room Rate And Currency Text actual & expected don't match")

		if(data.input.bookItem.toString().toBoolean())
		{
			//Add Remarks or Comment
			assertionEquals(resultData.hotel.itineraryPage.actualRemarksInBookedItmsScrn,resultData.hotel.itineraryPage.expectedRemarksInBookedItmsScrn,"Booked Items Screen, Booked Items Section - Please Note Text and special request text actual & expected don't match")

			//Edit icon display status
			assertionEquals(resultData.hotel.itineraryPage.actualEditIconInBookedDetailScrnDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Edit Icon display status actual & expected don't match")

			//Add comment or special request lighbox display
			assertionEquals(resultData.hotel.itineraryPage.actualHeaderTxtInAddCmntOrSpeclReqScrnDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request lighbox display status actual & expected don't match")

			//Add comment or special request lighbox text
			assertionEquals(resultData.hotel.itineraryPage.actualHeaderTxtInAddCmntOrSpeclReqScrn,data.expected.addCmntOrSpclReqTxt,"Booked Items Screen, Booked Items Section - Add comment or special request text actual & expected don't match")

			//Select from the choices below or write your own request - text
			assertionEquals(resultData.hotel.itineraryPage.actualHeaderTxtSelChoicesBelowInAddCmntOrSpeclReqScrn,data.expected.selectChoicesTxt,"Booked Items Screen, Booked Items Section - Select from the choices below or write your own request text actual & expected don't match")

			//Please Note text
			assertionEquals(resultData.hotel.itineraryPage.actualHeaderTxtPlzNoteInAddcmntOrSclReqScrn,data.expected.pleaseNoteInAbtToBookItemsTxt,"Booked Items Screen, Booked Items Section, Add comment or special request screen - Please Note text actual & expected don't match")

			//Will arrive without voucher- Should display with box ticked.
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualwilArrivWithoutVochrCheckBoxStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - With Or Without Vouched Tick Box Selected status actual & expected don't match")

			//Will arrive without voucehr - text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualwilArrivWithoutVochrTxt,data.expected.chkBoxTxt_1,"Booked Items Screen, Booked Items Section - Add comment or special request screen - With Or Without Voucher Text actual & expected don't match")

			//Untick validation
			//assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualCheckBoxTickStatusAfterClick,data.expected.notDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - With Or Without Voucher Untick status actual & expected don't match")

			//Previous night is booked for early morning arrival display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualPreviousNightBookChkBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Previous night is booked for early morning arrival check box display status actual & expected don't match")

			//Previous night is booked for early morning arrival text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualPreviousNightTxt,data.expected.chkBoxTxt_2,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Previous night is booked for early morning arrival text actual & expected don't match")

			//Late check out check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateChkOutDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late check out check box display status actual & expected don't match")
			//Late check out Text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateCheckOutTxt,data.expected.chkBoxTxt_3,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late check out text actual & expected don't match")

			//Late Arrival check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateArrivalDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late Arrival check box display status actual & expected don't match")

			//Late Arrival text box
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualLateArrivalTxt,data.expected.chkBoxTxt_4,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Late Arrival text actual & expected don't match")

			//Passengers Are HoneyMooners check box Display Status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualPassengersAreHoneyMoonersDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Passengers Are HoneyMooners check box Display status actual & expected don't match")

			//Passengers Are HoneyMooners Text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualPassengersAreHoneyMoonersTxt,data.expected.chkBoxTxt_5,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Passengers Are HoneyMooners text actual & expected don't match")

			//Early Arrival Disp Status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualEarlyArrivalCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Early Arrival Disp Status actual & expected don't match")

			//Early Arrival Text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualEarlyArrivalTxt,data.expected.chkBoxTxt_6,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Early Arrival text actual & expected don't match")

			//If possible, please provide: text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualHeaderTxtIfPssblPlzProvideTxt,data.expected.IfPsblePlzProvTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - If possible, please provide text actual & expected don't match")

			//Twin Room check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualTwinRoomCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Twin Room check box display Status actual & expected don't match")

			//Twin Room Text Box
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualTwinRoomtxt,data.expected.chkBoxTxt_7,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Twin Room text actual & expected don't match")

			//Smoking Room Check Box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingroomchkBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Smoking Room Check Box display Status actual & expected don't match")

			//Smoking Room Check Box Text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingRoomtxt,data.expected.chkBoxTxt_8,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Smoking Room  text actual & expected don't match")

			//Inter Connecting Rooms check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualInterConnectingRoomCheckboxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Inter Connecting Rooms check box display Status actual & expected don't match")

			//Inter connecting Rooms text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualIntrConnectngRoomtxt,data.expected.chkBoxTxt_9,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Inter connecting Rooms text actual & expected don't match")

			//Adjoining Rooms check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualAdjoiningRoomCheckboxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Adjoining Rooms check box display Status actual & expected don't match")

			//Adjoining Rooms check box text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualAdjoiningRoomtxt,data.expected.chkBoxTxt_10,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Adjoining Rooms check box text actual & expected don't match")

			//Quiet Rooms check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualQuietRoomCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Quiet Rooms check box  display Status actual & expected don't match")

			//Quiet Rooms check box text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualQuietRoomtxt,data.expected.chkBoxTxt_11,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Quiet Rooms check  box text actual & expected don't match")

			//Non-Smoking Rooms check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualNonSmokingRoomCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Non-Smoking Rooms check box display Status actual & expected don't match")

			//Non-Smoking Rooms text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualNonSmokingRoomtxt,data.expected.chkBoxTxt_12,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Non-Smoking Rooms check  box text actual & expected don't match")

			//Room on lowest floor display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnLowestFlrCheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on lowest floor display Status actual & expected don't match")

			//Room on lowest floor text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnLowestFloorRoomtxt,data.expected.chkBoxTxt_13,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on lowest floor check  box text actual & expected don't match")

			//Room on high floor checkbox display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnHighFloorcheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on high floor checkbox display Status actual & expected don't match")

			//Room on high floor text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomOnHighFloorRoomtxt,data.expected.chkBoxTxt_14,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room on high floor check  box text actual & expected don't match")

			//Double Room check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualDoubleRoomcheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Double Room check display Status actual & expected don't match")

			//Double Room text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualDoubleRoomtxt,data.expected.chkBoxTxt_15,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Double Room check  box text actual & expected don't match")

			//Room with bathtub check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomWithBathtubcheckBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room with bathtub display Status actual & expected don't match")

			//Room with bathtub text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualRoomwithBathTubtxt,data.expected.chkBoxTxt_16,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Room with bath tub text actual & expected don't match")

			//Arrival Flight Number check box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Arrival Flight Number check box display Status actual & expected don't match")
			//Arrival Flight Number text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumtxt,data.expected.chkBoxTxt_17,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Arrival Flight Number text actual & expected don't match")

			//Arrival Flight Number text box existence
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualArrivalFlightNumTxtBoxDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Arrival Flight Number text box display Status actual & expected don't match")

			// Hrs
			//at text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualAtTextinAddRemrksOrCmntScrn,data.expected.atTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - at text actual & expected don't match")
			//Hrs text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualHrsTextinAddRemrksOrCmntScrn,data.expected.hrsTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Hrs text actual & expected don't match")
			//Hrs select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualHrsSelectDropBoxinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Hrs select drop box display Status actual & expected don't match")

			//Mins
			//mins text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualminsTextinAddRemrksOrCmntScrn,data.expected.minsTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Mins text actual & expected don't match")
			//mins select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualMinsDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - mins select drop box display Status actual & expected don't match")

			//On Date
			//on text
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualOnTextinAddRemrksOrCmntScrn,data.expected.onTxt,"Booked Items Screen, Booked Items Section - Add comment or special request screen - On text actual & expected don't match")
			//on Day select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualDaySelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - on Day select drop box display Status actual & expected don't match")
			//on Month select drop box display status
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualMonthSelectDropBoxDispStatusinAddRemrksOrCmntScrn,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - on Month select drop box display Status actual & expected don't match")

			//Smoking Room check box ticked or not
			assertionEquals(resultData.hotel.itineraryPage.addComntOrSpclReq.actualSmokingRoomCheckBoxCheckedStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Add comment or special request screen - Smoking Room check box display Status after tick actual & expected don't match")

			//Hotel Image Display status
			assertionEquals(resultData.hotel.itineraryPage.actualHotelImageDispStatusInConfBkngAftrEdtRmrks,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Hotel Image Display Status after Edit Remarks actual & expected don't match")

			//Hotel Name
			assertionEquals(resultData.hotel.itineraryPage.actualHotelNameTxtInConfBkngAftrEdtRmrks,data.expected.cityAreaHotelText,"Booked Items Screen, Booked Items Section - Hotel Name  after Edit Remarks actual & expected don't match")

			//Hotel Star Rating
			assertionEquals(resultData.hotel.itineraryPage.actualStarRatingInBookedHotelItemInConfBkngAftrEdtRmrks,data.expected.starRating,"Booked Items Screen, Booked Items Section - Hotel Star Rating after Edit Remarks actual & expected don't match")

			//Room Desc
			assertionEquals(resultData.hotel.itineraryPage.actualroomdescTxtInBookedItmsScrnInConfBkngAftrEdtRmrks,data.expected.roomDesc,"Booked Items Screen, Booked Items Section - Room Desc after Edit Remarks actual & expected don't match")

			//Pax Number
			assertionEquals(resultData.hotel.itineraryPage.actualPaxNumInBookedItmsScrnInConfBkngAftrEdtRmrks,data.expected.totalPax,"Booked Items Screen, Booked Items Section - PAX Number after Edit Remarks actual & expected don't match")

			//Rate Plan
			assertionEquals(resultData.hotel.itineraryPage.actualratePlanInBookedItmsScrnInConfBkngAftrEdtRmrks,data.expected.ratePlan,"Booked Items Screen, Booked Items Section - Rate Plan after Edit Remarks actual & expected don't match")

			//Free Cancellation
			assertionEquals(resultData.hotel.itineraryPage.actualFreeCnclTxtInConfBkngAftrEdtRmrks,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Booked Items Screen, Booked Items Section - Free Cancel Text after Edit Remarks actual & expected don't match")

			//Requested Check in and nights
			assertionEquals(resultData.hotel.itineraryPage.actualDurationTxtInConfBkngAftrEdtRmrks,resultData.hotel.itineraryPage.expectedDurationTxt,"Booked Items Screen, Booked Items Section - Requested check in Date & nights Text after Edit Remarks actual & expected don't match")

			//commission and percentage
			assertionEquals(resultData.hotel.itineraryPage.actualCommissionAndPercentatgeInConfBkngAftrEdtRmrks,data.expected.commissionTxt,"Booked Items Screen, Booked Items Section - Commission And Percentage text after Edit Remarks actual & expected don't match")

			//Room rate amound and currency
			assertionEquals(resultData.hotel.itineraryPage.actualPriceAndcurrencyInConfBkngAftrEdtRmrks,resultData.hotel.searchResults.expectedPrice,"Booked Items Screen, Booked Items Section - Room Rate And Currency Text after Edit Remarks actual & expected don't match")
			if(!(data.input.unavailableItem.toString().toBoolean()))
			{
				//Add a remark or content
				assertionEquals(resultData.hotel.itineraryPage.actualRemarksInConfBkngAftrEdtRmrks,resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks,"Booked Items Screen, Booked Items Section - Add a remark or content Text after Edit Remarks actual & expected don't match")
			}
		}
		
		}
	}

	protected def verifyBookedItemScreenMultiRoomCancel(HotelSearchData data, HotelTransferTestResultData resultData)
	{
		//Title Text - Booked Items
		assertionEquals(resultData.hotel.itineraryPage.actualBookedItmsHeaderTxt,data.expected.bookedItemHeaderTxt,"Booked Items Screen, Title text booked items actual & expected don't match")

		if(data.input.noOfRooms.toString().toInteger()>1 && (data.input.cancelMultiRoom))
		{
			
			for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
			{
				Map roomDataMap = resultData.hotel.itineraryPage.multiRoomBooking.bookedItems.get("room"+i)
				Map roomExpectedData = data.expected.get("room" + i)
				Map priceDataMap=resultData.hotel.searchResults.priceAndCurrency.get("room"+i)
				//Booking ID & Number
				assertionEquals(roomDataMap.actualBookingID,roomDataMap.expectedBookingID,"Booked Items Screen, Booking ID and number for room $i actual & expected don't match")
		
				//Confirmed tab
				assertionEquals(roomDataMap.actualconfirmedTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Confirmed or Pending Tab display status for room $i actual & expected don't match")
				//Confirmed tab text
				assertionEquals(roomDataMap.actualStatusTabDispStatus,data.expected.statusTabTxt,"Booked Items Screen, Confirmed Or Pending Tab Text status for room $i actual & expected don't match")
		
				//Amend tab
				assertionEquals(roomDataMap.actualAmendTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Amend Tab display status for room$i actual & expected don't match")
				//Amend tab text
				assertionEquals(roomDataMap.actualAmendTabTxt,data.expected.amendTabTxt,"Booked Items Screen, Amend Tab Text for room$i actual & expected don't match")
		
				//Cancel tab
				assertionEquals(roomDataMap.actualCancelTabDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Cancel Tab display status for room $i actual & expected don't match")
				//Cancel tab text
				assertionEquals(roomDataMap.actualCancelTabTxt,data.expected.cancelTabTxt,"Booked Items Screen, Cancel Tab Text for room $i actual & expected don't match")
				//Hotel Image Display Status
				assertionEquals(roomDataMap.actualHotelImageDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Hotel Image display status for room $i actual & expected don't match")

				//Hotel Name
				assertionEquals(roomDataMap.actualHotelNameTxt,data.expected.cityAreaHotelText,"Booked Items Screen, Booked Items Section - Hotel Name Text for room $i actual & expected don't match")
		
				//Hotel Star Rating
				assertionEquals(roomDataMap.actualStarRatingInBookedHotelItem,data.expected.starRating,"Booked Items Screen, Booked Items Section - Hotel Star Rating Text for room $i actual & expected don't match")
				
				//Hotel Room Desc
				assertionEquals(roomDataMap.actualroomdescTxtInBookedItmsScrn,roomExpectedData.roomDesc,"Booked Items Screen, Booked Items Section - Room Desc Text for room $i actual & expected don't match")
		
				//PAX number
				assertionEquals(roomDataMap.actualPaxNumInBookedItmsScrn,roomExpectedData.totalPax,"Booked Items Screen, Booked Items Section - PAX Number Text for room $i actual & expected don't match")
		
				//Rate Plan
				assertionEquals(roomDataMap.actualratePlanInBookedItmsScrn,data.expected.ratePlan,"Booked Items Screen, Booked Items Section - Rate Plan/Meal Basis Text for room $i actual & expected don't match")
		
				//Free Cancel Text
				assertionEquals(roomDataMap.actualFreeCnclTxtInbookedItmsScrn,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Booked Items Screen, Booked Items Section - Free Cancel Text for room $i actual & expected don't match")
		
				//Requested Check in and nights
				assertionEquals(roomDataMap.actualDurationTxtInBookedItmsScrn,roomDataMap.expectedCheckInDateNumOfNights,"Booked Items Screen, Booked Items Section - Requested check in Date & nights Text for room$i actual & expected don't match")
		
				//Commission and percentage
				assertionEquals(roomDataMap.actualCommissionAndPercentatgeInBookedItmsScrn,data.expected.commissionTxt,"Booked Items Screen, Booked Items Section - Commission And Percentage text for room$i actual & expected don't match")
		
				//Room Rate Amount and currency
				assertionEquals(roomDataMap.actualPriceAndcurrencyInBookedItmsScrn,priceDataMap.expPriceAndCurrncy,"Booked Items Screen, Booked Items Section - Room Rate And Currency Text room$i actual & expected don't match")
		
				//Add Remarks or Comment
				assertionEquals(roomDataMap.actualRemarksInBookedItmsScrn,roomDataMap.expectedRemarksInBookedItmsScrn,"Booked Items Screen, Booked Items Section - Please Note Text and special request text for room$i actual & expected don't match")
	
				//Edit icon display status
				assertionEquals(roomDataMap.actualEditIconInBookedDetailScrnDispStatus,data.expected.yesDispStatus,"Booked Items Screen, Booked Items Section - Edit Icon display status for room$i actual & expected don't match")
				
			}
		}
	
	}
	
	protected def verifyCancelBookedItemsScreenresults(HotelSearchData data, HotelTransferTestResultData resultData)
	{
		if(data.input.noOfRooms.toString().toInteger()>1 && (data.input.cancelMultiRoom))
		{
			
			for(int i=1;i<=data.input.noOfRooms.toString().toInteger();i++)
			{
				Map roomDataCancelMap =  resultData.hotel.removeItemPage.multiRoomBooking.cancelItems.get("room"+i)
				Map roomDataMap = resultData.hotel.itineraryPage.multiRoomBooking.bookedItems.get("room"+i)
				Map roomExpectedData = data.expected.get("room" + i)
				Map priceDataMap=resultData.hotel.searchResults.priceAndCurrency.get("room"+i)
				//Title Text - Booked Items
				assertionEquals(roomDataCancelMap.actualCancelItemDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, Display status for room $i actual & expected don't match")
		
				//lightbox - Title text , Text - Cancel item
				assertionEquals(roomDataCancelMap.actualCancelItemTxt,data.expected.cancelTitleTxt,"Cancel Item Popup Screen, Cancel Item title text for room $i  actual & expected don't match")
		
				//Close lightbox X function
				assertionEquals(roomDataCancelMap.actualClosebuttonDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, Close lightbox X Display status for room $i  actual & expected don't match")
				
				//hotel image link
				//assertionEquals(roomDataCancelMap.actualImageIconLinkExistence,data.expected.yesDispStatus,"Cancel Item Popup Screen, Hotel Image Display status for room $i  actual & expected don't match")
		
				//hotel name
				assertionEquals(roomDataCancelMap.actualHotelNameTxt,data.expected.cityAreaHotelText,"Cancel Item Popup Screen, Hotel Name text for room $i  actual & expected don't match")
		
				//hotel star rating
				assertionEquals(roomDataCancelMap.actualHotelStarRating,data.expected.starRating,"Cancel Item Popup Screen, Hotel Star Rating text for room $i  actual & expected don't match")
		
				//Room - Description text
				assertionEquals(roomDataCancelMap.actualroomdescTxt,roomExpectedData.roomDesc,"Cancel Item Popup Screen, Room Desc text for room $i actual & expected don't match")
		
				//Pax Number
				assertionEquals(roomDataCancelMap.actualPaxNum,roomExpectedData.totalPax,"Cancel Item Popup Screen, Pax Num Text for room $i actual & expected don't match")
		
				//Rate Plan - meal basis
				assertionEquals(roomDataCancelMap.actualratePlan,data.expected.ratePlan,"Cancel Item Popup Screen, Rate Plan Mean Basis Text for room $i actual & expected don't match")
		
				//Free Cancel
				assertionEquals(roomDataCancelMap.actualFreeCnclTxt,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Cancel Item Popup Screen, Free Cancellation Until Date Text for room $i actual & expected don't match")
		
				//requested check in date and nights
				assertionEquals(roomDataCancelMap.actualDurationTxt,resultData.hotel.itineraryPage.expectedDurationTxt,"Cancel Item Popup Screen, requested check in date and nights Text for room $i actual & expected don't match")
		
				//Commission and percentage
				assertionEquals(roomDataCancelMap.actualcomPercentTxt,data.expected.commissionTxt,"Cancel Item Popup Screen, Commission and percentage Text for room $i actual & expected don't match")
		
				//Room rate amound and currency
				assertionEquals(roomDataCancelMap.actualPriceAndcurrency,priceDataMap.expPriceAndCurrncy,"Cancel Item Popup Screen, Room rate amound and currency Text for room $i actual & expected don't match")
		
				//display function button No
				assertionEquals(roomDataCancelMap.actualNoButtonDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, No Button Display status for room $i actual & expected don't match")
		
				//display function button Yes
				assertionEquals(roomDataCancelMap.actualYesButtonDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, Yes Button Display status for room $i actual & expected don't match")
		
				//remove item box disappear
				assertionEquals(roomDataCancelMap.actualremoveItemDispStatus,data.expected.notDispStatus,"Cancel Item Popup Screen, Display status for room $i actual & expected don't match")
		
				//title text - Unavailable and Cancelled items
				assertionEquals(roomDataCancelMap.actualCancelledItmsTxt,data.expected.unavailableAndCanclTxt,"Itinerary Screen, After Cancellation, Unavailable and Cancelled Items text for room $i actual and expected don't match")
		
				//Booking ID & Number
				assertionEquals(roomDataCancelMap.actualBookingIDinBookedDetailsScrn,roomDataCancelMap.expectedBookingID,"Itinerary Screen, After Cancellation, Booking ID and number for room $i actual & expected don't match")
		
				//Status - cancelled - text
				assertionEquals(roomDataCancelMap.actualStatus,data.expected.statusCancelledTxt,"Itinerary Screen, After Cancellation, Cancelled Status Text for room $i actual & expected don't match")
		
				//hotel image link
				//assertionEquals(roomDataCancelMap.actualImageIconLinkExistence,data.expected.yesDispStatus,"Itinerary Screen, After Cancellation, Hotel Image Display status for room $i actual & expected don't match")
		
				//hotel name
				assertionEquals(roomDataCancelMap.actualHotelNameTxt,data.expected.cityAreaHotelText,"Itinerary Screen, After Cancellation, Hotel Name text for room $i actual & expected don't match")
		
				//hotel star rating
				assertionEquals(roomDataCancelMap.actualHotelStarRating,data.expected.starRating,"Itinerary Screen, After Cancellation, Hotel Star Rating text for room $i actual & expected don't match")
		
				//Room - Description text
				assertionEquals(roomDataCancelMap.actualroomdescTxt,roomExpectedData.roomDesc,"Itinerary Screen, After Cancellation, Room Desc text for room $i actual & expected don't match")
		
				//Pax Number
				assertionEquals(roomDataCancelMap.actualPaxNum,roomExpectedData.totalPax,"Itinerary Screen, After Cancellation, Pax Num Text for room $i actual & expected don't match")
		
				//Rate Plan - meal basis
				assertionEquals(roomDataCancelMap.actualratePlan,data.expected.ratePlan,"Itinerary Screen, After Cancellation, Rate Plan Mean Basis Text for room $i actual & expected don't match")
		
				//Free Cancel
				assertionEquals(roomDataCancelMap.actualFreeCnclTxt,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Itinerary Screen, After Cancellation, Free Cancellation Until Date Text for room $i actual & expected don't match")
		
				//requested check in date and nights
				assertionEquals(roomDataCancelMap.actualDurationTxt,resultData.hotel.itineraryPage.expectedDurationTxt,"Itinerary Screen, After Cancellation, requested check in date and nights Text for room $i actual & expected don't match")
		
				//Commission and percentage
				assertionEquals(roomDataCancelMap.actualcomPercentTxt,data.expected.commissionTxt,"Itinerary Screen, After Cancellation, Commission and percentage Text for room $i actual & expected don't match")
		
				//Room rate amound and currency
				assertionEquals(roomDataCancelMap.actualPriceAndcurrency,priceDataMap.expPriceAndCurrncy,"Itinerary Screen, After Cancellation, Room rate amound and currency Text for room $i actual & expected don't match")
		
		
				//Travellers
				assertionEquals(roomDataCancelMap.actualTravellerDetailsTxt,roomDataCancelMap.expectedTravellerDetailsTxt,"Itinerary Screen, After Cancellation, Travellers Text for room $i actual & expected don't match")
		
				//Add a remark or content
				assertionEquals(roomDataCancelMap.actualRemarksUnavailableItms,roomDataCancelMap.expectedRemarksUnavailableItms,"Itinerary Screen, After Cancellation - Add a remark or content Text after Cancel Remarks for room $i actual & expected don't match")
		
				
			}
		}
		else{
		
		//Title Text - Booked Items
		assertionEquals(resultData.hotel.removeItemPage.actualCancelItemDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, Display status actual & expected don't match")

		//lightbox - Title text , Text - Cancel item
		assertionEquals(resultData.hotel.removeItemPage.actualCancelItemTxt,data.expected.cancelTitleTxt,"Cancel Item Popup Screen, Cancel Item title text actual & expected don't match")

		//Close lightbox X function
		assertionEquals(resultData.hotel.removeItemPage.actualClosebuttonDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, Close lightbox X Display status actual & expected don't match")

		//hotel image link
		//assertionEquals(resultData.hotel.removeItemPage.actualImageIconLinkExistence,data.expected.yesDispStatus,"Cancel Item Popup Screen, Hotel Image Display status actual & expected don't match")

		//hotel name
		assertionEquals(resultData.hotel.removeItemPage.actualHotelNameTxt,data.expected.cityAreaHotelText,"Cancel Item Popup Screen, Hotel Name text actual & expected don't match")

		//hotel star rating
		assertionEquals(resultData.hotel.removeItemPage.actualHotelStarRating,data.expected.starRating,"Cancel Item Popup Screen, Hotel Star Rating text actual & expected don't match")

		//Room - Description text
		assertionEquals(resultData.hotel.removeItemPage.actualroomdescTxt,data.expected.roomDesc,"Cancel Item Popup Screen, Room Desc text actual & expected don't match")

		//Pax Number
		assertionEquals(resultData.hotel.removeItemPage.actualPaxNum,data.expected.totalPax,"Cancel Item Popup Screen, Pax Num Text actual & expected don't match")

		//Rate Plan - meal basis
		assertionEquals(resultData.hotel.removeItemPage.actualratePlan,data.expected.ratePlan,"Cancel Item Popup Screen, Rate Plan Mean Basis Text actual & expected don't match")

		//Free Cancel
		assertionEquals(resultData.hotel.removeItemPage.actualFreeCnclTxt,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Cancel Item Popup Screen, Free Cancellation Until Date Text actual & expected don't match")

		//requested check in date and nights
		assertionEquals(resultData.hotel.removeItemPage.actualDurationTxt,resultData.hotel.itineraryPage.expectedDurationTxt,"Cancel Item Popup Screen, requested check in date and nights Text actual & expected don't match")

		//Commission and percentage
		assertionEquals(resultData.hotel.removeItemPage.actualcomPercentTxt,data.expected.commissionTxt,"Cancel Item Popup Screen, Commission and percentage Text actual & expected don't match")

		//Room rate amound and currency
		assertionEquals(resultData.hotel.removeItemPage.actualPriceAndcurrency,resultData.hotel.searchResults.expectedPrice,"Cancel Item Popup Screen, Room rate amound and currency Text actual & expected don't match")

		//display function button No
		assertionEquals(resultData.hotel.removeItemPage.actualNoButtonDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, No Button Display status actual & expected don't match")

		//display function button Yes
		assertionEquals(resultData.hotel.removeItemPage.actualYesButtonDispStatus,data.expected.yesDispStatus,"Cancel Item Popup Screen, Yes Button Display status actual & expected don't match")

		//remove item box disappear
		assertionEquals(resultData.hotel.removeItemPage.actualremoveItemDispStatus,data.expected.notDispStatus,"Cancel Item Popup Screen, Display status actual & expected don't match")

		//title text - Unavailable and Cancelled items
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualCancelledItmsTxt,data.expected.unavailableAndCanclTxt,"Itinerary Screen, After Cancellation, Unavailable and Cancelled Items text actual and expected don't match")

		//Booking ID & Number
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualBookingIDinBookedDetailsScrn,resultData.hotel.itineraryPage.retrievedbookingID,"Itinerary Screen, After Cancellation, Booking ID and number actual & expected don't match")

		//Status - cancelled - text
		//assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualStatus,data.expected.statusCancelledTxt,"Itinerary Screen, After Cancellation, Cancelled Status Text actual & expected don't match")

		//hotel image link
		//assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualImageIconLinkExistence,data.expected.yesDispStatus,"Itinerary Screen, After Cancellation, Hotel Image Display status actual & expected don't match")

		//hotel name
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualHotelNameTxt,data.expected.cityAreaHotelText,"Itinerary Screen, After Cancellation, Hotel Name text actual & expected don't match")

		//hotel star rating
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualHotelStarRating,data.expected.starRating,"Itinerary Screen, After Cancellation, Hotel Star Rating text actual & expected don't match")

		//Room - Description text
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualroomdescTxt,data.expected.roomDesc,"Itinerary Screen, After Cancellation, Room Desc text actual & expected don't match")

		//Pax Number
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualPaxNum,data.expected.totalPax,"Itinerary Screen, After Cancellation, Pax Num Text actual & expected don't match")

		//Rate Plan - meal basis
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualratePlan,data.expected.ratePlan,"Itinerary Screen, After Cancellation, Rate Plan Mean Basis Text actual & expected don't match")

		//Free Cancel
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualFreeCnclTxt,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Itinerary Screen, After Cancellation, Free Cancellation Until Date Text actual & expected don't match")

		//requested check in date and nights
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualDurationTxt,resultData.hotel.itineraryPage.expectedDurationTxt,"Itinerary Screen, After Cancellation, requested check in date and nights Text actual & expected don't match")

		//Commission and percentage
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualcomPercentTxt,data.expected.commissionTxt,"Itinerary Screen, After Cancellation, Commission and percentage Text actual & expected don't match")

		//Room rate amound and currency
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualPriceAndcurrency,resultData.hotel.searchResults.expectedPrice,"Itinerary Screen, After Cancellation, Room rate amound and currency Text actual & expected don't match")


		//Travellers
		assertionEquals(resultData.hotel.removeItemPage.cancelledItems.actualTravellerDetailsTxt,resultData.hotel.removeItemPage.cancelledItems.expectedTravellerDetailsTxt,"Itinerary Screen, After Cancellation, Travellers Text actual & expected don't match")

		//Add a remark or content
		assertionEquals(resultData.hotel.itineraryPage.actualRemarksInConfBkngAftrEdtRmrks,resultData.hotel.itineraryPage.expectedRemarksInConfBkngAftrEdtRmrks,"Itinerary Screen, After Cancellation - Add a remark or content Text after Edit Remarks actual & expected don't match")

		}
	}

	protected verifyUnavailableItemsScreenresults(HotelSearchData data, HotelTransferTestResultData resultData)
	{
		//remove item light box displayed
		assertionEquals(resultData.hotel.removeItemPage.actualremoveItemLightBoxDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen Light box, Display status actual & expected don't match")

		//lightbox - Title text , Text - Remove item
		assertionEquals(resultData.hotel.removeItemPage.actualremoveItemTxt,data.expected.cancelTitleTxt,"Remove Item Popup Screen, Remove Item title text actual & expected don't match")

		//Close lightbox X function
		assertionEquals(resultData.hotel.removeItemPage.actualClosebuttonDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen, Close lightbox X Display status actual & expected don't match")

		//text - Are you sure you want to remove the following item form this itinerary?
		assertionEquals(resultData.hotel.removeItemPage.actualremoveHeaderTxt,data.expected.removeHeaderTxt,"Remove Item Popup Screen, Remove Header Text actual & expected don't match")

		//hotel image link
		//assertionEquals(resultData.hotel.removeItemPage.actualImageIconLinkExistence,data.expected.yesDispStatus,"Remove Item Popup Screen, Hotel Image Display status actual & expected don't match")

		//hotel name
		assertionEquals(resultData.hotel.removeItemPage.actualHotelNameTxt,data.expected.cityAreaHotelText,"Remove Item Popup Screen, Hotel Name text actual & expected don't match")

		//hotel star rating
		assertionEquals(resultData.hotel.removeItemPage.actualHotelStarRating,data.expected.starRating,"Remove Item Popup Screen, Hotel Star Rating text actual & expected don't match")

		//Room - Description text
		assertionEquals(resultData.hotel.removeItemPage.actualroomdescTxt,data.expected.roomDesc,"Remove Item Popup Screen, Room Desc text actual & expected don't match")

		//Pax Number
		assertionEquals(resultData.hotel.removeItemPage.actualPaxNum,data.expected.totalPax,"Remove Item Popup Screen, Pax Num Text actual & expected don't match")

		//Rate Plan - meal basis
		assertionEquals(resultData.hotel.removeItemPage.actualratePlan,data.expected.ratePlan,"Remove Item Popup Screen, Rate Plan Mean Basis Text actual & expected don't match")

		//Free Cancel
		//assertionEquals(resultData.hotel.removeItemPage.actualFreeCnclTxt,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Remove Item Popup Screen, Free Cancellation Until Date Text actual & expected don't match")

		//requested check in date and nights
		assertionEquals(resultData.hotel.removeItemPage.actualDurationTxt,resultData.hotel.itineraryPage.expectedDurationTxt,"Remove Item Popup Screen, requested check in date and nights Text actual & expected don't match")

		//Commission and percentage
		assertionEquals(resultData.hotel.removeItemPage.actualcomPercentTxt,data.expected.commissionTxt,"Remove Item Popup Screen, Commission and percentage Text actual & expected don't match")

		//Room rate amound and currency
		assertionEquals(resultData.hotel.removeItemPage.actualPriceAndcurrency,resultData.hotel.searchResults.expectedPrice,"Remove Item Popup Screen, Room rate amound and currency Text actual & expected don't match")

		//display function button No
		assertionEquals(resultData.hotel.removeItemPage.actualNoButtonDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen, No Button Display status actual & expected don't match")

		//display function button Yes
		assertionEquals(resultData.hotel.removeItemPage.actualYesButtonDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen, Yes Button Display status actual & expected don't match")

		//remove item box disappear
		assertionEquals(resultData.hotel.removeItemPage.actualremoveItemDispStatus,data.expected.notDispStatus,"Remove Item Popup Screen, Display status actual & expected don't match")

		//Should not have suggest item list displayed
		//assertionEquals(resultData.hotel.removeItemPage.actualUnavailableAndCancelItemSecDispStatus,data.expected.notDispStatus,"Itinerary Screen , Unavailable And CancelItem Section Display status actual & expected don't match")
	}


	protected verifyRemoveItemsScreenresults(HotelSearchData data, HotelTransferTestResultData resultData)
	{
		//Remove function button display status
		assertionEquals(resultData.hotel.removeItemPage.actualRemoveIconDispStatus,data.expected.yesDispStatus,"Non Booked Items, Remove Icon Display status actual & expected don't match")

		//remove item light box displayed
		assertionEquals(resultData.hotel.removeItemPage.actualremoveItemLightBoxDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen Light box, Display status actual & expected don't match")

		//lightbox - Title text , Text - Remove item
		assertionEquals(resultData.hotel.removeItemPage.actualremoveItemTxt,data.expected.cancelTitleTxt,"Remove Item Popup Screen, Remove Item title text actual & expected don't match")

		//Close lightbox X function
		assertionEquals(resultData.hotel.removeItemPage.actualClosebuttonDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen, Close lightbox X Display status actual & expected don't match")

		//text - Are you sure you want to remove the following item form this itinerary?
		assertionEquals(resultData.hotel.removeItemPage.actualremoveHeaderTxt,data.expected.removeHeaderTxt,"Remove Item Popup Screen, Remove Header Text actual & expected don't match")

		//hotel image link
		//assertionEquals(resultData.hotel.removeItemPage.actualImageIconLinkExistence,data.expected.yesDispStatus,"Remove Item Popup Screen, Hotel Image Display status actual & expected don't match")

		//hotel name
		assertionEquals(resultData.hotel.removeItemPage.actualHotelNameTxt,data.expected.cityAreaHotelText,"Remove Item Popup Screen, Hotel Name text actual & expected don't match")

		//hotel star rating
		assertionEquals(resultData.hotel.removeItemPage.actualHotelStarRating,data.expected.starRating,"Remove Item Popup Screen, Hotel Star Rating text actual & expected don't match")

		//Room - Description text
		assertionEquals(resultData.hotel.removeItemPage.actualroomdescTxt,data.expected.roomDesc,"Remove Item Popup Screen, Room Desc text actual & expected don't match")

		//Pax Number
		assertionEquals(resultData.hotel.removeItemPage.actualPaxNum,data.expected.totalPax,"Remove Item Popup Screen, Pax Num Text actual & expected don't match")

		//Rate Plan - meal basis
		assertionEquals(resultData.hotel.removeItemPage.actualratePlan,data.expected.ratePlan,"Remove Item Popup Screen, Rate Plan Mean Basis Text actual & expected don't match")

		//Free Cancel
		assertionEquals(resultData.hotel.removeItemPage.actualFreeCnclTxt,resultData.hotel.itineraryPage.expectedFreeCanclTxt,"Remove Item Popup Screen, Free Cancellation Until Date Text actual & expected don't match")

		//requested check in date and nights
		assertionEquals(resultData.hotel.removeItemPage.actualDurationTxt,resultData.hotel.itineraryPage.expectedDurationTxt,"Remove Item Popup Screen, requested check in date and nights Text actual & expected don't match")

		//Inventory Availability
		assertionEquals(resultData.hotel.removeItemPage.actualInvStatusDisplayed,data.expected.invntryStatus,"Remove Item Popup Screen, Inventory Status Text actual & expected don't match")

		//Commission and percentage
		assertionEquals(resultData.hotel.removeItemPage.actualcomPercentTxt,data.expected.commissionTxt,"Remove Item Popup Screen, Commission and percentage Text actual & expected don't match")

		//Room rate amound and currency
		assertionEquals(resultData.hotel.removeItemPage.actualPriceAndcurrency,resultData.hotel.searchResults.expectedPrice,"Remove Item Popup Screen, Room rate amound and currency Text actual & expected don't match")

		//display function button No
		assertionEquals(resultData.hotel.removeItemPage.actualNoButtonDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen, No Button Display status actual & expected don't match")

		//display function button Yes
		assertionEquals(resultData.hotel.removeItemPage.actualYesButtonDispStatus,data.expected.yesDispStatus,"Remove Item Popup Screen, Yes Button Display status actual & expected don't match")

		//remove item box disappear
		assertionEquals(resultData.hotel.removeItemPage.actualremoveItemDispStatus,data.expected.notDispStatus,"Remove Item Popup Screen, Display status actual & expected don't match")

		//Item 1 is removed from Suggested items section
		assertionEquals(resultData.hotel.removeItemPage.actualItemDispStatusAfetRemovalInNonBookedItms,data.expected.notDispStatus,"Itinerary Screen , Item 1 Display status actual & expected don't match")

		//validate Total label text
		assertionEquals(resultData.hotel.removeItemPage.actualTotalLabelText,data.expected.totalText,"Itinerary page, Non Booked Items Total Text actual & expected don't match")

		//total amount for all item within the list
		//assertionEquals(resultData.hotel.removeItemPage.actualallItemsTotalAmountAndCurrency,resultData.hotel.removeItemPage.expectedallItemsTotalAmountAndCurrency,"Itinerary page, Non Booked Items Total Amount actual & expected don't match")

		//Capture Booked Items title text
		assertionEquals(resultData.hotel.removeItemPage.actualBookedItemsHeaderTxt,data.expected.bookedItemHeaderTxt,"Itinerary page, Booked Items Screen, Title text booked items actual & expected don't match")

		//Capture no. of items under Booked Items
		assertionEquals(resultData.hotel.removeItemPage.actualBookedItemsCount,data.expected.noOfItems.toString().toInteger(),"Itinerary page, Booked Items Screen, No. Of Items in Booked Items actual & expected don't match")

		//Total label text
		assertionEquals(resultData.hotel.removeItemPage.actualTotalLabelTextInBookedItemsSec,data.expected.bookedItemsTotalText,"Itinerary page, Booked Items Total Text actual & expected don't match")

		//Total / Room rate amount and currency
		assertionEquals(resultData.hotel.removeItemPage.actualallItemsTotalAmountAndCurrencyInBookedItemsSec,resultData.hotel.removeItemPage.expectedItemsTotalAmountAndCurrency,"Itinerary page, Booked Items Total Amount and currency Text actual & expected don't match")

		//Capture Unavailable and cancel item list
		assertionEquals(resultData.hotel.removeItemPage.actualUnavailableAndCancelItemsHeaderTxt,data.expected.unavailableItemHeaderTxt,"Itinerary page, Unavailable And Cancelled Items Screen, Title text actual & expected don't match")

		//Capture no. of items under Unavailable and cancel item list
		assertionEquals(resultData.hotel.removeItemPage.actualUnavailableAndCancelItemsCount,data.expected.noOfItems.toString().toInteger(),"Itinerary page, Unavailable And Cancelled Items Screen, No. Of Items in Unavailable And Cancelled Items actual & expected don't match")


		//should not shown total amount under Unavailable and cancel item list
		assertionEquals(resultData.hotel.removeItemPage.actualTotalDispStatus,data.expected.notDispStatus,"Itinerary Screen , Total Label & Amount with Currency Display status actual & expected don't match")

		//Should not have suggest item / Non booked item list displayed
		assertionEquals(resultData.hotel.removeItemPage.actualNonBookedItemOrSuggestedItmsSecDispStatus,data.expected.notDispStatus,"Itinerary Screen , suggest item / Non booked item Display status actual & expected don't match")
	}
}
