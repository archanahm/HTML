package com.kuoni.qa.automation.test.search.booking.transfer

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.TransferTestResultData
import com.kuoni.qa.automation.helper.TransfersTestData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.booking

/**
 * Created by kmahas on 7/25/2017.
 */
class SanityAirportToHotelTransfersTest extends TransfersSpec {

    @Shared
    ClientData clientData = new ClientData("creditClient")

    DateUtil dateUtil = new DateUtil()

    @Shared
    TransfersTestData transferData = new TransfersTestData("SanityAirportToHotel", "transfers", booking)
    @Shared
    TransferTestResultData resultData = new TransferTestResultData("SanityAirportToHotel", transferData)


    def "TC00: NOVA POS - Transfers Sanity Testing "() {
        given: "User is able to login and present on Hotel tab"

        loginToApplicaiton(clientData, transferData, resultData)// Test case 1,2 are covered
        SearchTransferAtoH(transferData, resultData)
        Itinerarybuilder(transferData, resultData)
        GoToitinerary(transferData, resultData)
        Itinerary(clientData, transferData, resultData)
        TravellerDetails(transferData, resultData)
        Booking(transferData, resultData)
        BookedItemslist(clientData,transferData, resultData)
        ItineraryPDF(transferData, resultData)
        CancelItem(transferData, resultData)

    }

    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(transferData, resultData)// Test case 1,2 are covered
    }
    def"TC02: Verify Search - Transfer A to A Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifySearchTransferAtoH(transferData, resultData)
    }
    def"TC03: Verify Itinerary builder, Go To Itinerary, Itinerary, Traveller Details, Booking & Booked Items Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        //VerifyItinerarybuilder(transferData, resultData)
        VerifyGoToitinerary(transferData, resultData)
        VerifyItinerary(transferData, resultData)
        VerifyTravellerDetails(transferData, resultData)
        VerifyBooking(transferData, resultData)
        VerifyBookedItemslist(transferData, resultData)
        VerifyItineraryPDF(transferData, resultData)
    }
   /* def"TC04: Verify Cancel Item  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyCancelItem(transferData, resultData)
    }*/

}
