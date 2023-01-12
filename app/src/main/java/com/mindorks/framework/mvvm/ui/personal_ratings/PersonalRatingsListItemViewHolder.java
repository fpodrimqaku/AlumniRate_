package com.mindorks.framework.mvvm.ui.personal_ratings;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.QuestionnaireDataCollected;
import com.mindorks.framework.mvvm.data.model.firebase.UserAnswerData;
import com.mindorks.framework.mvvm.ui.base.BaseViewHolder;
import com.mindorks.framework.mvvm.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;

public class PersonalRatingsListItemViewHolder extends BaseViewHolder {

    Context context;
    QuestionnaireDataCollected questionnaireDataCollected;
    TextView questionnaire_organization_collected_text;
    TextView questionnaire_organization_collected_attendees_num;
    TextView questionnaire_organization_collected_period;
     Chart chartView;
private String qrCode = "";


    public PersonalRatingsListItemViewHolder(Context context, View view) {
        super(view);
        questionnaire_organization_collected_text = view.findViewById(R.id.questionnaire_organization_collected_text);
        questionnaire_organization_collected_period = view.findViewById(R.id.questionnaire_organization_collected_period);
        questionnaire_organization_collected_attendees_num = view.findViewById(R.id.questionnaire_organization_collected_attendees_num);
        chartView = itemView.findViewById(R.id.user_specific_questionnaire_data_chart);
this.context = context;
    }

    @Override
    public void onBind(int position) {

    }


    public void initiateItem(QuestionnaireDataCollected questionnaireDataCollected) {
        qrCode = questionnaireDataCollected.getQuestionnaireOrganization().get_QRCode();
        questionnaire_organization_collected_text.setText(questionnaireDataCollected.getQuestionnaireOrganization().getQuestionnaireName());
        questionnaire_organization_collected_attendees_num.setText(""+questionnaireDataCollected.getPeopleParticipated());
        String timePattern = "hh:mm";
        String datePattern ="dd-MM-yyyy";
        SimpleDateFormat simpleDateFormatTimeOnly = new SimpleDateFormat(timePattern);
        SimpleDateFormat simpleDateFormatDateOnly = new SimpleDateFormat(datePattern);
        String fromTime = simpleDateFormatTimeOnly.format(questionnaireDataCollected.getQuestionnaireOrganization().getFromDateTime());
        String toTime = simpleDateFormatTimeOnly.format(questionnaireDataCollected.getQuestionnaireOrganization().getToDateTime());
        String helalDate = simpleDateFormatDateOnly.format(questionnaireDataCollected.getQuestionnaireOrganization().getFromDateTime());

        String period = "("+helalDate + ") Prej "+ fromTime + " deri " + toTime;

        questionnaire_organization_collected_period.setText(period);


try {
    chartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

    generateDefaultData((ColumnChartView) chartView, questionnaireDataCollected);

}catch(Exception exe){}



    }



    private void generateDefaultData(ColumnChartView chart, QuestionnaireDataCollected questionnaireDataCollected) {
        boolean hasAxes = true;
        boolean hasLabels = true;
        boolean hasLabelForSelected = true;
        boolean hasAxesNames = true;

        ColumnChartData data;
        int numSubcolumns = 5;
        int numColumns = 10;

        List<AxisValue> axisXValues = new ArrayList<AxisValue>();;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();

        Map<Integer, UserAnswerData> userAnswerDataa = questionnaireDataCollected.getUserAnswerDataCollectedForQuestionnaire();
       // axisXValues.add(new AxisValue(0));
        userAnswerDataa.keySet().stream().forEach(key->{

            axisXValues.add(new AxisValue(key -1 ).setLabel("Pyetja "+ key));

            List<SubcolumnValue> values;
            values = new ArrayList<SubcolumnValue>();
            UserAnswerData userData = userAnswerDataa.get(key);
            for (int j = 1; j <= numSubcolumns; ++j) {
                String label = "";
                label = "Pyetja " + key + "; Vlerësuar (" + context.getResources().getString(AppConstants.answersWordified.get(j)) + ")- " + userData.getOptionsPickedStats().get(j) + " person/a";
               SubcolumnValue subcolumnValue = new SubcolumnValue(userData.getOptionsPickedStats().get(j) , getChartColumnColorBasedOnRating(j)).setLabel(label);

                values.add(subcolumnValue);
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
        chart.setMaxZoom((float) 10);//Maximum method ratio
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setColumnChartData(data);
        chart.setVisibility(View.VISIBLE);
        chart.setOnValueTouchListener(new ValueTouchListener());


        /**Note: The following 7, 10 just represent a number to analogize.
         * At that time, it was to solve the fixed number of X-axis data. See (http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2);
         */
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        chart.setCurrentViewport(v);






    }


    public int getChartColumnColorBasedOnRating(int rating) {
        int color[] = {ChartUtils.COLOR_RED, ChartUtils.COLOR_ORANGE, ChartUtils.COLOR_BLUE, ChartUtils.COLOR_VIOLET, ChartUtils.COLOR_GREEN};
        return color[rating -1 ];
    }

    public String getQrCode() {
        return qrCode;
    }


    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(context, "Selected column: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            Toast.makeText(context, "Selected line point: " , Toast.LENGTH_SHORT).show();

        }





    }


}
