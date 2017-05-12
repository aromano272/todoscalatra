package com.aromano.todo.models

import com.mongodb.DBObject
import com.mongodb.casbah.Imports.ObjectId

/**
  * Created by andreromano on 09/05/17.
  */
class Todo(val id: String, val title: String, val description: String, val isCompleted:Boolean) {

  def this() {
    this(null, null, null, false)
  }

  def this(title: String, description:String) {
    this(null, title, description, false)
  }

  def this(title: String, description:String, isCompleted: Boolean) {
    this(null, title, description, isCompleted)
  }

  def toJson: String = {
    if (id != null)
      s"""  {
         |    "_id": "$id",
         |    "title": "$title",
         |    "description": "$description",
         |    "isCompleted": $isCompleted
         |  }
         |  """.stripMargin.stripLineEnd
    else
      s"""  {
         |    "title": "$title",
         |    "description": "$description",
         |    "isCompleted": $isCompleted
         |  }
         |  """.stripMargin.stripLineEnd
  }

  override def toString: String = {
    s""" id: "$id", title: "$title", description: "$description", isCompleted: $isCompleted """
  }

}

object Todo {

  def create(dbObject: DBObject): Todo = {
    val _id = dbObject.get("_id").toString
    val title = dbObject.get("title").toString
    val description = dbObject.get("description").toString
    val isCompleted = dbObject.get("isCompleted").asInstanceOf[Boolean]

    new Todo(_id, title, description, isCompleted)
  }

}
