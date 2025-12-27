package shopping

import com.raquo.airstream.web.WebStorageVar
import io.bullet.borer.AdtEncodingStrategy
import io.bullet.borer.Codec
import io.bullet.borer.Json
import io.bullet.borer.derivation.MapBasedCodecs._
import shopping.Categories._
import shopping.ViewModelState.BasketView
import shopping.ViewModelState.CategoriesView
import shopping.ViewModelState.ItemByCategoryView

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

// Source of Items
object Items {

  // List of items
  private val apples = Item("1", "Apples")
  private val berries = Item("2", "Berries")

  private val minWater = Item(id = "5", name = "Mineral Water")
  private val bread = Item(id = "7", name = "Bread")

  private val beer = Item(id = "11", name = "Beer")
  private val wine = Item(id = "12", name = "Wine")

  // Pantry
  private val pasta = Item(id = "21", name = "Pasta")
  private val rice = Item(id = "22", name = "Rice")
  private val cereal = Item(id = "23", name = "Cereal")
  private val sugar = Item(id = "24", name = "Sugar")
  private val oils = Item(id = "25", name = "Oils")

  // Dairy
  private val milk = Item(id = "50", name = "Milk")
  private val cheese = Item(id = "51", name = "Cheese")
  private val yogurt = Item(id = "52", name = "Yogurt")
  private val butter = Item(id = "53", name = "Butter")
  private val eggs = Item(id = "54", name = "Eggs")

  // category -> item relationship
  val defaultItemsByCategory: Map[String, List[SelectableItem]] =
    Map[String, List[SelectableItem]](
      produce.cid -> List(
        SelectableItem(item = apples, false),
        SelectableItem(item = berries, false)
      ),
      beverages.cid -> List(
        SelectableItem(item = beer, false),
        SelectableItem(item = wine, false)
      ),
      pantry.cid -> List(
        SelectableItem(item = pasta, false),
        SelectableItem(item = rice, false),
        SelectableItem(item = cereal, false),
        SelectableItem(item = sugar, false),
        SelectableItem(item = oils, false)
      ),
      dairy.cid -> List(
        SelectableItem(item = milk, false),
        SelectableItem(item = cheese, false),
        SelectableItem(item = yogurt, false),
        SelectableItem(item = butter, false),
        SelectableItem(item = eggs, false)
      )
    )
}

object Categories {
  val produce = Category(cid = "1", "Produce", "Fruits & Vegetables")

  val meatAndSeadFood = Category(
    cid = "2",
    "Meat & Seafood",
    "Chicken, beef, fish, shrimp, deli meats"
  )

  val bakery =
    Category(cid = "3", "Bakery", "Bread, bagels, muffins, tortillas, cakes")

  val beverages = Category(
    cid = "4",
    "Beverages",
    "Coffee, tea, juice, soda, water, beer, wine"
  )

  val frozenFoods = Category(
    cid = "5",
    "Frozen Foods",
    "Ice cream, frozen vegetables, pizzas, ready meals. "
  )

  val pantry = Category(
    cid = "6",
    "Pantry/Dry Goods",
    "Pasta, rice, cereal, flour, sugar, spices, canned goods, oils, sauces."
  )

  val dairy = Category(
    cid = "11",
    "Dairy & Refrigerated",
    "Milk, cheese, yogurt, butter, eggs, prepared meals"
  )

  val all: List[Category] = List(
    produce,
    meatAndSeadFood,
    bakery,
    beverages,
    frozenFoods,
    pantry,
    dairy
  )
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
