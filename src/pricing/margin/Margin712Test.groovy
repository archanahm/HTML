package com.kuoni.qa.automation.test.pricing.margin

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Margin712Test extends HotelSearchBaseTest {

    static def desc = "Margin"
    static def dataNames = ["margin.case1", "margin.case2" ,"margin.case3"]
//    static def dataNames =  ["margin.case1"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
