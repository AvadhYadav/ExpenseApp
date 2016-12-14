package com.example.vasu.expense_manager;

import android.app.DatePickerDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.vasu.expense_manager.Model.Person;
import com.example.vasu.expense_manager.interfaces.ServerAPI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";

    private CoordinatorLayout coordinatorLayout;
    public Calendar Calendar;
    public TextView DateOne;
    public DatePickerDialog.OnDateSetListener DateSetter;
    public TextView DateTwo;
    public Date TempDate;
    public int currID;
    public boolean selected = false;
    public boolean tempSelected = false;
    public String prevDateOneValue;
    public int prevRadioValue;
    public int tempCounter = 0;
    public Spinner spinner;
    public static SQLiteDatabase Database;
    private HomeFragment homeFragment;
    public static int currTab;

    public TextView youOweTextView;
    public TextView youGetTextView;

    public SimpleDateFormat numberFormat = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat textFormat = new SimpleDateFormat("MMMM dd, yyyy");
    public SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");

    private GoogleApiClient mGoogleApiClient;
    public TextView nav_name, nav_email;
    public ImageView nav_image;
    public NavigationView navigationView;

    public static final String ROOT_URL = "http://192.168.65.196:8085/Spring4/";


//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Database = openOrCreateDatabase("Database",MODE_PRIVATE,null);
        Database.execSQL("CREATE TABLE IF NOT EXISTS Expenses(ID INTEGER PRIMARY KEY,Username VARCHAR,Amount INTEGER, Category VARCHAR,Description VARCHAR, Date VARCHAR);");
        Database.execSQL("CREATE TABLE IF NOT EXISTS Debts(ID INTEGER PRIMARY KEY,User VARCHAR,Amount INTEGER, Description VARCHAR, Type VARCHAR, Confirmation VARCHAR, Date VARCHAR);");
        youGetTextView = (TextView) findViewById(R.id.you_get_text_view);
        youOweTextView = (TextView) findViewById(R.id.you_owe_text_view);

        DateOne = (TextView) findViewById(R.id.Date1);
        SimpleDateFormat textFormat = new SimpleDateFormat("MMMM ,yyyy");
        RadioGroup radiogroup = (RadioGroup) findViewById(R.id.RadioGroup1);

        ////////////////////RESTORE INSTANCE/////////////////////////////////////
        if(savedInstanceState!=null) {
            prevDateOneValue = savedInstanceState.getString("prevDateOneValue");
            DateOne.setText(prevDateOneValue);
            ResetSelectDate();

        }else {
            prevRadioValue = radiogroup.getCheckedRadioButtonId();
            Date date = new Date(System.currentTimeMillis());
            DateOne.setText(textFormat.format(date));
            String date1 = textFormat.format(date).split(",")[0]+"1, "+ textFormat.format(date).split(",")[1];
            String date2 = textFormat.format(date).split(",")[0]+"32, "+ textFormat.format(date).split(",")[1];
            loadExpenses(date1,date2, 2);
        }

//        getData(ROOT_URL+"debtmanagertableController/getAllDebt1?requestUserID=a&confirmationUserID=b");

//
        /////////////////////Getting data from server//////////////////////
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

//       Retrofit retrofit = new Retrofit.Builder().baseUrl("http:// 192.168.65.196:8085/").addConverterFactory(GsonConverterFactory.create()).build();

//        RestAdapter retrofit = new RestAdapter.Builder()
//                .setEndpoint(ROOT_URL)
//
//                .build();


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ROOT_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//
//        ServerAPI service = retrofit.create(ServerAPI.class);
//        Call<List<Person>> call = service.getDetails("a","b");
//        call.enqueue(new retrofit2.Callback<List<Person>>() {
//            @Override
//            public void onResponse(Call<List<Person>> call, retrofit2.Response<List<Person>> response) {
//                Toast.makeText(MainActivity.this, "TRUE", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<List<Person>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "FALSE", Toast.LENGTH_SHORT).show();
//            }
//        });

