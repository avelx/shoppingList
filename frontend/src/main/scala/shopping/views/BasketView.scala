package shopping.views

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.{_, given}
import com.raquo.laminar.api.features.unitArrows
import shopping.Controller
import shopping.models.SelectableItem
import shopping.models.ViewModel
import shopping.models.ViewModelState.BasketView

trait BasketView(controller: Controller) {

  def viewBasket(vm: Signal[ViewModel]): Div = {
    div(
      display <-- vm.map(m =>
        if (m.state == BasketView) then "block" else "none"
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
                    "Unselect"
                  )
                )
              ),
              tbody(
                className := "divide-y divide-gray-200",
                children <-- vm
                  .map(_.selectedItems)
                  .map(_.map(basketItem(_)))
              )
            )
          )
        )
      )
    )
  }

  private def basketItem(selectable: SelectableItem): Node = {
    tr(
      td(
        className := "px-6 py-4 text-gray-800 font-medium",
        s"${selectable.num.get.toString} :: ${selectable.item.name}"
      ),
      td(
        className := "px-4 py-4 text-center",
        div(
          onClick --> controller.onUnSelectedInBasket(selectable.item.id),
          svg.svg(
            svg.fill := "none",
            svg.stroke := "currentColor",
            svg.strokeWidth := "{1.5}",
            svg.path(
              svg.d := "m20.25 7.5-.625 10.632a2.25 2.25 0 0 1-2.247 2.118H6.622a2.25 2.25 0 0 1-2.247-2.118L3.75 7.5m6 4.125 2.25 2.25m0 0 2.25 2.25M12 13.875l2.25-2.25M12 13.875l-2.25 2.25M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125Z"
            ),
            svg.className := "size-6",
            onClick --> controller.onUnSelectedInBasket(selectable.item.id)
          )
        )
      )
    )
  }
}
