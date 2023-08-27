package me.qingshu.cwm.fetcher

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import okio.BufferedSource
import okio.buffer
import okio.source
import java.util.concurrent.TimeUnit

class RetrieverCoverSource(
    private val mContext: Application
) : CoverSource {

    @SuppressLint("Recycle")
    override suspend fun loadCover(uri: Uri): BufferedSource? {
        return mContext.contentResolver.openInputStream(uri)?.let {
            val source = it.source()
            source.timeout().timeout(50, TimeUnit.MILLISECONDS)
            source.buffer()
        }
    }
}