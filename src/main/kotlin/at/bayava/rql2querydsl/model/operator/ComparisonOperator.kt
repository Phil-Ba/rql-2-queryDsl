package at.bayava.rql2querydsl.model.operator

import com.querydsl.core.types.Operator
import com.querydsl.core.types.Ops

class ComparisonOperator(
    override val qOperator: Operator,
    override vararg val symbol: String,
    override val multiValue: Boolean = false
) : RqlOperator

enum class DefaultComparisonOperators(val operator: ComparisonOperator) {
    Eq(ComparisonOperator(Ops.EQ, "eq", "=")),
    Ge(ComparisonOperator(Ops.GOE, "ge", "=ge=")),
    GT(ComparisonOperator(Ops.GT, "gt", "=gt=")),
    Le(ComparisonOperator(Ops.LOE, "le", "=le=")),
    Lt(ComparisonOperator(Ops.LT, "lt", "=lt="))
}