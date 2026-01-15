package shopping

import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.dom.document
import shopping.Controller
import shopping.models.ViewModel
import shopping.models.ViewModel.viewModelVar
import shopping.services.DataService
import shopping.views.MainView

// Building Scala.js application
object FrontendApp {

  def main(args: Array[String]): Unit = {
    windowEvents(_.onLoad).foreach { _ =>
      lazy val appContainer: Element = dom.document.getElementById("mount")
      setupUI(appContainer)
    }(unsafeWindowOwner)
  }

  def setupUI(root: Element): Unit = {
    val dataService = DataService()
    val controller = Controller(dynModel = viewModelVar, dataService)
    val view = MainView(controller)
    val rootElement: Div = view.build(vm = viewModelVar.signal)
    render(root, rootElement)
  }

}
