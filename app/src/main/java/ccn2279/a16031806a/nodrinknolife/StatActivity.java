package ccn2279.a16031806a.nodrinknolife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public class StatActivity extends AppCompatActivity {
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        barChart = findViewById(R.id.barChart);
        updateChart();
    }

    private void updateChart() {
        final String[] day = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        // Format Y-Axis Label
        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return day[(int) value];
            }
        };

        // Format bar values
        IValueFormatter iValueFormatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat mFormat = new DecimalFormat("###,###,##0");
                return mFormat.format(value);
            }
        };

        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Test");
        dataSet.setColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setValueTextSize((float) 16);
        dataSet.setValueFormatter(iValueFormatter);

        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        //barChart.getLegend().setEnabled(false);
        barChart.setDescription(null);

        // Setting for X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(iAxisValueFormatter);
        ;
        xAxis.setPosition(XAxisPosition.BOTTOM);

        // Setting for Y-rightAxis
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        rightAxis.setAxisMinimum((float) 0);

        // Setting for Y-leftAxis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        leftAxis.setAxisMinimum((float) 0);

        // Reload chart
        barChart.invalidate();
    }
}
