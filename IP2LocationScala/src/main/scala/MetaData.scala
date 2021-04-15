package com.ip2location

class MetaData private[ip2location]() {
  private var _BaseAddr = 0
  private var _DBCount = 0
  private var _DBColumn = 0
  private var _DBType = 0
  private var _DBDay = 1
  private var _DBMonth = 1
  private var _DBYear = 1
  private var _BaseAddrIPv6 = 0
  private var _DBCountIPv6 = 0
  private var _OldBIN = false
  private var _Indexed = false
  private var _IndexedIPv6 = false
  private var _IndexBaseAddr = 0
  private var _IndexBaseAddrIPv6 = 0

  def BaseAddr: Int = _BaseAddr

  def BaseAddr_=(newValue: Int): Unit = {
    _BaseAddr = newValue
  }

  def DBCount: Int = _DBCount

  def DBCount_=(newValue: Int): Unit = {
    _DBCount = newValue
  }

  def DBColumn: Int = _DBColumn

  def DBColumn_=(newValue: Int): Unit = {
    _DBColumn = newValue
  }

  def DBType: Int = _DBType

  def DBType_=(newValue: Int): Unit = {
    _DBType = newValue
  }

  def DBDay: Int = _DBDay

  def DBDay_=(newValue: Int): Unit = {
    _DBDay = newValue
  }

  def DBMonth: Int = _DBMonth

  def DBMonth_=(newValue: Int): Unit = {
    _DBMonth = newValue
  }

  def DBYear: Int = _DBYear

  def DBYear_=(newValue: Int): Unit = {
    _DBYear = newValue
  }

  def BaseAddrIPv6: Int = _BaseAddrIPv6

  def BaseAddrIPv6_=(newValue: Int): Unit = {
    _BaseAddrIPv6 = newValue
  }

  def DBCountIPv6: Int = _DBCountIPv6

  def DBCountIPv6_=(newValue: Int): Unit = {
    _DBCountIPv6 = newValue
  }

  def OldBIN: Boolean = _OldBIN

  def OldBIN_=(newValue: Boolean): Unit = {
    _OldBIN = newValue
  }

  def Indexed: Boolean = _Indexed

  def Indexed_=(newValue: Boolean): Unit = {
    _Indexed = newValue
  }

  def IndexedIPv6: Boolean = _IndexedIPv6

  def IndexedIPv6_=(newValue: Boolean): Unit = {
    _IndexedIPv6 = newValue
  }

  def IndexBaseAddr: Int = _IndexBaseAddr

  def IndexBaseAddr_=(newValue: Int): Unit = {
    _IndexBaseAddr = newValue
  }

  def IndexBaseAddrIPv6: Int = _IndexBaseAddrIPv6

  def IndexBaseAddrIPv6_=(newValue: Int): Unit = {
    _IndexBaseAddrIPv6 = newValue
  }
}