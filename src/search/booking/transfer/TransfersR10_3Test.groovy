package com.kuoni.qa.automation.test.search.booking.transfer

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.TransferTestResultData
import com.kuoni.qa.automation.helper.TransfersTestData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.booking

/**
 * Created by kmahas on 4/14/2017.
 *
 * Sheet Name: TN_R_10.2 in file: NOVASITPOS_TN_TC_Updated
 *
 */
class TransfersR10_3Test extends TransfersSpec{

    @Shared
    //ClientData clientData = new ClientData("client670")
    ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    TransfersTestData transferData = new TransfersTestData("R103", "transfers", booking)
    @Shared
    TransferTestResultData resultData = new TransferTestResultData("R103", transferData)


    def "TC00: NOVA POS - Transfers 10.3 "() {
        given: "User is able to login and present on Hotel tab"

        loginToApplicaiton(clientData, transferData, resultData)// Test case 1,2 are covered
        SearchTransferAtoA(transferData, resultData)
        Itinerarybuilder(transferData, resultData)
        GoToitinerary(transferData, resultData)
        Itinerary(clientData,transferData, resultData)
        Traveller1Details(transferData, resultData)
        Bookanitemwithoutsave(transferData, resultData)
        EditAgentrefrence(transferData, resultData)
        EditTravellerdetails(transferData, resultData)
        BookingTAndC(transferData, resultData)
        Departuretimeerrormessage(transferData, resultData)


    }
    def"TC01: Verify Access to agent site Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyloginToApplicaiton(transferData, resultData)// Test case 1,2 are covered
    }
    def"TC03: Verify Search - Transfer A to A Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifySearchTransferAtoA(transferData, resultData)
    }
    def"TC04: Verify Itinerary builder Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItinerarybuilder(transferData, resultData)
    }
    def"TC05: Verify Go To itinerary  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyGoToitinerary(transferData, resultData)
    }
    def"TC06: Verify Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyItinerary(transferData, resultData)
    }
    def"TC07: Verify Traveller 1 Details Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyTraveller1Details(transferData, resultData)
    }
    def"TC08: Verify Book an  item  without save Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyBookanitemwithoutsave(transferData, resultData)
    }
    def"TC09: Verify Edit Agent refrence  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyEditAgentrefrence(transferData, resultData)
    }
    def"TC10: Verify Edit Traveller details Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyEditTravellerdetails(transferData, resultData)
    }
    def"TC11: Verify Booking T & C  Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyBookingTAndC(transferData, resultData)
    }
    def"TC12: Verify Departure time error message Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyDeparturetimeerrormessage(transferData, resultData)
    }
}
