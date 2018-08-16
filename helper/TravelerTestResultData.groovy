package com.kuoni.qa.automation.helper

/**
 * Created by mtmaku on 12/12/2016.
 */
class TravelerTestResultData {

    Map traveler = [:]
    String hotelName
    HotelSearchData hotelData
    TravelerTestResultData(String hotelName, HotelSearchData hotelData) {
        traveler.viewMyItinerary = [:]
        this.hotelName = hotelName
        this.hotelData = hotelData
    }
}
