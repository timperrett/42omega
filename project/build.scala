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
  lazy val root = Project("ftw", file("."),
    settings = BuildSettings.buildSettings
  ) aggregate(core, http, servlet)
  
  lazy val core: Project = Project("core", file("core"), 
    settings = BuildSettings.buildSettings)
  
  lazy val http: Project = Project("http", file("http"), 
    settings = BuildSettings.buildSettings) dependsOn(core)
  
  lazy val servlet: Project = Project("http-servlet", file("http-servlet"), 
    settings = BuildSettings.buildSettings ++ (
      libraryDependencies ++= Seq(
        "javax.servlet" % "servlet-api" % "2.5" % "provided"
      ))) dependsOn(http)
  
  lazy val samples = Project("examples", file("examples"),
    settings = parentSettings,
    aggregate = Seq(fsmSample, camelSample)
  )  
}

//settings(com.github.siasia.WebPlugin.webSettings :_*)
