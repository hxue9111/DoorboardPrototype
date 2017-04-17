package cmsc434.doorboardprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class OfficeHoursActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_hours);

        // Construct the data source
        ArrayList<Person> arrayOfUsers = new ArrayList<Person>(MainActivity.dataSource);
        // Create the adapter to convert the array to views
        PersonAdapter adapter = new PersonAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.person_list);
        listView.setAdapter(adapter);

        enableBackNavigation();
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
}
