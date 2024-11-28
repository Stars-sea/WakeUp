package im.stars_sea.wakeup.serializer

import im.stars_sea.wakeup.data.SentenceType
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


object SentenceTypeConverter : Converter.Factory() {
    private val ClassName = SentenceType::class.java.packageName

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if (type !is Class<*> || type.packageName != ClassName)
            return null
        return Converter<SentenceType, String> { it.code.toString() }
    }
}