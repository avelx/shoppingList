package shopping

import shopping.Categories._
import shopping.ViewModelState.BasketView
import shopping.ViewModelState.CategoriesView
import shopping.ViewModelState.ItemByCategoryView

final case class Category(cid: Int, name: String, desc: String)
final case class Item(id: Int, name: String)
final case class SelectableItem(item: Item, selected: Boolean)

enum ViewModelState:
  case CategoriesView
  case ItemByCategoryView
  case BasketView

final case class ViewModel(
    state: ViewModelState,
    selectedCategory: Option[Category],
    items: Map[Category, List[SelectableItem]],
    basket: Map[Int, SelectableItem]
) {

  val selectedCategoryItems: List[SelectableItem] = {
    selectedCategory match {
      case Some(category) =>
        this.items
          .get(category)
          .getOrElse(List.empty)
          .sortBy(_.item.name)
          .filter(_.selected == false)
      case None =>
        List.empty
    }

  }

  val selectedItems: List[SelectableItem] =
    items.values.flatten.toList.filter(_.selected)

  val pageTitle = state match {
    case CategoriesView =>
      "Category Selection"
    case ItemByCategoryView =>
      "Select Item"
    case BasketView =>
      "Basket Items"
  }
}

// Source of Items
object Items {

  // List of items
  private val apples = Item(1, "Apples")
  private val berries = Item(2, "Berries")

  private val minWater = Item(id = 5, name = "Mineral Water")
  private val bread = Item(id = 7, name = "Bread")
  private val eggs = Item(id = 9, name = "Eggs")

  private val beer = Item(id = 11, name = "Beer")
  private val wine = Item(id = 12, name = "Wine")

  // Pantry
  private val pasta = Item(id = 21, name = "Pasta")
  private val rice = Item(id = 22, name = "Rice")
  private val cereal = Item(id = 23, name = "Cereal")
  private val sugar = Item(id = 24, name = "Sugar")
  private val oils = Item(id = 25, name = "Oils")

  // category -> item relationship
  val defaultItemsByCategory: Map[Category, List[SelectableItem]] =
    Map[Category, List[SelectableItem]](
      produce -> List(
        SelectableItem(item = apples, false),
        SelectableItem(item = berries, false)
      ),
      beverages -> List(
        SelectableItem(item = beer, false),
        SelectableItem(item = wine, false)
      ),
      pantry -> List(
        SelectableItem(item = pasta, false),
        SelectableItem(item = rice, false),
        SelectableItem(item = cereal, false),
        SelectableItem(item = sugar, false),
        SelectableItem(item = oils, false)
      )
    )
}

object Categories {
  val produce = Category(cid = 1, "Produce", "Fruits & Vegetables")

  val meatAndSeadFood = Category(
    cid = 2,
    "Meat & Seafood",
    "Chicken, beef, fish, shrimp, deli meats"
  )

  val bakery =
    Category(cid = 3, "Bakery", "Bread, bagels, muffins, tortillas, cakes")

  val beverages = Category(
    cid = 4,
    "Beverages",
    "Coffee, tea, juice, soda, water, beer, wine"
  )

  val frozenFoods = Category(
    cid = 5,
    "Frozen Foods",
    "Ice cream, frozen vegetables, pizzas, ready meals. "
  )

  val pantry = Category(
    cid = 6,
    "Pantry/Dry Goods",
    "Pasta, rice, cereal, flour, sugar, spices, canned goods, oils, sauces."
  )
}

object ViewModel {
  val defaultViewModel =
    ViewModel(
      state = CategoriesView,
      selectedCategory = None,
      items = Items.defaultItemsByCategory,
      basket = Map.empty
    )
}
