package com.kuoni.qa.automation.helper

import java.util.Map;

class TransferTestResultData {
	
	String bookingId
	String transferName
	TransfersTestData transfersData
	
	Map transfers = [:]

	TransferTestResultData() 
	{
		transfers.searchResults = [:]
		transfers.searchResults.results = [:]
		transfers.pagination = [:]
		transfers.itineraryPage = [:]
		transfers.confirmationPage = [:]
		transfers.bookingResult = [:]
		transfers.shareItineraryPage=[:]
	}

	TransferTestResultData(String transferName, TransfersTestData transfersData) {

		transfers.search = [:]

		transfers.searchResults = [:]

		transfers.itineraryPage = [:]

		transfers.itineraryPage.bookedItems = [:]

		transfers.confirmationPage = [:]

		transfers.shareItineraryPage = [:]

		transfers.removeItemPage =[:]

		transfers.removeItemPage.cancelledItems = [:]


		this.transferName = transferName
		this.transfersData = transfersData
	}

}
