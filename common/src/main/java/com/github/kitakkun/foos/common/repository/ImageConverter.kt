package com.github.kitakkun.foos.common.repository

import android.graphics.Bitmap
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 画像変換を担うクラス
 */
object ImageConverter {

    /**
     * 画像のリサイズ
     * @param bitmap ビットマップオブジェクト
     * @param maxImageSize 最大のピクセル幅
     * @param filter 拡大縮小時に補完を行うかどうか
     */
    fun resize(bitmap: Bitmap, maxImageSize: Int, filter: Boolean = true): Bitmap {
        val ratio = min(
            maxImageSize.toDouble() / bitmap.width,
            maxImageSize.toDouble() / bitmap.height
        )
        val resizedWidth = (ratio * bitmap.width).roundToInt()
        val resizedHeight = (ratio * bitmap.height).roundToInt()

        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, filter)
    }

}
