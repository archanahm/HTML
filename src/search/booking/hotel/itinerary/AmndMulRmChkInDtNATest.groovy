package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itineraryTravellerDetail

/**
 * Created by kmahas on 9/23/2016.
 *
 * 13.10 Scenario
 *
 * NOVA POS - Itinerary Page - amend hotel Dates - multiple rooms - check in date move + amendment not allowed
 */
class AmndMulRmChkInDtNATest extends AmndOccpBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")
    //ClientData clientData = new ClientData("client2466")


    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("EvergreenLaurel-SITUJ20XMLAuto", "itinerarytravellerdetail", itineraryTravellerDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("EvergreenLaurel-SITUJ20XMLAuto", hotelSearchData)

    def "TC00: NOVA POS - Itinerary Page - amend hotel Dates - multiple rooms - check in date move + amendment not allowed "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        login(clientData)

        createNewItineraryMultiRoom(clientData, hotelSearchData, resultData)
        amendOccupancyDatesTab(hotelSearchData, resultData,1)
        currntDetailsDatesTab(hotelSearchData, resultData)
        chkIfAllowedDatesTab(hotelSearchData, resultData)
        currntDetailsDatesTab(hotelSearchData, resultData)
        changeToInDateTab(hotelSearchData, resultData)
        chkAvailabilityDatesTab(hotelSearchData, resultData)
        acceptingChange(hotelSearchData, resultData)
        TAndCpgereconfirmationFromDatesTab(clientData,hotelSearchData, resultData,hotelSearchData.input.children)
        bkngConfirmingFromDatesTab(clientData,hotelSearchData, resultData,1,0)

        itinaryUpdtngDatesTab(clientData,hotelSearchData, resultData)
        amndOccupDatesTab(hotelSearchData, resultData,3)
        curntDetailsInDatesTab(hotelSearchData, resultData)
        chkingIfAllowDatesTab(hotelSearchData, resultData)
        curntDetailsInDatesTab(hotelSearchData, resultData)
        chngToDatesTab(hotelSearchData, resultData)
        checkAvailabilityInDatesTab(hotelSearchData, resultData)
        acceptingChanges(hotelSearchData, resultData)
        TAndCpgreconfrmatnFromDatesTab(clientData,hotelSearchData, resultData,hotelSearchData.input.children)
        bkngConfFromDatesTab(clientData,hotelSearchData, resultData,1,1)

        itinryUpdngDatesTab(clientData,hotelSearchData, resultData)
        amendOccupancyNADatesTab(hotelSearchData, resultData,3)


    }
    def"TC01: Verify Create new itinerary - multiple rooms - 2 (inventory available) Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifycreateNewItineraryMultiRoom(hotelSearchData, resultData)
    }
    def"TC02: Verify Amending 1st room - Check in date forward Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyAmendOccupancyDatesTab(hotelSearchData, resultData)
    }
    def"TC03: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsDatesTab(hotelSearchData, resultData)
    }
    def"TC04: Verify Check if allow Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChkIfAllowedDatesTab(hotelSearchData, resultData)
    }
    def"TC05: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsDatesTab(hotelSearchData, resultData)
    }
    def"TC06: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeToInDatesTab(hotelSearchData, resultData)
    }
    def"TC07: Verify Check Availability Actual & Expected Results"() {

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyCheckAvailabilityDatesTab(hotelSearchData, resultData)
    }
    def"TC08: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChange(hotelSearchData, resultData)
    }
    def"TC09: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyTAndCpagereconfirmation(hotelSearchData, resultData)
    }
    def"TC10: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybookingConfirmingDatesTab(hotelSearchData, resultData)
    }
    def"TC11: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitineraryUpdatingDatesTab(hotelSearchData, resultData)
    }
    def"TC12: Verify Amending 2nd room - Check in backward Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyamndOccupDatesTab(hotelSearchData, resultData)
    }
    def"TC13: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurntDetailsInDatesTab(hotelSearchData, resultData)
    }
    def"TC14: Verify Check if allow Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifychkingIfAllowDatesTab(hotelSearchData, resultData)
    }
    def"TC15: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurntDetailsInDatesTab(hotelSearchData, resultData)
    }
    def"TC16: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifychngToDatesTab(hotelSearchData, resultData)
    }
    def"TC17: Verify Check Availability Actual & Expected Results"() {

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyCheckAvailabilityInDatesTab(hotelSearchData, resultData)
    }
    def"TC18: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChanges(hotelSearchData, resultData)
    }
    def"TC19: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyTAndCpgreconfrmatnFromDatesTab(hotelSearchData, resultData)
    }
    def"TC20: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybkngConfFromDatesTab(hotelSearchData, resultData)
    }
    def"TC21: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitinryUpdngDatesTab(hotelSearchData, resultData)
    }
    def"TC22: Verify Amend NOT allow Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyamendOccupancyNADatesTab(hotelSearchData, resultData)
    }
}

