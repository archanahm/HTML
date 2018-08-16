package com.kuoni.qa.automation.test.search.booking.transfer

import com.kuoni.qa.automation.helper.TransfersTestData
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.page.transfers.TransfersSearchPage
import org.testng.Assert
import org.testng.asserts.SoftAssert

import spock.lang.Ignore
import spock.lang.Unroll

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.TransferTestResultData
import com.kuoni.qa.automation.helper.TransfersData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.transfers.TransferSearchPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.ShareItineraryTransfersPage
import com.kuoni.qa.automation.page.transfers.TransferSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.DateUtil
import com.kuoni.qa.automation.util.TestConf

import static com.kuoni.qa.automation.util.TestConf.booking

abstract class TransferBookingBaseSpec extends BaseSpec {

	//public abstract def getTestDesc()
	public static resultMap = [:]
	ClientData clientData = new ClientData("creditClient")
	//ClientData clientData = new ClientData("client664")
	//ClientData clientData = new ClientData("client2466")
	TransferTestResultData resultData = new TransferTestResultData()

	DateUtil dateUtil = new DateUtil()

	public abstract def getDataNames()
	public abstract String getSubPath()

	@Unroll
	def "01 : Transfers Search And Booking : #transferData.caseName #transferData.desc"()
	{
		given: "User is able to login and go to transfers view"
		TransfersData data = transferData

		login(clientData)
		at HotelSearchPage
		clickTransfersTab()
		//at TransferSearchPage
		at TransfersSearchPage
		//hideItenaryBuilder()
		println("\nRunning ${transferData.caseName} - ${transferData.desc}")
		//TransferTestResultData resultData = new TransferTestResultData()
		resultMap.put(transferData.caseName, resultData)
		enterPickupInput(data.pickup)
		sleep(5000)
		waitTillLoadingIconDisappears()
		boolean pickupAutoSuggest=false
		for(int i=0;i<getNoOfItemsInPickupAutoSuggest();i++){
			if(getPickupAutoSuggestText(i).equalsIgnoreCase(data.selectPickUp)){
				pickupAutoSuggest=true
				break
			}
		}
		resultData.transfers.searchResults.actualPickupAutoSuggestDispStatus=pickupAutoSuggest
		selectFromAutoSuggestPickUp(data.selectPickUp)
		//selected item displaying in pick up field
		resultData.transfers.searchResults.actualPickupSelectedValInTxtBox=getPickUpSelectedValueInTextBox()

		enterDropoffInput(data.dropoff)
		sleep(1000)
		boolean dropOffAutoSuggest=false
		for(int i=0;i<getNoOfItemsInDropOffAutoSuggest();i++){
			if(getDropOffAutoSuggestText(i).equalsIgnoreCase(data.selectDropOff)){
				dropOffAutoSuggest=true
				break
			}
		}
		resultData.transfers.searchResults.actualdropOffAutoSuggestDispStatus=dropOffAutoSuggest
		selectFromAutoSuggestDropOff(data.selectDropOff)
		//selected item displaying in drop off field
		resultData.transfers.searchResults.actualDropOffSelectedValInTxtBox=getDropOffSelectedValueInTextBox()

		//date retrievel

		String depDate = dateUtil.addDaysChangeDatetformat(data.checkInDays.toInteger(), "ddMMMyy").toLowerCase()
		println "Retrieved Date is:$depDate"

		String cancelDate=dateUtil.removeDaysFromDate(dateUtil.addDaysChangeDatetformat(data.checkInDays.toInteger(), "yyyy-MM-dd'T'HH:mm:ss.SSS"), data.cancelDays.toInteger(), "ddMMMyy")
		println "Cancel Date is:$cancelDate"
		//tomorrow's date should display within field
		resultData.transfers.searchResults.actualdefaultDate=getTransferDate()
		resultData.transfers.searchResults.expecteddefaultDate=dateUtil.addDaysChangeDatetformat(1, "ddMMMyy").toUpperCase()

		selectDateCalender(data.checkInDays.toInteger())
		sleep(2000)

		//Date field populated the selected date with correct format (e.g. ddMMMyy)
		resultData.transfers.searchResults.actualmodifiedDate=getTransferDate()
		resultData.transfers.searchResults.expectedmodifiedDate=dateUtil.addDaysChangeDatetformat(data.checkInDays.toInteger(), "ddMMMyy").toUpperCase()

		//12:00 should display within field
		resultData.transfers.searchResults.actualDefaultTime=getTransferTime()
		//2 pax should display within field
		resultData.transfers.searchResults.actualPaxDefaultValue=getPaxInTextBox()
		enterPaxInput(data.pax,"0")

		//Click Find
		clickFindButton()
		sleep(5000)

		waitTillLoadingIconDisappears()

		at TransferSearchResultsPage

		if(getInfoBoxTxtDisplayStatus()==true){
		//verify info box message is displayed
		resultData.transfers.searchResults.infoBoxBeforeCloseDispStatus=getInfoBoxTxtDisplayStatus()

		//verify info box message text
		resultData.transfers.searchResults.infoTxt=getTransferInfoBoxTxt()

			//click on close
			clickOnCloseInfo()
			sleep(3000)

			//verify info box message is disappeared.
			resultData.transfers.searchResults.infoBoxAfterCloseDispStatus=getInfoBoxTxtDisplayStatus()
		}




		def index = data.index
		def transferIndex=data.transferIndex
		def transferName

		if(transferData.validateAllSearchResults.toBoolean()){

			List expectedTransferDescValues = []
			List actualTransferDescValues = []

			List expectedShowCarTypesIconDispStatus=[]
			List actualShowCarTypesIconDispStatus=[]

			List expectedShowCarTypesTxt=[]
			List actualShowCarTypesTxt=[]

			List expectedShowCarTypesCollapseStatus=[]
			List actualShowCarTypesCollapseStatus=[]

			List expectedFromPriceAndCurrencyTxt=[]
			List actualFromPriceAndCurrencyTxt=[]

			List expectedFromPriceText=[]
			List actualFromPriceText=[]

			List expectedShowCarTypesExpancdedStatus=[]
			List actualShowCarTypesExpandedStatus=[]

			List expShowCarTypesCollapseStatus=[]
			List actShowCarTypesCollapseStatus=[]

			int actualtotalSearrchResultsCount=getTotalSearchResultsCount()
			println("Total Search Results Count: $actualtotalSearrchResultsCount")
			resultData.transfers.searchResults.actualtotalSearchResultsCount=actualtotalSearrchResultsCount
			for(int i=0;i<actualtotalSearrchResultsCount;i++){

				//Transfer Desc
				actualTransferDescValues.add(getTransfersDesc(i))
				expectedTransferDescValues.add(transferData.expected.transfersDescriptionValues.getAt(i))

				//Show Car Types Icon
				actualShowCarTypesIconDispStatus.add(getShowCarTypesIconDisplayStatus(i))
				expectedShowCarTypesIconDispStatus.add(transferData.expected.dispStatus)

				//Show Car Types Text
				actualShowCarTypesTxt.add(getShowCarTypesText(i))
				expectedShowCarTypesTxt.add(transferData.expected.showCarTypesTxt)

				//Show Car Types Collapsed Status
				actualShowCarTypesCollapseStatus.add(getShowCarTypesExpandOrCollapseStatus(i))
				expectedShowCarTypesCollapseStatus.add(transferData.expected.notDispStatus)

				//From Price And currency Text
				actualFromPriceAndCurrencyTxt.add(getFromPriceAndCurrencyTransferItem(i).replaceAll(" ",""))

				//From Price Text
				actualFromPriceText.add(getFromPriceText(i))
				expectedFromPriceText.add(transferData.expected.fromPriceTxt)

				//Expand ShowCar Types
				clickShowCarTypes(i)

				//Expanded status
				actualShowCarTypesExpandedStatus.add(getShowCarTypesExpandOrCollapseStatus(i))
				expectedShowCarTypesExpancdedStatus.add(transferData.expected.dispStatus)

				//Get Price of first record in each of the search results
				expectedFromPriceAndCurrencyTxt.add(getPriceAndCurrencyTransferItem(i,0).replaceAll(" ",""))

				//Collapse ShowCarTypes
				clickShowCarTypes(i)

				//Show Car Types Collapsed Status
				actShowCarTypesCollapseStatus.add(getShowCarTypesExpandOrCollapseStatus(i))
				expShowCarTypesCollapseStatus.add(transferData.expected.notDispStatus)

			}
			//Transfer Desc Store in Map
			resultData.transfers.searchResults.results.put("actualTransferDescValues", actualTransferDescValues)
			resultData.transfers.searchResults.results.put("expectedTransferDescValues", expectedTransferDescValues)

			//Show Car Types Icon Disp Status store in map
			resultData.transfers.searchResults.results.put("actualShowCarTypesIconDispStatus", actualShowCarTypesIconDispStatus)
			resultData.transfers.searchResults.results.put("expectedShowCarTypesIconDispStatus", expectedShowCarTypesIconDispStatus)


			//Show Car Types Text store in Map
			resultData.transfers.searchResults.results.put("actualShowCarTypesTxt", actualShowCarTypesTxt)
			resultData.transfers.searchResults.results.put("expectedShowCarTypesTxt", expectedShowCarTypesTxt)

			//Show Car Types Collapsed Status
			resultData.transfers.searchResults.results.put("actualShowCarTypesCollapseStatus", actualShowCarTypesCollapseStatus)
			resultData.transfers.searchResults.results.put("expectedShowCarTypesCollapseStatus", expectedShowCarTypesCollapseStatus)

			//From Price And Currency Text
			resultData.transfers.searchResults.results.put("actualFromPriceAndCurrencyTxt", actualFromPriceAndCurrencyTxt)
			resultData.transfers.searchResults.results.put("expectedFromPriceAndCurrencyTxt", expectedFromPriceAndCurrencyTxt)

			//From Price Text
			resultData.transfers.searchResults.results.put("actualFromPriceText", actualFromPriceText)
			resultData.transfers.searchResults.results.put("expectedFromPriceText", expectedFromPriceText)

			//Expanded status
			resultData.transfers.searchResults.results.put("actualShowCarTypesExpandedStatus", actualShowCarTypesExpandedStatus)
			resultData.transfers.searchResults.results.put("expectedShowCarTypesExpancdedStatus", expectedShowCarTypesExpancdedStatus)

			//Show Car Types Collapsed Status
			resultData.transfers.searchResults.results.put("actShowCarTypesCollapseStatus", actShowCarTypesCollapseStatus)
			resultData.transfers.searchResults.results.put("expShowCarTypesCollapseStatus", expShowCarTypesCollapseStatus)


		}




		if(getShowCarTypesExpandOrCollapseStatus(index)==false){
			clickShowCarTypes(index)
			sleep(2000)
		}

		try{
			//transferName=getTransferVehicleName(index)
			transferName=getTransferVehicleName(index,transferIndex)
		}catch(Exception e)
		{
			org.junit.Assert.assertNotNull("Unable to fetch Transfer Name from UI", transferName)
		}
		//resultData.transfers.searchResults.expPrice=getPriceTransferItem(index)
		resultData.transfers.searchResults.expPrice=getPriceTransferItem(index,transferIndex)
		//resultData.transfers.searchResults.transferStatus = getTransferStatus(index)
		resultData.transfers.searchResults.transferStatus =getTransferStatus(index,transferIndex)


		if(transferData.validatePagination.toBoolean())
		{
			println("Entered validate pagination")
			//Validate total count of the search results
			int actualtotalSearrchResultsCount=getTotalSearchResultsCount()
			println("$actualtotalSearrchResultsCount")
			resultData.transfers.searchResults.actualtotalSearchResultsCount=actualtotalSearrchResultsCount
			//softAssert.assertEquals(actualtotalSearrchResultsCount,data.totalSearchResults,"\n Search Results Screen Expected & Actual Total Search Results Counts don't match i.e. Expected Count -> ($data.totalSearchResults) != Actual Count -> ($actualtotalSearrchResultsCount)")
			//To Validate first page results
			int totalresults=0
			int actualfirstPageResultCount=getPageResultsCount()
			resultData.transfers.searchResults.actualFirstpageResultCount=actualfirstPageResultCount
			println "Actual First Page Results Count " + actualfirstPageResultCount + " " + "Expected First Page Results Count " + data.resultsPerPage
			resultData.transfers.searchResults.totalFirstPageSearchResults=totalresults+resultData.transfers.searchResults.actualFirstpageResultCount
			sleep(3000)

			if( resultData.transfers.searchResults.actualtotalSearchResultsCount>transferData.resultsPerPage)
			{
				resultData.transfers.pagination.lastPageResults =  resultData.transfers.searchResults.actualtotalSearchResultsCount % transferData.resultsPerPage
				resultData.transfers.pagination.sizeofArr=getAllPageNumbersCount()
				println("$resultData.transfers.pagination.sizeofArr")
				for(int i=2;i<=resultData.transfers.pagination.sizeofArr;i++)
				{
					clickTopNextPage(i-1, resultData.transfers.pagination.sizeofArr)
					sleep(3000)
					waitForAjaxIconToDisappear()
					resultData.transfers.pagination.actualresultsPerPage=getPageResultsCount()
					//To navigate results from second till last but one pages
					if(i< resultData.transfers.pagination.sizeofArr)
					{
						println "Actual $i Page Results Count " +  resultData.transfers.pagination.actualresultsPerPage + " " + "Expected $i Page Results Count " + transferData.resultsPerPage
						resultData.transfers.pagination.put("page"+i, resultData.transfers.pagination.actualresultsPerPage)
					}

					//To navigate last page results
					else
					{
						println "Actual $i (Last)Page Results Count " + resultData.transfers.pagination.actualresultsPerPage  + " " + "Expected $i (Last)Page Results Count " +resultData.transfers.pagination.lastPageResults
						resultData.transfers.pagination."page$i" = resultData.transfers.pagination.actualresultsPerPage
						//Navigate to First Page of the search results
						clickOnFirstPage(resultData.transfers.pagination.sizeofArr)
						sleep(4000)
					}
					totalresults=totalresults+resultData.transfers.pagination.actualresultsPerPage

					println("Total Search Results Count is: $totalresults")
				}
				resultData.transfers.searchResults.totalSearchResults= resultData.transfers.searchResults.totalFirstPageSearchResults+totalresults
				println("Total Search Results Count is: $resultData.transfers.searchResults.totalSearchResults")
			}

		}

		if(transferData.validateSorting.toBoolean())
		{
			//sorting by lowest price
			sleep(5000)
			clickSortByPrice(data.sortByLowestPrice_Index)
			sleep(3000)
			waitForAjaxIconToDisappear()

			int actualtotalSearrchResultsCount=getTotalSearchResultsCount()
			List<String> fpriceList = new ArrayList<String>()
			List<String> ftransferNameList = new ArrayList<String>()
			//Get all the prices existing in first page
			List<String> firstpagepriceList=getPriceList()

			if(actualtotalSearrchResultsCount>transferData.resultsPerPage)
			{
				fpriceList=getPricesOrTransferNamesInAllPages(actualtotalSearrchResultsCount,transferData.resultsPerPage,firstpagepriceList,true,false)
				/*println("Lowest to Highest")
                for(String counter: fpriceList){
                    println(counter);}*/
			}
			resultData.transfers.searchResults.finalPriceListSortByLPrice=fpriceList
			//copy the list
			List<String> temppriceList=new ArrayList<String>(fpriceList)
			Collections.sort(temppriceList)
			println("Expected Sorting Lowest by price List $temppriceList")
			resultData.transfers.searchResults.tempPriceListSortByLPrice=temppriceList
			/*boolean psortresult=fpriceList.equals(temppriceList)
            println("price sorting $psortresult")*/
			sleep(5000)

			//sorting by highest price
			clickSortByPrice(data.sortByHighestPrice_Index)
			sleep(3000)
			waitForAjaxIconToDisappear()

			firstpagepriceList=getPriceList()
			if(actualtotalSearrchResultsCount>data.resultsPerPage)
			{
				fpriceList=getPricesOrTransferNamesInAllPages(actualtotalSearrchResultsCount,data.resultsPerPage,firstpagepriceList,true,false)
				/*println("Highest to Lowest")
                for(String counter: fpriceList){
                    println(counter)}*/
			}
			resultData.transfers.searchResults.finalPriceListSortByHPrice=fpriceList
			//copy the list
			temppriceList=new ArrayList<String>(fpriceList)
			Collections.sort(temppriceList, Collections.reverseOrder());
			boolean psortresult=fpriceList.equals(temppriceList)
			println("price sorting high to low $psortresult")
			resultData.transfers.searchResults.tempPriceListSortByHPrice=temppriceList
			sleep(5000)


			//sorting by alphabetical order
			clickSortByPrice(data.sortByAlphabeticalOrder_Index)
			sleep(5000)
			waitForAjaxIconToDisappear()

			//Get all the transfer names existing in first page
			List<String> firstPagetransferNameList=getTransferNameList()

			if(actualtotalSearrchResultsCount>data.resultsPerPage)
			{
				ftransferNameList=getPricesOrTransferNamesInAllPages(actualtotalSearrchResultsCount,data.resultsPerPage,firstPagetransferNameList,false,true)
				/*for(String counter: ftransferNameList){
                    println(counter);}*/
			}
			resultData.transfers.searchResults.finaltransferNameListSortByAlpha=ftransferNameList
			//copy the list
			List<String> temptransferNameList=new ArrayList<String>(ftransferNameList)
			Collections.sort(temptransferNameList)
			resultData.transfers.searchResults.temptransferNameListSortByAlpha=temptransferNameList
			/*boolean sortresult=ftransferNameList.equals(temptransferNameList)
            println("aplpha sorting $sortresult")*/
			sleep(5000)

			//sorting by reverse of alphabetical order
			clickSortByPrice(data.sortByreverseAlphabeticalOrder_Index)
			sleep(3000)
			waitForAjaxIconToDisappear()

			//Get all the transfer names existing in first page
			firstPagetransferNameList=getTransferNameList()

			if(actualtotalSearrchResultsCount>data.resultsPerPage)
			{
				ftransferNameList=getPricesOrTransferNamesInAllPages(actualtotalSearrchResultsCount,data.resultsPerPage,firstPagetransferNameList,false,true)
				/*for(String counter: ftransferNameList){
                    println(counter);}*/
			}
			resultData.transfers.searchResults.finaltransferNameListSortByRevAlpha=ftransferNameList
			//copy the list
			temptransferNameList=new ArrayList<String>(ftransferNameList)
			Collections.sort(temptransferNameList, Collections.reverseOrder())
			resultData.transfers.searchResults.temptransferNameListSortByRevAlpha=temptransferNameList
			/*boolean sortresult=ftransferNameList.equals(temptransferNameList)
            println("aplpha sorting $sortresult")*/
			sleep(5000)

			//make it to default sorting i.e. sorting by lowest price

			clickSortByPrice(data.sortByLowestPrice_Index)
			sleep(3000)
			waitForAjaxIconToDisappear()
		}


		//validate car name text
		//softAssert.assertEquals(getTransferVehicleName(index),expected_carName_txt,"\n Search Results Screen Actual & Expected Transfer Name details don't match: "+expected_carName_txt)
		//resultData.transfers.searchResults.actualtransferVehicleName=getTransferVehicleName(transferData.index)
		resultData.transfers.searchResults.actualtransferVehicleName=getTransferVehicleName(index,transferIndex)

		//item image
		//resultData.transfers.searchResults.actualtransferimage=getTransferImageDisplayStatus(transferData.index)
		resultData.transfers.searchResults.actualtransferimage=getTransferImageDisplayStatus(index,transferIndex)
		//item image URL
		//resultData.transfers.searchResults.retrievetransferFirstItemimageURL=getTransferImageURLInSearchResults(0)
		resultData.transfers.searchResults.retrievetransferFirstItemimageURL=getTransferImageURLInSearchResults(index,transferIndex)

		//validate car description text
		//softAssert.assertEquals(getTransferVehicleDesc(index),(expected_carDesc_txt), "\n Search Results Screen Text Displayed Car desc is not correct: "+expected_carDesc_txt)
		//resultData.transfers.searchResults.actualTransferVehicleDesc=getTransferVehicleDesc(transferData.index)
		resultData.transfers.searchResults.actualTransferVehicleDesc=getTransfersDesc(transferData.index)

		//validate time
		//softAssert.assertTrue(verifyTime(expected_carTime), "\n Time Displayed is not correct: "+expected_carTime)
		//resultData.transfers.searchResults.actualTime=getTime()

		//service duration icon
		//resultData.transfers.searchResults.actualDurationIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("time",transferData.index)
		resultData.transfers.searchResults.actualDurationIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("time",index,transferIndex)
		//place cursor on top of icon, tooltip show:		Duration"
		//clickTransferItemsIconsDispInSearchResultsScrn("time",transferData.index)
		clickTransferItemsIconsDispInSearchResultsScrn("time",transferData.index,transferIndex)
		//resultData.transfers.searchResults.actualDurationIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("time",transferData.index)
		resultData.transfers.searchResults.actualDurationIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("time",transferData.index,transferIndex)
		//service duration time & value
		//resultData.transfers.searchResults.actualDurationTimeAndValueText=getTransferItemIconsTextInSearchResultsScrn("time",transferData.index)
		resultData.transfers.searchResults.actualDurationTimeAndValueText=getTransferItemIconsTextInSearchResultsScrn("time",transferData.index,transferIndex)

		//"maximum luggage icon
		//resultData.transfers.searchResults.actualLuggageIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("luggage",transferData.index)
		resultData.transfers.searchResults.actualLuggageIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("luggage",transferData.index,transferIndex)
		//place cursor on top of icon, tooltip show:		Luggage allowance"
		//clickTransferItemsIconsDispInSearchResultsScrn("luggage",transferData.index)
		clickTransferItemsIconsDispInSearchResultsScrn("luggage",transferData.index,transferIndex)
		//resultData.transfers.searchResults.actualLuggageIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("luggage",transferData.index)
		resultData.transfers.searchResults.actualLuggageIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("luggage",transferData.index,transferIndex)
		//validate max luggage
		//resultData.transfers.searchResults.actualTransferLuggage=getMaxPassngrOrLuggageAllowance(transferData.expected_maxLuggage)
		resultData.transfers.searchResults.actualTransferLuggage=getTransferItemIconsTextInSearchResultsScrn("luggage",transferData.index,transferIndex)
		//softAssert.assertTrue(verifyMaxPassngrOrLuggageAllowance(expected_carluggage), "\n Max Luggage Displayed is not correct: "+expected_carluggage)

		//"maximum passenger allowed icon
		//resultData.transfers.searchResults.actualPaxIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("pax",transferData.index)
		resultData.transfers.searchResults.actualPaxIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("pax",transferData.index,transferIndex)
		//place cursor on top of icon, tooltip show:		Maximum passenger allowed"
		//clickTransferItemsIconsDispInSearchResultsScrn("pax",transferData.index)
		clickTransferItemsIconsDispInSearchResultsScrn("pax",transferData.index,transferIndex)
		//resultData.transfers.searchResults.actualPaxIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("pax",transferData.index)
		resultData.transfers.searchResults.actualPaxIconMouseHoverText=getTransferItemIconsMouseHoverTextInSearchResultsScrn("pax",transferData.index,transferIndex)
		//validate max pax
		//softAssert.assertEquals(getMaxPax(index),(expected_maxPassengAllwd), "\n Max pax Text Displayed is not correct: "+expected_maxPassengAllwd)
		//softAssert.assertTrue(verifyMaxPassngrOrLuggageAllowance(expected_maxPassengAllwd), "\n Max Pax Displayed is not correct: "+expected_maxPassengAllwd)
		//resultData.transfers.searchResults.actualTransferPax=getMaxPassngrOrLuggageAllowance(transferData.expected_maxpassengersAllwd)
		resultData.transfers.searchResults.actualTransferPax=getTransferItemIconsTextInSearchResultsScrn("pax",transferData.index,transferIndex)

		if(transferData.transfers_passenger_assitance_icon.toBoolean()) {
			try {
				//Language Supported icon
				resultData.transfers.searchResults.actualLanguageIconDispStatus = getTransferItemsIconsDispInSearchResultsScrn("language", transferData.index,transferIndex)
				//place cursor on top of icon, tooltip show:		Language Supported"
				clickTransferItemsIconsDispInSearchResultsScrn("language", transferData.index,transferIndex)
				resultData.transfers.searchResults.actualLanguageIconMouseHoverText = getTransferItemIconsMouseHoverTextInSearchResultsScrn("language", transferData.index,transferIndex)
				//Language Supported Value
				resultData.transfers.searchResults.actualLanguageValueText = getTransferItemIconsTextInSearchResultsScrn("language", transferData.index,transferIndex)
			} catch (Exception e) {
				//Language Supported icon
				resultData.transfers.searchResults.actualLanguageIconDispStatus = "Unable To Read lang icon display status"
				//place cursor on top of icon, tooltip show:		Language Supported"

				resultData.transfers.searchResults.actualLanguageIconMouseHoverText = "Unable To Read lang icon mouse hover text"
				//Language Supported Value
				resultData.transfers.searchResults.actualLanguageValueText = "Unable To Read language value text"

			}
		}
		//transfer condition icon & link
		//resultData.transfers.searchResults.actualConditionsIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("conditions",transferData.index)
		resultData.transfers.searchResults.actualConditionsIconDispStatus=getTransferItemsIconsDispInSearchResultsScrn("conditions",transferData.index,transferIndex)
		//click on transfer condition icon
		//clickTransferItemsIconsDispInSearchResultsScrn("conditions",transferData.index)
		clickTransferItemsIconsDispInSearchResultsScrn("conditions",transferData.index,transferIndex)
		sleep(5000)
		//user taken to Special Conditions lightbox - Displayed
		resultData.transfers.searchResults.actualTransferConditionsDisplayStatus=getTransferConditionsLightboxDisplayStatus()
		//Special Conditions title text
		resultData.transfers.searchResults.actualTransferConditionstitleText=getTransferConditionsPopupTitleText()
		//Close X function
		resultData.transfers.searchResults.actualTransferConditionsCloseXDisplayStatus=overlayCloseButton()

		//Transfer Conditions
		resultData.transfers.searchResults.actualTransferConditionsText=getTransferContions()
		//Meeting Point Text
		resultData.transfers.searchResults.actualTransferConditionsMeetingPointText=getMeetingPoint().trim()

		//Close
		closeLightBox()

		//confirm status text - Available or On Request
		//resultData.transfers.searchResults.actualTransferStatusTxt=getTransferStatus(transferData.index).trim()
		resultData.transfers.searchResults.actualTransferStatusTxt=getTransferStatus(transferData.index,transferIndex).trim()

		//validate free cancellation - hyperlink
		//validte free cancellation text along with the cancellation date which is 5 days earlier to booking date
		resultData.transfers.searchResults.expectedcancellationText_Date=transferData.expected_cancel_txt+" "+cancelDate.toUpperCase()
		//resultData.transfers.searchResults.actualCancellationText_Date=getCancellationChagesHyperLinkAndText(transferData.index)
		resultData.transfers.searchResults.actualCancellationText_Date=getCancellationChagesHyperLinkAndText(transferData.index,transferIndex)
		println("Actual $resultData.transfers.searchResults.actualCancellationText_Date")
		println("Expected $resultData.transfers.searchResults.expectedcancellationText_Date")

		//click on charges hyperlink
		clickOnCancelCharges(index,transferIndex)

		//Capture cancellation header
		resultData.transfers.searchResults.actualCancellationHeaderTxt=getCancellationPopupHeader()

		resultData.transfers.searchResults.actualSummaryText=getCarSummaryText(transferData.expected_summary_txt)

		//resultData.transfers.searchResults.actualCancellationChargeText=getTransferCancellationChargeText(transferData.expected_cancel_chrg_txt)
		resultData.transfers.searchResults.actualCancellationChargeText=getTransferCancellationChargesText(transferData.expected_cancel_chrg_txt)
		resultData.transfers.searchResults.actualPopupFooterText=getPopupFooterText(transferData.expected_popup_txt)

		//close x function
		resultData.transfers.searchResults.actualCloseXFuctionDispStatus=overlayCloseButton()
		//Click on close button to close popup
		closeLightBox()
		//clickOnCloseButton()

		//service item amount & currency
		//resultData.transfers.searchResults.expected_price_curr=getPriceAndCurrencyTransferItem(transferData.index).replaceAll(",","")
		resultData.transfers.searchResults.expected_price_curr=getPriceAndCurrencyTransferItem(transferData.index,transferIndex).replaceAll(",","")
		println ("$resultData.transfers.searchResults.expected_price_curr")
		//resultData.transfers.searchResults.actualpricecurrDispStatus=getPriceAndCurrencyTransferItemDispStatus(transferData.index)
		resultData.transfers.searchResults.actualpricecurrDispStatus=getPriceAndCurrencyTransferItemDispStatus(transferData.index,transferIndex)
		//<Add to itineray> functional button
		//resultData.transfers.searchResults.actualAddToItineraryButtonDispStatus=getAddToItineraryTransferItemDispStatus(transferData.index,1)
		resultData.transfers.searchResults.actualAddToItineraryButtonDispStatus=getAddToItineraryTransferItemDispStatus(transferData.index,transferIndex,0)


		//click on add to Itineary button
		//clickOnAddToItenary(index)
		clickOnAddToItenary(transferData.index,transferIndex)
		//clickOnAddToItenary(index+1)
		sleep(5000)
		/* commenting code since itinerary builder is removed in 10.3
		at HotelSearchResultsPage
		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Collapse
			hideItenaryBuilder()
			sleep(3000)
		}
		at TransferSearchResultsPage
		//item image
		resultData.transfers.searchResults.actualTransferItemImageDispStatus=getImgDisplayedInItineraryCardinItineraryBuilder(0)
		//transfer itineary name
		 resultData.transfers.searchResults.actualTransferItineraryName=getTransferItineraryItem(0)
		 //transfer pax
		 resultData.transfers.searchResults.actualItineraryPax=getPax(0)
		 //car desc text
		 resultData.transfers.searchResults.actualItineraryDesc=getItenaryDescreption(0)
		 // status
		 resultData.transfers.searchResults.actualItineraryStatus=getAvailability(0)
		 //price with currency
		 resultData.transfers.searchResults.actualItineraryPrice=gettheItenaryPrice(0)
		 //Itinerary ID and Name
		resultData.transfers.searchResults.actualItineraryIDAndNameDispStatus=getItineraryIdAndNamedispStatus()
		//Service Date, time & Value
		resultData.transfers.searchResults.actualServiceDateTimeValueText=getCancellationAndAmendmentItemOverlayHeaders(0)


	 		and :"go to itinerary builder page"
			at ItenaryBuilderPage

			//click on Go To Itineary button
			clickOnGotoItenaryButton()
			sleep(5000)

			*/
		resultData.transfers.searchResults.expectedServiceDateTimeValueText=resultData.transfers.searchResults.actualmodifiedDate+" "+resultData.transfers.searchResults.actualDefaultTime
		and :"go to itinerary page"
		at ItineraryTravllerDetailsPage

		//Edit Itinerary
		//click on Edit
		scrollToTopOfThePage()
		clickEditIconNextToItineraryHeader()
		sleep(3000)
		waitTillLoadingIconDisappears()
		//Update Name Text
		String todaysDate= dateUtil.addDaysChangeDatetformat(0, "ddMMMyy")
		println("$todaysDate")
		resultData.transfers.itineraryPage.expectedItnrName=todaysDate
		enterItenaryName(resultData.transfers.itineraryPage.expectedItnrName)

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
		resultData.transfers.itineraryPage.actualSavedItnrName=edtitinaryName.replaceAll("\nEdit", "")


		if(subPath=="AirportToHotel." || subPath=="HotelToAirport.")
		{
			scrollToBottomOfThePage()
		}

		//add agent refrence name and
		clickOnAddAgentRefButton()
		sleep(3000)
		enterAgentRef(data.agentRef)
		sleep(2000)
		// clik on save
		clickOnSaveOrCancelBtnsAgentRef(1)

		waitTillLoadingIconDisappears()
		sleep(5000)
		//user should be able to do so.
		//resultData.transfers.itineraryPage.actualSavedAgentRefName=getLeadTravellerName(4)
		resultData.transfers.itineraryPage.actualSavedAgentRefName=getSavedAgentRefName()
		// Save button should be removed .
		resultData.transfers.itineraryPage.actualSaveBtnDispStatus=verifySaveButtonInAgentRefSecDispStatus()
		//Edit button should be displayed
		resultData.transfers.itineraryPage.actualEditBtnDispStatus=verifyEditButtonDisplayed(0)

		//Non booked
		//the transfer item is included in Non-Booked Items block
		resultData.transfers.itineraryPage.actualTransferNameInNonBookedItms=getTransferNameInSuggestedItem(0)

		//Non-Booked Items - title text
		resultData.transfers.itineraryPage.actualNonBookedItemTxt=getNonBookedItemsText()
		//click on suggested items question mark icon
		clickonQstnMarkIcon()
		sleep(2000)

		//capture the text
		resultData.transfers.itineraryPage.actualQstnMarkTxt=getQuestionMarkMouseHoverTxt()

		//<Book> function button
		resultData.transfers.itineraryPage.actualBookButtonDispStatus=getBookBtndisplayStatus()
		//item image
		resultData.transfers.itineraryPage.actualNonBookedItemImageSrcURL=getItemsImageURLInItinerary(0)


		//Suggested Items validation

		//Transfer name in suggested item
		resultData.transfers.itineraryPage.actualTransferNameInSuggestedItem=getTransferNameInSuggestedItem(0)

		//pax
		resultData.transfers.itineraryPage.actualTransferPaxSuggestedItem=getIternarySuggestedItemsPaxOrDesc(0)

		//description
		resultData.transfers.itineraryPage.actualTransferDescSuggestedItem=getIternarySuggestedItemsPaxOrDesc(1)

		//code removed from this place
		TestCase21(transferData)

		//println("Transfer Date and time in non booked items: $resultData.transfers.itineraryPage.actualTransferDateAndtimeInNonBookedItms")
		//click on cancellation link
		clickSuggestedItemCancellationLink(0)
		sleep(3000)
		TestCase20(transferData)
		resultData.transfers.itineraryPage.expectedTransferCancellationOverlayHeaderText_2=transferData.cancel_HeaderText_2+" "+depDate.toUpperCase()

		//close cancel popup
		clickOnCloseButtonSuggestedItemsCancelpopup()

		sleep(5000)

		//at ItenaryPage
		at ItenaryPageItems
		//Input Title
		selectTitle(transferData.expected_transfers_title,0)
		//Input First Name
		enterFirstName(transferData.fNameFirst,0)
		//Input Last Name
		enterLastName(transferData.LNameFirst,0)
		//Input Email Address
		//resultData.transfers.itineraryPage.emailAddr=TestConf.getStringValue("travellerSite.email")
		resultData.transfers.itineraryPage.emailAddr=transferData.transfers_AgentEmailId
		enterEmail(resultData.transfers.itineraryPage.emailAddr,0)
		//Input Area Code
		//enterCountryCode(transferData.countryCode,0) /*/*commented for IST exection*/*/
		//enterCountryCode(transferData.countryCode,0) //commented for SIT 10.3
		//sleep(1000)
		//Input Telephone number
		enterTelephoneNumber(transferData.phoneNum,0)
		sleep(1000)
		if((subPath=="AirportToHotel.") && (transferData.pax.toInteger()>2)) {
			//Add Travellers and click on save
			fillTravellerDetailsNotSaved(transferData.expected_transfers_title, transferData.fNameSecond, transferData.LNameSecond, 1)
			fillTravellerDetailsNotSaved(transferData.expected_transfers_title, transferData.fNameThird, transferData.LNameThird, 2)
			sleep(4000)
		}else{
			//Add Travellers and click on save
			addNumberTravellers(transferData.expected_transfers_title, transferData.fNameSecond, transferData.LNameSecond, 1)

		}
		clickonSaveButton(0)
		sleep(2000)
		waitForAjaxIconToDisappear()

		//capture entered lead traveller details are displayed correctly
		resultData.transfers.itineraryPage.expectedleadTravellerName=transferData.expected_transfers_title+" "+transferData.fNameFirst+" "+transferData.LNameFirst
		println("$resultData.transfers.itineraryPage.expectedleadTravellerName")
		//resultData.transfers.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)
		resultData.transfers.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)

