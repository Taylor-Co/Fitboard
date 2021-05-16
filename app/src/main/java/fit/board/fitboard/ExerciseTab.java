package fit.board.fitboard;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ExerciseTab {

    MainActivity activity;

    LinearLayout mainLayout;
    LinearLayout exerciseInfoList;
    LinearLayout exerciseHistoryViewMain;
    LinearLayout exerciseHistoryScroll;

    ScrollView exerciseListScroll;
    Button exerciseViewReturnButton;



    public ExerciseTab(MainActivity activityIn){
        activity = activityIn;
        mainLayout = activity.findViewById(R.id.exercise_info_main);
        exerciseInfoList = activity.findViewById(R.id.exercise_info_list);
        exerciseHistoryViewMain = activity.findViewById(R.id.exercise_history_view_main);
        exerciseHistoryScroll = activity.findViewById(R.id.exercise_history_scroll);
        exerciseListScroll = activity.findViewById(R.id.exercise_tab_history_list_scroll);
        exerciseViewReturnButton = activity.findViewById(R.id.back_exercise_history_button);
        exerciseViewReturnButton.setOnClickListener(activity);


        loadExerciseInfoList();

    }

    void loadExerciseHistory(int exerciseID) {

        exerciseHistoryScroll.removeAllViews();


        //workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS Exercise" + lastWorkoutID + "(Day INT, Month INT, Year INT, Reps INT, Weight INT);");
        Cursor exerciseInfoCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM Exercise" + exerciseID, null);
        if(exerciseInfoCursor.getCount() < 1){
            return;
        }

        int rowCount = exerciseInfoCursor.getCount() / 3;
        int extras = exerciseInfoCursor.getCount() % 3;
        if(extras > 0){
            rowCount += 1;
        }


        ExerciseInfoLayout[] exerciseRows = new ExerciseInfoLayout[rowCount];
        exerciseInfoCursor.moveToFirst();


        int i = 0;
        int currentExercise = 0;
        while(i < rowCount){
            currentExercise = 0;
            exerciseRows[i] = new ExerciseInfoLayout(activity.context);
            while(currentExercise < 3){
                exerciseRows[i].views[currentExercise].weightView.setText(exerciseInfoCursor.getString(4) + " Lbs");
                exerciseRows[i].views[currentExercise].repsView.setText("x" + exerciseInfoCursor.getString(3));
                if(!exerciseInfoCursor.moveToNext()){
                    while(currentExercise < 2){
                        exerciseRows[i].views[++currentExercise].setVisibility(View.INVISIBLE);
                    }
                    break;
                }

                currentExercise++;
            }
            exerciseHistoryScroll.addView(exerciseRows[i]);
            i++;
        }

    }

    void loadExerciseInfoList() {
        exerciseInfoList.removeAllViews();
        Cursor exerciseCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM ExerciseList", null);
        exerciseCursor.moveToFirst();
        if(exerciseCursor.getCount() < 1){
            return;
        }
        int i = 0;
        while(i < exerciseCursor.getCount()){
            int exerciseID = exerciseCursor.getInt(0);

            TextView exercise = new TextView(activity.context);
            exercise.setBackgroundResource(R.drawable.title_background);
            exercise.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams exerciseParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(60 * activity.density));
            exerciseParams.setMargins(0,0,0,(int)(10 * activity.density));
            exercise.setLayoutParams(exerciseParams);
            exercise.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            exercise.setTextColor(Color.BLACK);
            exercise.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            exercise.setText(exerciseCursor.getString(1));
            exercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exerciseListScroll.setVisibility(View.GONE);
                    exerciseHistoryViewMain.setVisibility(View.VISIBLE);
                    loadExerciseHistory(exerciseID);
                }
            });

            exerciseInfoList.addView(exercise);
            if(i == exerciseCursor.getCount() - 1){
                break;
            }
            exerciseCursor.moveToNext();
            i++;
        }
    }

    public void returnToList() {
        exerciseListScroll.setVisibility(View.VISIBLE);
        exerciseHistoryViewMain.setVisibility(View.GONE);
    }

    class ExerciseInfoLayout extends LinearLayout{

        ExerciseInfoView[] views;

        public ExerciseInfoLayout(Context context) {
            super(context);

            setOrientation(HORIZONTAL);
            setPadding((int)(5 * activity.density), (int)(5 * activity.density), (int)(5 * activity.density), (int)(5 * activity.density));
            //setBackgroundResource(R.drawable.title_background);
            ViewGroup.LayoutParams mainParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (80 * activity.density));
            setLayoutParams(mainParams);

            views = new ExerciseInfoView[3];
            views[0] = new ExerciseInfoView(context);
            views[0].mainParams.setMargins(0, 0, (int)(15 * activity.density), 0);
            views[1] = new ExerciseInfoView(context);
            views[2] = new ExerciseInfoView(context);
            views[2].mainParams.setMargins((int)(15 * activity.density), 0, 0, 0);
            addView(views[0]);
            addView(views[1]);
            addView(views[2]);
        }

        class ExerciseInfoView extends LinearLayout{

            LayoutParams mainParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            TextView dateView = new TextView(activity.context);
            TextView weightView = new TextView(activity.context);
            TextView repsView = new TextView(activity.context);


            public ExerciseInfoView(Context context) {
                super(context);

                setOrientation(VERTICAL);
                setBackgroundResource(R.drawable.title_background);
                setPadding((int)(10 * activity.density), (int)(10 * activity.density), (int)(10 * activity.density), (int)(10 * activity.density));
                mainParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
                setLayoutParams(mainParams);

                dateView = new TextView(context);
                dateView.setText("Jan 15, 2021");
                dateView.setTextColor(Color.BLACK);
                dateView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                dateView.setTypeface(null, Typeface.BOLD);
                //dateView.setBackgroundColor(Color.BLACK);
                LayoutParams dateViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
                dateView.setLayoutParams(dateViewParams);

                LinearLayout weightRepsLayout = new LinearLayout(context);
                weightRepsLayout.setOrientation(HORIZONTAL);
                LayoutParams weightRepsLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
                weightRepsLayout.setLayoutParams(weightRepsLayoutParams);

                weightView = new TextView(context);
                weightView.setText("185 Lbs");
                weightView.setTextColor(Color.BLACK);
                weightView.setTypeface(null, Typeface.BOLD);
                weightView.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                LayoutParams weightViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 2);
                weightView.setLayoutParams(weightViewParams);

                repsView = new TextView(context);
                repsView.setText("x1");
                repsView.setTextColor(Color.BLACK);
                repsView.setTypeface(null, Typeface.BOLD);
                repsView.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
                LayoutParams repsViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
                repsView.setLayoutParams(repsViewParams);

                weightRepsLayout.addView(weightView);
                weightRepsLayout.addView(repsView);

                addView(dateView);
                addView(weightRepsLayout);

            }
        }
    }

}
