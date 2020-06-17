package ntut.org.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    TextView mInput,mSign;
    String sign,value1,value2;
    Double num1,num2,result;
    boolean decimalNumber , onShift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInput=(TextView)findViewById(R.id.input);
        mSign=(TextView)findViewById(R.id.sign);
        decimalNumber = onShift = false;
    }


    public void Click_0 (View view) { mInput.setText(mInput.getText()+"0"); }
    public void Click_1 (View view) { mInput.setText(mInput.getText()+"1"); }
    public void Click_2 (View view) { mInput.setText(mInput.getText()+"2"); }
    public void Click_3 (View view) { mInput.setText(mInput.getText()+"3"); }
    public void Click_4 (View view) { mInput.setText(mInput.getText()+"4"); }
    public void Click_5 (View view) { mInput.setText(mInput.getText()+"5"); }
    public void Click_6 (View view) { mInput.setText(mInput.getText()+"6"); }
    public void Click_7 (View view) { mInput.setText(mInput.getText()+"7"); }
    public void Click_8 (View view) { mInput.setText(mInput.getText()+"8"); }
    public void Click_9 (View view) { mInput.setText(mInput.getText()+"9"); }
    public void Click_dot(View view){
        if(!decimalNumber){
            if(mInput.getText().equals("")){
                mInput.setText("0.");
            }else {
                mInput.setText(mInput.getText()+".");
            }
            decimalNumber=true;
        }
    }
    public void Click_add(View view){
        sign = "+";
        value1 = mInput.getText().toString();
        mInput.setText(null);
        mSign.setText("+");
        decimalNumber=false;
    }
    public void Click_sub(View view){
        sign = "-";
        value1 = mInput.getText().toString();
        mInput.setText(null);
        mSign.setText("-");
        decimalNumber=false;
    }
    public void Click_multi(View view){
        sign = "*";
        value1 = mInput.getText().toString();
        mInput.setText(null);
        mSign.setText("x");
        decimalNumber=false;
    }
    public void Click_div(View view){
        sign = "/";
        value1 = mInput.getText().toString();
        mInput.setText(null);
        mSign.setText("/");
        decimalNumber=false;
    }
    public void Click_sin(View view){
        sign = "sin";
        value1 = mInput.getText().toString();
        mInput.setText(null);
        mSign.setText("sin");
    }
    public void Click_cos(View view){
        if(!onShift) {
            sign = "cos";
            value1 = mInput.getText().toString();
            mInput.setText(null);
            mSign.setText("cos");
        }
    }
    public void Click_tan(View view){
        if(!onShift) {
            sign = "tan";
            value1 = mInput.getText().toString();
            mInput.setText(null);
            mSign.setText("tan");
        }
    }
    public void Click_log(View view){
        if(!onShift) {
            sign = "log";
            value1 = mInput.getText().toString();
            mInput.setText(null);
            mSign.setText("log");
        }
    }
    public void Click_ln(View view){
        if(!onShift) {
            sign = "ln";
            value1 = mInput.getText().toString();
            mInput.setText(null);
            mSign.setText("ln");
        }
    }
    public void Click_power(View view){
        if(!onShift) {
            sign = "power";
            value1 = mInput.getText().toString();
            mInput.setText(null);
            mSign.setText("power");
        }
    }
    public void Click_root(View view){
        if(!onShift) {
            sign = "root";
            value1 = mInput.getText().toString();
            mInput.setText(null);
            mSign.setText("root");
        }
    }

    public void Click_Result(View view){
        if (sign == null) {
            mSign.setText(R.string.error);
        } else if (mInput.getText().equals("")) {
            mSign.setText(R.string.error);
        } else if ((sign.equals("+") || sign.equals("-") || sign.equals("*") || sign.equals("/")) && value1.equals("")) {
            mSign.setText(R.string.error);
        } else {
            switch (sign) {
                default:
                    break;
                case "log":
                    value1 =mInput.getText().toString();
                    num1 = Double.parseDouble(value1);
                    mInput.setText(Math.log10(num1) + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "ln":
                    value1 = mInput.getText().toString();
                    num1 = Double.parseDouble(value1);
                    mInput.setText(Math.log(num1) + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "power":
                    num1 = Double.parseDouble((value1));
                    value2 = mInput.getText().toString();
                    num2 = Double.parseDouble(value2);
                    mInput.setText(Math.pow(num1, num2) + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "root":
                    value1 = mInput.getText().toString();
                    num1 = Double.parseDouble((value1));
                    mInput.setText(Math.sqrt(num1) + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "factorial":
                    value1 = mInput.getText().toString();
                    num1 = Double.parseDouble((value1));
                    int i = Integer.parseInt(value1) - 1;

                    while (i > 0) {
                        num1 = num1 * i;
                        i--;
                    }

                    mInput.setText(num1 + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "sin":
                    value1 = mInput.getText().toString();
                    num1 = Double.parseDouble((value1));
                    mInput.setText(Math.sin(num1) + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "cos":
                    value1 = mInput.getText().toString();
                    num1 = Double.parseDouble((value1));
                    mInput.setText(Math.cos(num1) + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "tan":
                    value1 = mInput.getText().toString();
                    num1 = Double.parseDouble((value1));
                    mInput.setText(Math.tan(num1) + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "+":
                    value2 = mInput.getText().toString();
                    num1 = Double.parseDouble(value1);
                    num2 = Double.parseDouble(value2);
                    result = num1 + num2;
                    mInput.setText(result + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "-":
                    value2 = mInput.getText().toString();
                    num1 = Double.parseDouble(value1);
                    num2 = Double.parseDouble(value2);
                    result = num1 - num2;
                    mInput.setText(result + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "*":
                    value2 = mInput.getText().toString();
                    num1 = Double.parseDouble(value1);
                    num2 = Double.parseDouble(value2);
                    result = num1 * num2;
                    mInput.setText(result + "");
                    sign = null;
                    mSign.setText(null);
                    break;
                case "/":
                    value2 = mInput.getText().toString();
                    num1 = Double.parseDouble(value1);
                    num2 = Double.parseDouble(value2);
                    result = num1 / num2;
                    mInput.setText(result + "");
                    sign = null;
                    mSign.setText(null);
                    break;
            }

        }
    }

    public void Click_clear(View view) {
        mInput.setText(null);
        mSign.setText(null);
        value1 = null;
        value2 = null;
        sign = null;
        decimalNumber = false;
    }

    public void SwitchPage(View view) {
        Button buttonRoot = findViewById(R.id.buttonRoot);
        Button buttonSin = findViewById(R.id.buttonSin);
        Button buttonCos = findViewById(R.id.buttonCos);
        Button buttonTan = findViewById(R.id.buttonTan);
        Button buttonLn = findViewById(R.id.buttonLn);
        Button buttonLog = findViewById(R.id.buttonLog);
        Button buttonFraction = findViewById(R.id.buttonFraction);
        Button button27 = findViewById(R.id.button27);
        Button buttonSquare = findViewById(R.id.buttonSquare);
        Button buttonPower = findViewById(R.id.buttonPower);
        Button buttonAbsolute = findViewById(R.id.buttonAbsolute);
        Button buttonPhi = findViewById(R.id.buttonPhi);
        Button buttonFactorial = findViewById(R.id.buttonFactorial);
        if (!onShift) {
            onShift=true;
            buttonRoot.setText("³√");
            buttonSin.setText(R.string.inverseSine);
            buttonCos.setText(R.string.inverseCosine);
            buttonTan.setText(R.string.inverseTangent);
            buttonLn.setText(R.string.hyperbolicSine);
            buttonLog.setText(R.string.hyperbolicCosine);
            buttonFraction.setText(R.string.hyperbolicTangent);
            button27.setText(R.string.inverseHyperSine);
            button27.setTextSize(COMPLEX_UNIT_SP,18);
            buttonSquare.setText(R.string.inverseHyperCosine);
            buttonSquare.setTextSize(COMPLEX_UNIT_SP,18);
            buttonPower.setText(R.string.inverseHyperTangent);
            buttonPower.setTextSize(COMPLEX_UNIT_SP,18);
            buttonAbsolute.setText(R.string.twoPower);
            buttonPhi.setText(R.string.cube);
            buttonFactorial.setText(R.string.factorial);
        } else {
            onShift=false;
            buttonRoot.setText(R.string.squareRoot);
            buttonSin.setText(R.string.sine);
            buttonCos.setText(R.string.cosine);
            buttonTan.setText(R.string.tan);
            buttonLn.setText(R.string.naturalLog);
            buttonLog.setText(R.string.log);
            buttonFraction.setText(R.string.fraction);
            button27.setText(R.string.ePower);
            button27.setTextSize(COMPLEX_UNIT_SP,20);
            buttonSquare.setText(R.string.square);
            buttonSquare.setTextSize(COMPLEX_UNIT_SP,20);
            buttonPower.setText(R.string.power);
            buttonPower.setTextSize(COMPLEX_UNIT_SP,20);
            buttonAbsolute.setText(R.string.absolute);
            buttonPhi.setText(R.string.pi);
            buttonFactorial.setText("e");
        }
    }


}

