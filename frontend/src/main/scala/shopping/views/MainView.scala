package shopping.views

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.{_, given}
import com.raquo.laminar.api.features.unitArrows
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLDivElement
import shopping.Controller
import shopping.models.ViewModel
import shopping.models.ViewModelState.BasketView
import shopping.models.ViewModelState.CategoriesView
import shopping.models.ViewModelState.ItemByCategoryView

class MainView(controller: Controller)
    extends BasketView(controller)
    with CategoryView(controller)
    with ItemView(controller) {

  def build(vm: Signal[ViewModel]): ReactiveHtmlElement[HTMLDivElement] = {
    div(
      className := "container text-start",
      div(
        className := "row",
        div(
          className := "col",
          text <-- vm.map(_.pageTitle)
        )
      ),
      div(
        className := "row",
        div(
          className := "col",
          button(
            className <--
              vm.map(_.state)
                .map(s =>
                  s match {
                    case _ =>
                      "btn btn-primary"
                  }
                ),
            "Available Items",
            onClick.compose(_.delay(500)) --> controller.onViewButtonPressed(
              CategoriesView
            )
          ),
          onClick.compose(_.delay(500)) --> controller.onViewButtonPressed(
            CategoriesView
          )
        ),
        div(
          className := "col",
          button(
            className <--
              vm.map(_.state)
                .map(s =>
                  s match {
                    case _ =>
                      "btn btn-success"
                  }
                ),
            "View Basket",
            onClick.compose(_.delay(500)) --> controller.onViewButtonPressed(
              BasketView
            )
          ),
          onClick.compose(_.delay(500)) --> controller.onViewButtonPressed(
            BasketView
          )
        )
      ),
      div(
        className := "row",
        viewCategories(vm)
      ),
      div(
        className := "row",
        itemsView(vm)
      ),
      div(
        className := "row",
        viewBasket(vm)
      )
    )
  }

}
