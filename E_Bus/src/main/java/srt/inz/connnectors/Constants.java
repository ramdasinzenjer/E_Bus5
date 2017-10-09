package srt.inz.connnectors;


public interface Constants {

    //Progress Message
    String LOGIN_MESSAGE="Logging in...";
    String REGISTER_MESSAGE="Register in...";

    //Urls
    String BASE_URL="http://192.168.1.6/ebus/";
    String REGISTER_URL=BASE_URL+"mRegisterUser.php?";
    String LOGIN_URL=BASE_URL+"mLoginUser.php?";
    String NOTIFICATIONFETCH_URL=BASE_URL+"mNotifetch.php?";
    String BUSNOFETCH_URL=BASE_URL+"mBusinofetch.php?";
    String BUSLOCFETCH_URL=BASE_URL+"mBusloc.php?";
    String BUSBWSTOPS=BASE_URL+"mBusdatafetch.php?";
    String USERDETAILS=BASE_URL+"mUserdetails.php?";
    String BUSNOFETCH2_URL=BASE_URL+"mBusinofetch2.php?";
    
    String USERAPPROVE=BASE_URL+"mUserapprove.php?";
    String BUSDATAIN_URL=BASE_URL+"mBusinfoin.php?";
    String NOTIFICATIONUP_URL=BASE_URL+"mNotiup.php?";
    String LOCATIONUP_URL =BASE_URL+"mLocup.php?";
    String BUSSTOPFETCH_URL =BASE_URL+"mBusstops.php?";
    String BUSSTOPUPDATE_URL=BASE_URL+"mBusstopup.php?";
    
    String CARDDETAILS_URL=BASE_URL+"mCarddetails.php?";
    String ADDTRANSPORT_URL=BASE_URL+"mTransportreg.php?";
    String CARDREQ_PAY_URL=BASE_URL+"mCardpayment.php?";
    
    String CARDDETAILS=BASE_URL+"mfullCarddetails.php?";
    String CARDPAYMENTDETAILS_URL=BASE_URL+"mPaymentdet.php?";
    String USERAPPROVECARD=BASE_URL+"mApprovecard.php?";
    
    //Details
    String PASSWORD="Password";
    String USERNAME="Username";
    String LOGINSTATUS="LoginStatus";
    
    //SharedPreference
    String PREFERENCE_PARENT="Parent_Pref";
	
   
}
