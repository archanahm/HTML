package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

/**
 * Created by kmahas on 7/6/2017.
 */
class TransfersTestData extends BaseData {

    Map input
    Map expected
    Config localConf = null

    String transferName

    TransfersTestData(String transferName, String pathPrefix, Config conf) {
        super("$pathPrefix$transferName", conf.getConfig("$pathPrefix.$transferName"))
        this.transferName = transferName
        this.localConf = conf.getConfig("$pathPrefix.$transferName")
    }

    @Override
    protected void init() {
        input = conf.getAnyRef("input")
        expected = conf.getAnyRef("expected")
    }

    @Override
    protected Config loadConfig(String path) {
        return localConf = null == localConf ? TestConf.booking.getConfig(path) : localConf
    }
}

