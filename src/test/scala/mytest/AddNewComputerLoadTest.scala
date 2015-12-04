package mytest

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom
class AddNewComputerLoadTest extends Simulation {

	val httpProtocol = http
		.baseURL("http://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate, sdch")
		.acceptLanguageHeader("en-US,en;q=0.8")
		.contentTypeHeader("application/x-www-form-urlencoded")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Origin" -> "http://computer-database.gatling.io",
		"Upgrade-Insecure-Requests" -> "1")

    val uri1 = "http://computer-database.gatling.io"

	val scn = scenario("RecordedSimulation")
		// HomePage
		.exec(http("Home Page")
			.get("/computers")
			.check(status.is(200))
			.headers(headers_0))
		.pause(3)
		// AddNew
		.exec(http("Add New Computer")
			.get("/computers/new")
			.headers(headers_0))
		.pause(15)
		.exec(http("request_2")
			.post("/computers")
			.headers(headers_2)
			.formParam("name", "oleg'sMac")
			.formParam("introduced", "2000-02-15")
			.formParam("discontinued", "2015-01-01")
			.formParam("company", "1"))

		.pause(7)
		// FindAndCheck
		.exec(http("Search")
			.get("/computers?f=oleg%27sMac")
			.check(status.is(200))
			.check(css("a:contains('oleg'sMac')", "href"))
			.headers(headers_0))
		.pause(3)
	setUp(scn.inject(rampUsers(1) over (5 seconds))).protocols(httpProtocol)
}