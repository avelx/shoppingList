package shopping

import com.raquo.laminar.api.L.Var
import shopping.models.Category
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
                  val updatedBasket = {
                    if (!itemUpdate.selected) {
                      vm.basket.filter(_.item.id == itemUpdate.item.id)
                    } else {
                      vm.basket :+ itemUpdate
                    }
                  }
                  vm.copy(items = updated, basket = updatedBasket)
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
      vm.basket.find(_.item.id == id) match {
        case Some(selectable) =>
          val itemUpdate = selectable.copy(selected = false)
          // Fina categoryId
          val cid = vm.items
            .find(p => p._2.find(e => e.item.id == id).isDefined)
            .map(_._1)
            .getOrElse("")

          val basketItem = vm.basket.filterNot(e => e.item.id == id)

          vm.categories
            .find(c => c.cid == cid)
            .map(category =>
              val updated = vm.items + (category.cid -> (vm.items
                .get(category.cid)
                .get
                .filterNot(e => e.item.id == id) :+ itemUpdate))
              vm.copy(items = updated, basket = basketItem)
            )
            .getOrElse(vm)
        case None =>
          vm
      }
    )
  }

  def onBtnArchive() = {
    // Archive items:

    // Empty view model
    dynModel.now().basket.foreach(e => onUnSelectedInBasket(e.item.id))
  }

}
