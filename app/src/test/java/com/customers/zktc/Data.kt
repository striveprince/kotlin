package com.customers.zktc

import kotlinx.serialization.*
import kotlinx.serialization.internal.IntDescriptor
import kotlinx.serialization.internal.LongDescriptor
import kotlinx.serialization.internal.StringDescriptor
import java.text.SimpleDateFormat
import java.util.*

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/23 16:26
 * Email: 1033144294@qq.com
 */
val jsonString = """{"time":"23/10/2019 16:23:12:232","name":"arvin"}"""

@Serializable
data class Data(val name:String="arvin",
                @Serializable(with=DateStringSerializer::class) val time: Date
)

@Serializer(forClass = Date::class)
class DateStringSerializer:KSerializer<Date>{
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS")
    override val descriptor: SerialDescriptor=StringDescriptor.withName("date")
    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeString(dateFormat.format(obj))
    }

    override fun deserialize(decoder: Decoder): Date {
        return dateFormat.parse(decoder.decodeString())
    }

}

