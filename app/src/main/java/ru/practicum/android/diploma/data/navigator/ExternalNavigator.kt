package ru.practicum.android.diploma.data.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.ExternalNavigatorInterface

class ExternalNavigator(private val content: Context) : ExternalNavigatorInterface {

    override fun shareLink(link: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, link)
        }
        val chooser = Intent.createChooser(sendIntent, content.getString(R.string.chooser_text))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        content.startActivity(chooser)
    }

    override fun openEmail(emailTo: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.fromParts("mailto", emailTo, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        content.startActivity(intent)
    }

    override fun openPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.fromParts("tel", phone, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        content.startActivity(intent)
    }
}
