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

package net.gutefrage.mandrill.circe

import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax._
import net.gutefrage.mandrill.circe.EnumCodecs._
import net.gutefrage.mandrill.core.{MandrillApiError, MandrillApiErrorName, MandrillApiKey, MandrillDateTime}

import java.time.format.DateTimeFormatter

package object core {

  private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  implicit val mandrillDateTimeEncoder: Encoder[MandrillDateTime] = new Encoder[MandrillDateTime] {
    override def apply(mdt: MandrillDateTime): Json = format.format(mdt.value).asJson
  }

  implicit val apiErrorNameDecoder: Decoder[MandrillApiErrorName] = enumDecoder(MandrillApiErrorName)

  implicit val apiErrorDecoder: Decoder[MandrillApiError] = deriveDecoder[MandrillApiError]

  implicit val mandrillApiKeyEncoder: Encoder[MandrillApiKey] = Encoder.encodeString.contramap[MandrillApiKey](_.value)
}
