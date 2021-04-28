package com.ip2location

import com.google.gson.{JsonObject, JsonParser}

import java.util.regex._
import java.net.URL
import java.net.URLEncoder

/**
 * This class performs the lookup of IP2Location data from an IP address by querying the IP2Location Web Service.
 * <p>
 * Example usage scenarios:
 * <ul>
 * <li>Redirect based on country</li>
 * <li>Digital rights management</li>
 * <li>Web log stats and analysis</li>
 * <li>Auto-selection of country on forms</li>
 * <li>Filter access from countries you do not do business with</li>
 * <li>Geo-targeting for increased sales and click-through</li>
 * <li>And much, much more!</li>
 * </ul>
 * <p>
 * Copyright (c) 2002-2021 IP2Location.com
 * <p>
 *
 * @author IP2Location.com
 * @version 8.0.1
 */
object IP2LocationWebService {
  private val pattern = Pattern.compile("^[\\dA-Z]{10}$")
  private val pattern2 = Pattern.compile("^WS\\d+$")
}

class IP2LocationWebService() {
  private var _APIKey = ""
  private var _Package = ""
  private var _UseSSL = true

  /**
   * This function initializes the params for the web service.
   *
   * @param APIKey  IP2Location Web Service API key
   * @param Package IP2Location Web Service package (WS1 to WS24)
   * @throws IllegalArgumentException If an invalid parameter is specified
   */
  @throws[IllegalArgumentException]
  def Open(APIKey: String, Package: String): Unit = Open(APIKey, Package, true)

  /**
   * This function initializes the params for the web service.
   *
   * @param APIKey  IP2Location Web Service API key
   * @param Package IP2Location Web Service package (WS1 to WS24)
   * @param UseSSL  Set to true to call the web service using SSL
   * @throws IllegalArgumentException If an invalid parameter is specified
   */
  @throws[IllegalArgumentException]
  def Open(APIKey: String, Package: String, UseSSL: Boolean): Unit = {
    _APIKey = APIKey
    _Package = Package
    _UseSSL = UseSSL
    CheckParams()
  }

  /**
   * This function validates the API key and package params.
   */
  @throws[IllegalArgumentException]
  private def CheckParams(): Unit = if ((!IP2LocationWebService.pattern.matcher(_APIKey).matches) && (!(_APIKey == "demo"))) throw new IllegalArgumentException("Invalid API key.")
  else if (!IP2LocationWebService.pattern2.matcher(_Package).matches) throw new IllegalArgumentException("Invalid package name.")

  /**
   * This function to query IP2Location data.
   *
   * @param IPAddress IP Address you wish to query
   * @throws IllegalArgumentException If an invalid parameter is specified
   * @throws RuntimeException         If an exception occurred at runtime
   * @return IP2Location data
   */
  @throws[IllegalArgumentException]
  @throws[RuntimeException]
  def IPQuery(IPAddress: String): JsonObject = IPQuery(IPAddress, null, "en")

  /**
   * This function to query IP2Location data.
   *
   * @param IPAddress IP Address you wish to query
   * @param Language  The translation language
   * @throws IllegalArgumentException If an invalid parameter is specified
   * @throws RuntimeException         If an exception occurred at runtime
   * @return IP2Location data
   */
  @throws[IllegalArgumentException]
  @throws[RuntimeException]
  def IPQuery(IPAddress: String, Language: String): JsonObject = IPQuery(IPAddress, null, Language)

  /**
   * This function to query IP2Location data.
   *
   * @param IPAddress IP Address you wish to query
   * @param AddOns    The list of AddOns results to return
   * @param Language  The translation language
   * @throws IllegalArgumentException If an invalid parameter is specified
   * @throws RuntimeException         If an exception occurred at runtime
   * @return IP2Location data
   */
  @throws[IllegalArgumentException]
  @throws[RuntimeException]
  def IPQuery(IPAddress: String, AddOns: Array[String], Language: String): JsonObject = try {
    var myurl: String = null
    var myjson: String = null
    CheckParams() // check here in case user haven't called Open yet
    val bf = new StringBuffer
    bf.append("http")
    if (_UseSSL) bf.append("s")
    bf.append("://api.ip2location.com/v2/?key=").append(_APIKey).append("&package=").append(_Package).append("&ip=").append(URLEncoder.encode(IPAddress, "UTF-8")).append("&lang=").append(URLEncoder.encode(Language, "UTF-8"))
    if ((AddOns != null) && (AddOns.length > 0)) bf.append("&addon=").append(URLEncoder.encode(AddOns.mkString(","), "UTF-8"))
    myurl = bf.toString
    myjson = Http.get(new URL(myurl))
    val myresult = JsonParser.parseString(myjson).getAsJsonObject
    myresult
  } catch {
    case ex: IllegalArgumentException =>
      throw ex
    case ex2: Exception =>
      throw new RuntimeException(ex2)
  }

  /**
   * This function to check web service credit balance.
   *
   * @throws IllegalArgumentException If an invalid parameter is specified
   * @throws RuntimeException         If an exception occurred at runtime
   * @return Credit balance
   */
  @throws[IllegalArgumentException]
  @throws[RuntimeException]
  def GetCredit: JsonObject = try {
    var myurl: String = null
    var myjson: String = null
    CheckParams()
    val bf = new StringBuffer
    bf.append("http")
    if (_UseSSL) bf.append("s")
    bf.append("://api.ip2location.com/v2/?key=").append(_APIKey).append("&check=true")
    myurl = bf.toString
    myjson = Http.get(new URL(myurl))
    val myresult = JsonParser.parseString(myjson).getAsJsonObject
    myresult
  } catch {
    case ex: IllegalArgumentException =>
      throw ex
    case ex2: Exception =>
      throw new RuntimeException(ex2)
  }
}