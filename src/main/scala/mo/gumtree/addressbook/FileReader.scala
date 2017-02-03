package mo.gumtree.addressbook

import scala.io.Source

/**
  * Created by mo on 02/02/17.
  */
trait FileReader {
  def contacts: Set[Contact]
}

class FileReaderImpl(implicit val addressConfig: AddressConfig) extends FileReader {
  override def contacts: Set[Contact] =
    Source.fromFile(addressConfig.filePath).getLines.map(line => Contact.toContact(line)).toSet
}