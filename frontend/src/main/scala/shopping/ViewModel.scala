package shopping

final case class Item(id: Int, name: String)
final case class SelectableItem(item: Item, selected: Boolean)

final case class ViewModel(items: Map[Int, SelectableItem])

// Source of Items
// TODO: introduce categories:: Level1 selection
object Items {
  val defaultItems: List[Item] = List(
    Item(id = 5, name = "Mineral Water"),
    Item(id = 7, name = "Bread"),
    Item(id = 11, name = "Cats food"),
    Item(id = 9, name = "Eggs")
  )
}

object ViewModel {
  val defaultSelectableList: Map[Int, SelectableItem] = Items.defaultItems
    .map(item => (item.id, SelectableItem(item = item, selected = false)))
    .toMap
}
