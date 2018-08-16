package com.kuoni.qa.automation.test.pricing

import com.kuoni.qa.automation.helper.TestResultData
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.pricing.HotelInformationPagePricing
import com.kuoni.qa.automation.page.pricing.ItenaryPagePricing
import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Unroll

import java.text.SimpleDateFormat

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

//@Stepwise
abstract class HotelSearchBaseTest extends BasePricingSpec {

    public abstract def getTestDesc()

    public abstract def getDataNames()

    public static resultMap = [:]

    //R10 temp hack
    private mealBasisMap = ["Room Only": "None", "Room and Breakfast": "{0} Breakfast", "Room and Dinner": "{0} breakfast and dinner", "Full Board": "{0} breakfast, lunch and dinner"]

//    @IgnoreRest
    @Unroll
    def "01 : Hotel Search and Book  #searchData.caseName -#searchData.desc"() {
        given: "User is able to login"
        initValues()
        activeSearchData = searchData
        setCommission(searchData)
        println("\nRunning $tstDesc - ${searchData.caseName} - ${searchData.desc}")
        println("Client Data :$activeClientData")
        println("Search Data :$activeSearchData")
        String roomName = activeSearchData.expectedbookRoomName
        TestResultData resultData = new TestResultData()
        resultMap.put(searchData.caseName, resultData)
        def roomPriceMap = null
        def commissionMap = null
        def cancellationList = null
        def priceBreakdownMap = null
        if (searchData.hotelExpected && "".equals(searchData.mealBasisNotExpecting) && "".equals(searchData.roomNotExpecting)) {
//        if (searchData.hotelExpected) {
//        if(searchData.shouldContinueBooking) {
            loadCoherenceData(searchData)
            println("Coh: Price " + cohPriceMap.size())
            println("Coh: Commi from " + cohCommissionMap.size())
            println("Coh: Cancellation " + cohCancellationChargeMap.size())
            println("Coh: PriceBreakdown " + cohPriceBreakdownMap.size())
            if (searchData.hasPriceKey()) {
                String key = searchData.priceKey
                println("PriceKey : ${searchData.priceKey}")
                roomPriceMap = cohPriceMap.get(key)
                commissionMap = cohCommissionMap.get(key)
                cancellationList = cohCancellationChargeMap.get(key)
                priceBreakdownMap = cohPriceBreakdownMap.get(key)
            } else if (searchData.shouldCheckMealBasis()) {
                String name = "$roomName-${searchData.mealBasisKeyCoh}"
                println(name)
                roomPriceMap = cohPriceMap.get(name)
                commissionMap = cohCommissionMap.get(name)
                cancellationList = cohCancellationChargeMap.get(name)
                priceBreakdownMap = cohPriceBreakdownMap.get(name)
            } else {
                throw UnsupportedOperationException("we no longer get room by name")
                cohPriceMap.each { rName, priceMap -> if (rName.startsWith(roomName)) roomPriceMap = priceMap }
                cohCommissionMap.each { rName, commiMap -> if (rName.startsWith(roomName)) commissionMap = commiMap }
                cohCancellationChargeMap.each { rName, canMap -> if (rName.startsWith(roomName)) cancellationList = canMap }
            }
            assertNotNull("Failed to get price from Coherence for Room $roomName", roomPriceMap)
            String expectedRoomPrice = roomPriceMap.get("FINAL")
            String expectedWasPrice = roomPriceMap.get("WAS_PRICE")
            resultData.expectedPrice = expectedRoomPrice
            resultData.prices.expectedWasPrice = expectedWasPrice

            assertNotNull("Failed to get price from Coherence for Room $roomName", expectedRoomPrice)
        }

        loginToNova()

        fillSearchFormAndSubmit(searchData)

        at HotelInformationPagePricing
        if (!searchData.hotelExpected) {
            assertTrue("Hotel Search Results found. Expecting 0 Results", isZeroResults())
        } else if (!"".equals(activeSearchData.mealBasisNotExpecting) || !"".equals(activeSearchData.roomNotExpecting)) {
            assertFalse("Unexpected room:mealbasis \'${searchData.roomNotExpecting}:${searchData.mealBasisNotExpecting}\' found", checkMealBasisExists(searchData.roomNotExpecting, searchData.mealBasisNotExpecting))
        } else {
            assertFalse("Hotel Search Results not found. 0 Results", isZeroResults())
            if (!searchData.unExpectedPriceKey.isEmpty()) {
                def priceKeyMap = getRoomPOSWithPriceKey()
                resultData.prices.infoPage.priceKeyMap = priceKeyMap
            }
            Integer roomPos, mealBasisPos
            String key = roomName
            at HotelInformationPagePricing
            if (searchData.shouldCheckMealBasis()) {
                Map roomMealBasisPosMap
                if (searchData.hasPriceKey()) {
                    key = searchData.priceKey
                    List roomMealBasisPosList = getRoomPOSWithPriceKey(key)
                    roomPos = roomMealBasisPosList[0]
                    mealBasisPos = roomMealBasisPosList[1]
                } else {
                    roomMealBasisPosMap = getRoomPOSWithMealBasis()
                    key = searchData.mealBasisKeyUI
                    roomPos = roomMealBasisPosMap.findIndexOf { it.key == roomName }
//                    String mealBasis = searchData.mealBasisKeyUI
                    mealBasisPos = roomMealBasisPosMap.get(roomName).indexOf(key)
                    //R10 temp fix
                    if (mealBasisPos < 0) {
                        String tempKey = mealBasisMap.get(key)
                        mealBasisPos = roomMealBasisPosMap.get(roomName).indexOf(tempKey)
                    }
                }
//                roomPos = roomMealBasisPosMap.get(key)
            } else {
                throw UnsupportedOperationException("we no longer get room by name")
                roomPos = getRoomPos(roomName)
            }
            assertTrue("Unable to find room $key in search result.", roomPos >= 0)
            assertTrue("Unable to locate Meal Basis:$key", mealBasisPos >= 0)
            sleep(800)
            String rPrice = getPrice(roomPos, mealBasisPos).replace(activeClientData.currency, "")
            def r = (resultData.prices.hotelInfoPage = rPrice)

            if (activeSearchData.shouldCheckWasPrice()) {
                String wPrice = getWasPrice(roomPos, mealBasisPos)
                if (null != wPrice)
                    wPrice = wPrice[0..-4]
                resultData.prices.actualWasPrice = wPrice
            }
            if (activeSearchData.shouldCheckCommission()) {
                String expectedC = commissionMap.'COMMISSION_PERCENTAGE'.'total'
                String actualC = getCommission(roomPos, mealBasisPos)
                if (!actualC) actualC = ""
                actualC = actualC.replace("% Commission", "")
                resultData.prices.infoPage.commission.actual = actualC
                resultData.prices.infoPage.commission.expected = expectedC
            }
            if (activeSearchData.shouldCheckCancellationCharge()) {
                clickCancellationPolicyLink(roomPos, mealBasisPos)
                Map<String, String> dataMap = getCancellationPolicyLightBoxContent()
                resultData.prices.infoPage.cancellationCharge.actualMap = dataMap
                resultData.prices.infoPage.cancellationCharge.expectedList = cancellationList
                clickCloseCloseLightbox()
            }
            if (activeSearchData.shouldCheckPriceBreakdown()) {
                sleep(1000)
                Map priceBreakDown = getPriceBreakdowns(roomPos, mealBasisPos)
                resultData.prices.infoPage.priceBreakdownActual = priceBreakDown
                resultData.prices.infoPage.priceBreakdownExpected = priceBreakdownMap
            }

            sleep(1000)
            clickOnAddToItenary(roomPos, mealBasisPos)
/*
            sleep(5000)
            String itineraryPrice = getItineraryPrice(0)
            if (null != itineraryPrice)
                itineraryPrice = itineraryPrice[0..-4]
            resultData.prices.infoPage.itineraryCard.actual = itineraryPrice
            at ItenaryBuilderPage
            clickOnGotoItenaryButton()
*/
            at ItenaryPagePricing
            def email = searchData.passengerEmail
            enterPassengerDetails(searchData.guestsRoom1Adults, email)
            sleep(1000)
            enterChildDetails(activeSearchData.childrenAges, searchData.guestsRoom1Adults)
            clickonSaveButton()
/*
            sleep(2000)
            try {
                clickonSaveButton()
            } catch (Exception e) {
                println ("Unable to click save button")
            }
*/

            sleep(2000)
            String actualPrice = getPriceSuggestedItem(0)
            def itinerayPageMap = resultData.prices.itineraryPage
            itinerayPageMap.actual = actualPrice
            if (activeSearchData.shouldCheckCommission()) {
                String expectedC = commissionMap.'COMMISSION_PERCENTAGE'.'total'
                println("Comparing commission in Itinerary page")
                sleep(1000)
                String actualC = getCommissionPercentage(roomPos).replace("Commission ", "").replace("%", "")
                itinerayPageMap.commission.actual = actualC
                itinerayPageMap.commission.expected = expectedC
            }
            if (activeSearchData.shouldCheckCancellationCharge()) {
                clickSuggestedItemCancellationLink(0)
                Map<String, String> dataMap = getCancellationPolicyLightBoxContent()
                itinerayPageMap.cancellationCharge.actualMap = dataMap
                itinerayPageMap.cancellationCharge.expectedList = cancellationList
                clickCloseLightbox()
            }
            sleep(2000)
            clickBookButton(0)
            sleep(1000)

            verifyTandCDisplayedBookingLightBox()

            def total = getItenaryTotal()
            resultData.prices.confirmationPage.actual = total
            if (activeSearchData.shouldCheckCommission()) {
                String expectedC = commissionMap.'COMMISSION'.'total'
                String expectedCPercent = commissionMap.'COMMISSION_PERCENTAGE'.'total'
                resultData.prices.confirmationPage.commisssionTotal.expected = expectedC
                resultData.prices.confirmationPage.commisssionTotalPercent.expected = expectedCPercent

                String actualC = ""
                try {
                    actualC = getCommissionAmountInTAndC()
                    if (null != actualC)
                        actualC = actualC[0..-4]
                } catch (Exception e) {
                    println("Unable to read CommissionAmount from UI")
                    actualC = "Faild To read from UI"
                }
                String actualCPercent = getCommissionPctInTAndC().replace("Commission ", "").replace("%", "")

                resultData.prices.confirmationPage.commisssionTotal.actual = actualC
                resultData.prices.confirmationPage.commisssionTotalPercent.actual = actualCPercent
            }

/*
            for (i in 1..searchData.guestsRoom1Adults + searchData.childrenAges.size())
                clickOnTravellerTandCBookingLightBox(i - 1)
*/
            clickConfirmBooking()
            int bookingWaitTime = 60
            try {
                waitForBookingConfirmation(bookingWaitTime)
            } catch (Exception e) {
                assertFalse("Failed to confirm booking after waiting for $bookingWaitTime seconds", true)
            }
            String bookingStatus = getBookingStatus()
            println("Booking Status :- $bookingStatus")

            def confTotal = getBookingConfirmationTotal()
            resultData.prices.bookingResult.actual = confTotal
            def bookingId = getBookingId()
            def itineraryId = getItineraryId()
            String tripName = getTripName(itineraryId)
            println("Trip Name :- $tripName")
            if (activeSearchData.shouldCheckCommission()) {
                String expectedC = commissionMap.'COMMISSION'.'total'
                println("Comparing commission in confirmation page")
                String actualC = getCommissionAmountInConfirmedPage()
                if (actualC) actualC = actualC[0..-4]
                resultData.prices.bookingResult.commisssionTotalPercent.expected = expectedC
                resultData.prices.bookingResult.commisssionTotalPercent.actual = actualC
            }
            resultData.setBookingId(bookingId).setTripDesc(tripName)
            sleep(1000)
            coseBookItenary()
            if (activeSearchData.shouldCheckCancellationCharge()) {
                clickBookedItemCancellationLink(0)
                Map<String, String> dataMap = getCancellationPolicyLightBoxContent()
                resultData.prices.bookingResult.cancellationCharge.actualMap = dataMap
                resultData.prices.bookingResult.cancellationCharge.expectedList = cancellationList
                clickCloseLightbox()
            }

            // Dont edit and share as traveller site verification is disabled
//        editTripName(tripName)
//        shareDetailsToTravelller()

//        at ItenaryPagePricing
//        clickOnHome()
//        closeItinerary()
        }
        where:
        searchData << loadData(dataNames, isNotIgnored)
        tstDesc << testDesc

    }

