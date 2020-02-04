package com.paycraft.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.paycraft.model.beans.Station;
import com.paycraft.model.beans.User;
import com.paycraft.ticketbookingoperator.R;
import com.paycraft.presenter.modelview.dao.BookingDataAccessDao;
import com.paycraft.presenter.BookingPresenter;
import com.paycraft.presenter.modelview.BookingView;
import com.paycraft.view.adapter.StationAdapter;
import com.paycraft.view.fragment.DialogFragmentSummary;

import java.util.List;

public class BookingActivity extends AppCompatActivity implements BookingView {

    private BookingPresenter bookingPresenter;
    private AutoCompleteTextView source;
    private AutoCompleteTextView destination;
    private StationAdapter sourceAdapter;
    private StationAdapter destinationAdapter;
    private TextView prizeText;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        InitializeView();
        bookingPresenter = new BookingPresenter(this,new BookingDataAccessDao());
        bookingPresenter.requestStation();
        user = (User) getIntent().getSerializableExtra("user");

    }

    private void InitializeView() {
        source = findViewById(R.id.sourceStation);
        destination = findViewById(R.id.destinationStation);
        prizeText = findViewById(R.id.prizeText);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFragment();

            }
        });

        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bookingPresenter.setSource(sourceAdapter.getStation(i));
            }
        });

        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bookingPresenter.setDestination(destinationAdapter.getStation(i));
            }
        });
    }

    /**
     * Show Dialog Fragment for booking Summary Details.
     */

    private void showDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragmentSummary editNameDialogFragment = DialogFragmentSummary.newInstance("Some Title");
        Bundle bundle = new Bundle();
        bundle.putString("userName",user.getName());
        bundle.putString("documentID",user.getDocumentID());
        bundle.putString("source",source.getText().toString());
        bundle.putString("destination",destination.getText().toString());
        bundle.putString("prize",prizeText.getText().toString());
        editNameDialogFragment.setArguments(bundle);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }


    @Override
    public void initializeStations(List<Station> stations) {

        if(stations != null && stations.size() != 0){
            bookingPresenter.saveRecordIntoDataBase(stations);
            sourceAdapter = new StationAdapter(this,R.layout.row,stations);
            destinationAdapter = new StationAdapter(this,R.layout.row,stations);
            source.setAdapter(sourceAdapter);
            destination.setAdapter(destinationAdapter);
        }
        else{
            bookingPresenter.getAllStationFromDb();
        }

    }

    @Override
    public void calculatePrize(String prize) {

        prizeText.setText(getString(R.string.prizeText,prize));

    }

    @Override
    public void showSnackBar(String message) {

    }

    @Override
    protected void onDestroy() {
        bookingPresenter.onDestroy();
        super.onDestroy();
    }
}
