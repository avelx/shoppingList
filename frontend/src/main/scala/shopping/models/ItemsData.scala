package shopping.models

import shopping.models.CategoriesData.beverages
import shopping.models.CategoriesData.dairy
import shopping.models.CategoriesData.frozenFoods
import shopping.models.CategoriesData.pantry
import shopping.models.CategoriesData.produce

object ItemsData {

  // produce: 100-199
  private val apples = Item("1", "Apples")
  private val berries = Item("2", "Berries")
  private val minWater = Item(id = "5", name = "Mineral Water")
  private val bread = Item(id = "6", name = "Bread")
  private val bananas = Item(id = "7", name = "Bananas")

  // beverages: 500-600
  private val beer = Item(id = "500", name = "Beer")
  private val wine = Item(id = "501", name = "Wine")
  private val coffee = Item(id = "502", name = "Coffee")
  private val blackTea = Item(id = "553", name = "Black Tea")
  private val greenTea = Item(id = "554", name = "Green Tea")

  // Pantry: 700-800
  private val pasta = Item(id = "700", name = "Pasta")
  private val rice = Item(id = "701", name = "Rice")
  private val cereal = Item(id = "702", name = "Cereal")
  private val sugar = Item(id = "703", name = "Sugar")
  private val oils = Item(id = "704", name = "Oils")

  // Dairy :: 200-300
  private val milk = Item(id = "200", name = "Milk")
  private val cheese = Item(id = "201", name = "Cheese")
  private val yogurt = Item(id = "202", name = "Yogurt")
  private val butter = Item(id = "203", name = "Butter")
  private val eggs = Item(id = "204", name = "Eggs")

  private val juiceApple = Item(id = "205", name = "Apple Juice")
  private val juiceOrange = Item(id = "210", name = "Orange Juice")

  // Frozen Foods :: 400-499
  private val frozenFish = Item(id = "401", name = "Frozen Fish")
  private val frozenChicken = Item(id = "402", name = "Frozen Chicken")

  // category -> item relationship
  val defaultItemsByCategory: Map[String, List[SelectableItem]] =
    Map[String, List[SelectableItem]](
      produce.cid -> List(
        SelectableItem(item = apples, false),
        SelectableItem(item = berries, false),
        SelectableItem(item = bananas, false),
        SelectableItem(item = minWater, false)
      ),
      beverages.cid -> List(
        SelectableItem(item = beer, false),
        SelectableItem(item = wine, false),
        SelectableItem(item = coffee, false),
        SelectableItem(item = blackTea, false),
        SelectableItem(item = greenTea, false)
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
        SelectableItem(item = eggs, false),
        SelectableItem(item = juiceApple, false),
        SelectableItem(item = juiceOrange, false)
      ),
      frozenFoods.cid -> List(
        SelectableItem(item = frozenFish, false),
        SelectableItem(item = frozenChicken, false)
      )
    )

  def getCategoryByItemId(id: String): Option[Category] = {
    val res = defaultItemsByCategory
      .find(p => p._2.find(_.item.id == id).isDefined)
    res
      .map(_._1)
      .map(cid => CategoriesData.all.find(category => category.cid == cid))
      .flatten
  }
}
