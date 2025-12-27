import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element
import shopping.Controller
import shopping.View
import shopping.ViewModel
import shopping.ViewModel.viewModelVar

// Building Scala.js application
object Runner {

  def main(args: Array[String]): Unit = {
    val mount: Element = dom.document.getElementById("mount")
    run(mount)
  }

  private def run(mount: dom.Element): Unit = {
    // val defaultViewModel: Var[ViewModel] = Var(ViewModel.defaultViewModel)
    val controller = Controller(dynModel = viewModelVar)
    val view = View(controller)

    val rootElement: Div = view.build(vm = viewModelVar.signal)
    render(mount, rootElement)
  }

}
