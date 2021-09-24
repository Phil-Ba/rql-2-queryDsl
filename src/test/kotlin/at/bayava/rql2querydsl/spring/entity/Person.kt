package at.bayava.rql2querydsl.spring.entity

import java.util.*
import javax.persistence.*

@Entity
data class Person(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val age: Int,
    @ManyToOne
    @JoinColumn(name = "address_id")
    val address: Address? = null
) {

    init {
        address?.let { it.persons += this }
    }

    @Version
    private val version: Long? = null
}