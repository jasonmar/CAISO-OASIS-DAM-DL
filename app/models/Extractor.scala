package models

import java.io.{FileOutputStream, OutputStream, InputStream, File}
import java.util.jar.JarFile
import scala.collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: JMar
 * Date: 5/22/13
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */
class Extractor {
  private def unZip(file : File, extractPath : File) : List[File] = {

    def copyStream(istream : InputStream, ostream : OutputStream) : Unit = {
      var bytes =  new Array[Byte](1024)
      var len = -1
      while({ len = istream.read(bytes, 0, 1024); len != -1 })
        ostream.write(bytes, 0, len)
    }

    val zipFile = new JarFile(file)
    val zipEntries = zipFile.entries
    var fileList = new mutable.MutableList[File]

    while(zipEntries.hasMoreElements){
      val zipEntry = zipEntries.nextElement
      val zipEntryName = zipEntry.getName
      val istream = zipFile.getInputStream(zipEntry)
      val ofile = new File(extractPath, zipEntryName)
      fileList = fileList.+=:(ofile)
      val ostream = new FileOutputStream(ofile)
      copyStream(istream, ostream)
      ostream.close
      istream.close
    }

    zipFile.close()
    file.delete()
    fileList.toList
  }

  def extract(file : File, extractPath : File) : List[File] = {
    //Extract single zip/jar files or all of them in a directory.
    extractPath.mkdirs()
    unZip(file,extractPath)
  }

}
