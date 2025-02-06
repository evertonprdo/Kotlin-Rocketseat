package com.evertonprdo.myfirstappandroid

import android.content.Context
import java.lang.ref.WeakReference

class MyClass(val context: Context) {
    val contextWeakReference = WeakReference<Context>(context)

    fun doSomething() {
        val context = contextWeakReference.get()
    }
}