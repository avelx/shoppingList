package shopping

import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext._
import shopping.models.ViewModel.viewModelVar
import shopping.views.MainView
import utest._

import scala.runtime.stdLibPatches.Predef.assert
import scala.scalajs.js

// ::Simple test example::
object FrontendTest extends TestSuite {

  val controller = Controller(dynModel = viewModelVar)
  val view: MainView = MainView(controller)

  windowEvents(_.onLoad).foreach { _ =>
    val app = document.createElement("div")
    document.body.appendChild(app)
    FrontendApp.setupUI(app)
  }(unsafeWindowOwner)

  val tests = Tests {
    test("HelloWorld") {
      assert(
        document
          .querySelectorAll("div")
          .length == 0
      )
    }

  }

}
