package at.bayava.rql2querydsl

import com.querydsl.core.types.Path
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.Expressions
import net.jazdw.rql.parser.ASTNode
import net.jazdw.rql.parser.SimpleASTVisitor

class QueryDslAstVisitor(private val clazz: Class<*>) : SimpleASTVisitor<Predicate> {
    private val root: Path<Any> = Expressions.path(
        clazz,
        "root"
    )

    override fun visit(node: ASTNode): Predicate {
        println(node)
        TODO()
    }

    enum class Nodes(val nodeName: String) {
        And("and"),
        Or("or"),
        Eq("eq"),
        Neq("neq"),
        Lt("lt"),
        Le("le"),
        Gt("gt"),
        Ge("ge"),
    }

}