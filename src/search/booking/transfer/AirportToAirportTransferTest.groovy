package com.kuoni.qa.automation.test.search.booking.transfer

import com.kuoni.qa.automation.helper.TransferTestResultData
import spock.lang.Unroll

/**
 * Created by kmahas on 6/7/2016.
 */
class AirportToAirportTransferTest extends TransferBookingBaseSpec {

    static String subPath = "AirportToAirport."
    static def dataNames = ["case1"]

    @Unroll
    def "04.1 : Validate Itinerary Page #transferData.caseName #transferData.desc"() {
        TransferTestResultData resultData = resultMap.get(transferData.caseName)
        when: "Expected and actual  Results are available"

        then: "Assert the Itinerary Page"
        //Validate booking description text - added in test class
        assertionEquals(resultData.transfers.itineraryPage.actualBookingDesc, resultData.transfers.itineraryPage.expectedBookingDesc, "Itinerary Screen 'You Are About to Book Screen' Booking Description Expected & Actual details don't match")
        //item image
        assertionEquals(resultData.transfers.itineraryPage.actualNonBookedItemImageSrcURL,resultData.transfers.searchResults.retrievetransferFirstItemimageURL,"Itinerary Screen, Item Image displayed Actual & Expected don't match")
        where:
        transferData << loadData(dataNames, subPath)
    }
    @Unroll
    def "05.1 : Validate Confirmation Page #transferData.caseName #transferData.desc"() {
        TransferTestResultData resultData = resultMap.get(transferData.caseName)
        when: "Expected and actual  Results are available"

        then: "Assert the Confirmation Page Results"

        //validate pickup
        assertionEquals(resultData.transfers.confirmationPage.actualPickupATA,resultData.transfers.confirmationPage.expectedPickupATA,"Confirmation Screen Pick Up Details Actual & Expected don't match")

        //validate dropoff
        assertionEquals(resultData.transfers.confirmationPage.actualDropOffATA,resultData.transfers.confirmationPage.expectedDropOffATA,"Confirmation Screen Drop Off Details Actual & Expected don't match")

        //Validate Transfer Date And Time
        assertionEquals(resultData.transfers.confirmationPage.actualTransferDateAndTimeInBookingConf,resultData.transfers.itineraryPage.actualTransferDateAndtimeInNonBookedItms,"Confirmation Screen, Client Requested Service Pick up Date And Time Actual & Expected don't match")

        where:
        transferData << loadData(dataNames, subPath)
    }
    @Unroll
    def "06.1 : Validate Booking Result #transferData.caseName #transferData.desc"() {
        TransferTestResultData resultData = resultMap.get(transferData.caseName)
        when: "Expected and actual Results are available"

        then: "Assert the Booking Result"
        //validate pickup
        assertionEquals(resultData.transfers.bookingResult.actualPickupATA, resultData.transfers.bookingResult.expectedPickupATA, "Traveller Details (Booking Result) Pick Up Expected & Actual details don't match")
        //validate dropoff
        assertionEquals(resultData.transfers.bookingResult.actualDropOffATA, resultData.transfers.bookingResult.expectedDropOffATA, "Traveller Details (Booking Result) Drop Off Expected & Actual details don't match")

        where:
        transferData << loadData(dataNames, subPath)
    }
    @Unroll
    def "07.1 : Validate Share Itinerary #transferData.caseName #transferData.desc"() {
        TransferTestResultData resultData = resultMap.get(transferData.caseName)
        when: "Expected and actual  Results are available"

        then: "Assert the Share Itinerary"
        //validate pickup
        assertionEquals(resultData.transfers.shareItineraryPage.actualPickupATA, resultData.transfers.shareItineraryPage.expectedPickupATA, "Share Itinerary Screen Pick Up Expected & Actual details don't match")

        //validate dropoff
        assertionEquals(resultData.transfers.shareItineraryPage.actualDropOffATA, resultData.transfers.shareItineraryPage.expectedDropOffATA, "Share Itinerary Screen Drop Off Expected & Actual details don't match")

        //validate pickup
        assertionEquals(resultData.transfers.shareItineraryPage.actPickupATA,resultData.transfers.shareItineraryPage.expectedPickupATA,"Share Itinerary Screen Pick Up After Close popup Expected & Actual details don't match")

        //validate dropoff
        assertionEquals(resultData.transfers.shareItineraryPage.actDropOffATA,resultData.transfers.shareItineraryPage.expectedDropOffATA,"Share Itinerary Screen Drop Off After Close popup  Expected & Actual details don't match")

        where:
        transferData << loadData(dataNames, subPath)

    }
}
