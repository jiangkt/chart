package com.atu.chart.entity;

import com.atu.chartlibrary.entity.CustomChartEntity;

import java.util.Arrays;

public class BarChartEntity implements CustomChartEntity {
    private String[] barTypeNames;
    private double score;

    public void setBarTypeNames(String[] barTypeNames) {
        this.barTypeNames = barTypeNames;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String[] getBarTypeNames() {
        return barTypeNames;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "BarChartEntity{" +
                "barTypeNames=" + Arrays.toString(barTypeNames) +
                ", score=" + score +
                '}';
    }
}
