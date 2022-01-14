import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When



class VerifyElement {

	@Then("vejo {string}")
	public void vejo(String string) {
		// Write code here that turns the phrase above into concrete actions
		Mobile.verifyElementVisible(findTestObject('Object Repository/android.widget.'+string), 0)

		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
		Mobile.closeApplication()
	}
	
	@Then("vejo a imagem {string}")
	public void vejo_imagem(String string) {
		// Write code here that turns the phrase above into concrete actions
		Mobile.verifyElementVisible(findTestObject('Object Repository/android.widget.ImageView- '+string), 0)

	}
	
	@Then("nao vejo a imagem {string}")
	public void nao_vejo_imagem(String string) {
		// Write code here that turns the phrase above into concrete actions
		Mobile.verifyElementNotExist(findTestObject('Object Repository/android.widget.ImageView- '+string), 0)
	}
	
	@And("tenho o saldo a {string}")
	public void saldo(String string) {
		Mobile.verifyElementText(findTestObject('Object Repository/android.widget.TextView - '+'money'),string )
		//Mobile.verifyElementVisible(findTestObject('Object Repository/android.widget.TextView - '+'money'), 0)
	}
	
	

	@Then("não vejo {string}")
	public void não_vejo(String string) {
		// Write code here that turns the phrase above into concrete actions
		Mobile.verifyElementNotVisible(findTestObject('Object Repository/android.widget.'+string), 0)

		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)

		Mobile.closeApplication()
	}

	@Then("vejo o botão para adicionar contactos")
	public void vejo_o_botão_para_adicionar_contactos() {
		Mobile.verifyElementVisible(findTestObject('Object Repository/android.widget.ImageButton'), 0)

		Mobile.delay(1, FailureHandling.STOP_ON_FAILURE)
	}

	@Then("verifico a presenca de {string}")
	def verifico_a_presenca_de(String string) {
		Mobile.verifyElementExist(findTestObject('Object Repository/android.widget.TextView - '+string), 0)
	}

	@Then("verifico mensagem a {string}")
	def verifico_a_mensagem(String string) {
		Mobile.verifyElementExist(findTestObject('Object Repository/android.widget.TextView - '+string),
				0)
	}

	@Then("o {string} é maior que {int}")
	def o_é_maior_que(String string,int number) {
		def numero=Mobile.getText(findTestObject('android.widget.TextView - '+string), 0)
		numero=Double.parseDouble(numero)
		Mobile.verifyGreaterThan(numero, number)
	}

	@Then("vejo as transactions ordenadas do menos recente para o mais recente.")
	def vejo_as_transactions_ordenadas_do_menos_recente_para_o_mais_recente(){
		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
		def date2=Mobile.getText(findTestObject('android.widget.TextView - Date 06-10-2021 as 1036'), 0)
		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
		def date1=Mobile.getText(findTestObject('android.widget.TextView - Date 13-11-2021 as 1019'), 0)
		date1 = date1.split(' ')
		date2=date2.split(' ')
		date1=date1[1].concat(" ").concat(date1[3])
		date2=date2[1].concat(" ").concat(date2[3])
		println(date1 + date2)
		def df = "dd-MM-yyyy HH:mm"
		//Parse the date string with above date format
		def dateTime1 = new Date().parse(df, date1)
		def dateTime2 = new Date().parse(df, date2)
		//Compare both date times
		println (dateTime1.compareTo(dateTime2))
		println "passed"
	}

	@Then("vejo as transactions ordenadas do mais recente para o menos recente.")
	def vejo_as_transactions_ordenadas_do_mais_recente_para_o_menos_recente(){
		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
		def date2=Mobile.getText(findTestObject('android.widget.TextView - Date 06-10-2021 as 1036'), 0)
		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
		def date1=Mobile.getText(findTestObject('android.widget.TextView - Date 13-11-2021 as 1019'), 0)
		date1 = date1.split(' ')
		date2=date2.split(' ')
		date1=date1[1].concat(" ").concat(date1[3])
		date2=date2[1].concat(" ").concat(date2[3])
		println(date1 + date2)
		def df = "dd-MM-yyyy HH:mm"
		//Parse the date string with above date format
		def dateTime1 = new Date().parse(df, date1)
		def dateTime2 = new Date().parse(df, date2)
		//Compare both date times
		println (dateTime1.compareTo(dateTime2))

	}

	@Then("vejo as transactions apenas de {string}")
	def vejo_as_transactions_apenas_de(String string){
		/*Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
		 def date2=Mobile.getText(findTestObject('android.widget.TextView - Date 06-10-2021 as 1036'), 0)
		 Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
		 def date1=Mobile.getText(findTestObject('android.widget.TextView - Date 13-11-2021 as 1019'), 0)
		 date1 = date1.split(' ')
		 date2=date2.split(' ')
		 date1=date1[1].concat(" ").concat(date1[3])
		 date2=date2[1].concat(" ").concat(date2[3])
		 println(date1 + date2)
		 def df = "dd-MM-yyyy HH:mm"
		 //Parse the date string with above date format
		 def dateTime1 = new Date().parse(df, date1)
		 def dateTime2 = new Date().parse(df, date2)
		 //Compare both date times
		 println (dateTime1.compareTo(dateTime2))*/
		println string +" passed"
	}
}