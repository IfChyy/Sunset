package com.example.ifchyyy.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Ivo Georgiev(IfChyy)
 * SunsetFragment is a class representing two rectangles(sky and sea)
 * and two circles representing a sun and a sun reflection
 * idea is to learn animation classes in adnroid
 * while the sun goes up or down and the reflection to
 */

public class SunsetFragment extends Fragment implements View.OnClickListener {

    private View sceneView, sunView, skyView, sunViewReflected, seaView;
    private int blueSkyColor, sunsetSkyColor, nightSkyColor;
    private boolean isDone = false;

    public SunsetFragment newInstance() {
        return new SunsetFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        //init the whole view and set it id and onclick event
        sceneView = view;
        sceneView.setId(View.generateViewId());
        sceneView.setOnClickListener(this);
        //init sun and sky views
        sunView = view.findViewById(R.id.sun);
        skyView = view.findViewById(R.id.sky);
        //init reflection
        sunViewReflected = view.findViewById(R.id.sunReflection);
        //init sea
        seaView = view.findViewById(R.id.sea);

        //init sky colours
        Resources resources = getResources();
        blueSkyColor = resources.getColor(R.color.blue_sky);
        sunsetSkyColor = resources.getColor(R.color.sunset_sky);
        nightSkyColor = resources.getColor(R.color.night_sky);


        return view;
    }

    boolean isPaused = false;
    AnimatorSet animatorSet;
    AnimatorSet waterReflectStart;
    AnimatorSet animatorSetReverse;
    AnimatorSet waterReflectReverse;


    @Override
    public void onClick(View v) {
        if (v.getId() == sceneView.getId()) {
            //check which animation to play start or reverse

            if (!isDone) {

                startAnimation();
                sunStartReflecetion();
                isDone = true;
            } else {

                reverseAnimation();
                sunReversedAnimation();
                isDone = false;
            }
        }
    }


    //method to animate the sun and sky
    private void startAnimation() {
        float sunYStart = sunView.getTop();
        float sunYEnd = skyView.getHeight();

        //animates the sun from its position to hide in the sky
        ObjectAnimator heightAnmator = ObjectAnimator
                .ofFloat(sunView, "y", sunView.getY(), sunYEnd + sunView.getHeight() * 0.2f)
                .setDuration(3000);

        heightAnmator.setInterpolator(new AccelerateInterpolator());
        //    heightAnmator.start();

        ObjectAnimator heatAnimator = ObjectAnimator.ofPropertyValuesHolder(sunView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f)
                , PropertyValuesHolder.ofFloat("scaleY", 1.2f))
                .setDuration(500);
        heatAnimator.setRepeatCount(6);
        heatAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heatAnimator.setStartDelay(1000);

        //animates the colours of the sky
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
                .setDuration(3000);

        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());
        //  sunsetSkyAnimator.start();

        //animate the sky to night color
        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(skyView, "backgroundColor", sunsetSkyColor, nightSkyColor)
                .setDuration(3000);
        //ArgbEvaluetor makes a smoot transition between the colors chaning(else its super fast)
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());


        //animatorSet listnes for one animation finishing to start others
        animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnmator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);


        animatorSet.start();

    }


    //method to reverse the First Animation
    private void reverseAnimation() {
        float sunYStart = sunView.getTop();
        float sunYEnd = skyView.getHeight();

        //animates the sun from its position to hide in the sky
        ObjectAnimator heightAnmator = ObjectAnimator
                .ofFloat(sunView, "y", sunView.getY(), sunYStart)
                .setDuration(3000);

        heightAnmator.setInterpolator(new AccelerateInterpolator());
        //heightAnmator.start();

        ObjectAnimator heatAnimator = ObjectAnimator.ofPropertyValuesHolder(sunView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f)
                , PropertyValuesHolder.ofFloat("scaleY", 1.2f))
                .setDuration(500);
        heatAnimator.setRepeatCount(6);
        heatAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heatAnimator.reverse();
        heatAnimator.setStartDelay(1000);

        //animates the colours of the sky
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(skyView, "backgroundColor", sunsetSkyColor, blueSkyColor)
                .setDuration(3000);

        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());
        //  sunsetSkyAnimator.start();

        //animate the sky to night color
        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(skyView, "backgroundColor", nightSkyColor, sunsetSkyColor)
                .setDuration(3000);
        //ArgbEvaluetor makes a smoot transition between the colors chaning(else its super fast)
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());


        //reversing the previous animation
        animatorSetReverse = new AnimatorSet();
        animatorSetReverse
                .play(nightSkyAnimator)
                .before(sunsetSkyAnimator)
                .with(heightAnmator);


        animatorSetReverse.start();

    }


    //start the animation of the reflection of the sun in the water
    private void sunStartReflecetion() {
        //end positon of reflection sun
        float endPosition = sunViewReflected.getY() - seaView.getHeight();
        //start pos of reflection sun
        float startPosition = sunViewReflected.getHeight();
        //animator with chanlged start position CHALLENGE
        ObjectAnimator heightRefcAnimator = ObjectAnimator
                .ofFloat(sunViewReflected, "y", sunViewReflected.getY(), endPosition)
                .setDuration(5000);
        //interpolator used because Animator class doesnt know exactly how to shift int/float values
        //for smoot animation
        heightRefcAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        waterReflectStart = new AnimatorSet();
        waterReflectStart.play(heightRefcAnimator);
        waterReflectStart.start();
    }

    //start the animation of the reflection of the sun in the water reversed
    private void sunReversedAnimation() {
        //end position of reflection sun
        float endPosition = sunViewReflected.getY() - seaView.getHeight();
        //start position of reflection sun
        float startpPosition = sunViewReflected.getHeight();
        //animator class (instance) with changle end position CHALLENGE
        ObjectAnimator heightRefcAnimator = ObjectAnimator
                .ofFloat(sunViewReflected, "y", sunViewReflected.getY(), startpPosition)
                .setDuration(4000);
        //interpolator used because Animator class doesnt know exactly how to shift int/float values
        //for smoot animation
        heightRefcAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        waterReflectReverse = new AnimatorSet();
        waterReflectReverse.play(heightRefcAnimator);
        waterReflectReverse.start();
    }


}
