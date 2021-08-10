package com.github.shur.mariaquest.quest

class QuestId private constructor(private val id: String) {

    init {
        if (!id.matches(PATTERN))
            throw IllegalArgumentException("id must match [a-z_0-9]+")
    }

    override fun toString(): String = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuestId

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


    companion object {

        private val PATTERN = "[a-z_0-9]+".toRegex()

        @JvmStatic
        fun of(id: String) = QuestId(id)

    }

}
