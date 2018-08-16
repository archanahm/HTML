package com.kuoni.qa.automation.test.search.booking.activity

import com.kuoni.qa.automation.helper.ClientData
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Unroll

import com.kuoni.qa.automation.helper.ActivitiesSearchToolData
import com.kuoni.qa.automation.page.activity.ActivitySearchToolPage
import com.kuoni.qa.automation.util.DateUtil

class ActivitySearchToolTest extends ActivityBaseSpec {
	DateUtil dateUtil = new DateUtil()
	@Shared
	ClientData client = new ClientData("client664")

	def setup() {
		login(client)
	}

	@Unroll

//working fine in R12 D2
	def "Activities SearchTool AutoSuggest  #testCase" (){
		ActivitiesSearchToolData activitiesSearchToolData = new ActivitiesSearchToolData(testCase)
		when :"Click on Activities tab "
		and: "Enter text in activity field"
		then:"Auto suggest will be displayed for location and activities"
		searchToolFindActivity(activitiesSearchToolData)

		where:
		testCase << ["TS01", "TS02","TS03"]
		//Updated the test data in R12 D2
	}
//working fine in R12 D2
	@Ignore
	@Unroll

	def "Activities SearchTool Individual date type " (){
		when :"Click on Activities tab "

		at ActivitySearchToolPage
		clickActivitySearchTab()
		searchToolDateType(30)
		String datestr = dateUtil.addDays(30)
		softAssert.assertTrue(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct, Expected: "+ datestr)
		searchToolDateType(31)
		datestr = dateUtil.addDays(31)
		softAssert.assertTrue(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct, Expected: "+ datestr)
		searchToolDateType(32)
		datestr = dateUtil.addDays(32)
		softAssert.assertTrue(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct, Expected: "+ datestr)
		and: "Should not be able to sleect 4th date , selection is ristricted for 3 dates"
		searchToolDateType(33)
		datestr = dateUtil.addDays(33)
		softAssert.assertFalse(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct")
		and : "Perform deselect dates"
		searchToolDateType(30)
		datestr = dateUtil.addDays(30)
		softAssert.assertFalse(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct, Expected: "+ datestr)
		searchToolDateType(31)
		datestr = dateUtil.addDays(31)
		softAssert.assertFalse(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct")
		searchToolDateType(32)
		datestr = dateUtil.addDays(32)
		softAssert.assertFalse(getIndividualDate().contains(datestr), "\n Selected individual datestr is not correct")
		then:"Error Should be displayed if individual date not Selected"
		softAssert.assertTrue(getDatesErrorMessage().contains("required"),"\n Activity date error not displayed")
	}


	//Not working
	/*def "Activities SearchTool Date Range Type" (){
		when :"Click on Activities tab "
		at ActivitySearchToolPage
		clickActivitySearchTab()
		then:"should be able to select date range type"
		verifyDateRange()
	}*/
//working fine in R12 D2

	@Unroll

	def "Activities SearchTool Pax(Participants)" (){
		when :"Click on Activities tab "
		at ActivitySearchToolPage
		clickActivitySearchTab()
		then:"should be able to select participants input"		
		verifyPaxSearchTool()		
	}


	//working fine in R12 D2

	@Unroll
	def "Activities SearchTool Find Function" (){		
		when :"Click on Activities tab "	
		at ActivitySearchToolPage
		clickActivitySearchTab()
		then:"Find function should be disabled"
		verifyFindFunction()	
	}
	//working fine in R12 D2
	@Unroll
	def "Activities SearchTool Find Function with Search Results" (){
		when :"Click on Activities tab "
		ActivitiesSearchToolData activitiesSearchToolData = new ActivitiesSearchToolData(testCase)
		at ActivitySearchToolPage
		clickActivitySearchTab()
		then:"Find function should be disabled"
		verifyFindFunctionSearchResults(activitiesSearchToolData)
		
		where:
		testCase <<  ["TS08","TS09","TS10","TS11","TS06", "TS07"]
	}
}