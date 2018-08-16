package com.kuoni.qa.automation.test.itinerarySearch

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.ItinerarySearchTestData
import com.kuoni.qa.automation.helper.ItinerarySearchTestResultData
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itinerarySearch

/**
 * Created by kmahas on 1/30/2018.
 *
 * 61.05
 * Credit - Itinerary Search - Quick - Itinerary ID + Booking ID + traveller Name - View all bookings
 *
 */
class CreditItinryQuickSearchTest extends ItinerarySearchBaseSpec{

    //Client Login
    @Shared
    ClientData clientData = new ClientData("client87202")
    @Shared
    public static Map<String, ItinerarySearchTestResultData> resultMap = [:]
    @Shared
    ItinerarySearchTestData itinerarySearchData = new ItinerarySearchTestData("6105", "itinerarySearch", itinerarySearch)
    @Shared
    ItinerarySearchTestResultData resultData = new ItinerarySearchTestResultData("6105", itinerarySearchData)


    def "TC00: Credit - Itinerary Search - Quick - Itinerary ID + Booking ID + traveller Name - View all bookings "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(itinerarySearchData.scenarioNum, resultData)
        login(clientData)
        Accesstoagentsite(defaultData, resultData)
        ItineraryIDFullLegacybooking(itinerarySearchData.input.itineraryIDFullLegacyBooking,resultData)
        ItineraryIDFullNovabooking(itinerarySearchData.input.itineraryIDFullNovaBooking,resultData)
        ItinryIDFullNovabooking(itinerarySearchData.input.itinryIDFullNovaBooking,resultData)
        ItineraryIDPartialLegacyBooking(itinerarySearchData.input.itineraryIDPartialLegacyBooking,itinerarySearchData,resultData)
        ItinryIDPartialLegacyBooking(itinerarySearchData.input.itinryIDPartialLegacyBkng,itinerarySearchData,resultData)
        ItineraryIDPartialNovaBooking(itinerarySearchData.input.itineraryIDPartialNovaBooking,itinerarySearchData,resultData)
        ItinryIDPartialNovaBooking(itinerarySearchData.input.itinIDPartialNovaBooking,itinerarySearchData,resultData)
        ItinryIDPartlNovaBooking(itinerarySearchData.input.itinryIDPartlNovaBooking,itinerarySearchData,resultData)
        ItinryIDPartlNovaBookng(itinerarySearchData.input.itinryIDPartlNovaBookng,itinerarySearchData,resultData)

        BookingIDfulllegacyAndNovabooking(itinerarySearchData.input.bookingIdFullLegacyAndNovaBooking,resultData)
        BookingIDPartialLegacyAndNovabooking(itinerarySearchData.input.bookingIDPartialLegacyAndNovaBooking,itinerarySearchData,resultData)
        BookingIDPartalLegacyAndNovabooking(itinerarySearchData.input.bookingIDPartlLegacyAndNovaBooking,itinerarySearchData,resultData)
        TravellernamefulllegacyAndNovabooking(itinerarySearchData.input.travlrNameFullLegacyAndNovaBooking,itinerarySearchData,resultData)
        TravellernamepartiallegacyAndNovabooking(itinerarySearchData.input.travlrNamePartialLegacyAndNovaBooking,itinerarySearchData,resultData)
        TravlrnamepartiallegacyAndNovabooking(itinerarySearchData.input.travlrNamePartlLegacyAndNovaBooking,itinerarySearchData,resultData)
        TravlrNamePartalLegacyAndNovabooking(itinerarySearchData.input.travlerNameParLegacyAndNovBkng,itinerarySearchData,resultData)
        TravlrNamePartalLegacyAndNovabkng(itinerarySearchData.input.travlerNameParLegacyNovBkng,itinerarySearchData,resultData)
        TravellernamepartiallegacyAndNovabookingIgnoreSiteId(itinerarySearchData.input.travlerNameParLegacyNovBkngIgnoreSiteId,resultData)


    }
    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyAccesstoagentsite(defaultData, resultData)
    }
    def"TC02: Verify Itinerary ID - full Legacy booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItineraryIDFullLegacybooking(defaultData, itinerarySearchData,resultData)
    }
    def"TC03: Verify Itinerary ID - full Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItineraryIDFullNovabooking(defaultData, itinerarySearchData,resultData)
    }
    def"TC04: Verify Itinerary ID - full Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItinryIDFullNovabooking(defaultData, itinerarySearchData,resultData)
    }
    def"TC05: Verify Itinerary ID - Partial Legacy booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItineraryIDPartialLegacyBooking(defaultData, itinerarySearchData,resultData)
    }

    def"TC06: Verify Itinerary ID - Partial Legacy booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItinryIDPartialLegacyBooking(defaultData, itinerarySearchData,resultData)
    }

    def"TC07: Verify Itinerary ID - Partial Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItineraryIDPartialNovaBooking(defaultData, itinerarySearchData,resultData)
    }

    def"TC08: Verify Itinerary ID - Partial Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItinryIDPartialNovaBooking(defaultData, itinerarySearchData,resultData)
    }


    def"TC09: Verify Itinerary ID - Partial Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItinryIDPartlNovaBooking(defaultData, itinerarySearchData,resultData)
    }

    def"TC10: Verify Itinerary ID - partial legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItinryIDPartlNovaBookng(defaultData, itinerarySearchData,resultData)
    }

    def"TC11: Verify Booking ID - full legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyBookingIDfulllegacyAndNovabooking(defaultData, itinerarySearchData,resultData)
    }

    def"TC12: Verify Booking ID - Partial legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyBookingIDPartialLegacyAndNovabooking(defaultData, itinerarySearchData,resultData)
    }
    def"TC13: Verify Booking ID - Partial legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyBookingIDPartalLegacyAndNovabooking(defaultData, itinerarySearchData,resultData)
    }

    def"TC14: Verify Traveller name - full legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyTravellernamefulllegacyAndNovabooking(defaultData, itinerarySearchData,resultData)
    }
    def"TC15: Verify Traveller name - Partial legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyTravellernamepartiallegacyAndNovabooking(defaultData, itinerarySearchData,resultData)
    }
    def"TC16: Verify Traveller name - Partial legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyTravlrnamepartiallegacyAndNovabooking(defaultData, itinerarySearchData,resultData)
    }

    def"TC17: Verify Traveller name - Partial legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyTravlrNamePartalLegacyAndNovabooking(defaultData, resultData)
    }

    def"TC18: Verify Traveller name - Partial legacy & Nova booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyTravlrNamePartalLegacyAndNovabkng(defaultData, resultData)
    }

    def"TC19: Verify Itinerary ID - full Legacy booking Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyTravellernamepartiallegacyAndNovabookingIgnoreSiteId(defaultData, itinerarySearchData,resultData)
    }
}
