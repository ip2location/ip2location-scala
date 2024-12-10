package com.ip2location

/**
 * This class is used to store the geolocation data that is returned by the IP2Location class.
 * <p>
 * Copyright (c) 2002-2024 IP2Location.com
 * <p>
 *
 * @author IP2Location.com
 * @version 8.3.1
 */
object IPResult {
  private[ip2location] val NOT_SUPPORTED = "Not_Supported"
}

class IPResult private[ip2location](var ip_address: String) {
  private[ip2location] var country_short: String = _
  private[ip2location] var country_long: String = _
  private[ip2location] var region: String = _
  private[ip2location] var city: String = _
  private[ip2location] var isp: String = _
  private[ip2location] var latitude: Float = _
  private[ip2location] var longitude: Float = _
  private[ip2location] var domain: String = _
  private[ip2location] var zipcode: String = _
  private[ip2location] var netspeed: String = _
  private[ip2location] var timezone: String = _
  private[ip2location] var iddcode: String = _
  private[ip2location] var areacode: String = _
  private[ip2location] var weatherstationcode: String = _
  private[ip2location] var weatherstationname: String = _
  private[ip2location] var mcc: String = _
  private[ip2location] var mnc: String = _
  private[ip2location] var mobilebrand: String = _
  private[ip2location] var elevation: Float = _
  private[ip2location] var usagetype: String = _
  private[ip2location] var addresstype: String = _
  private[ip2location] var category: String = _
  private[ip2location] var district: String = _
  private[ip2location] var asn: String = _
  private[ip2location] var as: String = _
  private[ip2location] var status: String = _
  private[ip2location] val version: String = "Version 8.3.0"

  /**
   * This method to get two-character country code based on ISO 3166.
   *
   * @return the country code
   */
  def getCountryShort: String = country_short

  /**
   * This method to get country name based on ISO 3166.
   *
   * @return the country name
   */
  def getCountryLong: String = country_long

  /**
   * This method to get region or state name.
   *
   * @return the region or state name
   */
  def getRegion: String = region

  /**
   * This method to get city name.
   *
   * @return the city name
   */
  def getCity: String = city

  /**
   * This method to get Internet Service Provider (ISP) name.
   *
   * @return the ISP name
   */
  def getISP: String = isp

  /**
   * This method to get city latitude.
   *
   * @return the city latitude
   */
  def getLatitude: Float = latitude

  /**
   * This method to get city longitude.
   *
   * @return the city longitude
   */
  def getLongitude: Float = longitude

  /**
   * This method to get IP internet domain name associated to IP address range.
   *
   * @return the domain name
   */
  def getDomain: String = domain

  /**
   * This method to get ZIP/Postal code.
   *
   * @return the ZIP/Postal code
   */
  def getZipCode: String = zipcode

  /**
   * This method to get UTC time zone.
   *
   * @return the time zone
   */
  def getTimeZone: String = timezone

  /**
   * This method to get internet connection speed (DIAL) DIAL-UP,(DSL) DSL/CABLE or(COMP) COMPANY
   *
   * @return the net speed
   */
  def getNetSpeed: String = netspeed

  /**
   * This method to get the IDD prefix to call the city from another country.
   *
   * @return the idd code
   */
  def getIDDCode: String = iddcode

  /**
   * This method to get the varying length number assigned to geographic areas for call between cities.
   *
   * @return the area code
   */
  def getAreaCode: String = areacode

  /**
   * This method to get the special code to identify the nearest weather observation station.
   *
   * @return the weather station code
   */
  def getWeatherStationCode: String = weatherstationcode

  /**
   * This method to get the name of the nearest weather observation station.
   *
   * @return the weather station name
   */
  def getWeatherStationName: String = weatherstationname

  /**
   * This method to get the mobile country code.
   *
   * @return the mobile country code
   */
  def getMCC: String = mcc

  /**
   * This method to get the mobile network code.
   *
   * @return the mobile network code
   */
  def getMNC: String = mnc

  /**
   * This method to get the mobile brand.
   *
   * @return the mobile brand
   */
  def getMobileBrand: String = mobilebrand

  /**
   * This method to get city elevation.
   *
   * @return the city elevation
   */
  def getElevation: Float = elevation

  /**
   * This method to get usage type.
   *
   * @return the usage type
   */
  def getUsageType: String = usagetype

  /**
   * This method to get address type.
   *
   * @return the address type
   */
  def getAddressType: String = addresstype

  /**
   * This method to get category.
   *
   * @return the category
   */
  def getCategory: String = category

  /**
   * This method to get district.
   *
   * @return the district
   */
  def getDistrict: String = district

  /**
   * This method to get the autonomous system number (ASN).
   *
   * @return the ASN
   */
  def getASN: String = asn

  /**
   * This method to get the autonomous system (AS).
   *
   * @return the AS
   */
  def getAS: String = as

  /**
   * This method to get status code of query.
   *
   * @return the status code
   */
  def getStatus: String = status

  /**
   * This method to get component version.
   *
   * @return the component version.
   */
  def getVersion: String = version

  /**
   * This method to return all the fields.
   *
   * @return the result in a formatted string.
   */
  override def toString: String = {
    val NL = System.getProperty("line.separator")
    val buf = new StringBuffer("IP2LocationRecord:" + NL)
    buf.append("\tIP Address = " + ip_address + NL)
    buf.append("\tCountry Short = " + country_short + NL)
    buf.append("\tCountry Long = " + country_long + NL)
    buf.append("\tRegion = " + region + NL)
    buf.append("\tCity = " + city + NL)
    buf.append("\tISP = " + isp + NL)
    buf.append("\tLatitude = " + latitude + NL)
    buf.append("\tLongitude = " + longitude + NL)
    buf.append("\tDomain = " + domain + NL)
    buf.append("\tZipCode = " + zipcode + NL)
    buf.append("\tTimeZone = " + timezone + NL)
    buf.append("\tNetSpeed = " + netspeed + NL)
    buf.append("\tIDDCode = " + iddcode + NL)
    buf.append("\tAreaCode = " + areacode + NL)
    buf.append("\tWeatherStationCode = " + weatherstationcode + NL)
    buf.append("\tWeatherStationName = " + weatherstationname + NL)
    buf.append("\tMCC = " + mcc + NL)
    buf.append("\tMNC = " + mnc + NL)
    buf.append("\tMobileBrand = " + mobilebrand + NL)
    buf.append("\tElevation = " + elevation + NL)
    buf.append("\tUsageType = " + usagetype + NL)
    buf.append("\tAddressType = " + addresstype + NL)
    buf.append("\tCategory = " + category + NL)
    buf.append("\tDistrict = " + district + NL)
    buf.append("\tASN = " + asn + NL)
    buf.append("\tAS = " + as + NL)
    buf.toString
  }
}