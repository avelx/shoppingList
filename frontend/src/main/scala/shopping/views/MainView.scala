package shopping.views

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.{_, given}
import com.raquo.laminar.api.features.unitArrows
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.bullet.borer.Json
import org.scalajs.dom.HTMLDivElement
import shopping.Controller
import shopping.models.ViewModelState.BasketView
import shopping.models.ViewModelState.CategoriesView
import shopping.models._

import java.nio.charset.StandardCharsets

class MainView(controller: Controller)
    extends BasketView(controller)
    with CategoryView(controller)
    with ItemView(controller) {

  // TODO: add Json resource compile time validation logic
  private def loadCategories(response: String): List[Category] = {
    Json
      .decode(response.getBytes(StandardCharsets.UTF_8))
      .to[CategoryWrapper]
      .valueTry
      .toOption
      .map(_.data)
      .getOrElse(List.empty)
  }

  private def loadItems(response: String): Map[String, List[SelectableItem]] = {
    Json
      .decode(response.getBytes(StandardCharsets.UTF_8))
      .to[ItemWrapper]
      .valueTry
      .toOption
      .map(_.data)
      .getOrElse(Map.empty)
  }

  def build(vm: Signal[ViewModel]): ReactiveHtmlElement[HTMLDivElement] = {
    div(
      FetchStream.get("/data/categories.json") --> { responseText =>
        val categories = loadCategories(responseText)
        controller.onCategoriesFetch(categories)
      },
      FetchStream.get("/data/items.json") --> { responseText =>
        val items = loadItems(responseText)
        controller.onItemsFetch(items)
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
