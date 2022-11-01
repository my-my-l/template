package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.ir.IRImmediate;
import cn.edu.hitsz.compiler.ir.IRValue;
import cn.edu.hitsz.compiler.ir.IRVariable;
import cn.edu.hitsz.compiler.ir.Instruction;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.parser.table.Status;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.symtab.SymbolTableEntry;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class IRGenerator implements ActionObserver {

    private SymbolTable symbolTable;
    private Deque<IRValue> valueStack = new LinkedList<>();
    private List<Instruction> IrList = new ArrayList<>();

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {

        String type = currentToken.getKind().getIdentifier();
        if (type.equals("IntConst")) {
            int immediate = Integer.parseInt(currentToken.getText());
            valueStack.offerFirst(IRImmediate.of(immediate));
        } else if (type.equals("id")) {
            String name = currentToken.getText();
            valueStack.offerFirst(IRVariable.named(name));
        } else {
            valueStack.offerFirst(IRVariable.named(currentToken.getKindId()));
        }
    }

    @Override
    public void whenReduce(Status currentStatus, Production production) {

        switch (production.index()) {
            case 1 -> {
                for (int i=0; i<production.body().size(); i++) {
                    valueStack.pop();
                }
                valueStack.offerFirst(null);
            }
            case 2 -> {
                for (int i=0; i<production.body().size(); i++) {
                    valueStack.pop();
                }
                valueStack.offerFirst(null);
            }
            case 3 -> {
                for (int i=0; i<production.body().size(); i++) {
                    valueStack.pop();
                }
                valueStack.offerFirst(null);
            }
            case 4 -> {
                for (int i=0; i<production.body().size(); i++) {
                    valueStack.pop();
                }
                valueStack.offerFirst(null);
            }
            case 5 -> {
                for (int i=0; i<production.body().size(); i++) {
                    valueStack.pop();
                }
                valueStack.offerFirst(null);
            }

            case 6 -> {  // S -> id = E
                IRValue E = valueStack.pop();
                valueStack.pop();
                IRValue id = valueStack.pop();
                IrList.add(Instruction.createMov((IRVariable)id, E));
                valueStack.offerFirst(null);
            }
            case 7 -> {  // S -> return E
                IRValue E = valueStack.pop();
                valueStack.pop();
                IrList.add(Instruction.createRet(E));
                valueStack.offerFirst(null);
            }
            case 8 -> {  // E -> E + A
                IRValue A = valueStack.pop();
                valueStack.pop();
                IRValue E = valueStack.pop();
                IRVariable temp = IRVariable.temp();
                IrList.add(Instruction.createAdd(temp, E, A));
                valueStack.offerFirst(temp);
            }
            case 9 -> {  // E -> E - A
                IRValue A = valueStack.pop();
                valueStack.pop();
                IRValue E = valueStack.pop();
                IRVariable temp = IRVariable.temp();
                IrList.add(Instruction.createSub(temp, E, A));
                valueStack.offerFirst(temp);
            }
            case 10 -> {}
            case 11 -> {  // A -> A * B
                IRValue B = valueStack.pop();
                valueStack.pop();
                IRValue A = valueStack.pop();
                IRVariable temp = IRVariable.temp();
                IrList.add(Instruction.createMul(temp, A, B));
                valueStack.offerFirst(temp);
            }
            case 12 -> {}
            case 13 -> {  // B -> ( E )
                valueStack.pop();
                IRValue E = valueStack.pop();
                valueStack.pop();
                valueStack.offerFirst(E);
            }
            case 14 -> {}
            case 15 -> {}
            default -> {
                System.out.println(" production idex error");
            }
        }
    }


    @Override
    public void whenAccept(Status currentStatus) {}

    @Override
    public void setSymbolTable(SymbolTable table) {
        this.symbolTable = table;
    }

    public List<Instruction> getIR() {
        return IrList;
    }

    public void dumpIR(String path) {
        FileUtils.writeLines(path, getIR().stream().map(Instruction::toString).toList());
    }
}

