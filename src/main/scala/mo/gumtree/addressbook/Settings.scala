package mo.gumtree.addressbook

import java.text.SimpleDateFormat

import com.typesafe.config.Config

/**
  * Created by mo on 03/02/17.
  */
trait Settings {
  val config: Config

  lazy val addressConfig = AddressConfig(config.getString("address.config.filePath"),
    new SimpleDateFormat(config.getString("address.config.dateFormat")))
}

case class AddressConfig(filePath: String, dateFormat: SimpleDateFormat)