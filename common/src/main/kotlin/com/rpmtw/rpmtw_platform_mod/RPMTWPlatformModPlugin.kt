@file:JvmName("RPMTWPlatformModPlugin")

package com.rpmtw.rpmtw_platform_mod

import dev.architectury.injectables.annotations.ExpectPlatform

@ExpectPlatform
fun registerConfig() {
    // Just throw an error, the content should get replaced at runtime.
    throw AssertionError()
}
