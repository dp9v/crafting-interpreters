package com.dp9v.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SyntaxTreeGenerator {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", List.of(
                "Binary: Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal: Object value",
                "Unary: Token operator, Expr right"
        ));
    }

    private static void defineAst(
            String outputDir,
            String baseName,
            List<String> types
    ) throws IOException {
        var path = "%s/%s.java".formatted(outputDir, baseName);
        try (var writer = new PrintWriter(path, StandardCharsets.UTF_8)) {
            writer.println("package com.dp9v.loxinterpreter;");
            writer.println();
            writer.println("import java.util.List;");
            writer.println();
            writer.printf("abstract class %s {\n", baseName);
            writer.println("    abstract <R> R accept(Visitor<R> visitor);");
            defineVisitor(writer, baseName, types);
            for (String type : types) {
                String className = type.split(":")[0].trim();
                String fields = type.split(":")[1].trim();
                defineType(writer, baseName, className, fields);
            }
            writer.println("}");
        }
    }

    private static void defineType(
            PrintWriter writer,
            String baseName,
            String className,
            String fieldList
    ) {
        writer.printf("    static class %s extends %s {\n", className, baseName);
        writer.printf("        %s(%s) {\n", className, fieldList);
        var fields = fieldList.split(", ");
        for (String field : fields) {
            var name = field.split(" ")[1];
            writer.printf("            this.%s = %s;\n", name, name);
        }
        writer.println("        }");
        writer.println();
        for (String field : fields) {
            writer.printf("        final %s;\n", field);
        }
        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.printf("            return visitor.visit%s%s(this);\n", className, baseName);
        writer.println("        }");
        writer.println("    }");
        writer.println();
    }

    private static void defineVisitor(
            PrintWriter writer,
            String baseName,
            List<String> types
    ) {
        writer.println("    interface Visitor<R> {");
        for (String type : types) {
            var typeName = type.split(":")[0].trim();
            writer.printf("        R visit%s%s(%s %s);\n", typeName, baseName, typeName, baseName.toLowerCase());
        }
        writer.println("    }");
        writer.println();
    }
}
