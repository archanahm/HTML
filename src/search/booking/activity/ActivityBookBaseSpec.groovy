package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.activity.ActivityPDPInfoPage
import com.kuoni.qa.automation.page.activity.ActivitySearchResultsPage
import com.kuoni.qa.automation.page.activity.ActivitySearchToolPage
import com.kuoni.qa.automation.page.ActivityResultsPage
import com.kuoni.qa.automation.page.ActivityPDPPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage

/**
 * Created by ArchanaHM on 03/01/2018.
 */
class ActivityBookBaseSpec extends ActivityBaseSpec{


    def searchForActvity(ActivitiesSearchToolData data, HotelTransferTestResultData resultData) {
        at ActivitySearchToolPage
        clickActivitySearchTab()
        resultData.activity.activitySearchTabDefaultValues.put('activityTabHeaders',getActivityTabHeaders())
        resultData.activity.activitySearchTabDefaultValues.put('activityPlaceHolders',getActivitryPlaceHolder())
        resultData.activity.activitySearchTabDefaultValues.put('activityDefaultDateType',getActivityDefaultDateType())
        enterActivityDestination(data.input.desc)
        selectFromAutoSuggest(data.input.propertySuggest)
        List dates=data.input.date.split(";")

        for (int i=0; i<dates.size();i++){
            searchToolDateType(dates[i].toString().toInteger())
        }
        clickPax()
        sleep(1000)
        selectNoAdults(data.input.paxAdults)
        int numOfChild=data.input.paxChild.toString().toInteger()
        if (numOfChild>0){
            List age=data.input.age.toString().split(";")

            selectNoOfChild(data.input.paxChild)
            isErrorChildAgeDisplayed()
            for (int i=0;i<numOfChild;i++){
                enterChildAge(age[i] ,i)
            }
            clickPax()
            sleep(2000)
        }
        clickFindButton()
    }
/**
 * Steps included : Get first item in search result , check name displayed , image displayed, get price and save it
 * @param activitiesSearchToolData
 * @return
 */
    def getFirstAvailableItemPLP(ActivitiesSearchToolData data, HotelTransferTestResultData resultData){
        at ActivitySearchResultsPage
        if(searchResult()){
            resultData.activity.activityFirstAvilableNumPLP=getFirstAviItem()
            resultData.activity.activityDeatilsPLP.put('activityNamePLP',getActivityNameInPLP(resultData.activity.activityFirstAvilableNumPLP))
            this.itemName = resultData.activity.activityNamePLP
            resultData.activity.activityDeatilsPLP.put('activityStatusPLP',statusOfItem())
            verifyActivityDurationIcon(resultData.activity.activityFirstAvilableNumPLP)
            resultData.activity.activityDeatilsPLP.put('activityDurationToolText',getToolTipTextForDurationForTourIcon(resultData.activity.activityFirstAvilableNumPLP))
            verifyActivityLanuageIcon(resultData.activity.activityFirstAvilableNumPLP)
            resultData.activity.activityDeatilsPLP.put('activityLangugaeToolText',getToolTipTextForLanguageIcon(resultData.activity.activityFirstAvilableNumPLP))
            resultData.activity.activityDeatilsPLP.put('activityitemPricePLP',getPriceInTile(resultData.activity.activityFirstAvilableNumPLP).split(" "))
            price = resultData.activity.activityDeatilsPLP.activityitemPricePLP[0].toString()
            resultData.activity.activityDeatilsPLP.put('CurrencyType',resultData.activity.activityDeatilsPLP.activityitemPricePLP[1].toString())
            resultData.activity.activityDeatilsPLP.put('activityAvailableLangugaes',getActivityLanguage(resultData.activity.activityFirstAvilableNumPLP))
            resultData.activity.activityDeatilsPLP.put('activityDuration',getActivtiyDuration(resultData.activity.activityFirstAvilableNumPLP))

        }else{

            softAssert.fail("\n NO Search Result returned")
            }

    }
    def getDeatilsOfItemMoreLangugae(ActivitiesSearchToolData data, HotelTransferTestResultData resultData){
        at ActivityResultsPage
        if(moreLanugeOptionLink(1)){
            resultData.activity.activityDeatilsPLP.put('moreLangLinkTxt',getmoreLanugeOptionLink(resultData.activity.activityFirstAvilableNumPLP))
            clickOnMoreLangugaelink(1)
        }else {
            print "More language option not present For this Item"
        }
        resultData.activity.activityDeatilsPLP.put('activityDates',getActivityDateAllLanguagesPLP(resultData.activity.activityFirstAvilableNumPLP).split(":"))
        resultData.activity.activityDeatilsPLP.put('activityPrices',getActivityPriceAllLanguagesPLP(resultData.activity.activityFirstAvilableNumPLP))
        resultData.activity.activityDeatilsPLP.put('activityCancelLinks',getActivityCancellationAllLanguagesPLP(resultData.activity.activityFirstAvilableNumPLP))
        resultData.activity.activityDeatilsPLP.put('activityPaxIcons',getActivityPaxAllLanguagesPLP(resultData.activity.activityFirstAvilableNumPLP))
        resultData.activity.activityDeatilsPLP.put('activityPaxIconsToolTipTxt',getToolTipTextPaxIconAllLanguagesPLP(resultData.activity.activityFirstAvilableNumPLP))
        resultData.activity.activityDeatilsPLP.put('activityAvilablityTxt',getAvailablityAllLanguagesPLP(resultData.activity.activityFirstAvilableNumPLP))
        resultData.activity.activityDeatilsPLP.put('activityAddToItineraryTxt',getAddToItineraryAllLanguagesPLP(resultData.activity.activityFirstAvilableNumPLP))
        }

