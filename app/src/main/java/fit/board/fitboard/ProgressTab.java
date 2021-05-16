package fit.board.fitboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

public class ProgressTab {

    Button addEntryButton;
    EditText dateET;
    EditText weightET;
    ImageView attachButton;
    MainActivity activity;
    LinearLayout mainLayout;
    LinearLayout progressList;
    LinearLayout progressImageRow;
    LinearLayout tempImageRow;
    ImageView[] tempImages;
    Bitmap[] tempBitmaps;
    int currentTempImg = 0;
    ProgressLayout[] progressLayouts;
    File progressDir;
    int currentProgressID = 0;
    String permissions[] = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};



    public ProgressTab(MainActivity activityIn){
        activity = activityIn;

        String path = activity.getExternalFilesDir(null).getAbsolutePath();
        progressDir = new File(path + "/savedimages");

        mainLayout = activity.findViewById(R.id.progress_main);
        addEntryButton = activity.findViewById(R.id.progress_entry_add_button);
        addEntryButton.setOnClickListener(activity);
        dateET = activity.findViewById(R.id.progress_date_input);
        weightET = activity.findViewById(R.id.progress_weight_input);
        attachButton = activity.findViewById(R.id.progress_attach_button);
        attachButton.setOnClickListener(activity);
        progressList = activity.findViewById(R.id.progress_list);
        progressImageRow = activity.findViewById(R.id.progress_image_row);
        tempImageRow = activity.findViewById(R.id.temp_image_row);
        tempImages = new ImageView[4];
        tempImages[0] = activity.findViewById(R.id.progress_temp_img_1);
        tempImages[1] = activity.findViewById(R.id.progress_temp_img_2);
        tempImages[2] = activity.findViewById(R.id.progress_temp_img_3);
        tempImages[3] = activity.findViewById(R.id.progress_temp_img_4);
        tempBitmaps = new Bitmap[4];


        ProgressLayout p = new ProgressLayout(activity);
        progressList.addView(p);


        activity.requestPermissions(permissions, 0);

        loadEntries();
    }

    public void loadEntries(){
        progressList.removeAllViews();
        Cursor c  = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM ProgressList", null);
        DecimalFormat df = new DecimalFormat("0.0");
        float lastWeight = 0;
        if(c.getCount() > 0){
            c.moveToLast();
            currentProgressID = c.getInt(0) + 1;
            progressLayouts = new ProgressLayout[c.getCount()];
            for(int i = 0; i < progressLayouts.length; i++){
                int day, month, year;
                progressLayouts[i] = new ProgressLayout(activity);
                progressLayouts[i].progressID = c.getInt(0);
                progressLayouts[i].weightTV.setText(Float.toString(c.getFloat(4)) + "Lbs");
                if(i != 0){
                    progressLayouts[i - 1].difTV.setText("(" + df.format(lastWeight - c.getFloat(4)) + ")");
                }
                lastWeight = c.getFloat(4);
                progressList.addView(progressLayouts[i]);
                //Bitmap bm = BitmapFactory.decodeFile(progressDir.getAbsolutePath() + "/YoPic.jpg");
                //attachButton.setImageBitmap(bm);

                day = c.getInt(1);
                month = c.getInt(2);
                year = c.getInt(3);
                int yearTrimmed = year % 100;
                String yearString;

                String dateString = "";
                switch (month){
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

                progressLayouts[i].dateTV.setText(dateString + " " + day + ", '" + yearTrimmed);



                if(c.getInt(5) > 0){
                    Cursor imgCursor = activity.dbManager.workoutsDB.rawQuery("SELECT * FROM ProgressListPhotos WHERE ProgressID = " + c.getInt(0), null);
                    imgCursor.moveToFirst();

                    for(int j = 0; j < 4; j++){
                        if(!imgCursor.getString(j + 1).equals(" ")){

                            progressLayouts[i].picturesRow.setVisibility(View.VISIBLE);
                            Toast.makeText(activity, imgCursor.getString(j + 1), Toast.LENGTH_SHORT).show();
                            progressLayouts[i].images[j].setImageBitmap(BitmapFactory.decodeFile(imgCursor.getString(j + 1)));
                        }

                    }
                }

                c.moveToPrevious();
            }

        } else{
            return;
        }
    }

    public void addEntry() {
        if(ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }

        Calendar calendarDate = Calendar.getInstance();
        int month, day, year;
        day = calendarDate.get(Calendar.DAY_OF_MONTH);
        month = calendarDate.get(Calendar.MONTH);
        year = calendarDate.get(Calendar.YEAR);

        if(weightET.getText().toString().equals("")){
            Toast t = Toast.makeText(activity.context, "Weight Field Is Empty", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        float weight = Float.parseFloat(weightET.getText().toString());

        String[] imagePaths = new String[4];
        int i = 0;
        if(currentTempImg > 0){
            for(i = 0; i < currentTempImg; i++){
                saveImage(tempBitmaps[i], currentProgressID, i);
                imagePaths[i] = progressDir.getAbsolutePath() + "/" + currentProgressID + "_" + i + "progress.jpg";
            }
            while(i < 4){
                imagePaths[i] = " ";
                i++;
            }
            //workoutsDB.execSQL("CREATE TABLE IF NOT EXISTS ProgressListPhotos(ProgressID INT, One VARCHAR, Two VARCHAR, Three VARCHAR, Four VARCHAR);");
            activity.dbManager.workoutsDB.execSQL("INSERT INTO ProgressListPhotos Values(" + currentProgressID + ", '" + imagePaths[0] + "', '" + imagePaths[1] + "', '" + imagePaths[2] + "', '" + imagePaths[3] + "');");



        }

        activity.dbManager.workoutsDB.execSQL("INSERT INTO ProgressList Values(" + currentProgressID++ + ", " + day + ", " + month + ", " + year + ", " + weight + ", " + currentTempImg + ");");
        loadEntries();
        progressImageRow.setVisibility(View.GONE);
        weightET.setText("");
        for(i = 0; i < 4; i++){
            tempImages[i].setImageBitmap(null);
            tempBitmaps[i] = null;
        }
        currentTempImg = 0;
    }

    public void saveImage(Bitmap bm, int progressID, int index){


        if(!progressDir.exists()){
            progressDir.mkdir();
        }
       // if(progressDir.mkdir()){
        //    Toast t = Toast.makeText(activity, "Passed File", Toast.LENGTH_LONG);
        //    t.show();
       // }
        else{
            //Toast t = Toast.makeText(activity, path, Toast.LENGTH_LONG);
            //t.show();
        }

        File filePath = new File(progressDir, progressID + "_" + index + "progress.jpg");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            if(bm.compress(Bitmap.CompressFormat.JPEG, 100, stream)){
                fos.write(stream.toByteArray());
                Toast t = Toast.makeText(activity, filePath.getAbsolutePath(), Toast.LENGTH_LONG);
                t.show();
            }
            fos.flush();
            fos.close();
            //MediaStore.Images.Media.insertImage(activity.getContentResolver(), filePath.getAbsolutePath(), filePath.getName(), filePath.getName());

        } catch (FileNotFoundException e) {
            Toast.makeText(activity, "Save Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(activity, "Save Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void selectAttachment() {
        if(currentTempImg >= 4){
            Toast t = Toast.makeText(activity, "Photo Limit Reached", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, activity.RESULT_LOAD_IMAGE);

    }

    public void addImage(Bitmap bm) {
        if(tempImageRow.getVisibility() == View.GONE){
            tempImageRow.setVisibility(View.VISIBLE);
        }
        tempBitmaps[currentTempImg] = bm;
        tempImages[currentTempImg++].setImageBitmap(bm);

        //ImageView newImage = new ImageView(activity);
        //newImage.setImageBitmap(bm);
        //LinearLayout.LayoutParams newImageParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        //newImageParams.setMargins((int)(5 * activity.density),0,(int)(5 * activity.density),0);
        //newImage.setLayoutParams(newImageParams);
        //progressImageRow.addView(newImage);
        //saveImage(bm);
    }

    private class ProgressLayout extends LinearLayout{

        LinearLayout infoRow;
        LinearLayout picturesRow;
        TextView weightTV;
        int progressID;
        ImageView[] images;
        LayoutParams imageOneParams = new LayoutParams(0, (int)(80 * activity.density), 1);
        TextView difTV;
        TextView dateTV;

        public ProgressLayout(Context context) {
            super(context);
            setOrientation(VERTICAL);
            setPadding((int) (10 * activity.density),(int) (10 * activity.density),(int) (10 * activity.density),(int) (10 * activity.density));
            LayoutParams mainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(mainParams);

            infoRow = new LinearLayout(activity);
            infoRow.setOrientation(HORIZONTAL);
            LayoutParams infoRowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(40 * activity.density));
            infoRow.setLayoutParams(infoRowParams);

            dateTV = new TextView(activity);
            dateTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
            dateTV.setTextColor(Color.BLACK);
            dateTV.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
            dateTV.setText("Jan 28, '21");
            LayoutParams dateTVParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            dateTV.setLayoutParams(dateTVParams);

            weightTV = new TextView(activity);
            weightTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
            weightTV.setTextColor(Color.BLACK);
            weightTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            weightTV.setText("223.2 Lbs");
            LayoutParams weightTVParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            weightTV.setLayoutParams(weightTVParams);

            difTV = new TextView(activity);
            difTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
            difTV.setTextColor(Color.BLACK);
            difTV.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
            //difTV.setText("(-1.2) Lbs");
            LayoutParams difTVParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            difTV.setLayoutParams(difTVParams);

            infoRow.addView(dateTV);
            infoRow.addView(weightTV);
            infoRow.addView(difTV);

            addView(infoRow);
            /*
             <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="0px"
            android:orientation="horizontal"
            android:paddingLeft="10dp"
            android:visibility="gone"
            android:id="@+id/temp_image_row"
            android:paddingRight="10dp"
            android:layout_weight="9">

                <ImageView
            android:layout_width="0px"
            android:layout_height="80dp"
            android:layout_weight="1"
            android:id="@+id/progress_temp_img_1"
            android:layout_marginLeft="5dp"
            android:layout_marginRight="5dp" />

             */

            picturesRow = new LinearLayout(activity);

            //picturesRow.setBackgroundColor(Color.BLACK);
            picturesRow.setOrientation(HORIZONTAL);
            picturesRow.setPadding((int)(10 * activity.density),0,(int)(10 * activity.density),0);
            picturesRow.setVisibility(GONE);
            LayoutParams picturesRowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(80 * activity.density));
            picturesRow.setLayoutParams(picturesRowParams);

            images = new ImageView[4];

            for(int i = 0; i < 4; i++){
                images[i] = new ImageView(activity);
                images[i].setLayoutParams(imageOneParams);
                //images[i].setImageResource(R.drawable.add);
                picturesRow.addView(images[i]);
            }

            addView(picturesRow);


        }

    }
}
