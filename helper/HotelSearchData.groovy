package com.kuoni.qa.automation.helper

import java.util.Map;

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

class HotelSearchData extends BaseData {

    Map input
    Map expected
    Config localConf = null

    private static final String pathPrefix = "itinerary."
    String hotelName

    HotelSearchData(String hotelName) {
        super("$pathPrefix$hotelName")
        this.hotelName = hotelName
    }

    HotelSearchData(String hotelName, String pathPrefix, Config conf) {
        super("$pathPrefix$hotelName", conf.getConfig("$pathPrefix.$hotelName"))
        this.hotelName = hotelName
        this.localConf = conf.getConfig("$pathPrefix.$hotelName")
    }

    @Override
    protected void init() {
        input = conf.getAnyRef("input")
        expected = conf.getAnyRef("expected")
    }

    @Override
    protected Config loadConfig(String path) {
        return localConf = null == localConf ? TestConf.itinerary.getConfig(path) : localConf
    }

}
