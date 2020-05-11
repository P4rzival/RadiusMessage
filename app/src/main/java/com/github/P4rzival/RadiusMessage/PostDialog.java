package com.github.P4rzival.RadiusMessage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.IOException;
import java.io.InputStream;

public class PostDialog extends AppCompatDialogFragment {
    private EditText editPostText;
    private TextPostDialogListener listener;


    private static final int RESULT_LOAD_IMAGE = 1;
    private static SeekBar delayBar;
    private static TextView delayView;

    private static SeekBar timeBar;
    private static TextView timeView;
    private static SeekBar radiusBar;
    private static TextView radiusView;

    Bitmap bitmapImage;
    Uri selectedImage;
    ImageView imageToUpload;
    ImageButton cameraButton;
    ImageButton galleryButton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null)
        {
            selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(RadiusMessage.getAppInstance().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        delayBar = (SeekBar) view.findViewById(R.id.delayBar);
        delayView = (TextView) view.findViewById(R.id.delayView);

        delayBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int delayProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                delayProgress = progress;
                delayView.setText("Time Delay to Publish Post: " + delayBar.getProgress() + " Minute(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                delayView.setText("Time Delay to Publish Post: " + delayBar.getProgress() + " Minute(s)");
            }
        });


        timeBar = (SeekBar) view.findViewById(R.id.timeBar);
        timeView = (TextView) view.findViewById(R.id.timeView);

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int timeProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeProgress = progress;
                timeView.setText("Time Duration: " + timeBar.getProgress() + " Hour(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                timeView.setText("Time Duration: " + timeBar.getProgress() + " Hour(s)");
            }
        });

        radiusBar = (SeekBar) view.findViewById(R.id.radiusBar);
        radiusView = (TextView) view.findViewById(R.id.radiusView);

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int barProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            barProgress = progress;
            radiusView.setText("Radius: " + radiusBar.getProgress() + " Meter(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radiusView.setText("Radius: " + radiusBar.getProgress() + " Meter(s)");
            }
        });

        imageToUpload = (ImageView) view.findViewById(R.id.imageToUpload);
        galleryButton = (ImageButton) view.findViewById(R.id.galleryButton);
//        cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);

        View.OnClickListener imageToUploadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        imageToUpload.setOnClickListener(imageToUploadListener);


        View.OnClickListener galleryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        };
        galleryButton.setOnClickListener(galleryListener);

//        View.OnClickListener cameraListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        };
//        cameraButton.setOnClickListener(cameraListener);

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
                int postDelay;

                if (delayView.getText().toString().compareTo("Time Delay to Publish Post: 0-100 Minute(s)") == 0)
                {
                    postDelay = 0;
                }
                else
                {
                    postDelay = Integer.parseInt(delayView.getText().toString().replaceAll("\\D+",""));
                }

                if (timeView.getText().toString().compareTo("Time Duration: 1-100 Hour(s)") == 0)
                {
                    postDuration = 1;
                }
                else
                {
                    postDuration = Integer.parseInt(timeView.getText().toString().replaceAll("\\D+",""));
                }

                if (radiusView.getText().toString().compareTo("Radius: 1-100 Meter(s)") == 0)
                {
                    postRadius = 1;
                }
                else
                {
                    postRadius = Integer.parseInt(radiusView.getText().toString().replaceAll("\\D+",""));
                }

                try {
                    listener.applyTexts(postText, postRadius, postDuration, postDelay, bitmapImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        void applyTexts(String postText, int postRadius, int postDuration, int postDelay, Bitmap bitmap) throws IOException;
    }
}
