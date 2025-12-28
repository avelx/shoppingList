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
        div(
          className := "basis-1/2 items-center justify-center rounded-lg bg-sky-500",
          input(
            value := "[Select Item]",
            `type` := "button",
            onClick --> controller.onViewButtonPressed(CategoriesView)
          ),
          onClick --> controller.onViewButtonPressed(CategoriesView)
        ),
        div(
          className := "basis-1/2 items-center justify-center rounded-lg bg-sky-500",
          input(
            value := "[View Basket]",
            `type` := "button",
            onClick --> controller.onViewButtonPressed(BasketView)
          ),
          onClick --> controller.onViewButtonPressed(BasketView)
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
