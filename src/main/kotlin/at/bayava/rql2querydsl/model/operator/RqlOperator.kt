package at.bayava.rql2querydsl.model.operator

import com.querydsl.core.types.Operator

interface RqlOperator {
    val symbol: Array<out String>
    val multiValue: Boolean
    val qOperator: Operator
}