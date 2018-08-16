package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.multiModuleItineraryDetail

/**
 * Created by kmahas on 1/19/2017.
 *
 * Scenario 54.1
 *
 * NOVA POS - Multple modules within an itinerary - system flow 4 & cancel itinerary  (cancel itinerary full charge applied)
 */

class MuModlCanItinryFullChrgTest extends MultipleModuleBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client670")
    //ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("541", "multimoduleitinerarydetail", multiModuleItineraryDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("541", hotelSearchData)

    def "TC00: NOVA POS - Multple modules within an itinerary - system flow 4 & cancel itinerary  (cancel itinerary full charge applied) "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        loginToApplicaiton(clientData, hotelSearchData, resultData)
        addItemsToItinry(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        itineraryNameUpdate(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,2,0,0,hotelSearchData.input.children)
        bookItemFullCharge(hotelSearchData, resultData,0,2,0,1)
        cancelitinerary(hotelSearchData, resultData)
        cancelitinrylightbox(hotelSearchData, resultData)
        Itinerarydetails(hotelSearchData, resultData,0,9)
        functions(hotelSearchData, resultData)

        Cancelled(hotelSearchData, resultData)
        Unavailableblock(hotelSearchData, resultData)
        item1transferitemcard(hotelSearchData, resultData,0)
        item2hotelitemcard(hotelSearchData, resultData,2)
        item3activityitemcard(hotelSearchData, resultData,1)
        AppliedchargeForFullCharge(hotelSearchData, resultData)

    }

    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(hotelSearchData, resultData)
    }
    def"TC02: Verify add items to itinerary  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyaddItemsToItinry(hotelSearchData, resultData)
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
        VerifybookItemFullCharge(hotelSearchData, resultData)
    }
    def"TC07: Verify cancel itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifycancelitinerary(hotelSearchData, resultData)
    }
    def"TC08: Verify cancel itinerary lightbox Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"

        Verifycancelitinrylightbox(hotelSearchData, resultData)
    }
    def"TC09: Verify Itinerary details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItinerarydetails(hotelSearchData, resultData,0)
    }
    def"TC10: Verify function buttons Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyfunctions(hotelSearchData, resultData)
    }
    def"TC11: Verify Cancelled Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyCancelled(hotelSearchData, resultData)
    }
    def"TC12: Verify Unavailable block Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyUnavailableblock(hotelSearchData, resultData)
    }
    def"TC13: Verify transfer item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //Verifyitem1transferitemcard(hotelSearchData, resultData)
        Verifyitem1transferitemcard(hotelSearchData, resultData)
    }
    def"TC14: Verify item #2 - hotel item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyitem2hotelitemcard(hotelSearchData, resultData)
    }

    def"TC15: Verify item #3 - activity item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyitem3activityitemcard(hotelSearchData, resultData)
    }
    def"TC16: Verify Applied charge  Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyAppliedchargeForFullCharge(hotelSearchData, resultData)
    }
}
