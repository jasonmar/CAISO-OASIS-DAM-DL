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
import org.joda.time.DateTime
import java.io.File

case class NodePrice (iso: String = "CAISO", node: String = "", date: DateTime = new DateTime()) {

  lazy val pricingForm = Form(
    mapping(
      "iso" -> text.verifying(nonEmpty),
      "node" -> text.verifying(nonEmpty),
      "date" -> jodaDate
    )((iso,node,date) => NodePrice(iso,node,date))
      ((nodePrice: NodePrice) => Some(nodePrice.iso, nodePrice.node, nodePrice.date))
  )

  def fetch : List[File] = {
    val requester = new Requester
    val fileList = requester.oasisRequest(iso,node,date)
    fileList
  }

}