package com.kuoni.qa.automation.test.itinerarySearch

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.ItinerarySearchTestData
import com.kuoni.qa.automation.helper.ItinerarySearchTestResultData
import com.kuoni.qa.automation.page.ItinerarySearchPage
import org.junit.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Stepwise

import static com.kuoni.qa.automation.util.TestConf.itinerarySearch


class CreditItineraryAdvancedSearchOneTest extends ItinerarySearchBaseSpec {
    //Client Login
    @Shared
    ClientData clientData = new ClientData("client872")

    @Shared
    ItinerarySearchTestResultData resultData = new ItinerarySearchTestResultData()

    def "TC00: Destination search"() {
        given: "user is logged in and on hotel tab"
        ItinerarySearchTestData itinerarySearchData = new ItinerarySearchTestData("6102", "TS1")
        int count = itinerarySearchData.getScenarioTestCaseCount()
        login(clientData)
        Accesstoagentsite(defaultData, resultData)
        clickAdvanceSearchCheckBox(resultData)
        storeDefaultInputValuesAdvanceForm(resultData)
        advancedSearchForm(itinerarySearchData, true, resultData)
        checkSearchResultsReturned(resultData)
        advancedSearchAndStoreResults("6102", resultData, count)
    }


    def "TC01: Verify Access to agent site Actual & Expected Results"() {

        given: "User is logged in and present on Hotel Search"
        VerifyAccesstoagentsite(defaultData, resultData)
    }

    def "TC02: verifyDepatureDateDeafault"() {
        given: "User is logged in and present on itinerary search tab"
        verifyDepartureDateDefaultSearch(resultData)


    }

    //37 records dates: 1st march to 10th march
    def "TC03: verifyDepatureDateDeafault"() {
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData, 0)
        verifyCreditClientTableHeaders(resultData)
    }

     def "TC04: verifyDepatureDateGivenByUser"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,1)
        verifyCreditClientTableHeaders(resultData)

    }
    def "TC05: verifyDepatureDateGivenByUser"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,2)
        verifyCreditClientTableHeaders(resultData)

    }
    def "TC06: verifyDepatureDate&BookedStatusGivenByUser"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,3)
        verifyCreditClientTableHeaders(resultData)

    }

    //keeping it on hold as results not returned correctly
    def "TC07: verifyDepatureDate&PendingStatusGivenByUser"(){
        given: "User is on itinerary search results page"
        if( resultData.advancedSearch.searchResults.get("search4").toString().equals(" ")){
            verifySingleORMultiSearch(resultData,4)
            verifyCreditClientTableHeaders(resultData)
        }
        else{
            softAssert.fail("expected 13 results but redirected to itinerary page.")
        }

    }
    def "TC08: verifyDepatureDate&QuoteStatusGivenByUser"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,5)
        verifyCreditClientTableHeaders(resultData)

    }
    def "TC09: verifyDepatureDate&UnavailableStatusGivenByUser"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,6)
        verifyCreditClientTableHeaders(resultData)

    }
    def "TC10: verifyDepatureDate&CancelledStatusGivenByUser"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,7)
        verifyCreditClientTableHeaders(resultData)

    }
    def "TC11: verify DepatureDate -Status:Booked - Destination"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,8)
        verifyCreditClientTableHeaders(resultData)

    }
    def "TC12: verify DepatureDate -Status:Pending - Destination"(){
        given: "User is on itinerary search results page"
       // verifySingleORMultiSearch(resultData,11)
        assertionEquals(resultData.advancedSearch.searchResults.get("search9"),defaultData.expected.errorMsg,"Zero results not returned for current date")
    }
    def "TC13: verify DepatureDate -Status:Cancelled - Destination"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,10)
        verifyCreditClientTableHeaders(resultData)
    }
    def "TC14: verify DepatureDate -Status:unavailable - Destination"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,11)
        verifyCreditClientTableHeaders(resultData)
    }
    def "TC15: verify DepatureDate -Status:London - Quote - Destination"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,12)
        verifyCreditClientTableHeaders(resultData)
    }

//Test case failed, suppose to return records
    def "TC16: verify DepatureDate -Status:Quote - Destination"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,13)
        verifyCreditClientTableHeaders(resultData)
    }

    def "TC17: verify DepatureDate -Status:London - Booked - Destination"(){
        given: "User is on itinerary search results page"
        if( resultData.advancedSearch.searchResults.get("search14").toString().equals(" ")){
            verifySingleORMultiSearch(resultData,14)
            verifyCreditClientTableHeaders(resultData)
        }
        else{
            softAssert.fail("expected 13 results but redirected to itinerary page.")
        }


    }

    def "TC18: verify DepatureDate -Status:London - Pending - Destination"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,15)
        verifyCreditClientTableHeaders(resultData)
    }

    def "TC19: verify DepatureDate - AgentRef-Status:All - Destination(London)"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,16)
        verifyCreditClientTableHeaders(resultData)
    }
    def "TC20: verify DepatureDate - AgentRef-Status:Booked - Destination(London)"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,17)
        verifyCreditClientTableHeaders(resultData)
    }
    def "TC21: verify DepatureDate - AgentRef-Status:pending"(){
        given: "User is on itinerary search results page"
        assertionEquals(resultData.advancedSearch.searchResults.get("search18"),defaultData.expected.errorMsg,"Zero results not returned for current date")
    }
    def "TC22: verify DepatureDate(1-10) - AgentRef-Status:Booked - Destination(London)"(){
        given: "User is on itinerary search results page"
        verifySingleORMultiSearch(resultData,19)
        verifyCreditClientTableHeaders(resultData)
    }
    def "TC23: verify DepatureDate - AgentRef-Status:Booked -Destination(London) "(){
        given: "User is on itinerary search results page"
        assertionEquals(resultData.advancedSearch.searchResults.get("search20"),defaultData.expected.errorMsg,"Zero results not returned for current date")
    }
    def "TC24: verify DepatureDate - AgentRef-Status:All -Destination(London)"(){
        given: "User is on itinerary search results page"
        assertionEquals(resultData.advancedSearch.searchResults.get("search21"),defaultData.expected.errorMsg,"Zero results not returned for current date")
    }

    def "TC25: verify DepatureDate - AgentRef-Status:All"(){
        given: "User is on itinerary search results page"
        String itineraryId = resultData.advancedSearch.searchResults.get("search22").toString()
        softAssert.assertFalse(itineraryId.isEmpty(),"not navigated to itinerary page"+itineraryId)
    }
    def "TC26: verify DepatureDate - AgentRef-Status:All"(){
        given: "User is on itinerary search results page"
        String itineraryId = resultData.advancedSearch.searchResults.get("search23").toString()
        softAssert.assertFalse(itineraryId.isEmpty(),"not navigated to itinerary page"+itineraryId)
    }
}



