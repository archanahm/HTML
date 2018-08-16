package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import spock.lang.Shared

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.util.DateUtil

class ItineraryAddItemsAndRemove  extends ItineraryHotelItemBaseSpec{
	@Shared
	public static Map<String, HotelTransferTestResultData>  resultMap = [:]
	@Shared
	ClientData clientData = new ClientData("client664")
	
	DateUtil dateUtil = new DateUtil()
	
	
	@Shared
	HotelSearchData hotelDataFirstItem =new HotelSearchData("Cambridge-SIT")
	@Shared
	HotelTransferTestResultData resultDataFirstItem =new HotelTransferTestResultData("Cambridge-SIT",hotelDataFirstItem)
		
	def "01 : Login And Add To Itinerary   "() {
		given: "User is logged in and present on Hotel tab"
						
		resultMap.put(hotelDataFirstItem.hotelName, resultDataFirstItem)
		login(clientData)
		at HotelSearchPage
		addToItinerary(clientData,hotelDataFirstItem, resultDataFirstItem)
		
		HotelSearchData hotelDataSecondItem=new HotelSearchData("Lincoln-SITUJ19XMLAuto")
		HotelTransferTestResultData resultDataSecondItem=new HotelTransferTestResultData("Lincoln-SITUJ19XMLAuto",hotelDataSecondItem)
		resultMap.put(hotelDataSecondItem.hotelName, resultDataSecondItem)
		addToItinerary(clientData,hotelDataSecondItem, resultDataSecondItem)
		
		HotelSearchData hotelDataThirdItem=new HotelSearchData("LeesBoutique-SITUJ15XMLAuto")
		HotelTransferTestResultData resultDataThirdItem=new HotelTransferTestResultData("LeesBoutique-SITUJ15XMLAuto",hotelDataThirdItem)
		resultMap.put(hotelDataThirdItem.hotelName, resultDataThirdItem)
		addToItinerary(clientData,hotelDataThirdItem, resultDataThirdItem)
		
		HotelSearchData hotelDataFourthItem=new HotelSearchData("Capitol-MC")
		HotelTransferTestResultData resultDataFourthItem=new HotelTransferTestResultData("Capitol-MC",hotelDataFourthItem)
		resultMap.put(hotelDataFourthItem.hotelName, resultDataFourthItem)
		addToItinerary(clientData,hotelDataFourthItem, resultDataFourthItem)


		itineraryBuild(clientData,hotelDataFirstItem, resultDataFirstItem)
	}
	
	
	def "02 : Verify Results   "() {
	
		given: "User is logged in and present on Search Results"
		verifyresults(hotelDataFirstItem, resultDataFirstItem)
		}
	/*
	def "03 : Verify About To Book Screen Results   "() {
		
			given: "User is logged in and present on About to Book Screen"
			verifyAboutToBookScreenresults(hotelDataFirstItem, resultDataFirstItem)
			}
	
	def "04 : Verify Booking Confirmation Screen Results   "() {
		
			given: "User is logged in and present on Booking Confirmation Screen"
			verifyBookingConfirmationScreenresults(hotelDataFirstItem, resultDataFirstItem)
			}
	
	def "05 : Verify Booked Item Screen Results   "() {
		
			given: "User is logged in and present on Booked Items Screen"
			verifyBookedItemsScreenresults(hotelDataFirstItem, resultDataFirstItem)
			}
	*/
	def "06 : Verify Remove Item Screen Results   "() {
		
			given: "User is logged in and present on Remove Items Screen"
			verifyRemoveItemsScreenresults(hotelDataFirstItem, resultDataFirstItem)
	}


}
