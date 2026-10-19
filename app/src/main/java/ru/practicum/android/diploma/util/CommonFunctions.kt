package ru.practicum.android.diploma.util

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.practicum.android.diploma.R

fun ImageView.loadLogo(url: String?) {
    val isSvg = url
        ?.let { Uri.parse(it).path }
        ?.endsWith(".svg", ignoreCase = true) == true

    setLayerType(
        if (isSvg) View.LAYER_TYPE_SOFTWARE else View.LAYER_TYPE_NONE,
        null,
    )

    val requestManager = Glide.with(this)

    if (isSvg) {
        requestManager
            .`as`(PictureDrawable::class.java)
            .dontTransform()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .load(url)
            .placeholder(R.drawable.ic_placeholder_32px)
            .error(R.drawable.ic_placeholder_32px)
            .into(this)
    } else {
        requestManager
            .asDrawable()
            .load(url)
            .placeholder(R.drawable.ic_placeholder_32px)
            .error(R.drawable.ic_placeholder_32px)
            .into(this)
    }
}
