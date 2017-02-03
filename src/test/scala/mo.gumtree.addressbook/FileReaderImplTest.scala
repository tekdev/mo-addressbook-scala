package mo.gumtree.addressbook

import java.io.FileNotFoundException
import java.text.SimpleDateFormat

import org.scalatest.Matchers._
import org.scalatest.FunSpec

/**
  * Created by mo on 02/02/17.
  */
class FileReaderImplTest extends FunSpec with FileReaderTestFixture {

  // normal file reading

  // wrong data format

  // wrong gender

  // missing columns

  // file not found

  describe("File reader") {
    it("should read the file correctly") {
      implicit val addressConfig = config
      val fileReader = new FileReaderImpl
      fileReader.contacts shouldBe contacts
    }
    it(s"should throw an $InvalidValueException for wrong date format") {
      implicit val addressConfig = configWrongDateFormat
      val fileReader = new FileReaderImpl
      intercept[InvalidValueException] {
        fileReader.contacts
      }
    }
    it(s"should throw an $InvalidValueException for unknown gender") {
      implicit val addressConfig = configUnknownGender
      val fileReader = new FileReaderImpl
      intercept[InvalidValueException] {
        fileReader.contacts
      }
    }

    it(s"should throw an IllegalArgumentException for missing columns") {
      implicit val addressConfig = configMissingColumns
      val fileReader = new FileReaderImpl
      intercept[IllegalArgumentException] {
        fileReader.contacts
      }
    }

    it(s"should throw an FileNotFoundException for file not found") {
      implicit val addressConfig = configFileNotFound
      val fileReader = new FileReaderImpl
      intercept[FileNotFoundException] {
        fileReader.contacts
      }
    }
  }

}

trait FileReaderTestFixture {
  val config = AddressConfig("src/test/resources/addressBook",
    new SimpleDateFormat("dd/MM/yy"))

  val configWrongDateFormat = AddressConfig("src/test/resources/addressBook",
    new SimpleDateFormat("dd-MM-yy"))

  val configUnknownGender = AddressConfig("src/test/resources/addressBookUnknownGender",
    new SimpleDateFormat("dd/MM/yy"))

  val configMissingColumns = AddressConfig("src/test/resources/addressBookMissingColumns",
    new SimpleDateFormat("dd/MM/yy"))

  val configFileNotFound = AddressConfig("src/test/resources/nonExistingFile",
    new SimpleDateFormat("dd/MM/yy"))

  val contacts = Set(
    Contact("Bill McKnight", Male, config.dateFormat.parse("16/03/77")),
    Contact("Paul Robinson", Male, config.dateFormat.parse("15/01/85")))
}
