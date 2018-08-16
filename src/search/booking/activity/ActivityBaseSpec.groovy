package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.TransferTestResultData
import com.kuoni.qa.automation.helper.TransfersTestData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.test.payments.PaymentSpec
import org.apache.commons.lang3.StringUtils
import org.junit.internal.runners.statements.Fail
import spock.lang.Shared

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.activity.ActivityPDPInfoPage
import com.kuoni.qa.automation.page.activity.ActivitySearchResultsPage
import com.kuoni.qa.automation.page.activity.ActivitySearchToolPage
import com.kuoni.qa.automation.page.hotel.ItineraryPageBooking
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.DateUtil

import static org.junit.Assert.*
class ActivityBaseSpec extends PaymentSpec{
	DateUtil dateUtil = new DateUtil()
	def serviceDate=""
	def price=""
	List langLableList=[]
	List langCodeList=[]
	List travellerNames=[]
	List depPointList=[]
	List depTimeList=[]
	List depListTnC=[]
	List depTimeTnC=[]
	List sTarvellers=[]
	String []sortByoption
	String []filteroption
	String []languages
	String []langcode
	public String []itemPrice
	public int activityNum
	int itemNuminItinerary = 0
	String itemStatus
	public String itemName = ""
	public String price
	public String CahrgeApplyLink
	def activityPlpData =[:]

	protected def loginToApplicaiton(ClientData clientData){
		login(clientData)
		at HotelSearchPage

	}

	def searchToolFindActivity(ActivitiesSearchToolData activitiesSearchToolData){
		at ActivitySearchToolPage
		clickActivitySearchTab()
		softAssert.assertTrue(getActivitryPlaceHolder().contains("Destination, area, landmark, activity"), "\nPlaceHolder value not correct in activity")
		enterActivityDestination(activitiesSearchToolData.input.desc)
		int actNumOfLoc = getNumberOfLocationsInAutoSuggest()
		int expectedNumOfLoc = activitiesSearchToolData.output.numOfLocAutoSuggest
		softAssert.assertEquals(actNumOfLoc,expectedNumOfLoc, "\n Auto Suggets for Location is not displayed correct number of items Expected: $expectedNumOfLoc Vs Actual: $actNumOfLoc")
		int actNoOfActi = getNumberOfActivitiesInAutoSuggest()
		int expNoOfActi = activitiesSearchToolData.output.numOfActivityAutoSuggest
		softAssert.assertEquals(actNoOfActi, expNoOfActi, "\n Auto Suggets for Activities is not displayed correct number of items Expected: $expNoOfActi Vs Actual: $actNoOfActi")
		return true
	}

	def searchToolDateType(int days){
		at ActivitySearchToolPage
		clickActivityDateType()
		softAssert.assertEquals(getActiveDateTypeText(), "Individual dates", "\n Individual date ype is not active.")
		selectDateCalender(days)
		//softAssert.assertTrue(selectDateCalender(days), "\n Failed to select calender dates")
		return true
	}

