package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.DataType
import com.fasterxml.jackson.databind.JavaType

trait CodecProvider {

  def getDT(javaType: JavaType): DataType

}
