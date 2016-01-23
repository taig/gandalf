package io.taig.bsts

trait Validation[REI, REO, RAO] {
    def isSuccess: Boolean

    def isFailure: Boolean = !isSuccess

    def report( implicit r: REI ): REO

    def raw: RAO
}