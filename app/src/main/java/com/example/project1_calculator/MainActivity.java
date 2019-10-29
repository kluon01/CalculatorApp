// Karshin Luong
// CSCE 4623
// 9/20/19
// Project 1 Calculator

package com.example.project1_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    // Array for number buttons
    int[] numericButtons = {R.id.buttonZero,R.id.buttonOne,R.id.buttonTwo,R.id.buttonThree,R.id.buttonFour,R.id.buttonFive,R.id.buttonSix,R.id.buttonSeven,R.id.buttonEight,R.id.buttonNine};
    // Array for basic operators
    int[] operatorButtons = {R.id.buttonAdd,R.id.buttonSubtract,R.id.buttonMultiply,R.id.buttonDiv};
    // Text views
    TextView resultScreen, previousEq;
    // Boolean values for last button was digit, last button was decimal, and last button was operator
    boolean lastButtonDigit;
    boolean lastButtonDecimal;
    boolean lastButtonOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        this.resultScreen = findViewById(R.id.resultView);
        this.previousEq = findViewById(R.id.previousView);
        resultScreen.setTextColor(Color.WHITE);
        previousEq.setTextColor((Color.GRAY));
        setNumericOnClickListener();
        setOperatorOnClickListener();
    }

    // OnClickListener to handle number button presses
    private void setNumericOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Button button = (Button) v;
                    resultScreen.append(button.getText());
                    lastButtonDigit = true;
                    lastButtonOp = false;
            }
        };

        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    // OnClickListener to handle operator button presses
    private void setOperatorOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limit user to 1 operator per number
                if(lastButtonOp == false && lastButtonDigit == true && resultScreen.getText().length() != 0) {
                    Button button = (Button) v;
                    resultScreen.append(button.getText());
                    lastButtonOp = true;
                    lastButtonDigit = false;
                    lastButtonDecimal = false;
                }
            }
        };

        for(int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }

        // Negative Button
        findViewById(R.id.buttonNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Parse string to find last digit
                for(int i = resultScreen.getText().toString().length()-1; i >= 0; i--) {
                    char currentChar = resultScreen.getText().toString().charAt(i);
                    // Handle logic for placing the negative
                    if(currentChar == '*' || currentChar == '/') {
                        String left = resultScreen.getText().toString().substring(0,i+1);
                        String right = resultScreen.getText().toString().substring(i+1,resultScreen.getText().length());
                        resultScreen.setText(left + "-" + right);
                        break;
                    } else if(currentChar == '+') {
                        String left = resultScreen.getText().toString().substring(0,i);
                        String right = resultScreen.getText().toString().substring(i+1,resultScreen.getText().length());
                        resultScreen.setText(left + "-" + right);
                        break;
                    } else if(currentChar == '-') {
                        String left = resultScreen.getText().toString().substring(0,i);
                        String right = resultScreen.getText().toString().substring(i+1,resultScreen.getText().length());
                        resultScreen.setText(left + "+" + right);
                        break;
                    } else if(i == 0) {
                        resultScreen.setText("-" + resultScreen.getText());
                        break;
                    }
                lastButtonDigit = true;
                lastButtonOp = false;
            }
            }
        });

        // Decimal Point Button
        findViewById(R.id.buttonDecimal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limit the user to 1 decimal per number
                if(lastButtonDecimal == false && (lastButtonDigit == true || lastButtonOp == true)) {
                    resultScreen.append(".");
                    lastButtonDigit = false;
                    lastButtonDecimal = true;
                    lastButtonOp = false;
                }
            }
        });
        // Clear Button
        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultScreen.setText("");
                previousEq.setText("");
                lastButtonDigit = true;
                lastButtonDecimal = false;
            }
        });

        // Delete Button
        findViewById(R.id.buttonDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultScreen.getText().length() != 0) {
                    String deleted = resultScreen.getText().toString().substring(0, resultScreen.getText().length() - 1);
                    resultScreen.setText(deleted);
                    lastButtonDigit = true;
                    lastButtonDecimal = false;
                }
            }
        });

        // Equal Button
        findViewById(R.id.buttonEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if last character is an operator, delete if true
                char lastValue = resultScreen.getText().charAt(resultScreen.getText().length()-1);
                if(lastValue == '+' || lastValue =='-' || lastValue =='/' || lastValue == '*') {
                    String deleted = resultScreen.getText().toString().substring(0, resultScreen.getText().length() - 1);
                    resultScreen.setText(deleted);
                }

                // Calculate expression using exp4j api
                String eq = resultScreen.getText().toString();
                Expression expression = new ExpressionBuilder(eq).build();
                // try-catch for arithmetic errors
                try {
                    double result = expression.evaluate();
                    previousEq.setText(eq);
                    resultScreen.setText(Double.toString(result));
                    lastButtonDecimal = true;
                    lastButtonDigit = true;
                    lastButtonOp = false;

                } catch (ArithmeticException ex) {
                    resultScreen.setText("ERR");
                    lastButtonDigit = false;
                }
            }
        });
    }
}

