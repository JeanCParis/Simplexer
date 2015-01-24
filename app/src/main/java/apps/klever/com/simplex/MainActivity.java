package apps.klever.com.simplex;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView outputLog = (TextView)findViewById(R.id.outputLog);
        final TextView outputResult = (TextView)findViewById(R.id.outputResult);
        final FunctionsLayout functionsLayout = (FunctionsLayout)findViewById(R.id.main_layout_functions);
        final Button addUnknown = (Button)findViewById(R.id.button_add_unknown);
        final Button addFunction = (Button)findViewById(R.id.button_add_function);
        final Button calculate = (Button)findViewById(R.id.button_calculate);
        final Button reset = (Button)findViewById(R.id.button_reset);
        addUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionsLayout.addUnknown();
            }
        });
        addFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionsLayout.addFunction();
            }
        });
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Simplex simplex = new Simplex();
                float[][] simplexMatrix = functionsLayout.getSimplexMatrix();
                float[][] result = new float[simplexMatrix.length][simplexMatrix[0].length];
                copyTo(simplexMatrix, result);
                result = simplex.getResult(simplexMatrix);
                outputLog.setText(simplex.getLog());
                outputResult.setText(convertResultListToString(SimplexMatrixResolver.getUnknownsValue(result, simplexMatrix)));
                functionsLayout.refresh();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionsLayout.reset();
                outputLog.setText("");
                outputResult.setText("");
            }
        });
    }

    protected void copyTo(float[][] aSource, float[][] aDestination) {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }

    protected String convertResultListToString(List<Float> results)
    {
        String result = "";
        for (int i=0 ; i<results.size() ; ++i)
        {
            result += "X" + (i+1) + " : " + String.format("%.2f",results.get(i)) + "\n";
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
