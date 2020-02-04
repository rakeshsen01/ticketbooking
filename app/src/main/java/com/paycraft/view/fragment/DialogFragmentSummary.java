package com.paycraft.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.paycraft.presenter.BookingSummaryPresenter;
import com.paycraft.presenter.modelview.ConfirmBooking;
import com.paycraft.presenter.modelview.dao.ConfirmBookingDao;
import com.paycraft.ticketbookingoperator.R;

import java.util.Objects;


public class DialogFragmentSummary extends DialogFragment implements ConfirmBooking {
    private LinearLayout linear;
    private BookingSummaryPresenter bookingSummaryPresenter;

    public static DialogFragmentSummary newInstance(String title) {
        DialogFragmentSummary frag = new DialogFragmentSummary();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
        bookingSummaryPresenter = new BookingSummaryPresenter(this, new ConfirmBookingDao());
    }

    private void initializeView(View view) {
        TextView txtName,txtDocumentId,txtSource,txtDestination,txtPrize;
        txtName = view.findViewById(R.id.txtName);
        txtDocumentId= view.findViewById(R.id.txtDocumentId);
        txtSource= view.findViewById(R.id.txtSource);
        txtDestination= view.findViewById(R.id.txtDestination);
        txtPrize= view.findViewById(R.id.txtPrize);
        linear = view.findViewById(R.id.linear);
        Button generateButton = view.findViewById(R.id.button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingSummaryPresenter.generateBooking();
            }
        });


        txtName.setText(Objects.requireNonNull(getArguments()).getString("userName"));
        txtDocumentId.setText(getArguments().getString("documentID"));
        txtSource.setText(getArguments().getString("source"));
        txtDestination.setText(getArguments().getString("destination"));
        txtPrize.setText(getArguments().getString("prize"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bookingSummaryPresenter.onDestroy();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bookingSummaryPresenter.onDestroy();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(linear, message, Snackbar.LENGTH_LONG);
        snackbar.show();

    }
}
