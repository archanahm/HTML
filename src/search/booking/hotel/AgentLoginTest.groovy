package com.kuoni.qa.automation.test.search.booking.hotel

import com.kuoni.qa.automation.helper.CitySearchData
import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.page.ForGotPasswordPage
import com.kuoni.qa.automation.page.HomePage
import com.kuoni.qa.automation.page.LoginPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.TestConf
import spock.lang.IgnoreRest
import spock.lang.Shared

class AgentLoginTest extends BaseSpec {

    @Shared
    CitySearchData hotelData = new CitySearchData("client", "home", TestConf.home)

    @Shared
    //Client Login
    ClientData clientData = new ClientData("client664")
    @IgnoreRest
    def "TC00 Access Agent login page"() {
        given: "User is Agent login page."
        def loginURL = clientData.getURL()
        LoginPage.url = loginURL
        driver.get(loginURL)

    }
    def "TC01 verify login page"() {
        given: "user is on login page"
        at HomePage
        softAssert.assertTrue(verifyLogoHeader(), "logo not displayed.")
        at HomePage
        clickSiteHeaderImg()
        softAssert.assertTrue(verifyLogoHeader(), "logo not displayed.")
    }
    def "TC02 verify login form in Agent login page"(){
        given: "user is on login page"
        at HomePage
        assertionEquals(getAgentIdTxtFieldPalceHolder(),hotelData.expected.agentId,"Agent id not displayed.")
        assertionEquals(getAgentEmailTxtFieldPalceHolder(),hotelData.expected.email,"Email addr not displayed")
        assertionEquals(getPasswordTxtFieldPlaceHolder(),hotelData.expected.password,"password field not displayed")
        softAssert.assertTrue(getRememberMeCheckboxStatus(),"Remember me check box not displayed.")
        assertionEquals(getRemberMeCheckboxTxt().toString().trim(),hotelData.expected.rememberMeTxt.toString(),"Remember me checkbox text not displayed.")
        softAssert.assertTrue(getLoginButtonStatus(),"Login button not displayed.")
        softAssert.assertTrue(getRegisterLNKStatus(),"Register button not displayed.")
        assertionEquals(getForgotPasswordTxt(),hotelData.expected.pswdLinkTxt,"Forgot password text not displayed")
        softAssert.assertTrue(getPasswordLnkStatus(),"password link not displayed.")

    }
    def "TC03: verfiy right side in login page"(){
        given: "user is on login page"
        at HomePage
        softAssert.assertTrue( getGTALogoStatus(),"Right side logo not displayed.")
    }

    def "TC04: verifyFooter in login page"() {
        given: "user is on login page"
        at HomePage
        softAssert.assertTrue(verifyLoggedinFooterLinksDisplayed(hotelData.input.termsAndConditions), "Site terms link not displayed.")
        clickOnFooterLinks(hotelData.input.termsAndConditions)
        driver.navigate().back()
    }

        def "TC05: verifyFooter link bookingCodition"(){
            given: "user is on login page"
            at HomePage
            softAssert.assertTrue(verifyLoggedinFooterLinksDisplayed(hotelData.input.bookingConditionLNKTxt),"Site terms link not displayed.")
            clickOnFooterLinks(hotelData.input.bookingConditionLNKTxt)
            driver.navigate().back()
        }

       def "TC06: verify Footer Link agent Terms"(){
           given: "user is on login page"
           at HomePage
           softAssert.assertTrue(verifyLoggedinFooterLinksDisplayed(hotelData.input.agentTermsLNKTxt),"Site terms link not displayed.")
           clickOnFooterLinks(hotelData.input.agentTermsLNKTxt)
           driver.navigate().back()

       }

        def "TC07: verify footer link company profile"(){
            given: "user is on login page"
            at HomePage
            softAssert.assertTrue(verifyLoggedinFooterLinksDisplayed(hotelData.input.companyProfileTxt),"Site terms link not displayed.")
            clickOnFooterLinks(hotelData.input.companyProfileTxt)
            driver.navigate().back()

        }
    def "TC08: verify Footer Logos"(){
        given: "user is on login page"
        at HomePage
        softAssert.assertTrue(verifyOtherAgentIATAdisplayed(),"IATA logo not displayed")
        softAssert.assertTrue(verifyOtherAgentABTAdisplayed(),"ABTA logo not displayed")
        softAssert.assertNotNull(getCopyRightText(),"copyRight link not displayed")
    }

