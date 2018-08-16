package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.common.util.ExcelUtil
import com.kuoni.qa.automation.util.ConfigProperties
import com.kuoni.qa.automation.util.XMLValidationUtil
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.util.EntityUtils
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.testng.asserts.SoftAssert

import java.text.SimpleDateFormat

class SearchHotelPricePaxHelper {
	ExcelUtil excelUtil
	String sheetName
	Map dataMap
	Map validateMap= new HashMap()
	String ExpectedResult
	SoftAssert softAssert= new SoftAssert()
	XMLValidationUtil XmlValidate = new XMLValidationUtil()
	private static def xmlParser = new XmlParser()
	static final String URL= ConfigProperties.getValue("AuthenticationURL")
	static final def timeout = 10 * 60 * 1000
	static RequestConfig requestTimeoutConfig = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();
	static final def timeoOutMilliSecs = 5 * 1000

	public String getExpectedResult() {
		return ExpectedResult;
	}

    SearchHotelPricePaxHelper(String excelPath){
		excelUtil=new ExcelUtil(excelPath)
		println("-----------------------------------------------------------------------------\n")
		println("Request URL :  ${URL} \n")
		println("-----------------------------------------------------------------------------\n")
		
	}
	

	
	static String postHttpXmlRequestWithHeaderNode(String httpUrl, String contentType, String xmlBody, String testName, String headerName, String headerValue) {
		Node node
	   int timeout = 10_000
	   String responseBody=""
	   RequestConfig requestConfig = RequestConfig.custom()
			   .setSocketTimeout(timeout)
			   .setConnectTimeout(timeout)
			   .setConnectionRequestTimeout(timeout)
			   .build();

	   HttpClient httpClient = HttpClientBuilder.create().build()
//        httpClient

	   HttpResponse response
	   try {
		   HttpPost httpPost = getPostRequest(httpUrl, contentType, xmlBody, headerName, headerValue)
		   httpPost.setConfig(requestConfig)
		   response = httpClient.execute(httpPost)
		   HttpEntity resEntity = response.getEntity()
		   responseBody = EntityUtils.toString(resEntity, "UTF-8")
	   }
		  // println "Response \n$responseBody"
		  // node = xmlParser.parseText(responseBody)
	   
	   finally {
		   httpClient.getConnectionManager().shutdown()
	   }
	   return responseBody
   }

	static HttpPost getPostRequest(String httpUrl, String contentType, String xmlBody, String headerName, String headerValue) {
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(timeoOutMilliSecs)
				.setConnectTimeout(timeoOutMilliSecs).setSocketTimeout(timeoOutMilliSecs)
				.build()
		new HttpGet(httpUrl)
		HttpPost httpPost = new HttpPost(httpUrl)
		httpPost.setHeader("Content-Type", "")
		httpPost.setHeader(headerName, headerValue)
		httpPost.setConfig(defaultRequestConfig)
		HttpEntity reqEntity = new StringEntity(xmlBody, "UTF-8")
		reqEntity.setContentType(contentType)
		reqEntity.setChunked(true)
		httpPost.setEntity(reqEntity)
		return httpPost
	}
	/**Method to post https request with header*/
	def postHttpsXMLRequestWithHeaderNode(String httpsUrl,String contentType, String xmlBody, String testName, String headerName, String headerValue)
	{

	Node node
	
	   def timeout = 10 * 60 * 1000
	   RequestConfig requestTimeoutConfig = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();
	   HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultRequestConfig(requestTimeoutConfig).build();
	   HttpResponse response

	   try {
		   HttpPost httpPost = new HttpPost(httpsUrl)
		   httpPost.setHeader("Content-Type", "")
		   httpPost.setHeader(headerName, headerValue)
		   httpPost.setConfig(requestTimeoutConfig)
		   HttpEntity reqEntity = new StringEntity(xmlBody, "UTF-8")
		   reqEntity.setContentType(contentType)
		   reqEntity.setChunked(true)
		   httpPost.setEntity(reqEntity)
		   response = httpClient.execute(httpPost)
		   HttpEntity resEntity = response.getEntity()
		   String responseBody = EntityUtils.toString(resEntity)
		   println "Response \n$responseBody"
		   node = xmlParser.parseText(responseBody)
	   }
	   finally {
		   httpClient.getConnectionManager().shutdown()
	   }
	   return node
}

	/**
	 * Method for getting data from excel sheet to Map
	 * @return Map dataMap
	 */
	public def getSheetDataAsMap(String sheetName, int rowNum){
		this.sheetName=sheetName
		dataMap = new HashMap<String, String>()
		Sheet sheet= excelUtil.getSheet(sheetName)
		if (!sheet){
			return null;
		}
		Row headerRow=sheet.getRow(0)
		Row row1=sheet.getRow(rowNum)
		short minColIx = headerRow.getFirstCellNum();
		short maxColIx = headerRow.getLastCellNum()-1;
		for(int i in minColIx..maxColIx ){
			Cell key=headerRow.getCell(i)
			Cell value=row1.getCell(i)
			if(key && value){
				dataMap.put(key.getStringCellValue(),value.getStringCellValue())
			}
		}
		setExpectedResult(dataMap.get('ExpectedResult'))
		return dataMap;
	}

