package me.qingshu.cwm.fetcher

import android.net.Uri
import okio.BufferedSource

interface CoverSource {
    suspend fun loadCover(uri: Uri): BufferedSource?
}