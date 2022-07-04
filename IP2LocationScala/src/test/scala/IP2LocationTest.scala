package com.ip2location

import java.nio.file.Paths
import java.io.IOException
import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite

class IP2LocationTest extends AnyFunSuite with BeforeAndAfter with BeforeAndAfterAll {
  private var loc: IP2Location = _
  private val binfile = "IP2LOCATION-LITE-DB1.BIN"
  private var binfilepath: String = _
  private var binFileBytes: Array[Byte] = _
  private val ip = "8.8.8.8"

  override def beforeAll: Unit = {
    val binpath = Paths.get("src", "test", "resources", binfile)
    binfilepath = binpath.toFile.getAbsolutePath
    import java.nio.file.Files
    binFileBytes = Files.readAllBytes(binpath)
  }

  before {
    loc = new IP2Location
  }

  test("TestOpenException") {
    assertThrows[IOException] {
      loc.Open("dummy.bin")
    }

    assertThrows[NullPointerException] {
      loc.Open(null.asInstanceOf[Array[Byte]])
    }
  }

  test("TestQueryCountryCode") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getCountryShort == "US")
  }

  test("TestQueryCountryLong") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getCountryLong == "United States of America")
  }

  test("TestQueryRegion") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getRegion == "Not_Supported")
  }

  test("TestQueryCity") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getCity == "Not_Supported")
  }

  test("TestQueryLatitude") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getLatitude == 0)
  }

  test("TestQueryLongitude") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getLongitude == 0)
  }

  test("TestQueryZipCode") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getZipCode == "Not_Supported")
  }

  test("TestQueryTimeZone") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getTimeZone == "Not_Supported")
  }

  test("TestQueryISP") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getISP == "Not_Supported")
  }

  test("TestQueryDomain") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getDomain == "Not_Supported")
  }

  test("TestQueryNetSpeed") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getNetSpeed == "Not_Supported")
  }

  test("TestQueryIDDCode") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getIDDCode == "Not_Supported")
  }

  test("TestQueryAreaCode") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getAreaCode == "Not_Supported")
  }

  test("TestQueryWeatherStationCode") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getWeatherStationCode == "Not_Supported")
  }

  test("TestQueryWeatherStationName") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getWeatherStationName == "Not_Supported")
  }

  test("TestQueryMCC") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getMCC == "Not_Supported")
  }

  test("TestQueryMNC") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getMNC == "Not_Supported")
  }

  test("TestQueryMobileBrand") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getMobileBrand == "Not_Supported")
  }

  test("TestQueryElevation") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getElevation == 0)
  }

  test("TestQueryUsageType") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getUsageType == "Not_Supported")
  }

  test("TestQueryAddressType") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getAddressType == "Not_Supported")
  }

  test("TestQueryCategory") {
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assert(rec.getCategory == "Not_Supported")
  }

  after {
    loc.Close()
  }
}
