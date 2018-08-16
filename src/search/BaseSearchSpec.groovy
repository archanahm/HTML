package com.kuoni.qa.automation.test.search

import com.kuoni.qa.automation.helper.HotelData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.test.BaseSpec

abstract class BaseSearchSpec extends BaseSpec {

	//@Shared
	//protected static ClientData activeClientData
	
	//abstract void initValues()


	protected fillSearchFormAndSubmit(HotelData data) {
		String cityTypeText = data.cityTypeText
		String cityText = data.cityText
		int checkIn = data.checkInDaysToAdd
		int checkOut = data.checkOutDaysToAdd
		String guestsRoom1Adults = data.guestsRoom1Adults
		List<Integer> childrenAges = data.childrenAges

		at HotelSearchPage
		typeHotelDestination(cityTypeText)
		sleep(3000)
		selectFromAutoSuggest(cityText)
		selectcheckInCheckOutDateCalender(checkIn, checkOut)
		clickPaxRoom()
		selectNumberOfAdults(guestsRoom1Adults, 0)

		if (childrenAges.size() > 0) {
			clickPaxRoom()
			selectNumberOfChildren(childrenAges.size().toString(), 1)
			childrenAges.eachWithIndex { int age, int i ->
				enterChildrenAge(age.toString(), "1", i)
			}
			clickPaxRoom()
			sleep(3000)
		}
		sleep(1000)
		clickFindButton()
	}
}
