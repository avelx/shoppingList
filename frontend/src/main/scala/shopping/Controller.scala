package shopping

import com.raquo.laminar.api.L.Var
import shopping.ViewModel
import shopping.ViewModelState.ItemByCategoryView

// Various actions for view model
class Controller(dynModel: Var[ViewModel]) {

  def onViewButtonPressed(state: ViewModelState) = {
    dynModel.update(vm => vm.copy(state = state))
  }

  def onCategorySelected(category: Category) = {
    dynModel.update(vm =>
      vm.copy(state = ItemByCategoryView, selectedCategory = Some(category))
    )
  }

  def onSelectItem(id: Int): Unit = {
    dynModel.update(vm =>
      vm.selectedCategory match {
        case Some(category) =>
          vm.items.get(category) match {
            case Some(items) =>
              val item = items.find(_.item.id == id).get
              val itemUpdate = item.copy(selected = !item.selected)
              val updated = vm.items + (category -> (items
                .filterNot(e => e.item.id == id) :+ itemUpdate))
              vm.copy(items = updated)

            case None =>
              vm
          }
        case None => vm
      }
    )
  }
}
