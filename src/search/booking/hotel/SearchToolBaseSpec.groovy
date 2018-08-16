package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.page.HotelSearchPage
import org.testng.Assert
import spock.lang.Shared

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.SearchToolData
import com.kuoni.qa.automation.page.hotel.HotelInformationPageBooking
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec

class SearchToolBaseSpec extends BaseSpec{
//	@Shared
//	ClientData client617EURData = new ClientData("client617EUR")
	@Shared
	
	ClientData client = new ClientData("client662")
	def setupSpec() {
		println "setup Speccc"
		//login(client618Html)
	}
	def setup() {
		login(client)
	}
	def searchForSingleRoom(SearchToolData searchToolData) {
		List childages = []
		int numOfRooms	
		String errorRes= "test"
		at HotelSearchResultsPage
		enterHotelDestination(searchToolData.input.property)
		sleep(1000)
		if (searchToolData.output.autoSuggest) {
			selectFromAutoSuggest(searchToolData.input.propertySuggest)
		}
		else {
			Assert.assertFalse(verifyHighlightedSearchTerm(searchToolData.input.property), "AutoSuggest Displayed for the hotel: " + searchToolData.input.property)
			Assert.fail("stopping TC execution since autosuggest not displayed.")
		}
		entercheckInCheckOutDate(searchToolData.input.checkInDays.toString().toInteger(), searchToolData.input.checkOutDays.toString().toInteger())
		sleep(1000)
		clickPax()
		sleep(2000)
		//selectPaxNumOfRooms(searchToolData.input.rooms)
		selectNumberOfAdults(searchToolData.input.adults, 1)
		if (!searchToolData.input.child.equals("0")) {
			childages = searchToolData.input.age.split(";")
			sleep(1000)
			selectNumberOfChildren(searchToolData.input.child, 1)
			for (int i = 0; i < childages.size(); i++) {
				enterChildrenAge(childages[i], "1", i)
			}

		}
		sleep(1000)
		clickPaxRoom()
		clickFindButton()
		waitTillLoadingIconDisappears()
		try {
			at HotelSearchResultsPage
			clickCloseLightbox()
		}
		catch (Exception e) {
		}
		waitTillLoadingIconDisappears()
		sleep(1000)

			def expNumOfRooms = searchToolData.output.roomsReturn
			scrollToBottomOfThePage()
			if (expNumOfRooms) {
				at HotelInformationPageBooking
				sleep(2000)
				numOfRooms = getTotalNumOfRooms()
				softAssert.assertTrue(numOfRooms > 0, "\n Rooms not returned when expected")
			} else {
				errorRes = getZeroResultsError().toString()
				softAssert.assertTrue(errorRes.contains("0 Results"), "\n Zero results not returned. Actual: " + errorRes)
			}
			return true
		}

	def verifyAutoSuggest(SearchToolData searchToolData){
		at HotelSearchResultsPage
		enterHotelDestination(searchToolData.input.property)
		softAssert.assertTrue(verifyHighlightedSearchTerm(searchToolData.input.property), "AutoSuggest not Displayed for the hotel: "+searchToolData.input.property)
		return true
	}
	
	def verifyCityArea(SearchToolData searchToolData){
		List childages=[]
		int numOfRooms
		String errorRes="test"
		at HotelSearchResultsPage
		enterHotelDestination(searchToolData.input.property)
		if (searchToolData.output.autoSuggest)
		selectFromAutoSuggest(searchToolData.input.propertySuggest)
		else softAssert.assertFalse(verifyHighlightedSearchTerm(searchToolData.input.property), "AutoSuggest Displayed for the hotel: "+searchToolData.input.property)
		entercheckInCheckOutDate(searchToolData.input.checkInDays.toString().toInteger(),searchToolData.input.checkOutDays.toString().toInteger())
		waitTillLoadingIconDisappears()
		sleep(1000)
		clickPax()
		sleep(1500)
		//selectPaxNumOfRooms(searchToolData.input.rooms)
		selectNumberOfAdults(searchToolData.input.adults, 1)
		if (!searchToolData.input.child.equals("0")){
			childages=searchToolData.input.age.split(";")
			sleep(500)
			selectNumberOfChildren(searchToolData.input.child, 1)
			for (int i=0;i<childages.size();i++){
				enterChildrenAge(childages[i], "1",i)
			}
			
		}
		sleep(1000)
		clickPaxRoom()
		clickFindButton()

		sleep(2000)
		try {
			at HotelSearchResultsPage
			clickCloseLightbox()
		}
		catch (Exception e) {
		}
		waitUntillLoaderDisappears()
		sleep(2000)
	
		if (searchToolData.output.hotelsReturn){
			at HotelSearchResultsPage
			softAssert.assertTrue(getNumberOfHotelCardsInSearchResults() > 0 , "\n Number of hotels returned not correct!!")
			sleep(3000)
			 //Assertion added to verify Google Map is displayed
			softAssert.assertTrue(verifyMapBlockDisplayed(), "Map Block not displayed")
		}
		else{
			at HotelSearchResultsPage
			sleep(3000)
			errorRes=getZeroResultsError().toString()
			softAssert.assertTrue(errorRes.contains("0 Results"), "\n Zero results not returned. Actual: "+ errorRes)
		}
		//Adding assertions for NV-27547 ticket
		String hotelPropertyName = "Paris"

		if(searchToolData.input.property.toString().equals(hotelPropertyName)){
		for(int i=0;i<getPleaseNoteIconSize();i++){
			at HotelSearchResultsPage
				clickOnPleaseNoteIcon(i)
				sleep(1000)
				softAssert.assertTrue(getPleaseNoteLightBoxTitle(),"Text is not present in please note overlay")
				closeLightBox()
				sleep(1000)
			}
			if(getPleaseNoteIconSize()==0){
				softAssert.assertTrue(true,"Please note icon is not present for Paris city PLP page.")
			}
		}
		//Adding assertions for NV-24903
		sleep(1000)
		if(verifyImageFavorited(0)){
			clickImageFavoriteButton(0)
			waitTillLoadingIconDisappears()
			driver.navigate().refresh()
			waitTillLoadingIconDisappears()
		}
		String favHotelName = "Toulouse"
		if(searchToolData.input.property.toString().equals(favHotelName)) {
			String hotel2 = getHotelName(1)
			clickImageFavoriteButton(1)
			waitTillLoadingIconDisappears()
			verifyImageFavorited(1)
			sleep(1000)
			clickFindButton()
			waitTillLoadingIconDisappears()
			sleep(2000)
			String hotel1 = getHotelName(0)
			assertionEquals(hotel2,hotel1,"Fav item is not displayed as first item.")
		}

		return true
	}
}
