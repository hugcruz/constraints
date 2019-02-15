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
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;


public class BowlingGameNoLoopsTest extends BowlingGameTests {
    @Before
    public void setUp() {
        bowlingGame = new BowlingGameNoLoops();
    }

    @Test
    public void noForStatementsAreUsed() throws IOException, ParseException {
        File file = new File("src/main/java/org/hugomfcruz/" + bowlingGame.getClass().getSimpleName()  + ".java");
        CompilationUnit parser = JavaParser.parse(file);
        for (BodyDeclaration member: parser.getTypes().get(0).getMembers()) {
            if(member instanceof MethodDeclaration){
                MethodDeclaration method = (MethodDeclaration) member;

                assertThat(method.getName(), findOperation(method.getBody(), ForStmt.class), is(false));
                assertThat(method.getName(), findOperation(method.getBody(), ForeachStmt.class), is(false));
                assertThat(method.getName(), findOperation(method.getBody(), WhileStmt.class), is(false));
                assertThat(method.getName(), findOperation(method.getBody(), DoStmt.class), is(false));

            }
        }
    }

    private boolean findOperation(Statement body, Class<? extends Statement> operation) {
        if(body instanceof BlockStmt) {
            for (Statement statement : ((BlockStmt)body).getStmts()) {
                if(statement.getClass().equals(operation)){
                    fail("Forbidden operation used in " + System.lineSeparator() + statement);
                }

                if (statement instanceof ForStmt) {
                    findOperation(((ForStmt) statement).getBody(), operation);

                } else if (statement instanceof ForeachStmt) {
                    findOperation(((ForeachStmt) statement).getBody(), operation);

                } else if (statement instanceof IfStmt) {
                    findOperation(((IfStmt) statement).getThenStmt(), operation);
                    findOperation(((IfStmt) statement).getElseStmt(), operation);

                } else if (statement instanceof WhileStmt) {
                    findOperation(((WhileStmt) statement).getBody(), operation);

                } else if (statement instanceof DoStmt) {
                    findOperation(((DoStmt) statement).getBody(), operation);

                } else if (statement instanceof TryStmt) {
                    findOperation(((TryStmt) statement).getTryBlock(), operation);
                    findOperation(((TryStmt) statement).getFinallyBlock(), operation);
                    ((TryStmt) statement)   .getCatchs()
                                            .stream()
                                            .forEach(s -> findOperation(s.getCatchBlock(), operation));

                } else if (statement instanceof SwitchStmt) {
                    ((SwitchStmt) statement).getEntries()
                                            .stream()
                                            .flatMap(s -> s.getStmts().stream())
                                            .forEach(s -> findOperation(s, operation));
                }
            }
            return false;
        }
        else return false;
    }

}
