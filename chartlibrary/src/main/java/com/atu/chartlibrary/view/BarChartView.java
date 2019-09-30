package com.atu.chartlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.atu.chartlibrary.R;
import com.atu.chartlibrary.entity.CustomChartEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {

    private List<List<CustomChartEntity>> spLineList = new ArrayList<>();

    private List<CustomChartEntity> resultList;

    private int spLineCount = 0;
    private int barCharCount;

    private int currentBar;

    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);

        init();
    }

    /**
     * 柱状型宽度
     */
    private float barWidth;

    /**
     * 柱状型高度
     */
    private float barHeight;

    /**
     * 柱状型顶部到第一行文字距离
     */
    private float barMarginTop;

    /**
     * 柱状型底部到第二行文字距离
     */
    private float barMarginBottom;

    /**
     * 曲线外圆
     */
    private float lineOuterCircleRadius;

    /**
     * 曲线内圆
     */
    private float lineInnerCircleRadius;

    /**
     * 曲线宽度
     */
    private float splineWidth;

    /**
     * 柱状型圆角
     */
    private float barRadius;

    /**
     * 指示三角形到第二行文字距离
     */
    private float arrowMarginTop;

    /**
     * 指示三角形宽度
     */
    private float arrowStrokeWidth;

    /**
     * 曲线外圆颜色
     */
    private @ColorInt
    int lineOuterCircleColor;

    /**
     * 曲线内园颜色
     */
    private @ColorInt
    int lineInnerCircleColor;

    /**
     * 选中时柱状型颜色
     */
    private @ColorInt
    int barCheckedColor;

    /**
     * 未选中时柱状型颜色
     */
    private @ColorInt
    int barUncheckedColor;

    /**
     * 柱状型文字大小
     */
    private float barTextSize;

    /**
     * 柱状型文字颜色
     */
    private @ColorInt
    int barTextColor;

    /**
     * 顶部行文字准基线y轴
     */
    private float topTextBaseLineY;

    /**
     * 底部行文字准极限y轴
     */
    private float bottomTextBaselineY;

    /**
     * 柱状型顶部y轴
     */
    private float barTopY;

    /**
     * 柱状型底部y轴
     */
    private float barBottomY;

    /**
     * 三角形顶部y轴
     */
    private float arrowTopY;

    /**
     * 三角形底部y轴
     */
    private float arrowBottomY;

    private Paint textPaint;
    private Paint splinePaint;

    private List<RectF> barBoundList;

    private Paint barPaint;
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;

    private Paint arrowPaint;

    private List<CustomChartEntity> barCharList;

    private float lineSmoothness;
    private List<Path> dstList = new ArrayList<>();
    private List<PathMeasure> pathMeasureList = new ArrayList<>();
    private List<List<Point>> splinePointList = new ArrayList<>();
    private List<Path> splinePathList = new ArrayList<>();

    private List<Integer> lineColorList = new ArrayList<>();

    /**
     * 箭头列表
     */
    private List<Path> arrowPathList;


    /**
     * 设置是否可选择柱条
     */
    private boolean barCheckable;

    public void setBarCheckable(boolean barCheckable) {
        this.barCheckable = barCheckable;

        if (this.barCheckable) {
            currentBar = 0;
        } else {
            currentBar = -1;
        }
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BarChartView);

        barWidth = ta.getDimension(R.styleable.BarChartView_bar_width, 0);
        barHeight = ta.getDimension(R.styleable.BarChartView_bar_height, 0);
        barCheckable = ta.getBoolean(R.styleable.BarChartView_bar_checkable,false);
        barCheckedColor = ta.getColor(R.styleable.BarChartView_bar_checked_color, Color.parseColor("#9F76FF"));
        barUncheckedColor = ta.getColor(R.styleable.BarChartView_bar_unchecked_color, Color.parseColor("#E8C7FF"));
        barTextSize = ta.getDimension(R.styleable.BarChartView_bar_text_size, 0);
        barTextColor = ta.getColor(R.styleable.BarChartView_bar_text_color, Color.parseColor("#9E9E9E"));
        barMarginTop = ta.getDimension(R.styleable.BarChartView_bar_margin_top, 0);
        barMarginBottom = ta.getDimension(R.styleable.BarChartView_bar_margin_bottom, 0);
        lineSmoothness = ta.getFloat(R.styleable.BarChartView_line_smoothness, 0);
        lineOuterCircleColor = ta.getColor(R.styleable.BarChartView_line_outer_circle_color, Color.parseColor("#FFFFFF"));
        lineOuterCircleRadius = ta.getDimension(R.styleable.BarChartView_line_outer_circle_radius, 0);
        lineInnerCircleColor = ta.getColor(R.styleable.BarChartView_line_inner_circle_color, Color.parseColor("#FF5495"));
        lineInnerCircleRadius = ta.getDimension(R.styleable.BarChartView_line_inner_circle_radius, 0);
        splineWidth = ta.getDimension(R.styleable.BarChartView_spline_width, 0);
        barRadius = ta.getDimension(R.styleable.BarChartView_bar_radius, 0);
        arrowMarginTop = ta.getDimension(R.styleable.BarChartView_arrow_margin_top, 0);
        arrowStrokeWidth = ta.getDimension(R.styleable.BarChartView_arrow_stroke_width, 0);

        ta.recycle();
    }

    /**
     * 当前曲线数量
     *
     * @return
     */
    public int getSpLineCount() {
        return spLineCount;
    }

    /**
     * 添加曲线
     *
     * @param mindResultList
     * @param color
     * @param isShowResult
     */
    public void addSpLineList(List<CustomChartEntity> mindResultList, int color, boolean isShowResult) {
        if (mindResultList != null && mindResultList.size() == barCharCount) {
            spLineCount++;

            if (isShowResult) {
                this.resultList = mindResultList;
            }
            this.spLineList.add(mindResultList);
            this.lineColorList.add(color);


            List<Point> pointList = new ArrayList<>();
            for (int j = 0; j < barCharCount; j++) {
                Point point = new Point();


                pointList.add(point);

            }

            splinePointList.add(pointList);

            splinePathList.add(new Path());
            pathMeasureList.add(new PathMeasure());

            dstList.add(new Path());

            invalidate();
        }
    }

    /**
     * 描绘说明
     */
    private void drawDescribe(Canvas canvas, int index) {
        float centerPoint = (barBoundList.get(index).right - barBoundList.get(index).left) / 2 + barBoundList.get(index).left;

        // 绘制这个三角形,你可以绘制任意多边形
        arrowPathList.get(index).moveTo(centerPoint, arrowTopY);// 此点为多边形的起点

        arrowPathList.get(index).lineTo(centerPoint - arrowStrokeWidth, arrowBottomY);
        arrowPathList.get(index).lineTo(centerPoint + arrowStrokeWidth, arrowBottomY);
        arrowPathList.get(index).close(); // 使这些点构成封闭的多边形

        if (currentBar == index) {
            arrowPaint.setColor(Color.parseColor("#EEEEEE"));
        } else {
            arrowPaint.setColor(Color.parseColor("#00000000"));
        }

        canvas.drawPath(arrowPathList.get(index), arrowPaint);
    }

    /**
     * 初始化柱状图
     *
     * @param barCharList
     */
    public void initBar(List<CustomChartEntity> barCharList) {
        this.barCharList = barCharList;
        barCharCount = this.barCharList.size();
        barBoundList = new ArrayList<>();
        arrowPathList = new ArrayList<>();
        for (int i = 0; i < barCharCount; i++) {
            barBoundList.add(new RectF());
            arrowPathList.add(new Path());
        }

        topTextBaseLineY = -fontMetrics.ascent + getPaddingTop();
        barTopY = fontMetrics.descent + barMarginTop + topTextBaseLineY;
        barBottomY = barTopY + barHeight;
        bottomTextBaselineY = barBottomY + barMarginBottom - fontMetrics.ascent;
        arrowTopY = fontMetrics.descent + bottomTextBaselineY + arrowMarginTop;
        arrowBottomY = arrowTopY + arrowStrokeWidth;

        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    private Paint.FontMetrics fontMetrics;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float barDivideWidth = (getWidth() - getPaddingLeft() - getPaddingRight() - (barWidth * barCharCount)) / (barCharCount - 1F);

        float leftBar = getPaddingLeft();
        float rightBar = barWidth + getPaddingLeft();

        for (int i = 0; i < barCharCount; i++) {
            CustomChartEntity barChar = barCharList.get(i);

            checkBar(i);

            barBoundList.get(i).top = barTopY;
            barBoundList.get(i).bottom = barBottomY;
            barBoundList.get(i).left = leftBar;
            barBoundList.get(i).right = rightBar;

            canvas.drawRoundRect(barBoundList.get(i), barRadius, barRadius, barPaint);

            canvas.drawText(barChar.getBarTypeNames()[0], ((barBoundList.get(i).right - barBoundList.get(i).left) / 2 + barBoundList.get(i).left), topTextBaseLineY, textPaint);
            canvas.drawText(barChar.getBarTypeNames()[1], ((barBoundList.get(i).right - barBoundList.get(i).left) / 2 + barBoundList.get(i).left), bottomTextBaselineY, textPaint);

            leftBar += barDivideWidth + barWidth;
            rightBar += barDivideWidth + barWidth;

            if (barCheckable) {
                drawDescribe(canvas, i);
            }
        }

        for (int i = 0; i < spLineCount; i++) {
            for (int j = 0; j < barCharCount; j++) {

                splinePointList.get(i).get(j).x = (int) ((barBoundList.get(j).right - barBoundList.get(j).left) / 2 + barBoundList.get(j).left);
                splinePointList.get(i).get(j).y = (int) (mul(barBoundList.get(j).bottom - barBoundList.get(j).top, 100 - spLineList.get(i).get(j).getScore()) / 100F + barBoundList.get(j).top);

            }

            measurePath(i);

            dstList.get(i).rLineTo(0, 0);
            float drawScale = 1f;
            float distance = pathMeasureList.get(i).getLength() * drawScale;
            if (pathMeasureList.get(i).getSegment(0, distance, dstList.get(i), true)) {

                splinePaint.setColor(lineColorList.get(i));

                //绘制线
                canvas.drawPath(dstList.get(i), splinePaint);
//                pathMeasureList.get(i).getPosTan(distance, pos, null);
            }
        }

        for (int i = 0; i < spLineCount; i++) {
            for (int j = 0; j < barCharCount; j++) {

                if (barCheckable) {
                    drawCheckCircle(canvas, splinePointList.get(i).get(j).x, splinePointList.get(i).get(j).y, j == currentBar);
                } else {
                    drawCheckCircle(canvas, splinePointList.get(i).get(j).x, splinePointList.get(i).get(j).y, true);
                }

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) arrowBottomY);
    }

    /**
     * 重置分数，改成范围随机
     *
     * @param score
     * @return
     */
    private int switchBarLevel(double score) {
        int random = (int) (Math.random() * 20);
        if (score > 80 && score <= 100) {
            return 80 + random;
        } else if (score > 60 && score <= 80) {
            return 60 + random;
        } else if (score > 40 && score <= 60) {
            return 40 + random;
        } else if (score > 20 && score <= 40) {
            return 20 + random;
        } else if (score > 0 && score <= 20) {
            return random;
        } else {
            return 0;
        }
    }

    /**
     * 画圆
     *
     * @param canvas
     * @param pointX
     * @param pointY
     * @param check
     */
    private void drawCheckCircle(Canvas canvas, int pointX, int pointY, boolean check) {
        if (check) {
            outerCirclePaint.setColor(lineOuterCircleColor);
            innerCirclePaint.setColor(lineInnerCircleColor);
        } else {
            outerCirclePaint.setColor(Color.parseColor("#00000000"));
            innerCirclePaint.setColor(Color.parseColor("#00000000"));
        }

        canvas.drawCircle(pointX, pointY, lineOuterCircleRadius, outerCirclePaint);
        canvas.drawCircle(pointX, pointY, lineInnerCircleRadius, innerCirclePaint);
    }

    private float[] pos = new float[2];

    /**
     * 乘法
     */
    public float mul(float v1, double v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).floatValue();
    }

    private void checkBar(int index) {
        if (currentBar == index) {
            textPaint.setColor(barCheckedColor);
            barPaint.setColor(barCheckedColor);
        } else {
            textPaint.setColor(barTextColor);
            barPaint.setColor(barUncheckedColor);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case (MotionEvent.ACTION_DOWN):

                /**
                 * 判断是否可点击
                 */
                if (barCheckable) {

                    /**
                     * 遍历每个柱条的坐标范围
                     */
                    for (int i = 0; i < barCharCount; i++) {
                        if (event.getX() > barBoundList.get(i).left
                                && event.getX() < barBoundList.get(i).right
                                && event.getY() > barTopY
                                && event.getY() < barBottomY) {

                            /**
                             * 如果选中的不是当前选中的柱条，则改变切换柱条选中
                             */
                            if (i != currentBar) {
                                currentBar = i;
                                invalidate();

                                if (callback != null) {
                                    if (resultList != null && resultList.size() == barCharCount) {
//                                    callback.selectResult(resultList.get(i).getResult());
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case (MotionEvent.ACTION_UP):
            case (MotionEvent.ACTION_MOVE):
            case (MotionEvent.ACTION_SCROLL):
            default:
                break;
        }
        return true;
    }

    /**
     * 初始化笔
     */
    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        splinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        splinePaint.setStrokeWidth(splineWidth);

        splinePaint.setStyle(Paint.Style.STROKE);

        outerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        innerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint.setColor(barTextColor);
        textPaint.setTextSize(barTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        fontMetrics = textPaint.getFontMetrics();

        barPaint.setColor(barUncheckedColor);

        barBoundList = new ArrayList<>();
        for (int i = 0; i < barCharCount; i++) {
            RectF barBounds = new RectF();
            barBoundList.add(barBounds);
        }
    }

    /**
     * 曲线路径
     *
     * @param index
     */
    private void measurePath(int index) {
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        for (int valueIndex = 0; valueIndex < barCharCount; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = splinePointList.get(index).get(valueIndex);
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = splinePointList.get(index).get(valueIndex - 1);
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = splinePointList.get(index).get(valueIndex - 2);
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < barCharCount - 1) {
                Point point = splinePointList.get(index).get(valueIndex + 1);
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                splinePathList.get(index).moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX);
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY);
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX);
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY);
                //画出曲线
                splinePathList.get(index).cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        pathMeasureList.get(index).setPath(splinePathList.get(index), false);
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void selectResult(String result);
    }
}
