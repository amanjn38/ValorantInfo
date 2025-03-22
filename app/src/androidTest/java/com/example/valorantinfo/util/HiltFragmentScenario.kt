package com.example.valorantinfo.util

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.example.valorantinfo.HiltTestActivity

/**
 * Utility class to help launch Hilt fragments in tests.
 */
object HiltFragmentScenario {

    /**
     * Launch a Hilt fragment in a Hilt test activity.
     *
     * @param fragmentArgs Arguments to pass to the fragment
     * @param action Action to perform on the fragment after launch
     */
    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        @StyleRes themeResId: Int = android.R.style.Theme_Material_Light_NoActionBar,
        crossinline action: T.() -> Unit = {}
    ) {
        val activityScenario = launch(HiltTestActivity::class.java)
        activityScenario.onActivity { activity ->
            val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                T::class.java.classLoader!!,
                T::class.java.name
            ) as T
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()
            
            fragment.action()
        }
    }
} 