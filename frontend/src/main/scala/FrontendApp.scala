package shopping

import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.dom.document
import org.scalajs.dom.window
import shopping.Controller
import shopping.models.ViewModel
import shopping.models.ViewModel.viewModelVar
import shopping.views.MainView

// Building Scala.js application
object FrontendApp {

  def main(args: Array[String]): Unit = {

    val controller = Controller(dynModel = viewModelVar)
    val view: MainView = MainView(controller)

    windowEvents(_.onLoad).foreach { _ =>
      val app = document.createElement("div")
      document.body.appendChild(app)
      setupUI(app)
    }(unsafeWindowOwner)
  }

  def setupUI(root: Element): Unit = {
    val controller = Controller(dynModel = viewModelVar)
    val view = MainView(controller)
    val rootElement: Div = view.build(vm = viewModelVar.signal)
    render(root, rootElement)
  }

}
