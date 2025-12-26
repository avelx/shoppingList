package shopping

import com.raquo.laminar.api.L.Var
import shopping.ViewModel

// Various actions for view model
class Controller(dynModel: Var[ViewModel]) {
  def onSelect(id: Int): Unit = {
    dynModel.update(vm =>
      vm.items.get(id) match {
        case Some(item) =>
          var itemsUpdate = vm.items.filterNot(_._1 == id)
          vm.copy(
            items = itemsUpdate + (id -> SelectableItem(
              item = item.item,
              selected = !item.selected
            ))
          )
        case None =>
          vm
      }
    )
  }
}
