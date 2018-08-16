package com.kuoni.qa.automation.test.search.booking.hotel

import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.InventorySearchData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.hotel.HotelInformationPageBooking
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec

class InventoryBaseSpec extends BaseSpec{
	//@Shared
	//ClientData client617EURData = new ClientData("client617EUR")
	@Shared
	
	ClientData client = new ClientData("client662")
	def setupSpec() {
		println "setup Speccc"
		//login(client618Html)
	}
	def setup() {
		login(client)
	}
	String[] roomTypes
	boolean verifyInventory(InventorySearchData invSearchData){
		at HotelSearchResultsPage
		def hotelName1=""
		enterHotelDestination(invSearchData.input.property)
		selectFromAutoSuggest(invSearchData.input.propertySuggest)
		entercheckInCheckOutFormattedDates(invSearchData.input.checkInDate,invSearchData.input.checkOutDate)
		clickPaxRoom()
		selectNumberOfAdults("2", 0)
		sleep(1000)
		clickPaxRoom()
		clickFindButton()
		sleep(1000)

		int numOfRooms=0
		int expNumOfRooms=invSearchData.output.numOfRooms.toString().toInteger()
		println "Expected Number of Rooms:" + expNumOfRooms
		if (expNumOfRooms>0){
			at HotelInformationPageBooking
			numOfRooms=getTotalNumOfRooms()
			softAssert.assertTrue(numOfRooms.equals(expNumOfRooms), "\n Number Of Rooms returned is not correct")
			for (int i=0;i<expNumOfRooms;i++){
				if (i>0)
					clickExpandRoomButton(i)
				softAssert.assertTrue(getRoomStatus(i).equals(invSearchData.output.status), "\nRoom status is not correct")

			}
		}
		else{
			softAssert.assertTrue(getZeroResultsError().toString().contains("0 Results"), "\n Number Of Rooms returned is not correct")
		}
		//at HotelSearchPage
		//clickOnSearchToolTab("Hotels")
		return true
	}
	boolean verifyInventory_MultiRoom(InventorySearchData invSearchData){
		at HotelSearchResultsPage
		def hotelName1=""
		enterHotelDestination(invSearchData.input.property)
		selectFromAutoSuggest(invSearchData.input.propertySuggest)
		entercheckInCheckOutFormattedDates(invSearchData.input.checkInDate,invSearchData.input.checkOutDate)
		clickPaxNumOfRooms(invSearchData.input.numberofRooms)
		selectNumberOfAdults(invSearchData.input.numofPax)
		sleep(1000)
		clickPaxRoom()
		clickFindButton()
		sleep(1000)
		if(verifySearchResult()) {
			roomTypes =(invSearchData.output.recommededRoomTyp).split(";")
					if (verifyRecommendedRoom().contains("Recommended")) {
						at HotelInformationPageBooking
						softAssert.assertTrue(recommendedRoomCount().equals(invSearchData.output.recommendedRoomCnt.toString().toInteger()), "\n Recommended rooms count is not correct ")
						for (int i = 0; i <= invSearchData.output.recommendedRoomCnt.toString().toInteger()-1; i++) {
							softAssert.assertTrue(recommendedRoomType(i).contains(roomTypes[i]), "\n Recommended rooms type is not correct Expected :" + roomTypes[i]+ "actual :"+ recommendedRoomType(i) )
							print roomTypes[i]
						}
					}
			int numOfRooms=0
			int expNumOfRooms=invSearchData.output.numOfRooms.toString().toInteger()+invSearchData.output.recommendedRommSec.toString().toInteger()
			println "Expected Number of Rooms:" + expNumOfRooms
			if (expNumOfRooms>0){
				at HotelInformationPageBooking
				numOfRooms=getTotalNumOfRooms()

				softAssert.assertTrue(numOfRooms.equals(expNumOfRooms), "\n Number Of Rooms returned is not correct")
				for (int i=0;i<numOfRooms-recommendedRoomCount();i++){
					softAssert.assertTrue(getRoomStatus(i).equals(invSearchData.output.status), "\nRoom status is not correct expected "+invSearchData.output.status +"Actual"+getRoomStatus(i))
				}
			}
		}
		else{
			softAssert.assertTrue(getZeroResultsError().toString().contains("0 Results"), "\n Number Of Rooms returned is not correct")
		}

		//at HotelSearchPage
		//clickOnSearchToolTab("Hotels")
		return true
	}

	boolean verifyInventoryMultiRooms(InventorySearchData invSearchData){
		at HotelSearchResultsPage
		def hotelName1=""
		enterHotelDestination(invSearchData.input.property)
		selectFromAutoSuggest(invSearchData.input.propertySuggest)
		entercheckInCheckOutFormattedDates(invSearchData.input.checkInDate,invSearchData.input.checkOutDate)
		clickPaxRoom()
		selectNumberOfAdults("2", 0)
		sleep(1000)
		clickPaxRoom()
		clickFindButton()
		sleep(1000)

		def roomType=""
		int numOfRooms=0
		int expNumOfRooms=invSearchData.output.numOfRooms.toString().toInteger()
		println "Expected Number of Rooms:" + expNumOfRooms
		if (expNumOfRooms>0){
			at HotelInformationPageBooking
			numOfRooms=getTotalNumOfRooms()
			softAssert.assertTrue(numOfRooms.equals(expNumOfRooms), "\n Number Of Rooms returned is not correct")
			for (int i=0;i<expNumOfRooms;i++){
				if (i>0)
					clickExpandRoomButton(i)
				roomType=getRoomName(i).split(" ")[0]
				println "Expected Status for roomTYpe $roomType : "+invSearchData.output.get(roomType.toLowerCase())
				softAssert.assertTrue(getRoomStatus(i).equals(invSearchData.output.get(roomType.toLowerCase())), "\nRoom status is not correct")
			}
		}
		else{
			softAssert.assertTrue(getZeroResultsError().toString().contains("0 Results"), "\n Number Of Rooms returned is not correct")
		}
		//at HotelSearchPage
		//clickOnSearchToolTab("Hotels")
		return true
	}

	boolean verifyInventoryZeroResult(InventorySearchData invSearchData){
		at HotelSearchResultsPage
		def hotelName1=""
		enterHotelDestination(invSearchData.input.property)
		selectFromAutoSuggest(invSearchData.input.propertySuggest)
		entercheckInCheckOutFormattedDates(invSearchData.input.checkInDate,invSearchData.input.checkOutDate)
		clickPaxNumOfRooms(invSearchData.input.numberofRooms)
		selectNumberOfAdults(invSearchData.input.numofPax)
		sleep(1000)
		clickPaxRoom()
		clickFindButton()
		sleep(1000)
		if(verifySearchResult()) {
			return false
		}else {
			return true
		}

	}

}

