package ru.psdevelop.tdapptemareg;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.os.Message;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class RegistrationActivity extends AppCompatActivity {

    public static final int ID_ACTION_SET_ACTIVE_TAB = 100;
    public static final int ID_ACTION_SEND_REG_DATA = 101;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static Handler handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        handle = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1 == ID_ACTION_SET_ACTIVE_TAB) {
                    try {
                        mViewPager.setCurrentItem(msg.getData().getInt("tabNum"));
                    } catch(Exception hex)  {
                        showMyMsg("Ошибка переключения страницы "+hex);
                    }
                } else if(msg.arg1 == ID_ACTION_SEND_REG_DATA) {
                    sendRegData();
                }
            }
        };
    }

    String paramHtmlElm1 = "", //<p><b>
            paramHtmlElm2 = "", //</b>
            paramHtmlElm3 = ""; //</p>

    static boolean allRequired = true;

    public String getParamText(EditText paramEdit, String caption, boolean required) {
        boolean hasRequired = true;
        if (required) {
            hasRequired = false;
            if (paramEdit != null) {
                if (paramEdit.getText().length() > 0) {
                    hasRequired = true;
                }
            }
        }
        allRequired = allRequired && hasRequired;
        return paramEdit != null ?
                paramHtmlElm1 + caption + paramHtmlElm2 +
                        paramEdit.getText() + paramHtmlElm3 + " \n" : "" ;
    }

    public void sendRegData() {
        /*showMyMsg("Send email");
        try {
            GMailSender sender = new GMailSender("kubandevelop@gmail.com", "as29ss23asss");
            sender.sendMail("This is Subject",
                    "This is Body",
                    "kubandevelop@gmail.com",
                    "psdevelop@yandex.ru");
        } catch (Exception e) {
            showMyMsg("SendMail error" + e);
        }*/

        allRequired = true;

        try {

            String body = "Данные от водителя \n"; //"<html><body><h3>Данные от водителя</h3>"
            if (mSectionsPagerAdapter.tabs[0] != null) {
                body += getParamText(mSectionsPagerAdapter.tabs[0]
                        .editTextFIO, "ФИО: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[0]
                                .editTextPhone, "Телефон: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[0]
                                .editTextCardNumber, "Номер карты: ", false) +
                        getParamText(mSectionsPagerAdapter.tabs[1]
                                .editEmail, "Email: ", false) +
                        getParamText(mSectionsPagerAdapter.tabs[1]
                                .editRegNum, "Гос. номер: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[1]
                                .editAutoModel, "Модель, марка машины: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[2]
                                .editManufactureYear, "Год производства: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[2]
                                .editAutoColor, "Цвет машины: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[2]
                                .editSerNum, "Серия, номер прав: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[3]
                                .editPDate, "Дата выдачи: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[3]
                                .editPEndDate, "Дата действия: ", false) +
                        getParamText(mSectionsPagerAdapter.tabs[3]
                                .editTown, "Город: ", true) +
                        getParamText(mSectionsPagerAdapter.tabs[3]
                                .editLicence, "Ном. лицензии: ", false) +
                        getParamText(mSectionsPagerAdapter.tabs[3]
                                .editComments, "Комментарий: ", false);
            }
            body += ""; //"</body></html>"
            if (allRequired) {
                String[] recipients = {
                        "registra.tema@gmail.com",
                        "psdevelop@yandex.ru"
                };
                SendEmailAsyncTask email = new SendEmailAsyncTask();
                email.activity = this;
                email.m = new Mail("memory.tema", "");
                email.m.set_from("memory.tema");
                email.m.setBody(body);
                email.m.set_to(recipients);
                email.m.set_subject("Данные от водителя");
                email.execute();
            } else {
                showMyMsg("Не заполнены обязательные поля!");
            }

        } catch (Exception e) {
            showMyMsg("SendMail error" + e);
        }
    }

    public void showMyMsg(String message)   {
        try {
            Toast alertMessage = Toast.makeText(getApplicationContext(),
                    "СООБЩЕНИЕ: "
                            +message, Toast.LENGTH_LONG);
            alertMessage.show();
        } catch(Exception ex)   {
        }
    }

    public void displayMessage(String message) {
        Snackbar.make( findViewById(R.id.container), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        EditText editTextFIO = null, editTextPhone = null,
                editTextCardNumber = null,
                //поля ввода на второй странице
                editEmail = null, editRegNum = null, editAutoModel = null,
                //поля ввода на 3 странице
                editManufactureYear = null, editAutoColor = null,
                editSerNum = null,
                //поля ввода на 4 странице
                editPDate = null, editPEndDate = null,
                editTown = null, editLicence = null,
                editComments = null;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void setButtonPositionListener(Button bt, final int position) {
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.arg1 = ID_ACTION_SET_ACTIVE_TAB;
                    Bundle bnd = new Bundle();
                    bnd.putInt("tabNum", position);
                    msg.setData(bnd);
                    handle.sendMessage(msg);
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            switch(getArguments().getInt(ARG_SECTION_NUMBER))    {
                case 1:
                    rootView = inflater.inflate(
                            R.layout.fragment_registration_1step,
                            container, false);
                    setButtonPositionListener(
                            (Button)rootView.findViewById(R.id.buttonNext1), 1);
                    editTextFIO = (EditText) rootView
                            .findViewById(R.id.editTextFIO);
                    editTextPhone = (EditText) rootView
                            .findViewById(R.id.editTextPhone);
                    editTextCardNumber = (EditText) rootView
                            .findViewById(R.id.editTextCardNumber);
                    break;
                case 2:
                    rootView = inflater.inflate(
                            R.layout.fragment_registration_2step,
                            container, false);
                    setButtonPositionListener(
                            (Button)rootView.findViewById(R.id.buttonPrev2), 0);
                    setButtonPositionListener(
                            (Button)rootView.findViewById(R.id.buttonNext2), 2);
                    editEmail = (EditText) rootView
                            .findViewById(R.id.editEmail);
                    editRegNum = (EditText) rootView
                            .findViewById(R.id.editRegNum);
                    editAutoModel = (EditText) rootView
                            .findViewById(R.id.editAutoModel);
                    break;
                case 3:
                    rootView = inflater.inflate(
                            R.layout.fragment_registration_3step,
                            container, false);
                    setButtonPositionListener(
                            (Button)rootView.findViewById(R.id.buttonPrev3), 1);
                    setButtonPositionListener(
                            (Button)rootView.findViewById(R.id.buttonNext3), 3);
                    editManufactureYear = (EditText) rootView
                            .findViewById(R.id.editManufactureYear);
                    editAutoColor = (EditText) rootView
                            .findViewById(R.id.editAutoColor);
                    editSerNum = (EditText) rootView
                            .findViewById(R.id.editSerNum);
                    break;
                case 4:
                    rootView = inflater.inflate(
                            R.layout.fragment_registration_4step,
                            container, false);
                    setButtonPositionListener(
                            (Button)rootView.findViewById(R.id.buttonPrev4), 2);
                    ((Button)rootView.findViewById(R.id.buttonNext4))
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Message msg = new Message();
                                    msg.arg1 = ID_ACTION_SEND_REG_DATA;
                                    Bundle bnd = new Bundle();
                                    msg.setData(bnd);
                                    handle.sendMessage(msg);
                                }
                            });
                    editPDate = (EditText) rootView
                            .findViewById(R.id.editPDate);
                    editPDate.addTextChangedListener(new MaskWatcher("##.##.##"));
                    editPEndDate = (EditText) rootView
                            .findViewById(R.id.editPEndDate);
                    editPEndDate.addTextChangedListener(new MaskWatcher("##.##.##"));
                    editTown = (EditText) rootView
                            .findViewById(R.id.editTown);
                    editLicence = (EditText) rootView
                            .findViewById(R.id.editLicence);
                    editComments = (EditText) rootView
                            .findViewById(R.id.editComments);
                    break;
            }
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        PlaceholderFragment[] tabs = {null, null, null, null};
        // = null, tab2 = null,
        //tab3 = null, tab4 = null;

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fr = PlaceholderFragment.newInstance(position + 1);
            tabs[position] = (PlaceholderFragment) fr;
            return fr;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;
        RegistrationActivity activity;

        public SendEmailAsyncTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
                    activity.displayMessage("Email sent.");
                } else {
                    activity.displayMessage("Email failed to send.");
                }

                return true;
            } catch (AuthenticationFailedException e) {
                //Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                //e.printStackTrace();
                activity.displayMessage("Authentication failed."+e);
                return false;
            } catch (MessagingException e) {
                //Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
                //e.printStackTrace();
                activity.displayMessage("Email failed to send."+e);
                return false;
            } catch (Exception e) {
                //e.printStackTrace();
                activity.displayMessage("Unexpected error occured."+e);
                return false;
            }
        }
    }
}
