package com.kuoni.qa.automation.test.itinerarySearch

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.ItinerarySearchTestData
import com.kuoni.qa.automation.helper.ItinerarySearchTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItinerarySearchPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.Shared

import static com.kuoni.qa.automation.util.TestConf.clientData
import static com.kuoni.qa.automation.util.TestConf.itinerarySearch

/**
 * Created by kmahas on 11/14/2017.
 */
abstract class ItinerarySearchBaseSpec extends BaseSpec{


    DateUtil dateUtil = new DateUtil()
    @Shared
    public ItinerarySearchTestData defaultData = new ItinerarySearchTestData("default", "itinerarySearch",itinerarySearch)

    //Variable Declaration Madhavi's


    //Variable Declaration Kranthii's



    //Methods Author: Madhavi

    def storeDefaultInputValuesAdvanceForm(ItinerarySearchTestResultData resultData){
        at ItinerarySearchPage
        //Storing search fields default values
        resultData.advancedSearch.searchFields.defaultItemStatus = getDefaultItemStatusValue()
        resultData.advancedSearch.searchFields.fromDate = getFromDateDefaultValue()
        resultData.advancedSearch.searchFields.toDate = getToDateDefaultValue()
        resultData.advancedSearch.searchFields.departureDateStatus = selectDepartureCheckBoxStatus()
    }
    def clickAdvanceSearchCheckBox(ItinerarySearchTestResultData resultData){
        at ItinerarySearchPage
        //Click on advanced tab
        clickOnAdvancedSearch()
        resultData.advancedSearch.searchResults.searchDisplayedStatus = getAdvancedSearchFormStatus()
    }
        def advancedSearchForm(ItinerarySearchTestData itinerarySearchData,boolean flag,ItinerarySearchTestResultData resultData){
            at ItinerarySearchPage
            waitTillLoadingIconDisappears()
            sleep(2000)
            enterAgentRef(itinerarySearchData.input.agentRef==null?"":itinerarySearchData.input.agentRef)
            enterDestination(itinerarySearchData.input.destination==null?"":itinerarySearchData.input.destination)
            selectAutoSuggest(itinerarySearchData.input.autoSuggest==null?"":itinerarySearchData.input.autoSuggest)
            selectStatus(itinerarySearchData.input.status==null?"All":itinerarySearchData.input.status)
            if(flag.equals(true)){
               // selectDepartureCheckBox()
                resultData.advancedSearch.searchFields.departureDateStatus = selectDepartureCheckBoxStatus()
            }
            else{
                selectCreationDateCheckBox()
            }
            if(itinerarySearchData.input.fromDate != null){
                enterFromDate(itinerarySearchData.input.fromDate);
            }
            if(itinerarySearchData.input.toDate != null){
                enterToDate(itinerarySearchData.input.toDate);
                sleep(1000)
            }
            resultData.advancedSearch.searchFields.findbuttonStatus = getFindButtonEnabledOrDisabledStatus(1)
            sleep(2000)
            clickFindButtonItinerarySearch(1)
            waitTillLoadingIconDisappears()
        }
    def enterFromAndToDate(boolean flag,ItinerarySearchTestData itinerarySearchData){
        at ItinerarySearchPage
        if(flag.equals(true)){
            enterFromDate(itinerarySearchData.input.search1.get(0))
            enterToDate(itinerarySearchData.input.search1.get(1));
        }
        else{
            selectCreationDateCheckBox()
            enterFromDate(itinerarySearchData.input.search2.get(0))
            enterToDate(itinerarySearchData.input.search2.get(1));
        }
        clickFindButtonItinerarySearch(1)
    }
    def checkSearchResultsReturned(ItinerarySearchTestResultData resultData){
        at ItinerarySearchPage
        resultData.advancedSearch.searchResults.erroMsg = getErrorMsgTxt()
    }
    List<List<String>> getExpectedList(ItinerarySearchTestData itinerarySearchData){
        List<List<String>> rowList = []
        itinerarySearchData.testCaseName.size()

        String key = itinerarySearchData.expected.keySet().getAt(0)

        for(Map.Entry<String, List<List>> entry : itinerarySearchData.expected){
            if(entry.key.contains("testCase")){
                int rowSize = entry.value.size()
                int columnSize = entry.value.getAt(0).size()
                for(int i=0;i<rowSize;i++){
                    List<String> colList = []
                    for (int j = 0; j < columnSize; j++) {
                        colList.add(entry.value.get(i).get(j))
                    }
                    rowList.add(colList)
                    println(rowList)
                }
            }
        }
        return rowList
    }

