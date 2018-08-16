package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

/**
 * Created by mtmaku on 4/13/2017.
 */
class PaymentsTestData extends BaseData {

    Map input
    Map expected
    Config localConf = null

    String hotelName

    PaymentsTestData(String hotelName, String pathPrefix, Config conf) {
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
