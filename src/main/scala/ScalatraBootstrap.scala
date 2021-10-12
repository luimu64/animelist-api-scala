import com.anilist.api._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new aniapi, "/*")
    context.setInitParameter("org.scalatra.cors.allowedOrigins", "http://localhost:3000")
  }
}
