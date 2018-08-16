package com.kuoni.qa.automation.test.search.city


import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.test.search.BaseSearchSpec

class SearchByCityBaseTest extends BaseSearchSpec {
	ClientData clientData = new ClientData("client2771")
	
	def "01 : Hotel Search by City "() {
		given: "User is able to login"
			HotelData searchData = new HotelData("citySearch.case1")
			login(clientData)
						
			at HotelSearchPage
			fillSearchFormAndSubmit(searchData)
			
			
		when: ""
		then: ""
	}



}
