package models

import java.io._
import java.net.{HttpURLConnection, URL}

/**
 * Created with IntelliJ IDEA.
 * User: JMar
 * Date: 5/22/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */

class Downloader {
  def fetch(url : URL, ofile : File) : Unit = {

    try {
      val connection = url.openConnection().asInstanceOf[HttpURLConnection]
      connection.setRequestMethod("GET")

      val istream: InputStream = connection.getInputStream
      val ostream: OutputStream = new BufferedOutputStream(new FileOutputStream(ofile))
      val byteArray: Array[Byte] = Stream.continually(istream.read).takeWhile(-1 !=).map(_.toByte).toArray

      ostream.write(byteArray)
      ostream.close
      istream.close

    } catch {
      case e: Exception => println(e.printStackTrace())
    }

  }
}
