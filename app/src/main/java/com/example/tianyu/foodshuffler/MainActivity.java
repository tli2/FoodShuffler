package com.example.tianyu.foodshuffler;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private FloatingActionButton shuffleFab;
    private static Restaurant chosenRestaurant;

    private CardView card;
    private FrameLayout cardFrame;
    private ImageView cardImg;
    private ImageView cardCircle;
    private TextView cardTitle;
    private TextView cardSubhead;
    private Button cardButton;
    private int cardFrameWidth;
    private int cardFrameHeight;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState){
        //This simple UI consists of a TextView for displaying results or error messages and a button to initiate shuffle action
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_18dp);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        final MainActivity mainActivity = this;
        //Programs the shuffle button to perform shuffle action
        shuffleFab = (FloatingActionButton) findViewById(R.id.shuffle_fab);
        shuffleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShuffleFab();
                hideRestaurantCard();
                FetchRestaurantsTask fetchRestaurantsTask = new FetchRestaurantsTask(getApplicationContext(),mainActivity);
                fetchRestaurantsTask.execute();
            }
        });

        card = (CardView) findViewById(R.id.main_restaurant_card);
        cardFrame = (FrameLayout) findViewById(R.id.main_restaurant_card_frame);
        cardImg = (ImageView) findViewById(R.id.restaurant_card_image);
        cardCircle = (ImageView) findViewById(R.id.restaurant_card_circle);
        cardTitle = (TextView) findViewById(R.id.restaurant_card_title);
        cardSubhead = (TextView) findViewById(R.id.restaurant_card_subhead);
        cardButton = (Button) findViewById(R.id.restaurant_card_button);

        calculateAnimationValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void calculateAnimationValues() {
        ViewTreeObserver viewTreeObserver = cardFrame.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (android.os.Build.VERSION.SDK_INT < 16) {
                        cardFrame.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        cardFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    cardFrameWidth = cardFrame.getWidth();
                    cardFrameHeight = cardFrame.getHeight();
                    Log.d(LOG_TAG, "Card Dimensions:: width: " + cardFrameWidth + ", height: " + cardFrameHeight);
                }
            });
        }
    }

    private void showRestaurantCard() {
        cardFrame.setVisibility(View.VISIBLE);

        ObjectAnimator showCardTranslate = ObjectAnimator.ofFloat(cardFrame, "x", (float) -cardFrameWidth, 0);
        showCardTranslate.setDuration(300);
        showCardTranslate.setInterpolator(new LinearOutSlowInInterpolator());
        showCardTranslate.start();
    }

    private void hideRestaurantCard() {
        ObjectAnimator hideCardTranslate = ObjectAnimator.ofFloat(cardFrame, "x", 0, (float) cardFrameWidth);
        hideCardTranslate.setDuration(300);
        hideCardTranslate.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardFrame.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        hideCardTranslate.setInterpolator(new FastOutLinearInInterpolator());
        hideCardTranslate.start();
    }

   public void hideShuffleFab() {
        AnimatorSet hideShuffleFabAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.hide_shuffle_fab_animator);
        hideShuffleFabAnimatorSet.setTarget(shuffleFab);
        hideShuffleFabAnimatorSet.start();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showShuffleFab() {
        AnimatorSet showShuffleFabAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.show_shuffle_fab_animator);
        showShuffleFabAnimatorSet.setTarget(shuffleFab);
        showShuffleFabAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        showShuffleFabAnimatorSet.start();
    }

    public void setRestaurantCard(final Restaurant restaurant) {
        cardImg.setImageBitmap(restaurant.image);
        if(restaurant.getPalette() != null) {
            GradientDrawable circleDrawable = (GradientDrawable) cardCircle.getDrawable();
            circleDrawable.setColor(restaurant.getPalette().getVibrantColor(getResources().getColor(R.color.primary_grey)));
        }
        cardTitle.setText(restaurant.name);
        cardSubhead.setText(restaurant.getCategories());
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDetailsActivitywithRestaurant(restaurant);
            }
        });
        showRestaurantCard();
    }

    public void launchDetailsActivitywithRestaurant(Restaurant restaurant) {
        Intent detailIntent = new Intent(this, DetailsActivity.class);
        chosenRestaurant = restaurant;
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, cardImg, getResources().getString(R.string.restaurant_card_img_transition_name));
            this.startActivity(detailIntent, options.toBundle());
        } else {
            this.startActivity(detailIntent);
        }
    }

    public static Restaurant getChosenRestaurant() {
        return chosenRestaurant;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public FloatingActionButton getShuffleFab() {
        return shuffleFab;
    }
}
