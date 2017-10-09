package srt.inz.e_bus;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AdminApproveCards extends Activity{
	
	ListView mlist;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	String resdb,resdbup;	ApplicationPreference appPref;
	ListAdapter adapter;
	
	String username,card_type,issued_date,exp_date,status,sun,resp; 
	
	 TextView tvs; String issdt,expdt,cdtyp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminapprovecard);
		appPref=(ApplicationPreference)getApplication();
		mlist=(ListView)findViewById(R.id.mlist_cardreq);
		
		tvs=(TextView)findViewById(R.id.mtx_viewdetails);
		
	
		new CardlistApiTask().execute();
		
	}

	public class CardlistApiTask extends AsyncTask<String, String, String>
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
			resdb = Connectivity.excutePost(Constants.CARDDETAILS,
                    urlParameters);
			Log.e("AdminCard", resdb);
			return resdb;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			keyparser(resdb);
			Toast.makeText(getApplicationContext(), ""+resdb, Toast.LENGTH_SHORT).show();
		}

}
	

	public void keyparser(String result)
	{
		try
		{
			JSONObject  jObject = new JSONObject(result);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				username=data1.getString("username");
				card_type=data1.getString("card_type");
				issued_date=data1.getString("issued_date");
				exp_date=data1.getString("exp_date");
				status=data1.getString("status");
				 
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("username", username);
	            map.put("card_type", card_type);
	            map.put("issued_date", issued_date);
	            map.put("exp_date", exp_date);
	            map.put("status", status);   
           	         
	            oslist.add(map);
	            
	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	                R.layout.layout_single,
	                new String[] {"username"}, new int[] {R.id.mtext_single});
	            mlist.setAdapter(adapter);
	            
	            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	               
				@Override
	               public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {               
	               Toast.makeText(getApplicationContext(), 
	            		   " "+oslist.get(+position).get("username"), Toast.LENGTH_SHORT).show();	
	               openDialog(oslist.get(+position).get("status"), 
	            		   oslist.get(+position).get("card_type"),
	            		   oslist.get(+position).get("issued_date"), 
	            		   oslist.get(+position).get("exp_date"),
	            		   oslist.get(+position).get("username"));
	               
	               }
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	public void openDialog(String stat, String scdtyp, String isdat, String expd, String sunam){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      
	   final String mstat=stat; cdtyp=scdtyp;  issdt=isdat;
	   expdt=expd; sun=sunam;
	   	  
		   	alertDialogBuilder.setTitle("Please choose an action!");
		    alertDialogBuilder.setMessage("User Name : "+sun+"\n Issued Date : "+issdt+"\n Date of Expiry : "+expdt);
	 if(mstat.equals("0"))    { 
		    alertDialogBuilder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	        //    Toast.makeText(getApplicationContext(),"Approved",Toast.LENGTH_SHORT).show();
	            new UpdateCardStateApiTask().execute();
       
	         }
	      });
	   }
	      
	      alertDialogBuilder.setNegativeButton("View",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	
	        	 Toast.makeText(getApplicationContext(),"View ",Toast.LENGTH_SHORT).show();
	        	 
	        	 new Checkuserpaymentdata().execute();
	        	 
	         }
	      });
	      
	    	
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
	   }
	
	public class UpdateCardStateApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters= "username=" +URLEncoder.encode(sun,"UTF-8")
						+"&&"+"issued_date=" +URLEncoder.encode(issdt,"UTF-8")
						+"&&"+"exp_date=" +URLEncoder.encode(expdt,"UTF-8")
						;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resdbup = Connectivity.excutePost(Constants.USERAPPROVECARD,
                    urlParameters);
			Log.e("AdminApproval", resdbup);
			return resdbup;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), ""+resdbup, Toast.LENGTH_SHORT).show();
			
		}

}
	
	public class Checkuserpaymentdata extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "username="+ URLEncoder.encode(sun, "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resp = Connectivity.excutePost(Constants.CARDPAYMENTDETAILS_URL,
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
				
				String cardd_type=data1.getString("card_typ");
				String expp_date=data1.getString("expiry_date");
				
				String cardd_no=data1.getString("card_no");
            		    
				tvs.setText("Payment Type : "+cardd_type+"Card Number : "+cardd_no+"\n Expiry Date : "+expp_date
						+"\n CARD : "+cdtyp);
			
			}
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
}
