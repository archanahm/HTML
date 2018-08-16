package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

/**
 * Created by Joseph Sebastian on 07/12/2015.
 */
class HotelBookingData extends BaseData {

    private static final String pathPrefix = "booking."

    String city
    String autoSuggest
    String cityCode
    Map input
    Map expected
    Config localConf = null

    String dataName


    HotelBookingData(String path) {
        super("$pathPrefix$path")
    }

    @Override
    protected void init() {
        city = getConfString("cityTypeText")
        autoSuggest = getConfString("autoSuggest")
        cityCode = getConfString("cityCode")
        input = conf.getAnyRef("input")
        expected = conf.getAnyRef("expected")
    }


    @Override
    public String toString() {
        return "HotelBookingData{" +
                "city='" + city + '\'' +
                ", autoSuggest='" + autoSuggest + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }

    HotelBookingData(String dataName,String pathPrefix,Config conf) {
        super("$pathPrefix$dataName", conf.getConfig("$pathPrefix.$dataName"))
        this.dataName = dataName
        this.localConf = conf.getConfig("$pathPrefix.$dataName")
    }



    @Override
    protected Config loadConfig(String path) {
        return localConf = null == localConf ? TestConf.booking.getConfig(path) : localConf
    }
}
