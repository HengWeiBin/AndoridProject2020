package ntut.org.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
        factorial, switchSign}

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
        mInput = findViewById(R.id.input);
        mResult = findViewById(R.id.result);

        if (savedInstanceState != null)
        {
            SetText(mInput, savedInstanceState.getString(M_INPUT));
            SetText(mResult, savedInstanceState.getString(M_RESULT));
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

        // If user input a constant symbol, convert it to value
        if (value.equals(getString(R.string.pi))) {
            if (onShift)
                value = String.valueOf(Math.E);
            else
                value = String.valueOf(Math.PI);
        }
        if (value.equals(getString(R.string.answer))){
            value = mResult.getText().toString();
        }

        //Write value to variable
        if (onFunc) valueFunc = InputValue(valueFunc, value);

        else if (typing1) value1 = InputValue(value1, value);

        else if (typing2) value2 = InputValue(value2, value);

        else ToastError();
        //Do not show value on screen if all input is unavailable
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
            else
            {
                ToastError();
                return;
            }
        }

        if (onFunc)
        {
            if (valueFunc.equals(""))
            {   // Toast message and ignore input when user click operator with out input valueFunction
                ToastError();
                return;
            }

            if (typing1)
            {
                SetText(mInput, mInput.getText() + ")" + operator);
                value1 = GetFunctionValue();
            }
            else if (typing2)
            {
                value2 = GetFunctionValue();
                Click_Result(view);
                value1 = mResult.getText().toString();
                SetText(mInput, value1 + operator);
            }
        }
        else if (typing1)
        {
            SetText(mInput, value1+ operator);
        }
        else if (typing2 && !value2.equals(""))
        {
            Click_Result(view);
            value1 = mResult.getText().toString();
            SetText(mInput, value1 + operator);
        }
        else
        {
            SetText(mInput, value1 + operator);
        }
        sign = operator;
        typing1 = false;
        typing2 = true;
    }

    public void onClickFunction(View view)
    {
        math_func temp = func;
        func = GetMathFunc(view.getTag().toString());

        //ignore user if he's typing any func's value
        if (func == null || (onFunc && func != math_func.switchSign))
        {
            func = temp;
            ToastError();
            return;
        }

        switch(func) {
            case switchSign:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Double.parseDouble(value1) * -1);
                    SetText(mInput, value1);
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Double.parseDouble(value2) * -1);
                    SetText(mInput, value1 + sign + "(" + value2 + ")");
                } else if (onFunc && !valueFunc.equals("")){
                    valueFunc = String.valueOf(Double.parseDouble(valueFunc) * -1);
                    SetText(mInput, RemoveCurrentValue(mInput.getText().toString(), '(') + valueFunc);
                }
                else ToastError();
                func = temp;
                return;
            case percent:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Double.parseDouble(value1) / 100);
                    typing1 = typing2 = onFunc = false;
                    SetText(mInput, mInput.getText() + getString(R.string.percent));
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Double.parseDouble(value2) / 100);
                    Click_Result(view);
                }
                else ToastError();
                return;
            case square:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Math.pow(Double.parseDouble(value1), 2));
                    typing1 = typing2 = onFunc = false;
                    SetText(mInput, mInput.getText() + "²");
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Math.pow(Double.parseDouble(value2), 2));
                    Click_Result(view);
                }
                else ToastError();
                return;
            case cube:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(Math.pow(Double.parseDouble(value1), 3));
                    typing1 = typing2 = onFunc = false;
                    SetText(mInput, mInput.getText() + "³");
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(Math.pow(Double.parseDouble(value2), 3));
                    Click_Result(view);
                }
                else ToastError();
                return;
            case factorial:
                if (typing1 && !value1.equals("")) {
                    value1 = String.valueOf(factorial((int)Double.parseDouble(value1)));
                    typing1 = typing2 = onFunc = false;
                    SetText(mInput, mInput.getText() + "!");
                } else if (typing2 && !value2.equals("")) {
                    value2 = String.valueOf(factorial((int)Double.parseDouble(value2)));
                    Click_Result(view);
                }
                else ToastError();
                return;
            case pow:
                //Func power must be calculate by two value
                //if there's no first value, ignore user
                if ((typing1 && value1.equals("")) || (typing2 && value2.equals(""))){
                    ToastError();
                    return;
                }
                onFunc = true;
                SetText(mInput, mInput.getText() + getString(R.string.power) + "(");
                break;
        }

        if ((typing1 && !value1.equals("")) || typing2 && !value2.equals(""))
        {   //Toast error when user input a function behind value without operator
            ToastError();
            return;
        }

        switch(func) {
            case sin:
                SetText(mInput, mInput.getText() + "sin(");
                break;
            case cos:
                SetText(mInput, mInput.getText() + "cos(");
                break;
            case tan:
                SetText(mInput, mInput.getText() + "tan(");
                break;
            case inv_sin:
                SetText(mInput, mInput.getText() + getString(R.string.inverseSine) + "(");
                break;
            case inv_cos:
                SetText(mInput, mInput.getText() + getString(R.string.inverseCosine) + "(");
                break;
            case inv_tan:
                SetText(mInput, mInput.getText() + getString(R.string.inverseTangent) + "(");
                break;
            case hyp_sin:
                SetText(mInput, mInput.getText() + getString(R.string.hyperbolicSine) + "(");
                break;
            case hyp_cos:
                SetText(mInput, mInput.getText() + getString(R.string.hyperbolicCosine) + "(");
                break;
            case hyp_tan:
                SetText(mInput, mInput.getText() + getString(R.string.hyperbolicTangent) + "(");
                break;
            case inv_hyp_sin:
                SetText(mInput, mInput.getText() + getString(R.string.inverseHyperSine) + "(");
                break;
            case inv_hyp_cos:
                SetText(mInput, mInput.getText() + getString(R.string.inverseHyperCosine) + "(");
                break;
            case inv_hyp_tan:
                SetText(mInput, mInput.getText() + getString(R.string.inverseHyperTangent) + "(");
                break;
            case ln:
                SetText(mInput, mInput.getText() + getString(R.string.naturalLog) + "(");
                break;
            case log:
                SetText(mInput, mInput.getText() + getString(R.string.log) + "(");
                break;
            case fraction:
                SetText(mInput, mInput.getText() + "1/(");
                break;
            case e_pow:
                SetText(mInput, mInput.getText() + getString(R.string.ePower) + "(");
                break;
            case abs:
                SetText(mInput, mInput.getText() + "abs(");
                break;
            case squareRoot:
                SetText(mInput, mInput.getText() + getString(R.string.squareRoot) + "(");
                break;
            case cubeRoot:
                SetText(mInput, mInput.getText() + getString(R.string.cubeRoot) + "(");
                break;
            case twoPow:
                SetText(mInput, mInput.getText() + getString(R.string.twoPower) + "(");
                break;
        }
        onFunc = true;
    }

    public void Click_Result(View view){    //on press =
        double result;

        if (!onFunc && !value1.equals("") && !value2.equals(""))
        {   //Normal situation, an operator with two value
            if (sign.equals(getString(R.string.plus))) {
                result = Double.parseDouble(value1) + Double.parseDouble(value2);
            } else if (sign.equals(getString(R.string.minus))) {
                result = Double.parseDouble(value1) - Double.parseDouble(value2);
            } else if (sign.equals(getString(R.string.multi))) {
                result = Double.parseDouble(value1) * Double.parseDouble(value2);
            } else if (sign.equals(getString(R.string.divide))) {
                result = Double.parseDouble(value1) / Double.parseDouble(value2);
            } else result = Double.NaN;
            SetText(mResult, Double.toString(result));
        }
        else if(typing2 && onFunc)
        {   //when user is input value2 as function
            if (!valueFunc.equals("")) {
                value2 = String.valueOf(Double.parseDouble(GetFunctionValue()));
                Click_Result(view);
            }
            else {  //Toast error if function value is empty
                ToastError();
                return;
            }
        }
        else if(onFunc)
        {   //when user is input value1 as function
            if (!valueFunc.equals("")) {
                result = Double.parseDouble(GetFunctionValue());
                SetText(mResult, String.valueOf(result));
            }
            else{  //Toast error if function value is empty
                ToastError();
                return;
            }
        }
        else {
            SetText(mResult, value1);
        }

        //reset input
        SetText(mInput, "");
        value1 = value2 = valueFunc = sign = "";
        decimalNumber = typing2 = false;
        typing1 = true;
    }

    public void Click_clear(View view) {
        SetText(mInput, "");
        SetText(mResult, "");
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

    private void SetText(final TextView textView, String string)
    {   // Scale size of text automatically if it's portrait mode
        textView.setText(string);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {   // original text size of mResult is 100sp, mInput is 70sp
            float originalSize;
            if (textView == mResult)
                originalSize = 100;
            else originalSize = 70;

            // Start size = current size
            float startSize = textView.getTextSize() / getResources().getDisplayMetrics().scaledDensity;

            //End size according to length of string
            float endSize;
            if (string.length() < 7)
                endSize = originalSize;
            else if (string.length() < 8)
                endSize = originalSize * 0.92f;
            else if (string.length() < 9)
                endSize = originalSize * 0.84f;
            else if (string.length() < 10)
                endSize = originalSize * 0.76f;
            else if (string.length() < 11)
                endSize = originalSize * 0.68f;
            else if (string.length() < 12)
                endSize = originalSize * 0.60f;
            else
                endSize = originalSize * 0.52f;

            // Handle animation of text size change
            ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedValue = (float) valueAnimator.getAnimatedValue();
                    textView.setTextSize(animatedValue);
                }
            });
            animator.start();
        }

        // scrollView of mInput maintain at last digit
        if (textView == mInput)
        {
            HorizontalScrollView scrollView = findViewById(R.id.inputScrollView);
            scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
        }
    }

    private String InputValue(String string, String value)
    {
        //If user input a constant value or recent answer, ignore original value and set it to constant
        if (value.equals(String.valueOf(Math.E)) || value.equals(String.valueOf(Math.PI))
                || value.equals(mResult.getText().toString()))
        {   // recent result, Pi and e are decimal number
            decimalNumber = true;
            ShowValue(value);
            return value;
        }

        // if user already input a dot, toast error if a dot being input again
        if (value.equals(getString(R.string.dot)))
        {
            if (!decimalNumber) {
                decimalNumber = true;
                ShowValue(value);
                if (string.equals("")) return "0.";
                return string + value;
            }
            else {
                ToastError();
                return string;
            }
        }
        ShowValue(value);
        return string + value;
    }

    private void ShowValue(String value)
    {
        if (value.equals(String.valueOf(Math.PI)) || value.equals(String.valueOf(Math.E))
                || value.equals(mResult.getText().toString()))
        {
            if (onFunc)
                SetText(mInput, RemoveCurrentValue(mInput.getText().toString(), '(') + value);
            else if (typing2)
                SetText(mInput, RemoveCurrentValue(mInput.getText().toString(), sign.charAt(0)) + value);
            else
                SetText(mInput, value);
            return;
        }
        //Show input to screen
        //If value which user is inputting is empty, add "0." automatically
        //SetText(mInput, value1 + sign + value2);
        if (value.equals(getString(R.string.dot)))
        {
            if (onFunc && valueFunc.equals(""))
                SetText(mInput, mInput.getText() + "0.");
            else if (typing1 && value1.equals(""))
                SetText(mInput, mInput.getText() + "0.");
            else if (typing2 && value2.equals(""))
                SetText(mInput, mInput.getText() + "0.");
            else
                SetText(mInput, mInput.getText() + value);
        }
        else
            SetText(mInput, mInput.getText() + value);
    }

    private String RemoveCurrentValue(String string, char c)
    {
        // This function will return a subString before 'c'
        int charRemove = 0;
        for (int i = string.length() - 1; i >= 0; i--)
        {
            if (string.charAt(i) != c) charRemove++;
            else break;
        }
        return string.substring(0, string.length() - charRemove);
    }

    private void ToastError() {
        Toast.makeText(getApplicationContext(), "Wrong format!",
                Toast.LENGTH_SHORT).show();
    }

    private String GetFunctionValue() {
        Double value = Double.parseDouble(valueFunc);

        //reset state of typing function
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
                else if (typing2)
                    return String.valueOf(Math.pow(Double.parseDouble(value2), value));
            case abs:
                return String.valueOf(Math.abs(value));
            case squareRoot:
                return String.valueOf(Math.sqrt(value));
            case cubeRoot:
                return String.valueOf(Math.cbrt(value));
            case twoPow:
                return String.valueOf(Math.pow(2, value));
            default:
                return String.valueOf(Double.NaN);
        }
    }

    //================MATH=========================//
    private double asinh(double x)
    {   // return inverse of hyperbolic sine
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    private double acosh(double x)
    {   // return inverse of hyperbolic cosine
        return Math.log(x + Math.sqrt(x*x - 1.0));
    }

    private double atanh(double x)
    {   // return inverse of hyperbolic tangent
        return 0.5*Math.log( (x + 1.0) / (x - 1.0));
    }

    private double factorial(int x)
    {   // return factorial of x
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
        decimalNumber = onFunc = typing2 = false;
        isRadiance = typing1 = true;
        value1 = value2 = valueFunc = sign = "";
        func = null;
    }

    private math_func GetMathFunc(String function)
    {
        //There are two pages in our calculator,
        //but both pages of button share same button
        //hence, I use a function to identify them
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

