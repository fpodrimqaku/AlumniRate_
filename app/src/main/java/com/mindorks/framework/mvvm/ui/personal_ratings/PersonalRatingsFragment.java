
package com.mindorks.framework.mvvm.ui.personal_ratings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.framework.mvvm.BR;
import com.mindorks.framework.mvvm.HomeActivity;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.databinding.PersonalRatingsBinding;
import com.mindorks.framework.mvvm.di.component.FragmentComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


public class PersonalRatingsFragment extends BaseFragment<PersonalRatingsBinding, PersonalRatingsViewModel> implements PersonalRatingsNavigator {

    public static final String TAG = "PersonalRatings";
    private RecyclerView questionnaireRecyclerView;

    public static PersonalRatingsFragment newInstance() {
        Bundle args = new Bundle();
        PersonalRatingsFragment fragment = new PersonalRatingsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.personal_ratings;
    }


    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mViewModel.setNavigator(this);

        try{
            ClearCacheImprovised();

        }catch(Exception exe ){}
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initiateQuestionnaireRecyclerView(view);
    }

    private void initiateQuestionnaireRecyclerView(View parentView) {

        questionnaireRecyclerView
                = parentView.findViewById(R.id.rv_personalRatings);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getActivity());

        questionnaireRecyclerView.setLayoutManager(
                linearLayoutManager);
        PersonalRatingsAdapter adapter = new PersonalRatingsAdapter(getContext(),new ArrayList<>(), mViewModel,this::storeImageIntoCacheAndShareIt);


        //TODO INVESTIGATE THE ABOVE ROWS LATER

                super.mViewModel.questionnaireDataCollected.observe(this.getActivity(), new Observer<ConcurrentMap<String, QuestionnaireDataCollected>>() {
                    @Override
                    public void onChanged(ConcurrentMap<String, QuestionnaireDataCollected> stringQuestionnaireDataCollectedConcurrentMap) {
                        adapter.updateData(stringQuestionnaireDataCollectedConcurrentMap.values().stream().collect(Collectors.toList()));
                    }
                });

        questionnaireRecyclerView.setLayoutManager(linearLayoutManager);

        questionnaireRecyclerView.setAdapter(adapter);

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        // ClearCacheImprovised();
                    }
                }
            });

    public void shareQrCode(Uri ImageUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, ImageUri);
        shareIntent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/jpeg");
        // startActivity(Intent.createChooser(shareIntent, null));




        someActivityResultLauncher.launch(shareIntent);
    }

    public Uri storeImageIntoCacheAndShareIt(Bitmap bitmapToStore, String nameToStoreWith, String extension) {
        File sd = getContext().getCacheDir();
        File folder = new File(sd, "/edurate/");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                folder.mkdirs();
            }
        }

        File fileName = new File(folder, nameToStoreWith + "."+extension);

        try (FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName))) {
            bitmapToStore.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Uri uri = FileProvider.getUriForFile(getContext(), "com.mindorks.framework.mvvm", fileName);

            shareQrCode(uri);

            return uri;
        } catch (Exception e) {
            return null;
        }

    }





    public void ClearCacheImprovised() {
        try {
            File sd = getContext().getCacheDir();
            File folder = new File(sd, "/edurate");

            for (File c : folder.listFiles()) {
                c.delete();
            }


        } catch (Exception exe) {

        }
    }





}
