package com.kuoni.qa.automation.helper

import java.util.List;
import java.util.Map;

/**
 * Created by Joseph Sebastian on 07/12/2015.
 */
class AreaSearchData extends BaseData {

    private static final String pathPrefix = "booking.areaSearch."


	String property
	Map input
	Map expected
	Map filters
	Map multiFilters
	Map filtersAndSorting

    AreaSearchData(String path) {
        super("$pathPrefix$path")
		/* @Override
         public String toString() {
             return "HotelBookingData{" +
                     "city='" + city + '\'' +
                     ", autoSuggest='" + autoSuggest + '\'' +
                     ", cityCode='" + cityCode + '\'' +
                     ", checkInDaysToAdd='" + checkInDaysToAdd + '\'' +
                     ", checkOutDaysToAdd='" + checkOutDaysToAdd + '\'' +
                     ", guestsRoom1Adults='" + guestsRoom1Adults + '\'' +
                     '}';
         }*/
    }

    @Override
    protected void init() {
		input = conf.getAnyRef("input")	
		filters=conf.getAnyRef("filters")	
		multiFilters=conf.getAnyRef("multiFilters")
		expected=conf.getAnyRef("expected")
		filtersAndSorting=conf.getAnyRef("filtersAndSorting")
		//property = getConfString("property")
    }


}
