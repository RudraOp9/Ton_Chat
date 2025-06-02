package leo.decentralized.tonchat.domain.usecase

class FormatStringUseCase {
    fun splitStringBySpace(input: String): List<String> = input.split(" ")
}