package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.ConfigProperties
import com.kuoni.qa.automation.util.XMLValidationUtil

import java.util.Map;

import org.apache.http.client.methods.HttpPost;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.asserts.SoftAssert;


import com.kuoni.qa.automation.common.util.ExcelUtil;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.entity.StringEntity;

class SearchItemInformationNewHelper {

	ExcelUtil excelUtil
	String sheetName
	Map dataMap
	Map validateMap= new HashMap()
	String ExpectedResult
	SoftAssert softAssert= new SoftAssert()
	def xmlParser = new XmlParser()
	XMLValidationUtil XmlValidate = new XMLValidationUtil()
	static final String URL= ConfigProperties.getValue("AuthenticationURL")
	static final def timeout = 10 * 60 * 1000
	static RequestConfig requestTimeoutConfig = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();
	static final def timeoOutMilliSecs = 5 * 1000
	public String getExpectedResult() {
		return ExpectedResult;
	}


	SearchItemInformationNewHelper(String excelPath){
		excelUtil=new ExcelUtil(excelPath)
		println("-----------------------------------------------------------------------------\n")
		println("Request URL :  ${URL} \n")
		println("-----------------------------------------------------------------------------\n")
	}
	
	/**
	 * Method for getting data from excel sheet to Map
	 * @return Map dataMap
	 */
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
		Map validateAttrTagValueMap = new HashMap<String,String>()


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

			} else if (expectedName[i].contains(",") && !(expectedValue[i].contains(","))) {
				
				if (expectedValue[i].contains("&"))
				{
					// Attribute with Tag value
					validateAttrTagValueMap.put(expectedName[i],  expectedValue[i]);
				}
				else
				{
					// Attribute with single value
				validateAttrValue.put(expectedName[i], expectedValue[i]);
				}

			} else if (!(expectedName[i].contains(",")) && expectedValue[i].contains(",")) {
				// Tag with Multiple value

				li = new ArrayList<String>();
				st = new StringTokenizer(expectedValue[i], ",");
				while (st.hasMoreElements()) {
					li.add(st.nextToken());
				}
				validateTagMultiValueMap.put(expectedName[i], li);

			} else if (!(expectedName[i].contains(",")) && !(expectedValue[i].contains(","))) {
				// Tag with Single value
				validateTagValueMap.put(expectedName[i], expectedValue[i]);
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
			if(!validateAttrTagValueMap.isEmpty()){
				XmlValidate.validateAttrTagValue(validateAttrTagValueMap, ResponseBody)
			}
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
		assert(xml.toLowerCase().contains("<$elementName>$elementValue</$elementName>".toLowerCase()) || xml.toLowerCase().contains("<$elementName>".toLowerCase())) , "Response Body does not have the element tag "+ elementName +" with value "+ elementValue
	}
	

	public def validateResponseXml(Node node){

		def isErrorNode = node.depthFirst().findAll{
			it.name() == "Errors"
		}
		if(isErrorNode){
			println "\nError Response ... "
			def isResponseNode = node.depthFirst().findAll{
				it.name() == "ResponseDetails"
			}
			if(isResponseNode){
				def errorNode
				println "ResponseReference : ${node.@ResponseReference}"
				assert(node.@ResponseReference.startsWith("XA"))
				errorNode=node.ResponseDetails.SearchItemInformationResponse.Errors.Error
				println "ErrorId : ${errorNode.ErrorId.text()} \nErrorText : ${errorNode.ErrorText.text()}"
			}else{
				println "ErrorId : ${node.Errors.Error.ErrorId.text()} \nErrorText : ${node.Errors.Error.ErrorText.text()}"
			}
		}else{
			println "\nValid response ... \nResponseReference : ${node.@ResponseReference}"
			assert(node.@ResponseReference.startsWith("XA"))
			def itemDetails=node.ResponseDetails.SearchItemInformationResponse.ItemDetails
			def allItem= itemDetails.ItemDetail.size()
			if(allItem==0){
				println "\nEmpty Response..."
			}else{
				println "Number of Hotel : ${allItem}"
				for(int i in 0 ..(allItem-1) ){
					println"Hotel Item Code : ${itemDetails.ItemDetail[i].Item.@Code} \nHotel Item Name : ${itemDetails.ItemDetail[i].Item.text()}"
					println "Star Rating : ${itemDetails.ItemDetail[i].HotelInformation.StarRating.text()} \nLocation Code : ${itemDetails.ItemDetail[i].LocationDetails.Location.@Code}\n-----"
				}
			}
		}
	}

	public def validateLinksResponseXml(Node node){
		Iterator itt=node.depthFirst().iterator()
		while(itt.hasNext()){
			Node node1=itt.next()
			if(node1.name().equals("SearchItemInformationResponse")){
				println "\nSuccess response with <SearchItemInformationResponse> tag"
			}
			if(node1.name().equals("Item")){
				println "Hotel Code : ${node1.@Code} \nHotel Name : ${node1.text()}"
			}
			if(node1.name().equals("Links")){
				println "<Links> tag return having ChildNodes  -"
				node1.children().each {tag ->println "${tag.name()}"}
			}
		}
	}

	public def getInputXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>	
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}


	public def getCorrectItemNameXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName>${dataMap.get('ItemName')}</ItemName>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}

	public def getCorrectAbbreviationXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName>${dataMap.get('ItemName')}</ItemName>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}"/>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}

	public def getIncorrectAbbreviationXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName>${dataMap.get('ItemName')}</ItemName>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}">${dataMap.get('StarRating')}</StarRating>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}


	public def getMinimumStarRatingFalseXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}"/>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}

	public def getMinimumStarRatingTrueXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}">${dataMap.get('StarRating')}</StarRating>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}

	public def getLocationCodeXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode>${dataMap.get('LocationCode')}</LocationCode>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}

	public def getAnotherLocationCodeXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode>${dataMap.get('LocationCode')}</LocationCode>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}"/>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}


	public def getLocationCodeStarRatingXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode>${dataMap.get('LocationCode')}</LocationCode>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}">${dataMap.get('StarRating')}</StarRating>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}


	public def getMultipleLocationCodeStarRatingXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode>${dataMap.get('LocationCode')}</LocationCode>
			<LocationCode>${dataMap.get('LocationCode1')}</LocationCode>
			<LocationCode>${dataMap.get('LocationCode2')}</LocationCode>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}">${dataMap.get('StarRating')}</StarRating>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}


	public def getFacilityCodeXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
			<SearchHotelInformation>
				<FacilityCodes>
					<FacilityCode>${dataMap.get('FacilityCode')}</FacilityCode>
				</FacilityCodes>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}

	public def getXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
			<SearchHotelInformation>
				
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}

	public def getMultipleFacilityCodeXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode/>
			<ItemName/>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
			<SearchHotelInformation>
				<FacilityCodes>
					<FacilityCode>${dataMap.get('FacilityCode')}</FacilityCode>
					<FacilityCode>${dataMap.get('FacilityCode1')}</FacilityCode>
				</FacilityCodes>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}


	public def getMultipleFieldXml(String sheetName,int rowNum){
		Map dataMap=getSheetDataAsMap(sheetName,rowNum)
		if(dataMap && dataMap.get("Flag")){
			return"""<?xml version="1.0" encoding="UTF-8"?>
<Request>	
    <Source>	
        <RequestorID 
		Client="${dataMap.get('REQUEST.Client_id')}" 
        EMailAddress="${dataMap.get('REQUEST.Email_address')}" 
        Password="${dataMap.get('REQUEST.Password')}"/>	
        <RequestorPreferences 
        Country="${dataMap.get('REQUEST.Country')}" 
        Currency="${dataMap.get('REQUEST.Currency')}" 
        Language="${dataMap.get('REQUEST.Language')}">	
        <RequestMode>${dataMap.get('REQUEST.Mode')}</RequestMode>
        </RequestorPreferences>	
    </Source>	
    <RequestDetails>
		<SearchItemInformationRequest ItemType="${dataMap.get('ItemType')}">
			<ItemDestination DestinationCode="${dataMap.get('DestinationCode')}" DestinationType="${dataMap.get('DestinationType')}"/>
			<LocationCode>${dataMap.get('LocationCode')}</LocationCode>
			<ItemName>${dataMap.get('ItemName')}</ItemName>
			<ItemCode>${dataMap.get('ItemCode')}</ItemCode>	
			<SearchHotelInformation>
				<StarRating MinimumRating="${dataMap.get('StarRating.MinimumRating')}">${dataMap.get('StarRating')}</StarRating>
				<FacilityCodes>
					<FacilityCode>${dataMap.get('FacilityCode')}</FacilityCode>
				</FacilityCodes>
			</SearchHotelInformation>
		</SearchItemInformationRequest>
	</RequestDetails>
</Request>"""
		}
	}
}
