import com.aromano.todo._
import org.scalatra._
import javax.servlet.ServletContext

import com.mongodb.casbah.MongoClient

class ScalatraBootstrap extends LifeCycle {

  val mongoClient = MongoClient();

  override def init(context: ServletContext) {

    val mongoColl = mongoClient("db_title")("table_title")

    context.mount(new MyScalatraServlet(), "/*")
    context.mount(new TodoServlet(mongoColl), "/todo/*")
  }

  override def destroy(context: ServletContext) {
    mongoClient.close
  }

}
