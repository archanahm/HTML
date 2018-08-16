package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

/**
 * Created by Madhavi Parimi on 26/02/2015.
 */
class InventorySearchData extends BaseData {

	private static final String pathPrefix = "inventory.search."


	String property
	String propertySuggest
	String checkInDate
	String checkOutDate
	Map input
	Map output
	Map rooms

	InventorySearchData(String path) {
		super("$pathPrefix$path")
	}

	@Override
	protected void init() {
		input = conf.getAnyRef("input")
		output = conf.getAnyRef("output")
		//rooms= conf.getAnyRef("rooms")
		/*property = conf.getAnyRef("property")
		propertySuggest = conf.getAnyRef("propertySuggest")
		checkInDate = conf.getAnyRef("checkInDate")
		checkOutDate = conf.getAnyRef("checkOutDate")*/
	}
	
	@Override
	protected Config loadConfig(String path) {
	 return TestConf.inventory.getConfig(path)
	}
}
