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

import java.time.{Instant, LocalDateTime}
import java.util.TimeZone

import net.gutefrage.mandrill.core._
import org.scalacheck.{Arbitrary, Gen}

object core {

  val apiKeyGen: Gen[MandrillApiKey] = Gen.identifier.map(MandrillApiKey)

  val apiErrorNameGen: Gen[MandrillApiErrorName] = Gen.oneOf(MandrillApiErrorName.values.toSeq)

  val apiErrorGen: Gen[MandrillApiError] = for {
    message <- Gen.alphaNumStr
    errorName <- apiErrorNameGen
  } yield MandrillApiError(message, errorName)

  val mandrillDateTimeGen: Gen[MandrillDateTime] =
    Gen
      .posNum[Long]
      .map(
        millis =>
          MandrillDateTime(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis),
                                    TimeZone
                                      .getDefault()
                                      .toZoneId())))

  object arbitrary {
    implicit val arbitraryApiKey: Arbitrary[MandrillApiKey] = Arbitrary(apiKeyGen)
    implicit val arbitraryApiError: Arbitrary[MandrillApiError] = Arbitrary(apiErrorGen)
  }
}
