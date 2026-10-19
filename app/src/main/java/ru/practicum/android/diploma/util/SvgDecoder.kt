package ru.practicum.android.diploma.util

import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException
import java.io.InputStream

class SvgDecoder : ResourceDecoder<InputStream, PictureDrawable> {

    override fun handles(
        source: InputStream,
        options: Options,
    ): Boolean = true

    override fun decode(
        source: InputStream,
        width: Int,
        height: Int,
        options: Options,
    ): Resource<PictureDrawable> {
        try {
            val svg = SVG.getFromInputStream(source)
            val drawable = PictureDrawable(svg.renderToPicture())
            return SimpleResource(drawable)
        } catch (exception: SVGParseException) {
            throw IOException("Не удалось прочитать SVG", exception)
        }
    }
}
