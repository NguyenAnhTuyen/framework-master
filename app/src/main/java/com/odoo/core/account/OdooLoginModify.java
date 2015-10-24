//package com.odoo.core.account;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.text.method.LinkMovementMethod;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.odoo.App;
//import com.odoo.OdooActivity;
//import com.odoo.R;
//import com.odoo.base.addons.res.ResCompany;
//import com.odoo.config.FirstLaunchConfig;
//import com.odoo.core.auth.OdooAccountManager;
//import com.odoo.core.auth.OdooAuthenticator;
//import com.odoo.core.orm.ODataRow;
//import com.odoo.core.support.OUser;
//import com.odoo.core.support.OdooInstancesSelectorDialog;
//import com.odoo.core.support.OdooUserLoginSelectorDialog;
//import com.odoo.core.utils.IntentUtils;
//import com.odoo.core.utils.OResource;
//import com.odoo.datas.OConstants;
//import com.odoo.modify.connectActivity;
//import java.util.ArrayList;
//import java.util.List;
//import com.odoo.indexActivity;
//
//import odoo.Odoo;
//import odoo.helper.OdooInstance;
//import odoo.listeners.IDatabaseListListener;
//import odoo.listeners.IOdooConnectionListener;
//import odoo.listeners.IOdooInstanceListener;
//import odoo.listeners.IOdooLoginCallback;
//import odoo.listeners.OdooError;
//
//public class OdooLoginModify extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,IOdooConnectionListener {
//
//    private EditText  edtSelfHosted;
//    private Boolean mSelfHostedURL = false;
//    private Boolean mConnectedToServer = false;
//    private Boolean mAutoLogin = false;
//    private Boolean mRequestedForAccount = false;
//    public  String databaseName;
//    private Spinner databaseSpinner = null;
//    private List<String> databases = new ArrayList<>();
//    private TextView mLoginProcessStatus = null;
//    private TextView mTermsCondition;
//    private App mApp;
//    public static   Odoo mOdoo;
//    private odoo.helper.OUser mUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.base_login_modify);
//        mApp = (App) getApplicationContext();
//        init();
//        toggleSelfHostedURL();
//    }
//
//    private void init() {
//        mLoginProcessStatus = (TextView) findViewById(R.id.login_process_status);
//        mTermsCondition = (TextView) findViewById(R.id.termsCondition);
//        mTermsCondition.setMovementMethod(LinkMovementMethod.getInstance());
//
//        findViewById(R.id.btnConnect).setOnClickListener(this);
////        findViewById(R.id.txvAddSelfHosted).setOnClickListener(this);
//        edtSelfHosted = (EditText) findViewById(R.id.edtSelfHostedURL);
//    }
//
//    private void connectFrontend() {
////        startActivity(new Intent(this, connectActivity.class))
//        Intent intent= new Intent(OdooLoginModify.this,indexActivity.class);
//        Bundle b = new Bundle();
//        if((edtSelfHosted.getText().toString())!=""){
//            b.putString("sentUrl", edtSelfHosted.getText().toString());
//            b.putBoolean("mSelfHostedURL", mSelfHostedURL.booleanValue());
//            b.putSerializable("nameDB", databases.get(databaseSpinner.getSelectedItemPosition()));
//            b.putSerializable("databases",databases.toArray());
////            b.putSerializable("databaseSpinner",databaseSpinner);
//            intent.putExtra("selfHost",b);
//
//            startActivity(intent);
//            finish();
//        }
//
//    }
//
//
//
//    private void toggleSelfHostedURL() {
////        TextView txvAddSelfHosted = (TextView) findViewById(R.id.txvAddSelfHosted);
//        if (!mSelfHostedURL) {
//            mSelfHostedURL = true;
//            findViewById(R.id.layoutSelfHosted).setVisibility(View.VISIBLE);
//            edtSelfHosted.setOnFocusChangeListener(this);
//            edtSelfHosted.requestFocus();
////            txvAddSelfHosted.setText(R.string.label_login_with_odoo);
//        } else {
//            findViewById(R.id.layoutBorderDB).setVisibility(View.GONE);
//            findViewById(R.id.layoutDatabase).setVisibility(View.GONE);
//            findViewById(R.id.layoutSelfHosted).setVisibility(View.GONE);
//            mSelfHostedURL = false;
////            txvAddSelfHosted.setText(R.string.label_add_self_hosted_url);
//            edtSelfHosted.setText("");
//        }
//    }
//
//    @Override
//    public void onFocusChange(final View v, final boolean hasFocus) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mSelfHostedURL && v.getId() == R.id.edtSelfHostedURL && !hasFocus) {
//                    if (!TextUtils.isEmpty(edtSelfHosted.getText())
//                            && validateURL(edtSelfHosted.getText().toString())) {
//                        edtSelfHosted.setError(null);
//
//                        findViewById(R.id.imgValidURL).setVisibility(View.GONE);
//                        findViewById(R.id.serverURLCheckProgress).setVisibility(View.VISIBLE);
////                        findViewById(R.id.layoutBorderDB).setVisibility(View.GONE);
////                        findViewById(R.id.layoutDatabase).setVisibility(View.GONE);
//                        String test_url = createServerURL(edtSelfHosted.getText().toString());
//                        Log.v("", "Testing URL :" + test_url);
//                        Odoo.createInstance(OdooLoginModify.this, test_url).setOnConnect(OdooLoginModify.this);
//                    }
//                }
//            }
//        }, 500);
//    }
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_base_login, menu);
////        return true;
////    }
//
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        return super.onOptionsItemSelected(item);
////    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnConnect:
////                startActivity(new Intent(this, OdooActivity.class));
////                finish();
//                connectFrontend();
//                break;
//        }
//    }
//
//
//    private boolean validateURL(String url) {
//        return (url.contains("."));
//    }
//
//    private String createServerURL(String server_url) {
//        StringBuilder serverURL = new StringBuilder();
//        if (!server_url.contains("http://") && !server_url.contains("https://")) {
//            serverURL.append("http://");
//        }
//        serverURL.append(server_url);
//        return serverURL.toString();
//    }
//
//    private void showDatabases() {
//
//        if (databases.size() > 1) {
//            findViewById(R.id.layoutBorderDB).setVisibility(View.VISIBLE);
//            findViewById(R.id.layoutDatabase).setVisibility(View.VISIBLE);
//            databaseSpinner = (Spinner) findViewById(R.id.spinnerDatabaseList);
//            databases.add(0, OResource.string(this, R.string.label_select_database));
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databases);
//            databaseSpinner.setAdapter(adapter);
//
//        } else {
//            databaseSpinner = null;
//            findViewById(R.id.layoutBorderDB).setVisibility(View.GONE);
//            findViewById(R.id.layoutDatabase).setVisibility(View.GONE);
//        }
//    }
//    //    cua IOdooConnectionListener
//    @Override
//    public void onConnect(Odoo odoo) {
//        Log.v("Odoo", "Connected to server.");
//        mOdoo = odoo;
//        databases.clear();
//        findViewById(R.id.serverURLCheckProgress).setVisibility(View.GONE);
//        edtSelfHosted.setError(null);
//        mLoginProcessStatus.setText(OResource.string(OdooLoginModify.this, R.string.status_connected_to_server));
//        mOdoo.getDatabaseList(new IDatabaseListListener() {
//            @Override
//            public void onDatabasesLoad(List<String> strings) {
//                databases.addAll(strings);
//                showDatabases();
//                mConnectedToServer = true;
//                findViewById(R.id.imgValidURL).setVisibility(View.VISIBLE);
//                if (mAutoLogin) {
//                    connectFrontend();
//                }
//            }
//        });
//    }
//    //    cua IOdooConnectionListener
//    @Override
//    public void onError(OdooError odooError) {
//
//    }
//}
//
//
//
//
//
