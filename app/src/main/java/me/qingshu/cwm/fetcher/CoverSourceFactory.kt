package me.qingshu.cwm.fetcher

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSource

class CoverSourceFactory(
    private val retrieverCoverSource: RetrieverCoverSource
) : CoverSource {

    override suspend fun loadCover(uri: Uri): BufferedSource? =
        withContext(Dispatchers.IO) {
            return@withContext retrieverCoverSource.loadCover(uri)
        }
}