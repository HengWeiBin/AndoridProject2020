package ntut.org.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        squareRoot, cubeRoot, twoPow,
        factorial, switchSign};

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

        //Write value to variable
        if (onFunc) valueFunc = InputValue(valueFunc, value);

        else if (typing1) value1 = InputValue(value1, value);

        else if (typing2) value2 = InputValue(value2, value);
        //Do not show value on screen if all input is unavailable
        else return;

        ShowValue(value);
    }

    public void onClickOperator(View view)
    {
        String operator = view.getTag().toString();

        //If any operator clicked, any value of typing should be done
        //reset decimal detection
        decimalNumber = false;

        if (typing1 && value1.equals("") && valueFunc.equals(""))
        {   //if result != empty and another value is empty, value1 = result
            if (!mResult.getText().toString().equals(""))
            {
                value1 = mResult.getText().toString();
            }
            //ignore when user click operator without input number
            else return;
        }

        if (onFunc)
        {
            if (valueFunc.equals("")) return;

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
                value2 = GetFunctionValue();
                Click_Result(view);
                sign = operator;
                mInput.setText(value1 + sign);
            }
        }
        else if (typing1)
        {
            typing1 = false;
            mInput.setText(value1+ operator);
            sign = operator;
            typing2 = true;
        }
        else if (typing2 && !value2.equals(""))
        {
            Click_Result(view);
            typing1 = false;
            sign = operator;
            value1 = mResult.getText().toString();
            mInput.setText(value1 + sign);
        }
        else
        {
            sign = operator;
            mInput.setText(value1 + sign);
            typing2 = true;
        }
    }

    public void onClickFunction(View view)
    {
        //ignore user if he's typing any func's value
        if (onFunc) return;

        func = GetMathFunc(view.getTag().toString());

        switch(func) {
            case switchSign:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Double.parseDouble(value1) * -1);
                    mInput.setText(value1);
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Double.parseDouble(value2) * -1);
                    mInput.setText(value1 + sign + "(" + value2 + ")");
                }
                return;
            case percent:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Double.parseDouble(value1) / 100);
                    typing1 = typing2 = onFunc = false;
                    mInput.setText(mInput.getText() + getString(R.string.percent));
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Double.parseDouble(value2) / 100);
                    Click_Result(view);
                }
                return;
            case square:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Math.pow(Double.parseDouble(value1), 2));
                    typing1 = typing2 = onFunc = false;
                    mInput.setText(mInput.getText() + "²");
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Math.pow(Double.parseDouble(value2), 2));
                    Click_Result(view);
                }
                return;
            case cube:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Math.pow(Double.parseDouble(value1), 3));
                    typing1 = typing2 = onFunc = false;
                    mInput.setText(mInput.getText() + "³");
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Math.pow(Double.parseDouble(value2), 3));
                    Click_Result(view);
                }
                return;
            case factorial:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(factorial(Integer.parseInt(value1)));
                    typing1 = typing2 = onFunc = false;
                    mInput.setText(mInput.getText() + "!");
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(factorial(Integer.parseInt(value2)));
                    Click_Result(view);
                }
                return;
        }

        switch(func) {
            case sin:
                mInput.setText(mInput.getText() + "sin(");
                break;
            case cos:
                mInput.setText(mInput.getText() + "cos(");
                break;
            case tan:
                mInput.setText(mInput.getText() + "tan(");
                break;
            case inv_sin:
                mInput.setText(mInput.getText() + getString(R.string.inverseSine) + "(");
                break;
            case inv_cos:
                mInput.setText(mInput.getText() + getString(R.string.inverseCosine) + "(");
                break;
            case inv_tan:
                mInput.setText(mInput.getText() + getString(R.string.inverseTangent) + "(");
                break;
            case hyp_sin:
                mInput.setText(mInput.getText() + getString(R.string.hyperbolicSine) + "(");
                break;
            case hyp_cos:
                mInput.setText(mInput.getText() + getString(R.string.hyperbolicCosine) + "(");
                break;
            case hyp_tan:
                mInput.setText(mInput.getText() + getString(R.string.hyperbolicTangent) + "(");
                break;
            case inv_hyp_sin:
                mInput.setText(mInput.getText() + getString(R.string.inverseHyperSine) + "(");
                break;
            case inv_hyp_cos:
                mInput.setText(mInput.getText() + getString(R.string.inverseHyperCosine) + "(");
                break;
            case inv_hyp_tan:
                mInput.setText(mInput.getText() + getString(R.string.inverseHyperTangent) + "(");
                break;
            case ln:
                mInput.setText(mInput.getText() + getString(R.string.naturalLog) + "(");
                break;
            case log:
                mInput.setText(mInput.getText() + getString(R.string.log) + "(");
                break;
            case fraction:
                mInput.setText(mInput.getText() + "1/(");
                break;
            case e_pow:
                mInput.setText(mInput.getText() + getString(R.string.ePower) + "(");
                break;
            case pow:
                //Func power must be calculate by two value
                //if there's no first value, ignore user
                if ((typing1 && value1.equals("")) || (typing2 && value2.equals(""))) return;
                mInput.setText(mInput.getText() + getString(R.string.power) + "(");
                break;
            case abs:
                mInput.setText(mInput.getText() + "abs(");
                break;
            case squareRoot:
                mInput.setText(mInput.getText() + getString(R.string.squareRoot) + "(");
                break;
            case cubeRoot:
                mInput.setText(mInput.getText() + getString(R.string.cubeRoot) + "(");
                break;
            case twoPow:
                mInput.setText(mInput.getText() + getString(R.string.twoPower) + "(");
                break;
        }
        onFunc = true;
    }

    public void Click_Result(View view){    //on press =
        double result;

        if (!value1.equals("") && !value2.equals(""))
        {
            if (sign.equals(getString(R.string.plus))) {
                result = Double.parseDouble(value1) + Double.parseDouble(value2);
            } else if (sign.equals(getString(R.string.minus))) {
                result = Double.parseDouble(value1) - Double.parseDouble(value2);
            } else if (sign.equals(getString(R.string.multi))) {
                result = Double.parseDouble(value1) * Double.parseDouble(value2);
            } else if (sign.equals(getString(R.string.divide))) {
                result = Double.parseDouble(value1) / Double.parseDouble(value2);
            } else result = Double.NaN;
            mResult.setText(Double.toString(result));
        }
        else if(!valueFunc.equals(""))
        {
            result = Double.parseDouble(GetFunctionValue());
            mResult.setText(String.valueOf(result));
        }
        else {
            mResult.setText(value1);
        }

        //reset input
        mInput.setText(null);
        value1 = value2 = valueFunc = sign = "";
        typing1 = true;
    }

    public void Click_clear(View view) {
        mInput.setText(null);
        mResult.setText(null);
        initCalculator();
    }

    public void SwitchDegree(View view){
        Button btn = (Button) view;
        if(isRadiance)
            btn.setText(getString(R.string.degree));
        else
            btn.setText(getString(R.string.radius));
        isRadiance = !isRadiance;
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

    private void ShowValue(String value)
    {
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
            else
                mInput.setText(mInput.getText() + value);
        }
        else
            mInput.setText(mInput.getText() + value);
    }

    private String GetFunctionValue() {
        Double value = Double.parseDouble(valueFunc);

        //reset typing function
        onFunc = false;
        valueFunc = "";

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
        value1 = value2 = valueFunc = sign = "";
        func = null;
    }

    private math_func GetMathFunc(String function)
    {
        if (getString(R.string.switchSign).equals(function))
        {
            return math_func.switchSign;
        }
        else if (getString(R.string.sine).equals(function)) {
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

