package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itineraryTravellerDetail

/**
 * Created by kmahas on 8/22/2016.
 *
 * 13.7 Scenario - covering 2nd part
 *
 * NOVA POS - Itinerary Page - amend hotel occupancy - name change not allowed - single&multiple room -
 */
class AmndNamChngNAPart2Test extends AmndOccpBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("SuiteNovotel", "itinerarytravellerdetail", itineraryTravellerDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("SuiteNovotel", hotelSearchData)

    def "TC00: NOVA POS - Itinerary Page - amend hotel occupancy - name change not allowed - single&multiple room "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        login(clientData)

        addItinerary(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,2,0,0,hotelSearchData.input.addchildren)
        bookHotelItem(hotelSearchData, resultData)
        amendOccupancy(hotelSearchData, resultData,1)
        abtToAmndDtAddASpecialRemarkorComment(hotelSearchData, resultData)
    }

    def"TC01: Verify Create new itinerary - 1st item Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        Verifyadditinerary(hotelSearchData, resultData)
    }

    def"TC02: Verify Go To Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyGoToItinerary(hotelSearchData, resultData)
    }

    def"TC03: Verify Traveller Details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyTravellerDetails(hotelSearchData, resultData)
    }

    def"TC04: Verify Book the Hotel Item Actual & Expected Results"(){

        given: "User is logged in and present on Booking Confirmed"
        VerifyBookingConfirmDetails(hotelSearchData, resultData)
    }
    def"TC05: Verify name change Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyNameChange(hotelSearchData, resultData)
    }
    def"TC06: Verify About To Amend Add A Special Remark or Comment Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyaboutToAmendAddASpecialRemarkorComment(hotelSearchData, resultData)
    }

}
