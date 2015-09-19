package se.tmeit.app.ui.externalEvents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import se.tmeit.app.R;
import se.tmeit.app.model.ExternalEvent;
import se.tmeit.app.model.ExternalEventAttendee;

/**
 * Dialog for attending/unattending an external event.
 */
public final class ExternalEventAttendDialogFragment extends DialogFragment {
    private ExternalEventAttendDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        boolean isAttending = args.getBoolean(ExternalEvent.Keys.IS_ATTENDING);
        String name = args.getString(ExternalEventAttendee.Keys.NAME);
        String dob = args.getString(ExternalEventAttendee.Keys.DOB);
        String drinkPrefs = args.getString(ExternalEventAttendee.Keys.DRINK_PREFS);
        String foodPrefs = args.getString(ExternalEventAttendee.Keys.FOOD_PREFS);
        String notes = args.getString(ExternalEventAttendee.Keys.NOTES);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_external_event_attend, null);

        EditText nameText = (EditText) view.findViewById(R.id.event_attending_name);
        nameText.setText(name);
        final EditText dobText = (EditText) view.findViewById(R.id.event_attending_dob);
        dobText.setText(dob);
        final EditText drinkPrefsText = (EditText) view.findViewById(R.id.event_attending_drink_prefs);
        drinkPrefsText.setText(drinkPrefs);
        final EditText foodPrefsText = (EditText) view.findViewById(R.id.event_attending_food_prefs);
        foodPrefsText.setText(foodPrefs);
        final EditText notesText = (EditText) view.findViewById(R.id.event_attending_notes);
        notesText.setText(notes);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.event_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null == mListener) {
                            return;
                        }

                        ExternalEventAttendee attendee = new ExternalEventAttendee();
                        attendee.setDateOfBirth(dobText.getText().toString());
                        attendee.setDrinkPreferences(drinkPrefsText.getText().toString());
                        attendee.setFoodPreferences(foodPrefsText.getText().toString());
                        attendee.setNotes(notesText.getText().toString());
                        mListener.saveClicked(attendee);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        if (isAttending) {
            builder.setNeutralButton(R.string.event_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != mListener) {
                        mListener.deleteClicked();
                    }
                }
            });
        }

        return builder.create();
    }

    public void setListener(ExternalEventAttendDialogListener listener) {
        mListener = listener;
    }

    public interface ExternalEventAttendDialogListener {
        void deleteClicked();

        void saveClicked(ExternalEventAttendee attendee);
    }
}
