package shopping

import com.raquo.laminar.api.L.given
import org.scalajs.dom
import org.scalajs.dom.document
import shopping.models.ViewModel.viewModelVar
import shopping.views.MainView
import utest._

import scala.runtime.stdLibPatches.Predef.assert

// ::Simple test example::
object FrontendTest extends TestSuite {

  val dataService = DataService()
  val controller = Controller(dynModel = viewModelVar, dataService)
  val view: MainView = MainView(controller)
  val app = document.createElement("div")
  document.body.appendChild(app)

  // https://laminar.dev/documentation#waiting-for-the-dom-to-load
  document.addEventListener(
    "DOMContentLoaded",
    { (e: dom.Event) =>
      FrontendApp.setupUI(app, controller, view)
    }
  )

  val tests = Tests {

    test("Static element check") {
      assert(
        {
          document.querySelectorAll("div").length > 0
        }
      )
    }

  }

}
