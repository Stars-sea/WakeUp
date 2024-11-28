package im.stars_sea.wakeup.data

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sentence(
    @Required @SerialName("id")             val id: Int,
    @Required @SerialName("uuid")           val uuid: String,
    @Required @SerialName("hitokoto")       val hitokoto: String,
    @Required @SerialName("type")           val type: SentenceType,
    @Required @SerialName("from")           val from: String,
    @Required @SerialName("from_who")       val fromWho: String?,
    @Required @SerialName("creator")        val creator: String,
    @Required @SerialName("creator_uid")    val creatorUid: Int,
    @Required @SerialName("reviewer")       val reviewer: Int,
    @Required @SerialName("commit_from")    val commitFrom: String,
    @Required @SerialName("created_at")     val createdAt: String,
    @Required @SerialName("length")         val length: Int
): Parcelable {
    val author: String
        get() {
            return if (fromWho.isNullOrEmpty() || fromWho == from)
                { from } else { "$from - $fromWho" }
        }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        SentenceType.fromInt(parcel.readInt()),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(uuid)
        parcel.writeString(hitokoto)
        parcel.writeInt(type.ordinal)
        parcel.writeString(from)
        parcel.writeString(fromWho)
        parcel.writeString(creator)
        parcel.writeInt(creatorUid)
        parcel.writeInt(reviewer)
        parcel.writeString(commitFrom)
        parcel.writeString(createdAt)
        parcel.writeInt(length)
    }
    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Sentence> {
        override fun createFromParcel(parcel: Parcel): Sentence = Sentence(parcel)
        override fun newArray(size: Int): Array<Sentence?> = arrayOfNulls(size)

        val Empty = Sentence(
            id = -1,
            uuid = "",
            hitokoto = "Loading...",
            type = SentenceType.Other,
            from = "WakeUp App",
            fromWho = "Stars_sea",
            creator = "Stars_sea",
            creatorUid = -1,
            reviewer = -1,
            commitFrom = "app_builtin",
            createdAt = "",
            length = 10
        )
    }
}