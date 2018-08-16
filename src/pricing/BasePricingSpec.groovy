package com.kuoni.qa.automation.test.pricing

import com.kuoni.qa.automation.helper.CancellationConditionCoh
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.CoherenceResponse
import com.kuoni.qa.automation.helper.RoomRequirement
import com.kuoni.qa.automation.helper.PricingSearchData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItineraryPreviewPage
import com.kuoni.qa.automation.page.pricing.ItenaryPagePricing
import com.kuoni.qa.automation.page.pricing.ItineraryListPagePricing
import com.kuoni.qa.automation.page.pricing.ItinerarySharePagePricing

//import com.kuoni.qa.automation.page.traveller.EmutripLoginPage
//import com.kuoni.qa.automation.page.traveller.ItineraryOverviewPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.AS400Util
import com.kuoni.qa.automation.util.CoherenceAPIUtil
import com.kuoni.qa.automation.util.PriceSearchQueryBuilder
import org.joda.time.DateTime
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.getStringValue
import static com.kuoni.qa.automation.util.TestUtil.getDateString
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 * Created by Joseph Sebastian on 02/10/2015.
 */
abstract class BasePricingSpec extends BaseSpec {

    @Shared
    protected static PricingSearchData activeSearchData
    @Shared
    protected static ClientData activeClientData
    protected Map<String, Map<String, String>> cohPriceMap
    protected Map<String, Map<String, String>> cohCommissionMap
    protected Map<String, Map<String, String>> cohPriceBreakdownMap
    protected Map<String, List<CancellationConditionCoh>> cohCancellationChargeMap

    @Shared
    protected AS400Util dbUtil;

    abstract void initValues()

    @Shared
    boolean isTravellerSiteLoggedIn = false
    @Shared
    boolean isNovaSiteLoggedIn = false

    protected fillSearchFormAndSubmit(PricingSearchData data) {
        String cityAreaLandMarkHotelTypeText = activeSearchData.cityAreaLandMarkHotelTypeText
        String cityAreaLandMarkHotel = activeSearchData.cityAreaLandMarkHotel
        String checkIn = activeSearchData.checkInDaysToAdd.toString()
        String checkOut = activeSearchData.checkOutDaysToAdd.toString()
        String guestsRoom1Adults = activeSearchData.guestsRoom1Adults
        List<Integer> childrenAges = activeSearchData.childrenAges
        fillHotelSearchFormAndSubmit(cityAreaLandMarkHotelTypeText, cityAreaLandMarkHotel, checkIn, checkOut, guestsRoom1Adults, childrenAges)

/*
        at HotelSearchPage
        typeHotelDestination(cityAreaLandMarkHotelTypeText)
//        sleep(3000)
        try {
            selectFromAutoSuggest(cityAreaLandMarkHotel)
        } catch (Exception e) {
            assertNotNull("Failed to find hotel '$cityAreaLandMarkHotelTypeText'", null)
        }
        selectcheckInCheckOutDateCalender(checkIn, checkOut)

        // default is 1 room 2 pax so no need to set it if again
        if(guestsRoom1Adults.toInteger() != 2 || childrenAges.size() != 0){
            clickPaxRoom()
            selectNumberOfAdults(guestsRoom1Adults, 0)

            if (childrenAges.size() > 0) {
                clickPaxRoom()
                selectNumberOfChildren(childrenAges.size().toString(), 1)
                childrenAges.eachWithIndex { int age, int i ->
                    enterChildrenAge(age.toString(), "1", i)
                }
                clickPaxRoom()
                sleep(3000)
            }
        } //
        sleep(1000)
        clickFindButton()
*/
    }

    protected getCoherenceData(PriceSearchQueryBuilder builder, String propertyId, PricingSearchData searchData) {
        String query = builder.buildJson()
        def priceMap = CoherenceAPIUtil.instance.getData(query, propertyId, searchData)
        return priceMap
    }

    def setCommission(PricingSearchData searchData) {
        if (searchData.shouldCheckCommission()) {
            clickOnCommissionsAndCurrencyandSetYes()
        } else {
            clickOnCommissionsAndCurrencyandSetNo()
        }
    }

