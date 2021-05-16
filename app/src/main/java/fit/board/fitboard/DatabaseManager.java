package fit.board.fitboard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {

    SQLiteDatabase workoutsDB;
    MainActivity activity;

    public DatabaseManager(MainActivity activityIn){
        activity = activityIn;
        loadDatabase();
    }

    void loadDatabase() {

        Log.d("tag1", "Here");
        workoutsDB = activity.openOrCreateDatabase("CLIPBOARD", Context.MODE_PRIVATE, null);


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
        workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS ProgressList(ProgressID INT, Day INT, Month INT, Year INT, Weight FLOAT, PicsAttached INT);");
        workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS ProgressListPhotos(ProgressID INT, One VARCHAR, Two VARCHAR, Three VARCHAR, Four VARCHAR);");
        //workoutsDB.execSQL("INSERT INTO ProgressList Values(1, 1, 1, 1, 195.8, 0)");
        //workoutsDB.execSQL("INSERT INTO ProgressList Values(1, 1, 1, 1, 194.0, 0)");




        ////loadWorkouts();

        //loadDefaultWorkouts();

        //loadActiveWorkout();

    }

    int getLastWorkoutID(){
        Cursor c = workoutsDB.rawQuery("SELECT * FROM WorkoutList", null);
        Log.d("tag1", "Here1");
        if(c.getCount() > 0){
            Log.d("tag1", "Here2");
            c.moveToLast();
            return(c.getInt(0));
        } else{
            return 0;
        }
    }

}
