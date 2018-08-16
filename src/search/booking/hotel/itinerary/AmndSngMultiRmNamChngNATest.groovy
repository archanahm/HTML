package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itineraryTravellerDetail

/**
 * Created by kmahas on 8/18/2016.
 * 13.7 Scenario - covered till 15 test cases.
 *
 * NOVA POS - Itinerary Page - amend hotel occupancy - name change not allowed - single&multiple room -
 *
 */
class AmndSngMultiRmNamChngNATest extends AmndOccpBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("HowardPrince-SITUJ07XMLAutoMultiRoom", "itinerarytravellerdetail", itineraryTravellerDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("HowardPrince-SITUJ07XMLAutoMultiRoom", hotelSearchData)

    def "TC00: NOVA POS - Itinerary Page - amend hotel occupancy - name change not allowed - single&multiple room "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        login(clientData)

        addToItineraryMultiRoom(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,2,3,3,hotelSearchData.input.children_FirstRoom)
        //Add Travellers 4 - adult To 5 - Adult
        addSaveAndVerifyAdultTravellers(4,5,hotelSearchData)
        //Add Travellers 6 - child To 7 - Child
        addSaveAndVerifyChildTravellers(6,7,hotelSearchData,hotelSearchData.input.children_SecondRoom)
        bookMultipleHotelItem(hotelSearchData, resultData,hotelSearchData.input.children_FirstRoom,hotelSearchData.input.children_SecondRoom)
        amendOccupancyDatesTab(hotelSearchData, resultData,1)
        currentDetailsDatesTab(hotelSearchData, resultData)
        checkIfDatesTab(hotelSearchData, resultData)
        currentDetailsDatesTab(hotelSearchData, resultData)
        changeToInDatesTab(hotelSearchData, resultData)
        checkAvailabilityDatesTab(hotelSearchData, resultData)

        acceptingChange(hotelSearchData, resultData)
        TAndCpagereconfirmationFromDatesTab(clientData,hotelSearchData, resultData,hotelSearchData.input.children_FirstRoom)
        bookingConfirmingFromDatesTab(clientData,hotelSearchData, resultData,3,0)
        itinenaryUpdatingDates(clientData,hotelSearchData, resultData)
        amendOccupancyNADatesTab(hotelSearchData, resultData,1)
    }

    def"TC01: Verify Create new itinerary - 1st item Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyCreatenewitinerarymultiroom(hotelSearchData, resultData)
    }
    def"TC02: Verify Go To Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyGoToItinerary(hotelSearchData, resultData)
    }
    def"TC03: Verify Traveller Details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyTravellerDetails(hotelSearchData, resultData)
    }
    def"TC04: Verify Book the 2 rooms item Actual & Expected Results"(){

        given: "User is logged in and present on Booking Confirmed"
        VerifyBookHotelItemWithAllTravelers(hotelSearchData, resultData)
    }
    def"TC05: Verify Amend multiple 1nd room - backwards check in date Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyAmendOccupancyDatesTab(hotelSearchData, resultData)
    }
    def"TC06: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsDatesTab(hotelSearchData, resultData)
    }
    def"TC07: Verify Check if allow Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyCheckIfDatesTab(hotelSearchData, resultData)
    }
    def"TC08: Verify current booking detials - should retain Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsDatesTab(hotelSearchData, resultData)
    }
    def"TC09: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeToInDatesTab(hotelSearchData, resultData)
    }

    def"TC10: Verify Check Availability Actual & Expected Results"() {

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyCheckAvailabilityDatesTab(hotelSearchData, resultData)
    }
    def"TC11: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChange(hotelSearchData, resultData)
    }
    def"TC12: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyTAndCpagereconfirmation(hotelSearchData, resultData)
    }
    def"TC13: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybookingConfirmingDatesTab(hotelSearchData, resultData)
    }
    def"TC14: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitineraryUpdatingDates(hotelSearchData, resultData)
    }
    def"TC15: Verify Amend Tab Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyamendOccupancyNADatesTab(hotelSearchData, resultData)
    }


}
