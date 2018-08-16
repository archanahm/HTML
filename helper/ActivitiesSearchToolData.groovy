package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

/**
 * Created by Madhavi Parimi on 05/05/2016.
 * Modified by ArchanaHM 02/01/2018
 */
class ActivitiesSearchToolData extends BaseData {

	private static final String pathPrefix = "activities.searchTool."

	Map input
	Map output
	Map rooms
	/**new lines added */
	String itemName
	String autoSuggest
	String cityCode
	String checkInDaysToAdd
	String checkOutDaysToAdd
	String guestsRoom1Adults
	String guestsRoom1Child
	String activityDefaultDateType
	List childrenAges
	List activityTabHeader
	String property
	Map expected
	Map filters
	String dataName
	Config localConf = null

	ActivitiesSearchToolData(String path) {
		super("$pathPrefix$path")
	}


@Override
/*	protected void init() {

	input = conf.getAnyRef("input")
	output = conf.getAnyRef("output")
	print output
	print input

}*/
		protected void init() {
			input = conf.getAnyRef("input")
			output = conf.getAnyRef("output")
			itemName = getConfString("input.desc")
			autoSuggest = getConfString("input.propertySuggest")
		//	cityCode = getConfString("input.cityCOde")
			checkInDaysToAdd = getConfString("input.date")

			guestsRoom1Adults = getConfString("input.paxAdults")
			guestsRoom1Child = getConfString("input.paxChild")
			activityTabHeader = getStringList("input.ActivityTabHeader")
			activityDefaultDateType = getConfString("input.defaultDateType")
			//childrenAges= getConfString("childrenAges")
			property = getConfString("property")
		}

	@Override
	public String toString() {
		return "ActivityBookingData{" +
				"itemName='" + itemName + '\'' +
				", autoSuggest='" + autoSuggest + '\'' +
				", checkInDaysToAdd='" + checkInDaysToAdd + '\'' +
				", guestsRoom1Adults='" + guestsRoom1Adults + '\'' +
				", guestsRoom1Child='" + guestsRoom1Child + '\'' +
						'}';
	}



	@Override
	protected Config loadConfig(String path) {
		return localConf = null == localConf ? TestConf.activities.getConfig(path) : localConf


	}
}
