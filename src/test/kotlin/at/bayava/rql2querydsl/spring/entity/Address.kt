package at.bayava.rql2querydsl.spring.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Version

@Entity
class Address(
    @Id
    val id: UUID = UUID.randomUUID(),
    val street: String,
    val number: Int,
    val door: Int,
) {
    @OneToMany(orphanRemoval = true)
    val persons: MutableList<Person> = mutableListOf()

    @Version
    private val version: Long? = null
}