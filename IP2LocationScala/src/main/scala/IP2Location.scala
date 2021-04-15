package com.ip2location

import java.net.InetAddress
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.UnknownHostException
import java.io._
import java.util._
import java.util.regex._
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * This class performs the lookup of IP2Location data from an IP address by reading a BIN file.
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
 * @version 8.5.2
 */
object IP2Location {
  private val pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$") // IPv4
  private val pattern2 = Pattern.compile("^([0-9A-F]{1,4}:){6}(0[0-9]+\\.|.*?\\.0[0-9]+).*$", Pattern.CASE_INSENSITIVE)
  private val pattern3 = Pattern.compile("^[0-9]+$")
  private val pattern4 = Pattern.compile("^(.*:)(([0-9]+\\.){3}[0-9]+)$")
  private val pattern5 = Pattern.compile("^.*((:[0-9A-F]{1,4}){2})$")
  private val pattern6 = Pattern.compile("^[0:]+((:[0-9A-F]{1,4}){1,2})$", Pattern.CASE_INSENSITIVE)
  private val MAX_IPV4_RANGE = new BigInteger("4294967295")
  private val MAX_IPV6_RANGE = new BigInteger("340282366920938463463374607431768211455")
  private val FROM_6TO4 = new BigInteger("42545680458834377588178886921629466624")
  private val TO_6TO4 = new BigInteger("42550872755692912415807417417958686719")
  private val FROM_TEREDO = new BigInteger("42540488161975842760550356425300246528")
  private val TO_TEREDO = new BigInteger("42540488241204005274814694018844196863")
  private val LAST_32BITS = new BigInteger("4294967295")
  private val COUNTRY_POSITION = Array(0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
  private val REGION_POSITION = Array(0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3)
  private val CITY_POSITION = Array(0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4)
  private val ISP_POSITION = Array(0, 0, 3, 0, 5, 0, 7, 5, 7, 0, 8, 0, 9, 0, 9, 0, 9, 0, 9, 7, 9, 0, 9, 7, 9)
  private val LATITUDE_POSITION = Array(0, 0, 0, 0, 0, 5, 5, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5)
  private val LONGITUDE_POSITION = Array(0, 0, 0, 0, 0, 6, 6, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6)
  private val DOMAIN_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 6, 8, 0, 9, 0, 10, 0, 10, 0, 10, 0, 10, 8, 10, 0, 10, 8, 10)
  private val ZIPCODE_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 7, 7, 7, 0, 7, 7, 7, 0, 7, 0, 7, 7, 7, 0, 7)
  private val TIMEZONE_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 7, 8, 8, 8, 7, 8, 0, 8, 8, 8, 0, 8)
  private val NETSPEED_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 11, 0, 11, 8, 11, 0, 11, 0, 11, 0, 11)
  private val IDDCODE_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 12, 0, 12, 0, 12, 9, 12, 0, 12)
  private val AREACODE_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 13, 0, 13, 0, 13, 10, 13, 0, 13)
  private val WEATHERSTATIONCODE_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 14, 0, 14, 0, 14, 0, 14)
  private val WEATHERSTATIONNAME_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 15, 0, 15, 0, 15, 0, 15)
  private val MCC_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 16, 0, 16, 9, 16)
  private val MNC_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 17, 0, 17, 10, 17)
  private val MOBILEBRAND_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 18, 0, 18, 11, 18)
  private val ELEVATION_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 19, 0, 19)
  private val USAGETYPE_POSITION = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 20)
}

class IP2Location() {
  private var _MetaData: MetaData = _
  private var _IPv4Buffer: MappedByteBuffer = _
  private var _IPv6Buffer: MappedByteBuffer = _
  private var _MapDataBuffer: MappedByteBuffer = _
  private val _IndexArrayIPv4 = Array.ofDim[Int](65536, 2)
  private val _IndexArrayIPv6 = Array.ofDim[Int](65536, 2)
  private var _IPv4Offset: Long = _
  private var _IPv6Offset: Long = _
  private var _MapDataOffset: Long = _
  private var _IPv4ColumnSize: Int = _
  private var _IPv6ColumnSize: Int = _
  /**
   * To use memory mapped file for faster queries, set to true.
   */
  var UseMemoryMappedFile = false
  /**
   * Sets the path for the BIN database (IPv4 BIN or IPv4+IPv6 BIN).
   */
  var IPDatabasePath = ""
  private var COUNTRY_POSITION_OFFSET: Int = _
  private var REGION_POSITION_OFFSET: Int = _
  private var CITY_POSITION_OFFSET: Int = _
  private var ISP_POSITION_OFFSET: Int = _
  private var DOMAIN_POSITION_OFFSET: Int = _
  private var ZIPCODE_POSITION_OFFSET: Int = _
  private var LATITUDE_POSITION_OFFSET: Int = _
  private var LONGITUDE_POSITION_OFFSET: Int = _
  private var TIMEZONE_POSITION_OFFSET: Int = _
  private var NETSPEED_POSITION_OFFSET: Int = _
  private var IDDCODE_POSITION_OFFSET: Int = _
  private var AREACODE_POSITION_OFFSET: Int = _
  private var WEATHERSTATIONCODE_POSITION_OFFSET: Int = _
  private var WEATHERSTATIONNAME_POSITION_OFFSET: Int = _
  private var MCC_POSITION_OFFSET: Int = _
  private var MNC_POSITION_OFFSET: Int = _
  private var MOBILEBRAND_POSITION_OFFSET: Int = _
  private var ELEVATION_POSITION_OFFSET: Int = _
  private var USAGETYPE_POSITION_OFFSET: Int = _
  private var COUNTRY_ENABLED: Boolean = _
  private var REGION_ENABLED: Boolean = _
  private var CITY_ENABLED: Boolean = _
  private var ISP_ENABLED: Boolean = _
  private var LATITUDE_ENABLED: Boolean = _
  private var LONGITUDE_ENABLED: Boolean = _
  private var DOMAIN_ENABLED: Boolean = _
  private var ZIPCODE_ENABLED: Boolean = _
  private var TIMEZONE_ENABLED: Boolean = _
  private var NETSPEED_ENABLED: Boolean = _
  private var IDDCODE_ENABLED: Boolean = _
  private var AREACODE_ENABLED: Boolean = _
  private var WEATHERSTATIONCODE_ENABLED: Boolean = _
  private var WEATHERSTATIONNAME_ENABLED: Boolean = _
  private var MCC_ENABLED: Boolean = _
  private var MNC_ENABLED: Boolean = _
  private var MOBILEBRAND_ENABLED: Boolean = _
  private var ELEVATION_ENABLED: Boolean = _
  private var USAGETYPE_ENABLED: Boolean = _