//        Call<List<Person>> debts = service.getDetails("a","b");
//        debts.enqueue(new retrofit2.Callback<List<Person>>() {
//            @Override
//            public void onResponse(Call<List<Person>> call, retrofit2.Response<List<Person>> response) {
//
//                Toast.makeText(MainActivity.this, "TRUE", Toast.LENGTH_SHORT).show();
//                List<Person> debtLedger = response.body();
//                for(Person p : debtLedger)
//                {
//                    Log.e("GOOD", p.getamount()+"");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Person>> call, Throwable throwable) {
//
//                Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
//            }
//        });


        ////////////////////////////////////////////////////////////////////////

        //Mah Work
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        nav_email = (TextView) hView.findViewById(R.id.nav_email);
        nav_image = (ImageView) hView.findViewById(R.id.nav_pic);
        nav_name = (TextView) hView.findViewById(R.id.nav_name);


        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.v("Main Activity", "Entered");
            GoogleSignInResult result = opr.get();
            String profname = result.getSignInAccount().getDisplayName().toString();
            String profemail = result.getSignInAccount().getEmail().toString();
            nav_email.setText(profemail);
            nav_name.setText(profname);
            Log.v("Entered", profname + " " + profemail);
            if(result.getSignInAccount().getPhotoUrl() != null) {
                Log.v("Profile image", "Found");
                String personPhotoUrl = result.getSignInAccount().getPhotoUrl().toString();
//                pic_flag = 1;
                Glide.with(getApplicationContext()).load(personPhotoUrl)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(nav_image);

            }
            else{
                final Resources res = getResources();
                final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size2);

                final LetterTileProvider tileProvider = new LetterTileProvider(this, 2);
                final Bitmap letterTile = tileProvider.getLetterTile(profname, "key", tileSize, tileSize);
                nav_image.setImageBitmap(letterTile);
                Log.v("Profile image", "Not Found");
