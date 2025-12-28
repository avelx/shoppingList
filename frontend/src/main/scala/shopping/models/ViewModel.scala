package shopping.models

import com.raquo.airstream.web.WebStorageVar
import io.bullet.borer.AdtEncodingStrategy
import io.bullet.borer.Codec
import io.bullet.borer.Json
import io.bullet.borer.derivation.MapBasedCodecs._
import shopping._
import shopping.models.ViewModelState.BasketView
import shopping.models.ViewModelState.CategoriesView
import shopping.models.ViewModelState.ItemByCategoryView

import java.nio.charset.StandardCharsets
import scala.util.Try

final case class Category(cid: String, name: String, desc: String) derives Codec
final case class Item(id: String, name: String) derives Codec
final case class SelectableItem(
    item: Item,
    selected: Boolean,
    num: Option[Int] = None
) derives Codec

enum ViewModelState:
  case CategoriesView
  case ItemByCategoryView
  case BasketView

final case class ViewModel(
    state: ViewModelState,
    selectedCategory: Option[Category],
    items: Map[String, List[SelectableItem]],
    basket: Map[String, SelectableItem]
) derives Codec {

  val selectedCategoryItems: List[SelectableItem] = {
    selectedCategory match {
      case Some(category) =>
        this.items
          .get(category.cid)
          .getOrElse(List.empty)
          .sortBy(_.item.name)
          .filter(_.selected == false)
      case None =>
        List.empty
    }

  }

  val selectedItems: List[SelectableItem] =
    items.values.flatten.toList
      .filter(_.selected)
      .zipWithIndex
      .map(e => e._1.copy(num = Some(e._2 + 1)))

  val pageTitle = state match {
    case CategoriesView =>
      "Category Selection"
    case ItemByCategoryView =>
      "Select Item"
    case BasketView =>
      "Basket Items"
  }

  val categories = Categories.all
}

object ViewModel {
  given viewModelStateCodec: Codec[ViewModelState] = deriveCodec

  val defaultViewModel =
    ViewModel(
      state = CategoriesView,
      selectedCategory = None,
      items = Items.defaultItemsByCategory,
      basket = Map.empty
    )

  val viewModelVar = WebStorageVar
    .localStorage(
      key = "ShoppingListViewModel",
      syncOwner = None
    )
    .withCodec[ViewModel](
      encode = hs => Json.encode(hs).toUtf8String,
      decode = jsonStr =>
        Json
          .decode(jsonStr.getBytes(StandardCharsets.UTF_8))
          .to[ViewModel]
          .valueTry,
      default = Try(defaultViewModel)
    )
}
