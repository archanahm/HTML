package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

import static com.kuoni.qa.automation.util.TestConf.itinerarySearch

/**
 * Created by kmahas on 11/14/2017.
 */
class ItinerarySearchTestData extends BaseData {

    Map input
    Map expected
    Map scenarioMap
    Config localConf = null
    private static final String pathPrefixOne = "itinerarySearch"
    private static final  Config confOne = itinerarySearch
    String scenarioNum
    String testCaseName

    ItinerarySearchTestData(String scenarioNum, String pathPrefix, Config conf) {
       super("$pathPrefix$scenarioNum", conf.getConfig("$pathPrefix.$scenarioNum"))
        this.scenarioNum = scenarioNum
        this.localConf = conf.getConfig("$pathPrefix.$scenarioNum")
    }

    ItinerarySearchTestData(String scenarioNum, String testCaseName) {
        super("$pathPrefixOne$scenarioNum$testCaseName", confOne.getConfig("$pathPrefixOne.$scenarioNum.$testCaseName"))
        this.scenarioNum = scenarioNum
        this.testCaseName = testCaseName
        this.localConf = confOne.getConfig("$pathPrefixOne.$scenarioNum.$testCaseName")
        scenarioMap = confOne.getAnyRef("$pathPrefixOne.$scenarioNum")
    }

    @Override
    protected void init() {
        input = conf.getAnyRef("input")
        expected = conf.getAnyRef("expected")
    }

    public Integer getScenarioTestCaseCount(){
        return scenarioMap.size();
    }

    @Override
    protected Config loadConfig(String path) {
        return localConf = null == localConf ? TestConf.itinerary.getConfig(path) : localConf
    }
}
