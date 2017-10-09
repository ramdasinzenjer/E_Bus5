package srt.inz.e_bus;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
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

public class MainHomeConductor extends Activity{
	
	Spinner sp_bussrc,sp_busdest; 
	ArrayAdapter<String> sarray_bussrc,sarray_busdest; String sbssrc,sbdest,stun,resdest;
	
	EditText epid; Button bcheck,bconf; TextView tvdata;
	
	Spinner sp_busno; 
	ArrayAdapter<String> sarray_bus,sarray_dusdest; String sbsno,sbn_db,lresp,resp,myresponse,systime;
	
	//for mapping value from database
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> oslist1 = new ArrayList<HashMap<String, String>>();
	
	ApplicationPreference appPreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conductorhome);
		
		sp_bussrc=(Spinner)findViewById(R.id.mspinnsrc1);
		sp_busdest=(Spinner)findViewById(R.id.mspinndest1);
		sp_busno=(Spinner)findViewById(R.id.mspinnerbusabc);
		
		tvdata=(TextView)findViewById(R.id.txt_pdata);
		epid=(EditText)findViewById(R.id.edit_cusername);
		
		bcheck=(Button)findViewById(R.id.bt_check);
		bconf=(Button)findViewById(R.id.bt_confirmtravell);
		
		appPreference=(ApplicationPreference)getApplication();
		
		bcheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stun=epid.getText().toString();
				
				new Checkuserdata().execute();
			}
		});
		
		bconf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar calobj = Calendar.getInstance();
				systime=df.format(calobj.getTime());
				new Addtransport().execute();
			}
		});
		
		String[] dis = getResources().getStringArray(R.array.districts);
		sarray_bussrc=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dis);
        sarray_bussrc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_bussrc.setAdapter(sarray_bussrc);
		sp_bussrc.setOnItemSelectedListener(new OnItemSelectedListener()
        {
    	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sbssrc=arg0.getItemAtPosition(arg2).toString();
				
				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
				
				Toast.makeText(getApplicationContext(), ""+sbssrc, Toast.LENGTH_SHORT).show();
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
        
       
        new getbusno_date().execute();
        
		
	}
	
	
	
	public class Checkuserdata extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "username="+ URLEncoder.encode(stun, "UTF-8");
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
				Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
				resparsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
	
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
            		    
				tvdata.setText("Card Type : "+card_type+"\n Exp Date : "+exp_date);
			}
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	
	public class getbusno_date extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			lresp = Connectivity.excutePost(Constants.BUSNOFETCH_URL,
                    urlParameters);
			return lresp;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(lresp.contains("success"))
			{
				mparsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), lresp, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	public void mparsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(lresp);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();
			/*oslist.add(null);
			sp_date.setAdapter(null);*/
			List<String> lables1 = new ArrayList<String>();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				sbn_db=data1.getString("busno");
				String src=data1.getString("src");
				String dest=data1.getString("dest");
				String start_time=data1.getString("start_time");
				String end_time=data1.getString("end_time");
				String bus_fare=data1.getString("bus_fare");
				String bus_type=data1.getString("bus_type");
				
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("busno", sbn_db);
	            map.put("src", src);
	            map.put("dest", dest);
	            map.put("start_time", start_time);
	            map.put("end_time", end_time);
	            map.put("bus_fare", bus_fare);
	            map.put("bus_type", bus_type);
	            
	            oslist.add(map);
	            
	            lables1.add(oslist.get(i).get("busno"));
	            
	            sarray_bus=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lables1);
	            sarray_bus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            sp_busno.setAdapter(sarray_bus);
	            
	            sp_busno.setOnItemSelectedListener(new OnItemSelectedListener()
		        {

			    	
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						sbsno=arg0.getItemAtPosition(arg2).toString();
						
						((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
						
						Toast.makeText(getApplicationContext(), ""+sbsno, Toast.LENGTH_SHORT).show();
						
						new getbusdestination().execute();
						/*
						tvdata.setText("Bus Nuber : "+oslist.get(arg2).get("busno")+"\n Source : "
						+oslist.get(arg2).get("src") +"\n Destination :  "+oslist.get(arg2).get("dest")
						+"\n Starting Time :  "+oslist.get(arg2).get("start_time")+"\n End Time : "+
						oslist.get(arg2).get("end_time")+"\n Bus Fare : "+oslist.get(arg2).get("bus_fare"));*/
					
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub	
					}
		        	
		        });
			    
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	
	public class getbusdestination extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "busno="+URLEncoder.encode(sbsno,"UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resdest = Connectivity.excutePost(Constants.BUSSTOPFETCH_URL,
                    urlParameters);
			return resdest;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resdest.contains("success"))
			{
				destparsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), resdest, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	public void destparsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(resdest);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();
			oslist.add(null);
			sp_busdest.setAdapter(null);
			List<String> lables1 = new ArrayList<String>();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				
				String stop=data1.getString("stop");
				
				
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("stop", stop);
	            
	            
	            oslist1.add(map);
	            
	            lables1.add(oslist1.get(i).get("stop"));
	            
	            sarray_dusdest=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lables1);
	            sarray_dusdest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            sp_busdest.setAdapter(sarray_dusdest);
	            
	            sp_busdest.setOnItemSelectedListener(new OnItemSelectedListener()
		        {

			    	
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						sbdest=arg0.getItemAtPosition(arg2).toString();
						
						((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
						
						Toast.makeText(getApplicationContext(), ""+sbdest, Toast.LENGTH_SHORT).show();
		
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub	
					}
		        	
		        });
			    
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	public class Addtransport extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters= "username="+ URLEncoder.encode(stun, "UTF-8")+ "&&" 
						+ "con_id=" + URLEncoder.encode(appPreference.getUserId(), "UTF-8")+ "&&" 
                        + "src=" + URLEncoder.encode(sbssrc, "UTF-8")+ "&&" 
                        + "dest=" + URLEncoder.encode(sbdest, "UTF-8")+ "&&" 
                        + "time=" + URLEncoder.encode(systime, "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			myresponse = Connectivity.excutePost(Constants.ADDTRANSPORT_URL,
                    urlParameters);
			return myresponse;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(myresponse.contains("success"))
			{
				Toast.makeText(getApplicationContext(), myresponse, Toast.LENGTH_SHORT).show();
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), myresponse, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	

}
