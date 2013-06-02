package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import scala.Some
import org.joda.time.DateTime

/**
 * Created with IntelliJ IDEA.
 * User: JMar
 * Date: 5/27/13
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */

case class CalEvent (start: DateTime = new DateTime(), startHour: Int = 1, startMin: Int = 0, duration: Int = 60, summary: String = "default summary", location: String = "default location", description: String = "default description", organizerName: String = "", organizerEmail: String = "") {

  val dt0 = new DateTime(start.getYear, start.getMonthOfYear, start.getDayOfMonth , startHour, startMin )
  val dt1 = dt0.plusMinutes(duration)
  val dtstart = dt0.toString("yyyyMMdd") + "T" + dt0.toString("HHmmss")
  val dtend = dt1.toString("yyyyMMdd") + "T" + dt1.toString("HHmmss")

  /**
   *
   *  BEGIN:VCALENDAR
      VERSION:1.0
      BEGIN:VEVENT
      DTSTART:20130527T090000
      DTEND:20130527T100000
      SUMMARY:my event
      LOCATION:my conference room
      DESCRIPTION:my event in my conference room
      PRIORITY:3
      ORGANIZER:"Jason Mar"<jmar@semprautilities.com>
      END:VEVENT
      END:VCALENDAR
   *
   */

  val eventForm = Form(
    mapping(
      "start" -> jodaDate,
      "startHour" -> number.verifying(min(0),max(23)),
      "startMin" -> number.verifying(min(0),max(59)),
      "duration" -> number.verifying(min(0),max(480)),
      "summary" -> text.verifying(nonEmpty),
      "location" -> text.verifying(nonEmpty),
      "description" -> text.verifying(nonEmpty),
      "organizerName" -> text.verifying(nonEmpty),
      "organizerEmail" -> email.verifying(nonEmpty)
    )((start, startHour, startMin, duration, summary, location, description, organizerName, organizerEmail) => CalEvent(start, startHour, startMin, duration, summary, location, description, organizerName, organizerEmail))
      ((event: CalEvent) => Some(event.start, event.startHour, event.startMin, event.duration, event.summary, event.location, event.description, event.organizerName, event.organizerEmail))
  )

}
