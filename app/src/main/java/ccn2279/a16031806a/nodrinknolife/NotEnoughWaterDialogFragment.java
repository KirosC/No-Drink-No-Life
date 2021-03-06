package ccn2279.a16031806a.nodrinknolife;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import ccn2279.a16031806a.nodrinknolife.utilities.CalculationUtils;

/**
 * Dialog to show when user drinks not enough water.
 */
public class NotEnoughWaterDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_dead_not_enough_water)
                .setIcon(R.drawable.ic_dissatisfied_black_24dp)
                .setTitle(R.string.dialog_title)
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