	public def validateSuccessResponse(String ResponseBody){

		StringTokenizer st;
		int count = 0;
		String[] expectedName;
		String[] expectedValue;

		st = new StringTokenizer(dataMap.get('ExpectedName'), '|');
		expectedName = new String[st.countTokens()];
		while (st.hasMoreElements()) {
			expectedName[count] = st.nextToken();
			++count;
		}

		st = new StringTokenizer(dataMap.get('ExpectedValue'), '|');
		expectedValue = new String[st.countTokens()];
		count = 0;
		while (st.hasMoreElements()) {
			expectedValue[count] = st.nextToken();
			++count;
		}
		assert expectedName.length==expectedValue.length,"Expected name and expected value are not matching"

		Map validateAttrMultiValue =  new HashMap<String,List>()
		Map validateAttrValue =  new HashMap<String, String>()
		Map validateTagMultiValueMap =  new HashMap<String,List>()
		Map validateTagValueMap =  new HashMap<String,String>()
		Map validateTagValueRangeMap =  new HashMap<String,String>()
		Map validateSingleAttrValue =  new HashMap<String, String>()

		List<String> li;

		for (int i = 0; i < expectedValue.length; i++) {

			if (expectedName[i].contains(",") && expectedValue[i].contains(",")) {
				// Attribute with Multiple value

				li = new ArrayList<String>();
				st = new StringTokenizer(expectedValue[i], ",");
				while (st.hasMoreElements()) {
					li.add(st.nextToken());
				}
				validateAttrMultiValue.put(expectedName[i], li);

			} else if (expectedName[i].contains(",") && !(expectedValue[i].contains(",")) && !(expectedName[i].contains("["))) {
				// Attribute with single value
				validateAttrValue.put(expectedName[i], expectedValue[i]);

			} else if (!(expectedName[i].contains(",")) && expectedValue[i].contains(",")) {
				// Tag with Multiple value

				li = new ArrayList<String>();
				st = new StringTokenizer(expectedValue[i], ",");
				while (st.hasMoreElements()) {
					li.add(st.nextToken());
				}
				validateTagMultiValueMap.put(expectedName[i], li);

			} else if (!(expectedName[i].contains(",")) && !(expectedValue[i].contains(","))) {
				// Tag with Range of values
				if(expectedValue[i].contains("-")){
					validateTagValueRangeMap.put(expectedName[i], expectedValue[i]);
				}
				// Tag with Single value
				else{
					validateTagValueMap.put(expectedName[i], expectedValue[i]);
				}
			}
			else if (expectedName[i].contains(",") && !(expectedValue[i].contains(",")) && expectedName[i].contains("[")) {
				// Attribute with single value and searching for at least one time

				expectedName[i] = expectedName[i].replace("[", "")
				expectedName[i] = expectedName[i].replace("]", "")
				expectedValue[i] = expectedValue[i].replace("[", "")
				expectedValue[i] = expectedValue[i].replace("]", "")
				validateSingleAttrValue.put(expectedName[i], expectedValue[i]);

			}
		}
		if(!validateAttrMultiValue.isEmpty()){
			XmlValidate.validateAttrMultiValue(validateAttrMultiValue, ResponseBody)
		}
		if(!validateAttrValue.isEmpty()){
			XmlValidate.validateAttrValue(validateAttrValue, ResponseBody)
		}
		if(!validateTagMultiValueMap.isEmpty()){
			XmlValidate.validateTagMultiValue(validateTagMultiValueMap, ResponseBody)
		}
		if(!validateTagValueMap.isEmpty()){
			XmlValidate.validateTagValue(validateTagValueMap, ResponseBody)
		}
		if(!validateTagValueRangeMap.isEmpty()){
			XmlValidate.validateTagRangeValue(validateTagValueRangeMap, ResponseBody)
		}
		if(!validateSingleAttrValue.isEmpty()){
			XmlValidate.validateSingleAttrValue(validateSingleAttrValue, ResponseBody)
		}
	}



	public def validateFailureResponse(String ResponseBody){
		validateXmlWithExpectedElementValuesPair(dataMap.get('ExpectedName'), dataMap.get('ExpectedValue'), ResponseBody)
	}
	public def validateXmlWithEmptyResponse(String xml){
		String elementName =dataMap.get('ExpectedName')
		assert xml.toLowerCase().contains("<$elementName/>".toLowerCase()),"Response Body does not have the element "+ elementName
	}

	public def validateXmlWithExpectedElementValuesPair(String elementName, String elementValue, String xml){
		assert xml.toLowerCase().contains("<$elementName>$elementValue</$elementName>".toLowerCase()), "Response Body does not have the element tag "+ elementName +" with value "+ elementValue
	}



