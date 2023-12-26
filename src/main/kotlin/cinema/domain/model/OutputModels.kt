package cinema.domain.model

sealed class Result

object Success : Result()
class Error(val message: String) : Result()

@JvmInline
value class OutputModel(val value: String)