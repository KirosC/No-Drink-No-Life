package ccn2279.a16031806a.nodrinknolife;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class StatActivity extends AppCompatActivity {
    public static final String TAG = "Debug_NoDrinkNoLife";

    private BarChart barChartA, barChartB;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharedPreferences = getSharedPreferences(SharedPreferencesUtils.PREFERENCE_NAME, MODE_PRIVATE);

        barChartA = findViewById(R.id.barChartA);
        barChartB = findViewById(R.id.barChartB);
        updateChart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateChart();
    }

    /**
     * Update the chart to the newest data
     */
    private void updateChart() {
        // Get the Y-Axis Label String
        final String[] day = getResources().getStringArray(R.array.day_array);

        // Format Y-Axis Label
        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return day[(int) value - 1];
            }
        };

        // Format bar values to whole integer
        IValueFormatter iValueFormatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat mFormat = new DecimalFormat("###,###,##0");
                return mFormat.format(value);
            }
        };

        // ArrayList of bar for each set of data
        List<BarEntry> entriesA = new ArrayList<>();
        List<BarEntry> entriesB = new ArrayList<>();

        boolean firstWeek = mSharedPreferences.getBoolean(getString(R.string.first_week), true);
        boolean isUsingWeekA = mSharedPreferences.getBoolean(getString(R.string.is_using_weekA), true);
        boolean drawWeekAFirst;
        int todayDay = mSharedPreferences.getInt(getString(R.string.today_day), -1);
        int todayDrinks = mSharedPreferences.getInt(getString(R.string.today_drinks), -1);

        // Determine which week in SharePreferences is drawn first
        if (!(!firstWeek && isUsingWeekA)) {
            Log.d(TAG, "Draw week A first");
            drawWeekAFirst = true;
        } else {
            Log.d(TAG, "Draw week B first");
            drawWeekAFirst = false;
        }

        // Load the data from SharedPreference two times
        for (int i = 0; i < 2; i++) {
            // Draw only 1 bar chart if it is the first week
            if (firstWeek && i == 1) {
                barChartB.setVisibility(View.GONE);
                break;
            }

            // Determine which week is drawn first
            String prefix;
            if (drawWeekAFirst) {
                if (i == 0) {
                    prefix = getString(R.string.weekA_prefix);
                } else {
                    prefix = getString(R.string.weekB_prefix);
                }
            } else {
                if (i == 0) {
                    prefix = getString(R.string.weekB_prefix);
                } else {
                    prefix = getString(R.string.weekA_prefix);
                }
            }

            // Add the data into corresponding Entry
            for (int j = SharedPreferencesUtils.MONDAY; j <= SharedPreferencesUtils.SUNDAY; j++) {
                float value = (float) mSharedPreferences.getInt(prefix + j, 10);

                if (i == 0) {
                    if (j == todayDay && firstWeek) {
                        entriesA.add(new BarEntry(j, todayDrinks));
                    } else {
                        entriesA.add(new BarEntry(j, value));
                    }
                } else {
                    if (j == todayDay && !firstWeek) {
                        entriesB.add(new BarEntry(j, todayDrinks));
                    } else {
                        entriesB.add(new BarEntry(j, value));
                    }
                }
            }
        }

        // Add the Entry to the corresponding BarDataSet
        BarDataSet dataSetA, dataSetB = null;
        if (firstWeek) {
            dataSetA = new BarDataSet(entriesA, getString(R.string.current_week));
            dataSetA.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            dataSetA = new BarDataSet(entriesA, getString(R.string.last_week));
            dataSetB = new BarDataSet(entriesB, getString(R.string.current_week));
            dataSetA.setColor(getResources().getColor(R.color.colorAccent));

        }
        dataSetA.setValueTextSize((float) 16);
        dataSetA.setValueFormatter(iValueFormatter);

        // Hook the BarDataSet to the BarData which represents all data for the BarChart.
        BarData barDataA = new BarData(dataSetA);

        barChartA.setData(barDataA);
        barChartA.setDescription(null);

        // Setting for X-Axis
        XAxis xAxis = barChartA.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(iAxisValueFormatter);
        xAxis.setPosition(XAxisPosition.BOTTOM);

        // Setting for Y-rightAxis
        YAxis rightAxis = barChartA.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        rightAxis.setAxisMinimum((float) 0);

        // Setting for Y-leftAxis
        YAxis leftAxis = barChartA.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        leftAxis.setAxisMinimum((float) 0);

        // Reload chart
        barChartA.invalidate();

        // Apply the setting again if BarChart B needs to be drawn
        if (dataSetB != null) {
            dataSetB.setColor(getResources().getColor(R.color.colorPrimaryDark));
            dataSetB.setValueTextSize((float) 16);
            dataSetB.setValueFormatter(iValueFormatter);

            BarData barDataB = new BarData(dataSetB);

            barChartB.setData(barDataB);
            barChartB.setDescription(null);

            XAxis xAxisB = barChartB.getXAxis();
            xAxisB.setDrawGridLines(false);
            xAxisB.setGranularity(1f);
            xAxisB.setValueFormatter(iAxisValueFormatter);
            xAxisB.setPosition(XAxisPosition.BOTTOM);

            YAxis rightAxisB = barChartB.getAxisRight();
            rightAxisB.setDrawGridLines(false);
            rightAxisB.setEnabled(false);
            rightAxisB.setAxisMinimum((float) 0);

            YAxis leftAxisB = barChartB.getAxisLeft();
            leftAxisB.setDrawGridLines(false);
            leftAxisB.setEnabled(false);
            leftAxisB.setAxisMinimum((float) 0);

            barChartB.invalidate();
        }
    }
}