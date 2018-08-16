package com.kuoni.qa.automation.test.pricing.currency

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class CurrencyConv704NOKTest extends HotelSearchBaseTest {

    static def desc = "Currency Conversion"
    static def dataNames =  ["currency.case7"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client704NOK")
    }

}
