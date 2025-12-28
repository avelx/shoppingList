import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element
import shopping.Controller
import shopping.models.ViewModel
import shopping.models.ViewModel.viewModelVar
import shopping.views.MainView

// Building Scala.js application
object FrontendRunner {

  def main(args: Array[String]): Unit = {
    val mount: Element = dom.document.getElementById("mount")
    run(mount)
  }

  private def run(mount: dom.Element): Unit = {
    val controller = Controller(dynModel = viewModelVar)
    val view = MainView(controller)
    val rootElement: Div = view.build(vm = viewModelVar.signal)
    render(mount, rootElement)
  }

}