    List<ItinerarySearchTestData> addObjects(String scenarioNum,int tCcount){
        List<ItinerarySearchTestData> listObj = new ArrayList<ItinerarySearchTestData>()

        for(int i=2;i<=tCcount;i++){
            listObj.add(new ItinerarySearchTestData(scenarioNum, "TS" + i))
        }
        return listObj
    }
    def advancedSearchAndStoreResults(String scenarioNum, ItinerarySearchTestResultData resultData,int tCcount){
        at ItinerarySearchPage
        List<ItinerarySearchTestData> listObj = addObjects(scenarioNum,tCcount)
        for(int i=0;i<listObj.size();i++) {
            //    if(i==25) {
                        advancedSearchForm(listObj.get(i), true, resultData)
                        String search = "search" + i;
                        try {
                            if (getZeroResultsStatus()) {
                                resultData.advancedSearch.searchResults.put(search, getErrorMsgTxt())
                            } else if (getSearchResultsCount() > 20) {
                                resultData.advancedSearch.searchResults.put(search, searchResultsFromNoOfPages())
                                at ItineraryTravllerDetailsPage
                            }
                            else if (verifyEditButtonDisplayed()) {
                                at ItineraryTravllerDetailsPage
                                String completeItineraryTxt = getItineraryIdTextAfterBooking().replaceAll("# ", "")
                                List<String> itineraryIdTxt = completeItineraryTxt.tokenize(" ")
                                resultData.advancedSearch.searchResults.put(search, itineraryIdTxt.getAt(0))
                                driver.navigate().back()
                                waitTillLoadingIconDisappears()
                            } else {
                                at ItinerarySearchPage
                                resultData.advancedSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()
                                resultData.advancedSearch.searchResults.put(search, getSearchResultsRows())
                            }
                            at ItinerarySearchPage
                            String count = "count" + i
                            resultData.advancedSearch.searchResults.put(count, getSearchResultsCount())
                            String expectedData = "expectedData" + i
                            resultData.advancedSearch.searchResults.put(expectedData, getExpectedList(listObj.get(i)))
                            String expectedCount = "expectedCount" + i
                            resultData.advancedSearch.searchResults.put(expectedCount, listObj.get(i).expected.expectedCount)
                        }
                        catch (Exception e) {
                        }
                    /*   break
        }*/
}

    }

    List<List<String>>  searchResultsFromNoOfPages(){
        at ItinerarySearchPage
        List<List<String>> rowList = []
        int pageCount = getPaginationPageCount()
        for(int i=0;i<pageCount;i++){
          rowList.addAll(getSearchResultsRows())
            if(nextButtonEnabledStatus()){
                clickNextOrPreviousBtnInQuickSearchResults(1)
            }

        }
        return rowList
    }
    def storeSearchPaginationResults(int topOrBottomIndex, int num){
        at ItinerarySearchPage
        def dataMap = [:]
        dataMap.count = getSearchResultsCount()
        dataMap.tableHeaders = getSearchResultsTableHeaders()
        dataMap.verifyHighlightedDepatureLabel = verifyHighlightedLabel()
        dataMap.getResultsLabel = getResultsLabel()
        dataMap.defaultResultCount = getResultsCountStatus()
        dataMap.leftNavButton = getPaginationLinkStatus(topOrBottomIndex, num)
        dataMap.rightNavButton = getPaginationLinkStatus(topOrBottomIndex,num+1)
        dataMap.getPageLinkNum1 = getPageLinkNum(topOrBottomIndex,num)
        dataMap.getPageLinkNum2 = getPageLinkNum(topOrBottomIndex,num+1)
        dataMap.getPageLinkNum3 = getPageLinkNum(topOrBottomIndex,num+2)
        dataMap.getCurrentLinkNumColor = getCurrentLinkNumColor(topOrBottomIndex,num)
        dataMap.getPageLinkColor2 = getLinkNumColor(topOrBottomIndex,num+1)
        dataMap.getPageLinkColor3 = getLinkNumColor(topOrBottomIndex,num+2)

        return  dataMap
    }
    def storePaginationResults2(int topOrBottomIndex, int num){
        at ItinerarySearchPage
        def dataMap = [:]
        clickPageLink(topOrBottomIndex,num+1)
        dataMap.pageNumUpdatingStatus = getPageNumber()
        dataMap.getCurrentLinkNum2Color = getCurrentLinkNumColor(topOrBottomIndex,num+1)
        dataMap.getPageLink1Color = getLinkNumColor(topOrBottomIndex,num)
        dataMap.navNextButtonDisabledStatus = navigationButtonEnabledOrDisabledStatus(topOrBottomIndex,num+1)
        dataMap.navPreButtonEnabledStatus = navigationButtonEnabledOrDisabledStatus(topOrBottomIndex,num)
        clickOnnextOrPrevButton(topOrBottomIndex,num)
        dataMap.getUpdatedPageLinkNum1 = getPageLinkNum(topOrBottomIndex,num)
        clickOnnextOrPrevButton(topOrBottomIndex,num+1)
        dataMap.getPageUpdtedLinkNum2 = getPageLinkNum(topOrBottomIndex,num+1)


        return dataMap
    }
    /***************Validations*******************/

