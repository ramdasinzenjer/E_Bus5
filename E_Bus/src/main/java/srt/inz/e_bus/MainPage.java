package srt.inz.e_bus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainPage extends Activity {
	
	Button bl,br;
	EditText et1,et2;
	String s1,s2,styp,resultout;
	LinearLayout linlaHeaderProgress;
	ApplicationPreference applicationPreference;
	Spinner sp_typ; ArrayAdapter<String> sadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        et1=(EditText)findViewById(R.id.edituid);
        et2=(EditText)findViewById(R.id.editpass);
        applicationPreference= (ApplicationPreference) getApplication();
        sp_typ=(Spinner)findViewById(R.id.spinner_mtyp);
              
        String[] typ = getResources().getStringArray(R.array.type);
        
        sadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typ);
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_typ.setAdapter(sadapter);
	    sp_typ.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				styp=arg0.getItemAtPosition(arg2).toString();
				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
        
        bl=(Button)findViewById(R.id.btnlog);
        br=(Button)findViewById(R.id.btnreg);
        linlaHeaderProgress=(LinearLayout)findViewById(R.id.linlaHeaderProgress);
        
        br.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainPage.this,RegPage.class);
				startActivity(i);
			}
		});
        bl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				s1=et1.getText().toString();
				s2=et2.getText().toString();
				
				SharedPreferences share=getSharedPreferences("mKey", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("keyuid", s1);
				ed.commit();
				
				if(s1.equals("admin")||s2.equals("password"))
				{
				Intent i=new Intent(getApplicationContext(),AdminHome.class);
				startActivity(i);
				}
				else{				
				new LoginApiTask().execute();							
				}
			}
		});	
        	
	}
	public class LoginApiTask extends AsyncTask<String,String,String> {
	    
	    @Override
	    protected String doInBackground(String... params) {


	            String urlParameters = null;
	            try {
	                urlParameters =  "password=" + URLEncoder.encode(s2, "UTF-8") + "&&"
	                		+ "type="+ URLEncoder.encode(styp, "UTF-8") + "&&"
	                        + "username=" + URLEncoder.encode(s1, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	        String    result = Connectivity.excutePost(Constants.LOGIN_URL,
	                    urlParameters);
	            Log.e("You are at", "" + result);
	            resultout=result;

	       return result;
	    }

	    @Override
	    protected void onPostExecute(String s) {
	        super.onPostExecute(s);
	        
	        linlaHeaderProgress.setVisibility(View.GONE);
	        //Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_SHORT).show();
	      
	        if(resultout.contains("success"))
	        {
	        	applicationPreference.setLoginStatus(true);
	        	applicationPreference.setUserId(s1);
	        Toast.makeText(getApplicationContext(), ""+resultout, Toast.LENGTH_SHORT).show();
	        if(styp.equals("User"))
	        {
	        	Intent i=new Intent(getApplicationContext(),MainHome.class);
	        	startActivity(i);
		        finish();
	        }
	        else
	        {
	        	Intent i=new Intent(getApplicationContext(),MainHomeConductor.class);
	        	startActivity(i);
		        finish();
	        }
	        
	        
	        }
	        else
	        {
	        	Toast.makeText(getApplicationContext(), ""+resultout, Toast.LENGTH_SHORT).show();
	        }
	        
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	        linlaHeaderProgress.setVisibility(View.VISIBLE);

	    }
	}
}
