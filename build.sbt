import sbt._
import Keys._

lazy val jacksonModuleScala3Enum = (project in file("."))
  .settings(
    name := "jackson-module-scala3-enum",
    organization := "com.github.pjfanning",
    ThisBuild / scalaVersion := "3.0.2",

    sbtPlugin := false,

    scalacOptions ++= Seq("-deprecation", "-Xcheckinit", "-encoding", "utf8", "-g:vars", "-unchecked", "-optimize"),
    parallelExecution := true,
    homepage := Some(new java.net.URL("https://github.com/pjfanning/jackson-module-scala3-enum/")),
    description := "A library for serializing/deserializing scala3 enums using Jackson.",

    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),

    scmInfo := Some(
      ScmInfo(
        url("https://github.com/pjfanning/jackson-module-scala3-enum"),
        "scm:git@github.com:pjfanning/jackson-module-scala3-enum.git"
      )
    ),

    developers := List(
      Developer(id="pjfanning", name="PJ Fanning", email="", url=url("https://github.com/pjfanning"))
    ),

    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.0",
      "org.scalatest" %% "scalatest" % "3.2.10" % Test
    ),

    // enable publishing the main API jar
    packageDoc / publishArtifact := true,

    // build.properties
    Compile / resourceGenerators += Def.task {
      val file = (Compile / resourceManaged).value / "com" / "github" / "pjfanning" / "enum" / "build.properties"
      val contents = "version=%s\ngroupId=%s\nartifactId=%s\n".format(version.value, organization.value, name.value)
      IO.write(file, contents)
      Seq(file)
    }.taskValue,

    ThisBuild / githubWorkflowJavaVersions := Seq("adopt@1.8"),
    ThisBuild / githubWorkflowTargetTags ++= Seq("v*"),
    ThisBuild / githubWorkflowPublishTargetBranches := Seq(
      RefPredicate.Equals(Ref.Branch("main")),
      RefPredicate.StartsWith(Ref.Tag("v"))
    ),

    ThisBuild / githubWorkflowPublish := Seq(
      WorkflowStep.Sbt(
        List("ci-release"),
        env = Map(
          "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
          "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
          "SONATYPE_PASSWORD" -> "${{ secrets.CI_DEPLOY_PASSWORD }}",
          "SONATYPE_USERNAME" -> "${{ secrets.CI_DEPLOY_USERNAME }}",
          "CI_SNAPSHOT_RELEASE" -> "+publishSigned"
        )
      )
    )

  )

