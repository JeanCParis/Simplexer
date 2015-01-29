package apps.klever.com.simplex;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Christophe on 24/01/2015.
 */
public class FunctionLayout extends LinearLayout {
    List<EditText> editTexts = new ArrayList<>();

   public FunctionLayout(Context context) {
        super(context);
        inflate(context, R.layout.layout_function, this);
    }

    public void initUnknowns(int unknownsAmount)
    {
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(25, 10, 25, 10);

        for (int i=0 ; i<unknownsAmount ; ++i) {
            EditText editText = new EditText(getContext());
            //editText.setText("0");
            addView(editText, params);
            editTexts.add(editText);
        }
        EditText editText = new EditText(getContext());
        //editText.setText("0");
        addView(editText, params);
        editTexts.add(editText);
    }

    public void addUnknown()
    {
        EditText result = editTexts.get(editTexts.size()-1);
        editTexts.remove(result);
        removeView(result);
        EditText editText = new EditText(getContext());
        //editText.setText("0");
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(25, 10, 25, 10);
        addView(editText, params);
        editTexts.add(editText);
        addView(result, params);
        editTexts.add(result);
    }

    public void removeUnknown(int index)
    {
        EditText unknown = editTexts.get(index);
        editTexts.remove(unknown);
        removeView(unknown);
    }

    public void setValues(List<Float> values)
    {
        for(int i=0 ; i<values.size() ; ++i)
        {
            editTexts.get(i).setText(values.get(i).toString());
        }
    }

    public List<Float> getFunctionValues() throws WrongInputException
    {
        List<Float> function = new ArrayList<>();
        for (EditText editText : editTexts)
        {
                try {
                    String valueString = editText.getText().toString();
                    if (!valueString.isEmpty()) {
                        float value = 0;
                        value = Float.parseFloat(valueString);
                        function.add(value);
                    } else {
                        function.add(0f);
                    }
                } catch (java.lang.NumberFormatException e)
                {
                    throw new WrongInputException();
                }

        }
        return function;
    }
}

class WrongInputException extends Exception {}