    protected static loadData(List<String> paths, def ignoreCase) {
        List<PricingSearchData> dataList = paths.collect { path ->
            new PricingSearchData(path)
        }.findAll { ignoreCase(it) }
        return dataList
    }

    protected void assertPrice(String actual, String expected, String message) {
        if (actual == null || expected == null) {
            softAssert.assertTrue(false, "Unable to compare as one of the values is null $expected(expected) != $actual(actual) -- $message\n")
        } else {
            try {
                println("Comparing prices $expected(expected) -> $actual(actual)")
                BigDecimal actualVal = getBigDecimal(actual)
                BigDecimal expectedVal = getBigDecimal(expected)
                softAssert.assertTrue(actualVal.compareTo(expectedVal) == 0, "$message $expectedVal(expected) != $actualVal(actual)\n")
            } catch (Exception e) {
                print("Failed to compare Price got exception caused by: " + e.getCause())
                softAssert.assertNull(e, "$message $expected(expected) != $actual(actual)\n")
            }
        }
    }

    protected void assertCommission(String actual, String expected, String message) {
        BigDecimal expectedVal = getBigDecimal(expected)
        if (!expectedVal == BigDecimal.ZERO) {
            if (actual == null || expected == null) {
                softAssert.assertTrue(false, "Unable to compare as one of the value is null $expected(expected) != $actual(actual) -- $message\n")
            } else {
                try {
                    println("Comparing Commission $expected(expected) -> $actual(actual)")
                    BigDecimal actualVal = getBigDecimal(actual)

                    softAssert.assertTrue(actualVal.compareTo(expectedVal) == 0, "$message $expectedVal(expected) != $actualVal(actual)\n")
                } catch (Exception e) {
                    print("Failed to compare Commission got exception caused by: " + e.getCause())
                    softAssert.assertNull(e, "$message $expected(expected) != $actual(actual)\n")
                }
            }
        }
    }

    private static BigDecimal getBigDecimal(String amt) {
        amt = amt.replaceAll(",", "").replaceAll(" ", "")
        BigDecimal value = amt.toBigDecimal()
        return value
    }

    protected void loadCoherenceData(PricingSearchData searchData) {
        int checkIn = searchData.checkInDaysToAdd
        int checkOut = searchData.checkOutDaysToAdd
        def checkInDate = new Date() + checkIn
        def checkOutDate = new Date() + checkOut
        String propertyId = searchData.propertyId
        String clientId = activeClientData.clientId
        String siteId = activeClientData.siteId
        String currency = activeClientData.currency
        String nationalityCode = activeClientData.nationalityCode
        String guestsRoom1Adults = searchData.guestsRoom1Adults
        List<Integer> childAges = searchData.childrenAges

        /*  Loading Coherence data */
        def roomRequirements = [new RoomRequirement(guestsRoom1Adults.toInteger()).withChildAges(childAges)]
        def priceQueryBuilder = new PriceSearchQueryBuilder()
                .addId(propertyId.toLong())
                .withCheckInDate(checkInDate).withCheckOutDate(checkOutDate)
                .withCurrency(currency).withCountry(nationalityCode).withSiteId(siteId.toInteger()).withClientId(clientId.toLong())
                .addRoomRequirements(roomRequirements).withAutoEmail()
        CoherenceResponse coherenceData = getCoherenceData(priceQueryBuilder, propertyId, searchData)
        if (searchData.hotelExpected) {
            assertNotNull("Failed to fetch data from Coherence Web UI API", coherenceData)
            println("Rooms :- " + coherenceData.priceMap.keySet())
        }
        cohPriceMap = coherenceData.priceMap
        cohCommissionMap = coherenceData.commissionMap
        cohCancellationChargeMap = coherenceData.cancellationMap
        cohPriceBreakdownMap = coherenceData.roomPricePerDay
    }

