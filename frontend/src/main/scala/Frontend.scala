import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.Element

import scala.scalajs.js

object Frontend {
  def main(args: Array[String]): Unit = {
    val mount: Element = dom.document.getElementById("mount")
    run(mount)
  }

  def run(mount: dom.Element): Unit = {
    val app: Div = {
      div(
        input(
          value := "test22",
          `type` := "button"
        )
      )
    }
    render(mount, app)
  }
}
