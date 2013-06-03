package controllers

import play.api.mvc._
import models.{Requester, NodePrice, User}
import org.joda.time.DateTime
import scala.io.Source
import play.libs.Json

object Pricing extends Controller {



  def price = Action { implicit request =>
    request.headers.get("X-Forwarded-User") match {
      case Some(x) =>
        val user = new User (uid = x)
        val nodePrice = new NodePrice()
        Ok(views.html.form_price(user,nodePrice,nodePrice.pricingForm))
      case None => Status(403)("403 Unauthorized")
    }
  }

  def pricing = Action { implicit request =>
    session.get("authenticated") match {
      case None =>
        Redirect(routes.Application.login)
      case Some(x) =>
        request.headers.get("X-Forwarded-User") match {
          case Some(x) =>
            val user = new User (uid = x)


            lazy val iso = request.body.asFormUrlEncoded.get("iso")(0)
            lazy val node = request.body.asFormUrlEncoded.get("node")(0)
            lazy val date : DateTime = new DateTime(request.body.asFormUrlEncoded.get("date")(0))

            val nodePrice = new NodePrice(iso,node,date)
            val fileList = nodePrice.fetch
            var content = new String
            fileList.foreach(f =>
              for(line <- Source.fromFile(f).getLines())
                content = content + line
            )

            nodePrice.pricingForm.bindFromRequest.fold(
              formWithErrors => // binding failure, you retrieve the form containing errors,
                BadRequest(views.html.error_pricing(user,formWithErrors)),
              value => // binding success, you get the actual value
                Ok(views.html.viewpost_pricing(user,value,content))
            )
          case None =>
            // uid not available, send 403
            Status(403)("403 Unauthorized")
        }
    }
  }

}