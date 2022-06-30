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
import net.gutefrage.mandrill.messages.{
  Email,
  MergeVar,
  Message,
  Name,
  Recipient,
  RecipientMergeVars,
  SendTemplate,
  SendTemplateResponse,
  Subject,
  TemplateContent,
  TemplateName
}
import io.circe.generic.extras.semiauto._
import io.circe.generic.semiauto._
import net.gutefrage.mandrill.circe.core._
import net.gutefrage.mandrill.messages.Recipient.RecipientType
import net.gutefrage.mandrill.messages.SendTemplateResponse.{RejectReason, Status}

package object messages {

  implicit val emailEncoder = deriveUnwrappedEncoder[Email]

  implicit val nameEncoder = deriveUnwrappedEncoder[Name]

  implicit val recipientTypeEncoder = io.circe.generic.semiauto.deriveEncoder[RecipientType]

  implicit val recipientEncoder = io.circe.generic.semiauto.deriveEncoder[Recipient]

  implicit val mergeVarEncoder = io.circe.generic.semiauto.deriveEncoder[MergeVar]

  implicit val recipientMergeVarsEncoder = io.circe.generic.semiauto.deriveEncoder[RecipientMergeVars]

  implicit val subkjectEncoder = deriveUnwrappedEncoder[Subject]

  implicit val sendTemplateContentEncoder = io.circe.generic.semiauto.deriveEncoder[TemplateContent]

  implicit val sendTemplateMessageEncoder = io.circe.generic.semiauto.deriveEncoder[Message]

  implicit val templateNameeEncoder = deriveUnwrappedEncoder[TemplateName]

  implicit val sendTemplateEncoder = io.circe.generic.semiauto.deriveEncoder[SendTemplate]

  implicit val sendTemplateResponseStatusDecoder = io.circe.generic.semiauto.deriveDecoder[Status]

  implicit val sendTemplateResponseRejectReasonDecoder = io.circe.generic.semiauto.deriveDecoder[RejectReason]

  implicit val endTemplateResponseReads = io.circe.generic.semiauto.deriveDecoder[SendTemplateResponse]

}
