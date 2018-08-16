package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itineraryTravellerDetail

/**
 * Created by kmahas on 7/18/2016.
 * 13.3 Scenario
 * NOVA POS - Itinerary Page - amend hotel occupancy - single room - adult (add/remove)  - room rate changed
 */
class AmndSingRoomAddRemAdltTest extends AmndOccpBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("HowardPrince-SITUJ07XMLAuto", "itinerarytravellerdetail", itineraryTravellerDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("HowardPrince-SITUJ07XMLAuto", hotelSearchData)

    def "TC00: NOVA POS - Itinerary Page - amend hotel occupancy - single room - adult (add/remove)  - room rate changed "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        login(clientData)
        addToItinerary(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,4,5,6,hotelSearchData.input.addchildren)
        bookHotelItem(hotelSearchData, resultData)
        amendOccupancy(hotelSearchData, resultData,1)
        currentDetails(hotelSearchData, resultData)
        changeAddAndRemove(hotelSearchData, resultData)
        selectToAssign(hotelSearchData, resultData,2,4,5,6,hotelSearchData.input.addchildren)
        adding1adultpaxtoroomAllowed(hotelSearchData, resultData)
        currentBookingDetailsShouldRetain(hotelSearchData, resultData)
        changeTo(hotelSearchData, resultData)
        selectToAssignAfterFirstAmend(hotelSearchData, resultData,2,4,5,6,hotelSearchData.input.addchildren)
        addinganotheradditionaladultpaxtoroomAllowed(hotelSearchData, resultData)
        currentBookingDetailsShouldRetain(hotelSearchData, resultData)
        changeToAfterSecondAmend(hotelSearchData, resultData)
        selectToAssignAfterSecondAmend(hotelSearchData, resultData,2,4,5,6,hotelSearchData.input.addchildren)
        acceptingChange(hotelSearchData, resultData)
        TAndCpagereconfirmation(clientData,hotelSearchData, resultData)
        bookingConfirming(clientData,hotelSearchData, resultData)
        itinenaryUpdating(clientData,hotelSearchData, resultData)
        amendOccupancy(hotelSearchData, resultData,1)
        currentDetailsSecond(hotelSearchData, resultData)
        changeAddAndRemoveLess(hotelSearchData, resultData)
        selectToAssignAfterSecondAmend(hotelSearchData, resultData,2,4,5,6,hotelSearchData.input.addchildren)
        removeanadultpaxtoroomAllowed(hotelSearchData, resultData)
        currentDetailsSecond(hotelSearchData, resultData)
        changeToBeforeThirdAmend(hotelSearchData, resultData)
        selectToAssignBeforeThirdAmend(hotelSearchData, resultData,2,4,5,6,hotelSearchData.input.addchildren)
        acceptingChange(hotelSearchData, resultData)
        TAndCpagereconfirmationAfterReduceAmend(clientData,hotelSearchData, resultData)
        bookingConfirmingAfterReduceAmend(clientData,hotelSearchData, resultData)
        itinenaryUpdatingAfterReduceAmend(clientData,hotelSearchData, resultData)
    }

    def"TC01: Verify Create new itinerary - 1st item Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search"
        VerifyCreatenewitinerary(hotelSearchData, resultData)
    }

    def"TC02: Verify Go To Itinerary Actual & Expected Results"(){

        given: "User is logged in and present on Hotel Search Results"
        VerifyGoToItinerary(hotelSearchData, resultData)
    }

    def"TC03: Verify Traveller Details Actual & Expected Results"(){

        given: "User is logged in and present on Itinerary Page"
        VerifyTravellerDetails(hotelSearchData, resultData)
    }

    def"TC04: Verify Book the Hotel Item Actual & Expected Results"(){

        given: "User is logged in and present on Booking Confirmed"
        VerifyBookingConfirmDetails(hotelSearchData, resultData)
    }

    def"TC05: Verify Amend Occupancy Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyAmendOccupancy(hotelSearchData, resultData)
    }

    def"TC06: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetails(hotelSearchData, resultData)
    }

    def"TC07: Verify Change Add And Remove Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeAddAndRemove(hotelSearchData, resultData)
    }

    def"TC08: Verify Select to Assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifySelectToAssign(hotelSearchData, resultData)
    }
    def"TC09: Verify Adding 1 adult pax to room allowed Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        Verifyadding1adultpaxtoroomAllowed(hotelSearchData, resultData)
    }
    def"TC10: Verify current booking detials - should retain Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentBookingDetailsShouldRetain(hotelSearchData, resultData)
    }
    def"TC11: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeTo(hotelSearchData, resultData)
    }

    def"TC12: Verify Select to assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifySelectToAssignAfterFirstAmend(hotelSearchData, resultData)
    }
    def"TC13: Verify adding another additional adult pax to room - allowed Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyaddinganotheradditionaladultpaxtoroomAllowed(hotelSearchData, resultData)
    }
    def"TC14: Verify current booking detials - should retain Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentBookingDetailsShouldRetain(hotelSearchData, resultData)
    }
    def"TC15: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeToAfterSecondAmend(hotelSearchData, resultData)
    }
    def"TC16: Verify Select to assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyselectToAssignAfterSecondAmend(hotelSearchData, resultData)
    }
    def"TC17: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChange(hotelSearchData, resultData)
    }
    def"TC18: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyTAndCpagereconfirmation(hotelSearchData, resultData)
    }
    def"TC19: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybookingConfirming(hotelSearchData, resultData)
    }
    def"TC20: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitineraryUpdating(hotelSearchData, resultData)
    }
    def"TC21: Verify Amend Occupancy To Less Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyAmendOccupancy(hotelSearchData, resultData)
    }
    def"TC22: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsSecond(hotelSearchData, resultData)
    }
    def"TC23: Verify Change Add And Remove Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeAddAndRemoveLess(hotelSearchData, resultData)
    }
    def"TC24: Verify Select to assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyselectToAssignAfterSecondAmend(hotelSearchData, resultData)
    }

    def"TC25: Verify remove an adult pax to room - allowed Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyremoveanadultpaxtoroomAllowed(hotelSearchData, resultData)
    }
    def"TC26: Verify Current Details Should Retain Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsSecond(hotelSearchData, resultData)
    }

    def"TC27: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifychangeToBeforeThirdAmend(hotelSearchData, resultData)
    }
    def"TC28: Verify Select to assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyselectToAssignBeforeThirdAmend(hotelSearchData, resultData)
    }
    def"TC29: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChange(hotelSearchData, resultData)
    }
    def"TC30: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyTAndCpagereconfirmationAfterReduceAmend(hotelSearchData, resultData)
    }
    def"TC31: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybookingConfirmingAfterReduceAmend(hotelSearchData, resultData)
    }
    def"TC32: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitinenaryUpdatingAfterReduceAmend(hotelSearchData, resultData)
    }

}