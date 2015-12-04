package mytest

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom

class LoginAndLogoutLoadTest extends Simulation {

	val httpProtocol = http
		.baseURL("http://vstraining.vrn.dataart.net")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate, sdch")
		.acceptLanguageHeader("en-US,en;q=0.8")
		.contentTypeHeader("application/x-www-form-urlencoded")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Origin" -> "http://vstraining.vrn.dataart.net",
		"Upgrade-Insecure-Requests" -> "1")

	val base_uri = "http://vstraining.vrn.dataart.net"

	val scn = scenario("Login and Logout")

		// Log in action
		.exec(http("Log in action")
			.post("/login")
			.check(status.is(200))
			//.check(css("div:contains('Login as: gatlingtest')", "class")
			.headers(headers_1)
			.formParam("login", "gatlingtest")
			.formParam("password", "gatlingtest"))

		.pause(5)
		// Check Home page
			//
		// Log out action
		.exec(http("Log out action")
			.get("/logout")
			.headers(headers_0))

	//setUp(scn.inject(atOnceUsers(20))).protocols(httpProtocol)

	setUp(
		scn.inject(
			nothingFor(4 seconds), // 1
			atOnceUsers(50), // 2
			rampUsers(100) over(50 seconds), // 3
			constantUsersPerSec(25) during(15 seconds)

		).protocols(httpProtocol)
	)
}