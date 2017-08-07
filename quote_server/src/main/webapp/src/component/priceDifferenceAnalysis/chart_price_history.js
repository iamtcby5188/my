const initView = Symbol('initView');

const loadData = Symbol('loadData');
const initChart = Symbol('initChart');

const convertDataMapToArray = Symbol('convertDataMapToArray');
const buildDataForChart = Symbol('buildDataForChart');
const buildChartOption = Symbol('buildChartOption');
const buildChartGraphDefines = Symbol('buildChartGraphDefines');
const buildChartGraphs = Symbol('buildChartGraphs');
const buildChartListeners = Symbol('buildChartListeners');
const getSpGraphValueField = Symbol('getSpGraphValueField');

const getUpdateTrendsHistoryDto = Symbol('getUpdateTrendsHistoryDto');
const showChartPointDetail = Symbol('showChartPointDetail');

const dataDefine = {

    termGraphMap: {
        default: ['IBO001', 'R001'],

        SHIBOR_O_N: ['IBO001', 'R001'],

        SHIBOR_7D: ['IBO007', 'R007'],
        SHIBOR_1W: ['IBO007', 'R007'],

        SHIBOR_14D: ['IBO014', 'R014'],
        SHIBOR_2W: ['IBO014', 'R014'],

        SHIBOR_1M: ['IBO1M', 'R1M'],
        SHIBOR_3M: ['IBO3M', 'R3M'],
        SHIBOR_6M: ['IBO6M', 'R6M'],
        SHIBOR_9M: ['IBO9M', 'R9M'],
        SHIBOR_1Y: ['IBO1Y', 'R1Y']

    },

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
                "align": "left",
                "position": "top",
                "useGraphSettings": true,
                "valueAlign": "left",
                "valueWidth": 0,
                "fontSize": 14,

            },
            "chartCursor": {
                "enabled": true,
            },
            "dataDateFormat": 'MM-DD',
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

class chartPriceHistoryCtrl {

    constructor($mdDialog, gridPriceMarginService) {
        this.$mdDialog = $mdDialog;

        this.gridPriceMarginService = gridPriceMarginService;

        this[initView]();
    };

    onSearchCriteriaChanged() {
        var dto = this[getUpdateTrendsHistoryDto](this.chartRef);

        delete dto.beginDate;
        delete dto.endDate;

        if (!this.lastUpdateTrendsHistoryDto) {
            this.lastUpdateTrendsHistoryDto = angular.copy(dto);
            return;
        };

        this.lastUpdateTrendsHistoryDto = angular.copy(dto);

        this.gridPriceMarginService.updateTrendsHistory(dto).then(res => {

            if (!res || !res.result || !res.result[0]) return;

            // this.chartData.data = this[buildDataForChart](res.result[0]);
            // this.chartData.data = this[buildDataForChart](res.result[0], new Map(this.chartData.data.map(e => [e.date, e])));
            this.chartData.data = this[buildDataForChart](res.result[0]);

            // this[initChart]();
            // 需要重建 buildChartGraphs
            if(this.chartRef) this.chartRef.clear();
            var option = this[buildChartOption]();
            this.chartRef = AmCharts.makeChart("chart_price_history", option);

            this[loadData]();
        }, res => {
            console.error('chartPriceHistoryCtrl[buildChartListeners] e.chart.chartDiv.addEventListener("mouseup") error)');
        });
    };

    [buildDataForChart](source, oldMap) {
        var dataMap = oldMap ? oldMap : new Map();

        var setMapData = (e, i) => {

            e.quoteDate = e.quoteDate || e.date;
            if (typeof e.quoteDate !== 'number') {
                e.quoteDate = e.date;
            }

            if (e.quoteDate === undefined) {
                console.debug(`ignored item while e.quoteDate： ${JSON.stringify(e)}`);
                return;
            }

            if (dataMap.has(e.quoteDate)) {
                if (e.signCode && e.priceAvg !== undefined && e.priceAvg !== null) dataMap.get(e.quoteDate)[e.signCode] = e.priceAvg;
                if (e.price || e.period) {
                    dataMap.get(e.quoteDate).shiborPrice = e.price;
                    dataMap.get(e.quoteDate).shiborPeriod = e.period;
                }
            } else {
                let point = {
                    date: e.date,
                    quoteDate: e.quoteDate
                };

                if (e.signCode && e.priceAvg !== undefined && e.priceAvg !== null) point[e.signCode] = e.priceAvg;
                if (e.price || e.period) {
                    point.shiborPrice = e.price;
                    point.shiborPeriod = e.period;
                }

                dataMap.set(e.quoteDate, point);
            }
        };

        if (source.priceTrendsHistory instanceof Array)
            source.priceTrendsHistory.forEach(setMapData);

        if (source.shiborHistory instanceof Array)
            source.shiborHistory.forEach(setMapData);

        return this[convertDataMapToArray](dataMap);
    };

