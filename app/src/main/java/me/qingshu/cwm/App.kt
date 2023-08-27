package me.qingshu.cwm

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import me.qingshu.cwm.fetcher.CoverSourceFactory
import me.qingshu.cwm.fetcher.EmbeddedCoverFetcher
import me.qingshu.cwm.fetcher.RetrieverCoverSource

class App:Application(), ImageLoaderFactory {

    private val coverFetcher = EmbeddedCoverFetcher(
        CoverSourceFactory(
            RetrieverCoverSource(this@App)
        )
    )

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .componentRegistry {
                add(coverFetcher)
            }.build()
}