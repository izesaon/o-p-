package com.example.cindy.op;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class DialogBox2 extends DialogFragment {
    public ArrayList<Integer> mSelectedItems;
    public static ArrayList friendlist ;
    public ArrayList temp;
    public ArrayList<String> friendsselected;
    boolean[] ischecked;
    //public ArrayList friendsselected;

    public DialogBox2() {
        mSelectedItems = new ArrayList<>();
        friendlist = new ArrayList();
        friendlist.add("Dorette");
        friendlist.add("Jia Wen");
        temp = new ArrayList();
       friendsselected  = new ArrayList();
      //  friendsselected.add("Testing");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        friendsselected = new ArrayList<>();
        ischecked = new boolean[friendlist.size()];



       // for(int i = 0 ; i < friendlist.size() ; i++){
       //     ischecked[i] = false;}
        //String[] friendlist = getResources().getStringArray(R.array.friendlist);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.selectfriends);
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
        builder.setMultiChoiceItems((CharSequence[]) friendlist.toArray(new CharSequence[friendlist.size()]), null,
                new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                    temp.add(friendlist.get(which));

                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                    temp.remove(friendlist.get(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        friendsselected.clear();
                        friendsselected.addAll(temp);
                        System.out.println(friendsselected.toString());
                        System.out.println("TEMP " + temp.toString());

                       //intent.putExtra("value",friendsselected );
                       // getActivity().finish();
                        //Intent intent = new Intent(getContext(),Main2Activity.class);
                       // startActivity(intent);



                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

}