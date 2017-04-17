package cmsc434.doorboardprototype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    // Hardcoded Data Start -------------------------------->
    String name_key = "name",
            title_key ="title",
            hours_key = "hours",
            profile_key = "profile_visibility",
            message_key = "message",
            status_key = "status",
            multi_to_single_view_key = "m_t_s";
    static Person p1 = new Person("Evan Golub", "Professor", "1:00pm - 2:00pm", false);
    static Person p2 = new Person("Bob", "TA", "1:00pm - 2:00pm", true);
    static Person p3 = new Person("Bill", "TA", "1:00pm - 2:00pm", false);

    static List<Person> single = new ArrayList<>(Arrays.asList(new Person[]{p1}));
    static List<Person> multi = new ArrayList<>(Arrays.asList(new Person[]{p1,p2,p3}));
    static List<Person> dataSource = new ArrayList<>(single);

    TextView name;
    TextView title;
    TextView message;
    TextView status;
    ImageView iv;
    // Hardcoded Data End ---------------------------------->
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getBooleanExtra(multi_to_single_view_key, false)){
            setContentView(R.layout.activity_main);
            name = (TextView)findViewById(R.id.name);
            title = (TextView) findViewById(R.id.title);
            message = (TextView) findViewById(R.id.message);
            status = (TextView) findViewById(R.id.status);
            iv = (ImageView) findViewById(R.id.profile);

            Intent i = getIntent();

            name.setText(i.getStringExtra(name_key));
            title.setText(i.getStringExtra(title_key));

            if(i.hasExtra(message_key)){
                String message_extra = i.getStringExtra(message_key);
                message.setText(message_extra);
                message.setVisibility(View.VISIBLE);
                findViewById(R.id.message_label).setVisibility(View.VISIBLE);

            }else{
                message.setVisibility(View.INVISIBLE);
                findViewById(R.id.message_label).setVisibility(View.INVISIBLE);
            }

            status.setText(i.getStringExtra(status_key));
            status.setVisibility(View.VISIBLE);

            if(i.getBooleanExtra(profile_key, true)){
                iv.setImageResource(Person.default_profile);
                iv.setVisibility(View.VISIBLE);
                enableProfileZoom();
            }

            enableBackNavigation();

        } else if (dataSource.size() == 1) {
            setContentView(R.layout.activity_main);

            name = (TextView)findViewById(R.id.name);
            title = (TextView) findViewById(R.id.title);
            message = (TextView) findViewById(R.id.message);
            status = (TextView) findViewById(R.id.status);
            iv = (ImageView) findViewById(R.id.profile);
            Person p = dataSource.get(0);
            p.message = "Away until 3:00pm";
            p.status = "Status: Out";
            p.profile_visible = true;


            name.setText(p.name);
            title.setText(p.title);
            message.setText(p.message);

            if (p.status != null) {
                status.setText(p.status);
                status.setVisibility(View.VISIBLE);
            }

            if (p.profile_visible) {
                iv.setImageResource(p.default_profile);
                iv.setVisibility(View.VISIBLE);
                enableProfileZoom();
            }

            enableViewModeChange();
            enableUserAdd();
        } else {
            setContentView(R.layout.multi_person_view);

            // Construct the data source
            ArrayList<Person> arrayOfUsers = new ArrayList<Person>(dataSource);
            // Create the adapter to convert the array to views
            PersonAdapter adapter = new PersonAdapter(this, arrayOfUsers);
            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.person_list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Person item = (Person) adapterView.getItemAtPosition(i);

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra(name_key, item.name);
                    intent.putExtra(title_key, item.title);
                    intent.putExtra(status_key, item.status);
                    intent.putExtra(profile_key, item.profile_visible);
                    intent.putExtra(multi_to_single_view_key, true);
                    if(item.message != null) {
                        intent.putExtra(message_key, item.message);
                    }
                    startActivity(intent);
                }
            });
            enableViewModeChange();
        }
        enableMapLayer();
        enableCalenderExpansion();
        enableUserAdd();

    }

    private void enableMapLayer(){
        ImageView map_icon = (ImageView) findViewById(R.id.map);
        map_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* adapt the image to the size of the display */
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(),R.drawable.f0_map),size.x,size.y,true);

                /* fill the background ImageView with the resized image */
                final ImageView map_layer = (ImageView) findViewById(R.id.map_layer);
                map_layer.setImageBitmap(bmp);
                map_layer.setVisibility(View.VISIBLE);
                map_layer.bringToFront();
                map_layer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        map_layer.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    private void enableCalenderExpansion(){
        ImageView cal_icon = (ImageView) findViewById(R.id.calender_icon);
        cal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OfficeHoursActivity.class);
                startActivity(i);
            }
        });
    }
    private void enableProfileZoom() {
        final ImageView profile_icon = (ImageView) findViewById(R.id.profile);
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bmp = ((BitmapDrawable)profile_icon.getDrawable()).getBitmap();

                /* fill the background ImageView with the resized image */
                final ImageView profile_layer = (ImageView) findViewById(R.id.profile_layer);
                profile_layer.setImageBitmap(bmp);
                profile_layer.setVisibility(View.VISIBLE);
                profile_layer.bringToFront();
                profile_layer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        profile_layer.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    private void enableViewModeChange(){
        Button view_mode_btn = (Button) findViewById(R.id.view_mode);
        view_mode_btn.setVisibility(View.VISIBLE);
        view_mode_btn.setText(((dataSource.size() == 1) ? "Multi-person view": "Single person view")+"\nDEBUG ONLY");
        view_mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource = new ArrayList<Person>((dataSource.size() == 1) ? multi : single);
                recreate();
            }
        });
    }
    private void enableBackNavigation(){
        Button back_btn = (Button)findViewById(R.id.back);
        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void enableUserAdd(){
        Button add_user_btn = (Button) findViewById(R.id.add_user);
        add_user_btn.setVisibility(View.VISIBLE);
        add_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.add(new Person("Fake person"+dataSource.size(), "Fake Title", "Fake Hours", (Math.random() > 0.5)? true: false));
                recreate();
            }
        });
    }

}
