package com.github.P4rzival.RadiusMessage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PostDialog extends AppCompatDialogFragment {
    private EditText editPostText;
    private TextPostDialogListener listener;

    private static SeekBar timeBar;
    private static TextView timeView;
    private static SeekBar radiusBar;
    private static TextView radiusView;

    ImageView uploadImage;
    ImageButton cameraButton;
    ImageButton galleryButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        timeBar = (SeekBar) view.findViewById(R.id.timeBar);
        timeView = (TextView) view.findViewById(R.id.timeView);

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int timeProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeProgress = progress;
                timeView.setText("Time Duration: " + timeBar.getProgress() + " Hours");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                timeView.setText("Time Duration: " + timeBar.getProgress() + " Hours");
            }
        });

        radiusBar = (SeekBar) view.findViewById(R.id.radiusBar);
        radiusView = (TextView) view.findViewById(R.id.radiusView);

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int barProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            barProgress = progress;
            radiusView.setText("Radius: " + radiusBar.getProgress() + " Meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radiusView.setText("Radius: " + radiusBar.getProgress() + " Meters");
            }
        });





        builder.setView(view).setTitle("Make a Post!").setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String postText = editPostText.getText().toString();
                int postRadius;
                int postDuration;

                if (timeView.getText().toString().compareTo("Time Duration: 0-100 Hours") == 0)
                {
                    postDuration = 0;
                }
                else
                {
                    postDuration = Integer.parseInt(timeView.getText().toString().replaceAll("\\D+",""));
                }

                if (radiusView.getText().toString().compareTo("Radius: 0-100 Meters") == 0)
                {
                    postRadius = 0;
                }
                else
                {
                    postRadius = Integer.parseInt(radiusView.getText().toString().replaceAll("\\D+",""));
                }
                
                listener.applyTexts(postText, postRadius, postDuration);
            }
        });
        editPostText = view.findViewById(R.id.edit_textPost);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (TextPostDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement TextPostDialogListener");
        }
    }

    public interface TextPostDialogListener{
        void applyTexts(String postText, int postRadius, int postDuration);
    }
}
