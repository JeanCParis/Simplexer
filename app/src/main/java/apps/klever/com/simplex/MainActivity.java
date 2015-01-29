package apps.klever.com.simplex;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView outputResult = (TextView)findViewById(R.id.outputResult);
        final TextView outputLog = (TextView)findViewById(R.id.outputLog);
        final FunctionsLayout functionsLayout = (FunctionsLayout)findViewById(R.id.main_layout_functions);

        functionsLayout.addFunction();
        functionsLayout.addFunction();
        functionsLayout.addFunction();
        functionsLayout.addUnknown();
        functionsLayout.addUnknown();
        functionsLayout.setFunctionValues(0, Arrays.asList(1f, 3f, 450f));
        functionsLayout.setFunctionValues(1, Arrays.asList(2f, 1f, 350f));
        functionsLayout.setFunctionValues(2, Arrays.asList(1f, 1f, 200f));
        functionsLayout.setMaximizeFunctionValues(Arrays.asList(1f, 2f));

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

                try {
                    float[][] simplexMatrix = functionsLayout.getSimplexMatrix();
                    Simplex simplex = new Simplex(simplexMatrix);
                    outputResult.setText(simplex.getResultString());
                    outputLog.setText(simplex.getLog());
                    functionsLayout.refresh();
                } catch (WrongInputException e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Only numbers are allowed").setTitle("Wrong input").setPositiveButton("Ok", null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
                catch (NoUnknownException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please add at least one unknown").setTitle("No unknown").setPositiveButton("Ok", null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
                catch (InsufficientConstraintsException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please add at least one constraint").setTitle("No unknown").setPositiveButton("Ok", null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionsLayout.reset();
                outputResult.setText("");
                outputLog.setText("");
            }
        });
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
