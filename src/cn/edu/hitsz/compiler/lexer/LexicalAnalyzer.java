package cn.edu.hitsz.compiler.lexer;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * TODO: 实验一: 实现词法分析
 * <br>
 * 你可能需要参考的框架代码如下:
 *
 * @see Token 词法单元的实现
 * @see TokenKind 词法单元类型的实现
 */
public class LexicalAnalyzer {
    private final SymbolTable symbolTable;

    private List<Character> characters = new ArrayList<>();
    private List<Token> tokens = new ArrayList<>() ;


    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }


    /**
     * 从给予的路径中读取并加载文件内容
     *
     * @param path 路径
     */
    public void loadFile(String path) throws IOException {
        //按字符读取文件
        FileReader fileReader = new FileReader(path);
        int ch;
        ch = fileReader.read();
        while(ch!=-1){
            characters.add((char)ch);
            ch = fileReader.read();
        }
    }

    /**
     * 执行词法分析, 准备好用于返回的 token 列表 <br>
     * 需要维护实验一所需的符号表条目, 而得在语法分析中才能确定的符号表条目的成员可以先设置为 null
     */
    public void run() {
        String newStr = "";
        int i = 0;
        while(i < characters.size()){
            newStr = "";
            //空格、tap、换行符号忽略
            if(characters.get(i) ==' '|characters.get(i) =='\r'|characters.get(i) =='\n'|characters.get(i) =='\t'){
                i++;
            }

            //读取int、return或其他变量名称
            else if(Character.isLetter(characters.get(i))){
                while(Character.isDigit(characters.get(i))|Character.isLetter(characters.get(i))){
                    newStr = newStr + String.valueOf(characters.get(i));
                    i++;
                }
                //int
                if("int".equals(newStr)){tokens.add(Token.simple("int"));}
                //return
                else if("return".equals(newStr)){tokens.add(Token.simple("return"));}
                //and
                else if("and".equals(newStr)){tokens.add(Token.simple("and"));}
                //or
                else if("or".equals(newStr)){tokens.add(Token.simple("or"));}
                //变量名称
                else{
                    tokens.add(Token.normal("id",newStr));
                    symbolTable.add(newStr);
                }
            }

            //读取数字
            else if(Character.isDigit(characters.get(i))){
                while(Character.isDigit(characters.get(i))){
                    newStr = newStr + String.valueOf(characters.get(i));
                    i++;
                }
                tokens.add(Token.normal("IntConst",newStr));
            }

            //读取特殊符号
            else {
                switch (characters.get(i)) {
                    case '*' -> tokens.add(Token.simple("*"));
                    case '%' -> tokens.add(Token.simple("%"));
                    case '=' -> {
                        if(characters.get(i+1)=='='){
                            tokens.add(Token.simple("=="));
                            i++;
                        }
                        else {
                            tokens.add(Token.simple("="));
                        }
                    }
                    case '(' -> tokens.add(Token.simple("("));
                    case ')' -> tokens.add(Token.simple(")"));
                    case ':' -> tokens.add(Token.simple(":"));
                    case '+' -> tokens.add(Token.simple("+"));
                    case '-' -> tokens.add(Token.simple("-"));
                    case '/' -> tokens.add(Token.simple("/"));
                    case ',' -> tokens.add(Token.simple(","));
                    case '?' -> tokens.add(Token.simple("?"));
                    case '&' -> tokens.add(Token.simple("&"));
                    case '^' -> tokens.add(Token.simple("^"));
                    case '!' -> {
                        if(characters.get(i+1)=='='){
                            tokens.add(Token.simple("!="));
                            i++;
                        }
                        else {
                            tokens.add(Token.simple("!"));
                        }
                    }
                    case '>' -> {
                        if(characters.get(i+1)=='='){
                            tokens.add(Token.simple(">="));
                            i++;
                        }
                        else if(characters.get(i+1)=='>'){
                            tokens.add(Token.simple(">>"));
                            i++;
                        }
                        else{
                            tokens.add(Token.simple(">"));
                        }
                    }
                    case '<' -> {
                        if(characters.get(i+1)=='='){
                            tokens.add(Token.simple("<="));
                            i++;
                        }
                        else if(characters.get(i+1)=='<'){
                            tokens.add(Token.simple("<<"));
                            i++;
                        }
                        else {
                            tokens.add(Token.simple("<"));
                        }
                    }
                    case ';' -> tokens.add(Token.simple("Semicolon"));
                    default -> System.out.println("出错啦>.<");
                }
                i++;
            }

        }
        //终止符
        tokens.add(Token.eof());

    }

    /**
     * 获得词法分析的结果, 保证在调用了 run 方法之后调用
     *
     * @return Token 列表
     */
    public Iterable<Token> getTokens() {
        return tokens;
    }

    public void dumpTokens(String path) {
        FileUtils.writeLines(
            path,
            StreamSupport.stream(getTokens().spliterator(), false).map(Token::toString).toList()
        );
    }


}
