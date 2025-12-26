package shopping

final case class Item(id: Int, name: String)
final case class ViewModel(items: Map[Int, Item])

object ViewModel {
  val defaultItems: Map[Int, Item] = Map(
    1 -> Item(id = 5, name = "Mineral Water"),
    2 -> Item(id = 7, name = "Bread"),
    3 -> Item(id = 11, name = "Cats food"),
    6 -> Item(id = 9, name = "Eggs")
  )
}
