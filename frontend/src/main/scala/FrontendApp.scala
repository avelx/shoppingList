package shopping

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L.{_, given}
import io.github.nguyenyou.webawesome.laminar._
import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.dom.document
import shopping.Controller
import shopping.models.ViewModel
import shopping.models.ViewModel.viewModelVar
import shopping.views.MainView

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport(
  "@awesome.me/webawesome/dist/styles/webawesome.css",
  JSImport.Namespace
)
@js.native
object Stylesheet extends js.Object
val _ = Stylesheet

object FrontendApp {

  def main(args: Array[String]): Unit = {
    windowEvents(_.onLoad).foreach { _ =>
      lazy val appContainer: Element = dom.document.getElementById("mount")
      setupUI(appContainer)
    }(unsafeWindowOwner)
  }

  def setupUI(root: Element): Unit = {
    val controller = Controller(dynModel = viewModelVar)
    val view = MainView(controller)
    val rootElement: Div = view.build(vm = viewModelVar.signal)
    render(root, rootElement)
  }

}
