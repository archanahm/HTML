package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

import java.util.List;
import java.util.Map;

/**
 * Created by Joseph Sebastian on 07/12/2015.
 */
class CitySearchData extends BaseData {

    private static final String pathPrefix = "booking.citySearch."

    String city
    String autoSuggest
    String cityCode
	String checkInDaysToAdd
	String checkOutDaysToAdd
	String guestsRoom1Adults
	List childrenAges	
	String property
	Map input
	Map expected
	Map filters
	String dataName
	Config localConf = null
	
    CitySearchData(String path) {
        super("$pathPrefix$path")
    }

    @Override
    protected void init() {
		input = conf.getAnyRef("input")
		expected = conf.getAnyRef("expected")
		filters = conf.getAnyRef("filters")
        city = getConfString("input.cityTypeText")
        autoSuggest = getConfString("input.cityText")
        cityCode = getConfString("input.cityCOde")
		checkInDaysToAdd = getConfString("input.checkInDaysToAdd")
		checkOutDaysToAdd = getConfString("input.checkOutDaysToAdd")
		guestsRoom1Adults = getConfString("input.guestsRoom1Adults")
		//childrenAges= getConfString("childrenAges")
		property = getConfString("property")
    }


    @Override
    public String toString() {
        return "HotelBookingData{" +
                "city='" + city + '\'' +
                ", autoSuggest='" + autoSuggest + '\'' +
                ", cityCode='" + cityCode + '\'' +
				", checkInDaysToAdd='" + checkInDaysToAdd + '\'' +
				", checkOutDaysToAdd='" + checkOutDaysToAdd + '\'' +
				", guestsRoom1Adults='" + guestsRoom1Adults + '\'' +			
                '}';
    }
	CitySearchData(String dataName,String pathPrefix,Config conf) {
		super("$pathPrefix$dataName", conf.getConfig("$pathPrefix.$dataName"))
		this.dataName = dataName
		this.localConf = conf.getConfig("$pathPrefix.$dataName")
	}



	@Override
	protected Config loadConfig(String path) {
		return localConf = null == localConf ? TestConf.booking.getConfig(path) : localConf
	}
}
