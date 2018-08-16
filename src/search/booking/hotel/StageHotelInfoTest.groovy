package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.PropertySearchData
import com.kuoni.qa.automation.page.hotel.ItineraryBuilderPageBooking
import com.kuoni.qa.automation.page.hotel.ItineraryPageBooking
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.test.search.booking.hotel.HotelBookingBaseSpec
import com.kuoni.qa.automation.helper.StagePropertySearchData
import spock.lang.IgnoreRest

class StageHotelInfoTest extends HotelBookingBaseSpec {

	PropertySearchData propertySearchData = new PropertySearchData("Beverley City")


	@spock.lang.Ignore
	def "TC6 Hotel information page - access from itinerary - Suggested item"() {
		println "\n ***TestName: TC6 Hotel information page - access from itinerary - Suggested item"
		given: "that the travel agent is on home page"
		when: "search for hotel"
		searchCityAndAddToItinerary(propertySearchData)
		at ItineraryBuilderPageBooking
		clickOnGotoItenaryButton()
		at ItineraryPageBooking
		softAssert.assertTrue(veryhotelNameInSuggestedItem(propertySearchData.input.property, 0), "\n Hotel name not correct on Itinerary page suggested item")
		clickHotelOnSuggestedItem(0)
		at ItineraryBuilderPageBooking
		closeItenaryBuilder()
		verifyHotelInfo(propertySearchData)
		then: "Navigates to Hotel info page"
		println "Hotel information page displayed"
	}

	@IgnoreRest
	def "TC7 Hotel information page - access from itinerary - confirmed item"() {
		println "\n ***TestName: TC7 Hotel information page - access from itinerary - confirmed item"
		given: "that the travel agent is on home page"
		when: "search for hotel"
		searchCityAndAddToItinerary(propertySearchData)
		at ItineraryBuilderPageBooking
		//clickOnGotoItenaryButton()
		at ItineraryPageBooking
		enterTravellerDetailsWithoutSave("Mrs", "test", "AutoUser", 0)
		clickAddTravellersButton()
		enterTravellerDetails("Mr", "test", "AutoUserOne", 1)
		sleep(1000)
		clickBookButton(0)
		sleep(1000)
		selectPaxName(0)
		selectPaxName(1)
		clickConfirmBooking()
		waitTillConformationPage()
		coseBookItenary()
		coseBookItenary()
		sleep(1000)
		def bookId = getBookingId(0)
		softAssert.assertTrue(verifyBookingItemPropName(propertySearchData.input.property, 0), "\n Hotel name not correct in booked confirmed item")
		clickHotelNameOnConfirmedItem(0)
		at ItineraryBuilderPageBooking
		closeItenaryBuilder()
		sleep(1000)
		verifyHotelInfo(propertySearchData)
		then: "Navigates to Hotel info page"
		println "Hotel information page displayed"
	}

	@spock.lang.Ignore
	def "TC8 Hotel information page - access from itinerary - remove item"() {
		println "\n ***TestName: TC8 Hotel information page - access from itinerary - remove item"
		given: "that the travel agent is on home page"
		when: "search for hotel"
		searchCityAndAddToItinerary(propertySearchData)
		at ItineraryBuilderPageBooking
		clickOnGotoItenaryButton()
		at ItineraryPageBooking
		softAssert.assertTrue(veryhotelNameInSuggestedItem(propertySearchData.input.property, 0), "\n Hotel name not correct on Itinerary page suggested item")
		RemoveItenaryButton(0)
		sleep(1000)
		at ItineraryBuilderPageBooking
		clickOnHotelRemoveItinerary()
		sleep(1000)
		closeItenaryBuilder()
		verifyHotelInfo(propertySearchData)
		then: "Navigates to Hotel info page"
		println "Hotel information page displayed"
	}
	//Addition of NV-25054 ticket,NV-27645 ticket.
	//Modified flow as per 10.3 UI changes
	def "TC9 Itinerary page - Editing traveler Details"() {
		println "\n ***TestName: TC9 Itinerary page - Editing traveler Details"
		given: "that the travel agent is on home page"
		when: "search for hotel"
		searchCityAndAddToItinerary(propertySearchData)
		/*at ItineraryBuilderPageBooking
		clickOnGotoItenaryButton()
		sleep(1000)*/
		at ItineraryTravllerDetailsPage
		clickOnAddAgentRefButton()
		enterAgentRef(propertySearchData.input.agentRef)
		clickOnSaveOrCancelBtnsAgentRef(1)
		at ItineraryPageBooking
		enterTravellerDetailsWithoutSave("Mrs", "test", "AutoUser", 0)
		//clickAddTravellersButton()
		enterTravellerDetails("Mr", "test", "AutoUserOne", 1)
		sleep(1000)
		enterEmail("abc@gmail.com",0)
		sleep(1000)
		enterTelephoneNumber("9999999999",0)
		sleep(1000)
		clickonSaveButton()
		at ItenaryPageItems
		softAssert.assertTrue(verifyEditButtonDisplayed(2),"Agent Reference eidt button is not displayed.")
		sleep(1000)
		clickOnEditButton(0)
		sleep(500)
		clickOnCancelTravelerButton()
		sleep(500)
		then: "agent reference edit button should be present."
		softAssert.assertTrue(verifyEditButtonDisplayed(2),"Agent Reference eidt button is not displayed.")
		when: "User click's on agent ref edit button"
		clickOnEditButton(2)
		then: "Traveler details edit buttons shown"
		softAssert.assertTrue(verifyEditButtonDisplayed(0),"Traveler 1 edit button not displayed.")
		softAssert.assertTrue(verifyEditButtonDisplayed(1),"Traveller 2 edit button not displayed.")
	}

}
