package shopping

import com.raquo.laminar.api.L.{_, given}
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext._
import utest._

import scala.runtime.stdLibPatches.Predef.assert
import scala.scalajs.js

// ::Simple test example::
object FrontendTest extends TestSuite {

  // Initialize App
//  lazy val mount = dom.document.getElementById("mount")
//  FrontendApp.setupUI(mount)
  windowEvents(_.onLoad).foreach { _ =>
    lazy val appContainer = dom.document.getElementById("mount")
    FrontendApp.setupUI(appContainer)
  }(unsafeWindowOwner)

  val tests = Tests {

    test("HelloWorld") {
      assert(
        document != null
//          .querySelectorAll("div")
//          .count(x => x.id.isEmpty) == 1
      )
    }
  }

}
