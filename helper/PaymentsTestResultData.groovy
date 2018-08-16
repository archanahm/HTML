package com.kuoni.qa.automation.helper

/**
 * Created by mtmaku on 4/13/2017.
 */
class PaymentsTestResultData {

    Map hotel = [:]
    Map transfer = [:]
    Map activity = [:]
    Map account = [:]
    String scenarioName
    PaymentsTestData paymentsData

    PaymentsTestResultData(String scenarioName, PaymentsTestData paymentsData) {

        hotel.search = [:]
        hotel.home = [:]
        hotel.searchResults = [:]
        hotel.itineraryPage = [:]
        hotel.itineraryPage.bookedItems = [:]
        hotel.searchResults.itineraryBuilder = [:]
        hotel.itineraryOverlay = [:]
        hotel.itineraryOverlay.checkBoxStatus = [:]
        hotel.paymentpage = [:]
        hotel.confirmBookingOverlay = [:]

        transfer.searchResults=[:]
        transfer.itineraryPage = [:]
        transfer.itineraryPage.bookedItems = [:]
        transfer.itineraryOverlay = [:]
        transfer.itineraryOverlay.checkBoxStatus = [:]
        transfer.searchResults.itineraryBuilder = [:]
        transfer.confirmBookingOverlay = [:]
        transfer.paymentpage = [:]
        transfer.removeItemPage=[:]
        transfer.removeItemPage.cancelledItems=[:]

        activity.searchResults=[:]
        activity.itineraryPage = [:]
        activity.itineraryPage.bookedItems = [:]
        activity.searchResults.itineraryBuilder = [:]
        activity.itineraryOverlay = [:]
        activity.itineraryOverlay.checkBoxStatus = [:]
        activity.confirmBookingOverlay = [:]
        activity.paymentpage = [:]

        account.accountPage = [:]
        this.scenarioName = scenarioName
        this.paymentsData = paymentsData
    }
}
