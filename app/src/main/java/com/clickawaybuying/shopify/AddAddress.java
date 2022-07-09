package com.clickawaybuying.shopify;
//imports
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.classes.AddressData;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class AddAddress extends AppCompatActivity {
    //initialize variables
    public static EditText mFullname,mPhone,mPostal,mLotNum;
    Button mSubmit;
    ImageButton mBack;
    RequestQueue requestQueue;
    TextView result;
    SwitchMaterial mSwitch;
    private boolean defaultAddress=false;
    Spinner mRegion,mProvince,mCity,mBarangay;
    Boolean update;
    int id;
    String selectedRegion,selectedProvince,selectedCity,selectedBarangay,err = "",respo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        //variable assignment
        mFullname = findViewById(R.id.addFullName);
        mPhone = findViewById(R.id.addPhoneNum);
        mRegion = findViewById(R.id.addRegion);
        mProvince = findViewById(R.id.addProvince);
        mCity = findViewById(R.id.addCity);
        mBarangay = findViewById(R.id.addBarangay);
        mPostal = findViewById(R.id.addPostal);
        mSubmit = findViewById(R.id.btnAddSubmit);
        result = findViewById(R.id.dres);
        mSwitch = findViewById(R.id.addressDefault);
        mBack = findViewById(R.id.addressBackBtn);
        mLotNum = findViewById(R.id.lotNum);

        //calling methods
        regionSpinner();
        defaultSpinners();
        catchIntent();

        mRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//region spinner onclick listener
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedRegion = parentView.getItemAtPosition(position).toString();//gets the selected item in spinner by position then assign as variable
                if (selectedRegion.equals("Metro Manila")){//if dropdown menu selected was Metro Manila, call method manilaProvince()
                        manilaProvince();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        mProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//province spinner onlick listener
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProvince = adapterView.getItemAtPosition(i).toString();//gets the selected item in spinner by position then assign as variable
                if (selectedProvince.equals("Metro Manila")){//if dropdown menu selected was Metro Manila, call method showManila()
                    showManilaCity();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//city spinner onlick Listener
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity = adapterView.getItemAtPosition(i).toString();//gets the selected item in spinner by position then assign as variable
                if (selectedCity.equals("Binondo")){
                    BinondoBrgy();
                }
                if (selectedCity.equals("Caloocan City")){//if dropdown menu selected was Caloocan City, call method showCaloocanBarangay
                        showCaloocanBarangay();
                }
                if (selectedCity.equals("Ermita")){
                    ErmitaBrgy();
                }
                if (selectedCity.equals("Intramuros")){
                    IntramurosBrgy();
                }
                if (selectedCity.equals("Las Pinas City")){
                    LasPinasBrgy();
                }
                if (selectedCity.equals("Makati City")){
                    MakatiBrgy();
                }
                if (selectedCity.equals("Malabon City")){
                    MalabonBrgy();
                }
                if (selectedCity.equals("Malate")){
                    MalateBrgy();
                }
                if (selectedCity.equals("Mandaluyong City")){
                    MandaluyongBrgy();
                }
                if (selectedCity.equals("Marikina City")){
                    MarikinaBrgy();
                }
                if (selectedCity.equals("Muntinlupa City")){
                    MuntinlupaBrgy();
                }
                if (selectedCity.equals("Navotas City")){
                    NavotasBrgy();
                }
                if (selectedCity.equals("Paco")){
                    PacoBrgy();
                }
                if (selectedCity.equals("Paranaque City")){
                    ParanaqueBrgy();
                }
                if (selectedCity.equals("Pasay City")){
                    PasayBrgy();
                }
                if (selectedCity.equals("Pateros")){
                    PaterosBrgy();
                }
                if (selectedCity.equals("Port Area")){
                    PortAreaBrgy();
                }
                if (selectedCity.equals("Quezon City")){
                    QuezonBrgy();
                }
                if (selectedCity.equals("Quiapo")){
                    QuiapoBrgy();
                }
                if (selectedCity.equals("Sampaloc")){
                    SampalocBrgy();
                }
                if (selectedCity.equals("San Juan City")){
                    SanJuanBrgy();
                }
                if (selectedCity.equals("San Miguel")){
                    SanMiguelBrgy();
                }
                if (selectedCity.equals("San Nicolas")){
                    SanNicolasBrgy();
                }
                if (selectedCity.equals("Santa Ana")){
                    SantaAnaBrgy();
                }
                if (selectedCity.equals("Santa Cruz")){
                    SantaCruzBrgy();
                }
                if (selectedCity.equals("Taguig City")){
                    TaguigBrgy();
                }
                if (selectedCity.equals("Tondo I/II")){
                    TondoBrgy();
                }
                if (selectedCity.equals("Valenzuela City")){
                    ValenzuelaBrgy();
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBarangay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//barangay spinner onlick listener
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBarangay = adapterView.getItemAtPosition(i).toString();//gets the selected item in spinner by position then assign as variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//button submit onclick listener

                if (mSwitch.isChecked())//if switch is on make address as default
                {
                    defaultAddress=true;
                }
                else if (mFullname.length()<1){//checks if field is empty
                    mFullname.setError("This field is required");//sets an error in textbox
                    mFullname.requestFocus();//set edittext as active
                    return;
                }
                else if (mPhone.length()<1){//checks if field is empty
                    mPhone.setError("Please insert a phone number");//sets an error in textbox
                    mPhone.requestFocus();//set edittext as active
                    return;
                }
                else if (mPhone.length()!=10){//checks if entered phone number is valid
                    mPhone.setError("Invalid phone number");//sets an error in textbox
                    mPhone.requestFocus();//set edittext as active
                    return;
                }
                else if (mLotNum.length()<1){//checks if field is empty
                    mLotNum.setError("Please insert your lot number");//sets an error in textbox
                    mLotNum.requestFocus();//set edittext as active
                    return;
                }
                else if (selectedRegion.equals("Set Region"))//checks if user did not select anything in dropdown
                {
                    Toast.makeText(AddAddress.this, "Please select a Region", Toast.LENGTH_SHORT).show();
                }
                else if (selectedCity.equals("Set City"))//checks if user did not select anything in dropdown
                {
                    Toast.makeText(AddAddress.this, "Please select a City", Toast.LENGTH_SHORT).show();
                }
                else if (selectedProvince.equals("Set Province"))//checks if user did not select anything in dropdown
                {
                    Toast.makeText(AddAddress.this, "Please select a Province", Toast.LENGTH_SHORT).show();
                }
                else if (selectedCity.equals("Set Barangay"))//checks if user did not select anything in dropdown
                {
                    Toast.makeText(AddAddress.this, "Please select a Barangay", Toast.LENGTH_SHORT).show();
                }
                else if (mPostal.length()<1){//checks if field is empty
                    mPostal.setError("This field is required");//sets an error in textbox
                    mPostal.requestFocus();//set edittext as active
                    return;
                }
                else
                {
                    defaultAddress = false;
                    conditions();//calls a method
                }
            }
        });

    }
    public void defaultSpinners(){//method for setting spinners default value
        String[] provinceSpinner = new String[] {
                "Set Province"
        };
        String[] citySpinner = new String[] {
                "Set City"
        };
        String[] barangaySpinner = new String[] {
                "Set Barangay"
        };
        provinceAdapter(provinceSpinner);
        cityAdapter(citySpinner);
        barangayAdapter(barangaySpinner);
    }
    public void showCaloocanBarangay(){//method for showing caloocan barangays
        String[] arraySpinner = new String[] {
                "Set Barangay", "Barangay 1","Barangay 2","Barangay 3","Barangay 4","Barangay 5","Barangay 6","Barangay 7","Barangay 8","Barangay 9","Barangay 10","Barangay 11"
                ,"Barangay 12","Barangay 13","Barangay 14","Barangay 15","Barangay 16","Barangay 17","Barangay 18","Barangay 19","Barangay 20","Barangay 21","Barangay 22","Barangay 23","Barangay 24"
                ,"Barangay 25","Barangay 26","Barangay 27","Barangay 28","Barangay 29","Barangay 30"
        };
        barangayAdapter(arraySpinner);

    }

    private void barangayAdapter(String[] arraySpinner) {
        Spinner s = (Spinner) findViewById(R.id.addBarangay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    public void showManilaCity(){//method for showing Manila Cities
        String[] arraySpinner = new String[] {
                "Set City", "Binondo","Caloocan City","Ermita","Intramuros","Las Pinas City","Makati City","Malabon City","Malate","Mandaluyong City","Marikina City","Muntinlupa City"
                ,"Navotas City","Paco","Pandacan","Paranaque City","Pasay City","Pasig City","Pateros","Port Area","Quezon City","Quiapo","Sampaloc","San Juan City","San Miguel"
                ,"San Nicolas","Santa Ana","Santa Cruz","Taguig City","Tondo I/II","Valenzuela City"
        };
        cityAdapter(arraySpinner);

    }

    private void cityAdapter(String[] arraySpinner) {
        Spinner s = (Spinner) findViewById(R.id.addCity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    public void manilaProvince(){//method for showing Metro Manila in province
        String[] arraySpinner = new String[] {
                "Set Province", "Metro Manila"
        };
        provinceAdapter(arraySpinner);
    }

    private void provinceAdapter(String[] arraySpinner) {
        Spinner s = (Spinner) findViewById(R.id.addProvince);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    public void regionSpinner(){//method for showing Regions
        String[] arraySpinner = new String[] {
                "Set Region", "Metro Manila", "Mindanao", "North Luzon", "South Luzon", "Visayas"
        };
        regionAdapter(arraySpinner);

    }

    private void regionAdapter(String[] arraySpinner) {
        Spinner s = (Spinner) findViewById(R.id.addRegion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    public void conditions(){//checks if user wants to update address or not

        if (update){//if update is true then use method and get back
            addressUpdate();
            AddAddress.super.onBackPressed();
        }
        else {//insert the address to database
            final String user_name  = PreferenceUtils.getEmail(AddAddress.this);//get shared pref username
            String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertAddress.php";//access webhost php file
            requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
            StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
                @Override
                public void onResponse(String response) {//gets the php json responses
                    //Toast.makeText(AddAddress.this, ""+response.length(), Toast.LENGTH_SHORT).show();
                    respo = response;
                }
            }, new Response.ErrorListener() {//gets php error messages
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_LONG).show();
                    err = String.valueOf(error);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {//parameters
                    Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                    parameters.put("fullname",mFullname.getText().toString());
                    parameters.put("phone",mPhone.getText().toString());
                    parameters.put("lotNum",mLotNum.getText().toString());
                    parameters.put("barangay",selectedBarangay);
                    parameters.put("city",selectedCity);
                    parameters.put("province",selectedProvince);
                    parameters.put("region",selectedRegion);
                    parameters.put("postal",mPostal.getText().toString());
                    parameters.put("username",user_name);

                    return parameters;
                }
            };
            requestQueue.add(request);
            if (err.length() > 0 || respo.length() > 0){
                Toast.makeText(this, "An error occurred, "+err+respo, Toast.LENGTH_SHORT).show();
            }
            else{
                final ProgressDialog progress = new ProgressDialog(AddAddress.this);//progressbar
                progress.setTitle("Loading");//progressbar title
                progress.setMessage("Please wait...");//progressbar message
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();//show progressbar
                // To dismiss the dialog
                new CountDownTimer(2000, 1000) {//timer
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {//when timer ends
                        progress.dismiss();//terminate progressbar
                        Toast.makeText(AddAddress.this, "Address Saved Successfully", Toast.LENGTH_SHORT).show();//toast message
                        ChooseAddress.reloadNedeed = true;//set boolean reloadneeded as true
                        finish();//kill activity
                    }
                }.start();//start timer
            }

        }
    }
    public void addreBack(View v){
        super.onBackPressed();
    }//address back button
    String region,barangay,province,city;//String assignment
    public void catchIntent(){
        // Catching incoming intent
        Intent intent = getIntent();
        id =  intent.getIntExtra("id", 0);
        String fullname = intent.getStringExtra("fullname");
        int phone = intent.getIntExtra("phone", 0);
        int lotNum = intent.getIntExtra("lotNum", 0);
        region = intent.getStringExtra("region");
        barangay = intent.getStringExtra("barangay");
        province = intent.getStringExtra("province");
        city = intent.getStringExtra("city");
        int postal = intent.getIntExtra("postal", 0);
        update = intent.getBooleanExtra("update",false);

        mFullname.setText(fullname);
        if (!(phone == 0)){
            mPhone.setText(String.valueOf(phone));
        }
        if (!(lotNum == 0)){
            mLotNum.setText(String.valueOf(lotNum));
        }
        if (!(postal == 0)){
            mPostal.setText(String.valueOf(postal));
        }


    }
    public void spinnerCatcher(){

        String[] regionSpinner = new String[] {
                region,"Metro Manila", "Mindanao", "North Luzon", "South Luzon", "Visayas"
        };
        Spinner d = (Spinner) findViewById(R.id.addRegion);
        ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, regionSpinner);
        dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d.setAdapter(dAdapter);

        String[] provinceSpinner = new String[] {
                province
        };
        Spinner a = (Spinner) findViewById(R.id.addProvince);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, provinceSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        a.setAdapter(adapter);

        String[] citySpinner = new String[] {
                city
        };
        Spinner b = (Spinner) findViewById(R.id.addCity);
        ArrayAdapter<String> bAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citySpinner);
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.setAdapter(bAdapter);

        String[] barangaySpinner = new String[] {
                barangay
        };
        Spinner c = (Spinner) findViewById(R.id.addBarangay);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, barangaySpinner);
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        c.setAdapter(cAdapter);

    }
    public void addressUpdate(){//method for updating address

        result.setText(String.valueOf(defaultAddress));
        final String user_name  = PreferenceUtils.getEmail(AddAddress.this);
        String updateURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/updateAddress.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest updateRequest = new StringRequest(Request.Method.POST, updateURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                parameters.put("fullname",mFullname.getText().toString());
                parameters.put("phone",mPhone.getText().toString());
                parameters.put("lotNum",mLotNum.getText().toString());
                parameters.put("barangay",selectedBarangay);
                parameters.put("city",selectedCity);
                parameters.put("province",selectedProvince);
                parameters.put("region",selectedRegion);
                parameters.put("postal",mPostal.getText().toString());
                parameters.put("id",String.valueOf(id));
                parameters.put("username",user_name);


                return parameters;
            }
        };
        requestQueue.add(updateRequest);
        Toast.makeText(AddAddress.this, "Address Updated Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddAddress.this,ChooseAddress.class);//go to chooseaddress.class
        startActivity(i);
        finish();
    }
    public void BinondoBrgy(){
        String[] str = new String[10];
        for(int i = 287;i <= 296;i++){
                str[i-287] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void ErmitaBrgy(){
        String[] str = {"Barangay 659", "Barangay 659-A", "Barangay 660", "Barangay  660-A", "Barangay 661", "Barangay 663", "Barangay 663-A", "Barangay 664", "Barangay 666", "Barangay 667", "Barangay 668", "Barangay 669", "Barangay 670"};
        barangayAdapter(str);
    }
    private void IntramurosBrgy(){
        String[] str = new String[5];
        for(int i = 4;i <= 8;i++){
            str[i-4] = "Barangay 65" + i;
        }
        barangayAdapter(str);
    }
    private void LasPinasBrgy(){
        String[] str = {"Almanza Dos", "Almanza Uno","B.F International Village","Danile Fajardo", "Elias Aldana","Ilaya", "Munayo Dos", "Munayo Uno", "Pamplona Dos","Pamplona Tres", "Pamplona Uno", "Pilar", "Pulang Lupa Dos", "Talon Kuatro", "Talon Singko", "Talon Tres", "Talon Uno", "Zapote"};
        barangayAdapter(str);
        }
    private void MakatiBrgy() {
        String[] str = {"Bangkal", "Bel-air", "Carmona","Cembo","Comembo","Dasmarinas","East Rembo", "Forbes Park", "Guadalupe Nuevo", "Gadalupe Viejo", "Kasilawan", "La Paz","Magallanes","Olympia","Palanan", "Pembo", "Pinagkaisahan", "Pio Del Pilar", "Pitogo", "Poblacion", "Post Proper Northside", "Post Proper Southside", "Rizal", "San Antonio", "San Isidro", "San Lorenzo", "Santa Cruz","Singkamas", "South Cembo", "Tejeros", "Urdaneta", "Valenzuela", "West Rembo" };
        barangayAdapter(str);
        }
    private void MalabonBrgy(){
        String[] str = {"Acacia","Baritan", "Bayan-Bayanan", "Catmon","Conception","Dampalit","Flores","Hulong Duhat", "Ibaba, Longos", "Maysilo","Muzon","Niugan","Panghulo","Potrero","San Agustin","Santolohan","Tanong","Tinajeros","Tonsuya","Tugatog"};
        barangayAdapter(str);
    }
    private void MalateBrgy(){
        String[] str = new String[57];
        for(int i = 688;i <= 744;i++){
            str[i-688] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void MandaluyongBrgy(){
        String[] str = {"Addition Hills", "Bagong Silang","Barangka Drive", "Barangka Ibaba", "Barangka Ilaya", "Barangka Itaas", "Buayang Bato", "Burol", "Daang Bakal", "Hagdang Bato Itaas", "Hagdang Bato Libis","Harapan Ang Bukasm Highway Hills" , "Hulo", "Mabini-J. Rizal","Malamig", "Mauway", "Namayan", "New Zaniga", "Old Zaniga","Pag-asa", "Plainview","Pleasant Hills", "Poblacion","San Jose", "Vergara", "Wack-Wack Greenhills"};
        barangayAdapter(str);
    }
    private void MarikinaBrgy(){
        String[] str = {"Baranka","Calumpang","Conception Dos","Conception Uno","Fortune","Industrial Valley", "Jesus De La Pena", "Malanday", "Marikina Heights (Concepcion)","Nangka", "Parang", "San Roque","Santa Elena", "Santo Nino", "Tanong", "Tumana"};
        barangayAdapter(str);
    }
    private void MuntinlupaBrgy(){
        String[] str = {"Alabang", "Ayala Alabang", "Bayanan", "Buli", "Cupang", "Poblacion", "Putatan", "Sucat", "Tunasan"};
        barangayAdapter(str);
    }
    private void NavotasBrgy(){
        String[] str = {"Bagumabayan North","Bagumbayan South", "Bangculasi", "Daanghari", "Navotas East", "Navotas West", "North Bay Blvd", "San Jose", "San Rafael Village", "San Roque", "Sipac-Almacen","Tangos","Tanza"};
        barangayAdapter(str);
    }
    private void PacoBrgy(){
        String[] str = new String[42];
        str[0] = "Barangay 662";
        str[1] = "Barangay 664-A";
        for(int i = 671;i <= 687;i++){
            str[i-669] = "Barangay " + i;
        }
        for(int i = 809;i <= 832;i++){
            str[i-791] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void PandacanBrgy(){
        String[] str = new String[40];
        for(int i = 833;i <= 872;i++){
            str[i-833] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void ParanaqueBrgy(){
        String[] str = {"B.F Homes", "Baclaran", "Don Bosco", "Don Galo", "La Huerta", "Marcelo Green Village", "Merville", "Moonwalk","San Antonio", "San Dionisio", "San Isidoro","San Martin De Porres", "Santo Nino", "Sun Valley", "Tambo", "Vitalez"};
        barangayAdapter(str);
    }
    private void PasigBrgy(){
        String[] str = new String[201];
        for(int i = 1;i <= 201;i++){
            str[i-1] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void PasayBrgy(){
        String[] str = {"Bagong Ilog","Bagong Katipunan", "Bambang", "Buting", "Caniogan", "Dela Paz", "Kalawaan", "Kapasigan", "Kapitolyo", "Malinao", "Manggahan", "Maybunga", "Oranbo", "Palatiw", "Pinagbuhatan", "Pineda", "Rosario","Sagad", "San Antonio", "San Joaquin","San Jose", "San Miguel", "San Nicolas", "Santa Cruz", "Santa Lucia", "Santa Rosa", "Santo Tomas", "Santolan", "Sumilang", "Ugong"};
        barangayAdapter(str);
    }
    private void PaterosBrgy(){
        String[] str = {"Aguho","Magtanggol", "Martires Del 96", "Poblacion", "San Pedro", "San Roque", "Santa Ana", "Santo Rosario-Kanluran","Santo Rosario-Silangan", "Tabacalera"};
        barangayAdapter(str);
    }
    private void PortAreaBrgy(){
        String[] str = new String[6];
        for(int i = 649;i <= 653;i++){
            str[i-649] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void QuezonBrgy(){
        String[] str = {"Alicia","Amihan","Apolonio Samson","Aurora","Baesa", "Bagbag", "Bagong Lipunan Ng Crame", "Pagong Pag-Asa", "Bagong Silangan", "Bagumbayan","Bagumbuhay","Bahay Toro", "Balingasa", "Balong Bato","Batasan Hills","Bayanihan","Blue Ridge A", "Blue Ridge B","Botocan","Bungad", "Camp Aguinaldo", "Capri","Central", "Claro","Commonwealth","Culiat","Damar","Damayan","Damayang Lagi","Del Monte","Dioquino Zobel", "Don Manuel Dona Imelda", "Dona Josefa","Duyan-Duyan","E.Rodriguez","East Camias","Escopa I","Escopa II", "Escopa III","Escopa IV","Fairview", "Greater Lagro", "Gulod", "Holy Spirit","Horseshoe","Immaculate Conception","Kaligayahan","Kalusugan","Kamuning","Katipunan","Kaunlaran","Kristong Hari","Krus Na Ligas", "Laging Handa","Libis","Lourdes","Layola Heights","Maharlika","Malaya","Mangga","Manresa","Mariana","Mariblo","Marilag","Masana","Masambong","Matandang Balara", "Milagrosa", "N.s. Aoranto (Gintong Silahis)", "Nagkaisang Nayon", "Nayong Kanluran","New Era(Constitution Hills)","North Fairview","Novaliches Proper", "Obrero", "Old Capitol Site","Paang Bundok","Pag-ibig Sa Nayon", "Paligsahan","Paltok","Pansol","Paraiso","Pasong Putik Proper","Pasong Tamo", "Payatas", "Phil-Am","Pinagkaisahan","Pinyahan","Project 6","Quirino 2A", "Quirino 2B", "Quirino 2C", "Quirino 3A", "Ramon Magsaysay", "Roxas", "Sacred Heart", "Saint Ignatius", "Saint Peter", "Salvacion", "San Agustin", "San Antonio","San Bartolome", "San Isidro", "San Isidro Labrador", "San Jose", "San Martin De Porres", "San Roque", "San Vicente", "Sangandaan" , "Santa Cruz", "Santa Lucia", "Santa Monica", "Santa Teresita","Santo Cristo", "Santo Domingo","Santo Nino", "Santol", "Sauyo", "Sienna", "Sikatuna Village", "Silangan", "Soccoro", "South Triangle", "Tagumpay","Talayan", "Talipapa", "Tandang Sora", "Tatalon", "Teachers Village East", "Teachers Village West", "U.p. Campus", "U.p. Village", "Ugong Norte", "Unang Sigaw","Valencia", "Vasra","Veterans Village", "Villa Maria Clara", "West Kamias","West Triangle", "White Plains"};
        barangayAdapter(str);
    }
    private void QuiapoBrgy(){
        String[] str = new String[17];
        for(int i = 306;i <= 309;i++){
            str[i-306] = "Barangay " + i;
        }
        for(int i = 383;i <= 394;i++){
            str[i-379] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void SampalocBrgy(){
        String[] str = new String[242];
        for(int i = 649;i <= 653;i++){
            str[i-649] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void SanJuanBrgy(){
        String[] str = {"Addition Hills", "Balong Bato", "Batis", "Corazon De Jesus", "Ermitano", "Greenhills", "Halo-Halo(St.Joseph)","Isabelita", "Kababayan", "Little Baguio", "Maytunas","Onse","Pasadena","Pedro Cruz", "Progreso", "Rivera", "Salapan", "San Perfecto", "Santa Lucia", "Tigban", "West Crame"};
        barangayAdapter(str);
    }
    private void SanMiguelBrgy(){
        String[] str = new String[12];
        for(int i = 637;i <= 648;i++){
            str[i-637] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void SanNicolasBrgy(){
        String[] str = new String[19];
        for(int i = 268;i <= 286;i++){
            str[i-268] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void SantaAnaBrgy(){
        String[] str = new String[161];
        for(int i = 745;i <= 905;i++){
            str[i-745] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void SantaCruzBrgy(){
        String[] str = new String[86];
        for(int i = 297;i <= 382;i++){
            str[i-297] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void TaguigBrgy(){
        String[] str = {"Bagumbayan","Bambang","Calzada","Central Bicutan","Central Cignal Village", "Fort Bonifacio", "Hagonoy", "Ibayo-Tipas","Katuparan", "Ligid-Tipas", "Lower Bicutan","Maharlika Village", "Napindan","New Lower Bicutan", "North Daan Hari", "North Signal Village", "Palingon", "Pinagsama", "San Miguel","Santa Ana", "South Daan Hari","South Signal Village", "Tanyag","Tuktukan","Upper Bicutan", "Ususan", "Wawa"," Western Bicutan"};
        barangayAdapter(str);
    }
    private void TondoBrgy(){
        String[] str = new String[268];
        for(int i = 1;i <= 267;i++){
            str[i-1] = "Barangay " + i;
        }
        barangayAdapter(str);
    }
    private void ValenzuelaBrgy(){
        String[] str = {"Arkong Bato","Bagbaguin","Balankas","Bignay","Bisig","Canumay East","Canumay West","Coloong","Dalandanan","Hen. T. De Leon", "Isla","Karuhatan","Lawang Bato","Lingunan","Malabo","Malanday","Malinta","Mapulang Lupa","Marulas","Maysan","Palasan","Parada","Pariancillo Villa","Paso De Blas", "Pasola", "Poblacion", "Pulo","Punturin","Rincon","Tagalog", "Ugong", "Viente Reales", "Wawang Pulo"};
        barangayAdapter(str);
    }

    }
