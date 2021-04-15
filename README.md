# IP2Location IP Geolocation Scala Library

This IP Geolocation Scala library allows user to query an IP address for info such as the visitor’s country, region, city, ISP or company name. In addition, users can also determine extra useful geolocation information such as latitude, longitude, ZIP code, domain name, time zone, connection speed, IDD code, area code, weather station code, weather station name, MCC, MNC, mobile brand name, elevation and usage type. It lookup the IP address from **IP2Location BIN Data** file. This data file can be downloaded at

* Free IP2Location IP Geolocation BIN Data: https://lite.ip2location.com
* Commercial IP2Location IP Geolocation BIN Data: https://www.ip2location.com/database/ip2location

As an alternative, this IP Geolocation Scala library can also call the IP2Location Web Service. This requires an API key. If you don't have an existing API key, you can subscribe for one at the below:

https://www.ip2location.com/web-service/ip2location

## Requirements ##
Intellij IDEA: https://www.jetbrains.com/idea/

## QUERY USING THE BIN FILE

## Functions
Below are the functions supported in this library.

|Function Name|Description|
|---|---|
|Open(DBPath: String, UseMMF: Boolean)|Initialize the component with the BIN file path and whether to use MemoryMappedFile.|
|Open(DBPath: String)|Initialize the component with the BIN file path.|
|IPQuery(IPAddress: String)|Query IP address. This function returns results in the IPResult object.|
|Close()|Destroys the mapped bytes.|

## Result functions
Below are the result functions.

|Function Name|Description|
|---|---|
|getCountryShort|Two-character country code based on ISO 3166.|
|getCountryLong|Country name based on ISO 3166.|
|getRegion|Region or state name.|
|getCity|City name.|
|getLatitude|City level latitude.|
|getLongitude|City level longitude.|
|getZipCode|ZIP code or postal code.|
|getTimeZone|Time zone in UTC (Coordinated Universal Time).|
|getISP|Internet Service Provider (ISP) name.|
|getDomain|Domain name associated to IP address range.|
|getNetSpeed|Internet connection speed <ul><li>(DIAL) Dial-up</li><li>(DSL) DSL/Cable</li><li>(COMP) Company/T1</li></ul>|
|getIDDCode|The IDD prefix to call the city from another country.|
|getAreaCode|A varying length number assigned to geographic areas for call between cities.|
|getWeatherStationCode|Special code to identify the nearest weather observation station.|
|getWeatherStationName|Name of the nearest weather observation station.|
|getMCC|Mobile country code.|
|getMNC|Mobile network code.|
|getMobileBrand|Mobile carrier brand.|
|getElevation|Average height of city above sea level in meters (m).|
|getUsageType|Usage type classification of ISP or company:<ul><li>(COM) Commercial</li><li>(ORG) Organization</li><li>(GOV) Government</li><li>(MIL) Military</li><li>(EDU) University/College/School</li><li>(LIB) Library</li><li>(CDN) Content Delivery Network</li><li>(ISP) Fixed Line ISP</li><li>(MOB) Mobile ISP</li><li>(DCH) Data Center/Web Hosting/Transit</li><li>(SES) Search Engine Spider</li><li>(RSV) Reserved</li></ul>|
|getStatus|Status code of query.|

## Status codes
Below are the status codes.
|Code|Description|
|---|---|
|OK|The query has been successfully performed.|
|EMPTY_IP_ADDRESS|The IP address is empty.|
|INVALID_IP_ADDRESS|The format of the IP address is wrong.|
|MISSING_FILE|The BIN file path is wrong or the BIN file is unreadable.|
|IP_ADDRESS_NOT_FOUND|The IP address does not exists in the BIN file.|
|IPV6_NOT_SUPPORTED|The BIN file does not contain IPv6 data.|

## Usage

