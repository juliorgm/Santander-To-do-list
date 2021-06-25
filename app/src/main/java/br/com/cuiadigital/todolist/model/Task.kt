package br.com.cuiadigital.todolist.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task (
    var title: String = "",
    var hour: String = "",
    var date: String = "",
    val id:Int = 0
    ) : Parcelable{


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
