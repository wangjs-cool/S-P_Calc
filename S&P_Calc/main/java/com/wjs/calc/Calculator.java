package com.wjs.calc;

/**
 * Created by cool on 2017/8/31.
 */
import java.util.Collections;
import java.util.Stack;

/**
 *  算数表达式求值
 *  直接调用Calculator的类方法conversion()
 *  传入算数表达式，将返回一个浮点值结果
 *  如果计算过程错误，将返回一个NaN
 */

public class Calculator {
    private Stack<String> postfixStack = new Stack<String>();// 后缀式栈
    private Stack<Character> opStack = new Stack<Character>();// 运算符栈
    // 运算符索引的运算符优先级
    private int operatPriority(char s){
        int i;
        switch (s) {
            case ',':i = -1;break;
            case '(':i = 0;break;
            case '+':i = 1;break;
            case '-':i = 1;break;
            case '*':i = 2;break;
            case '/':i = 2;break;
            case '√':i= 3;break;
            case '^':i = 3;break;
            case '%':i = 3;break;
            case 's':i = 4;break;
            case 'c':i = 4;break;
            case 't':i = 4;break;
            case 'S':i = 4;break;
            case 'C':i = 4;break;
            case 'T':i = 4;break;
            case 'g':i = 4;break;
            case 'n':i = 4;break;
            case '°':i= 5;break;
            case '!':i = 5;break;
            case ')':i = 6;break;
            default:i = -2;break;
        }
        return i;
    }

    public static double conversion(String expression) {
        double result = 0;
        Calculator cal = new Calculator();
        try {
            expression = transform_e(expression);
            expression=expression.replace("%","%1");
            expression=expression.replace("×","*");
            expression=expression.replace("÷","/");
            expression=expression.replace("arcsin","1S");
            expression=expression.replace("arccos","1C");
            expression=expression.replace("arctan","1T");
            expression=expression.replace("sin","1s");
            expression=expression.replace("cos","1c");
            expression=expression.replace("tan","1t");
            expression=expression.replace("lg","1g");
            expression=expression.replace("ln","1n");
            expression=expression.replace("π",Math.PI+ "");
            expression=expression.replace("e",Math.E+ "");
            expression=expression.replace("－","-");
            expression=expression.replace("＋","+");
            expression=expression.replace("-√","-2√");
            expression=expression.replace("+√","+2√");
            expression=expression.replace("*√","*2√");
            expression=expression.replace("/√","/2√");
            expression=expression.replace("!","!1");
            expression = transform(expression);
            result = cal.calculate(expression);
        } catch (Exception e) {
            // e.printStackTrace();
            // 运算错误返回NaN
            return 0.0 / 0.0;
        }
        // return new String().valueOf(result);
        return result;
    }

