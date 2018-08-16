package com.kuoni.qa.automation.test.pricing.exclusions

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Exclusions710Test extends HotelSearchBaseTest {

    static def desc = "Exclusions"
    static def dataNames = ["exclusions.case5", "exclusions.case11"]

//    static def dataNames =  ["exclusions.case3"]
//    static def dataNames =  ["exclusions.case1", "exclusions.case2"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client710")
		
    }

}
