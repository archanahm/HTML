package com.kuoni.qa.automation.test.pricing.incluExclu

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class IncExc706Test extends HotelSearchBaseTest {

    static def desc = "InclusionsExclusions"
    static
	
    def dataNames = ["inclusionsExclusions.incExc03", "inclusionsExclusions.incExc05", "inclusionsExclusions.incExc09", 
		"inclusionsExclusions.incExc10", "inclusionsExclusions.incExc11", "inclusionsExclusions.incExc13", 
		"inclusionsExclusions.incExc15", "inclusionsExclusions.incExc16", "inclusionsExclusions.incExc17" ]

    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client706")
    }

}
