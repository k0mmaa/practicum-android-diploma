package ru.practicum.android.diploma.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.ContactsEntity


class VacancyDbConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSkills(skills: List<String>): String = gson.toJson(skills)

    @TypeConverter
    fun toSkills(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromAddress(address: AddressEntity?): String? = address?.let { gson.toJson(it) }

    @TypeConverter
    fun toAddress(json: String?): AddressEntity? = json?.let { gson.fromJson(it, AddressEntity::class.java) }

    @TypeConverter
    fun fromContacts(contacts: ContactsEntity?): String? = contacts?.let { gson.toJson(it) }

    @TypeConverter
    fun toContacts(json: String?): ContactsEntity? = json?.let { gson.fromJson(it, ContactsEntity::class.java) }
}