		//email
		resultData.transfers.itineraryPage.actualemailId=getLeadTravellerName(2)
		//capture entered first traveller details are displayed correctly
		resultData.transfers.itineraryPage.expectedFirstTravellerName=transferData.expected_transfers_title+" "+transferData.fNameSecond+" "+transferData.LNameSecond
		resultData.transfers.itineraryPage.actualFirstTravellerName=getLeadTravellerName(3)

		sleep(5000)
		//CLICK ON BOOK
		clickBookButton(0)
		sleep(5000)

		//booking description text
		resultData.transfers.itineraryPage.expectedBookingDesc = transferData.expected_car_txt+","+" "+transferData.expected_carDesc_txt+" "+transferData.expected_itinearyPax+","+" "+transferData.transfers_luggagePieces+" luggage pieces"+" "+dateUtil.addDaysChangeDatetformat(data.checkInDays.toInteger(), "ddMMMyy").toUpperCase()+" "+transferData.transfers_pickupTime_Hrs+":"+transferData.timeOfArrival_mins
		println("$resultData.transfers.confirmationPage.expectedBookingDesc")
		resultData.transfers.itineraryPage.actualBookingDesc=getBookingDescText()

		//Lead passenger details
		resultData.transfers.itineraryPage.actualfirstPassengerName=getPassengerNameDetails(0)
		resultData.transfers.itineraryPage.expectedfirstPassengerName=transferData.fNameFirst+" "+transferData.LNameFirst+"(Lead Name)"
		//Other Traveller Details
		resultData.transfers.itineraryPage.actualsecondPassengerName=getPassengerNameDetails(1)
		resultData.transfers.itineraryPage.expectedsecondPassengerName=transferData.fNameSecond+" "+transferData.LNameSecond


