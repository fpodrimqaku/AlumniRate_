package com.mindorks.framework.mvvm.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;
import com.mindorks.framework.mvvm.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

public class DashboardFragment extends Fragment {


    private DashboardViewModel dashboardViewModel;



    WindowManager manager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);



        final TextInputEditText fvalue = (TextInputEditText) root.findViewById(R.id.nested_et_fvalue);
        final ImageView qrCode_image = (ImageView) root.findViewById(R.id.imageView_qr_code);
        Log.d("blu3", "" + fvalue);
        Log.d("blu3", "" + qrCode_image);

        qrCode_image.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String fvalue_string = fvalue.getText().toString();
                                                //  String svalue_string = svalue.getText().toString();

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
                                                    Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                                                    // the bitmap is set inside our image
                                                    // view using .setimagebitmap method.
                                                    qrCode_image.setImageBitmap(bitmap);
                                                } catch (WriterException e) {
                                                    // this method is called for
                                                    // exception handling.
                                                    Log.e("Tag", e.toString());
                                                }


                                            }
                                        }

        );

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //   ButterKnife.bind(this, view);


        initiateThings();
    }


    public void initiateThings() {

    }

}
