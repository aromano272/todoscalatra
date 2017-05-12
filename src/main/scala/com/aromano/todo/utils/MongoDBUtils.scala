package com.aromano.todo.utils

import com.mongodb.DBObject

/**
  * Created by andreromano on 12/05/17.
  */
object MongoDBUtils {

  def convertObjectIdToString(dbObject: DBObject): DBObject = {
    val _id = dbObject.get("_id").toString
    dbObject.put("_id", _id)
    dbObject
  }

}
