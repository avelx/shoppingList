package shopping.views

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.{_, given}
import com.raquo.laminar.api.features.unitArrows
import shopping.Controller
import shopping.models.SelectableItem
import shopping.models.ViewModel
import shopping.models.ViewModelState.ItemByCategoryView

trait ItemView(controller: Controller) {
  def itemsView(vm: Signal[ViewModel]): Div = {
    div(
      display <-- vm.map(m =>
        if (m.state == ItemByCategoryView) then "block" else "none"
      ),
      div(
        div(
          div(
            className := "overflow-x-auto",
            table(
              className := "w-full",
              thead(
                className := "bg-gray-50 border-b",
                tr(
                  th(
                    className := "px-6 py-4 text-left text-sm font-semibold text-gray-700",
                    "Item"
                  ),
                  th(
                    className := "px-4 py-4 text-left text-sm font-semibold text-gray-700",
                    "Select"
                  )
                )
              ),
              tbody(
                className := "divide-y divide-gray-200",
                children <-- vm
                  .map(_.selectedCategoryItems)
                  .map(_.map(item(_)))
              )
            )
          )
        )
      )
    )
  }

  def item(selectable: SelectableItem): Node = {
    tr(
      td(
        className := "px-6 py-4 text-gray-800 font-medium",
        selectable.item.name
      ),
      td(
        className := "px-4 py-4 text-center",
        if (selectable.selected) {
          div(
            onClick --> controller.onSelectItem(selectable.item.id),
            svg.svg(
              svg.fill := "none",
              svg.stroke := "currentColor",
              svg.strokeWidth := "{1.5}",
              svg.path(
                svg.d := "m20.25 7.5-.625 10.632a2.25 2.25 0 0 1-2.247 2.118H6.622a2.25 2.25 0 0 1-2.247-2.118L3.75 7.5m6 4.125 2.25 2.25m0 0 2.25 2.25M12 13.875l2.25-2.25M12 13.875l-2.25 2.25M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125Z"
              ),
              svg.className := "size-6",
              onClick --> controller.onSelectItem(selectable.item.id)
            )
          )
        } else {
          div(
            onClick --> controller.onSelectItem(selectable.item.id),
            svg.svg(
              svg.fill := "none",
              svg.stroke := "currentColor",
              svg.strokeWidth := "{1.5}",
              svg.path(
                svg.d := "m12.75 15 3-3m0 0-3-3m3 3h-7.5M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
              ),
              svg.className := "size-6",
              onClick --> controller.onSelectItem(selectable.item.id)
            )
          )
        }
      )
    )
  }

}
