package shopping

import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.dom.document
import shopping.models.ViewModel
import shopping.models.ViewModel.viewModelVar
import shopping.views.MainView

// Building Scala.js application
object FrontendApp {

  def main(args: Array[String]): Unit = {

    val dataService = DataService()
    val controller = Controller(dynModel = viewModelVar, dataService)
    val view: MainView = MainView(controller)
    // Static context
    val app = document.createElement("div")
    dom.document.body.appendChild(app)
    controller.loadData()

    document.addEventListener(
      "DOMContentLoaded",
      { (e: dom.Event) =>
        setupUI(app, controller, view)
      }
    )

  }

  def setupUI(
      root: Element,
      controller: Controller,
      view: MainView
  ): RootNode = {
    val rootElement = view.build(vm = viewModelVar.signal)
    render(root, rootElement)
  }

}
