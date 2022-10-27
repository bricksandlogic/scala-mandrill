package net.gutefrage.mandrill.http4s

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import net.gutefrage.mandrill.Mandrill
import net.gutefrage.mandrill.core.{MandrillApiError, MandrillApiErrorName, MandrillApiKey}
import net.gutefrage.mandrill.messages.SendTemplateResponse
import net.gutefrage.mandrill.messages.SendTemplateResponse.Status
import net.gutefrage.mandrill.users.Pong
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec._
import org.scalatest.Assertion

import scala.concurrent.Future
import scala.language.implicitConversions

class Http4sMandrillClientSpec extends AsyncWordSpec with Matchers {

  val apiUri = "https://mandrillapp.com/api/1.0"
  val apiKey = MandrillApiKey("yG3ETbFWZKbbjjAtNR0RDQ") // test API key

  implicit def toFuture(x: IO[Assertion]): Future[Assertion] =
    x.unsafeToFuture()

  def withClient[A](f: Http4sMandrillClient[IO] => IO[A]): IO[A] =
    Http4sMandrillClient
      .resource[IO](apiUri)
      .use(f)

  "Http4sMandrillClient" should {

    "return api error" in {
      withClient { client =>
        client.ping(Mandrill(MandrillApiKey("wrong key")).users().ping)
      }.map(_ shouldBe Left(MandrillApiError("Invalid API key", MandrillApiErrorName.Invalid_Key)))
    }

    "ping" in {
      withClient { client =>
        client.ping(Mandrill(apiKey).users().ping)
      }.map(_ shouldBe Right(Pong("PONG!")))
    }

    "send" in {
      withClient { client =>
        client.send(
          Mandrill(apiKey)
            .messages()
            .sendTemplate("test")
            .subject("Test email")
            .from(Some("test@scalamandrill.com"))
            .signingDomain("scalamandrill.com")
            .to("test@gmail.com", "var1" -> "abc")
            .templateContent(("abc", "def")))
      }.map {
        _ should matchPattern {
          case Right(SendTemplateResponse(_, "test@gmail.com", Status.Rejected, None) :: Nil) =>
        }
      }
    }
  }
}
