package mo.gumtree.addressbook

/**
  * Created by mo on 02/02/17.
  */
trait AddressService {
  val maleCount: Either[AddressError, Int]
}

class AddressServiceImpl(fileReader: FileReader) extends AddressService {
  val contacts = fileReader.contacts

  override val maleCount: Either[AddressError, Int] =
    if (contacts.nonEmpty)
      Right(contacts.groupBy(_.gender).mapValues(_.size).getOrElse(Male, 0))
    else Left(AddressError("addressBook is empty"))

}

case class AddressError(message: String)