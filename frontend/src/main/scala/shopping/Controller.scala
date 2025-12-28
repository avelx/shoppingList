package shopping

import com.raquo.laminar.api.L.Var
import shopping.models.Category
import shopping.models.ViewModel
import shopping.models.ViewModelState
import shopping.models.ViewModelState.ItemByCategoryView

// Various actions for view model
class Controller(dynModel: Var[ViewModel]) {

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
      vm.selectedCategory match {
        case Some(category) =>
          vm.items.get(category.cid) match {
            case Some(items) =>
              items.find(_.item.id == id) match {
                case Some(item) =>
                  val itemUpdate = item.copy(selected = false)
                  val updated = vm.items + (category.cid -> (items
                    .filterNot(e => e.item.id == id) :+ itemUpdate))
                  vm.copy(items = updated)
                case None =>
                  println(
                    s"onUnSelectedInBasket::Unable to locate item with id: $id"
                  )
                  vm
              }
            case None =>
              println(
                s"onUnSelectedInBasket::Unable to locate item with id2: $id"
              )
              vm
          }
        case None =>
          println(
            s"onUnSelectedInBasket::Unable to locate item with id3: $id"
          )
          vm
      }
    )
  }

}