    def "TC09: verifyAgent id in login page"()
    {
        given: "user is on Agent id in login page"
        at LoginPage
        clickOnAgencyId()
        at HomePage
        clickOutside()
        at LoginPage
        sleep(2000)
        softAssert.assertTrue(verifyAgencyError(hotelData.expected.emptyAgencyErr),"Agent id error empty error msg not displayed")
        enterAgencyId(hotelData.input.agentIdInvalid)
        clickOutside()
        sleep(2000)
        softAssert.assertTrue(verifyAgencyError(hotelData.expected.agentError),"agent id error not displayed")
        enterAgencyId(hotelData.input.agentIdValid)
        clickOutside()
        softAssert.assertTrue(verifyTickmark(0, hotelData.expected.tickMark),"tick mark not displayed")
        //Tick mark validation
    }

    def "TC10: verify email text field in login page"(){
        given: "user is on Agent id in login page"
        at LoginPage
        clickOnUserName()
        at HomePage
        clickOutside()
        at LoginPage
        sleep(2000)
        softAssert.assertTrue(verifyUsernameMissingError(hotelData.expected.emptyEmailErr),"Error msg not displayed when user clicked outside")
        enterUsername(hotelData.input.email1)
        clickOutside()
        sleep(2000)
        softAssert.assertTrue(verifyTickmark(1, hotelData.expected.tickMark),"tick mark not displayed")
        //Tick mark validation
        enterUsername(hotelData.input.email2)
        clickOutside()
        sleep(2000)
        softAssert.assertTrue(verifyTickmark(1, hotelData.expected.tickMark),"tick mark not displayed")
        //Tick mark
    }

    def "TC11: verify password text field and loging error in login page"(){
        given: "user is on Agent id in login page"
        at LoginPage
        enterPassword(hotelData.input.pswd)
        at HomePage
        clickOutside()
        at LoginPage
        sleep(2000)
        softAssert.assertTrue(verifyTickmark(2, hotelData.expected.tickMark),"tick mark not displayed")
        //Tick Mark Validation
        clickOnLoginButton()
        waitTillLoadingIconDisappears()
        try {
            softAssert.assertTrue(verifyInvalidErrorDisplayed(hotelData.expected.loginErr), "Invalid login error msg not displayed.")
        }catch(Exception e){}
        login(hotelData.input.agentIdValid, hotelData.input.email2, hotelData.input.pswd)
           // clickOnLoginButton()
            waitTillLoadingIconDisappears()
        try {
            softAssert.assertTrue(verifyInvalidErrorDisplayed(hotelData.expected.loginErr2), "Invalid login error msg not displayed.")
        }
        catch(Exception e){}
        login(hotelData.input.agentIdValid,hotelData.input.email2,hotelData.input.pwdValid)
      //  clickOnLoginButton()
        waitTillLoadingIconDisappears()
        at HotelSearchResultsPage
        softAssert.assertTrue(verifySearchToolDispalyedforHotel(),"hotel search not displayed")

    }

    def "TC12: verify login error page"(){
        given: "user is on login page"
        at HomePage
        viewProfile()
       // softAssert.assertTrue()
        String proifleTxt = getUserProfileTxt(1)
        softAssert.assertTrue(proifleTxt.toString().contains(hotelData.expected.profileTxt),"Hi 664 agent msg not displayed")
        softAssert.assertTrue(proifleTxt.toString().contains(hotelData.expected.logoutLNK),"Hi 664 agent msg not displayed")
        logoutProfile()
        waitTillLoadingIconDisappears()
        softAssert.assertTrue( getGTALogoStatus(),"Right side logo not displayed.")
        softAssert.assertFalse(getRembemberCheckBoxStatus(),"Remember me checkbox is checked")

    }

    def "TC13: verify forgot password page"(){
        given: "user is on forgot pwd page"
        at LoginPage
        clickForgotPasswordLink()
        at ForGotPasswordPage
        softAssert.assertEquals(getResetPwdTxt(),hotelData.expected.resetPwd,"reset password txt not displayed")
        softAssert.assertTrue(getAgentIDStatus(),"Agent Id txt field not displayed")
        softAssert.assertTrue(getEmailStatus(),"email txt field not displayed")
        softAssert.assertTrue(getPwdBtnStatus(),"password field not displayed")
        enterAgencyId(hotelData.input.agentIdValid)
        enterEmail(hotelData.input.email1)
        at LoginPage
        acceptCookies()
        sleep(1000)
        at ForGotPasswordPage
        softAssert.assertTrue(verifyEmailError(hotelData.expected.emailAddrErrMsg),"please enter valid addr error msg not displayed")
        enterEmail(hotelData.input.newEmail)
        clickSubmitbutton()
        waitTillLoadingIconDisappears()
        sleep(1000)
        softAssert.assertTrue(verifyEmailError(hotelData.expected.emailErrMsg),"user not found error msg not displayed.")
    }


    }

