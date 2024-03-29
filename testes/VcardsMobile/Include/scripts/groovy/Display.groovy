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



class Display {
	@Given("tendo a aplicacao aberta")
	public void tendo_a_aplicacao_aberta() {
		Mobile.startApplication('C:\\Projetos\\TAES\\project\\app\\build\\outputs\\apk\\debug\\app-debug.apk',
				true)
		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
	}

	@Given("Tendo a dashboard aberta")
	public void tendo_a_dashboard_aberta() {
		Mobile.startApplication('C:\\Projetos\\TAES\\project\\app\\build\\outputs\\apk\\debug\\app-debug.apk',
				false)
		Mobile.verifyElementVisible(findTestObject('Object Repository/android.widget.TextView - Balance (3)'), 0)
	}

	@Given("estou na lista de contactos")
	def estou_na_lista_de_contactos() {
		Mobile.startApplication('C:\\Projetos\\TAES\\project\\app\\build\\outputs\\apk\\debug\\app-debug.apk',
				false)
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - SEND MONEY'), 0)
		Mobile.delay(3, FailureHandling.STOP_ON_FAILURE)
	}

	@Given("estou no ecra de enviar dinheiro")
	def ecra_enviar_dinheiro() {
		Mobile.startApplication('C:\\Projetos\\TAES\\project\\app\\build\\outputs\\apk\\debug\\app-debug.apk',
				false)

		Mobile.tap(findTestObject('Object Repository/android.widget.Button - SEND MONEY'), 0)

		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Search'), 'Teste', 0)

		Mobile.tap(findTestObject('Object Repository/android.widget.contactos'), 0)
	}

	@Then("entro na Dashboard da aplicação e vejo o balance")
	public void entro_na_Dashboard_da_aplicação_e_vejo_o_balance() {

		Mobile.verifyElementExist(findTestObject('Object Repository/android.widget.TextView - Balance (1)'), 0)

		Mobile.closeApplication()
	}
}