import sbt._, Keys._

object BuildSettings {
  val buildOrganization = "org.ftw"
  val buildVersion      = "0.0.1-SNAPSHOT"
  val buildScalaVersion = "2.9.1"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions += "-deprecation",
    crossScalaVersions := Seq("2.8.1", "2.9.0", "2.9.0-1", "2.9.1")
  )
  
  val servletSettings = Defaults.defaultSettings ++ Seq(
    libraryDependencies ++= Seq(
      "javax.servlet" % "servlet-api" % "2.5" % "provided"
  ))
  
  val jettySettings = servletSettings ++ Seq(
    libraryDependencies ++= Seq(
      "org.eclipse.jetty" % "jetty-webapp" % "7.3.0.v20110203" % "jetty",
      "ch.qos.logback" % "logback-classic" % "0.9.26"
  ))
}

object Build extends Build {
  import BuildSettings._
  
  lazy val root = Project("ftw", file("."),
    settings = buildSettings
  ) aggregate(core, http, servlet, examples)
  
  lazy val core: Project = Project("core", file("core"), 
    settings = buildSettings)
  
  lazy val http: Project = Project("http", file("http"), 
    settings = buildSettings) dependsOn(core)
  
  lazy val servlet: Project = Project("servlet", file("servlet"), 
    settings = buildSettings ++ servletSettings) dependsOn(http)
  
  lazy val examples = Project("examples", file("examples"),
    settings = buildSettings,
    aggregate = Seq(exampleServlet)
  )
  
  lazy val exampleServlet = Project(
    "example-servlet", file("examples/servlet"),
    dependencies = Seq(servlet),
    settings = buildSettings ++ jettySettings
  ) dependsOn(servlet) settings(com.github.siasia.WebPlugin.webSettings :_*)
}
