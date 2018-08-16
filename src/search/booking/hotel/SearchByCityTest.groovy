package com.kuoni.qa.automation.test.search.booking.hotel

import spock.lang.Ignore
import spock.lang.IgnoreRest

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.PropertySearchData
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage

class SearchByCityTest extends HotelBookingBaseSpec {


	def "TC1 - Single rooms Single rate plans"(){
		println "**** TestName: TC1 - Single rooms Single rate plans ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Kingdom-SITUJ15XMLAuto")
		searchByCitySingleRoom2A1C(citySearchData, propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(citySearchData,propertySearchData), "\n Hotel not found in search results")
	}

	/*
	def "TC2 - multiple rooms  multiple rate plans 2 rooms available"(){
		println "**** TestName: TC2 - multiple rooms  multiple rate plans 2 rooms available ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Cu - SITUJ15XMLAuto")
		searchByCitySingleRoom2A1C(citySearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(citySearchData,propertySearchData), "\n Hotel not found in search results")
	}
*/
	def "TC3 - multiple rooms  multiple rate plans item has 3 or more rooms available"(){
		println "**** TestName:TC3 - multiple rooms  multiple rate plans item has 3 or more rooms available ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("National Citizen-SITUJ15XMLAuto")
		searchByCitySingleRoom2A1C(citySearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(citySearchData,propertySearchData), "\n Hotel not found in search results")
	}


	def "TC4 - Single rooms Single rate plans ListView"(){
		println "**** TestName: TC4 - Single rooms Single rate plans ListView ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Kingdom-SITUJ15XMLAuto")
		clickOnListViewIcon()
		searchByCitySingleRoom2A1C(citySearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(citySearchData,propertySearchData), "\n Hotel not found in search results")
	}


	def "TC5 - multiple rooms  multiple rate plans 2 rooms available List view"(){
		println "**** TestName: TC5 - multiple rooms  multiple rate plans 2 rooms available List view ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Cu - SITUJ15XMLAuto")
		clickOnListViewIcon()
		searchByCitySingleRoom2A1C(citySearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(citySearchData,propertySearchData), "\n Hotel not found in search results")
	}



	def "TC6 - multiple rooms  multiple rate plans item has 3 or more rooms available List view"(){
		println "**** TestName: TC6 - multiple rooms  multiple rate plans item has 3 or more rooms available List view ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("National Citizen-SITUJ15XMLAuto")
		clickOnListViewIcon()
		searchByCitySingleRoom2A1C(citySearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(citySearchData,propertySearchData), "\n Hotel not found in search results")
	}


	def "TC7 - By City level Grid view Filter - By StarRating"(){
		println "**** TestName:TC7 -  By City level Grid view Filter - By StarRating ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyStarRatingFilterResults(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllRatingsSelectedAndDeselect(), "\n Filers not applied in search when select all")
	}

	def "TC8 - By City level Grid view Filter - By Location"(){
		println "**** TestName: TC8 - By City level Grid view Filter - By Location ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyLocationFilterResults(citySearchData), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllLocationFiltersSelectedAndDeselect(citySearchData), "\n Filers not applied in search when select all")
	}

	@Ignore
	def "TC9 - By City level Grid view Filter - By Facility"(){
		println "**** TestName: TC9 - By City level Grid view Filter - By Facility ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyFacilityFilterResults(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyWithAllFacilitySelectedAndDeselect(), "\n Filers not applied in search when select all")
	}


	def "TC10 - By City level List view Filter - By StarRating"(){
		println "**** TestName: TC10 - By City level List view Filter - By StarRating ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		clickOnListViewIcon()
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyStarRatingFilterResultsListView(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllRatingsSelectedAndDeselectListView(), "\n Filers not applied in search when select all")
	}


	def "TC11 - By City level List view Filter - By Location"(){
		println "**** TestName: TC11 - By City level List view Filter - By Location - By Location ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		clickOnListViewIcon()
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyLocationFilterResults(citySearchData), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllLocationFiltersSelectedAndDeselect(citySearchData), "\n Filers not applied in search when select all")
	}

	@Ignore
	def "TC12 - By City level List view Filter - By Facility"(){
		println "**** TestName: TC12 - By City level List view Filter - By Facility ****"
		when:"Search by city with 2 adults and 1 child "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		clickOnListViewIcon()
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyFacilityFilterResults(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyWithAllFacilitySelectedAndDeselect(), "\n Filers not applied in search when select all")
	}


	def "TC13 - By City level Grid view Sorting - By StarRating"(){
		println "**** TestName:TC13 - By City level Grid view Sorting - By StarRating ****"
		when:"Search by city with 2 adults "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		def paginationheader = getPaginationCounterTop()
		and: "sort by star rating 5 stars"
		clickOnSortingOrder("5 star")
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order from high to low"
		softAssert.assertTrue(verifyStarRating5to1() , "Star rating not sorted correctly from 5 to 1!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")

		when: "sort by star rating 1 stars"
		clickOnSortingOrder("1 star")
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order low to high"
		softAssert.assertTrue(verifyStarRatingsorted1To5() , "Star rating not sorted correctly from 1 to 5!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")
	}


	def "TC14 - By City level Grid view Sorting - By Price"(){
		println "**** TC14 - By City level Grid view Sorting - By Price****"
		when:"Search by city with 2 adults  "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		at HotelSearchResultsPage
		def paginationheader = getPaginationCounterTop()
		and:"the TA selects to Sort By Price High-to-Low"
		clickOnSortingOrder("Highest price")
		sleep(4000)
		then: "the results are refreshed to show hotels in price order highest first"
		at HotelSearchResultsPage
		softAssert.assertTrue(verifyPriceSortedHighToLow(), "Price not sorted correctly in high to low!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")
		when:"the TA selects to Sort By Price Low-to-High"
		clickOnSortingOrder("Lowest price")
		sleep(1000)
		then: "the results are refreshed to show hotels in price order lowest first"
		at HotelSearchResultsPage
		softAssert.assertTrue(verifyPriceSortedLowToHigh(), "Price not sorted correctly in low to high!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")
	}

	def "TC15 - By City level Grid view Sorting - By alphabetical order"(){
		println "**** TC15 - By City level Grid view Sorting - By alphabetical order****"
		when:"Search by city with 2 adults  "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		at HotelSearchResultsPage
		def paginationheader = getPaginationCounterTop()
		and :"the TA selects to Sort By Z-A"
		clickOnSortingOrder("Z-A")
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from Z to A"
		softAssert.assertTrue(verifyhotelOrderZtoA() , "Hotels not sorted correctly Z to A !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by Z to A")
		when :"the TA selects to Sort By A-Z"
		clickOnSortingOrder("A-Z")
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from A-Z"
		softAssert.assertTrue(verifyHotelOrderAtoZ() , "Hotels not sorted correctly A to Z !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by A to ZA")
	}
	

	def "TC16 - By City level List view Sorting - By StarRating"(){
		println "**** TestName:TC16 - By City level List view Sorting - By StarRating ****"
		when:"Search by city with 2 adults "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		def paginationheader = getPaginationCounterTop()
		and: "click on ListView"
		clickOnListViewIcon()
		and: "sort by star rating 5 stars"
		clickOnSortingOrder("5 star")
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order from high to low"
		softAssert.assertTrue(verifyStarRating5to1ListView() , "Star rating not sorted correctly from 5 to 1!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")

		when: "sort by star rating 1 stars"
		clickOnSortingOrder("1 star")
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order low to high"
		softAssert.assertTrue(verifyStarRatingsorted1To5ListView() , "Star rating not sorted correctly from 1 to 5!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")
	}

	def "TC17 - By City level List view Sorting - By Price"(){
		println "**** TC17 - By City level List view Sorting - By Price****"
		when:"Search by city with 2 adults  "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		at HotelSearchResultsPage
		def paginationheader = getPaginationCounterTop()
		and: "click on ListView"
		clickOnListViewIcon()
		and:"the TA selects to Sort By Price High-to-Low"
		clickOnSortingOrder("Highest price")
		sleep(5000)
		then: "the results are refreshed to show hotels in price order highest first"
		softAssert.assertTrue(verifyPriceSortedHighToLow(), "Price not sorted correctly in high to low!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")

		when:"the TA selects to Sort By Price Low-to-High"
		clickOnSortingOrder("Lowest price")
		sleep(1000)
		then: "the results are refreshed to show hotels in price order lowest first"
		softAssert.assertTrue(verifyPriceSortedLowToHigh(), "Price not sorted correctly in low to high!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")
	}



	def "TC18 - By City level Grid view Sorting - By alphabetical order"(){
		println "**** TC18 - By City level List view Sorting - By alphabetical order****"
		when:"Search by city with 2 adults  "
		CitySearchData citySearchData = new CitySearchData("case1")
		searchByCitySingleRoom2Adults(citySearchData)
		def paginationheader = getPaginationCounterTop()
		and: "click on ListView"
		clickOnListViewIcon()
		and :"the TA selects to Sort By Z-A"
		clickOnSortingOrder("Z-A")
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from Z to A"
		softAssert.assertTrue(verifyhotelOrderZtoA() , "Hotels not sorted correctly Z to A !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by Z to A")
		when :"the TA selects to Sort By A-Z"
		clickOnSortingOrder("A-Z")
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from A-Z"

		softAssert.assertTrue(verifyHotelOrderAtoZ() , "Hotels not sorted correctly A to Z !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by A to ZA")
	}
}
