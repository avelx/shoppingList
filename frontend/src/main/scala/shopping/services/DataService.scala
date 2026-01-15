package shopping.services

import io.bullet.borer.Json
import shopping.models.Category
import shopping.models.CategoryWrapper
import shopping.models.ItemWrapper
import shopping.models.SelectableItem

import java.nio.charset.StandardCharsets

class DataService {

  def parseResponseToCategories(response: String): List[Category] = {
    Json
      .decode(response.getBytes(StandardCharsets.UTF_8))
      .to[CategoryWrapper]
      .valueTry
      .toOption
      .map(_.data)
      .getOrElse(List.empty)
  }

  def parseResponseToItems(
      response: String
  ): Map[String, List[SelectableItem]] = {
    Json
      .decode(response.getBytes(StandardCharsets.UTF_8))
      .to[ItemWrapper]
      .valueTry
      .toOption
      .map(_.data)
      .getOrElse(Map.empty)
  }

}
