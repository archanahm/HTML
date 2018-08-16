package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.spock.annotation.Sanity
import spock.lang.Shared

/**
 * Created by mtmaku on 8/8/2017.
 */
class TestHotelCanChrgApplyTest extends SanityHotelBookingBaseSpec {
    @Shared
    ClientData client = new ClientData("creditClient")
    @Shared
    CitySearchData citySearchData = new CitySearchData("sanityCancellationChargesData")
    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData(citySearchData)

    def "TC00 Hotel Booking Multi room"() {
        resultMap.put(citySearchData.city, resultData)
        given: "that the travel agent is on home page"
        when: "search for hotel"
        login(client)
        fillHotelSearchFormAndSubmit(citySearchData.input.cityText.toString(), citySearchData.input.cityTypeText.toString(), citySearchData.input.checkInDaysToAdd.toString(), citySearchData.input.checkOutDaysToAdd.toString(), citySearchData.input.noOfPax.toString(), citySearchData.input.child)
        at HotelSearchResultsPage
        resultData.hotel.searchResults.hotelsResultStatus = searchResultsReturned()
        int index = getAddToItineraryCancellationLinkIndex(citySearchData.expected.cancellationLink, citySearchData.expected.statusTxt)
        resultData.hotel.cancellationLink = getCancellationLinkText(index, 0)
        clickAddToItinry(citySearchData.expected.cancellationLink, citySearchData.expected.statusTxt)
        bookSingleRoomItem(citySearchData, 0, resultData)
        resultData.hotel.itineraryPage.bookingStatus = getBookingStatus(0)
        resultData.hotel.itineraryPage.afterBookingPrice = getItenaryPriceInBookedItem(1)
        resultData.hotel.itineraryPage.actualCancellationLink = getCancellationText()
        clickOnItemCancllationLink(0)
        resultData.hotel.itineraryPage.cancellationChargesAfterBooking = getCancellationChargesInOverlay()
        resultData.hotel.itineraryPage.amendChargesAfterBooking = getAmendChargesTxtInOverlay()
        then: "items should be cancelled successfully"

    }
    def "TC01: verifySearchHotelAndAddtoItinerary"() {
        given: "User searches for hotel"
        verifySearchHotel(resultData)
    }
    def "TC:02 verifyHotelBookedItemInItinerary"(){
        given: "User is on Itinerary page."
        assertionEquals(resultData.hotel.itineraryPage.bookingStatus,"Confirmed","Hotel item not booked.")
        assertionEquals(resultData.hotel.itineraryPage.price,resultData.hotel.itineraryPage.afterBookingPrice,"Prices are not equal before booking and after booking.")
        assertionEquals(resultData.hotel.cancellationLink,resultData.hotel.itineraryPage.cancellationLink,"Cancellation link not matched against Hotel PLP page.")
        assertionEquals(resultData.hotel.itineraryPage.cancellationLink,resultData.hotel.itineraryPage.actualCancellationLink,"Cancellation Fee Apply is not matching before booking after booking")

    }
    def "TC03: verifyCancellationCharges"(){
        given: "user is on itineary page"
        assertList( resultData.hotel.itineraryPage.cancellationChargesBeforeBooking,resultData.hotel.itineraryPage.cancellationChargesAfterBooking,"Cancellation charges amount not matching.")
        assertList( resultData.hotel.itineraryPage.amendCharges,resultData.hotel.itineraryPage.amendChargesAfterBooking,"Amend charges not matching.")
    }

}
