# Akka HTTP w/ All Scala Showcase

I am just trying to try an all-scala approach here as a PoC. I am also using Gradle instead of SBT just to do it and explore the possibility.

Goals:

* Feature parity with https://github.com/hussachai/play-scalajs-showcase
* Pure Akka HTTP
* No SBT, use Gradle
* Only use ScalaJS, ScalaCSS, ScalaTags, etc

I tend to run the following w/ the "nodemon" node module globally installed for now until I make a gradle plugin:

nodemon --legacy-watch --watch server/src -e scala --exec gradle scala-web-ideal-server:run