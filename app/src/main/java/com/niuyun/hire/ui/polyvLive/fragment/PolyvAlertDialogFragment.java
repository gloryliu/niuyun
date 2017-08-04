package com.niuyun.hire.ui.polyvLive.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.niuyun.hire.R;


/**
 * 是否要结束直播的对话框
 */
public class PolyvAlertDialogFragment extends DialogFragment {
    private DialogFragmentClickImpl dialogFragmentClick;

    public interface DialogFragmentClickImpl {
        void doPositiveClick();

        void doNegativeClick();
    }

    public void show(FragmentManager manager, String tag, DialogFragmentClickImpl dialogFragmentClick){
        this.dialogFragmentClick = dialogFragmentClick;
        show(manager,tag);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setCustomTitle(LayoutInflater.from(getActivity()).inflate(R.layout.polyv_fragment_dialog_title, null)).setMessage("你确定要结束直播吗?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (dialogFragmentClick != null)
                            dialogFragmentClick.doPositiveClick();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (dialogFragmentClick != null)
                            dialogFragmentClick.doNegativeClick();
                    }
                }).create();
        alertDialog.show();
        // show之后才可以获取，否则获取为null
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_main_d));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_custom));
        return alertDialog;
    }
}
