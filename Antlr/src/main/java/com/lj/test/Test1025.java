package com.lj.test;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * author shangsong 2023/10/25
 */
public class Test1025 {

    public static void main(String[] args) {

        String query = "3 * (6 - 4) + 5 * 4";

        CalculatorLexer lexer = new CalculatorLexer(new ANTLRInputStream(query));
        CalculatorParser parser = new CalculatorParser(new CommonTokenStream(lexer));
        CalculatorVisitor visitor = new MyCalculatorVisitor();

        System.out.println(visitor.visit(parser.expr()));  // 25.549

    }

}
