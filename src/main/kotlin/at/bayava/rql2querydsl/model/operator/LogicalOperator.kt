package at.bayava.rql2querydsl.model.operator

import com.querydsl.core.types.Operator
import com.querydsl.core.types.Ops

class LogicalOperator(
    override val qOperator: Operator,
    override vararg val symbol: String,
    override val multiValue: Boolean = false,
) : RqlOperator

enum class DefaultLogicalOperators(val operator: LogicalOperator) {
    And(LogicalOperator(Ops.AND, "and", "&")),
    Or(LogicalOperator(Ops.OR, "or", "|"))
}