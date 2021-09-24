package at.bayava.rql2querydsl

import at.bayava.rql2querydsl.spring.entity.Address
import at.bayava.rql2querydsl.spring.entity.Person
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import io.kotest.core.spec.style.FunSpec
import io.kotest.spring.SpringListener
import net.jazdw.rql.parser.RQLParser
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import javax.persistence.EntityManager
import javax.transaction.Transactional


@Transactional
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
)
@SpringBootConfiguration
@EnableAutoConfiguration
class QueryDslAstVisitorTest @Autowired constructor(val em: EntityManager) : FunSpec({

    listOf(
        "and(name=John,lt(age,20))&gt(age,10)",
        "name=John&lt(age,20)&age=gt=10",
        "and(name=John,lt(age,20),gt(age,10))"
    ).forEach {
        test("testing basic query[$it]") {
            val person1 = Person(
                name = "John",
                age = 12
            )
            em.persist(person1)
            val person2 = Person(
                name = "John",
                age = 14
            )
            em.persist(person2)
            em.persist(
                Person(
                    name = "John",
                    age = 8
                )
            )
            em.persist(
                Person(
                    name = "John",
                    age = 22
                )
            )
            em.persist(
                Person(
                    name = "Hank",
                    age = 12
                )
            )
            em.persist(
                Person(
                    name = "Hank",
                    age = 14
                )
            )

            val spec = RQLParser().parse(
                it,
                QueryDslAstVisitor(Person::class.java)
            )
            val result = JPAQueryFactory(em).selectFrom(
                PathBuilder(
                    Person::class.java,
                    "root"
                )
            )
                .where(spec)
                .fetch()

            println(result)
            Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                    person1,
                    person2
                )
        }
    }

    listOf(
        "name=in=(Heinz,Karl,Frida)",
        "in(name,Heinz,Karl,Frida)",
    ).forEach {
        test("testing 'in' query[$it]") {
            val person1 = Person(
                name = "Heinz",
                age = 12
            )
            val person2 = Person(
                name = "Frida",
                age = 14
            )
            em.persist(person1)
            em.persist(person2)
            em.persist(
                Person(
                    name = "Heinzi",
                    age = 8
                )
            )
            em.persist(
                Person(
                    name = "John",
                    age = 22
                )
            )
            em.persist(
                Person(
                    name = "Hank",
                    age = 12
                )
            )
            em.persist(
                Person(
                    name = "Peter",
                    age = 14
                )
            )

            val spec = RQLParser().parse(
                it,
                QueryDslAstVisitor(Person::class.java)
            )
            val result = JPAQueryFactory(em).selectFrom(
                PathBuilder(
                    Person::class.java,
                    "root"
                )
            )
                .where(spec)
                .fetch()

            println(result)
            Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                    person1,
                    person2
                )
        }
    }

    listOf(
        "address.street=in=(street1,street2)",
        "in(address.street,street1,street2)",
    ).forEach {
        test("testing property path with 'in' query[$it]") {
            val address1 = Address(
                street = "street1",
                number = 1,
                door = 2
            )
            val person1 = Person(
                name = "Heinz",
                age = 12,
                address = address1
            )
            val address2 = Address(
                street = "street2",
                number = 3,
                door = 4
            )
            val person2 = Person(
                name = "Frida",
                age = 14,
                address = address2
            )
            em.persist(person1)
            em.persist(address1)
            em.persist(person2)
            em.persist(address2)
            em.persist(
                Person(
                    name = "Heinzi",
                    age = 8
                )
            )
            em.persist(
                Person(
                    name = "John",
                    age = 22
                )
            )
            em.persist(
                Person(
                    name = "Hank",
                    age = 12
                )
            )
            em.persist(
                Person(
                    name = "Peter",
                    age = 14
                )
            )
            em.flush()

            val spec = RQLParser().parse(
                it,
                QueryDslAstVisitor(Person::class.java)
            )
            val result = JPAQueryFactory(em).selectFrom(
                PathBuilder(
                    Person::class.java,
                    "root"
                )
            )
                .where(spec)
                .fetch()

            println(result)
            Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                    person1,
                    person2
                )
        }
    }
}) {
    override fun listeners() = listOf(SpringListener)
}




