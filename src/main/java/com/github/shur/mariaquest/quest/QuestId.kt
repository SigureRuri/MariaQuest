package com.github.shur.mariaquest.quest

class QuestId private constructor(private val id: String) {

    init {
        if (!id.matches(PATTERN))
            throw IllegalArgumentException("")
    }

    override fun toString(): String = id

    companion object {

        private val PATTERN = Regex("([0-9]|[a-z]|_)+")

        @JvmStatic
        fun of(id: String) = QuestId(id)

    }

}
