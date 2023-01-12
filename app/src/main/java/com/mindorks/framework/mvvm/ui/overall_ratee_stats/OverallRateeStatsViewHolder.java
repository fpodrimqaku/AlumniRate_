package com.mindorks.framework.mvvm.ui.overall_ratee_stats;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.RateeRankingsData;
import com.mindorks.framework.mvvm.data.model.firebase.UserAnswerData;
import com.mindorks.framework.mvvm.ui.base.BaseViewHolder;
import com.mindorks.framework.mvvm.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;

public class OverallRateeStatsViewHolder extends BaseViewHolder {

    Context context;
    OverallRateeStatsViewModel viewModel;
    public OverallRateeStatsViewHolder(Context context, View itemView,OverallRateeStatsViewModel viewModel) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_profileImage);
        userFullName = itemView.findViewById(R.id.rate_overall_data_name_lastname);
        userTitle = itemView.findViewById(R.id.ratee_title);
        chartView = itemView.findViewById(R.id.user_overall_data_chart);
        this.context = context;
        this.viewModel  =viewModel;
    }

    public ImageView userImage;
    public TextView userFullName;
    public TextView userTitle;
    public Chart chartView;

    @Override
    public void onBind(int position) {

    }

    public void initiateItem(RateeRankingsData rateeRankingsData) {
        if (rateeRankingsData.getUser().getPhotoUrl() != null) {
            //  userImage.setImageURI(Uri.parse(rateeRankingsData.getUser().getPhotoUrl()));
            Glide.with(context).load(rateeRankingsData.getUser().getPhotoUrl()).into(userImage);
        }

        userFullName.setText(rateeRankingsData.getUser().getFirst() + " " + rateeRankingsData.getUser().getLast());
        userTitle.setText(rateeRankingsData.getUser().getTitle());


        chartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);


        generateDefaultData((ColumnChartView) chartView, rateeRankingsData);

    }

    private void generateDefaultData(ColumnChartView chart, RateeRankingsData rateeRankingsData) {
        boolean hasAxes = true;
        boolean hasLabels = true;
        boolean hasLabelForSelected = true;
        boolean hasAxesNames = true;

        ColumnChartData data;
        int numSubcolumns = 5;
        int numColumns = 10;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();;
        Map<Integer, UserAnswerData> userAnswerDataa = rateeRankingsData.getUserAnswerDataCollectedForUser();

        userAnswerDataa.keySet().stream().sorted().forEach(key -> {

                axisXValues.add(new AxisValue(key - 1).setLabel("Pyetja "+key));

            UserAnswerData userData = userAnswerDataa.get(key);
            List<SubcolumnValue> values;
            values = new ArrayList<SubcolumnValue>();
            for (int j = 1; j <= numSubcolumns; ++j) {
                String label = "";
                label = "Pyetja " + key + "; Vlerësuar (" + context.getResources().getString(AppConstants.answersWordified.get(j)) + ")- " + userData.getOptionsPickedStats().get(j) + " person/a";
                values.add(new SubcolumnValue(userData.getOptionsPickedStats().get(j), getChartColumnColorBasedOnRating(j)).setLabel(label));
            }

            Column column = new Column(values);

            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);


        });


        data = new ColumnChartData(columns);

        // data.setStacked(true);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                // axisX.setValues(new ArrayList<AxisValue>());
                axisX.setName("Pyetja");
                axisY.setName("Nr. Studentëve");
                axisY.setHasLines(false);

                axisX.setFormatter(new SimpleAxisValueFormatter().setDecimalDigitsNumber(0));
                axisY.setFormatter(new SimpleAxisValueFormatter().setDecimalDigitsNumber(0));
                axisX.setValues(axisXValues);
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }


        //Setting behavioral properties to support zooming, sliding, and Translation
        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setMaxZoom((float) 5);//Maximum method ratio
        chart.setZoomLevel(0,10,(float) 5);//Maximum method ratio
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setColumnChartData(data);
        chart.setVisibility(View.VISIBLE);
        chart.setOnValueTouchListener(new ValueTouchListener());

        /**Note: The following 7, 10 just represent a number to analogize.
         * At that time, it was to solve the fixed number of X-axis data. See (http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2);
         */
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.left = 0;
        v.right = 11;
       // chart.setCurrentViewport(v);


    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            // Toast.makeText(context, "Selected column: " + value, Toast.LENGTH_SHORT).show();
            try {
                String questionText = viewModel.getDataManager().getQuestions().getValue().stream().filter(x -> x.getNum() == columnIndex+1).findFirst().get().getQuestion();
                showModal(questionText, (int) value.getValue(), subcolumnIndex + 1);
            }catch(Exception exe){
                Toast.makeText(context, "Ka ndodhur një gabim gjatë marrjes së të dhënave!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onValueDeselected() {
            // Toast.makeText(context, "Selected line point: " , Toast.LENGTH_SHORT).show();

        }
    }



    public int getChartColumnColorBasedOnRating(int rating) {
        int color[] = {ChartUtils.COLOR_RED, ChartUtils.COLOR_ORANGE, ChartUtils.COLOR_BLUE, ChartUtils.COLOR_VIOLET, ChartUtils.COLOR_GREEN};
        return color[rating - 1];
    }


    public void showModal(String questionText, int numberOfRaters,int optionPicked) {

        Dialog dialog = new Dialog(context);
        Button closeButton;
        TextView questionTextView, ratedText;
        //ImageView qrCodeDisplayer ;
        dialog.setContentView(R.layout.question_stats);
        // qrCodeDisplayer = dialog.findViewById(R.id.imageViewShowQrCodeForSharing);
        //initiateQrCode(viewHolder.getQrCode(),qrCodeDisplayer);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        questionTextView = dialog.findViewById(R.id.modal_question_text);
        ratedText = dialog.findViewById(R.id.modal_q_stats_persons_answered);
        closeButton = dialog.findViewById(R.id.modal_view_q_stats_close);

        questionTextView.setText(questionText);
        ratedText.setText(""
                .concat("")
                .concat(String.valueOf(numberOfRaters))
                .concat(" studentë përgjigjen/et me: ")
                .concat(context.getResources().getString(AppConstants.answersWordified.get(optionPicked))));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();

    }





}


