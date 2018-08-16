package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import spock.lang.Shared

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil

class UnavailableHotelItemTest extends ItineraryHotelItemBaseSpec{
	@Shared
	public static Map<String, HotelTransferTestResultData>  resultMap = [:]

	ClientData clientData = new ClientData("client664")
	//ClientData clientData = new ClientData("client21")

	DateUtil dateUtil = new DateUtil()
	
	@Shared
	HotelSearchData hotelData = new HotelSearchData("Capitol-MC")
	@Shared
	HotelTransferTestResultData resultData = new HotelTransferTestResultData("Capitol-MC",hotelData)
	
	def "01 : Itinerary Builder   "() {
		given: "User is able to login and present on Hotel tab"

		resultMap.put(hotelData.hotelName, resultData)
		login(clientData)
		addToItinerary(clientData,hotelData, resultData)
		itineraryBuilder(clientData,hotelData, resultData)
	}
	
	def "02 : Verify Results   "() {
	
		given: "User is logged in and present on Search Results"
		verifyresults(hotelData, resultData)
		}
	def "03 : Verify About To Book Screen Results   "() {
		
			given: "User is logged in and present on About to Book Screen"
			verifyAboutToBookScreenresults(hotelData, resultData)
			}
	
	def "04 : Verify Booking Confirmation Screen Results   "() {
		
			given: "User is logged in and present on Booking Confirmation Screen"
			verifyBookingConfirmationScreenresults(hotelData, resultData)
			}
	
	def "05 : Verify Booked Item Screen Results   "() {
		
			given: "User is logged in and present on Booked Items Screen"
			verifyBookedItemsScreenresults(hotelData, resultData)
			}
	
	def "06 : Verify Unavailable Item Screen Results   "() {
		
			given: "User is logged in and present on Cancel Items Screen"
			verifyUnavailableItemsScreenresults(hotelData, resultData)
	}

}
