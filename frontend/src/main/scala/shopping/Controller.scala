package shopping

import com.raquo.laminar.api.L.Var
import shopping.models.Category
import shopping.models.ItemsData
import shopping.models.SelectableItem
import shopping.models.ViewModel
import shopping.models.ViewModelState
import shopping.models.ViewModelState.ItemByCategoryView

// Various actions for view model
class Controller(dynModel: Var[ViewModel]) {

  // Fetch response processing:
  def onCategoriesFetch(loadedCategories: List[Category]): Unit = {
    dynModel.update(vm => vm.copy(categories = loadedCategories))
  }

  def onItemsFetch(loadedItems: Map[String, List[SelectableItem]]): Unit = {
    dynModel.update(vm => vm.copy(items = loadedItems))
  }

  // Main View
  def onViewButtonPressed(state: ViewModelState) = {
    dynModel.update(vm => vm.copy(state = state))
  }

  // Categories View
  def onCategorySelected(category: Category) = {
    dynModel.update(vm =>
      vm.copy(state = ItemByCategoryView, selectedCategory = Some(category))
    )
  }

  // ItemSelection view
  def onSelectItem(id: String): Unit = {
    dynModel.update(vm =>
      vm.selectedCategory match {
        case Some(category) =>
          vm.items.get(category.cid) match {
            case Some(items) =>
              items.find(_.item.id == id) match {
                case Some(item) =>
                  val itemUpdate = item.copy(selected = !item.selected)
                  val updated = vm.items + (category.cid -> (items
                    .filterNot(e => e.item.id == id) :+ itemUpdate))
                  vm.copy(items = updated)
                case None =>
                  println(s"onSelectItem::Unable to locate item with id: $id")
                  vm
              }
            case None =>
              vm
          }
        case None =>
          println(s"onSelectItem::Unable to locate item with id2: $id")
          vm
      }
    )
  }

  // BasketView
  def onUnSelectedInBasket(id: String): Unit = {
    dynModel.update(vm =>
      vm.selectedItems.find(_.item.id == id) match {
        case Some(selectable) =>
          val itemUpdate = selectable.copy(selected = false)
          val category = ItemsData.getCategoryByItemId(selectable.item.id).get
          val updated = vm.items + (category.cid -> (vm.items
            .get(category.cid)
            .get
            .filterNot(e => e.item.id == id) :+ itemUpdate))
          vm.copy(items = updated)
        case None =>
          vm
      }
    )
  }

}
