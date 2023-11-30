package com.example.user_tasks_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class TaskActivity extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtDescription;
    private Button btSave;
    private Button btDelete;
    private TextView txtNoTitleMSG;
    private ListView listTasks;
    private CheckBox chkStatus;
    private boolean finished = false;
    private static final String TASKS = "TASKS";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        setViews();

        Intent intent = getIntent();
        String intentPurpose = intent.getStringExtra("TaskCreation");
        //This is to be used to differentiate between when creating and editing a task
        // using the if statements below

        if ("createTask".equals(intentPurpose)) {
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = edtTitle.getText().toString();
                    String desc = edtDescription.getText().toString();

                    if (edtTitle.getText().toString().isEmpty()) {
                        txtNoTitleMSG.setVisibility(View.VISIBLE);
                    } else {
                        txtNoTitleMSG.setVisibility(View.GONE);
                        Task newTask = new Task(title, desc, finished);
                        Task.addTask(newTask);

                        updatePreferences();

                        finish();
                    }
                }
            });
        } else if ("editTask".equals(intentPurpose)) {
            int id = (int) intent.getIntExtra("taskID", -1);
            if (id != -1) {
                Task task = getTaskById(id);
                if (task != null) {
                    edtTitle.setText(task.getTitle());
                    edtDescription.setText(task.getDescription());

                    btSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String title = edtTitle.getText().toString().trim();
                            String desc = edtDescription.getText().toString().trim();

                            if (title.isEmpty()) {
                                txtNoTitleMSG.setVisibility(View.VISIBLE);
                            } else {
                                txtNoTitleMSG.setVisibility(View.GONE);

                                task.setTitle(title);
                                task.setDescription(desc);

                                updatePreferences();

                                finish();
                            }
                        }
                    });

                    btDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Task.removeTask(task.getId());
                            updatePreferences();
                            finish();
                        }
                    });

                    chkStatus.setChecked(task.isFinished());

                    chkStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            task.setFinished(chkStatus.isChecked());
                            checkStatus(task);
                        }
                    });
                }
            }
        }
    }

        private void setViews () {
            edtTitle = findViewById(R.id.edtTitle);
            edtDescription = findViewById(R.id.edtDescription);
            btSave = findViewById(R.id.btSave);
            txtNoTitleMSG = findViewById(R.id.txtNoTitleMSG);
            chkStatus = findViewById(R.id.chkStatus);
            btDelete = findViewById(R.id.btDelete);
            listTasks = findViewById(R.id.listTasks);
        }

        private void checkStatus (Task task){
            if(task.isFinished()){
                Task.removeTask(task.getId());
                updatePreferences();
            }else{
                updatePreferences();
            }
        }

        private void updatePreferences () {
            Gson gson = new Gson();
            String taskListString = gson.toJson(Task.tasks);

            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences
                            (TaskActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TASKS, taskListString);
            editor.apply();
        }
    private Task getTaskById ( int taskId){
            for (Task task : Task.tasks) {
                if (task.getId() == taskId) {
                    return task;
                }
            }
            return null;
        }
}