    [loadData]() {

        if (!this.chartData.data instanceof Array) return;

        // console.debug(this.chartData.data.map(e => e.quoteDate));

        // 不连续出现两天的报价在图表上不连续
        // let propObj = {};
        // this.chartData.data.forEach(e => {
        //     angular.merge(propObj, e);
        // });
        // this.chartData.data.forEach(e => {
        //     for (let prop in propObj) {
        //         if (!e.hasOwnProperty(prop)) e[prop] = null;
        //     }
        // });

        // this.chartData.data = this.chartData.data.findWhere(e => e.SSC);

        this.chartData.data.forEach(e => console.debug(`${e.quoteDate} : ${JSON.stringify(e)}`));

        if (this.chartRef) {
            this.chartRef.dataProvider = this.chartData.data;
            this.chartRef.validateData();
        } else {
            dataDefine.chart.baseOption.dataProvider = this.chartData.data;
        }
    };

    [buildChartOption]() {
        var option = angular.copy(dataDefine.chart.baseOption);

        option.graphs = this[buildChartGraphs]();

        option.listeners = this[buildChartListeners]();

        return option;
    };

    [getSpGraphValueField]() {
        if (!this.term || !this.term.code) return dataDefine.termGraphMap.default;

        if (dataDefine.termGraphMap.hasOwnProperty(this.term.code)) {
            return dataDefine.termGraphMap[this.term.code];
        } else {
            return dataDefine.termGraphMap.default;
        }
    };

    [buildChartGraphs]() {

        var spGraphs = this[getSpGraphValueField]();

        return [
            { id: "graph1", lineColor: "#dc4444", balloonText: "直贴价格:[[SSC]]", title: "直贴价格", valueField: "SSC" },
            { id: "graph2", lineColor: "#ffc96c", balloonText: "转帖价格:[[NPC]]", title: "转帖价格", valueField: "NPC" },
            { id: "graph3", lineColor: "#58b96d", balloonText: "SHIBOR:[[shiborPrice]]", title: "SHIBOR", valueField: "shiborPrice" },
            {
                id: "graph4", lineColor: "blue",
                balloonText: `${spGraphs[0]}:[[${spGraphs[0]}]]`, title: spGraphs[0], valueField: spGraphs[0], "hidden": true,
            },
            {
                id: "graph5", lineColor: "purple",
                balloonText: `${spGraphs[1]}:[[${spGraphs[1]}]]`, title: spGraphs[1], valueField: spGraphs[1], "hidden": true,
            },
        ].map(e => {
            return {
                "id": e.id,
                "balloonText": e.balloonText,
                "bullet": "round",
                "title": e.title,
                "bulletSize": 4,
                // "bulletBorderColor": "black",
                "connect": false,
                "lineColor": e.lineColor,
                "lineThickness": 2,
                "hidden": e.hidden || false,
                // "type": "smoothedLine",
                "valueField": e.valueField,
                "bulletBorderAlpha": 1
            }
        });
    };

    [getUpdateTrendsHistoryDto](chart) {
        var dto = {};

        if (chart && chart.chartScrollbar.start >= 0 && chart.chartScrollbar.start < this.chartData.data.length)
            dto.beginDate = this.chartData.data[chart.chartScrollbar.start].date;

        if (chart && chart.chartScrollbar.end >= 0 && chart.chartScrollbar.end < this.chartData.data.length)
            dto.endDate = this.chartData.data[chart.chartScrollbar.end].date;

        if (this.billMedium) dto.billMedium = this.billMedium.code;
        if (this.quotePriceType) dto.quotePriceType = this.quotePriceType.code;

        if (this.term) dto.shiborParameter = this.term.code;

        return dto;
    };

