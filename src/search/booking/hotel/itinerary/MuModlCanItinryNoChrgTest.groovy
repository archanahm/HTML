package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.multiModuleItineraryDetail

/**
 * Created by kmahas on 10/7/2016.
 * Scenario 51.1
 *
 * NOVA POS - Multple modules within an itinerary - single hotel room / TN / RS - system flow 1 - cancel itinerary (no charge)
 */
class MuModlCanItinryNoChrgTest extends MultipleModuleBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    //ClientData clientData = new ClientData("client664")
    ClientData clientData = new ClientData("creditClient")
    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("511", "multimoduleitinerarydetail", multiModuleItineraryDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("511", hotelSearchData)

    def "TC00: NOVA POS - Multple modules within an itinerary - single hotel room / TN / RS - system flow 1 - cancel itinerary (no charge) "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        loginToApplicaiton(clientData, hotelSearchData, resultData)
        addItemsToItinerary(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        itineraryNameUpdate(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,2,0,0,hotelSearchData.input.children)
        bookItem(hotelSearchData, resultData,0,1,2,3,4)
        closeItinerary(hotelSearchData, resultData)
        Searchtheitinerary(hotelSearchData, resultData)
        cancelitinerary(hotelSearchData, resultData)
        cancelitinerarylightbox(hotelSearchData, resultData)

        Itinerarydetails(hotelSearchData, resultData,0,11)
        functionbuttons(hotelSearchData, resultData)
        Cancelled(hotelSearchData, resultData)
        Unavailableblock(hotelSearchData, resultData)
        //item1transferitemcard(hotelSearchData, resultData,1)
        item1transferitemcard(hotelSearchData, resultData,0)
        item2hotelitemcard(hotelSearchData, resultData,2)
        item3activityitemcard(hotelSearchData, resultData,3)
        item4transferitemcard(hotelSearchData, resultData,4)
        Appliedcharge(hotelSearchData, resultData)
        returntohome(hotelSearchData, resultData)


    }
    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(hotelSearchData, resultData)
    }

    def"TC02: Verify add items to itinerary  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyaddItemsToItinerary(hotelSearchData, resultData)
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
        VerifybookItem(hotelSearchData, resultData)
    }
    def"TC07: Verify Close Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyCloseItinerary(hotelSearchData, resultData)
    }
    def"TC08: Verify Search the itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifySearchtheitinerary(hotelSearchData, resultData)
    }
    def"TC09: Verify cancel itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifycancelitinerary(hotelSearchData, resultData)
    }
    def"TC10: Verify cancel itinerary lightbox Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifycancelitinerarylightbox(hotelSearchData, resultData)
    }
    def"TC11: Verify Itinerary details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItinerarydetails(hotelSearchData, resultData,0)
    }
    def"TC12: Verify function buttons Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyfunctionbuttons(hotelSearchData, resultData)
    }
    def"TC13: Verify Cancelled Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyCancelled(hotelSearchData, resultData)
    }
    def"TC14: Verify Unavailable block Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyUnavailableblock(hotelSearchData, resultData)
    }
    def"TC15: Verify item #1 - transfer item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyitem1transferitemcard(hotelSearchData, resultData)
    }
    def"TC16: Verify item #2 - hotel item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyitem2hotelitemcard(hotelSearchData, resultData)
    }

    def"TC17: Verify item #3 - activity item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyitem3activityitemcard(hotelSearchData, resultData)
    }
    def"TC18: Verify item #4 - transfer item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyitem4transferitemcard(hotelSearchData, resultData)
    }
    def"TC19: Verify Applied charge  Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyAppliedcharge(hotelSearchData, resultData)
    }
    def"TC20: Verify return to home  Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyreturntohome(hotelSearchData, resultData)
    }

}