//                pic_flag = 0;
            }
        }

        ////////////////////////////////////////////////////

        //////////////////////////TOOLBAR/////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DebtApp");

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currTab==R.id.tab_home) {
                    addExpense();
                }
                else if(currTab == R.id.tab_debts){
                    addDebt();
                }
            }
        });
        //////////////////////////////////////////////////////////////////


        /////////////NAVIGATION DRAWER/////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ////////////////////////////////////////////////////////////////////

        ///////////////////////BOTTOM MENU///////////////////////////////////

        final CardView topDebtCardView = (CardView) findViewById(R.id.debt_header_card);
        final CardView topCardView = (CardView) findViewById(R.id.card_view);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottom_main);
        Log.d(String.valueOf(currTab == R.id.tab_home), "onCreate: ");
        if(currTab==R.id.tab_debts)
            bottomBar.setDefaultTab(currTab);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    currTab = R.id.tab_home;
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, new HomeFragment());
                    fragmentTransaction.commit();
                    getFragmentManager().executePendingTransactions();
                    fab.setVisibility(View.VISIBLE);
                    topCardView.setVisibility(View.VISIBLE);
                    topDebtCardView.setVisibility(View.GONE);
                } else if (tabId == R.id.tab_stats) {
                    currTab = R.id.tab_stats;
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, new StatisticsFragment());
                    fragmentTransaction.commit();
                    getFragmentManager().executePendingTransactions();
                    fab.setVisibility(View.GONE);
                    topCardView.setVisibility(View.VISIBLE);
                    topDebtCardView.setVisibility(View.GONE);

                } else if (tabId == R.id.tab_debts) {
                    currTab = R.id.tab_debts;
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, new DebtFragment());
                    fragmentTransaction.commit();
                    getFragmentManager().executePendingTransactions();
                    fab.setVisibility(View.VISIBLE);
                    topCardView.setVisibility(View.GONE);
                    topDebtCardView.setVisibility(View.VISIBLE);
                    loadDebts();
                } else {
                    fab.setVisibility(View.GONE);
                    topCardView.setVisibility(View.VISIBLE);
                    topDebtCardView.setVisibility(View.GONE);
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////



    }



    //Add Debts or Loans
    private void addDebt() {
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_debt, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add a Debt/Loan");

        final EditText name = (EditText) dialogView.findViewById(R.id.debt_name);
        final EditText desc = (EditText) dialogView.findViewById(R.id.debt_desc);
        final EditText amount = (EditText) dialogView.findViewById(R.id.debt_amount);
        final TextView dateAddDebt = (TextView) dialogView.findViewById(R.id.addDateDebt);
        final RadioButton debt_button = (RadioButton) dialogView.findViewById(R.id.radio_debt);
        final RadioButton loan_button = (RadioButton) dialogView.findViewById(R.id.radio_loan);

        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDate = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat textFormat = new SimpleDateFormat("MMMM dd,yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mySqlFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c.getTime());
        final String mySqlDate = mySqlFormat.format(c.getTime());

        try {
            Date date = numberFormat.parse(currentDate + "/" + (currentMonth + 1) + "/" + currentYear);
            dateAddDebt.setText(textFormat.format(date));
        }catch (Exception e){
            e.printStackTrace();
        }

        dialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String user_name = String.valueOf(name.getText());
                String user_desc = String.valueOf(desc.getText());
                String amountString = String.valueOf(amount.getText());

                int debt_amount = Integer.parseInt(String.valueOf(amount.getText()));

                if (debt_button.isChecked()) {
                    Debt newDebt = new Debt("user", user_name, debt_amount, user_desc,0,0,mySqlDate );
                    DebtFragment.debts.add(newDebt);
                    Database.execSQL("INSERT INTO Debts VALUES(NULL,'" + user_name + "'," + debt_amount + ",'" + user_desc + "','0','0','" + formattedDate + "' );");
                    newDebt("avadh",user_name,debt_amount,user_desc, 0 /*0 = debt*/, "0", formattedDate);
//                    String emailID = "a@b.com";
//                    putNewDebt(ROOT_URL+"debtmanagertableController/newDebt/requestUserID="+emailID+"&confirmationUserID="+user_name+"&amount="+debt_amount+"" +
//                            "&description="+user_desc+"&type=0&confirmation=0&date="+mySqlDate);

                }
                if (loan_button.isChecked()) {
                    Debt newDebt = new Debt("user", user_name, debt_amount, user_desc,0,0,mySqlDate );
                    DebtFragment.debts.add(newDebt);
                    Database.execSQL("INSERT INTO Debts VALUES(NULL,'" + user_name + "'," + debt_amount + ",'" + user_desc + "','0','0','" + formattedDate + "' );");
//                    newDebt("avadh",user_name,debt_amount,user_desc, 1 /*1 = loan*/, "0", formattedDate);
//                    String emailID = "a@b.com";
//                    putNewDebt(ROOT_URL+"debtmanagertableController/newDebt/requestUserID="+emailID+"&confirmationUserID="+user_name+"&amount="+debt_amount+"" +
//                            "&description="+user_desc+"&type=1&confirmation=0&date="+mySqlDate);

                }
                loadDebts();
            }
        });

        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        android.support.v7.app.AlertDialog addExpenseDialog = dialogBuilder.create();
        addExpenseDialog.show();
    }

    @PUT("/newDebt/requestUserID={requestUserID}&confirmationUserID={confirmationUserID}&amount={amount}&description={description}&type={type}&confirmation={confirmation}&date={date}")
    public void newDebt(@Query("requestUserID") String requestUserID,
                        @Query("confirmationUserID") String confirmationUserID,
                        @Query("amount") double amount,
                        @Query("description") String description,
                        @Query("type") double type,
                        @Query("confirmation") String confirmation,
                        @Query("date") String date){

    }

    private void loadDebts() {
//        String userID = "a@b.com";
//        Log.d("Before DB exec", "loadDebts: ");
//        String JSON_debt = getDebtByUserID(ROOT_URL+"debtmanagertableController/getAllDebtUserID?userID="+userID);
//        Log.e("JSON-debt", JSON_debt);
////        String[] currList;
////        JSON_debt.substring(1,JSON_debt.length());
//        Log.d("Before GSON", "loadDebts: ");
//        Gson gson=new GsonBuilder().create();
//        Debt[] debt_array = gson.fromJson(JSON_debt, Debt[].class);
//        Log.d("After GSON", "loadDebts: ");
//        int count=0;
//        DebtFragment.debts = new ArrayList<Debt>();
//        for(Debt dbt : debt_array)
//        {
//            DebtFragment.debts.add(dbt);
//            count++;
//            Log.e("DEBT",dbt.getDescription());
//        }

        String query = "Select * From Debts WHERE Confirmation='false';";
        Cursor resultSet = Database.rawQuery(query,null);
        resultSet.moveToFirst();
        int j=0;
        int count = resultSet.getCount();
        Log.d(String.valueOf(count), "loadDebts: ");
        DebtFragment.debts = new ArrayList<>();
        while (j < count){
            Debt currDebt = new Debt("admin",resultSet.getString(1),Integer.parseInt(resultSet.getString(2)),resultSet.getString(3),resultSet.getString(4).charAt(0),0,resultSet.getString(6));
            DebtFragment.debts.add(currDebt);
            resultSet.moveToNext();
            j++;
        }
        HashMap<String,NetDebt> netDebtsHashmap = new HashMap<String,NetDebt>();
        for(int i=0;i<count;i++){
            if(netDebtsHashmap.get(DebtFragment.debts.get(i).getConfirmationUserID())==null){
                NetDebt currNetDebt = new NetDebt(DebtFragment.debts.get(i).getConfirmationUserID(),0);
                if(DebtFragment.debts.get(i).getType()=='L'){
                    currNetDebt.setNetDebt(DebtFragment.debts.get(i).getAmount());
                }else{
                    currNetDebt.setNetDebt(-DebtFragment.debts.get(i).getAmount());
                }
                netDebtsHashmap.put(DebtFragment.debts.get(i).getConfirmationUserID(),currNetDebt);
            }else{
                NetDebt currNetDebt = netDebtsHashmap.get(DebtFragment.debts.get(i).getConfirmationUserID());
                if(DebtFragment.debts.get(i).getType()=='L'){
                    currNetDebt.setNetDebt(currNetDebt.getNetDebt()+DebtFragment.debts.get(i).getAmount());
                }else{
                    currNetDebt.setNetDebt(currNetDebt.getNetDebt()-DebtFragment.debts.get(i).getAmount());
                }
                netDebtsHashmap.put(DebtFragment.debts.get(i).getConfirmationUserID(),currNetDebt);
            }
        }

        List<NetDebt> tempList = new ArrayList<NetDebt>( netDebtsHashmap.values() );
        Log.d(String.valueOf(netDebtsHashmap.size()), "loadDebts: ");
        RecyclerAdapterDebt.NetDebts = new ArrayList<NetDebt>();
        for(int i=0;i<netDebtsHashmap.size();i++)
            RecyclerAdapterDebt.NetDebts.add(tempList.get(i));
        if(DebtFragment.adapter!=null)
            DebtFragment.adapter.notifyDataSetChanged();
        int get = 0;
        int owe = 0;
        for(int i=0;i<RecyclerAdapterDebt.NetDebts.size();i++){
            if(RecyclerAdapterDebt.NetDebts.get(i).getNetDebt()<0){
                owe -= RecyclerAdapterDebt.NetDebts.get(i).getNetDebt();
            }else{
                get += RecyclerAdapterDebt.NetDebts.get(i).getNetDebt();
            }
        }
        DebtFragment.adapter.notifyDataSetChanged();
        youGetTextView.setText("Rs: " + String.valueOf(get));
        youOweTextView.setText("Rs :" + String.valueOf(owe));

    }

    ////////////////////////ADD NEW EXPENSE//////////////////////////////////
    private void addExpense() {

        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_expense, null);
        dialogBuilder.setView(dialogView);

        /////////////////////////////Variable Declaration////////////////////////////////////
        final EditText title = (EditText) dialogView.findViewById(R.id.task_title);
        final EditText content = (EditText) dialogView.findViewById(R.id.task_content);
        final TextView dateAdd = (TextView) dialogView.findViewById(R.id.addDate);
        spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        ///////////////////////////////////////////////////////////////////////////////////

        dialogBuilder.setTitle("Add an Expense");

        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDate = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat textFormat = new SimpleDateFormat("MMMM dd,yyyy");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = df.format(c.getTime());

        try {
            Date date = numberFormat.parse(currentDate + "/" + (currentMonth + 1) + "/" + currentYear);
            dateAdd.setText(textFormat.format(date));
        }catch (Exception e){

        }
        android.support.v7.app.AlertDialog.Builder builder = dialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //add stuff
                String value = (String.valueOf(title.getText()));
                String desc = String.valueOf(content.getText());
                String catg = spinner.getSelectedItem().toString();

                if (!value.equals("")) {
                    Database.execSQL("INSERT INTO Expenses VALUES(NULL,'admin'," + Integer.parseInt(value) + ",'" + catg + "','" + desc + "','" + formattedDate + "' );");
                    Expense expense = new Expense(Integer.parseInt(value),catg,desc,formattedDate);


                    HomeFragment.expenses.add(expense);
                    HomeFragment.adapter.notifyDataSetChanged();
                }

            }
        });

        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //pass
            }
        });




        android.support.v7.app.AlertDialog addExpenseDialog = dialogBuilder.create();
        addExpenseDialog.show();

    }
    ///////////////////////////////////////////////////////////////////////////////////

    private void updateLabel() {
        String myFormat = "dd MMMM, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        DateOne.setText(sdf.format(Calendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        currTab = R.id.tab_debts;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            signout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void DateSelectorClicked(View view) {

        Log.d(String.valueOf(prevRadioValue), "DateSelectorClicked: ");
        boolean checked = ((RadioButton) view).isChecked();
        prevDateOneValue = String.valueOf(DateOne.getText());

        switch (view.getId()) {
            case R.id.radioButton1:
                chooseDate(1, null);
                break;

            case R.id.radioButton2:
                chooseDate(2, null);
                break;

            case R.id.radioButton3:
                chooseDate(3,null);
                break;

            case R.id.radioButton4:
                chooseDate(4, null);
                break;

        }
    }


    public void chooseDate(int ID, Date minDate) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDate = c.get(Calendar.DAY_OF_MONTH);


        currID = ID;



        if (currID != 3) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    new DatePickerDialog.OnDateSetListener() {


                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            selected = true;

                            try {
                                Date date = numberFormat.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                if (currID == 5) {
                                    DateOne.setText(DateOne.getText() + " - " + textFormat.format(date));
                                    loadExpenses(String.valueOf(DateOne.getText()).split(" - ")[0], String.valueOf(DateOne.getText()).split(" - ")[1], 3);

                                } else {
                                    DateOne.setText(textFormat.format(date));
                                    TempDate = new Date(date.getTime() + 518400000);
                                    if (currID == 1){
                                        loadExpenses(textFormat.format(date),null,0);
                                    }
                                    if (currID == 2) {
                                        DateOne.setText(DateOne.getText() + " - " + textFormat.format(TempDate));
                                        loadExpenses(String.valueOf(DateOne.getText()).split(" - ")[0], String.valueOf(DateOne.getText()).split(" - ")[1], 1);
                                    }
                                    if (currID == 4) {
                                        tempSelected = true;
                                        DateOne.setText(textFormat.format(date));
                                        chooseDate(5, date);
                                    }

                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, currentYear, currentMonth, currentDate);

            SimpleDateFormat numberFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat textFormat = new SimpleDateFormat("MMMM dd, yyyy");


            switch (ID) {
                case 1:
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.setTitle("Select Date");
                    break;
                case 2:
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 518400000);
                    datePickerDialog.setTitle("Select Start Date of the Week");
                    break;
                case 4:
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.setTitle("From");
                    break;
                case 5:
                    if (minDate != null) {
                        datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                    }
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.setTitle("To");
                    break;
            }


            datePickerDialog.setOnDismissListener(onDismisslistener);
            selected = false;
            if (currID - 4 < 0) {
                tempSelected = false;
            }
            datePickerDialog.show();

        }else{
            DatePickerDialog dialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener(){

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dateOfMonth) {

                    selected = true;
                    resetCharts();
                    SimpleDateFormat textFormat = new SimpleDateFormat("MMMM ,yyyy");
                    try {
                        Date date = numberFormat.parse(dateOfMonth + "/" + (month + 1) + "/" + year);
                        DateOne.setText(textFormat.format(date));
                        String date1 = textFormat.format(date).split(",")[0]+"1, "+ textFormat.format(date).split(",")[1];
                        String date2 = textFormat.format(date).split(",")[0]+"32, "+ textFormat.format(date).split(",")[1];
                        loadExpenses(date1, date2, 2);
                    }catch (Exception e){

                    }
                }
            }, currentYear, currentMonth, currentDate);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ((ViewGroup) dialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.setOnDismissListener(onDismisslistener);
            selected = false;
            dialog.setTitle("");
            dialog.show();

        }
    }



    public void ResetSelectDate(){
        RadioButton radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        switch (prevRadioValue){
            case R.id.radioButton1:
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
                loadExpenses(prevDateOneValue,null,0);
                break;
            case R.id.radioButton2:
                radioButton1.setChecked(false);
                radioButton2.setChecked(true);
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
                loadExpenses(prevDateOneValue.split(" - ")[0], prevDateOneValue.split(" - ")[1],1);
                break;
            case R.id.radioButton3:
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(true);
                radioButton4.setChecked(false);
                String date1 = prevDateOneValue.split(",")[0]+"1, "+ prevDateOneValue.split(",")[1];
                String date2 = prevDateOneValue.split(",")[0]+"32, "+ prevDateOneValue.split(",")[1];
                loadExpenses(date1,date2, 2);
                break;
            case R.id.radioButton4:
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                radioButton4.setChecked(true);
                loadExpenses(prevDateOneValue.split(" - ")[0], prevDateOneValue.split(" - ")[1],3);
                break;
        }
    }

    public void SelectDate(View v){
        RadioGroup radiogroup = (RadioGroup) findViewById(R.id.RadioGroup1);
        int radioID = radiogroup.getCheckedRadioButtonId();
        switch (radioID){
            case R.id.radioButton1:
                chooseDate(1,null);
                break;
            case R.id.radioButton2:
                chooseDate(2,null);
                break;
            case R.id.radioButton3:
                chooseDate(3,null);
                break;
            case R.id.radioButton4:
                chooseDate(4,null);
                break;

        }
    }

    ///////////////HANDLING DISMISS OF DIALOG////////////////////////////////
    DialogInterface.OnDismissListener onDismisslistener =
            new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    tempCounter++;
                    if (selected) {
                        RadioGroup radiogroup = (RadioGroup) findViewById(R.id.RadioGroup1);
                        prevRadioValue = radiogroup.getCheckedRadioButtonId();
                        prevDateOneValue = String.valueOf(DateOne.getText());
                        tempCounter = 0;
                    } else if (!selected) {
                        if (tempCounter == 1 && !tempSelected) {
                            DateOne.setText(prevDateOneValue);
                            ResetSelectDate();
                            tempCounter = 0;
                        } else if (tempCounter == 2) {
                            DateOne.setText(prevDateOneValue);
                            ResetSelectDate();
                            tempCounter = 0;
                        }
                    }

                }
            };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("prevRadioValue", prevRadioValue);
        outState.putString("prevDateOneValue", prevDateOneValue);
        outState.putInt("ActiveTab", currTab);
        Log.d(String.valueOf(currTab), "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        prevRadioValue = savedInstanceState.getInt("prevRadioValue");
        prevDateOneValue = savedInstanceState.getString("prevDateOneValue");
        currTab = savedInstanceState.getInt("ActiveTab");
        Log.d(String.valueOf(currTab), "onRestoreActivityState ");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //might be used in future
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //meh
    }

    public void loadExpenses(String date, String date2, int type){
        String query;

        try {
            Date tempdate = textFormat.parse(date);
            date = sqlFormat.format(tempdate);
            if(date2!=null) {
                Date tempdate2 = textFormat.parse(date2);
                date2 = sqlFormat.format(tempdate2);
            }

        } catch (ParseException e) {
            Log.d("failed", "loadExpenses: ");
        }
        switch (type){
            case 0:
                query = "Select * From Expenses WHERE Date='" + date + "';";
                break;
            default:
                query = "Select * From Expenses WHERE Date BETWEEN '" + date + "' AND '" + date2 + "';";
                break;
        }
        Cursor resultSet = Database.rawQuery(query,null);
        resultSet.moveToFirst();
        int i=0;
        int count = resultSet.getCount();
        HomeFragment.expenses = new ArrayList<>();
        while (i < count){
            ///FIX ICON BUG HERE
            Expense currExpense = new Expense(Integer.parseInt(resultSet.getString(2)),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
            HomeFragment.expenses.add(currExpense);
            resultSet.moveToNext();
            i++;
        }
        Log.d(String.valueOf(date), "loadExpenses: ");
        if(HomeFragment.adapter!=null){
            HomeFragment.adapter.notifyDataSetChanged();
        }

    }

    public void resetCharts(){
        if (currTab == R.id.tab_stats) {
            Log.d(String.valueOf(currTab==R.id.tab_stats), "onClick: ");
            currTab = R.id.tab_stats;
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, new StatisticsFragment());
            fragmentTransaction.commit();


        }
    }

    private void resetRecyclerView() {
        if (currTab == R.id.tab_home) {
            currTab = R.id.tab_home;
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, new HomeFragment());
            fragmentTransaction.commit();
        }
    }

    public void signout(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("MainActivity", "onConnectionFailed:" + connectionResult);
    }



    public void getData(String URL)
    {

        Log.e("URL",URL);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new DownloadWebpageTask().execute(URL);
        }
        else
        {
            Toast.makeText(this, "NO NETWORK", Toast.LENGTH_SHORT).show();
        }
    }


    public void putNewDebt(String URL)
    {
        Log.e("URL",URL);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new PutTask().execute(URL);
        }
        else
        {
            Toast.makeText(this, "NO NETWORK", Toast.LENGTH_SHORT).show();
        }
    }

    public String getDebtByUserID(String URL)
    {
        Log.e("URL",URL);
        String ret="";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            try {
                ret = new GetDebtTask().execute(URL).get();
                Log.d(ret, "getDebtByUserID: ");
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            }
        }
        else
        {
            Toast.makeText(this, "NO NETWORK", Toast.LENGTH_SHORT).show();
        }
        Log.d(ret, "getDebtByUserID: ");
        return ret;

    }

    private class GetDebtTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("RESULT", result);
        }
    }

    private class PutTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("RESULT", result);
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Log.e("RESULT", result);
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
// Only display the first 500 characters of the retrieved
// web page content.
        int len = 15000;
        try
        {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
// Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("RESULT", "The response is: " + response);
            is = conn.getInputStream();
// Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;
// Makes sure that the InputStream is closed after the app is
// finished using it.
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
