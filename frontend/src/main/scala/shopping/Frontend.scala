package shopping

import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element
import shopping.ViewModel.defaultItems

// Building Scala.js application
object Frontend {

  def main(args: Array[String]): Unit = {
    val mount: Element = dom.document.getElementById("mount")
    run(mount)
  }

  private def run(mount: dom.Element): Unit = {
    val dynViewModel: Var[ViewModel] = Var(ViewModel(items = defaultItems))
    val controller = Controller(dynModel = dynViewModel)
    val view = View(controller)

    val rootElement: Div = view.build(vm = dynViewModel.signal)
    render(mount, rootElement)
  }

}
