
package com.mindorks.framework.mvvm.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.framework.mvvm.BR;
import com.mindorks.framework.mvvm.BuildConfig;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireOrganization;
import com.mindorks.framework.mvvm.databinding.ActivityMainBinding;
import com.mindorks.framework.mvvm.databinding.NavHeaderMainBinding;
import com.mindorks.framework.mvvm.di.component.ActivityComponent;
import com.mindorks.framework.mvvm.ui.about.AboutFragment;
import com.mindorks.framework.mvvm.ui.base.BaseActivity;
import com.mindorks.framework.mvvm.ui.feed.FeedActivity;
import com.mindorks.framework.mvvm.ui.login.LoginActivity;
import com.mindorks.framework.mvvm.ui.main.rating.RateUsDialog;
import com.mindorks.framework.mvvm.utils.ScreenUtils;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator {

    private ActivityMainBinding mActivityMainBinding;
    private SwipePlaceHolderView mCardsContainerView;
    private DrawerLayout mDrawer;

    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(AboutFragment.TAG);
        if (fragment == null) {
            super.onBackPressed();
        } else {
            onFragmentDetached(AboutFragment.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(mViewModel.getDataManager().getCurrentLoginUserMode())
        getMenuInflater().inflate(R.menu.ratee_main_menu, menu);
        else
            getMenuInflater().inflate(R.menu.rater_main_menu, menu);
        return true;
    }

    public void onFragmentDetached(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow();
            unlockDrawer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        switch (item.getItemId()) {
            case R.id.scan_qr_code: {
            startScanningActivity();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void openLoginActivity() {
        startActivity(LoginActivity.newIntent(this));
        finish();
    }


    protected void onCreatesss(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = getViewDataBinding();
        mViewModel.setNavigator(this);
        setUp();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (getViewDataBinding().getViewModel().getDataManager().getCurrentLoginUserMode() == true) {
            navView.inflateMenu(R.menu.ratee_nav_menu);

        } else {
            navView.inflateMenu(R.menu.rater_nav_menu);

        }

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mActivityMainBinding = getViewDataBinding();
        mViewModel.setNavigator(this);
        setUp();

    }


    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawer != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void lockDrawer() {
        if (mDrawer != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void setUp() {
        mDrawer = mActivityMainBinding.drawerView;
        mToolbar = mActivityMainBinding.toolbar;
        mNavigationView = mActivityMainBinding.navigationView;
        mCardsContainerView = mActivityMainBinding.cardsContainer;

        //  setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        setupNavMenu();
        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        mViewModel.updateAppVersion(version);
        mViewModel.onNavMenuCreated();
        setupCardContainerView();
        subscribeToLiveData();
    }

    private void setupCardContainerView() {
        int screenWidth = ScreenUtils.getScreenWidth(this);
        int screenHeight = ScreenUtils.getScreenHeight(this);

        mCardsContainerView.getBuilder()
                .setDisplayViewCount(3)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth((int) (0.90 * screenWidth))
                        .setViewHeight((int) (0.75 * screenHeight))
                        .setPaddingTop(20)
                        .setSwipeRotationAngle(10)
                        .setRelativeScale(0.01f));

        mCardsContainerView.addItemRemoveListener(count -> {
            if (count == 0) {
                // reload the contents again after 1 sec delay
                new Handler(getMainLooper()).postDelayed(() -> {
                    //Reload once all the cards are removed
                    mViewModel.loadQuestionCards();
                }, 800);
            } else {
                mViewModel.removeQuestionCard();
            }
        });
    }

    private void setupNavMenu() {
        NavHeaderMainBinding navHeaderMainBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_header_main, mActivityMainBinding.navigationView, false);
        mActivityMainBinding.navigationView.addHeaderView(navHeaderMainBinding.getRoot());
        navHeaderMainBinding.setViewModel(mViewModel);

        mNavigationView.setNavigationItemSelectedListener(
                item -> {
                    mDrawer.closeDrawer(GravityCompat.START);
                    switch (item.getItemId()) {
                        case R.id.navItemAbout:
                            showAboutFragment();
                            return true;
                        case R.id.navItemRateUs:
                            RateUsDialog.newInstance().show(getSupportFragmentManager());
                            return true;
                        case R.id.navItemFeed:
                            startActivity(FeedActivity.newIntent(MainActivity.this));
                            return true;
                        case R.id.navItemLogout:
                            mViewModel.logout();
                            return true;
                        default:
                            return false;
                    }
                });
    }

    private void showAboutFragment() {
        lockDrawer();
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.clRootView, AboutFragment.newInstance(), AboutFragment.TAG)
                .commit();
    }

    private void subscribeToLiveData() {
        mViewModel.getQuestionCardData().observe(this, questionCardDatas -> mViewModel.setQuestionDataList(questionCardDatas));
    }

    private void unlockDrawer() {
        if (mDrawer != null) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


    public void startScanningActivity() {

        IntentIntegrator cameraScanner  = new IntentIntegrator(this);
        cameraScanner
                //.forSupportFragment(this)
                .setPrompt("Scan the QR code!")
                .setCameraId(0)
                //.setCaptureActivity(this.getActivity().getClass())
                .setOrientationLocked(true)
                .initiateScan();


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {

                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                MutableLiveData<QuestionnaireOrganization> questionnaireOrganizationMutableLiveData = mViewModel.CheckIfOrganizedQestionnaireExists(intentResult.getContents());
                questionnaireOrganizationMutableLiveData.observe((LifecycleOwner) this, (x) -> {

                    if (x != null && x.get_QRCode() != null) {

                        MainActivity homeActivity  = (MainActivity) this;
                        mViewModel.setCurrentFormScannedUID(intentResult.getContents());

                        BottomNavigationView bottomNavigationView;
                        bottomNavigationView = (BottomNavigationView) homeActivity.findViewById(R.id.nav_view);
                        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);


                    } else {
                        Toast.makeText(this, "Questionnaire Not Found", Toast.LENGTH_SHORT).show();

                    }


                });
                Toast.makeText(this, intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}