    [buildChartListeners]() {
        return [
            {
                event: "init",
                method: e => {

                    return;

                    // 单击事件直接注册到chartDiv上， 注册到chart对象上无效
                    e.chart.chartDiv.addEventListener("click", type => {
                    });

                    e.chart.chartDiv.addEventListener("mouseup", type => {

                        return;

                        if(!type || !type.target || type.target.nodeName !== 'rect' ) return;

                        var dto = this[getUpdateTrendsHistoryDto](e.chart);

                        this.gridPriceMarginService.updateTrendsHistory(dto).then(res => {

                            if (!res || !res.result || !res.result[0]) return;

                            // var dataMap = new Map(this.chartData.data.map(e => [e.date, e]));

                            // var setMapData = e => {
                            //     e.quoteDate = e.quoteDate || e.date;
                            //     if (typeof e.quoteDate !== 'number') {
                            //         e.quoteDate = e.date;
                            //     }

                            //     if (e.quoteDate === undefined) return;

                            //     if (dataMap.has(e.quoteDate)) {
                            //         if (e.signCode && e.priceAvg !== undefined) dataMap.get(e.quoteDate)[e.signCode] = e.priceAvg;
                            //         if (e.price || e.period) {
                            //             dataMap.get(e.quoteDate).shiborPrice = e.price;
                            //             dataMap.get(e.quoteDate).shiborPeriod = e.period;
                            //         }
                            //     } else {
                            //         console.warn(`updateTrendsHistory received illegal data: ${JSON.stringify(e)} in Date: ${new Date(e.quoteDate).formatDate('MM-dd')}`);
                            //     }
                            // };

                            // if (res.result[0].priceTrendsHistory instanceof Array)
                            //     res.result[0].priceTrendsHistory.forEach(setMapData);

                            // if (res.result[0].shiborHistory instanceof Array)
                            //     res.result[0].shiborHistory.forEach(setMapData);

                            // this.chartData.data = this[convertDataMapToArray](dataMap);

                            this.chartData.data = this[buildDataForChart](res.result[0], new Map(this.chartData.data.map(e => [e.date, e])));

                            this[loadData]();

                        }, res => {
                            console.error('chartPriceHistoryCtrl[buildChartListeners] e.chart.chartDiv.addEventListener("mouseup") error)');
                        });

                    });
                }
            }, {
                event: "changed",
                method: e => { e.chart.lastCursorPosition = e.index; }
            }
        ];

    };

    [initChart]() {
        if (!this.chartRef) {

            var option = this[buildChartOption]();

            this.chartRef = AmCharts.makeChart("chart_price_history", option);
        }
    };

    [convertDataMapToArray](map) {
        var arrayData = [...map.keys()];
        arrayData = arrayData.sort((e1, e2) => e1 - e2);

        var resultArray = [];

        var stack = [];

        arrayData.map(e => {
            var point = map.get(e);
            if (point.quoteDate && typeof point.quoteDate === 'number') {
                point.date = point.quoteDate;
                point.quoteDate = new Date(point.quoteDate).formatDate('MM-dd');
            }
            return point;
        }).forEach(e => {
            if (stack.length === 0) {
                stack.push(e);
            } else {
                if (stack[0].quoteDate === e.quoteDate) {

                    stack[0] = angular.merge(stack[0], e);
                } else {
                    resultArray.push(stack.pop());
                    stack.push(e);
                }
            }
        });

        if (stack.length > 0) resultArray.push(stack.pop());

        return resultArray;
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

            if (event.chartData.currentValue.filterDto && event.chartData.currentValue.filterDto instanceof Array) {
                event.chartData.currentValue.filterDto.forEach((item, index) => {
                    var setItemSource = prop => {
                        if (!this[prop] && item.conditions instanceof Array) {
                            item.conditions.forEach((e, i) => {
                                e.$id = i;
                                e.isDefault = i === 0;
                            });
                            this[prop] = item.conditions;
                        }
                    };

                    if (item.conditionName === "承兑行类别") {
                        this.quotePriceTypeLabel = item.conditionName;
                        setItemSource("quotePriceTypeItemSource");
                    } else if (item.conditionName === "期限") {
                        this.termLabel = item.conditionName;
                        setItemSource("termItemSource");
                    }
                });
            }

            this.chartData.data = this[buildDataForChart](this.chartData);

            this[initChart]();

            this[loadData]();
        }
    };

};

let chartPriceHistory = () => {
    return {
        template: require('./template/chart_price_history.html'),
        bindings: {
            theme: '@mdTheme',
            chartData: '<',

            billMedium: '<'
        },
        controller: ['$mdDialog', 'gridPriceMarginService', chartPriceHistoryCtrl]
    }
};

export default chartPriceHistory;