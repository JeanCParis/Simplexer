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
    protected FunctionLayout maximizeFunctionLayout;
    protected List<FunctionLayout> functionLayouts = new ArrayList<>();
    protected List<Integer> unknownsIndexes = new ArrayList<>();

    protected SimplexMatrix simplexMatrix = new SimplexMatrix();

    public FunctionsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_functions, this);
        setOrientation(VERTICAL);
        initMaximizeFunction();
    }

    protected void initMaximizeFunction()
    {
        maximizeFunctionLayout = new FunctionLayout(getContext());
        maximizeFunctionLayout.initUnknowns(0);
        addView(maximizeFunctionLayout);
    }

    public void addUnknown()
    {
        int index = simplexMatrix.addUnknown();
        unknownsIndexes.add(index);
        for(FunctionLayout functionLayout : functionLayouts) {
            functionLayout.addUnknown();
        }
        maximizeFunctionLayout.addUnknown();
    }

    public void addFunction()
    {
        FunctionLayout functionLayout = new FunctionLayout(getContext());
        functionLayout.initUnknowns(unknownsIndexes.size());
        addView(functionLayout);
        functionLayouts.add(functionLayout);
    }

    public void reset()
    {
        functionLayouts = new ArrayList<>();
        unknownsIndexes = new ArrayList<>();
        removeAllViews();
        initMaximizeFunction();
        simplexMatrix.reset();
    }

    public float[][] getSimplexMatrix()
    {
        for (FunctionLayout functionLayout : functionLayouts) {
            simplexMatrix.addFunction(functionLayout.getFunctionValues(), unknownsIndexes);
        }
        simplexMatrix.setMaximizeFunction(maximizeFunctionLayout.getFunctionValues());
        return simplexMatrix.getMatrix();
    }
}
