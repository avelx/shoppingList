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
      className := "col-12",
      display <-- vm.map(m =>
        if (m.state == CategoriesView) then "block" else "none"
      ),
      table(
        className := "table table-success table-striped table-hover table-bordered border-primary",
        thead(
          tr(
            th(
              className := "col",
              "Category"
            )
          )
        ),
        tbody(
          children <-- vm
            .map(_.items)
            .map(_.toList.collect { case (cid, v) =>
              val categoryCount = v.map(_.selected == false).length
              categoryItem(cid, categoryCount)
            })
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
            className := "col",
            s"($count) - ${cItem.name}",
            onClick --> controller.onCategorySelected(cItem)
          )
        )
      )
      .getOrElse {
        tr(
          td(
            className := "col",
            "No category selected"
          )
        )
      }
  }

}
