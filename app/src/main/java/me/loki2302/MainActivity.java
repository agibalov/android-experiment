package me.loki2302;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.loki2302.events.DisplayErrorMessageRequestedEvent;
import me.loki2302.events.DisplayProgressDialogRequestedEvent;
import me.loki2302.events.ErrorMessageDialogClosedEvent;
import me.loki2302.events.ProgressDialogClosedEvent;
import me.loki2302.fsm.FSMActivity;
import me.loki2302.fsm.State;
import me.loki2302.states.DoingNothingState;

public class MainActivity extends FSMActivity<MainActivityContext> implements MainActivityContext {
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.displayProgressDialogButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processEvent(new DisplayProgressDialogRequestedEvent());
            }
        });

        findViewById(R.id.displayErrorMessageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processEvent(new DisplayErrorMessageRequestedEvent());
            }
        });
    }

    @Override
    public void displayProgressDialog() {
        if(progressDialog != null) {
            throw new IllegalStateException();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                processEvent(new ProgressDialogClosedEvent());
            }
        });
    }

    @Override
    public void hideProgressDialog() {
        if(progressDialog == null) {
            throw new IllegalStateException();
        }

        progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    public void displayErrorMessage() {
        if(alertDialog != null) {
            throw new IllegalStateException();
        }

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Making it look like there's an error")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        processEvent(new ErrorMessageDialogClosedEvent());
                    }
                }).show();
    }

    @Override
    public void hideErrorMessage() {
        if(alertDialog == null) {
            throw new IllegalStateException();
        }

        alertDialog.dismiss();
        alertDialog = null;
    }

    @Override
    public void trace(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Class<? extends State<MainActivityContext>> getStateClass() {
        return MainActivityContextState.class;
    }

    @Override
    protected State<MainActivityContext> getDefaultState() {
        return new DoingNothingState();
    }

    @Override
    protected int getLogicalInstanceId() {
        return 1;
    }
}
