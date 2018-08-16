package com.kuoni.qa.automation.helper


class HotelTransferTestResultData {
    Map hotel = [:]
	Map transfer =[:]
	Map activity=[:]
    String hotelName
    HotelSearchData hotelData
	CitySearchData cityData
	PropertySearchData propertySearchData
	//ActivitiesSearchToolData activitiesSearchToolData

    HotelTransferTestResultData(String hotelName, HotelSearchData hotelData) {
        hotel.activity=[:]

		hotel.search=[:]
		hotel.searchResults = [:]
		hotel.searchResults.priceAndCurrency=[:]
		hotel.searchResults.price=[:]
        hotel.searchResults.itineraryBuilder = [:]
        hotel.pagination = [:]
        hotel.itineraryPage = [:]
		transfer.itineraryPage = [:]
		hotel.itineraryPage.bookedItems = [:]
		hotel.itineraryPage.addComntOrSpclReq = [:]
		hotel.itineraryPage.multiRoomBooking = [:]
		hotel.itineraryPage.multiRoomBooking.travellers = [:]
		hotel.itineraryPage.multiRoomBooking.expectedTravellers = [:]
		hotel.itineraryPage.multiRoomBooking.travellers.chkBoxStatus=[:]
		hotel.itineraryPage.multiRoomBooking.travellers.expectedChkBoxStatus=[:]
        hotel.confirmationPage = [:]
		hotel.confirmationPage.travellers=[:]
		hotel.confirmationPage.multiroomBooking=[:]
		hotel.itineraryPage.multiRoomBooking.bookedItems=[:]
		hotel.shareItineraryPage = [:]
		hotel.amendBooking=[:]
		hotel.amendBooking.occupTab=[:]
		hotel.amendBooking.occupTab.currntDetails=[:]
		hotel.amendBooking.occupTab.plzSelect=[:]
		hotel.amendBooking.occupTab.changeTo=[:]
		hotel.amendBooking.aboutToAmend=[:]
		hotel.amendBooking.bookingConf=[:]
		hotel.amendBooking.itineraryAfterReduce=[:]
        hotel.removeItemPage = [:]
		hotel.removeItemPage.multiRoomBooking = [:]
		hotel.removeItemPage.multiRoomBooking.cancelItems=[:]
		hotel.removeItemPage.cancelledItems = [:]
        hotel.carouselVerify = [:]
		hotel.unavailableAndCancelItems=[:]
		transfer.unavailableAndCancelItems=[:]
		activity.unavailableAndCancelItems=[:]
		activity.activityPlaceHolders=[:]
		activity.NumOfAutoSuggestion=[:]
        this.hotelName = hotelName
        this.hotelData = hotelData
    }
	HotelTransferTestResultData(CitySearchData cityData) {
		hotel.search=[:]
		hotel.searchResults = [:]
		hotel.itineraryPage = [:]
		activity.itineraryPage = [:]
		activity.searchResults = [:]
		hotel.itineraryOverlay = [:]
		this.cityData = cityData
	}
	HotelTransferTestResultData(PropertySearchData propertySearchData){
		hotel.search=[:]
		hotel.searchResults = [:]
		hotel.hotelInfo = [:]
		this.propertySearchData = propertySearchData
	}

	HotelTransferTestResultData(ActivitiesSearchToolData activitiesSearchToolData){
		hotel.activity=[:]
		activity.activityDeatilsPLP =[:]
		activity.activityCancelOverlayPLP =[:]
		activity.activityPDPPagedetails =[:]
		activity.activitySearchTabDefaultValues =[:]
		activity.unavailableAndCancelItems=[:]
		activity.NumOfAutoSuggestion=[:]
		activity.activityFirstAvilableNumPLP = [:]
		activity.activityPriceList =[:]
		hotel.search=[:]
		hotel.searchResults = [:]
		hotel.hotelInfo = [:]
		this.propertySearchData = propertySearchData
	}
}
