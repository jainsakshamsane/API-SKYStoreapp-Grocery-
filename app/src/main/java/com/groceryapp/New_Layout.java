package com.groceryapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class New_Layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);
    }

    public void showDialog(View view) {
        int viewId = view.getId();

        if (viewId == R.id.basic_dialog) {
            new SweetAlertDialog(this)
                    .setTitleText("Here's a message!")
                    .setContentText("This is Basic Dialog").show();
        } else if (viewId == R.id.error_dialog) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Something went wrong!").show();
        } else if (viewId == R.id.warning_dialog) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to recover this file !")
                    .setConfirmText("Yes, delete it!").show();
        } else if (viewId == R.id.success_dialog) {
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Great!")
                    .setContentText("You completed this task.").show();
        }
    }
}
