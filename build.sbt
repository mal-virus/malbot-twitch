val thisVersion = "0.0.1"

lazy val root = (project in file(".")).
	settings(
		name := "twitchApi",
		version := thisVersion,
		organization := "thirus.malcolm",
		cleanFiles <+= baseDirectory { _ / "RUNNING_PID" },
		libraryDependencies ++= Seq(
				"pircbot" % "pircbot" % "1.5.0",
				"com.typesafe" % "config" % "1.2.1")
	)