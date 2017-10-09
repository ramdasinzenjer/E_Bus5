package srt.inz.e_bus;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat") public class CardDetails extends Activity{
	
	Spinner spcard; ArrayAdapter<String> sarray; String sctyp;
	Spinner spPcard; ArrayAdapter<String> sParray; String sPctyp;
	TextView tv; ApplicationPreference appPreference;
	String resp; String upres;	
	EditText etnum,etpin,etexp; String scardno,spin,sexp,samount,sdate,s_exdate;
	Button btup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carddetails);
		spcard=(Spinner)findViewById(R.id.spinner1_mcardtype);
		spPcard=(Spinner)findViewById(R.id.mpinner_paytype);
		tv=(TextView)findViewById(R.id.text_carddetails);
		etnum=(EditText)findViewById(R.id.edit_cardno);
		etexp=(EditText)findViewById(R.id.edit_expdate);
		etpin=(EditText)findViewById(R.id.edit_pin);
		btup=(Button)findViewById(R.id.btn_sendreq);
		
		appPreference=(ApplicationPreference)getApplication();	
		
		
		btup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				scardno=etnum.getText().toString();
				spin=etpin.getText().toString();
				sexp=etexp.getText().toString();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar calobj = Calendar.getInstance();
				sdate=df.format(calobj.getTime());
				
				calobj.add(Calendar.DATE, 30);
				Date d=calobj.getTime();
				s_exdate=df.format(d);
				
				new Req_UpdateCard().execute();
				
			}
		});
		
		String[] c_ty = getResources().getStringArray(R.array.pay_type);
		sParray=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,c_ty);
		sParray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPcard.setAdapter(sParray);
		spPcard.setOnItemSelectedListener(new OnItemSelectedListener()
        {
    	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sPctyp=arg0.getItemAtPosition(arg2).toString();
				
				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
		
				
				Toast.makeText(getApplicationContext(), ""+sPctyp, Toast.LENGTH_SHORT).show();
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
		
		String[] c_typ = getResources().getStringArray(R.array.card_type);
		sarray=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,c_typ);
		sarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spcard.setAdapter(sarray);
		spcard.setOnItemSelectedListener(new OnItemSelectedListener()
        {
    	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sctyp=arg0.getItemAtPosition(arg2).toString();
				
				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
				
				if(sctyp.equals("Silver"))
				{
					samount="1000";
				}
				else if(sctyp.equals("Gold"))
				{
					samount="2000";
				}else
				{
					samount="5000";
				}
				
				Toast.makeText(getApplicationContext(), ""+sctyp+" selected. Amount :"+samount, Toast.LENGTH_SHORT).show();
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
		
		new Carddatafetch().execute();
	}
	
	public class Carddatafetch extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "username="+ URLEncoder.encode(appPreference.getUserId(), "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resp = Connectivity.excutePost(Constants.CARDDETAILS_URL,
                    urlParameters);
			return resp;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resp.contains("success"))
			{
				//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
				resparsingmethod();
			}
			else {
			//	Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
				tv.setText("No card available...");
			}
		}
		
	}
	public void resparsingmethod()
	{

		try
		{
			JSONObject jobject=new JSONObject(resp);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();

			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				
				String card_type=data1.getString("card_type");
				String exp_date=data1.getString("exp_date");
				String status=data1.getString("status");
            if(status.equals("0"))
            {
			tv.setText("Card Type : "+card_type+"\n Exp Date : "+exp_date+"\n Status : Pending");
			
            }
            else {
            	tv.setText("Card Type : "+card_type+"\n Exp Date : "+exp_date+"\n Status : Accepted");
			}
            
            
            }
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	public class Req_UpdateCard extends AsyncTask<String, String, String>
	
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "username="+ URLEncoder.encode(appPreference.getUserId(), "UTF-8")
						+"&&"+"card_typ="+ URLEncoder.encode(sPctyp, "UTF-8")
						+"&&"+"card_no="+ URLEncoder.encode(scardno, "UTF-8")
						+"&&"+"expiry_date="+ URLEncoder.encode(sexp, "UTF-8")
						+"&&"+"amount="+ URLEncoder.encode(samount, "UTF-8")
						
						+"&&"+"card_type="+ URLEncoder.encode(sctyp, "UTF-8")
						+"&&"+"issued_date="+ URLEncoder.encode(sdate, "UTF-8")
						+"&&"+"exp_date="+ URLEncoder.encode(s_exdate, "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			upres = Connectivity.excutePost(Constants.CARDREQ_PAY_URL,
                    urlParameters);
			return upres;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(upres.contains(""))
			{
				Toast.makeText(getApplicationContext(), ""+upres, Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), ""+upres, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	
}
