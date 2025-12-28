package shopping.models

import shopping.models.CategoriesData.beverages
import shopping.models.CategoriesData.dairy
import shopping.models.CategoriesData.pantry
import shopping.models.CategoriesData.produce

object ItemsData {

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
