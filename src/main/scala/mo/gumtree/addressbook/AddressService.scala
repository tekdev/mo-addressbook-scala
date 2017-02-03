package mo.gumtree.addressbook

import scala.util.Try

/**
  * Created by mo on 02/02/17.
  */
trait AddressService {
  val maleCount: Either[AddressError, Int]

  val oldest: Either[AddressError, String]
}

class AddressServiceImpl(fileReader: FileReader) extends AddressService {
  val contacts = fileReader.contacts

  val emptyAddressBookResp = Left(AddressError("addressBook is empty"))

  override val maleCount: Either[AddressError, Int] =
    if (contacts.nonEmpty)
      Right(contacts.groupBy(_.gender).mapValues(_.size).getOrElse(Male, 0))
    else emptyAddressBookResp

  override val oldest: Either[AddressError, String] = Try(contacts.minBy(_.dob).name)
    .toOption
    .map(Right(_))
    .getOrElse(emptyAddressBookResp)

}

case class AddressError(message: String)