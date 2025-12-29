package shopping

import scala.io.Source
import scala.util.Try

object Utils {
  def readFileFromResource(filePath: String): Either[Throwable, String] = {
    Try {
      val content = Source.fromResource(filePath).getLines.toList.mkString("\n")
      content
    }.toEither
  }
}
