package com.kuoni.qa.automation.test.pricing.incluExclu

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class IncExc704Test extends HotelSearchBaseTest {

    static def desc = "InclusionsExclusions"
    static def dataNames = ["inclusionsExclusions.incExc01", "inclusionsExclusions.incExc02", "inclusionsExclusions.incExc06",
		"inclusionsExclusions.incExc12", "inclusionsExclusions.incExc14" ]
//    static def dataNames = ["inclusionsExclusions.incExc01"]

            static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client704")
    }

}
