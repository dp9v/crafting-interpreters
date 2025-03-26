package com.dp9v.loxinterpreter;

public class PostfixPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return expr.left.accept(this) + " " + expr.right.accept(this) + " " + expr.operator.lexeme();
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null)
            return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return expr.right.accept(this) + " " + expr.operator.lexeme();
//        return parenthesize(expr.operator.lexeme(), expr.right);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder result = new StringBuilder();
        result.append("(").append(name);
        for (Expr expr : exprs) {
            result.append(" ").append(expr.accept(this));
        }
        result.append(")");
        return result.toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Grouping(
                        new Expr.Binary(
                                new Expr.Literal(1),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Expr.Literal(2)
                        )
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Binary(
                                new Expr.Literal(10),
                                new Token(TokenType.PLUS, "-", null, 1),
                                new Expr.Literal(20)
                        )));
        System.out.println(new PostfixPrinter().print(expression));
    }
}
