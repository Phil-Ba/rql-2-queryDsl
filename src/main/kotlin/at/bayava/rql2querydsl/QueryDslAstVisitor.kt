package at.bayava.rql2querydsl

import at.bayava.rql2querydsl.model.operator.*
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Ops
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.PathBuilder
import mu.KotlinLogging
import net.jazdw.rql.parser.ASTNode
import net.jazdw.rql.parser.SimpleASTVisitor

private val logger = KotlinLogging.logger {}
class QueryDslAstVisitor(private val clazz: Class<*>) : SimpleASTVisitor<Predicate> {

    val operatorsBySymbol: Map<String, RqlOperator> =
        DefaultLogicalOperators.values()
            .map { it.operator }
            .plus(DefaultComparisonOperators.values().map { it.operator })
            .flatMap { it.symbol.map { symbol -> symbol to it } }
            .toMap()

    override fun visit(node: ASTNode): Predicate {
        logger.debug { "Node[${node}]" }
        val operator = operatorsBySymbol[node.name]!!
        val arguments = node.arguments
        logger.debug { "Operator[${operator}]" }
        return when (operator) {
            is LogicalOperator -> {
                if (arguments.any { it is ASTNode == false }) throw IllegalArgumentException("Operator[${operator}] can only contain other operators!")
                val nodeArguments = arguments as List<ASTNode>
                val args = nodeArguments.map { it.accept<Predicate, Void>(this) }
                return when (operator.qOperator) {
                    Ops.AND -> ExpressionUtils.allOf(args)!!
                    Ops.OR -> ExpressionUtils.anyOf(args)!!
                    else -> throw IllegalArgumentException("Logical op[${operator.qOperator}] not implemented!")
                }
            }
            is ComparisonOperator -> {
                if (arguments.any { it is ASTNode }) throw IllegalArgumentException("Operator[${operator}] can't contain other operators!")
                val selector = arguments[0] as String
                val opArguments = arguments.drop(1) as List<Any>
                val path: PathBuilder<Any> = PathBuilder(
                    clazz,
                    "root"
                ).get(selector)

//                val field = (clazz::getDeclaredField)(selector)


                val constant = if (operator.multiValue)
                    Expressions.list(
                        *opArguments.map {
                            Expressions.constant(it)
                        }.toTypedArray()
                    )
                else Expressions.constant(
                    opArguments
//                        convert(
//                            node,
//                            field.type
//                        )
                )
                return Expressions.predicate(
                    operator.qOperator,
                    path,
                    constant
                )
            }
            else -> throw IllegalArgumentException("Operator[${operator::class}] type not implemented!")
        }
    }

//    private fun convert(
//        arguments: List<Any>,
//        type: Class<*>
//    ): List<*> {
//        return when (type) {
//            String::class.java -> arguments
//            Int::class.java -> arguments.map { Integer.it.toInt() }
//            else -> throw IllegalArgumentException("")
//        }
//    }

}