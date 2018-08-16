package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.page.hotel.ItineraryPageBooking
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Unroll

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.ItenaryPage

	class ActivityPDPTest extends ActivityBaseSpec  {
	@Shared
	ClientData client = new ClientData("creditClient")

	def setup() {
		login(client)
}

	@Unroll
	def "Activity Search and PDP  - Information"(){
		given: "The agent logged in"
		when:"Agent search for on Activity"
		loginToApplicaiton(client)
		ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData(testCase)
		searchForActivity(activitiesSearchToolData)
		then:"user navigates to activity PDP page"
		verifyActivityPDP(activitiesSearchToolData)

		where:
		testCase <<  ["TS13"]
	}
	/** This  test case is not valid from R10.3 , since itinerery builder is removed
	def "Activity Search and PDP - Itiniery Builder"(){
		given: "The agent logged in"
		when:"Agent search for on Activity"
		ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData(testCase)
		searchForActivity(activitiesSearchToolData)
		and: "add first available item to itinerary"
		AddFirstAvailableItemToItinerary(activitiesSearchToolData)
		then:"Activity Item added to Itinerary Builder"
		verifyActivityItineraryBuilder(activitiesSearchToolData)

		where:
		testCase <<  ["TS13"]
	} **/


@Unroll


	def "Activity Search and PDP -  Itinerary Page #testCase"(){
		given: "The agent logged in"
		when:"Agent search for on Activity"
		loginToApplicaiton(client)
		List age=[]
		ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData(testCase)
		searchForActivity(activitiesSearchToolData)
		and: "add first available item to itinerary and Go to Itinerary Page"
		AddFirstAvailableItemToItinerary(activitiesSearchToolData)
		at ItenaryBuilderPage
		//clickOnGotoItenaryButton() *commented since in R10.3  itinerary builder is removed*
		then:"Suggested item will be displayed"
		verifySuggestedItem(activitiesSearchToolData)

		when :"Add travellers and click Book Button"
		int numOfAdult=activitiesSearchToolData.input.paxAdults.toString().toInteger()
		int numOfChild=activitiesSearchToolData.input.paxChild.toString().toInteger()
		if (numOfChild>0)
			age=activitiesSearchToolData.input.age.toString().split(";")
		at ItenaryPage
		addTravellers(numOfAdult,numOfChild,age)
		sleep(800)
		clickonSaveButton(0)
		at ItenaryPage
		clickBookButton(0)
		sleep(800)
		then:"Terms and conditions page displayed"
		verifyTermsAndConditions(activitiesSearchToolData)
		when:"select travellers"
		//selectTravelelrsInTandC()  *commented since in R10.3 travellers are selected by default*
		and:"click Confirm BookingButton"
		clickConfirmBooking()
		waitTillConformationPage()
		then:"Confirmation page will be displayed"
		verifyBookingConfirmationPage(activitiesSearchToolData)
		when:"close lightbox"
		coseBookItenary()
		sleep(1000)
		then:"Booked item displayed"
		verifyBookedItem(activitiesSearchToolData)
		
		where:
		testCase << ["TS17"] //["TS20","TS14","TS17","TS16","TS18","TS19","TS21"]
}
// TS15 and TS16 needs modification
//

	def "Activity Search and PDP -  Itinerary Page test #testCase"(){
		given: "The agent logged in"
		when:"Agent search for on Activity"
		List age=[]
		ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData(testCase)
		searchForActivity(activitiesSearchToolData)
		and: "add first available item to itinerary and Go to Itinerary Page"
		AddFirstAvailableItemToItinerary(activitiesSearchToolData)
		//clickOnGotoItenaryButton() *commented since in R10.3  itinerary builder is removed*
		then:"Suggested item will be displayed"
		verifySuggestedItem(activitiesSearchToolData)

		when :"Add travellers and click Book Button"
		int numOfAdult=activitiesSearchToolData.input.paxAdults.toString().toInteger()
		int numOfChild=activitiesSearchToolData.input.paxChild.toString().toInteger()
		if (numOfChild>0)
			age=activitiesSearchToolData.input.age.toString().split(";")
		at ItenaryPage

		addTravellers(numOfAdult,numOfChild,age)
		sleep(1000)
		clickonSaveButton(0)
		at ItineraryPageBooking
		clickBookButton(0)
		sleep(1000)
		then:"Terms and conditions page displayed"
		verifyTermsAndConditions(activitiesSearchToolData)
		when:"select travellers"
		//selectTravelelrsInTandC() *commented since in R10.3 travellers are selected by default*
		and:"click Confirm BookingButton"
		clickConfirmBooking()
		waitTillConformationPage()
		then:"Confirmation page will be displayed"
		verifyBookingConfirmationPage(activitiesSearchToolData)
		when:"close lightbox"
		coseBookItenary()
		sleep(1000)
		then:"Booked item displayed"
		verifyBookedItem(activitiesSearchToolData)

		where:
		testCase <<["TS20","TS16", "TS17", "TS18","TS19","TS21"]

		// "TS15" =================>Hop on Hop off activity setup changes required in application
		// "TS16" =====> Child traveller coding needs update ,"TS14",  "TS17", "TS18", "TS19", "TS20","TS21"
	}
}
