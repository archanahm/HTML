package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.page.transfers.ItineraryTravllerDetailsPage
import org.testng.Assert
import spock.lang.Unroll

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.helper.HotelSearchData
import com.kuoni.qa.automation.helper.HotelTransferTestResultData
import com.kuoni.qa.automation.page.HotelSearchPage
import com.kuoni.qa.automation.page.ItenaryBuilderPage
import com.kuoni.qa.automation.page.hotel.HotelSearchResultsPage
import com.kuoni.qa.automation.page.transfers.ItenaryPageItems
import com.kuoni.qa.automation.test.BaseSpec
import com.kuoni.qa.automation.util.DateUtil

class ItnryBuldrTravlrDetailsSpec extends BaseSpec{

	public static Map<String, HotelTransferTestResultData>  resultMap = [:]
	
	ClientData clientData = new ClientData("client664")

	DateUtil dateUtil = new DateUtil()
	
	def "01 : Itinerary Builder  "()
	{
		given: "User is able to login and go to transfers view"
		
			
		 login(clientData)
		 at HotelSearchPage
		 HotelSearchData hotelData
		 HotelTransferTestResultData resultData
			  
		 hotelData = new HotelSearchData("Cambridge-SITUJ18XMLAuto")
		 resultData = new HotelTransferTestResultData("Cambridge-SITUJ18XMLAuto",hotelData)
		 resultMap.put(hotelData.hotelName, resultData)
		 addToItinerary(hotelData, resultData)
		 
		
 //		Remove item from itinerary builder
 
		 
	}
	