	/*public def validateXmlFileWithExpectedElement(String elementName, String xml){
	 def lower = elementName.toLowerCase()
	 softAssert.assertTrue(xml.toLowerCase().contains(lower),"Response Body does not have the element "+ elementName)
	 }
	 public def validateXmlFileWithExpectedValuesAttrs(String attributeName, String attributeValue, String xml){
	 softAssert.assertTrue(xml.toLowerCase().contains("$attributeName=\"$attributeValue\"".toLowerCase()),"Response Body does not have the element tag "+ attributeName +" with value "+ attributeValue)
	 }
	 */
	public def traverseResponseXml(Node node, String testDesc){

		println "\n\nTest Runnning : ${testDesc}"
		def isErrorNode = node.depthFirst().findAll{ it.name() == "Errors" }
		def isEmptyNode = node.depthFirst().findAll{ it.name() == "Hotel" }
		def isResponseNode = node.depthFirst().findAll{ it.name() == "ResponseDetails" }
		if(!isEmptyNode && !isErrorNode){
			if(isResponseNode){
				println "\nResponseReference : ${node.@ResponseReference}"
			}
			println "\nEmpty Response ... "
		}else if(isErrorNode){
			println "\nError Response ... "
			if(isResponseNode){
				def errorNode
				softAssert.assertTrue(node.@ResponseReference.startsWith('XA'))
				println "\nResponseReference : ${node.@ResponseReference}"
				def isBookingResponse = node.depthFirst().findAll{ it.name() == "BookingResponse" }
				def isSearchHotelPriceResponse = node.depthFirst().findAll{ it.name() == "SearchHotelPricePaxResponse" }
				if(isBookingResponse){
					errorNode=node.ResponseDetails.BookingResponse.Errors.Error
				}else if(isSearchHotelPriceResponse){
					errorNode=node.ResponseDetails.SearchHotelPriceResponse.Errors.Error
				}else{
					errorNode=node.ResponseDetails.Errors.Error
				}
				println "ErrorId : ${errorNode.ErrorId.text()} \nErrorText : ${errorNode.ErrorText.text()}"
			}else{
				println "ErrorId : ${node.Errors.Error.ErrorId.text()} \nErrorText : ${node.Errors.Error.ErrorText.text()}"
			}
		}else{
			softAssert.assertTrue(node.@ResponseReference.startsWith('XA'))
			println "\n	Valid response ... \nResponseReference : ${node.@ResponseReference}"

			def hotelDetails=node.ResponseDetails.SearchHotelPriceResponse.HotelDetails
			def allHotel= hotelDetails.Hotel.size()
			def isRoomPrice = node.depthFirst().findAll{ it.name() == "RoomPrice" }
			for(int i in 0 .. (allHotel-1)){

				String cityCode=hotelDetails.Hotel[i].City.@Code
				softAssert.assertTrue(cityCode==dataMap.get('ExpectedValues'))

				println "Hotel City Code : ${hotelDetails.Hotel[i].City.@Code}\nHotel City Name : ${hotelDetails.Hotel[i].City.text()}"
				println "Hotel Item Code : ${hotelDetails.Hotel[i].Item.@Code}\nHotel Item Name : ${hotelDetails.Hotel[i].Item.text()}"
				println "Hotel Star Rating : ${hotelDetails.Hotel[i].StarRating.text()}\nHotel Location Code : ${hotelDetails.Hotel[i].LocationDetails.Location.@Code}"
				if(isRoomPrice){
					def allRoomCategory=hotelDetails.Hotel[i].RoomCategories.RoomCategory.size()
					for(int j in 0 .. (allRoomCategory-1)){
						println "Room Discription : ${hotelDetails.Hotel[i].RoomCategories.RoomCategory[j].Description.text()}"
						println "Room Price : ${hotelDetails.Hotel[i].RoomCategories.RoomCategory[j].HotelRoomPrices.HotelRoom.RoomPrice.@Gross}"
					}
					println "--------------------------------"
				}
			}
		}
	}
	public def traverseDayFormateResponseXml(Node node,String testDesc){

		println "\n\nTest Runnning : ${testDesc}"
		def isResponseNode = node.depthFirst().findAll{ it.name() == "ResponseDetails" }
		if(isResponseNode){
			softAssert.assertTrue(node.@ResponseReference.startsWith('XA'))
			println "\nResponseReference : ${node.@ResponseReference}"
		}
		Iterator ittr = node.depthFirst().iterator()
		while(ittr.hasNext()){
			Node node1=ittr.next()
			if(node1.name().equals("Item")){
				println"Hotel Code : ${node1.@Code} \nHotel Name : ${node1.text()}"
			}
			if(node1.name().equals("RoomCategory")){
				println"RoomCategory Id : ${node1.@Id} \nRoomCategory Description : ${node1.Description.text()}"
			}
			if(node1.name().equals("ChargeCondition")){
				println"Type : ${node1.@Type}"
				Iterator ittr1 = node1.children().iterator()
				while(ittr1.hasNext()){
					Node node2=ittr1.next()
					if(node2.@Charge.equals("true")){
						println "Charge Condition : ${node2.@Charge} \tCharge Amount : ${node2.@ChargeAmount} \tCurrency : ${node2.@Currency} \tFrom Day : ${node2.@FromDay} \tTo Day : ${node2.@ToDay}"
					}
					else{
						println "Charge Condition : ${node2.@Charge} \tFrom Day : ${node2.@FromDay}"
					}
				}
			}
		}
	}
	public def traverseDateFormateResponseXml(Node node, String testDesc){

		println "\n\nTest Runnning : ${testDesc}"
		def isResponseNode = node.depthFirst().findAll{ it.name() == "ResponseDetails" }
		if(isResponseNode){
			softAssert.assertTrue(node.@ResponseReference.startsWith('XA'))
			println "\nResponseReference : ${node.@ResponseReference}"
		}
		Iterator ittr = node.depthFirst().iterator()
		while(ittr.hasNext()){
			Node node1=ittr.next()
			if(node1.name().equals("Item")){
				println"Hotel Code : ${node1.@Code} \nHotel Name : ${node1.text()}"
			}
			if(node1.name().equals("RoomCategory")){
				println"RoomCategory Id : ${node1.@Id} \nRoomCategory Description : ${node1.Description.text()}"
			}
			if(node1.name().equals("ChargeCondition")){
				println"Type : ${node1.@Type}"
				Iterator ittr1 = node1.children().iterator()
				while(ittr1.hasNext()){
					Node node2=ittr1.next()
					if(node2.@Charge.equals("true")){
						println "Charge Condition : ${node2.@Charge} \tCharge Amount : ${node2.@ChargeAmount} \tCurrency : ${node2.@Currency} \tFrom Date : ${node2.@FromDate} \tTo Date : ${node2.@ToDate}"
					}
					else{
						println "Charge Condition : ${node2.@Charge} \tFrom Date : ${node2.@FromDate}"
					}
				}
			}
		}
	}

	public def traverseOfferResponseXml(Node node, String testDesc){

		println "\n\nTest Runnning : ${testDesc}"
		def isResponseNode = node.depthFirst().findAll{ it.name() == "ResponseDetails" }
		if(isResponseNode){
			softAssert.assertTrue(node.@ResponseReference.startsWith('XA'))
			println "\nResponseReference : ${node.@ResponseReference}"
		}
		Iterator<Node> itr= node.depthFirst().iterator()
		while(itr.hasNext()){
			boolean flag=false
			Node node1= itr.next()
			if(node1.name().equals("Hotel")){
				println "Hotel Code : ${node1.Item.@Code}\nHotel Name : ${node1.Item.text()}"
			}
			if(node1.name().equals("RoomCategories")){
				//Node node3=node1.children().get(0)
				Iterator<Node> ittr = node1.depthFirst().iterator()
				while(ittr.hasNext()){
					Node node2=ittr.next()
					if(node2.name().equals("Offer")){
						flag=true
						println "Offer Code : ${node2.@Code} \nOffer Description : ${node2.text()}\n"
						break;
					}
				}
				if(!flag){
					println "No Offer associated with this Hotel\n"
				}
			}
		}
	}

	public def traverseDIResponseXml(Node node , String testDesc){

		println "\n\nTest Runnning : ${testDesc}"
		def isResponseNode = node.depthFirst().findAll{ it.name() == "ResponseDetails" }
		if(isResponseNode){
			softAssert.assertTrue(node.@ResponseReference.startsWith('XA'))
			println "\nResponseReference : ${node.@ResponseReference}"
		}
		def isEmptyNode = node.depthFirst().findAll{ it.name() == "Hotel" }
		if(!isEmptyNode){
			println "\nEmpty Response ... "
		}
		Iterator<Node> itr= node.depthFirst().iterator()
		while(itr.hasNext()){
			Node node1=itr.next()
			if(node1.name().equals("Item")){
				println"\nHotel Code : ${node1.@Code} \nHotel Name : ${node1.text()}"
			}
			if(node1.name().equals("RoomCategory")){
				println"RoomCategory Id : ${node1.@Id} \nRoomCategory Description : ${node1.Description.text()}"
				if(node1.@Id.contains("CAESARS") || node1.@Id.contains("BONOTEL")){
					println " NO Offer Discount == true"
				}
			}
		}
	}

