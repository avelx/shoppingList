import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.comcast.ip4s.{ipv4, port}
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.{Router, Server}
import org.http4s._
import org.http4s.dsl.io._
import fs2.io.file.Path

object SimpleHttpServer extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    app.use(_ => IO.never).as(ExitCode.Success)

  private val routes = HttpRoutes.of[IO] {
    case request@GET -> Root =>
      StaticFile.fromPath(Path("/Users/pavel/devcore/plaground/shoppingList/resources/index.html"), Some(request))
        .getOrElseF(NotFound()) // In case the file doesn't exist
  }

  private val httpApp: HttpApp[IO] =
    Router(
      "app" -> routes
    ).orNotFound

  private val app: Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build
}