    @Unroll
    def "02 : Verify price in hotel search info page : #searchData.caseName"() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE")
        TestResultData resultData = resultMap.get(searchData.caseName)
        when: "Expected and actual price are available"
        String expectedPrice = resultData.expectedPrice
        String actualPrice = resultData.prices.hotelInfoPage

        then: "Price in hotel info page is same"
        assertNotNull("Failed to get price from Coherence", expectedPrice)
        assertPrice(actualPrice, expectedPrice, "Room price in hotel information page is not same.")
        if (searchData.shouldCheckWasPrice()) {
            String expectedWasPrice = resultData.prices.expectedWasPrice
            String actualWasPrice = resultData.prices.actualWasPrice
            assertNotNull("Failed to get price from Coherence", expectedWasPrice)
            if (!actualWasPrice) {
                softAssert.assertNotNull(actualWasPrice, "Unable to read was price from UI $expectedWasPrice(expected) != $actualWasPrice(actual)\n")
            } else {
                assertPrice(actualWasPrice, expectedWasPrice, "Was price does not match in Hotel Information Page")
            }
        }
        if (searchData.shouldCheckCommission()) {
            String actualC = resultData.prices.infoPage.commission.actual
            String expectedC = resultData.prices.infoPage.commission.expected
            assertNotNull("Failed to get commission from Coherence", expectedC)
            assertCommission(actualC, expectedC, "Commission Percentage does not match")
        }
        if (searchData.shouldCheckCancellationCharge()) {
            Map actualMap = resultData.prices.infoPage.cancellationCharge.actualMap
            List coherenceValueList = resultData.prices.infoPage.cancellationCharge.expectedList
            coherenceValueList = checkAndAddNoSHOW(coherenceValueList)
            assertNotNull("Failed to get cancellation charge from Coherence", coherenceValueList)
            validateCancellationCharge(actualMap, coherenceValueList)
        }
        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yy")
        if (activeSearchData.shouldCheckPriceBreakdown()) {
            Map priceBreakDownA = resultData.prices.infoPage.priceBreakdownActual
            Map priceBreakDownE = resultData.prices.infoPage.priceBreakdownExpected
            priceBreakDownE.each { Calendar day, def expAmt ->
                String dateStr = sd.format(day.time)
                println(dateStr)
                String actAmt = priceBreakDownA.get(day.get(Calendar.DAY_OF_WEEK) - 1)
                assertPrice(actAmt, expAmt.toString(), "PriceBreakdown value for ${sdf.format(day.time)} does not match")
            }
        }
        where:
        searchData << loadData(dataNames, continueBooking)
    }

    @Unroll
    def "03 : Verify price in hotel search result Itinerary card: #searchData.caseName"() {
        TestResultData resultData = resultMap.get(searchData.caseName)
        when: "Expected and actual price are available"
        String actualPrice = resultData.prices.infoPage.itineraryCard.actual
        String expectedPrice = resultData.expectedPrice

        then: "Price in hotel info page is same"
        assertNotNull("Failed to get price from Coherence", expectedPrice)
//        assertPrice(actualPrice, expectedPrice, "Room price after adding to Itinerary does not match.")

        and: "Verify any unexpected meal basis is present"
        if (!searchData.unExpectedPriceKey.isEmpty()) {
            def uiPriceKeys = resultData.prices.infoPage.priceKeyMap.values()
            searchData.unExpectedPriceKey.each { key ->
                softAssert.assertNull(uiPriceKeys.find { it.contains(key) }, "Unexpected price key found : $key\n")
            }
        }
        where:
        searchData << loadData(dataNames, continueBooking)
    }

    @Unroll
    def "04 : Verify price after adding traveller details: #searchData.caseName"() {
        TestResultData resultData = resultMap.get(searchData.caseName)
        when: "Expected and actual price are available"
        String expectedPrice = resultData.expectedPrice
        String actualPrice = resultData.prices.itineraryPage.actual

        then: "Price in hotel info page is same"
        assertNotNull("Failed to get price from Coherence", expectedPrice)
        assertPrice(actualPrice, expectedPrice, "Room price in Itinerary page not match.")

        if (searchData.shouldCheckCommission()) {
            String actualC = resultData.prices.itineraryPage.commission.actual
            String expectedC = resultData.prices.itineraryPage.commission.expected
            assertNotNull("Failed to get commission from Coherence", expectedC)
            assertCommission(actualC, expectedC, "Commission Percentage in itinerary page does not match")
        }
        if (searchData.shouldCheckCancellationCharge()) {
            Map actualMap = resultData.prices.itineraryPage.cancellationCharge.actualMap
            List coherenceValueList = resultData.prices.itineraryPage.cancellationCharge.expectedList
            coherenceValueList = checkAndAddNoSHOW(coherenceValueList)
            assertNotNull("Failed to get cancellation charge from Coherence", coherenceValueList)
            validateCancellationCharge(actualMap, coherenceValueList)
        }
        where:
        searchData << loadData(dataNames, continueBooking)
    }

    @Unroll
    def "05 : Verify price in booking T&C page: #searchData.caseName"() {
        TestResultData resultData = resultMap.get(searchData.caseName)
        when: "Expected and actual price are available"
        String expectedPrice = resultData.expectedPrice
        String actualPrice = resultData.prices.confirmationPage.actual
        assertNotNull("Failed to get price from Coherence", expectedPrice)
        actualPrice = actualPrice.replaceAll(activeClientData.currency.toUpperCase(), "")

        then: "Price in hotel info page is same"
        assertPrice(actualPrice, expectedPrice, "Total price in Itinerary page not match.")
        if (searchData.shouldCheckCommission()) {
            String actualC = resultData.prices.confirmationPage.commisssionTotal.actual
            String expectedC = resultData.prices.confirmationPage.commisssionTotal.expected
            assertNotNull("Failed to get commission from Coherence", expectedC)
            assertCommission(actualC, expectedC, "Commission amount in T&C page does not match")

            actualC = resultData.prices.confirmationPage.commisssionTotalPercent.actual
            expectedC = resultData.prices.confirmationPage.commisssionTotalPercent.expected
            assertNotNull("Failed to get commission percentage from Coherence", expectedC)
            assertCommission(actualC, expectedC, "Commission percentage in T&C page does not match")
        }
        where:
        searchData << loadData(dataNames, continueBooking)
    }

    @Unroll
    def "06 : Verify price in booking result page: #searchData.caseName"() {
        TestResultData resultData = resultMap.get(searchData.caseName)
        when: "Expected and actual price are available"
        String expectedPrice = resultData.expectedPrice
        String actualPrice = resultData.prices.bookingResult.actual

        then: "Price in hotel info page is same"
        assertNotNull("Failed to get price from Coherence", expectedPrice)
        assertPrice(actualPrice, expectedPrice, "Total price in Booking confirmation page not match")
        if (searchData.shouldCheckCommission()) {
            String actualC = resultData.prices.bookingResult.commisssionTotalPercent.actual
            if (null != actualC) actualC = actualC.replace('Commission ', "").trim()
            String expectedC = resultData.prices.bookingResult.commisssionTotalPercent.expected
            assertNotNull("Failed to get price from Coherence", expectedC)
            assertCommission(actualC, expectedC, "Commission Percentage in booking result page does not match")
        }
        if (searchData.shouldCheckCancellationCharge()) {
            Map actualMap = resultData.prices.bookingResult.cancellationCharge.actualMap
            List coherenceValueList = resultData.prices.bookingResult.cancellationCharge.expectedList
            coherenceValueList = mergeNoSHOW(coherenceValueList)
            assertNotNull("Failed to get cancellation charge from Coherence", coherenceValueList)
            validateCancellationCharge(actualMap, coherenceValueList)
        }
        where:
        searchData << loadData(dataNames, continueBooking)
    }

    @Ignore
    @Unroll
    def "07 : Verify booking details in traveller site : #searchData.caseName"() {
        TestResultData resultData = resultMap.get(searchData.caseName)
        given: "User is able to login"
        String tripName = resultData.tripDesc
        String expectedPrice = resultData.expectedPrice
        assertNotNull("Failed to get price from Coherence", expectedPrice)
        assertNotNull("Failed to get tripName", tripName)

        null == verifyInTravellerSite(tripName, expectedPrice)

        where:
        searchData << loadData(dataNames, continueBooking)
    }

    @Unroll
    def "08 : Verify amount in CBS : #searchData.caseName"() {
        TestResultData resultData = resultMap.get(searchData.caseName)
        given: "User is able to login"
        String expectedPrice = resultData.expectedPrice
        String bookingId = resultData.bookingId
        assertNotNull("Failed to complete booking", bookingId)
        assertFalse("Failed read booking id from UI", bookingId.equals(""))
        assertNotNull("Unable to connect to AS400", dbUtil)
        String actualPrice = dbUtil.getBookingAmount(activeClientData.siteId, bookingId)
        assertNotNull("Failed to get price from UI", expectedPrice)
        assertNotNull("Failed to get price from CBS", actualPrice)

        assertPrice(actualPrice, expectedPrice, "Total price from CBS system does not match")

        where:
        searchData << loadData(dataNames, continueBooking)
    }

    @Shared
    def static continueBooking = { srchData -> srchData.shouldContinueBooking }

    @Shared
    def static isNotIgnored = { srchData -> !srchData.ignored }


}