package fmc.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class FiltersActivity extends Activity {

    public static String EXTRA_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static String getResult(Intent intent) {
        return (String) intent.getSerializableExtra(EXTRA_RESULT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnResult();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnResult() {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, "Filters");
        setResult(RESULT_OK, intent);
    }
}