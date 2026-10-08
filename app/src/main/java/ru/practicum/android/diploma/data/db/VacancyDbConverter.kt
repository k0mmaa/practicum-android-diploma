package ru.practicum.android.diploma.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.domain.models.vacancy.Address
import ru.practicum.android.diploma.domain.models.vacancy.Contacts

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
    fun fromAddress(address: Address?): String? = address?.let { gson.toJson(it) }

    @TypeConverter
    fun toAddress(json: String?): Address? = json?.let { gson.fromJson(it, Address::class.java) }

    @TypeConverter
    fun fromContacts(contacts: Contacts?): String? = contacts?.let { gson.toJson(it) }

    @TypeConverter
    fun toContacts(json: String?): Contacts? = json?.let { gson.fromJson(it, Contacts::class.java) }
}