    def verifyDepartureDateDefaultSearch(ItinerarySearchTestResultData resultData){

        ItinerarySearchTestData itinerarySearchData = new ItinerarySearchTestData("6102", "TS1")
        softAssert.assertTrue(resultData.advancedSearch.searchResults.searchDisplayedStatus,"advance serach form not displayed.")
        softAssert.assertTrue( resultData.advancedSearch.searchFields.departureDateStatus,"Departure date checkbox not displayed  by default")
        assertionEquals(resultData.advancedSearch.searchFields.defaultItemStatus,itinerarySearchData.expected.defaultItemStatus,"item status by default not All")
        assertionEquals(resultData.advancedSearch.searchFields.fromDate,dateUtil.addDays(itinerarySearchData.expected.currentDateDays.toString().toInteger()),"current date not displayed in from input field")
        assertionEquals(resultData.advancedSearch.searchFields.toDate,dateUtil.addDays(itinerarySearchData.expected.futureDateDays.toString().toInteger()),"future 1month date not displayed")
        softAssert.assertTrue(resultData.advancedSearch.searchFields.findbuttonStatus,"find button not enabled")
        assertionEquals(resultData.advancedSearch.searchResults.erroMsg,itinerarySearchData.expected.errorMsg,"Zero results not returned for current date")
    }

    def verifySingleORMultiSearch(ItinerarySearchTestResultData resultData,int testCaseNum){
        String actualCount = "count" + testCaseNum
        String expectedCount = "expectedCount" + testCaseNum
        String expectedResult = "expectedData" + testCaseNum
        String actualSearch = "search" + testCaseNum
        assertionEquals(resultData.advancedSearch.searchResults.get(actualCount).toString(),resultData.advancedSearch.searchResults.get(expectedCount).toString(),"search results count not matching")
        assertList(resultData.advancedSearch.searchResults.get(actualSearch),resultData.advancedSearch.searchResults.get(expectedResult),"search results returned not matched")
    }
    def verifyCreditClientTableHeaders(ItinerarySearchTestResultData resultData){
        assertList(resultData.advancedSearch.searchResults.tableHeaders,defaultData.expected.tableHeaders_CreditClient,"table headers not matching")
    }

    //Methods Author: Kranthi
    protected def Accesstoagentsite(ItinerarySearchTestData defaultData, ItinerarySearchTestResultData resultData){

        at HotelSearchPage
        clickOnSearchToolTab(defaultData.input.itineraryTabTxt)
        at ItinerarySearchPage
        //Itinerary search should display
        resultData.quickSearch.searchFields.itinerarySearchDispStatus=verifyItinerarySearchDisplayed()
        //quick search should be selected and displayed
        resultData.quickSearch.searchFields.quickSearchSelectStatus=getQuickSearchCheckedOrUncheckedStatus()

        //Place cursor on top of find an itineray icon
        mouseOverOnFindAnItineraryIcon()
        //tooltip displaying text:
        resultData.quickSearch.searchFields.toolTipText=getFindAnItineraryToolTipTxt()
    }

    protected def ItineraryIDFullLegacybooking(String ItineraryId,ItinerarySearchTestResultData resultData){
        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId,resultData)

        at ItineraryTravllerDetailsPage

        //User taken to itinerary page of: L63507016
        String completeItineraryTxt=getItineraryIdTextAfterBooking().replaceAll("# ","")
        List<String> itineraryIdTxt=completeItineraryTxt.tokenize(" ")

