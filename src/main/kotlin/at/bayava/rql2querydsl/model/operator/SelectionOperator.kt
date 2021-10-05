package at.bayava.rql2querydsl.model.operator

import com.querydsl.core.types.Operator
import com.querydsl.core.types.Ops

class SelectionOperator : RqlOperator {
    override val symbol: Array<out String>
        get() = arrayOf("select")
    override val multiValue: Boolean
        get() = false
    override val qOperator: Operator
        get() = Ops.ALIAS
}