    private static String transform_e(String expression) {

        char[] arr = expression.toCharArray();

        for (int i = 1; i < arr.length; i++) {
            if((arr[i-1]=='0'||arr[i-1]=='1'||arr[i-1]=='2'||arr[i-1]=='3'||arr[i-1]=='4'||arr[i-1]=='5'||arr[i-1]=='6'||arr[i-1]=='7'||arr[i-1]=='8'||arr[i-1]=='9')&&(arr[i]=='π'||arr[i]=='e')){
                return null;
            }
        }
        return new String(arr);
    }
        /**
         * 将表达式中负数的符号更改
         *
         * @param expression
         *            例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
         * @return
         */
    private static String transform(String expression) {

        char[] arr = expression.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == '^'||c == 's'||c == 'c'||c ==  't'||c=='√'||c == 'S'||c == 'C'||c ==  'T'||c == 'g'||c ==  'n'||c ==  '°'||c == '!'||c == '%') {
                        arr[i] = '~';
                    }
                }
            }
        }
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == '(') {
                char c = arr[i - 1];
                if (!(c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == '^'||c == 's'||c == 'c'||c ==  't'||c=='√'||c == 'S'||c == 'C'||c ==  'T'||c == 'g'||c ==  'n'||c ==  '°'||c == '!'||c == '%')) {
                    return null;
                }
            }
        }
        if(arr.length>1) {
            if (arr[0] == '~' && arr[1] == '(') {
                arr[0] = '-';
                return "0" + new String(arr);
            } else if(arr[0]=='√'){
                return "2" + new String(arr);
            }else {
                return new String(arr);
            }
        }
        return new String(arr);
    }

    /**
     * 按照给定的表达式计算
     *
     * @param expression
     *            要计算的表达式例如:5+12*(3+5)/7
     * @return
     */
    public double calculate(String expression) {
        Stack<String> resultStack = new Stack<String>();
        prepare(expression);
        Collections.reverse(postfixStack);// 将后缀式栈反转
        String firstValue, secondValue, currentValue;// 参与计算的第一个值，第二个值和算术运算符
        while (!postfixStack.isEmpty()) {
            currentValue = postfixStack.pop();
            if (!isOperator(currentValue.charAt(0))) {// 如果不是运算符则存入操作数栈中
                currentValue = currentValue.replace("~", "-");
                resultStack.push(currentValue);
            } else {// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
                secondValue = resultStack.pop();
                firstValue = resultStack.pop();

                // 将负数标记符改为负号
                firstValue = firstValue.replace("~", "-");
                secondValue = secondValue.replace("~", "-");

                String tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
                resultStack.push(tempResult);
            }
        }
        return Double.valueOf(resultStack.pop());
    }

    /**
     * 数据准备阶段将表达式转换成为后缀式栈
     *
     * @param expression
     */
    private void prepare(String expression) {
        opStack.push(',');// 运算符放入栈底元素逗号，此符号优先级最低
        char[] arr = expression.toCharArray();
        int currentIndex = 0;// 当前字符的位置
        int count = 0;// 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
        char currentOp, peekOp;// 当前操作符和栈顶操作符
        for (int i = 0; i < arr.length; i++) {
            currentOp = arr[i];
            if (isOperator(currentOp)) {// 如果当前字符是运算符
                if (count > 0) {
                    postfixStack.push(new String(arr, currentIndex, count));// 取两个运算符之间的数字
                }
                peekOp = opStack.peek();
                if (currentOp == ')') {// 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
                    while (opStack.peek() != '(') {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(currentOp);
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            postfixStack.push(new String(arr, currentIndex, count));
        }

        while (opStack.peek() != ',') {
            postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
        }
    }

    /**
     * 判断是否为算术符号
     *
     * @param c
     * @return
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')'|| c == '^' ||c == 's' ||c == 'c' ||c ==  't' ||c=='√'||c == 'S'||c == 'C'||c ==  'T'||c == 'g'||c ==  'n'||c ==  '°'||c == '!'||c == '%';
    }

    /**
     * 利用ASCII码-40做下标去算术符号优先级
     *
     * @param cur
     * @param peek
     * @return
     */
    public boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
        boolean result = false;
        if (operatPriority(peek) >= operatPriority(cur)) {
            result = true;
        }
        return result;
    }

    /**
     * 按照给定的算术运算符做计算
     *
     * @param firstValue
     * @param secondValue
     * @param currentOp
     * @return
     */
    private String calculate(String firstValue, String secondValue, char currentOp) {
        String result = "";
        switch (currentOp) {
            case '+':result = String.valueOf(ArithHelper.add(firstValue, secondValue));break;
            case '-':result = String.valueOf(ArithHelper.sub(firstValue, secondValue));break;
            case '*':result = String.valueOf(ArithHelper.mul(firstValue, secondValue));break;
            case '/':result = String.valueOf(ArithHelper.div(firstValue, secondValue));break;
            case '^':result = String.valueOf(ArithHelper.pow(firstValue, secondValue));break;
            case '%':result = String.valueOf(ArithHelper.percent(firstValue, secondValue));break;
            case 's':result = String.valueOf(ArithHelper.sin(firstValue, secondValue));break;
            case 'c':result = String.valueOf(ArithHelper.cos(firstValue, secondValue));break;
            case 't':result = String.valueOf(ArithHelper.tan(firstValue, secondValue));break;
            case 'S':result = String.valueOf(ArithHelper.asin(firstValue, secondValue));break;
            case 'C':result = String.valueOf(ArithHelper.acos(firstValue, secondValue));break;
            case 'T':result = String.valueOf(ArithHelper.atan(firstValue, secondValue));break;
            case 'g':result = String.valueOf(ArithHelper.lg(firstValue, secondValue));break;
            case 'n':result = String.valueOf(ArithHelper.ln(firstValue, secondValue));break;
            case '√':result = String.valueOf(ArithHelper.root(firstValue, secondValue));break;
            case '!':result = String.valueOf(ArithHelper.fac(firstValue, secondValue));break;
        }
        return result;
    }
}