  /**
   * This function can be used to pre-load the BIN file.
   *
   * @param DBPath The full path to the IP2Location BIN database file
   * @throws IOException If an input or output exception occurred
   */
  @throws[IOException]
  def Open(DBPath: String): Unit = {
    IPDatabasePath = DBPath
    LoadBIN
  }

  /**
   * This function can be used to initialized the component with params and pre-load the BIN file.
   *
   * @param DBPath The full path to the IP2Location BIN database file
   * @param UseMMF Set to true to load the BIN database file into memory mapped file
   * @throws IOException If an input or output exception occurred
   */
  @throws[IOException]
  def Open(DBPath: String, UseMMF: Boolean): Unit = {
    UseMemoryMappedFile = UseMMF
    Open(DBPath)
  }

  /**
   * This function destroys the mapped bytes.
   */
  def Close(): Unit = {
    _MetaData = null
    DestroyMappedBytes()
  }

  private def DestroyMappedBytes(): Unit = {
    _IPv4Buffer = null
    _IPv6Buffer = null
    _MapDataBuffer = null
  }

  @throws[IOException]
  private def CreateMappedBytes(): Unit = {
    var aFile: RandomAccessFile = null // shift here to address file handle exhaustion issue and NOT using FINAL variable
    try {
      aFile = new RandomAccessFile(IPDatabasePath, "r")
      val inChannel = aFile.getChannel
      CreateMappedBytes(inChannel)
    } finally if (aFile != null) aFile.close()
  }

