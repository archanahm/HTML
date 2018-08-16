package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

class StagePropertySearchData extends BaseData {

    private static final String pathPrefix = "hotelInfo.hotel."

    Map input
	Map reports
    Map pleaseNoteReport
    Map facilities
    Map room
	Map images
	Map poi
	Map essensialInfo
	Map tripAdvisor

    StagePropertySearchData(String propertyName) {
        super("$pathPrefix$propertyName")
    }

    @Override
    protected Config loadConfig(String path) {
        return TestConf.propertyData.getConfig(path)
    }

    @Override
    protected void init() {
		input = conf.getAnyRef("input")
        reports = conf.getAnyRef("reports")
        pleaseNoteReport = conf.getAnyRef("pleaseNoteReport")
        facilities = conf.getAnyRef("facilities")
        room = conf.getAnyRef("room")	
		images = conf.getAnyRef("images")
		poi = conf.getAnyRef("poi")
		essensialInfo = conf.getAnyRef("essensialInfo")
		tripAdvisor = conf.getAnyRef("tripAdvisor")
    }

    /**
     * Example to show how to fetch data from object
     * @param a
     */
     public static void main(String[] args) {
        StagePropertySearchData data = new StagePropertySearchData("Beverley City")
        println(data.facilities.facList)
        println(data.pleaseNoteReport.typeOfProperty)
    }

}

