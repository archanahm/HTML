package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.hotel.ItineraryPageBooking
import spock.lang.Shared

class ActivityBokunTest extends ActivityBaseSpec  {

	@Shared
	ClientData client = new ClientData("BokunRomeClient")
	@Shared
	ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData("SanityTestDataCity1")

	def "TC01: Launch the URL and Search For Bokun SightSeeing , addfirst Available Item Book and Cancel "(){
		given: "The agent logged in"
		loginToApplicaiton(client)
		when:"Agent search for on Activity in City"
		searchForActivity(activitiesSearchToolData)
		then:"activites PLP Page displayed"
		verifyBokunItemAddToItinerary(activitiesSearchToolData)
		//given: "Itinerary Page displayed"
		List age=[]
		when :"Enter traveller details"
		int numOfAdult = activitiesSearchToolData.input.paxAdults.toString().toInteger()
		int numOfChild = activitiesSearchToolData.input.paxChild.toString().toInteger()
		if (numOfChild > 0)
			age = activitiesSearchToolData.input.age.toString().split(";")
		at ItenaryPage
		addTravellers(numOfAdult, numOfChild, age)
		sleep(1000)
		then:"Save button enabled"
		clickonSaveButton(0)
		at ItineraryPageBooking
		clickBookButton(0)
		sleep(1000)
		then:"Terms and conditions page displayed"

		when:"select travellers"
		//selectTravelelrsInTandC()  *commented since in R10.3 travellers are selected by default*
		and:"click Confirm BookingButton"
		clickConfirmBooking()
		waitTillConformationPage()
		then:"Confirmation page will be displayed"
		when:"close lightbox"
		coseBookItenary()
		sleep(1000)
		then:"Booked item displayed"

		and:"verify booked item "
		bookedItemItineraryPage()
		at ItenaryPage
		then:"Cancel the item "
		cancelBookedItem()
		sleep(3500)
		verifyCancelledItem()

	}
	// add new method to verify if method connectivity works





}
