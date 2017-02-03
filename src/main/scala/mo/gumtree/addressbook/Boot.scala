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

  addressService.oldest match {
    case Right(name) => logger.info(s"The oldest person is $name")
    case Left(error) => logger.error(error.message)
  }

  addressService.compareOlderToYoung("Bill", "Paul") match {
    case Right(days) => logger.info(s"Bill is older than paul ${days} days")
    case Left(error) => logger.error(error.message)
  }
}