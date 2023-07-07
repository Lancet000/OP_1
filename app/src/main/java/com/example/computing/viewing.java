package com.example.computing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.computing.databinding.ActivityViewingBinding;

import java.util.Locale;
import java.util.Stack;

public class viewing extends AppCompatActivity {
    private ActivityViewingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button0.setOnClickListener(this::onNumberClick);
        binding.button1.setOnClickListener(this::onNumberClick);
        binding.button2.setOnClickListener(this::onNumberClick);
        binding.button3.setOnClickListener(this::onNumberClick);
        binding.button4.setOnClickListener(this::onNumberClick);
        binding.button5.setOnClickListener(this::onNumberClick);
        binding.button6.setOnClickListener(this::onNumberClick);
        binding.button7.setOnClickListener(this::onNumberClick);
        binding.button8.setOnClickListener(this::onNumberClick);
        binding.button9.setOnClickListener(this::onNumberClick);
        binding.buttonJIA.setOnClickListener(this::onOPClick);
        binding.buttonJIAN.setOnClickListener(this::onOPClick);
        binding.buttonCHENG.setOnClickListener(this::onOPClick);
        binding.buttonCHU.setOnClickListener(this::onOPClick);
        binding.buttonY.setOnClickListener(this::onOPClick);
        binding.buttonC.setOnClickListener(this::onCClick);
        binding.buttonD.setOnClickListener(this::onDClick);
        binding.buttonZuo.setOnClickListener(this::onZUOClick);
        binding.buttonYOU.setOnClickListener(this::onYOUClick);
        binding.buttonDIAN.setOnClickListener(this::onDIANClick);
        binding.buttonF.setOnClickListener(this::onFClick);
        binding.buttonDENG.setOnClickListener(this::onDENGClick);
        binding.buttonS.setOnClickListener(this::onSClick);
    }

    @SuppressLint("SetTextI18n")
    private void onSClick(View view) {
        CharSequence viewINText = binding.textViewIN.getText();
        int len = viewINText.length();
        if (!(len != 0 && Character.isDigit(viewINText.charAt(len - 1)))) {
            Toast.makeText(this, "没有数字无法开平方", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = len - 1;
        for (; i >= 0; i--) {
            if (!Character.isDigit(viewINText.charAt(i)) && viewINText.charAt(i) != '.') {
                break;
            }
        }
        CharSequence number = viewINText.subSequence(i + 1, len);
        if (i > 0 && viewINText.charAt(i) == '-' && viewINText.charAt(i - 1) == '(') {
            Toast.makeText(this, "负数不可以开平方", Toast.LENGTH_SHORT).show();
        } else {
            double tmp = Math.sqrt(Double.parseDouble(number.toString()));
            binding.textViewIN.setText(viewINText.subSequence(0, i + 1) + String.format(Locale.getDefault(), "%.6f", tmp));
        }
    }

    private void onDENGClick(View view) {
        CharSequence viewINText = binding.textViewIN.getText();
        if (viewINText.length() == 0) {
            Toast.makeText(this, "空，无法计算结果", Toast.LENGTH_SHORT).show();
            return;
        }
        Stack<Character> op = new Stack<>();
        Stack<Double> num = new Stack<>();
        int index = 0;
        boolean fu = false;
        try {
            for (int i = 0; i < viewINText.length(); i++) {
                char ch = viewINText.charAt(i);
                if (!(Character.isDigit(ch) || ch == '.')) {
                    if (index != i) {
                        double number = Double.parseDouble(viewINText.subSequence(index, i).toString());
                        if (fu) {
                            number = 0 - number;
                            fu = false;
                        }
                        num.push(number);
                    }
                    index = i + 1;

                    if (ch == '-' && viewINText.charAt(i - 1) == '(') {
                        fu = true;
                        continue;
                    }


                    if (op.isEmpty() || inStackPrior(op.peek()) < outStackPrior(ch)) {
                        op.push(ch);
                    } else {
                        while (!op.isEmpty() && inStackPrior(op.peek()) >= outStackPrior(ch)) {
                            if (op.peek() != '(') {
                                num.push(Math.round(calculate(op.pop(), num.pop(), num.pop()) * 1e6) / 1e6);
                            } else {
                                op.pop();
                                break;
                            }
                        }
                        if (ch != ')')
                            op.push(ch);
                    }
                }
            }

            if (index != viewINText.length()) {
                double number = Double.parseDouble(viewINText.subSequence(index, viewINText.length()).toString());
                if (fu) {
                    number = 0 - number;
                }
                num.push(number);
            }

            while (!op.isEmpty()) {
                num.push(calculate(op.pop(), num.pop(), num.pop()));
            }

            if (num.size() != 1)
                throw new ArithmeticException();
            binding.textViewOUT.setText("");
            binding.textViewIN.setText(String.format(Locale.getDefault(), "%.6f", num.pop()));
            binding.textViewOUT.setText(binding.textViewIN.getText());
        } catch (Exception e) {
            Toast.makeText(this, e.toString() + "\n表达式有问题", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculate(char op, double b, double a) {
        Log.d("YKH", "calculate: " + a + " " + op + " " + b);
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '÷':
                if (Double.compare(b, 0.) == 0) throw new ArithmeticException();
                return a / b;
            case '%':
                return a % b;
        }
        throw new ArithmeticException("符号错误");
    }

    private int inStackPrior(char op) {
        switch (op) {
            case '(':
                return 1;
            case '+':
            case '-':
                return 3;
            case '*':
            case '÷':
            case '%':
                return 5;
            case ')':
                return 6;
        }
        return 0;
    }

    private int outStackPrior(char op) {
        switch (op) {
            case '(':
                return 6;
            case '+':
            case '-':
                return 2;
            case '*':
            case '÷':
            case '%':
                return 4;
            case ')':
                return 1;
        }
        return 0;
    }

    @SuppressLint("SetTextI18n")
    private void onFClick(View view) {
        CharSequence viewINText = binding.textViewIN.getText();
        int len = viewINText.length();
        if (!(len != 0 && Character.isDigit(viewINText.charAt(len - 1)))) {
            Toast.makeText(this, "没有数字没办法更改正负", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = len - 1;
        for (; i >= 0; i--) {
            if (!Character.isDigit(viewINText.charAt(i)) && viewINText.charAt(i) != '.') {
                break;
            }
        }
        CharSequence number = viewINText.subSequence(i + 1, len);
        if (i > 0 && viewINText.charAt(i) == '-' && viewINText.charAt(i - 1) == '(') {
            binding.textViewIN.setText(viewINText.subSequence(0, i - 1) + number.toString());
        } else {
            binding.textViewIN.setText(viewINText.subSequence(0, i + 1) + "(-" + number.toString() + ")");
        }
    }

    private void onDIANClick(View view) {
        TextView viewIN = binding.textViewIN;
        int len = viewIN.getText().length();
        if (len == 0) {
            Toast.makeText(this, "不支持以小数点开头，请将小数写为0.3类似的形式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Character.isDigit(viewIN.getText().charAt(len - 1))) {
            binding.textViewIN.append(".");
        } else {
            Toast.makeText(this, "这里不可以加小数点", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void onNumberClick(View view) {
        TextView viewIN = binding.textViewIN;
        int len = viewIN.getText().length();
        if (len != 0 && viewIN.getText().charAt(len - 1) == ')') {
            Toast.makeText(this, "不可以在右括号后面输入数字，不符合表达式规范", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.textViewIN.append(((TextView) view).getText());
    }

    private boolean isOP(char ch) {
        switch (ch) {
            case '+':
            case '-':
            case '*':
            case '÷':
            case '%':
                return true;
        }
        return false;
    }

    private void onZUOClick(View view) {
        CharSequence viewINText = binding.textViewIN.getText();
        int len = viewINText.length();
        if (len == 0 || isOP(viewINText.charAt(len - 1)) || viewINText.charAt(len - 1) == '(') {
            binding.textViewIN.append("(");
            flag++;
        } else {
            Toast.makeText(this, "左括号前面不能是数字/右括号/.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onYOUClick(View view) {
        if (flag == 0) {
            Toast.makeText(this, "右括号输多了，请注意匹配括号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.textViewIN.getText().charAt(binding.textViewIN.getText().length() - 1) == '(') {
            Toast.makeText(this, "不接受输入内容为空的括号", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.textViewIN.append(")");
        flag--;
    }

    private void onOPClick(View view) {
        if (binding.textViewIN.getText().length() == 0) {
            Toast.makeText(this, "算式不能以操作符开头", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isOP(binding.textViewIN.getText().charAt(binding.textViewIN.getText().length() - 1))) {
            Toast.makeText(this, "操作符后不可以跟操作符", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.textViewIN.append(((TextView) view).getText());
    }

    private void onCClick(View view) {
        if (binding.textViewIN.getText().length() == 0) {
            binding.textViewOUT.setText("");
            return;
        }
        binding.textViewIN.setText("");
        flag = 0;
    }

    private int flag = 0;

    private void onDClick(View view) {
        TextView viewIN = binding.textViewIN;
        int len = viewIN.getText().length();
        if (len == 0) {
            Toast.makeText(this, "空，不能删除", Toast.LENGTH_SHORT).show();
            return;
        }
        if (viewIN.getText().charAt(len - 1) == '(') {
            flag--;
        }
        if (viewIN.getText().charAt(len - 1) == ')') {
            flag++;
        }
        viewIN.setText(viewIN.getText().subSequence(0, len - 1));
    }

}

