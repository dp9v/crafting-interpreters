package com.dp9v.loxinterpreter;

public class AstPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null)
            return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme(), expr.right);
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
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Binary(
                                new Expr.Literal(10),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Expr.Literal(20)
                        )));
        System.out.println(new AstPrinter().print(expression));
    }
}
