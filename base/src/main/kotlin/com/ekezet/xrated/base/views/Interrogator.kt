package com.ekezet.xrated.base.views

import android.content.Intent

/**
 * @author kiri
 */
interface Interrogator {
    fun startActivityForResult(intent: Intent, requestCode: Int)
}
