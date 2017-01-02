/*
 * Copyright 2016-2017 gutefrage.net GmbH
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

import net.gutefrage.mandrill.core.{MandrillApiKey, MandrillDateTime}
import org.joda.time.DateTime

/**
 * == Mandrill Templates ==
 *
 * Modelling the responses/requests for the `/messages/send-template.json` method
 *
 * Every template defined in mandrill is represented by its own type.
 *
 * A template has a `name` and a `message` object, which is represented by
 * [[net.gutefrage.mandrill.messages.SendTemplate.Message]].
 *
 * The message object defines:
 *
 * - the recipients
 * - the recipients individual merge_vars (placeholders)
 * - the global_merge_vars that are inserted in each template
 *
 * == JSON format ==
 *
 * This is the minimal format that gets generated by our API
 *
 * {{{
 * {
 *    "key": "the-api-key",
 *    "subject": "optional subject to override defaults in mandrill template",
 *    "template_name": "template-name",
 *    "template_content": [],
 *    "send_at": "YYYY-MM-DD HH:MM:SS",
 *    "message": {
 *      "to": [{
 *          "email": "test@example.com"
 *      }],
 *      "merge_vars": [
 *        {
 *          "rcpt": "test@example.com",
 *          "vars": [
 *             {
 *               "name": "SOME_LINK",
 *               "content": "https://www.example.com"
 *             }
 *          ]
 *        }
 *      ]
 *    }
 * }
 * }}}
 *
 * @param key A valid API key
 * @param template_name The immutable name or slug of a template that exists in the user's account. For
 *                      backwards-compatibility, the template name may also be used but the immutable slug is preferred.
 * @param template_content Additional template content
 * @param message An array of template content to send. Each item in the array should be a struct with two keys - name:
 *                the name of the content block to set the content for, and content: the actual content to put into the block
 * @param send_at When this message should be sent as a UTC timestamp in YYYY-MM-DD HH:MM:SS format. If you specify a
 *                time in the past, the message will be sent immediately. An additional fee applies for scheduled email,
 *                and this feature is only available to accounts with a positive balance.
 * @see [[https://mandrillapp.com/templates]]
 * @see [[https://mandrillapp.com/api/docs/messages.JSON.html#method=send-template]]
 */
case class SendTemplate(
  key: MandrillApiKey,
  template_name: SendTemplate.TemplateName,
  to: Seq[Recipient] = Nil,
  template_content: Seq[SendTemplate.TemplateContent] = Nil,
  message: SendTemplate.Message = SendTemplate.Message(),
  send_at: Option[MandrillDateTime] = None
) {

  import SendTemplate._

  /**
   * Adds a new recipient for this template.
   *
   * TODO write usage examples
   *
   * @param recipient New recipient
   * @param mergeVars Optional variables for this recipient
   * @return A new template with the recipient added
   */
  def to(recipient: Recipient, mergeVars: Seq[MergeVar] = Nil): SendTemplate = {
    val newRecipients = to :+ recipient
    val newRecipientMergeVars = message.merge_vars ++ (
        if (mergeVars.isEmpty) Seq.empty else Seq(RecipientMergeVars(recipient, mergeVars))
      )
    val newMessage = message.copy(merge_vars = newRecipientMergeVars)
    copy(message = newMessage, to = newRecipients)
  }

  /**
   * Adds a new recipient for this template.
   *
   * TODO write usage examples
   *
   * @param email Recipient email
   * @param mergeVars Optional variables for this recipient
   * @return A new template with the recipient added
   */
  def to(email: String, mergeVars: (String, String)*): SendTemplate =
    to(Recipient(Recipient.Email(email)), mergeVars.map(toMergeVar))

  /**
   * Adds a new recipient for this template with header type CC.
   *
   * TODO write usage examples
   *
   * @param email Recipient email
   * @param mergeVars Optional variables for this recipient
   * @return A new template with the recipient added
   */
  def cc(email: String, mergeVars: (String, String)*): SendTemplate =
    to(Recipient(Recipient.Email(email), `type` = Some(Recipient.RecipientType.Cc)), mergeVars.map(toMergeVar))

  /**
   * Adds a new recipient for this template with header type BCC.
   *
   * TODO write usage examples
   *
   * @param email Recipient email
   * @param mergeVars Optional variables for this recipient
   * @return A new template with the recipient added
   */
  def bcc(email: String, mergeVars: (String, String)*): SendTemplate =
    to(Recipient(Recipient.Email(email), `type` = Some(Recipient.RecipientType.Bcc)), mergeVars.map(toMergeVar))

  /**
   * Adds template contents to this template
   *
   * TODO write usage examples
   *
   * @param first first template content item
   * @param rest optional other template content items
   * @return A new template with template content items added
   */
  def templateContent(first: (String, String), rest: (String, String)*): SendTemplate = {
    val newTemplateContent = template_content ++ (toTemplateContent(first) +: rest.map(toTemplateContent))
    copy(template_content = newTemplateContent)
  }

  /**
   * Schedules this template at the given point in time.
   * If you specify a time in the past, the message will be sent immediately.
   *
   * An additional fee applies for scheduled email,
   * and this feature is only available to accounts with a positive balance.
   *
   * @param schedule The time the message should be sent
   * @return A new template with sendAt date
   */
  def sendAt(schedule: DateTime): SendTemplate = copy(send_at = Some(MandrillDateTime(schedule)))

  private def toMergeVar(nameAndContent: (String, String)) =
    MergeVar(nameAndContent._1, nameAndContent._2)

  private def toTemplateContent(nameAndContent: (String, String)) =
    TemplateContent(nameAndContent._1, nameAndContent._2)

}

object SendTemplate {

  final case class TemplateName(value: String) extends AnyVal

  final case class TemplateContent(name: String, content: String)

  /**
   * Represents a part of the `message` field. Only the required/needed fields are implemented.
   *
   * @param merge_vars define the `to` and `merge_vars` fields as the recipients email is used as a key
   * @param global_merge_vars for global placeholders
   * @param subject optional subject for the email
   */
  final case class Message(
    merge_vars: Seq[RecipientMergeVars] = Nil,
    global_merge_vars: Seq[MergeVar] = Nil,
    subject: Option[Message.Subject] = None
  )

  object Message {
    final case class Subject(value: String) extends AnyVal
  }

}
