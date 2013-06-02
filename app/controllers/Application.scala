package controllers

import play.api.mvc._
import models.{CalEvent, User}

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(""))
  }

  def healthcheck = Action { implicit request =>
    Ok(views.html.healthcheck("200 OK" + "   header= " + request.headers ))
  }

  def login = Action { implicit request =>
    val user = new User (uid = "")
    Ok(views.html.login(user.userForm))
  }

  def logout = Action { implicit request =>
    val user = new User (uid = "")
    Ok(views.html.login(user.userForm)).withNewSession
  }

  def profile = Action { implicit request =>
    session.get("authenticated") match {
      case None =>
        Redirect(routes.Application.login)
      case Some(x) =>
        request.headers.get("X-Forwarded-User") match {
          case Some(x) =>
            val user = new User (uid = x)
            // uid obtained via SSO, send 200
            Ok(views.html.profile(user,user.userForm))
          case None =>
            // uid not available, send 403
            Status(403)("403 Unauthorized")
        }
    }
  }

  def authenticate = Action { implicit request =>
    request.headers.get("X-Forwarded-User") match {
      case Some(x) =>
        Redirect(routes.Application.profile).withSession(
        "authenticated" -> "yes",
        "userName" -> "JMar")
      case None => Status(403)("403 Unauthorized")
    }
  }

  def schedule = Action { implicit request =>
    request.headers.get("X-Forwarded-User") match {
      case Some(x) =>
        val user = new User (uid = x)
        Ok(views.html.schedule(user))
      case None => Status(403)("403 Unauthorized")
    }
  }

  def icalform = Action { implicit request =>
    request.headers.get("X-Forwarded-User") match {
      case Some(x) =>
        val user = new User (uid = x)
        val event = new CalEvent
        // uid obtained via SSO, send 200
        Ok(views.html.icalform(user,event.eventForm))
      case None => Status(403)("403 Unauthorized")
    }
  }

  def ical = Action { implicit request =>
    session.get("authenticated") match {
      case None =>
        Redirect(routes.Application.login)
      case Some(x) =>
        request.headers.get("X-Forwarded-User") match {
          case Some(x) =>
            val user = new User (uid = x)
            // uid obtained via SSO, send 200
            val event = new CalEvent
            val eventForm = event.eventForm
            eventForm.bindFromRequest.fold(
              formWithErrors => // binding failure, you retrieve the form containing errors,
                BadRequest(views.html.ical_error(user,formWithErrors)),
              value => // binding success, you get the actual value
                Ok(views.html.ical(user,value)).as("application/calendar")
            )
          case None =>
            // uid not available, send 403
            Status(403)("403 Unauthorized")
        }
    }
  }

  def report = Action { implicit request =>
    request.headers.get("X-Forwarded-User") match {
      case Some(x) =>
        val user = new User (uid = x)
        Ok(views.html.report(user))
      case None => Status(403)("403 Unauthorized")
    }
  }

  def submit = Action { implicit request =>
    request.headers.get("X-Forwarded-User") match {
      case Some(x) =>
        val user = new User (uid = x)
        val userForm = user.userForm
        userForm.bindFromRequest.fold(
          formWithErrors => // binding failure, you retrieve the form containing errors,
            BadRequest(views.html.error(user,formWithErrors)),
          value => // binding success, you get the actual value
            Ok(views.html.submit(user,value))
        )
      case None => Status(403)("403 Unauthorized")
    }
  }

}