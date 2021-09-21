package at.bayava.rql2querydsl

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
            em.persist(Person(name = "John", age = 12))
            em.persist(Person(name = "John", age = 14))
            em.persist(Person(name = "John", age = 8))
            em.persist(Person(name = "John", age = 22))
            em.persist(Person(name = "Hank", age = 12))
            em.persist(Person(name = "Hank", age = 14))

            val spec = RQLParser().parse(it, QueryDslAstVisitor(Person::class.java))
            val result = JPAQueryFactory(em)
                .selectFrom(PathBuilder(Person::class.java, "root"))
                .where(spec)
                .fetch()

            println(result)
            Assertions.assertThat(result)
                .hasSize(2)
        }
    }
}) {
    override fun listeners() = listOf(SpringListener)
}




