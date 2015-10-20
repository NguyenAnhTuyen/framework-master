package com.odoo.modify;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.odoo.App;
import com.odoo.OdooActivity;
import com.odoo.base.addons.res.ResCompany;
import com.odoo.config.FirstLaunchConfig;
import com.odoo.core.account.OdooLoginModify;
import com.odoo.R;
import com.odoo.core.auth.OdooAccountManager;
import com.odoo.core.auth.OdooAuthenticator;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.support.OdooInstancesSelectorDialog;
import com.odoo.core.support.OdooUserLoginSelectorDialog;
import com.odoo.core.utils.IntentUtils;
import com.odoo.core.utils.OResource;
import com.odoo.datas.OConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import odoo.Odoo;
import odoo.helper.OUser;
import odoo.helper.OdooInstance;
import odoo.listeners.IOdooConnectionListener;
import odoo.listeners.IOdooInstanceListener;
import odoo.listeners.IOdooLoginCallback;
import odoo.listeners.OdooError;

public class connectActivity extends AppCompatActivity implements View.OnClickListener,
        OdooUserLoginSelectorDialog.IUserLoginSelectListener, IOdooConnectionListener, IOdooLoginCallback,
        View.OnFocusChangeListener
{

    public static final String TAG = connectActivity.class.getSimpleName();
    public String selfHost;
//    private TextView mLoginProcessStatus = null;
    private EditText edtUsername, edtPassword;
//    private Boolean mSelfHostedURL = false;
    private Boolean mCreateAccountRequest = false;
    private Boolean mSelfHostedURL;
    private Boolean mConnectedToServer = true;// do URL hop le nen ben nay mac dinh true
    private Boolean mAutoLogin = false;
    private Boolean mRequestedForAccount = false;
    private AccountCreater accountCreator=null;
    private Spinner databaseSpinner = null;
    private List<String> databases ;
    private TextView mLoginProcessStatus = null;
    private TextView mTermsCondition;
    private Serializable nameDb,db;
    private ArrayList dbs;
    private AppCompatSpinner dbSpinner;
    private App mApp;
    private odoo.helper.OUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mApp = (App) getApplicationContext();


        Bundle getUrl = getIntent().getBundleExtra("selfHost");
        if (getUrl == null) {
            return;
        }
        selfHost = getUrl.getString("sentUrl");
        mSelfHostedURL=getUrl.getBoolean("mSelfHostedURL");
        nameDb = getUrl.getSerializable("nameDB");
//        db= getUrl.getSerializable("databases");
//        ArrayList dbs= (ArrayList)db;

//        dbSpinner=(AppCompatSpinner)getUrl.getSerializable("databaseSpinner");

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            if (extras.containsKey(OdooAuthenticator.KEY_NEW_ACCOUNT_REQUEST))
//                mCreateAccountRequest = true;
//            if (extras.containsKey(OdooActivity.KEY_ACCOUNT_REQUEST)) {
//                mRequestedForAccount = true;
//                setResult(RESULT_CANCELED);
//            }
//        }
        if (!mCreateAccountRequest) {
            if (OdooAccountManager.anyActiveUser(this)) {
                startOdooActivity();
                return;
            } else if (OdooAccountManager.hasAnyAccount(this)) {
                onRequestAccountSelect();
            }
        }
        init();

    }

    private void init() {
        mLoginProcessStatus = (TextView) findViewById(R.id.login_process_status);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.forgot_password).setOnClickListener(this);
        findViewById(R.id.create_account).setOnClickListener(this);

    }
    private void startOdooActivity() {
        startActivity(new Intent(this, OdooActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                loginUser(selfHost,mSelfHostedURL,nameDb);
                break;
            case R.id.forgot_password:
                IntentUtils.openURLInBrowser(this, OConstants.URL_ODOO_RESET_PASSWORD);
                break;
            case R.id.create_account:
                IntentUtils.openURLInBrowser(this, OConstants.URL_ODOO_SIGN_UP);
                break;
        }
    }

    private String createServerURL(String server_url) {
        StringBuilder serverURL = new StringBuilder();
        if (!server_url.contains("http://") && !server_url.contains("https://")) {
            serverURL.append("http://");
        }
        serverURL.append(server_url);
        return serverURL.toString();
    }
    // User Login
    private void loginUser( String selfHost, Boolean mSelfHostedURL, Serializable nameDb) {
        Log.v("", "LoginUser()");
//        Bundle getInfo = getIntent().getBundleExtra("selfHost");
//        ArrayList data= (ArrayList) getInfo.getSerializable("databases");
        String serverURL = createServerURL((mSelfHostedURL) ? selfHost :
                OConstants.URL_ODOO);
        String databaseName;
        edtUsername = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        if (mSelfHostedURL) {
            if (databaseSpinner != null && databases.size() > 1 && databaseSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, OResource.string(this, R.string.label_select_database), Toast.LENGTH_LONG).show();
                return;
            }

        }
        edtUsername.setError(null);
        edtPassword.setError(null);
        if (TextUtils.isEmpty(edtUsername.getText())) {
            edtUsername.setError(OResource.string(this, R.string.error_provide_username));
            edtUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText())) {
            edtPassword.setError(OResource.string(this, R.string.error_provide_password));
            edtPassword.requestFocus();
            return;
        }
        findViewById(R.id.controls).setVisibility(View.GONE);
        findViewById(R.id.login_progress).setVisibility(View.VISIBLE);
        mLoginProcessStatus.setText(OResource.string(connectActivity.this,
                R.string.status_connecting_to_server));
        if (mConnectedToServer) {
            databaseName = (String) nameDb;
//            if (databaseSpinner != null) {
//                databaseName = databases.get(databaseSpinner.getSelectedItemPosition());
//            }
            mAutoLogin = false;
            loginProcess(null, serverURL, databaseName);

        } else {
            mAutoLogin = true;
            Log.v("", "Testing URL: " + serverURL);
            Odoo.createInstance(connectActivity.this, serverURL).setOnConnect(connectActivity.this);
        }

    }

    @Override
    public void onUserSelected(com.odoo.core.support.OUser user) {
        OdooAccountManager.login(this, user.getAndroidName());
        startOdooActivity();
    }

    @Override
    public void onNewAccountRequest() {
        init();
    }

    @Override
    public void onCancelSelect() {


    }

    @Override
    public void onRequestAccountSelect() {
        OdooUserLoginSelectorDialog dialog = new OdooUserLoginSelectorDialog(this);
        dialog.setUserLoginSelectListener(this);
        dialog.show();
    }

    @Override
    public void canceledInstanceSelect() {
        findViewById(R.id.controls).setVisibility(View.VISIBLE);
        findViewById(R.id.login_progress).setVisibility(View.GONE);
        findViewById(R.id.serverURLCheckProgress).setVisibility(View.VISIBLE);
    }

    @Override
    public void instanceSelected(OdooInstance instance) {
        // Logging in to instance
        loginProcess(instance, null, null);
    }
    private void loginProcess(final OdooInstance instance, String url, final String database) {
        Log.v("", "LoginProcess");
        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        if (instance == null && url.equals(OConstants.URL_ODOO)) {
            // OAuth Login or Odoo.com Login
            mLoginProcessStatus.setText(OResource.string(connectActivity.this, R.string.status_getting_instances));
            OdooLoginModify.mOdoo.authenticate(username, password, database, new IOdooLoginCallback() {
                @Override
                public void onLoginSuccess(Odoo odoo, odoo.helper.OUser oUser) {
                    OdooLoginModify.mOdoo = odoo;
                    mUser = oUser;
                    OdooLoginModify.mOdoo.getSaasInstances(new IOdooInstanceListener() {
                        @Override
                        public void onInstancesLoad(List<OdooInstance> odooInstances) {
                            OdooInstance oInstance = new OdooInstance();
                            oInstance.setCompanyName(OConstants.ODOO_COMPANY_NAME);
                            oInstance.setUrl(OConstants.URL_ODOO);
                            oInstance.setDbName(database);
                            odooInstances.add(0, oInstance);
                            if (odooInstances.size() > 1) {
                                OdooInstancesSelectorDialog instancesSelectorDialog =
                                        new OdooInstancesSelectorDialog(connectActivity.this);
                                instancesSelectorDialog.setInstances(odooInstances);
                                instancesSelectorDialog.setOnInstanceSelectListener((OdooInstancesSelectorDialog.OnInstanceSelectListener) connectActivity.this);
                                instancesSelectorDialog.showDialog();
                            } else {
                                //Loggin in to odoo.com (default instance)
                                loginProcess(oInstance, oInstance.getUrl(), database);
                            }
                        }
                    });
                }

                @Override
                public void onLoginFail(OdooError error) {
                    loginFail(error);
                }
            });
        } else if (instance == null) {
            Log.v("", "Processing Self Hosted Server Login");
            mLoginProcessStatus.setText(OResource.string(connectActivity.this, R.string.status_logging_in));
            OdooLoginModify.mOdoo.authenticate(username, password, database, connectActivity.this);
//            Toast.makeText(connectActivity.this,"chay tiep thoi", Toast.LENGTH_SHORT).show();
        } else {
            // Instance login
            Log.v("", "Processing Odoo Instance Login");
            mLoginProcessStatus.setText(OResource.string(connectActivity.this,
                    R.string.status_logging_in_with_instance));
            new AsyncTask<Void, Void, OUser>() {

                @Override
                protected odoo.helper.OUser doInBackground(Void... params) {
                    // Need to execute in background task.
                    return OdooLoginModify.mOdoo.oAuthLogin(instance, username, password);
                }

                @Override
                protected void onPostExecute(odoo.helper.OUser oUser) {
                    super.onPostExecute(oUser);
                    onLoginSuccess(OdooLoginModify.mOdoo, oUser);
                }
            }.execute();
        }
    }

    @Override
    public void onLoginSuccess(Odoo odoo, odoo.helper.OUser oUser) {
        mApp.setOdoo(odoo, oUser);
        mLoginProcessStatus.setText(OResource.string(connectActivity.this, R.string.status_login_success));
        OdooLoginModify.mOdoo = odoo;
        if (accountCreator != null) {
            accountCreator.cancel(true);
        }
        accountCreator = new AccountCreater();
        com.odoo.core.support.OUser user = new com.odoo.core.support.OUser();
        user.setFromBundle(oUser.getAsBundle());
        accountCreator.execute(user);
    }

    @Override
    public void onLoginFail(OdooError error) {
        loginFail(error);
    }

    private void loginFail(OdooError error) {
        findViewById(R.id.controls).setVisibility(View.VISIBLE);
        findViewById(R.id.login_progress).setVisibility(View.GONE);
        edtUsername.setError(OResource.string(this, R.string.error_invalid_username_or_password));
    }

    @Override
    public void onConnect(Odoo odoo) {

    }

    @Override
    public void onError(OdooError odooError) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    private class AccountCreater extends AsyncTask<com.odoo.core.support.OUser, Void, Boolean> {

        private com.odoo.core.support.OUser mUser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoginProcessStatus.setText(OResource.string(connectActivity.this, R.string.status_creating_account));
        }

        @Override
        protected Boolean doInBackground(com.odoo.core.support.OUser... params) {
            mUser = params[0];
            if (OdooAccountManager.createAccount(connectActivity.this, mUser)) {
                mUser = OdooAccountManager.getDetails(connectActivity.this, mUser.getAndroidName());
                OdooAccountManager.login(connectActivity.this, mUser.getAndroidName());
                FirstLaunchConfig.onFirstLaunch(connectActivity.this, mUser);
                try {
                    // Syncing company details
                    ODataRow company_details = new ODataRow();
                    company_details.put("id", mUser.getCompanyId());
                    ResCompany company = new ResCompany(connectActivity.this, mUser);
                    company.quickCreateRecord(company_details);
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            mLoginProcessStatus.setText(OResource.string(connectActivity.this, R.string.status_redirecting));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mRequestedForAccount)
                        startOdooActivity();
                    else {
                        Intent intent = new Intent();
                        intent.putExtra(OdooActivity.KEY_NEW_USER_NAME, mUser.getAndroidName());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }, 1500);
        }
    }
}
