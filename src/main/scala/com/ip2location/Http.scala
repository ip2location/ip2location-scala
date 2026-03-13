package com.ip2location

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object Http {
  def get(url: URL): String = try {
    java.lang.System.setProperty("https.protocols", "TLSv1.2")
    val conn = url.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("GET")
    conn.setRequestProperty("Accept", "application/json")
    if (conn.getResponseCode != 200) return "Failed : HTTP error code : " + conn.getResponseCode
    val br = new BufferedReader(new InputStreamReader(conn.getInputStream))
    var output: String = null
    val resultFromHttp = new StringBuilder
    while ( {
      output = br.readLine
      output != null
    }) resultFromHttp.append(output).append("\n")
    br.close()
    conn.disconnect()
    resultFromHttp.toString
  } catch {
    case e: Exception =>
      throw new RuntimeException(e)
  }

  def post(url: URL, post: String): String = try {
    java.lang.System.setProperty("https.protocols", "TLSv1.2")
    val conn = url.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("POST")
    conn.setRequestProperty("Accept", "application/json")
    val urlParameters = post
    conn.setDoOutput(true)
    val dos = new DataOutputStream(conn.getOutputStream)
    dos.writeBytes(urlParameters)
    dos.flush()
    dos.close()
    if (conn.getResponseCode != 200) return "Failed : HTTP error code : " + conn.getResponseCode
    val br = new BufferedReader(new InputStreamReader(conn.getInputStream))
    var output: String = null
    val resultFromHttp = new StringBuilder()
    while ( {
      output = br.readLine
      output != null
    }) resultFromHttp.append(output).append("\n")
    br.close()
    conn.disconnect()
    resultFromHttp.toString
  } catch {
    case e: Exception =>
      throw new RuntimeException(e)
  }
}