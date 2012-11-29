package ie.cathalcoffey.android.projecteuler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setTitle("Authenticating");
        
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog, null);
        
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        
        builder.setCancelable(false);
        
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onDialogPositiveClick(LoginDialogFragment.this);
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onDialogNegativeClick(LoginDialogFragment.this);
                   }
               });
        
        // Create the AlertDialog object and return it
    	AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        
        // Workaround: http://code.google.com/p/android/issues/detail?id=6360
        alert.setOnShowListener(new OnShowListener() 
        {	
        	@Override
        	public void onShow(DialogInterface dialog) 
        	{	
        		if(MyApplication.login_opt != null && MyApplication.login_opt.dialog != null && MyApplication.login_opt.progressMsg != null)
            	{
            		MyApplication.login_opt.dialog.setMessage(MyApplication.login_opt.progressMsg);
            		
            		if(MyApplication.login_opt.completed)
            			MyApplication.login_opt.dialog.completed();
            		else
            			((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            	}
        		
        		else
        		{
            		((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        		}
        	}
        });
     
        MyApplication.login_opt.dialog = this;
        
        return alert;
    }
    
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void solved();
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

	public void setMessage(String msg) 
	{
		Dialog d = MyApplication.login_opt.dialog.getDialog();
		if(d != null && d.isShowing())
		{
		    TextView textView = (TextView)d.findViewById(R.id.textView1);
		    if(textView != null)
			{
				textView.setText(msg);
			}   
		}
	}
	
	public void completed()
	{
		AlertDialog d = (AlertDialog)MyApplication.login_opt.dialog.getDialog();
		
		if(d != null)
		{
			ProgressBar pb = (ProgressBar)d.findViewById(R.id.activityIndicator);
			ImageView correct_wrong = (ImageView)d.findViewById(R.id.correct_wrong);
		    if(pb != null)
			{
		    	pb.setVisibility(View.GONE);
		    	
		    	if(MyApplication.login_opt.success)
		    	{
		    		correct_wrong.setImageResource(R.drawable.answer_correct);
		    		mListener.solved();
		    	}
		    	correct_wrong.setVisibility(View.VISIBLE);
			}
		    
			Button b1 = d.getButton(AlertDialog.BUTTON_POSITIVE);
			if(b1 !=null)
				b1.setEnabled(true);
			
			Button b2 = d.getButton(AlertDialog.BUTTON_NEGATIVE);
			if(b2 !=null)
			    b2.setEnabled(false);
		}
	}
}