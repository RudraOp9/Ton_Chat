package leo.decentralized.tonchat.utils

data class Effect <R>  (
    val success : Boolean,
    val result  : R? = null,
    val error   : Exception? = null
)