/*
 * Copyright 2015 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gutefrage.mandrill.http4s

import cats.effect.{Async, Resource}
import cats.implicits._
import net.gutefrage.mandrill.circe.core._
import net.gutefrage.mandrill.circe.messages._
import net.gutefrage.mandrill.circe.users._
import net.gutefrage.mandrill.core.MandrillApiError
import net.gutefrage.mandrill.messages.{SendTemplate, SendTemplateResponse}
import net.gutefrage.mandrill.users.{Ping, Pong}
import org.http4s.{Method, Request, Status, Uri}
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder

import scala.language.higherKinds

object Http4sMandrillClient {
  def resource[F[_]](apiUri: String)(implicit F: Async[F]): Resource[F, Http4sMandrillClient[F]] =
    for {
      uri <- Resource.eval(F.fromEither(Uri.fromString(apiUri)))
      client <- EmberClientBuilder.default[F].build
    } yield new Http4sMandrillClient(uri, client)
}

class Http4sMandrillClient[F[_]](apiUri: Uri, client: Client[F])(implicit F: Async[F]) {

  def send(template: SendTemplate): F[Either[MandrillApiError, List[SendTemplateResponse]]] = {
    val req = Request[F]()
      .withMethod(Method.POST)
      .withUri(apiUri / "messages" / "send-template")
      .withEntity(template)

    client.run(req).use {
      case Status.Successful(resp) =>
        resp.as[List[SendTemplateResponse]].map(_.asRight)
      case resp =>
        resp.as[MandrillApiError].map(_.asLeft)
    }
  }

  def ping(ping: Ping): F[Either[MandrillApiError, Pong]] = {
    val req = Request[F]()
      .withMethod(Method.POST)
      .withUri(apiUri / "users" / "ping2")
      .withEntity(ping)

    client.run(req).use {
      case Status.Successful(resp) =>
        resp.as[Pong].map(_.asRight)
      case resp =>
        resp.as[MandrillApiError].map(_.asLeft)
    }
  }
}
