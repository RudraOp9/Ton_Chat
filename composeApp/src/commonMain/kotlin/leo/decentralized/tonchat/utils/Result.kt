package leo.decentralized.tonchat.utils

data class Result <R>  (
    val success : Boolean,
    val result  : R? = null,
    val error   : Exception? = null
)