	private def addToItinerary(HotelSearchData data, HotelTransferTestResultData resultData)
	{
		
		at HotelSearchPage
		//Enter Hotel Destination
		enterHotelDestination(data.input.cityAreaHotelTypeText)
		selectFromAutoSuggest(data.input.cityAreaHotelautoSuggest)
		
		//Enter Check In and Check Out Dates
		entercheckInCheckOutDate(data.input.checkInDays.toString().toInteger(),data.input.checkOutDays.toString().toInteger())
		sleep(3000)
		clickPaxRoom()
		sleep(3000)
		//Enter Pax
		clickPaxRoom()
		selectNumberOfAdults(data.input.pax.toString(), 0)
		
		selectNumberOfChildren(data.input.children.toString() , 0)
		sleep(2000)
		enterChildrenAge(data.expected.childAgeFirst,"0", 0)
		sleep(2000)
		enterChildrenAge(data.expected.childAgeSecond,"1", 1)
		sleep(2000)
		clickPaxRoom()
		//click on Find button
		clickFindButton()
		sleep(7000)
				
		at HotelSearchResultsPage
		
		waitTillAddToItineraryBtnLoads()
		
		scrollToAddToItineraryBtn()
		
		clickAddToitinerary(0)
		sleep(4000)
		at ItineraryTravllerDetailsPage
		selectOptionFromManageItinerary(data.input.manageItinryValue)
		sleep(3000)

		//commenting below lines since itinerary builder is removed in 10.3
		at HotelSearchResultsPage
		if(getItineraryBarSectionDisplayStatus()==false) {
			at ItenaryBuilderPage
			//Expand
			hideItenaryBuilder()
			sleep(3000)
		}

		at HotelSearchResultsPage
		//Capture items added to itinerary builder
		resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder=getCountOfItemsAddedToItineraryBuilder()
		println("$resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder")
	   //capture itinerary builder screen display status
	   resultData.hotel.searchResults.itineraryBuilder.actualitineraryBuilderSectionStatus=getItineraryBuilderSectionDisplayStatus()
	   //println("$resultData.hotel.searchResults.itineraryBuilder.actualitineraryBuilderSectionStatus")
	   at ItenaryBuilderPage
	   
	   //capture new Itinerary reference number
	   String itineraryBuilderTitle = getItenaryBuilderTtile()
	   //println("$itineraryBuilderTitle")
	   List<String> tempList=itineraryBuilderTitle.tokenize(" ")
	   //println("$tempList")
	   String itineraryId=tempList.getAt(0)+tempList.getAt(1)
	   resultData.hotel.searchResults.itineraryBuilder.retitineraryId=itineraryId
	   //println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryId")
	   String itineraryName=tempList.getAt(2)
	   resultData.hotel.searchResults.itineraryBuilder.retitineraryName=itineraryName
	   //println("$resultData.hotel.searchResults.itineraryBuilder.retitineraryName")
	   
	   //Click on Go to Itinerary Link
	   clickOnGotoItenaryButton()
	   sleep(4000)
	   
	   at ItineraryTravllerDetailsPage
	   //capture hotel name in suggested items
	   resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms=getTransferNameInSuggestedItem(0)
	   
	   //Capture Itinerary Page - header 
	   String itineraryPageTitle = getItenaryName()
	   //println("$itineraryPageTitle")
	   List<String> tmpList=itineraryPageTitle.tokenize(" ")
	   //println("$tmpList")
	   String itinryId=tmpList.getAt(0)+tmpList.getAt(1)
	   resultData.hotel.itineraryPage.actualItineraryId=itinryId
	   //println("$resultData.hotel.itineraryPage.actualItineraryId")
	   String itinaryName=tmpList.getAt(2)
	   resultData.hotel.itineraryPage.actualitineraryName=itinaryName.replaceAll("\nEdit", "")
	   
	   //click on Edit
	   clickEditIconNextToItineraryHeader()
	   sleep(5000)
	   
	   //capture itinerary field name is editable
	    resultData.hotel.itineraryPage.actualEditStatus=getItineraryNameEditableStatus()
	   
	   //capture save button display status
	   //resultData.hotel.itineraryPage.actualSaveBtnDispStatus=getSaveButtondisplayStatus()
		resultData.hotel.itineraryPage.actualSaveBtnDispStatus=getSaveOrCancelButtondisplayStatus(0)
	   //Update Name Text
	  String todaysDate= dateUtil.addDaysChangeDatetformat(0, "ddMMMyy")
	  println("$todaysDate")
	  resultData.hotel.itineraryPage.expectedItnrName=todaysDate
	  enterItenaryName(todaysDate)
	  
	  resultData.hotel.itineraryPage.actualItnrEnteredName=getItenaryname()

		//commented below method since page object changed in 10.3
		//clickSaveButton()
		clickOnSaveOrCancelBtnsEditItinry(0)
		sleep(4000)
		waitTillLoadingIconDisappears()
	  //capture entered Itinerary Name
	  //Capture Itinerary Page - header
	  String edtditineraryPageTitle = getItenaryName()
	  //println("$itineraryPageTitle")
	  List<String> tList=edtditineraryPageTitle.tokenize(" ")
	  String edtitinaryName=tList.getAt(2)
	  resultData.hotel.itineraryPage.actualSavedItnrName=edtitinaryName.replaceAll("\nEdit", "")
	 
	  //capture created date
	  String createdDate= dateUtil.addDaysChangeDatetformat(0, "dd/MM/yy")
	  resultData.hotel.itineraryPage.expectedCreatedDate="Created "+createdDate
	  String tripHeaderSubtitle=getTripHeaderSubtitle()
	  List<String> tempheadertitle=tripHeaderSubtitle.tokenize(" ")
	  println("$tempheadertitle")
	  String actdate=tempheadertitle.getAt(0)+" "+tempheadertitle.getAt(1)
	  resultData.hotel.itineraryPage.actualCreatedDate=actdate
	  println("$resultData.hotel.itineraryPage.actualCreatedDate")
	  //String createdBy=tempheadertitle.getAt(3)+" "+tempheadertitle.getAt(4)+" "+tempheadertitle.getAt(5)+" "+tempheadertitle.getAt(6)
	  String createdBy=tempheadertitle.getAt(3)+" "+tempheadertitle.getAt(4)+" "+tempheadertitle.getAt(5)+" "+tempheadertitle.getAt(6)
	  resultData.hotel.itineraryPage.actualCreatedBy=createdBy.toLowerCase()
	  println("Actual: $resultData.hotel.itineraryPage.actualCreatedBy")
	  
	  //String tmpCreatedBy="by  $clientData.usernameOrEmail"
		String tmpCreatedBy=getCreatedBy()
		resultData.hotel.itineraryPage.expectedCreatedBy=tmpCreatedBy.toLowerCase()+":"
	  println("Expected: $resultData.hotel.itineraryPage.expectedCreatedBy")
	  
	  //Capture Share Itinerary button displayed
	   resultData.hotel.itineraryPage.actualShareItnrBtnDispStatus=verifyShareItenaryDisplayed()
	   
	   //Input Title
	   selectTitle(data.expected.title_txt,0)
		   //Input First Name
	   enterFirstName(data.expected.firstName,0)
		   //Input Last Name
	   enterLastName(data.expected.lastName,0)
		   //Input Email Address
	   enterEmail(data.expected.emailAddr,0)
		   //Input Area Code
	   //enterCountryCode(data.expected.countryCode,0)
	   //sleep(1000)
		   //Input Telephone number
	   enterTelephoneNumber(data.expected.telephone_Num,0)
	   sleep(1000)
		  
	   //Remove before save
	   		//Click Add Travellers button
	   		//clickAddTravellersButton()
			//Enter Traveller Details
			 sleep(2000)
			 //String scndTrvlrfirstName=data.expected.firstName+"2"
			 //String scndTrvlrlastName=data.expected.lastName+"2"
		String scndTrvlrfirstName=data.expected.firstName+"Second"
		String scndTrvlrlastName=data.expected.lastName+"Second"
		    fillTravellerDetailsNotSaved(data.expected.title_txt,scndTrvlrfirstName,scndTrvlrlastName,1)
			sleep(2000)
	   		 //click Remove button
			clickRemoveButton(0)
		sleep(2000)
		clickRemoveButton(0)
		sleep(2000)
		clickRemoveButton(0)

		sleep(2000)
		//Click on Save Button
		//clickonSaveButton()
		clickonSaveButton(0)
		sleep(3000)

		waitTillLoadingIconDisappears()
		sleep(4000)
		
	//Capture lead traveller details 
		resultData.hotel.itineraryPage.expectedleadTravellerName=data.expected.title_txt+" "+data.expected.firstName+" "+data.expected.lastName
		resultData.hotel.itineraryPage.actualLeadTravellerName=getLeadTravellerName(0)
		
	//Capture lead traveller - telephone number
		resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum=data.expected.defaultcountryCode+""+data.expected.telephone_Num
		resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum=getLeadTravellerName(1)
	
	//Caputre lead traveller - email address
		resultData.hotel.itineraryPage.expectedleadTravellerEmail=data.expected.emailAddr
		resultData.hotel.itineraryPage.actualLeadTravellerEmail=getLeadTravellerName(2)
		
	    //Add 2nd Traveller details and click on save
	   enterAdultTravellerDetails(data.expected.title_txt, scndTrvlrfirstName , scndTrvlrlastName,0)
	   sleep(4000)
   //Capture 2nd traveller details
	   resultData.hotel.itineraryPage.expectedscndTravellerName=data.expected.title_txt+" "+scndTrvlrfirstName+" "+scndTrvlrlastName
	   resultData.hotel.itineraryPage.actualscndTravellerName=getLeadTravellerName(3)
	 
	     //Add 3rd Traveller Details - child traveller
	   //String thirdTrvlrfirstName=data.expected.childFirstName+"3"
	   //String thirdTrvlrlastName=data.expected.childLastName+"3"
		String thirdTrvlrfirstName=data.expected.childFirstName+"Third"
		String thirdTrvlrlastName=data.expected.childLastName+"Third"
	   String firstChildAge=data.expected.childAgeFirst
	   enterChildTravellerDetails(data.expected.title_txt,thirdTrvlrfirstName,thirdTrvlrlastName,firstChildAge,0)
	   sleep(4000)
  //Capture 3rd traveller details
	  
	   resultData.hotel.itineraryPage.expectedthirdTravellerName=thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"
	   println("$resultData.hotel.itineraryPage.expectedthirdTravellerName")
	   resultData.hotel.itineraryPage.actualthirdTravellerName=getLeadTravellerName(4)
	   
	   //Add 4th Traveller Details - child traveller
	   //String fourthTrvlrfirstName=data.expected.childFirstName+"4"
	   //String fourthTrvlrlastName=data.expected.childLastName+"4"
		String fourthTrvlrfirstName=data.expected.childFirstName+"Fourth"
		String fourthTrvlrlastName=data.expected.childLastName+"Fourth"
	   String secondChildAge=data.expected.childAgeSecond
	   enterChildTravellerDetails(data.expected.title_txt,fourthTrvlrfirstName,fourthTrvlrlastName,secondChildAge,0)
	   sleep(2000)
   //Capture 4th traveller details
	   
		resultData.hotel.itineraryPage.expectedfourthTravellerName=fourthTrvlrfirstName+" "+fourthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
		println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
		resultData.hotel.itineraryPage.actualfourthTravellerName=getLeadTravellerName(5)
	   
	   
	    //Add 5th Traveller Details
	   //String fifthTrvlrfirstName=data.expected.firstName+"5"
	   //String fifthTrvlrlastName=data.expected.lastName+"5"
		String fifthTrvlrfirstName=data.expected.firstName+"Fifth"
		String fifthTrvlrlastName=data.expected.lastName+"Fifth"
	   enterAdultTravellerDetails(data.expected.title_txt, fifthTrvlrfirstName , fifthTrvlrlastName, 0)
	   sleep(2000)
	   //clickOnExpandTraveller()
	   //sleep(2000)
	//Capture 5th traveller details
	   resultData.hotel.itineraryPage.expectedfifthTravellerName=data.expected.title_txt+" "+fifthTrvlrfirstName+" "+fifthTrvlrlastName
	   resultData.hotel.itineraryPage.actualfifthTravellerName=getLeadTravellerName(6)
	   
	    //Add 6th Traveller Details
	   //String sixthTrvlrfirstName=data.expected.childFirstName+"6"
	   //String sixthTrvlrlastName=data.expected.childLastName+"6"
		String sixthTrvlrfirstName=data.expected.childFirstName+"Sixth"
		String sixthTrvlrlastName=data.expected.childLastName+"Sixth"
	   enterChildTravellerDetails(data.expected.title_txt,sixthTrvlrfirstName,sixthTrvlrlastName,secondChildAge,0)
	   sleep(2000)
	   //clickOnExpandTraveller()
	   //sleep(2000)
	//Capture 6th traveller details	   
		resultData.hotel.itineraryPage.expectedsixthTravellerName=sixthTrvlrfirstName+" "+sixthTrvlrlastName+" "+"("+secondChildAge+"yrs)"
		println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
		resultData.hotel.itineraryPage.actualsixthTravellerName=getLeadTravellerName(7)
	   
	 //Add 7th Traveller Details
	   //String seventhTrvlrfirstName=data.expected.childFirstName+"7"
	   //String seventhTrvlrlastName=data.expected.childLastName+"7"
		String seventhTrvlrfirstName=data.expected.childFirstName+"Seventh"
		String seventhTrvlrlastName=data.expected.childLastName+"Seventh"
	   enterChildTravellerDetails(data.expected.title_txt,seventhTrvlrfirstName,seventhTrvlrlastName,secondChildAge,0)
	   sleep(2000)
	   //clickOnExpandTraveller()
	   //sleep(2000)
	   //Capture 7th traveller details
	   resultData.hotel.itineraryPage.expectedsevnthTravellerName=seventhTrvlrfirstName+" "+seventhTrvlrlastName+" "+"("+secondChildAge+"yrs)"
	   println("$resultData.hotel.itineraryPage.expectedfourthTravellerName")
	   resultData.hotel.itineraryPage.actualsevthTravellerName=getLeadTravellerName(8)
	   
	   //Add 8th Traveller Details
	   //String eigthTrvlrfirstName=data.expected.firstName+"8"
	   //String eigthTrvlrlastName=data.expected.lastName+"8"
		String eigthTrvlrfirstName=data.expected.firstName+"Eighth"
		String eigthTrvlrlastName=data.expected.lastName+"Eighth"
	   enterAdultTravellerDetails(data.expected.title_txt, eigthTrvlrfirstName , eigthTrvlrlastName, 0)
	   sleep(4000)
	   //clickOnExpandTraveller()
	   //sleep(2000)
	 //Capture 8th traveller details
	   resultData.hotel.itineraryPage.expectedeigthTravellerName=data.expected.title_txt+" "+eigthTrvlrfirstName+" "+eigthTrvlrlastName
	   resultData.hotel.itineraryPage.actualeigthTravellerName=getLeadTravellerName(9)
	   //Add 9th Traveller Details
	   //String ninthTrvlrfirstName=data.expected.firstName+"9"
	   //String ninthTrvlrlastName=data.expected.lastName+"9"
		String ninthTrvlrfirstName=data.expected.firstName+"Ninth"
		String ninthTrvlrlastName=data.expected.lastName+"Ninth"
	   enterAdultTravellerDetails(data.expected.title_txt, ninthTrvlrfirstName , ninthTrvlrlastName, 0)
	   sleep(2000)
	   //clickOnExpandTraveller()
	   //sleep(2000)
	 //Capture 9th traveller details
	   resultData.hotel.itineraryPage.expectedninthTravellerName=data.expected.title_txt+" "+ninthTrvlrfirstName+" "+ninthTrvlrlastName
	   resultData.hotel.itineraryPage.actualninthTravellerName=getLeadTravellerName(10)
	   sleep(1000)
	   //click Edit function
	   	//click Edit button for first traveller
	   scrollToTopOfThePage()
	   clickSpecificTravellerEditButton(0)
	   
	   //Input Modified Title
	   selectTitle(data.expected.modified_title_txt,0)
		   //Input Modified First Name
	   enterFirstName(data.expected.modified_firstName,0)
		   //Input Modified Last Name
	   enterLastName(data.expected.modified_lastName,0)
		   //Delete Email Address
	   clearEmail(0)
		   //Input default Area Code
	   //entermodifiedCountryCode(data.expected.defaultcountryCode,0)
	   //sleep(1000)
		   //Delete Telephone number
	   clearTelephoneNumber(0)
	   sleep(1000)
	  	   
	   //Click on Save Button
	   //clickonSaveButton()
	   //sleep(2000)
		clickOnEditSaveOrCancelButton(1)
		sleep(3000)

		waitTillLoadingIconDisappears()
		sleep(4000)
	   
	   //Capture First Traveller Modified Details
	   //Capture lead traveller details - modified
	   resultData.hotel.itineraryPage.expectedmodifiedleadTravellerName=data.expected.modified_title_txt+" "+data.expected.modified_firstName+" "+data.expected.modified_lastName
	   resultData.hotel.itineraryPage.actualmodifiedLeadTravellerName=getLeadTravellerName(0)
	   
	   //Capture lead traveller - telephone number - modified
	   resultData.hotel.itineraryPage.expectedmodifiedleadTravellerPhoneNum=""
	   resultData.hotel.itineraryPage.actualLeadTravellermodifiedPhoneNum=getLeadTravellerName(1)
   
       //Caputre lead traveller - email address - modified
	   resultData.hotel.itineraryPage.expectedmodifiedleadTravellerEmail=""
	   resultData.hotel.itineraryPage.actualLeadTravellermodifiedEmail=getLeadTravellerName(2)
	   
	   //Edit First Traveller Details
	   scrollToTopOfThePage()
	   clickSpecificTravellerEditButton(0)
	   
	   //String firstTrvlrfirstName=data.expected.modified_firstName+"1"
	   //String firstTrvlrlastName=data.expected.modified_lastName+"1"

		String firstTrvlrfirstName=data.expected.modified_firstName+"First"
		String firstTrvlrlastName=data.expected.modified_lastName+"First"

		//Input Title
	   selectTitle(data.expected.title_txt,0)
		   //Input First Name
	   enterFirstName(firstTrvlrfirstName,0)
		   //Input Last Name
	   enterLastName(firstTrvlrlastName,0)
		   //Input Email Address
	   enterEmail(data.expected.modified_emailAddr,0)
		   //Input Area Code
	   //entermodifiedCountryCode(data.expected.countryCode,0)
	   //sleep(1000)
		   //Input Telephone number
	   enterTelephoneNumber(data.expected.modified_mobileNum,0)
	   sleep(1000)
		
	   //Click on Save Button
		clickOnEditSaveOrCancelButton(1)
	   //clickonSaveButton()
	   sleep(2000)
	   
	   //Capture lead traveller details - modified
	   resultData.hotel.itineraryPage.expectedAmmendleadTravellerName=data.expected.title_txt+" "+firstTrvlrfirstName+" "+firstTrvlrlastName
	   resultData.hotel.itineraryPage.actualAmmendLeadTravellerName=getLeadTravellerName(0)
	   
	   //Capture lead traveller - telephone number - modified
	   resultData.hotel.itineraryPage.expectedAmmendleadTravellerPhoneNum=data.expected.defaultcountryCode+""+data.expected.modified_mobileNum
	   resultData.hotel.itineraryPage.actualLeadTravellerAmmendPhoneNum=getLeadTravellerName(1)
   
	   //Caputre lead traveller - email address - modified
	   resultData.hotel.itineraryPage.expectedAmmendleadTravellerEmail=data.expected.modified_emailAddr
	   resultData.hotel.itineraryPage.actualLeadTravellerAmmendEmail=getLeadTravellerName(2)
	   
	   
	   //Amend Child Age
	   clickSpecificTravellerEditButton(3)
	   sleep(2000)
	   //String fourthTrvlrmodifiedfirstName=data.expected.modified_childFirstName+"4"
	   //String fourthTrvlrmodifiedlastName=data.expected.modified_childLastName+"4"
		String fourthTrvlrmodifiedfirstName=data.expected.modified_childFirstName+"Fourth"
		String fourthTrvlrmodifiedlastName=data.expected.modified_childLastName+"Fourth"
	   String modifiedChildAge=data.expected.modified_age
	   //modifyChildTravellerDetails(data.expected.modified_childTitle,fourthTrvlrmodifiedfirstName,fourthTrvlrmodifiedlastName,modifiedChildAge,0)
		enterFirstName(fourthTrvlrmodifiedfirstName, 0)
		enterLastName(fourthTrvlrmodifiedlastName, 0)
		sleep(5000)
		clickAndModifyAge(modifiedChildAge)
		//Click on Save Button
		clickOnEditSaveOrCancelButton(1)
		//clickonSaveButton()
		sleep(5000)


	   //clickOnExpandTraveller()
	   //sleep(2000)
	   
	   //Capture 4th traveller details
	   
		resultData.hotel.itineraryPage.expectedmodifiedfourthTravellerName=fourthTrvlrmodifiedfirstName+" "+fourthTrvlrmodifiedlastName+" "+"("+modifiedChildAge+"yrs)"
		resultData.hotel.itineraryPage.actualmodifiedfourthTravellerName=getLeadTravellerName(5)
	   
		scrollToTopOfThePage()
		sleep(3000)
		//Edit 4th traveller - change child to adult
		clickSpecificTravellerEditButton(3)
		sleep(3000)
		//String fourthTrvlrchangefirstName=data.expected.modified_firstName+"4"
		//String fourthTrvlrchangelastName=data.expected.modified_lastName+"4"
		String fourthTrvlrchangefirstName=data.expected.modified_firstName+"Fourth"
		String fourthTrvlrchangelastName=data.expected.modified_lastName+"Fourth"
		//changeChildTravellerToAdult(data.expected.title_txt,fourthTrvlrchangefirstName,fourthTrvlrchangelastName,0)
		ClickRadioButtonAdultOrChild(1)
		sleep(3000)
		fillTravellerDetailsNotSaved(data.expected.title_txt,fourthTrvlrchangefirstName,fourthTrvlrchangelastName,0)


		sleep(2000)
		//capture Age Text Box displayed or not
		resultData.hotel.itineraryPage.actualAgeTxtBoxDispStatus=getAgeTxtBoxdisplayStatus()
		println("$resultData.hotel.itineraryPage.actualAgeTxtBoxDispStatus")
		clickOnEditSaveOrCancelButton(1)
		//clickonSaveButton()
		sleep(2000)
		
		//Capture 4th Traveller Details
			
		 resultData.hotel.itineraryPage.expectedchangefourthTravellerName=data.expected.title_txt+" "+fourthTrvlrchangefirstName+" "+fourthTrvlrchangelastName
		 resultData.hotel.itineraryPage.actualchangefourthTravellerName=getLeadTravellerName(5)
		 //clickOnExpandTraveller()
		 sleep(2000)
		 scrollToBottomOfThePage()
		 sleep(3000)
		//Edit 9th traveller
		 clickSpecificTravellerEditButton(8)
		 sleep(5000)
		 //String ninthTrvlrmodifiedfirstName=data.expected.modified_childFirstName+"9"
		 //String ninthTrvlrmodifiedlastName=data.expected.modified_childLastName+"9"
		String ninthTrvlrmodifiedfirstName=data.expected.modified_childFirstName+"Ninth"
		String ninthTrvlrmodifiedlastName=data.expected.modified_childLastName+"Ninth"
		 modifiedChildAge=data.expected.modified_age
		try{
			changeAdultTravellerToChild(data.expected.modified_childTitle,ninthTrvlrmodifiedfirstName,ninthTrvlrmodifiedlastName,modifiedChildAge,0)
			sleep(3000)
		}catch(Exception e){
			softAssert.assertFalse(true,"Failed To Change Adult Traveller to Child Traveller")
			clickOnEditSaveOrCancelButton(1)
		}

		 //clickOnExpandTraveller()
		 //sleep(3000)
		 //Capture 9th traveller details
		 
		  resultData.hotel.itineraryPage.expectedmodifiedninthTravellerName=ninthTrvlrmodifiedfirstName+" "+ninthTrvlrmodifiedlastName+" "+"("+modifiedChildAge+"yrs)"
		  resultData.hotel.itineraryPage.actualmodifiedninthTravellerName=getLeadTravellerName(10)
		 
		 //Click on Book Item - in Suggested Items
		  //CLICK ON BOOK
		  clickBookButton(0)
		  sleep(5000)
		  
		  //Select the check boxes for 1,2,3 & 6th traveller (2 Adult & 2 childeren)
		  clickOnTravellerTandCBookingLightBox(0)
		  sleep(3000)
		  clickOnTravellerTandCBookingLightBox(1)
		  sleep(3000)
		  clickOnTravellerTandCBookingLightBox(2)
		  sleep(3000)
		  clickOnTravellerTandCBookingLightBox(5)
		  sleep(3000)
		  
		  //Click on Confirm Booking
		  clickConfirmBooking()
			sleep(4000)
		  
		  //wait for confirmation page
		  waitTillConformationPage()
		  sleep(5000)
		  
		  //capture traveller details
		  resultData.hotel.confirmationPage.actualfirstTravellerName=getTravellerNamesConfirmationDialog(0)
		  resultData.hotel.confirmationPage.actualsecondTravellerName=getTravellerNamesConfirmationDialog(1)
		  resultData.hotel.confirmationPage.actualthirdTravellerName=getTravellerNamesConfirmationDialog(2)
		  resultData.hotel.confirmationPage.actualfourthTravellerName=getTravellerNamesConfirmationDialog(3)
		  
		   //Capture close edit icon presence
		  //Click on Close button in Booking confirmed window
		  coseBookItenary()
		  sleep(2000)
	 
		  //Lead name change after booking
		  scrollToTopOfThePage()
		  sleep(3000)
		  clickSpecificTravellerEditButton(0)
		   //Input First Name
		  enterFirstName(data.expected.modified_firstName,0)
			  //Input Last Name
		  enterLastName(data.expected.modified_lastName,0)
			  
		  //Click on Save Button
		clickOnEditSaveOrCancelButton(1)
		  //clickonSaveButton()
		  sleep(2000)
		  	 
		  
		  //Capture Existence of Modified Traveller Details
		  
		  //resultData.hotel.shareItineraryPage.expectedtravellerDetails="Travellers: "+sixthTrvlrfirstName+" "+sixthTrvlrlastName+" "+"("+secondChildAge+"yrs)"+" , "+thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"+" , "+scndTrvlrfirstName+" "+scndTrvlrlastName+", "+data.expected.modified_firstName+" "+data.expected.modified_lastName
		  resultData.hotel.shareItineraryPage.actualtravellerDetailsPresence=getExistenceOfModifiedAdultNameInTravlrDetails(data.expected.modified_firstName,data.expected.modified_lastName)
	  
		    
		at ItenaryPageItems
		
		scrollToTopOfThePage()
		clickSpecificTravellerEditButton(2)
		sleep(2000)
		//String modifiedchildfirstName=data.expected.modified_childFirstName+"3"
		//String modifiedchildrlastName=data.expected.modified_childLastName+"3"
		String modifiedchildfirstName=data.expected.modified_childFirstName+"Third"
		String modifiedchildrlastName=data.expected.modified_childLastName+"Third"
		
		//modifyChildTravellerDetails(data.expected.modified_childTitle,modifiedchildfirstName,modifiedchildrlastName,modifiedChildAge,0)
		enterFirstName(modifiedchildfirstName, 0)
		enterLastName(modifiedchildrlastName, 0)
		sleep(3000)
		clickAndModifyAge(modifiedChildAge)
		//Click on Save Button
		//clickonSaveButton()
		clickOnEditSaveOrCancelButton(1)
		sleep(5000)
		
		//close the popup
		coseBookItenary()
		
		resultData.hotel.shareItineraryPage.actualeditedchildagetravellerDetails=getExistenceOfModifiedChildNameInTravlrDetails(modifiedchildfirstName,modifiedchildrlastName,modifiedChildAge)


		scrollToTopOfThePage()
		//clickSpecificTravellerEditButton(2)
		clickSpecificTravellerEditButton(3)
		sleep(2000)
		fourthTrvlrmodifiedfirstName=data.expected.modified_childFirstName
		fourthTrvlrmodifiedlastName=data.expected.modified_childLastName
		
		//modifyChildTravellerDetails(data.expected.modified_childTitle,fourthTrvlrmodifiedfirstName,fourthTrvlrmodifiedlastName,firstChildAge,0)
		try {
			ClickRadioButtonAdultOrChild(2)
			enterFirstName(fourthTrvlrmodifiedfirstName, 0)
			enterLastName(fourthTrvlrmodifiedlastName, 0)
			sleep(3000)
			clickAndModifyAge(firstChildAge)
			sleep(4000)
			//Click on Save Button
			//clickonSaveButton()
			clickOnEditSaveOrCancelButton(1)
			sleep(5000)
		}catch(Exception e){
			softAssert.assertFalse(true,"Failed To convert adult to child and Save Child traveller after booking")
			clickOnEditSaveOrCancelButton(1)
		}

		
		//Validate Traveller Details
		
		//resultData.hotel.shareItineraryPage.expectededitedchildnametravellerDetails="Travellers: "+sixthTrvlrfirstName+" "+sixthTrvlrlastName+" "+"("+secondChildAge+"yrs)"+" , "+thirdTrvlrfirstName+" "+thirdTrvlrlastName+" "+"("+firstChildAge+"yrs)"+" , "+scndTrvlrfirstName+" "+scndTrvlrlastName+", "+data.expected.modified_firstName+" "+data.expected.modified_lastName
		resultData.hotel.shareItineraryPage.actualeditedchildnametravellerDetails=getExistenceOfModifiedChildNameInTravlrDetails(fourthTrvlrmodifiedfirstName,fourthTrvlrmodifiedlastName,firstChildAge)
		scrollToTopOfThePage()
		
		//remove 2nd traveller
		clickItenaryRemoveButton(0)
		sleep(5000)
		resultData.hotel.shareItineraryPage.actualHeadetText=getTravellerCannotBeDeletedheaderText()
		resultData.hotel.shareItineraryPage.actualInsideText=getTravellerCannotBeDeletedinsideText()
		//close the popup
		coseBookItenary()
		sleep(3000)
		
		//change child to adult - 3rd traveller
		scrollToTopOfThePage()
		clickSpecificTravellerEditButton(2)
		sleep(6000)
		
		//String thirdTrvlrchangefirstName=data.expected.modified_firstName+"3"
		//String thirdTrvlrchangelastName=data.expected.modified_lastName+"3"
		String thirdTrvlrchangefirstName=data.expected.modified_firstName+"Third"
		String thirdTrvlrchangelastName=data.expected.modified_lastName+"Third"
		//changeChildTravellerToAdult(data.expected.title_txt,thirdTrvlrchangefirstName,thirdTrvlrchangelastName,0)
		ClickRadioButtonAdultOrChild(1)
		sleep(3000)
		fillTravellerDetailsNotSaved(data.expected.title_txt,thirdTrvlrchangefirstName,thirdTrvlrchangelastName,0)
		sleep(2000)

		//clickonSaveButton()
		clickOnEditSaveOrCancelButton(1)
		sleep(4000)

		//close the popup
		coseBookItenary()
		sleep(4000)
		resultData.hotel.shareItineraryPage.actualeditedchildNameToAdulttravellerDetails=getExistenceOfModifiedAdultNameInTravlrDetails(thirdTrvlrchangefirstName,thirdTrvlrchangelastName)
		
		//change adult to child - 2nd traveller
		scrollToTopOfThePage()
		sleep(4000)
		clickSpecificTravellerEditButton(1)
		sleep(3000)
		
		//String secondTrvlrmodifiedfirstName=data.expected.modified_childFirstName+"2"
		//String secondTrvlrmodifiedlastName=data.expected.modified_childLastName+"2"
		String secondTrvlrmodifiedfirstName=data.expected.modified_childFirstName+"Second"
		String secondTrvlrmodifiedlastName=data.expected.modified_childLastName+"Second"
		modifiedChildAge=data.expected.modified_age
		try {
			changeAdultTravellerToChild(data.expected.modified_childTitle, secondTrvlrmodifiedfirstName, secondTrvlrmodifiedlastName, modifiedChildAge, 0)
			sleep(3000)
		}catch(Exception e){
			softAssert.assertFalse(true,"Failed To convert adult to child after booking")
			clickOnEditSaveOrCancelButton(1)
			sleep(5000)
		}
		//close the popup
		coseBookItenary()
		
		resultData.hotel.shareItineraryPage.actualeditedAdultNameToCahildNametravellerDetails=getExistenceOfModifiedChildNameInTravlrDetails(secondTrvlrmodifiedfirstName,secondTrvlrmodifiedlastName,modifiedChildAge)
		
		//Remove 3rd traveller
		clickItenaryRemoveButton(1)
		sleep(5000)
		resultData.hotel.shareItineraryPage.actualthirdtravelremoveHeadetText=getTravellerCannotBeDeletedheaderText()
		resultData.hotel.shareItineraryPage.actualthirdtravelrremoveInsideText=getTravellerCannotBeDeletedinsideText()
		//close the popup
		coseBookItenary()
		sleep(3000)
		
		
		  //Click on Close Itinerary link
	   clickCloseItineraryButton()
	}
	
	
	@Unroll
	def "02 : Validate Search Results  #resultDat.hotelName"(){
		
		HotelTransferTestResultData resultData = resultDat
		HotelSearchData hotelData=resultData.hotelData
		//HotelTransferTestResultData resultData = resultMap.get(hotelData.hotelName)
		
		when: "Expected and actual Search Results are available"
		
		then: "Assert the Search Results"
		
		//Validate Itinerary builder section display status
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualitineraryBuilderSectionStatus,hotelData.expected.itinerarybulderSectionStatus,"Hotel Search Results Screen Itinerary Builder Section display status Actual & Expected don't match")
		//Validate Itinerary builder items added count
		assertionEquals(resultData.hotel.searchResults.itineraryBuilder.actualCountOfItemsItineraryBuilder,hotelData.expected.itinerarybulderSectionCount,"Hotel Search Results Screen Itinerary Builder Section Items count added Actual & Expected don't match")
		

		
		where :
		resultDat << resultMap.values()
	}

	@Unroll
	def "03 : Validate Itinerary Page Actual & Expected  #resultDat.hotelName"(){

		HotelTransferTestResultData resultData = resultDat
		HotelSearchData hotelData=resultData.hotelData
		//HotelTransferTestResultData resultData = resultMap.get(hotelData.hotelName)

		when: "Expected and actual Search Results are available"

		then: "Assert the Search Results"


		//Validate Itinerary page displayed with selected item
		assertionEquals(resultData.hotel.itineraryPage.actualHotelNameInSuggestedItms,hotelData.expected.cityAreaHotelText,"Itinerary Page, Suggested Items hotel name Actual & Expected don't match")

		//Validate Itinerary reference number
		assertionEquals(resultData.hotel.itineraryPage.actualItineraryId,resultData.hotel.searchResults.itineraryBuilder.retitineraryId,"Itinerary Page, Itinerary Id Actual & Expected don't match")
		//Validate Itinerary Name
		assertionEquals(resultData.hotel.itineraryPage.actualitineraryName, resultData.hotel.searchResults.itineraryBuilder.retitineraryName,"Itinerary Page, Itinerary Name Actual & Expected don't match")
		//Validate Edit text box status

		assertionEquals(resultData.hotel.itineraryPage.actualEditStatus,hotelData.expected.itinerarybulderSectionStatus,"Itinerary Page, Text Box Enabled status Actual & Expected don't match")
		//Validate Save button display status
		assertionEquals(resultData.hotel.itineraryPage.actualSaveBtnDispStatus,hotelData.expected.itinerarybulderSectionStatus,"Itinerary Page, Save Button Display status Actual & Expected don't match")

		//Validate entered name
		assertionEquals(resultData.hotel.itineraryPage.actualItnrEnteredName,resultData.hotel.itineraryPage.expectedItnrName,"Itinerary page, Itinerary Name actual & expected don't match")

		//Validate saved itinerary name
		assertionEquals(resultData.hotel.itineraryPage.actualSavedItnrName,resultData.hotel.itineraryPage.expectedItnrName,"Itinerary page, Saved Itinerary Name actual & expected don't match")

		//Validate Created Date
		assertionEquals(resultData.hotel.itineraryPage.actualCreatedDate, resultData.hotel.itineraryPage.expectedCreatedDate,"Itinerary page, Created Date actual & expected don't match")

		//Validate Created by
		assertionEquals(resultData.hotel.itineraryPage.actualCreatedBy,resultData.hotel.itineraryPage.expectedCreatedBy,"Itinerary page, Created By actual & expected don't match")



		where :
		resultDat << resultMap.values()
	}

	@Unroll
	def "04 : Validate Share Itinerary Page  #resultDat.hotelName"(){

		HotelTransferTestResultData resultData = resultDat
		HotelSearchData hotelData=resultData.hotelData
		//HotelTransferTestResultData resultData = resultMap.get(hotelData.hotelName)

		when: "Expected and actual Search Results are available"

		then: "Assert the Search Results"


		//Validate Share Itinerary Button display
		assertionEquals(resultData.hotel.itineraryPage.actualShareItnrBtnDispStatus,hotelData.expected.itinerarybulderSectionStatus,"Itinerary page, Share Itinerary Button Display Status actual & expected don't match")

		//Validate Lead Traveller Name - 1st Traveller
		assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerName,resultData.hotel.itineraryPage.expectedleadTravellerName,"Itinerary page, Traveller Details - First or Lead Traveller Name Details actual & expected don't match")

		//Validate Lead Traveller - 1st Traveller - Telephone Number
		assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerPhoneNum,resultData.hotel.itineraryPage.expectedleadTravellerPhoneNum,"Itinerary page, Traveller Details - First or Lead Traveller Telephone Number Details actual & expected don't match")

		//Validate Lead Traveller - 1st Traveller - Email Address
		assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerEmail,resultData.hotel.itineraryPage.expectedleadTravellerEmail,"Itinerary page, Traveller Details - First or Lead Traveller Email Address Details actual & expected don't match")

		//Validate 2nd Traveller Details
		assertionEquals(resultData.hotel.itineraryPage.actualscndTravellerName,resultData.hotel.itineraryPage.expectedscndTravellerName,"Itinerary page, Traveller Details - Second Traveller Details actual & expected don't match")

		//Validate 3rd Traveller Details
		assertionEquals(resultData.hotel.itineraryPage.actualthirdTravellerName,resultData.hotel.itineraryPage.expectedthirdTravellerName,"Itinerary page, Traveller Details - Third Traveller Details actual & expected don't match")

		//Validate 4th Traveller Details
		assertionEquals(resultData.hotel.itineraryPage.actualfourthTravellerName,resultData.hotel.itineraryPage.expectedfourthTravellerName,"Itinerary page, Traveller Details - Fourth Traveller Details actual & expected don't match")

		//Validate 5th Traveller Details
		assertionEquals(resultData.hotel.itineraryPage.actualfifthTravellerName,resultData.hotel.itineraryPage.expectedfifthTravellerName,"Itinerary page, Traveller Details - Fifth Traveller Details actual & expected don't match")

		//Validate 6th Traveller Details
		assertionEquals(resultData.hotel.itineraryPage.actualsixthTravellerName,resultData.hotel.itineraryPage.expectedsixthTravellerName,"Itinerary page, Traveller Details - Sixth Traveller Details actual & expected don't match")

		//Validate 7th Traveller Details
		assertionEquals(resultData.hotel.itineraryPage.actualsevthTravellerName,resultData.hotel.itineraryPage.expectedsevnthTravellerName,"Itinerary page, Traveller Details - Seventh Traveller Details actual & expected don't match")

		//Validate 8th Traveller Details
		assertionEquals(resultData.hotel.itineraryPage.actualeigthTravellerName,resultData.hotel.itineraryPage.expectedeigthTravellerName,"Itinerary page, Traveller Details - Eighth Traveller Details actual & expected don't match")

		//Validate 9th Traveller Details
		assertionEquals( resultData.hotel.itineraryPage.actualninthTravellerName,resultData.hotel.itineraryPage.expectedninthTravellerName,"Itinerary page, Traveller Details - Ninth Traveller Details actual & expected don't match")


		//Validate Lead Traveller modified name
		assertionEquals(resultData.hotel.itineraryPage.actualmodifiedLeadTravellerName,resultData.hotel.itineraryPage.expectedmodifiedleadTravellerName,"Itinerary page, Traveller Details - Modified First Traveller Name Details actual & expected don't match")

		//Validate Lead Traveller modified telephone number
		assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellermodifiedPhoneNum,resultData.hotel.itineraryPage.expectedmodifiedleadTravellerPhoneNum,"Itinerary page, Traveller Details - Modified First Traveller Telephone Number Details actual & expected don't match")

		//Validate Lead Traveller modified Email Address
		assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellermodifiedEmail,resultData.hotel.itineraryPage.expectedmodifiedleadTravellerEmail,"Itinerary page, Traveller Details - Modified First Traveller Email Address Details actual & expected don't match")

		//Validate
		//Validate Lead Traveller modified name - 2nd time
		assertionEquals( resultData.hotel.itineraryPage.actualAmmendLeadTravellerName,resultData.hotel.itineraryPage.expectedAmmendleadTravellerName,"Itinerary page, Traveller Details - 2nd time Modified First Traveller Name Details actual & expected don't match")

		//Validate Lead Traveller modified telephone number - 2nd time
		assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerAmmendPhoneNum,resultData.hotel.itineraryPage.expectedAmmendleadTravellerPhoneNum,"Itinerary page, Traveller Details - 2nd time Modified First Traveller Telephone Number Details actual & expected don't match")

		//Validate Lead Traveller modified Email Address - 2nd time
		assertionEquals(resultData.hotel.itineraryPage.actualLeadTravellerAmmendEmail,resultData.hotel.itineraryPage.expectedAmmendleadTravellerEmail,"Itinerary page, Traveller Details - 2nd time Modified First Traveller Email Address Details actual & expected don't match")

		//Validate Fourth Traveller Modified  Details
		assertionEquals(resultData.hotel.itineraryPage.actualmodifiedfourthTravellerName,resultData.hotel.itineraryPage.expectedmodifiedfourthTravellerName,"Itinerary page, Traveller Details - Fourth Traveller Modified  Details actual & expected don't match")

		//Validate Age Text Box display status
		assertionEquals(resultData.hotel.itineraryPage.actualAgeTxtBoxDispStatus,hotelData.expected.notDispStatus,"Itinerary page, Traveller Details - Fourth Traveller Age Text Box Display Status actual & expected don't match")

		//Validate Fourth Traveller Modified Details - 2nd Time
		assertionEquals(resultData.hotel.itineraryPage.actualchangefourthTravellerName,resultData.hotel.itineraryPage.expectedchangefourthTravellerName,"Itinerary page, Traveller Details - 2nd Time Fourth Traveller Details actual & expected don't match")

		//Validate ninth Traveller Modified Details
		assertionEquals(resultData.hotel.itineraryPage.actualmodifiedninthTravellerName,resultData.hotel.itineraryPage.expectedmodifiedninthTravellerName,"Itinerary page, Traveller Details - Ninth Traveller Details actual & expected don't match")

		//Validate confirm booking - first traveller name
		assertionEquals(resultData.hotel.confirmationPage.actualfirstTravellerName,resultData.hotel.itineraryPage.expectedAmmendleadTravellerName,"Confirmation Screen Traveller Details - Lead or First Traveller Expected & Actual Traveller details don't match")

		//Validate confirm booking - second traveller name
		assertionEquals(resultData.hotel.confirmationPage.actualsecondTravellerName,resultData.hotel.itineraryPage.expectedscndTravellerName,"Confirmation Screen Traveller Details - Second Traveller Expected & Actual Traveller details don't match")

		//Validate confirm booking - third traveller name
		assertionEquals(resultData.hotel.confirmationPage.actualthirdTravellerName.replaceAll(" ",""),resultData.hotel.itineraryPage.expectedthirdTravellerName.replaceAll(" ",""),"Confirmation Screen Traveller Details - Third Traveller Expected & Actual Traveller details don't match")

		//Validate confirm booking - fourth traveller name
		assertionEquals(resultData.hotel.confirmationPage.actualfourthTravellerName.replaceAll(" ",""),resultData.hotel.itineraryPage.expectedsixthTravellerName.replaceAll(" ",""),"Confirmation Screen Traveller Details - fourth Traveller Expected & Actual Traveller details don't match")

		//Validate Travellers after lead traveller edit and page refresh
		assertionEquals(resultData.hotel.shareItineraryPage.actualtravellerDetailsPresence,hotelData.expected.itinerarybulderSectionStatus,"Itinerary Screen - Booked Items Traveller Details Lead Traveller Edited Expected & Actual details don't match")

		//Validate Travellers after child traveller name change and page referesh
		assertionEquals(resultData.hotel.shareItineraryPage.actualeditedchildnametravellerDetails,hotelData.expected.itinerarybulderSectionStatus,"Itinerary Screen - Booked Items Child Name Traveller Edited Details Expected & Actual details don't match")

		//Validate Travellers after child age change and page refresh
		assertionEquals(resultData.hotel.shareItineraryPage.actualeditedchildagetravellerDetails,hotelData.expected.itinerarybulderSectionStatus,"Itinerary Screen - Booked Items Child Age Edited Details Expected & Actual details don't match")

		//Validate Header Text - Cannot Delete Traveller popup
		assertionEquals(resultData.hotel.shareItineraryPage.actualHeadetText,hotelData.expected.headerText,"Itinerary Screen - Delete 2nd Travller Popup Header Text Expected & Actual details don't match")

		//Validate Inside Text -
		assertionEquals(resultData.hotel.shareItineraryPage.actualInsideText,hotelData.expected.insideText,"Itinerary Screen - Delete 2nd Travller Popup Inside Text Expected & Actual details don't match")

		//Validate child to Adult - Change
		assertionEquals(resultData.hotel.shareItineraryPage.actualeditedchildNameToAdulttravellerDetails,hotelData.expected.itinerarybulderSectionStatus,"Itinerary Screen - Booked Items Traveller Details (Child to adult)Text Expected & Actual details don't match")

		//Validate adult to child - change
		assertionEquals(resultData.hotel.shareItineraryPage.actualeditedAdultNameToCahildNametravellerDetails,hotelData.expected.itinerarybulderSectionStatus,"Itinerary Screen - Booked Items Traveller Details (Adult to child)Text Expected & Actual details don't match")

		//Validate Header Text - Cannot Delete Traveller popup
		assertionEquals(resultData.hotel.shareItineraryPage.actualthirdtravelremoveHeadetText,hotelData.expected.headerText,"Itinerary Screen - Delete 3rd Travller Popup Header Text Expected & Actual details don't match")

		//Validate Inside Text -
		assertionEquals(resultData.hotel.shareItineraryPage.actualthirdtravelrremoveInsideText,hotelData.expected.insideText,"Itinerary Screen - Delete 3rd Travller Popup Inside Text Expected & Actual details don't match")


		where :
		resultDat << resultMap.values()
	}
}
