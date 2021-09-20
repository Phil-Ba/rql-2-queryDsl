package at.bayava.rql2querydsl

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Operator
import com.querydsl.core.types.Ops
import com.querydsl.core.types.Path
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.PathBuilder
import cz.jirutka.rsql.parser.ast.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class QueryDslVisitor(private val clazz: Class<*>) : NoArgRSQLVisitorAdapter<Predicate>() {

    private val root: Path<Any> = Expressions.path(
        clazz,
        "root"
    )

    override fun visit(node: AndNode): Predicate {
        logger.debug(
            "and[{}]",
            node
        )
        return BooleanBuilder()
            .orAllOf(
                *node.children.map { it.accept(this) }
                    .toTypedArray()
            )
    }

    override fun visit(node: OrNode): Predicate {
        logger.debug(
            "or[{}]",
            node
        )
        return BooleanBuilder()
            .andAnyOf(
                *node.children.map { it.accept(this) }
                    .toTypedArray()
            )
    }

    override fun visit(node: ComparisonNode): Predicate {
        logger.debug(
            "compare[{}]",
            node
        )
        val ops: Operator = when (node.operator) {
            RSQLOperators.EQUAL -> Ops.EQ
            RSQLOperators.IN -> Ops.IN
            RSQLOperators.GREATER_THAN -> Ops.GT
            RSQLOperators.LESS_THAN -> Ops.LT
            RSQLOperators.NOT_IN -> Ops.NOT_IN
            RSQLOperators.GREATER_THAN_OR_EQUAL -> Ops.GOE
            RSQLOperators.LESS_THAN_OR_EQUAL -> Ops.LOE
            RSQLOperators.NOT_EQUAL -> Ops.NE
            else -> throw IllegalArgumentException("Unknown operator [${node.operator}]!")
        }
        val path: PathBuilder<Any> = PathBuilder(
            clazz,
            "root"
        ).get(node.selector)
        logger.debug(
            "Path[{}]",
            path
        )

        val field = (clazz::getDeclaredField)(node.selector)

        return Expressions.predicate(
            ops,
            path,
            Expressions.constant(
                convert(
                    node,
                    field.type
                )
            )
        )
    }

    private fun convert(node: ComparisonNode,
                        type: Class<*>
    ): List<*> {
        return when (type) {
            String::class.java -> node.arguments
            Int::class.java -> node.arguments.map { it.toInt() }
            else -> throw IllegalArgumentException("")
        }
    }

}