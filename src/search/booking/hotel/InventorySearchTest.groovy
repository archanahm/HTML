package com.kuoni.qa.automation.test.search.booking.hotel

import org.junit.Ignore
import spock.lang.IgnoreRest
import spock.lang.Unroll

import com.kuoni.qa.automation.helper.InventorySearchData

class InventorySearchTest extends InventoryBaseSpec  {


	@Unroll

	def "Static Category Inventory  #testCase" (){	
		InventorySearchData invSearchData = new InventorySearchData(testCase)
		//def desc=invSearchData.input.desc
		when :"Search for hotel"		
		then:"Number of rooms and status should be returned correct in hotel information page"
		verifyInventory(invSearchData)	
		
		where:
		testCase <<  ["TS01","TS02","TS03","TS04","TS05","TS10", "TS11","TS12", "TS14","TS15","TS16","TS17","TS18","TS20","TS21","TS24_1","TS27","TS29_1","TS32_3Nights_1","TS34_1"]
	}

	@Unroll

	def "Static Category Inventory for MultiRoom  #testCase" (){
		InventorySearchData invSearchData = new InventorySearchData(testCase)
		//def desc=invSearchData.input.desc
		when :"Search for hotel"
		then:"Number of rooms and status should be returned correct in hotel information page"
		verifyInventory_MultiRoom(invSearchData)

		where:
		testCase <<  ["TS03_02","TS02_02","TS01_02","TS10_02","TS11_02","TS12_02","TS13_02","TS14_02","TS16_03","TS16_07","TS17_03","TS18_02","TS18_05","TS19_02","TS19_7Nights","TS21_05","TS27_03","TS27_09","TS27_02","TS28_4Nights","TS32_02"]

	}

	@Unroll
	def "Static Category Inventory for 0 result returned  #testCase" (){
		InventorySearchData invSearchData = new InventorySearchData(testCase)
		//def desc=invSearchData.input.desc
		when :"Search for hotel"
		then:"Number of rooms and status should be returned correct in hotel information page"
		verifyInventoryZeroResult(invSearchData)
		
		where:
		testCase <<  ["TS06","TS06_02","TS07","TS07_02","TS08","TS09","TS12_05","TS14_04","TS15_03","TS17_05","TS19_09","TS19_4Nights","TS20_02","TS20_09","TS21_08","TS23","TS23_03","TS23_05","TS23_7Nights","TS24","TS24_09","TS25","TS26_2Nights","TS27_03_01","TS28_02","TS28_05","TS29","TS29_2Nights","TS32_4Nights_1","TS32_6Nights_1","TS33","TS34","TS34_4Nights_1",]
		
	}
	
}
