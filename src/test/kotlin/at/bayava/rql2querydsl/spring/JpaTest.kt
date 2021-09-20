package at.bayava.rql2querydsl.spring

import at.bayava.rql2querydsl.QueryDslVisitor
import at.bayava.rql2querydsl.spring.entity.Person
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import cz.jirutka.rsql.parser.RSQLParser
import cz.jirutka.rsql.parser.ast.Node
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
//    classes = [JpaTest::class]
)
@SpringBootConfiguration
@EnableAutoConfiguration
//@TestConfiguration
internal class JpaTest @Autowired constructor(val em: EntityManager) {

    @Test
    @Transactional
    internal fun name() {
        em.persist(Person(name = "John", age = 12))
        em.persist(Person(name = "John", age = 14))
        em.persist(Person(name = "John", age = 22))
        em.persist(Person(name = "Hank", age = 12))
        em.persist(Person(name = "Hank", age = 14))

        val rootNode: Node = RSQLParser().parse("name==John;age=lt=20")
        val spec = rootNode.accept(QueryDslVisitor(Person::class.java))
        val result = JPAQueryFactory(em)
            .selectFrom(PathBuilder(Person::class.java,"root"))
            .where(spec)
            .fetch()

        println(result)
    }

}