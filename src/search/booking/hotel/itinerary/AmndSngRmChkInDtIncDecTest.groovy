package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itineraryTravellerDetail

/**
 * Created by kmahas on 9/20/2016.
 *
 * 13.9 Scenario
 *
 * NOVA POS - Itinerary Page - amend hotel Dates - single room - check in date move + increase / decrease stay night
 *
 */
class AmndSngRmChkInDtIncDecTest extends AmndOccpBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")
    //ClientData clientData = new ClientData("client2466")


    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("Hibiscus-SITUJ20XMLAuto", "itinerarytravellerdetail", itineraryTravellerDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("Hibiscus-SITUJ20XMLAuto", hotelSearchData)

    def "TC00: NOVA POS - Itinerary Page - amend hotel Dates - single room - check in date move + increase / decrease stay night "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        login(clientData)

        createNewItinerary(clientData, hotelSearchData, resultData)
        addSecondItem(clientData, hotelSearchData, resultData)
        amendOccupancyDatesTab(hotelSearchData, resultData,1)
        currentDetailsDatesTab(hotelSearchData, resultData)
        chkIfAllowDatesTab(hotelSearchData, resultData)
        currentDetailsDatesTabSecondHotel(hotelSearchData, resultData)
        changeToInDatesTab(hotelSearchData, resultData)
        checkAvailabilityDatesTab(hotelSearchData, resultData)
        acceptingChange(hotelSearchData, resultData)
        TAndCpagereconfirmationFromDatesTab(clientData,hotelSearchData, resultData,hotelSearchData.input.children)

        bookingConfirmingFromDatesTab(clientData,hotelSearchData, resultData,4,0)
        itinenaryUpdatingDatesTab(clientData,hotelSearchData, resultData)
        //amndOccupDatesTab(hotelSearchData, resultData,3)
        amndOccupDatesTab(hotelSearchData, resultData,1)
        curntDetailsDatesTab(hotelSearchData, resultData)
        checkingIfAllowDatesTab(hotelSearchData, resultData)
        curntDetailsDatesTab(hotelSearchData, resultData)
        changeToDatesTab(hotelSearchData, resultData)
        checkAvailabilityInDatesTab(hotelSearchData, resultData)
        acceptingChanges(hotelSearchData, resultData)
        TAndCpgreconfrmtnFromDatesTab(clientData,hotelSearchData, resultData,hotelSearchData.input.children)

        bkngConfrmngFromDatesTab(clientData,hotelSearchData, resultData,4,1)
        itinryUpdatingDatesTab(clientData,hotelSearchData, resultData)
    }
    def"TC01: Verify Create new itinerary - 1st item (inventory available) Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyCreatenewitinry(hotelSearchData, resultData)
    }
    def"TC02: Verify add 2nd item Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyaddSecondItem(hotelSearchData, resultData)
    }
    def"TC03: Verify Amending 1st item - more stay nights Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyAmendOccupancyDatesTab(hotelSearchData, resultData)
    }
    def"TC04: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsDatesTab(hotelSearchData, resultData)
    }
    def"TC05: Verify Check if allow Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChkIfAllowDatesTab(hotelSearchData, resultData)
    }
    def"TC06: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsDatesTabSecondHotel(hotelSearchData, resultData)
    }

    def"TC07: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeToInDatesTab(hotelSearchData, resultData)
    }
    def"TC08: Verify Check Availability Actual & Expected Results"() {

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyCheckAvailabilityDatesTab(hotelSearchData, resultData)
    }
    def"TC09: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChange(hotelSearchData, resultData)
    }
    def"TC10: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyTAndCpagereconfirmation(hotelSearchData, resultData)
    }
    def"TC11: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybookingConfirmingDatesTab(hotelSearchData, resultData)
    }
    def"TC12: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitineraryUpdatingDatesTab(hotelSearchData, resultData)
    }
    def"TC13: Verify Amending 2nd item - Less stay nights Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyamndOccupDatesTab(hotelSearchData, resultData)
    }
    def"TC14: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurntDetailsDatesTab(hotelSearchData, resultData)
    }
    def"TC15: Verify Check if allow Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycheckingIfAllowDatesTab(hotelSearchData, resultData)
    }
    def"TC16: Verify Current Details - should retain Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurntDetailsDatesTab(hotelSearchData, resultData)
    }
    def"TC17: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeToDatesTab(hotelSearchData, resultData)
    }
    def"TC18: Verify Check Availability Actual & Expected Results"() {

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyCheckAvailabilityInDatesTab(hotelSearchData, resultData)
    }
    def"TC19: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChanges(hotelSearchData, resultData)
    }
    def"TC20: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyTAndCpgreconfrmtnFromDatesTab(hotelSearchData, resultData)
    }
    def"TC21: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybkngConfrmngFromDatesTab(hotelSearchData, resultData)
    }
    def"TC22: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitinryUpdatingDatesTab(hotelSearchData, resultData)
    }
}
