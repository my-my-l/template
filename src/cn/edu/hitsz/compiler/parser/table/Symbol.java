package cn.edu.hitsz.compiler.parser.table;

import cn.edu.hitsz.compiler.ir.IRValue;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.symtab.SourceCodeType;

public class Symbol {
    public Token token;
    public NonTerminal nonTerminal;
    public SourceCodeType type;

    private Symbol(Token token, NonTerminal nonTerminal,SourceCodeType type){
        this.token = token;
        this.nonTerminal = nonTerminal;
        this.type = type;
    }

    public Symbol(Token token){
        this(token, null,null);
    }

    public Symbol(NonTerminal nonTerminal){
        this(null, nonTerminal,null);
    }

    public Symbol(SourceCodeType sourceCodeType){
        this(null, null,sourceCodeType);
    }


    public boolean isToken(){
        return this.token != null;
    }

    public boolean isNonterminal(){
        return this.nonTerminal != null;
    }

    public boolean isSourceCodeType(){
        return this.type != null;
    }

    public Token getToken(){
        return token;
    }

    public SourceCodeType getType(){
        return type;
    }

}
