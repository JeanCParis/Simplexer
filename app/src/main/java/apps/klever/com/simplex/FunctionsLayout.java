package apps.klever.com.simplex;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Christophe on 24/01/2015.
 */
public class FunctionsLayout extends LinearLayout {
    protected MaximizeFunctionLayout maximizeFunctionLayout;
    protected List<FunctionLayout> functionLayouts = new ArrayList<>();
    protected List<Integer> unknownsIndexes = new ArrayList<>();

    protected SimplexMatrix simplexMatrix = new SimplexMatrix();

    public FunctionsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_functions, null);
        setOrientation(VERTICAL);
        initMaximizeFunction();
    }

    protected void initMaximizeFunction() {
        maximizeFunctionLayout = new MaximizeFunctionLayout(getContext());
        maximizeFunctionLayout.initUnknowns(0);
        addView(maximizeFunctionLayout);
    }

    public void addUnknown() {
        int index = simplexMatrix.addUnknown();
        unknownsIndexes.add(index);
        for(FunctionLayout functionLayout : functionLayouts) {
            functionLayout.addUnknown();
        }
        maximizeFunctionLayout.addUnknown();
    }

    public void addFunction() {
        FunctionLayout functionLayout = new FunctionLayout(getContext());
        functionLayout.initUnknowns(unknownsIndexes.size());
        addView(functionLayout);
        functionLayouts.add(functionLayout);
    }

    public void setFunctionValues(int index, List<Float> values)
    {
        functionLayouts.get(index).setValues(values);
    }

    public void setMaximizeFunctionValues(List<Float> values)
    {
        maximizeFunctionLayout.setValues(values);
    }

    public void refresh() {
        simplexMatrix.reset();
        for(int unknownIndex : unknownsIndexes) {
            simplexMatrix.addUnknown();
        }
    }

    public void reset() {
        functionLayouts = new ArrayList<>();
        unknownsIndexes = new ArrayList<>();
        removeAllViews();
        initMaximizeFunction();
        simplexMatrix.reset();
    }

    public float[][] getSimplexMatrix() throws WrongInputException, NoUnknownException, InsufficientConstraintsException {
        if (simplexMatrix.unknownsAmount()==0) {
            throw new NoUnknownException();
        }
        for (FunctionLayout functionLayout : functionLayouts) {
            simplexMatrix.addFunction(functionLayout.getFunctionValues(), unknownsIndexes);
        }
        if (simplexMatrix.functionsAmount()==0) {
            throw new InsufficientConstraintsException();
        }
        simplexMatrix.setMaximizeFunction(maximizeFunctionLayout.getFunctionValues());
        return simplexMatrix.getMatrix();
    }
}

class NoUnknownException extends Exception {}
class InsufficientConstraintsException extends Exception {}