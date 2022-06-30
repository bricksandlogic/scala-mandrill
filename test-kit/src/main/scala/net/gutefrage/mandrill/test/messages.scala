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

package net.gutefrage.mandrill.test

import net.gutefrage.mandrill.messages._
import org.scalacheck.{Arbitrary, Gen}

object messages {

  val mergeVarGen: Gen[MergeVar] = for {
    name <- Gen.alphaNumStr
    content <- Gen.alphaNumStr
  } yield MergeVar(name, content)

  val templateContentGen: Gen[TemplateContent] = for {
    name <- Gen.alphaNumStr
    content <- Gen.alphaNumStr
  } yield TemplateContent(name, content)

  val recipientTypeGen: Gen[Recipient.RecipientType] = Gen.oneOf(Recipient.RecipientType.values.toSeq)

  /**
   * Generate a random example.tld mail address.
   */
  val exampleEMailAddress: Gen[String] = for {
    // We limit to alphanumeric chars to make test output more readable
    length <- Gen.choose(1, 20)
    prefix <- Gen.listOfN(length, Gen.alphaNumChar).map(_.mkString)
    tld <- Gen.oneOf("net", "com", "de", "org")
  } yield s"$prefix@example.$tld"

  val recipientGen: Gen[Recipient] = for {
    email <- exampleEMailAddress.map(Email)
    name <- Gen.option(Gen.alphaNumStr.map(Name))
    recipientType <- Gen.option(recipientTypeGen)
  } yield Recipient(email, name, recipientType)

  val recipientMergeVarsGen: Gen[RecipientMergeVars] = for {
    recipient <- recipientGen
    vars <- Gen.listOf(mergeVarGen)
  } yield RecipientMergeVars(recipient, vars)

  val messageGen: Gen[Message] = for {
    recipientMergeVars <- Gen.listOf(recipientMergeVarsGen)
    globalMergeVars <- Gen.listOf(mergeVarGen)
    subject <- Gen.option(Gen.alphaNumStr.map(Subject))
  } yield Message(recipientMergeVars, globalMergeVars, subject)

  val sendTemplateGen: Gen[SendTemplate] = for {
    key <- core.apiKeyGen
    templateName <- Gen.alphaNumStr.map(TemplateName)
    templateContent <- Gen.listOf(templateContentGen)
    to <- Gen.nonEmptyListOf(recipientGen)
    message <- messageGen
    sendAt <- Gen.option(core.mandrillDateTimeGen)
  } yield
    SendTemplate(
      key,
      templateName,
      templateContent,
      message,
      sendAt
    )

  object arbitrary {
    implicit val arbitraryMergeVar: Arbitrary[MergeVar] = Arbitrary(mergeVarGen)
    implicit val arbitraryTemplateContent: Arbitrary[TemplateContent] = Arbitrary(templateContentGen)
    implicit val arbitraryRecipient: Arbitrary[Recipient] = Arbitrary(recipientGen)
    implicit val arbitraryRecipientMergeVars: Arbitrary[RecipientMergeVars] = Arbitrary(recipientMergeVarsGen)
    implicit val arbitraryMessage: Arbitrary[Message] = Arbitrary(messageGen)
    implicit val arbitrarySendTemplate: Arbitrary[SendTemplate] = Arbitrary(sendTemplateGen)
  }
}
