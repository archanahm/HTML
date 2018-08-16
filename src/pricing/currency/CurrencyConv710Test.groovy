package com.kuoni.qa.automation.test.pricing.currency

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class CurrencyConv710Test extends HotelSearchBaseTest {

    static def desc = "Currency Conversion"
    static def dataNames =  ["currency.case2","currency.case3"]
//        static def dataNames =  ["currency.case3"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client710")
    }

}
