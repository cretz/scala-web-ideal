# Akka HTTP w/ All Scala Showcase

This is basically a fork of the
[Play Scala.js Showcase](https://github.com/hussachai/play-scalajs-showcase) project using Akka
HTTP and an all Scala codebase. This is just a playground for learning new tech. It contains 5
pages/apps: a simple hello world, a TodoMVC impl, a hangman game, a file uploader, and a
websocket/server-sent-event chat app. I'm sure there are bugs...

#### Features

* Akka HTTP for serving pages and 100% all-Scala codebase
* Akka HTTP websocket chat support
* Akka HTTP server-sent-event chat support
* Akka HTTP multipart file upload support
* WebJars support
* [Autowire](https://github.com/lihaoyi/autowire) +
  [Prickle](https://github.com/benhutchison/prickle) for simple, typesafe AJAX
* [ScalaCSS](https://github.com/japgolly/scalacss/) for typesafe (server-side) CSS values
* [ScalaTags](https://github.com/lihaoyi/scalatags) for typesafe HTML
* [Scala.js](http://www.scala-js.org/) for typesafe JS including shared, typesafe interfaces/models
* Clean separation of pages and code with a decent architecture
* Gradle instead of SBT (just because I could and I am evaluating options)

Many more-professional things left out of the demo such as tests, configuration, etc.

#### Building

In order to build, first compile the JS file:

    gradle scala-web-ideal-client:compileScalaJs

Then just run the webapp (starts on port 8080):

    gradle scala-web-ideal-server:run

Note: During development I chose not to optimize the Scala.js-emitted code. But the options can
easily be configured in client/build.gradle.

#### Development

``gradle eclipse`` works perfectly fine (including on Windows). While developing, I tend to use
[nodemon](http://nodemon.io/) to restart my applications on change. I have the following two
commands running:

    nodemon --legacy-watch --watch server/src --watch shared/src -e scala --exec gradle scala-web-ideal-server:run
    
    nodemon --legacy-watch --watch client/src --watch shared/src -e scala --exec gradle scala-web-ideal-client:compileScalaJs

It's not as fast as Play (or SBT revolver or the Scala.js SBT plugin, etc), but it's a good
learning experience.

#### Thoughts

I battled several little bugs to get this working, but overall I think it is a decent stack for
a web application. If this were to turn into a legitmate web stack, a couple of things would be
needed. Obviously a Gradle plugin would be much preferred over over manually working with things
like Eclipse, nodemon, and the Scala.js compiler. Also, many generic utilities would need to be
written for Akka HTTP along with a best practices guide. This is especially true concerning how
to tie things like WebJars, ScalaTags, ScalaCSS, ScalaJS, etc together in a strict, typesafe
manner.