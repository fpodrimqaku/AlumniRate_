package com.mindorks.framework.mvvm.ui.overall_ratee_stats;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.firebase.RateeRankingsData;
import com.mindorks.framework.mvvm.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class OverallRateeStatsViewHolder extends BaseViewHolder {

    Context context;

    public OverallRateeStatsViewHolder(Context context, View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_profileImage);
        userFullName = itemView.findViewById(R.id.rate_overall_data_name_lastname);
        userTitle = itemView.findViewById(R.id.ratee_title);
        chartView = itemView.findViewById(R.id.user_overall_data_chart);
        this.context = context;
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


        chartView.setInteractive(false);
        chartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        chartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);


        List<PointValue> values = new ArrayList<PointValue>();

        values.add(new PointValue(1, 21));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 7));
        values.add(new PointValue(4, 12));
        values.add(new PointValue(5, 7));
        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        ColumnChartData data = new ColumnChartData();
       // data.setLines(lines);

        // LineChartView chart = new LineChartView(context);
      //  ColumnChartView lcv = (ColumnChartView) chartView;
      //  lcv.setColumnChartData(data);
        generateDefaultData((ColumnChartView)chartView);

    }

    private void generateDefaultData(ColumnChartView chart) {
        boolean hasAxes = true;
        boolean hasLabels = true;
        boolean hasLabelForSelected = true;
        boolean hasAxesNames = true;

        ColumnChartData data;
        int numSubcolumns = 1;
        int numColumns = 8;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);

    }

}
