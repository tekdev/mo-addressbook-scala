package mo.gumtree.addressbook

import java.text.SimpleDateFormat

import com.typesafe.config.ConfigFactory
import mo.gumtree.addressbook.AddressServiceImplTest.{DummyFileReaderImpl, DummyFileReaderImplEmpty, DummyFileReaderImplNoMale}
import org.scalatest.{FlatSpec, FunSpec}
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

/**
  * Created by mo on 02/02/17.
  */
class AddressServiceImplTest extends FunSpec with AddressServiceTestFixture with MockitoSugar {

  val mockFileReader = mock[FileReaderImpl]

  describe("count males") {
    it("should find 3 males") {
      val addressService = new AddressServiceImpl(new DummyFileReaderImpl)
      addressService.maleCount shouldBe Right(3)
    }

    it("should find 0 males") {
      val addressService = new AddressServiceImpl(new DummyFileReaderImplNoMale)
      addressService.maleCount shouldBe Right(0)
    }

    it("should return an error for an empty addressbook") {
      val addressService = new AddressServiceImpl(new DummyFileReaderImplEmpty)
      addressService.maleCount shouldBe Left(AddressError("addressBook is empty"))
    }
  }

  describe("find oldest") {
    it("should find \"Wes Jackson\" to be the oldest") {
      val addressService = new AddressServiceImpl(new DummyFileReaderImpl)
      addressService.oldest shouldBe Right("Wes Jackson")
    }

    it("should return an error for an empty addressbook") {
      val addressService = new AddressServiceImpl(new DummyFileReaderImplEmpty)
      addressService.maleCount shouldBe Left(AddressError("addressBook is empty"))
    }
  }

  describe("compare dateOfBirth") {
    it("Bill should be 10 days older than Paul ") {
      when(mockFileReader.contacts).thenReturn(dobComparisonContacts)
      val addressService = new AddressServiceImpl(mockFileReader)
      addressService.compareOlderToYoung("Bill", "Paul") shouldBe Right(10)
    }
  }
}

trait AddressServiceTestFixture {
  val dateFormat = new SimpleDateFormat("dd/mm/yy")

  val dobComparisonContacts = Set(
    Contact("Bill McKnight", Male, dateFormat.parse("10/03/77")),
    Contact("Paul Robinson", Male, dateFormat.parse("20/03/77")),
    Contact("Gemma Lane", Female, dateFormat.parse("20/11/91")))
}

object AddressServiceImplTest {
  val dateFormat = new SimpleDateFormat("dd/mm/yy")

  class DummyFileReaderImpl extends FileReader {
    override lazy val contacts: Set[Contact] = Set(
      Contact("Bill McKnight", Male, dateFormat.parse("16/03/77")),
      Contact("Paul Robinson", Male, dateFormat.parse("15/01/85")),
      Contact("Gemma Lane", Female, dateFormat.parse("20/11/91")),
      Contact("Sarah Stone", Female, dateFormat.parse("20/09/80")),
      Contact("Wes Jackson", Male, dateFormat.parse("14/08/74")))
  }

  class DummyFileReaderImplNoMale extends FileReader {
    override lazy val contacts: Set[Contact] = Set(
      Contact("Gemma Lane", Female, dateFormat.parse("20/11/91")),
      Contact("Sarah Stone", Female, dateFormat.parse("20/09/80")))
  }

  class DummyFileReaderImplEmpty extends FileReader {
    override lazy val contacts: Set[Contact] = Set()
  }

}