package com.progrema.superbaby.ui.fragment.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

public class BabyInputFragment extends Fragment implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private final int INTENT_USE_CAMERA = 0;
    private final int INTENT_FROM_GALLERY = 1;
    private final int INTENT_CROP_PICTURE = 2;
    private EditText nameHandler;
    private Button birthdayHandler;
    private Spinner sexHandler;
    private ImageButton acceptHandler;
    private ImageButton cancelHandler;
    private ImageButton imageHandler;
    private View root;
    private ArrayAdapter<String> adapter;
    private String babyName, babyBirthday, babySexType, imageUriString;
    private Uri cameraImageUri;
    private Bitmap imageBitmap;
    private int year, month, date;
    private boolean isCameraIntent;

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
        prepareCancelHandler();
        prepareImageHandler();
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

    private void prepareCancelHandler() {
        cancelHandler = (ImageButton) root.findViewById(R.id.baby_input_cancel);
        cancelHandler.setOnClickListener(this);
    }

    private void prepareImageHandler() {
        imageHandler = (ImageButton) root.findViewById(R.id.baby_input_image);
        imageHandler.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.baby_input_birthday:
                onBirthdayClick();
                break;
            case R.id.baby_input_image:
                onImageClick();
                break;
            case R.id.baby_input_accept:
                onAcceptClick();
                break;
            case R.id.baby_input_cancel:
                onCancelClick();
                break;
        }
    }

    private void onCancelClick() {
        goToHomeActivity();
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
        deleteTemporaryBitmap();
        saveBitmapOnDirectory();
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
        baby.setPicture(Uri.parse(replaceFileName(imageUriString)));
        if (babySexType.equals(BaseActor.Sex.MALE.getTitle())) {
            baby.setSex(BaseActor.Sex.MALE);
        } else if (babySexType.equals(BaseActor.Sex.FEMALE.getTitle())) {
            baby.setSex(BaseActor.Sex.FEMALE);
        }
        baby.insert(getActivity());
    }

    private String replaceFileName(String fileName) {
        if (isCameraIntent()) {
            return (fileName.replace("IMG_TMP", "IMG_" + babyName));
        } else {
            File imageDirectory =
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Superbaby");
            return ("file://" + imageDirectory.getPath() + File.separator + "IMG_" + babyName + ".jpg");
        }
    }

    private void setActiveBabyContext() {
        ActiveContext.setActiveBaby(getActivity(), babyName);
    }

    private void skipLoginNextStartup() {
        SharedPreferences setting = getActivity().getSharedPreferences(LoginActivity.PREF_LOGIN, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean(LoginActivity.PREF_SKIP_LOGIN, false);
        editor.commit();
    }

    private void goToHomeActivity() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    private void onImageClick() {
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
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraImageUri = Uri.fromFile(createImageOnDirectory());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            startActivityForResult(intent, INTENT_USE_CAMERA);
        } catch (ActivityNotFoundException error) {
            String errorMessage = getString(R.string.camera_intent_error);
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void onGalleryClick() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, INTENT_FROM_GALLERY);
        } catch (ActivityNotFoundException error) {
            String errorMessage = getString(R.string.gallery_intent_error);
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_FROM_GALLERY:
                processImageFromGallery(resultCode, data);
                break;
            case INTENT_USE_CAMERA:
                processImageFromCamera(resultCode);
                break;
            case INTENT_CROP_PICTURE:
                showFinalImage(resultCode, data);
                break;
        }
    }

    private void processImageFromCamera(int resultCode) {
        setCameraIntent(true);
        if (resultCode == Activity.RESULT_OK)
            imageUriString = cameraImageUri.toString();
        performImageCrop(Uri.parse(imageUriString));
    }

    private void processImageFromGallery(int resultCode, Intent data) {
        setCameraIntent(false);
        if (resultCode == Activity.RESULT_OK)
            imageUriString = data.getData().toString();
        performImageCrop(Uri.parse(imageUriString));
    }

    private void showFinalImage(int resultCode, Intent data) {
        Bundle extras;
        if (resultCode == Activity.RESULT_OK) {
            extras = data.getExtras();
            imageBitmap = extras.getParcelable("data");
            imageBitmap = getRoundedCornerBitmap(imageBitmap);
            imageHandler.setImageBitmap(imageBitmap);
        }
    }

    private void performImageCrop(Uri selectedImage) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(selectedImage, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, INTENT_CROP_PICTURE);
        } catch (ActivityNotFoundException error) {
            String errorMessage = getString(R.string.crop_intent_error);
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            performDefaultImageCrop();
        }
    }

    private void performDefaultImageCrop() {
        try {
            imageBitmap = decodeUri(Uri.parse(imageUriString), getActivity());
            imageHandler.setImageBitmap(imageBitmap);
        } catch (Exception e) {
            Log.e("_DBG_IMAGE", Log.getStackTraceString(e));
        }
    }

    private Bitmap decodeUri(Uri selectedImage, Context inputContext) throws FileNotFoundException {
        int REQUIRED_SIZE = 140;
        BitmapFactory.Options optionsOne = new BitmapFactory.Options();
        BitmapFactory.Options optionsTwo = new BitmapFactory.Options();
        int width = optionsOne.outWidth, height = optionsOne.outHeight;
        int scale = 1;
        optionsOne.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                inputContext.getContentResolver().openInputStream(selectedImage), null, optionsOne);
        while (true) {
            if (width / 2 < REQUIRED_SIZE || height / 2 < REQUIRED_SIZE)
                break;
            width /= 2;
            height /= 2;
            scale *= 2;
        }
        optionsTwo.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                inputContext.getContentResolver().openInputStream(selectedImage), null, optionsTwo);
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

    private File createImageOnDirectory() {
        File imageDirectory =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Superbaby");
        if ((!imageDirectory.exists()) && (!imageDirectory.mkdir())) return null;
        return new File(imageDirectory.getPath() + File.separator + "IMG_TMP.jpg");
    }

    private void deleteTemporaryBitmap() {
        File imageDirectory =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Superbaby");
        File tempFile = new File(imageDirectory.getPath() + File.separator + "IMG_TMP.jpg");
        if (tempFile.exists())
            tempFile.delete();
    }

    private void saveBitmapOnDirectory() {
        File imageDirectory =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Superbaby");
        Baby baby = ActiveContext.getActiveBaby(getActivity());
        try {
            FileOutputStream out = new FileOutputStream(imageDirectory.getPath() + File.separator + "IMG_" + baby.getName() + ".jpg");
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCameraIntent() {
        return isCameraIntent;
    }

    private void setCameraIntent(boolean isCameraIntent) {
        this.isCameraIntent = isCameraIntent;
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int color = 0xff424242;
        float roundPx = 10;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
