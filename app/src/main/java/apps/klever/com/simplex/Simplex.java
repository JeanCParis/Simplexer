package apps.klever.com.simplex;

/**
 * Created by Jean-Christophe on 22/01/2015.
 */
public class Simplex
{
    protected String log;

    public float[][] getResult(float[][] simplexMatrix) {

        log = convertSimplexMatrixToString(simplexMatrix);

        int pivotColumnIndex = getPivotColumnIndex(simplexMatrix);
        while (pivotColumnIndex != -1) {
            int pivotRowIndex = getPivotRowIndex(simplexMatrix, pivotColumnIndex);

            simplexMatrix = updatePivotRows(simplexMatrix, pivotRowIndex, pivotColumnIndex);
            updateIndices(simplexMatrix, pivotRowIndex, pivotColumnIndex);

            log+=convertSimplexMatrixToString(simplexMatrix);
            pivotColumnIndex = getPivotColumnIndex(simplexMatrix);
        }

        return simplexMatrix;
    }

    public String getLog() {
        return log;
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

    protected float[][] updatePivotRows(float[][] simplexMatrix, int pivotRowIndex, int pivotColumnIndex)
    {
        int width = simplexMatrix.length;
        int height = simplexMatrix[width-1].length;
        float[][] result = new float[width][height];
        copyTo(simplexMatrix, result);
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

    protected void copyTo(float[][] aSource, float[][] aDestination) {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
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
}
