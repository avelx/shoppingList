package shopping

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.{_, given}
import com.raquo.laminar.api.features.unitArrows
import com.raquo.laminar.nodes.ReactiveHtmlElement

class View(controller: Controller) {

  def build(vm: Signal[ViewModel]) = {
    div(
      className := "bg-white rounded-lg shadow-lg overflow-hidden",
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
                className := "px-4 py-4 text-center text-sm font-semibold text-gray-700",
                "Selected"
              )
            )
          ),
          tbody(
            className := "divide-y divide-gray-200",
            children <-- vm.map(_.items).map(_.values.toSeq.map(item))
          )
        )
      )
    )
  }

  def item(item: Item): Node = {
    tr(
      td(className := "px-6 py-4 text-gray-800 font-medium", item.name),
      td(
        className := "px-4 py-4 text-center",
        span(
          className := "inline-flex items-center px-3 py-1 rounded-full text-sm font-semibold bg-orange-100 text-orange-700",
          "[X]"
        )
      )
    )
  }

}