		if(subPath=="AirportToHotel.")
		{
			//Drop Off point details
			resultData.transfers.itineraryPage.actualDropOffDetailsATH=getBookingPickUpOrDropOffDetailsText(1)

		}
		else if (subPath=="HotelToAirport.")
		{
			//Pick Up details
			resultData.transfers.itineraryPage.actualpickUpDetailsHTA=getBookingPickUpOrDropOffDetailsText(0)

		}
		else if (subPath=="HotelToStation.")
		{
			//Pick Up details
			resultData.transfers.itineraryPage.actualpickUpDetailsHTS=getBookingPickUpOrDropOffDetailsText(0)

		}

		else if(subPath=="HotelToPort.")
		{
			//Pick Up details
			resultData.transfers.itineraryPage.actualpickUpDetailsHTP=getBookingPickUpOrDropOffDetailsText(0)
		}
		else if(subPath=="PortToHotel.")
		{
			//Drop Off point details
			resultData.transfers.itineraryPage.actualpickUpDetailsPTH=getBookingPickUpOrDropOffDetailsText(1)
		}

		else if(subPath=="HotelToOther.")
		{
			//Pick Up details
			resultData.transfers.itineraryPage.actualpickUpDetailsHTO=getBookingPickUpOrDropOffDetailsText(0)
		}

		else if(subPath=="OtherToHotel.")
		{
			//Drop Off details
			resultData.transfers.itineraryPage.actualpickUpDetailsOTH=getBookingPickUpOrDropOffDetailsText(1)
		}


		//Transfer Conditions Header text
		resultData.transfers.itineraryPage.actualheaderText1 = getOverlayHeadersTextInAboutToBookItems(0)

		//Transfer Conditions Desc text
		resultData.transfers.itineraryPage.actualTransfersConditionsText=getTextInAboutToBookItems(0)


		//Special Conditions Header text
		resultData.transfers.itineraryPage.actualheaderText2 = getOverlayHeadersTextInAboutToBookItems(1)
		//Special conditions Desc text
		resultData.transfers.itineraryPage.actualSpecialConditionsText=getTextInAboutToBookItems(1)

		//Cancellation Charge Header Text
		//resultData.transfers.itineraryPage.actualheaderText3 = getOverlayHeadersTextInAboutToBookItems(2)
		/*commented for IST exection*/
		resultData.transfers.itineraryPage.actualheaderText3=getOverlayHeadersTextInAboutToBookItemScrn(0)

		//All Dates Text
		//resultData.transfers.itineraryPage.actualDatesText=getTextInAboutToBookItems(2)
		resultData.transfers.itineraryPage.actualDatesText=getTextInAboutToBookItems(3)

		//Selecting travellers. Note: this step is not there in test case - but we cannot click on confirm booking without selecting travellers

		boolean firstCheckBoxStatus=getTravellerCheckBoxCheckedStatus(0)
		boolean secondCheckBoxStatus=getTravellerCheckBoxCheckedStatus(1)
		//commented since in 10.3 by default travllers are selected
		/*if(!firstCheckBoxStatus)
        {
                clickOnTravellerCheckBox(0)
                sleep(3000)
        }*/
		/*int maxTravellers=transferData.pax.toInteger()
        //Selecting all travellers
        for(int i=0;i<maxTravellers;i++) {

            clickOnTravellerCheckBox(i)
            sleep(500)
        }

        if(!secondCheckBoxStatus)
        {
                clickOnTravellerCheckBox(1)
        }*/



