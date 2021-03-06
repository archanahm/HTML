package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.multiModuleItineraryDetail

/**
 * Created by kmahas on 2/10/2017.
 *
 * Scenario 57.1
 *
 * NOVA POS - Multple modules within an itinerary - system flow 7 - amending traveller name on traveller details section after traveller assigned
 *
 */
class MuModAmndTrvlAftTrvlAsgndTest extends MultipleModuleBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    //ClientData clientData = new ClientData("client670")
    ClientData clientData = new ClientData("client662")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("571", "multimoduleitinerarydetail", multiModuleItineraryDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("571", hotelSearchData)

    def "TC00: NOVA POS - Multple modules within an itinerary - system flow 7 - amending traveller name on traveller details section after traveller assigned "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        loginToApplicaiton(clientData, hotelSearchData, resultData)
        addItemsToItinery(clientData, hotelSearchData, resultData)
        goToItinery(hotelSearchData, resultData)
        itineraryNameUpdate(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData)
        addremainingTravellers(hotelSearchData, resultData)
        bookItems(hotelSearchData, resultData)
        Edittravellername(hotelSearchData, resultData)
        Itemscouldnotbeamendedbox(hotelSearchData, resultData)
        Item1(hotelSearchData, resultData)
        Item2(hotelSearchData, resultData)

        Item3(hotelSearchData, resultData)
        Item4(hotelSearchData, resultData)
        Item5(hotelSearchData, resultData)
        Itinerarypage(hotelSearchData, resultData)
    }
    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(hotelSearchData, resultData)
    }
    def"TC02: Verify add items to itinerary  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyaddItemsToItinery(hotelSearchData, resultData)
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
        VerifyaddremainingTravellers(hotelSearchData, resultData)
    }
    def"TC06: Verify Book item Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifybookItems(hotelSearchData, resultData)
    }
    def"TC07: Verify Edit traveller name Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyEdit(hotelSearchData, resultData)

    }
    def"TC08: Verify Items could not be amended box Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItemscouldnotbeamendedbox(hotelSearchData, resultData)

    }
    def"TC09: Verify item #1 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyItem1(hotelSearchData, resultData)
        VerifyItem1Modified(hotelSearchData, resultData)

    }
    def"TC10: Verify item #2 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItem2(hotelSearchData, resultData)

    }
    def"TC11: Verify item #3 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItem3(hotelSearchData, resultData)

    }

    def"TC12: Verify item #4 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyItem4(hotelSearchData, resultData)
        VerifyItem4Modified(hotelSearchData, resultData)

    }
    def"TC13: Verify item #5 Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        //VerifyItem5(hotelSearchData, resultData)
        VerifyItem5Modified(hotelSearchData, resultData)

    }

    def"TC14: Verify Itinerary page Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyItinerarypage(hotelSearchData, resultData)

    }
}