  @throws[IOException]
  private def CreateMappedBytes(inChannel: FileChannel): Unit = {
    if (_IPv4Buffer == null) {
      val _IPv4Bytes = _IPv4ColumnSize.toLong * _MetaData.DBCount.toLong
      _IPv4Offset = _MetaData.BaseAddr - 1
      _IPv4Buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, _IPv4Offset, _IPv4Bytes)
      _IPv4Buffer.order(ByteOrder.LITTLE_ENDIAN)
      _MapDataOffset = _IPv4Offset + _IPv4Bytes
    }
    if (!_MetaData.OldBIN && _IPv6Buffer == null) {
      val _IPv6Bytes = _IPv6ColumnSize.toLong * _MetaData.DBCountIPv6.toLong
      _IPv6Offset = _MetaData.BaseAddrIPv6 - 1
      _IPv6Buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, _IPv6Offset, _IPv6Bytes)
      _IPv6Buffer.order(ByteOrder.LITTLE_ENDIAN)
      _MapDataOffset = _IPv6Offset + _IPv6Bytes
    }
    if (_MapDataBuffer == null) {
      _MapDataBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, _MapDataOffset, inChannel.size - _MapDataOffset)
      _MapDataBuffer.order(ByteOrder.LITTLE_ENDIAN)
    }
  }

  @throws[IOException]
  private def LoadBIN = {
    var loadOK = false
    var aFile: RandomAccessFile = null
    try if (IPDatabasePath.nonEmpty) {
      aFile = new RandomAccessFile(IPDatabasePath, "r")
      val inChannel = aFile.getChannel
      val _HeaderBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, 64) // 64 bytes header
      _HeaderBuffer.order(ByteOrder.LITTLE_ENDIAN)
      _MetaData = new MetaData
      _MetaData.DBType = _HeaderBuffer.get(0)
      _MetaData.DBColumn = _HeaderBuffer.get(1)
      _MetaData.DBYear = _HeaderBuffer.get(2)
      _MetaData.DBMonth = _HeaderBuffer.get(3)
      _MetaData.DBDay = _HeaderBuffer.get(4)
      _MetaData.DBCount = _HeaderBuffer.getInt(5) // 4 bytes
      _MetaData.BaseAddr = _HeaderBuffer.getInt(9)
      _MetaData.DBCountIPv6 = _HeaderBuffer.getInt(13)
      _MetaData.BaseAddrIPv6 = _HeaderBuffer.getInt(17)
      _MetaData.IndexBaseAddr = _HeaderBuffer.getInt(21) //4 bytes
      _MetaData.IndexBaseAddrIPv6 = _HeaderBuffer.getInt(25)
      if (_MetaData.IndexBaseAddr > 0) _MetaData.Indexed = true
      if (_MetaData.DBCountIPv6 == 0) { // old style IPv4-only BIN file
        _MetaData.OldBIN = true
      }
      else if (_MetaData.IndexBaseAddrIPv6 > 0) _MetaData.IndexedIPv6 = true
      val dbcoll = _MetaData.DBColumn
      _IPv4ColumnSize = dbcoll << 2 // 4 bytes each column
      _IPv6ColumnSize = 16 + ((dbcoll - 1) << 2) // 4 bytes each column, except IPFrom column which is 16 bytes
      val dbtype = _MetaData.DBType

      COUNTRY_POSITION_OFFSET = if (IP2Location.COUNTRY_POSITION(dbtype) != 0) (IP2Location.COUNTRY_POSITION(dbtype) - 2) << 2
      else 0
      REGION_POSITION_OFFSET = if (IP2Location.REGION_POSITION(dbtype) != 0) (IP2Location.REGION_POSITION(dbtype) - 2) << 2
      else 0
      CITY_POSITION_OFFSET = if (IP2Location.CITY_POSITION(dbtype) != 0) (IP2Location.CITY_POSITION(dbtype) - 2) << 2
      else 0
      ISP_POSITION_OFFSET = if (IP2Location.ISP_POSITION(dbtype) != 0) (IP2Location.ISP_POSITION(dbtype) - 2) << 2
      else 0
      DOMAIN_POSITION_OFFSET = if (IP2Location.DOMAIN_POSITION(dbtype) != 0) (IP2Location.DOMAIN_POSITION(dbtype) - 2) << 2
      else 0
      ZIPCODE_POSITION_OFFSET = if (IP2Location.ZIPCODE_POSITION(dbtype) != 0) (IP2Location.ZIPCODE_POSITION(dbtype) - 2) << 2
      else 0
      LATITUDE_POSITION_OFFSET = if (IP2Location.LATITUDE_POSITION(dbtype) != 0) (IP2Location.LATITUDE_POSITION(dbtype) - 2) << 2
      else 0
      LONGITUDE_POSITION_OFFSET = if (IP2Location.LONGITUDE_POSITION(dbtype) != 0) (IP2Location.LONGITUDE_POSITION(dbtype) - 2) << 2
      else 0
      TIMEZONE_POSITION_OFFSET = if (IP2Location.TIMEZONE_POSITION(dbtype) != 0) (IP2Location.TIMEZONE_POSITION(dbtype) - 2) << 2
      else 0
      NETSPEED_POSITION_OFFSET = if (IP2Location.NETSPEED_POSITION(dbtype) != 0) (IP2Location.NETSPEED_POSITION(dbtype) - 2) << 2
      else 0
      IDDCODE_POSITION_OFFSET = if (IP2Location.IDDCODE_POSITION(dbtype) != 0) (IP2Location.IDDCODE_POSITION(dbtype) - 2) << 2
      else 0
      AREACODE_POSITION_OFFSET = if (IP2Location.AREACODE_POSITION(dbtype) != 0) (IP2Location.AREACODE_POSITION(dbtype) - 2) << 2
      else 0
      WEATHERSTATIONCODE_POSITION_OFFSET = if (IP2Location.WEATHERSTATIONCODE_POSITION(dbtype) != 0) (IP2Location.WEATHERSTATIONCODE_POSITION(dbtype) - 2) << 2
      else 0
      WEATHERSTATIONNAME_POSITION_OFFSET = if (IP2Location.WEATHERSTATIONNAME_POSITION(dbtype) != 0) (IP2Location.WEATHERSTATIONNAME_POSITION(dbtype) - 2) << 2
      else 0
      MCC_POSITION_OFFSET = if (IP2Location.MCC_POSITION(dbtype) != 0) (IP2Location.MCC_POSITION(dbtype) - 2) << 2
      else 0
      MNC_POSITION_OFFSET = if (IP2Location.MNC_POSITION(dbtype) != 0) (IP2Location.MNC_POSITION(dbtype) - 2) << 2
      else 0
      MOBILEBRAND_POSITION_OFFSET = if (IP2Location.MOBILEBRAND_POSITION(dbtype) != 0) (IP2Location.MOBILEBRAND_POSITION(dbtype) - 2) << 2
      else 0
      ELEVATION_POSITION_OFFSET = if (IP2Location.ELEVATION_POSITION(dbtype) != 0) (IP2Location.ELEVATION_POSITION(dbtype) - 2) << 2
      else 0
      USAGETYPE_POSITION_OFFSET = if (IP2Location.USAGETYPE_POSITION(dbtype) != 0) (IP2Location.USAGETYPE_POSITION(dbtype) - 2) << 2
      else 0
      COUNTRY_ENABLED = IP2Location.COUNTRY_POSITION(dbtype) != 0
      REGION_ENABLED = IP2Location.REGION_POSITION(dbtype) != 0
      CITY_ENABLED = IP2Location.CITY_POSITION(dbtype) != 0
      ISP_ENABLED = IP2Location.ISP_POSITION(dbtype) != 0
      LATITUDE_ENABLED = IP2Location.LATITUDE_POSITION(dbtype) != 0
      LONGITUDE_ENABLED = IP2Location.LONGITUDE_POSITION(dbtype) != 0
      DOMAIN_ENABLED = IP2Location.DOMAIN_POSITION(dbtype) != 0
      ZIPCODE_ENABLED = IP2Location.ZIPCODE_POSITION(dbtype) != 0
      TIMEZONE_ENABLED = IP2Location.TIMEZONE_POSITION(dbtype) != 0
      NETSPEED_ENABLED = IP2Location.NETSPEED_POSITION(dbtype) != 0
      IDDCODE_ENABLED = IP2Location.IDDCODE_POSITION(dbtype) != 0
      AREACODE_ENABLED = IP2Location.AREACODE_POSITION(dbtype) != 0
      WEATHERSTATIONCODE_ENABLED = IP2Location.WEATHERSTATIONCODE_POSITION(dbtype) != 0
      WEATHERSTATIONNAME_ENABLED = IP2Location.WEATHERSTATIONNAME_POSITION(dbtype) != 0
      MCC_ENABLED = IP2Location.MCC_POSITION(dbtype) != 0
      MNC_ENABLED = IP2Location.MNC_POSITION(dbtype) != 0
      MOBILEBRAND_ENABLED = IP2Location.MOBILEBRAND_POSITION(dbtype) != 0
      ELEVATION_ENABLED = IP2Location.ELEVATION_POSITION(dbtype) != 0
      USAGETYPE_ENABLED = IP2Location.USAGETYPE_POSITION(dbtype) != 0
      if (_MetaData.Indexed) {
        val _IndexBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, _MetaData.IndexBaseAddr - 1, _MetaData.BaseAddr - _MetaData.IndexBaseAddr) // reading indexes
        _IndexBuffer.order(ByteOrder.LITTLE_ENDIAN)
        var pointer = 0
        // read IPv4 index
        for (x <- _IndexArrayIPv4.indices) {
          _IndexArrayIPv4(x)(0) = _IndexBuffer.getInt(pointer) // 4 bytes for from row
          _IndexArrayIPv4(x)(1) = _IndexBuffer.getInt(pointer + 4) // 4 bytes for to row
          pointer += 8
        }
        if (_MetaData.IndexedIPv6) { // read IPv6 index
          for (x <- _IndexArrayIPv6.indices) {
            _IndexArrayIPv6(x)(0) = _IndexBuffer.getInt(pointer)
            _IndexArrayIPv6(x)(1) = _IndexBuffer.getInt(pointer + 4)
            pointer += 8
          }
        }
      }
      if (UseMemoryMappedFile) CreateMappedBytes(inChannel)
      else DestroyMappedBytes()
      loadOK = true
    }
    finally if (aFile != null) aFile.close()
    loadOK
  }

  /**
   * This function to query IP2Location data.
   *
   * @param IPAddress IP Address you wish to query
   * @throws IOException If an input or output exception occurred
   * @return IP2Location data
   */
  @throws[IOException]
  def IPQuery(IPAddress: String): IPResult = {
    val IP = IPAddress.trim
    val record = new IPResult(IP)
    var filehandle: RandomAccessFile = null
    var mybuffer: ByteBuffer = null
    var mydatabuffer: ByteBuffer = null
    try {
      if (IP == null || IP.isEmpty) {
        record.status = "EMPTY_IP_ADDRESS"
        return record
      }
      var ipno: BigInteger = null
      var indexaddr = 0
      var actualiptype = 0
      var myiptype = 0
      var mybaseaddr = 0
      var mycolumnsize = 0
      var mybufcapacity = 0
      var MAX_IP_RANGE = BigInteger.ZERO
      var rowoffset: Long = 0
      var rowoffset2: Long = 0
      var bi: Array[BigInteger] = null
      var overcapacity = false
      var retarr: Array[String] = null
      var breakloop = false
      try {
        bi = ip2no(IP)
        myiptype = bi(0).intValue
        ipno = bi(1)
        actualiptype = bi(2).intValue
        if (actualiptype == 6) { // means didn't match IPv4 regex
          retarr = ExpandIPv6(IP, myiptype)
          record.ip_address = retarr(0) // return after expand IPv6 format
          myiptype = retarr(1).toInt // special cases
        }
      } catch {
        case _: UnknownHostException =>
          record.status = "INVALID_IP_ADDRESS"
          return record
      }
      var low: Long = 0
      var high: Long = 0
      var mid: Long = 0
      var position: Long = 0
      var ipfrom = BigInteger.ZERO
      var ipto = BigInteger.ZERO
      // Read BIN if haven't done so
      if (_MetaData == null) if (!LoadBIN) { // problems reading BIN
        record.status = "MISSING_FILE"
        return record
      }
      if (UseMemoryMappedFile) {
        if ((_IPv4Buffer == null) || (!_MetaData.OldBIN && _IPv6Buffer == null) || (_MapDataBuffer == null)) {
          CreateMappedBytes()
        }
      }
      else {
        DestroyMappedBytes()
        filehandle = new RandomAccessFile(IPDatabasePath, "r")
        if (filehandle == null) {
          record.status = "MISSING_FILE"
          return record
        }
      }

      if (myiptype == 4) {
        MAX_IP_RANGE = IP2Location.MAX_IPV4_RANGE
        high = _MetaData.DBCount
        if (UseMemoryMappedFile) {
          mybuffer = _IPv4Buffer.duplicate // this enables this thread to maintain its own position in a multi-threaded environment
          mybuffer.order(ByteOrder.LITTLE_ENDIAN)
          mybufcapacity = mybuffer.capacity
        }
        else mybaseaddr = _MetaData.BaseAddr
        mycolumnsize = _IPv4ColumnSize
        if (_MetaData.Indexed) {
          indexaddr = ipno.shiftRight(16).intValue //new style for array
          low = _IndexArrayIPv4(indexaddr)(0)
          high = _IndexArrayIPv4(indexaddr)(1)
        }
      }
      else { // IPv6
        if (_MetaData.OldBIN) {
          record.status = "IPV6_NOT_SUPPORTED"
          return record
        }
        MAX_IP_RANGE = IP2Location.MAX_IPV6_RANGE
        high = _MetaData.DBCountIPv6
        if (UseMemoryMappedFile) {
          mybuffer = _IPv6Buffer.duplicate
          mybuffer.order(ByteOrder.LITTLE_ENDIAN)
          mybufcapacity = mybuffer.capacity
        }
        else mybaseaddr = _MetaData.BaseAddrIPv6
        mycolumnsize = _IPv6ColumnSize
        if (_MetaData.IndexedIPv6) {
          indexaddr = ipno.shiftRight(112).intValue
          low = _IndexArrayIPv6(indexaddr)(0)
          high = _IndexArrayIPv6(indexaddr)(1)
        }
      }
      if (ipno.compareTo(MAX_IP_RANGE) == 0) ipno = ipno.subtract(BigInteger.ONE)
      while ( {
        !breakloop && (low <= high)
      }) {
        mid = (low + high) / 2
        rowoffset = mybaseaddr + (mid * mycolumnsize)
        rowoffset2 = rowoffset + mycolumnsize
        if (UseMemoryMappedFile) overcapacity = rowoffset2 >= mybufcapacity
        ipfrom = read32or128(rowoffset, myiptype, mybuffer, filehandle)
        ipto = if (overcapacity) BigInteger.ZERO
        else read32or128(rowoffset2, myiptype, mybuffer, filehandle)
        if (ipno.compareTo(ipfrom) >= 0 && ipno.compareTo(ipto) < 0) {
          var firstcol = 4 // IP From is 4 bytes
          if (myiptype == 6) {
            firstcol = 16 // IPv6 is 16 bytes
          }
          // read the row here after the IP From column (remaining columns are all 4 bytes)
          val rowlen = mycolumnsize - firstcol
          var row: Array[Byte] = null
          row = readrow(rowoffset + firstcol, rowlen, mybuffer, filehandle)
          if (UseMemoryMappedFile) {
            mydatabuffer = _MapDataBuffer.duplicate // this is to enable reading of a range of bytes in multi-threaded environment
            mydatabuffer.order(ByteOrder.LITTLE_ENDIAN)
          }
          if (COUNTRY_ENABLED) {
            position = read32_row(row, COUNTRY_POSITION_OFFSET).longValue
            record.country_short = readStr(position, mydatabuffer, filehandle)
            position += 3
            record.country_long = readStr(position, mydatabuffer, filehandle)
          }
          else {
            record.country_short = IPResult.NOT_SUPPORTED
            record.country_long = IPResult.NOT_SUPPORTED
          }
          if (REGION_ENABLED) {
            position = read32_row(row, REGION_POSITION_OFFSET).longValue
            record.region = readStr(position, mydatabuffer, filehandle)
          }
          else record.region = IPResult.NOT_SUPPORTED
          if (CITY_ENABLED) {
            position = read32_row(row, CITY_POSITION_OFFSET).longValue
            record.city = readStr(position, mydatabuffer, filehandle)
          }
          else record.city = IPResult.NOT_SUPPORTED
          if (ISP_ENABLED) {
            position = read32_row(row, ISP_POSITION_OFFSET).longValue
            record.isp = readStr(position, mydatabuffer, filehandle)
          }
          else record.isp = IPResult.NOT_SUPPORTED
          if (LATITUDE_ENABLED) {
            // new requirement to "round" to 6 decimals
            record.latitude = setDecimalPlaces(readFloat_row(row, LATITUDE_POSITION_OFFSET)).toFloat
          }
          else record.latitude = 0
          if (LONGITUDE_ENABLED) {
            record.longitude = setDecimalPlaces(readFloat_row(row, LONGITUDE_POSITION_OFFSET)).toFloat
          }
          else record.longitude = 0
          if (DOMAIN_ENABLED) {
            position = read32_row(row, DOMAIN_POSITION_OFFSET).longValue
            record.domain = readStr(position, mydatabuffer, filehandle)
          }
          else record.domain = IPResult.NOT_SUPPORTED
          if (ZIPCODE_ENABLED) {
            position = read32_row(row, ZIPCODE_POSITION_OFFSET).longValue
            record.zipcode = readStr(position, mydatabuffer, filehandle)
          }
          else record.zipcode = IPResult.NOT_SUPPORTED
          if (TIMEZONE_ENABLED) {
            position = read32_row(row, TIMEZONE_POSITION_OFFSET).longValue
            record.timezone = readStr(position, mydatabuffer, filehandle)
          }
          else record.timezone = IPResult.NOT_SUPPORTED
          if (NETSPEED_ENABLED) {
            position = read32_row(row, NETSPEED_POSITION_OFFSET).longValue
            record.netspeed = readStr(position, mydatabuffer, filehandle)
          }
          else record.netspeed = IPResult.NOT_SUPPORTED
          if (IDDCODE_ENABLED) {
            position = read32_row(row, IDDCODE_POSITION_OFFSET).longValue
            record.iddcode = readStr(position, mydatabuffer, filehandle)
          }
          else record.iddcode = IPResult.NOT_SUPPORTED
          if (AREACODE_ENABLED) {
            position = read32_row(row, AREACODE_POSITION_OFFSET).longValue
            record.areacode = readStr(position, mydatabuffer, filehandle)
          }
          else record.areacode = IPResult.NOT_SUPPORTED
          if (WEATHERSTATIONCODE_ENABLED) {
            position = read32_row(row, WEATHERSTATIONCODE_POSITION_OFFSET).longValue
            record.weatherstationcode = readStr(position, mydatabuffer, filehandle)
          }
          else record.weatherstationcode = IPResult.NOT_SUPPORTED
          if (WEATHERSTATIONNAME_ENABLED) {
            position = read32_row(row, WEATHERSTATIONNAME_POSITION_OFFSET).longValue
            record.weatherstationname = readStr(position, mydatabuffer, filehandle)
          }
          else record.weatherstationname = IPResult.NOT_SUPPORTED
          if (MCC_ENABLED) {
            position = read32_row(row, MCC_POSITION_OFFSET).longValue
            record.mcc = readStr(position, mydatabuffer, filehandle)
          }
          else record.mcc = IPResult.NOT_SUPPORTED
          if (MNC_ENABLED) {
            position = read32_row(row, MNC_POSITION_OFFSET).longValue
            record.mnc = readStr(position, mydatabuffer, filehandle)
          }
          else record.mnc = IPResult.NOT_SUPPORTED
          if (MOBILEBRAND_ENABLED) {
            position = read32_row(row, MOBILEBRAND_POSITION_OFFSET).longValue
            record.mobilebrand = readStr(position, mydatabuffer, filehandle)
          }
          else record.mobilebrand = IPResult.NOT_SUPPORTED
          if (ELEVATION_ENABLED) {
            position = read32_row(row, ELEVATION_POSITION_OFFSET).longValue
            record.elevation = convertFloat(readStr(position, mydatabuffer, filehandle)) // due to value being stored as a string but output as float
          }
          else record.elevation = 0
          if (USAGETYPE_ENABLED) {
            position = read32_row(row, USAGETYPE_POSITION_OFFSET).longValue
            record.usagetype = readStr(position, mydatabuffer, filehandle)
          }
          else record.usagetype = IPResult.NOT_SUPPORTED
          record.status = "OK"
          breakloop = true
        }
        else if (ipno.compareTo(ipfrom) < 0) high = mid - 1
        else low = mid + 1
      }
      record
    } finally if (filehandle != null) filehandle.close()
  }

  private def ExpandIPv6(myIP: String, myiptype: Int): Array[String] = {
    val tmp = "0000:0000:0000:0000:0000:"
    val padme = "0000"
    val hexoffset = 0xFF
    var myIP2 = myIP.toUpperCase
    var rettype = String.valueOf(myiptype)
    // expand ipv4-mapped ipv6
    if (myiptype == 4) if (IP2Location.pattern4.matcher(myIP2).matches) myIP2 = myIP2.replaceAll("::", tmp)
    else {
      val mat = IP2Location.pattern5.matcher(myIP2)
      if (mat.matches) {
        val mymatch = mat.group(1)
        val myarr = mymatch.replaceAll("^:+", "").replaceAll(":+$", "").split(":")
        val len = myarr.length
        val bf = new StringBuffer(32)
        for (x <- 0 until len) {
          val unpadded = myarr(x)
          bf.append(padme.substring(unpadded.length) + unpadded) // safe padding for JDK 1.4
        }
        var mylong = new BigInteger(bf.toString, 16).longValue
        val b: Array[Long] = Array(0, 0, 0, 0) // using long in place of bytes due to 2's complement signed issue
        for (x <- 0 until 4) {
          b(x) = mylong & hexoffset
          mylong = mylong >> 8
        }
        myIP2 = myIP2.replaceAll(mymatch + "$", ":" + b(3) + "." + b(2) + "." + b(1) + "." + b(0))
        myIP2 = myIP2.replaceAll("::", tmp)
      }
    }
    else if (myiptype == 6) if (myIP2 == "::") {
      myIP2 = myIP2 + "0.0.0.0"
      myIP2 = myIP2.replaceAll("::", tmp + "FFFF:")
      rettype = "4"
    }
    else { // same regex as myiptype 4 but different scenario
      val mat = IP2Location.pattern4.matcher(myIP2)
      if (mat.matches) {
        val v6part = mat.group(1)
        val v4part = mat.group(2)
        val v4arr = v4part.split("\\.")
        val v4intarr = new Array[Int](4)
        var len = v4intarr.length
        for (x <- 0 until len) {
          v4intarr(x) = v4arr(x).toInt
        }
        val part1 = (v4intarr(0) << 8) + v4intarr(1)
        val part2 = (v4intarr(2) << 8) + v4intarr(3)
        val part1hex = Integer.toHexString(part1)
        val part2hex = Integer.toHexString(part2)
        val bf = new StringBuffer(v6part.length + 9)
        bf.append(v6part)
        bf.append(padme.substring(part1hex.length))
        bf.append(part1hex)
        bf.append(":")
        bf.append(padme.substring(part2hex.length))
        bf.append(part2hex)
        myIP2 = bf.toString.toUpperCase
        val myarr = myIP2.split("::")
        val leftside = myarr(0).split(":")
        val bf2 = new StringBuffer(40)
        val bf3 = new StringBuffer(40)
        val bf4 = new StringBuffer(40)
        len = leftside.length
        var totalsegments = 0
        for (x <- 0 until len) {
          if (leftside(x).nonEmpty) {
            totalsegments += 1
            bf2.append(padme.substring(leftside(x).length))
            bf2.append(leftside(x))
            bf2.append(":")
          }
        }
        if (myarr.length > 1) {
          val rightside = myarr(1).split(":")
          len = rightside.length
          for (x <- 0 until len) {
            if (rightside(x).nonEmpty) {
              totalsegments += 1
              bf3.append(padme.substring(rightside(x).length))
              bf3.append(rightside(x))
              bf3.append(":")
            }
          }
        }
        val totalsegmentsleft = 8 - totalsegments
        if (totalsegmentsleft == 6) {
          for (_ <- 1 until totalsegmentsleft) {
            bf4.append(padme)
            bf4.append(":")
          }
          bf4.append("FFFF:")
          bf4.append(v4part)
          rettype = "4"
          myIP2 = bf4.toString
        }
        else {
          for (_ <- 0 until totalsegmentsleft) {
            bf4.append(padme)
            bf4.append(":")
          }
          bf2.append(bf4).append(bf3)
          myIP2 = bf2.toString.replaceAll(":$", "")
        }
      }
      else { // expand IPv4-compatible IPv6
        val mat2 = IP2Location.pattern6.matcher(myIP2)
        if (mat2.matches) {
          val mymatch = mat2.group(1)
          val myarr = mymatch.replaceAll("^:+", "").replaceAll(":+$", "").split(":")
          val len = myarr.length
          val bf = new StringBuffer(32)
          for (x <- 0 until len) {
            val unpadded = myarr(x)
            bf.append(padme.substring(unpadded.length) + unpadded)
          }
          var mylong = new BigInteger(bf.toString, 16).longValue
          val b: Array[Long] = Array(0, 0, 0, 0)
          for (x <- 0 until 4) {
            b(x) = mylong & hexoffset
            mylong = mylong >> 8
          }
          myIP2 = myIP2.replaceAll(mymatch + "$", ":" + b(3) + "." + b(2) + "." + b(1) + "." + b(0))
          myIP2 = myIP2.replaceAll("::", tmp + "FFFF:")
          rettype = "4"
        }
        else { // should be normal IPv6 case
          val myarr = myIP2.split("::")
          val leftside = myarr(0).split(":")
          val bf2 = new StringBuffer(40)
          val bf3 = new StringBuffer(40)
          val bf4 = new StringBuffer(40)
          var len = leftside.length
          var totalsegments = 0
          for (x <- 0 until len) {
            if (leftside(x).nonEmpty) {
              totalsegments += 1
              bf2.append(padme.substring(leftside(x).length))
              bf2.append(leftside(x))
              bf2.append(":")
            }
          }
          if (myarr.length > 1) {
            val rightside = myarr(1).split(":")
            len = rightside.length
            for (x <- 0 until len) {
              if (rightside(x).nonEmpty) {
                totalsegments += 1
                bf3.append(padme.substring(rightside(x).length))
                bf3.append(rightside(x))
                bf3.append(":")
              }
            }
          }
          val totalsegmentsleft = 8 - totalsegments
          for (_ <- 0 until totalsegmentsleft) {
            bf4.append(padme)
            bf4.append(":")
          }
          bf2.append(bf4).append(bf3)
          myIP2 = bf2.toString.replaceAll(":$", "")
        }
      }
    }
    val retarr = Array(myIP2, rettype)
    retarr
  }

  private def convertFloat(mystr: String): Float = try mystr.toFloat
  catch {
    case _: NumberFormatException =>
      0
  }

  private def reverse(myarray: Array[Byte]): Unit = {
    if (myarray == null) return
    var i = 0
    var j = myarray.length - 1
    while ( {
      j > i
    }) {
      val tmp = myarray(j)
      myarray(j) = myarray(i)
      myarray(i) = tmp
      j -= 1
      i += 1
    }
  }

  @throws[IOException]
  private def readrow(position: Long, mylen: Long, mybuffer: ByteBuffer, filehandle: RandomAccessFile): Array[Byte] = {
    val row = new Array[Byte](mylen.toInt)
    if (UseMemoryMappedFile) {
      mybuffer.position(position.toInt)
      mybuffer.get(row, 0, mylen.toInt)
    }
    else {
      filehandle.seek(position - 1)
      filehandle.read(row, 0, mylen.toInt)
    }
    row
  }

  @throws[IOException]
  private def read32or128(position: Long, myiptype: Int, mybuffer: ByteBuffer, filehandle: RandomAccessFile): BigInteger = {
    if (myiptype == 4) return read32(position, mybuffer, filehandle)
    else if (myiptype == 6) return read128(position, mybuffer, filehandle) // only IPv6 will run this
    BigInteger.ZERO
  }

  @throws[IOException]
  private def read128(position: Long, mybuffer: ByteBuffer, filehandle: RandomAccessFile): BigInteger = {
    var retval = BigInteger.ZERO
    val bsize = 16
    val buf = new Array[Byte](bsize)
    if (UseMemoryMappedFile) {
      mybuffer.position(position.toInt)
      mybuffer.get(buf, 0, bsize)
    }
    else {
      filehandle.seek(position - 1)
      filehandle.read(buf, 0, bsize)
    }
    reverse(buf)
    retval = new BigInteger(1, buf)
    retval
  }

  @throws[IOException]
  private def read32_row(row: Array[Byte], from: Int): BigInteger = {
    val len = 4 // 4 bytes
    val buf = new Array[Byte](len)
    System.arraycopy(row, from, buf, 0, len)
    reverse(buf)
    new BigInteger(1, buf)
  }

  @throws[IOException]
  private def read32(position: Long, mybuffer: ByteBuffer, filehandle: RandomAccessFile): BigInteger = if (UseMemoryMappedFile) {
    // simulate unsigned int by using long
    BigInteger.valueOf(mybuffer.getInt(position.toInt) & 0xffffffffL) // use absolute offset to be thread-safe
  }
  else {
    val bsize = 4
    filehandle.seek(position - 1)
    val buf = new Array[Byte](bsize)
    filehandle.read(buf, 0, bsize)
    reverse(buf)
    new BigInteger(1, buf)
  }

  @throws[IOException]
  private def readStr(position: Long, mydatabuffer: ByteBuffer, filehandle: RandomAccessFile): String = {
    var size = 0
    var buf: Array[Byte] = null
    var pos = position
    if (UseMemoryMappedFile) {
      pos = pos - _MapDataOffset // position stored in BIN file is for full file, not just the mapped data segment, so need to minus
      size = _MapDataBuffer.get(pos.toInt) // use absolute offset to be thread-safe (keep using the original buffer since is absolute position & just reading 1 byte)
      try {
        buf = new Array[Byte](size)
        mydatabuffer.position(pos.toInt + 1)
        mydatabuffer.get(buf, 0, size)
      } catch {
        case _: NegativeArraySizeException =>
          return null
      }
    }
    else {
      filehandle.seek(pos)
      size = filehandle.read
      try {
        buf = new Array[Byte](size)
        filehandle.read(buf, 0, size)
      } catch {
        case _: NegativeArraySizeException =>
          return null
      }
    }
    val s = new String(buf)
    s
  }

  private def readFloat_row(row: Array[Byte], from: Int): Float = {
    val len = 4
    val buf = new Array[Byte](len)
    System.arraycopy(row, from, buf, 0, len)
    java.lang.Float.intBitsToFloat((buf(3) & 0xff) << 24 | (buf(2) & 0xff) << 16 | (buf(1) & 0xff) << 8 | (buf(0) & 0xff)) // the AND is converting byte to unsigned byte in the form of an int
  }

  private def setDecimalPlaces(myfloat: Float): String = {
    val currentLocale = Locale.getDefault
    val nf = NumberFormat.getNumberInstance(currentLocale)
    val df = nf.asInstanceOf[DecimalFormat]
    df.applyPattern("###.######")
    df.format(myfloat).replace(',', '.')
  }

  @throws[UnknownHostException]
  private def ip2no(ipstring: String): Array[BigInteger] = {
    var a1 = BigInteger.ZERO
    var a2 = BigInteger.ZERO
    var a3 = new BigInteger("4")
    if (IP2Location.pattern.matcher(ipstring).matches) { // should be IPv4
      a1 = new BigInteger("4")
      a2 = new BigInteger(String.valueOf(ipv4no(ipstring)))
    }
    else if (IP2Location.pattern2.matcher(ipstring).matches || IP2Location.pattern3.matcher(ipstring).matches) throw new UnknownHostException
    else {
      a3 = new BigInteger("6")
      val ia = InetAddress.getByName(ipstring)
      val byteArr = ia.getAddress
      var myiptype = "0" // BigInteger needs String in the constructor
      ia match {
        case _: Inet6Address => myiptype = "6"
        case _: Inet4Address => myiptype = "4" // this will run in cases of IPv4-mapped IPv6 addresses
        case _ =>
      }
      a2 = new BigInteger(1, byteArr) // confirmed correct for IPv6
      if (a2.compareTo(IP2Location.FROM_6TO4) >= 0 && a2.compareTo(IP2Location.TO_6TO4) <= 0) { // 6to4 so need to remap to ipv4
        myiptype = "4"
        a2 = a2.shiftRight(80)
        a2 = a2.and(IP2Location.LAST_32BITS)
        a3 = new BigInteger("4")
      }
      else if (a2.compareTo(IP2Location.FROM_TEREDO) >= 0 && a2.compareTo(IP2Location.TO_TEREDO) <= 0) { // Teredo so need to remap to ipv4
        myiptype = "4"
        a2 = a2.not
        a2 = a2.and(IP2Location.LAST_32BITS)
        a3 = new BigInteger("4")
      }
      a1 = new BigInteger(myiptype)
    }
    val bi = Array[BigInteger](a1, a2, a3)
    bi
  }

  private def ipv4no(ipstring: String): Int = {
    val ipAddressInArray = ipstring.split("\\.")
    var result: Long = 0
    var ip: Long = 0
    for (x <- 3 to 0 by -1) {
      ip = ipAddressInArray(3 - x).toLong
      result |= ip << (x << 3)
    }
    result.toInt
  }
}