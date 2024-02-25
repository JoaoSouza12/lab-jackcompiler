package br.ufma.ecp;

import br.ufma.ecp.token.Token;
import br.ufma.ecp.token.TokenType;

import static br.ufma.ecp.token.TokenType.NUMBER;
import static br.ufma.ecp.token.TokenType.STRING;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.foreign.MemorySegment.NULL;

public class Parser {
    private br.ufma.ecp.Scanner scan;
    private Token currentToken;
    
    public Parser (byte[] input) {
        scan = new br.ufma.ecp.Scanner(input);
        currentToken = scan.nextToken();
        
    }

    public void parse () {
        expr();
    }

    void expr() {
        number();
        oper();
    }

    void number () {
        System.out.println(currentToken.lexeme);
        match(NUMBER);
    }

    private void nextToken () {
        currentToken = scan.nextToken();
    }

   private void match(TokenType t) {
        if (currentToken.type == t) {
            nextToken();
        }else {
            throw new Error("syntax error");
        }
   }

    void oper () {
        if (currentToken.type == TokenType.PLUS) {
            match(TokenType.PLUS);
            number();
            System.out.println("add");
            oper();
        } else if (currentToken.type == TokenType.MINUS) {
            match(TokenType.MINUS);
            number();
            System.out.println("sub");
            oper();
        } else if (currentToken.type == TokenType.EOF) {
            // vazio
        } else {
            throw new Error("syntax error");
        }
    }
    static public boolean isOperator(String op) {
        return op!= "" && "+-*/<>=~&|".contains(op);
    }

    public String VMOutput() {
        return "";
    }
    void parseTerm() {
        printNonTerminal("term");
        switch (peekToken.type) {
            case NUMBER:
                expectPeek(NUMBER);
                break;
            case STRING:
                expectPeek(STRING);
                break;
            case FALSE:
            case NULL:
            case TRUE:
                expectPeek(TokenType.FALSE, TokenType.NULL, TokenType.TRUE);
                break;
            case THIS:
                expectPeek(TokenType.THIS);
                break;
            case IDENT:
                expectPeek(TokenType.IDENT);
                break;
            default:
                throw error(peekToken, "term expected");
        }

        printNonTerminal("/term");
    }
    void parseExpression() {
        printNonTerminal("expression");
        parseTerm ();
        while (isOperator(peekToken.lexeme)) {
            expectPeek(peekToken.type);
            parseTerm();
        }
        printNonTerminal("/expression");
    }
}
