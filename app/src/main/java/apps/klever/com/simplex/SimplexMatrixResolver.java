package apps.klever.com.simplex;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Christophe on 24/01/2015.
 */
public class SimplexMatrixResolver
{
    public static List<Float> getUnknownsValue(float[][] simplexResultMatrix, float[][] simplexOriginalMatrix)
    {
        int unknowns = simplexResultMatrix[0].length-2;
        List<Float> values = new ArrayList<>();
        for (int i=0 ; i<unknowns ; ++i)
        {
            values.add(null);
        }
        for (int i=0 ; i<simplexResultMatrix.length-1 ; ++i) {
            values.set((int)simplexResultMatrix[i][0]-1, simplexResultMatrix[i][simplexResultMatrix[0].length-1]);
        }

        for (int i=0 ; i<values.size() ; ++i)
        {
            if (values.get(i)==null)
            {
                //calculate
                for (int j=0 ; j<simplexResultMatrix.length ; ++j) {
                    if (simplexResultMatrix[j][i+1]!=0)
                    {
                        try {
                            values.set(i, calculateValue(i, values, simplexOriginalMatrix[j]));
                            j=simplexResultMatrix.length;
                        } catch (MoreThanOneUnknownException e)
                        {
                            Log.e("SimpleMatrixResolver calculateValue", e.getMessage());
                        }
                    }
                }
            }
            Log.i("SimplexMatrix values", "i = " + i + " ; value = " + values.get(i));
        }

        return values;
    }

    protected static float calculateValue(int unknownIndex, List<Float> unknownsValues, float[] function) throws MoreThanOneUnknownException {
        int result=0;
        for(int i=0 ; i<unknownsValues.size() ; ++i) {
            if (i!=unknownIndex && function[i+1]!=0)
            {
                if (unknownsValues.get(i) == null)
                {
                    throw new MoreThanOneUnknownException();
                }

                result += function[i+1] * unknownsValues.get(i);
                Log.i("SimplexMatrix result", "" + result);
            }
        }
        Log.i("SimplexMatrix equals", "" + function[function.length-1]);
        return function[function.length-1]-result;
    }
}

class MoreThanOneUnknownException extends Exception {
    @Override
    public String getMessage()
    {
        return "More than one unknown in function";
    }
}
