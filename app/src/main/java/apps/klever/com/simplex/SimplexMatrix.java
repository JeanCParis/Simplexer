package apps.klever.com.simplex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Christophe on 23/01/2015.
 */
public class SimplexMatrix {
    protected List<List<Float>> matrix = new ArrayList<List<Float>>();

    public SimplexMatrix() {
        init();
    }

    protected void init()
    {
        List<Float> result = new ArrayList<>();
        result.add(0f);
        result.add(0f);
        matrix.add(result);
    }

    public void reset()
    {
        matrix = new ArrayList<>();
        init();
    }

    public int addFunction(List<Float> values, List<Integer> indexes) {
        addUnknown();
        List<Float> function = new ArrayList<Float>();
        for(int i=0 ; i<matrix.get(0).size() ; ++i) {
           function.add(0f);
        }
        for(int i=0 ; i<indexes.size() ; ++i)
        {
            function.set(indexes.get(i)-1, values.get(i));
        }

        matrix.add(function);
        for(int i=0 ; i<matrix.size()-1 ; ++i) {
            matrix.get(i).set(matrix.get(0).size()-1, matrix.get(i).get(matrix.get(0).size()-2));
            matrix.get(i).set(matrix.get(0).size()-2, 0f);
        }
        matrix.get(matrix.size()-1).set(matrix.get(0).size()-2, 1f);
        matrix.get(matrix.size()-1).set(matrix.get(0).size()-1, values.get(values.size()-1));

        return matrix.size()-1;
    }

    public float[][] setMaximizeFunction(List<Float> values) {
        List<Float> maximizeFunction = new ArrayList<Float>();
        for(int i=0 ; i<matrix.get(0).size() ; ++i) {
            maximizeFunction.add(0f);
        }
        for(int i=0 ; i<values.size() ; ++i) {
            maximizeFunction.set(i+1, values.get(i));
        }
        matrix.add(maximizeFunction);

        int unknowns = matrix.get(0).size()-1-(matrix.size()-2)-1;
        ++unknowns;
        for(int i=1 ; i<matrix.size()-1 ; ++i) {
            matrix.get(i).set(0, new Float(unknowns));
            ++unknowns;
        }
        return getMatrix();
    }

    public void removeFunction(int index)
    {
        for(int i=0 ; i<matrix.get(0).size() ; ++i) {
            matrix.get(index).set(i,0f);
        }
    }

    public int addUnknown()
    {
        for(List<Float> floats : matrix) {
            floats.add(0f);
        }
        return matrix.get(0).size()-1;
    }

    public void removeUnknown(int index)
    {
        for(List<Float> floats : matrix) {
            floats.set(index, 0f);
        }
    }

    public float[][] getMatrix()
    {
        float[][] result = new float[matrix.size()-1][matrix.get(0).size()];
        for (int i=0 ; i<matrix.size()-1 ; ++i) {
            for (int j=0 ; j<matrix.get(0).size() ; ++j) {
                result[i][j]=matrix.get(i+1).get(j);
            }
        }
        return result;
    }
}
