package at.bayava.rql2querydsl

import at.bayava.rql2querydsl.internal.QueryDslAstVisitor
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import net.jazdw.rql.parser.RQLParser
import javax.persistence.EntityManager


private val rqlParser = RQLParser()
private val queryDslAstVisitor = QueryDslAstVisitor()

class RqlExecutor(entityManager: EntityManager) {
    private val queryFactory = JPAQueryFactory(entityManager)

    fun <T> queryByRql(rql: String,
        clazz: Class<T>
    ): List<T> {

        val spec = rqlParser.parse(
            rql,
            queryDslAstVisitor
        )

        return queryFactory.selectFrom(
            PathBuilder(
                clazz,
                "root"
            )
        )
            .where(spec)
            .fetch()
    }


}