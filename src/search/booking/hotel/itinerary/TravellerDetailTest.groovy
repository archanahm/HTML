package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import com.kuoni.qa.automation.util.DateUtil
import spock.lang.IgnoreRest
import spock.lang.Shared
import static com.kuoni.qa.automation.util.TestConf.*

class TravellerDetailTest extends TravellerDetailBaseSpec{
	@Shared
	public static Map<String, HotelTransferTestResultData>  resultMap = [:]
	@Shared
	ClientData clientData = new ClientData("client664")

	DateUtil dateUtil = new DateUtil()
	
	@Shared
	HotelSearchData hotelSearchData = new HotelSearchData("Cambridge-SITUJ18XMLAuto", "itinerarytravellerdetail", itineraryTravellerDetail)
	@Shared
	HotelTransferTestResultData resultData = new HotelTransferTestResultData("Cambridge-SITUJ18XMLAuto",hotelSearchData)

	@IgnoreRest
	def "Add To Itinerary, Add, Edit & Verify Traveller Details "() {
		given: "User is able to login and present on Hotel tab"

		resultMap.put(hotelSearchData.hotelName, resultData)
		login(clientData)
		addToItinerary(clientData, hotelSearchData, resultData)
		addLeadTravellerAndVerify()

		at ItineraryTravllerDetailsPage

		//click Edit button
		clickEditButton()
		sleep(3000)
		//Capture Traveller Label Text

		//String actualTravellerLabelText_FirstEdit=getTravellelersLabelName()
		String actualTravellerLabelText_FirstEdit=getTravellelersLabelName(1)
		//Validate Itinerary Page Traveller Label Txt
		//assertionEquals(actualTravellerLabelText_FirstEdit, hotelSearchData.expected.travlrLabelText, "Itinerary Page, Traveller Details Label Text after first edit Actual & Expected don't match")
		assertionEquals(actualTravellerLabelText_FirstEdit, hotelSearchData.expected.travellerLabelTxt, "Itinerary Page, Traveller Details Label Text after first edit Actual & Expected don't match")



		//Capture Adult or child radio button display status
		boolean actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.notDispStatus, "Itinerary Page, Adult or Child Radio button Display Status Actual & Expected don't match")

		//Capture Mandatory Field Text
		String actualMandatoryFieldTxt_FirstEdit=getMandatoryFieldLabelTextInTravellerDetailsPage()
		//Validate Mandatory Field label Text
		assertionEquals(actualMandatoryFieldTxt_FirstEdit, hotelSearchData.expected.mandatoryFieldTxt, "Itinerary Page, Mandatory Field Label Text after Edit Actual & Expected don't match")

		//Fetch DropDown Values
		List<String> actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Title Drop Down Values after edit Actual & Expected don't match")

