package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.ItenaryPage
import com.kuoni.qa.automation.page.hotel.ItineraryPageBooking
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Archana on 13/06/2017.
 */
class ActivityPLPTest extends ActivityBaseSpec {


    @Shared
    ClientData client = new ClientData("client697")

   def setup() {
      login(client)
   }

    def "Activity Search By City -PLP-  Itinerary Page #testCase"(){
        given: "The agent logged in"
        when:"Agent search for on Activity in City"
        ActivitiesSearchToolData activitiesSearchToolData =new ActivitiesSearchToolData(testCase)
        searchForActivity(activitiesSearchToolData)
        then:"activites PLP Page displayed"
        verifyActivityPLP(activitiesSearchToolData)
        verifyItemDetailsPLP(activitiesSearchToolData)
        VerifyEssentailInfo()
        verifyCancellation()
        VerifyMoreLangOption()
        VerifySortLowestPrice()
        VerifySortHigestPrice()
       // verifySortAToZ()
        //verifySortZToA()
        VerifyFilterfunctionality()
/*        NavigateToPDPFirstAvilable()
        AddFirstAvailableItemToItinerary(activitiesSearchToolData)
        and:"Suggested item will be displayed"
        verifySuggestedItem(activitiesSearchToolData)

        then :"Add travellers and click Book Button"
        int numOfAdult=activitiesSearchToolData.input.paxAdults.toString().toInteger()
        int numOfChild=activitiesSearchToolData.input.paxChild.toString().toInteger()
        if (numOfChild>0)
            age=activitiesSearchToolData.input.age.toString().split(";")
        at ItenaryPage

        addTravellers(numOfAdult,numOfChild,age)
        sleep(1000)
        clickonSaveButton(0)
        at ItineraryPageBooking
        clickBookButton(0)
        sleep(1000)
        then:"Terms and conditions page displayed"
        verifyTermsAndConditions(activitiesSearchToolData)

        and:"click Confirm BookingButton"
        clickConfirmBooking()
        waitTillConformationPage()
        then:"Confirmation page will be displayed"
        verifyBookingConfirmationPage(activitiesSearchToolData)
        and:"close lightbox"
        coseBookItenary()
        sleep(1000)
        then:"Booked item displayed"
        verifyBookedItem(activitiesSearchToolData)*/
        where:
        testCase <<  ["TSCitySearch"]


    }

}