	def verifyDateRange(){
		at ActivitySearchToolPage
		//clickActivityDateType()
		//softAssert.assertTrue(selectDateCalender(30), "\n Failed to select calender dates")
		clickActivityDateType()
		clickDateRangeType()
		String datestr = dateUtil.addDays(0)
		softAssert.assertTrue(getInputDateFrom().contains(datestr), "\n Selected From date is not correct, Expected: "+ datestr)
		datestr = dateUtil.addDays(1)
		softAssert.assertTrue(getInputDateTo().contains(datestr), "\n Selected From Date is not correct, Expected: "+ datestr)
		clickActivityDateType()
		clickDateRangeType()
		selectFromDateCalender(30)
		datestr = dateUtil.addDays(30)
		softAssert.assertTrue(getInputDateFrom().contains(datestr), "\n Selected From Date is not correct, Expected: "+ datestr)
		datestr = dateUtil.addDays(31)
		softAssert.assertTrue(getInputDateTo().contains(datestr), "\n Selected From Date is not correct, Expected: "+ datestr)
		selectToDateCalender(32)
		datestr = dateUtil.addDays(32)
		softAssert.assertTrue(getInputDateTo().contains(datestr), "\n Selected From Date is not correct, Expected: "+ datestr)
		selectToDateCalender(33)
		softAssert.assertTrue(getInputDateTo().contains(datestr), "\n Selected From Date is not correct todate should work only upto 2 days, Expected: "+ datestr)

		selectFromDateCalender(35)
		datestr = dateUtil.addDays(35)
		softAssert.assertTrue(getInputDateFrom().contains(datestr), "\n Selected From Date is not correct, Expected: "+ datestr)
		clickActivityDateType()
		clickInvidualType()
		datestr = dateUtil.addDays(30)
		softAssert.assertTrue(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct, Expected: "+ datestr)
		clickActivityDateType()
		clickDateRangeType()
		datestr = dateUtil.addDays(35)
		softAssert.assertTrue(getInputDateFrom().contains(datestr), "\n Selected From Date is not correct, Expected: "+ datestr)

		return true
	}

	def verifyPaxSearchTool(){
		at ActivitySearchToolPage
		clickPax()
		selectNoAdults("2")
		softAssert.assertTrue(getPaxValue().contains("2 PAX"), "\n Number of pax Displayed Correct ")

		selectNoAdults("8")
		softAssert.assertTrue(getPaxValue().contains("8 PAX"), "\n Number of pax Displayed Correct ")
		selectNoOfChild("2")
		def error=getPaxAgeError()
		println "Pax Error: " + error
		softAssert.assertTrue(error.contains("Please input children ages"),"\n Error for child age not displayed")
		softAssert.assertTrue(getMaxPaxError().contains("9 maximum pax"), "\n maximum pax not displayed ")
		enterChildAge("8" ,0)
		softAssert.assertTrue(getMaxPaxError().contains("9 maximum pax"), "\n maximum pax not displayed ")
		enterChildAge("2" ,1)
		enterChildAge("10" ,0)
		enterChildAge("11" ,1)
		softAssert.assertTrue(getMaxPaxError().contains("9 maximum pax"), "\n maximum pax not displayed ")
		selectNoAdults("7")
		softAssert.assertFalse(paxAgeErrorDisplayed(), "\n Pax Age error displayed when pax input is valid")
		softAssert.assertFalse(maxPaxErrorDisplayed(), "\n Max pax error displayed when pax is lessa than 9 pax")
		selectNoAdults("4")
		selectNoOfChild("1")
		softAssert.assertFalse(paxAgeErrorDisplayed(), "\n Pax Age error displayed when pax input is valid")
		softAssert.assertFalse(maxPaxErrorDisplayed(), "\n Max pax error displayed when pax is lessa than 9 pax")
		softAssert.assertTrue(getFindButtoncss().contains("disabled"), "\n Find Button not disabled when activity destination not entered")
		return true
	}
	def verifyFindFunction(){
		at ActivitySearchToolPage
		clickActivityDateType()
		softAssert.assertTrue(selectDateCalender(0), "\n Failed to select calender dates for today")
		clickPax()
		selectNoAdults("2")
		softAssert.assertTrue(getFindButtoncss().contains("disabled"), "\n Find Button not disabled when activity destination not entered")
		enterActivityDestination("Toulouse, France")
		selectFromAutoSuggest("Toulouse & Surrounding Area, France")
		clickActivityDateType()
		softAssert.assertTrue(selectDateCalender(0), "\n Failed to select calender dates for today")
		clickPax()
		selectNoAdults("2")
		softAssert.assertFalse(getFindButtoncss().contains("disabled"), "\n Find Button not enabled when activity destination entered")
		return true
	}
	def verifyFindFunctionSearchResults(ActivitiesSearchToolData activitiesSearchToolData){
		at ActivitySearchToolPage
		enterActivityDestination(activitiesSearchToolData.input.desc)
		selectFromAutoSuggest(activitiesSearchToolData.input.propertySuggest)
		clickActivityDateType()
		softAssert.assertTrue(selectDateCalender(activitiesSearchToolData.input.date), "\n Failed to select calender dates for today")
		clickPax()
		selectNoAdults(activitiesSearchToolData.input.paxAdults)
		int numOfChild=activitiesSearchToolData.input.paxChild.toString().toInteger()
		if (numOfChild>0){
			List age=activitiesSearchToolData.input.age.toString().split(";")
			selectNoOfChild(activitiesSearchToolData.input.paxChild)
			for (int i=0;i<numOfChild;i++){
				enterChildAge(age[i] ,i)
			}
			clickPax()
			sleep(2000)
		}

		clickFindButton()
		at ActivitySearchResultsPage
		if (activitiesSearchToolData.output.searchResults)
			softAssert.assertTrue(getNumberOfSearchResults()>0, "\n Activities not returned i search results.")
		else softAssert.assertTrue(getZeroResultsError().toString().contains("0 Results"), "\n Number Of Rooms returned is not correct")
		return true
	}
/**searchForActivity
 *Steps inculded : search for activity in given city
 * select from auto sugesstion , select pax and click on find
 * @param activitiesSearchToolData
 * @return
 */
	def searchForActivity(activitiesSearchToolData){
		at ActivitySearchToolPage
		clickActivitySearchTab()
		enterActivityDestination(activitiesSearchToolData.input.desc)
		selectFromAutoSuggest(activitiesSearchToolData.input.propertySuggest)
		List dates=activitiesSearchToolData.input.date.split(";")
		for (int i=0; i<dates.size();i++){
			searchToolDateType(dates[i].toString().toInteger())
		}
		clickPax()
		sleep(1000)
		selectNoAdults(activitiesSearchToolData.input.paxAdults)
		int numOfChild=activitiesSearchToolData.input.paxChild.toString().toInteger()
		if (numOfChild>0){
			List age=activitiesSearchToolData.input.age.toString().split(";")

			selectNoOfChild(activitiesSearchToolData.input.paxChild)
			isErrorChildAgeDisplayed()
			for (int i=0;i<numOfChild;i++){
				enterChildAge(age[i] ,i)
			}
		//	isErrorChildAgeRemoved()
			clickPax()
			sleep(2000)
		}

		clickFindButton()
	}
	/**
	 * To check if the item is Bokun item
	 */

	def verifyBokunItemAddToItinerary(activitiesSearchToolData){
		at ActivitySearchResultsPage
		if(isBokunItemReturned()) {
			activityNum = bokunItemNum()
			activityNameDisplayed(activityNum)
			activityImgDisplayed(activityNum)
			verifyActivityDurationIcon(activityNum)
			getToolTipTextForDurationForTourIcon(activityNum)
			verifyActivityLanuageIcon(activityNum)
			getToolTipTextForLanguageIcon(activityNum)
			itemPrice = getPriceInTile(activityNum).split(" ")
			if (verifyAddToItineraryBtn(activityNum)) {
				clickAddToitinerary(activityNum)
			}
			return true
		}
		else {
			println "BOKUN ITEM NOT RETURNED"
			return false
		}

		}

/**
 * Steps included : Get first item in search result , check name dispalyed , image displayed, get price and save it
 * @param activitiesSearchToolData
 * @return
 */
	def addFirstAvailableItem(activitiesSearchToolData){
		at ActivitySearchResultsPage
		if(searchResult()){
			sleep(2000)
			activityNum=getFirstAviItem()
			itemName = getActivityNameInPLP(activityNum)
			this.itemName = itemName
			itemStatus=statusOfItem()
			activityNameDisplayed(activityNum)
			activityImgDisplayed(activityNum)
			verifyActivityDurationIcon(activityNum)
			getToolTipTextForDurationForTourIcon(activityNum)
			verifyActivityLanuageIcon(activityNum)
			getToolTipTextForLanguageIcon(activityNum)
			itemPrice=getPriceInTile(activityNum).split(" ")
			price = itemPrice[0].toString()
			if(verifyAddToItineraryBtn(activityNum)){
				clickAddToitinerary(activityNum)
		}
			else{
				print "NO Search Result returned"
				return false
			}
		}
		return true
	}
	def getItemNameAndPrice(HotelTransferTestResultData resultData){
		at ActivitySearchResultsPage
		resultData.activity.searchResults.itemName = getActivityNameIti(activityNum)
		resultData.activity.searchResults.price = getPriceInTile(activityNum).split(" ")
	}


	def verifyActivityPDP(ActivitiesSearchToolData activitiesSearchToolData){
		def datestr=""
		def pax=""
		List dates=activitiesSearchToolData.input.date.split(";")
		at ActivityPDPInfoPage
		assertFalse("Zero results found ", isZeroResults())
		softAssert.assertTrue(getActivityNameOnPDP().equals(activitiesSearchToolData.output.itemName),"\n Activity name not correct on PDP")
		softAssert.assertTrue(getActivityImageSrc().contains("images.gta-travel.com/RS/Images"), "\n Image source not displayed")
		softAssert.assertTrue(getTourSummaryText().contains(activitiesSearchToolData.output.summary),"\n Activity Summary not correct")
		softAssert.assertTrue(getDurationOnPDP().contains(activitiesSearchToolData.output.duration),"\nDuration not correct on activity pdp")
		softAssert.assertTrue(getFrequencyOnPDP().contains(activitiesSearchToolData.output.frequency), "\n Frequency not correct on activity pdp")
		softAssert.assertTrue(getDeparturePointOnPDP().contains(activitiesSearchToolData.output.depPoint), "\n Depparture Point not correct on activity pdp")
		softAssert.assertTrue(getDepartureTimeOnPDP().contains(activitiesSearchToolData.output.depTime), "\n Departure time not correct on activity pdp Actual : "+getDepartureTimeOnPDP()+ "Expected :"+activitiesSearchToolData.output.depTime )
		softAssert.assertTrue(getLanguageOnPDP().contains(activitiesSearchToolData.output.langList), "\n  Language correct on activity pdp")
		for (int i=0; i<dates.size();i++){
			datestr = dateUtil.addDays(dates[i].toString().toInteger())
			softAssert.assertTrue(getDateCell(i).contains(datestr), "\n Date is not correct in date cell $i")
			softAssert.assertTrue(getLanguagesAndExtraCell(i).contains(activitiesSearchToolData.output.LanguagesAndExtra), "\nLanguages and extra is not correct in cell $i")
			pax=getPaxCell(i)

			softAssert.assertTrue(pax.contains(activitiesSearchToolData.output.pax), "\n Pax not correct in pax cell $i Actual :"+pax+ "Expected :"+activitiesSearchToolData.output.pax)
			clickCancellationLink(i)
			softAssert.assertTrue(getLightBoxHeader().contains("Cancellation Policy"), "\n Cancellation policy lightbox not displayed")
			closeLightBox()
			softAssert.assertTrue(getAvailability(i).contains(activitiesSearchToolData.output.status), "\nAvailability is not correct in cell $i")
			softAssert.assertTrue(addToItineraryButtonDisplayed(i), "\n Add to itinerary button not displayed in cell $i")
		}
		softAssert.assertTrue(verifyactivityDescreptionDisplayed(activitiesSearchToolData.output.tourDesc), "\n Activity Tour descreption not correct")
		softAssert.assertTrue(verifyactivityDescreptionDisplayed(activitiesSearchToolData.output.tourDesc), "\n Activity Tour descreption not correct")
		softAssert.assertTrue(verifyactivityDescreptionDisplayed(activitiesSearchToolData.output.pleaseNote), "\n Activity Please note descreption not correct")
		softAssert.assertTrue(verifyactivityDescreptionDisplayed(activitiesSearchToolData.output.info), "\n Activity Info descreption not correct")

		return true
	}

	def verifyActivityPLP(ActivitiesSearchToolData activitiesSearchToolData){
		at ActivitySearchResultsPage
		if ( isActivityPlpPageDispalyed()){
			softAssert.assertTrue(isFilterSectionDisplayed())
			softAssert.assertTrue(sortByDefaultValue().equals(activitiesSearchToolData.output.sortByDefault),"\n sort by Does not have default value displayed")
			selectSortBy()
			sortByoption= activitiesSearchToolData.output.sortByOption.split(";")
			for (int i=0 ;i<sortByoption.size();i++){
				softAssert.assertTrue(sortByDropDownList(i).equals(sortByoption[i]),"\n sort by options are not dispalyed corretly in Drop down list")
			}
			selectSortBy()
			assertContains(resultAndCount(),"Results 1","result and pagination values are not displayed correct")

			pagenavigationIcons()
			filteroption = activitiesSearchToolData.output.filterType.split(";")
			for (int i=0 ;i<filteroption.size();i++){
				softAssert.assertTrue(activityFilters(i).equals(filteroption[i]),"\n Filter options are not displayed correct")
			}
			//assertContains(clickPagenationArrow(0),activitiesSearchToolData.output.page2Result,"result displayed in page 2 is not correct")
			/*Navigate back to page 1 and check the result */
		//	assertContains(clickPagenationArrow(0),activitiesSearchToolData.output.result,"result displayed in page 1 is not correct")
			/*Page navigation by clicking on page number*/
			//for (int i=1;i <= getTotalNumPages();i++){
			//	softAssert.assertEquals(i,clickOnPageNum(i),"Expected Page is not loaded expected:"+i)
			//}

		}

		return true
	}
	/** activity item card verfication **/
	def verifyItemDetailsPLP(ActivitiesSearchToolData activitiesSearchToolData){
		at ActivitySearchResultsPage
		activityNum=getFirstAviItem()
		activityNameDisplayed(activityNum)
		activityImgDisplayed(activityNum)
		verifyActivityDurationIcon(activityNum)
		getToolTipTextForDurationForTourIcon(activityNum)
		softAssert.assertTrue(!getActivtiyDuration(activityNum).equals(" "),"\n Activity duration not dispalyed")
		softAssert.assertTrue(!getCancellationCondition().equals(" "),"\n Cancellation policy link  not dispalyed")
		softAssert.assertTrue(!getPriceInTile().equals(" "),"\n Price and currency type not displayed")
		softAssert.assertTrue(!getActivityLanguage().equals(" "),"\n Langugae(s) not displayed")
		verifyActivityLanuageIcon(activityNum)
		getToolTipTextForLanguageIcon(activityNum)
		languages=getActivityLanguage(0).split(',')
		langcode=getActivityLanguageCode(0).split(',')
		softAssert.assertTrue(!getActivityLanguageCode(0).equals(" "),"\n Language Code not displayed")
		//for (int i=0; i<languages.size();i++){
		//	softAssert.assertTrue(langcode[i].charAt(0).equals(languages[i].charAt(0)),"\n Activity langugae and language code not in SYnc langcode :"+langcode[i]+ "languages:"+languages[i])
		//}
		verifyPaxIconIsVisible()
		getToolTipTextForPaxIcon()

		verifyPaxNumberIsVisible(activitiesSearchToolData.input.totalPax.toInteger(),0)
		softAssert.assertTrue(!statusOfItem().equals(" "),"\n Available text not displayed")
		verifyAddToItineraryBtn(activityNum)
		activityPlpData=storeSightSeeingItemDetails(activityNum)
		return true
	}
	def VerifyEssentailInfo(){
		if(verifyEssentialInfoOverlay(activityNum)){
			verifyEssentialInfoClosebtn()
			verifyEssentialInfoContent()
			closeEssentialInfoOverlay()
		}else{
			println "Essential info icon not displayed"
					}
		return true
	}
	def verifyCancellation(){
	if (cancellationLink(activityNum)){
		clickOnCancellationLink(activityNum)
		cancellationOverlayDisplayed()
		softAssert.assertTrue(cancellationOverlayHeader().equals("Cancellation Policy"),"\n Cancellation Policy header not displayed")
		softAssert.assertTrue(!cancellationOverlaySubHeader().equals(null),"\n cancellation overlay sub Headers are not displayed")
		softAssert.assertTrue(!cancellationoveralySpecialCndt().equals(null),"\n cancellation overlay please note text not displayed")
		softAssert.assertTrue(!cancellationoverlayTandCText().equals(null),"\n cancellation overlay T and c not displayed")
		closeCancellationOverlay()
	}else{
		print "Cancellation link not Displayed"
		return false
	}
		return true
	}

	def VerifyMoreLangOption(){
		if (moreLangOptionlink(activityNum)){
			int aviitemCount= (Integer)clickMoreLangOptionLink(activityNum)
			softAssert.assertTrue(aviitemCount>1,"\n More Available item not displayed on clicking on option Link")
			collapseMoreLangOptionLink(activityNum)
		}else{
			println("This item don't have More languages link")
		}
		return true
	}

	def VerifySortLowestPrice(){
		selectSortByoption("Lowest price")
		sleep(400)
		waitTillLoadingIconDisappears()
		List<Double> pricelist =priceofAllItems()
		List<Double> pricelistSorted = new ArrayList(pricelist)
		Collections.sort(pricelistSorted)
		boolean sorted = pricelistSorted.equals(pricelist)
		//cleanup to click on first page
		clickOnPageNum(1)

	}

	def VerifySortHigestPrice(){
		selectSortByoption("Highest price")
		sleep(400)
		waitTillLoadingIconDisappears()
		List<Double> pricelist =priceofAllItems()
		List<Double> pricelistSorted = new ArrayList(pricelist)
		Collections.sort(pricelistSorted, Collections.reverseOrder())
		boolean sorted = pricelistSorted.equals(pricelist)
		clickOnPageNum(1)
		selectSortByoption("Lowest price")

	}

	def verifySortAToZ(){
		selectSortByoption("A-Z")
		sleep(100)
		waitTillLoadingIconDisappears()
		List<String> itemsName =NameofAllItems()
		List<String> itemNameSorted = new ArrayList(itemsName)
		Collections.sort(itemNameSorted)
		boolean sorted = itemNameSorted.equals(itemsName)

	}

	def verifySortZToA(){
		selectSortByoption("Z-A")
		sleep(100)
		waitTillLoadingIconDisappears()
		List<String> itemsName =NameofAllItems()
		List<String> itemNameSorted = new ArrayList(itemsName)
		Collections.sort(itemNameSorted, Collections.reverseOrder())
		boolean sorted = itemNameSorted.equals(itemsName)
	}
	def VerifyFilterfunctionality(){
		if(totalFilteroption()>= 8){
			checkAndUncheckFilter()
		}
		else{
			print "Sight Seeing Activity count is less than 8"
		}
		return true
	}

	def NavigateToPDPFirstAvilable(){
		at ActivitySearchResultsPage
		if(searchResult()) {
			sleep(2000)
			clickOnActivityName(activityNum)
			at ActivityPDPInfoPage
			if(activityPageDisplayed()){
				softAssert.assertTrue(activityPlpData.sightSeeingName.equals(getActivityName()),"\n Name Dispalyed in PDP is not matching with Item name in PLP")
				softAssert.assertTrue(activityPlpData.sightSeeingStatus.equals(getAvailability(0)),"\n Item Status in PDP is not matching with Item Status in PLP")
				softAssert.assertTrue(activityPlpData.sightSeeingAmntAndCurncy.equals(getPriceandCurrecny(0)),"\n Item Price in PDP is not matching with Item Price in PLP")
				softAssert.assertTrue(activityPlpData.sightSeeingCancelChrgTxt.equals(getclickCancellationLink(0)),"\n Item Charge applied link in PDP is not matching with Item Charge applied link in PLP")
				String [] languageOptions=getLanguageOnPDP().split("Language: ")
				softAssert.assertTrue(activityPlpData.sightSeeingLanguageOption.equals(languageOptions[1]),"\n Item languages options in PDP is not matching with languages options in PLP " + languageOptions[1])
			}
		}
		return true
	}

	def AddFirstAvailableItemToItinerary(ActivitiesSearchToolData activitiesSearchToolData){
				at ActivityPDPInfoPage
		assertFalse("Zero results found ", isZeroResults())
		List dates=activitiesSearchToolData.input.date.split(";")
		if (verifyMultipleDepartureLinkDisplayed()){
			depPointList=getMultipleLocations()
			depTimeList=getMultipleLocationsTime()
			closeLightBox()
		}
		for (int i=0; i<dates.size();i++){
			if (getAvailability(i).equals("Available")){
				serviceDate=getDateCell(0)
				price=getPrice(i)
				langLableList=getLanguageOnPDP().toString().replaceAll("Language:","").split(",")
				langCodeList=getLanguageCodesOnPDP(0).split(",")
				CahrgeApplyLink=getclickCancellationLink(i)
				clickOnAddToItinerary(i)
				break
			}else if(getAvailability(i).equals("On Request")){
				serviceDate=getDateCell(0)
				//serviceDate=dateUtil.addDays(dates[i].toString().toInteger())
				price=getPrice(i)
				langLableList=getLanguageOnPDP().toString().replaceAll("Language:","").split(",")
				langCodeList=getLanguageCodesOnPDP(0).split(",")
				CahrgeApplyLink=getclickCancellationLink(i)
				clickOnAddToItinerary(i)
				break
			}
		}
		return true
	}

	def verifyActivityItineraryBuilder(ActivitiesSearchToolData activitiesSearchToolData){
		at ActivityPDPInfoPage
		assertFalse("Zero results found ", isZeroResults())
		def datestr = dateUtil.addDays(0)
		softAssert.assertTrue(getItenaryBuilderTitle().startsWith("#")&&contains("I"), "\n Itinerary id not generated with I")
		softAssert.assertTrue(getItenaryBuilderTitle().contains(serviceDate), "\n Itinerary id not generated with todays date")
		softAssert.assertTrue(verifyActivityNameOnBuilder(0).equals(activitiesSearchToolData.output.itemName),"\n Activity name not correct on Itinerary Builder")
		softAssert.assertTrue(verifyImageDisplayedOnItineraryBuilder(0), "\n Image not displayed on itinerary Builder")
		softAssert.assertTrue(getDurationfromItineraryBuilder(0).equals(activitiesSearchToolData.output.duration.toString().replace("Duration: ","").trim()), "\n Duration not correct on itinerary Builder")
		String languageList=activitiesSearchToolData.output.langList
		String[] LanguSplit =languageList.split(':')
		softAssert.assertTrue(getLanguagefromItineraryBuilder(0).contains(LanguSplit[0]), "\n lanugage text not correct on itinerary Builder")
		softAssert.assertTrue(getLanguagefromItineraryBuilder(0).contains(LanguSplit[1]), "\n langList not correct on itinerary Builder")
		softAssert.assertTrue(getServiceDateAndPaxfromItineraryBuilder(0).contains(serviceDate), "\n Service Date not correct on itinerary builder")
		softAssert.assertTrue(getServiceDateAndPaxfromItineraryBuilder(0).contains(activitiesSearchToolData.output.NoOfPax+" PAX"), "\n Number Of Pax not correct on itinerary builder")
		softAssert.assertTrue(getAvailabiliytyFromItineraryBuilder(0).contains(activitiesSearchToolData.output.status), "\n Status not correct on itinerary builder")
		softAssert.assertTrue(getPriceItinineraryBuilder(0).contains(price), "\n Price not correct on itinerary builder")
		softAssert.assertTrue(getPriceItinineraryBuilder(0).contains(client.currency), "\n Price not correct on itinerary builder")
		softAssert.assertTrue(verifyItineraryItemRemoveButtonDisplayed(0), "\n Itinerary Remove Button not displayed")
		return true
	}

	def verifySuggestedItem(ActivitiesSearchToolData activitiesSearchToolData){
		at ItineraryPageBooking
		softAssert.assertTrue(verifyHotelNameOnsuggstedItem(activitiesSearchToolData.output.itemName,0), "\n Hotel name not correct on suggested item, expected: "+activitiesSearchToolData.output.itemName)
		softAssert.assertTrue(imgDisplayedOnSugestedItem(0), "\n Image not displayed on suggested item!!")
		softAssert.assertTrue(verifyBookButtonDisplayed(0), "\n Book Button not displayed on suggested item!!")
		softAssert.assertTrue(CahrgeApplyLink.equals(ItineraryCancellationText(activityNum)), "\n Cancellation link mismatch between PDP page and Itinerary Page!!")
		softAssert.assertTrue(getActivityDateDurationSuggestedItem(0).contains(activitiesSearchToolData.output.duration.toString().replace("Duration: ","").trim()), "\n Duration not correct on suggested item, Expected: "+ getActivityDateDurationSuggestedItem(0) +"Actual"+activitiesSearchToolData.output.duration.toString().replace("Duration: ","").trim())
		softAssert.assertTrue(getActivityDateDurationSuggestedItem(1).contains(activitiesSearchToolData.output.langList.split(":")[1].toString().toLowerCase().trim()), "\n language list not correct on Suggested Item Actual :"+getActivityDateDurationSuggestedItem(1)+ "Expected "+ activitiesSearchToolData.output.langList.split(":")[1].toString().toLowerCase() )
		softAssert.assertTrue(verifyCheckInDateDurationSuggestedItem(serviceDate,0), "\n Service Date not correct on Suggested Item")
		softAssert.assertTrue(verifyAmountSuggestedItem(price, 0), "\n Price not correct on suggested Item!!")
		softAssert.assertTrue(verifyCurrencySuggestedItem(client.currency, 0), "\nCrighturrency is not correct!!")
		softAssert.assertTrue(verifyhotelStatusDisplayedSuggestedItem(0), "\n Item Status not correct on suggested item!!")
		return true
	}

	def verifyTermsAndConditions(ActivitiesSearchToolData activitiesSearchToolData){
		def name=""
		at ItineraryPageBooking

		softAssert.assertTrue(verifyTandCHotelName(activitiesSearchToolData.output.itemName), "Hotelname not correct in TandC section!!")
		softAssert.assertTrue(verifyTandCRoomPaxDetails(activitiesSearchToolData.output.NoOfPax+" PAX", 1), "\n Number of Pax not correct on tandc!!")
		softAssert.assertTrue(verifyTandCRoomPaxDetails(serviceDate,0), "\n service date not correct!!")
		softAssert.assertTrue(getPriceFromTandCLightbox().contains(price), "\n Price not correct on terms and conditions!!")
		softAssert.assertTrue(getPriceFromTandCLightbox().contains(client.currency), "\n Price not correct!!")
		depListTnC= getPickUpLocationTandC()
		String depTimeTnCfull=getDepartureTime()
		depTimeTnC=depTimeTnCfull.split("Time:")

		for (int i=0;i<travellerNames.size();i++){
			name=getTravellerName(i)
			softAssert.assertTrue(name.contains(travellerNames[i]), "\n Traveller1 name not correct on Tand C, Actual: "+name+" Expected:"+travellerNames[i])
		}

		if (langCodeList.size().equals(1))
			softAssert.assertTrue(getLanguageTandC().contains(activitiesSearchToolData.output.language), "\n AvailableLangugae displayed not correct in T and C, Actual: "+getLanguageTandC() +  " Expected: "+activitiesSearchToolData.output.language)
		if (activitiesSearchToolData.output.depPoint.toString().contains("Multiple")){
			for (int i=0;i<depPointList.size();i++){
				if (verifyMultipleDepartureLinkDisplayed()){
					softAssert.assertTrue(getDepaturePointListTandC(i).contains(depPointList[i]), "\n Departure point in the list not correct for 1 , Expected: "+depPointList[i])
					softAssert.assertTrue(getDepartureTime(i).contains(depTimeList[i]), "\n Departure Time in the list not correct for 1 , Expected: "+depTimeList[i])
				}
			}
		}
		else{
			softAssert.assertTrue(getDeparturePoint().contains(activitiesSearchToolData.output.depPoint), "\n Departure point not correct on Tand C, Actual: "+getDeparturePoint() +" Expected: " +activitiesSearchToolData.output.depPoint)
			softAssert.assertTrue(getDepartureTime().contains(activitiesSearchToolData.output.depTime), "\n Departure time not correct on Tand C Actual: "+getDepartureTime() + "Expected: "+ activitiesSearchToolData.output.depTime)
		}

		return true
	}

	def selectTravelelrsInTandC(){
		for (int i=0;i<travellerNames.size();i++){
			selectPaxName(i)
		}
	}

	def addTravellers(int adults , int numOfChild, List age){
		def name=""
		int num=0
		at ItenaryPage
		enterEmail("622@html.com",0)
		for (int i=0;i<adults;i++){
			name="testA"
			enterTravellerDetailsWithoutSave("Mrs", name, "AutoUser" ,i)
			//enterEmail("622@html.com",0)
			//clickAddTravellersButton()
			num=num+1
			travellerNames.add(name+" AutoUser")
		}

		for (int j=0;j<numOfChild;j++){
			name="testC"
			enterChildTravellerDetailsWithoutSave( name, "AutoUser" ,num)
			//addAgeForChildTraveller(age[j], j)
			//clickAddTravellersButton()
			num=num+1
			travellerNames.add(name+" AutoUser\n"+"("+age[j]+"yrs)")
		}

		saveTravellerDetails()
	}


	def verifyBookingConfirmationPage(ActivitiesSearchToolData activitiesSearchToolData){
		def name=""
		at ItineraryPageBooking
		//softAssert.assertTrue(bookingConfirmationEmail(client.usernameOrEmail), "\n Email is not correct on booking confirmation page.")
		boolean bookingIdDisplayed = false
		try {
			bookingIdDisplayed = verifyBookingIdDisplayed()
		} catch (Exception e) {
		}
		softAssert.assertTrue(bookingIdDisplayed, "\n Booking Id not displayed!!")
		for (int i=0;i<travellerNames.size();i++){
			softAssert.assertTrue(verifyTravellerDetailsDisplayed(travellerNames[i],i), "\nTraveller not displayed")
		}
		softAssert.assertTrue(verifyBookingDepartureDate(serviceDate) , "\n Booking departure date not correct!!")
		def desc=getItemNameOnBookingConfirmation()
		def depPoint= getBookingDeparturePointConfirmation()
		println "Booking Deparutre on confirmation page: "	+ depPoint
		//println "Expected name on confirmation page: "	+ activitiesSearchToolData.output.itemName
		softAssert.assertTrue(desc.contains(activitiesSearchToolData.output.itemName), "\n Activity name not displayed on confirmation page, actual: "+ desc)
		softAssert.assertTrue(depPoint.contains(depListTnC[0]), "\nDeparture point not correct on confirmation page!!, Actual: " +depListTnC[0] +" Expected: "+depPoint)
		softAssert.assertTrue(getBookingDepartureDateandTimeConfirmation().contains(serviceDate), "\n departure date not correct")
		softAssert.assertTrue(getBookingDepartureDateandTimeConfirmation().contains(depTimeTnC[0]), "\n departure time not correct  on confirmation page:" +depTimeTnC )
		softAssert.assertTrue(getPriceBookingConfirmation().contains(price), "\n Price not correct on booking confirmation")
		softAssert.assertTrue(getPriceBookingConfirmation().contains(client.currency), "\n Currency not correct on booking confirmation")
		return true
	}

	def verifyBookedItem(ActivitiesSearchToolData activitiesSearchToolData){

		def bookId=getBookingId(0)
		softAssert.assertTrue(bookId.length()>0, "\n Booking Id is not correct on the booking card!!")

		softAssert.assertTrue(verifyBookingStatus(activitiesSearchToolData.output.status, 0), "\n Booking status not correct on Booking item")
		softAssert.assertTrue(verifyBookingItemContentImgDisplayed(0), "\n Image not displayed on booking item")
		softAssert.assertTrue(verifyBookingItemPropName(activitiesSearchToolData.output.itemName, 0), "\n Hotel name not correct in booked item")
		softAssert.assertTrue(verifyBookedItemPax(activitiesSearchToolData.output.NoOfPax+" PAX", 0), "\n Pax not correct on booked item")
		String CancelLInkPDP=verifyCancellationLinkDisplayed(activityNum)
		softAssert.assertTrue(CahrgeApplyLink.equals(CancelLInkPDP), "\n Cancellation link mismatch before and After Booking!! Actual : $CahrgeApplyLink Vs Expected : $CancelLInkPDP")

		softAssert.assertTrue(verifyCheckInDuration(serviceDate, 0), "\n Duration not correct on booked item")
		softAssert.assertTrue(getBookedItemAmount(0).equals(price), "\n Amount not displayed on booked item , Expected: $price")
		softAssert.assertTrue(verifyBookedItemCurrency(client.currency, 0), "\n Currency not correct on booke ditem!!")
		String sTarveller= getTravellerNamesBookedItem(0)
		sTarvellers=sTarveller.split(",")
		for (int i=0;i<travellerNames.size();i++){
			String tarvellerNames=travellerNames[i]
			travellerNames[i]=tarvellerNames.replace("\n"," ")
			softAssert.assertTrue(sTarvellers[i].contains(travellerNames[i]), "\n traveller name : "+travellerNames[i]+ "not displayed on Booked item" +sTarvellers[i])

		}
		//softAssert.assertTrue(StringUtils.containsIgnoreCase(getDeparturePointBookedItem(0),depListTnC[0]),"\n Departure point not correct on booked item!!")
		/*uncomment after fixing issue
		softAssert.assertTrue(getDeparturePointBookedItem(0).contains(depListTnC[0]), "\nDeparture point not correct on booked item!! Actual : "+ getDeparturePointBookedItem(0)+"Expected :"+depListTnC[0])
		softAssert.assertTrue(getDepartureTimeBookedItem(0).contains(depTimeTnC[0]), "\n departure time not correct  on booked item")
		*/
		return true
	}

	def bookedItemItineraryPage(activitiesSearchToolData){
		at ItenaryPage
		def bookId=getBookingId(itemNuminItinerary)
		//softAssert.assertFalse("yee", true)
		softAssert.assertTrue(bookId.length()>0, "\n Booking Id is not correct on the booking card!!")
		bookingStatus(itemNuminItinerary)
		softAssert.assertTrue(bookingStatus(itemNuminItinerary).equals("Confirmed") ,"\n Bookings are not confirmed , Status Displayed is : "+ bookingStatus(itemNuminItinerary))
		softAssert.assertTrue(getIndividualPrice(itemNuminItinerary).equals(itemPrice[0]),"\n Item Price is not same after booking before booking :"+ itemPrice+"After Booking"+getIndividualPrice(itemNuminItinerary))
		if ( bookedVoucherIcon() && platform.equals("web")){
			clickOnVoucherButton(itemNuminItinerary)
			softAssert.assertTrue(DownldVocherFrmOverLay(),"\n Error Displayed on trying to download")
			closeDwldVocherOverlay()
		}else{
			softAssert.assertTrue(bookedVoucherIcon(),"\n Voucher icon not dispalyed for booked item")
		}

		return true
	}

	def cancelBookedItem (){
		at ItenaryPage
		if(verifyCancelItemBtn(itemNuminItinerary)){
			softAssert.assertTrue(clikCancelItemBtn(),"\n Cancel Overlay Not displayed after clicking on cancel btn")
			cancelItemFromOveralay()
		}

		return true
	}


	def verifyCancelledItem (){
		at ItenaryPage
		cancelledItemStatus()
		softAssert.assertTrue(cancelledItemStatus().equals("Cancelled"),"\n item is not cancelled , Status Of item is :"+  cancelledItemStatus())
		//softAssert.assertFalse(bookedVoucherIcon(),"\n vocher icon of cancelled item is not removed")
		return true
	}





}
