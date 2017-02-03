package mo.gumtree.addressbook

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging

/**
  * Created by mo on 02/02/17.
  */
object Boot extends App with Settings with StrictLogging {
  override val config = ConfigFactory.load()

  implicit val addConfig = addressConfig

  val fileReader = new FileReaderImpl
  val addressService = new AddressServiceImpl(fileReader)

  addressService.maleCount match {
    case Right(count) => logger.info(s"There are $count males in the addressBook")
    case Left(e) => logger.error(e.message)
  }

}