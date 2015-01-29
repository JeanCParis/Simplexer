package apps.klever.com.simplex;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Jean-Christophe on 28/01/2015.
 */
public class MaximizeFunctionLayout extends FunctionLayout {
    public MaximizeFunctionLayout(Context context) {
        super(context);
    }

    @Override
    public void initUnknowns(int unknownsAmount) {
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
        editText.setVisibility(INVISIBLE);
        editTexts.add(editText);
    }
}
