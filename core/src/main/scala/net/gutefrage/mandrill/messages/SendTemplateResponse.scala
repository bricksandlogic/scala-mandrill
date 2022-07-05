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

package net.gutefrage.mandrill.messages

import enumeratum.{Enum, EnumEntry}

/**
 * == Send-Template Response ==
 *
 * Response from the `/messages/send-template.json` endpoint.
 *
 * @param _id Mandrill id for the performed action
 * @param email Receiver email
 * @param status Status of the sent email
 * @param rejectReason Optional reject reason if the email could not be sent
 */
final case class SendTemplateResponse(
  _id: String,
  email: String,
  status: SendTemplateResponse.Status,
  rejectReason: Option[SendTemplateResponse.RejectReason]
)

object SendTemplateResponse {
  sealed abstract class Status(val status: String) extends EnumEntry {
    override def entryName: String = status
  }

  case object Status extends Enum[Status] {
    case object Sent extends Status("sent")
    case object Scheduled extends Status("scheduled")
    case object Queued extends Status("queued")
    case object Rejected extends Status("rejected")
    case object Invalid extends Status("invalid")

    // todo: revert to macros when Enumeratum supports Scala 3
    val values = Vector(Sent, Scheduled, Queued, Rejected, Invalid)
  }

  sealed abstract class RejectReason(val reason: String) extends EnumEntry {
    override def entryName: String = reason
  }

  case object RejectReason extends Enum[RejectReason] {
    case object Rule extends RejectReason("rule")
    case object Spam extends RejectReason("spam")
    case object Unsubscribed extends RejectReason("unsub")
    case object Custom extends RejectReason("custom")
    case object HardBounce extends RejectReason("hard-bounce")
    case object SoftBounce extends RejectReason("soft-bounce")
    case object InvalidSender extends RejectReason("invalid-sender")
    case object TestModeLimit extends RejectReason("test-mode-limit")

    // todo: revert to macros when Enumeratum supports Scala 3
    val values = Vector(Rule, Spam, Unsubscribed, Custom, HardBounce, SoftBounce, InvalidSender, TestModeLimit)
  }

}
