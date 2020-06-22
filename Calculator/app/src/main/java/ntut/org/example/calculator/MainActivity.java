package ntut.org.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    enum math_func {
        sin, cos, tan,
        inv_sin, inv_cos, inv_tan,
        hyp_sin, hyp_cos, hyp_tan,
        inv_hyp_sin, inv_hyp_cos, inv_hyp_tan,
        percent, ln, log, fraction,
        e_pow, square, cube, pow, abs,
        squareRoot, cubeRoot, twoPow, factorial};

    TextView mInput, mResult;
    String sign, value1, value2, valueFunc;
    boolean decimalNumber, onShift, onFunc, typing1, typing2, isRadiance;
    math_func func;


    static final String
            M_INPUT = "input textView",
            M_RESULT = "result textView",
            SIGN = "operator",
            VALUE_1 = "value 1",
            VALUE_2 = "value 2",
            VALUE_FUNC = "value function",
            IS_DECIMAL = "decimal number",
            ON_SHIFT = "onShift",
            ON_FUNC = "onFunction",
            IS_TYPING1 = "typing1",
            IS_TYPING2 = "typing2",
            IS_RAD = "is radiance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInput = (TextView)findViewById(R.id.input);
        mResult = (TextView)findViewById(R.id.result);

        if (savedInstanceState != null)
        {
            mInput.setText(savedInstanceState.getString(M_INPUT));
            mResult.setText(savedInstanceState.getString(M_RESULT));
            sign = savedInstanceState.getString(SIGN);
            value1 = savedInstanceState.getString(VALUE_1);
            value2 = savedInstanceState.getString(VALUE_2);
            valueFunc = savedInstanceState.getString(VALUE_FUNC);
            decimalNumber = savedInstanceState.getBoolean(IS_DECIMAL);
            onShift = savedInstanceState.getBoolean(ON_SHIFT);
            onFunc = savedInstanceState.getBoolean(ON_FUNC);
            typing1 = savedInstanceState.getBoolean(IS_TYPING1);
            typing2 = savedInstanceState.getBoolean(IS_TYPING2);
            isRadiance = savedInstanceState.getBoolean(IS_RAD);
        }
        else
            initCalculator();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(M_INPUT, mInput.getText().toString());
        savedInstanceState.putString(M_RESULT, mResult.getText().toString());
        savedInstanceState.putString(SIGN, sign);
        savedInstanceState.putString(VALUE_1, value1);
        savedInstanceState.putString(VALUE_2, value2);
        savedInstanceState.putString(VALUE_FUNC, valueFunc);
        savedInstanceState.putBoolean(IS_DECIMAL, onShift);
        savedInstanceState.putBoolean(ON_FUNC, onFunc);
        savedInstanceState.putBoolean(IS_TYPING1, typing1);
        savedInstanceState.putBoolean(IS_TYPING2, typing2);
        savedInstanceState.putBoolean(IS_RAD, isRadiance);
    }

    public void onClickNum(View view)
    {
        String value = view.getTag().toString();

        //Show input to screen
        //If value which user is inputting is empty, add "0." automatically
        if (value.equals(getString(R.string.dot)))
        {
            if (onFunc && valueFunc.equals(""))
                mInput.setText(mInput.getText() + "0.");
            else if (typing1 && value1.equals(""))
                mInput.setText(mInput.getText() + "0.");
            else if (typing2 && value2.equals(""))
                mInput.setText(mInput.getText() + "0.");
        }
        else
            mInput.setText(mInput.getText() + value);

        //Write value to variable
        if (onFunc) valueFunc = InputValue(valueFunc, value);

        else if (typing1) value1 = InputValue(value1, value);

        else if (typing2) value2 = InputValue(value2, value);
    }

    public void onClickOperator(View view)
    {
        String operator = view.getTag().toString();

        //If any operator clicked, any value of typing should be done
        //reset decimal detection
        decimalNumber = false;

        if (onFunc)
        {
            if (typing1)
            {
                typing1 = false;
                mInput.setText(mInput.getText() + ")" + operator);
                sign = operator;
                value1 = GetFunctionValue();
                typing2 = true;
            }
            else if (typing2)
            {
                sign = operator;
                value2 = GetFunctionValue();
                Click_Result(view);
            }
            onFunc = false;
        }
        if (typing1)
        {
            typing1 = false;
            mInput.setText(mInput.getText() + operator);
            sign = operator;
            typing2 = true;
        }
        else if (typing2)
        {
            Click_Result(view);
            sign = operator;
            mInput.setText(value1 + sign);
        }
    }

    public void onClickFunction(View view)
    {
        func = GetMathFunc(view.getTag().toString());

        switch(func) {
            case sin:
                mInput.setText(mInput.getText() + "sin(");
                onFunc = true;
                break;
            case cos:
                mInput.setText(mInput.getText() + "cos(");
                onFunc = true;
                break;
            case tan:
                mInput.setText(mInput.getText() + "tan(");
                onFunc = true;
                break;
            case inv_sin:
                mInput.setText(mInput.getText() + getString(R.string.inverseSine) + "(");
                onFunc = true;
                break;
            case inv_cos:
                mInput.setText(mInput.getText() + getString(R.string.inverseCosine) + "(");
                onFunc = true;
                break;
            case inv_tan:
                mInput.setText(mInput.getText() + getString(R.string.inverseTangent) + "(");
                onFunc = true;
                break;
            case hyp_sin:
                mInput.setText(mInput.getText() + getString(R.string.hyperbolicSine) + "(");
                onFunc = true;
                break;
            case hyp_cos:
                mInput.setText(mInput.getText() + getString(R.string.hyperbolicCosine) + "(");
                onFunc = true;
                break;
            case hyp_tan:
                mInput.setText(mInput.getText() + getString(R.string.hyperbolicTangent) + "(");
                onFunc = true;
                break;
            case inv_hyp_sin:
                mInput.setText(mInput.getText() + getString(R.string.inverseHyperSine) + "(");
                onFunc = true;
                break;
            case inv_hyp_cos:
                mInput.setText(mInput.getText() + getString(R.string.inverseHyperCosine) + "(");
                onFunc = true;
                break;
            case inv_hyp_tan:
                mInput.setText(mInput.getText() + getString(R.string.inverseHyperTangent) + "(");
                onFunc = true;
                break;
            case percent:
                mInput.setText(mInput.getText() + getString(R.string.percent));
                if (typing1){
                    value1 = String.valueOf(Double.parseDouble(value1) / 100);
                    typing1 = false;
                }
                else if (typing2){
                    value2 = String.valueOf(Double.parseDouble(value2) / 100);
                    Click_Result(view);
                }
                break;
            case ln:
                mInput.setText(mInput.getText() + getString(R.string.naturalLog) + "(");
                onFunc = true;
                break;
            case log:
                mInput.setText(mInput.getText() + getString(R.string.log) + "(");
                onFunc = true;
                break;
            case fraction:
                mInput.setText(mInput.getText() + "1/(");
                onFunc = true;
                break;
            case e_pow:
                mInput.setText(mInput.getText() + getString(R.string.ePower) + "(");
                onFunc = true;
                break;
            case square:
                mInput.setText(mInput.getText() + getString(R.string.square));
                if (typing1){
                    value1 = String.valueOf(Math.pow(Double.parseDouble(value1), 2));
                    typing1 = false;
                }
                else if (typing2){
                    value2 = String.valueOf(Math.pow(Double.parseDouble(value2), 2));
                    Click_Result(view);
                }
                break;
            case cube:
                mInput.setText(mInput.getText() + getString(R.string.square));
                if (typing1){
                    value1 = String.valueOf(Math.pow(Double.parseDouble(value1), 3));
                    typing1 = false;
                }
                else if (typing2){
                    value2 = String.valueOf(Math.pow(Double.parseDouble(value2), 3));
                    Click_Result(view);
                }
                break;
            case pow:
                mInput.setText(mInput.getText() + getString(R.string.power) + "(");
                onFunc = true;
                break;
            case abs:
                mInput.setText(mInput.getText() + getString(R.string.absolute) + "(");
                onFunc = true;
                break;
            case squareRoot:
                mInput.setText(mInput.getText() + getString(R.string.squareRoot) + "(");
                onFunc = true;
                break;
            case cubeRoot:
                mInput.setText(mInput.getText() + getString(R.string.cubeRoot) + "(");
                onFunc = true;
                break;
            case twoPow:
                mInput.setText(mInput.getText() + getString(R.string.cubeRoot) + "(");
                onFunc = true;
                break;
            case factorial:
                mInput.setText(mInput.getText() + getString(R.string.factorial));
                if (typing1){
                    value1 = String.valueOf(factorial(Integer.parseInt(value1)));
                    typing1 = false;
                }
                else if (typing2){
                    value2 = String.valueOf(factorial(Integer.parseInt(value2)));
                    Click_Result(view);
                }
                break;
        }
    }

    public void Click_Result(View view){
        double result, num1, num2;

        if (!value1.equals("") && !value2.equals("")) {
            if (sign.equals(getString(R.string.plus))) {
                result = Double.parseDouble(value1) + Double.parseDouble(value2);
                mResult.setText(Double.toString(result));
                mInput.setText(null);
                value1 = Double.toString(result);
                value2 = sign = "";
            } else if (sign.equals(getString(R.string.minus))) {
                result = Double.parseDouble(value1) - Double.parseDouble(value2);
                mResult.setText(Double.toString(result));
                mInput.setText(null);
                value1 = Double.toString(result);
                value2 = sign = "";
            } else if (sign.equals(getString(R.string.multi))) {
                result = Double.parseDouble(value1) * Double.parseDouble(value2);
                mResult.setText(Double.toString(result));
                mInput.setText(null);
                value1 = Double.toString(result);
                value2 = sign = "";
            } else if (sign.equals(getString(R.string.divide))) {
                num1 = Double.parseDouble(value1);
                num2 = Double.parseDouble(value2);
                if (num2 == 0) {
                    mResult.setText(R.string.error);
                    initCalculator();
                } else {
                    result = num1 / num2;
                    mResult.setText(Double.toString(result));
                    mInput.setText(null);
                    value1 = Double.toString(result);
                    value2 = sign = "";
                }
            }
        }
        else {
            mResult.setText(value1);
            mInput.setText(null);
            sign = "";
        }
    }

    public void Click_clear(View view) {
        mInput.setText(null);
        mResult.setText(null);
        initCalculator();
    }

    public void SwitchPage(View view) {
        Button buttonRoot = findViewById(R.id.buttonRoot);
        Button buttonSin = findViewById(R.id.buttonSin);
        Button buttonCos = findViewById(R.id.buttonCos);
        Button buttonTan = findViewById(R.id.buttonTan);
        Button buttonLn = findViewById(R.id.buttonLn);
        Button buttonLog = findViewById(R.id.buttonLog);
        Button buttonFraction = findViewById(R.id.buttonFraction);
        Button buttonEPower = findViewById(R.id.buttonEPower);
        Button buttonSquare = findViewById(R.id.buttonSquare);
        Button buttonPower = findViewById(R.id.buttonPower);
        Button buttonAbsolute = findViewById(R.id.buttonAbsolute);
        Button buttonPhi = findViewById(R.id.buttonPhi);
        Button buttonFactorial = findViewById(R.id.buttonFactorial);
        if (!onShift) {
            onShift=true;
            buttonRoot.setText(getString(R.string.cubeRoot));
            buttonSin.setText(R.string.inverseSine);
            buttonCos.setText(R.string.inverseCosine);
            buttonTan.setText(R.string.inverseTangent);
            buttonLn.setText(R.string.hyperbolicSine);
            buttonLog.setText(R.string.hyperbolicCosine);
            buttonFraction.setText(R.string.hyperbolicTangent);
            buttonEPower.setText(R.string.inverseHyperSine);
            buttonEPower.setTextSize(COMPLEX_UNIT_SP,18);
            buttonSquare.setText(R.string.inverseHyperCosine);
            buttonSquare.setTextSize(COMPLEX_UNIT_SP,18);
            buttonPower.setText(R.string.inverseHyperTangent);
            buttonPower.setTextSize(COMPLEX_UNIT_SP,18);
            buttonAbsolute.setText(R.string.twoPower);
            buttonPhi.setText(R.string.e);
            buttonFactorial.setText(R.string.cube);
        } else {
            onShift=false;
            buttonRoot.setText(R.string.squareRoot);
            buttonSin.setText(R.string.sine);
            buttonCos.setText(R.string.cosine);
            buttonTan.setText(R.string.tan);
            buttonLn.setText(R.string.naturalLog);
            buttonLog.setText(R.string.log);
            buttonFraction.setText(R.string.fraction);
            buttonEPower.setText(R.string.ePower);
            buttonEPower.setTextSize(COMPLEX_UNIT_SP,20);
            buttonSquare.setText(R.string.square);
            buttonSquare.setTextSize(COMPLEX_UNIT_SP,20);
            buttonPower.setText(R.string.power);
            buttonPower.setTextSize(COMPLEX_UNIT_SP,20);
            buttonAbsolute.setText(R.string.absolute);
            buttonPhi.setText(R.string.pi);
            buttonFactorial.setText(R.string.factorial);
        }
    }

    private String InputValue(String string, String value)
    {
        if (value.equals(getString(R.string.dot)))
        {
            if (!decimalNumber) {
                decimalNumber = true;
                if (string.equals("")) return "0.";
                return string + value;
            }
            else return string;
        }
        return string + value;
    }

    private String GetFunctionValue() {
        Double value = Double.parseDouble(valueFunc);
        switch(func)
        {
            case sin:
                if (isRadiance)
                    return String.valueOf(Math.sin(value));
                else return String.valueOf(Math.sin(Math.toRadians(value)));
            case cos:
                if (isRadiance)
                    return String.valueOf(Math.cos(value));
                else return String.valueOf(Math.cos(Math.toRadians(value)));
            case tan:
                if (isRadiance)
                    return String.valueOf(Math.tan(value));
                else return String.valueOf(Math.tan(Math.toRadians(value)));
            case inv_sin:
                if (isRadiance)
                    return String.valueOf(Math.asin(value));
                else return String.valueOf(Math.asin(Math.toRadians(value)));
            case inv_cos:
                if (isRadiance)
                    return String.valueOf(Math.acos(value));
                else return String.valueOf(Math.acos(Math.toRadians(value)));
            case inv_tan:
                if (isRadiance)
                    return String.valueOf(Math.atan(value));
                else return String.valueOf(Math.atan(Math.toRadians(value)));
            case hyp_sin:
                if (isRadiance)
                    return String.valueOf(Math.sinh(value));
                else return String.valueOf(Math.sinh(Math.toRadians(value)));
            case hyp_cos:
                if (isRadiance)
                    return String.valueOf(Math.cosh(value));
                else return String.valueOf(Math.cosh(Math.toRadians(value)));
            case hyp_tan:
                if (isRadiance)
                    return String.valueOf(Math.tanh(value));
                else return String.valueOf(Math.tanh(Math.toRadians(value)));
            case inv_hyp_sin:
                if (isRadiance)
                    return String.valueOf(asinh(value));
                else return String.valueOf(asinh(Math.toRadians(value)));
            case inv_hyp_cos:
                if (isRadiance)
                    return String.valueOf(acosh(value));
                else return String.valueOf(acosh(Math.toRadians(value)));
            case inv_hyp_tan:
                if (isRadiance)
                    return String.valueOf(atanh(value));
                else return String.valueOf(atanh(Math.toRadians(value)));
            case percent:
                return String.valueOf(value / 100);
            case ln:
                return String.valueOf(Math.log(value));
            case log:
                return String.valueOf(Math.log10(value));
            case fraction:
                return String.valueOf(1 / value);
            case e_pow:
                return String.valueOf(Math.exp(value));
            case square:
                return String.valueOf(value * value);
            case cube:
                return String.valueOf(value * value * value);
            case pow:
                if (typing1)
                    return String.valueOf(Math.pow(Double.parseDouble(value1), value));
                else
                    return String.valueOf(Math.pow(Double.parseDouble(value2), value));
            case abs:
                return String.valueOf(Math.abs(value));
            case squareRoot:
                return String.valueOf(Math.sqrt(value));
            case cubeRoot:
                return String.valueOf(Math.cbrt(value));
            default:
                return null;
        }
    }

    //================MATH=========================//
    private double asinh(double x)
    {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    private double acosh(double x)
    {
        return Math.log(x + Math.sqrt(x*x - 1.0));
    }

    private double atanh(double x)
    {
        return 0.5*Math.log( (x + 1.0) / (x - 1.0));
    }

    private double factorial(int x)
    {
        int ans = 1;
        while (x != 0)
        {
            ans *= x--;
        }
        return ans;
    }
    //==============================================//

    private void initCalculator()
    {
        decimalNumber = onShift = onFunc = typing2 = false;
        typing1 = true;
        value1 = value2 = sign = "";
    }

    private math_func GetMathFunc(String function)
    {
        if (getString(R.string.sine).equals(function)) {
            if (onShift)
                return math_func.inv_sin;
            return math_func.sin;
        }
        else if (getString(R.string.cosine).equals(function)){
            if (onShift)
                return math_func.inv_cos;
            return math_func.cos;
        }
        else if (getString(R.string.tan).equals(function)){
            if (onShift)
                return math_func.inv_tan;
            return math_func.tan;
        }
        else if (getString(R.string.percent).equals(function)){
            return math_func.percent;
        }
        else if (getString(R.string.naturalLog).equals(function)){
            if (onShift)
                return math_func.hyp_sin;
            return math_func.ln;
        }
        else if (getString(R.string.log).equals(function)){
            if (onShift)
                return math_func.hyp_cos;
            return math_func.log;
        }
        else if (getString(R.string.fraction).equals(function)){
            if (onShift)
                return math_func.hyp_tan;
            return math_func.fraction;
        }
        else if (getString(R.string.ePower).equals(function)){
            if (onShift)
                return math_func.inv_hyp_sin;
            return math_func.e_pow;
        }
        else if (getString(R.string.square).equals(function)){
            if (onShift)
                return math_func.inv_hyp_cos;
            return math_func.square;
        }
        else if (getString(R.string.power).equals(function)){
            if (onShift)
                return math_func.inv_hyp_tan;
            return math_func.pow;
        }
        else if (getString(R.string.absolute).equals(function)){
            if (onShift)
                return math_func.twoPow;
            return math_func.abs;
        }
        else if (getString(R.string.squareRoot).equals(function)){
            if (onShift)
                return math_func.cubeRoot;
            return math_func.squareRoot;
        }
        else if (getString(R.string.factorial).equals(function)){
            if (onShift)
                return math_func.cube;
            return math_func.factorial;
        }
        return null;
    }


}

