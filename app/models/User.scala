package models

/**
 * Created with IntelliJ IDEA.
 * User: JMar
 * Date: 5/26/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */

import play.api.data.Forms._
import play.api.data.Form
import play.api.data.validation.Constraints._

case class User (uid: String = "anon", name: String = "Anonymous User") {

  val userForm = Form(
    mapping(
      "uid" -> text.verifying(nonEmpty),
      "name" -> text.verifying(nonEmpty)
    )((uid, name) => User(uid,name))
      ((user: User) => Some(user.uid, user.name))
  )

}