        resultData.quickSearch.itineraryPage.itineraryIdFullLeagcyBookingTxt=itineraryIdTxt.getAt(0)

        sleep(3000)
        //Click Done Button on Itinerary Page
        clickOnCloseItineraryButton()
        waitTillLoadingIconDisappears()
        sleep(3000)
        at HotelSearchResultsPage
        //User is taken to client home page
        resultData.quickSearch.homePage.welcomeTxt=getWelcomeTextInHomePage()

    }

    protected def ItineraryIDFullNovabooking(String ItineraryId,ItinerarySearchTestResultData resultData){

        at HotelSearchResultsPage
        clickOnSearchToolTab(defaultData.input.itineraryTabTxt)

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId,resultData)

        at ItineraryTravllerDetailsPage

        //User taken to itinerary page of: I719000410

        String completeItineraryTxt=getItineraryIdTextAfterBooking().replaceAll("# ","")
        List<String> itineraryIdTxt=completeItineraryTxt.tokenize(" ")

        resultData.quickSearch.itineraryPage.itineraryIdFullNovaBookingTxt=itineraryIdTxt.getAt(0)

        sleep(3000)
        //Click Done Button on Itinerary Page
        clickOnCloseItineraryButton()
        waitTillLoadingIconDisappears()
        sleep(3000)
        at HotelSearchResultsPage
        //User is taken to client home page
        resultData.quickSearch.homePage.welcomeText=getWelcomeTextInHomePage()

    }

    protected def ItinryIDFullNovabooking(String ItineraryId,ItinerarySearchTestResultData resultData){

        at HotelSearchResultsPage
        clickOnSearchToolTab(defaultData.input.itineraryTabTxt)

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId,resultData)

        at ItineraryTravllerDetailsPage

        //User taken to itinerary page of: I719000410

        String completeItineraryTxt=getItineraryIdTextAfterBooking().replaceAll("# ","")
        List<String> itineraryIdTxt=completeItineraryTxt.tokenize(" ")

        resultData.quickSearch.itineraryPage.itinryIdFullNovaBookingTxt=itineraryIdTxt.getAt(0)

        sleep(3000)
        //Click Done Button on Itinerary Page
        clickOnCloseItineraryButton()
        waitTillLoadingIconDisappears()
        sleep(3000)
        at HotelSearchResultsPage
        //User is taken to client home page
        resultData.quickSearch.homePage.welcomeText=getWelcomeTextInHomePage()

    }

    protected def ItineraryIDPartialLegacyBooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at HotelSearchResultsPage
        clickOnSearchToolTab(defaultData.input.itineraryTabTxt)

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expectedRowValues = []
        List actualRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()
            //All Row in Search results
            for(int i=1;i<=totalSearchResultsCount;i++){
                try{
                    actualRowValues.add(getSearchResultsRows(i))
                    expectedRowValues.add(data.expected.testCase05_RowValues.get(i-1))

                }catch(Exception e){
                    break
                }

            }

        resultData.quickSearch.searchResults.tableRows.put("actRowValues", actualRowValues)
        resultData.quickSearch.searchResults.tableRows.put("expRowValues", expectedRowValues)

    }


    protected def ItinryIDPartialLegacyBooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableColumnHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List exptdRowValues = []
        List actlRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                actlRowValues.add(getSearchResultsRows(i))

                exptdRowValues.add(data.expected.testCase06_RowValues.get(i-1))
            }catch(Exception e){
                break
            }

        }

        resultData.quickSearch.searchResults.tableRows.put("actlRowValues", actlRowValues)
        resultData.quickSearch.searchResults.tableRows.put("exptdRowValues", exptdRowValues)

    }


    protected def ItineraryIDPartialNovaBooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableColumnHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List exRowValues = []
        List acRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                acRowValues.add(getSearchResultsRows(i))

                exRowValues.add(data.expected.testCase07_RowValues.get(i-1))
            }catch(Exception e){
                break
            }

        }

        resultData.quickSearch.searchResults.tableRows.put("acRowValues", acRowValues)
        resultData.quickSearch.searchResults.tableRows.put("exRowValues", exRowValues)

    }

    protected def ItinryIDPartialNovaBooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableColumnHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List exRowValue = []
        List acRowValue = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                acRowValue.add(getSearchResultsRows(i))

                exRowValue.add(data.expected.testCase08_RowValues.get(i-1))
            }catch(Exception e){
                break
            }

        }

        resultData.quickSearch.searchResults.tableRows.put("acRowValue", acRowValue)
        resultData.quickSearch.searchResults.tableRows.put("exRowValue", exRowValue)

    }


    protected def ItinryIDPartlNovaBooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableColumnHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List exRowVal = []
        List acRowVal = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                acRowVal.add(getSearchResultsRows(i))

                exRowVal.add(data.expected.testCase09_RowValues.get(i-1))
            }catch(Exception e){
                break
            }


        }

        resultData.quickSearch.searchResults.tableRows.put("acRowVal", acRowVal)
        resultData.quickSearch.searchResults.tableRows.put("exRowVal", exRowVal)

    }


    protected def ItinryIDPartlNovaBookng(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableColumnHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expRowVal = []
        List actRowVal = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                actRowVal.add(getSearchResultsRows(i))

                expRowVal.add(data.expected.testCase10_RowValues.get(i-1))
            }catch(Exception e){
                break
            }


        }

        resultData.quickSearch.searchResults.tableRows.put("actRowVal", actRowVal)
        resultData.quickSearch.searchResults.tableRows.put("expRowVal", expRowVal)

    }

    protected def BookingIDfulllegacyAndNovabooking(String ItineraryId,ItinerarySearchTestResultData resultData){
        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId,resultData)

        at ItineraryTravllerDetailsPage

        //User taken to itinerary page of: L63507016
        String completeItineraryTxt=getItineraryIdTextAfterBooking().replaceAll("# ","")
        List<String> itineraryIdTxt=completeItineraryTxt.tokenize(" ")

        resultData.quickSearch.itineraryPage.bookingIdFullLeagcyAndNovaBookingTxt=itineraryIdTxt.getAt(0)

        sleep(3000)
        //Click Done Button on Itinerary Page
        clickOnCloseItineraryButton()
        waitTillLoadingIconDisappears()
        sleep(3000)
        at HotelSearchResultsPage
        //User is taken to client home page
        resultData.quickSearch.homePage.BookingIdwelcomeTxt=getWelcomeTextInHomePage()

    }

    protected def BookingIDPartialLegacyAndNovabooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at HotelSearchResultsPage
        clickOnSearchToolTab(defaultData.input.itineraryTabTxt)

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expectedBookingIdRowVal = []
        List actualBookingIdRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                actualBookingIdRowValues.add(getSearchResultsRows(i))
                expectedBookingIdRowVal.add(data.expected.testCase12_RowValues.get(i-1))
            }catch(Exception e){
                break
            }


        }

        resultData.quickSearch.searchResults.tableRows.put("actualBookingIdRowValues", actualBookingIdRowValues)
        resultData.quickSearch.searchResults.tableRows.put("expectedBookingIdRowVal", expectedBookingIdRowVal)

    }

    protected def BookingIDPartialLegacyAndNovabookings(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {

        at HotelSearchResultsPage
        clickOnSearchToolTab(defaultData.input.itineraryTabTxt)

        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expectedBookingIdRowVal = []
        List actualBookingIdRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()

        int remainder
        int quotient
        int pageCount
        int numberOfRecords
        if(totalSearchResultsCount>data.expected.recordsPerPage)
        {
            remainder=totalSearchResultsCount%data.expected.recordsPerPage
            quotient=totalSearchResultsCount/data.expected.recordsPerPage
            println("Remainder is "+remainder)
            println("Quotient is "+quotient)

            if(remainder ==0){
                pageCount==quotient
                println("Remainder 0"+pageCount)
            }
            if(remainder>1){
                pageCount=quotient+1
                println("Remainder greater than zero"+pageCount)
            }

        }
        for(int i=1;i<=pageCount;i++){
            numberOfRecords=getNoOfResultsInEachPage()
            for(int j=1;j<=numberOfRecords;j++){
                try{

                    actualBookingIdRowValues.add(getSearchResultsRows(j))
                   
                }catch(Exception e){
                    break
                }

            }
            if(i!=pageCount){
                clickNextOrPreviousBtnInQuickSearchResults(1)
                waitTillLoadingIconDisappears()
                sleep(2000)
            }

        }

        //All Expected Rows
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{

                expectedBookingIdRowVal.add(data.expected.testCase12_RowValues.get(i-1))
            }catch(Exception e){
                break
            }

        }

        resultData.quickSearch.searchResults.tableRows.put("actualBookingIdRowValues", actualBookingIdRowValues)
        resultData.quickSearch.searchResults.tableRows.put("expectedBookingIdRowVal", expectedBookingIdRowVal)

    }


    protected def BookingIDPartalLegacyAndNovabooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {


        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expBookingIdRowValues = []
        List actBookingIdRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                actBookingIdRowValues.add(getSearchResultsRows(i))

                expBookingIdRowValues.add(data.expected.testCase13_RowValues.get(i-1))
            }catch(Exception e){
                break
            }


        }

        resultData.quickSearch.searchResults.tableRows.put("actBookingIdRowValues", actBookingIdRowValues)
        resultData.quickSearch.searchResults.tableRows.put("expBookingIdRowValues", expBookingIdRowValues)

    }

    protected def TravellernamefulllegacyAndNovabooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {


        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expTNameRowValues = []
        List actTNameRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                actTNameRowValues.add(getSearchResultsRows(i))

                expTNameRowValues.add(data.expected.testCase14_RowValues.get(i-1))
            }catch(Exception e){
                break
            }



        }

        resultData.quickSearch.searchResults.tableRows.put("actTNameRowValues", actTNameRowValues)
        resultData.quickSearch.searchResults.tableRows.put("expTNameRowValues", expTNameRowValues)

    }

    protected def TravellernamepartiallegacyAndNovabooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {


        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expectedTNameRowValues = []
        List actualTNameRowValues = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                actualTNameRowValues.add(getSearchResultsRows(i))

                expectedTNameRowValues.add(data.expected.testCase15_RowValues.get(i-1))
            }catch(Exception e){
                break
            }


        }

        resultData.quickSearch.searchResults.tableRows.put("actualTNameRowValues", actualTNameRowValues)
        resultData.quickSearch.searchResults.tableRows.put("expectedTNameRowValues", expectedTNameRowValues)

    }

    protected def TravlrnamepartiallegacyAndNovabooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {


        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId, resultData)

        resultData.quickSearch.searchResults.tableHeaders = getSearchResultsTableHeaders()

        sleep(3000)

        List expectedTNameRowVal = []
        List actualTNameRowVal = []
        int totalSearchResultsCount=getSearchResultsCount()
        //All Row in Search results
        for(int i=1;i<=totalSearchResultsCount;i++){
            try{
                actualTNameRowVal.add(getSearchResultsRows(i))

                expectedTNameRowVal.add(data.expected.testCase16_RowValues.get(i-1))
            }catch(Exception e){
                break
            }



        }

        resultData.quickSearch.searchResults.tableRows.put("actualTNameRowVal", actualTNameRowVal)
        resultData.quickSearch.searchResults.tableRows.put("expectedTNameRowVal", expectedTNameRowVal)

    }

    protected def TravlrNamePartalLegacyAndNovabooking(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {


        at ItinerarySearchPage
        //Add entry to - Find an itinerary:
        enterItineraryIdOrBookingIdOrTravlrName(ItineraryId)
        sleep(5000)
        //<Find> button should disable
        resultData.quickSearch.searchFields.findBtnDisabledStatus=getFindButtonEnabledOrDisabledStatus()

        //Error Text shown:        Use only letters, numbers, and spaces. Do not start with a space.
        resultData.quickSearch.searchFields.findAndErrTxt=getFindAnItineraryErrorText()

    }

    protected def TravlrNamePartalLegacyAndNovabkng(String ItineraryId,ItinerarySearchTestData data,ItinerarySearchTestResultData resultData) {


        at ItinerarySearchPage
        //Add entry to - Find an itinerary:
        enterItineraryIdOrBookingIdOrTravlrName(ItineraryId)
        sleep(5000)
        //<Find> button should Enable
        resultData.quickSearch.searchFields.findButtonEnabledStatus=getFindButtonEnabledOrDisabledStatus()

        //Click Find
        clickFindButtonQuickSearch()
        waitTillLoadingIconDisappears()
        sleep(2000)
        //user taken to 0 results returned page correct.
        resultData.quickSearch.searchFields.zeroSearchResultsTxt=getZeroSearchResultsErrorText()

    }


    protected def TravellernamepartiallegacyAndNovabookingIgnoreSiteId(String ItineraryId,ItinerarySearchTestResultData resultData){
        at ItinerarySearchPage

        AddEntryToFindAnItineraryAndclickFindBtn(ItineraryId,resultData)

        at ItineraryTravllerDetailsPage

        //User taken to itinerary page of: L63507016
        String completeItineraryTxt=getItineraryIdTextAfterBooking().replaceAll("# ","")
        List<String> itineraryIdTxt=completeItineraryTxt.tokenize(" ")

        resultData.quickSearch.itineraryPage.itineraryIdIgnoreSiteIdTxt=itineraryIdTxt.getAt(0)

        sleep(3000)
        //Click Done Button on Itinerary Page
        clickOnCloseItineraryButton()
        waitTillLoadingIconDisappears()
        sleep(3000)
        at HotelSearchResultsPage
        //User is taken to client home page
        resultData.quickSearch.homePage.welcomeTxt=getWelcomeTextInHomePage()

    }


    protected def AddEntryToFindAnItineraryAndclickFindBtn(String id, ItinerarySearchTestResultData resultData){

        at ItinerarySearchPage
        //Add entry to - Find an itinerary:
        enterItineraryIdOrBookingIdOrTravlrName(id)
        //<Find> button should enable
        resultData.quickSearch.searchFields.findBtnEnabledStatus=getFindButtonEnabledOrDisabledStatus()
        //Click Find
        clickFindButtonQuickSearch()
        waitTillLoadingIconDisappears()
        sleep(2000)
    }

    /****** Validations *********/

    protected def "VerifyAccesstoagentsite"(ItinerarySearchTestData data, ItinerarySearchTestResultData resultData){

        //Itinerary search should display
        assertionEquals(resultData.quickSearch.searchFields.itinerarySearchDispStatus,data.expected.dispStatus,"Itinerary Search Display Status Actual & Expected don't match")

        //quick search should be selected and displayed
        assertionEquals(resultData.quickSearch.searchFields.quickSearchSelectStatus,data.expected.dispStatus,"Quick Search Display Checked Status Actual & Expected don't match")

        //tooltip displaying text:
        assertionEquals(resultData.quickSearch.searchFields.toolTipText,data.expected.findAnItineraryToolTipTxt,"Find An Itinerary Icon Tool Tip Text Actual & Expected don't match")


    }

    protected def "VerifyItineraryIDFullLegacybooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //User taken to Itinerary page of:
        assertionEquals(resultData.quickSearch.itineraryPage.itineraryIdFullLeagcyBookingTxt,data.input.itineraryIDFullLegacyBooking,"Itinerary ID Full leagcy booking, Itinerary Id Actual & Expected don't match")

        //User is taken to client home page
        assertionEquals(resultData.quickSearch.homePage.welcomeTxt,defaultData.expected.welcomeTxt,"Client Home Page Text Actual & Expected don't match")
    }

    protected def "VerifyItineraryIDFullNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //User taken to Itinerary page of:
        assertionEquals(resultData.quickSearch.itineraryPage.itineraryIdFullNovaBookingTxt,data.input.itineraryIDFullNovaBooking,"Itinerary ID Full Nova booking, Itinerary Id Actual & Expected don't match")

        //User is taken to client home page
        assertionEquals(resultData.quickSearch.homePage.welcomeText,defaultData.expected.welcomeTxt,"Client Home Page Text Actual & Expected don't match")
    }


    protected def "VerifyItinryIDFullNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //User taken to Itinerary page of:
        assertionEquals(resultData.quickSearch.itineraryPage.itinryIdFullNovaBookingTxt,data.input.itinryIDFullNovaBooking,"Itinerary ID Full Nova booking 4th TC, Itinerary Id Actual & Expected don't match")

        //User is taken to client home page
        assertionEquals(resultData.quickSearch.homePage.welcomeText,defaultData.expected.welcomeTxt,"Client Home Page Text Actual & Expected don't match")
    }

    protected def "VerifyItineraryIDPartialLegacyBooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actRowValues")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("expRowValues")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 05  actual & expected don't match")

    }

    protected def "VerifyItinryIDPartialLegacyBooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableColumnHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actlRowValues")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("exptdRowValues")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 06  actual & expected don't match")

    }

    protected def "VerifyItineraryIDPartialNovaBooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableColumnHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("acRowValues")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("exRowValues")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 07   actual & expected don't match")

    }

    protected def "VerifyItinryIDPartialNovaBooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableColumnHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("acRowValue")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("exRowValue")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 08   actual & expected don't match")

    }

    protected def "VerifyItinryIDPartlNovaBooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableColumnHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("acRowVal")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("exRowVal")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 09   actual & expected don't match")

    }
    protected def "VerifyItinryIDPartlNovaBookng"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableColumnHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actRowVal")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("expRowVal")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 10   actual & expected don't match")

    }

    protected def "VerifyBookingIDfulllegacyAndNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //User taken to Itinerary page of:
        assertionEquals(resultData.quickSearch.itineraryPage.bookingIdFullLeagcyAndNovaBookingTxt,data.expected.itineraryIDFullLegacyBooking,"Booking ID Full leagcy & Nova booking, Itinerary Id Actual & Expected don't match")

        //User is taken to client home page
        assertionEquals(resultData.quickSearch.homePage.BookingIdwelcomeTxt,defaultData.expected.welcomeTxt,"Client Home Page Text Actual & Expected don't match")
    }



    protected def "VerifyBookingIDPartialLegacyAndNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actualBookingIdRowValues")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("expectedBookingIdRowVal")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 12  actual & expected don't match")

    }

    protected def "VerifyBookingIDPartalLegacyAndNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actBookingIdRowValues")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("expBookingIdRowValues")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 13  actual & expected don't match")

    }
    protected def "VerifyTravellernamefulllegacyAndNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actTNameRowValues")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("expTNameRowValues")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 14  actual & expected don't match")

    }

    protected def "VerifyTravellernamepartiallegacyAndNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actualTNameRowValues")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("expectedTNameRowValues")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 15  actual & expected don't match")

    }

    protected def "VerifyTravlrnamepartiallegacyAndNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //Column Headers:
        assertionListEquals(resultData.quickSearch.searchResults.tableHeaders,data.expected.tableHeaders,"All column headers  actual & expected don't match")

        List actualRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("actualTNameRowVal")
        List expectedRetrievedRowData=resultData.quickSearch.searchResults.tableRows.get("expectedTNameRowVal")
        //Validate  All Rows in Search Results
        assertionListEquals(actualRetrievedRowData,expectedRetrievedRowData,"Itinerary Search Results, TC 16  actual & expected don't match")

    }

    protected def "VerifyTravlrNamePartalLegacyAndNovabooking"(ItinerarySearchTestData defaultData, ItinerarySearchTestResultData resultData){

        //Find Button Disabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnDisabledStatus,defaultData.expected.notDispStatus,"Itinerary Quick Search Find Button Disabled Status Actual & Expected don't match")

        ////Error Text shown:        Use only letters, numbers, and spaces. Do not start with a space.
        assertionEquals(resultData.quickSearch.searchFields.findAndErrTxt,defaultData.expected.errorText,"Error Text  actual & expected don't match")




    }

    protected def "VerifyTravlrNamePartalLegacyAndNovabkng"(ItinerarySearchTestData defaultData, ItinerarySearchTestResultData resultData){

        //Find Button Disabled Status
        assertionEquals(resultData.quickSearch.searchFields.findButtonEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //user taken to 0 results returned page correct
        assertionEquals(resultData.quickSearch.searchFields.zeroSearchResultsTxt,defaultData.expected.zeroSearchResultsTxt,"Zero Search Results Text  actual & expected don't match")




    }

    protected def "VerifyTravellernamepartiallegacyAndNovabookingIgnoreSiteId"(ItinerarySearchTestData defaultData, ItinerarySearchTestData data,ItinerarySearchTestResultData resultData){

        //Find Button Enabled Status
        assertionEquals(resultData.quickSearch.searchFields.findBtnEnabledStatus,defaultData.expected.dispStatus,"Itinerary Quick Search Find Button Enabled Status Actual & Expected don't match")

        //User taken to Itinerary page of:
        assertionEquals(resultData.quickSearch.itineraryPage.itineraryIdIgnoreSiteIdTxt,data.expected.travlrNameIdIgnoreSiteId,"Itinerary Id Ignore Site ID Parital Legacy & Nova Booking Actual & Expected don't match")

        //User is taken to client home page
        assertionEquals(resultData.quickSearch.homePage.welcomeTxt,defaultData.expected.welcomeTxt,"Client Home Page Text Actual & Expected don't match")
    }







}
