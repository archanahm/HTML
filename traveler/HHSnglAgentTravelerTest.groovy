package com.kuoni.qa.automation.test.traveler

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.TravelerTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.travelersite


/**
 * Created by mtmaku on 11/21/2016.
 *
 * Scenario - 41.1 : Hotel SingleRoom(Booked/Non-Booked) Agent to Traveler site Validation.
 */

class HHSnglAgentTravelerTest extends TravelerSiteBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    //Client Login
    ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("411", "travelerSite", travelersite)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("GardenPlaza-SITUJ15XMLAuto", hotelSearchData)
    @Shared
    TravelerTestResultData tData = new TravelerTestResultData("GardenPlaza-SITUJ15XMLAuto", hotelSearchData)

    def"TC00: NOVA POS - Share Itinerary / Traveller View - HH - single room - booked / non-booked"(){

        given: "User is logged in and present on Hotel Search"
        loginToApplicaiton(clientData,hotelSearchData, resultData)
        searchItemsAndAddToItinerary(hotelSearchData,resultData)
        goToItinerary(hotelSearchData, resultData)
        itineraryNameUpdate(hotelSearchData, resultData)
        travellerDetails(hotelSearchData,2,2,3,3,hotelSearchData.input.children)
        storeTravelerDetails(hotelSearchData, resultData)
        bookHotelItem()
        storeHotelItems(resultData)
        clickOnShareItinerary(resultData)
        ShareItineraryHeader(resultData)
        ShareItinerarySection1(hotelSearchData,resultData)
        storeShareItineraryValidations(resultData)
        ShareItineraryOverlay(hotelSearchData,resultData,tData)
        sampleTravelerSiteTC(resultData,hotelSearchData,tData)
        travelerTripOverViewDetails(tData)
        storeTravelerSiteDetails(tData)
    }
    def"TC01: Login to Agent Site Verification"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(hotelSearchData, resultData)
    }
    def"TC02: Add items to itinerary Verification"(){

        given: "User is logged in and present on Hotel Search"
        VerifyaddItemsToItinerary(hotelSearchData, resultData)

    }
    def"TC03: Verify Go To Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyGoToItinerary(hotelSearchData, resultData)
    }
    def"TC04: Verify itinerary name update"(){

        given: "User is logged in and present on itinerary page"
        VerifyitineraryNameUpdate(hotelSearchData, resultData)
    }
    def"TC05: Verify Traveller Details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyTravellerDetails(hotelSearchData, resultData)
    }

    def "TC06: Verify BookItem"(){
        given: "User is logged in and present on Itinerary Page"
        verifyBookingItem(hotelSearchData, resultData)
    }

    def "TC07: Verify ShareItinerary"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerary(hotelSearchData, resultData)

    }
    def "TC08: Verify ShareItinerary Header"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItineraryHeader(hotelSearchData, resultData)
    }

    def "TC09: Verify ShareItinerary Section1"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerarySection1(hotelSearchData, resultData)
    }
    def "TC10: Verify ShareItinerary Section2"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerarySection2(hotelSearchData, resultData)
}
    def "TC11: Verify ShareItinerary Section2 ToggleButton1"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerarySection2ToggleButton1(hotelSearchData, resultData)
    }

    def "TC12: Verify ShareItinerary Section2 ToggleButton2"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerarySection2ToggleButton2(hotelSearchData, resultData)
    }
    def "TC13: Verify ShareItinerary Section2 ToggleButton3"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerarySection2ToggleButton3(hotelSearchData, resultData)
    }
    def "TC14: Verify ShareItinerary Section2 ToggleButton4"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerarySection2ToggleButton4(hotelSearchData, resultData)
    }
    def "TC15: Verify BookedHotelItem Against Itinerary"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyBookedHotelItemAgainstItinerary(hotelSearchData, resultData)
    }
    def "TC16: Verify ShareItiBookingTotalRate"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItiBookingTotalRate(hotelSearchData, resultData)
    }
    def "TC17: Verify ShareItinerary Section3"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyShareItinerarySection3(hotelSearchData, resultData)
    }
    def "TC18: Verify Section3 ToggleButton1"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifySection3ToggleButton1(hotelSearchData, resultData)
    }
    def "TC19: Verify Section3 ToggleButton2"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifySection3ToggleButton2(hotelSearchData, resultData)
    }
    def "TC20: Verify Section3 ToggleButton3"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifySection3ToggleButton3(hotelSearchData, resultData)
    }
    def "TC21: Verify NonBookedItemCard Against Itinerary"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyNonBookedItemCardAgainstItinerary(hotelSearchData, resultData)
    }
    def "TC22: Verify BookedAndNonBooked TotalRate"(){
        given: "User is logged in and present on Share Itinerary Page"
        VerifyBNBTotalRate(hotelSearchData, resultData)
    }
    def "TC23: Verify ShareItinerary Overlay"(){
        given: "User is logged in and clicked on shareURL button present on Share Itinerary Page"
        VerifyShareItineraryOverlay(hotelSearchData, resultData,tData)
    }
    def "TC24: Verify Hotel Facilities"(){
        given: "User is on traveler site"
        VerifyHotelFacilities(resultData,tData)
    }

}
