package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HomePage
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.hotel.AgentHotelInfoPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.util.DbUtil

import static com.kuoni.qa.automation.util.TestConf.*
import spock.lang.Shared

import com.kuoni.qa.automation.helper.AreaSearchData
import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.PropertySearchData
import com.kuoni.qa.automation.page.HotelInformationPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.hotel.HotelInformationPageBooking
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.AS400Util
import com.kuoni.qa.automation.util.ATGUtil


class HotelBookingBaseSpec extends BaseSpec {

	//@Shared
	//ClientData client617EURData = new ClientData("client617EUR")
	@Shared
	public DbUtil dbUtil = DbUtil.getInstance()
	ClientData client = new ClientData("client662")
	CitySearchData CitySearchData = new CitySearchData("case1")
	public PropertySearchData propertySearchData = new PropertySearchData("Kingdom-SITUJ15XMLAuto")

	ATGUtil atgUtil = new ATGUtil()


	def hotelName = ""
	def dur = ""
	def roomname = ""
	def mealbasis = ""
	def expPrice = ""
	int nights = 0
	def dateRange = ""
	String actualRating = ""
	int hotelNuminSearch = 0
	String actualPrice = ""

	String expRating = ""
	String expAddress = ""
	Map expAddressMap = [:]
	public static def tyId

//	AS400Util aS400Util = new AS400Util()
	def platform = System.getProperty("platform")

	def addItineraryAndGotToItineryPageSearchResults() {
		login(client)
		at HotelSearchResultsPage
		def hotelName1 = ""
		at HotelSearchResultsPage
		enterHotelDestination(CitySearchData.city)
		selectFromAutoSuggest(CitySearchData.autoSuggest)
		if (platform.equals("web")) {
			entercheckInCheckOutDate(CitySearchData.checkInDaysToAdd.toInteger(), CitySearchData.checkOutDaysToAdd.toInteger())
			clickPaxRoom()
		} else selectcheckInCheckOutDateCalender(CitySearchData.checkInDaysToAdd, CitySearchData.checkOutDaysToAdd)
		def checkInDt = getCheckInDate()
		nights = propertySearchData.input.checkOutDaysToAdd.toString().toInteger() - propertySearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickFindButton()

		hotelName = getHotelName(0)
		actualRating = getRatingForTheHotel(0)
		roomname = getRoomName(0, 0)
		mealbasis = getMealBasis(0, 0).replaceAll(roomname, "").trim()
		expPrice = getGammaPrice(0, 0)
		clickOnAddToItenary(0, 0)
		at ItenaryBuilderPage
//		hideItenaryBuilder()
//		sleep(2000)
//		at HotelSearchResultsPage
//		hotelName1 = getHotelName(1)
//		clickOnAddToItenary(1,0)
		sleep(1000)

//		at ItenaryBuilderPage
//		def iteTitle = getItenaryBuilderTtile()
//		sleep(1000)
//		clickOnGotoItenaryButton()
	}

	def searchHotel(PropertySearchData propertySearchData) {

		login(client)
		at HomePage
		//Addition of assertion to validate view profile option in home page.
		softAssert.assertTrue(viewProfile(), "View profile option is not displayed.")
		at HotelSearchResultsPage
		def hotelName1 = ""
		at HotelSearchResultsPage
		enterHotelDestination(propertySearchData.input.property)
		selectFromAutoSuggest(propertySearchData.input.property)

		entercheckInCheckOutDate(propertySearchData.input.checkInDaysToAdd.toString().toInteger(), propertySearchData.input.checkOutDaysToAdd.toString().toInteger())
		clickPaxRoom()

		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = propertySearchData.input.checkOutDaysToAdd.toString().toInteger() - propertySearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickFindButton()

		at HotelInformationPage
		dateRange = checkInDt + " to " + checkOutDt
		expRating = atgUtil.getStarRatingFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		expAddressMap = atgUtil.getPropertyAddressFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		if (!expAddressMap.get("addressLine2").equals(null))
			expAddress = expAddressMap.get("addressLine1") + ", " + expAddressMap.get("addressLine2") + ", " + propertySearchData.input.city
		else expAddress = expAddressMap.get("addressLine1") + ", " + propertySearchData.input.city
		//+" "+expAddressMap.get("postCode")+" "+propertySearchData.input.country
		/*if(!expAddressMap.get("addressLine3").equals("null"))
		 expAddress=expAddress+", "+expAddressMap.get("addressLine3")*/
		if (!expAddressMap.get("postCode").equals("null"))
			expAddress = expAddress + " " + expAddressMap.get("postCode")
		expAddress = expAddress + " " + propertySearchData.input.country

	}

	def storeHotelInfoInPDP(PropertySearchData propertySearchData) {
		def dataMap = [:]
		at HotelSearchResultsPage
		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		at HotelInformationPage
		dateRange = checkInDt + " to " + checkOutDt
		expRating = atgUtil.getStarRatingFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		expAddressMap = atgUtil.getPropertyAddressFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		if (!expAddressMap.get("addressLine2").equals(null))
			expAddress = expAddressMap.get("addressLine1") + ", " + expAddressMap.get("addressLine2") + ", " + propertySearchData.input.city
		else expAddress = expAddressMap.get("addressLine1") + ", " + propertySearchData.input.city
		//+" "+expAddressMap.get("postCode")+" "+propertySearchData.input.country
		/*if(!expAddressMap.get("addressLine3").equals("null"))
		 expAddress=expAddress+", "+expAddressMap.get("addressLine3")*/
		if (!expAddressMap.get("postCode").equals("null"))
			expAddress = expAddress + " " + expAddressMap.get("postCode")
		expAddress = expAddress + " " + propertySearchData.input.country
	}


	def searchCityAndClickHotel(PropertySearchData propertySearchData) {

		login(client)
		at HotelSearchResultsPage
		def hotelName1 = ""
		at HotelSearchResultsPage
		enterHotelDestination(propertySearchData.input.city)
		selectFromAutoSuggest(propertySearchData.input.cityAutoSuggest)

		entercheckInCheckOutDate(propertySearchData.input.checkInDaysToAdd.toString().toInteger(), propertySearchData.input.checkOutDaysToAdd.toString().toInteger())
		clickPaxRoom()

		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = propertySearchData.input.checkOutDaysToAdd.toString().toInteger() - propertySearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickFindButton()
		waitTillLoadingIconDisappears()
		int num = getNumberOfHotelsInSearchResults()
		softAssert.assertTrue(num > 0, "\n Zero hotels returned ")
		for (int i = 0; i < num; i++) {
			if (getHotelName(i).equals(propertySearchData.input.property)) {
				sleep(1000)
				clickOnHotelName(i)
				break
			}
		}

		at HotelInformationPage
		dateRange = checkInDt + " to " + checkOutDt
		expRating = atgUtil.getStarRatingFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		expAddressMap = atgUtil.getPropertyAddressFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		if (!expAddressMap.get("addressLine2").equals(null))
			expAddress = expAddressMap.get("addressLine1") + ", " + expAddressMap.get("addressLine2") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
		else expAddress = expAddressMap.get("addressLine1") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
	}

	def searchAreaAndClickHotel(PropertySearchData propertySearchData) {

		login(client)
		at HotelSearchResultsPage

		def hotelName1 = ""
		at HotelSearchResultsPage
		enterHotelDestination(propertySearchData.input.city)
		selectFromAutoSuggest(propertySearchData.input.areaAutoSuggest)

		entercheckInCheckOutDate(propertySearchData.input.checkInDaysToAdd.toString().toInteger(), propertySearchData.input.checkOutDaysToAdd.toString().toInteger())
		clickPaxRoom()

		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = propertySearchData.input.checkOutDaysToAdd.toString().toInteger() - propertySearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickFindButton()
		int num = getNumberOfHotelsInSearchResults()
		softAssert.assertTrue(num > 0, "\n Zero hotels returned ")
		for (int i = 0; i < num; i++) {
			if (getHotelName(i).equals(propertySearchData.input.property)) {
				clickOnHotelName(i)
				break
			}
		}

		at HotelInformationPage
		dateRange = checkInDt + " to " + checkOutDt
		expRating = atgUtil.getStarRatingFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		expAddressMap = atgUtil.getPropertyAddressFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		if (!expAddressMap.get("addressLine2").equals(null))
			expAddress = expAddressMap.get("addressLine1") + ", " + expAddressMap.get("addressLine2") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
		else expAddress = expAddressMap.get("addressLine1") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
	}

	def searchCityAndAddToItinerary(PropertySearchData propertySearchData) {

		login(client)
		at HotelSearchResultsPage

		def hotelName1 = ""
		at HotelSearchResultsPage
		enterHotelDestination(propertySearchData.input.city)
		selectFromAutoSuggest(propertySearchData.input.cityAutoSuggest)

		entercheckInCheckOutDate(propertySearchData.input.checkInDaysToAdd.toString().toInteger(), propertySearchData.input.checkOutDaysToAdd.toString().toInteger())
		clickPaxRoom()

		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = propertySearchData.input.checkOutDaysToAdd.toString().toInteger() - propertySearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickFindButton()
		int num = getNumberOfHotelsInSearchResults()
		softAssert.assertTrue(num > 0, "\n Zero hotels returned ")
		for (int i = 0; i < num; i++) {
			if (getHotelName(i).equals(propertySearchData.input.property)) {
				clickOnAddToItenary(i, 0)
				break
			}
		}
		dateRange = checkInDt + " to " + checkOutDt
		expRating = atgUtil.getStarRatingFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		expAddressMap = atgUtil.getPropertyAddressFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		if (!expAddressMap.get("addressLine2").equals(null))
			expAddress = expAddressMap.get("addressLine1") + ", " + expAddressMap.get("addressLine2") + ", " + propertySearchData.input.city
		else expAddress = expAddressMap.get("addressLine1") + ", " + propertySearchData.input.city
		//+" "+expAddressMap.get("postCode")+" "+propertySearchData.input.country
		/*if(!expAddressMap.get("addressLine3").equals("null"))
		 expAddress=expAddress+", "+expAddressMap.get("addressLine3")*/
		if (!expAddressMap.get("postCode").equals("null"))
			expAddress = expAddress + " " + expAddressMap.get("postCode")
		expAddress = expAddress + " " + propertySearchData.input.country
	}

