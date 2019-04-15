package fewchore.com.exolve.fewchore.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import fewchore.com.exolve.fewchore.R;


public class SliderActivity
        extends AppCompatActivity
{
    private Button buttonSignUp;
    private Button buttonLogin;
    private LinearLayout dotsLayout;
    private int[] layouts;
    OnPageChangeListener viewPagerListener = new OnPageChangeListener()
    {
        public void onPageScrollStateChanged(int paramAnonymousInt) {}

        public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {}

        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == (layouts.length - 1))
            {
                buttonSignUp.setText(R.string.register);
                buttonLogin.setText(R.string.log_in);
            }

            dotsLayout.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonSignUp.setVisibility(View.VISIBLE);
        }
    };

    private void addBottomDots(int currentPage)
    {
        TextView[] dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_slider);
        ViewPager viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonLogin = findViewById(R.id.buttonLogin);
        layouts = new int[] {
                R.layout.slider1,
                R.layout.slider2};

        addBottomDots(0);
        buttonLogin.setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                    startActivity(new Intent(SliderActivity.this, LoginActivity.class));

            }
        });
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(this.viewPagerListener);
        viewPager.setOffscreenPageLimit(2);
        buttonSignUp.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(new Intent(SliderActivity.this, SignUpActivity.class));
            }
        });
    }

    private class MyViewPagerAdapter extends PagerAdapter {


        private MyViewPagerAdapter() {}

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        public int getCount()
        {
            return layouts.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