		//Capture DropDown values selected
		List<String> actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		List tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		List reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.modified_title_txt,0)

		//Input First Name
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.modified_firstName)
		boolean actualFirstNameTickDisplayStatus_FirstEdit=getEditedfirstNameTickMarkDisplayStatus()
		//Validate first Name entry
		assertionEquals(actualFirstNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, First Name Valid Entry Text after edit Actual & Expected don't match")


		//Input Last Name
		enterEditedLastNameAndTabOut(hotelSearchData.expected.modified_lastName)

		boolean actualLastNameTickDisplayStatus_FirstEdit=getEditedLastNameTickMarkDisplayStatus()
		//Validate last name entry
		assertionEquals(actualLastNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Last Name Valid Entry Text after edit Actual & Expected don't match")

		//boolean actualSaveButtonStatus_FirstEdit=getSaveButtonEnableOrDisableStatus()
		boolean actualSaveButtonStatus_FirstEdit=getSaveCancelButtonEnableOrDisableStatus(2)
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus_FirstEdit,hotelSearchData.expected.dispStatus, "Itinerary Page, Save Button Enable Status after edit Actual & Expected don't match")

		//Input Email Address
		enterEmailAndTabOut(hotelSearchData.expected.modified_emailAddr,0)
		boolean actualEmailTickDispStatus_FirstEdit=getEmailTickMarkDisplayStatus()
		//Validate email
		assertionEquals(actualEmailTickDispStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Email Valid Entry Text Actual & Expected don't match")

		//Input Telephone number
		enterEditTelephoneNumberAndTabOut(hotelSearchData.expected.modified_telephoneNum)
		sleep(2000)
		boolean actualPhoneNumTickDisplayStatus_FirstEdit=getEditedPhoneNumTickMarkDisplayStatus()
		//Validate telephone number
		//assertionEquals(actualPhoneNumTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Telephone Number Valid Entry Text after edit Actual & Expected don't match")
		//Click on Save Button
		clickOnEditSaveOrCancelButton(1)
		//clickonSaveButton()
		sleep(3000)

		//Capture the entered edit details
		//capture entered lead traveller details are displayed correctly
		String expectedleadTravellerName=hotelSearchData.expected.modified_title_txt+" "+hotelSearchData.expected.modified_firstName+" "+hotelSearchData.expected.modified_lastName
		String actualLeadTravellerName=getLeadTravellerName(0)
		//Validate Lead Traveller Name is Saved
		assertionEquals(actualLeadTravellerName,expectedleadTravellerName, "Itinerary Page, Edited Lead Traveller name Actual & Expected don't match")
		//capture entered telephone number are displayed correctly
		String expectedleadTelephoneNum=hotelSearchData.expected.defaultcountryCode+""+hotelSearchData.expected.modified_telephoneNum
		String actualleadTelephoneNum=getLeadTravellerName(1)
		//Validate Telephone number is Saved
		assertionEquals(actualleadTelephoneNum,expectedleadTelephoneNum, "Itinerary Page, Edited Telephone Number Actual & Expected don't match")

		//Capture entered email are displayed correctly
		String actualemailId=getLeadTravellerName(2)
		//Validate Email is Saved
		assertionEquals(actualemailId,hotelSearchData.expected.modified_emailAddr, "Itinerary Page, Edited Entered Email Actual & Expected don't match")

		//Add Travellers Button display status
		boolean actualAddTravellersbuttonDispStatus=getAddTravellersButtonDisplayStatus()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualAddTravellersbuttonDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Add Travellers Button Display status Actual & Expected don't match")

		//Add Travellers Button Text
		String actualTravellersButtonTxt=getAddTravellersButtonTxt()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellersButtonTxt, hotelSearchData.expected.addTravellersBtnTxt, "Itinerary Page, Add Travellers Text Display status Actual & Expected don't match")

		//Add Travellers

		clickAddTravellersButton()
		sleep(5000)

		waitTillLoadingFinished()

		boolean actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Fields Display Status Actual & Expected don't match")


		//Capture Traveller X text
		String actualScndTravellerLabelTxt=getTravellerLabelTxt(2)
		String expectedScndTravellerLabelTxt=hotelSearchData.expected.travellerLabelText+" "+"2"
		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller label text Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult or Child Radio button Display Status Actual & Expected don't match")

		//Capture Adult Radio button select status
		boolean actualAdultRadioButtonSelectStatus=getAdultRadioButtonSelectedStatus(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualAdultRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult Radio button Selected Status Actual & Expected don't match")

		//Capture Remove Text
		String actualRemoveTxt=getRemoveLabelTxt(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualRemoveTxt,hotelSearchData.expected.removeTxt, "Itinerary Page, Second Traveller Remove Text Actual & Expected don't match")

		//click on Remove button
		clickRemoveButton(0)
		actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.notDispStatus, "Itinerary Page, Second Traveller Fields Display Status after remove function Actual & Expected don't match")

		//Add Travellers Button display status
		actualAddTravellersbuttonDispStatus=getAddTravellersButtonDisplayStatus()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualAddTravellersbuttonDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Add Travellers Button Display status after remove function Actual & Expected don't match")

		//Add Travellers Button Text
		actualTravellersButtonTxt=getAddTravellersButtonTxt()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellersButtonTxt, hotelSearchData.expected.addTravellersBtnTxt, "Itinerary Page, Add Travellers Text Display status after remove Actual & Expected don't match")

		clickAddTravellersButton()
		sleep(5000)
		actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Fields Display Status after remove and click on AddTravellers Actual & Expected don't match")


		//Capture Traveller X text
		actualScndTravellerLabelTxt=getTravellerLabelTxt(2)
		expectedScndTravellerLabelTxt=hotelSearchData.expected.travellerLabelText+" "+"2"
		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller label text after remove and click on AddTravellers Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult or Child Radio button Display Status after remove and click on AddTravellers Actual & Expected don't match")

		//Capture Adult Radio button select status
		actualAdultRadioButtonSelectStatus=getAdultRadioButtonSelectedStatus(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualAdultRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult Radio button Selected Status after remove and click on AddTravellers Actual & Expected don't match")

		//Capture Remove Text
		actualRemoveTxt=getRemoveLabelTxt(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualRemoveTxt,hotelSearchData.expected.removeTxt, "Itinerary Page, Second Traveller Remove Text after remove and click on AddTravellers Actual & Expected don't match")

		//Fetch DropDown Values
		actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values after edit Actual & Expected don't match")

		//Capture DropDown values selected
		actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.scndTraveller_title_txt,0)

		//Input First Name
		//enterFirstName(hotelSearchData.expected.scndTraveller_firstName,0)
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.scndTraveller_firstName)
		actualFirstNameTickDisplayStatus_FirstEdit=getEditedfirstNameTickMarkDisplayStatus()
		//Validate first Name entry
		assertionEquals(actualFirstNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller First Name Valid Entry Text after edit Actual & Expected don't match")


		//Input Last Name
		//enterLastName(hotelSearchData.expected.scndTraveller_lastName,0)
		//Input Last Name
		enterEditedLastNameAndTabOut(hotelSearchData.expected.scndTraveller_lastName)

		actualLastNameTickDisplayStatus_FirstEdit=getEditedLastNameTickMarkDisplayStatus()
		//Validate last name entry
		assertionEquals(actualLastNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Last Name Valid Entry Text after edit Actual & Expected don't match")

		sleep(3000)
		//boolean actualSaveButtonStatus=getSaveButtonEnableOrDisableStatus()
		boolean actualSaveButtonStatus=getSaveBtnEnableOrDisableStatus()
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Save Button Enable Status Actual & Expected don't match")

		//Click on Save Button
		clickonSaveButton()

		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()


		//Edit Button display status
		boolean actualEditButtonDispStatus=getEditButtonDisplayStatus(1)
		//Validate Edit button display status
		assertionEquals(actualEditButtonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Edit Button Display Status Actual & Expected don't match")

		boolean actualRemoveIconDispStatus=getRemoveIconDisplayStatus(1)
		//Validate Remove Icon display status
		assertionEquals(actualRemoveIconDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Remove Icon Display Status Actual & Expected don't match")

		//Click Edit
		clickEditButton(1)
		sleep(3000)

		actualAdditionalPaxDispStatus=getEditFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Edit Fields Display Status Actual & Expected don't match")

		//Capture Traveller X text
		actualScndTravellerLabelTxt=getTravellerLabelTxt(3)
		expectedScndTravellerLabelTxt=hotelSearchData.expected.travellerLabelText+" "+"2"
		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller label text after Edit Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult or Child Radio button Display Status after Edit Actual & Expected don't match")

		//Capture Adult Radio button select status
		actualAdultRadioButtonSelectStatus=getAdultRadioButtonSelectedStatus(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualAdultRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult Radio button Selected Status after Edit Actual & Expected don't match")

		//Fetch DropDown Values
		actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values after edit Actual & Expected don't match")

		//Capture DropDown values selected
		actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.modified_title_txt,0)

		//Input First Name
		//enterFirstName(hotelSearchData.expected.modified_scndTraveller_firstName,0)
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.modified_scndTraveller_firstName)
		actualFirstNameTickDisplayStatus_FirstEdit=getEditedfirstNameTickMarkDisplayStatus()
		//Validate first Name entry
		assertionEquals(actualFirstNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller First Name Edit Valid Entry Text after edit Actual & Expected don't match")

		//Input Last Name
		//enterLastName(hotelSearchData.expected.modified_scndTraveller_lastName,0)
		enterEditedLastNameAndTabOut(hotelSearchData.expected.modified_scndTraveller_lastName)

		actualLastNameTickDisplayStatus_FirstEdit=getEditedLastNameTickMarkDisplayStatus()
		//Validate last name entry
		assertionEquals(actualLastNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Last Name Valid Entry Text after edit Actual & Expected don't match")
		sleep(3000)

		//actualSaveButtonStatus=getSaveButtonEnableOrDisableStatus()
		actualSaveButtonStatus=getSaveCancelButtonEnableOrDisableStatus(2)
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Save Button Enable Status after Edit Actual & Expected don't match")

		//Click on Save Button
		//clickonSaveButton()
		clickOnEditSaveOrCancelButton(1)
		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()


		//Capture the entered edit details
		//capture entered second traveller details are displayed correctly
		String expectedScndTravellerName=hotelSearchData.expected.modified_title_txt+" "+hotelSearchData.expected.modified_scndTraveller_firstName+" "+hotelSearchData.expected.modified_scndTraveller_lastName
		//String actualScndTravellerName=getTravellerName(3)
		String actualScndTravellerName=getLeadTravellerName(3)
		//Validate Lead Traveller Name is Saved
		assertionEquals(actualScndTravellerName,expectedScndTravellerName, "Itinerary Page, Edited Second Traveller name Actual & Expected don't match")

		actualRemoveIconDispStatus=getRemoveIconDisplayStatus(1)
		//Validate Remove Icon display status
		assertionEquals(actualRemoveIconDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Remove Icon Display Status after Edit Actual & Expected don't match")

		//click on Remove Icon
		clickRemoveIcon(1)
		sleep(3000)

		boolean actualTravellerDispStatus=getTravellerFieldsDisplayStatus(1)
		//Validate Second Traveller display status
		assertionEquals(actualTravellerDispStatus,hotelSearchData.expected.notDispStatus, "Itinerary Page, Second Traveller Fields Display Status after Edit & remove function Actual & Expected don't match")

		//Add Travellers 2 - child

		//Add Travellers Button display status
		actualAddTravellersbuttonDispStatus=getAddTravellersButtonDisplayStatus()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualAddTravellersbuttonDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Add Travellers Button Display status Actual & Expected don't match")

		//Add Travellers Button Text
		actualTravellersButtonTxt=getAddTravellersButtonTxt()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellersButtonTxt, hotelSearchData.expected.addTravellersBtnTxt, "Itinerary Page, Second Traveller (child) Add Travellers Text Display status Actual & Expected don't match")

		//Add Travellers

		clickAddTravellersButton()
		sleep(5000)

		waitTillLoadingFinished()

		actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Fields Display Status Actual & Expected don't match")


		//Capture Traveller X text
		actualScndTravellerLabelTxt=getTravellerLabelTxt(2)

		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller (child) label text Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Adult or Child Radio button Display Status Actual & Expected don't match")

		//Capture Adult Radio button select status
		actualAdultRadioButtonSelectStatus=getAdultRadioButtonSelectedStatus(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualAdultRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Adult Radio button Selected Status Actual & Expected don't match")

		//Capture Remove Text
		actualRemoveTxt=getRemoveLabelTxt(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualRemoveTxt,hotelSearchData.expected.removeTxt, "Itinerary Page, Second Traveller Remove Text Actual & Expected don't match")

		//Select radio button child
		ClickRadioButtonAdultOrChild(2)
		sleep(2000)
		//Age field display status
		boolean actualAgeFieldDisplayStatus=getAgeTxtBoxdisplayStatus()
		//Validate Age field display status
		assertionEquals(actualAgeFieldDisplayStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Age Field Display Status Actual & Expected don't match")

		/* For child traveller title dropdown is removed in 10.3. Hence commenting below code.
		//Fetch DropDown Values
		actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values after edit Actual & Expected don't match")



		//Capture DropDown values selected
		actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.title_txt,0)
		*/

		//Input First Name
		//enterFirstName(hotelSearchData.expected.modified_scndTraveller_firstName,0)
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.childFirstName)
		actualFirstNameTickDisplayStatus_FirstEdit=getEditedfirstNameTickMarkDisplayStatus()
		//Validate first Name entry
		assertionEquals(actualFirstNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller First Name Edit Valid Entry Text after edit Actual & Expected don't match")

		//Input Last Name
		//enterLastName(hotelSearchData.expected.modified_scndTraveller_lastName,0)
		enterEditedLastNameAndTabOut(hotelSearchData.expected.childLastName)

		actualLastNameTickDisplayStatus_FirstEdit=getEditedLastNameTickMarkDisplayStatus()
		//Validate last name entry
		assertionEquals(actualLastNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Last Name Valid Entry Text after edit Actual & Expected don't match")
		sleep(3000)

		//Enter Age
		clickAndEnterAge(hotelSearchData.input.children.getAt(0).toString())

		//actualSaveButtonStatus=getSaveButtonEnableOrDisableStatus()
		actualSaveButtonStatus=getSaveBtnEnableOrDisableStatus()
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (Child) Save Button Enable Status after Edit Actual & Expected don't match")

		//Click on Save Button
		clickonSaveButton()
		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()


		//Edit Traveller 2 - child

		//Edit Button display status
		actualEditButtonDispStatus=getEditButtonDisplayStatus(1)
		//Validate Edit button display status
		assertionEquals(actualEditButtonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Edit Button Display Status Actual & Expected don't match")

		actualRemoveIconDispStatus=getRemoveIconDisplayStatus(1)
		//Validate Remove Icon display status
		assertionEquals(actualRemoveIconDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Remove Icon Display Status Actual & Expected don't match")

		//Click Edit
		clickEditButton(1)
		sleep(3000)

		actualAdditionalPaxDispStatus=getEditFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Edit Fields Display Status Actual & Expected don't match")

		//Capture Traveller X text
		actualScndTravellerLabelTxt=getTravellerLabelTxt(3)
		expectedScndTravellerLabelTxt=hotelSearchData.expected.travellerLabelText+" "+"2"
		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller (child) label text after Edit Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Adult or Child Radio button Display Status after Edit Actual & Expected don't match")

		//Capture Child Radio button select status
		boolean actualChildRadioButtonSelectStatus=getChildRadioButtonSelectedStatus(1)
		//Validate child  radio button selected status
		assertionEquals(actualChildRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Child Radio button Selected Status after Edit Actual & Expected don't match")

		/* for child traveller title drop down is removed so commenting below code
		//Fetch DropDown Values
		actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Second Traveller(Child) Title Drop Down Values after edit Actual & Expected don't match")
		*/
		/* Commented since title field is removed for child traveller.
		//Capture DropDown values selected
		actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Second Traveller (child) Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.modified_childTitle,0)
		*/
		//Input First Name
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.modified_childFirstName)
		actualFirstNameTickDisplayStatus_FirstEdit=getEditedfirstNameTickMarkDisplayStatus()
		//Validate first Name entry
		assertionEquals(actualFirstNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) First Name Edit Valid Entry Text after edit Actual & Expected don't match")

		//Input Last Name
		enterEditedLastNameAndTabOut(hotelSearchData.expected.modified_childLastName)

		actualLastNameTickDisplayStatus_FirstEdit=getEditedLastNameTickMarkDisplayStatus()
		//Validate last name entry
		assertionEquals(actualLastNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Last Name Valid Entry Text after edit Actual & Expected don't match")
		sleep(3000)

		//commenting below code since the field is changed is changed to select field instead of text field. So cannot
		//enter the age greater than 17. Change is done in 10.3
		/*
		//Enter Age greater than 17
		enterEditedChildAgeAndTabOut(hotelSearchData.expected.modified_age)
		sleep(3000)
		//Capture child age error text
		String actualChildAgeErrorTxt=getChildAgeErrorTxt()
		//Validate Child age error
		assertionEquals(actualChildAgeErrorTxt,hotelSearchData.expected.childAge_ErrorTxt, "Itinerary Page, Second Traveller (Child) child age error text Actual & Expected don't match")
		*/
		//Enter Age
		enterEditedChildAgeAndTabOut(hotelSearchData.input.children.getAt(0).toString())
		sleep(3000)
		//actualSaveButtonStatus=getSaveButtonEnableOrDisableStatus()
		actualSaveButtonStatus=getSaveCancelButtonEnableOrDisableStatus(2)
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (Child) Save Button Enable Status after Edit Actual & Expected don't match")

		//Click on Save Button
		//clickonSaveButton()
		clickOnEditSaveOrCancelButton(1)
		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()

		//Capture the entered edit details
		//capture entered second traveller details are displayed correctly
		//expectedScndTravellerName=hotelSearchData.expected.modified_childTitle+" "+hotelSearchData.expected.modified_childFirstName+" "+hotelSearchData.expected.modified_childLastName+" "+"("+hotelSearchData.input.children.getAt(0).toString()+"yrs)"
		expectedScndTravellerName=hotelSearchData.expected.modified_childFirstName+" "+hotelSearchData.expected.modified_childLastName+" "+"("+hotelSearchData.input.children.getAt(0).toString()+"yrs)"
		//actualScndTravellerName=getTravellerName(3)
		actualScndTravellerName=getLeadTravellerName(3)
		//Validate Lead Traveller Name is Saved
		assertionEquals(actualScndTravellerName,expectedScndTravellerName, "Itinerary Page, Edited Second Traveller (child) name after Edit Actual & Expected don't match")

		actualRemoveIconDispStatus=getRemoveIconDisplayStatus(1)
		//Validate Remove Icon display status
		assertionEquals(actualRemoveIconDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) after Edit Remove Icon Display Status after Edit Actual & Expected don't match")

		//click on Remove Icon
		clickRemoveIcon(1)
		sleep(3000)

		actualTravellerDispStatus=getTravellerFieldsDisplayStatus(1)
		//Validate Second Traveller display status
		assertionEquals(actualTravellerDispStatus,hotelSearchData.expected.notDispStatus, "Itinerary Page, Second Traveller (child) after edit Fields Display Status after Edit & remove function Actual & Expected don't match")


		//Add Travellers 2 - adult

		//Add Travellers Button display status
		actualAddTravellersbuttonDispStatus=getAddTravellersButtonDisplayStatus()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualAddTravellersbuttonDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Add Travellers Button Display status after remove function Actual & Expected don't match")

		//Add Travellers Button Text
		actualTravellersButtonTxt=getAddTravellersButtonTxt()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellersButtonTxt, hotelSearchData.expected.addTravellersBtnTxt, "Itinerary Page, Add Travellers Text Display status after remove Actual & Expected don't match")

		clickAddTravellersButton()
		sleep(5000)
		actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Fields Display Status after twice remove  and click on AddTravellers Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.scndTraveller_title_txt,0)

		//Input First Name
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.scndTraveller_firstName)

		//Input Last Name
		enterEditedLastNameAndTabOut(hotelSearchData.expected.scndTraveller_lastName)
		sleep(3000)
		//Click on Save Button
		clickonSaveButton()
		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()


		//Capture the entered edit details
		//capture entered second traveller details are displayed correctly
		expectedScndTravellerName=hotelSearchData.expected.scndTraveller_title_txt+" "+hotelSearchData.expected.scndTraveller_firstName+" "+hotelSearchData.expected.scndTraveller_lastName
		//actualScndTravellerName=getTravellerName(3)
		actualScndTravellerName=getLeadTravellerName(3)
		//Validate Second Traveller Name is Saved
		assertionEquals(actualScndTravellerName,expectedScndTravellerName, "Itinerary Page, Second Traveller name Actual & Expected don't match")

		//Edit traveller 2 - to child

		//Click Edit
		clickEditButton(1)
		sleep(4000)

		actualAdditionalPaxDispStatus=getEditFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Edit Fields Display Status Actual & Expected don't match")

		//Select radio button child
		ClickRadioButtonAdultOrChild(2)
		sleep(2000)

		//Age field display status
		actualAgeFieldDisplayStatus=getAgeTxtBoxdisplayStatus()
		//Validate Age field display status
		assertionEquals(actualAgeFieldDisplayStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (convert from adult to child) Age Field Display Status Actual & Expected don't match")

		//commenting below code since the field is changed is changed to select field instead of text field. So cannot
		//enter the age greater than 17. Change is done in 10.3
		/*
		//Enter Age greater than 17
		enterEditedChildAgeAndTabOut(hotelSearchData.expected.modified_age)
		sleep(3000)
		//Capture child age error text
		actualChildAgeErrorTxt=getChildAgeErrorTxt()
		//Validate Child age error
		assertionEquals(actualChildAgeErrorTxt,hotelSearchData.expected.childAge_ErrorTxt, "Itinerary Page, Second Traveller (Child) child age error text Actual & Expected don't match")
		*/

		try{
			//Enter Age
			enterEditedChildAgeAndTabOut(hotelSearchData.expected.childAgeMax)
			sleep(3000)
		}catch(Exception e){
			softAssert.assertFalse(true,"Failed To enter age since the field was not displayed")
		}


		//Click on Save Button
		//clickonSaveButton()
		clickOnEditSaveOrCancelButton(1)

		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()

		//Capture the entered edit details
		//capture entered second traveller details are displayed correctly
		expectedScndTravellerName=hotelSearchData.expected.scndTraveller_firstName+" "+hotelSearchData.expected.scndTraveller_lastName+" "+"("+hotelSearchData.expected.childAgeMax+"yrs)"
		//actualScndTravellerName=getTravellerName(3)
		actualScndTravellerName=getLeadTravellerName(3)
		//Validate Lead Traveller Name is Saved
		assertionEquals(actualScndTravellerName,expectedScndTravellerName, "Itinerary Page, Edited Second Traveller (convert adult to child) name after Edit Actual & Expected don't match")


		//Add Travellers 3 - child

		//Add Travellers Button display status
		actualAddTravellersbuttonDispStatus=getAddTravellersButtonDisplayStatus()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualAddTravellersbuttonDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Add Travellers Button Display status after remove function Actual & Expected don't match")

		//Add Travellers Button Text
		actualTravellersButtonTxt=getAddTravellersButtonTxt()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellersButtonTxt, hotelSearchData.expected.addTravellersBtnTxt, "Itinerary Page, Add Travellers Text Display status after remove Actual & Expected don't match")

		clickAddTravellersButton()
		sleep(5000)
		actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Fields Display Status after twice remove  and click on AddTravellers Actual & Expected don't match")

		//Select radio button child
		ClickRadioButtonAdultOrChild(2)
		sleep(2000)

		//Age field display status
		actualAgeFieldDisplayStatus=getAgeTxtBoxdisplayStatus()
		//Validate Age field display status
		assertionEquals(actualAgeFieldDisplayStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (convert from adult to child) Age Field Display Status Actual & Expected don't match")
		/*title field is removed for child traveller in 10.3. Hence commenting
		//Input Title
		selectTitle(hotelSearchData.expected.thirdTraveller_title_txt,0)
		*/
		//Input First Name
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.thirdTraveller_firstName)

		//Input Last Name
		enterEditedLastNameAndTabOut(hotelSearchData.expected.thirdTraveller_lastName)

		//Enter Age
		enterEditedChildAgeAndTabOut(hotelSearchData.expected.childAgeThird)
		sleep(3000)

		//Click on Save Button
		clickonSaveButton()
		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()

		//Capture the entered edit details
		//capture entered third traveller details are displayed correctly
		String expectedThirdTravellerName=hotelSearchData.expected.thirdTraveller_firstName+" "+hotelSearchData.expected.thirdTraveller_lastName+" "+"("+hotelSearchData.expected.childAgeThird+"yrs)"
		//String actualThirdTravellerName=getTravellerName(4)
		String actualThirdTravellerName=getLeadTravellerName(4)
		//Validate Third Traveller Name is Saved
		assertionEquals(actualThirdTravellerName,expectedThirdTravellerName, "Itinerary Page, Third Traveller Details after Save Actual & Expected don't match")


		//Edit traveller 3 - to adult
		//Click Edit
		clickEditButton(2)
		sleep(3000)

		actualAdditionalPaxDispStatus=getEditFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller (child) Edit Fields Display Status Actual & Expected don't match")

		//Select radio button child
		ClickRadioButtonAdultOrChild(1)
		sleep(2000)

		//Age field display status
		actualAgeFieldDisplayStatus=getAgeTxtBoxdisplayStatus()
		//Validate Age field display status
		assertionEquals(actualAgeFieldDisplayStatus,hotelSearchData.expected.notDispStatus, "Itinerary Page, Third Traveller (convert from child to adult) Age Field Display Status Actual & Expected don't match")

		sleep(3000)

		//Click on Save Button
		//clickonSaveButton()
		clickOnEditSaveOrCancelButton(1)
		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()

		//Capture the entered edit details
		//capture entered third traveller details are displayed correctly
		expectedThirdTravellerName=hotelSearchData.expected.thirdTraveller_firstName+" "+hotelSearchData.expected.thirdTraveller_lastName

		//actualThirdTravellerName=getTravellerName(4)
		actualThirdTravellerName=getLeadTravellerName(4)
		//Validate Third Traveller Name is Saved
		assertionEquals(actualThirdTravellerName,expectedThirdTravellerName, "Itinerary Page, Third Traveller Details after edit Save Actual & Expected don't match")

		//Add Travellers 4 - adult To 10 - Adult
		addSaveAndVerifyAdultTravellers(4,10,hotelSearchData)

		//Add Travellers11 - child To 2 - Child
		addSaveAndVerifyChildTravellers(11,12,hotelSearchData,hotelSearchData.input.children)


	}


	def "02 : Edit & Verify Lead Traveller Details   "() {
		given: "User has added one lead traveller with all the fields"

		at ItineraryTravllerDetailsPage

		//click Edit button
		clickEditButton()
		sleep(3000)
		//Capture Traveller Label Text

		String actTravellerLabelTxt_FirstEdit=getTravellelersLabelName()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actTravellerLabelTxt_FirstEdit, hotelSearchData.expected.travlrLabelText, "Itinerary Page, Traveller Details Label Text after edit Actual & Expected don't match")


		//Capture Adult or child radio button display status
		boolean actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.notDispStatus, "Itinerary Page, Adult or Child Radio button Display Status Actual & Expected don't match")

		//Capture Mandatory Field Text
		String actualMandatoryFieldTxt_FirstEdit=getMandatoryFieldLabelTextInTravellerDetailsPage()
		//Validate Mandatory Field label Text
		assertionEquals(actualMandatoryFieldTxt_FirstEdit, hotelSearchData.expected.mandatoryFieldTxt, "Itinerary Page, Mandatory Field Label Text after Edit Actual & Expected don't match")

		//Fetch DropDown Values
		List<String> actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Title Drop Down Values after edit Actual & Expected don't match")

		//Capture DropDown values selected
		List<String> actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		List tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		List reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.modified_title_txt,0)

		//Input First Name
		enterEditedFirstNameAndTabOut(hotelSearchData.expected.modified_firstName)
		boolean actualFirstNameTickDisplayStatus_FirstEdit=getEditedfirstNameTickMarkDisplayStatus()
		//Validate first Name entry
		assertionEquals(actualFirstNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, First Name Valid Entry Text after edit Actual & Expected don't match")


		//Input Last Name
		enterEditedLastNameAndTabOut(hotelSearchData.expected.modified_lastName)

		boolean actualLastNameTickDisplayStatus_FirstEdit=getEditedLastNameTickMarkDisplayStatus()
		//Validate last name entry
		assertionEquals(actualLastNameTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Last Name Valid Entry Text after edit Actual & Expected don't match")

		boolean actualSaveButtonStatus_FirstEdit=getSaveButtonEnableOrDisableStatus()
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus_FirstEdit,hotelSearchData.expected.dispStatus, "Itinerary Page, Save Button Enable Status after edit Actual & Expected don't match")

		//Input Email Address
		enterEmailAndTabOut(hotelSearchData.expected.modified_emailAddr,0)
		boolean actualEmailTickDispStatus_FirstEdit=getEmailTickMarkDisplayStatus()
		//Validate email
		assertionEquals(actualEmailTickDispStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Email Valid Entry Text Actual & Expected don't match")

		//Input Telephone number
		enterEditTelephoneNumberAndTabOut(hotelSearchData.expected.modified_telephoneNum)
		sleep(2000)
		boolean actualPhoneNumTickDisplayStatus_FirstEdit=getEditedPhoneNumTickMarkDisplayStatus()
		//Validate telephone number
		//assertionEquals(actualPhoneNumTickDisplayStatus_FirstEdit, hotelSearchData.expected.dispStatus, "Itinerary Page, Telephone Number Valid Entry Text after edit Actual & Expected don't match")
		//Click on Save Button
		clickonSaveButton()
		sleep(3000)

		//Capture the entered edit details
			//capture entered lead traveller details are displayed correctly
			String expectedleadTravellerName=hotelSearchData.expected.modified_title_txt+" "+hotelSearchData.expected.modified_firstName+" "+hotelSearchData.expected.modified_lastName
			String actualLeadTravellerName=getLeadTravellerName(0)
			//Validate Lead Traveller Name is Saved
			assertionEquals(actualLeadTravellerName,expectedleadTravellerName, "Itinerary Page, Edited Lead Traveller name Actual & Expected don't match")
			//capture entered telephone number are displayed correctly
			String expectedleadTelephoneNum=hotelSearchData.expected.defaultcountryCode+" "+hotelSearchData.expected.modified_telephoneNum
			String actualleadTelephoneNum=getLeadTravellerName(1)
			//Validate Telephone number is Saved
			assertionEquals(actualleadTelephoneNum,expectedleadTelephoneNum, "Itinerary Page, Edited Telephone Number Actual & Expected don't match")

			//Capture entered email are displayed correctly
			String actualemailId=getLeadTravellerName(2)
			//Validate Email is Saved
			assertionEquals(actualemailId,hotelSearchData.expected.modified_emailAddr, "Itinerary Page, Edited Entered Email Actual & Expected don't match")


	}

	def "03 : Add Second Traveller Details   "(){

		given: "User has added one lead traveller with all the fields"
		at ItineraryTravllerDetailsPage

		//Add Travellers Button display status
		boolean actualAddTravellersbuttonDispStatus=getAddTravellersButtonDisplayStatus()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualAddTravellersbuttonDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Add Travellers Button Display status Actual & Expected don't match")

		//Add Travellers Button Text
		String actualTravellersButtonTxt=getAddTravellersButtonTxt()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellersButtonTxt, hotelSearchData.expected.addTravellersBtnTxt, "Itinerary Page, Add Travellers Text Display status Actual & Expected don't match")

		//Add Travellers

		clickAddTravellersButton()
		sleep(5000)

		waitTillLoadingFinished()

		boolean actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Fields Display Status Actual & Expected don't match")


		//Capture Traveller X text
		String actualScndTravellerLabelTxt=getTravellerLabelTxt(2)
		String expectedScndTravellerLabelTxt=hotelSearchData.expected.travellerLabelText+" "+"2"
		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller label text Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		boolean actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult or Child Radio button Display Status Actual & Expected don't match")

		//Capture Adult Radio button select status
		boolean actualAdultRadioButtonSelectStatus=getAdultRadioButtonSelectedStatus(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualAdultRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult Radio button Selected Status Actual & Expected don't match")

		//Capture Remove Text
		String actualRemoveTxt=getRemoveLabelTxt(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualRemoveTxt,hotelSearchData.expected.removeTxt, "Itinerary Page, Second Traveller Remove Text Actual & Expected don't match")

		//click on Remove button
		clickRemoveButton(0)
		actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.notDispStatus, "Itinerary Page, Second Traveller Fields Display Status after remove function Actual & Expected don't match")

		//Add Travellers Button display status
		actualAddTravellersbuttonDispStatus=getAddTravellersButtonDisplayStatus()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualAddTravellersbuttonDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Add Travellers Button Display status after remove function Actual & Expected don't match")

		//Add Travellers Button Text
		actualTravellersButtonTxt=getAddTravellersButtonTxt()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellersButtonTxt, hotelSearchData.expected.addTravellersBtnTxt, "Itinerary Page, Add Travellers Text Display status after remove Actual & Expected don't match")

		clickAddTravellersButton()
		sleep(5000)
		actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Fields Display Status after remove and click on AddTravellers Actual & Expected don't match")


		//Capture Traveller X text
		actualScndTravellerLabelTxt=getTravellerLabelTxt(2)
		expectedScndTravellerLabelTxt=hotelSearchData.expected.travellerLabelText+" "+"2"
		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller label text after remove and click on AddTravellers Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult or Child Radio button Display Status after remove and click on AddTravellers Actual & Expected don't match")

		//Capture Adult Radio button select status
		actualAdultRadioButtonSelectStatus=getAdultRadioButtonSelectedStatus(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualAdultRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult Radio button Selected Status after remove and click on AddTravellers Actual & Expected don't match")

		//Capture Remove Text
		actualRemoveTxt=getRemoveLabelTxt(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualRemoveTxt,hotelSearchData.expected.removeTxt, "Itinerary Page, Second Traveller Remove Text after remove and click on AddTravellers Actual & Expected don't match")

		//Fetch DropDown Values
		List<String> actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values after edit Actual & Expected don't match")

		//Capture DropDown values selected
		List<String> actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		List tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		List reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.scndTraveller_title_txt,0)

		//Input First Name
		enterFirstName(hotelSearchData.expected.scndTraveller_firstName,0)

		//Input Last Name
		enterLastName(hotelSearchData.expected.scndTraveller_lastName,0)
		sleep(3000)
		boolean actualSaveButtonStatus=getSaveButtonEnableOrDisableStatus()
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Save Button Enable Status Actual & Expected don't match")

		//Click on Save Button
		clickonSaveButton()
		sleep(6000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()


	}

	def "04 : Edit Second Traveller Details   "() {

		given: "User has added one lead traveller with all the fields"
		at ItineraryTravllerDetailsPage

		//Edit Button display status
		boolean actualEditButtonDispStatus=getEditButtonDisplayStatus(1)
		//Validate Edit button display status
		assertionEquals(actualEditButtonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Edit Button Display Status Actual & Expected don't match")

		boolean actualRemoveIconDispStatus=getRemoveIconDisplayStatus(1)
		//Validate Remove Icon display status
		assertionEquals(actualRemoveIconDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Remove Icon Display Status Actual & Expected don't match")

		//Click Edit
		clickEditButton(1)

		boolean actualAdditionalPaxDispStatus=getAdditionalPaxFieldsDisplayStatus()
		//Validate Additional pax display status
		assertionEquals(actualAdditionalPaxDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Edit Fields Display Status Actual & Expected don't match")

		//Capture Traveller X text
		String actualScndTravellerLabelTxt=getTravellerLabelTxt(2)
		String expectedScndTravellerLabelTxt=hotelSearchData.expected.travellerLabelText+" "+"2"
		assertionEquals(actualScndTravellerLabelTxt,expectedScndTravellerLabelTxt, "Itinerary Page, Second Traveller label text after Edit Display status Actual & Expected don't match")

		//Capture Adult or child radio button display status
		boolean  actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()
		//Validate Adult or Child radio button display status
		assertionEquals(actualAdultOrChildRadioButonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult or Child Radio button Display Status after Edit Actual & Expected don't match")

		//Capture Adult Radio button select status
		boolean actualAdultRadioButtonSelectStatus=getAdultRadioButtonSelectedStatus(1)
		//Validate Adult  radio button selected status
		assertionEquals(actualAdultRadioButtonSelectStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Second Traveller Adult Radio button Selected Status after Edit Actual & Expected don't match")

		//Fetch DropDown Values
		List<String> actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues_FirstEdit,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values after edit Actual & Expected don't match")

		//Capture DropDown values selected
		List<String> actualDropDownSelected_FirstEdit=getDropDownValuesSelected()
		//Expected DropDown values selected
		List tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		List reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Second Traveller Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Input Title
		selectTitle(hotelSearchData.expected.modified_title_txt,0)

	}

	private def addLeadTravellerAndVerify() {

		at ItenaryBuilderPage
		hideItenaryBuilder()

		//click Go to Itinerary Link
		clickOnGotoItenaryButton()
		sleep(2000)

		at ItineraryTravllerDetailsPage

		//Capture Traveller Label Text
		String actualTravellerLabelTxt=getTravllerLabelTextInTravellerDetailsPage()
		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(actualTravellerLabelTxt, hotelSearchData.expected.travellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")

		//Capture Mandatory Field Text
		String actualMandatoryFieldTxt=getMandatoryFieldLabelTextInTravellerDetailsPage()
		//Validate Mandatory Field label Text
		assertionEquals(actualMandatoryFieldTxt, hotelSearchData.expected.mandatoryFieldTxt, "Itinerary Page, Mandatory Field Label Text Actual & Expected don't match")

		//Fetch DropDown Values
		List<String> actualDropDownListValues=getAllOptionValuesFromTitleDropDown()
		//Validate Drop down list values
		assertionListEquals(actualDropDownListValues,hotelSearchData.expected.titleDropDownValues,"Itinerary Page, Title Drop Down Values Actual & Expected don't match")


		//Capture DropDown values selected
		List<String> actualDropDownSelected=getDropDownValuesSelected()
		//Expected DropDown values selected
		List tmpDropDownValues=hotelSearchData.expected.titleDropDownValues
		List reverseDropDownValues=tmpDropDownValues.reverse()
		//Validate Drop down list selected or not
		assertionListEquals(actualDropDownSelected,reverseDropDownValues,"Itinerary Page, Title Drop Down Values Selected Actual & Expected don't match")

		//click Remove button
		clickRemoveButton(0)
		sleep(2000)
		clickRemoveButton(0)
		sleep(2000)
		clickRemoveButton(0)
		sleep(2000)

		//Input Title
		selectTitle(hotelSearchData.expected.title_txt,0)

		//Input First Name
		enterFirstNameAndTabOut(hotelSearchData.expected.firstName,0)

		boolean actualFirstNameTickDisplayStatus=getfirstNameTickMarkDisplayStatus()
		//Validate first Name entry
		assertionEquals(actualFirstNameTickDisplayStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, First Name Valid Entry Text Actual & Expected don't match")

		//Input Last Name
		enterLastNameAndTabOut(hotelSearchData.expected.lastName,0)

		boolean actualLastNameTickDisplayStatus=getLastNameTickMarkDisplayStatus()
		//Validate last name entry
		assertionEquals(actualLastNameTickDisplayStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Last Name Valid Entry Text Actual & Expected don't match")

		boolean actualSaveButtonStatus=getSaveButtonEnableOrDisableStatus()
		//Validate Save button enable status
		assertionEquals(actualSaveButtonStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Save Button Enable Status Actual & Expected don't match")

		//Input Email Address
		enterEmailAndTabOut(hotelSearchData.expected.emailAddr,0)
		boolean actualEmailTickDispStatus=getEmailTickMarkDisplayStatus()
		//Validate email
		assertionEquals(actualEmailTickDispStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Email Valid Entry Text Actual & Expected don't match")

		//Input Area Code
		//enterCountryCode(hotelSearchData.expected.defaultcountryCode,0)
		//sleep(1000)
		//Input Telephone number
		enterTelephoneNumberAndTabOut(hotelSearchData.expected.telephone_Num,0)
		boolean actualPhoneNumTickDisplayStatus=getPhoneNumTickMarkDisplayStatus()
		//Validate telephone number
		//assertionEquals(actualPhoneNumTickDisplayStatus, hotelSearchData.expected.dispStatus, "Itinerary Page, Telephone Number Valid Entry Text Actual & Expected don't match")

		sleep(1000)


		//Click on Save Button
		//clickonSaveButton()
		clickonSaveButton(0)
		sleep(3000)

		//sleep(2000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()

		//Capture the entered details
			//capture entered lead traveller details are displayed correctly
			String expectedleadTravellerName=hotelSearchData.expected.title_txt+" "+hotelSearchData.expected.firstName+" "+hotelSearchData.expected.lastName
			String actualLeadTravellerName=getLeadTravellerName(0)
			//Validate Lead Traveller Name is Saved
			assertionEquals(actualLeadTravellerName,expectedleadTravellerName, "Itinerary Page, Lead Traveller name Actual & Expected don't match")
			//capture entered telephone number are displayed correctly
			String expectedleadTelephoneNum=hotelSearchData.expected.defaultcountryCode+""+hotelSearchData.expected.telephone_Num
			String actualleadTelephoneNum=getLeadTravellerName(1)
			//Validate Telephone number is Saved
			assertionEquals(actualleadTelephoneNum,expectedleadTelephoneNum, "Itinerary Page, Telephone Number Actual & Expected don't match")

			//Capture entered email are displayed correctly
			String actualemailId=getLeadTravellerName(2)
			//Validate Email is Saved
			assertionEquals(actualemailId,hotelSearchData.expected.emailAddr, "Itinerary Page, Email Entered Actual & Expected don't match")


		//Edit Button display status
		boolean actualEditButtonDispStatus=verifyTravellerEditButtonDisplayed()
		//Validate Edit button display status
		assertionEquals(actualEditButtonDispStatus,hotelSearchData.expected.dispStatus, "Itinerary Page, Edit Button Display Status Actual & Expected don't match")


	}

/*
	def "01 : Itinerary Builder   "() {
		given: "User is able to login and present on Hotel tab"

		resultMap.put(hotelSearchData.hotelName, resultData)
		login(clientData)
		addToItinerary(clientData,hotelSearchData, resultData)
		addTravellers(clientData,hotelSearchData, resultData)
		//verifyTravllerDetailresults(hotelSearchData, resultData)
	
	}*/
/*
	def addTravellers(ClientData clientData,HotelSearchData data, HotelTransferTestResultData resultData) {


		at ItenaryBuilderPage
		hideItenaryBuilder()

		//click Go to Itinerary Link
		clickOnGotoItenaryButton()
		sleep(2000)

		at ItineraryTravllerDetailsPage

		//Capture Traveller Label Text
		resultData.hotel.itineraryPage.actualTravellerLabelTxt=getTravllerLabelTextInTravellerDetailsPage()

		//Capture Mandatory Field Text
		resultData.hotel.itineraryPage.actualMandatoryFieldTxt=getMandatoryFieldLabelTextInTravellerDetailsPage()

		//Fetch DropDown Values
		resultData.hotel.itineraryPage.actualDropDownListValues=getAllOptionValuesFromTitleDropDown()

		//Capture DropDown values selected
		resultData.hotel.itineraryPage.actualDropDownSelected=getDropDownValuesSelected()

		//Input Title
		selectTitle(data.expected.title_txt,0)

		//Input First Name
		enterFirstNameAndTabOut(data.expected.firstName,0)

		resultData.hotel.itineraryPage.actualFirstNameTickDisplayStatus=getfirstNameTickMarkDisplayStatus()
		//Input Last Name
		enterLastNameAndTabOut(data.expected.lastName,0)

		resultData.hotel.itineraryPage.actualLastNameTickDisplayStatus=getLastNameTickMarkDisplayStatus()
		resultData.hotel.itineraryPage.actualSaveButtonStatus=getSaveButtonEnableOrDisableStatus()

		//Input Email Address
		enterEmailAndTabOut(data.expected.emailAddr,0)
		resultData.hotel.itineraryPage.actualEmailTickDispStatus=getEmailTickMarkDisplayStatus()
		println("Email Tick Box display status:$resultData.hotel.itineraryPage.actualEmailTickDispStatus")

		//Input Area Code
		enterCountryCode(data.expected.countryCode,0)
		sleep(1000)
		//Input Telephone number
		enterTelephoneNumberAndTabOut(data.expected.telephone_Num,0)
		resultData.hotel.itineraryPage.actualPhoneNumTickDisplayStatus=getPhoneNumTickMarkDisplayStatus()
		sleep(1000)

		//Click on Save Button
		clickonSaveButton()
		//sleep(2000)
		waitTillLoadingIconDisappears()
		waitTillTravellerDetailsSaved()
		//Edit Button display status
		resultData.hotel.itineraryPage.actualEditButtonDispStatus=verifyTravellerEditButtonDisplayed()

		//click Edit button
		clickEditButton()
		sleep(3000)
		//Capture Traveller Label Text
		resultData.hotel.itineraryPage.actualTravellerLabelTxt_FirstEdit=getTravellelersLabelName()

		//Capture Adult or child radio button display status
		resultData.hotel.itineraryPage.actualAdultOrChildRadioButonDispStatus=getAdultOrChildRadioButtonDisplayStatus()

		//Capture Mandatory Field Text
		resultData.hotel.itineraryPage.actualMandatoryFieldTxt_FirstEdit=getMandatoryFieldLabelTextInTravellerDetailsPage()

		//Fetch DropDown Values
		resultData.hotel.itineraryPage.actualDropDownListValues_FirstEdit=getAllOptionValuesFromTitleDropDown()

		//Capture DropDown values selected
		resultData.hotel.itineraryPage.actualDropDownSelected_FirstEdit=getDropDownValuesSelected()

		//Input Title
		selectTitle(data.expected.modified_title_txt,0)

		//Input First Name
		enterEditedFirstNameAndTabOut(data.expected.modified_firstName)

		resultData.hotel.itineraryPage.actualFirstNameTickDisplayStatus_FirstEdit=getEditedfirstNameTickMarkDisplayStatus()
		//Input Last Name
		enterEditedLastNameAndTabOut(data.expected.modified_lastName)

		resultData.hotel.itineraryPage.actualLastNameTickDisplayStatus_FirstEdit=getEditedLastNameTickMarkDisplayStatus()
		resultData.hotel.itineraryPage.actualSaveButtonStatus_FirstEdit=getSaveButtonEnableOrDisableStatus()

		//Input Email Address
		enterEmailAndTabOut(data.expected.emailAddr,0)
		resultData.hotel.itineraryPage.actualEmailTickDispStatus_FirstEdit=getEmailTickMarkDisplayStatus()
		println("Email Tick box after edit:$resultData.hotel.itineraryPage.actualEmailTickDispStatus_FirstEdit ")
		//Input Telephone number
		enterEditTelephoneNumberAndTabOut(data.expected.telephone_Num)
		sleep(2000)
		resultData.hotel.itineraryPage.actualPhoneNumTickDisplayStatus_FirstEdit=getEditedPhoneNumTickMarkDisplayStatus()
		sleep(1000)

		//Click on Save Button
		clickonSaveButton()
		//sleep(2000)

	}*/


/*
	protected def verifyTravllerDetailresults(HotelSearchData data, HotelTransferTestResultData resultData) {
		//Expected DropDown values selected
		List tmpDropDownValues=data.expected.titleDropDownValues
		List reverseDropDownValues=tmpDropDownValues.reverse()

		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(resultData.hotel.itineraryPage.actualTravellerLabelTxt, data.expected.travellerLabelTxt, "Itinerary Page, Traveller Details Label Text added Actual & Expected don't match")

		//Validate Mandatory Field label Text
		assertionEquals(resultData.hotel.itineraryPage.actualMandatoryFieldTxt, data.expected.mandatoryFieldTxt, "Itinerary Page, Mandatory Field Label Text Actual & Expected don't match")

		//Validate Drop down list values
		assertionListEquals(resultData.hotel.itineraryPage.actualDropDownListValues,data.expected.titleDropDownValues,"Itinerary Page, Title Drop Down Values Actual & Expected don't match")

		//Validate Drop down list selected or not
		assertionListEquals(resultData.hotel.itineraryPage.actualDropDownSelected,reverseDropDownValues,"Itinerary Page, Title Drop Down Values Selected Actual & Expected don't match")

		//Validate first Name entry
		assertionEquals(resultData.hotel.itineraryPage.actualFirstNameTickDisplayStatus, data.expected.dispStatus, "Itinerary Page, First Name Valid Entry Text Actual & Expected don't match")

		//Validate last name entry
		assertionEquals(resultData.hotel.itineraryPage.actualLastNameTickDisplayStatus, data.expected.dispStatus, "Itinerary Page, Last Name Valid Entry Text Actual & Expected don't match")

		//Validate email
		//assertionEquals(resultData.hotel.itineraryPage.actualEmailTickDispStatus, data.expected.dispStatus, "Itinerary Page, Email Valid Entry Text Actual & Expected don't match")

		//Validate telephone number
		assertionEquals(resultData.hotel.itineraryPage.actualPhoneNumTickDisplayStatus, data.expected.dispStatus, "Itinerary Page, Telephone Number Valid Entry Text Actual & Expected don't match")

		//Validate Save button enable status
		assertionEquals(resultData.hotel.itineraryPage.actualSaveButtonStatus,data.expected.dispStatus, "Itinerary Page, Save Button Enable Status Actual & Expected don't match")

		//Validate Edit button display status
		assertionEquals(resultData.hotel.itineraryPage.actualEditButtonDispStatus,data.expected.dispStatus, "Itinerary Page, Edit Button Display Status Actual & Expected don't match")

		//Validate Itinerary Page Traveller Label Txt
		assertionEquals(resultData.hotel.itineraryPage.actualTravellerLabelTxt_FirstEdit, data.expected.travellerLabelTxt, "Itinerary Page, Traveller Details Label Text after edit Actual & Expected don't match")

		//Validate Adult or Child radio button display status
		assertionEquals(resultData.hotel.itineraryPage.actualAdultOrChildRadioButonDispStatus,data.expected.notDispStatus, "Itinerary Page, Adult or Child Radio button Display Status Actual & Expected don't match")

		//Validate Mandatory Field label Text
		assertionEquals(resultData.hotel.itineraryPage.actualMandatoryFieldTxt_FirstEdit, data.expected.mandatoryFieldTxt, "Itinerary Page, Mandatory Field Label Text after Edit Actual & Expected don't match")

		//Validate Drop down list values
		assertionListEquals(resultData.hotel.itineraryPage.actualDropDownListValues_FirstEdit,data.expected.titleDropDownValues,"Itinerary Page, Title Drop Down Values after edit Actual & Expected don't match")

		//Validate Drop down list selected or not
		assertionListEquals(resultData.hotel.itineraryPage.actualDropDownSelected_FirstEdit,reverseDropDownValues,"Itinerary Page, Title Drop Down Values Selected after edit  Actual & Expected don't match")

		//Validate first Name entry
		assertionEquals(resultData.hotel.itineraryPage.actualFirstNameTickDisplayStatus_FirstEdit, data.expected.dispStatus, "Itinerary Page, First Name Valid Entry Text after edit Actual & Expected don't match")

		//Validate last name entry
		assertionEquals(resultData.hotel.itineraryPage.actualLastNameTickDisplayStatus_FirstEdit, data.expected.dispStatus, "Itinerary Page, Last Name Valid Entry Text after edit Actual & Expected don't match")

		//Validate email
		//assertionEquals(resultData.hotel.itineraryPage.actualEmailTickDispStatus_FirstEdit, data.expected.dispStatus, "Itinerary Page, Email Valid Entry Text Actual & Expected don't match")

		//Validate telephone number
		assertionEquals(resultData.hotel.itineraryPage.actualPhoneNumTickDisplayStatus_FirstEdit, data.expected.dispStatus, "Itinerary Page, Telephone Number Valid Entry Text after edit Actual & Expected don't match")

		//Validate Save button enable status
		assertionEquals(resultData.hotel.itineraryPage.actualSaveButtonStatus_FirstEdit,data.expected.dispStatus, "Itinerary Page, Save Button Enable Status after edit Actual & Expected don't match")



	}*/
			/*
            def "02 : Verify Results   "() {

                given: "User is logged in and present on Search Results"
                verifyresults(hotelData, resultData)
                }
            def "03 : Verify About To Book Screen Results   "() {

                    given: "User is logged in and present on About to Book Screen"
                    verifyAboutToBookScreenresults(hotelData, resultData)
                    }

            def "04 : Verify Booking Confirmation Screen Results   "() {

                    given: "User is logged in and present on Booking Confirmation Screen"
                    verifyBookingConfirmationScreenresults(hotelData, resultData)
                    }

            def "05 : Verify Booked Item Screen Results   "() {

                    given: "User is logged in and present on Booked Items Screen"
                    verifyBookedItemsScreenresults(hotelData, resultData)
                    }*/
}
