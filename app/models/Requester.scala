package models

import java.io.File
import java.net.URL
import org.joda.time.DateTime

/**
 * Created with IntelliJ IDEA.
 * User: JMar
 * Date: 5/22/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
class Requester {
  //val node = "SDG1_1_PDRP01-APND"
  //val startdate = "20130324"

  def oasisRequest (iso : String, node : String, date : DateTime) : List[File] = {

    if (iso != "CAISO") { throw new IllegalArgumentException("Currently only CAISO is supported") }

    val d0 = date.toString("yyyyMMdd")
    val d1 = d0
    val url : URL = new URL("http://oasis.caiso.com/mrtu-oasis/SingleZip?queryname=PRC_LMP&startdate=" + d0 + "&enddate=" + d1 + "&market_run_id=DAM&node=" + node)
    val ofile : File = new File("R:\\" + node + d0 + ".zip")

    val downloader = new Downloader
    val extractor = new Extractor

    downloader.fetch(url,ofile)

    val extractPath : File = new File("R:\\")
    extractor.extract(ofile,extractPath)

  }


}
