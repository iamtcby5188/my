const initView = Symbol('initView');

const loadData = Symbol('loadData');
const initChart = Symbol('initChart');

const buildChartOption = Symbol('buildChartOption');
const buildChartGraphs = Symbol('buildChartGraphs');
const buildChartListeners = Symbol('buildChartListeners');

const showChartPointDetail = Symbol('showChartPointDetail');

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
            "backgroundColor": "#505050",

            "valueAxes": [{
                "id": "v1",
                "axisAlpha": 1,
                "position": "left",
                "title": "数量（亿）",
                "axisColor": "white",
                "ignoreAxisWidth": true
            }],
            "chartCursor": {
                "enabled": true,
            },
            "dataDateFormat": 'YYYY-MM-DD',
            "categoryField": "date",
            "categoryAxis": {
                // classNameField: "quoteDate",
                // categoryFunction: function (category, dataItem, categoryAxis) {
                //     return new Date(category);
                // },
                "minPeriod": "DD",
                "axisAlpha": 1,
                "axisColor": "white",
                // "parseDates": true
            },

            "chartScrollbar": {
                // "graph": "graph1",
                "autoGridCount": true,
                "graphType": "column",
                "oppositeAxis": false,
                "dragIcon": "dragIconRectSmall",
                "dragIconHeight": 20,
                "offset": 36,
                "gridAlpha": 0,
                "color": "#888888",
                "scrollbarHeight": 20,
                "backgroundAlpha": 1,
                "backgroundColor": "#1e2021",
                "selectedBackgroundAlpha": 0.6,
                "selectedBackgroundColor": "#333739",
                "graphFillAlpha": 0,
                "autoGridCount": true,
                "selectedGraphFillAlpha": 0,
                "graphLineAlpha": 0.2,
                "graphLineColor": "#c2c2c2",
                "selectedGraphLineColor": "#888888",
                "selectedGraphLineAlpha": 1

            },
        }
    }
};

class chartNetVolumeHistoryCtrl {

    constructor($mdDialog) {
        this.$mdDialog = $mdDialog;

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

        option.listeners = this[buildChartListeners]();

        return option;
    };

    [buildChartGraphs]() {
        return [
            { id: "graph1", lineColor: "none", balloonText: "[[netVolume]]", title: "", valueField: "netVolume" },
        ].map(e => {
            return {
                "id": e.id,
                "balloonText": e.balloonText,
                // "fillAlphas": 1,
                "bullet": "none",
                "title": e.title,
                "bulletSize": 4,
                // "bulletBorderColor": "black",

                "colorField": "color",
                "fillAlphas": 1,
                "connect": false,
                
                "lineColor": e.lineColor,
                "lineColorField": "color",
                // "lineThickness": 1,
                "type": "column",
                "valueField": e.valueField,
                "bulletBorderAlpha": 1
            }
        });
    };

    [buildChartListeners]() {
        return [
            {
                event: "init",
                method: e => {

                    // 单击事件直接注册到chartDiv上， 注册到chart对象上无效
                    e.chart.chartDiv.addEventListener("click", type => {
                        if (type && type.target && type.target.nodeName === 'path') {
                            this.selectChartPoint = e.chart.dataProvider[e.chart.lastCursorPosition];
                            this[showChartPointDetail](e, this.selectChartPoint);
                        }
                    });
                }
            }, {
                event: "changed",
                method: e => { e.chart.lastCursorPosition = e.index; }
            }
        ];

    };

    [showChartPointDetail](event, data) {
        var config = {
            // attachTo: angular.element(document.querySelector('#chart_net_volume_history')),
            parent: angular.element(document.querySelector('#chart_net_volume_history')),
            // targetEvent: event,
            controller: [() => { }],
            controllerAs: '$ctrl',
            bindToController: true,
            locals: {
                theme: this.theme || 'default',
                dataSource: data
            },
            template: require('./template/dialog_chart_net_volume_detail.html'),
            clickOutsideToClose: true,
        };

        this.$mdDialog.show(config);
    };

    [initChart]() {
        if (!this.chartRef) {

            var option = this[buildChartOption]();

            this.chartRef = AmCharts.makeChart("chart_net_volume_history", option);
        }
    };

    [initView]() {
        console.debug('chartNetVolumeHistoryCtrl initView');

        this[initChart]();
    };

    $onInit() {
    };

    $onChanges(event) {

        if (!event) return;

        if (event.chartData && event.chartData.currentValue) {

            if (this.chartData instanceof Array) {

                this.chartData.sort((e1, e2) => e1.date - e2.date);

                this.chartData.forEach(e => {
                    e.date = new Date(e.date).formatDate('MM-dd');
                    e.color = e.netVolume === 0 ? "white" : (e.netVolume > 0 ? "#dc4444" : "#25ba93");
                });
            }

            this[initChart]();

            this[loadData]();
        }
    };

};

let chartNetVolumeHistory = () => {
    return {
        template: require('./template/chart_net_volume_history.html'),
        bindings: {
            theme: '@mdTheme',
            chartData: '<'
        },
        controller: ['$mdDialog', chartNetVolumeHistoryCtrl]
    }
};

export default chartNetVolumeHistory;