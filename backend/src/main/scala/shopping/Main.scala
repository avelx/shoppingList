package shopping

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import com.comcast.ip4s.ipv4
import com.comcast.ip4s.port
import fs2.io.file.Path
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.server.Server

object Backend extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    app.use(_ => IO.never).as(ExitCode.Success)

  private val static = HttpRoutes.of[IO] { case request @ GET -> Root =>
    StaticFile
      .fromPath(
        Path(
          "/Users/pavel/devcore/plaground/shoppingList/backend/src/main/resources/index.html"
        ),
        Some(request)
      )
      .getOrElseF(NotFound()) // In case the file doesn't exist
  }

  private val javascript = HttpRoutes.of[IO] {
    case request @ GET -> Root / "main.js" =>
      StaticFile
        .fromPath(
          Path(
            "/Users/pavel/devcore/plaground/shoppingList/frontend/target/scala-3.6.4/shopping-frontend-fastopt/main.js"
          ),
          Some(request)
        )
        .getOrElseF(NotFound()) // In case the file doesn't exist
  }

  private val httpApp: HttpApp[IO] =
    Router(
      "app" -> static,
      "js" -> javascript
    ).orNotFound

  private val app: Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build

}
