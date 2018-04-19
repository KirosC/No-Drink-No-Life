package ccn2279.a16031806a.nodrinknolife;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import ccn2279.a16031806a.nodrinknolife.utilities.CalculationUtils;

/**
 * Dialog class to show when user drinks too much water.
 */
public class TooMuchWaterDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_dead_too_much_water)
                .setIcon(R.drawable.ic_dissatisfied_black_24dp)
                .setTitle(R.string.dialog_title_two)
                .setPositiveButton(R.string.respawn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            CalculationUtils.respawnCharacter(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((MainActivity) getActivity()).characterIsDead = false;
                        ((MainActivity) getActivity()).handler.post(((MainActivity) getActivity()).runnableCode);
                        ((MainActivity) getActivity()).fAB.setImageResource(R.drawable.ic_tint);
                        ((MainActivity) getActivity()).fAB.setScaleType(ImageView.ScaleType.CENTER);
                        ((MainActivity) getActivity()).fAB.setClickable(true);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
