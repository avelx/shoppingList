package shopping

import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element

// Building Scala.js application
object Frontend {

  def main(args: Array[String]): Unit = {
    val mount: Element = dom.document.getElementById("mount")
    run(mount)
  }

  private def run(mount: dom.Element): Unit = {
    val defaultViewModel: Var[ViewModel] = Var(ViewModel.defaultViewModel)
    val controller = Controller(dynModel = defaultViewModel)
    val view = View(controller)

    val rootElement: Div = view.build(vm = defaultViewModel.signal)
    render(mount, rootElement)
  }

}
