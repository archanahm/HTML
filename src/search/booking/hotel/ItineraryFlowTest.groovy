package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.hotel.ItineraryBuilderPageBooking
import com.kuoni.qa.automation.page.pricing.HotelInformationPagePricing
import com.kuoni.qa.automation.page.pricing.HotelSearchResultsPricingPage
import com.kuoni.qa.automation.page.pricing.ItenaryPagePricing
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.page.transfers.TransferSearchPage
import com.kuoni.qa.automation.page.transfers.TransferSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import static org.junit.Assert.*

/**
 * Created by Joseph Sebastian on 14/03/2016.
 */
class ItineraryFlowTest extends BaseSpec {

    def "01 Login and search"() {
        given: "User is able to login and go to transfers view"
        ClientData clientData = new ClientData("client625")
        login(clientData)
        at HotelSearchPage
        HotelSearchData searchData = new HotelSearchData("itineraryFlow.singleRoom")
        HotelTransferTestResultData resultData = new HotelTransferTestResultData("", searchData)
        Map step1 = searchData.input.step1
        when:"first hotel is searched and added"
        fillHotelSearchFormAndSubmit(step1.destination, step1.destination, step1.checkInDays, step1.checkOutDays, step1.pax, step1.children)
        at HotelSearchResultsPricingPage
        int hotelPOS = getHotelPosition(step1.hotel)
        assertTrue("Unable to fine hotel ${step1.hotel} in search results", hotelPOS >= 0)
        clickOnAddToItinerary(hotelPOS, 0)
        try {
            Map data = getItineraryData(0)
        } catch (Exception e) {
            assertNull("Failed to get Itineraray card details after \"Add to Itinerary\"", e)
        }
        at ItineraryBuilderPageBooking
        clickOnGotoItenaryButton()
        at ItenaryPagePricing
        int pax =  step1.pax.toInteger()
        enterPassengerDetails(pax, "")
//        sleep(1000)
        enterChildDetails(step1.children, pax)
        sleep(2000)
        clickonSaveButton()
        at ItineraryBuilderPageBooking
        Map iteneraryDataMap = getItineraryDataNonBooked(hotelPOS)
        clickBookButton(0)
        sleep(1000)

        verifyTandCDisplayedBookingLightBox()
        clickCloseOnRemoveItenary()

        iteneraryDataMap = getItinerayData(hotelPOS)
        for (i in 0..3)
            clickOnTravellerTandCBookingLightBox(i)
        clickConfirmBooking()
        null == waitTillConformationPage()
        String bookingStatus = getBookingStatus()
        println("Booking Status :- $bookingStatus")

        iteneraryDataMap = getItinerayData(hotelPOS)


        and:"adding a transer"
        clickOnAddTransferIcon()
        at TransferSearchPage
        Map step2 = searchData.input.step2
        enterPickupInput(step2.pickup)
        sleep(1000)
        selectFromAutoSuggestPickUp(step2.pickup)
        enterDropoffInput(step2.dropoff)
        sleep(1000)
        selectFromAutoSuggestDropOff(step2.dropoff)

        selectDateCalender(step2.checkInDays.toInteger())

        enterPaxInput(step2.pax, "0")

        clickFindButton()

        sleep(5000)

        at TransferSearchResultsPage
        clickOnAddToItenary(0)

        at ItineraryBuilderPageBooking
        data = getItineraryData(0)
        data = getItineraryData(1)

        at ItineraryBuilderPageBooking

        //click on Go To Itineary button
        clickOnGotoItenaryButton()

        iteneraryDataMap = getItineraryDataBooked(0)
        iteneraryDataMap = getItineraryDataNonBooked(0)

        at ItenaryPageItems
        clickBookButton(0)
        sleep(5000)

        at ItineraryBuilderPageBooking
        iteneraryDataMap = getItineraryDataBooked(0)
        iteneraryDataMap = getItineraryDataBooked(1)


        and: "adding a DI item"
        at ItenaryPagePricing
        clickOnAddHotelIcon()

        at HotelSearchPage
        Map step3 = searchData.input.step3
        fillHotelSearchFormAndSubmit(step3.destination, step3.destination, step3.checkInDays, step3.checkOutDays, step3.pax, [])
        at HotelSearchResultsPricingPage
        hotelPOS = getHotelPosition(step3.hotel)
        clickOnAddToItinerary(hotelPOS, 0)
        at ItineraryBuilderPageBooking
        data = getItineraryData(0)
        data = getItineraryData(1)
        data = getItineraryData(2)

        at ItineraryBuilderPageBooking

        //click on Go To Itineary button
        clickOnGotoItenaryButton()

        iteneraryDataMap = getItineraryDataBooked(0)
        iteneraryDataMap = getItineraryDataBooked(1)
        iteneraryDataMap = getItineraryDataNonBooked(0)

        at ItenaryPageItems
        clickBookButton(0)
        sleep(5000)

        at ItineraryBuilderPageBooking
        iteneraryDataMap = getItineraryDataBooked(0)
        iteneraryDataMap = getItineraryDataBooked(1)
        iteneraryDataMap = getItineraryDataBooked(2)
        then : "verify results"
    }
}