    protected void validateCancellationCharge(Map<String, String> dataMap, List<CancellationConditionCoh> cohValues) {
        println(cohValues)
        println(dataMap)
        cohValues.each { cohValue ->
            String dateKey = getDateString(cohValue)
            println(dateKey)
            String actualPrice = dataMap.get(dateKey)
            if (actualPrice != null) {
                actualPrice = actualPrice.replace(activeClientData.currency, "")
                assertPrice(actualPrice, cohValue.chargeAmount.toString(), "Cancellation charge does not match for $dateKey")
            } else {
                softAssert.assertNotNull(actualPrice, "Unable to find cancellation charge for $dateKey\n")
            }
        }
    }

    protected List mergeNoSHOW(List<CancellationConditionCoh> orgCoherenceValueList) {
        List<CancellationConditionCoh> coherenceValueList = orgCoherenceValueList.clone()
        def noShow = coherenceValueList.find { it.type.equals("NO_SHOW") }
        if (noShow) {
            def sDate = coherenceValueList.find {
                (it.startDate.equals(noShow.startDate) && !it.type.equals(noShow.type))
            }
            if(sDate) {
                noShow.startDate = sDate.endDate
                noShow.type = "NO_SHOW_EDITED"
                coherenceValueList.remove(sDate)
            }
        } else {
          CancellationConditionCoh  maxDate = coherenceValueList.max { a, b ->
              a.startDate.getTime()
          }
            maxDate.type = "NO_SHOW"
        }
        coherenceValueList
    }

    protected checkAndAddNoSHOW(List<CancellationConditionCoh> orgCoherenceValueList) {
        List<CancellationConditionCoh> coherenceValueList = orgCoherenceValueList.clone()
        def noShow = coherenceValueList.find { it.type.equals("NO_SHOW") }
        if (!noShow) {
            CancellationConditionCoh  maxDate = coherenceValueList.max { a, b ->
                a.startDate.getTime()
            }
            maxDate.type = "NO_SHOW"
        }
        coherenceValueList
    }

    def setupSpec() {
        try {
            dbUtil = new AS400Util()
        } catch (Exception e) {
            println "Unable to create AS400 connection"
        }
    }

    protected def loginToNova() {
        if (!isNovaSiteLoggedIn) {
            login(activeClientData)
//            isNovaSiteLoggedIn = true
        }
    }

    String getCurrentPageUrl() {
        def url = browser.driver.currentUrl
        println "Current Page URL: $url"
        return url
    }

    def verifyInTravellerSite(String iteneraryName, String expectedPrice) {
        logIntoTravellerSite()
        at ItineraryListPagePricing
        if (clickOnItinerary(iteneraryName)) {
            at ItineraryOverviewPage
            String cardPrice = getTripCardTotalPrice()
            cardPrice = cardPrice.substring(4, cardPrice.length())
            String headerPrice = tripPrice.text()
            headerPrice = headerPrice.substring(4, headerPrice.length())
            assertPrice(cardPrice, expectedPrice.toString(), "Price in Traveller site - Trip overview card does not match")
            assertPrice(headerPrice, expectedPrice.toString(), "Price in Traveller site - Trip overview header does not match")
        } else {
            softAssert.assertTrue(false, "Failed to find itinerary: $iteneraryName verification in traveller site failed");
        }
        at ItineraryListPagePricing
        clickHome()
    }

    private def logIntoTravellerSite() {
        if (!isTravellerSiteLoggedIn || isLoginPage()) {
            String email = getStringValue("travellerSite.email")
            String password = getStringValue("travellerSite.password")
            EmutripLoginPage.url = getStringValue("travellerSite.URL")
            to EmutripLoginPage
            login(email, password)
//            isTravellerSiteLoggedIn = true
        }
    }

    private boolean isLoginPage() {
        getTitle().contains("LoginPage")
    }

    def shareDetailsToTravelller() {
        at ItenaryPagePricing
        clickShareItineraryButtonAgentPos()
        waitFor { at ItinerarySharePagePricing }
        clickSaveButton()
        clickSaveAndPriviewButton()
        waitFor { at ItineraryPreviewPage }
        clickOnSendLink()
        clickOnSendButtonInSendLinkLightBox()
        clickOnClosePreviewButton()
    }
}