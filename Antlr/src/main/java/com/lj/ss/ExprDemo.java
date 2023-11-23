package com.lj.ss;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * author shangsong 2023/10/24
 */
public class ExprDemo {

    public static void main(String[] args) {
        // 构建字符流
        CodePointCharStream charStream = CharStreams.fromString("1+2+3*4");

        // 从字符流分析词法， 解析为token
        ExprLexer lexer = new ExprLexer(charStream);

        // 从token进行分析
        ExprParser parser = new ExprParser(new CommonTokenStream(lexer) );

        // 使用监听器，遍历语法树，根据语法定义，prog为语法树的根节点
        ExprParser.ProgContext prog = parser.prog();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk( new ExprBaseListener(), prog );

        // 使用visitor，生成自定义的对象
        Object accept = prog.accept(new ExprBaseVisitor<>());

        // 打印生成的语法树
        System.out.println( prog.toStringTree(parser));

    }
}
