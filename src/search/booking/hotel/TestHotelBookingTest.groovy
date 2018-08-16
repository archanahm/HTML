package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import spock.lang.Shared

/**
 * Created by mtmaku on 7/25/2017.
 */
class TestHotelBookingTest extends SanityHotelBookingBaseSpec {

    @Shared
    ClientData client = new ClientData("creditClient")
    @Shared
    CitySearchData citySearchData = new CitySearchData("sanityData")
    @Shared
    public static Map<String,HotelTransferTestResultData> resultMap = [:]
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData(citySearchData)

    def "TC0 Hotel Booking Multi room"() {
        resultMap.put(citySearchData.city,resultData)
        given: "that the travel agent is on home page"
        when: "search for hotel"
        searchHotelAndAddToItinerary(citySearchData,resultData)
        then: "search results returned."
        when: "user clicks book an item"
        enterTravelerDetails(citySearchData,0,resultData)
        bookAnItem(citySearchData,0, resultData)
        then: "Hotel item should book successfully."
        amendAnItem(0,resultData,citySearchData)
        amendAnItem(1,resultData,citySearchData)
        when: "user click on cancel items"
        cancelAnItem(0,resultData)
        cancelAnItem(0,resultData)
        resultData.hotel.itineraryPage.cancelStatusFirstItem = getBookingStatus(0)
        resultData.hotel.itineraryPage.cancelStatusSecondItem = getBookingStatus(1)
        then: "items should be cancelled successfully"
    }

    def "TC01: verifySearchHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        verifySearchHotel(resultData)
    }

    def "TC:02 verifyHotelBookedItemInItinerary"(){
        given: "User is on Itinerary page."
        verifyBookedItem(resultData)
    }

    def "TC:03 verifyAmendedItem"(){
        given: "User is on itinerary page."
        verifyAmendedItem(resultData)
    }
    def "TC04: verifyCancelledItem"(){
        given: "User is on itinerary page."
        assertionEquals(resultData.hotel.itineraryPage.cancelStatusFirstItem,"Cancelled","Item1 is not cancelled")
        assertionEquals(resultData.hotel.itineraryPage.cancelStatusSecondItem,"Cancelled","Item2 is not cancelled")

    }

}
