ThisBuild / organization := "org.playground"
ThisBuild / scalaVersion := "3.6.4"
ThisBuild / semanticdbEnabled := true

Global / onChangedBuildSource := ReloadOnSourceChanges

val http4sVersion = "0.23.33"
val laminarVersion = "17.2.1"
val shoppingVersion = "0.0.1"
val BorerVersion = "1.14.0"

commands += Command.command("build") { state =>
  "clean" ::
    "compile" ::
    "fastLinkJS" ::
    "test" ::
    "scalafixAll" ::
    "scalafmtAll" ::
    "scalafmtSbt" ::
//    "dependencyUpdates" ::
//    "reload plugins; dependencyUpdates; reload return" ::
    "reload plugins; reload return" ::
    state
}

lazy val root = project
  .in(file("."))
  .settings(
    name := """shopping"""
  )
  .aggregate(backend, frontend, shared.jvm, shared.js)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(
    name := """shopping-shared""",
    Compile / unmanagedSourceDirectories ++= Seq(
      baseDirectory.value.getParentFile / "src"
    )
  )

lazy val backend = project
  .in(file("backend"))
  .settings(
    name := """shopping-backend""",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion
    ),
    run / javaOptions += "-Dshop.mode=development",
    reStart / javaOptions += "-Dshop.mode=development",
    run / fork := true,
    mainClass := Some("shopping.Backend"),
    buildInfoKeys := Seq[BuildInfoKey](
      "shoppingVersion" -> shoppingVersion,
      "frontendPath" -> (frontend / Compile / fastLinkJS / scalaJSLinkerOutputDirectory).value
    ),
    buildInfoPackage := "shopping"
  )
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(shared.jvm)

lazy val frontend = project
  .in(file("frontend"))
  .settings(
    name := """shopping-frontend""",
    mainClass := Some("shopping.Frontend"),
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % laminarVersion,
      "io.bullet" %%% "borer-core" % BorerVersion,
      "io.bullet" %%% "borer-derivation" % BorerVersion,
      "org.scala-js" %%% "scalajs-dom" % "2.8.0", // TODO: do we need to remove this??
      "com.lihaoyi" %%% "utest" % "0.9.2" % "test"
    ),
    scalaJSUseMainModuleInitializer := true,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(shared.js)
