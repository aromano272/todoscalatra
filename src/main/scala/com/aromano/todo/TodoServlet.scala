package com.aromano.todo

import com.aromano.todo.models.ErrorResponse
import com.aromano.todo.models.Todo
import com.aromano.todo.utils.MongoDBUtils
import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON.serialize
import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

/**
  * Created by andreromano on 09/05/17.
  * Resources:
  *   http://alvinalexander.com/scala/mongodb-how-update-documents-in-collection-scala-casbah
  */
class TodoServlet(mongoColl: MongoCollection) extends TodoscalatraStack with MethodOverride {
//  with JacksonJsonSupport {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
//  protected implicit val jsonFormats: Formats = DefaultFormats

  before("/*") {
//    contentType = formats("json")
    contentType = "application/json"
  }

  after("/*") {
    // disconnect db

  }

  get("/:id") {
    // read
    val id = params("id")

    val errorMessage = new ErrorResponse("Couldn't find a todo with this id").toJson

    var oid: ObjectId = null

    try {
      oid = new ObjectId(id)
    } catch {
      // halt is like a return, its stops the rest form running
      case e: IllegalArgumentException => halt(BadRequest(errorMessage))
    }

    val query = MongoDBObject("_id" -> oid)
    val obj = for (x <- mongoColl.findOne(query)) yield x

    if (obj.isDefined)
      Ok(serialize(MongoDBUtils.convertObjectIdToString(obj.get)))
    else
      BadRequest(errorMessage)

  }

  get("/") {
    // read
    mongoColl.find()

    val dbObj:Iterable[DBObject] = for { x <- mongoColl } yield {
      MongoDBUtils.convertObjectIdToString(x)
    }

    Ok(serialize(dbObj.toList))
  }

  post("/") {
    // create
    val title = params("title")
    val description = params("description")

    val todo = new Todo(title, description)

    val newObj = MongoDBObject(todo.toJson)
    mongoColl += newObj

    Ok(serialize(MongoDBUtils.convertObjectIdToString(newObj)))
  }

  put("/") {
    // update
    val id = params("id")
    val title = params("title")
    val description = params("description")
    val isCompleted = params.getAs[Boolean]("isCompleted").get

    var oid: ObjectId = null

    val errorMessage = new ErrorResponse("Couldn't find a todo with this id").toJson

    try {
      oid = new ObjectId(id)
    } catch {
      // halt is like a return, its stops the rest form running
      case e: IllegalArgumentException => halt(BadRequest(errorMessage))
    }

    val todo = new Todo(title, description, isCompleted)
    val newObj = MongoDBObject(todo.toJson)

    val query = MongoDBObject("_id" -> oid)
    val res = mongoColl.update(query, newObj)

    if (res.wasAcknowledged()) {
      newObj.put("_id", id)
      Ok(serialize(MongoDBUtils.convertObjectIdToString(newObj)))
    } else {
      InternalServerError()
    }

  }

  delete("/") {
    // delete
    val id = params("id")

    var oid: ObjectId = null

    val errorMessage = new ErrorResponse("Couldn't find a todo with this id").toJson

    try {
      oid = new ObjectId(id)
    } catch {
      // halt is like a return, its stops the rest form running
      case e: IllegalArgumentException => halt(BadRequest(errorMessage))
    }

    val query = MongoDBObject("_id" -> new ObjectId(id))
    mongoColl.findAndRemove(query) match {
      case Some(any) => Ok()
      case None => BadRequest(errorMessage)
    }
  }

}
