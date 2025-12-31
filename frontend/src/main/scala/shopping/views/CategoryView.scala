package shopping.views

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.{_, given}
import com.raquo.laminar.api.features.unitArrows
import shopping.Controller
import shopping.models.Category
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
              className := "col-12",
              "Category"
            )
          )
        ),
        tbody(
          children <-- rowsStream(vm)
        )
      )
    )
  }

  private def rowsStream(vm: Signal[ViewModel]): Signal[List[Node]] = {

    vm.map(_.categories)
      .map(_.toList)
      .map { categories =>
        categories.map(category =>
          // Nested level signal
          val counts: Signal[Option[String]] = vm.map(
            _.items
              .get(category.cid)
              .map(_.filter(_.selected == false).length)
              .map(_.toString)
          )
          categoryItem(category, counts)
        )
      }
  }

  private def categoryItem(
      category: Category,
      count: Signal[Option[String]]
  ): Node = {
    tr(
      td(
        className := "col-12",
        div(
          text <-- count.map(
            _.map(c => s"( $c ) - ${category.name}")
              .getOrElse(s"(N/A) - ${category.name}")
          )
        ),
        onClick --> controller.onCategorySelected(category)
      )
    )
  }

}