    def getDetailsFromCancellationOverlay(ActivitiesSearchToolData data, HotelTransferTestResultData resultData){
        at ActivityResultsPage
        clickOnCancellationLink(resultData.activity.activityFirstAvilableNumPLP)
        if (cancellationOverlayDisplayed()){
           resultData.activity.activityCancelOverlayPLP.put('specialCondition',getSpecialCndnCancellationOverlay())
           resultData.activity.activityCancelOverlayPLP.put('chargeConditionHeaderText',chargeConditionHeader())
           resultData.activity.activityCancelOverlayPLP.put('chargeCancellationDate',getFreeCancellationDate())
           resultData.activity.activityCancelOverlayPLP.put('chargeCancellationPrice',getFreeCancellationText())
           resultData.activity.activityCancelOverlayPLP.put('chargeCancellationApplyDate',getchargedCancellationDate())
           resultData.activity.activityCancelOverlayPLP.put('chargeCancellationApplyPrice',getChargedCancellationPrice())
            closeCancellationOverlay()

        }else {
            softAssert.fail("\n Cancelation Overlay Not Displayed ")
        }

    }

    def navigateToPDPPage(ActivitiesSearchToolData data,HotelTransferTestResultData resultData){
        at ActivitySearchResultsPage
        clickOnFirstAvilableItem(resultData.activity.activityFirstAvilableNumPLP)
        if(activityPDPPage()){
            at ActivityPDPPage
            resultData.activity.activityPDPPagedetails.put('activityNameFromPDP',getActivityNameOnPDP())
            resultData.activity.activityPDPPagedetails.put('activityDurationFromPDP',getDurationOnPDP())
            resultData.activity.activityPDPPagedetails.put('activityFrequencyFromPDP',getFrequencyOnPDP())
            resultData.activity.activityPDPPagedetails.put('activityLanguageFromPDP',getLanguageOnPDP())

        }else {
            softAssert.fail("\n SightSeeing PDP Page Not Displayed ")
        }

    }
    def getDeparturePointDetails(ActivitiesSearchToolData data,HotelTransferTestResultData resultData){
        at ActivityPDPInfoPage
        if (verifyMultipleDepartureLinkDisplayed()){
            resultData.activity.activityPDPPagedetails.put('depPointList',getMultipleLocations())
            resultData.activity.activityPDPPagedetails.put('depTimeList',getMultipleLocationsTime())
            closeLightBox()
        }else{
            resultData.activity.activityPDPPagedetails.put('depPointList',getDeparturePointOnPDP())

        }
    }
    def getDetailsDiffItemsPDP(ActivitiesSearchToolData data,HotelTransferTestResultData resultData){
        at ActivityPDPPage
        resultData.activity.activityPDPPagedetails.put('depPointListPDP', getActivityDateOnPDP())
        resultData.activity.activityPDPPagedetails.put('activityPricetPDP', getActivityPriceOnPDP())
        resultData.activity.activityPDPPagedetails.put('actvityPaxListPDP', getActivityPaxOnPDP())

    }


