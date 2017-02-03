package mo.gumtree.addressbook

import java.text.SimpleDateFormat

import org.mockito.Mockito._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar

/**
  * Created by mo on 02/02/17.
  */
class AddressServiceImplTest extends FunSpec with AddressServiceTestFixture with MockitoSugar {

  val mockFileReader = mock[FileReaderImpl]

  describe("count males") {
    it("should find 3 males") {
      when(mockFileReader.contacts).thenReturn(contacts)
      val addressService = new AddressServiceImpl(mockFileReader)
      addressService.maleCount shouldBe Right(3)
    }

    it("should find 0 males") {
      when(mockFileReader.contacts).thenReturn(contactsNoMale)
      val addressService = new AddressServiceImpl(mockFileReader)
      addressService.maleCount shouldBe Right(0)
    }

    it("should return an error for an empty addressbook") {
      when(mockFileReader.contacts).thenReturn(contactsEmpty)
      val addressService = new AddressServiceImpl(mockFileReader)
      addressService.maleCount shouldBe Left(AddressError("addressBook is empty"))
    }
  }

  describe("find oldest") {
    it("should find \"Wes Jackson\" to be the oldest") {
      when(mockFileReader.contacts).thenReturn(contacts)
      val addressService = new AddressServiceImpl(mockFileReader)
      addressService.oldest shouldBe Right("Wes Jackson")
    }

    it("should return an error for an empty addressbook") {
      when(mockFileReader.contacts).thenReturn(contactsEmpty)
      val addressService = new AddressServiceImpl(mockFileReader)
      addressService.maleCount shouldBe Left(AddressError("addressBook is empty"))
    }
  }

  describe("compare dateOfBirth") {
    it("Bill should be 10 days older than Paul ") {
      when(mockFileReader.contacts).thenReturn(contactsDobComparison)
      val addressService = new AddressServiceImpl(mockFileReader)
      addressService.compareOlderToYoung("Bill", "Paul") shouldBe Right(10)
    }
  }
}

trait AddressServiceTestFixture {
  val dateFormat = new SimpleDateFormat("dd/mm/yy")

  val contacts: Set[Contact] = Set(
    Contact("Bill McKnight", Male, dateFormat.parse("16/03/77")),
    Contact("Paul Robinson", Male, dateFormat.parse("15/01/85")),
    Contact("Gemma Lane", Female, dateFormat.parse("20/11/91")),
    Contact("Sarah Stone", Female, dateFormat.parse("20/09/80")),
    Contact("Wes Jackson", Male, dateFormat.parse("14/08/74")))

  val contactsNoMale: Set[Contact] = Set(
    Contact("Gemma Lane", Female, dateFormat.parse("20/11/91")),
    Contact("Sarah Stone", Female, dateFormat.parse("20/09/80")))

  val contactsEmpty: Set[Contact] = Set()

  val contactsDobComparison = Set(
    Contact("Bill McKnight", Male, dateFormat.parse("10/03/77")),
    Contact("Paul Robinson", Male, dateFormat.parse("20/03/77")),
    Contact("Gemma Lane", Female, dateFormat.parse("20/11/91")))
}