	/**
	 * @return String CheckInDate = 1 month after today date
	 */
	public String getCheckInDate(){
		Calendar cal = Calendar.getInstance()
		cal.add(Calendar.DAY_OF_MONTH, 30)
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")
		return DATE_FORMAT.format(cal.getTime())
	}
	
	public String getCheckInDateToday5(){
		Calendar cal = Calendar.getInstance()
		cal.add(Calendar.DAY_OF_MONTH, 5)
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")
		return DATE_FORMAT.format(cal.getTime())
	}

	/**
	 * @return String CheckInDate = today's date
	 */
	public String getCheckInTodayDate(){
		Calendar cal = Calendar.getInstance()
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")
		return DATE_FORMAT.format(cal.getTime())
	}

	/**
	 * @return String CheckOutDate = 1 month + 1 Day after today date
	 */
	public String getCheckOutDate(){
		Calendar cal = Calendar.getInstance()
		cal.add(Calendar.DAY_OF_MONTH, 31)
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")
		return DATE_FORMAT.format(cal.getTime())
	}

	public def getInputXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
							
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                            <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getInputXml_destCodeMissing(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemCode></ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
							
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                            <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getSiteIdNotExistXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return """<?xml version="1.0" encoding="UTF-8"?>
<Request>
    <Source>
        <RequestorID
            Client = "${dataMap.get('REQUEST.Client_id')}"
            EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
            Password = "${dataMap.get('REQUEST.Password')}"/>
        <RequestorPreferences
            Country = "${dataMap.get('REQUEST.Country')}"
            Currency = "${dataMap.get('REQUEST.Currency')}"
            Language = "${dataMap.get('REQUEST.Language')}">
            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>
    </Source>
    <RequestDetails>
        <AddBookingRequest Currency = "${dataMap.get('AddBookingRequest.Currency')}">
            <BookingName/>
            <BookingReference>${dataMap.get('AddBookingRequest.BookingReference')}</BookingReference>
            <BookingDepartureDate>${dataMap.get('AddBookingRequest.BookingDepartureDate')}</BookingDepartureDate>
            <PaxNames>
                <PaxName PaxId = "${dataMap.get('PaxNames.PaxId1')}" PaxType = "${dataMap.get('PaxNames.PaxType1')}">${dataMap.get('PaxName1')}</PaxName>
                <PaxName PaxId = "${dataMap.get('PaxNames.PaxId1')}" PaxType = "${dataMap.get('PaxNames.PaxType2')}">${dataMap.get('PaxName2')}</PaxName>
            </PaxNames>
            <BookingItems>
                <BookingItem ItemType = "${dataMap.get('BookingItem.ItemType')}">
                    <ItemReference>${dataMap.get('BookingItem.ItemReference')}</ItemReference>
                    <ItemCity Code = "${dataMap.get('SEARCH_HOTEL.Destination_code')}"/>
                    <Item Code = "${dataMap.get('SEARCH_HOTEL.ItemCode')}"/>
                    <ItemRemarks/>
                    <HotelItem>
                        <AlternativesAllowed>${dataMap.get('HotelItem.AlternativesAllowed')}</AlternativesAllowed>
                        <PeriodOfStay>
                            <CheckInDate>${getCheckInDate()}</CheckInDate>
                            <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
                        </PeriodOfStay>
                        <HotelRooms>
                            <HotelRoom
                                Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
                                ExtraBed = "${dataMap.get('HotelRoom.ExtraBed')}"
                                Id = "${dataMap.get('HotelRoom.Id')}"
                                NumberOfCots = "${dataMap.get('HotelRoom.NumberOfCots')}">
                                <PaxIds>
                                    <PaxId>${dataMap.get('PaxNames.PaxId1')}</PaxId>
                                    <PaxId>${dataMap.get('PaxNames.PaxId2')}</PaxId>
                                </PaxIds>
                            </HotelRoom>
                        </HotelRooms>
                    </HotelItem>
                </BookingItem>
            </BookingItems>
        </AddBookingRequest>
    </RequestDetails>
</Request>"""

		}
	}
	public def getAnotherInputXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
							<IncludePriceBreakdown/>
							<IncludeChargeConditions/> 
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                            <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	public def getGeocodeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
					<ItemDestination
                DestinationType = "${dataMap.get('ItemDestination.DestinationType')}"
                Latitude = "${dataMap.get('ItemDestination.Latitude')}"
                Longitude = "${dataMap.get('ItemDestination.Longitude')}"
                RadiusKm = "${dataMap.get('ItemDestination.RadiusKm')}"/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			       		
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getItemNameXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemName>${dataMap.get('SEARCH_HOTEL.ItemName')}</ItemName>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			           
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}


	public def getItemNameItemCodeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemName>${dataMap.get('SEARCH_HOTEL.ItemName')}</ItemName>
						<ItemCode></ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getItemCodeItemNameXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ItemName>${dataMap.get('SEARCH_HOTEL.ItemName')}</ItemName>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			          
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
		
		public def getItemCodeItemNameNoPeriodOfStayXml(String sheetName,int rowNum) {
			dataMap=getSheetDataAsMap(sheetName,rowNum)
			if(dataMap && dataMap.get("Flag")){
				//return file
				return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ItemName>${dataMap.get('SEARCH_HOTEL.ItemName')}</ItemName>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
		               <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
			}
	}
		
		public def getItemCodeItemNamePeriodOfStayXml_WrongOrder(String sheetName,int rowNum) {
			dataMap=getSheetDataAsMap(sheetName,rowNum)
			if(dataMap && dataMap.get("Flag")){
				//return file
				return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ItemName>${dataMap.get('SEARCH_HOTEL.ItemName')}</ItemName>
						<PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
						<PeriodOfStay>
		               <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
						<PeriodOfStay>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
			}
	}


	public def getMultipleItemNameXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemName>${dataMap.get('SEARCH_HOTEL.ItemName')}</ItemName>
						<ItemName1>${dataMap.get('SEARCH_HOTEL.ItemName1')}</ItemName1>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			           
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	public def getImmediateConfirmationOnlyXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ImmediateConfirmationOnly/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			         
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getImmediateConfirmationOnlyXml2(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ImmediateConfirmationOnly></ImmediateConfirmationOnly>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			         
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getImmediateConfirmationOnlyXml_MultiRoom(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ImmediateConfirmationOnly/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			         
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getNoImmediateConfirmationOnlyXml_MultiRoom(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
					
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			         
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	public def getImmediateConfirmationOnlyXml_MultiRoom2(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ImmediateConfirmationOnly></ImmediateConfirmationOnly>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			         
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getItemCodeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
							
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                            <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
			                
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getItemCodeXml2(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
							
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getItemCodeXml_4Rooms(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
							
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
                            <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
                             <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults3')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots3')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex3')}"/>
                              <PaxRoom
			                    Adults = "2"
			                    Cots = "0"
			                    RoomIndex = "4"/>
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	
	public def getItemCodeXml_3Rooms(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
							
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
                            <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
                             <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults3')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots3')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex3')}"/>
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	
	public def getItemCodeMultipleRoomsXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
                          <Room 
                            Code = "TB" 
                            NumberOfCots = "0" 
                            NumberOfRooms = "1"> 
                            <ExtraBeds/>
                          </Room>
                           <Room 
                            Code = "SB" 
                            NumberOfCots = "0" 
                            NumberOfRooms = "2"> 
                            <ExtraBeds/>
                          </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	
	public def getItemCodeCheckOutXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <CheckOutDate>${getCheckOutDate()}</CheckOutDate>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds/>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getCheckInOutXml(String sheetName,int rowNum,String checkInDate,String checkOutDate) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${checkInDate}</CheckInDate>
							<CheckOutDate>${checkOutDate}</CheckOutDate>
			            </PeriodOfStay>
			            
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                            <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
			                    
			            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getIncludeRecommendedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeRecommended/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	
	public def getOfferXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDateToday5()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeRecommended/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getRecommendedOnlyXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<RecommendedOnly/>
			           
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getIncludePriceBreakdownXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					 
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
                          
						  <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getIncludePriceBreakdownXml3Rooms(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					 
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
                          
						  <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}"/>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults3')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots3')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex3')}"/>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getIncludePriceBreakdownXml_1Cot(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					 
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges> <Age>8</Age></ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_1Child(String sheetName,int rowNum, int childAge) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges> <Age>${childAge}</Age></ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_2Child(String sheetName,int rowNum, int childAge1, int childAge2) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges> 
									<Age>${childAge1}</Age>
									<Age>${childAge2}</Age>
								</ChildAges>

							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_2Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges> 
									<Age>8</Age>
									<Age>7</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_3Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>9</Age>
									<Age>8</Age>
									<Age>7</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_4Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>9</Age>
									<Age>8</Age>
									<Age>7</Age>
									<Age>6</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_5Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>9</Age>
									<Age>8</Age>
									<Age>7</Age>
									<Age>6</Age>
									<Age>6</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	
	public def getSHPPR_6Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>9</Age>
									<Age>9</Age>
									<Age>8</Age>
									<Age>7</Age>
									<Age>6</Age>
									<Age>6</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_7Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>9</Age>
									<Age>9</Age>
									<Age>8</Age>
									<Age>8</Age>
									<Age>7</Age>
									<Age>6</Age>
									<Age>6</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	
	public def getSHPPR_8Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>9</Age>
									<Age>9</Age>
									<Age>8</Age>
									<Age>8</Age>
									<Age>7</Age>
									<Age>7</Age>
									<Age>6</Age>
									<Age>6</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_9Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>9</Age>
									<Age>9</Age>
									<Age>8</Age>
									<Age>8</Age>
									<Age>7</Age>
									<Age>7</Age>
									<Age>6</Age>
									<Age>6</Age>
                                    <Age>5</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_LessThan2Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>1</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_grtThan18Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>19</Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSHPPR_emptyChildAge(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
					      <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age></Age>
								</ChildAges>
							</PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	public def getIncludePriceBreakdownOneExtraBedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>		           
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
			                    <ChildAges> <Age>8</Age></ChildAges>
                              </PaxRoom>
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	public def getIncludePriceBreakdownTwoExtraBedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
			             <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
									<Age>8</Age>
								</ChildAges>
			                 </PaxRoom>   
							 <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
								<ChildAges>
									<Age>9</Age>
								</ChildAges>
			                 </PaxRoom>   
                            </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}


	public def getIncludePriceBreakdownThreeExtraBedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds>
								<Age>${dataMap.get('ExtraBed1.Child_Age')}</Age>
								<Age>${dataMap.get('ExtraBed2.Child_Age')}</Age>
                                <Age>${dataMap.get('ExtraBed3.Child_Age')}</Age>
								</ExtraBeds>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithoutFormatXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithoutFormatXml_1Child(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
                                   <Age>8</Age>
								</ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
                                <ChildAges>
                                   <Age>9</Age>
								</ChildAges>
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithoutFormatXml_2ChildDI(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges>
                                   <Age>8</Age>
								   <Age>9</Age>
								</ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
                                <ChildAges>
                                   <Age>8</Age>
                                   <Age>9</Age>
								</ChildAges>
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithoutFormatXml_City(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
					      <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	
	public def getChargeConditionsWithoutFormatSingleXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithoutFormatMultiXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	
	public def getChargeConditionsWithoutFormatExtraBedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions/>
			              <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
                                <ChildAges> <Age>6</Age> </ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
								<ChildAges> <Age>8</Age> </ChildAges>
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	

	public def getChargeConditionsWithFormatXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions DateFormatResponse = "${dataMap.get('DateFormateResponse')}"/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithFormatSingleXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions DateFormatResponse = "${dataMap.get('DateFormateResponse')}"/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	
	public def getChargeConditionsWithFormat2ItemCodesSingleXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
					<ItemCodes>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode1')}</ItemCode>
					</ItemCodes>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions DateFormatResponse = "${dataMap.get('DateFormateResponse')}"/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	
	public def getChargeConditionsWithFormat2ItemCodes4CodesXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
					<ItemCodes>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode1')}</ItemCode>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode2')}</ItemCode>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode3')}</ItemCode>
					</ItemCodes>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions DateFormatResponse = "${dataMap.get('DateFormateResponse')}"/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithFormat2ItemCodes3CodesXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
					<ItemCodes>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode1')}</ItemCode>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode2')}</ItemCode>
					</ItemCodes>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions DateFormatResponse = "${dataMap.get('DateFormateResponse')}"/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getChargeConditionsWithFormat50CodesXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
					<ItemCodes>
						<ItemCode>gla</ItemCode> <ItemCode>ATL</ItemCode>
						<ItemCode>clam</ItemCode> <ItemCode>abb</ItemCode> 
						<ItemCode>9L</ItemCode> <ItemCode>BELA</ItemCode>
						<ItemCode>9N</ItemCode> <ItemCode>ACA3</ItemCode> 
						<ItemCode>prig</ItemCode> <ItemCode>relu</ItemCode> 
						<ItemCode>camg</ItemCode><ItemCode>fou2</ItemCode> 
                        <ItemCode>cor</ItemCode> <ItemCode>9k</ItemCode> 
                        <ItemCode>vic</ItemCode> <ItemCode>aca6</ItemCode> 
                        <ItemCode>san</ItemCode> <ItemCode>roy5</ItemCode> 
                        <ItemCode>9z</ItemCode> <ItemCode>rap</ItemCode> 
                        <ItemCode>for</ItemCode> <ItemCode>rel8</ItemCode> 
                        <ItemCode>leb1</ItemCode> <ItemCode>9w</ItemCode>
						<ItemCode>9f</ItemCode> <ItemCode>relp</ItemCode> 
						<ItemCode>deh</ItemCode> <ItemCode>acr</ItemCode>
						<ItemCode>reln</ItemCode> <ItemCode>bwm</ItemCode>
						<ItemCode>aux</ItemCode> <ItemCode>alb3</ItemCode>
						<ItemCode>relk</ItemCode> <ItemCode>alb2</ItemCode>
						<ItemCode>9e</ItemCode> <ItemCode>ely9</ItemCode> 
						<ItemCode>ber5</ItemCode> <ItemCode>rels</ItemCode> 
						<ItemCode>int1</ItemCode> <ItemCode>gob1</ItemCode>
						<ItemCode>cam7</ItemCode> <ItemCode>dec</ItemCode> 
						<ItemCode>graj</ItemCode> <ItemCode>cos1</ItemCode>
						<ItemCode>gra</ItemCode> <ItemCode>gol7</ItemCode>
						<ItemCode>mag1</ItemCode> <ItemCode>dor</ItemCode> 
						<ItemCode>dut</ItemCode> <ItemCode>9c</ItemCode> 
					</ItemCodes>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions DateFormatResponse = "${dataMap.get('DateFormateResponse')}"/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	
	public def getChargeConditionsWithFormat55CodesXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
					<ItemCodes>
						<ItemCode>gla</ItemCode> <ItemCode>ATL</ItemCode> 
						<ItemCode>clam</ItemCode> <ItemCode>abb</ItemCode> 
						<ItemCode>9L</ItemCode> <ItemCode>BELA</ItemCode>
						<ItemCode>9N</ItemCode> <ItemCode>ACA3</ItemCode> 
						<ItemCode>prig</ItemCode> <ItemCode>relu</ItemCode> 
						<ItemCode>camg</ItemCode> <ItemCode>fou2</ItemCode>
						<ItemCode>cor</ItemCode> <ItemCode>9k</ItemCode> 
						<ItemCode>vic</ItemCode> <ItemCode>aca6</ItemCode>
						<ItemCode>san</ItemCode> <ItemCode>roy5</ItemCode>
						<ItemCode>9z</ItemCode> <ItemCode>rap</ItemCode> 
						<ItemCode>for</ItemCode> <ItemCode>rel8</ItemCode> 
						<ItemCode>leb1</ItemCode> <ItemCode>9w</ItemCode> 
						<ItemCode>9f</ItemCode> <ItemCode>relp</ItemCode> 
						<ItemCode>deh</ItemCode> <ItemCode>acr</ItemCode>
						<ItemCode>reln</ItemCode> <ItemCode>bwm</ItemCode> 
						<ItemCode>aux</ItemCode> <ItemCode>alb3</ItemCode> 
						<ItemCode>relk</ItemCode> <ItemCode>alb2</ItemCode> 
						<ItemCode>9e</ItemCode> <ItemCode>ely9</ItemCode> 
						<ItemCode>ber5</ItemCode> <ItemCode>rels</ItemCode>
						<ItemCode>int1</ItemCode> <ItemCode>gob1</ItemCode>
						<ItemCode>cam7</ItemCode> <ItemCode>dec</ItemCode> 
						<ItemCode>graj</ItemCode> <ItemCode>cos1</ItemCode> 
						<ItemCode>gra</ItemCode> <ItemCode>gol7</ItemCode> 
						<ItemCode>mag1</ItemCode> <ItemCode>dor</ItemCode> 
						<ItemCode>dut</ItemCode> <ItemCode>9c</ItemCode> 
						<ItemCode>eve</ItemCode> <ItemCode>roc</ItemCode> 
						<ItemCode>91n</ItemCode> <ItemCode>ala</ItemCode> 
						<ItemCode>mex1</ItemCode>
					</ItemCodes>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeChargeConditions DateFormatResponse = "${dataMap.get('DateFormateResponse')}"/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getCancellationDeadlineHoursXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<ExcludeChargeableItems>
							<CancellationDeadlineHours>${dataMap.get('CancellationDeadlineHours')}</CancellationDeadlineHours>
						</ExcludeChargeableItems>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getIncorrectSchemaCancellationHourXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ExcludeChargeableItems>
							<CancellationDeadlineHours>${dataMap.get('CancellationDeadlineHours')}</CancellationDeadlineHours>
						</ExcludeChargeableItems>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			             <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getWithoutCancellationHoursXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<ExcludeChargeableItems>
						</ExcludeChargeableItems>
			             <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getCancellationDeadlineDaysXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<ExcludeChargeableItems>
							<CancellationDeadlineDays>${dataMap.get('CancellationDeadlineDays')}</CancellationDeadlineDays>
						</ExcludeChargeableItems>
			             <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			           
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getIncorrectSchemaCancellationDaysXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
						<ExcludeChargeableItems>
							<CancellationDeadlineDays>${dataMap.get('CancellationDeadlineDays')}</CancellationDeadlineDays>
						</ExcludeChargeableItems>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getWithoutCancellationDaysXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<ExcludeChargeableItems>
						</ExcludeChargeableItems>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getMultipleRoomTypeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds/>
			                </Room>
							<Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Multi.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Multi.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Multi.Room_numberOfRooms')}">
			                    <ExtraBeds/>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getTwoExtraBedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds>
								<Age>${dataMap.get('ExtraBed1.Child_Age')}</Age>
								<Age>${dataMap.get('ExtraBed2.Child_Age')}</Age>
								</ExtraBeds>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	public def getOneExtraBedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds>
								<Age>${dataMap.get('ExtraBed1.Child_Age')}</Age>
								</ExtraBeds>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}


	public def getThreeExtraBedXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds>
								<Age>${dataMap.get('ExtraBed1.Child_Age')}</Age>
								<Age>${dataMap.get('ExtraBed2.Child_Age')}</Age>
								<Age>${dataMap.get('ExtraBed3.Child_Age')}</Age>
								</ExtraBeds>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getStarRatingRangeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			           <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<StarRatingRange>
			                <Min>${dataMap.get('StarRatingRange.Min')}</Min>
						    <Max>${dataMap.get('StarRatingRange.Max')}</Max>
			            </StarRatingRange>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getStarRatingPlusRangeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			           <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<StarRating>${dataMap.get('StarRating')}</StarRating>
						<StarRatingRange>
			                <Min>${dataMap.get('StarRatingRange.Min')}</Min>
						    <Max>${dataMap.get('StarRatingRange.Max')}</Max>
			            </StarRatingRange>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}


	public def getStarRatingRangeMaxOnlyXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<StarRatingRange>
						    <Max>${dataMap.get('StarRatingRange.Max')}</Max>
			            </StarRatingRange>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getStarRatingPlusRangeMinOnlyXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			           <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<StarRatingRange>
			                <Min>${dataMap.get('StarRatingRange.Min')}</Min>
			            </StarRatingRange>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getStarRatingMinimumXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			           <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}">${dataMap.get('StarRating')}</StarRating>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getStarRatingXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			           <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<StarRating>${dataMap.get('StarRating')}</StarRating>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getLocationCodeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<LocationCode>${dataMap.get('LocationCode')}</LocationCode>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}


	public def getGeocodeNSWEXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"
                         WestLongitude="3.5444"
                         SouthLatitude="43.4827"
                         EastLongitude="4.4056"
                         NorthLatitude="43.726"/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			             
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getFacilityCodeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			             
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
						<FacilityCodes>
							<FacilityCode>${dataMap.get('FacilityCode')}</FacilityCode>
						</FacilityCodes>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getMultipleFacilityCodeXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds/>
			                </Room>
						</Rooms>
						<FacilityCodes>
							<FacilityCode>${dataMap.get('FacilityCode')}</FacilityCode>
							<FacilityCode>${dataMap.get('FacilityCode_1')}</FacilityCode>
							<FacilityCode>${dataMap.get('FacilityCode_2')}</FacilityCode>
						</FacilityCodes>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request> """
		}
	}

	public def getInputXmlNumberofReturnedItems(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <PaxRooms> 
                         <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
                        </PaxRooms>
						<NumberOfReturnedItems>3</NumberOfReturnedItems>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getInputXmlNumberofReturnedItemsWithOrder(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds/>
			                </Room>
			            </Rooms>
                        <OrderBy>pricehightolow</OrderBy>
						<NumberOfReturnedItems>5</NumberOfReturnedItems>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getCombinedInputXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
                        <ImmediateConfirmationOnly/>
                        <ItemCode></ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludeRecommended/>
						<IncludePriceBreakdown/>
						<IncludeChargeConditions/>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
							</PaxRoom>
						</PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}


	public def getCheckInOutDurationXml(String sheetName,int rowNum,String checkInDate,String checkOutDate) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPricePaxRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${checkInDate}</CheckInDate>
                             <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
							<CheckOutDate>${checkOutDate}</CheckOutDate>
			            </PeriodOfStay>
			            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}"/>
			                    
                          </PaxRooms>
			        </SearchHotelPricePaxRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

	public def getIncludePriceBreakdownThreeRoomsXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
	<Request>
	 <Source>
	  <RequestorID 
           Client = "${dataMap.get('REQUEST.Client_id')}"
           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
           Password = "${dataMap.get('REQUEST.Password')}"/>
        <RequestorPreferences
            Country = "${dataMap.get('REQUEST.Country')}"
            Currency = "${dataMap.get('REQUEST.Currency')}"
            Language = "${dataMap.get('REQUEST.Language')}">
            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>
    </Source>
	    <RequestDetails>
	        <SearchHotelPricePaxRequest>
	            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
				<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
	            <PeriodOfStay>
	                <CheckInDate>${checkInDate}</CheckInDate>
                     <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
				 </PeriodOfStay>
            <IncludePriceBreakdown/>
            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges> <Age>8</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
								<ChildAges> <Age>7</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults3')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots3')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex3')}">
								<ChildAges> <Age>9</Age></ChildAges>
							</PaxRoom>
                          </PaxRooms>
	  </SearchHotelPricePaxRequest>
	 </RequestDetails>
	</Request>"""
		}
	}
	
	public def getIncludePriceBreakdownSixRoomsXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
	<Request>
	 <Source>
	  <RequestorID 
           Client = "${dataMap.get('REQUEST.Client_id')}"
           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
           Password = "${dataMap.get('REQUEST.Password')}"/>
        <RequestorPreferences
            Country = "${dataMap.get('REQUEST.Country')}"
            Currency = "${dataMap.get('REQUEST.Currency')}"
            Language = "${dataMap.get('REQUEST.Language')}">
            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>
    </Source>
	    <RequestDetails>
	        <SearchHotelPricePaxRequest>
	            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
				<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
	            <PeriodOfStay>
	                <CheckInDate>${checkInDate}</CheckInDate>
                     <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
				 </PeriodOfStay>
            <IncludePriceBreakdown/>
            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges> <Age>8</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
								<ChildAges> <Age>7</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults3')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots3')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex3')}">
								<ChildAges> <Age>9</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "2"
			                    Cots = "2"
			                    RoomIndex = "4">
								<ChildAges> <Age>8</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "2"
			                    Cots = "2"
			                    RoomIndex = "5">
								<ChildAges> <Age>7</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "2"
			                    Cots = "2"
			                    RoomIndex = "6">
								<ChildAges> <Age>9</Age></ChildAges>
							</PaxRoom>
                          </PaxRooms>
	  </SearchHotelPricePaxRequest>
	 </RequestDetails>
	</Request>"""
		}
	}
	
	public def getNineRoomsXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
	<Request>
	 <Source>
	  <RequestorID 
           Client = "${dataMap.get('REQUEST.Client_id')}"
           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
           Password = "${dataMap.get('REQUEST.Password')}"/>
        <RequestorPreferences
            Country = "${dataMap.get('REQUEST.Country')}"
            Currency = "${dataMap.get('REQUEST.Currency')}"
            Language = "${dataMap.get('REQUEST.Language')}">
            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>
    </Source>
	    <RequestDetails>
	        <SearchHotelPricePaxRequest>
	            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
				<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
	            <PeriodOfStay>
	                <CheckInDate>${checkInDate}</CheckInDate>
                     <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
				 </PeriodOfStay>
            <IncludePriceBreakdown/>
            <PaxRooms>
			                <PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "1">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "2">
							</PaxRoom>
							<PaxRoom
			                     Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "3">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "4">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "5">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "6">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "7">
							</PaxRoom>
                            <PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "8">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "9">
							</PaxRoom>
                          </PaxRooms>
	  </SearchHotelPricePaxRequest>
	 </RequestDetails>
	</Request>"""
		}
	}
	
	public def getTenRoomsXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
	<Request>
	 <Source>
	  <RequestorID 
           Client = "${dataMap.get('REQUEST.Client_id')}"
           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
           Password = "${dataMap.get('REQUEST.Password')}"/>
        <RequestorPreferences
            Country = "${dataMap.get('REQUEST.Country')}"
            Currency = "${dataMap.get('REQUEST.Currency')}"
            Language = "${dataMap.get('REQUEST.Language')}">
            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>
    </Source>
	    <RequestDetails>
	        <SearchHotelPricePaxRequest>
	            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
				<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
	            <PeriodOfStay>
	                <CheckInDate>${checkInDate}</CheckInDate>
                     <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
				 </PeriodOfStay>
            <IncludePriceBreakdown/>
            <PaxRooms>
			                <PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "1">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "2">
							</PaxRoom>
							<PaxRoom
			                     Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "3">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "4">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "5">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "6">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "7">
							</PaxRoom>
                            <PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "8">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "9">
							</PaxRoom>
							<PaxRoom
			                    Adults = "1"
			                    Cots = "0"
			                    RoomIndex = "10">
							</PaxRoom>
                          </PaxRooms>
	  </SearchHotelPricePaxRequest>
	 </RequestDetails>
	</Request>"""
		}
	}
	
	public def getThreeRooms2ChildXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
	<Request>
	 <Source>
	  <RequestorID 
           Client = "${dataMap.get('REQUEST.Client_id')}"
           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
           Password = "${dataMap.get('REQUEST.Password')}"/>
        <RequestorPreferences
            Country = "${dataMap.get('REQUEST.Country')}"
            Currency = "${dataMap.get('REQUEST.Currency')}"
            Language = "${dataMap.get('REQUEST.Language')}">
            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>
    </Source>
	    <RequestDetails>
	        <SearchHotelPricePaxRequest>
	            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
				<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
	            <PeriodOfStay>
	                <CheckInDate>${checkInDate}</CheckInDate>
                     <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
				 </PeriodOfStay>
            <IncludePriceBreakdown/>
            <PaxRooms>
			                <PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults1')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots1')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex1')}">
								<ChildAges> <Age>8</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults2')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots2')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex2')}">
								<ChildAges> <Age>7</Age></ChildAges>
							</PaxRoom>
							<PaxRoom
			                    Adults = "${dataMap.get('SEARCH_HOTEL.Number_Adults3')}"
			                    Cots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots3')}"
			                    RoomIndex = "${dataMap.get('SEARCH_HOTEL.RoomIndex3')}">
							</PaxRoom>
                          </PaxRooms>
	  </SearchHotelPricePaxRequest>
	 </RequestDetails>
	</Request>"""
		}
	}
	
	
	public def getSharedBedding1ChildXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
                        <ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds>
                  				  <Age>${dataMap.get('ExtraBed1.Child_Age')}</Age>
                    			</ExtraBeds>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getSharedBedding2ChildXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
                        <ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
			                <Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds>
                  				  <Age>${dataMap.get('ExtraBed1.Child_Age')}</Age>
								  <Age>${dataMap.get('ExtraBed2.Child_Age')}</Age>
                    			</ExtraBeds>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}
	
	public def getIncludePriceBreakdown3DB1ChildXml(String sheetName,int rowNum) {
		dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			//return file
			return	"""<?xml version="1.0" encoding="UTF-8"?>
			<Request>
			   <Source>
			       <RequestorID
			           Client = "${dataMap.get('REQUEST.Client_id')}"
			           EMailAddress = "${dataMap.get('REQUEST.Email_address')}"
			           Password = "${dataMap.get('REQUEST.Password')}"/>
			        <RequestorPreferences
			            Country = "${dataMap.get('REQUEST.Country')}"
			            Currency = "${dataMap.get('REQUEST.Currency')}"
			            Language = "${dataMap.get('REQUEST.Language')}">
			            <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
			        </RequestorPreferences>
			    </Source>
			    <RequestDetails>
			        <SearchHotelPriceRequest>
			            <ItemDestination DestinationCode = "${dataMap.get('SEARCH_HOTEL.Destination_code')}" DestinationType ="${dataMap.get('SEARCH_HOTEL.Destination_type')}"/>
						<ItemCode>${dataMap.get('SEARCH_HOTEL.ItemCode')}</ItemCode>
			            <PeriodOfStay>
			                <CheckInDate>${getCheckInDate()}</CheckInDate>
							<Duration>${dataMap.get('SEARCH_HOTEL.Duration')}</Duration>
			            </PeriodOfStay>
						<IncludePriceBreakdown/>
			            <Rooms>
			                <Room
			                    Code = "${dataMap.get('SEARCH_HOTEL.Room_code')}"
			                    NumberOfCots = "${dataMap.get('SEARCH_HOTEL.Room_numberOfCots')}"
			                    NumberOfRooms = "${dataMap.get('SEARCH_HOTEL.Room_numberOfRooms')}">
			                    <ExtraBeds>
								<Age>${dataMap.get('ExtraBed1.Child_Age')}</Age>
								<Age>${dataMap.get('ExtraBed2.Child_Age')}</Age>
                                <Age>${dataMap.get('ExtraBed3.Child_Age')}</Age>
								</ExtraBeds>
			                </Room>
			            </Rooms>
			        </SearchHotelPriceRequest>
			    </RequestDetails>
			</Request>"""
		}
	}

}