		if(subPath=="AirportToHotel.")
		{
			//Input Flight Number
			enterPickUpFlightNumber(transferData.flightNumber)

			//Input Arriving From
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of arrival - hrs
			enterPickUpArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of arrival - mins
			//enterPickUpArrivalMins(transferData.timeOfArrival_mins)
			enterArrivalMins(transferData.timeOfArrival_mins)
		}
		else if (subPath=="HotelToAirport.")
		{
			//Input Flight Number
			enterFlightNumber(transferData.flightNumber)

			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)
			//Input time of arrival - hrs
			enterDEpartureTime(transferData.timeOfArrival_Hrs)
			sleep(2000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfArrival_mins)



		}
		else if(subPath=="HotelToStation.")
		{
			//Input Train Name
			enterTrain(transferData.transfers_trainName)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)

			//Input time of arrival - hrs
			enterDEpartureTime(transferData.timeOfDepart_Hrs)
			sleep(2000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfDepart_mins)

		}

		else if(subPath=="HotelToPort.")
		{
			//Input Ship Name
			enterShipName(transferData.transfers_shipName)
			//Input Ship Company
			enterShipCompany(transferData.transfers_shipCompany)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)

			//Input time of departure - hrs
			enterShipDepartureTime(transferData.timeOfArrival_Hrs)
			sleep(2000)
			//Input time of departure - mins
			enterShipDepartureTimeMins(transferData.timeOfArrival_mins)

		}
		else if(subPath=="PortToHotel.")
		{
			//Input Ship Name
			enterPickUpShipName(transferData.transfers_shipName)
			//Input Ship Company
			enterPickUpShipCompany(transferData.transfers_shipCompany)
			//Input
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of departure - hrs
			enterShipArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of departure - mins
			enterShipArrivalTimeMins(transferData.timeOfArrival_mins)
		}

		else if(subPath=="HotelToOther.")
		{
			//Input Address
			enterAddress(transferData.transfers_Address)
			//Input PostCode
			enterPostCode(transferData.transfers_Pincode)
			/*
            //Input time of departure - hrs
            enterShipDepartureTime(transferData.timeOfArrival_Hrs)
            sleep(2000)
            //Input time of departure - mins
            enterShipDepartureTimeMins(transferData.timeOfArrival_mins) */
		}

		else if(subPath=="OtherToHotel.")
		{
			//Input Address
			//enterPickUpAddress(transferData.transfers_Address)
			//Input PostCode
			//enterPickUpPostCode(transferData.transfers_Pincode)

			//Input Address 1
			enterPickUpAddress1(transferData.transfers_Address1)

			//Input Address 2
			enterPickUpAddress2(transferData.transfers_Address2)

			//Input Pick Up City
			enterPickUpCity(transferData.city)

			//Input PostCode
			enterPickUpPostCode(transferData.transfers_Pincode)
		}

		else if(subPath=="AirportToAirport.")
		{
			//Input Flight Number
			enterPickUpFlightNumber(transferData.flightNumber)
			//Input Arriving From
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of arrival - hrs
			enterPickUpArrivalTime(transferData.timeOfArrival_Hrs)
			sleep(3000)
			//Input time of arrival - mins
			enterTimeOfArrivalMins(transferData.timeOfArrival_mins)

			//Input Flight Number
			enterFlightNumber(transferData.transfers_dropFlightNo)

			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)
			sleep(3000)
			//Input time of departure - hrs
			enterDEpartureTime(transferData.timeOfDepart_Hrs)
			sleep(3000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfDepart_mins)
			sleep(3000)
		}

		else if(subPath=="AirportToStation.")
		{
			//Input Flight Number
			enterPickUpFlightNumber(transferData.flightNumber)
			//Input Arriving From
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of arrival - hrs
			enterPickUpArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of arrival - mins
			enterTimeOfArrivalMins(transferData.timeOfArrival_mins)

			//Input Train Name
			//enterFlightNumber(transferData.transfers_trainName)
			enterTrain(transferData.transfers_trainName)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)

			//Input time of departure - hrs
			enterDEpartureTime(transferData.timeOfDepart_Hrs)
			sleep(2000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfDepart_mins)
		}

		else if(subPath=="StationToAirport.")
		{
			//Input Train Name
			enterTrainNumber(transferData.transfers_trainName)
			//Arrival From
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of departure - hrs
			enterPickUpArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of arrival - mins
			enterTimeOfArrivalMins(transferData.timeOfArrival_mins)

			//Input Flight Number
			enterFlightNumber(transferData.flightNumber)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)
			//Input time of arrival - hrs
			enterDEpartureTime(transferData.timeOfDepart_Hrs)
			sleep(2000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfDepart_mins)


		}

		else if(subPath=="AirportToPort.")
		{
			//Input Flight Number
			enterPickUpFlightNumber(transferData.flightNumber)
			//Input Arriving From
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of arrival - hrs
			enterPickUpArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of arrival - mins
			enterTimeOfArrivalMins(transferData.timeOfArrival_mins)

			//Input Ship Name
			enterShipName(transferData.transfers_shipName)
			//Input Ship Company
			enterShipCompany(transferData.transfers_shipCompany)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)
			//Input time of departure - hrs
			enterShipDepartureTime(transferData.timeOfDepart_Hrs)
			//Input time of departure - mins
			enterShipDepartureTimeMins(transferData.timeOfDepart_mins)

		}

		else if(subPath=="PortToAirport.")
		{
			//Input Ship Name
			enterPickUpShipName(transferData.transfers_shipName)
			//Input Ship Company
			enterPickUpShipCompany(transferData.transfers_shipCompany)
			//Input Arriving From
			//enterAndSelectArrivingFrom(transferData.arrivingText,transferData.arrivingFrom)
			enterArrivalFrom(transferData.arrivingText)
			sleep(2000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)

			//Input time of departure - hrs
			enterShipArrivalTime(transferData.timeOfArrival_Hrs)
			sleep(2000)
			//Input time of departure - mins
			enterShipArrivalTimeMins(transferData.timeOfArrival_mins)

			//Input Flight Number
			enterFlightNumber(transferData.flightNumber)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)
			//Input time of arrival - hrs
			enterDEpartureTime(transferData.timeOfDepart_Hrs)
			sleep(2000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfDepart_mins)

		}

		else if(subPath=="AirportToOther.")
		{
			//Input Flight Number
			enterPickUpFlightNumber(transferData.flightNumber)
			//Input Arriving From
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of arrival - hrs
			enterPickUpArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of arrival - mins
			enterTimeOfArrivalMins(transferData.timeOfArrival_mins)

			//Input Address
			//enterAddress(transferData.transfers_Address)
			//Input Address 1
			enterDropOffAddress1(transferData.transfers_Address1)
			//Input Address 2
			enterDropOffAddress2(transferData.transfers_Address2)
			//Input City
			enterDropOffCity(transferData.city)
			//Input PostCode
			enterDropOffPostCode(transferData.transfers_Pincode)
			//Input time of departure - hrs
			//enterShipDepartureTime(transferData.timeOfArrival_Hrs)
			//Input time of departure - mins
			//enterShipDepartureTimeMins(transferData.timeOfArrival_mins)
		}

		else if(subPath=="OtherToAirport.")
		{
			//Input Address
			//enterPickUpAddress(transferData.transfers_Address)

			//Input Address 1
			enterPickUpAddress1(transferData.transfers_Address1)

			//Input Address 2
			enterPickUpAddress2(transferData.transfers_Address2)

			//Input Pick Up City
			enterPickUpCity(transferData.city)

			//Input PostCode
			enterPickUpPostCode(transferData.transfers_Pincode)

			//Input Flight Number
			enterFlightNumber(transferData.flightNumber)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)
			//Input time of arrival - hrs
			enterDEpartureTime(transferData.timeOfDepart_Hrs)
			sleep(2000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfDepart_mins)

		}
		else if(subPath=="StationToPort.")
		{
			//Input Train Name
			enterTrainNumber(transferData.transfers_trainName)
			//Input Arriving From
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of departure - hrs
			enterPickUpArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of arrival - mins
			enterTimeOfArrivalMins(transferData.timeOfArrival_mins)

			//Input Ship Name
			enterShipName(transferData.transfers_shipName)
			//Input Ship Company
			enterShipCompany(transferData.transfers_shipCompany)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)

			//Input time of departure - hrs
			enterShipDepartureTime(transferData.timeOfDepart_Hrs)
			//Input time of departure - mins
			enterShipDepartureTimeMins(transferData.timeOfDepart_mins)
		}

		else if(subPath=="PortToStation.")
		{
			//Input Ship Name
			enterPickUpShipName(transferData.transfers_shipName)
			//Input Ship Company
			enterPickUpShipCompany(transferData.transfers_shipCompany)
			//Input Arriving From Text
			enterArrivalFrom(transferData.arrivingText)
			sleep(3000)
			//Auto Suggest select
			selectArrivalAutoSuggest(transferData.arrivingFrom)
			//Input time of departure - hrs
			enterShipArrivalTime(transferData.timeOfArrival_Hrs)
			//Input time of departure - mins
			enterShipArrivalTimeMins(transferData.timeOfArrival_mins)

			//Input Train Name
			enterTrain(transferData.transfers_trainName)
			//Input Departing To
			enterAndSelectDepartingTo(transferData.transfers_departTxt,transferData.transfers_departingTo)

			//Input time of departure - hrs
			enterDEpartureTime(transferData.timeOfDepart_Hrs)
			sleep(2000)
			//Input time of arrival - mins
			enterDEpartureTimeMins(transferData.timeOfDepart_mins)

		}

		//total in confirm booking
		//resultData.transfers.itineraryPage.actualTotalInconfirmBooking=getTotalInConfirmbooking()
		resultData.transfers.itineraryPage.actualTotalInconfirmBooking=getTotalInConfirmbooking(0).replaceAll(",","")
		sleep(1000)

		//at ItenaryPage

		//Click on Confirm Booking
		//clickConfirmBooking()
		clickOnConfirmBookingAndPayNow()
		sleep(5000)


		try{
			//wait for confirmation page
			waitTillConformationPage()
			//waitTillLoadingIconDisappears()

			//scroll to top of the page
			scrollToTopOfThePage()

			//Lead Traveller Details - Confirmation Dialog
			resultData.transfers.confirmationPage.actualLeadTravellerDetailsConfirmationDialog=getLeadTravellerDetailsConfirmationDialog(0)

		}catch(Exception e)
		{
			Assert.assertFalse(true,"Failed To Complete Booking")
		}

		//Email
		try{
			resultData.transfers.confirmationPage.actualAgentEmailId=getEmailConfirmationDialog(1)
		}catch(Exception e){
			resultData.transfers.confirmationPage.actualAgentEmailId="Unable To Read Confirmation Text And Email ID from UI"
		}

		//resultData.transfers.confirmationPage.actualAgentEmailId=resultData.transfers.confirmationPage.actualAgentEmailId.toLowerCase()
		//resultData.transfers.confirmationPage.expectedAgentEmailId=transferData.transfers_AgentEmailId
		//resultData.transfers.confirmationPage.expectedAgentEmailId=clientData.usernameOrEmail.toLowerCase()
		resultData.transfers.confirmationPage.expectedAgentEmailId=transferData.transfers_mailTxt
		//Departure Date
		resultData.transfers.confirmationPage.actualConfimrationDialogDepDate=getBookingDepartDate()
		resultData.transfers.confirmationPage.expectedConfimrationDialogDepDate=depDate.toUpperCase()


		//read Booking id
		resultData.transfers.confirmationPage.actualbookingID=getBookingIdFromBookingConfirmed(0)
		println "Booking ID:$resultData.transfers.confirmationPage.actualbookingID"

		//read Itineary id
		resultData.transfers.confirmationPage.readitinearyID=getItinearyID(0)
		println "Itineary ID:$resultData.transfers.confirmationPage.actualitinearyID"

		//Transfer name
		resultData.transfers.confirmationPage.actualTransferNameConfirmationDialog=getConfirmBookedTransferName()

		//Transfer desc
		//resultData.transfers.confirmationPage.expectedTransferDescBookingConf_txt=transferData.expected_carDesc_txt+", "+transferData.expected_itinearyPax
		//resultData.transfers.confirmationPage.expectedTransferDescBookingConf_txt=transferData.expected_carDesc_txt+",  "+(data.pax.toInteger()-1)+" PAX"
		resultData.transfers.confirmationPage.expectedTransferDescBookingConf_txt=transferData.expected_carDesc_txt+",  "+(data.pax.toInteger())+" PAX"
		println "$resultData.transfers.confirmationPage.expectedTransferDescBookingConf_txt"
		resultData.transfers.confirmationPage.actualTransferDescBookingConf_txt=getTransferDescBookingConfirmed()


		if(subPath=="AirportToHotel.")
		{

			//pickup
			resultData.transfers.confirmationPage.expectedPickupATH = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			println "$resultData.transfers.confirmationPage.expectedPickup"
			resultData.transfers.confirmationPage.actualPickupATH=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			resultData.transfers.confirmationPage.actualDropOffATH=getDropOffDetailsOnBookingConfirmation()

		}
		else if (subPath=="HotelToAirport.")
		{

			//pickup
			resultData.transfers.confirmationPage.actualPickupHTA=getPickUpDetailsOnBookingConfirmation()

			//dropoff
			resultData.transfers.confirmationPage.expectedDropOffHTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			println "$resultData.transfers.confirmationPage.expectedDropOffHTA"
			resultData.transfers.confirmationPage.actualDropOffHTA=getDropOffDetailsOnBookingConfirmation()


		}
		else if(subPath=="HotelToStation.")
		{
			//pickup
			resultData.transfers.confirmationPage.actualPickupHTS=getPickUpDetailsOnBookingConfirmation()

			//validate dropoff
			//resultData.transfers.confirmationPage.expecteddropOffHTS = "Drop off: Train Name: $data.transfers_trainName. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.confirmationPage.expecteddropOffHTS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $transferData.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualdropOffHTS=getDropOffDetailsOnBookingConfirmation()


		}
		else if(subPath=="HotelToPort.")
		{
			//pickup
			resultData.transfers.confirmationPage.actualPickupHTP=getPickUpDetailsOnBookingConfirmation()

			//validate dropoff
			resultData.transfers.confirmationPage.expecteddropOffHTP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.confirmationPage.actualdropOffHTP=getDropOffDetailsOnBookingConfirmation()

		}
		else if(subPath=="PortToHotel.")
		{
			//pickup
			resultData.transfers.confirmationPage.actualPickupPTH=getPickUpDetailsOnBookingConfirmation()
			resultData.transfers.confirmationPage.expectedPickupPTH = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			//validate dropoff
			resultData.transfers.confirmationPage.actualdropOffPTH=getDropOffDetailsOnBookingConfirmation()

		}

		else if(subPath=="HotelToOther.")
		{
			//pickup
			resultData.transfers.confirmationPage.actualPickupHTO=getPickUpDetailsOnBookingConfirmation()

			//validate dropoff
			//resultData.transfers.confirmationPage.expecteddropOffHTO = "Drop off: Address: $data.transfers_Address, $data.transfers_Pincode. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.confirmationPage.expecteddropOffHTO = "Drop off: Address $data.transfers_Address, $data.transfers_Pincode."
			resultData.transfers.confirmationPage.actualdropOffHTO=getDropOffDetailsOnBookingConfirmation()

		}

		else if(subPath=="OtherToHotel.")
		{
			//pickup
			resultData.transfers.confirmationPage.actualPickupOTH=getPickUpDetailsOnBookingConfirmation()
			//resultData.transfers.confirmationPage.expectedPickupOTH = "Pick up: Address: $data.transfers_Address, $data.transfers_Pincode. $data.transfers_pickupTime_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.confirmationPage.expectedPickupOTH = "Pick up: Address $data.transfers_Address, $data.transfers_Pincode"
			//validate dropoff
			resultData.transfers.confirmationPage.actualdropOffOTH=getDropOffDetailsOnBookingConfirmation()

		}

		else if(subPath=="AirportToAirport.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupATA = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.confirmationPage.actualPickupATA=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			resultData.transfers.confirmationPage.expectedDropOffATA = "Drop off: Flight Number: $transferData.transfers_dropFlightNo. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffATA=getDropOffDetailsOnBookingConfirmation()
		}

		else if(subPath=="AirportToStation.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupATS = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.confirmationPage.actualPickupATS=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			resultData.transfers.confirmationPage.expectedDropOffATS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $transferData.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffATS=getDropOffDetailsOnBookingConfirmation()
		}

		else if(subPath=="StationToAirport.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupSTA = "Pick up: "+"Station: $transferData.pickup"+" Train Name:"+" "+data.transfers_trainName+"  "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.confirmationPage.actualPickupSTA=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			resultData.transfers.confirmationPage.expectedDropOffSTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffSTA=getDropOffDetailsOnBookingConfirmation()
		}

		else if(subPath=="AirportToPort.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupATP = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.confirmationPage.actualPickupATP=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			resultData.transfers.confirmationPage.expectedDropOffATP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffATP=getDropOffDetailsOnBookingConfirmation()
		}

		else if(subPath=="PortToAirport.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupPTA = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.confirmationPage.actualPickupPTA=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			resultData.transfers.confirmationPage.expectedDropOffPTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffPTA=getDropOffDetailsOnBookingConfirmation()
		}
		else if(subPath=="AirportToOther.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupATO = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.confirmationPage.actualPickupATO=getPickUpDetailsOnBookingConfirmation()

			//validate dropoff
			resultData.transfers.confirmationPage.expecteddropOffATO = "Drop off: Address $data.transfers_Address, $data.transfers_Pincode."
			resultData.transfers.confirmationPage.actualdropOffATO=getDropOffDetailsOnBookingConfirmation()
		}

		else if(subPath=="OtherToAirport.")
		{
			//pickup
			resultData.transfers.confirmationPage.actualPickupOTA=getPickUpDetailsOnBookingConfirmation()
			//resultData.transfers.confirmationPage.expectedPickupOTA = "Pick up: Address: $data.transfers_Address, $data.transfers_Pincode. $data.transfers_pickupTime_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.confirmationPage.expectedPickupOTA = "Pick up: Address $data.transfers_Address, $data.transfers_Pincode"
			// dropoff
			resultData.transfers.confirmationPage.expectedDropOffOTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffOTA=getDropOffDetailsOnBookingConfirmation()
		}

		else if(subPath=="StationToPort.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupSTP = "Pick up: "+"Station: $transferData.pickup"+" Train Name:"+" "+data.transfers_trainName+"  "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins

			resultData.transfers.confirmationPage.actualPickupSTP=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			resultData.transfers.confirmationPage.expectedDropOffSTP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffSTP=getDropOffDetailsOnBookingConfirmation()
		}
		else if(subPath=="PortToStation.")
		{
			//pickup
			resultData.transfers.confirmationPage.expectedPickupPTS = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.confirmationPage.actualPickupPTS=getPickUpDetailsOnBookingConfirmation()

			// dropoff
			//resultData.transfers.confirmationPage.expectedDropOffPTS = "Drop off: Train Name: $data.transfers_trainName. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.expectedDropOffPTS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $transferData.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.confirmationPage.actualDropOffPTS=getDropOffDetailsOnBookingConfirmation()
		}

		//Transfer Service Date and time
		resultData.transfers.confirmationPage.actualTransferDateAndTimeInBookingConf=getTransferDateAndTimeInBookingConfirmed()
		println("Transfer Service Date and time in Confirmation Screen: $resultData.transfers.confirmationPage.actualTransferDateAndTimeInBookingConf")
		//Commission Value
		float fpercentage = Float.parseFloat(transferData.transfers_percentage)
		resultData.transfers.searchResults.expPrice=resultData.transfers.searchResults.expPrice.replaceAll(",","")
		float commissionAmount=Float.parseFloat(resultData.transfers.searchResults.expPrice)
		String commissionPercentValue=getCommissionPercentageValue(commissionAmount,fpercentage)

		resultData.transfers.confirmationPage.expectedCommissionAmountText = transferData.confirm_commision_txt+" "+commissionPercentValue+" "+clientData.currency
		println("Commisstion Text Value: $resultData.transfers.confirmationPage.expectedCommissionAmountText")
		//commission text
		String commissionTxt
		try{
			commissionTxt=getCommisionText()
		}catch(Exception e)
		{
			commissionTxt="Unable to Read From UI"
		}

		resultData.transfers.confirmationPage.actualCommissionAmounttext=commissionTxt

		//Click on Close button in Booking confirmed window
		coseBookItenary()
		sleep(7000)

		//at ItenaryPage
		at ItineraryTravllerDetailsPage

		//Booking ID
		//println("Before booking ID validation")
		resultData.transfers.bookingResult.actualBookingID=getBookingIdFromBookedItems()

		//println("After booking ID validation")
		try{
			// Status
			resultData.transfers.bookingResult.actualBookingStatus=getBookingStatus(0)

		}catch(Exception e){
			// Status
			resultData.transfers.bookingResult.actualBookingStatus="Confirmed Status Is Not Dispalyed"

		}

		//Validate Transfer Date And Time
		resultData.transfers.bookingResult.actualTransferDateAndtimeInBookedItms=getTransferDateAndTimeInNonBookedItems()
		println("Booked Items scrn Transfer Date And Time: $resultData.transfers.bookingResult.actualTransferDateAndtimeInBookedItms")


		//at ItenaryPageItems

		if(subPath=="AirportToHotel.")
		{

			//pickup
			resultData.transfers.bookingResult.expectedpickupATH = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			println "$resultData.transfers.bookingResult.expectedpickupATH"
			resultData.transfers.bookingResult.actualPickUpATH=getPickUpAndDropOffDetails_BookedItems(0)

			//dropoff
			resultData.transfers.bookingResult.actualDropOffATH=getPickUpAndDropOffDetails_BookedItems(1)



		}
		else if (subPath=="HotelToAirport.")
		{

			//pick up details
			resultData.transfers.bookingResult.actualPickUpDetailsHTA=getPickUpAndDropOffDetails_BookedItems(0)

			//drop off details
			resultData.transfers.bookingResult.expecteddropOffDetailsHTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			println "$resultData.transfers.bookingResult.expecteddropOffDetailsHTA"
			resultData.transfers.bookingResult.actualdropOffDetailsHTA=getPickUpAndDropOffDetails_BookedItems(1)

		}

		else if(subPath=="HotelToStation.")
		{
			//pickup
			resultData.transfers.bookingResult.actualPickupHTS=getPickUpAndDropOffDetails_BookedItems(0)

			//dropoff
			//resultData.transfers.bookingResult.expecteddropOffHTS = "Drop off: Train Name: $transferData.transfers_trainName. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.bookingResult.expecteddropOffHTS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $data.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualdropOffHTS=getPickUpAndDropOffDetails_BookedItems(1)
		}

		else if(subPath=="HotelToPort.")
		{
			//pickup
			resultData.transfers.bookingResult.actualPickupHTP=getPickUpAndDropOffDetails_BookedItems(0)

			//dropoff
			resultData.transfers.bookingResult.expecteddropOffHTP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.bookingResult.actualdropOffHTP=getPickUpAndDropOffDetails_BookedItems(1)
		}
		else if(subPath=="PortToHotel.")
		{
			//pickup
			resultData.transfers.bookingResult.actualPickupPTH=getPickUpAndDropOffDetails_BookedItems(0)
			resultData.transfers.bookingResult.expectedPickupPTH = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			// dropoff
			resultData.transfers.bookingResult.actualdropOffPTH=getPickUpAndDropOffDetails_BookedItems(1)

		}

		else if(subPath=="HotelToOther.")
		{
			//pickup
			resultData.transfers.bookingResult.actualPickupHTO=getPickUpAndDropOffDetails_BookedItems(0)

			//validate dropoff
			//resultData.transfers.bookingResult.expecteddropOffHTO = "Drop off: Address: $data.transfers_Address, $data.transfers_Pincode. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.bookingResult.expecteddropOffHTO = "Drop off: Address $data.transfers_Address, $data.transfers_Pincode."
			resultData.transfers.bookingResult.actualdropOffHTO=getPickUpAndDropOffDetails_BookedItems(1)

		}

		else if(subPath=="OtherToHotel.")
		{
			//pickup
			resultData.transfers.bookingResult.actualPickupOTH=getPickUpAndDropOffDetails_BookedItems(0)
			//resultData.transfers.bookingResult.expectedPickupOTH = "Pick up: Address: $data.transfers_Address, $data.transfers_Pincode. $data.transfers_pickupTime_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.bookingResult.expectedPickupOTH = "Pick up: Address $data.transfers_Address, $data.transfers_Pincode"
			//validate dropoff
			resultData.transfers.bookingResult.actualdropOffOTH=getPickUpAndDropOffDetails_BookedItems(1)

		}

		else if(subPath=="AirportToAirport.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupATA = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.bookingResult.actualPickupATA=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffATA = "Drop off: Flight Number: $transferData.transfers_dropFlightNo. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffATA=getPickUpAndDropOffDetails_BookedItems(1)
		}

		else if(subPath=="AirportToStation.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupATS = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.bookingResult.actualPickupATS=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffATS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $data.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffATS=getPickUpAndDropOffDetails_BookedItems(1)
		}

		else if(subPath=="StationToAirport.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupSTA = "Pick up: "+"Station: $transferData.pickup"+" Train Name:"+" "+data.transfers_trainName+"  "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.bookingResult.actualPickupSTA=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffSTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffSTA=getPickUpAndDropOffDetails_BookedItems(1)
		}

		else if(subPath=="AirportToPort.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupATP = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.bookingResult.actualPickupATP=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffATP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffATP=getPickUpAndDropOffDetails_BookedItems(1)
		}

		else if(subPath=="PortToAirport.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupPTA = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.bookingResult.actualPickupPTA=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffPTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffPTA=getPickUpAndDropOffDetails_BookedItems(1)
		}

		else if(subPath=="AirportToOther.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupATO = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.bookingResult.actualPickupATO=getPickUpAndDropOffDetails_BookedItems(0)

			//validate dropoff
			resultData.transfers.bookingResult.expecteddropOffATO = "Drop off: Address $data.transfers_Address, $data.transfers_Pincode."
			resultData.transfers.bookingResult.actualdropOffATO=getPickUpAndDropOffDetails_BookedItems(1)
		}
		else if(subPath=="OtherToAirport.")
		{
			//pickup
			//resultData.transfers.bookingResult.expectedPickupOTA = "Pick up: Address: $data.transfers_Address, $data.transfers_Pincode. $data.transfers_pickupTime_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.bookingResult.expectedPickupOTA = "Pick up: Address $data.transfers_Address, $data.transfers_Pincode"
			resultData.transfers.bookingResult.actualPickupOTA=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffOTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffOTA=getPickUpAndDropOffDetails_BookedItems(1)
		}

		else if(subPath=="StationToPort.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupSTP = "Pick up: "+"Station: $transferData.pickup"+" Train Name:"+" "+data.transfers_trainName+"  "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins

			resultData.transfers.bookingResult.actualPickupSTP=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffSTP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffSTP=getPickUpAndDropOffDetails_BookedItems(1)
		}
		else if(subPath=="PortToStation.")
		{
			//pickup
			resultData.transfers.bookingResult.expectedPickupPTS = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.bookingResult.actualPickupPTS=getPickUpAndDropOffDetails_BookedItems(0)

			// dropoff
			resultData.transfers.bookingResult.expectedDropOffPTS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $transferData.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.bookingResult.actualDropOffPTS=getPickUpAndDropOffDetails_BookedItems(1)
		}

		//at ItenaryPage

		//Click Share Itineary button
		//clickShareItineraryButtonAgentPos()
		selectOptionFromManageItinerary(transferData.manageItineraryValue)

		sleep(7000)

		at ShareItineraryTransfersPage

		resetToggleBookedSection()

		//itinerary id
		resultData.transfers.shareItineraryPage.actualItenaryID=getItinearyID(0)

		//email field is auto populated
		//Removed from UI for 10.3 hence commenting
		//resultData.transfers.shareItineraryPage.actualEmail=getEmail()

		//INPUT email
		/*/
        entering email is commented since the email text box is removed in R10.1
         */
		//enterEmail(resultData.transfers.itineraryPage.emailAddr)

		//validate Booked Items

		//Departure Date
		resultData.transfers.shareItineraryPage.actualBookingDepDate=getBookingDate()
		resultData.transfers.shareItineraryPage.expectedBookingDepDate=depDate

		sleep(7000)
		//Validate Booking ID
		//resultData.transfers.shareItineraryPage.acutalbookingId="Booking ID: "+getBookingID()
		resultData.transfers.shareItineraryPage.acutalbookingId=getBookingID()
		sleep(1000)

		// Commission
		resultData.transfers.shareItineraryPage.expectedcomission_txt=transferData.confirm_commision_txt+" "+transferData.transfers_percentage+"%"
		println("commissioned Text Displayed")
		println("$resultData.transfers.shareItineraryPage.expectedcomission_txt")
		String commission
		try{
			commission=getCommission()
		}
		catch(Exception e)
		{
			commission="Unable to read from UI"
		}
		resultData.transfers.shareItineraryPage.actualCommission_txt=commission
		// Status
		try{
			resultData.transfers.shareItineraryPage.actualStatus=getStatusInShareItineraryPage()
		}catch(Exception e){
			resultData.transfers.shareItineraryPage.actualStatus="Unable to read from UI"
		}
		at ItenaryPageItems
		//Validate Transfer Date And Time
		resultData.transfers.shareItineraryPage.actualTransferDateAndtimeInBookedItms=getTransferDateAndTimeInNonBookedItems()
		println("Share Itinerary Screen Transfer Date And Time: $resultData.transfers.shareItineraryPage.actualTransferDateAndtimeInBookedItms")

		at ShareItineraryTransfersPage

		//Validate Traveller Details
		if((subPath=="AirportToHotel.") && (transferData.pax.toInteger()>2)) {
			resultData.transfers.shareItineraryPage.expectedtravellerDetails="Travellers: "+transferData.fNameFirst+" "+transferData.LNameFirst+", "+transferData.fNameSecond+" "+transferData.LNameSecond+", "+transferData.fNameThird+" "+transferData.LNameThird
		}else{
			resultData.transfers.shareItineraryPage.expectedtravellerDetails="Travellers: "+transferData.fNameFirst+" "+transferData.LNameFirst+", "+transferData.fNameSecond+" "+transferData.LNameSecond
		}

		resultData.transfers.shareItineraryPage.actualtravellerDetails=getTravellerDetails()


		if(subPath=="AirportToHotel.")
		{

			//validate pickup
			resultData.transfers.shareItineraryPage.expectedpickupATH = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			println "$resultData.transfers.shareItineraryPage.pickupATH"
			resultData.transfers.shareItineraryPage.actualPickUpATH=getPickUpDetailsOnShareItinerary()

			//Validate Drop off details
			resultData.transfers.shareItineraryPage.actualDropOffATH=getDropOffDetailsOnShareItinerary()

		}
		else if (subPath=="HotelToAirport.")
		{
			// Pick Up details
			resultData.transfers.shareItineraryPage.actualPickUPHTA=getPickUpDetailsOnShareItinerary()

			//Drop off details
			resultData.transfers.shareItineraryPage.expecteddropOffHTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			println "$resultData.transfers.shareItineraryPage.expecteddropOffHTA"
			resultData.transfers.shareItineraryPage.actualDropOffHTA=getDropOffDetailsOnShareItinerary()


		}

		else if(subPath=="HotelToStation.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actualPickupHTS=getPickUpDetailsOnShareItinerary()


			// dropoff
			//resultData.transfers.shareItineraryPage.expecteddropOffHTS = "Drop off: Train Name: $transferData.transfers_trainName. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.shareItineraryPage.expecteddropOffHTS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $data.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualdropOffHTS=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="HotelToPort.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actualPickupHTP=getPickUpDetailsOnShareItinerary()


			// dropoff
			resultData.transfers.shareItineraryPage.expecteddropOffHTP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.shareItineraryPage.actualdropOffHTP=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="PortToHotel.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actualPickupPTH=getPickUpDetailsOnShareItinerary()
			resultData.transfers.shareItineraryPage.expectedPickupPTH = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			// dropoff
			resultData.transfers.shareItineraryPage.actualdropOffPTH=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="HotelToOther.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actualPickupHTO=getPickUpDetailsOnShareItinerary()

			//validate dropoff
			//resultData.transfers.shareItineraryPage.expecteddropOffHTO = "Drop off: Address: $data.transfers_Address, $data.transfers_Pincode. Estimated time of departure: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.shareItineraryPage.expecteddropOffHTO = "Drop off: Address $data.transfers_Address, $data.transfers_Pincode."
			resultData.transfers.shareItineraryPage.actualdropOffHTO=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="OtherToHotel.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actualPickupOTH=getPickUpDetailsOnShareItinerary()
			//resultData.transfers.shareItineraryPage.expectedPickupOTH = "Pick up: Address: $data.transfers_Address, $data.transfers_Pincode. $data.transfers_pickupTime_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.shareItineraryPage.expectedPickupOTH = "Pick up: Address $data.transfers_Address, $data.transfers_Pincode"
			//validate dropoff
			resultData.transfers.shareItineraryPage.actualdropOffOTH=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="AirportToAirport.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupATA = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.shareItineraryPage.actualPickupATA=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffATA = "Drop off: Flight Number: $transferData.transfers_dropFlightNo. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffATA=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="AirportToStation.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupATS = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.shareItineraryPage.actualPickupATS=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffATS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $data.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffATS=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="StationToAirport.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupSTA = "Pick up: "+"Station: $transferData.pickup"+" Train Name:"+" "+data.transfers_trainName+"  "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins

			resultData.transfers.shareItineraryPage.actualPickupSTA=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffSTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffSTA=getDropOffDetailsOnShareItinerary()
		}
		else if(subPath=="AirportToPort.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupATP = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.shareItineraryPage.actualPickupATP=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffATP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffATP=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="PortToAirport.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupPTA = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.shareItineraryPage.actualPickupPTA=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffPTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffPTA=getDropOffDetailsOnShareItinerary()
		}
		else if(subPath=="AirportToOther.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupATO = "Pick up: "+"Flight Number:"+" "+transferData.flightNumber+". "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins
			resultData.transfers.shareItineraryPage.actualPickupATO=getPickUpDetailsOnShareItinerary()

			//validate dropoff
			resultData.transfers.shareItineraryPage.expecteddropOffATO = "Drop off: Address $data.transfers_Address, $data.transfers_Pincode."
			resultData.transfers.shareItineraryPage.actualdropOffATO=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="OtherToAirport.")
		{
			//pickup
			//resultData.transfers.shareItineraryPage.expectedPickupOTA = "Pick up: Address: $data.transfers_Address, $data.transfers_Pincode. $data.transfers_pickupTime_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.shareItineraryPage.expectedPickupOTA = "Pick up: Address $data.transfers_Address, $data.transfers_Pincode"
			resultData.transfers.shareItineraryPage.actualPickupOTA=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffOTA = "Drop off: Flight Number: $transferData.flightNumber. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffOTA=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="StationToPort.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupSTP = "Pick up: "+"Station: $transferData.pickup"+" Train Name:"+" "+data.transfers_trainName+"  "+"Estimated time of arrival:"+" "+transferData.timeOfArrival_Hrs+":"+transferData.timeOfArrival_mins

			resultData.transfers.shareItineraryPage.actualPickupSTP=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffSTP = "Drop off: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffSTP=getDropOffDetailsOnShareItinerary()
		}
		else if(subPath=="PortToStation.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.expectedPickupPTS = "Pick up: Ship Name: $data.transfers_shipName, $data.transfers_shipCompany. Disembarkment Time: $transferData.timeOfArrival_Hrs:$transferData.timeOfArrival_mins"
			resultData.transfers.shareItineraryPage.actualPickupPTS=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.expectedDropOffPTS = "Drop off: "+"Station: $transferData.dropoff"+" Train Name: $transferData.transfers_trainName  Estimated time of departure: $transferData.timeOfDepart_Hrs:$transferData.timeOfDepart_mins"
			resultData.transfers.shareItineraryPage.actualDropOffPTS=getDropOffDetailsOnShareItinerary()
		}

		//Validate Total and Currency
		//validate currency
		resultData.transfers.shareItineraryPage.expectedtotalcurrencyText="Total"+"  "+clientData.currency
		try{
			resultData.transfers.shareItineraryPage.actualtotalcurrencyText=getCurrencyAndTotalText()
		}catch(Exception e){
			resultData.transfers.shareItineraryPage.actualtotalcurrencyText="Unable To Read Total And Currency From UI"
		}


		//validate total amount
		resultData.transfers.shareItineraryPage.actualTotalAmount=getAmount().replaceAll(",","")
		scrollToTopOfThePage()

		//Click on Save & Preview
		//clickOnSaveAndPreviewButton()
		clickOnSavePreviewButton()

		//wait till send link loads
		//waitTillSendLinkLoads()
		//click on send link
		//clickOnSendLink()
		sleep(5000)
		//commented getting email from Send email screen bcz screen is updated and it does not have the field in R10.1
		//in the send email screen verify email to address only
		//resultData.transfers.shareItineraryPage.actualEmailInToAddr=getEmail(1)

		//click on send and wait for the page to close preview button
		//clickOnSendButton()
		//sleep(7000)

		//close share link sent popup
		//coseBookItenary()

		//validation after closing the popup
		dataStoreAfterClosePopup(transferData)




		where:
		transferData << loadData(dataNames, subPath)

	}


	def "TestCase21"(TransfersData data){

		TransfersData transferData=data
		scrollToNonBookedItem(0)
		//time
		resultData.transfers.itineraryPage.actualFirstItemDurationIconDisplayStatus=verifyItineryItemIconsDisplayed("time",0)
		resultData.transfers.itineraryPage.actualTransferTimeSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(0)
		//place cursor on top of icon, tooltip show:		Duration"
		clickTransfrItmIconsDispInNonBkdItem("time",0)
		resultData.transfers.itineraryPage.actualDurationIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("time",0)

		//max luggage
		resultData.transfers.itineraryPage.actualFirstItemLuggageIconDisplayStatus=verifyItineryItemIconsDisplayed("luggage",0)
		resultData.transfers.itineraryPage.actualTransferMaxLuggageSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(1)
		//place cursor on top of icon, tooltip show:		Luggage allowance"
		clickTransfrItmIconsDispInNonBkdItem("luggage",0)
		resultData.transfers.itineraryPage.actualLuggageIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("luggage",0)
		//max number of persons allowed
		resultData.transfers.itineraryPage.actualFirstItemPaxIconDisplayStatus=verifyItineryItemIconsDisplayed("pax",0)
		resultData.transfers.itineraryPage.actualTransfermaxPersonsSuggestedItem=getItenarySuggestedItemTimeMaxLuggPersons(2)
		//place cursor on top of icon, tooltip show:		Maximum passenger allowed"
		clickTransfrItmIconsDispInNonBkdItem("pax",0)
		resultData.transfers.itineraryPage.actualPaxIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("pax",0)



		if(transferData.transfers_passenger_assitance_icon.toBoolean()){
			try{
				//Passenger Icon Existence
				resultData.transfers.itineraryPage.actualTransferIconExistenceStatusSuggestedItem=getPassengerAssitanceIconInIternarySuggestedItems()
			}catch(Exception e){
				//Passenger Icon Existence
				resultData.transfers.itineraryPage.actualTransferIconExistenceStatusSuggestedItem="Unable to Read From UI"

			}
			try{
				//place cursor on top of icon, tooltip show:		Language Supported"
				clickTransfrItmIconsDispInNonBkdItem("language",0)
				resultData.transfers.itineraryPage.actualLanguageIconMouseHoverText=getsTransfrItmMouseHoverTextInNonBkdItemsScrn("language",0)

			}catch(Exception e){
				resultData.transfers.itineraryPage.actualLanguageIconMouseHoverText="Unable to Read From UI"

			}
		}

		//status
		/*status is not displayed twice. It is displayed only once so commenting below code*/
		//resultData.transfers.itineraryPage.actualTransferStatusSuggestedItem=getTransferStatusDisplayedSuggestedItem(0)
		resultData.transfers.itineraryPage.actualTransferStatusInNonBookedItem=getStatusTextInNonBookedItemScrn(1)


		//Remove funciton icon
		resultData.transfers.itineraryPage.actualTransferItemRemoveIconDispStatus=getRemoveButtonDisplayStatus(1)
		//price
		resultData.transfers.itineraryPage.actualTransferPriceSuggestedItem=getSuggestedItemsItenaryPrice(0)

		//Cancellation link
		resultData.transfers.itineraryPage.actualTransferCancellationLinkSuggestedItem=getCancellationLinkSuggestedItem(0)

		//Transfer Date and time in Non-Booked Items screen
		resultData.transfers.itineraryPage.actualTransferDateAndtimeInNonBookedItms=getTransferDateAndTimeInNonBookedItems()


	}


	def "TestCase20"(TransfersData data){

		TransfersData transferData=data
		//cancellation policy header
		resultData.transfers.itineraryPage.actualTransferCancellationHeaderTextSuggestedItme=getCancellationHeader()

		//Header 1. Eg: Transfer Conditions
		resultData.transfers.itineraryPage.actualTransferCancellationOverlayHeaderText_1=getCancellationItemOverlayHeaders(0)
		//Header 2. Eg: Special conditions from <Date>
		resultData.transfers.itineraryPage.actualTransferCancellationOverlayHeaderText_2=getCancellationItemOverlayHeaders(1)

		//Header 3. Eg: Cancellation Charge
		resultData.transfers.itineraryPage.actualTransferCancellationOverlayHeaderText_3=getCancellationAndAmendmentItemOverlayHeaders(0)

		//Transfer Conditions Desc text
		resultData.transfers.itineraryPage.actualTransferCancellationOverlayDescText_1=getCancelPopupInsideTextInSuggestedItems(0)

		//Special Conditions Desc Text
		resultData.transfers.itineraryPage.actualTransferCancellationOverlayDescText_2=getCancelPopupInsideTextInSuggestedItems(1)

		//All Dates Desc text
		//resultData.transfers.itineraryPage.actualTransferCancellationOverlayDescText_3=getCancelPopupInsideTextInSuggestedItems(2)
		resultData.transfers.itineraryPage.actualTransferCancellationOverlayDescText_3=getCancelPopupInsideTextInSuggestedItems(3)


	}

	def "dataStoreAfterClosePopup"(TransfersData data){
		//validate Booked Items
		TransfersData transferData=data
		//Validate Booking ID
		//resultData.transfers.shareItineraryPage.actbookingId="Booking ID: "+getBookingID()
		resultData.transfers.shareItineraryPage.actbookingId=getBookingID()
		sleep(3000)

		// Commission
		String commission
		try{
			commission=getCommission()
		}
		catch(Exception e)
		{
			commission="Unable to read from UI"
		}
		resultData.transfers.shareItineraryPage.actCommission_txt=commission
		// Status
		try{
			resultData.transfers.shareItineraryPage.actStatus=getStatusInShareItineraryPage()
		}catch(Exception e){
			resultData.transfers.shareItineraryPage.actStatus="Unable to read from UI"
		}



		//Validate Traveller Details


		resultData.transfers.shareItineraryPage.acttravellerDetails=getTravellerDetails()


		if(subPath=="AirportToHotel.")
		{

			resultData.transfers.shareItineraryPage.actPickUpATH=getPickUpDetailsOnShareItinerary()

			//Validate Drop off details
			resultData.transfers.shareItineraryPage.actDropOffATH=getDropOffDetailsOnShareItinerary()

		}
		else if (subPath=="HotelToAirport.")
		{
			// Pick Up details
			resultData.transfers.shareItineraryPage.actPickUPHTA=getPickUpDetailsOnShareItinerary()

			//Drop off details
			resultData.transfers.shareItineraryPage.actDropOffHTA=getDropOffDetailsOnShareItinerary()


		}

		else if(subPath=="HotelToStation.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actPickupHTS=getPickUpDetailsOnShareItinerary()


			// dropoff
			resultData.transfers.shareItineraryPage.actdropOffHTS=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="HotelToPort.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actPickupHTP=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.actdropOffHTP=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="PortToHotel.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actPickupPTH=getPickUpDetailsOnShareItinerary()

			// dropoff
			resultData.transfers.shareItineraryPage.actdropOffPTH=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="HotelToOther.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actPickupHTO=getPickUpDetailsOnShareItinerary()

			//validate dropoff

			resultData.transfers.shareItineraryPage.actdropOffHTO=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="OtherToHotel.")
		{
			//pickup
			resultData.transfers.shareItineraryPage.actPickupOTH=getPickUpDetailsOnShareItinerary()

			//validate dropoff
			resultData.transfers.shareItineraryPage.actdropOffOTH=getDropOffDetailsOnShareItinerary()

		}

		else if(subPath=="AirportToAirport.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupATA=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffATA=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="AirportToStation.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupATS=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffATS=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="StationToAirport.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupSTA=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffSTA=getDropOffDetailsOnShareItinerary()
		}
		else if(subPath=="AirportToPort.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupATP=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffATP=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="PortToAirport.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupPTA=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffPTA=getDropOffDetailsOnShareItinerary()
		}
		else if(subPath=="AirportToOther.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupATO=getPickUpDetailsOnShareItinerary()

			//validate dropoff

			resultData.transfers.shareItineraryPage.actdropOffATO=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="OtherToAirport.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupOTA=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffOTA=getDropOffDetailsOnShareItinerary()
		}

		else if(subPath=="StationToPort.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupSTP=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffSTP=getDropOffDetailsOnShareItinerary()
		}
		else if(subPath=="PortToStation.")
		{
			//pickup

			resultData.transfers.shareItineraryPage.actPickupPTS=getPickUpDetailsOnShareItinerary()

			// dropoff

			resultData.transfers.shareItineraryPage.actDropOffPTS=getDropOffDetailsOnShareItinerary()
		}

		//Validate Total and Currency
		//validate currency
		resultData.transfers.shareItineraryPage.expectedtotalcurrencyText="Total"+" "+clientData.currency
		try{
			resultData.transfers.shareItineraryPage.acttotalcurrencyText=getCurrencyAndTotalText()
		}catch(Exception e)
		{
			resultData.transfers.shareItineraryPage.acttotalcurrencyText="Unable To Read From UI"
		}


		//validate total amount
		resultData.transfers.shareItineraryPage.actTotalAmount=getAmount().replaceAll(",","")

		/* commented as per change in the implementation
        //wait till send link loads
        waitTillSendLinkLoads()
        //click on close preview button
        clickOnClosePreviewButton()
        //click on left arrow prefixed to the itinerary id
        sleep(5000)
        */
	}

	@Unroll
	def "02 : Validate Search Results Pagination And Sorting #transferData.caseName #transferData.desc"(){

		TransferTestResultData resultData = resultMap.get(transferData.caseName)

		if(transferData.validateAllSearchResults.toBoolean()){

			//Transfer Description Text
			List actualRetrievedData=resultData.transfers.searchResults.results.get("actualTransferDescValues")
			List expectedRetrievedData=resultData.transfers.searchResults.results.get("expectedTransferDescValues")
			//Validate  All Rows in Search Results
			assertionListEquals(actualRetrievedData,expectedRetrievedData,"Transfer Search Results, Transfers Description actual & expected don't match")

			//Show Car Types Icon Disp Status store in map
			List actualShowCarTypesIconData=resultData.transfers.searchResults.results.get("actualShowCarTypesIconDispStatus")
			List expectedShowCarTypesIconData=resultData.transfers.searchResults.results.get("expectedShowCarTypesIconDispStatus")
			assertionListEquals(actualShowCarTypesIconData,expectedShowCarTypesIconData,"Transfer Search Results, Transfers Show Car Types Down Arrow Display status actual & expected don't match")

			//Show Car Types Text store in Map
			List actualShowCarTypesText=resultData.transfers.searchResults.results.get("actualShowCarTypesTxt")
			List expectedShowCarTypesText=resultData.transfers.searchResults.results.get("expectedShowCarTypesTxt")
			assertionListEquals(actualShowCarTypesText,expectedShowCarTypesText,"Transfer Search Results, Transfers Show Car Types Text actual & expected don't match")

			//Show Car Types Collapsed Status
			List actualShowCarTypesCollapsedStatus=resultData.transfers.searchResults.results.get("actualShowCarTypesCollapseStatus")
			List expectedShowCarTypesCollapsedStatus=resultData.transfers.searchResults.results.get("expectedShowCarTypesCollapseStatus")
			assertionListEquals(actualShowCarTypesCollapsedStatus,expectedShowCarTypesCollapsedStatus,"Transfer Search Results, Transfers Show Car Types Collapsed Status actual & expected don't match")

			//From Price Text
			List actualFromPriceTxt=resultData.transfers.searchResults.results.get("actualFromPriceText")
			List expectedFromPriceTxt=resultData.transfers.searchResults.results.get("expectedFromPriceText")
			assertionListEquals(actualFromPriceTxt,expectedFromPriceTxt,"Transfer Search Results, Transfers From Price Text actual & expected don't match")

			//Expanded status
			List actualExpandedStatus=resultData.transfers.searchResults.results.get("actualShowCarTypesExpandedStatus")
			List expectedExpandedStatus=resultData.transfers.searchResults.results.get("expectedShowCarTypesExpancdedStatus")
			assertionListEquals(actualExpandedStatus,expectedExpandedStatus,"Transfer Search Results, Transfers Search Results Expanded Status actual & expected don't match")


			//From Price And Currency Text
			List actualPriceAndCurrencyTxt=resultData.transfers.searchResults.results.get("actualFromPriceAndCurrencyTxt")
			List expectedPriceAndCurrencyTxt=resultData.transfers.searchResults.results.get("expectedFromPriceAndCurrencyTxt")
			assertionListEquals(actualPriceAndCurrencyTxt,expectedPriceAndCurrencyTxt,"Transfer Search Results, Price And Currency text actual & expected don't match")

			//Show Car Types Collapsed Status
			List actCollapseStatus=resultData.transfers.searchResults.results.get("actShowCarTypesCollapseStatus")
			List expCollapseStatus=resultData.transfers.searchResults.results.get("expShowCarTypesCollapseStatus")
			assertionListEquals(actCollapseStatus,expCollapseStatus,"Transfer Search Results, After Expand and Collapse, Collapse Status actual & expected don't match")


		}

		if(transferData.validatePagination.toBoolean())
		{

			when: "Expected and actual count are available"

			int expectedtotalSearchResultsCount=transferData.totalSearchResults
			int actualTotalsearchResultCount=resultData.transfers.searchResults.actualtotalSearchResultsCount
			then: "Assert the count"
			//Validate total count of the search results
			assertionEquals(actualTotalsearchResultCount,expectedtotalSearchResultsCount,"Search Results Screen Expected & Actual Total Search Results Counts don't match")
			//To Validate first page results
			int expectedFirstPageResultsCount=transferData.resultsPerPage
			int actualFirstpageResultsCount=resultData.transfers.searchResults.totalFirstPageSearchResults
			println "Actual First Page Results Count " + actualFirstpageResultsCount + " " + "Expected First Page Results Count " + expectedFirstPageResultsCount
			assertionEquals(actualFirstpageResultsCount,expectedFirstPageResultsCount,"Search Results Screen Expected & Actual First Page Search Results Counts don't match")


			if( resultData.transfers.searchResults.actualtotalSearchResultsCount>transferData.resultsPerPage)
			{
				println("entered pagination validation new")
				for(int i=2;i<=resultData.transfers.pagination.sizeofArr;i++)
				{
					//To validate the results from second till last but one pages
					int actualresultsPerPage=resultData.transfers.pagination.get("page"+i)
					if(i< resultData.transfers.pagination.sizeofArr)
					{
						println "Actual $i Page Results Count " +  actualresultsPerPage + " " + "Expected $i Page Results Count " + transferData.resultsPerPage
						assertionEquals(actualresultsPerPage,transferData.resultsPerPage,"Search Results Screen Expected & Actual $i Page Search Results Counts don't match")
					}

					//To validate last page results
					else
					{
						println "Actual $i (Last)Page Results Count " + actualresultsPerPage  + " " + "Expected $i (Last)Page Results Count " +resultData.transfers.pagination.lastPageResults
						assertionEquals(actualresultsPerPage,resultData.transfers.pagination.lastPageResults,"Search Results Screen Expected & Actual $i (Last)Page Search Results Counts don't match")
						sleep(4000)
					}

				}

			}


		}
		if(transferData.validateSorting.toBoolean())
		{
			when: "Expected and actual Sorted values are available"

			then: "Assert the sorting values"
			//Validate sort by lowest price
			assertionEquals(resultData.transfers.searchResults.finalPriceListSortByLPrice, resultData.transfers.searchResults.tempPriceListSortByLPrice,"Search Results Screen Expected & Actual Sort by Lowest Price don't match")
			//Validate sort by Highest price
			assertionEquals(resultData.transfers.searchResults.finalPriceListSortByHPrice,resultData.transfers.searchResults.tempPriceListSortByHPrice,"Search Results Screen Expected & Actual Sort by Highest Price don't match")
			//Validate sorting by alphabetical order
			assertionEquals(resultData.transfers.searchResults.finaltransferNameListSortByAlpha,resultData.transfers.searchResults.temptransferNameListSortByAlpha,"\n Search Results Screen Expected & Actual Sort by Alphabetical Order don't match")
			//Valicate sorting by reverse of alphabetical order
			assertionEquals(resultData.transfers.searchResults.finaltransferNameListSortByRevAlpha,resultData.transfers.searchResults.temptransferNameListSortByRevAlpha,"\n Search Results Screen Expected & Actual Sort by Alphabetical Order don't match")
		}

		where:
		transferData << loadData(dataNames, subPath)

	}

	@Unroll
	def "03 : Validate Search Results  #transferData.caseName #transferData.desc"(){

		TransferTestResultData resultData = resultMap.get(transferData.caseName)

		when: "Expected and actual Search Results are available"

		then: "Assert the Search Results"

		//Validate Pick up auto Suggest
		assertionEquals(resultData.transfers.searchResults.actualPickupAutoSuggestDispStatus,transferData.dispStatus.toBoolean(),"Pick Up Auto Suggest Actual & Expected details don't match")

		//selected item displaying in pick up field
		assertionEquals(resultData.transfers.searchResults.actualPickupSelectedValInTxtBox,transferData.selectPickUp,"Pick Up Selected Value Actual & Expected details don't match")

		//Validate Drop Off Auto suggest
		assertionEquals(resultData.transfers.searchResults.actualdropOffAutoSuggestDispStatus,transferData.dispStatus.toBoolean(),"Drop Off Auto Suggest Actual & Expected details don't match")

		//selected item displaying in drop off field
		assertionEquals(resultData.transfers.searchResults.actualDropOffSelectedValInTxtBox	,transferData.selectDropOff,"Drop Off Selected Value Actual & Expected details don't match")

		//tomorrow's date should display within field
		assertionEquals(resultData.transfers.searchResults.actualdefaultDate,resultData.transfers.searchResults.expecteddefaultDate,"Date Default Value Actual & Expected details don't match")

		//Date field populated the selected date with correct format (e.g. ddMMMyy)
		assertionEquals(resultData.transfers.searchResults.actualmodifiedDate,resultData.transfers.searchResults.expectedmodifiedDate,"Date Modified Value Actual & Expected details don't match")

		//12:00 should display within field
		assertionEquals(resultData.transfers.searchResults.actualDefaultTime,transferData.defaultTime,"Time Default Value Actual & Expected details don't match")

		//2 pax should display within field
		assertionEquals(resultData.transfers.searchResults.actualPaxDefaultValue,transferData.defaultroomPax,"Room Pax Default Value Actual & Expected details don't match")

		if(resultData.transfers.searchResults.infoBoxBeforeCloseDispStatus==true){
			//verify info box message is displayed
			assertionEquals(resultData.transfers.searchResults.infoBoxBeforeCloseDispStatus,transferData.dispStatus.toBoolean(),"Transfer Search Results, Info Text Display Before click on Close Actual & Expected details don't match")

			//verify info box message text
			assertionEquals(resultData.transfers.searchResults.infoTxt,transferData.infoTxt,"Transfer Search Results, Info Text Actual & Expected details don't match")

			//verify info box message is disappeared.
			assertionEquals(resultData.transfers.searchResults.infoBoxAfterCloseDispStatus,transferData.notDispStatus.toBoolean(),"Transfer Search Results, Info Text Display After Click on Close Actual & Expected details don't match")

		}


		//Validate transfer name text
		assertionEquals(resultData.transfers.searchResults.actualtransferVehicleName, transferData.expected_car_txt,"Search Results Screen Actual & Expected Transfer Name details don't match")
		//Validate transfer description text
		assertionEquals(resultData.transfers.searchResults.actualTransferVehicleDesc,transferData.expected_carDesc_txt, "\n Search Results Screen Transfer Desc Text displayed Actual & Exected don't match")
		//item image
		assertionEquals(resultData.transfers.searchResults.actualtransferimage,transferData.dispStatus.toBoolean(),"Item Image Display Status Actual & Expected details don't match")

		//service duration icon
		assertionEquals(resultData.transfers.searchResults.actualDurationIconDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Duration Icon Display Status Actual & Expected don't match")
		//place cursor on top of icon, tooltip show:		Duration"
		assertionEquals(resultData.transfers.searchResults.actualDurationIconMouseHoverText,transferData.mousehoverDurationTxt, "\n Search Results Screen, Duration mouse hover displayed Actual & Expected don't match")
		//service duration time & value
		assertionEquals(resultData.transfers.searchResults.actualDurationTimeAndValueText,transferData.expected_carTime, "\n Search Results Screen, Duration Time text displayed Actual & Expected don't match")

		//"maximum luggage icon
		assertionEquals(resultData.transfers.searchResults.actualLuggageIconDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Luggage Icon Display Status Actual & Expected don't match")
		//place cursor on top of icon, tooltip show:		Luggage allowance"
		assertionEquals(resultData.transfers.searchResults.actualLuggageIconMouseHoverText,transferData.mousehoverLuggageTxt, "\n Search Results Screen, Luggage mouse hover displayed Actual & Expected don't match")
		//Validate Max Luggage
		assertionEquals( resultData.transfers.searchResults.actualTransferLuggage,transferData.expected_maxLuggage, "\n Search Results Screen Max Luggage Displayed Actual & Expected don't match")

		//"maximum passenger allowed icon
		assertionEquals(resultData.transfers.searchResults.actualPaxIconDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Pax Icon Display Status Actual & Expected don't match")
		//place cursor on top of icon, tooltip show:		Maximum passenger allowed"
		assertionEquals(resultData.transfers.searchResults.actualPaxIconMouseHoverText,transferData.mousehoverMaxPaxTxt, "\n Search Results Screen, Pax mouse hover displayed Actual & Expected don't match")
		//Validate Max pax
		assertionEquals(resultData.transfers.searchResults.actualTransferPax,transferData.expected_maxpassengersAllwd, "\n Search Results Screen Max Pax Displayed Actual & Expected don't match")

		if(transferData.transfers_passenger_assitance_icon.toBoolean()) {
			//Language Supported icon
			assertionEquals(resultData.transfers.searchResults.actualLanguageIconDispStatus, transferData.dispStatus.toBoolean(), "\n Search Results Screen, Language Icon Display Status Actual & Expected don't match")
			//place cursor on top of icon, tooltip show:		Language Supported"
			assertionEquals(resultData.transfers.searchResults.actualLanguageIconMouseHoverText, transferData.mousehoverlanguageTxt, "\n Search Results Screen, Language mouse hover displayed Actual & Expected don't match")
			//Language Supported Value
			assertionEquals(resultData.transfers.searchResults.actualLanguageValueText, transferData.languageTxt, "\n Search Results Screen Language Text Displayed Actual & Expected don't match")
		}
		//transfer condition icon & link
		assertionEquals(resultData.transfers.searchResults.actualConditionsIconDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Condtions Icon Display Status Actual & Expected don't match")
		//user takent to Special Conditions lightbox - Displayed
		assertionEquals(resultData.transfers.searchResults.actualTransferConditionsDisplayStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Transfer Conditions Popup Display Status Actual & Expected don't match")
		//Special Conditions title text
		assertionEquals(resultData.transfers.searchResults.actualTransferConditionstitleText,transferData.splConditionTxt, "\n Search Results Screen, Special Conditions Actual & Expected don't match")
		//Close X function
		assertionEquals(resultData.transfers.searchResults.actualTransferConditionsCloseXDisplayStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Transfer Conditions Popup Close X Display Status Actual & Expected don't match")
		//Transfer Conditions
		assertionEquals(resultData.transfers.searchResults.actualTransferConditionsText,transferData.trnsfrConditionTxt, "\n Search Results Screen, Transfer Conditions Text Actual & Expected don't match")
		//Meeting Point Text
		assertionEquals(resultData.transfers.searchResults.actualTransferConditionsMeetingPointText,transferData.meetingPointTxt, "\n Search Results Screen, Meeting Point Text Actual & Expected don't match")
		//confirm status text - Available or On Request
		assertionEquals(resultData.transfers.searchResults.actualTransferStatusTxt,transferData.confirm_BookingStatusConfirm_txt, "\n Search Results Screen, Transfer Status Actual & Expected don't match")

		//Validate free cancellation - hyperlink
		assertionEquals(resultData.transfers.searchResults.actualCancellationText_Date, resultData.transfers.searchResults.expectedcancellationText_Date, "\n Search Results Screen free cancellation Text Displayed Actual & Expected don't match")
		//Validate cancellation header
		assertionEquals(resultData.transfers.searchResults.actualCancellationHeaderTxt,transferData.expected_Cancel_header_txt,"Search Results Screen Cancel Popup Cancel Header Text Displayed is not correct")
		//Validate please note text
		assertionEquals(resultData.transfers.searchResults.actualSummaryText,transferData.expected_summary_txt,"Search Results Screen Cancel Popup Please Note Text displayed is not correct")
		//validate Cancellation Charge - header validation
		//assertionEquals(resultData.transfers.searchResults.actualCancellationChargeText,transferData.expected_cancel_chrg_txt,"Search Results Screen Cancel Popup Cancellation Charge Text displayed Actual & Expected don't match")
		assertionEquals(resultData.transfers.searchResults.actualCancellationChargeText,transferData.expected_cancel_chrg_txt,"Search Results Screen Cancel Popup Cancellation Charge Text displayed Actual & Expected don't match")
		//Validate All Dates text
		assertionEquals(resultData.transfers.searchResults.actualPopupFooterText,transferData.expected_popup_txt,"Search Results Screen Cancel Popup Text Displayed Popup footer text displayed Actual & Expected don't match")
		//close x function
		assertionEquals(resultData.transfers.searchResults.actualCloseXFuctionDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Cancellation Charge Popup Close X Display Status Actual & Expected don't match")

		//service item amount & currency
		assertionEquals(resultData.transfers.searchResults.actualpricecurrDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Item amount and currency Display Status Actual & Expected don't match")

		//<Add to itineray> functional button
		assertionEquals(resultData.transfers.searchResults.actualAddToItineraryButtonDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Add to Itinerary Button Display Status Actual & Expected don't match")

		/* commenting since itinerary builder validations are not there in 10.3

		//Itinerary ID and Name
		assertionEquals(resultData.transfers.searchResults.actualItineraryIDAndNameDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Itinerary Id and Name Display Status Actual & Expected don't match")

		//item image
		assertionEquals(resultData.transfers.searchResults.actualTransferItemImageDispStatus,transferData.dispStatus.toBoolean(), "\n Search Results Screen, Item Image Display Status Actual & Expected don't match")

		//Service Date, time & Value
		assertionEquals(resultData.transfers.searchResults.actualServiceDateTimeValueText,resultData.transfers.searchResults.expectedServiceDateTimeValueText, "\n Search Results Screen, Service Date Time Value Text Actual & Expected don't match")

		//Validate tile window
		//Validate Transfer Price with Currency
			assertionEquals(resultData.transfers.searchResults.actualItineraryPrice,resultData.transfers.searchResults.expected_price_curr,"Search Results Screen Tile window Transfer Price with Currency Actual & Expected don't match")
		//Validate Transfer Status
			assertionEquals(resultData.transfers.searchResults.actualItineraryStatus,resultData.transfers.searchResults.transferStatus,"Search Results Screen Tile window Transfer Status Actual & Expected don't match")
		//Validate Transfer Desc
			assertionEquals(resultData.transfers.searchResults.actualItineraryDesc,transferData.expected_carDesc_txt,"Search Results Screen Tile window Transfer Desc Actual & Expected don't match")
		//Validate Pax
			assertionEquals(resultData.transfers.searchResults.actualItineraryPax,transferData.expected_itinearyPax,"Search Results Screen Tile window Transfer Pax Actual & Expected don't match")
		//Validate Transfer Name
			assertionEquals(resultData.transfers.searchResults.actualTransferItineraryName,transferData.expected_car_txt,"Search Results Screen Tile window Transfer Itinerary Name Actual & Expected don't match")


		*/
		//Edit Itinerary Name
		assertionEquals(resultData.transfers.itineraryPage.actualSavedItnrName,resultData.transfers.itineraryPage.expectedItnrName, "\n Itinerary Screen, Edit Itinerary Value Text Actual & Expected don't match")






		where:
		transferData << loadData(dataNames, subPath)

	}

	@Unroll
	def "04 : Validate Itinerary Page  #transferData.caseName #transferData.desc"(){

		TransferTestResultData resultData = resultMap.get(transferData.caseName)

		when: "Expected and actual Itinerary Page values are available"

		then: "Assert the Itinerary Page values"

		//user should be able to do so.
		assertionEquals(resultData.transfers.itineraryPage.actualSavedAgentRefName,transferData.agentRef,"Itinerary Screen Agent Reference Name displayed Actual & Expected don't match")


		// Save button should be removed .
		assertionEquals(resultData.transfers.itineraryPage.actualSaveBtnDispStatus,transferData.notDispStatus.toBoolean(),"Itinerary Screen Agent Reference Section Save button displayed Actual & Expected don't match")

		//Edit button should be displayed
		assertionEquals(resultData.transfers.itineraryPage.actualEditBtnDispStatus,transferData.dispStatus.toBoolean(),"Itinerary Screen Agent Reference Section Edit Button displayed Actual & Expected don't match")

		//the transfer item is included in Non-Booked Items block
		assertionEquals(resultData.transfers.itineraryPage.actualTransferNameInNonBookedItms,transferData.expected_car_txt,"Itinerary Screen Non Booked Items Transfer Name displayed Actual & Expected don't match")

		//Non-Booked Items - title text
		assertionEquals(resultData.transfers.itineraryPage.actualNonBookedItemTxt,transferData.nonBookedItemsHeaderTxt,"Itinerary Screen Non Booked Items Transfer Name displayed Actual & Expected don't match")

		//Non-Booked items icon		Place cursor on top of the icon
		assertionEquals(resultData.transfers.itineraryPage.actualQstnMarkTxt,transferData.mousehoverTxt,"Itinerary Screen Non Booked Items Questoin mark mouse hover test displayed Actual & Expected don't match")

		//<Book> function button
		assertionEquals(resultData.transfers.itineraryPage.actualBookButtonDispStatus,transferData.dispStatus.toBoolean(),"Itinerary Screen, Book function Button displayed Actual & Expected don't match")

		//item image - added in test class
		//assertionEquals(resultData.transfers.itineraryPage.actualNonBookedItemImageSrcURL,resultData.transfers.searchResults.retrievetransferFirstItemimageURL,"Itinerary Screen, Item Image displayed Actual & Expected don't match")





		//Validate Suggested Items
		//Validate Transfer Name
		assertionEquals(resultData.transfers.itineraryPage.actualTransferNameInSuggestedItem,transferData.expected_car_txt,"Itinerary Screen Suggested Items Transfer Name displayed Actual & Expected don't match")
		//Validate Transfer pax
		assertionEquals(resultData.transfers.itineraryPage.actualTransferPaxSuggestedItem,transferData.expected_itinearyPax,"Itinerary Screen Suggested Items Transfer Pax displayed Actual & Expected don't match")
		//Validate Transfer Desc
		assertionEquals(resultData.transfers.itineraryPage.actualTransferDescSuggestedItem,transferData.expected_carDesc_txt,"Itinerary Screen Suggested Items Transfer Desc displayed Actual & Expected don't match")
		//Validate Transfer time
		assertionEquals(resultData.transfers.itineraryPage.actualTransferTimeSuggestedItem,transferData.expected_carTime,"Itinerary Screen Suggested Items Transfer Time displayed Actual & Expected don't match")
		assertionEquals(resultData.transfers.itineraryPage.actualFirstItemDurationIconDisplayStatus,transferData.dispStatus.toBoolean(),"Itinerary Screen Suggested Items Transfer Duration Icon Display Status Actual & Expected don't match")
//place cursor on top of icon, tooltip show:		Duration"
		assertionEquals(resultData.transfers.itineraryPage.actualDurationIconMouseHoverText,transferData.mousehoverDurationTxt, "\n Search Results Screen, Duration mouse hover displayed Actual & Expected don't match")

		//Validate Max Luggage
		assertionEquals(resultData.transfers.itineraryPage.actualTransferMaxLuggageSuggestedItem,transferData.expected_maxLuggage,"Itinerary Screen Suggested Items Transfer Max Luggage displayed Actual & Expected don't match")
		assertionEquals(resultData.transfers.itineraryPage.actualFirstItemLuggageIconDisplayStatus,transferData.dispStatus.toBoolean(),"Itinerary Screen Non Booked Items Transfer Max Luggage Icon displayed Actual & Expected don't match")
//place cursor on top of icon, tooltip show:		Luggage allowance"
		assertionEquals(resultData.transfers.itineraryPage.actualLuggageIconMouseHoverText,transferData.mousehoverLuggageTxt, "\n Search Results Screen, Luggage mouse hover displayed Actual & Expected don't match")

		//Validate Max Persons
		assertionEquals(resultData.transfers.itineraryPage.actualTransfermaxPersonsSuggestedItem,transferData.expected_maxpassengersAllwd,"Itinerary Screen Suggested Items Transfer Max Persons displayed Actual & Expected don't match")
		assertionEquals(resultData.transfers.itineraryPage.actualFirstItemPaxIconDisplayStatus,transferData.dispStatus.toBoolean(),"Itinerary Screen Non Booked Items Transfer Max Persons Icon displayed Actual & Expected don't match")
		//place cursor on top of icon, tooltip show:		Maximum passenger allowed"
		assertionEquals(resultData.transfers.itineraryPage.actualPaxIconMouseHoverText,transferData.mousehoverMaxPaxTxt, "\n Search Results	Screen, Pax mouse hover displayed Actual & Expected don't match")

		if(transferData.transfers_passenger_assitance_icon.toBoolean()){
			//Validate Language Assistance Icon status
			assertionEquals(resultData.transfers.itineraryPage.actualTransferIconExistenceStatusSuggestedItem,transferData.transfers_passenger_assitance_icon.toBoolean(),"Itinerary Screen Non Booked Items Language Assitance Icon displayed Actual & Expected don't match")
//place cursor on top of icon, tooltip show:		Language Supported"
			assertionEquals(resultData.transfers.itineraryPage.actualLanguageIconMouseHoverText,transferData.mousehoverlanguageTxt, "\n Itinerary Screen, Non Booked ItemsLanguage mouse hover displayed Actual & Expected don't match")
		}
		//Remove funciton icon
		assertionEquals(resultData.transfers.itineraryPage.actualTransferItemRemoveIconDispStatus,transferData.dispStatus.toBoolean(),"Itinerary Screen Non Booked Items Transfer Remove Icon displayed Actual & Expected don't match")

		//Validate Transfer Date And Time
		assertionEquals(resultData.transfers.itineraryPage.actualTransferDateAndtimeInNonBookedItms,resultData.transfers.searchResults.expectedServiceDateTimeValueText, "Itinerary Screen, Non Booked items Date And Time Actual & Expected don't match")

		//Validate Transfer Status
		/*Transfer Status is displayed only once*/
		//assertionEquals(resultData.transfers.itineraryPage.actualTransferStatusSuggestedItem,resultData.transfers.searchResults.transferStatus,"Itinerary Screen Suggested Items Transfer Status displayed Actual & Expected don't match")
		assertionEquals(resultData.transfers.itineraryPage.actualTransferStatusInNonBookedItem,resultData.transfers.searchResults.transferStatus,"Itinerary Screen Non Booked items Transfer Status displayed Actual & Expected don't match")


		//Validate Price
		assertionEquals(resultData.transfers.itineraryPage.actualTransferPriceSuggestedItem,resultData.transfers.searchResults.expected_price_curr,"Itinerary Screen Suggested Items Transfer Price displayed Actual & Expected don't match")
		//Validate Cancellation Link
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationLinkSuggestedItem,resultData.transfers.searchResults.expectedcancellationText_Date,"Itinerary Screen Suggested Items Transfer Cancellation Link displayed Actual & Expected don't match")

		//Validate Cancellation Header
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationHeaderTextSuggestedItme,transferData.expected_Cancel_header_txt,"Itinerary Screen Suggested Items Cancellation Popup Cancellation Header Text displayed Actual & Expected don't match")
		//Validate Header 1. Eg: Transfer Conditions
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationOverlayHeaderText_1,transferData.cancel_HeaderText_1,"Itinerary Screen Suggested Items Cancellation Popup Cancellation Overlay Header Text 'Tansfer Conditions' displayed Actual & Expected don't match")
		//Validate Header 2. Eg: Special conditions from <Date>
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationOverlayHeaderText_2,resultData.transfers.itineraryPage.expectedTransferCancellationOverlayHeaderText_2,"Itinerary Screen Suggested Items Cancellation Popup Cancellation Overlay Header Text 'Special conditions from <Date>' displayed Actual & Expected don't match")
		//Validate Header 3. Eg: Cancellation Charge
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationOverlayHeaderText_3,transferData.expected_cancel_chrg_txt,"Itinerary Screen Suggested Items Cancellation Popup Cancellation Overlay Header Text 'Cancellation Charge' displayed Actual & Expected don't match")
		//Validate Transfer Conditions Description text
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationOverlayDescText_1,transferData.cancel_trnsfrConditionsTxt,"Itinerary Screen Suggested Items Cancellation Popup Cancellation Overlay 'Transfer Conditions Desc Text' displayed Actual & Expected don't match")
		//Validate Special Conditions Description Text
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationOverlayDescText_2,transferData.expected_summary_txt,"Itinerary Screen Suggested Items Cancellation Popup Cancellation Overlay 'Special Conditions Description text' displayed Actual & Expected don't match")
		//Validate All Dates Description text
		assertionEquals(resultData.transfers.itineraryPage.actualTransferCancellationOverlayDescText_3,transferData.expected_popup_txt,"Itinerary Screen Suggested Items Cancellation Popup Cancellation Overlay 'All Dates Description text' displayed Actual & Expected don't match")

		//Validate Entered Traveller Details - for lead traveller
		assertionEquals(resultData.transfers.itineraryPage.actualLeadTravellerName,resultData.transfers.itineraryPage.expectedleadTravellerName,"Itinerary Screen Traveller Details - Lead Traveller Expected & Actual Traveller details don't match")
		//validate email
		assertionEquals(resultData.transfers.itineraryPage.actualemailId,resultData.transfers.itineraryPage.emailAddr,"Itinerary Screen Traveller Email Details Expected & Actual don't match")
		//Validate Entered Traveller Details - for another traveller
		assertionEquals(resultData.transfers.itineraryPage.actualFirstTravellerName,resultData.transfers.itineraryPage.expectedFirstTravellerName,"Itinerary Screen Traveller Details - First Traveller Expected & Actual details don't match")

		//Validate booking description text - added in test class
		//assertionEquals(resultData.transfers.itineraryPage.actualBookingDesc,resultData.transfers.itineraryPage.expectedBookingDesc,"Itinerary Screen 'You Are About to Book Screen' Booking Description Expected & Actual details don't match")
		//Validate lead passenger details
		assertionEquals(resultData.transfers.itineraryPage.actualfirstPassengerName,resultData.transfers.itineraryPage.expectedfirstPassengerName,"Itinerary Screen 'You Are About to Book Screen' Lead Passenger Expected & Actual details don't match")
		//Validate Other passenger details
		assertionEquals(resultData.transfers.itineraryPage.actualsecondPassengerName,resultData.transfers.itineraryPage.expectedsecondPassengerName,"Itinerary Screen 'You Are About to Book Screen' Other Passenger Expected & Actual details don't match")


		if(subPath=="AirportToHotel.")
		{
			//Validate Drop Off details
			assertionEquals(resultData.transfers.itineraryPage.actualDropOffDetailsATH,transferData.transfers_booking_dropOff_text,"Itinerary Screen 'You Are About to Book Screen' Drop Off Details Expected & Actual details don't match")
		}
		else if (subPath=="HotelToAirport.")
		{
			//Validate Pick Up details
			assertionEquals(resultData.transfers.itineraryPage.actualpickUpDetailsHTA,transferData.transfers_pickUp_text,"Itinerary Screen 'You Are About to Book Screen' Pick UP  Details Expected & Actual details don't match")
		}
		else if (subPath=="HotelToStation.")
		{
			//Validate Pick Up details
			assertionEquals(resultData.transfers.itineraryPage.actualpickUpDetailsHTS,transferData.transfers_pickUp_text,"Itinerary Screen 'You Are About to Book Screen' Pick UP  Details Expected & Actual details don't match")

		}
		else if(subPath=="HotelToPort.")
		{
			//Validate Pick Up details
			assertionEquals(resultData.transfers.itineraryPage.actualpickUpDetailsHTP,transferData.transfers_pickUp_text,"Itinerary Screen 'You Are About to Book Screen' Pick UP  Details Expected & Actual details don't match")

		}

		else if(subPath=="PortToHotel.")
		{
			//Validate Drop Off details
			assertionEquals(resultData.transfers.itineraryPage.actualpickUpDetailsPTH,transferData.transfers_booking_dropOff_text,"Itinerary Screen 'You Are About to Book Screen' Pick UP  Details Expected & Actual details don't match")

		}

		else if(subPath=="HotelToOther.") {

			//Validate Pick Up details
			assertionEquals(resultData.transfers.itineraryPage.actualpickUpDetailsHTO,transferData.transfers_pickUp_text,"Itinerary Screen 'You Are About to Book Screen' Pick UP  Details Expected & Actual details don't match")

		}
		else if(subPath=="OtherToHotel.") {

			//Validate Drop Off details
			assertionEquals(resultData.transfers.itineraryPage.actualpickUpDetailsOTH,transferData.transfers_booking_dropOff_text,"Itinerary Screen 'You Are About to Book Screen' Pick UP  Details Expected & Actual details don't match")

		}

		//Validate Header 1. Transfer conditions
		assertionEquals(resultData.transfers.itineraryPage.actualheaderText1,transferData.cancel_HeaderText_1,"Itinerary Screen 'You Are About to Book Screen' Header 1. Transfer conditions Expected & Actual details don't match")
		//Validate Transfer Conditions Desc Text
		assertionEquals(resultData.transfers.itineraryPage.actualTransfersConditionsText,transferData.cancel_trnsfrConditionsTxt,"Itinerary Screen 'You Are About to Book Screen' Transfer conditions Desc Text Expected & Actual details don't match")
		//Validate Header 2. Special Conditions text
		assertionEquals(resultData.transfers.itineraryPage.actualheaderText2,resultData.transfers.itineraryPage.expectedTransferCancellationOverlayHeaderText_2,"Itinerary Screen 'You Are About to Book Screen' Special conditions Header Text Expected & Actual details don't match")
		//Validate Special conditions Desc text
		assertionEquals(resultData.transfers.itineraryPage.actualSpecialConditionsText,transferData.expected_summary_txt,"Itinerary Screen 'You Are About to Book Screen' Special conditions Desc Text Expected & Actual details don't match")
		//Validate Header 3.Cancellation Charge
		assertionEquals(resultData.transfers.itineraryPage.actualheaderText3,transferData.expected_cancel_chrg_txt,"Itinerary Screen 'You Are About to Book Screen' Cancellation Charge Header Text Expected & Actual details don't match")
		//Validate All Dates Text
		assertionEquals(resultData.transfers.itineraryPage.actualDatesText,transferData.expected_popup_txt,"Itinerary Screen 'You Are About to Book Screen' All Dates Text Expected & Actual details don't match")

		//Validate Total in Confirm Booking
		assertionEquals(resultData.transfers.itineraryPage.actualTotalInconfirmBooking,resultData.transfers.searchResults.expected_price_curr,"Itinerary Screen 'You Are About to Book Screen' Total in Confirm Booking  Expected & Actual details don't match")


		where:
		transferData << loadData(dataNames, subPath)
	}

	@Unroll
	def "05 : Validate Confirmation Page  #transferData.caseName #transferData.desc"(){

		TransferTestResultData resultData = resultMap.get(transferData.caseName)

		when: "Expected and actual Confirmation Page values are available"

		then: "Assert the Confirmation Page values"

		//Validate Traveller Details - for lead traveller
		assertionEquals(resultData.transfers.confirmationPage.actualLeadTravellerDetailsConfirmationDialog,resultData.transfers.itineraryPage.expectedleadTravellerName,"Confirmation Screen Traveller Details - Lead Traveller Expected & Actual Traveller details don't match")
		//validate email
		assertionEquals(resultData.transfers.confirmationPage.actualAgentEmailId,resultData.transfers.confirmationPage.expectedAgentEmailId,"Confirmation Screen Email Id Expected & Actual don't match")
		//Validate Departure Date
		assertionEquals(resultData.transfers.confirmationPage.actualConfimrationDialogDepDate,resultData.transfers.confirmationPage.expectedConfimrationDialogDepDate,"Confirmation Screen Departure Date Expected & Actual don't match")
		//Validate Transfer Name
		assertionEquals(resultData.transfers.confirmationPage.actualTransferNameConfirmationDialog,transferData.expected_car_txt,"Confirmation Screen Transfer Name Expected & Actual don't match")
		//Validate Transfer Desc
		assertionEquals(resultData.transfers.confirmationPage.actualTransferDescBookingConf_txt,resultData.transfers.confirmationPage.expectedTransferDescBookingConf_txt,"Confirmation Screen Transfer Desc Expected & Actual don't match")

		if(subPath=="AirportToHotel.")
		{

			//Validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupATH,resultData.transfers.confirmationPage.expectedPickupATH,"Confirmation Screen Pick Up Details Actual & Expected don't match")
			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffATH,transferData.confirm_dropOff_text,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}
		else if (subPath=="HotelToAirport.")
		{

			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupHTA,transferData.booking_pickup_txt,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffHTA,resultData.transfers.confirmationPage.expectedDropOffHTA,"Confirmation Screen Drop Off Details Actual & Expected don't match")
		}
		else if(subPath=="HotelToStation.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupHTS,transferData.booking_pickup_txt,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualdropOffHTS,resultData.transfers.confirmationPage.expecteddropOffHTS,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}
		else if(subPath=="HotelToPort.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupHTP,transferData.booking_pickup_txt,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualdropOffHTP,resultData.transfers.confirmationPage.expecteddropOffHTP,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}
		else if(subPath=="PortToHotel.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupPTH,resultData.transfers.confirmationPage.expectedPickupPTH,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualdropOffPTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="HotelToOther.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupHTO,transferData.booking_pickup_txt,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualdropOffHTO,resultData.transfers.confirmationPage.expecteddropOffHTO,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="OtherToHotel.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupOTH,resultData.transfers.confirmationPage.expectedPickupOTH,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualdropOffOTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="AirportToAirport.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupATA,resultData.transfers.confirmationPage.expectedPickupATA,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffATA,resultData.transfers.confirmationPage.expectedDropOffATA,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}
		else if(subPath=="AirportToStation.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupATS,resultData.transfers.confirmationPage.expectedPickupATS,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffATS,resultData.transfers.confirmationPage.expectedDropOffATS,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="StationToAirport.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupSTA,resultData.transfers.confirmationPage.expectedPickupSTA,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffSTA,resultData.transfers.confirmationPage.expectedDropOffSTA,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="AirportToPort.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupATP,resultData.transfers.confirmationPage.expectedPickupATP,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffATP,resultData.transfers.confirmationPage.expectedDropOffATP,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="PortToAirport.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupPTA,resultData.transfers.confirmationPage.expectedPickupPTA,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffPTA,resultData.transfers.confirmationPage.expectedDropOffPTA,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="AirportToOther.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupATO,resultData.transfers.confirmationPage.expectedPickupATO,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualdropOffATO,resultData.transfers.confirmationPage.expecteddropOffATO,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}
		else if(subPath=="OtherToAirport.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupOTA,resultData.transfers.confirmationPage.expectedPickupOTA,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffOTA,resultData.transfers.confirmationPage.expectedDropOffOTA,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="StationToPort.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupSTP,resultData.transfers.confirmationPage.expectedPickupSTP,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffSTP,resultData.transfers.confirmationPage.expectedDropOffSTP,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}

		else if(subPath=="PortToStation.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.confirmationPage.actualPickupPTS,resultData.transfers.confirmationPage.expectedPickupPTS,"Confirmation Screen Pick Up Details Actual & Expected don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.confirmationPage.actualDropOffPTS,resultData.transfers.confirmationPage.expectedDropOffPTS,"Confirmation Screen Drop Off Details Actual & Expected don't match")

		}


		//Validate Commission Text
		assertionEquals(resultData.transfers.confirmationPage.actualCommissionAmounttext,resultData.transfers.confirmationPage.expectedCommissionAmountText ,"Confirmation Screen Commission Text Details Actual & Expected don't match")

		//Validate Transfer Date And Time - added in test class
		//assertionEquals(resultData.transfers.confirmationPage.actualTransferDateAndTimeInBookingConf,resultData.transfers.itineraryPage.actualTransferDateAndtimeInNonBookedItms,"Confirmation Screen, Client Requested Service Pick up Date And Time Actual & Expected don't match")

		where:
		transferData << loadData(dataNames, subPath)

	}

	@Unroll
	def "06 : Validate Booking Result  #transferData.caseName #transferData.desc"(){

		TransferTestResultData resultData = resultMap.get(transferData.caseName)

		when: "Expected and actual Booking Result Values are available"

		then: "Assert the Booing Result Page values"

		//Validate Booking ID
		assertionEquals(resultData.transfers.bookingResult.actualBookingID,resultData.transfers.confirmationPage.actualbookingID,"Traveller Details (Booking Result) Booking Id Expected & Actual details don't match")
		//Validate Status
		assertionEquals(resultData.transfers.bookingResult.actualBookingStatus,transferData.transfers_FinalStatus_text,"Traveller Details (Booking Result) Status Expected & Actual details don't match")

		//Validate Transfer Date And Time
		assertionEquals(resultData.transfers.bookingResult.actualTransferDateAndtimeInBookedItms,resultData.transfers.itineraryPage.actualTransferDateAndtimeInNonBookedItms,"Booked Items screen, Client Requested Service Pick up Date And Time Expected & Actual details don't match")

		/*
		if(subPath=="AirportToHotel.")
		{

			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickUpATH,resultData.transfers.bookingResult.expectedpickupATH,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffATH,transferData.confirm_dropOff_text,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}
		else if (subPath=="HotelToAirport.")
		{

			//Validate pick up details
			assertionEquals(resultData.transfers.bookingResult.actualPickUpDetailsHTA,transferData.booking_pickup_txt,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")

			//validate drop off details
			assertionEquals(resultData.transfers.bookingResult.actualdropOffDetailsHTA,resultData.transfers.bookingResult.expecteddropOffDetailsHTA,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")
		}

		else if(subPath=="HotelToStation.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupHTS,transferData.booking_pickup_txt,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualdropOffHTS,resultData.transfers.bookingResult.expecteddropOffHTS,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}
		else if(subPath=="HotelToPort.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupHTP,transferData.booking_pickup_txt,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualdropOffHTP,resultData.transfers.bookingResult.expecteddropOffHTP,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="PortToHotel.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupPTH,resultData.transfers.bookingResult.expectedPickupPTH,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualdropOffPTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="HotelToOther.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupHTO,transferData.booking_pickup_txt,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualdropOffHTO,resultData.transfers.bookingResult.expecteddropOffHTO,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")
		}

		else if(subPath=="OtherToHotel.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupOTH,resultData.transfers.bookingResult.expectedPickupOTH,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualdropOffOTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="AirportToAirport.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.bookingResult.actualPickupATA,resultData.transfers.bookingResult.expectedPickupATA,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			//assertionEquals(resultData.transfers.bookingResult.actualDropOffATA,resultData.transfers.bookingResult.expectedDropOffATA,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="AirportToStation.")
		{

			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupATS,resultData.transfers.bookingResult.expectedPickupATS,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffATS,resultData.transfers.bookingResult.expectedDropOffATS,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="StationToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupSTA,resultData.transfers.bookingResult.expectedPickupSTA,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffSTA,resultData.transfers.bookingResult.expectedDropOffSTA,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="AirportToPort.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupATP,resultData.transfers.bookingResult.expectedPickupATP,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffATP,resultData.transfers.bookingResult.expectedDropOffATP,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="PortToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupPTA,resultData.transfers.bookingResult.expectedPickupPTA,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffPTA,resultData.transfers.bookingResult.expectedDropOffPTA,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}
		else if(subPath=="AirportToOther.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupATO,resultData.transfers.bookingResult.expectedPickupATO,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualdropOffATO,resultData.transfers.bookingResult.expecteddropOffATO,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")
		}
		else if(subPath=="OtherToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupOTA,resultData.transfers.bookingResult.expectedPickupOTA,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffOTA,resultData.transfers.bookingResult.expectedDropOffOTA,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")
		}
		else if(subPath=="StationToPort.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupSTP,resultData.transfers.bookingResult.expectedPickupSTP,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffSTP,resultData.transfers.bookingResult.expectedDropOffSTP,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="PortToStation.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.bookingResult.actualPickupPTS,resultData.transfers.bookingResult.expectedPickupPTS,"Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.bookingResult.actualDropOffPTS,resultData.transfers.bookingResult.expectedDropOffPTS,"Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

		}
		*/
		where:
		transferData << loadData(dataNames, subPath)
	}
	@Unroll
	def "07 : Validate Share Itinerary  #transferData.caseName #transferData.desc"(){

		TransferTestResultData resultData = resultMap.get(transferData.caseName)

		when: "Expected and actual Share Itinerary Values are available"

		then: "Assert the Share Itinerary Page values"
		//Validate Itinerary ID
		assertionEquals(resultData.transfers.shareItineraryPage.actualItenaryID,resultData.transfers.confirmationPage.readitinearyID,"Share Itinerary Screen Itinerary Id Expected & Actual details don't match")

		//Validate Email
		//removed from 10.3
		//assertionEquals(resultData.transfers.shareItineraryPage.actualEmail,resultData.transfers.itineraryPage.emailAddr,"Share Itinerary Screen Email Id Expected & Actual details don't match")

		//Validate Booking dAte
		assertionEquals(resultData.transfers.shareItineraryPage.actualBookingDepDate,resultData.transfers.shareItineraryPage.expectedBookingDepDate,"Share Itinerary Screen Departure Date Expected & Actual details don't match")

		//Validate Booking ID
		assertionEquals(resultData.transfers.shareItineraryPage.acutalbookingId,resultData.transfers.confirmationPage.actualbookingID,"Share Itinerary Screen Booking Id Expected & Actual details don't match")
		//Validate Commission Text
		//assertionEquals(resultData.transfers.shareItineraryPage.actualCommission_txt,resultData.transfers.shareItineraryPage.expectedcomission_txt,"Share Itinerary Screen Commisstion Text Expected & Actual details don't match")
		//Validate Status
		//Status is not checked after booking confirmation. SIT 10.3
		//assertionEquals(resultData.transfers.shareItineraryPage.actualStatus,transferData.confirm_BookingStatusConfirm_txt,"Share Itinerary Screen Booking Status Expected & Actual details don't match")

		//Validate Transfer Date And Time
		assertionEquals(resultData.transfers.shareItineraryPage.actualTransferDateAndtimeInBookedItms,resultData.transfers.itineraryPage.actualTransferDateAndtimeInNonBookedItms,"Share Itinerary Screen, Client Requested Service Pick up Date And Time Actual & Expected don't match")


		//Validate Traveller Details
		assertionEquals(resultData.transfers.shareItineraryPage.actualtravellerDetails,resultData.transfers.shareItineraryPage.expectedtravellerDetails,"Share Itinerary Screen Traveller Details Expected & Actual details don't match")
		/*
		if(subPath=="AirportToHotel.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickUpATH,resultData.transfers.shareItineraryPage.expectedpickupATH,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//Validate Drop off details
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffATH,transferData.confirm_dropOff_text,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}
		else if (subPath=="HotelToAirport.")
		{
			//Validate Pick Up details
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickUPHTA,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//Validate Drop off details
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffHTA,resultData.transfers.shareItineraryPage.expecteddropOffHTA ,"Share Itinerary Screen Drop Off Expected & Actual details don't match")
		}

		else if(subPath=="HotelToStation.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupHTS,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualdropOffHTS,resultData.transfers.shareItineraryPage.expecteddropOffHTS,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="HotelToPort.") {

			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupHTP,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualdropOffHTP,resultData.transfers.shareItineraryPage.expecteddropOffHTP,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="PortToHotel.") {

			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupPTH,resultData.transfers.shareItineraryPage.expectedPickupPTH,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualdropOffPTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="HotelToOther.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupHTO,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualdropOffHTO,resultData.transfers.shareItineraryPage.expecteddropOffHTO,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="OtherToHotel.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupOTH,resultData.transfers.shareItineraryPage.expectedPickupOTH,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualdropOffOTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="AirportToAirport.")
		{
			//validate pickup - added in test class
			//assertionEquals(resultData.transfers.shareItineraryPage.actualPickupATA,resultData.transfers.shareItineraryPage.expectedPickupATA,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff - added in test class
			//assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffATA,resultData.transfers.shareItineraryPage.expectedDropOffATA,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="AirportToStation.")
		{

			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupATS,resultData.transfers.shareItineraryPage.expectedPickupATS,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffATS,resultData.transfers.shareItineraryPage.expectedDropOffATS,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}
		else if(subPath=="StationToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupSTA,resultData.transfers.shareItineraryPage.expectedPickupSTA,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffSTA,resultData.transfers.shareItineraryPage.expectedDropOffSTA,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}
		else if(subPath=="AirportToPort.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupATP,resultData.transfers.shareItineraryPage.expectedPickupATP,"Share Itinerary Screen Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffATP,resultData.transfers.shareItineraryPage.expectedDropOffATP,"Share Itinerary Screen Drop Off Expected & Actual details don't match")
		}

		else if(subPath=="PortToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupPTA,resultData.transfers.shareItineraryPage.expectedPickupPTA,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffPTA,resultData.transfers.shareItineraryPage.expectedDropOffPTA,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}
		else if(subPath=="AirportToOther.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupATO,resultData.transfers.shareItineraryPage.expectedPickupATO,"Share Itinerary Screen Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualdropOffATO,resultData.transfers.shareItineraryPage.expecteddropOffATO,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="OtherToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupOTA,resultData.transfers.shareItineraryPage.expectedPickupOTA,"Share Itinerary Screen Pick Up Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffOTA,resultData.transfers.shareItineraryPage.expectedDropOffOTA,"Share Itinerary Screen Drop Off Expected & Actual details don't match")
		}
		else if(subPath=="StationToPort.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupSTP,resultData.transfers.shareItineraryPage.expectedPickupSTP,"Share Itinerary Screen Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffSTP,resultData.transfers.shareItineraryPage.expectedDropOffSTP,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}

		else if(subPath=="PortToStation.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actualPickupPTS,resultData.transfers.shareItineraryPage.expectedPickupPTS,"Share Itinerary Screen Pick Up Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffPTS,resultData.transfers.shareItineraryPage.expectedDropOffPTS,"Share Itinerary Screen Drop Off Expected & Actual details don't match")

		}
		*/
		//Validate Total and curency
		assertionEquals(resultData.transfers.shareItineraryPage.actualtotalcurrencyText,resultData.transfers.shareItineraryPage.expectedtotalcurrencyText,"Share Itinerary Screen Total and Currency Expected & Actual details don't match")

		//Validate total amount
		assertionEquals(resultData.transfers.shareItineraryPage.actualTotalAmount,resultData.transfers.searchResults.expPrice,"Share Itinerary Screen Total Currency Expected & Actual details don't match")

		//Validate in the send email screen verify email to address only
		//assertionEquals(resultData.transfers.shareItineraryPage.actualEmailInToAddr,resultData.transfers.itineraryPage.emailAddr,"Share Itinerary Screen Email Expected & Actual details don't match")

		//Validation after click on close popup

		//Validate Booking ID
		assertionEquals(resultData.transfers.shareItineraryPage.actbookingId,resultData.transfers.confirmationPage.actualbookingID,"Share Itinerary Screen Booking Id After Close popup Expected & Actual details don't match")
		//Validate Commission Text
		//assertionEquals(resultData.transfers.shareItineraryPage.actCommission_txt,resultData.transfers.shareItineraryPage.expectedcomission_txt,"Share Itinerary Screen Commisstion Text After Close popup Expected & Actual details don't match")
		//Validate Status
		//status is not checked after booking confirmation. SIT 10.3
		//assertionEquals(resultData.transfers.shareItineraryPage.actStatus,transferData.confirm_BookingStatusConfirm_txt,"Share Itinerary Screen Booking Status After Close popup Expected & Actual details don't match")

		//Validate Traveller Details
		assertionEquals(resultData.transfers.shareItineraryPage.acttravellerDetails,resultData.transfers.shareItineraryPage.expectedtravellerDetails,"Share Itinerary Screen Traveller Details After Close popup Expected & Actual details don't match")

		/*
		if(subPath=="AirportToHotel.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickUpATH,resultData.transfers.shareItineraryPage.expectedpickupATH,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//Validate Drop off details
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffATH,transferData.confirm_dropOff_text,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}
		else if (subPath=="HotelToAirport.")
		{
			//Validate Pick Up details
			assertionEquals(resultData.transfers.shareItineraryPage.actPickUPHTA,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//Validate Drop off details
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffHTA,resultData.transfers.shareItineraryPage.expecteddropOffHTA ,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")
		}

		else if(subPath=="HotelToStation.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupHTS,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actdropOffHTS,resultData.transfers.shareItineraryPage.expecteddropOffHTS,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}

		else if(subPath=="HotelToPort.") {

			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupHTP,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actdropOffHTP,resultData.transfers.shareItineraryPage.expecteddropOffHTP,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}

		else if(subPath=="PortToHotel.") {

			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupPTH,resultData.transfers.shareItineraryPage.expectedPickupPTH,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actdropOffPTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}

		else if(subPath=="HotelToOther.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupHTO,transferData.booking_pickup_txt,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actdropOffHTO,resultData.transfers.shareItineraryPage.expecteddropOffHTO,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}

		else if(subPath=="OtherToHotel.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupOTH,resultData.transfers.shareItineraryPage.expectedPickupOTH,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actdropOffOTH,transferData.transfers_bookingConfirm_dropOff_Txt,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}

		else if(subPath=="AirportToAirport.")
		{
			//validate pickup
			//assertionEquals(resultData.transfers.shareItineraryPage.actPickupATA,resultData.transfers.shareItineraryPage.expectedPickupATA,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			//assertionEquals(resultData.transfers.shareItineraryPage.actDropOffATA,resultData.transfers.shareItineraryPage.expectedDropOffATA,"Share Itinerary Screen Drop Off After Close popup  Expected & Actual details don't match")

		}

		else if(subPath=="AirportToStation.")
		{

			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupATS,resultData.transfers.shareItineraryPage.expectedPickupATS,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffATS,resultData.transfers.shareItineraryPage.expectedDropOffATS,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}
		else if(subPath=="StationToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupSTA,resultData.transfers.shareItineraryPage.expectedPickupSTA,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffSTA,resultData.transfers.shareItineraryPage.expectedDropOffSTA,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}
		else if(subPath=="AirportToPort.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupATP,resultData.transfers.shareItineraryPage.expectedPickupATP,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffATP,resultData.transfers.shareItineraryPage.expectedDropOffATP,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")
		}

		else if(subPath=="PortToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupPTA,resultData.transfers.shareItineraryPage.expectedPickupPTA,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffPTA,resultData.transfers.shareItineraryPage.expectedDropOffPTA,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}
		else if(subPath=="AirportToOther.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupATO,resultData.transfers.shareItineraryPage.expectedPickupATO,"Share Itinerary Screen Pick Up After Close popupExpected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actdropOffATO,resultData.transfers.shareItineraryPage.expecteddropOffATO,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}

		else if(subPath=="OtherToAirport.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupOTA,resultData.transfers.shareItineraryPage.expectedPickupOTA,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffOTA,resultData.transfers.shareItineraryPage.expectedDropOffOTA,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")
		}
		else if(subPath=="StationToPort.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupSTP,resultData.transfers.shareItineraryPage.expectedPickupSTP,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffSTP,resultData.transfers.shareItineraryPage.expectedDropOffSTP,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}

		else if(subPath=="PortToStation.")
		{
			//validate pickup
			assertionEquals(resultData.transfers.shareItineraryPage.actPickupPTS,resultData.transfers.shareItineraryPage.expectedPickupPTS,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")
			//validate dropoff
			assertionEquals(resultData.transfers.shareItineraryPage.actDropOffPTS,resultData.transfers.shareItineraryPage.expectedDropOffPTS,"Share Itinerary Screen Drop Off After Close popup Expected & Actual details don't match")

		}
		*/
		//Validate Total and curency
		assertionEquals(resultData.transfers.shareItineraryPage.acttotalcurrencyText,resultData.transfers.shareItineraryPage.expectedtotalcurrencyText,"Share Itinerary Screen Total and Currency Expected & Actual details don't match")

		//Validate total amount
		assertionEquals(resultData.transfers.shareItineraryPage.actTotalAmount,resultData.transfers.searchResults.expPrice,"Share Itinerary Screen Total Currency Expected & Actual details don't match")


		where:
		transferData << loadData(dataNames, subPath)
	}


	protected static loadData(List<String> paths, String subPath) {
		List<TransfersData> dataList = paths.collect { path ->
			new TransfersData(path, subPath)
		}
		return dataList
	}

	public List<String> getPricesOrTransferNamesInAllPages(int actualtotalSearrchResultsCount,int resultsPerPage,List<String> priceOrTransferList,boolean sortByPrice, boolean sortByAlphabeticalOrder)
	{
		int lastPageResults = actualtotalSearrchResultsCount % resultsPerPage
		int sizeofArr=getAllPageNumbersCount()
		List<String> priceList
		List<String> transferNameList
		if(sortByPrice)
		{
			priceList=priceOrTransferList
		}
		if(sortByAlphabeticalOrder)
		{
			transferNameList=priceOrTransferList
		}

		//println("$sizeofArr")
		for(int i=2;i<=sizeofArr;i++)
		{	//println("Entered for loop")
			clickTopNextPage(i-1, sizeofArr)
			sleep(3000)
			waitForAjaxIconToDisappear()
			int actualresultsPerPage=getPageResultsCount()
			//To validate the results from second till last but one pages
			if(i<sizeofArr)
			{
				if(sortByPrice)
				{
					//Get all the prices existing in  page
					priceList=priceList+getPriceList()
				}

				if(sortByAlphabeticalOrder)
				{
					//Get all the transfers name existing in  page
					transferNameList=transferNameList+getTransferNameList()

				}
			}

			//To validate last page results
			if(i==sizeofArr)
			{
				if(sortByPrice)
				{
					//Get all the prices existing in  page
					priceList=priceList+getPriceList()
				}

				if(sortByAlphabeticalOrder)
				{
					//Get all the transfers name existing in  page
					transferNameList=transferNameList+getTransferNameList()
				}
				//Navigate to First Page of the search results
				clickOnFirstPage(sizeofArr)
				sleep(4000)
			}

		}

		if(sortByPrice)
		{

			return priceList
		}

		if(sortByAlphabeticalOrder)
		{
			return transferNameList
		}
	}

}
