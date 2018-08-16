package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.itineraryTravellerDetail

/**
 * Created by kmahas on 8/3/2016.
 * 13.5 Scenario
 * NOVA POS - Itinerary Page - amend hotel occupancy - single room - infant  (add/remove ) - change rate
 *
 */
class AmndSingRoomAddRemInfantTest extends AmndOccpBaseSpec {

    @Shared
    public static Map<String, HotelTransferTestResultData> resultMap = [:]
    @Shared
    ClientData clientData = new ClientData("client664")

    DateUtil dateUtil = new DateUtil()

    @Shared
    HotelSearchData hotelSearchData = new HotelSearchData("Cambridge-SITUJ18XMLAutoInfant", "itinerarytravellerdetail", itineraryTravellerDetail)
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData("Cambridge-SITUJ18XMLAutoInfant", hotelSearchData)

    def "TC00: NOVA POS - Itinerary Page - amend hotel occupancy - single room - infant  (add/remove ) - change rate "() {
        given: "User is able to login and present on Hotel tab"

        resultMap.put(hotelSearchData.hotelName, resultData)
        login(clientData)
        addToItinerary(clientData, hotelSearchData, resultData)
        goToItinerary(hotelSearchData, resultData)
        travellerDetails(hotelSearchData, resultData,2,2,3,4,hotelSearchData.input.children)
        bookHotelItemWithAllTravelers(hotelSearchData, resultData,hotelSearchData.input.children)
        amendOccupancy(hotelSearchData, resultData,1)
        currentDetails(hotelSearchData, resultData)
        changeAddAndRemoveLess(hotelSearchData, resultData)
        selectToAssign(hotelSearchData, resultData,2,2,3,4,hotelSearchData.input.children)
        add2infant(hotelSearchData, resultData)
        currentBookingDetailsShouldRetain(hotelSearchData, resultData)

        changeTo(hotelSearchData, resultData)
        selectToAssignAfterFirstAmend(hotelSearchData, resultData,2,2,3,4,hotelSearchData.input.children)
        acceptingChange(hotelSearchData, resultData)
        TAndCpagereconfirmation(clientData,hotelSearchData, resultData)
        bookingConfirming(clientData,hotelSearchData, resultData)
        itinenaryUpdating(clientData,hotelSearchData, resultData)
        amendOccupancy(hotelSearchData, resultData,1)
        currentDetailsSecond(hotelSearchData, resultData)
        changeAddRemoveMore(hotelSearchData, resultData)
        selectToAssignAfterSecondAmend(hotelSearchData, resultData,2,2,3,4,hotelSearchData.input.children)

        removeanInfant(hotelSearchData, resultData)
        currentDetailsSecond(hotelSearchData, resultData)
        changeToBeforeThirdAmend(hotelSearchData, resultData)
        selectToAssignBeforeThirdAmend(hotelSearchData, resultData,2,2,3,4,hotelSearchData.input.children)
        acceptingChange(hotelSearchData, resultData)
        TAndCpagereconfirmationAfterReduceAmend(clientData,hotelSearchData, resultData)
        bookingConfirmingAfterReduceAmend(clientData,hotelSearchData, resultData)
        itinenaryUpdatingAfterReduceAmend(clientData,hotelSearchData, resultData)
        abtToAmndOcupAddASpecialRemarkorComment(hotelSearchData, resultData)

    }
    def"TC01: Verify Create new itinerary - 1st item (inventory available) Actual & Expected Results"(){

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
        VerifyBookHotelItemWithAllTravelers(hotelSearchData, resultData)
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
        VerifyChangeAddAndRemoveLess(hotelSearchData, resultData)
    }

    def"TC08: Verify Select to Assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifySelectToAssign(hotelSearchData, resultData)
    }
    def"TC09: Verify Add 2 Infant Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        Verifyadd2Infant(hotelSearchData, resultData)
    }

    def"TC10: Verify current booking detials - should retain Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentBookingDetailsShouldRetain(hotelSearchData, resultData)
    }
    def"TC11: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        //VerifyChangeTo(hotelSearchData, resultData)
        VerifyChangeToModified(hotelSearchData, resultData)
    }
    def"TC12: Verify Select to assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifySelectToAssignAfterFirstAmend(hotelSearchData, resultData)
    }

    def"TC13: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChange(hotelSearchData, resultData)
    }
    def"TC14: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        //VerifyTAndCpagereconfirmation(hotelSearchData, resultData)
        VerifyTAndCpagereconfirmationModified(hotelSearchData, resultData)
    }
    def"TC15: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybookingConfirming(hotelSearchData, resultData)
    }
    def"TC16: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitineraryUpdating(hotelSearchData, resultData)
    }
    def"TC17: Verify Amend Occupancy To Less Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyAmendOccupancy(hotelSearchData, resultData)
    }
    def"TC18: Verify Current Details Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsSecond(hotelSearchData, resultData)
    }
    def"TC19: Verify Change Add And Remove Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyChangeAddAndRemoveLess(hotelSearchData, resultData)
    }
    def"TC20: Verify Select to assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyselectToAssignAfterSecondAmend(hotelSearchData, resultData)
    }

    def"TC21: Verify remove 1  infant Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        Verifyremove1Infant(hotelSearchData, resultData)
    }

    def"TC22: Verify Current Details Should Retain Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifycurrentDetailsSecond(hotelSearchData, resultData)
    }
    def"TC23: Verify Change to: Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        //VerifychangeToBeforeThirdAmend(hotelSearchData, resultData)
        VerifychangeToBeforeThirdAmendModified(hotelSearchData, resultData)
    }
    def"TC24: Verify Select to assign Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyselectToAssignBeforeThirdAmend(hotelSearchData, resultData)
    }
    def"TC25: Verify accepting change Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyacceptingChange(hotelSearchData, resultData)
    }
    def"TC26: Verify T&C page - reconfirmation Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        //VerifyTAndCpagereconfirmationAfterReduceAmend(hotelSearchData, resultData)
        VerifyTAndCpagereconfirmationAfterReduceAmendModified(hotelSearchData, resultData)
    }
    def"TC27: Verify Booking confirming Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifybookingConfirmingAfterReduceAmend(hotelSearchData, resultData)
    }
    def"TC28: Verify Itinerary Updating Actual & Expected Results"(){

        given: "User is logged in and present with Ammend Tab, Occupancy"
        VerifyitinenaryUpdatingAfterReduceAmend(hotelSearchData, resultData)
    }
    def"TC29: Verify About To Amend Add A Special Remark or Comment Actual & Expected Results"(){

        given: "User is logged in and present with Booked Item"
        VerifyaboutToAmendAddASpecialRemarkorComment(hotelSearchData, resultData)
    }

}
