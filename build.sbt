val thisVersion = "0.0.1"

lazy val root = (project in file(".")).
	//enablePlugins(PlayScala).
	settings(
		name := "twitchApi",
		version := thisVersion,
		organization := "thirus.malcolm",
		fork in run := true,
		cleanFiles <+= baseDirectory { _ / "RUNNING_PID" },
		libraryDependencies ++= Seq(
				"pircbot" % "pircbot" % "1.5.0",
				"com.typesafe" % "config" % "1.2.1")
	)

fork in run := true

fork in run := true