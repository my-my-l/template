package cn.edu.hitsz.compiler;
import cn.edu.hitsz.compiler.asm.AssemblyGenerator;
import cn.edu.hitsz.compiler.lexer.LexicalAnalyzer;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.lexer.TokenKind;
import cn.edu.hitsz.compiler.parser.IRGenerator;
import cn.edu.hitsz.compiler.parser.ProductionCollector;
import cn.edu.hitsz.compiler.parser.SemanticAnalyzer;
import cn.edu.hitsz.compiler.parser.SyntaxAnalyzer;
import cn.edu.hitsz.compiler.parser.table.GrammarInfo;
import cn.edu.hitsz.compiler.parser.table.Symbol;
import cn.edu.hitsz.compiler.parser.table.TableLoader;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FilePathConfig;
import cn.edu.hitsz.compiler.utils.FileUtils;
import cn.edu.hitsz.compiler.utils.IREmulator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Test {

    public static void main(String[] args) throws IOException {

      /*  enum ActionKind {Reduce, Shift, Accept, Error}
        System.out.println(ActionKind.Accept);*/
        Deque<Integer> deque = new LinkedList<>();
        List<Integer> characters = new ArrayList<Integer>();
        characters.add(1);
        characters.add(2);
        characters.add(3);
        for (int x:characters)
        {
            deque.addFirst(x);
        }
        for(int a:deque){
            System.out.println(a);
        }
        int test = deque.pop();
        for(int a:deque){
            System.out.println(a);
        }
        deque.getFirst();
        for(int a:deque){
            System.out.println(a);
        }
        System.out.println(test);

    }
}


