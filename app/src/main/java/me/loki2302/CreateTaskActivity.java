package me.loki2302;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.inject.Inject;

import me.loki2302.app.App;
import me.loki2302.app.commands.CreateTaskApplicationCommand;

public class CreateTaskActivity extends RoboActionBarActivity {
    @Inject
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.saveTaskMenuItem) {
            String taskDescription = ((EditText)findViewById(R.id.taskDescriptionEditText)).getText().toString();
            app.submit(new CreateTaskApplicationCommand(taskDescription));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
