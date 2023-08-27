package me.qingshu.cwm.fetcher

import android.net.Uri
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.size.Size

class EmbeddedCoverFetcher(
    private val coverSourceFactory: CoverSourceFactory
) : Fetcher<Uri> {
    override suspend fun fetch(
        pool: BitmapPool,
        data: Uri,
        size: Size,
        options: Options
    ): FetchResult {
        val bufferedSource = coverSourceFactory.loadCover(data)
            ?: throw NullPointerException()
        return SourceResult(
            source = bufferedSource,
            mimeType = null,
            dataSource = DataSource.DISK
        )
    }

    override fun key(data: Uri): String {
        return "embedded_cover_${data}"
    }
}