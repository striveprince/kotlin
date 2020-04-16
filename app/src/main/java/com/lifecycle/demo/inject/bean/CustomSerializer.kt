package com.lifecycle.demo.inject.bean

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * @ProjectName:    ExamRetrieve
 * @Package:        com.eword.consult.inject.bean
 * @ClassName:      CustomSerializer
 * @Description:
 * @Author:         A
 * @CreateDate:     2020/4/2 14:36
 * @UpdateUser:     A
 * @UpdateDate:     2020/4/2 14:36
 * @UpdateRemark:
 * @Version:
 */

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date>{
    override val descriptor: SerialDescriptor = PrimitiveDescriptor("java.util.Date", PrimitiveKind.STRING)
    private val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    override fun deserialize(decoder: Decoder): Date {
        return df.parse(decoder.decodeString())!!
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(df.format(value))
    }
}
