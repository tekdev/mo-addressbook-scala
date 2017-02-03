package mo.gumtree.addressbook

import java.util.concurrent.TimeUnit._

import scala.util.Try
/**
  * Created by mo on 02/02/17.
  */
trait AddressService {
  val maleCount: Either[AddressError, Int]

  val oldest: Either[AddressError, String]

  def compareOlderToYoung(olderPersonName: String, youngerPersonName: String): Either[AddressError, Long]
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

  override def compareOlderToYoung(olderPersonName: String, youngerPersonName: String): Either[AddressError, Long] = {
    val groupedByname = contacts.groupBy(_.name)

    val oldPMap = groupedByname.filterKeys(_.startsWith(olderPersonName)).headOption
    val youngPMap = groupedByname.filterKeys(_.startsWith(youngerPersonName)).headOption

    if (oldPMap.isDefined && youngPMap.isDefined) {
      // extracting from maps
      val p1 = oldPMap.head._2.head
      val p2 = youngPMap.head._2.head

      //comparing the date
      p2.dob.getTime - p1.dob.getTime match {
        case a if (a == 0) => Left(AddressError(s"$olderPersonName has the same age as $youngerPersonName"))
        case a if (a < 0) => Left(AddressError(s"$olderPersonName is younger than $youngerPersonName"))
        case r@_ => Right(DAYS.convert(r, MILLISECONDS))
      }
    } else {
      // case where either both or one of the person is not found
      Left(AddressError(s"both $olderPersonName and $youngerPersonName contacts has to be defined in order to compare DOB"))
    }
  }
}

case class AddressError(message: String)