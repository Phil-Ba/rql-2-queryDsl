package at.bayava.rql2querydsl

import cz.jirutka.rsql.parser.RSQLParser
import cz.jirutka.rsql.parser.ast.Node
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class QueryDslVisitorTest{

    @Test
    internal fun name() {
        val rootNode: Node = RSQLParser().parse("firstName==john;lastName==doe")
        val spec = rootNode.accept(QueryDslVisitor(String::class.java))
        println(spec)
    }
}