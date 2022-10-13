package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.*;
import cn.edu.hitsz.compiler.symtab.SymbolTable;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

//TODO: 实验二: 实现 LR 语法分析驱动程序

/**
 * LR 语法分析驱动程序
 * <br>
 * 该程序接受词法单元串与 LR 分析表 (action 和 goto 表), 按表对词法单元流进行分析, 执行对应动作, 并在执行动作时通知各注册的观察者.
 * <br>
 * 你应当按照被挖空的方法的文档实现对应方法, 你可以随意为该类添加你需要的私有成员对象, 但不应该再为此类添加公有接口, 也不应该改动未被挖空的方法,
 * 除非你已经同助教充分沟通, 并能证明你的修改的合理性, 且令助教确定可能被改动的评测方法. 随意修改该类的其它部分有可能导致自动评测出错而被扣分.
 */
public class SyntaxAnalyzer {
    private final SymbolTable symbolTable;
    private final List<ActionObserver> observers = new ArrayList<>();
    private Deque<Token> tokenStack = new LinkedList<>();
    private Deque<Status> statusStack = new LinkedList<>();
    private Deque<Symbol> symbolStack = new LinkedList<>();


    public SyntaxAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * 注册新的观察者
     *
     * @param observer 观察者
     */
    public void registerObserver(ActionObserver observer) {
        observers.add(observer);
        observer.setSymbolTable(symbolTable);
    }

    /**
     * 在执行 shift 动作时通知各个观察者
     *
     * @param currentStatus 当前状态
     * @param currentToken  当前词法单元
     */
    public void callWhenInShift(Status currentStatus, Token currentToken) {
        for (final var listener : observers) {
            listener.whenShift(currentStatus, currentToken);
        }
    }

    /**
     * 在执行 reduce 动作时通知各个观察者
     *
     * @param currentStatus 当前状态
     * @param production    待规约的产生式
     */
    public void callWhenInReduce(Status currentStatus, Production production) {
        for (final var listener : observers) {
            listener.whenReduce(currentStatus, production);
        }
    }

    /**
     * 在执行 accept 动作时通知各个观察者
     *
     * @param currentStatus 当前状态
     */
    public void callWhenInAccept(Status currentStatus) {
        for (final var listener : observers) {
            listener.whenAccept(currentStatus);
        }
    }

    public void loadTokens(Iterable<Token> tokens) {
        //token先进先出
        for(Token token:tokens){
            tokenStack.offerLast(token);
        }
    }

    public void loadLRTable(LRTable table) {
        //初始化状态栈，把初始状态压入
        statusStack.addFirst(table.getInit());
        //初始化符号栈，把结束符号压入
        symbolStack.addFirst(new Symbol(Token.simple("$")));
    }


    public void run() {
        Action action;
        boolean acc = false;
        while(!acc){
            action = statusStack.getFirst().getAction(tokenStack.getFirst());
            switch (action.getKind()) {
                case Shift : {
                    System.out.println("shift");
                    final var shiftTo = action.getStatus();
                    callWhenInShift(statusStack.getFirst(),tokenStack.getFirst());
                    //压入新状态
                    statusStack.addFirst(shiftTo);
                    //压入新符号
                    symbolStack.addFirst(new Symbol(tokenStack.getFirst()));
                    //弹出token
                    tokenStack.pop();
                    break;
                }

                case Reduce : {
                    System.out.println("reduce");
                    final var production = action.getProduction();
                    callWhenInReduce(statusStack.getFirst(),production);
                    //按space分解产生式
                    String []str = production.toString().split(" ");
                    //计算产生式右部symbol个数
                    int count = str.length-2;
                    //产生式左部
                    Symbol newSymbol = new Symbol(new NonTerminal(str[0]));
                    //弹出符号和状态
                    for(int i=0;i<count;i++){
                        symbolStack.pop();
                        statusStack.pop();
                    }
                    //压入新符号
                    symbolStack.addFirst(newSymbol);
                    //压入新符号对应的新状态
                    statusStack.addFirst(statusStack.getFirst().getGoto(new NonTerminal(str[0])));
                    break;
                }

                case Accept : {
                    System.out.println("accept");
                    callWhenInAccept(statusStack.getFirst());
                    acc = true;
                    break;
                }

                case Error : {
                    System.out.println("error");
                    break;
                }
                default :
                    break;

            }
        }
    }

}

