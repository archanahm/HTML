package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

/**
 * Created by Joseph Sebastian on 12/10/2015.
 */

class ClientData extends BaseData {

    private static final String clientSettingsPath = "clientDetails."
    private static final Map commonDataMap = TestConf.getConfMap("common", "appURL")
   private String env = System.getProperty("env", "sit")

    String name
    int clientId
    int siteId
    String usernameOrEmail
    String pass
    String nationality
    String nationalityCode
    String currency
    String agentname
    String countryCode
    String brand


    ClientData(String confPath) {
        super("$clientSettingsPath$confPath")
        name = confPath
    }

    @Override
    protected Config loadConfig(String path) {
        return TestConf.clientData.getConfig(path)
    }

    @Override
     void init() {
        clientId = conf.getInt("clientId")
        siteId = conf.getInt("siteId")
        usernameOrEmail = getConfString("usernameOrEmail")
        pass = getConfString("pass")
        nationality = getConfString("nationality")
        nationalityCode = getConfString("nationalityCode")
        currency = getConfString("currency")
        agentname = getConfString("agentname")
        countryCode = getConfString("countryCode")
        brand = getConfString("brand")
    }


    @Override
    String toString() {
        return "ClientData{" +
                "  clientId=" + clientId +
                ", siteId=" + siteId +
                ", usernameOrEmail='" + usernameOrEmail + '\'' +
                ", pass='" + pass + '\'' +
                ", nationality='" + nationality + '\'' +
                ", nationalityCode='" + nationalityCode + '\'' +
                ", currency='" + currency + '\'' +
                ", agentname='" + agentname + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }

    /*String getURL() {
        if(env.equalsIgnoreCase("stage"))
            return commonDataMap
        else {
            String url = commonDataMap."${brand}"
            return "$url/${countryCode}/login"
        }

    }*/
    String getURL() {
        if(env.equalsIgnoreCase("stage"))
            return commonDataMap."${brand}"
           // return "http://longwlg05i-tc.test.gta-travel.com/uk/login/"
        else {
            String url = commonDataMap."${brand}"
            return "$url/${countryCode}/login"
          //  return "http://pilot.test.gta-travel.com/wbs/login"
        }

    }
}
