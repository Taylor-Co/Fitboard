package fit.board.fitboard;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WorkoutTab2{

    MainActivity activity;
    LinearLayout mainLayout;

    ImageView addWorkout;
    ImageView returnIcon;
    ImageView addNewExerciseButton;
    ImageView finishAddButton;
    EditText exerciseTitleET;
    LinearLayout currentWorkouts;
    LinearLayout createWorkout;
    LinearLayout newWorkoutMain;
    NewWorkoutLayout[] newWorkoutLayouts;
    CurrentWorkoutLayout2[] workoutRows;
    int lastWorkoutID = 0;

    int newWorkoutBaseID = 1000;
    int currentWorkoutBaseID = 2000;
    int currentWorkoutHistoryBaseID = 3000;
    Boolean moving = false;
    int movingIndex;

    LinearLayout exerciseInfoList;
    LinearLayout exerciseHistoryViewMain;
    LinearLayout workoutList;

    FrameLayout activeWorkoutMain;
    TextView activeTitle;
    int activeIndex = 0;
    int activeWorkoutID;
    LinearLayout activeWorkoutList;
    ActiveExerciseLayout[] activeExercises;

    ImageView activeReturnButton;
    ImageView activeTimerButton;
    ImageView activeFinishButton;
    EditText activeWeightInput, activeRepsInput;
    ImageView activeInfoButton, activeAddFifteenButton;
    ScrollView scrollMain;
    LinearLayout greyOut, saveMain;
    Button saveYesButton, saveNoButton;
    int activeTimer = 0;
    Thread t;
    Timer timer;

    LinearLayout historyMain;
    LinearLayout historyDateScroll;
    LinearLayout historyExercisesScroll;

    TextView[] historyExercises;
    HistoryColumn[] historyColumns;

    LinearLayout historyLayoutColumns;

    ScrollView exercisesNameScroll;
    ScrollView exercisesScrollVert;
    HorizontalScrollView exercisesScrollHor;
    HorizontalScrollView historyDateScrollview;
    int historyColumnWidth;
    int historyRowHeight;
    Button historyBackButton;

    public WorkoutTab2(MainActivity activityIn){
        activity = activityIn;
        init();
    }

    public void setVisible(){
        mainLayout.setVisibility(View.VISIBLE);
    }

    private void init() {
        mainLayout = activity.findViewById(R.id.workout_main);

        addWorkout = activity.findViewById(R.id.add_workout_button);
        currentWorkouts = activity.findViewById(R.id.current_workouts);
        createWorkout = activity.findViewById(R.id.create_workout);
        addWorkout.setOnClickListener(activity);
        newWorkoutMain = activity.findViewById(R.id.new_workout_main);
        newWorkoutLayouts = new NewWorkoutLayout[1];
        newWorkoutLayouts[0] = new NewWorkoutLayout(activity.context, 0);
        newWorkoutMain.addView(newWorkoutLayouts[0], 0);
        addNewExerciseButton = activity.findViewById(R.id.addNewExercise);
        addNewExerciseButton.setOnClickListener(activity);
        returnIcon = activity.findViewById(R.id.return_icon);
        returnIcon.setOnClickListener(activity);
        exerciseTitleET = activity.findViewById(R.id.exerciseTitleTV);
        finishAddButton = activity.findViewById(R.id.finishAddButton);
        finishAddButton.setOnClickListener(activity);
        workoutList = activity.findViewById(R.id.workout_list);

        exerciseInfoList = activity.findViewById(R.id.exercise_info_list);

        exerciseHistoryViewMain = activity.findViewById(R.id.exercise_history_view_main);

        activeWorkoutMain = activity.findViewById(R.id.active_workout);
        activeTitle = activity.findViewById(R.id.active_title);
        activeWorkoutList = activity.findViewById(R.id.active_workout_list);


        activeReturnButton = activity.findViewById(R.id.active_return_icon);

        activeTimerButton = activity.findViewById(R.id.active_timer_button);
        activeWeightInput = activity.findViewById(R.id.active_weight_input);
        activeRepsInput = activity.findViewById(R.id.active_reps_input);

        activeFinishButton = activity.findViewById(R.id.active_finish_button);
        activeInfoButton = activity.findViewById(R.id.active_info_button);
        activeAddFifteenButton = activity.findViewById(R.id.active_plus_timer_button);

        saveYesButton = activity.findViewById(R.id.save_yes_button);
        saveNoButton = activity.findViewById(R.id.save_no_button);

        scrollMain = activity.findViewById(R.id.scroll_main);
        greyOut = activity.findViewById(R.id.grey_out);
        saveMain = activity.findViewById(R.id.save_main);

        activeTimerButton.setOnClickListener(activity);
        activeFinishButton.setOnClickListener(activity);
        activeReturnButton.setOnClickListener(activity);
        saveNoButton.setOnClickListener(activity);
        saveYesButton.setOnClickListener(activity);

        historyMain = activity.findViewById(R.id.history_main);
        historyDateScroll = activity.findViewById(R.id.history_date_scroll);
        historyExercisesScroll = activity.findViewById(R.id.history_exercises_scrollview);

        TextView[] historyExercises;
        HistoryColumn[] historyColumns;

        historyLayoutColumns = activity.findViewById(R.id.history_columns_layout);

        exercisesNameScroll = activity.findViewById(R.id.exercises_name_scroll);
        exercisesScrollVert = activity.findViewById(R.id.exercises_scroll_vert);
        exercisesScrollHor = activity.findViewById(R.id.exercises_scroll_hor);
        historyDateScrollview = activity.findViewById(R.id.history_date_scrollview);
        historyColumnWidth = (int)(80 * activity.density);
        historyRowHeight = (int)(100 * activity.density);

        exercisesScrollHor.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                historyDateScrollview.scrollTo(scrollX, scrollY);
            }
        });
        exercisesScrollVert.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                exercisesNameScroll.scrollTo(scrollX, scrollY);
            }
        });
        historyDateScrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                exercisesScrollHor.scrollTo(scrollX, scrollY);
            }
        });
        exercisesNameScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                exercisesScrollVert.scrollTo(scrollX, scrollY);
            }
        });

        historyBackButton = activity.findViewById(R.id.history_back_button);
        historyBackButton.setOnClickListener(activity);

        //String[] exerciseNames = {"Pushups", "Pullups"};
        //saveWorkoutPre("Chest && Back", exerciseNames);

        loadWorkouts();
    }

    boolean saveWorkout(){
        if(exerciseTitleET.getText().toString().equals("")){
            return false;
        }
        activity.dbManager.workoutsDB.execSQL("INSERT INTO WorkoutList Values(" + ++lastWorkoutID + ", '" +  exerciseTitleET.getText() +"');");
        activity.dbManager.workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS Workout" + lastWorkoutID + "(Exercise VARCHAR, Type VARCHAR, Reps INT);");

        String[] exerciseNames = new String[newWorkoutLayouts.length];

        int i = 0;
        int breakS = 0;
        while(i < newWorkoutLayouts.length){
            exerciseNames[i] = newWorkoutLayouts[i].exerciseInput.getText().toString();
            exerciseNames[i] = exerciseNames[i].trim();
            exerciseNames[i] = exerciseNames[i].toLowerCase();
            if(Integer.parseInt(newWorkoutLayouts[i].breakET.getText().toString()) < 0){
                breakS = 0;
            } else {
                breakS = Integer.parseInt(newWorkoutLayouts[i].breakET.getText().toString());
            }
            activity.dbManager.workoutsDB.execSQL("INSERT INTO Workout" + lastWorkoutID + " Values('" + newWorkoutLayouts[i].exerciseInput.getText() + "', 'Reps', " + breakS + ");" );

            i++;
        }

        Cursor exerciseCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM Workout" + lastWorkoutID, null);
        i = 0;
        exerciseCursor.moveToNext();
        String documentsSQL = "CREATE TABLE IF NOT EXISTS Workout" + lastWorkoutID + "Documents(";
        //documentsSQL = documentsSQL.concat("Date VARCHAR, ");
        documentsSQL = documentsSQL.concat("Day INT, Month INT, Year INT, ");
        while(i < exerciseCursor.getCount()){
            String columnName = "Exercise" + i + "Reps INT, Exercise" + i + "Weight INT";
            documentsSQL = documentsSQL.concat(columnName);
            if(i < exerciseCursor.getCount() - 1){
                documentsSQL = documentsSQL.concat(", ");
            }
            i++;
        }
        documentsSQL = documentsSQL.concat(");");
        Log.d("SQL", documentsSQL);
        activity.dbManager.workoutsDB.execSQL(documentsSQL);

        i = 0;
        while(i < exerciseNames.length){
            Log.d("Names", exerciseNames[i] + "www");
            i++;
        }

        //Add To ExerciseHistory

        addToExerciseDB(exerciseNames);
        activity.exerciseTab.loadExerciseInfoList();

        Toast.makeText(activity.context, "ID: " + lastWorkoutID, Toast.LENGTH_SHORT).show();
        return true;
    }

    void saveWorkoutPre(String workoutNameIn, String[] exerciseNamesIn){
        activity.dbManager.workoutsDB.execSQL("INSERT INTO WorkoutList Values(" + ++lastWorkoutID + ", '" +  workoutNameIn +"');");
        activity.dbManager.workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS Workout" + lastWorkoutID + "(Exercise VARCHAR, Type VARCHAR, Reps INT);");

        //String[] exerciseNames = new String[newWorkoutLayouts.length];

        int i = 0;
        int breakS = 0;
        while(i < exerciseNamesIn.length){
            //exerciseNames[i] = newWorkoutLayouts[i].exerciseInput.getText().toString();
            //exerciseNames[i] = exerciseNames[i].trim();
            //exerciseNames[i] = exerciseNames[i].toLowerCase();
            //if(Integer.parseInt(newWorkoutLayouts[i].breakET.getText().toString()) < 0){
                //breakS = 0;
            //} else {
                //breakS = Integer.parseInt(newWorkoutLayouts[i].breakET.getText().toString());
            //}
            activity.dbManager.workoutsDB.execSQL("INSERT INTO Workout" + lastWorkoutID + " Values('" + exerciseNamesIn[i] + "', 'Reps', " + breakS + ");" );

            i++;
        }

        //Cursor exerciseCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM Workout" + lastWorkoutID, null);
        i = 0;
        //exerciseCursor.moveToNext();
        String documentsSQL = "CREATE TABLE IF NOT EXISTS Workout" + lastWorkoutID + "Documents(";
        //documentsSQL = documentsSQL.concat("Date VARCHAR, ");
        documentsSQL = documentsSQL.concat("Day INT, Month INT, Year INT, ");
        while(i < exerciseNamesIn.length){
            String columnName = "Exercise" + i + "Reps INT, Exercise" + i + "Weight INT";
            documentsSQL = documentsSQL.concat(columnName);
            if(i < exerciseNamesIn.length - 1){
                documentsSQL = documentsSQL.concat(", ");
            }
            i++;
        }
        documentsSQL = documentsSQL.concat(");");
        Log.d("SQL", documentsSQL);
        activity.dbManager.workoutsDB.execSQL(documentsSQL);

        i = 0;
        //while(i < exerciseNames.length){
            //Log.d("Names", exerciseNames[i] + "www");
            //i++;
        //}

        //Add To ExerciseHistory

        addToExerciseDB(exerciseNamesIn);
        //activity.exerciseTab.loadExerciseInfoList();

    }

    void setCreateMode(){
        currentWorkouts.setVisibility(View.GONE);
        createWorkout.setVisibility(View.VISIBLE);
    }

    void returnClick(){
        currentWorkouts.setVisibility(View.VISIBLE);
        createWorkout.setVisibility(View.GONE);
        exerciseTitleET.setText("");
        newWorkoutLayouts = new NewWorkoutLayout[1];
        newWorkoutLayouts[0] = new NewWorkoutLayout(activity.context, 0);
        newWorkoutMain.removeAllViews();
        newWorkoutMain.addView(newWorkoutLayouts[0]);
    }

    void addExercise(int indexInput, int count, Boolean main){
        int length = newWorkoutLayouts.length;
        NewWorkoutLayout[] temp = new NewWorkoutLayout[length];
        int i = 0;
        int j = 0;
        while(i < length){
            temp[i] = new NewWorkoutLayout(activity.context, i);
            temp[i].exerciseInput.setText(newWorkoutLayouts[i].exerciseInput.getText());
            temp[i].mult = newWorkoutLayouts[i].mult;
            if(!newWorkoutLayouts[i].isMainLayer){
                temp[i].makeSubLayer();
            }
            i++;
        }

        newWorkoutLayouts = new NewWorkoutLayout[length + count];
        i = 0;
        while(i < indexInput){
            newWorkoutLayouts[i] = new NewWorkoutLayout(activity.context, i);
            if(!temp[j].isMainLayer){
                newWorkoutLayouts[i].makeSubLayer();
            }
            newWorkoutLayouts[i].mult = temp[j].mult;
            newWorkoutLayouts[i].exerciseInput.setText(temp[j++].exerciseInput.getText());

            i++;
        }

        int c = 0;
        String mainText = newWorkoutLayouts[i - 1].exerciseInput.getText().toString();
        while(c < count){
            newWorkoutLayouts[i] = new NewWorkoutLayout(activity.context, i);

            if(main == false){
                newWorkoutLayouts[i].exerciseInput.setText(mainText);
                newWorkoutLayouts[i].makeSubLayer();
            }


            i++;
            c++;
        }

        while(j < length){
            newWorkoutLayouts[i] = new NewWorkoutLayout(activity.context, i);
            if(!temp[j].isMainLayer){
                newWorkoutLayouts[i].makeSubLayer();
            }
            newWorkoutLayouts[i].mult = temp[j].mult;
            newWorkoutLayouts[i].exerciseInput.setText(temp[j++].exerciseInput.getText());

            i++;
        }

        newWorkoutMain.removeAllViews();

        i = 0;
        while(i < length + count){
            newWorkoutMain.addView(newWorkoutLayouts[i]);
            //newWorkoutLayouts[i].exerciseInput.setText(Integer.toString(newWorkoutLayouts[i].mult));
            i++;
        }


        getNewLocations();
        applyListeners();

    }

    void removeSubRows(int indexStart, int removeRowCount){
        int i = 0;
        int j = 0;
        int length = newWorkoutLayouts.length;
        NewWorkoutLayout[] temp = new NewWorkoutLayout[length];

        while(i < length){
            temp[i] = new NewWorkoutLayout(activity.context, i);
            temp[i].exerciseInput.setText(newWorkoutLayouts[i].exerciseInput.getText());
            temp[i].mult = newWorkoutLayouts[i].mult;
            if(!newWorkoutLayouts[i].isMainLayer){
                temp[i].makeSubLayer();
            }
            i++;
        }

        newWorkoutLayouts = new NewWorkoutLayout[length - removeRowCount];
        i = 0;
        while(i <= indexStart){
            newWorkoutLayouts[i] = new NewWorkoutLayout(activity.context, i);
            if(!temp[j].isMainLayer){
                newWorkoutLayouts[i].makeSubLayer();
            }
            newWorkoutLayouts[i].mult = temp[j].mult;
            newWorkoutLayouts[i].exerciseInput.setText(temp[j++].exerciseInput.getText());

            i++;
        }

        j += removeRowCount;

        while(i < length - removeRowCount){
            newWorkoutLayouts[i] = new NewWorkoutLayout(activity.context, i);
            if(!temp[j].isMainLayer){
                newWorkoutLayouts[i].makeSubLayer();
            }
            newWorkoutLayouts[i].mult = temp[j].mult;
            newWorkoutLayouts[i].exerciseInput.setText(temp[j++].exerciseInput.getText());

            i++;
        }

        newWorkoutMain.removeAllViews();

        i = 0;
        while(i < length - removeRowCount){
            newWorkoutMain.addView(newWorkoutLayouts[i]);
            //newWorkoutLayouts[i].exerciseInput.setText(Integer.toString(newWorkoutLayouts[i].mult));
            i++;
        }



        applyListeners();

    }

    void applyListeners(){
        int length = newWorkoutLayouts.length;
        int i = 0;

        while(i < length){
            int current = i;
            newWorkoutLayouts[i].moveIcon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        moving = true;
                        movingIndex = newWorkoutLayouts[current].index;
                        return true;
                    } else if(event.getAction() == MotionEvent.ACTION_UP){
                        moving = false;
                        return false;
                    } else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        //if(movingIndex == newWorkoutLayouts[current].index){
                        //int i = 0;
                        //Toast t = Toast.makeText(context, newWorkoutLayouts[0].getHeight() + " & " + newWorkoutLayouts[0].yBottom, Toast.LENGTH_SHORT);
                        //t.show();
                        if(newWorkoutLayouts[0].isInBounds((int)event.getRawY())){
                            Toast t = Toast.makeText(activity.context, "1st Bounds", Toast.LENGTH_SHORT);
                            t.show();
                        }
                        return true;

                        //}
                    }
                    return false;
                }
            });

            newWorkoutLayouts[i].exerciseInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("Debug", s.toString());
                    Log.d("Debug", Integer.toString(newWorkoutLayouts[current].mult));
                    if(newWorkoutLayouts[current].mult > 0){
                        int i = 1;
                        while(i <= newWorkoutLayouts[current].mult){
                            newWorkoutLayouts[current + i++].exerciseInput.setText(s.toString());
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            i++;
        }



            /* while(i < 1){
                int mult = newWorkoutLayouts[i].mult;
                int current = i;
                newWorkoutLayouts[0].exerciseInput.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        mainLayout.removeAllViews();
                        if(mult > 0){
                            mainLayout.removeAllViews();
                            //newWorkoutLayouts[current].exerciseInput.setText("Sub");
                        }
                        return true;
                    }
                });
                i++;

            } */
    }

    private void getNewLocations() {
        int length = newWorkoutLayouts.length;
        int i = 0;

        while(i < length){
            newWorkoutLayouts[i].getLocationOnScreen(newWorkoutLayouts[i].xy);
            newWorkoutLayouts[i].height = newWorkoutLayouts[i].getHeight();
            newWorkoutLayouts[i].yTop = newWorkoutLayouts[i].xy[1];
            newWorkoutLayouts[i].yBottom = newWorkoutLayouts[i].yTop + newWorkoutLayouts[i].height;
            i++;
        }
    }

    public void checkNewWorkoutIDs(int ID) {

        for(int i = 0; i < newWorkoutLayouts.length; i++){
            if(ID == newWorkoutBaseID + i){
                Toast t = Toast.makeText(activity.context, "fewsfws", Toast.LENGTH_SHORT);
                t.show();
                newWorkoutLayouts[i].showExtrasRow();
            }
        }
    }

    public void saveAndReset() {
        if(!saveWorkout()){
            Toast.makeText(activity.context, "Workout name must be specified" + lastWorkoutID, Toast.LENGTH_SHORT).show();
            return;
        }
        loadWorkouts();
        returnIcon.callOnClick();
        exerciseTitleET.setText("");
        newWorkoutLayouts = new NewWorkoutLayout[1];
        newWorkoutLayouts[0] = new NewWorkoutLayout(activity.context, 0);
        newWorkoutMain.removeAllViews();
        newWorkoutMain.addView(newWorkoutLayouts[0]);
    }

    void loadWorkouts() {
        workoutList.removeAllViews();
        Cursor cursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM WorkoutList", null);
        if(!cursor.moveToFirst()){
            Log.d("tag", "Here5");
            Toast.makeText(activity.context, "NULLNULL", Toast.LENGTH_LONG).show();
            String[] exerciseNames = {"Pushups", "Pushups", "Pushups", "Pushups","Pushups", "Pullups", "Pullups", "Pullups", "Pullups", "Pullups"};
            saveWorkoutPre("Chest & Back", exerciseNames);
            String[] exerciseNamesLegs = {"Squat", "Squat", "Squat", "Squat","Squat", "Calf Raises", "Calf Raises", "Calf Raises", "Calf Raises", "Calf Raises"};
            saveWorkoutPre("Legs", exerciseNamesLegs);
            cursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM WorkoutList", null);
            //return;
        }
        cursor.moveToFirst();

        int rows = cursor.getCount();
        workoutRows = new CurrentWorkoutLayout2[rows];

        for (int i = 0; i < rows; i++) {
            workoutRows[i] = new CurrentWorkoutLayout2(activity.context, i);
            workoutRows[i].workoutName.setText(cursor.getString(1));
            workoutRows[i].workoutID = cursor.getInt(0);

            workoutList.addView(workoutRows[i], i);
            lastWorkoutID = workoutRows[i].workoutID;
            cursor.moveToNext();
        }

    }

    public void checkCurrentWorkoutIDs(int ID) {
        for(int i = 0; i < workoutRows.length; i++){
            if(ID == currentWorkoutBaseID + i){
                Toast t = Toast.makeText(activity.context, "fewsfws", Toast.LENGTH_SHORT);
                t.show();
                currentWorkouts.setVisibility(View.GONE);
                activeWorkoutMain.setVisibility(View.VISIBLE);
                ////currentPage = Page.ACTIVE_WORKOUT;
                activeTitle.setText(workoutRows[i].workoutName.getText().toString());
                loadActiveWorkout(workoutRows[i].workoutID);
                activeIndex = 0;
            }
        }
    }

    void loadActiveWorkout(int workoutID){
        //Cursor activeCursor = workoutsDB.rawQuery("SELECT * FROM Workout001", null);
        activeWorkoutID = workoutID;
        activeWorkoutList.removeAllViews();
        Cursor activeCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM Workout" + workoutID, null);
        activeExercises = new ActiveExerciseLayout[activeCursor.getCount()];
        if(activeCursor.getCount() > 0){

            activeCursor.moveToFirst();
            int i = 0;
            while(i < activeCursor.getCount()){

                activeExercises[i] = new ActiveExerciseLayout(activity.context);
                if(i == 0){
                    activeExercises[i].bottomLine.setBackgroundColor(Color.parseColor("#5002ce"));
                    activeExercises[i].lineOne.setBackgroundColor(Color.parseColor("#5002ce"));
                    activeExercises[i].lineTwo.setBackgroundColor(Color.parseColor("#5002ce"));
                }
                activeExercises[i].exerciseTV.setText(activeCursor.getString(0));
                activeExercises[i].restTV.setText(Integer.toString(activeCursor.getInt(2)));
                activeWorkoutList.addView(activeExercises[i]);
                activeCursor.moveToNext();
                i++;
            }
            //activeExercises[activeIndex].setBackgroundResource(R.drawable.active_selected_background2);
        }
        activeCursor.close();

    }

    public void timerSelect() {
        activeTimerButton.setVisibility(View.GONE);
        activeFinishButton.setVisibility(View.VISIBLE);
        activeTimer = Integer.parseInt(activeExercises[activeIndex].restTV.getText().toString());
        //beginTimer(activeTimer);
        activeInfoButton.setVisibility(View.GONE);
        activeAddFifteenButton.setVisibility(View.VISIBLE);
        timer = new Timer(activeTimer);
        t = new Thread(timer);
        t.start();
    }

    public void activeFinish() {
        scrollMain.smoothScrollTo(0, activeExercises[activeIndex].getBottom());
        activeExercises[activeIndex].setAlpha((float)0.4);
        String weight = activeWeightInput.getText().toString();
        String reps = activeRepsInput.getText().toString();

        activeExercises[activeIndex].exerciseInfoTV.setText("Weight: " + weight + "   Reps: " + reps);
        activeExercises[activeIndex].repsET.setText(reps);
        activeExercises[activeIndex].weightET.setText(weight);

        if(activeIndex < activeExercises.length - 1){
            if(!activeExercises[activeIndex].exerciseTV.getText().toString().equals(activeExercises[activeIndex + 1].exerciseTV.getText().toString())){
                activeWeightInput.setText("");
            }

            activeRepsInput.setText("");

            activeIndex++;

            activeExercises[activeIndex].bottomLine.setBackgroundColor(Color.parseColor("#5002ce"));
            activeExercises[activeIndex].lineOne.setBackgroundColor(Color.parseColor("#5002ce"));
            activeExercises[activeIndex].lineTwo.setBackgroundColor(Color.parseColor("#5002ce"));
            activeTimerButton.setVisibility(View.VISIBLE);
            activeFinishButton.setVisibility(View.GONE);
            if(timer.running){
                timer.running = false;
            }
        }
        activeInfoButton.setVisibility(View.VISIBLE);
        activeAddFifteenButton.setVisibility(View.GONE);
    }

    public void activeReturn() {
        greyOut.setVisibility(View.VISIBLE);
        saveMain.setVisibility(View.VISIBLE);
        //currentWorkouts.setVisibility(View.VISIBLE);
        //activeWorkoutMain.setVisibility(View.GONE);
        //currentPage = Page.WORKOUTS;
    }

    public void noSave() {
        greyOut.setVisibility(View.GONE);
        saveMain.setVisibility(View.GONE);
        currentWorkouts.setVisibility(View.VISIBLE);
        activeWorkoutMain.setVisibility(View.GONE);
        //currentPage = Page.WORKOUTS;
    }

    public void yesSave() {
        documentWorkout();
        greyOut.setVisibility(View.GONE);
        saveMain.setVisibility(View.GONE);
        currentWorkouts.setVisibility(View.VISIBLE);
        activeWorkoutMain.setVisibility(View.GONE);
        //currentPage = Page.WORKOUTS;
    }

    void addToExerciseDB(String[] newExerciseNames){
        //ExerciseList(exerciseID INT, exercise VARCHAR)
        String[] currentExerciseNames;
        Cursor exerciseNameCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM ExerciseList", null);
        currentExerciseNames = new String[exerciseNameCursor.getCount()];
        int lastExerciseID;
        if(exerciseNameCursor.getCount() > 0) {
            exerciseNameCursor.moveToLast();
            lastExerciseID = exerciseNameCursor.getInt(0);
            exerciseNameCursor.moveToFirst();



            int i = 0;
            while (i < exerciseNameCursor.getCount()) {
                currentExerciseNames[i] = exerciseNameCursor.getString(1);
                if (i < exerciseNameCursor.getCount() - 1) {
                    exerciseNameCursor.moveToNext();
                }

                i++;
            }

        } else{
            lastExerciseID = 1;
            //workoutsDB.execSQL("INSERT INTO ExerciseList Values(1, 'pushups');");  // MUST
            //exerciseNameCursor = workoutsDB.rawQuery("SELECT * FROM ExerciseList", null);
        }

        // Blank Out Duplicates
        int i = 0;
        int j = 0;
        while(i < newExerciseNames.length){
            j = i + 1;
            while(j < newExerciseNames.length){
                if(newExerciseNames[i].equals(newExerciseNames[j])){
                    newExerciseNames[j] = " ";
                }
                j++;
            }
            i++;
        }





        if(exerciseNameCursor.getCount() > 0) {
            i = 0;
            while (i < newExerciseNames.length) {
                j = 0;
                while (j < currentExerciseNames.length) {
                    if (newExerciseNames[i].equals(currentExerciseNames[j++])) {
                        newExerciseNames[i] = " ";
                        break;
                    }

                }

                i++;
            }
        }



        i = 0;
        while(i < newExerciseNames.length){
            if(!newExerciseNames[i].equals(" ")){
                Log.d("DBProb", Integer.toString(lastExerciseID));
                activity.dbManager.workoutsDB.execSQL("INSERT INTO ExerciseList Values(" + ++lastExerciseID + ", '" + newExerciseNames[i] + "');");
                activity.dbManager.workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS Exercise" + lastExerciseID + "(Day INT, Month INT, Year INT, Reps INT, Weight INT);");
                //workoutsDB.execSQL("INSERT INTO Exercise" + lastExerciseID + " Values(1,1,1,12,155);");
                Log.d("Inserted", newExerciseNames[i]);
            }
            i++;
        }

        //loadExerciseInfoList();

    }

    public void checkCurrentWorkoutHistoryIDs(int ID) {
        for(int i = 0; i < workoutRows.length; i++){
            if(ID == currentWorkoutHistoryBaseID + i){
                //currentPage = Page.WORKOUT_HISTORY;
                currentWorkouts.setVisibility(View.GONE);
                historyMain.setVisibility(View.VISIBLE);
                loadWorkoutHistory(workoutRows[i].workoutID);
                //loadWorkoutHistory(workoutID);

            }
        }
    }

    private void documentWorkout() {
        String documentWorkout = "INSERT INTO Workout" + activeWorkoutID + "Documents Values(";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        Calendar calendarDate = Calendar.getInstance();
        int month, day, year;
        day = calendarDate.get(Calendar.DAY_OF_MONTH);
        month = calendarDate.get(Calendar.MONTH);
        year = calendarDate.get(Calendar.YEAR);
        Toast t = Toast.makeText(activity.context, date, Toast.LENGTH_LONG);
        t.show();
        //documentWorkout = documentWorkout.concat("'" + date + "', ");
        documentWorkout = documentWorkout.concat(day + ", " + month + ", " + year + ", ");
        int i = 0;
        while(i < activeExercises.length){
            int weight = -1, reps = -1;
            if(!activeExercises[i].repsET.getText().toString().equals("")){
                reps = Integer.parseInt(activeExercises[i].repsET.getText().toString());
            } else {
                reps = -1;
            }
            if(!activeExercises[i].weightET.getText().toString().equals("")){
                weight = Integer.parseInt(activeExercises[i].weightET.getText().toString());
            } else{
                weight = -1;
            }

            //weight = Integer.parseInt(activeExercises[i].weightET.getText().toString());
            documentWorkout = documentWorkout.concat(reps + ", " + weight);
            if(i < activeExercises.length - 1){
                documentWorkout = documentWorkout.concat(", ");
            }
            i++;
        }
        documentWorkout = documentWorkout.concat(");");
        Log.d("SQL", documentWorkout);
        activity.dbManager.workoutsDB.execSQL(documentWorkout);

        //// Insert into ExerciseINT DB

        i = 0;
        while(i < activeExercises.length){
            int reps, weight;
            if(activeExercises[i].exerciseTV.getText().toString().equals(" ")){
                continue;
            }
            String exerciseName = activeExercises[i].exerciseTV.getText().toString();
            if(!activeExercises[i].repsET.getText().toString().equals("")){
                reps = Integer.parseInt(activeExercises[i].repsET.getText().toString());
            } else {
                reps = -1;
            }
            if(!activeExercises[i].weightET.getText().toString().equals("")){
                weight = Integer.parseInt(activeExercises[i].weightET.getText().toString());
            } else{
                weight = -1;
            }
            if(reps == -1 && weight == -1){
                i++;
                continue;
            }
            activity.dbManager.workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS ExerciseList(exerciseID INT, exercise VARCHAR);");
            Cursor exCurs = activity.dbManager.workoutsDB.rawQuery("SELECT exerciseID FROM ExerciseList WHERE exercise = '" + exerciseName + "'", null);
            exCurs.moveToFirst();
            activity.dbManager.workoutsDB.execSQL("INSERT INTO Exercise" + exCurs.getInt(0) + " Values (" + day + ", " + month + ", " + year + ", " + reps + ", " + weight + ");");
            Log.d("Help","INSERT INTO Exercise" + exCurs.getInt(0) + " Values (" + day + ", " + month + ", " + year + ", " + reps + ", " + weight + ");");
            i++;
        }

            /* Cursor idFinder = workoutsDB.rawQuery("SELECT exerciseID FROM ExerciseList", null);
            i = 0;
            int j = 0;
            while(i < activeExercises.length){
                String currentExercise = activeExercises[i].exerciseTV.getText().toString();
                // workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS Exercise" + lastWorkoutID + "(Day INT, Month INT, Year INT, Reps INT, Weight INT);");
                while(j < idFinder.getCount()){
                   // if(currentExercise == idFinder.getString(1)){
                       // workoutsDB.execSQL("INSERT INTO Exercise" + idFinder.getInt(0) + " Values(" + day + ", " + month + ", " + year + ", " + activeExercises[i].repsET.getText().toString() + ", " + activeExercises[i].weightET.getText().toString() + ");");

                    //}
                    j++;
                }
                i++;
            } */
        //documentWorkout = documentWorkout.concat(day + ", " + month + ", " + year + ", ");
    }

    void loadWorkoutHistory(int workoutID){
        historyLayoutColumns.removeAllViews();
        historyDateScroll.removeAllViews();
        historyExercisesScroll.removeAllViews();
        Cursor historyCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM WORKOUT" + workoutID +  "Documents", null);
        if(historyCursor.getCount() < 1){
            Toast t = Toast.makeText(activity.context, "Empty", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        historyCursor.moveToFirst();
        Toast t = Toast.makeText(activity.context, historyCursor.getString(0), Toast.LENGTH_LONG);
        t.show();

        Drawable titleBackground2 = activity.getResources().getDrawable(R.drawable.title_background, null);
        // Dates Scrollview
        TextView[] historyDates = new TextView[historyCursor.getCount()];

        int i = 0;
        ViewGroup.LayoutParams historyDatesParams = new ViewGroup.LayoutParams(historyColumnWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams lineParams = new ViewGroup.LayoutParams( (int) (5 * activity.density), ViewGroup.LayoutParams.MATCH_PARENT);
        while(i < historyCursor.getCount()){
            historyDates[i] = new TextView(activity.context);
            //historyDates[i].setText(historyCursor.getString(2));
            historyDates[i].setGravity(Gravity.CENTER);
            historyDates[i].setTextColor(Color.parseColor("#f6f6f6"));
            historyDates[i].setTypeface(null, Typeface.BOLD);
            historyDates[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
           // historyDates[i].setBackground(titleBackground2);

            String dateString = "";
            switch (historyCursor.getInt(1)){
                case 0:
                    dateString = "Jan ";
                    break;
                case 1:
                    dateString = "Feb ";
                    break;
                case 2:
                    dateString = "Mar ";
                    break;
                case 3:
                    dateString = "Apr ";
                    break;
                case 4:
                    dateString = "May ";
                    break;
                case 5:
                    dateString = "Jun ";
                    break;
                case 6:
                    dateString = "Jul ";
                    break;
                case 7:
                    dateString = "Aug ";
                    break;
                case 8:
                    dateString = "Sep ";
                    break;
                case 9:
                    dateString = "Oct ";
                    break;
                case 10:
                    dateString = "Nov ";
                    break;
                case 11:
                    dateString = "Dec ";
                    break;

            }
            dateString = dateString.concat(historyCursor.getString(0));

            historyDates[i].setText(dateString);

            historyDates[i].setLayoutParams(historyDatesParams);
            historyCursor.moveToNext();
            View line = new View(activity.context);

            line.setBackgroundColor(Color.parseColor("#f6f6f6"));
            line.setLayoutParams(lineParams);

            historyDateScroll.addView(historyDates[i]);
            historyDateScroll.addView(line);
            i++;
        }

        Cursor exercisesCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM Workout" + workoutID, null);
        historyExercises = new TextView[exercisesCursor.getCount()];
        exercisesCursor.moveToFirst();
        i = 0;

        // History Exercises Scroll
        ViewGroup.LayoutParams historyExercisesParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(100 * activity.density));
        ViewGroup.LayoutParams lineParamsVert = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(5 * activity.density));
        Drawable titleBackground = activity.getResources().getDrawable(R.drawable.title_background, null);
        while(i < historyExercises.length){
            historyExercises[i] = new TextView(activity.context);
            historyExercises[i].setText(exercisesCursor.getString(0));
            historyExercises[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            historyExercises[i].setTypeface(null, Typeface.BOLD);
            historyExercises[i].setGravity(Gravity.CENTER);
            //historyExercises[i].setBackground(titleBackground);
            historyExercises[i].setTextColor(Color.parseColor("#f6f6f6"));

            historyExercises[i].setLayoutParams(historyExercisesParams);

            View line = new View(activity.context);

            line.setBackgroundColor(Color.parseColor("#f6f6f6"));
            line.setLayoutParams(lineParamsVert);

            historyExercisesScroll.addView(historyExercises[i]);
            historyExercisesScroll.addView(line);
            exercisesCursor.moveToNext();
            i++;
        }


        loadHistory(workoutID);
        //historyColumns = new HistoryColumn[1];

    }

    void loadHistory(int workoutID){

        Cursor historyCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM Workout" + workoutID + "Documents", null);
        historyCursor.moveToFirst();
        historyColumns = new HistoryColumn[historyCursor.getCount()];
        int i = 0;
        ViewGroup.LayoutParams spaceParams = new ViewGroup.LayoutParams((int)(5 * activity.density), ViewGroup.LayoutParams.MATCH_PARENT);
        while(i < historyColumns.length){
            historyColumns[i] = new HistoryColumn(activity.context);
            historyColumns[i].setExercises(historyCursor);
            View space = new View(activity.context);
            space.setBackgroundColor(Color.parseColor("#f6f6f6"));

            space.setLayoutParams(spaceParams);
            historyLayoutColumns.addView(historyColumns[i]);
            historyLayoutColumns.addView(space);
            if(i == historyColumns.length - 1){
                break;
            }
            historyCursor.moveToNext();
            i++;
        }

    }

    public void returnFromHistory() {
        currentWorkouts.setVisibility(View.VISIBLE);
        historyMain.setVisibility(View.GONE);
    }

    class CurrentWorkoutLayout2 extends LinearLayout {

        int workoutID;
        TextView workoutName;
        int index;

        public CurrentWorkoutLayout2(Context context, int indexIn) {
            super(context);

            index = indexIn;

            //Set Layout Params
            setOrientation(HORIZONTAL);

            LayoutParams CWLParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
            CWLParams.height = (int) (60 * activity.density);
            CWLParams.bottomMargin = (int) (20 * activity.density);
            setLayoutParams(CWLParams);

            //Add Workout Name View
            workoutName = new TextView(context);

            LayoutParams workoutNameParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);
            workoutName.setLayoutParams(workoutNameParams);

            //workoutName.setText("Chest & Back");
            workoutName.setId(currentWorkoutHistoryBaseID + index);
            workoutName.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            workoutName.setTextColor(Color.parseColor("#f6f6f6"));
            workoutName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            workoutName.setGravity(TEXT_ALIGNMENT_CENTER);
            //workoutName.setBackground(activity.getDrawable(R.drawable.title_background));
            workoutName.setOnClickListener(activity);

            //Edit Button
            ImageView editIV = new ImageView(context);
            editIV.setPadding(5,5,5,5);

            LayoutParams editIVParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            editIV.setLayoutParams(editIVParams);

            editIV.setImageResource(R.drawable.edit_purple);

            //Center Layout
            LinearLayout centerLL = new LinearLayout(context);

            LayoutParams centerLLParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5);
            centerLL.setLayoutParams(centerLLParams);
            centerLL.setOrientation(VERTICAL);

            TextView infoRow = new TextView(context);
            LayoutParams infoRowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            infoRow.setLayoutParams(infoRowParams);

            infoRow.setText("24 Sets    6 Exercises");
            infoRow.setTextColor(Color.parseColor("#f6f6f6"));
            infoRow.setTextAlignment(TEXT_ALIGNMENT_CENTER);

            centerLL.addView(workoutName);
            centerLL.addView(infoRow);

            //Play Button
            ImageView playIV = new ImageView(context);
            playIV.setPadding(5,5,5,5);

            LayoutParams playIVParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            playIV.setLayoutParams(playIVParams);

            playIV.setImageResource(R.drawable.play_blue);
            playIV.setId(currentWorkoutBaseID + index);
            playIV.setOnClickListener(activity);

            addView(editIV);
            addView(centerLL);
            addView(playIV);
        }


    }

    class CurrentWorkoutLayout extends LinearLayout {

        int workoutID;
        TextView workoutName;
        int index;

        public CurrentWorkoutLayout(Context context, int indexIn) {
            super(context);

            index = indexIn;

            //Set Layout Params
            setOrientation(HORIZONTAL);

            LayoutParams CWLParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
            CWLParams.height = (int) (50 * activity.density);
            CWLParams.bottomMargin = (int) (20 * activity.density);
            setLayoutParams(CWLParams);

            //Add Workout Name View
            workoutName = new TextView(context);

            //workoutName.setText("Chest & Back");
            workoutName.setId(currentWorkoutHistoryBaseID + index);
            workoutName.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            workoutName.setTextColor(Color.BLACK);
            workoutName.setTypeface(null, Typeface.BOLD);
            workoutName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            workoutName.setGravity(TEXT_ALIGNMENT_CENTER);
            workoutName.setBackground(activity.getDrawable(R.drawable.title_background));
            workoutName.setOnClickListener(activity);

            /*workoutName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage = Page.WORKOUT_HISTORY;
                    currentWorkouts.setVisibility(GONE);
                    historyMain.setVisibility(VISIBLE);
                    loadWorkoutHistory(workoutID);
                    //loadWorkoutHistory(workoutID);
                }
            }); */

            LayoutParams workoutNameParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            workoutNameParams.weight = 40;

            workoutName.setLayoutParams(workoutNameParams);

            //Space
            Space space1 = new Space(context);
            LayoutParams spaceParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            spaceParams.weight = 2;
            space1.setLayoutParams(spaceParams);

            //Edit Button
            ImageView editButton = new ImageView(context);
            editButton.setImageResource(R.drawable.edit);
            editButton.setPadding((int) (5 * activity.density), (int) (5 * activity.density), (int) (5 * activity.density), (int) (5 * activity.density));
            LayoutParams editButtonParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            editButtonParams.weight = 9;
            editButton.setLayoutParams(editButtonParams);

            //Space
            Space space2 = new Space(context);
            LayoutParams space2Params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            space2Params.weight = 2;
            space2.setLayoutParams(space2Params);

            //Start Button
            ImageView startButton = new ImageView(context);
            startButton.setImageResource(R.drawable.play);
            startButton.setPadding((int) (5 * activity.density), (int) (5 * activity.density), (int) (5 * activity.density), (int) (5 * activity.density));
            LayoutParams startButtonParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            startButtonParams.weight = 9;
            startButton.setLayoutParams(editButtonParams);
            startButton.setId(currentWorkoutBaseID + index);
            startButton.setOnClickListener(activity);

            addView(workoutName, 0);
            addView(space1, 1);
            addView(editButton, 2);
            addView(space2, 3);
            addView(startButton, 4);
        }


    }

    class NewWorkoutLayout extends LinearLayout {

        Boolean hidden = true;
        Boolean isMainLayer = true;

        LinearLayout mainRow;
        LayoutParams mainRowParams;

        EditText exerciseInput;
        LinearLayout multColumn;
        EditText breakET;
        ImageView moveIcon;
        ImageView extrasButton;
        LinearLayout extrasRow;
        EditText multET;

        int[] xy = new int[2];
        int height;
        int yTop = 0;
        int yBottom = 0;

        int index;
        int mult = 0;

        public NewWorkoutLayout(Context context, int indexIn) {
            super(context);

            index = indexIn;

            //Set Main Layout Params
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setOrientation(VERTICAL);
            setLayoutParams(params);

            //Main New Workout Row
            mainRow = new LinearLayout(context);
            mainRowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (60 * getContext().getResources().getDisplayMetrics().density));
            mainRow.setOrientation(HORIZONTAL);
            mainRow.setLayoutParams(mainRowParams);

            //Extras Button
            extrasButton = new ImageView(context);
            extrasButton.setId(newWorkoutBaseID + index);
            extrasButton.setOnClickListener(activity);
            extrasButton.setImageResource(R.drawable.extras_button_show);
            extrasButton.setPadding((int) (10 * getContext().getResources().getDisplayMetrics().density), (int) (10 * getContext().getResources().getDisplayMetrics().density), (int) (10 * getContext().getResources().getDisplayMetrics().density), (int) (10 * getContext().getResources().getDisplayMetrics().density));
            LayoutParams extrasButtonParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            extrasButtonParams.weight = 20;
            extrasButton.setLayoutParams(extrasButtonParams);


            //Exercise Name Input
            exerciseInput = new EditText(context);
            exerciseInput.setTextColor(Color.parseColor("#f6f6f6"));
            exerciseInput.setHint("Exercise");
            exerciseInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
            exerciseInput.setSingleLine();
            exerciseInput.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            exerciseInput.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            exerciseInput.setHintTextColor(Color.parseColor("#6f6f6f"));
            LayoutParams exerciseInputParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            exerciseInputParams.weight = 60;
            exerciseInput.setLayoutParams(exerciseInputParams);


            //Move Icon
            moveIcon = new ImageView(context);
            moveIcon.setImageResource(R.drawable.move);
            moveIcon.setPadding((int) (10 * getContext().getResources().getDisplayMetrics().density), (int) (10 * getContext().getResources().getDisplayMetrics().density), (int) (10 * getContext().getResources().getDisplayMetrics().density), (int) (10 * getContext().getResources().getDisplayMetrics().density));
            LayoutParams moveIconParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            moveIconParams.weight = 20;
            moveIcon.setLayoutParams(moveIconParams);

            mainRow.addView(extrasButton);
            mainRow.addView(exerciseInput);
            mainRow.addView(moveIcon);

            //Extras Row
            extrasRow = new LinearLayout(context);
            extrasRow.setOrientation(HORIZONTAL);
            extrasRow.setId(index * 10);
            LayoutParams extrasRowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (80 * getContext().getResources().getDisplayMetrics().density));
            extrasRow.setLayoutParams(extrasRowParams);
            extrasRow.setVisibility(GONE);

            //Type Column
            LinearLayout typeColumn = new LinearLayout(context);
            typeColumn.setOrientation(VERTICAL);
            LayoutParams typeColumnParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            typeColumnParams.weight = 1;
            typeColumnParams.setMargins((int) (20 * getContext().getResources().getDisplayMetrics().density), 0, (int) (20 * getContext().getResources().getDisplayMetrics().density), 0);
            typeColumn.setLayoutParams(typeColumnParams);

            //Type Textview
            TextView typeTV = new TextView(context);
            typeTV.setTextColor(Color.parseColor("#f6f6f6"));
            typeTV.setText("Type");
            typeTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            typeTV.setTypeface(null, Typeface.BOLD);
            typeTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            LayoutParams typeTVParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            typeTVParams.weight = 1;
            typeTV.setLayoutParams(typeTVParams);

            //Type Dropdown
            Spinner typeDropdown = new Spinner(context);
            typeDropdown.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            typeDropdown.setPrompt("Reps");
            LayoutParams typeDropdownParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            typeDropdownParams.weight = 2;
            typeDropdown.setLayoutParams(typeDropdownParams);

            //Complete Type Column
            typeColumn.addView(typeTV);
            typeColumn.addView(typeDropdown);

            //Break Column
            LinearLayout breakColumn = new LinearLayout(context);
            breakColumn.setOrientation(VERTICAL);
            LayoutParams breakColumnParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            breakColumnParams.weight = 1;
            breakColumnParams.setMargins((int) (20 * getContext().getResources().getDisplayMetrics().density), 0, (int) (20 * getContext().getResources().getDisplayMetrics().density), 0);
            breakColumn.setLayoutParams(breakColumnParams);

            //Break Textview
            TextView breakTV = new TextView(context);
            breakTV.setText("Break");
            breakTV.setTextColor(Color.parseColor("#f6f6f6"));
            breakTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            breakTV.setTypeface(null, Typeface.BOLD);
            breakTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            LayoutParams breakTVParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            breakTVParams.weight = 1;
            breakTV.setLayoutParams(breakTVParams);

            //Break EditText
            breakET = new EditText(context);
            breakET.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            breakET.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            breakET.setTextColor(Color.parseColor("#f6f6f6"));
            breakET.setText("120");
            breakET.setSingleLine();
            breakET.setImeOptions(EditorInfo.IME_ACTION_DONE);
            //breakET.setHint("∞");
            breakET.setTypeface(null, Typeface.BOLD);
            LayoutParams breakETParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            breakETParams.weight = 2;
            breakET.setLayoutParams(breakETParams);

            //Complete Break Column

            breakColumn.addView(breakTV);
            breakColumn.addView(breakET);

            //Mult Column
            multColumn = new LinearLayout(context);
            multColumn.setOrientation(VERTICAL);
            LayoutParams multColumnParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            multColumnParams.weight = 1;
            multColumnParams.setMargins((int) (20 * getContext().getResources().getDisplayMetrics().density), 0, (int) (20 * getContext().getResources().getDisplayMetrics().density), 0);
            multColumn.setLayoutParams(multColumnParams);

            //Mult Textview
            TextView multTV = new TextView(context);
            multTV.setText("Mult");
            multTV.setTextColor(Color.parseColor("#f6f6f6"));
            multTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            multTV.setTypeface(null, Typeface.BOLD);
            multTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            LayoutParams multTVParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            multTVParams.weight = 1;
            multTV.setLayoutParams(multTVParams);

            //Break EditText
            multET = new EditText(context);
            multET.setTextColor(Color.parseColor("#f6f6f6"));
            multET.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            multET.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            multET.setInputType(InputType.TYPE_CLASS_NUMBER);
            multET.setHint("1");
            multET.setSingleLine();
            multET.setImeOptions(EditorInfo.IME_ACTION_DONE);
            multET.setTypeface(null, Typeface.BOLD);
            LayoutParams multETParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            multETParams.weight = 2;
            multET.setLayoutParams(multETParams);

            //Complete Mult Column
            multColumn.addView(multTV);
            multColumn.addView(multET);


            extrasRow.addView(typeColumn);
            extrasRow.addView(breakColumn);
            extrasRow.addView(multColumn);


            addView(mainRow);
            addView(extrasRow);
        }

        void showExtrasRow(){
            if (hidden == true) {  //If extra row is hidden
                extrasRow.setVisibility(VISIBLE);
                hidden = false;
                extrasButton.setImageResource(R.drawable.extras_button_hide);
            } else {  //If extra row is visible
                extrasRow.setVisibility(GONE);
                hidden = true;
                if(!multET.getText().toString().trim().isEmpty()) {

                    int newRowCount = Integer.parseInt(multET.getText().toString());

                    //addExercise(index + 1, newRowCount);

                    if (newRowCount > mult) {

                        int addRowCount = newRowCount - mult;
                        int lastMult = mult;

                        mult = newRowCount;

                        addExercise(index + 1, addRowCount, false);

                    } else if(newRowCount < mult){
                        int removeRowCount = mult - newRowCount;
                        mult = newRowCount;
                        removeSubRows(index, removeRowCount);
                    }

                }
                extrasButton.setImageResource(R.drawable.extras_button_show);
            }
        }

        Boolean isInBounds(int y){
            int ybottom1 = yTop + getHeight();
            if(y > yTop && y < ybottom1){
                return true;
            } else{
                return false;
            }
        }

        void makeSubLayer(){
            mainRowParams.height *= .8;
            mainRowParams.setMargins((int) (30 * getContext().getResources().getDisplayMetrics().density), 0, (int) (30 * getContext().getResources().getDisplayMetrics().density), 0);
            exerciseInput.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            multColumn.setVisibility(INVISIBLE);
            isMainLayer = false;
            exerciseInput.setFocusable(false);
            //exerciseInput.setText(Integer.toString(index));
        }

    }

    class ActiveExerciseLayout extends LinearLayout {

        TextView exerciseTV;
        TextView restTV;
        TextView exerciseInfoTV;
        EditText repsET;
        EditText weightET;
        View bottomLine;
        View lineOne, lineTwo;

        public ActiveExerciseLayout(Context context) {
            super(context);



            setOrientation(VERTICAL);
            //setBackground(activity.getDrawable(R.drawable.title_background));
            LayoutParams mainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(80 * getResources().getDisplayMetrics().density));
            mainParams.setMargins((int)(10 * getResources().getDisplayMetrics().density), (int)(10 * getResources().getDisplayMetrics().density), (int)(10 * getResources().getDisplayMetrics().density), 0);
            setLayoutParams(mainParams);

            LayoutParams horiMainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 99);

            LinearLayout mainLayout = new LinearLayout(context);
            mainLayout.setOrientation(HORIZONTAL);
            mainLayout.setLayoutParams(horiMainParams);

            //Infobutton Layout
            ImageView infoButton = new ImageView(context);
            infoButton.setImageResource(R.drawable.info_icon_purp);
            infoButton.setPadding((int)(5 * getResources().getDisplayMetrics().density), (int)(10 * getResources().getDisplayMetrics().density), (int)(10 * getResources().getDisplayMetrics().density), (int)(10 * getResources().getDisplayMetrics().density));
            LayoutParams infoButtonParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            infoButtonParams.weight = 20;
            infoButton.setLayoutParams(infoButtonParams);

            lineOne = new View(context);
            LayoutParams lineParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lineParams.setMargins(0,(int)(10 * getResources().getDisplayMetrics().density), 0, (int)(10 * getResources().getDisplayMetrics().density));
            lineOne.setLayoutParams(lineParams);
            lineOne.setBackgroundColor(Color.parseColor("#f6f6f6"));

            //Center Layout
            LinearLayout centerLayout = new LinearLayout(context);
            centerLayout.setOrientation(VERTICAL);
            LayoutParams centerLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            centerLayoutParams.weight = 80;
            centerLayoutParams.setMargins((int)(10 * getResources().getDisplayMetrics().density), 0, (int)(10 * getResources().getDisplayMetrics().density), 0);
            centerLayout.setLayoutParams(centerLayoutParams);

            //Space space1 = new Space(context);
            //LayoutParams space1Params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            //space1.setLayoutParams(space1Params);

            lineTwo = new View(context);
            lineTwo.setLayoutParams(lineParams);
            lineTwo.setBackgroundColor(Color.parseColor("#f6f6f6"));

            // Exercise Name And Stats
            exerciseTV = new TextView(context);
            exerciseTV.setText("Trap Pulls");
            exerciseTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            exerciseTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            exerciseTV.setTextColor(Color.parseColor("#f6f6f6"));
            LayoutParams exerciseTVParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4);
            exerciseTV.setLayoutParams(exerciseTVParams);

            // Exercise Info
            exerciseInfoTV = new TextView(context);
            exerciseInfoTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            exerciseInfoTV.setTextColor(Color.parseColor("#f6f6f6"));
            exerciseInfoTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            LayoutParams exerciseInfoTVParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);

            exerciseInfoTV.setLayoutParams(exerciseInfoTVParams);

            LinearLayout exerciseInfo = new LinearLayout(context);
            exerciseInfo.setOrientation(HORIZONTAL);
            exerciseInfo.setGravity(Gravity.CENTER);
            LayoutParams exerciseInfoParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4);
            exerciseInfo.setLayoutParams(exerciseInfoParams);

            TextView weightTV = new TextView(context);
            weightTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            weightTV.setTextColor(Color.parseColor("#f6f6f6"));
            weightTV.setGravity(Gravity.CENTER);
            weightTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            weightTV.setText("Weight: ");
            LayoutParams weightTVParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            weightTV.setLayoutParams(weightTVParams);

            weightET = new EditText(context);
            weightET.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            weightET.setTextColor(Color.parseColor("#f6f6f6"));
            weightET.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            weightET.setText("");
            weightET.setEnabled(false);
            weightET.setPadding((int)(10 * getResources().getDisplayMetrics().density),0,(int)(10 * getResources().getDisplayMetrics().density),0);
            LayoutParams weightETParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            weightET.setLayoutParams(weightETParams);

            TextView repsTV = new TextView(context);
            repsTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            repsTV.setTextColor(Color.parseColor("#f6f6f6"));
            repsTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            repsTV.setText("Reps");
            repsTV.setGravity(Gravity.CENTER);
            LayoutParams repsTVParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            repsTV.setLayoutParams(repsTVParams);

            repsET = new EditText(context);
            repsET.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            repsET.setTextColor(Color.parseColor("#f6f6f6"));
            repsET.setEnabled(false);
            repsET.setPadding((int)(10 * getResources().getDisplayMetrics().density),0,(int)(10 * getResources().getDisplayMetrics().density),0);
            repsET.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            repsET.setText("");
            LayoutParams repsETParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            repsET.setLayoutParams(repsETParams);

            exerciseInfo.addView(weightTV);
            exerciseInfo.addView(weightET);
            exerciseInfo.addView(repsTV);
            exerciseInfo.addView(repsET);

            // Complete Center Layout
            //centerLayout.addView(space1);
            centerLayout.addView(exerciseTV);
            centerLayout.addView(exerciseInfo);

            // Rest Layout
            LinearLayout restLayout = new LinearLayout(context);
            restLayout.setOrientation(VERTICAL);
            LayoutParams restLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 20);
            restLayout.setLayoutParams(restLayoutParams);


            //Timer Icon
            ImageView activeTimerIcon = new ImageView(context);
            activeTimerIcon.setPadding((int)(5 * getResources().getDisplayMetrics().density), (int)(5 * getResources().getDisplayMetrics().density),(int)(5 * getResources().getDisplayMetrics().density), (int)(5 * getResources().getDisplayMetrics().density));
            activeTimerIcon.setImageResource(R.drawable.timer_icon_blue);
            LayoutParams activeTimerIconParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3);
            activeTimerIcon.setLayoutParams(activeTimerIconParams);

            restTV = new TextView(context);
            restTV.setText("120");
            restTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            restTV.setTypeface(null, Typeface.BOLD);
            restTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            restTV.setTextColor(Color.parseColor("#f6f6f6"));
            LayoutParams restTVParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);
            restTV.setLayoutParams(restTVParams);

            restLayout.addView(activeTimerIcon);
            restLayout.addView(restTV);

            bottomLine = new View(context);
            LayoutParams bottomLineLP = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            bottomLineLP.setMargins((int) (10 * getResources().getDisplayMetrics().density), 0, (int) (10 * getResources().getDisplayMetrics().density),0);
            bottomLine.setLayoutParams(bottomLineLP);
            bottomLine.setBackgroundColor(Color.parseColor("#f6f6f6"));

            addView(mainLayout);
            addView(bottomLine);

            // Add Views To Main
            mainLayout.addView(infoButton);
            mainLayout.addView(lineOne);
            mainLayout.addView(centerLayout);
            mainLayout.addView(lineTwo);
            mainLayout.addView(restLayout);


        }
    }

    class Timer implements Runnable{

        int time;
        Boolean running = false;

        public Timer(int timeIn){
            time = timeIn;
        }

        @Override
        public void run() {
            running = true;
            while(time > 0 && running == true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(running == true){
                    activeExercises[activeIndex].restTV.setText(Integer.toString(--time));
                }

            }
        }
    }

    class HistoryColumn extends LinearLayout{

        BoxLayout[] exercises;
        LayoutParams ETParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 50);
        LayoutParams boxParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, historyRowHeight);


        public HistoryColumn(Context context) {
            super(context);
            setOrientation(VERTICAL);
            LayoutParams mainParams = new LayoutParams(historyColumnWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(mainParams);


        }

        void setExercises(Cursor columnCursor){
            exercises = new BoxLayout[(columnCursor.getColumnCount() - 3) / 2];
            int i = 0;
            int currentColumn = 3;
            LayoutParams spaceParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(5 * activity.density));
            while(i < exercises.length){
                exercises[i] = new BoxLayout(activity.context);
                exercises[i].repsET.setText("x" + columnCursor.getString(currentColumn++));
                //exercises[i].repsET.setBackgroundColor(Color.LTGRAY);
                exercises[i].repsET.setTextColor(Color.parseColor("#f6f6f6"));
                exercises[i].weightET.setText(columnCursor.getString(currentColumn++));
                exercises[i].weightET.setTextColor(Color.parseColor("#f6f6f6"));
                addView(exercises[i]);
                View space = new View(activity.context);

                space.setLayoutParams(spaceParams);
                space.setBackgroundColor(Color.parseColor("#f6f6f6"));
                addView(space);
                //TextView t = new TextView(context);
                //t.setText("Surp");
                //addView(t);
                i++;
            }

        }


        class BoxLayout extends LinearLayout{

            EditText repsET;
            EditText weightET;

            public BoxLayout(Context context) {
                super(context);

                setOrientation(VERTICAL);

                setLayoutParams(boxParams);

                repsET = new EditText(context);
                repsET.setText("Reps");
                repsET.setTextColor(Color.BLACK);
                repsET.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                repsET.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                repsET.setLayoutParams(ETParams);

                weightET = new EditText(context);
                weightET.setText("Weight");
                weightET.setTextColor(Color.BLACK);
                weightET.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                weightET.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                weightET.setLayoutParams(ETParams);

                addView(repsET);
                addView(weightET);
            }
        }


    }
}
