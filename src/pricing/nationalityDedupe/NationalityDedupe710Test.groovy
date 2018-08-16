package com.kuoni.qa.automation.test.pricing.nationalityDedupe

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class NationalityDedupe710Test extends HotelSearchBaseTest {

    static def desc = "NationalityDedupe"
    static def dataNames = ["nationalityDedupe.NatDedup02"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client710")
    }

}
