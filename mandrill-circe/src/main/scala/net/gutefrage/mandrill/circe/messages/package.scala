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
import io.circe.{Decoder, Encoder}
import net.gutefrage.mandrill.circe.EnumCodecs.{enumDecoder, enumEncoder}
import net.gutefrage.mandrill.messages._
import net.gutefrage.mandrill.messages.Recipient.RecipientType
import net.gutefrage.mandrill.messages.SendTemplateResponse.{RejectReason, Status}
import net.gutefrage.mandrill.circe.core._

package object messages {

  implicit val emailEncoder: Encoder[Email] = Encoder.encodeString.contramap[Email](_.value)

  implicit val nameEncoder: Encoder[Name] = Encoder.encodeString.contramap[Name](_.value)

  implicit val recipientTypeEncoder: Encoder[RecipientType] = enumEncoder(RecipientType)

  implicit val recipientEncoder: Encoder.AsObject[Recipient] = io.circe.generic.semiauto.deriveEncoder[Recipient]

  implicit val mergeVarEncoder: Encoder.AsObject[MergeVar] = io.circe.generic.semiauto.deriveEncoder[MergeVar]

  implicit val recipientMergeVarsEncoder: Encoder.AsObject[RecipientMergeVars] =
    io.circe.generic.semiauto.deriveEncoder[RecipientMergeVars]

  implicit val subjectEncoder: Encoder[Subject] = Encoder.encodeString.contramap[Subject](_.value)

  implicit val sendTemplateContentEncoder: Encoder.AsObject[TemplateContent] =
    io.circe.generic.semiauto.deriveEncoder[TemplateContent]

  implicit val sendTemplateMessageEncoder: Encoder.AsObject[Message] = io.circe.generic.semiauto.deriveEncoder[Message]

  implicit val templateNameeEncoder: Encoder[TemplateName] = Encoder.encodeString.contramap[TemplateName](_.value)

  implicit val sendTemplateEncoder: Encoder.AsObject[SendTemplate] =
    io.circe.generic.semiauto.deriveEncoder[SendTemplate]

  implicit val sendTemplateResponseStatusDecoder: Decoder[Status] = enumDecoder(Status)

  implicit val sendTemplateResponseRejectReasonDecoder: Decoder[RejectReason] = enumDecoder(RejectReason)

  implicit val endTemplateResponseReads: Decoder[SendTemplateResponse] =
    io.circe.generic.semiauto.deriveDecoder[SendTemplateResponse]

}
