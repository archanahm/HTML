package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.SoftAssertHelper
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.hotel.ItineraryPageBooking
import org.testng.asserts.SoftAssert
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Unroll

class StageActivityTest extends ActivityBaseSpec  {

	@Shared
	ClientData client = new ClientData("creditClient")
	@Shared
	ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData("SanityTestDataCity2")
	SoftAssert saTC01 = null


	def "TC00: Launch a URL and Search For SightSeeing addfirst Avilable Item Book and Cancel "(){
		given: "The agent logged in"
		//saTC01 = new SoftAssertHelper()
		loginToApplicaiton(client)
		driver.navigate().refresh()
		when:"Agent search for on Activity in City"
		searchForActivity(activitiesSearchToolData)
		then:"activites PLP Page displayed"
		addFirstAvailableItem(activitiesSearchToolData)

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
		true
		//assign(softAssert ,saTC01)
		bookedItemItineraryPage(activitiesSearchToolData)
		at ItenaryPage
		then:"Cancel the item "
		cancelBookedItem(activitiesSearchToolData)
		sleep(5500)
		verifyCancelledItem(activitiesSearchToolData)

	}
/*
	def assign(SoftAssert org, SoftAssert newSA) {
		org = newSA
		true
	}*/

	/*def "TC01: verify ItinerayPage add traveller deatils and book an item"() {

	}


	def "TC02: Verify Booking confirmation page and book the item"(){


	}

	def "TC03: verify voucher icon of booked item"() {
		given:"item is booked"
		bookedItemItineraryPage()
	}

	def "TC04:Verify Item is cancelled"() {
		given:"item is booked"
		//assign(softAssert ,saTC01)
		sleep(4500)
		verifyCancelledItem(activitiesSearchToolData)
	}*/




}
