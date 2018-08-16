package com.kuoni.qa.automation.test.search.booking.hotel


import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Unroll

import com.kuoni.qa.automation.helper.SearchToolData
class HotelSearchTool extends SearchToolBaseSpec {
	@Unroll

	def "Hotel Search tool for Property or single hotels available #testCase"() {
		SearchToolData searchToolData = new SearchToolData(testCase)
		when: "Search for hotel"
		then: "Number of rooms and status should be returned correct in hotel information page"
		searchForSingleRoom(searchToolData)

		where:
		testCase << ["TS01", "TS02", "TS03", "TS04", "TS05", "TS06", "TS07", "TS08", "TS09", "TS10", "TS11", "TS12", "TS13", "TS14", "TS15", "TS16", "TS17", "TS18", "TS19", "TS20", "TS21", "TS22", "TS23", "TS24", "TS25", "TS26", "TS27", "TS28", "TS29", "TS30", "TS31", "TS32", "TS33", "TS34", "TS45", "TS46", "TS47", "TS55"]
	}

	@Unroll
	@Ignore
	def "Hotel Search tool for verify AutoSuggest #testCase"() {
		SearchToolData searchToolData = new SearchToolData(testCase)
		when: "Search for hotel"
		then: "Number of rooms and status should be returned correct in hotel information page"
		verifyAutoSuggest(searchToolData)

		where:
		testCase << ["TS35", "TS36"]
	}

	@Unroll

	def "Hotel Search tool for verify city and area search #testCase"() {
		SearchToolData searchToolData = new SearchToolData(testCase)
		when: "Search for hotel"
		then: "Number of rooms and status should be returned correct in hotel information page"
		verifyCityArea(searchToolData)

		where:
		testCase << ["TS36_1", "TS37", "TS38", "TS39", "TS40", "TS41", "TS42", "TS43", "TS44", "TS48", "TS49", "TS50", "TS50_1", "TS51", "TS52", "TS53", "TS54"]
	}
	/*@IgnoreRest
	def "Hotel Search tool for Property or single hotels available #testCase2"() {
		SearchToolData searchToolData = new SearchToolData(testCase)
		when: "Search for hotel"
		then: "Number of rooms and status should be returned correct in hotel information page"
		searchForSingleRoom(searchToolData)

		where:
		testCase << ["TS55"]
	}*/
	/*	@IgnoreRest
	def "Hotel Search tool for verify city and area search #testCase1"() {
		SearchToolData searchToolData = new SearchToolData(testCase)
		when: "Search for hotel"
		then: "Number of rooms and status should be returned correct in hotel information page"
		verifyCityArea(searchToolData)

		where:
		testCase << ["TS44"]
	}*/
}