package org.xi.maple.datacalc

import org.slf4j.{Logger, LoggerFactory}

import scala.annotation.tailrec
import scala.collection.mutable

object ArgsParser {

  private val log: Logger = LoggerFactory.getLogger(ArgsParser.getClass)

  val usage: String =
    """
      |Usage: spark-submit maple.jar [-d <configData> | -f <configFile>]
      |""".stripMargin

  def getParams(args: Array[String]): mutable.Map[String, String] = {
    @tailrec
    def nextOption(map: mutable.Map[String, String], list: List[String]): mutable.Map[String, String] = {
      list match {
        case Nil => map
        case ("-h" | "--help") :: _ =>
          println(usage)
          System.exit(0)
          null
        case ("-f" | "--file") :: value :: tail =>
          nextOption(map ++ Map("file" -> value), tail)
        case ("-d" | "--data") :: value :: tail =>
          nextOption(map ++ Map("data" -> value), tail)
        case key :: value :: tail =>
          if (!key.startsWith("-")) {
            nextOption(map, value :: tail)
          } else if (value.startsWith("-")) {
            nextOption(map ++ Map(key.stripPrefix("-").stripPrefix("-") -> ""), value :: tail)
          } else {
            nextOption(map ++ Map(key.stripPrefix("-").stripPrefix("-") -> value), tail)
          }
        case _ :: tail =>
          nextOption(map, tail)
      }
    }

    val map = mutable.Map[String, String]()
    val result = nextOption(map, args.toList)

    if (result.contains("file") && result.contains("data")) {
      log.error("Config file or config data string cannot be set up at the same time")
      System.exit(1)
    }
    if (!result.contains("file") && !result.contains("data")) {
      log.error("Config file or config data string should be set up")
      System.exit(1)
    }
    result
  }
}
