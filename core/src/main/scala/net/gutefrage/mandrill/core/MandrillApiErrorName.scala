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

package net.gutefrage.mandrill.core

import enumeratum.{Enum, EnumEntry}

/**
 * == Mandrill API Error ==
 *
 * Mandrill has a fixed format for API errors, which is represented by this case object structure
 */
sealed abstract class MandrillApiErrorName extends EnumEntry

case object MandrillApiErrorName extends Enum[MandrillApiErrorName] {

  // common errors
  case object Invalid_Key extends MandrillApiErrorName
  case object ValidationError extends MandrillApiErrorName
  case object GeneralError extends MandrillApiErrorName
  case object Unknown_Subaccount extends MandrillApiErrorName

  // messages errors
  case object Unknown_Template extends MandrillApiErrorName
  case object PaymentRequired extends MandrillApiErrorName

  // todo: revert to macros when Enumeratum supports Scala 3
  val values = Vector(Invalid_Key, ValidationError, GeneralError, Unknown_Subaccount, Unknown_Template, PaymentRequired)

}
