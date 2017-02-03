package mo.gumtree.addressbook

import java.util.Date

import scala.util.Try

/**
  * Created by mo on 02/02/2017.
  */

sealed trait Gender

case object Male extends Gender

case object Female extends Gender

case class Contact(name: String, gender: Gender, dob: Date)

object Contact {
  import CellMapping._

  def toContact(row: String)(implicit addressConfig: AddressConfig) = {
    // extracting the rows
    val cells = row.split(",")

    // validating the number of columns
    require(cells.length >= 3, "expecting at least 3 columns per row ")

    //extracting the values from the cells
    val name = Try[String](cells(NAME)).getOrElse(throw InvalidValueException(s"${cells(NAME)} not valid ")).trim

    val gender = Try[String](cells(GENDER)).getOrElse(throw InvalidValueException(s"${cells(GENDER)} not  valid ")).trim match {
      case "Male" => Male
      case "Female" => Female
      case _ => throw InvalidValueException("gender not valid ")
    }

    val dob = Try(cells(DOB)).getOrElse(throw InvalidValueException(s"${cells(DOB)} not  valid ")).trim

    Contact(name, gender, addressConfig.dataFormat.parse(dob))
  }
}

object CellMapping {
  val NAME = 0
  val GENDER = 1
  val DOB = 2
}