package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import spock.lang.Ignore
import spock.lang.IgnoreRest

import com.kuoni.qa.automation.helper.AreaSearchData
import com.kuoni.qa.automation.helper.PropertySearchData

class SearchByAreaTest extends HotelBookingBaseSpec {

	def "TC1 - Single rooms Single rate plans"(){
		println "**** TestName: TC1 - Single rooms Single rate plans ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Rivamare")
		searchByAreaSingleRoom2A1C(areaSearchData, propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(areaSearchData,propertySearchData), "\n Hotel not found in search results")
	}


	def "TC2 - multiple rooms  multiple rate plans 2 rooms available"(){
		println "**** TestName: TC2 - multiple rooms  multiple rate plans 2 rooms available ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Ca Del Borgo")
		searchByAreaSingleRoom2A1C(areaSearchData,propertySearchData)
		at HotelSearchResultsPage
		String hotelName = getHotelName(0)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(areaSearchData,propertySearchData), "\n Hotel not found in search results")
	}

	def "TC3 - multiple rooms  multiple rate plans item has 3 or more rooms available"(){
		println "**** TestName:TC3 - multiple rooms  multiple rate plans item has 3 or more rooms available ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Ducale")
		searchByAreaSingleRoom2A1C(areaSearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(areaSearchData,propertySearchData), "\n Hotel not found in search results")

	}

	def "TC4 - Single rooms Single rate plans ListView"(){
		println "**** TestName: TC4 - Single rooms Single rate plans ListView ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Rivamare")
		clickOnListViewIcon()
		searchByAreaSingleRoom2A1C(areaSearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(areaSearchData,propertySearchData), "\n Hotel not found in search results")
	}

	def "TC5 - multiple rooms  multiple rate plans 2 rooms available List view"(){
		println "**** TestName: TC5 - multiple rooms  multiple rate plans 2 rooms available List view ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Ca Del Borgo")
		clickOnListViewIcon()
		searchByAreaSingleRoom2A1C(areaSearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(areaSearchData,propertySearchData), "\n Hotel not found in search results")
	}


	def "TC6 - multiple rooms  multiple rate plans item has 3 or more rooms available List view"(){
		println "**** TestName: TC6 - multiple rooms  multiple rate plans item has 3 or more rooms available List view ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Ducale")
		clickOnListViewIcon()
		searchByAreaSingleRoom2A1C(areaSearchData,propertySearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyHotelinSearchResults(areaSearchData,propertySearchData), "\n Hotel not found in search results")
	}


	def "TC7 - By Area level Grid view Filter - By StarRating"(){
		println "**** TestName:TC7 -  By Area level Grid view Filter - By StarRating ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyStarRatingFilterResults(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllRatingsSelectedAndDeselect(), "\n Filers not applied in search when select all")
	}

	def "TC8 - By Area level Grid view Filter - By Location"(){
		println "**** TestName: TC8 - By Area level Grid view Filter - By Location ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyLocationFilterResults(areaSearchData), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllLocationFiltersSelectedAndDeselect(areaSearchData), "\n Filers not applied in search when select all")
	}

	@Ignore
	def "TC9 - By Area level Grid view Filter - By Facility"(){
		println "**** TestName: TC9 - By Area level Grid view Filter - By Facility ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyFacilityFilterResults(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyWithAllFacilitySelectedAndDeselect(), "\n Filers not applied in search when select all")
	}

	def "TC10 - By Area level List view Filter - By StarRating"(){
		println "**** TestName: TC10 - By Area level List view Filter - By StarRating ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		clickOnListViewIcon()
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyStarRatingFilterResultsListView(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllRatingsSelectedAndDeselectListView(), "\n Filers not applied in search when select all")
	}

	def "TC11 - By Area level List view Filter - By Location"(){
		println "**** TestName: TC11 - By Area level List view Filter - By Location - By Location ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		clickOnListViewIcon()
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyLocationFilterResults(areaSearchData), "\n Filers not applied in search")
		softAssert.assertTrue(verifyFiltersWithAllLocationFiltersSelectedAndDeselect(areaSearchData), "\n Filers not applied in search when select all")
	}

	@Ignore
	def "TC12 - By Area level List view Filter - By Facility"(){
		println "**** TestName: TC12 - By Area level List view Filter - By Facility ****"
		when:"Search by city with 2 adults and 1 child "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		clickOnListViewIcon()
		then: "Hotel will be displayed in search results"
		softAssert.assertTrue(verifyFacilityFilterResults(), "\n Filers not applied in search")
		softAssert.assertTrue(verifyWithAllFacilitySelectedAndDeselect(), "\n Filers not applied in search when select all")
	}

	def "TC13 - By Area level Grid view Sorting - By StarRating"(){
		println "**** TestName:TC13 - By Area level Grid view Sorting - By StarRating ****"
		when:"Search by city with 2 adults "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		at HotelSearchResultsPage
		sleep(3000)
		waitUntilPaginationTopAppears()
		println("search results displayed.")
		def paginationheader = getPaginationCounterTop()
		and: "sort by star rating 5 stars"
		clickOnSortingOrder("5 star")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order from high to low"
		softAssert.assertTrue(verifyStarRating5to1() , "Star rating not sorted correctly from 5 to 1!!")
		sleep(1000)
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")
		when: "sort by star rating 1 stars"
		clickOnSortingOrder("1 star")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order low to high"
		softAssert.assertTrue(verifyStarRatingsorted1To5() , "Star rating not sorted correctly from 1 to 5!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")
	}

	def "TC14 - By Area level Grid view Sorting - By Price"(){
		println "**** TC14 - By Area level Grid view Sorting - By Price****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		at HotelSearchResultsPage
		waitUntilPaginationTopAppears()
		sleep(1000)
		def paginationheader = getPaginationCounterTop()
		and:"the TA selects to Sort By Price High-to-Low"
		clickOnSortingOrder("Highest price")
		waitTillLoadingIconDisappears()
		sleep(2000)
		then: "the results are refreshed to show hotels in price order highest first"
		softAssert.assertTrue(verifyPriceSortedHighToLow(), "Price not sorted correctly in high to low!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")
		//Adding Assertions for NV-27307, NV-25462, NV-25552
		when: "User enters hotelname and click on find button"
		enterHotelNameInFilterSection(areaSearchData.input.hotelFilterInput)
		sleep(500)
		clickOnFindButtonInFilters()
		waitTillLoadingIconDisappears()
		sleep(1000)
		then: "No restults alert shows."
		softAssert.assertTrue(getEssentialInformationText().toString().contains(areaSearchData.expected.alertText),"alert message not displayed.")
		clickCloseLightbox()
		sleep(500)
		clearHotelNameInFilterSection()
		softAssert.assertTrue(verifyFindButtonInFilterSectionDisabled(),"Find button is enabled after clearing text in filter search.")
		sleep(1000)
		when: "user clicks on hotel name in PLP page"
		clickOnHotelName(0)
		waitTillLoadingIconDisappears()
		sleep(1000)
		driver.navigate().back()
		sleep(3000)
		then: "user is able to navigate to PDP page and navigates back to PLP page"
		softAssert.assertTrue(getNumberOfHotelsInSearchResults()>0,"Hotel PLP page is not displayed.")
		when: "user clicks on filter lowest price"
		clickOnSortingOrder("Lowest price")
		waitTillLoadingIconDisappears()
		sleep(2000)
		then: "No results alert message should not show"
		softAssert.assertFalse(getEssentialInformationText().toString().contains(areaSearchData.expected.alertText),"alert message displayed.")
		sleep(3000)
		then: "the results are refreshed to show hotels in price order lowest first"
		softAssert.assertTrue(verifyPriceSortedLowToHigh(), "Price not sorted correctly in low to high!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")
	}

	def "TC15 - By Area level Grid view Sorting - By alphabetical order"(){
		println "**** TC15 - By Area level Grid view Sorting - By alphabetical order****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		waitUntilPaginationTopAppears()
		sleep(1000)
		def paginationheader = getPaginationCounterTop()
		and :"the TA selects to Sort By Z-A"
		clickOnSortingOrder("Z-A")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from Z to A"
		softAssert.assertTrue(verifyhotelOrderZtoA() , "Hotels not sorted correctly Z to A !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by Z to A")
		when :"the TA selects to Sort By A-Z"
		clickOnSortingOrder("A-Z")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from A-Z"
		softAssert.assertTrue(verifyHotelOrderAtoZ() , "Hotels not sorted correctly A to Z !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by A to ZA")
	}


	def "TC16 - By Area level List view Sorting - By StarRating"(){
		println "**** TestName:TC16 - By Area level List view Sorting - By StarRating ****"
		when:"Search by city with 2 adults "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		sleep(1000)
		waitUntilPaginationTopAppears()
		def paginationheader = getPaginationCounterTop()
		and: "click on ListView"
		clickOnListViewIcon()
		waitTillLoadingIconDisappears()
		and: "sort by star rating 5 stars"
		clickOnSortingOrder("5 star")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order from high to low"
		softAssert.assertTrue(verifyStarRating5to1ListView() , "Star rating not sorted correctly from 5 to 1!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")

		when: "sort by star rating 1 stars"
		clickOnSortingOrder("1 star")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order low to high"
		softAssert.assertTrue(verifyStarRatingsorted1To5ListView() , "Star rating not sorted correctly from 1 to 5!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")
	}

	def "TC17 - By Area level List view Sorting - By Price"(){
		println "**** TC17 - By Area level List view Sorting - By Price****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		waitUntilPaginationTopAppears()
		sleep(1000)
		def paginationheader = getPaginationCounterTop()
		and: "click on ListView"
		clickOnListViewIcon()
		and:"the TA selects to Sort By Price High-to-Low"
		clickOnSortingOrder("Highest price")
		waitTillLoadingIconDisappears()
		sleep(6000)
		then: "the results are refreshed to show hotels in price order highest first"
		at HotelSearchPage
		softAssert.assertTrue(verifyPriceSortedHighToLow(), "Price not sorted correctly in high to low!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")

		when:"the TA selects to Sort By Price Low-to-High"
		clickOnSortingOrder("Lowest price")
		waitTillLoadingIconDisappears()
		sleep(2000)
		then: "the results are refreshed to show hotels in price order lowest first"
		softAssert.assertTrue(verifyPriceSortedLowToHigh(), "Price not sorted correctly in low to high!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")
	}


	def "TC18 - By Area level List view Sorting - By alphabetical order"(){
		println "**** TC18 - By Area level List view Sorting - By alphabetical order****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		sleep(1000)
		def paginationheader = getPaginationCounterTop()
		and: "click on ListView"
		clickOnListViewIcon()
		and :"the TA selects to Sort By Z-A"
		clickOnSortingOrder("Z-A")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from Z to A"
		softAssert.assertTrue(verifyhotelOrderZtoA() , "Hotels not sorted correctly Z to A !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by Z to A")
		when :"the TA selects to Sort By A-Z"
		clickOnSortingOrder("A-Z")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:" the results are refreshed to show hotels in alphabetical order - from A-Z"
		sleep(1000)
		softAssert.assertTrue(verifyHotelOrderAtoZ() , "Hotels not sorted correctly A to Z !!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by A to ZA")
	}


	def "TC19 - Filter - Star rating, location and Facility"(){
		println "**** TC19 - Filter - Star rating, location and Facility****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		sleep(1000)
		def paginationheader = getPaginationCounterTop()
		then:"Multi filters results not correct"
		softAssert.assertTrue(verifyMultiFilters(areaSearchData), "\n Multi filters not correct")
		when:"Deselect Rating , location and facilituies"
		deselectMultiFilters(areaSearchData)
		sleep(5000)
		then:" result displaying hotels with all facility available"
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after deselecting all filters")
	}


	def "TC20 - By Area level Paginate Grid view"(){
		println "**** TC20 -  By Area level Paginate Grid view ****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		then: "pagination on top should be functional for left and right arrows"
		softAssert.assertTrue(verifyPaginationTopArrows(areaSearchData) , "\n Top Pagination top arrows not functional")
		softAssert.assertTrue(verifyPaginationBottom(areaSearchData) , "\n Bottom Pagination numbers or arrows not functional")
	}

	def "TC21 - By Area level Paginate List view"(){
		println "**** TC21 -  By Area level Paginate List view ****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		and: "click on ListView"
		clickOnListViewIcon()
		then: "pagination on top should be functional for left and right arrows"
		softAssert.assertTrue(verifyPaginationTopArrows(areaSearchData) , "\n Top Pagination top arrows not functional")
		softAssert.assertTrue(verifyPaginationBottom(areaSearchData) , "\n Bottom Pagination numbers or arrows not functional")
	}

	def "TC21 - By Area level Sorting + Paginate + Filters  Grid view"(){
		println "**** TC21 - By Area level Sorting + Paginate + Filters  Grid view ****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		and :"the TA selects to Sort By Z-A"
		sleep(2000)
		at HotelSearchResultsPage
		clickOnSortingOrder("Z-A")
		waitTillLoadingIconDisappears()
		and : "Filter by star rating"
		List ratingList = areaSearchData.filtersAndSorting.rating
		List locFilterList = areaSearchData.filtersAndSorting.location
		for (int i=0; i<ratingList.size(); i++){
			println "Filter: "+ ratingList[i]
			//mouseHoversearchFiltersItem("Stars")
			selectStarRatingFilter(ratingList[i].toString())
			sleep(3000)
		}
		then: "user able to do so, screen refresh, user taken to correct result"
		for (int j=0; j< getNumberOfHotelCardsInSearchResults() ; j++){
			softAssert.assertTrue(ratingList.contains(getRatingForTheHotel(j).toString()), "\nStar Rating is not correct , ACtual:" + getRatingForTheHotel(j)+"Hotel;: $j")
		}
		softAssert.assertTrue(verifyhotelOrderZtoA() , "Hotels not sorted correctly Z to A !!")

		when: "sort by star rating 5 stars"
		def paginationheader = getPaginationCounterTop()
		sleep(2000)
		clickOnSortingOrder("Star rating: 5 star")
		waitTillLoadingIconDisappears()
		sleep(3000)
		then:"the results are refreshed to show hotels in star rating order from high to low"
		softAssert.assertTrue(verifyStarRating5to1() , "Star rating not sorted correctly from 5 to 1!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")

		when: "filter by location"
		for (int i=0; i<locFilterList.size(); i++){
		//	mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			println "Filter: "+ locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
			waitTillLoadingIconDisappears()
		}
		sleep(3000)
		def num=getNumberOfHotelCardsInSearchResults()
		then:"then correct number of hotels will be  returned"
		sleep(500)
		softAssert.assertTrue(areaSearchData.filtersAndSorting.numOfHotels2.equals(num.toString()), "\nNumber of hotels returned not correct when location filters selected, Actual: "+ num)
	}


	def "TC22 -By Area level Sorting + Paginate + filter Grid view"(){
		println "**** TC22 -By Area level Sorting + Paginate + filter Grid view ****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		List ratingList = areaSearchData.filtersAndSorting.rating
		List locFilterList = areaSearchData.filtersAndSorting.location
		and:"Sorting by price from H to L"
		sleep(1000)
		def paginationheader = getPaginationCounterTop()
		sleep(2000)
		clickOnSortingOrder("Highest price")
		waitTillLoadingIconDisappears()
		sleep(4000)
		then: "the results are refreshed to show hotels in price order highest first"
		softAssert.assertTrue(verifyPriceSortedHighToLow(), "Price not sorted correctly in high to low!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")

		when: "filter by location"
		for (int i=0; i<locFilterList.size(); i++){
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			println "Filter: "+ locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
			waitTillLoadingIconDisappears()
		}
		sleep(3000)
		def num=getNumberOfHotelCardsInSearchResults()
		sleep(500)
		paginationheader = getPaginationCounterTop()
		sleep(500)
		then:"then correct number of hotels will be returned"
		softAssert.assertTrue(paginationheader.contains(areaSearchData.filtersAndSorting.numOfHotels1), "\nNumber of hotels returned not correct when location filters selected, Actual: "+ paginationheader +"Expected: "+ areaSearchData.filtersAndSorting.numOfHotels1)

		when:"Filter by 4 and 5 star rating"
		for (int i=0; i<ratingList.size(); i++){
			println "Filter: "+ ratingList[i]
			//mouseHoversearchFiltersItem("Stars")
			selectStarRatingFilter(ratingList[i].toString())
			waitTillLoadingIconDisappears()
			sleep(4000)
		}
		num=getNumberOfHotelCardsInSearchResults()
		then: "user able to do so, screen refresh, user taken to correct result"
		softAssert.assertTrue(areaSearchData.filtersAndSorting.numOfHotels2.equals(num.toString()), "\nNumber of hotels returned not correct when rating filters selected, Actual: "+ num)
		for (int j=0; j< getNumberOfHotelCardsInSearchResults() ; j++){
			softAssert.assertTrue(ratingList.contains(getRatingForTheHotel(j).toString()), "\nStar Rating is not correct , ACtual:" + getRatingForTheHotel(j)+"Hotel;: $j")
		}

		when:"Sorting by 1 star rating"
		paginationheader = getPaginationCounterTop()
		scrollToTopOfThePage()
		clickOnSortingOrder("Star rating: 1 star")
		waitTillLoadingIconDisappears()
		sleep(3000)
		then:"the results are refreshed to show hotels in star rating order low to high"
		softAssert.assertTrue(verifyStarRatingsorted1To5() , "Star rating not sorted correctly from 1 to 5!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")
		
		when:"Filter by facility"
		//mouseHoversearchFiltersItem("Facilities")
		//clicksearchFiltersItem("Facilities")
		println "Filter: "+ areaSearchData.filtersAndSorting.facility
		clickOnFacilityFilter(areaSearchData.filtersAndSorting.facility)
		waitTillLoadingIconDisappears()
		then:"REsults should be refresh correctly"
		softAssert.assertTrue(areaSearchData.filtersAndSorting.numOfHotels3.equals(num.toString()), "\nNumber of hotels returned not correct when facility filters selected, Actual: "+ num)
	}


	def "TC23 - By Area level Sorting + Paginate + Filters  List view"(){
		println "**** TC23 - By Area level Sorting + Paginate + Filters  List view ****"
		when:"Search by city with 2 adults and click on Listview  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		clickOnListViewIcon()
		and :"the TA selects to Sort By Z-A"
		sleep(1000)
		clickOnSortingOrder("Z-A")
		waitTillLoadingIconDisappears()
		and : "Filter by star rating"
		List ratingList = areaSearchData.filtersAndSorting.rating
		List locFilterList = areaSearchData.filtersAndSorting.location
		for (int i=0; i<ratingList.size(); i++){
			println "Filter: "+ ratingList[i]
			//mouseHoversearchFiltersItem("Stars")
			selectStarRatingFilter(ratingList[i].toString())
			waitTillLoadingIconDisappears()
			sleep(4000)
		}
		then: "user able to do so, screen refresh, user taken to correct result"
		for (int j=0; j< getNumberOfHotelCardsInSearchResults() ; j++){
			softAssert.assertTrue(ratingList.contains(getRatingForTheHotelListView(j).toString()), "\nStar Rating is not correct , ACtual:" + getRatingForTheHotelListView(j)+"Hotel;: $j")
		}
		softAssert.assertTrue(verifyhotelOrderZtoA() , "Hotels not sorted correctly Z to A !!")

		when: "sort by star rating 5 stars"
		def paginationheader = getPaginationCounterTop()
		scrollToTopOfThePage()
		clickOnSortingOrder("Star rating: 5 star")
		waitTillLoadingIconDisappears()
		sleep(3000)
		then:"the results are refreshed to show hotels in star rating order from high to low"
		softAssert.assertTrue(verifyStarRating5to1ListView() , "Star rating not sorted correctly from 5 to 1!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")

		when: "filter by location"
		for (int i=0; i<locFilterList.size(); i++){
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			println "Filter: "+ locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
			waitTillLoadingIconDisappears()
		}
		sleep(4000)
		def num=getNumberOfHotelCardsInSearchResults()
		sleep(500)
		then:"then correct number of hotels will be  returned"
		softAssert.assertTrue(areaSearchData.filtersAndSorting.numOfHotels2.equals(num.toString()), "\nNumber of hotels returned not correct when location filters selected, Actual: "+ num)
	}

	def "TC24 -By Area level Sorting + Paginate + filter List view"(){
		println "**** TC24 -By Area level Sorting + Paginate + filter List view ****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)
		clickOnListViewIcon()
		List ratingList = areaSearchData.filtersAndSorting.rating
		List locFilterList = areaSearchData.filtersAndSorting.location
		and:"Sorting by price from H to L"
		def paginationheader = getPaginationCounterTop()
		sleep(1000)
		clickOnSortingOrder("Highest price")
		waitTillLoadingIconDisappears()
		sleep(4000)
		then: "the results are refreshed to show hotels in price order highest first"
		at HotelSearchResultsPage
		softAssert.assertTrue(verifyPriceSortedHighToLow(), "Price not sorted correctly in high to low!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by hightest price")

		when: "filter by location"
		for (int i=0; i<locFilterList.size(); i++){
			//mouseHoversearchFiltersItem("Locations")
			//clicksearchFiltersItem("Locations")
			println "Filter: "+ locFilterList[i]
			clickOnLocationFilter(locFilterList[i])
			waitTillLoadingIconDisappears()
		}
		sleep(2000)
		def num=getNumberOfHotelCardsInSearchResults()
		paginationheader = getPaginationCounterTop()
		then:"then correct number of hotels will be returned"
		softAssert.assertTrue(paginationheader.contains(areaSearchData.filtersAndSorting.numOfHotels1), "\nNumber of hotels returned not correct when location filters selected, Actual: "+ paginationheader +"Expected: "+ areaSearchData.filtersAndSorting.numOfHotels1)

		when:"Filter by 4 and 5 star rating"
		for (int i=0; i<ratingList.size(); i++){
			println "Filter: "+ ratingList[i]
			//mouseHoversearchFiltersItem("Stars")
			selectStarRatingFilter(ratingList[i].toString())
			waitTillLoadingIconDisappears()
			sleep(4000)
		}
		num=getNumberOfHotelCardsInSearchResults()
		sleep(500)
		then: "user able to do so, screen refresh, user taken to correct result"
		softAssert.assertTrue(areaSearchData.filtersAndSorting.numOfHotels2.equals(num.toString()), "\nNumber of hotels returned not correct when rating filters selected, Actual: "+ num)
		for (int j=0; j< getNumberOfHotelCardsInSearchResults() ; j++){
			softAssert.assertTrue(ratingList.contains(getRatingForTheHotelListView(j).toString()), "\nStar Rating is not correct , ACtual:" + getRatingForTheHotelListView(j)+"Hotel;: $j")
		}

		when:"Sorting by 1 star rating"
		paginationheader = getPaginationCounterTop()
		scrollToTopOfThePage()
		clickOnSortingOrder("Star rating: 1 star")
		waitTillLoadingIconDisappears()
		sleep(1000)
		then:"the results are refreshed to show hotels in star rating order low to high"
		softAssert.assertTrue(verifyStarRatingsorted1To5ListView() , "Star rating not sorted correctly from 1 to 5!!")
		softAssert.assertTrue(verifyPaginationCounterTop(paginationheader), "\n Pagination header or number of hotels not same after sorting by rating 5 star")
		
		when:"Filter by facility"
		//mouseHoversearchFiltersItem("Facilities")
		//clicksearchFiltersItem("Facilities")
		println "Filter: "+ areaSearchData.filtersAndSorting.facility
		clickOnFacilityFilter(areaSearchData.filtersAndSorting.facility)
		waitTillLoadingIconDisappears()
		then:"REsults should be refresh correctly"
		softAssert.assertTrue(areaSearchData.filtersAndSorting.numOfHotels3.equals(num.toString()), "\nNumber of hotels returned not correct when facility filters selected, Actual: "+ num)
	}

	def "TC25 By Area level Grid and list view "(){
		println "**** TC25 By Area level Grid and list view ****"
		when:"Search by city with 2 adults  "
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		searchByCitySingleRoom2Adults(areaSearchData)		
		def paginationheader = getPaginationCounterTop()
		and: "Grid to list view"
		clickOnListViewIcon()
		sleep(1000)
		then: "Search and map block will be displayed"
		softAssert.assertTrue(getPaginationCounterTop().equals(paginationheader), "\n Pagination not matches with grid view")
		softAssert.assertTrue(verifysearchResultsAndMapBlocks() , "\n Search results block not displayed in list view ")
		softAssert.assertTrue(verifyMapBlockDisplayed() , "\n Map block not displayed in list view ")
		
		when:"change List to grid view"
		clickOnGridView()
		sleep(2000)
		then: "Search and map block will be displayed"
		softAssert.assertTrue(getPaginationCounterTop().equals(paginationheader), "\n Pagination not matches with grid view")
		softAssert.assertTrue(verifysearchResultsAndMapBlocks() , "\n Search results block not displayed in list view ")
		softAssert.assertTrue(verifyMapBlockDisplayed() , "\n Map block not displayed in list view ")
		
	}

	def "TC26 - Search By city with 2 adults and 1 child"(){
		//Adding additonal steps and assertions NV-26835
		AreaSearchData areaSearchData = new AreaSearchData("case1")
		PropertySearchData propertySearchData = new PropertySearchData("Ca Del Borgo")
		when:"Search by city with 2 adults and 1 child "
		searchByAreaSingleRoom2A1C(areaSearchData,propertySearchData)
		at HotelSearchResultsPage
		String hotelName = getHotelName(0)
		sleep(1000)
		clearHotelName()
		typeHotelDestination(hotelName)
		selectFromAutoSuggest(hotelName)
		sleep(500)
		clickFindButton()
		at ItenaryPageItems
		then: "Error should not displayed in Hotel PDP"
		softAssert.assertFalse(checkBookingErrorDispStatus(),"Error displayed on PDP page.")
	}

}
