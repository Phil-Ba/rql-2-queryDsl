package at.bayava.rql2querydsl.spring.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
data class Person(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val age: Int
) {
    @Version
    private val version: Long? = null

}