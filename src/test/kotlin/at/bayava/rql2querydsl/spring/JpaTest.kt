package at.bayava.rql2querydsl.spring

import at.bayava.rql2querydsl.QueryDslAstVisitor
import at.bayava.rql2querydsl.spring.entity.Person
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import net.jazdw.rql.parser.RQLParser
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
)
@SpringBootConfiguration
@EnableAutoConfiguration
internal class JpaTest @Autowired constructor(val em: EntityManager) {

    @Test
    @Transactional
    internal fun name() {
        em.persist(Person(name = "John", age = 12))
        em.persist(Person(name = "John", age = 14))
        em.persist(Person(name = "John", age = 8))
        em.persist(Person(name = "John", age = 22))
        em.persist(Person(name = "Hank", age = 12))
        em.persist(Person(name = "Hank", age = 14))

        val spec = RQLParser()
//            .parse("and(name=John,lt(age,string:20),gt(age,10))", QueryDslAstVisitor(Person::class.java))
            .parse("and(name=John,lt(age,20))&gt(age,10)", QueryDslAstVisitor(Person::class.java))
//            .parse("and(name=John,lt(age,string:20))", QueryDslAstVisitor(Person::class.java))
//            .parse("name=John&lt(age,string:20)&age=lt=30", QueryDslAstVisitor(Person::class.java))
//        val rootNode: Node = RSQLParser().parse("name==John;age=lt=20")
//        val spec = rootNode.accept(QueryDslVisitor(Person::class.java))
        val result = JPAQueryFactory(em)
            .selectFrom(PathBuilder(Person::class.java, "root"))
            .where(spec)
            .fetch()

        println(result)
        Assertions.assertThat(result)
            .hasSize(2)
    }

}