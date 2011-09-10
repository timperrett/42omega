import sbt._, Keys._

object BuildSettings {
  val buildOrganization = "org.ftw"
  val buildVersion      = "0.0.1-SNAPSHOT"
  val buildScalaVersion = "2.9.1"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions += "-deprecation",
    crossScalaVersions := Seq("2.8.1", "2.9.0", "2.9.0-1", "2.9.1")
  )
}

object Build extends Build {
  import BuildSettings.buildSettings
  
  lazy val root = Project("ftw", file("."),
    settings = buildSettings
  ) aggregate(core, http, servlet)
  
  lazy val core: Project = Project("core", file("core"), 
    settings = buildSettings)
  
  lazy val http: Project = Project("http", file("http"), 
    settings = buildSettings) dependsOn(core)
  
  lazy val servlet: Project = Project("http-servlet", file("http-servlet"), 
    settings = buildSettings ++ (
      libraryDependencies ++= Seq(
        "javax.servlet" % "servlet-api" % "2.5" % "provided"
      ))) dependsOn(http)
  
  // lazy val examples = Project("examples", file("examples"),
  //   settings = buildSettings,
  //   aggregate = Seq(fsmSample, camelSample)
  // )
  
  // lazy val exampleServlet = Project("")
}

//settings(com.github.siasia.WebPlugin.webSettings :_*)
