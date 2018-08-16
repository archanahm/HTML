package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.multiModuleItineraryDetail

/**
 * Created by kmahas on 2/9/2017.
 *
 * Scenario 56.1
 *
 * NOVA POS - Multple modules within an itinerary - system flow 6 - unavailbe / non-booked items & cancel itinerary

 */
class MuModUnavNonBkdCanItnryTest extends MultipleModuleBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client670")
    //ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("561", "multimoduleitinerarydetail", multiModuleItineraryDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("561", hotelSearchData)

    def "TC00: NOVA POS - Multple modules within an itinerary - system flow 6 - unavailbe / non-booked items & cancel itinerary   "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        loginToApplicaiton(clientData, hotelSearchData, resultData)
        addItemsToItinry(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        itineraryNameUpdate(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData, 2, 2, 0, 0, hotelSearchData.input.children)
        cancelitinerary(hotelSearchData, resultData)
        cancelitinerarylightboxNonBooked(hotelSearchData, resultData)
        //Itinerarydetails(hotelSearchData, resultData,2)
        Itinerarydetails(hotelSearchData, resultData,2,8)
        functionbuttonsNonBooked(hotelSearchData, resultData)
        NonBookedblock(hotelSearchData, resultData)

        Appliedcharge(hotelSearchData, resultData)
        addadditionalitem(hotelSearchData, resultData)
        Item4Hotel(clientData, hotelSearchData, resultData)
        goToItinry(hotelSearchData, resultData)
        Bookitem4(hotelSearchData, resultData)
        cancelitinerary(hotelSearchData, resultData)
        cancelitinerarylightbox(hotelSearchData, resultData)
        Itinerarydetails(hotelSearchData, resultData,1,18)
        functions(hotelSearchData, resultData)
        Cancelled(hotelSearchData, resultData) //functions & cancelled methods both are part of 19 test case
        NonBookedBlock(hotelSearchData, resultData)

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
    def"TC06: Verify cancel itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifycancelitinerary(hotelSearchData, resultData)
    }
    def"TC07: Verify cancel itinerary lightbox Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"

        VerifycancelitinerarylightboxNonBooked(hotelSearchData, resultData)
    }
    def"TC08: Verify Itinerary details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItinerarydetails(hotelSearchData, resultData,2)
    }
    def"TC09: Verify function buttons Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyfunctionbuttonsNonBooked(hotelSearchData, resultData)
    }
    def"TC10: Verify Non-Booked block Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyNonBookedblock(hotelSearchData, resultData)
    }
    def"TC11: Verify Applied charge  Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyAppliedcharge(hotelSearchData, resultData)
    }
    def"TC12: Verify add additional item  Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyaddadditionalitem(hotelSearchData, resultData)
    }
    def"TC13: Verify Item # 4 - Hotel  Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItem4Hotel(hotelSearchData, resultData)
    }

    def"TC14: Verify Go To Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyGoToItinerary(hotelSearchData, resultData)
    }
    def"TC15: Verify Book item # 4 Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyBookitem4(hotelSearchData, resultData)
    }
    def"TC16: Verify cancel itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifycancelitinerary(hotelSearchData, resultData)
    }
    def"TC17: Verify cancel itinerary lightbox Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"

        Verifycancelitinerarylightbox(hotelSearchData, resultData)
    }
    def"TC18: Verify Itinerary details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItinerarydetails(hotelSearchData, resultData,1)
    }
    def"TC19: Verify function buttons Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifyfunctions(hotelSearchData, resultData)
        VerifyCancelled(hotelSearchData, resultData)
    }

    def"TC20: Verify Non-Booked block Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyNonBookedBlock(hotelSearchData, resultData)
    }

}
