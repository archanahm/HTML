package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.multiModuleItineraryDetail

/**
 * Created by kmahas on 10/20/2016.
 *
 * * Scenario 52.1
 *  NOVA POS - Multple modules within an itinerary - system flow 2 (multiRms/multipleItems) & cancel itinerary  (no charge applied)
 */
class MuModlMulRmCanItnryNoChrgTest extends MultipleModuleBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("521", "multimoduleitinerarydetail", multiModuleItineraryDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("521", hotelSearchData)

    def "TC00: NOVA POS - Multple modules within an itinerary - system flow 2 (multiRms/multipleItems) & cancel itinerary  (no charge applied) "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        loginToApplicaiton(clientData, hotelSearchData, resultData)
        addItemsToItineraryMultiRoom(clientData, hotelSearchData, resultData)
        goToItinery(hotelSearchData, resultData)
        itineraryNameUpdate(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,4,0,0,hotelSearchData.input.children)
        bookItemMultiRoom(hotelSearchData, resultData)
        cancelitineraryMultiRoom(hotelSearchData, resultData)
        returntohome(hotelSearchData, resultData)
    }
    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(hotelSearchData, resultData)
    }
    def"TC02: Verify add items to itinerary  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyaddItemsToItineraryMultiRoom(hotelSearchData, resultData)
    }
    def"TC03: Verify Go To Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyGoToItinerary(hotelSearchData, resultData)
    }
    def"TC04: Verify itinerary name update Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyitineraryNameUpdate(hotelSearchData, resultData)
    }
    def"TC05: Verify Traveller Details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyTravellerDetails(hotelSearchData, resultData)
    }
    def"TC06: Verify Book item Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifybookItemMultiRoom(hotelSearchData, resultData)
    }
    def"TC07: Verify cancel itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifycancelitineraryMultiRoom(hotelSearchData, resultData)
    }
    def"TC08: Verify return to home  Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyreturntohome(hotelSearchData, resultData)
    }
}
