package com.kuoni.qa.automation.test.search.booking.hotel

import spock.lang.Ignore

import com.kuoni.qa.automation.page.ItenaryPage


class BookedItemTest extends HotelBookingBaseSpec{



	def "TC1 ITINERARY Booked item hotel card"(){
		println "\n ***TestName: TC1 ITINERARY: Booked item hotel card"
		given:"that the travel agent is on Itinerary page"
		when:"that the travel agent has clicked on confirm booking button"
		addItineraryAndGotToItineryPageSearchResults()
		and:"they click on book button on one of the items"
		at ItenaryPage
		enterTravellerDetailsWithoutSave("Mrs", "test", "AutoUser" ,0)
		clickAddTravellersButton()
		enterTravellerDetails("Mr" , "test" , "AutoUser1", 1)
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
		def bookId=getBookingId(0)
		then:"the booking Item is displayed"
		softAssert.assertTrue(bookId.length()>0, "\n Booking Id is not correct on the booking card!!")
		softAssert.assertTrue(verifyBookingStatus("Confirmed", 0), "\n Booking status not correct on Booking item")
		if (!platform.toString().contains("mobile"))
			softAssert.assertTrue(verifyBookingItemContentImgDisplayed(0), "\n Image not displayed on booking item")
		softAssert.assertTrue(verifyBookingItemPropName(hotelName, 0), "\n Hotel name not correct in booked item")
		softAssert.assertTrue(verifyBookedItemStarRating(actualRating, 0), "\n Hotel rating not correct on booked item")
		softAssert.assertTrue(verifyBookedItemRoomAndPax(roomname, 0), "\n Room name not correct on booked item")
		softAssert.assertTrue(verifyBookedItemRoomAndPax("2PAX", 0), "\n Pax not correct on booked item")
		softAssert.assertTrue(verifyBookedItemRoomAndPax(mealbasis, 0), "\n Meal basis not correct on booked item")
		softAssert.assertTrue(verifyCancellationLinkDisplayed(0), "\n Cancellation link not displayed in booked item")
		softAssert.assertTrue(verifyCheckInDuration(dur, 0), "\n Duration not correct on booked item")
		softAssert.assertTrue(getBookedItemAmount(0).equals(expPrice), "\n Amount not displayed on booked item , Expected: $expPrice")
		softAssert.assertTrue(verifyBookedItemCurrency(clientData.currency, 0), "\n Currency not correct on booke ditem!!")
		
		when :"Search bOoking id in CBS"		
		def bookingGrossAmount = aS400Util.getBookingAmount(clientData.siteId ,bookId)
		def bookingStatus = aS400Util.getBookingStatus(clientData.siteId ,bookId).toString().trim()
		then: "Booking should be available in CBS"
		println "booking status : " + bookingStatus
		println "booking gross amount: " + bookingGrossAmount		
		softAssert.assertTrue(bookingStatus.equals("C"), "\n Booking status not correct in CBS")
		softAssert.assertTrue(bookingGrossAmount.equals(expPrice.toString()), "\n Booking groos not correct in CBS, expected:"+expPrice.toString().replace(".",""))

	}
}
