package com.example.user_tasks_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Button btQuickAdd;
    private Button bTCreateTask;
    private TextView txtErrorMSG;
    private TextView txtNoTaskMSG;
    private EditText edtTask;
    private ListView listTasks;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String TASKS = "TASKS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setupPrefrences();

        Gson gson = new Gson();
        String listString = sharedPreferences.getString(TASKS, "");

        Task[] tasks = gson.fromJson(listString, Task[].class);

        if (tasks != null) {
            Task.tasks.addAll(Arrays.asList(tasks));

            ArrayAdapter<Task> taskListAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, Task.tasks);

            listTasks.setAdapter(taskListAdapter);

            updateListVisibility();
            AdapterView.OnItemClickListener listItemClickListener =
                    new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(MainActivity.this,
                            TaskActivity.class);
                    intent.putExtra("TaskCreation", "editTask");
                    intent.putExtra("taskID",Task.tasks.get(position).getId());
                    startActivity(intent);
                }
            };
            listTasks.setOnItemClickListener(listItemClickListener);
        }

        btQuickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskTitleEntered = edtTask.getText().toString();
                if(taskTitleEntered.isEmpty()){
                    txtErrorMSG.setVisibility(View.VISIBLE);
                }else {
                    txtErrorMSG.setVisibility(View.GONE);
                    Task newTask = new Task (taskTitleEntered,"",false);
                    Task.addTask(newTask);

                    Gson gson = new Gson();
                    String taskListString = gson.toJson(Task.tasks);
                    editor.putString(TASKS, taskListString);
                    editor.apply();

                    ArrayAdapter<Task> taskListAdapter = (ArrayAdapter<Task>) listTasks.getAdapter();
                    if(taskListAdapter!= null){
                        taskListAdapter.notifyDataSetChanged();
                    }
                }
                edtTask.setText("");
                updateListVisibility();
            }
        });

        bTCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this,TaskActivity.class);
                intent2.putExtra("TaskCreation","createTask");
                startActivity(intent2);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateListVisibility();
    }

    private void setViews(){
        btQuickAdd = findViewById(R.id.btQuickAdd);
        bTCreateTask = findViewById(R.id.btCreateTask);
        txtErrorMSG = findViewById(R.id.txtErrorMSG);
        txtNoTaskMSG = findViewById(R.id.txtNoTaskMSG);
        edtTask = findViewById(R.id.edtTask);
        listTasks = findViewById(R.id.listTasks);
    }

    private void setupPrefrences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }

    private void updateListVisibility() {
        ArrayAdapter<Task> taskListAdapter = (ArrayAdapter<Task>) listTasks.getAdapter();
        if (taskListAdapter != null) {
            taskListAdapter.notifyDataSetChanged();
        }

        if (Task.tasks.size() > 0) {
            listTasks.setVisibility(View.VISIBLE);
            txtNoTaskMSG.setVisibility(View.GONE);
        } else {
            listTasks.setVisibility(View.GONE);
            txtNoTaskMSG.setVisibility(View.VISIBLE);
        }
    }
}