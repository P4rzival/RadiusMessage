package com.github.P4rzival.RadiusMessage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TextPostDialog extends AppCompatDialogFragment {
    private EditText editPostText;
    private EditText editPostRadius;
    private EditText editPostDuration;
    private TextPostDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("TextPost").setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String postText = editPostText.getText().toString();
                int postRadius = Integer.parseInt(editPostRadius.getText().toString());
                int postDuration = Integer.parseInt(editPostDuration.getText().toString());
                listener.applyTexts(postText, postRadius, postDuration);
            }
        });
        editPostText = view.findViewById(R.id.edit_textPost);
        editPostRadius = view.findViewById(R.id.edit_radius);
        editPostDuration = view.findViewById(R.id.edit_duration);
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
