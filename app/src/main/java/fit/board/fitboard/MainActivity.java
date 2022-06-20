package fit.board.fitboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.SSLCertificateSocketFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    SQLiteDatabase workoutsDB;
    float density;

    int PROFILE_ID = 0, PROGRESS_ID = 1, HOME_ID = 2, WORKOUTS_ID = 3, EXERCISES_ID = 4;
    LinearLayout[] mainLayouts;
    IconBar iconBar;

    DatabaseManager dbManager;

    WorkoutTab2 workoutTab2;
    ExerciseTab exerciseTab;
    ProgressTab progressTab;
    TextView tabTitleTV;

    int lastWorkoutID = 0;
    static int RESULT_LOAD_IMAGE = 1;
    static int PIC_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        
        init();
        loadDatabase();
        try {
            loadFromOnlineDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        iconBar.changeTab(WORKOUTS_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    

    private void init() {
        context = this;
        density = getResources().getDisplayMetrics().density;

        dbManager = new DatabaseManager(this);

        this.getSupportActionBar().hide();

        workoutTab2 = new WorkoutTab2(this);
        exerciseTab = new ExerciseTab(this);
        progressTab = new ProgressTab(this);

        mainLayouts = new LinearLayout[5];
        mainLayouts[WORKOUTS_ID] = workoutTab2.mainLayout;
        mainLayouts[PROFILE_ID] = findViewById(R.id.profile_main);
        mainLayouts[HOME_ID] = findViewById(R.id.home_main);
        mainLayouts[PROGRESS_ID] = findViewById(R.id.progress_main);
        mainLayouts[EXERCISES_ID] = exerciseTab.mainLayout;

        tabTitleTV = findViewById(R.id.tab_title);

        iconBar = new IconBar();
        /*try {
            loadFromOnlineDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } */



    }

    void loadFromOnlineDatabase() throws ClassNotFoundException {
        Log.i(null, "entered");
        // TODO Auto-generated method stub
        URL url;
        BufferedReader reader = null;
        String s = "";
        try {
            url = new URL("https://192.168.56.1/test.php");
            URLConnection con = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = reader.readLine().toString();
            while ((line = reader.readLine()) != null) {
                s = s + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("wowz", "response: " + s);
        return;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.workout_icon:
                iconBar.changeTab(WORKOUTS_ID);
                //workoutTab.setVisible();
                break;
            /*case R.id.home_button:
                iconBar.changeTab(HOME_ID);
                break; */
            /* case R.id.profile_icon:
                iconBar.changeTab(PROFILE_ID);
                break; */
            /* case R.id.exercise_tab_icon:
                iconBar.changeTab(EXERCISES_ID);
                break; */
            case R.id.weight_tab_icon:
                iconBar.changeTab(PROGRESS_ID);
                break;
            case R.id.add_workout_button:
                workoutTab2.setCreateMode();
                break;
            case R.id.addNewExercise:
                workoutTab2.addExercise(workoutTab2.newWorkoutLayouts.length, 1, true);
                break;
            case R.id.return_icon:
                workoutTab2.returnClick();
                break;
            case R.id.finishAddButton:
                workoutTab2.saveAndReset();
                break;
            case R.id.active_timer_button:
                workoutTab2.timerSelect();
                break;
            case R.id.active_finish_button:
                workoutTab2.activeFinish();
                break;
            case R.id.active_return_icon:
                workoutTab2.activeReturn();
                break;
            case R.id.save_no_button:
                workoutTab2.noSave();
                break;
            case R.id.save_yes_button:
                workoutTab2.yesSave();
                break;
            case R.id.history_back_button:
                workoutTab2.returnFromHistory();
                break;
            case R.id.back_exercise_history_button:
                exerciseTab.returnToList();
                break;
            case R.id.progress_entry_add_button:
                progressTab.addEntry();
                break;
            case R.id.progress_attach_button:
                progressTab.selectAttachment();
                break;

        }

        workoutTab2.checkNewWorkoutIDs(v.getId());
        workoutTab2.checkCurrentWorkoutIDs(v.getId());
        workoutTab2.checkCurrentWorkoutHistoryIDs(v.getId());

    }

    void loadDatabase() {

        Log.d("tag1", "Here");
        workoutsDB = openOrCreateDatabase("CLIPBOARD", MODE_PRIVATE, null);


        workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS WorkoutList(workoutID INT, workout VARCHAR);");



        //workoutsDB.execSQL("INSERT INTO WorkoutList Values(0, 'Chest & Back');");
        //workoutsDB.execSQL("INSERT INTO WorkoutList Values(1, 'Legs');");
        //workoutsDB.execSQL("INSERT INTO WorkoutList Values(2, 'Abs');");
        //workoutsDB.execSQL("INSERT INTO WorkoutList Values(3, 'Bis & Tris');");
        //workoutsDB.execSQL("INSERT INTO WorkoutList Values(4, 'Chest (Heavy)');");
        //workoutsDB.execSQL("INSERT INTO WorkoutList Values(5, 'Sprints');");

        // workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS Workout001(Exercise VARCHAR, Type VARHAR, Break INT)");
        // workoutsDB.execSQL("INSERT INTO Workout001 Values('Sh. Press', 'Reps', 120)");
        //workoutsDB.execSQL("INSERT INTO Workout001 Values('Sh. Press2', 'Reps', 120)");
        //workoutsDB.execSQL("INSERT INTO Workout001 Values('Sh. Press3', 'Reps', 120)");

        // workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS Workout001Exercises(bench_press INT, dumbbell_flys INT);");
        //workoutsDB.execSQL("INSERT INTO Workout001Exercises Values(120, 120);");
        //workoutsDB.execSQL("INSERT INTO Workout001Exercises Values(120, 120);");
        //workoutsDB.execSQL("INSERT INTO Workout001Exercises Values(120, 120);");

        workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS ExerciseList(exerciseID INT, exercise VARCHAR);");

        Cursor c = workoutsDB.rawQuery("SELECT * FROM WorkoutList", null);
        Log.d("tag1", "Here1");
        if(c.getCount() > 0){
            Log.d("tag1", "Here2");
            c.moveToLast();
            lastWorkoutID = c.getInt(0);
        }



        //loadWorkouts();

        //loadDefaultWorkouts();

        //loadActiveWorkout();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*Uri selectedData = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedData, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        controller.profilePicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        //controller.profilePicture.setImageResource(R.drawable.timer_icon);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            controller.profilePicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } */


        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");
                //controller.profilePicture.setScaleType(ImageView.ScaleType.FIT_XY);
                progressTab.addImage(selectedBitmap);
                return;
            }
        }

        Uri selectedData = data.getData();
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(selectedData, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 3);
            cropIntent.putExtra("aspectY", 4);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 400);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
        //controller.profilePicture.setImageURI(selectedData);
    }

    public class IconBar {

        int TAB_COUNT = 5;
        int currentTab;
        ImageView[] tabs = new ImageView[TAB_COUNT];
        int[] unselected = new int[TAB_COUNT];
        int[] selected = new int[TAB_COUNT];
        String[] tabTitles = new String[TAB_COUNT];

        public IconBar(){
            unselected[PROFILE_ID] = R.drawable.profile;
            unselected[HOME_ID] = R.drawable.home;
            unselected[WORKOUTS_ID] = R.drawable.dumbbell;
            unselected[EXERCISES_ID] = R.drawable.exercise_tab_icon;
            unselected[PROGRESS_ID] = R.drawable.weight_tab_icon;

            selected[PROFILE_ID] = R.drawable.profile_selected;
            selected[HOME_ID] = R.drawable.home_selected;
            selected[WORKOUTS_ID] = R.drawable.dumbbell_selected;
            selected[EXERCISES_ID] = R.drawable.exercise_tab_icon;
            selected[PROGRESS_ID] = R.drawable.weight_tab_icon;

            //tabs[PROFILE_ID] = findViewById(R.id.profile_icon);
            tabs[HOME_ID] = findViewById(R.id.home_button);
            tabs[WORKOUTS_ID] = findViewById(R.id.workout_icon);
            //tabs[EXERCISES_ID] = findViewById(R.id.exercise_tab_icon);
            tabs[PROGRESS_ID] = findViewById(R.id.weight_tab_icon);

            //tabs[PROFILE_ID].setOnClickListener(MainActivity.this);
            tabs[HOME_ID].setOnClickListener(MainActivity.this);
            tabs[WORKOUTS_ID].setOnClickListener(MainActivity.this);
            //tabs[EXERCISES_ID].setOnClickListener(MainActivity.this);
            tabs[PROGRESS_ID].setOnClickListener(MainActivity.this);

            tabTitles[PROFILE_ID] = " Profile";
            tabTitles[HOME_ID] = " Home";
            tabTitles[WORKOUTS_ID] = " Workouts";
            tabTitles[EXERCISES_ID] = " Exercises";
            tabTitles[PROGRESS_ID] = " Progress";

            currentTab = HOME_ID;
        }

        public void changeTab(int newTab){

            if(currentTab == newTab){
                return;
            }

            tabs[currentTab].setImageResource(unselected[currentTab]);
            currentTab = newTab;

            for(int i = 0; i < TAB_COUNT;i++){
                if(i != newTab){
                    mainLayouts[i].setVisibility(View.GONE);
                } else{
                    mainLayouts[i].setVisibility(View.VISIBLE);
                    tabs[i].setImageResource(selected[i]);
                    tabTitleTV.setText(tabTitles[i]);

                }
            }
        }

    }


}