    def verifyActivityTabDefaultValues(ActivitiesSearchToolData data,HotelTransferTestResultData resultData) {
        print "verify Activity search page"
        int i = 0;
        resultData.activity.activitySearchTabDefaultValues.activityTabHeaders.each {
            String label = " ${it}"
            softAssert.assertTrue(label.contains(data.input.ActivityTabHeader[i]), "\nActivity tab  header value's are not correct Expected: $label Vs Actual: $data.input.ActivityTabHeader[i]")
            i++
        }
        softAssert.assertTrue(resultData.activity.activitySearchTabDefaultValues.activityPlaceHolders.contains("Destination, area, landmark, activity"), "\nPlaceHolder value not correct in activity Actual : $resultData.activity.activitySearchTabDefaultValues.activityPlaceHolders ")
        softAssert.assertTrue(resultData.activity.activitySearchTabDefaultValues.activityDefaultDateType.equals(data.input.defaultDateType), "\nPlaceHolder value not correct in activity Actual : $resultData.activity.activitySearchTabDefaultValues.activityDefaultDateType Vs Expected : $data.input.defaultDateType")


    }


 /*       def verifyActivityPLPPageDetails(ActivitiesSearchToolData data,HotelTransferTestResultData resultData){
            at ActivitySearchResultsPage
            softAssert.assertTrue(activityNameDisplayed(resultData.activity.activityFirstAvilableNumPLP),"\n Activity Name not displayed")
            softAssert.assertTrue(activityImgDisplayed(resultData.activity.activityFirstAvilableNumPLP),"\n Activity Image not displayed")

        }*/

    def VerifyActivityDetailsPDP(ActivitiesSearchToolData data,HotelTransferTestResultData resultData){
        softAssert.assertTrue(resultData.activity.activityDeatilsPLP.activityNamePLP.equals(resultData.activity.activityPDPPagedetails.activityNameFromPDP), "\nActivity Name is not correct in PDP Page , Name in PDP Actual : $resultData.activity.activityPDPPagedetails.activityNameFromPDP Vs Expected : $resultData.activity.activityDeatilsPLP.activityNamePLP")
        softAssert.assertTrue(resultData.activity.activityPDPPagedetails.activityDurationFromPDP.contains(resultData.activity.activityDeatilsPLP.activityDuration), "\nActivity Duration is not correct in PDP Page , Duration in PDP Actual : $resultData.activity.activityPDPPagedetails.activityDuration Vs Expected : $resultData.activity.activityDeatilsPLP.activityDurationFromPDP")
        softAssert.assertTrue(resultData.activity.activityPDPPagedetails.activityLanguageFromPDP.contains(resultData.activity.activityDeatilsPLP.activityAvailableLangugaes), "\nActivity Language is not correct in PDP Page , Language in PDP Actual : $resultData.activity.activityPDPPagedetails.activityAvailableLangugaes Vs Expected : $resultData.activity.activityDeatilsPLP.activityLanguageFromPDP")
      int i = 0
        resultData.activity.activityDeatilsPLP.activityDates.each {
            String date = " ${it}"
            softAssert.assertTrue(date.trim().equalsIgnoreCase(resultData.activity.activityPDPPagedetails.depPointListPDP[i].trim()), "\nActivity date in PDP is not correct Expected: $date Vs Actual: $resultData.activity.activityPDPPagedetails.depPointListPDP[i]")
            i++
        }
        int j=0
        resultData.activity.activityDeatilsPLP.activityPaxIcons.each {
            String pax = " ${it}"
            softAssert.assertTrue(pax.trim().equals(resultData.activity.activityPDPPagedetails.actvityPaxListPDP[i]), "\nActivity pax  value's are not correct in PDP Expected: $pax Vs Actual: $resultData.activity.activityPDPPagedetails.depPaxListPDP[j]")
            j++
        }
        int k = 0
        resultData.activity.activityDeatilsPLP.activityPrices.each{
            String price = " ${it}"
            softAssert.assertTrue(price.trim().equals(resultData.activity.activityPDPPagedetails.activityPricetPDP[k]), "\nActivity price value's are not correct Expected: $price Vs Actual: $resultData.activity.activityPDPPagedetails.activityPricetPDP[k]")
            k++
        }


    }

}
