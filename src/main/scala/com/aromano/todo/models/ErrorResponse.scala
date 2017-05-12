package com.aromano.todo.models

/**
  * Created by andreromano on 12/05/17.
  */
class ErrorResponse(val message: String) {

  def toJson: String = {
    s"""{
      |   "error": {
      |     "message": "$message"
      |   }
      | }""".stripMargin.stripLineEnd
  }

}
