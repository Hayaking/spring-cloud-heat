package com.consumer.consumer.bean.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ChartResponse {
    private int code = 200;
    private List<MetricNote> metricNoteList = new LinkedList<>();
    private ChartData data = new ChartData();
    public ChartResponse() {
    }

    public Serie addSerie(String name, SerieType type, String unit,
                          boolean isLast, String reportName, String aggr, String typeName) {
        Serie serie = this.addSerie(name, type, unit);
        serie.setLast(isLast);
        serie.setReportName(reportName);
        serie.setAggr(aggr);
        serie.setMetricTypeName(typeName);
        return serie;
    }

    public Serie addSerie(String name, SerieType type, String unit) {
        Serie serie = new Serie();
        serie.setUnit(unit);
        serie.setName(name);
        serie.setType(type);
        serie.setFillOpacity(0.3);
        serie.setZIndex(0);
        this.data.series.add(serie);
        return serie;
    }

    public Serie addSerie(String name, SerieType type) {
        return this.addSerie(name, type, 0.3, 0);
    }

    public Serie addSerie(String name, SerieType type, double fillOpacity, int zIndex) {
        Serie serie = new Serie();
        serie.setName(name);
        serie.setType(type);
        serie.setFillOpacity(fillOpacity);
        serie.setZIndex(zIndex);
        this.data.series.add(serie);
        return serie;
    }

    @JsonIgnore
    public List<Serie> getSeries() {
        return this.data.getSeries();
    }

    public void setSeries(List<Serie> series) {
        this.data.series = series;
    }

    public void setYAxis(List<YAxis> yAxisList) {
        this.data.setYAxis(yAxisList);
    }

    public ChartResponse cleanEmptySeries() {
        List<Serie> series = getSeries();
        for (int i = 0; i < series.size(); i++) {
            if (series.get(i).isEmpty()) {
                series.remove(i);
            }
        }
        return this;
    }

    public enum SerieType {
        pie,
        line,
        bar,
        column,
        area;

        SerieType() {
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    public static class SerieDataInfoData {
        private String title;
        private Double value;
        private String unit;
        private int scale = 2;

        public SerieDataInfoData(String title, Double value, String unit, int scale) {
            this.title = title;
            this.value = value;
            this.unit = unit;
            this.scale = scale;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    public static class SerieDataInfo {
        private String title;
        private List<SerieDataInfoData> data;

        public SerieDataInfo() {
        }

        public SerieDataInfo addData(String title, Double value, String unit) {
            return this.addData(title, value, unit, 2);
        }

        public SerieDataInfo addData(String title, Double value, String unit, int scale) {
            if (this.data == null) {
                this.data = new ArrayList();
            }
            this.data.add(new SerieDataInfoData(title, value, unit, scale));
            return this;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SerieData {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String name;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long x;
        private Double y;
        @JsonIgnore
        private SerieDataInfo info;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String color;

        public SerieData(Double y) {
            this.y = y;
        }

        public SerieData(Long x, Double y) {
            this.x = x;
            this.y = y;
        }

        public SerieData(String name, Double y) {
            this.name = name;
            this.y = y;
        }

        public SerieData(Long x, Double y, String color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        @JsonProperty("tooltip")
        public String getTooltip() throws Exception {
            return (new ObjectMapper()).writeValueAsString(this.info);
        }

        public SerieDataInfo newInfo(String title) {
            this.info = new SerieDataInfo();
            this.info.setTitle(title);
            return this.info;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"yaxis", "zindex"})
    @Setter
    @Getter
    public static class Serie implements Serializable {
        private String metricTypeName;
        private String reportName;
        private String aggr;
        private String unit;
//        @NonNull
        private String name;
//        @NonNull
        private ChartResponse.SerieType type;
        @JsonProperty("yAxis")
        private int yAxis;
        @JsonProperty("zIndex")
        private int zIndex;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String stack;
        private double fillOpacity;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private boolean isLast;

        private List<SerieData> data = new ArrayList();
        private HashMap<String, Object> params;

        public Serie() {
        }

        public Serie(@NonNull String name, @NonNull ChartResponse.SerieType type) {
            if (name == null) {
                throw new NullPointerException("name");
            } else if (type == null) {
                throw new NullPointerException("type");
            } else {
                this.name = name;
                this.type = type;
            }
        }

        public SerieData addData(long x, Double y) {
            SerieData serieData = new SerieData(x, y);
            this.data.add(serieData);
            return serieData;
        }

        public SerieData addData(String name, Double y) {
            SerieData serieData = new SerieData(name, y);
            this.data.add(serieData);
            return serieData;
        }

        public SerieData addData(long x, Double y, String color) {
            SerieData serieData = new SerieData(x, y, color);
            this.data.add(serieData);
            return serieData;
        }

        public Serie addParams(String key, Object value) {
            if (null == this.params) {
                this.params = new HashMap();
            }

            this.params.put(key, value);
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "ChartResponse.Serie(name=" + this.getName() + ", type=" + this.getType() + ", yAxis=" + this.getYAxis() + ", zIndex=" + this.getZIndex() + ", data=" + this.getData() + ", params=" + this.getParams() + ")";
        }

        public boolean isEmpty() {
            return CollectionUtils.isEmpty(this.data);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    @Getter
    public static class XAxis {
        private List<String> categories = new LinkedList<>();

        public XAxis() {
        }

        public static XAxis addCategory(String category) {
            XAxis xAxis = new XAxis();
            xAxis.getCategories().add(category);
            return xAxis;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    @Getter
    public static class YAxis {
        private String tickUnit;
        private boolean opposite;
        private Long min;
        private Long max;
        private Long ceiling;
        private Long floor;

        public YAxis() {
        }

        public static YAxis addTickUnit(String tickUnit) {
            YAxis yAxis = new YAxis();
            yAxis.setTickUnit(tickUnit);
            return yAxis;
        }

        public static YAxis addTickUnit(String tickUnit, boolean opposite) {
            YAxis yAxis = new YAxis();
            yAxis.setTickUnit(tickUnit);
            yAxis.setOpposite(opposite);
            return yAxis;
        }

        public YAxis setTickUnit(String tickUnit) {
            this.tickUnit = tickUnit;
            return this;
        }

        public YAxis setOpposite(boolean opposite) {
            this.opposite = opposite;
            return this;
        }

        public YAxis setMin(Long min) {
            this.min = min;
            return this;
        }

        public YAxis setMax(Long max) {
            this.max = max;
            return this;
        }

        public YAxis setCeiling(Long ceiling) {
            this.ceiling = ceiling;
            return this;
        }

        public YAxis setFloor(Long floor) {
            this.floor = floor;
            return this;
        }
    }

    @Getter
    @Setter
    public static class MetricNote {
        private String metricName;
        private String note;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"xaxis", "yaxis"})
    @Setter
    @Getter
    public class ChartData {
        private List<Serie> series = new ArrayList();
        @JsonProperty("xAxis")
        private List<XAxis> xAxis;
        @JsonProperty("yAxis")
        private List<YAxis> yAxis = new ArrayList<>();
        private List<Annotation> annotations = new LinkedList<>();
        public ChartData() {
        }
    }

    @Setter
    @Getter
    public static class Annotation {
        private LabelOptions labelOptions = new LabelOptions();
        private List<Label> labels = new LinkedList<>();
    }

    @Setter
    @Getter
    public static class LabelOptions {
        private String backgroundColor = "rgba(255,255,255,0.5)";
        private String verticalAlign = "top";
        private Integer y = 15;
    }

    @Setter
    @Getter
    public static class Label {
        private Point point;
        private String text = "异常";
    }

    @Setter
    @Getter
    public static class Point {
        @JsonProperty("xAxis")
        private Long xAxis = 0l;
        @JsonProperty("yAxis")
        private Double yAxis = 0d;
        private Long x = 0L;
        private Double y = 0D;
    }
}