```scala
object IP2LocationTest {
  def main(args: Array[String]): Unit = {
    val loc = new IP2Location
    try {
      val ip = "8.8.8.8"
      val binfile = "/usr/data/IP-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ZIPCODE-TIMEZONE-ISP-DOMAIN-NETSPEED-AREACODE-WEATHER-MOBILE-ELEVATION-USAGETYPE.BIN"
      val useMMF = true
      loc.Open(binfile, useMMF)
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

## QUERY USING THE IP2LOCATION IP GEOLOCATION WEB SERVICE

## Functions
Below are the functions supported in this library.

|Function Name|Description|
|---|---|
|Open(APIKey: String, Package: String, UseSSL: Boolean)|Initialize component with the API key and package (WS1 to WS24).|
|IPQuery(IPAddress: String)|Query IP address. This function returns a JsonObject.|
|IPQuery(IPAddress: String, Language: String)|Query IP address and translation language. This function returns a JsonObject.|
|IPQuery(IPAddress: String, AddOns: Array\[String\], Language: String)|Query IP address and Addons. This function returns a JsonObject.|
|GetCredit()|This function returns the web service credit balance in a JsonObject.|

Below are the Addons supported in this library.

|Addon Name|Description|
|---|---|
|continent|Returns continent code, name, hemispheres and translations.|
|country|Returns country codes, country name, flag, capital, total area, population, currency info, language info, IDD, TLD and translations.|
|region|Returns region code, name and translations.|
|city|Returns city name and translations.|
|geotargeting|Returns metro code based on the ZIP/postal code.|
|country_groupings|Returns group acronyms and names.|
|time_zone_info|Returns time zones, DST, GMT offset, sunrise and sunset.|

## Result fields
Below are the result fields.

|Name|
|---|
|<ul><li>country_code</li><li>country_name</li><li>region_name</li><li>city_name</li><li>latitude</li><li>longitude</li><li>zip_code</li><li>time_zone</li><li>isp</li><li>domain</li><li>net_speed</li><li>idd_code</li><li>area_code</li><li>weather_station_code</li><li>weather_station_name</li><li>mcc</li><li>mnc</li><li>mobile_brand</li><li>elevation</li><li>usage_type</li><li>continent<ul><li>name</li><li>code</li><li>hemisphere</li><li>translations</li></ul></li><li>country<ul><li>name</li><li>alpha3_code</li><li>numeric_code</li><li>demonym</li><li>flag</li><li>capital</li><li>total_area</li><li>population</li><li>currency<ul><li>code</li><li>name</li><li>symbol</li></ul></li><li>language<ul><li>code</li><li>name</li></ul></li><li>idd_code</li><li>tld</li><li>translations</li></ul></li><li>region<ul><li>name</li><li>code</li><li>translations</li></ul></li><li>city<ul><li>name</li><li>translations</li></ul></li><li>geotargeting<ul><li>metro</li></ul></li><li>country_groupings</li><li>time_zone_info<ul><li>olson</li><li>current_time</li><li>gmt_offset</li><li>is_dst</li><li>sunrise</li><li>sunset</li></ul></li><ul>|

## Usage

```scala
object IP2LocationTest {
  def main(args: Array[String]): Unit = {
    try {
      val ws = new IP2LocationWebService
      val strIPAddress = "8.8.8.8"
      val strAPIKey = "XXXXXXXX" // edit this to be your own API key
      val strPackage = "WS24"
      val addOn = Array("continent", "country", "region", "city", "geotargeting", "country_groupings", "time_zone_info")
      val strLang = "es"
      val boolSSL = true
      ws.Open(strAPIKey, strPackage, boolSSL)
      var myresult = ws.IPQuery(strIPAddress, addOn, strLang)
      println(myresult.toString)
      if (myresult.get("response") == null) { // standard results
        System.out.println("country_code: " + (if (myresult.get("country_code") != null) myresult.get("country_code").getAsString
        else ""))
        System.out.println("country_name: " + (if (myresult.get("country_name") != null) myresult.get("country_name").getAsString
        else ""))
        System.out.println("region_name: " + (if (myresult.get("region_name") != null) myresult.get("region_name").getAsString
        else ""))
        System.out.println("city_name: " + (if (myresult.get("city_name") != null) myresult.get("city_name").getAsString
        else ""))
        System.out.println("latitude: " + (if (myresult.get("latitude") != null) myresult.get("latitude").getAsString
        else ""))
        System.out.println("longitude: " + (if (myresult.get("longitude") != null) myresult.get("longitude").getAsString
        else ""))
        System.out.println("zip_code: " + (if (myresult.get("zip_code") != null) myresult.get("zip_code").getAsString
        else ""))
        System.out.println("time_zone: " + (if (myresult.get("time_zone") != null) myresult.get("time_zone").getAsString
        else ""))
        System.out.println("isp: " + (if (myresult.get("isp") != null) myresult.get("isp").getAsString
        else ""))
        System.out.println("domain: " + (if (myresult.get("domain") != null) myresult.get("domain").getAsString
        else ""))
        System.out.println("net_speed: " + (if (myresult.get("net_speed") != null) myresult.get("net_speed").getAsString
        else ""))
        System.out.println("idd_code: " + (if (myresult.get("idd_code") != null) myresult.get("idd_code").getAsString
        else ""))
        System.out.println("area_code: " + (if (myresult.get("area_code") != null) myresult.get("area_code").getAsString
        else ""))
        System.out.println("weather_station_code: " + (if (myresult.get("weather_station_code") != null) myresult.get("weather_station_code").getAsString
        else ""))
        System.out.println("weather_station_name: " + (if (myresult.get("weather_station_name") != null) myresult.get("weather_station_name").getAsString
        else ""))
        System.out.println("mcc: " + (if (myresult.get("mcc") != null) myresult.get("mcc").getAsString
        else ""))
        System.out.println("mnc: " + (if (myresult.get("mnc") != null) myresult.get("mnc").getAsString
        else ""))
        System.out.println("mobile_brand: " + (if (myresult.get("mobile_brand") != null) myresult.get("mobile_brand").getAsString
        else ""))
        System.out.println("elevation: " + (if (myresult.get("elevation") != null) myresult.get("elevation").getAsString
        else ""))
        System.out.println("usage_type: " + (if (myresult.get("usage_type") != null) myresult.get("usage_type").getAsString
        else ""))
        System.out.println("credits_consumed: " + (if (myresult.get("credits_consumed") != null) myresult.get("credits_consumed").getAsString
        else ""))
        // continent addon
        if (myresult.get("continent") != null) {
          val continentObj = myresult.getAsJsonObject("continent")
          System.out.println("continent => name: " + continentObj.get("name").getAsString)
          System.out.println("continent => code: " + continentObj.get("code").getAsString)
          val myarr = continentObj.getAsJsonArray("hemisphere")
          System.out.println("continent => hemisphere: " + myarr.toString)
          System.out.println("continent => translations: " + continentObj.getAsJsonObject("translations").get(strLang).getAsString)
        }
        // country addon
        if (myresult.get("country") != null) {
          val countryObj = myresult.getAsJsonObject("country")
          System.out.println("country => name: " + countryObj.get("name").getAsString)
          System.out.println("country => alpha3_code: " + countryObj.get("alpha3_code").getAsString)
          System.out.println("country => numeric_code: " + countryObj.get("numeric_code").getAsString)
          System.out.println("country => demonym: " + countryObj.get("demonym").getAsString)
          System.out.println("country => flag: " + countryObj.get("flag").getAsString)
          System.out.println("country => capital: " + countryObj.get("capital").getAsString)
          System.out.println("country => total_area: " + countryObj.get("total_area").getAsString)
          System.out.println("country => population: " + countryObj.get("population").getAsString)
          System.out.println("country => idd_code: " + countryObj.get("idd_code").getAsString)
          System.out.println("country => tld: " + countryObj.get("tld").getAsString)
          System.out.println("country => translations: " + countryObj.getAsJsonObject("translations").get(strLang).getAsString)
          val currencyObj = countryObj.getAsJsonObject("currency")
          System.out.println("country => currency => code: " + currencyObj.get("code").getAsString)
          System.out.println("country => currency => name: " + currencyObj.get("name").getAsString)
          System.out.println("country => currency => symbol: " + currencyObj.get("symbol").getAsString)
          val languageObj = countryObj.getAsJsonObject("language")
          System.out.println("country => language => code: " + languageObj.get("code").getAsString)
          System.out.println("country => language => name: " + languageObj.get("name").getAsString)
        }
        // region addon
        if (myresult.get("region") != null) {
          val regionObj = myresult.getAsJsonObject("region")
          System.out.println("region => name: " + regionObj.get("name").getAsString)
          System.out.println("region => code: " + regionObj.get("code").getAsString)
          System.out.println("region => translations: " + regionObj.getAsJsonObject("translations").get(strLang).getAsString)
        }
        // city addon
        if (myresult.get("city") != null) {
          val cityObj = myresult.getAsJsonObject("city")
          System.out.println("city => name: " + cityObj.get("name").getAsString)
          System.out.println("city => translations: " + cityObj.getAsJsonArray("translations").toString)
        }
        // geotargeting addon
        if (myresult.get("geotargeting") != null) {
          val geoObj = myresult.getAsJsonObject("geotargeting")
          System.out.println("geotargeting => metro: " + geoObj.get("metro").getAsString)
        }
        // country_groupings addon
        if (myresult.get("country_groupings") != null) {
          val myarr = myresult.getAsJsonArray("country_groupings")
          if (myarr.size > 0) for (x <- 0 until myarr.size) {
            System.out.println("country_groupings => #" + x + " => acronym: " + myarr.get(x).getAsJsonObject.get("acronym").getAsString)
            System.out.println("country_groupings => #" + x + " => name: " + myarr.get(x).getAsJsonObject.get("name").getAsString)
          }
        }
        // time_zone_info addon
        if (myresult.get("time_zone_info") != null) {
          val tzObj = myresult.getAsJsonObject("time_zone_info")
          System.out.println("time_zone_info => olson: " + tzObj.get("olson").getAsString)
          System.out.println("time_zone_info => current_time: " + tzObj.get("current_time").getAsString)
          System.out.println("time_zone_info => gmt_offset: " + tzObj.get("gmt_offset").getAsString)
          System.out.println("time_zone_info => is_dst: " + tzObj.get("is_dst").getAsString)
          System.out.println("time_zone_info => sunrise: " + tzObj.get("sunrise").getAsString)
          System.out.println("time_zone_info => sunset: " + tzObj.get("sunset").getAsString)
        }
      }
      else System.out.println("Error: " + myresult.get("response").getAsString)
      myresult = ws.GetCredit
      if (myresult.get("response") != null) System.out.println("Credit balance: " + myresult.get("response").getAsString)
    } catch {
      case e: Exception =>
        System.out.println(e)
        e.printStackTrace(System.out)
    }
  }
}
```
