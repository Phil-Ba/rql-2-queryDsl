package at.bayava.rql2querydsl

import at.bayava.rql2querydsl.internal.QueryDslAstVisitor
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Projections
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

        val pathBuilder = PathBuilder(clazz, "root")
        val paths = mutableListOf<Expression<*>>()
        val spec = rqlParser.parse(
            rql,
            queryDslAstVisitor,
            Pair(clazz, paths)
        )

//        return queryFactory.select(PathBuilder(clazz,"root").path(clazz,"root.*"))
        return queryFactory.select(Projections.fields(clazz, *paths.toTypedArray()))
            .from(
                PathBuilder(
                    clazz,
                    "root"
                )
            )
            .where(spec)
            .fetch()
    }


}