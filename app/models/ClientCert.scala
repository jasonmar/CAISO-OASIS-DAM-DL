package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import scala.Some
import javax.security.cert.X509Certificate
import javax.security.cert.X509Certificate.getInstance

/**
 * Created with IntelliJ IDEA.
 * User: jmar
 * Date: 6/2/13
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
case class ClientCert (name: String = "", description: String = "", pem: String = "") {

  val clientCertForm = Form(
    mapping(
      "name" -> text.verifying(nonEmpty,maxLength(128)),
      "description" -> text.verifying(maxLength(256)),
      "pem" -> text.verifying(nonEmpty,maxLength(4096))
    )((name,description,pem) => ClientCert(name,description,pem))
      ((clientCert: ClientCert) => Some(clientCert.name,clientCert.description,clientCert.pem))
  )

  def getSerial : String = {
    val cert = X509Certificate.getInstance(pem.getBytes())
    val serial = cert.getSerialNumber
    serial.toString()
  }

  def getIssuer : String = {
    val cert = X509Certificate.getInstance(pem.getBytes())
    val issuer = cert.getIssuerDN
    issuer.toString
  }

  def getSubject : String = {
    val cert = X509Certificate.getInstance(pem.getBytes())
    val subject = cert.getSubjectDN
    subject.toString
  }

}
