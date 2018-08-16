package com.kuoni.qa.automation.test.trustYou

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.helper.PropertySearchData
import com.kuoni.qa.automation.page.HotelInformationPage
import com.kuoni.qa.automation.page.hotel.AgentHotelInfoPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.test.search.booking.hotel.HotelBookingBaseSpec
import spock.lang.Shared

class HotelNonTrustYouTest extends HotelBookingBaseSpec {
    @Shared
    ClientData clientData = new ClientData("client854")
    @Shared
    PropertySearchData data = new PropertySearchData("Non_TY")
    @Shared
    HotelTransferTestResultData resultData = new HotelTransferTestResultData(data)


    def "TC00: Agent POS Trust You Hydbbrid content verification"() {
        given: "user logeed in"

        login(clientData)
        fillHotelSearchFormAndSubmit(data.input.city.toString(), data.input.cityAutoSuggest.toString(), data.input.checkInDaysToAdd.toString(), data.input.checkOutDaysToAdd.toString(), data.input.adults.toString(), data.input.child)
        at HotelSearchResultsPage
       resultData.hotel.searchResults.searchReturned = searchResultsReturned()
        enterHotelNameInFilterSection(data.input.property)
        clickOnFindButtonInFilters()
        waitTillLoadingIconDisappears()
        at AgentHotelInfoPage
        resultData.hotel.searchResults.footerStatus =turstYouFooter()
        HotelInformationPage
        clickOnHotelName(0)
        waitTillLoadingIconDisappears()
        at AgentHotelInfoPage
        resultData.hotel.hotelInfo.trustYouDispStatus = turstYouDispStatus()
        resultData.hotel.hotelInfo.footerStatus = turstYouFooter()
    }
    def "TC01: Non Trust you hotel - turst you fields verification PLP"(){
        given: "user is on PLP page"
        softAssert.assertTrue(resultData.hotel.searchResults.searchReturned,"serach not reuturned any values")
        softAssert.assertTrue( resultData.hotel.searchResults.footerStatus,"turst you footer not displayed")

    }
    def "TC02: Non Trust you hotel - turst you fields verification PDP"(){
        given: "user is on PDP page"
        softAssert.assertFalse(resultData.hotel.hotelInfo.trustYouDispStatus,"trust you star rating displayed")
        softAssert.assertTrue(resultData.hotel.hotelInfo.footerStatus,"turst you footer not displayed when trust you on")
    }
}
