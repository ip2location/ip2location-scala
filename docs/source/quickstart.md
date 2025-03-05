# Quickstart

## Dependencies

This library requires IP2Location BIN database to function. You may download the BIN database at

-   IP2Location LITE BIN Data (Free): <https://lite.ip2location.com>
-   IP2Location Commercial BIN Data (Comprehensive):
    <https://www.ip2location.com>

## IPv4 BIN vs IPv6 BIN

Use the IPv4 BIN file if you just need to query IPv4 addresses.

Use the IPv6 BIN file if you need to query BOTH IPv4 and IPv6 addresses.

## Requirements ##
Intellij IDEA: https://www.jetbrains.com/idea/

## Sample Codes

### Query geolocation information from BIN database

You can query the geolocation information from the IP2Location BIN database as below:

```scala
object IP2LocationTest {
  def main(args: Array[String]): Unit = {
    val loc = new IP2Location
    try {
      val ip = "8.8.8.8"
      val binfile = "/usr/data/IP-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ZIPCODE-TIMEZONE-ISP-DOMAIN-NETSPEED-AREACODE-WEATHER-MOBILE-ELEVATION-USAGETYPE-ADDRESSTYPE-CATEGORY-DISTRICT-ASN.BIN"

      val useMMF = true
      loc.Open(binfile, useMMF) // initialize with BIN file

      // val binpath = Paths.get(binfile)
      // val binFileBytes = Files.readAllBytes(binpath)
      // loc.Open(binFileBytes) // initialize with byte array

      val rec = loc.IPQuery(ip)
      if ("OK" == rec.getStatus) System.out.println(rec)
      else if ("EMPTY_IP_ADDRESS" == rec.getStatus) System.out.println("IP address cannot be blank.")
      else if ("INVALID_IP_ADDRESS" == rec.getStatus) System.out.println("Invalid IP address.")
      else if ("MISSING_FILE" == rec.getStatus) System.out.println("Invalid database path.")
      else if ("IPV6_NOT_SUPPORTED" == rec.getStatus) System.out.println("This BIN does not contain IPv6 data.")
      else System.out.println("Unknown error." + rec.getStatus)
    } catch {
      case e: Exception =>
        System.out.println(e)
        e.printStackTrace(System.out)
    } finally loc.Close()
  }
}
```