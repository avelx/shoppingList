package shopping.views

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.{_, given}
import com.raquo.laminar.api.features.unitArrows
import shopping.Controller
import shopping.models.CategoriesData
import shopping.models.ViewModel
import shopping.models.ViewModelState.CategoriesView

trait CategoryView(controller: Controller) {

  def viewCategories(vm: Signal[ViewModel]): Div = {
    div(
      display <-- vm.map(m =>
        if (m.state == CategoriesView) then "block" else "none"
      ),
      div(
        div(
          className := "overflow-x-auto",
          table(
            className := "w-full",
            thead(
              className := "bg-gray-50 border-b",
              tr(
                th(
                  className := "px-12 py-4 text-left text-sm font-semibold text-gray-700",
                  "Category"
                )
              )
            ),
            tbody(
              className := "divide-y divide-gray-200",
              children <-- vm
                .map(_.items)
                .map(_.toList.collect { case (cid, v) =>
                  val categoryCount = v.map(_.selected == false).length
                  categoryItem(cid, categoryCount)
                })
            )
          )
        )
      )
    )
  }

  private def categoryItem(cid: String, count: Int): Node = {
    CategoriesData.all
      .find(_.cid == cid)
      .map(cItem =>
        tr(
          td(
            className := "px-12 py-4 text-gray-800 font-medium",
            div(
              s"($count) - ${cItem.name}",
              onClick --> controller.onCategorySelected(cItem)
            )
          )
        )
      )
      .getOrElse {
        tr(
          td(
            className := "px-12 py-4 text-gray-800 font-medium",
            div(
              "No category selected"
            )
          )
        )
      }
  }

}
