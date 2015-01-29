package apps.klever.com.simplex;

import android.util.Log;

import java.util.List;

/**
 * Created by Jean-Christophe on 22/01/2015.
 */
public class Simplex
{
    protected String log;
    protected float[][] originalMatrix, resultMatrix;
    protected List<Float> unknownsValues;
    protected float maximizationResult;

    public Simplex(float[][] simplexMatrix) {

        originalMatrix = copyTo(simplexMatrix);
        resultMatrix = simplexMatrix;
        log = convertSimplexMatrixToString(simplexMatrix);

        int pivotColumnIndex = getPivotColumnIndex(simplexMatrix);
        while (pivotColumnIndex != -1) {
            int pivotRowIndex = getPivotRowIndex(simplexMatrix, pivotColumnIndex);

            simplexMatrix = updateMatrix(simplexMatrix, pivotRowIndex, pivotColumnIndex);
            updateIndices(simplexMatrix, pivotRowIndex, pivotColumnIndex);

            log+=convertSimplexMatrixToString(simplexMatrix);
            pivotColumnIndex = getPivotColumnIndex(simplexMatrix);
        }

        unknownsValues = SimplexMatrixResolver.getUnknownsValue(simplexMatrix, originalMatrix);
        maximizationResult = getMaxValue(originalMatrix, unknownsValues);
    }

    public float[][] getOriginalMatrix() {
        return originalMatrix;
    }

    public float[][] getResultMatrix() {
        return resultMatrix;
    }

    public List<Float> getUnknownsValues() {
        return unknownsValues;
    }

    public float getMaximizationResult() {
        return maximizationResult;
    }

    public String getLog() {
        return log;
    }

    protected String getResultString()
    {
        String result = "Max(z) = " + String.format("%.2f",maximizationResult) + "\n";
        for (int i=0 ; i<unknownsValues.size() ; ++i)
        {
            result += "\nX" + (i+1) + " = " + String.format("%.2f",unknownsValues.get(i));
        }
        return result;
    }

    protected float[][] updateIndices(float[][] simplexMatrix, int pivotRowIndex, int pivotColumnIndex)
    {
        simplexMatrix[pivotRowIndex][0] = pivotColumnIndex;
        return simplexMatrix;
    }
    protected String convertSimplexMatrixToString(float [][] matrix)
    {
        int width = matrix.length;
        int height = matrix[width-1].length;
        String result="";

        for (int row=0 ; row<width ; ++row) {
            result+=(int)matrix[row][0] + " ";
            for (int column=1; column<height; ++column) {
                result+=String.format("%.2f",matrix[row][column]) + " ";
            }
            result += "\n";
        }
        result += "\n";
        return result;
    }

    protected float[][] updateMatrix(float[][] simplexMatrix, int pivotRowIndex, int pivotColumnIndex)
    {
        int width = simplexMatrix.length;
        int height = simplexMatrix[width-1].length;
        float[][] result = copyTo(simplexMatrix);
        float pivotValue = simplexMatrix[pivotRowIndex][pivotColumnIndex];

        for (int row=0 ; row<pivotRowIndex ; ++row)
        {
            float factor = simplexMatrix[row][pivotColumnIndex]/pivotValue;
            for(int column=1; column<height;++column){
                result[row][column]-=factor * simplexMatrix[pivotRowIndex][column];
            }
        }
        for(int column=1; column<height;++column){
            result[pivotRowIndex][column]=simplexMatrix[pivotRowIndex][column]/pivotValue;
        }
        for (int row=pivotRowIndex+1 ; row<width ; ++row)
        {
            float factor = simplexMatrix[row][pivotColumnIndex]/pivotValue;
            for(int column=1; column<height;++column){
                result[row][column]-=factor * simplexMatrix[pivotRowIndex][column];
            }
        }

        return result;
    }

    protected float[][] copyTo(float[][] aSource)
    {
        float[][] copy = new float[aSource.length][aSource[0].length];
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, copy[i], 0, aSource[i].length);
        }
        return copy;
    }

    protected int getPivotColumnIndex(float[][] simplexMatrix) {
        int width = simplexMatrix.length;
        int height = simplexMatrix[width-1].length;
        int maxIndex = 1;

        for (int column = 2; column < height; ++column) {
            if (simplexMatrix[width-1][column] > simplexMatrix[width - 1][maxIndex]) {
                maxIndex = column;
            }
        }
        if (simplexMatrix[width-1][maxIndex] > 0)
        {
            return maxIndex;
        }
        return -1;
    }

    protected int getPivotRowIndex(float[][] simplexMatrix, int maxColumnIndex)
    {
        int width = simplexMatrix.length;
        int height = simplexMatrix[width-1].length;
        int minIndex = 0;
        float minValue = simplexMatrix[minIndex][height-1]/simplexMatrix[minIndex][maxColumnIndex];

        for (int row=1; row < width-1; ++row) {
            float rowValue = simplexMatrix[row][height-1]/simplexMatrix[row][maxColumnIndex];
            if (rowValue < minValue)
            {
                minIndex = row;
                minValue = rowValue;
            }
        }

        return minIndex;
    }

    protected float getMaxValue(float[][] simplexMatrix, List<Float> values)
    {
        float result=0f;

        for (int i=0 ; i<simplexMatrix[0].length-2-(simplexMatrix.length-1) ; ++i)
        {
            result += simplexMatrix[simplexMatrix.length-1][i+1] * values.get(i);
            Log.i("result" + i, "" + result);
        }

        return result;
    }
}
