package com.mindorks.framework.mvvm.ui.dashboard;

import static android.content.Context.WINDOW_SERVICE;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.databinding.FragmentDashboardBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;
import com.mindorks.framework.mvvm.ui.home.QuestionnaireListNavigator;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DashboardFragment extends BaseFragment<FragmentDashboardBinding,DashboardViewModel> implements QuestionnaireListNavigator {


    private DashboardViewModel dashboardViewModel;
    private QuestionnaireOrganization questionnaireOrganization = new QuestionnaireOrganization();


    WindowManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  mViewModel.setNavigator(this);
        mViewModel.setNavigator(this);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


       // super.onCreateView(inflater, container, savedInstanceState);
       // View root = inflater.inflate(getLayoutId(), container, false);



        final TextInputEditText fvalue = (TextInputEditText) root.findViewById(R.id.nested_et_fvalue);
        final ImageView qrCode_image = (ImageView) root.findViewById(R.id.imageView_qr_code);
       // Log.d("blu3", "" + fvalue);
       // Log.d("blu3", "" + qrCode_image);

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


       // initiateThings();
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dashboard;
    }



    public void initiateQuestionnaire( ){

      /*  getDataManager().signInWithEmailAndPassword(email, password, () -> {

            getNavigator().openMainActivity();
            setIsLoading(false);
        }, () -> {

            Log.d("blu3", "here");
            setIsLoading(false);
        });
*/

    }

    @Override
    public void goBack() {

    }
}
