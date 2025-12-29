package shopping

import cats._
import cats.effect._
import com.comcast.ip4s.ipv4
import com.comcast.ip4s.port
import fs2.io.file.Path
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.`Content-Type`
import org.http4s.server.Router
import org.http4s.server.Server

import Utils.readFileFromResource

object Backend extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    app.use(_ => IO.never).as(ExitCode.Success)

  private val staticFilesExtensions: List[String] = List(".html", ".json")

  private val static = HttpRoutes.of[IO] {
    case request @ GET -> Root / path
        if staticFilesExtensions.exists(path.endsWith) =>
      readFileFromResource(path) match {
        case Right(content) =>
          Ok(content).map(
            _.withContentType(if (path.contains("manifest.json")) {
              `Content-Type`(MediaType.application.`manifest+json`)
            } else {
              `Content-Type`(MediaType.text.html)
            })
          ) // Support other content types)
        case Left(ex) =>
          NotFound()
      }
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

  private val imagesExtensions: List[String] = List(".png")

  private val images = HttpRoutes.of[IO] {
    case request @ GET -> Root / path
        if imagesExtensions.exists(path.endsWith) =>
      readFileFromResource("images/" + path) match {
        case Right(content) =>
          Ok(content).map(
            _.withContentType(`Content-Type`(MediaType.image.png))
          ) // Support other content types)
        case Left(ex) =>
          NotFound()
      }
  }

  private val httpApp: HttpApp[IO] =
    Router(
      "app" -> static,
      "js" -> javascript,
      "images" -> images
    ).orNotFound

  private val app: Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build

}
