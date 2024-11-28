package im.stars_sea.wakeup.network

import im.stars_sea.wakeup.serializer.SentenceTypeConverter
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.data.SentenceType
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import kotlin.random.Random

interface RandomSentence {
    @GET("/?encode=json") suspend fun getSentence(
        @Query("c", encoded = true) type: SentenceType
    ): Sentence

    companion object {
        private const val BASE_URL = "https://v1.hitokoto.cn"
        private var serviceInstance: RandomSentence? = null

        @JvmStatic
        fun getApi(): RandomSentence {
            if (serviceInstance != null)
                return serviceInstance!!

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClient.Builder()
                    .callTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()
                )
                .addConverterFactory(
                    Json.asConverterFactory(MediaType.get("application/json; charset=UTF8"))
                )
                .addConverterFactory(SentenceTypeConverter)
                .build()

            serviceInstance = retrofit.create(RandomSentence::class.java)
            return serviceInstance!!
        }
    }
}

suspend fun randomSentence(type: SentenceType): Sentence {
    return RandomSentence.getApi().getSentence(type)
}

suspend fun randomSentence(): Sentence = randomSentence(
    SentenceType.entries[Random.nextInt(0, 11)]
)
