package com.mindorks.framework.mvvm.ui.personal_ratings;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.WriterException;
import com.mindorks.framework.mvvm.HomeActivity;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import kotlin.jvm.functions.Function3;

public class PersonalRatingsAdapter extends RecyclerView.Adapter<PersonalRatingsListItemViewHolder> {

    List<QuestionnaireDataCollected> items;
    public PersonalRatingsViewModel viewModel;
    private Context context;
    private Function3<Bitmap,String,String,Uri> shareImageFunction;
    private Bitmap bitmapToShareTempStoreHere ;
    WindowManager manager;

    public PersonalRatingsAdapter(Context context, List<QuestionnaireDataCollected> items, PersonalRatingsViewModel viewModel, Function3<Bitmap,String,String,Uri> shareImage) {
       items.sort((y,x)->{return x.getQuestionnaireOrganization().getCreationDateTime().compareTo(y.getQuestionnaireOrganization().getCreationDateTime());});
        this.items = items;
        this.viewModel = viewModel;
        this.context = context;
        this.manager =  (WindowManager) context.getSystemService(WINDOW_SERVICE);
        this.shareImageFunction = shareImage;
    }


    @Override
    public PersonalRatingsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.questionnaire_organizations_collected_data_list_item,
                        parent,
                        false);


        PersonalRatingsListItemViewHolder personalRatingsListItemViewHolder = new PersonalRatingsListItemViewHolder(context,itemView);
        setViewShareQrCodeEventOnCLick(personalRatingsListItemViewHolder, itemView);
        return personalRatingsListItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalRatingsListItemViewHolder holder, int position) {
        QuestionnaireDataCollected item = items.get(position);
        holder.initiateItem(item);
    }

    public void updateData(List<QuestionnaireDataCollected> newItems) {
        newItems.sort((y,x)->{return x.getQuestionnaireOrganization().getCreationDateTime().compareTo(y.getQuestionnaireOrganization().getCreationDateTime());});
        items.clear();
        items.addAll(newItems);
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setViewShareQrCodeEventOnCLick(PersonalRatingsListItemViewHolder viewHolder, View view) {

        ImageView mDialogButton;


        mDialogButton = view.findViewById(R.id.imageViewShowQrCode);
        Dialog dialog = new Dialog(context);


        mDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button okay_text, cancel_text;
                ImageView qrCodeDisplayer ;
                dialog.setContentView(R.layout.share_qr_code_dialog);
                 qrCodeDisplayer = dialog.findViewById(R.id.imageViewShowQrCodeForSharing);
                initiateQrCode(viewHolder.getQrCode(),qrCodeDisplayer);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                okay_text = dialog.findViewById(R.id.okay_text);
                cancel_text = dialog.findViewById(R.id.cancel_text);

                okay_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        shareImageFunction.invoke( bitmapToShareTempStoreHere, viewHolder.getQrCode(),  "jpeg");
                       // dialog.dismiss();
                       // Toast.makeText(context, "okay clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                cancel_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                       // Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });


    }


    public void initiateQrCode(String qrCode, ImageView imageViewToPutCodeOn) {
        final ImageView qrCode_image = imageViewToPutCodeOn;
        String fvalue_string = "";

        fvalue_string = qrCode;
        //getViewDataBinding().getViewModel().setQuestionnaireQrCode(fvalue_string);

         Display display = manager.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(fvalue_string, null, QRGContents.Type.TEXT, dimen);

        try {
            // getting our qrcode in the form of bitmap.
             bitmapToShareTempStoreHere = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCode_image.setImageBitmap(bitmapToShareTempStoreHere);
            //Uri imageToShareUri= storeImageIntoCacheAndReturnUri(bitmap,fvalue_string,"jpeg");
            //  shareQrCode(imageToShareUri);

        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }


    }

}