	def verifyHotelInfo(PropertySearchData propertySearchData) {
		at HotelInformationPageBooking
		softAssert.assertEquals(getHotelNameInInfoPage(), propertySearchData.input.property, "\n Hotel name not correct on PDP expected: " + propertySearchData.input.property)
		softAssert.assertTrue(getStarRating().contains(expRating), "\n Hotel rating not correct on PDP , expected: " + expRating)
		softAssert.assertTrue(getHotelAddress().contains(expAddress), "\n Hotel Address not correct on PDP , expected: " + expAddress)
		softAssert.assertTrue(clickViewMoreDisplayed(), "\n View More link not displayed")

		sleep(1500)
		softAssert.assertEquals((getNumberOfHotelImages()).toString(), propertySearchData.images.noOfImgs, "\nNumber of images not correct")
		def reportsData = getHotelReportsData()
		List reportHeader = propertySearchData.reports.header
		for (int i = 0; i < reportHeader.size(); i++) {
			softAssert.assertEquals(getReportHeader(i), reportHeader[i], "\n report header not correct for report $i")
		}
		softAssert.assertTrue(getHotelReportsData(0).contains(propertySearchData.reports.general), "\n General report not correct")
		softAssert.assertTrue(getHotelReportsData(1).contains(propertySearchData.reports.exterior), "\n Exterior report not correct")
		softAssert.assertTrue(getHotelReportsData(2).contains(propertySearchData.reports.lobby), "\n Lobby report not correct")
		softAssert.assertTrue(getHotelReportsData(3).contains(propertySearchData.reports.location), "\n Location report not correct")
		softAssert.assertTrue(getHotelReportsData(4).contains(propertySearchData.reports.restaurant), "\n Restaurant report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkInTime.toString().startsWith(getPleaseNoteReport(1)), "\n checkInTime label report not correct, Actual: " + getPleaseNoteReport(1))
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkOutTime.toString().startsWith(getPleaseNoteReport(2)), "\n check in time report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkInTime.toString().endsWith(getPleaseNoteReport(3)), "\n checkout time label report not correct, Actual: " + getPleaseNoteReport(1))
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkOutTime.toString().endsWith(getPleaseNoteReport(4)), "\n checkOutTime report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.typeOfProperty.toString().startsWith(getPleaseNoteReport(5)), "\n typeOfProperty report label not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.noRooms.toString().startsWith(getPleaseNoteReport(6)), "\n type of property report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.typeOfProperty.toString().endsWith(getPleaseNoteReport(7)), "\n no.of rooms label report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.noRooms.toString().endsWith(getPleaseNoteReport(8)), "\n noRooms report not correct")

		softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().startsWith(getPleaseNoteReport(9)), "\n earliestBreakfast label report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.roomService.toString().startsWith(getPleaseNoteReport(10)), "\n earliestBreakfast report not correct")

		softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().endsWith(getPleaseNoteReport(11)), "\n roomService label report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.roomService.toString().endsWith(getPleaseNoteReport(12)), "\n roomService report not correct")

		//softAssert.assertTrue(getPleaseNoteReport(7).contains(propertySearchData.pleaseNoteReport.maidService), "\n maidService report not correct")
		softAssert.assertTrue(getFacilitiesBlockTxt(5).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[0]), "\n shuttlBusOptions1 report not correct")
		softAssert.assertTrue(getFacilitiesBlockTxt(5).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[1]), "\n shuttlBusOptions2 report not correct")
		softAssert.assertTrue(getFacilitiesBlockTxt(7).contains(propertySearchData.pleaseNoteReport.parkingOptions[0]), "\n parkingOptions1 report not correct")
		softAssert.assertTrue(getFacilitiesBlockTxt(7).contains(propertySearchData.pleaseNoteReport.parkingOptions[1]), "\n parkingOptions2 report not correct")
		//softAssert.assertTrue(getPleaseNoteReport(14).contains(propertySearchData.pleaseNoteReport.coachDropoff), "\n coachDropoff report not correct")
		//softAssert.assertTrue(getPleaseNoteReport(15).contains(propertySearchData.pleaseNoteReport.minimumAge), "\n minimumAge report not correct")
		//softAssert.assertTrue(getHotelAreaInfo().contains(propertySearchData.pleaseNoteReport.area), "\n area report not correct expected: "+propertySearchData.pleaseNoteReport.area)
		//softAssert.assertTrue(getHotelFacilities(propertySearchData.facilities.facList), "\n Hotel Facility List not correct")

		int numOfRooms = propertySearchData.room.size()
		Map rooms = propertySearchData.room
		for (int i = 0; i < numOfRooms; i++) {
			clickExpandRoomButton(i)
			sleep(1000)
			clickMoreRoomInfoButton(i + 1, 0)
			sleep(1000)
			Map room = rooms.get(rooms.keySet().getAt(i))
			softAssert.assertTrue(verifyRoomDescreption(room.descreption, i), "\n Room descreption not correct for Room: $i")
			softAssert.assertEquals(getRoomName(i), room.roomName, "\n Room name not correct for Room;$i")
			//softAssert.assertEquals(getNumberOfRoomImages(i).toString(),propertySearchData.images.noOfRoomImgs, "\n Number of  room images not correct" )
			softAssert.assertTrue(verifyRoomFacilitiesList(room.facList, i), "\n Room facilities list not correct for room: $i")
			softAssert.assertEquals(getMealBasis(i + 1).trim(), room.mealBasis, "\n Meal basis not correct for room : $i")
			softAssert.assertTrue(verifyNumberOfPax(i + 1, room.pax), "\n Number of Pax not correct for room : $i")
			softAssert.assertEquals(getRoomStatus(i), room.status, "\n Room status is not correct for room:$i")
			softAssert.assertTrue(verifyCommissionDisplayed(i + 1), "\n Commission not displayed for room: $i")
			sleep(2000)
			scrollToBottomOfThePage()
			sleep(1000)
			softAssert.assertTrue(verifyPriceBreakDownDates(dateRange, i + 1, 0), "\n Date range not correct on price breakdown table")
			softAssert.assertTrue(verifyPriceBreakdownTableDisplayed(i + 1, 0), "\n Price break doan table not displayed")
			softAssert.assertTrue(getPriceBreakDownTotalCurrency(i).contains(client.currency), "\n Currency not correct on price breakdown total price")
		}
		softAssert.assertTrue(verifyMapDisplayed(), "Map Block not displayed")
		/*if(propertySearchData.tripAdvisor.logo.equals("yes")){
			softAssert.assertTrue(verifyTripAdvisorLogoDisplayed(), "\n Trip advisor logo not displayed")
			clickOnTripAdvisorReviews()
			clickBackButton()
		}*/
	}

	def verifyHotelInfoDI(PropertySearchData propertySearchData) {
		at HotelInformationPage
		softAssert.assertEquals(getHotelInfoHeader(), propertySearchData.input.property, "\n Hotel name not correct on PDP expected: " + propertySearchData.input.property)
		softAssert.assertTrue(getStarRating().contains(expRating), "\n Hotel rating not correct on PDP , expected: " + expRating)
		softAssert.assertTrue(getHotelAddress().contains(expAddress), "\n Hotel Address not correct on PDP , expected: " + expAddress)
		if (propertySearchData.tripAdvisor.logo.equals("yes")) {
			softAssert.assertTrue(verifyTripAdvisorLogoDisplayed(), "\n Trip advisor logo not displayed")
			clickOnTripAdvisorReviews()
			clickBackButton()
		}
		softAssert.assertEquals(getNumberOfImages().toString(), propertySearchData.images.noOfImgs, "\nNumber of images not correct")
		softAssert.assertTrue(clickViewMoreDisplayed(), "\n View More link not displayed")

		def reportsData = getHotelReportsData()
		List reportHeader = propertySearchData.reports.header
		def actualrepheader = ""
		for (int i = 0; i < reportHeader.size(); i++) {
			actualrepheader = getReportsHeader(i)
			softAssert.assertEquals(getReportsHeader(i), reportHeader[i], "\n report header not correct for report $i, Actual : " + actualrepheader)
		}
		softAssert.assertTrue(reportsData.contains(propertySearchData.reports.exterior), "\n Exterior report not correct")
		softAssert.assertTrue(reportsData.contains(propertySearchData.reports.general), "\n General report not correct")
		softAssert.assertTrue(reportsData.contains(propertySearchData.reports.lobby), "\n Lobby report not correct")
		softAssert.assertTrue(reportsData.contains(propertySearchData.reports.location), "\n Location report not correct")
		softAssert.assertTrue(reportsData.contains(propertySearchData.reports.restaurant), "\n Restaurant report not correct")
		def rep = ""
		def exprep = ""
		for (int i = 0; i < propertySearchData.pleaseNoteReport.size(); i++) {
			rep = getPleaseNoteReport(i)
			exprep = propertySearchData.pleaseNoteReport.values().getAt(i)
			softAssert.assertTrue(rep.contains(exprep), "\n Please note report report not correct for $i:" + exprep + "ACtual:" + rep)
		}
			softAssert.assertTrue(getPleaseNoteReport(1).contains(propertySearchData.pleaseNoteReport.checkInTime), "\n checkInTime report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(2).contains(propertySearchData.pleaseNoteReport.checkOutTime), "\n checkOutTime report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(3).contains(propertySearchData.pleaseNoteReport.typeOfProperty), "\n typeOfProperty report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(4).contains(propertySearchData.pleaseNoteReport.noRooms), "\n noRooms report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(5).contains(propertySearchData.pleaseNoteReport.roomService), "\n roomService report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(6).contains(propertySearchData.pleaseNoteReport.earliestBreakfast), "\n earliestBreakfast report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(7).contains(propertySearchData.pleaseNoteReport.maidService), "\n maidService report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(9).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[0]), "\n shuttlBusOptions1 report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(10).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[1]), "\n shuttlBusOptions2 report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(12).contains(propertySearchData.pleaseNoteReport.parkingOptions[0]), "\n parkingOptions1 report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(13).contains(propertySearchData.pleaseNoteReport.parkingOptions[1]), "\n parkingOptions2 report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(14).contains(propertySearchData.pleaseNoteReport.coachDropoff), "\n coachDropoff report not correct")
		 softAssert.assertTrue(getPleaseNoteReport(15).contains(propertySearchData.pleaseNoteReport.minimumAge), "\n minimumAge report not correct")
		 softAssert.assertTrue(getHotelAreaInfo().contains(propertySearchData.pleaseNoteReport.area), "\n area report not correct expected: "+propertySearchData.pleaseNoteReport.area)
		softAssert.assertTrue(getHotelFacilities(propertySearchData.facilities.facList), "\n Hotel Facility List not correct")
		int numOfRooms = propertySearchData.room.size()
		Map rooms = propertySearchData.room
		for (int i = 0; i < numOfRooms; i++) {
			Map room = rooms.get(rooms.keySet().getAt(i))
			softAssert.assertTrue(verifyRoomDescreption(room.descreption, i), "\n Room descreption not correct for Room: $i")
			softAssert.assertEquals(getRoomName(i), room.roomName, "\n Room name not correct for Room;$i")
			softAssert.assertEquals(getNumberOfRoomImages(i).toString(), propertySearchData.images.noOfRoomImgs, "\n Number of  room images not correct")
			softAssert.assertTrue(verifyRoomFacilities(room.facList, i), "\n Room facilities list not correct for room: $i")
			softAssert.assertEquals(getMealBasis(i).trim(), room.mealBasis, "\n Meal basis not correct for room : $i")
			softAssert.assertTrue(verifyNumberOfPax(i, room.pax), "\n Number of Pax not correct for room : $i")
			softAssert.assertEquals(getRoomStatus(i), room.status, "\n Room status is not correct for room:$i")
			softAssert.assertTrue(verifyCommissionDisplayed(i), "\n Commission not displayed for room: $i")
			clickMoreRoomInfoButton(i, 0)
			softAssert.assertTrue(verifyPriceBreakDownDates(dateRange, i, 0), "\n Date range not correct on price breakdown table")
			softAssert.assertTrue(verifyPriceBreakdownTableDisplayed(i, 0), "\n Price break doan table not displayed")
			softAssert.assertTrue(getPriceBreakDownTotalCurrency(i).contains(client.currency), "\n Currency not correct on price breakdown total price")
		}
		softAssert.assertTrue(verifyMapDisplayed(), "Map Block not displayed")
	}


	def searchByCitySingleRoom2A1C(CitySearchData citySearchData, PropertySearchData propertySearchData) {
		login(client)
		at HotelSearchResultsPage
		def hotelName1 = ""
		at HotelSearchResultsPage
		println citySearchData.input.cityTypeText
		enterHotelDestination(citySearchData.input.cityTypeText)
		selectFromAutoSuggest(citySearchData.input.cityText)
		entercheckInCheckOutDate(citySearchData.input.checkInDaysToAdd.toString().toInteger(), citySearchData.input.checkOutDaysToAdd.toString().toInteger())
		clickPaxRoom()
		clickPaxRoom()
		selectNumberOfAdults("2", 0)
		selectNumberOfChildren("1", 0)
		sleep(1000)
		enterChildrenAge("5", "0", 0)
		sleep(1000)
		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = citySearchData.input.checkOutDaysToAdd.toString().toInteger() - citySearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickPaxRoom()
		clickFindButton()
		waitTillLoadingIconDisappears()
		sleep(1000)
		int num = getNumberOfHotelsInSearchResults()
		softAssert.assertTrue(num > 0, "\n Zero hotels returned ")
		for (int i = 0; i < num; i++) {
			if (getHotelName(i).equals(propertySearchData.input.property)) {
				hotelNuminSearch = i
				break
			}
		}

		at HotelInformationPage
		dateRange = checkInDt + " to " + checkOutDt
		expRating = atgUtil.getStarRatingFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		expAddressMap = atgUtil.getPropertyAddressFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		if (!expAddressMap.get("addressLine2").equals(null))
			expAddress = expAddressMap.get("addressLine1") + ", " + expAddressMap.get("addressLine2") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
		else expAddress = expAddressMap.get("addressLine1") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
	}


	def searchByAreaSingleRoom2A1C(AreaSearchData areaSearchData, PropertySearchData propertySearchData) {
		login(client)
		at HotelSearchResultsPage
		def hotelName1 = ""
		at HotelSearchResultsPage
		println areaSearchData.input.cityTypeText
		enterHotelDestination(areaSearchData.input.areaTypeText)
		selectFromAutoSuggest(areaSearchData.input.areaText)
		entercheckInCheckOutDate(areaSearchData.input.checkInDaysToAdd.toString().toInteger(), areaSearchData.input.checkOutDaysToAdd.toString().toInteger())
		clickPaxRoom()
		clickPaxRoom()
		selectNumberOfAdults("2", 0)
		selectNumberOfChildren("1", 0)
		sleep(1000)
		enterChildrenAge("5", "0", 0)
		sleep(1000)
		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = areaSearchData.input.checkOutDaysToAdd.toString().toInteger() - areaSearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickPaxRoom()
		clickFindButton()
		waitTillLoadingIconDisappears()
		sleep(1000)
		int num = getNumberOfHotelsInSearchResults()
		softAssert.assertTrue(num > 0, "\n Zero hotels returned ")
		for (int i = 0; i < num; i++) {
			if (getHotelName(i).equals(propertySearchData.input.property)) {
				hotelNuminSearch = i
				break
			}
		}
		//	println("get Hotels"+getHotelNamesOfAllPages())
		at HotelInformationPage
		sleep(1000)
		dateRange = checkInDt + " to " + checkOutDt
		expRating = atgUtil.getStarRatingFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		expAddressMap = atgUtil.getPropertyAddressFromAtg(propertySearchData.input.property, propertySearchData.input.cityCode)
		if (!expAddressMap.get("addressLine2").equals(null))
			expAddress = expAddressMap.get("addressLine1") + ", " + expAddressMap.get("addressLine2") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
		else expAddress = expAddressMap.get("addressLine1") + ", " + propertySearchData.input.city + " " + expAddressMap.get("postCode") + " " + propertySearchData.input.country
	}

	def searchByCitySingleRoom2Adults(AreaSearchData areaSearchData) {
		login(client)
		at HotelSearchResultsPage
		def hotelName1 = ""
		at HotelSearchResultsPage
		println citySearchData.input.areaTypeText
		enterHotelDestination(areaSearchData.input.areaText)
		sleep(1000)
		selectFromAutoSuggest(areaSearchData.input.areaText)
		entercheckInCheckOutDate(areaSearchData.input.checkInDaysToAdd.toString().toInteger(), areaSearchData.input.checkOutDaysToAdd.toString().toInteger())
		sleep(4000)
		/*at HotelSearchResultsPage
		clickPaxRoom()
		sleep(3000)
		selectNumberOfAdults("2", 0)
		sleep(1000)*/
		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = areaSearchData.input.checkOutDaysToAdd.toString().toInteger() - areaSearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		sleep(1000)
		clickFindButton()
		waitTillLoadingIconDisappears()
		sleep(2000)
	}

	def searchByCitySingleRoom2Adults(CitySearchData citySearchData) {
		login(client)
		at HotelSearchResultsPage
		def hotelName1 = ""
		at HotelSearchResultsPage
		println citySearchData.input.cityTypeText
		enterHotelDestination(citySearchData.input.cityTypeText)
		selectFromAutoSuggest(citySearchData.input.cityText)
		entercheckInCheckOutDate(citySearchData.input.checkInDaysToAdd.toString().toInteger(), citySearchData.input.checkOutDaysToAdd.toString().toInteger())
		clickPaxRoom()
		clickPaxRoom()
		selectNumberOfAdults("2", 0)
		sleep(1000)

		def checkInDt = getCheckInDate()
		def checkOutDt = getCheckOutDate()
		nights = citySearchData.input.checkOutDaysToAdd.toString().toInteger() - citySearchData.input.checkInDaysToAdd.toString().toInteger()
		if (nights > 1)
			dur = checkInDt + " - " + nights + " nights"
		else dur = checkInDt + " - " + nights + " night"
		clickPaxRoom()
		clickFindButton()
		waitTillLoadingIconDisappears()
		sleep(1000)
	}


	boolean verifyHotelinSearchResults(CitySearchData citySearchData, PropertySearchData propertySearchData) {
		List essentialInfoHeader = []
		List essentialInfo = []
		at HotelSearchResultsPage
		clickOnImage(hotelNuminSearch)
		sleep(2000)
		softAssert.assertEquals((getNumberOfImages(hotelNuminSearch) - 2).toString(), propertySearchData.images.noOfImgs, "\n Number of Images not correct for the hotel")
		clickCloseIconOnImg(hotelNuminSearch)
		sleep(1000)
		actualPrice = getGammaPrice(hotelNuminSearch, 0)
		softAssert.assertEquals(getHotelName(hotelNuminSearch), propertySearchData.input.property, "\n Property name not displayed in Search resiults")
		softAssert.assertTrue(getRatingForTheHotel(hotelNuminSearch).contains(expRating), "\n Star rating not correct")
		softAssert.assertTrue(getAddressForTheHotel(hotelNuminSearch).contains(expAddress), "\n Hotel Address not correct , expected: " + expAddress)
		//softAssert.assertEquals(getHotelDistanceDesc(hotelNuminSearch,0),propertySearchData.poi.area, "\n Hotel Point of interest area not correct, expected: "+propertySearchData.poi.area)
		if (propertySearchData.facilities.facList.size() > 0)
			softAssert.assertEquals(getHotelFacilitiesList(hotelNuminSearch).sort(true), propertySearchData.facilities.facList.sort(true), "\n Hotel Facility List not correct")
		if (propertySearchData.tripAdvisor.logo.equals("yes"))
			softAssert.assertTrue(verifyTripAdvisorRatingDisplayed(hotelNuminSearch), "\n Trip Advisor rating not displayed")
		if (propertySearchData.essensialInfo.exist.equals("true")) {
			softAssert.assertTrue(hotelotelEssentialInformationIconIsDisplayed(hotelNuminSearch), "\n Hotel essensial information icon not displayed")
			clickOnEssentialInfoLink(hotelNuminSearch)
			sleep(1000)
			essentialInfoHeader = getEssentionalInfoHeaders()
			essentialInfo = getEssentialInfoOverlay()
			clickOnCloseLightBox()
		}
		else softAssert.assertFalse(hotelotelEssentialInformationIconIsDisplayed(hotelNuminSearch), "\n Hotel essensial information icon displayed")

		softAssert.assertTrue(verifyMapPinDisplayed(hotelNuminSearch), "\n Map pin not displayed for the hotel")
		clickMapPinForTheHotel(hotelNuminSearch)

		sleep(3000)
		softAssert.assertEquals(getHotelNameInMapCard(), propertySearchData.input.property, "\n Property name not corect in hotel Map Card")
		softAssert.assertTrue(getHotelRatingInMapCard().contains(expRating), "\n Hotel rating not correct in map card")
		softAssert.assertTrue(getHotelAddressInMapCard().contains(expAddress), "\n Hotel address not correct on map card")
		softAssert.assertTrue(verifyImageDisplayedInMapCard(), "\n Hotel image not displayed in map card")
		//softAssert.assertTrue(getHotelDistanceMapCard().contains(propertySearchData.poi.area), "point of interest area not correct in hotel map card")
		softAssert.assertTrue(getHotelPriceInMapCard().contains(actualPrice), "\n Map card Price not correctly matching with search results")
		softAssert.assertTrue(getHotelPriceInMapCard().contains(client.currency), "\n Map card Price not correctly matching with search results")
		//softAssert.assertTrue(verifyRecommendedInformation(hotelNuminSearch, "Recommended for: 1 room"))
		def roomName = ""
		scrollToBottomOfThePage()
		int roomNum = 0
		int numOfRooms = propertySearchData.room.size()
		Map rooms = propertySearchData.room
		Map room
		Map<String, String> actualRoomname = new HashMap<String, String>()

		if (numOfRooms > 1)
			clickMoreRoomTypeCTA(hotelNuminSearch)
		sleep(1000)
		int actualNumRooms = getNumberOfRooms(hotelNuminSearch)
		println "*** Number of rooms: " + actualNumRooms
		for (int r = 0; r < actualNumRooms; r++) {
			roomName = getRoomNameAndMealBasis(hotelNuminSearch, r).trim().replaceAll("\\n", " ")
			println "**Room name: " + roomName
			actualRoomname.put(roomName, r.toString())
		}
		println "Map content is:" + actualRoomname

		for (int i = 0; i < numOfRooms; i++) {

			println "\n *** Room : $i"
			room = rooms.get(rooms.keySet().getAt(i))
			println "room.key :" + room.key
			println "key value" + actualRoomname.get(room.key.trim())
			roomNum = actualRoomname.get(room.key.trim()).toInteger()
			println "Exp room name: " + room.roomName
			println "room Number: " + roomNum
			expandMoreRoomInfo(hotelNuminSearch, roomNum)
			sleep(2000)
			roomName = getRoomNameAndMealBasis(hotelNuminSearch, roomNum)
			softAssert.assertTrue(roomName.contains(room.roomName), "\n Room name not correct for Room;$i , expected : " + room.roomName + "Actual: " + roomName)
			softAssert.assertTrue(getMealBasis(hotelNuminSearch, roomNum).contains(room.mealBasis), "\n Meal basis not correct for room : $roomNum")
			softAssert.assertTrue(verifyNumberOfPaxRoomDisplayed(hotelNuminSearch, roomNum, citySearchData.input.noOfPax), "\n Number of Pax not correct for room : $roomNum")
			softAssert.assertEquals(getStatusOfTheRoom(hotelNuminSearch, roomNum), room.status, "\n Room status is not correct for room:$roomNum")
			softAssert.assertTrue(verifyCommissionDisplayed(hotelNuminSearch, roomNum), "\n Commission not displayed for room: $roomNum")

			softAssert.assertTrue(verifyRoomDescreption(hotelNuminSearch, roomNum, room.descreption), "\n Room descreption not correct for Room: $roomNum")
			softAssert.assertTrue(verifyRoomFacilities(hotelNuminSearch, roomNum, room.facList), "\n Room facilities list not correct for room: $roomNum")

			softAssert.assertTrue(verifyPriceBreakdownHeder(hotelNuminSearch, roomNum, dateRange), "\n Date range not correct on price breakdown table")
			softAssert.assertTrue(verifyPriceBreakDownTableDisplayed(hotelNuminSearch, roomNum), "\n Price break doan table not displayed")
			softAssert.assertTrue(getPriceBreakDownTotalCurrency(hotelNuminSearch, roomNum).contains(client.currency), "\n Currency not correct on price breakdown total price")
			softAssert.assertEquals(getPriceCurrency(hotelNuminSearch, roomNum, client.currency), client.currency, "\n Gross price currency not correct")
			softAssert.assertTrue(AddToItenaryButtonDisplayed(hotelNuminSearch, roomNum), "\n Add to itinerary button not displayed")
			softAssert.assertTrue(verifyCancellationPolicyLinkDisplayed(hotelNuminSearch, roomNum), "\n Cancellation link poilicy link not displayed")
			if (propertySearchData.essensialInfo.exist.equals("true")) {
				clickAddToitinerary(hotelNuminSearch)
				waitTillLoadingIconDisappears()
				at ItineraryTravllerDetailsPage
				clickOnEssentialInfoIcon(0)
				at HotelSearchResultsPage
				List essentialInfoHeaderActual = getEssentionalInfoHeaders()
				List essentialInfoActual = getEssentialInfoOverlay()
				assertList(essentialInfoHeader, essentialInfoHeaderActual, "Essential info headers not matched")
				assertList(essentialInfo, essentialInfoActual, "essential info data not matched against PLP")
			}
		}

		return true
	}

	boolean verifyHotelinSearchResults(AreaSearchData areaSearchData, PropertySearchData propertySearchData) {
		at HotelSearchResultsPage
		clickOnImage(hotelNuminSearch)
		waitTillLoadingIconDisappears()
		sleep(2000)
		softAssert.assertEquals((getNumberOfImages(hotelNuminSearch) - 2).toString(), propertySearchData.images.noOfImgs, "\n Number of Images not correct for the hotel")
		sleep(2000)
		clickCloseIconOnImg(hotelNuminSearch)
		sleep(1000)
		actualPrice = getGammaPrice(hotelNuminSearch, 0)
		softAssert.assertEquals(getHotelName(hotelNuminSearch), propertySearchData.input.property, "\n Property name not displayed in Search resiults")
		softAssert.assertTrue(getRatingForTheHotel(hotelNuminSearch).contains(expRating), "\n Star rating not correct")
		softAssert.assertTrue(getAddressForTheHotel(hotelNuminSearch).contains(expAddress), "\n Hotel Address not correct , expected: " + expAddress)
		//softAssert.assertEquals(getHotelDistanceDesc(hotelNuminSearch,0),propertySearchData.poi.area, "\n Hotel Point of interest area not correct, expected: "+propertySearchData.poi.area)
		if (propertySearchData.facilities.facList.size() > 0)
			softAssert.assertTrue(getHotelFacilitiesList(hotelNuminSearch).sort(true).equals(propertySearchData.facilities.facList.sort(true)), "\n Hotel Facility List not correct")
		if (propertySearchData.tripAdvisor.logo.equals("yes"))
		//softAssert.assertTrue(verifyTripAdvisorRatingDisplayed(hotelNuminSearch), "\n Trip Advisor rating not displayed")
			if (propertySearchData.essensialInfo.exist.equals("true"))
				softAssert.assertTrue(hotelotelEssentialInformationIconIsDisplayed(hotelNuminSearch), "\n Hotel essensial information icon not displayed")
			else softAssert.assertFalse(hotelotelEssentialInformationIconIsDisplayed(hotelNuminSearch), "\n Hotel essensial information icon not displayed")
		softAssert.assertTrue(verifyMapPinDisplayed(hotelNuminSearch), "\n Map pin not displayed for the hotel")
		clickMapPinForTheHotel(hotelNuminSearch)
		sleep(1000)
		clickMapPinForTheHotel(hotelNuminSearch)
		sleep(2000)
		softAssert.assertEquals(getHotelNameInMapCard(), propertySearchData.input.property, "\n Property name not corect in hotel Map Card")
		softAssert.assertTrue(getHotelRatingInMapCard().contains(expRating), "\n Hotel rating not correct in map card")
		softAssert.assertTrue(getHotelAddressInMapCard().contains(expAddress), "\n Hotel address not correct on map card")
		softAssert.assertTrue(verifyImageDisplayedInMapCard(), "\n Hotel image not displayed in map card")
		//softAssert.assertTrue(getHotelDistanceMapCard().contains(propertySearchData.poi.area), "point of interest area not correct in hotel map card")
		softAssert.assertTrue(getHotelPriceInMapCard().contains(actualPrice), "\n Map card Price not correctly matching with search results")
		softAssert.assertTrue(getHotelPriceInMapCard().contains(client.currency), "\n Map card Price not correctly matching with search results")
		//softAssert.assertTrue(verifyRecommendedInformation(hotelNuminSearch, "Recommended for: 1 room"))
		def roomName = ""


		int roomNum = 0
		Map<String, String> actualRoomname = new HashMap<String, String>()
		Map rooms = propertySearchData.room
		Map room

		int numOfRooms = propertySearchData.room.size()
		if (numOfRooms > 1)
			clickMoreRoomTypeCTA(hotelNuminSearch)
		sleep(1000)
		int actualNumRooms = getNumberOfRooms(hotelNuminSearch)
		println "*** Number of rooms: " + actualNumRooms
		for (int r = 0; r < actualNumRooms; r++) {
			roomName = getRoomNameAndMealBasis(hotelNuminSearch, r).trim().replaceAll("\n", " ")
			println "**Room name: " + roomName
			actualRoomname.put(roomName, r.toString())
		}

		for (int i = 0; i < numOfRooms; i++) {

			println "\n *** Room : $i"
			room = rooms.get(rooms.keySet().getAt(i))
			println "room.key :" + room.key
			roomNum = actualRoomname.get(room.key.trim()).toInteger()
			println "Exp room name: " + room.roomName
			println "room Number: " + roomNum

			expandMoreRoomInfo(hotelNuminSearch, roomNum)
			sleep(2000)
			roomName = getRoomName(hotelNuminSearch, roomNum)
			softAssert.assertTrue(roomName.contains(room.roomName), "\n Room name not correct for Room;$i , expected : " + room.roomName + "Actual: " + roomName)
			sleep(500)
			softAssert.assertTrue(verifyRoomDescreption(hotelNuminSearch, roomNum, room.descreption), "\n Room descreption not correct for Room: $roomNum")
			softAssert.assertTrue(verifyRoomFacilities(hotelNuminSearch, roomNum, room.facList), "\n Room facilities list not correct for room: $roomNum")
			softAssert.assertTrue(getMealBasis(hotelNuminSearch, roomNum).contains(room.mealBasis), "\n Meal basis not correct for room : $roomNum")
			if (room.mealBasis.toLowerCase().contains("breakfast")) {
				softAssert.assertTrue(verifyRoomContentHeadings(hotelNuminSearch, roomNum, room.breakfast), "\n breakfast header not correct for room: $roomNum, expected:" + room.breakfast)
				softAssert.assertTrue(verifyRoomContentDescreption(hotelNuminSearch, roomNum, room.breakfastDesc), "\n breakfast descreption not correct for room: $roomNum")
			}
			softAssert.assertTrue(verifyNumberOfPaxRoomDisplayed(hotelNuminSearch, roomNum, areaSearchData.input.noOfPax), "\n Number of Pax not correct for room : $roomNum")
			softAssert.assertEquals(getStatusOfTheRoom(hotelNuminSearch, roomNum), room.status, "\n Room status is not correct for room:$roomNum")
			softAssert.assertTrue(verifyCommissionDisplayed(hotelNuminSearch, roomNum), "\n Commission not displayed for room: $roomNum")
			softAssert.assertTrue(verifyPriceBreakdownHeder(hotelNuminSearch, roomNum, dateRange), "\n Date range not correct on price breakdown table")
			softAssert.assertTrue(verifyPriceBreakDownTableDisplayed(hotelNuminSearch, roomNum), "\n Price break doan table not displayed")
			softAssert.assertTrue(getPriceBreakDownTotalCurrency(hotelNuminSearch, roomNum).contains(client.currency), "\n Currency not correct on price breakdown total price")
			softAssert.assertEquals(getPriceCurrency(hotelNuminSearch, roomNum, client.currency), client.currency, "\n Gross price currency not correct")
			softAssert.assertTrue(AddToItenaryButtonDisplayed(hotelNuminSearch, roomNum), "\n Add to itinerary button not displayed")
			softAssert.assertTrue(verifyCancellationPolicyLinkDisplayed(hotelNuminSearch, roomNum), "\n Cancellation link poilicy link not displayed")
		}
		return true
	}

	List getStarRatingListFromSearchResults() {
		List ratingList = []
		def rating = ""
		at HotelSearchResultsPage
		int num = getNumberOfHotelsInSearchResults()
		softAssert.assertTrue(num > 0, "\n Zero hotels returned ")
		for (int j = 0; j < num; j++) {
			rating = getRatingForTheHotel(j)
			if (rating.equals(""))
				actualRating = 1
			else if (rating.equals("-1"))
				actualRating = 1
			else if (rating.toInteger() > 6)
				actualRating = 6
			else actualRating = getRatingForTheHotel(j).toInteger()
			if (!ratingList.contains(actualRating))
				ratingList << actualRating
		}
		return ratingList
	}

	List getStarRatingListFromSearchResultsListView() {
		List ratingList = []
		def rating = ""
		at HotelSearchResultsPage
		int num = getNumberOfHotelsInSearchResults()
		softAssert.assertTrue(num > 0, "\n Zero hotels returned ")
		for (int j = 0; j < num; j++) {
			rating = getRatingForTheHotelListView(j)
			if (rating.equals("") || rating.equals("-1") || rating.equals("null"))
				actualRating = 1
			else if (rating.toInteger() > 6)
				actualRating = 6
			else actualRating = rating.toInteger()
			if (!(ratingList.contains(actualRating)))
				ratingList << actualRating
		}
		return ratingList
	}

	boolean verifyStarRatingFilterResults() {
		at HotelSearchResultsPage
		clickImageFavoriteButton(0)
		waitTillLoadingIconDisappears()
		List ratingList = getStarRatingListFromSearchResults()
		List ratingListNew = []
		for (int i = 0; i < ratingList.size(); i++) {
			//mouseHoversearchFiltersItem("Stars")
			println "Filter: " + ratingList[i]
			selectStarRatingFilter(ratingList[i].toString())
			sleep(5000)
			ratingListNew = getStarRatingListFromSearchResults()
			for (int j = 0; j < ratingListNew.size; j++) {
				softAssert.assertEquals(ratingListNew[j], ratingList[i], "\nStar Rating is not correct after filter with rating: " + ratingList[i])
				if(ratingListNew==5){
					softAssert.assertTrue(verifyImageFavorited(0),"fav item not displayed when user applies star rating filter")
				}
			}
			selectStarRatingFilter(ratingList[i].toString())
			sleep(5000)
		}
		return true
	}

	boolean verifyStarRatingFilterResultsListView() {
		at HotelSearchResultsPage
		List ratingList = getStarRatingListFromSearchResultsListView()
		println "ratingList before for loop " + ratingList
		List ratingListNew = []
		for (int i = 0; i < ratingList.size(); i++) {
			//	mouseHoversearchFiltersItem("Stars")
			println "Filter: " + ratingList[i]
			selectStarRatingFilter(ratingList[i].toString())
			sleep(5000)
			ratingListNew = getStarRatingListFromSearchResultsListView()
			println "ratingListNew value is: " + ratingListNew
			for (int j = 0; j < ratingListNew.size(); j++) {
				softAssert.assertEquals(ratingListNew[j], ratingList[i], "\nStar Rating is not correct after filter with rating: " + ratingList[i])
			}
			sleep(4000)
			selectStarRatingFilter(ratingList[i].toString())
			sleep(5000)
		}
		return true
	}

	boolean verifyFiltersWithAllRatingsSelectedAndDeselect() {
		at HotelSearchResultsPage
		List ratingList = getStarRatingListFromSearchResults()
		List ratingListNew = []
		sleep(500)
		int numOfHotels = getNumberOfHotelsInSearchResults()
		for (int i = 0; i < ratingList.size(); i++) {
			println "Filter: " + ratingList[i]
			//mouseHoversearchFiltersItem("Stars")
			sleep(500)
			selectStarRatingFilter(ratingList[i].toString())
			sleep(3000)
		}
		sleep(4000)
		softAssert.assertEquals(getNumberOfHotelsInSearchResults(), numOfHotels, "\nNumber of hotels returned not correct when all rating filters selected.")
		for (int i = ratingList.size() - 1; i >= 0; i--) {
			//mouseHoversearchFiltersItem("Stars")
			println "deselect Filter: " + ratingList[i]
			selectStarRatingFilter(ratingList[i].toString())
			sleep(4000)
			ratingListNew = getStarRatingListFromSearchResults()
			for (int j = 0; j < ratingListNew.size; j++) {
				if (i != 0)
					softAssert.assertFalse(ratingListNew[j].equals(ratingList[i]), "\nStar Rating is not correct when deselect filter: " + ratingList[i] + "ACtual:" + ratingListNew[j] + "Hotel;: $j")
			}
			sleep(4000)
		}
		softAssert.assertEquals(getNumberOfHotelsInSearchResults(), numOfHotels, "\nNumber of hotels returned not correct when all rating filters de selected.")
		return true
	}

	boolean verifyFiltersWithAllRatingsSelectedAndDeselectListView() {
		at HotelSearchResultsPage
		List ratingList = getStarRatingListFromSearchResultsListView()
		List ratingListNew = []
		int numOfHotels = getNumberOfHotelsInSearchResults()

		for (int i = 0; i < ratingList.size(); i++) {
			//mouseHoversearchFiltersItem("Stars")
			sleep(500)
			println "Filter: " + ratingList[i]
			selectStarRatingFilter(ratingList[i].toString())
		}
		sleep(5000)
		softAssert.assertEquals(getNumberOfHotelsInSearchResults(), numOfHotels, "\nNumber of hotels returned not correct when all rating filters selected.")
		for (int i = ratingList.size() - 1; i >= 0; i--) {
			//mouseHoversearchFiltersItem("Stars")
			println "deselect Filter: " + ratingList[i]
			selectStarRatingFilter(ratingList[i].toString())
			sleep(4000)
			ratingListNew = getStarRatingListFromSearchResultsListView()
			for (int j = 0; j < ratingListNew.size(); j++) {
				if (i != 0)
					softAssert.assertFalse(ratingListNew[j].equals(ratingList[i]), "\nStar Rating is not correct when deselect filter: " + ratingList[i] + "ACtual:" + ratingListNew[j] + "Hotel;: $j")
			}
			sleep(2000)
		}
		softAssert.assertEquals(getNumberOfHotelsInSearchResults(), numOfHotels, "\nNumber of hotels returned not correct when all rating filters de selected.")
		return true
	}

	boolean verifyLocationFilterResults(AreaSearchData areaSearchData) {
		at HotelSearchResultsPage
		List locFilterList = areaSearchData.filters.location
		List numOfHotels = areaSearchData.filters.numOfHotels
		def paginationheader = ""
		int num = 0
		for (int i = 0; i < locFilterList.size(); i++) {
			//	mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			//println "Filter: "+ locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
			waitTillLoadingIconDisappears()
			sleep(2000)
			num = getNumberOfHotelsInSearchResults()
			try {
				if (getEssentialInformationText().toString().contains(areaSearchData.expected.alertText)) {
					clickCloseLightbox()
					sleep(500)
					num = 0
				}
			}
			catch (Exception e) {
			}
			sleep(1000)
			waitUntilPaginationTopAppears()
			paginationheader = getPaginationCounterTop()
			if (numOfHotels[i].toInteger() <= 20)
				softAssert.assertTrue(num == numOfHotels[i].toString().toInteger(), "\nNumber of hotels returned not correct when location:" + locFilterList[i] + " filters selected, expected: " + numOfHotels[i] + "Actual: " + num)
			else softAssert.assertTrue(paginationheader.contains(numOfHotels[i].toString()), "\nNumber of hotels returned not correct when location:" + locFilterList[i] + " filters selected, expected: " + numOfHotels[i] + "Actual: " + paginationheader)
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			//clickOnLocationFilter(locFilterList[i])
			sleep(3000)
		}
		return true
	}

	boolean verifyLocationFilterResults(CitySearchData citySearchData) {
		at HotelSearchResultsPage
		List locFilterList = citySearchData.filters.location
		List numOfHotels = citySearchData.filters.numOfHotels
		def paginationheader = ""
		int num = 0
		for (int i = 0; i < locFilterList.size(); i++) {
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			println "Filter: " + locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
			sleep(3000)
			num = getNumberOfHotelsInSearchResults()
			sleep(2000)
			waitUntilPaginationTopAppears()
			paginationheader = getPaginationCounterTop()
			if (numOfHotels[i].toInteger() <= 20)
				softAssert.assertTrue(num == numOfHotels[i].toString().toInteger(), "\nNumber of hotels returned not correct when location:" + locFilterList[i] + " filters selected, expected: " + numOfHotels[i] + "Actual: " + num)
			else softAssert.assertTrue(paginationheader.contains(numOfHotels[i].toString()), "\nNumber of hotels returned not correct when location:" + locFilterList[i] + " filters selected, expected: " + numOfHotels[i] + "Actual: " + paginationheader)
			//clicksearchFiltersItem("Locations")
			clickOnLocationFilter(locFilterList[i])
			sleep(3000)
		}
		return true
	}

	boolean verifyFiltersWithAllLocationFiltersSelectedAndDeselect(AreaSearchData areaSearchData) {
		at HotelSearchResultsPage
		List locFilterList = areaSearchData.filters.location
		int numOfHotels = getNumberOfHotelsInSearchResults()
		def paginationheader = ""
		for (int i = 0; i < locFilterList.size(); i++) {
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			sleep(3000)
			println "Filter: " + locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
			try {
				if (getEssentialInformationText().toString().contains(areaSearchData.expected.alertText)) {
					clickCloseLightbox()
					sleep(500)
				}

			}
			catch (Exception e) {

			}
		}
		sleep(4000)

		softAssert.assertTrue(getNumberOfHotelCardsInSearchResults() == numOfHotels, "\nNumber of hotels returned not correct when all location filters selected.")
		for (int i = 0; i < locFilterList.size(); i++) {
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			println "deselect Filter: " + locFilterList[i]
			softAssert.assertTrue(getNumberOfHotelCardsInSearchResults() > 0, "\nNumber of hotels returned not correct when filter deselected: " + locFilterList[i])
		}
		sleep(3000)
		softAssert.assertTrue(getNumberOfHotelCardsInSearchResults() == numOfHotels, "\nNumber of hotels returned not correct when all location filters deselected.")
		return true
	}

	boolean verifyFiltersWithAllLocationFiltersSelectedAndDeselect(CitySearchData citySearchData) {
		at HotelSearchResultsPage
		List locFilterList = citySearchData.filters.location
		int numOfHotels = getNumberOfHotelCardsInSearchResults()
		for (int i = 0; i < locFilterList.size(); i++) {
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			sleep(2000)
			println "Filter: " + locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
		}
		sleep(3000)
		softAssert.assertTrue(getNumberOfHotelCardsInSearchResults() == numOfHotels, "\nNumber of hotels returned not correct when all location filters selected.")
		for (int i = 0; i < locFilterList.size(); i++) {
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			println "deselect Filter: " + locFilterList[i]
			softAssert.assertTrue(getNumberOfHotelCardsInSearchResults() > 0, "\nNumber of hotels returned not correct when filter deselected: " + locFilterList[i])
		}
		sleep(3000)
		softAssert.assertTrue(getNumberOfHotelCardsInSearchResults() == numOfHotels, "\nNumber of hotels returned not correct when all location filters deselected.")
		return true
	}

	boolean verifyFacilityFilterResults() {
		at HotelSearchResultsPage
		List facFilterList = getHotelFacilitiesSearchResults()
		List hotelFac = []
		int num = 0
		for (int i = 0; i < facFilterList.size(); i++) {
			mouseHoversearchFiltersItem("Facilities")
			clicksearchFiltersItem("Facilities")
			println "Filter: " + facFilterList[i]
			clickOnFacilityFilter(facFilterList[i])
			sleep(1000)
			for (int j = 0; j < getNumberOfHotelCardsInSearchResults(); j++) {
				hotelFac = getHotelFacilitiesList(j)
				softAssert.assertTrue(hotelFac.contains(facFilterList[i]), "\nFacility for hotel: $j is not correct after filter with facility: " + facFilterList[i])
			}
			clicksearchFiltersItem("Facilities")
			clickOnFacilityFilter(facFilterList[i])
			sleep(1000)
		}
		return true
	}

	boolean verifyWithAllFacilitySelectedAndDeselect() {
		at HotelSearchResultsPage
		List facFilterList = getHotelFacilitiesSearchResults()
		List hotelFac = []
		int numOfHotels = getNumberOfHotelCardsInSearchResults()
		for (int i = 0; i < facFilterList.size(); i++) {
			println "Filter: " + facFilterList[i]
			mouseHoversearchFiltersItem("Facilities")
			clickOnFacilityFilter(facFilterList[i].toString())
		}
		sleep(2000)
		softAssert.assertEquals(getNumberOfHotelCardsInSearchResults(), numOfHotels, "\nNumber of hotels returned not correct when all facility filters selected.")
		for (int i = facFilterList.size() - 1; i >= 0; i--) {
			mouseHoversearchFiltersItem("Facilities")
			println "deselect Filter: " + facFilterList[i]
			clickOnFacilityFilter(facFilterList[i].toString())
			sleep(2000)
			for (int j = 0; j < getNumberOfHotelCardsInSearchResults(); j++) {
				hotelFac = getHotelFacilitiesList(j)
				if (i != 0)
					softAssert.assertFalse(hotelFac.contains(facFilterList[i]), "\nFacilities for the hotel: $j is not correct when deselect filter: " + facFilterList[i])
			}
			sleep(2000)
		}
		softAssert.assertEquals(getNumberOfHotelCardsInSearchResults(), numOfHotels, "\nNumber of hotels returned not correct when all facility filters de selected.")
		return true
	}


	boolean verifyMultiFilters(AreaSearchData areaSearchData) {

		at HotelSearchResultsPage
		//mouseHoversearchFiltersItem("Facilities")
		clickOnFacilityFilter(areaSearchData.multiFilters.facility)
		sleep(3000)
		//clicksearchFiltersItem("Locations")
		clickOnLocationFilter(areaSearchData.multiFilters.location)
		sleep(3000)
		//mouseHoversearchFiltersItem("Stars")
		selectStarRatingFilter(areaSearchData.multiFilters.rating)
		sleep(4000)
		softAssert.assertTrue(getNumberOfHotelCardsInSearchResults().toInteger() >= areaSearchData.multiFilters.numOfHotels.toInteger(), "\nNumber of hotels returned not correct when all location filters deselected.")
		List hotelFac = []
		for (int j = 0; j < getNumberOfHotelCardsInSearchResults(); j++) {
			softAssert.assertTrue(getRatingForTheHotel(j).equals(areaSearchData.multiFilters.rating), "\nStar Rating is not correct when deselect filter: " + areaSearchData.multiFilters.rating + "ACtual:" + getRatingForTheHotel(j) + "Hotel;: $j")
			hotelFac = getHotelFacilitiesList(j)
			softAssert.assertTrue(hotelFac.contains(areaSearchData.multiFilters.facilityResults), "\nFacility for hotel: $j is not correct after filter with facility: " + areaSearchData.multiFilters.facility)
		}
		return true
	}

	boolean deselectMultiFilters(AreaSearchData areaSearchData) {
		at HotelSearchResultsPage
		/*//mouseHoversearchFiltersItem("Facilities")
		clickOnFacilityFilter(areaSearchData.multiFilters.facility)
		sleep(5000)
		//clicksearchFiltersItem("Locations")
		clickOnLocationFilter(areaSearchData.multiFilters.location)
		sleep(5000)
		//mouseHoversearchFiltersItem("Stars")
		selectStarRatingFilter(areaSearchData.multiFilters.rating)
		sleep(5000)*/
		clickOnClearAll()

		return true
	}

	boolean verifyPaginationTopArrows(AreaSearchData areaSearchData) {
		int noOfHotels = areaSearchData.expected.numOfHotels.toInteger()
		int totPages
		def pageHeader = "", pageHeader2, pageHeader3, pageHeader7
		if (noOfHotels % 20 > 0)
			totPages = noOfHotels / 20 + 1
		else totPages = noOfHotels / 20
		if (noOfHotels >= 20)
			pageHeader = "Results 1 - 20 of " + noOfHotels
		else pageHeader = "Results 1 - " + noOfHotels + " of " + noOfHotels
		if (noOfHotels >= 40)
			pageHeader2 = "Results 21 - 40 of " + noOfHotels
		else pageHeader2 = "Results 21 - " + noOfHotels + " of " + noOfHotels
		if (noOfHotels >= 60)
			pageHeader3 = "Results 41 - 60 of " + noOfHotels
		else pageHeader3 = "Results 41 - " + noOfHotels + " of " + noOfHotels

		softAssert.assertTrue(verifyPaginationCounterTop(pageHeader), "\n 1 Pagination header on top is not correct in first page  when search with area!!")
		sleep(500)
		clickPaginationRightArrow()
		sleep(6000)
		softAssert.assertTrue(verifyPaginationCounterTop(pageHeader2), "\n 2 Pagination header on top is not correct in second page when search with area!!")
		sleep(1000)
		clickPaginationLeftArrow()
		waitTillLoadingIconDisappears()
		sleep(5000)
		softAssert.assertTrue(verifyPaginationCounterTop(pageHeader), "\n 3 Pagination header on top is  not correct in first page when search with area!!")
		if (totPages > 2) {
			clickPaginationRightArrow()
			sleep(6000)
			clickPaginationRightArrow()
			sleep(6000)
			softAssert.assertTrue(verifyPaginationCounterTop(pageHeader3), "\n 4 Pagination header on top is not correct in third page when search with area!!")
			clickPaginationLeftArrow()
			sleep(6000)
			softAssert.assertTrue(verifyPaginationCounterTop(pageHeader2), "\n 5 Pagination header on top is not correct in second page when search with area!!")
			clickPaginationLeftArrow()
			sleep(6000)
			softAssert.assertTrue(verifyPaginationCounterTop(pageHeader), "\n 6 Pagination header on top is  not correct in first page when search with area!!")
		}
		return true
	}

	boolean verifyPaginationBottom(AreaSearchData areaSearchData) {
		int noOfHotels = areaSearchData.expected.numOfHotels.toInteger()
		int totPages
		def pageHeader = "", pageHeader2, pageHeader3, pageHeader7
		if (noOfHotels % 20 > 0)
			totPages = noOfHotels / 20 + 1
		else totPages = noOfHotels / 20
		if (noOfHotels >= 20)
			pageHeader = "Results 1 - 20 of " + noOfHotels
		else pageHeader = "Results 1 - " + noOfHotels + " of " + noOfHotels
		if (noOfHotels >= 40)
			pageHeader2 = "Results 21 - 40 of " + noOfHotels
		else pageHeader2 = "Results 21 - " + noOfHotels + " of " + noOfHotels
		if (noOfHotels >= 60)
			pageHeader3 = "Results 41 - 60 of " + noOfHotels
		else pageHeader3 = "Results 41 - " + noOfHotels + " of " + noOfHotels


		softAssert.assertTrue(verifyPaginationCounterDisplayed(pageHeader), "\n 1 Pagination header on bottom is not correct in first page  when search with area!!")
		softAssert.assertTrue(clickBottomPaginationNumberDisplayed("2"), "\n Page number 2 not displayed!!")
		sleep(5000)
		softAssert.assertTrue(verifyPaginationCounterDisplayed(pageHeader2), "\n 2 Pagination header on bottom is not correct in second page  when search with area!!")
		softAssert.assertTrue(clickBottomPaginationNumberDisplayed("1"), "\n Page number 1 not displayed!!")
		sleep(5000)
		softAssert.assertTrue(verifyPaginationCounterDisplayed(pageHeader), "\n 3 Pagination header on bottom is not correct in first page  when search with area!!")
		softAssert.assertTrue(clickBottomPaginationNumberDisplayed("3"), "\n Page number 3 not displayed!!")
		sleep(5000)
		softAssert.assertTrue(verifyPaginationCounterDisplayed(pageHeader3), "\n 4 Pagination header on bottom is not correct in third page  when search with area!!")

		softAssert.assertTrue(clickBottomPaginationNumberDisplayed("1"), "\n Page number 1 not displayed!!")
		sleep(5000)
		softAssert.assertTrue(verifyPaginationCounterDisplayed(pageHeader), "\n 5 Pagination header on bottom is not correct in first page  when search with area!!")

		clickPaginationRightArrow()
		sleep(5000)
		softAssert.assertTrue(verifyPaginationCounterDisplayed(pageHeader2), "\n 6 Pagination header on bottom is not correct in second page  when search with area!!")

		clickPaginationLeftArrow()
		waitTillLoadingIconDisappears()
		sleep(5000)
		softAssert.assertTrue(verifyPaginationCounterDisplayed(pageHeader), "\n 7 Pagination header on bottom is not correct in first page  when search with area!!")

		return true
	}

	boolean verifySortingAndFilters(AreaSearchData areaSearchData) {
	}

	def getTYRatingAndReviewsInPLP(int hotelNum, HotelTransferTestResultData resultData) {
		at HotelSearchResultsPage
		def dataMap = [:]
		dataMap.hotelLabel = getHotelNameLabel()
		def hotel = getHotelEle(hotelNum)
		dataMap.hotelName = getHotelName(hotelNum).toString()
		tyId = dbUtil.getTyId(getExternalRefId(hotel))
		this.tyId = tyId
		def uiScore = trustYouRating(hotel).toString().trim()
		dataMap.score = uiScore.toFloat()
		println "UI Score" + uiScore
		dataMap.uiReviews = noOfReviews(hotel).toString()
		dataMap.uiStar = trustYouStarRating(hotel).toString()

		dataMap.DBtrustYouRatingPLP = dbUtil.getRating(tyId).toFloat()
		dataMap.DBnumberOfReviewsPLP = dbUtil.getReviewCount(tyId)

		clickOnTrustYouTotalReviews(getHotelEle(hotelNum))
		waitTillLoadingIconDisappears()
		return dataMap
	}

	def getTYRatingAndReviewsPDP(HotelTransferTestResultData resultData, String langCode) {
		at AgentHotelInfoPage
		def dataMap = [:]
		sleep(3000)
		dataMap.tyWidget = navigateToTheWidgetSection()
	//	def tyId = dbUtil.getTyId(tyId)
		dataMap.UIrating = getTrustYouHotelRatingOnPDP()
		dataMap.UIReviews = getNumberOfReviews()
		clickTrustYouHotelStarRating()
		dataMap.clickTYRating = navigateToTheWidgetSection()
		scrollToTopOfThePage()
		clickNumberOfTYReviews()
		dataMap.clickTYReviews = navigateToTheWidgetSection()
		dataMap.goodToKnwDB = dbUtil.goodToKnowList(tyId, langCode)
		dataMap.goodToKnwUI = getGoodToKnow()
		dataMap.dbCategories = dbUtil.categoryList(tyId, langCode)
		dataMap.topRatedCategories = getTopCategoryScore()
		dataMap.summeryLabel = getSummeryTextLabel()
		dataMap.summery = dbUtil.getText(tyId, "TY_SUMMARY", langCode, "AWS")
		dataMap.summeryUI = getSummaryText()
		scrollToTopOfThePage()
		clickOnsummeryTextLink()
		waitTillLoadingIconDisappears()
		dataMap.clickTYsummeryLink = navigateToTheWidgetSection()
		return dataMap
	}

	/*********** Verifications*******************/

	def verifySearchResults(HotelTransferTestResultData resultData,PropertySearchData data){
		softAssert.assertTrue(resultData.hotel.searchResults.searchReturned,"Search results not returned.")
		assertionEquals(resultData.hotel.searchResults.hotelInfoPLP.hotelLabel,data.input.hotelLabel,"hotel label not displayed.")
		assertionEquals(resultData.hotel.searchResults.hotelInfoPLP.hotelName,data.input.property,"hotel did not displayed in search results")
	}

	def trustYouValidationPLP(HotelTransferTestResultData resultData){
		assertionEquals(resultData.hotel.searchResults.hotelInfoPLP.score.toString(),resultData.hotel.searchResults.hotelInfoPLP.DBtrustYouRatingPLP.toString(),"rating score did not match against DB")
		assertionEquals(resultData.hotel.searchResults.hotelInfoPLP.DBnumberOfReviewsPLP.toString(),resultData.hotel.searchResults.hotelInfoPLP.uiReviews.toString(),"reviews are not matching against DB")
		softAssert.assertTrue( resultData.hotel.hotelInfo.hotelDetailsPDP.tyWidget,"TY wideget section not displayed.")
	}

	def trustYouRatingReviewValidation(HotelTransferTestResultData resultData){
		softAssert.assertTrue(resultData.hotel.hotelInfo.hotelDetailsPDP.UIrating.toString().equals( resultData.hotel.searchResults.hotelInfoPLP.score.toString()), "Rating displayed in the UI is not matching with DB value for the hotel:" +hotelName )
		softAssert.assertTrue(resultData.hotel.hotelInfo.hotelDetailsPDP.clickTYRating, "User is not navigated to to TrustYou widget when clicked on the score rating" )
		softAssert.assertTrue(resultData.hotel.hotelInfo.hotelDetailsPDP.UIReviews.toString().equals(resultData.hotel.searchResults.hotelInfoPLP.uiReviews.toString()), "Total number of reviews displayed in the UI is not matching with DB value for the hotel:" +hotelName )
		softAssert.assertTrue(resultData.hotel.hotelInfo.hotelDetailsPDP.clickTYReviews, "User is not navigated to to TrustYou widget when clicked on reviews" )
		assertionEquals(resultData.hotel.searchResults.hotelInfoPLP.uiReviews.toString(),resultData.hotel.hotelInfo.hotelDetailsPDP.UIReviews.toString(),"reviews not matching against PDP page")


}

	def verifyIndividualRatingDistrub() {
		at AgentHotelInfoPage
		Map reviewDistribution = getRatingDistPrd()
		//[5:100, 4:12, 3:2323, 2:342, 1:89]
		def reviewDistributionDB = dbUtil.getReviewDistribution(tyId)
		//1::0|2::4|3::54|4::345|5::23
		def frmDb = reviewDistributionDB.split(/\|/)
		reviewDistribution.keySet().each { key ->
			switch (key) {
				case ~/1/:
					softAssert.assertTrue(frmDb[4].split("::")[1] == reviewDistribution.getAt(key), " rating dsitribution for Key $key, actual ${reviewDistribution.getAt(key)}, expected: ${frmDb[4].split("::")[1]}")
					break;
				case ~/2/:
					softAssert.assertTrue(frmDb[3].split("::")[1] == reviewDistribution.getAt(key), " rating dsitribution for Key $key, actual ${reviewDistribution.getAt(key)}, expected: ${frmDb[3].split("::")[1]}")
					break;
				case ~/3/:
					softAssert.assertTrue(frmDb[2].split("::")[1] == reviewDistribution.getAt(key), " rating dsitribution for Key $key, actual ${reviewDistribution.getAt(key)}, expected: ${frmDb[2].split("::")[1]}")
					break;
				case ~/4/:
					softAssert.assertTrue(frmDb[1].split("::")[1] == reviewDistribution.getAt(key), " rating dsitribution for Key $key, actual ${reviewDistribution.getAt(key)}, expected: ${frmDb[1].split("::")[1]}")
					break;
				case ~/5/:
					softAssert.assertTrue(frmDb[0].split("::")[1] == reviewDistribution.getAt(key), " rating dsitribution for Key $key, actual ${reviewDistribution.getAt(key)}, expected: ${frmDb[0].split("::")[1]}")
					break;
			}
		}


	}
	def goodToKnwCategoryValidationPDP(HotelTransferTestResultData resultData){
		def uiGTK = resultData.hotel.hotelInfo.hotelDetailsPDP.goodToKnwUI
		def dbGTK =  resultData.hotel.hotelInfo.hotelDetailsPDP.goodToKnwDB.toString().split(/\|/)
		println "dbGTK: $dbGTK"
		dbGTK.each{ dbGTKText ->
			softAssert.assertTrue(uiGTK.toString().contains(dbGTKText.trim()), "IN UI: $uiGTK, in DB: $dbGTKText\n")}

		def categoryList = ""
		Map uiCategory = resultData.hotel.hotelInfo.hotelDetailsPDP.topRatedCategories
		Map dbCategories = resultData.hotel.hotelInfo.hotelDetailsPDP.dbCategories

		uiCategory.each{categoryName, value->
			categoryList = categoryList + categoryName + ", "
		}
		uiCategory.each{categoryName, score ->
			softAssert.assertTrue(dbCategories.getAt(categoryName)['categoryName'].toString().trim()==categoryName.toString().trim(), "CategoryList in UI: ${categoryName} in DB: ${dbCategories.getAt(categoryName)['categoryName'].toString().trim()}\n")
			softAssert.assertTrue(dbCategories.getAt(categoryName)['score'].toString().trim() == score.toString().trim(), "For Category: $categoryName, CategoryScore in UI $score, in DB ${dbCategories.getAt(categoryName)['score']}")
		}
	}
	def summeryTextValidationPDP(HotelTransferTestResultData resultData){
		assertionEquals(resultData.hotel.hotelInfo.hotelDetailsPDP.summeryLabel,data.reports.summeryLabel,"summery label not matching")
		softAssert.assertTrue(resultData.hotel.hotelInfo.hotelDetailsPDP.summeryUI.toString().contains(resultData.hotel.hotelInfo.hotelDetailsPDP.summery.toString()),"summery review not matched")
		softAssert.assertTrue(resultData.hotel.hotelInfo.hotelDetailsPDP.clickTYsummeryLink,"TY widget not displayed.")

	}
	def verifyHotelInfoPDP(PropertySearchData propertySearchData) {
		at AgentHotelInfoPage
		def contentType = dbUtil.contentSelectionType(getExternalRefId())
		at HotelInformationPageBooking
		softAssert.assertEquals(getHotelNameInInfoPage(), propertySearchData.input.property, "\n Hotel name not correct on PDP expected: " + propertySearchData.input.property)
		softAssert.assertTrue(getStarRating().contains(expRating), "\n Hotel rating not correct on PDP , expected: " + expRating)
		softAssert.assertTrue(getHotelAddress().contains(expAddress), "\n Hotel Address not correct on PDP , expected: " + expAddress)
		softAssert.assertTrue(clickViewMoreDisplayed(), "\n View More link not displayed")
		at AgentHotelInfoPage
		softAssert.assertTrue(getFavIconsStatus(),"fav icons on right side on PDP not displayed")

		sleep(1500)
		softAssert.assertEquals((getNumberOfHotelImages()).toString(), propertySearchData.images.noOfImgs, "\nNumber of images not correct")
		def reportsData = getHotelReportsData()
		List reportHeader = propertySearchData.reports.header
		for (int i = 0; i < reportHeader.size(); i++) {
			softAssert.assertEquals(getReportHeader(i), reportHeader[i], "\n report header not correct for report $i")
		}
		switch(contentType){
			case ~/(?i)TrustYou/:
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().startsWith(getPleaseNoteReport(11)), "\n earliestBreakfast label report not correct")
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.roomService.toString().startsWith(getPleaseNoteReport(12)), "\n earliestBreakfast report not correct")

				softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().endsWith(getPleaseNoteReport(13)), "\n roomService label report not correct")
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.roomService.toString().endsWith(getPleaseNoteReport(14)), "\n roomService report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(28).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[0]), "\n shuttlBusOptions1 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(28).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[1]), "\n shuttlBusOptions2 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(30).contains(propertySearchData.pleaseNoteReport.parkingOptions[0]), "\n parkingOptions1 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(30).contains(propertySearchData.pleaseNoteReport.parkingOptions[1]), "\n parkingOptions2 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(31).contains(propertySearchData.pleaseNoteReport.parkingOptions[2]), "\n parkingOptions2 report not correct")

				break;

			case ~/(?i)GC/:
				softAssert.assertTrue(getHotelReportsData(0).contains(propertySearchData.reports.general), "\n General report not correct")
				softAssert.assertTrue(getHotelReportsData(1).contains(propertySearchData.reports.exterior), "\n Exterior report not correct")
				softAssert.assertTrue(getHotelReportsData(2).contains(propertySearchData.reports.lobby), "\n Lobby report not correct")
				softAssert.assertTrue(getHotelReportsData(3).contains(propertySearchData.reports.location), "\n Location report not correct")
				softAssert.assertTrue(getHotelReportsData(4).contains(propertySearchData.reports.restaurant), "\n Restaurant report not correct")
				softAssert.assertTrue(getHotelReportsData(5).contains(propertySearchData.reports.apartment), "\n board type report not correct")
				softAssert.assertTrue(getHotelReportsData(6).contains(propertySearchData.reports.boardType), "\n apartment report not correct")

				softAssert.assertTrue(getFacilitiesBlockTxt(24).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[0]), "\n shuttlBusOptions1 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(24).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[1]), "\n shuttlBusOptions2 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(26).contains(propertySearchData.pleaseNoteReport.parkingOptions[0]), "\n parkingOptions1 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(26).contains(propertySearchData.pleaseNoteReport.parkingOptions[1]), "\n parkingOptions2 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(27).contains(propertySearchData.pleaseNoteReport.parkingOptions[2]), "\n parkingOptions2 report not correct")


				softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().startsWith(getPleaseNoteReport(9)), "\n earliestBreakfast label report not correct")
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.earlyBreakfast.toString().startsWith(getPleaseNoteReport(10)), "\n earliestBreakfast report not correct")

				softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().endsWith(getPleaseNoteReport(11)), "\n roomService label report not correct")
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.roomService.toString().endsWith(getPleaseNoteReport(12)), "\n roomService report not correct")

				break;
			case ~/(?i)Hybrid/:
				softAssert.assertTrue(getHotelReportsData(0).contains(propertySearchData.reports.general), "\n General report not correct")
				softAssert.assertTrue(getHotelReportsData(1).contains(propertySearchData.reports.boardType), "\n Location report not correct")
				softAssert.assertTrue(getHotelReportsData(2).contains(propertySearchData.reports.apartment), "\n Restaurant report not correct")

				softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().startsWith(getPleaseNoteReport(9)), "\n earliestBreakfast label report not correct")
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.roomService.toString().startsWith(getPleaseNoteReport(10)), "\n earliestBreakfast report not correct")
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.earliestBreakfast.toString().endsWith(getPleaseNoteReport(11)), "\n roomService label report not correct")
				softAssert.assertTrue(propertySearchData.pleaseNoteReport.roomService.toString().endsWith(getPleaseNoteReport(12)), "\n roomService report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(28).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[0]), "\n shuttlBusOptions1 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(28).contains(propertySearchData.pleaseNoteReport.shuttlBusOptions[1]), "\n shuttlBusOptions2 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(30).contains(propertySearchData.pleaseNoteReport.parkingOptions[0]), "\n parkingOptions1 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(30).contains(propertySearchData.pleaseNoteReport.parkingOptions[1]), "\n parkingOptions2 report not correct")
				softAssert.assertTrue(getFacilitiesBlockTxt(31).contains(propertySearchData.pleaseNoteReport.parkingOptions[2]), "\n parkingOptions2 report not correct")

				break;
			default:
				println("Invalid ContentType \'$contentType\' for hotel")
				break;
		}

		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkInTime.toString().startsWith(getPleaseNoteReport(1)), "\n checkInTime label report not correct, Actual: " + getPleaseNoteReport(1))
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkOutTime.toString().startsWith(getPleaseNoteReport(2)), "\n check in time report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkInTime.toString().endsWith(getPleaseNoteReport(3)), "\n checkout time label report not correct, Actual: " + getPleaseNoteReport(1))
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.checkOutTime.toString().endsWith(getPleaseNoteReport(4)), "\n checkOutTime report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.typeOfProperty.toString().startsWith(getPleaseNoteReport(5)), "\n typeOfProperty report label not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.noRooms.toString().startsWith(getPleaseNoteReport(6)), "\n type of property report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.typeOfProperty.toString().endsWith(getPleaseNoteReport(7)), "\n no.of rooms label report not correct")
		softAssert.assertTrue(propertySearchData.pleaseNoteReport.noRooms.toString().endsWith(getPleaseNoteReport(8)), "\n noRooms report not correct")


		//softAssert.assertTrue(getPleaseNoteReport(7).contains(propertySearchData.pleaseNoteReport.maidService), "\n maidService report not correct")

		assertList(propertySearchData.facilities.facList,getHotelFacilitiesPDP(),"facilities list is not matched")
		//softAssert.assertTrue(getPleaseNoteReport(14).contains(propertySearchData.pleaseNoteReport.coachDropoff), "\n coachDropoff report not correct")
		//softAssert.assertTrue(getPleaseNoteReport(15).contains(propertySearchData.pleaseNoteReport.minimumAge), "\n minimumAge report not correct")
		//softAssert.assertTrue(getHotelAreaInfo().contains(propertySearchData.pleaseNoteReport.area), "\n area report not correct expected: "+propertySearchData.pleaseNoteReport.area)
		//softAssert.assertTrue(getHotelFacilities(propertySearchData.facilities.facList), "\n Hotel Facility List not correct")

		int numOfRooms = propertySearchData.room.size()
		Map rooms = propertySearchData.room
		for (int i = 0; i < numOfRooms; i++) {
			at HotelInformationPageBooking
			clickExpandRoomButton(i)
			sleep(1000)
			clickMoreRoomInfoButton(i + 1, 0)
			sleep(1000)
			Map room = rooms.get(rooms.keySet().getAt(i))
			softAssert.assertTrue(verifyRoomDescreption(room.descreption, i), "\n Room descreption not correct for Room: $i")
			softAssert.assertEquals(getRoomName(i), room.roomName, "\n Room name not correct for Room;$i")
			//softAssert.assertEquals(getNumberOfRoomImages(i).toString(),propertySearchData.images.noOfRoomImgs, "\n Number of  room images not correct" )
			softAssert.assertTrue(verifyRoomFacilitiesList(room.facList, i), "\n Room facilities list not correct for room: $i")
			softAssert.assertEquals(getMealBasis(i + 1).trim(), room.mealBasis, "\n Meal basis not correct for room : $i")
			softAssert.assertTrue(verifyNumberOfPax(i + 1, room.pax), "\n Number of Pax not correct for room : $i")
			softAssert.assertEquals(getRoomStatus(i), room.status, "\n Room status is not correct for room:$i")
			softAssert.assertTrue(verifyCommissionDisplayed(i + 1), "\n Commission not displayed for room: $i")
			sleep(2000)
			scrollToBottomOfThePage()
			sleep(1000)
			softAssert.assertTrue(verifyPriceBreakDownDates(dateRange, i + 1, 0), "\n Date range not correct on price breakdown table")
			softAssert.assertTrue(verifyPriceBreakdownTableDisplayed(i + 1, 0), "\n Price break doan table not displayed")
			softAssert.assertTrue(getPriceBreakDownTotalCurrency(i).contains(client.currency), "\n Currency not correct on price breakdown total price")
			softAssert.assertTrue(verifyRecommendedRoomAddToItinerary(i+1), "\n Add to itinerary button not displayed")
			String cancellationLink = getCancellationPolicyLinkTextAfterReDesign(hotelNuminSearch, i).toString()
			softAssert.assertFalse(cancellationLink.isEmpty(),"cancellation policy link not displayed")
		}
		at AgentHotelInfoPage
		softAssert.assertTrue(verifyMapDisplayed(), "Map Block not displayed")
		softAssert.assertTrue(getIframeDataLoadedStatus(),"TY widget data not loaded")
		/*if(propertySearchData.tripAdvisor.logo.equals("yes")){
			softAssert.assertTrue(verifyTripAdvisorLogoDisplayed(), "\n Trip advisor logo not displayed")
			clickOnTripAdvisorReviews()
			clickBackButton()
		}*/
	}
}


