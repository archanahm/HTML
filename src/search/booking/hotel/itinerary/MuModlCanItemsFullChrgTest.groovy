package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.multiModuleItineraryDetail

/**
 * Created by kmahas on 10/26/2016.
 *
 * Scenario 53.1
 *
 * NOVA POS - Multple modules within an itinerary - system flow 3 & cancel items  (cancel items full charge applied)

 */
class MuModlCanItemsFullChrgTest extends MultipleModuleBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")
    //ClientData clientData = new ClientData("client21")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("531", "multimoduleitinerarydetail", multiModuleItineraryDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("531", hotelSearchData)

    def "TC00: NOVA POS - Multple modules within an itinerary - system flow 3 & cancel items  (cancel items full charge applied) "() {
        given: "User is able to login and present on Hotel tab"

         resultMap.put(hotelSearchData.hotelName, resultData)
        loginToApplicaiton(clientData, hotelSearchData, resultData)
        addItemsToItinry(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        itineraryNameUpdate(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,2,0,0,hotelSearchData.input.children)
        bookItemFullCharge(hotelSearchData, resultData,0,1,0,2)
        cancelItem(hotelSearchData, resultData,1,1)
        cancelitemlightbox(hotelSearchData, resultData)
        cancelled1(hotelSearchData, resultData)
        Unavailableblock(hotelSearchData, resultData)

        transferitemcard(hotelSearchData, resultData,3)
        Appliedcharges(hotelSearchData, resultData,1)

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
    def"TC07: Verify cancel item #1 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifycancelItem(hotelSearchData, resultData,1)
    }
    def"TC08: Verify cancel item lightbox Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifycancelitemlightbox(hotelSearchData, resultData)
    }
    def"TC09: Verify Cancelled # 1 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        Verifycancelled1(hotelSearchData, resultData)
    }
    def"TC10: Verify Unavailable block Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyUnavailableblock(hotelSearchData, resultData)
    }

    def"TC11: Verify transfer item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //Verifytransferitemcard(hotelSearchData, resultData)
        Verifyitem1transferitemcard(hotelSearchData, resultData)
    }
    def"TC12: Verify Applied charge Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC13: Verify cancel item # 2 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC14: Verify cancel item lightbox Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC15: Verify link back to PDP Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC16: Verify cancelled item #2 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC17: Verify hotel item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC18: Verify Applied charge 2 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC19: Verify cancel item # 3 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC20: Verify cancel item lightbox Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC21: Verify link back to PDP Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC22: Verify cancelled # 3 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC23: Verify activity item card Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }
    def"TC24: Verify Applied charge 2 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyAppliedcharges(hotelSearchData, resultData)
    }

}
