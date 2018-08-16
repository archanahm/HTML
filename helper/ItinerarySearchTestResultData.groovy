package com.kuoni.qa.automation.helper

/**
 * Created by kmahas on 11/14/2017.
 */
class ItinerarySearchTestResultData {

    Map quickSearch = [:]
    Map advancedSearch = [:]

    String scenarioNum
    ItinerarySearchTestData itinerarySearchData

    ItinerarySearchTestResultData(String scenarioNum, ItinerarySearchTestData itinerarySearchData) {

        quickSearch.searchResults = [:]
        advancedSearch.searchResults = [:]

        //Code Author: Madhavi
        advancedSearch.searchFields = [:]
        //Code Author: Kranthi
        quickSearch.searchFields=[:]
        quickSearch.itineraryPage=[:]
        quickSearch.homePage=[:]
        quickSearch.searchResults.tableRows=[:]


        this.scenarioNum = scenarioNum
        this.itinerarySearchData = itinerarySearchData
    }

    ItinerarySearchTestResultData() {
        quickSearch.searchFields = [:]
        advancedSearch.searchResults = [:]
        advancedSearch.searchFields = [:]
    }
}
