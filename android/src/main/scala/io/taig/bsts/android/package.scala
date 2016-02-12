package io.taig.bsts

import io.taig.android.viewvalue

package object android {
    type Extraction[-V, +T] = viewvalue.Extraction[V, T]
    val Extraction = viewvalue.Extraction

    type Injection[-V, -T] = viewvalue.Injection[V, T]
    val Injection = viewvalue.Injection
}