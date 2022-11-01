package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.parser.table.Status;
import cn.edu.hitsz.compiler.parser.table.Symbol;
import cn.edu.hitsz.compiler.symtab.SourceCodeType;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.symtab.SymbolTableEntry;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

import static cn.edu.hitsz.compiler.symtab.SourceCodeType.Int;

// 实现语义分析

public class SemanticAnalyzer implements ActionObserver {

    private  SymbolTable symbolTable;
    private Deque<Symbol> irStack = new LinkedList<>();
    private String id;

    @Override
    public void whenAccept(Status currentStatus) {
    }

    @Override
    public void whenReduce(Status currentStatus, Production production) {
        switch (production.index()) {
            case 1 -> {}
            case 2 -> {}
            case 3 -> {}
            case 4 -> {
                Token token = irStack.pop().getToken(); //id
                SymbolTableEntry symbolTableEntry = symbolTable.get(token.getText());
                symbolTableEntry.setType(irStack.pop().getType());
            }
            case 5 -> {}
            case 6 -> {}
            case 7 -> {}
            case 8 -> {}
            case 9 -> {}
            case 10 -> {}
            case 11 -> {}
            case 12 -> {}
            case 13 -> {}
            case 14 -> {}
            case 15 -> {}
            default -> {
                System.out.println(" production idex error");
            }
        }
    }

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {
        if (Objects.equals(currentToken.getKindId(), "int")) {
            irStack.addFirst(new Symbol(Int));
        } else {
            irStack.addFirst(new Symbol(currentToken));

        }
    }

    @Override
    public void setSymbolTable(SymbolTable table) {
        symbolTable = table;
    }
}

