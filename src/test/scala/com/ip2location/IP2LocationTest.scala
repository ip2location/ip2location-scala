package com.ip2location

import java.nio.file.{Files, Paths}
import java.io.IOException
import munit.FunSuite

class IP2LocationTest extends FunSuite {

  private val binfile = "IP2LOCATION-LITE-DB1.BIN"
  private var binfilepath: String = _
  private val ip = "8.8.8.8"

  // Setup logic that runs once for the whole class
  override def beforeAll(): Unit = {
    val binpath = Paths.get("src", "test", "resources", binfile)
    if (!Files.exists(binpath)) {
      fail(s"BIN file not found at ${binpath.toAbsolutePath}. Please ensure it is in src/test/resources/")
    }
    binfilepath = binpath.toFile.getAbsolutePath
  }

  // Helper to manage the 'loc' lifecycle safely
  // This replaces 'before' and 'after' with a "Fixture"
  val locFixture = FunFixture[IP2Location](
    setup = { test =>
      val loc = new IP2Location()
      loc
    },
    teardown = { loc =>
      loc.Close()
    }
  )

  test("TestOpenException") {
    val loc = new IP2Location()
    // MUnit uses intercept instead of assertThrows
    intercept[IOException] {
      loc.Open("dummy.bin")
    }

    intercept[NullPointerException] {
      loc.Open(null.asInstanceOf[Array[Byte]])
    }
  }

  // Use the fixture for individual tests to ensure Close() is always called
  locFixture.test("TestQueryCountryCode") { loc =>
    loc.Open(binfilepath)
    val rec = loc.IPQuery(ip)
    assertEquals(rec.getCountryShort, "US")
  }
}