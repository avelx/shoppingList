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

  private def processResponse(response: String): Unit = {
    println(response)
  }

  def build(vm: Signal[ViewModel]): ReactiveHtmlElement[HTMLDivElement] = {
    div(
      FetchStream.get("/data/categories.json") --> { responseText =>
        processResponse(responseText)
      },
      className := "container text-start",
      div(
        nbsp
      ),
      div(
        className := "row",
        div(
          className := "col",
          p(
            className := "fs-1 text-center",
            text <-- vm.map(_.pageTitle)
          )
        )
      ),
      div(
        nbsp
      ),
      div( // TopLevel::Buttons
        className := "row",
        div(
          className := "col-6",
          div(
            className := "d-grid gap-2",
            button(
              className <--
                vm.map(_.state)
                  .map(s =>
                    s match {
                      case _ =>
                        "btn btn-primary btn-lg"
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
          )
        ),
        div(
          className := "col-6",
          div(
            className := "d-grid gap-2",
            button(
              className <--
                vm.map(_.state)
                  .map(s =>
                    s match {
                      case _ =>
                        "btn btn-success btn-lg"
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
        )
      ),
      // Sections
      div(
        nbsp
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
