package com.progrema.superbaby.ui.fragment.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.BaseActor;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.ui.activity.LoginActivity;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;

import java.io.File;
import java.util.Calendar;

public class BabyInputFragment extends Fragment implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_CODE_TAKE_PHOTO = 0;
    private static final int REQUEST_CODE_SELECT_PHOTO = 1;
    private EditText nameHandler;
    private Button birthdayHandler;
    private Spinner sexHandler;
    private ImageButton acceptHandler;
    private ImageButton pictureHandler;
    private View root;
    private ArrayAdapter<String> adapter;
    private String babyName, babyBirthday, babySexType, imageUriString;
    private Uri imageUri;
    private Bitmap babyBitmap;
    private int year, month, date;

    public static BabyInputFragment getInstance() {
        return new BabyInputFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_baby_input_login, container, false);
        prepareCalendar();
        prepareNameHandler();
        prepareBirthdayHandler();
        prepareSexHandler();
        prepareAcceptHandler();
        preparePictureHandler();
        return root;
    }

    private void prepareCalendar() {
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        date = now.get(Calendar.DATE);
    }

    private void prepareNameHandler() {
        nameHandler = (EditText) root.findViewById(R.id.baby_input_name);
    }

    private void prepareBirthdayHandler() {
        birthdayHandler = (Button) root.findViewById(R.id.baby_input_birthday);
        birthdayHandler.setText(FormatUtils.fmtDate(getActivity(),
                String.valueOf(Calendar.getInstance().getTimeInMillis())));
        birthdayHandler.setOnClickListener(this);
    }

    private void prepareSexHandler() {
        sexHandler = (Spinner) root.findViewById(R.id.baby_input_sex);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.gender_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexHandler.setAdapter(adapter);
    }

    private void prepareAcceptHandler() {
        acceptHandler = (ImageButton) root.findViewById(R.id.baby_input_accept);
        acceptHandler.setOnClickListener(this);
    }

    private void preparePictureHandler() {
        pictureHandler = (ImageButton) root.findViewById(R.id.baby_input_picture);
        pictureHandler.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.baby_input_birthday:
                onBirthdayClick();
                break;
            case R.id.baby_input_accept:
                onAcceptClick();
                break;
            case R.id.baby_input_picture:
                onPictureClick();
                break;
        }
    }

    private void onBirthdayClick() {
        DatePickerDialog dateChooser = new DatePickerDialog(getActivity(), this, year, month, date);
        dateChooser.show();
    }

    private void onAcceptClick() {
        getBabyProperty();
        storeBabyToDataBase();
        setActiveBabyContext();
        skipLoginNextStartup();
        goToHomeActivity();
    }

    private void getBabyProperty() {
        Calendar dob = Calendar.getInstance();
        dob.set(year, month, date);
        babyName = nameHandler.getText().toString();
        babyBirthday = String.valueOf(dob.getTimeInMillis());
        babySexType = (String) sexHandler.getAdapter().getItem(sexHandler.getSelectedItemPosition());
    }

    private void storeBabyToDataBase() {
        Baby baby = new Baby();
        baby.setName(babyName);
        baby.setBirthday(babyBirthday);
        if (babySexType.equals(BaseActor.Sex.MALE.getTitle())) {
            baby.setSex(BaseActor.Sex.MALE);
        } else if (babySexType.equals(BaseActor.Sex.FEMALE.getTitle())) {
            baby.setSex(BaseActor.Sex.FEMALE);
        }
        baby.insert(getActivity());
    }

    private void setActiveBabyContext() {
        ActiveContext.setActiveBaby(getActivity(), babyName);
    }

    private void skipLoginNextStartup() {
        SharedPreferences setting = getActivity().getSharedPreferences(LoginActivity.PREF_LOGIN, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean(LoginActivity.PREF_SKIP_LOGIN, true);
        editor.commit();
    }

    private void goToHomeActivity() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    private void onPictureClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.baby_image_selector_title);
        builder.setItems(R.array.add_image_method, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    onCameraClick();
                } else {
                    onGalleryClick();
                }
            }
        });
        builder.setNegativeButton(R.string.diaper_dialog_negative_button, null);
        builder.show();
    }

    private void onCameraClick() {
        Intent cameraShoot = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(getImageDir()); // create a file to save the image
        cameraShoot.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // set the image file name
        startActivityForResult(cameraShoot, REQUEST_CODE_TAKE_PHOTO);
    }

    private void onGalleryClick() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    imageUriString = data.getData().toString();
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    imageUriString = imageUri.toString();
                }
                break;
        }
        try {
            babyBitmap = FormatUtils.decodeUri(Uri.parse(imageUriString), getActivity());
            pictureHandler.setImageBitmap(babyBitmap);
        } catch (Exception e) {
            Log.e("_DBG_IMAGE", Log.getStackTraceString(e));
        }
    }

    private static File getImageDir() {
        //TODO: Can we get from camera directory?
        File imageDirectory =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Superbaby");
        if ((!imageDirectory.exists()) && (!imageDirectory.mkdir())) return null;
        String name = String.valueOf(Calendar.getInstance().getTimeInMillis());
        return new File(imageDirectory.getPath() + File.separator + "IMG_" + name + ".jpg");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.date = dayOfMonth;
        Calendar dob = Calendar.getInstance();
        dob.set(year, month, date);
        birthdayHandler.setText(FormatUtils.fmtDate(getActivity(), String.valueOf(dob.getTimeInMillis())));
    }

}
