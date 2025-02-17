package com.vozmediano.vozmedianonasa.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream

class Utils {
    companion object {
        fun getVideoThumbnail(videoUrl: String): String? {
            // Check if the URL is a YouTube URL
            val youtubePattern = "https://www.youtube.com/embed/([a-zA-Z0-9_-]+)".toRegex()
            val matchResult = youtubePattern.find(videoUrl)

            if (matchResult != null) {
                val videoId = matchResult.groupValues[1]
                val youtubeThumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
                return youtubeThumbnailUrl
            }

            // If it's not a YouTube URL, proceed with MediaMetadataRetriever (for other types of videos)
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(videoUrl)
                val bitmap: Bitmap? = retriever.getFrameAtTime(0)

                if (bitmap == null) {
                    Log.e("VideoThumbnailError", "Failed to extract frame from video")
                    return null
                }

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                return Base64.encodeToString(byteArray, Base64.NO_WRAP)
            } catch (e: Exception) {
                Log.e("VideoThumbnailError", "Failed to retrieve video frame: ${e.message}")
            } finally {
                retriever.release()
            }
            return null
        }
    }
}