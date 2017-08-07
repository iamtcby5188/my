const initView = Symbol('initView');

const initChart = Symbol('initChart');
const loadData = Symbol('loadData');

const buildChartGraphs = Symbol('buildChartGraphs');
const buildChartOption = Symbol('buildChartOption');

const dataDefine = {
    chart: {
        baseOption: {
            "type": "serial",
            "theme": "ssChartStyle",
            // 顶部外余白是为了纵轴顶部的文字不被遮挡
            // "dataProvider": [],
            "marginLeft": 75,
            // "marginRight": 0,
            // "synchronizeGrid": true,
            "valueAxes": [{
                "id": "v1",
                "axisAlpha": 1,
                "position": "left",
                "title": "价格（元）",
                "axisColor": "white",
                "ignoreAxisWidth": true
            }],
            "legend": {
                "enabled": true,
                "align": "right",
                "position": "top",
                "useGraphSettings": true,
                "valueAlign": "left",
                "valueWidth": 20,
                "fontSize": 14,
            },
            "chartCursor": {
                "enabled": true,
            },
            "dataDateFormat": 'YYYY-MM-DD',
            "categoryField": "quoteDate",
            "categoryAxis": {
                // classNameField: "quoteDate",
                // categoryFunction: function (category, dataItem, categoryAxis) {
                //     return new Date(category);
                // },
                "minPeriod": "DD",
                "axisAlpha": 1,
                "axisColor": "white",
                // "parseDates": true
            }
        }
    }
};

class chartManageCtrl {

    constructor($scope) {
        this.$scope = $scope;

        this[initView]();
    };

    [loadData]() {
        if (this.chartRef) {
            this.chartRef.dataProvider = this.chartData;
            this.chartRef.validateData();
        } else {
            dataDefine.chart.baseOption.dataProvider = this.chartData;
        }
    };

    [buildChartOption]() {

        var option = angular.copy(dataDefine.chart.baseOption);

        option.graphs = this[buildChartGraphs]();

        return option;
    };

    [buildChartGraphs]() {
        return [
            { id: "graph1", lineColor: "#dc4444", balloonText: "[[priceMax]]", title: "最高值", valueField: "priceMax" },
            { id: "graph2", lineColor: "#ffc96c", balloonText: "[[priceAvg]]", title: "平均值", valueField: "priceAvg" },
            { id: "graph3", lineColor: "#58b96d", balloonText: "[[priceMin]]", title: "最低值", valueField: "priceMin" },
        ].map(e => {
            return {
                "id": e.id,
                "balloonText": e.balloonText,
                "bullet": "round",
                "title": e.title,
                "bulletSize": 4,
                "connect": false, 
                // "bulletBorderColor": "black",
                "lineColor": e.lineColor,
                "lineThickness": 2,
                // "type": "smoothedLine",
                "valueField": e.valueField,
                "bulletBorderAlpha": 1
            }
        });
    };

    [initChart]() {
        if (!this.chartRef) {

            var option = this[buildChartOption]();

            this.chartRef = AmCharts.makeChart("chart_manage", option);
        }
    };

    [initView]() {
        console.debug('chartManageCtrl initView');

        this[initChart]();
    };

    $onInit() {
    };

    $onChanges(event) {

        if (!event) return;

        if (event.chartData && event.chartData.currentValue) {
            // if (this.chartData instanceof Array) this.chartData = this.chartData.map(e => {
            //     return {
            //         quoteDate: new Date(e.quoteDate).formatDate('yyyy-MM-dd'),
            //         priceMax: e.priceMax
            //     };
            // });
            // console.debug(`set chartData`);

            if (this.chartData instanceof Array) {
                this.chartData.sort((e1, e2) => e1.quoteDate - e2.quoteDate);

                this.chartData.forEach(e => {
                    e.quoteDate = new Date(e.quoteDate).formatDate('MM-dd');
                    // if (!e.priceMax) e.priceMax = 0;
                    // console.debug(`x: ${e.quoteDate} y:${e.priceMax}`);
                });
            }

            this[initChart]();
            
            this[loadData]();
        }
    };

};

let chartManage = () => {
    return {
        template: require('./template/chart_manage.html'),
        bindings: {
            chartData: '<'
        },
        controller: ['$scope', chartManageCtrl]
    }
};

export default chartManage;