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

import io.github.nguyenyou.webawesome.laminar.*

class MainView(controller: Controller)
    extends BasketView(controller)
    with CategoryView(controller)
    with ItemView(controller) {

  def build(vm: Signal[ViewModel]): ReactiveHtmlElement[HTMLDivElement] = {
    div(
      div(
        className := "max-w-5xl mx-auto",
        div(
          className := "text-center mb-8",
          text <-- vm.map(_.pageTitle)
        )
      ),
      div(
        className := "flex flex-row",
        Button()("Click me"),
        div(
          className := "basis-1/2 items-center justify-center rounded-lg",
          button(
            className <--
              vm.map(_.state)
                .map(s =>
                  s match {
                    case CategoriesView =>
                      "bg-indigo-600 hover:focus:bg-indigo-500"
                    case BasketView =>
                      "bg-green--400 hover:focus:bg-green-500"
                    case ItemByCategoryView =>
                      "bg-red-400 hover:not-focus:bg-red-700"
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
          className := "basis-1/2 items-center justify-center rounded-lg",
          button(
            className <--
              vm.map(_.state)
                .map(s =>
                  s match {
                    case ItemByCategoryView =>
                      "bg-indigo-600 hover:focus:bg-indigo-500"
                    case CategoriesView =>
                      "bg-green--400 hover:focus:bg-green-500"
                    case BasketView =>
                      "bg-red-400 hover:not-focus:bg-red-700"
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
        viewCategories(vm),
        itemsView(vm),
        viewBasket(vm)
      )
    )
  }

}
