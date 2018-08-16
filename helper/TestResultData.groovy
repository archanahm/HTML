package com.kuoni.qa.automation.helper

/**
 * Created by Joseph Sebastian on 12/11/2015.
 */
class TestResultData {
    String bookingId
    String tripDesc
    String expectedPrice
    Map prices = [:]

    TestResultData() {
        prices.infoPage = [:]
        prices.infoPage.itineraryCard = [:]
        prices.infoPage.commission = [:]
        prices.infoPage.cancellationCharge = [:]

        prices.itineraryPage = [:]
        prices.itineraryPage.commission = [:]
        prices.itineraryPage.cancellationCharge = [:]
        prices.confirmationPage = [:]
        prices.confirmationPage.commisssionTotal = [:]
        prices.confirmationPage.commisssionTotalPercent = [:]
        prices.bookingResult = [:]
        prices.bookingResult.commisssionTotalPercent= [:]
        prices.bookingResult.cancellationCharge = [:]
    }

    TestResultData setBookingId(String bookingId) {
        this.bookingId = bookingId
        this
    }

    TestResultData setTripDesc(String tripDesc) {
        this.tripDesc = tripDesc
        this
    }
}
