package org.hugomfcruz;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;


public class BowlingGameSmallMethodsTest extends BowlingGameTests {
    @Before
    public void setUp() {
        bowlingGame = new BowlingGameSmallMethods();
    }

    @Test
    public void allMethodsAreSmallInSource() throws IOException, ParseException {
        File file = new File("src/main/java/org/hugomfcruz/BowlingGameSmallMethods.java");
        CompilationUnit parser = JavaParser.parse(file);
        for (BodyDeclaration member: parser.getTypes().get(0).getMembers()) {
            if(member instanceof MethodDeclaration){
                MethodDeclaration method = (MethodDeclaration) member;
                int statements = calculateStatements(method.getBody());
                System.out.println(method.getName() + " " + statements);
                assertThat(method.getName(), statements, is(lessThanOrEqualTo(4)));
            }
        }
    }

    private int calculateStatements(Statement body) {
        if(body instanceof BlockStmt) {
            int numberOfStatements = 0;
            for (Statement statement : ((BlockStmt)body).getStmts()) {
                if (statement instanceof ForStmt) {
                    numberOfStatements += calculateStatements(((ForStmt) statement).getBody());

                } else if (statement instanceof ForeachStmt) {
                    numberOfStatements += calculateStatements(((ForeachStmt) statement).getBody());

                } else if (statement instanceof IfStmt) {
                    numberOfStatements += calculateStatements(((IfStmt) statement).getThenStmt());
                    numberOfStatements += calculateStatements(((IfStmt) statement).getElseStmt());

                } else if (statement instanceof WhileStmt) {
                    numberOfStatements += calculateStatements(((WhileStmt) statement).getBody());

                } else if (statement instanceof DoStmt) {
                    numberOfStatements += calculateStatements(((DoStmt) statement).getBody());

                } else if (statement instanceof TryStmt) {
                    numberOfStatements += calculateStatements(((TryStmt) statement).getTryBlock());
                    numberOfStatements += calculateStatements(((TryStmt) statement).getFinallyBlock());
                    numberOfStatements += ((TryStmt) statement) .getCatchs()
                                                                .stream()
                                                                .map(s -> calculateStatements(s.getCatchBlock()))
                                                                .mapToInt(Integer::intValue)
                                                                .sum();

                } else if (statement instanceof SwitchStmt) {
                    numberOfStatements += ((SwitchStmt) statement)  .getEntries()
                                                                    .stream()
                                                                    .flatMap(s -> s.getStmts().stream())
                                                                    .map(s -> calculateStatements(s))
                                                                    .mapToInt(Integer::intValue)
                                                                    .sum();
                }

                numberOfStatements += 1;
            }
            return numberOfStatements;
        }
        else return 0;
    }

}
