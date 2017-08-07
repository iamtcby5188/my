/**
 * Created by jiannan.niu on 2016/12/30.
 */
const getHeaderCellTemplate = Symbol('getHeaderCellTemplate');
const getHeaderTemplate = Symbol('getHeaderTemplate');
import showImgCtrl from './../controller/showImgCtrl';

class gridColumnFactoryService {
    constructor (uiGridConstants, httpService, userService, configConsts, $interval, $timeout, gridDataDefineService, componentCommonService) {
        this.uiGridConstants = uiGridConstants;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this.userService = userService;
        this.$interval = $interval;
        this.$timeout = $timeout;
        this.gridDataDefineService = gridDataDefineService;
        this.componentCommonService = componentCommonService;

        this.columnDefs = [
            {//0
                name: '报价方',
                width: 200,
                visible: true,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '报价方',
                sort: null
            },
            {//1
                name: '联系方式',
                width: 100,
                visible: true,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '联系方式',
                sort: null
            },
            {//2
                name: '序号',
                width: 50,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '序号',
                sort: null
            },
            {//3
                name: '方向',
                width: 50,
                visible: true,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '方向',
                sort: null
            },
            {//4
                name: '类别',
                width: 50,
                visible: true,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '类别',
                sort: null
            },
            {//5
                name: '交易地',
                width: 80,
                visible: true,
                isSort: false,
                sortDirection: '',
                // field: 'quoteProvinces.provinceName',
                template: '交易地',
                sort: null
            },
            {//6
                name: '承兑行类别',
                width: 90,
                visible: true,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '承兑行类别',
                sort: null
            },
            {//7
                name: '承兑方类别',
                width: 90,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '承兑方类别',
                sort: null
            },
            {//8
                name: '承兑方全称',
                width: 150,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '承兑方全称',
                sort: null
            },
            {//9
                name: '到期日',
                width: 100,
                visible: true,
                isSort: true,
                sortDirection: 'DA',
                headerCellTemplate: this[getHeaderCellTemplate](),
                field: '',
                template: '到期日',
                sort: null
            },
            {//10
                name: '剩余天数',
                width: 100,
                visible: true,
                isSort: true,
                sortDirection: 'DA',
                headerCellTemplate: this[getHeaderCellTemplate](),
                field: '',
                template: '天数',
                sort: null
            },
            {//11
                name: '利率',
                displayName: '利率(%)',
                width: 100,
                visible: true,
                isSort: true,
                sortDirection: 'DA',
                headerCellTemplate: this[getHeaderCellTemplate](),
                field: '',
                template: '利率(%)',
                sort: null
            },
            {//12
                name: '金额',
                displayName: '金额(万)',
                width: 100,
                visible: true,
                isSort: true,
                sortDirection: 'DA',
                headerCellTemplate: this[getHeaderCellTemplate](),
                // field: 'amount',
                template: '金额(万)',
                sort: null
            },
            {//13
                name: '票面',
                width: 60,
                visible: true,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '票面',
                sort: null
            },
            {//14
                name: '备注',
                width: '*',
                minWidth: 100,
                visible: true,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '备注',
                sort: null
            },
            {//15
                name: '时间',
                displayName: '时间',
                width: 150,
                visible: true,
                isSort: true,
                sortDirection: 'D',
                headerCellClass: 'last-update-time',
                headerCellTemplate: this[getHeaderCellTemplate](),
                field: '',
                template: '时间',
                sort: {direction: uiGridConstants.DESC, priority: 1}
            },
            {//16
                name: '交易对手',
                width: '*',
                minWidth: 100,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: 'memo',
                template: '备注',
                sort: null
            },
            {//17
                name: '清单',
                width: 60,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '清单',
                sort: null
            },
            {//18
                name: '询价中操作',
                displayName: '操作',
                width: 80,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '询价中操作',
                sort: null
            },
            {//19
                name: '已成交操作',
                displayName: '操作',
                width: 60,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '已成交操作',
                sort: null
            },
            {//20
                name: '已撤销操作',
                displayName: '操作',
                width: 60,
                visible: false,
                isSort: false,
                sortDirection: '',
                field: '',
                template: '已撤销操作',
                sort: null
            }
        ];
        this.templateDefine = new Map([
            ['报价方', '<div class="ui-grid-cell-contents"><span>{{ !row.entity.containsAdditionalInfo ? (row.entity.quoteCompanyDto.name +"-"+ row.entity.contactDto.name) : (row.entity.additionalInfo.quoteCompanyName + "-" + row.entity.additionalInfo.contactName) }}<md-tooltip class="tooltip" md-direction="right">{{ !row.entity.containsAdditionalInfo ? (row.entity.quoteCompanyDto.name +"-"+ row.entity.contactDto.name) : (row.entity.additionalInfo.quoteCompanyName + "-" + row.entity.additionalInfo.contactName) }}</md-tooltip></span></div>'],
            ['联系方式', '<div class="ui-grid-cell-contents"><i class="ss-icon ss-icon-qm-entry-normal pointer" ng-class="{\'gray\': !row.entity.contactDto.id}" ng-click="grid.appScope.openQM($event, row.entity, grid.appScope)"></i><md-menu md-offset="0 20"><i md-menu-origin class="mif-mobile pointer mobile" ng-class="{\'gray\': !row.entity.contactDto.mobileTel && !row.entity.additionalInfo.contactTelephone}" ng-click="grid.appScope.openMenu($mdOpenMenu, $event, row.entity)"></i><md-menu-content><md-menu-item class="grid-mobile-item" ng-repeat="item in grid.appScope.getMobileTel(row.entity)">{{ item }}</md-menu-item></md-menu-content></md-menu><img class="pointer" ng-class="{\'gray\': !row.entity.contactDto.qq}" src="img/qqlogo.png" ng-click="grid.appScope.copyQQ($event, row.entity, grid.appScope)"></div>'],
            ['序号', '<div class="ui-grid-cell-contents">{{ row.entity.index }}</div>'],
            ['方向', '<div class="ui-grid-cell-contents"><div class="direction" ng-class="{\'in\':row.entity.direction === \'IN\',\'out\':row.entity.direction === \'OUT\'}">{{ row.entity.direction ? grid.appScope.gridDataDefineService.quotString.dir[row.entity.direction] : "" }}</div></div>'],
            ['类别', '<div class="ui-grid-cell-contents">{{ (row.entity.billMedium&&row.entity.billType) ? ((row.entity.billMedium === "ELE"?"电":"纸") + (row.entity.billType === "CMB"?"商":"银")) : "" }}</div>'],
            ['承兑行类别', '<div class="ui-grid-cell-contents">{{ row.entity.quotePriceType ? grid.appScope.gridDataDefineService.BABQuotePriceType[row.entity.quotePriceType] : "" }}</div>'],
            ['承兑方类别', '<div class="ui-grid-cell-contents">{{ row.entity.containsAdditionalInfo ? grid.appScope.gridDataDefineService.BABQuotePriceType[row.entity.additionalInfo.companyType] : grid.appScope.gridDataDefineService.BABQuotePriceType[row.entity.acceptingHouse.companyType] || "" }}</div>'],
            ['承兑方全称', '<div class="ui-grid-cell-contents">{{ row.entity.containsAdditionalInfo ? row.entity.additionalInfo.acceptingHouseName : row.entity.acceptingHouse.companyName || ""}}</div>'],
            ['到期日', '<div class="ui-grid-cell-contents">{{ row.entity.dueDate || "不限" | date:"yyyy/MM/dd"}}</div>'],
            ['天数', '<div class="ui-grid-cell-contents" ng-bind="grid.appScope.getRemainingDays(row.entity.dueDate)"></div>'],
            ['利率(%)', '<div class="ui-grid-cell-contents">{{ row.entity.price > 0? (row.entity.price):"电议" }}</div>'],
            ['票面', '<div class="ui-grid-cell-contents" ng-if="row.entity.containsImg" ng-click="grid.appScope.showBill(row.entity,grid.appScope)"><a class="pointer">查看</a></div>'],
            ['清单', '<div class="ui-grid-cell-contents"><a href="">清单</a></div>'],
            ['交易地', '<div class="ui-grid-cell-contents">{{ row.entity.quoteProvinces.provinceName || "不限"}}</div>'],
            ['金额(万)', '<div class="ui-grid-cell-contents">{{ row.entity.amount ? (row.entity.amount/10000 | numberFilter:6) : "不限" }}</div>'],
            ['询价中操作', '<div class="ui-grid-cell-contents" ng-if="!row.entity.readOnly"><i ng-if="false" class="ss-icon ss-icon-finish-edit-normal ss-icon-highlight-bg pointer" tag="publish"></i> <i class="ss-icon ss-icon-edit-normal ss-icon-highlight-bg pointer" tag="edit"><md-tooltip class="tooltip" md-direction="right">编辑</md-tooltip></i> <i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" tag="del"><md-tooltip class="tooltip" md-direction="right">撤销</md-tooltip></i></div>'],
            ['已成交操作', '<div class="ui-grid-cell-contents" ng-if="!row.entity.readOnly"><i ng-if="false" class="ss-icon ss-icon-edit-normal ss-icon-highlight-bg pointer" tag="edit"></i> <i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" tag="del"><md-tooltip class="tooltip" md-direction="right">删除</md-tooltip></i></div>'],
            ['已撤销操作', '<div class="ui-grid-cell-contents" ng-if="!row.entity.readOnly"><i ng-if="false" class="ss-icon ss-icon-backward-normal ss-icon-highlight-bg pointer" tag="publish"></i> <i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" tag="del"><md-tooltip class="tooltip" md-direction="right">删除</md-tooltip></i></div>'],
            ['备注', '<div class="ui-grid-cell-contents">{{ row.entity.memo }}<md-tooltip class="tooltip" md-direction="left" ng-if="row.entity.memo">{{ row.entity.memo }}</md-tooltip></div>'],
            ['时间', '<div class="ui-grid-cell-contents" ng-if="!grid.appScope.getLastUpdateTime(row.entity.lastUpdateDate)">{{ row.entity.lastUpdateDate | date:"yyyy/MM/dd HH:mm:ss"}}</div><div class="ui-grid-cell-contents" ng-if="grid.appScope.getLastUpdateTime(row.entity.lastUpdateDate)">{{ row.entity.lastUpdateDate | date:"HH:mm:ss"}}</div>']
        ]);

        this.bShowBill = false;

        this.spin = new Spinner({ color: '#fff', lines: 12, top: '30%' });
    }

    viewBusy(bBusy){
            if(bBusy){
                this.spin.spin(document.getElementById("gridViewBusy"));
            }else{
                this.spin.stop();
            }   
    }

    //计算最后更新时间
    getLastUpdateTime (time) {
        if (!time) return;
        let now = new Date();
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setMilliseconds(0);

        return parseInt(time) > now.getTime();
    }

    //计算剩余天数
    getRemainingDays (time) {
        if (!time) return;
        let date = new Date(time);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        let now = new Date();
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setMilliseconds(0);
        let d = parseInt((date - now) / (24 * 60 * 60 * 1000) + 1);
        d = d < 0 ? '' : d;
        return d;
    }

    //点击复制QQ号
    copyQQ (event, quote, scope) {
        if (!quote || !quote.contactDto || !quote.contactDto.qq) return;
        document.oncopy = function (e) {
            e.clipboardData.setData('text/plain', quote.contactDto.qq);
            e.preventDefault();
        };
        document.execCommand("copy");
        scope.copyqqsuccess = true;

        var qqcopy = document.getElementById('qqcopy');
        qqcopy.style.left = +event.clientX + 100 + 'px';
        qqcopy.style.top = +event.clientY - 68 + 'px';

        scope.qqnumber = quote.contactDto.qq;

        if (scope.qqtimer) scope.$timeout.cancel(scope.qqtimer);
        scope.qqtimer = scope.$timeout(function () {
            scope.copyqqsuccess = false;
        }, 1000);
    }

    //点击打开QM
    openQM (event, quote, scope) {
        if (!quote || !quote.contactDto || !quote.contactDto.id) return;
        scope.qbService.openQM(quote.contactDto.id);
    }

    //查看票面
    showBill (quote, scope) {
        if (!quote || this.bShowBill) return;
        this.bShowBill = true;
        let headers = scope.gridColumnFactoryService.getAuthHeaders();
        var componentCommonService = scope.gridColumnFactoryService.componentCommonService;
        scope.gridColumnFactoryService.httpService.postService('/bab_quote/quoteQuery/getSSRImage', quote.id, headers)
            .then((res) => {
                if (res && res.result && angular.isArray(res.result) && res.result.length > 0) {
                    var base64Img = res.result[0].base64Img;
                    this.bShowBill = false;
                    componentCommonService.openShowImg(showImgCtrl, {
                        theme: "",
                        title: "查看原图",
                        thumb: res.result[0].base64Img
                    });
                }
            });
    }

    //点击联系人电话
    getMobileTel (quote) {
        if (!quote) return;
        let tel = quote.containsAdditionalInfo ? quote.additionalInfo.contactTelephone : quote.contactDto.mobileTel;
        if (!tel) return;
        let mobile = tel.split(',');
        mobile.forEach(function (item, index, array) {
            if (item.indexOf('-') === -1) {
                try {
                    item = item.substring(0, 3) + '-' + item.substring(3, 7) + '-' + item.substring(7);
                    array[index] = item;
                } catch (e) {
                    console.log(e);
                }
            }
        });
        return mobile;
    }

    //重置grid大小，自动调整grid高度到底部
    resizeGrid (chart) {
        this.$interval(function () {
            var winHeight = window.innerHeight;
            if (!$('.ui-grid').offset()) return;
            var gridOffsetTop = $('.ui-grid').offset().top;
            if (chart) {
                let chartHeight = $(chart+'>div').height();
                $('.ui-grid').height(winHeight - gridOffsetTop - 5 - chartHeight);
            } else {
                $('.ui-grid').height(winHeight - gridOffsetTop - 5);
            }
            // console.log(winHeight, gridOffsetTop);
        }, 10, 30);
    }

    //获取登录验证的token和userid
    getAuthHeaders () {
        return this.userService.getAuthHeaders();
    };

    //请求grid数据
    gridInit (scope, url, param) {
        this.viewBusy(true);
        let headers = this.getAuthHeaders();
        this.httpService.postService(url, param, headers)
            .then((res) => {
                if (res.result.length !== 0) {
                    res.result.forEach(function (item, index) {
                        item.index = index + 1;
                    });
                }
                this.viewBusy(false);
                scope.gridOptions.data = res.result;
                // console.log(res.result);
            },(res)=>{
                this.viewBusy(false);
            });
    }

    //无限滚动加载数据
    getDataDown (scope, url, param) {
        let headers = this.getAuthHeaders();
        let data = this.httpService.postService(url, param, headers)
            .then(function (res) {
                let index = scope.gridOptions.data[scope.gridOptions.data.length - 1].index;
                res.result.forEach(function (item) {
                    item.index = index++;
                });
                let data = res.result;
                // console.log(res);
                scope.gridApi.infiniteScroll.saveScrollPercentage();

                scope.gridOptions.data = scope.gridOptions.data.concat(data);

                return scope.gridApi.infiniteScroll.dataLoaded(false, true);
            });
        return data;
    }

    columnFactory (define, templateDefine) {
        const uiGridConstants = this.uiGridConstants;
        const sortDefdine = new Map([
            ['', []],
            ['D', [uiGridConstants.DESC]],
            ['A', [uiGridConstants.ASC]],
            ['DA', [uiGridConstants.DESC, uiGridConstants.ASC]],
            ['AD', [uiGridConstants.ASC, uiGridConstants.DESC]],
            ['nDA', [null, uiGridConstants.DESC, uiGridConstants.ASC]],
            ['nAD', [null, uiGridConstants.ASC, uiGridConstants.DESC]]
        ]);

        return {
            name: define.name,
            displayName: define.displayName,
            width: define.width || 100,
            minWidth: define.minWidth || 50,
            maxWidth: define.maxWidth,
            visible: define.visible,
            enableSorting: define.isSort,
            sortDirectionCycle: sortDefdine.get(define.sortDirection),
            headerCellTemplate: define.headerCellTemplate,
            field: define.field,
            sort: define.sort,
            headerCellClass: define.headerCellClass,
            cellTemplate: templateDefine.get(define.template),
            enableCellEdit: define.enableCellEdit,
            type: define.type,
            enableCellEdit: define.enableCellEdit,
            editModelField: define.editModelField,
            editableCellTemplate: define.editableCellTemplate,
            editDropdownOptionsArray: define.editDropdownOptionsArray,
            editDropdownOptionsFunction: define.editDropdownOptionsFunction,
            editDropdownIdLabel: define.editDropdownIdLabel,
            editDropdownValueLabel: define.editDropdownValueLabel,
            pinnedRight: define.pinnedRight,
            pinnedLeft: define.pinnedLeft,
            enablePinning: define.enablePinning

        };
    }

    buildColumn (columnDefs, templateDefine) {
        return columnDefs.map(e => this.columnFactory(e, templateDefine));
    }

    buildGridOptions (scope, columnDefs, templateDefine) {
        const uiGridConstants = this.uiGridConstants;
        let options = {
            //行高
            rowHeight: 30,
            //是否显示横向滚动条，只有两个值，默认uiGridConstants.scrollbars.ALWAYS
            enableHorizontalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            enableVerticalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            //是否开启列菜单，默认true
            enableColumnMenus: false,
            //是否可以过滤，默认false
            enableFiltering: false,
            //是否允许排序，默认true
            enableSorting: true,
            //是否显示grid底边
            // showGridFooter: false,
            //grid底边的高度
            // columnFooterHeight: 50,
            //grid底边的模板
            // gridFooterTemplate: '',
            //是否允许表头全选
            // enableRowHeaderSelection: false,
            //是否允许选取整行
            // enableFullRowSelection: true,


            //是否允许调整列宽度
            enableColumnResizing: true,
            //是否允许调整列的顺序
            enableColumnMoving: true,
            //是否无限滚动，默认true
            enableInfiniteScroll: true,
            //滚动到距离底部10条时加载数据，默认20
            infiniteScrollRowsFromEnd: 20,
            //是否允许向下无限滚动，默认true
            infiniteScrollDown: true,
            //是否允许向上无限滚动，默认true
            infiniteScrollUp: false,

            //是否保存这些数据，默认都是true
            saveWidths: true,
            saveVisible: true,
            saveOrder: true,
            saveScroll: false,
            saveFocus: false,
            saveSort: false,
            saveFilter: false,
            savePinning: true,
            saveGrouping: false,
            saveGroupingExpandedStates: false,
            saveTreeView: false,
            saveSelection: false,

            // headerTemplate: this[getHeaderTemplate](),

            //ui-grid回调函数注册
            onRegisterApi: (gridApi) => {
                scope.gridApi = gridApi;
                //注册滚动条滚动到最下方时加载数据的函数
                gridApi.infiniteScroll.on.needLoadMoreData(scope, scope.getDataDown);

                // gridApi.core.on.gridDimensionChanged(scope, (gridApi) => {
                //     if (!localStorage.getItem(scope.type + 'BackState')) {
                //         scope.saveState(scope.type + 'BackState');
                //         localStorage.setItem(scope.type + 'BackState', JSON.stringify(scope[scope.type + 'BackState']));
                //     }
                //
                //     if (!localStorage.getItem(scope.type + 'State')) {
                //         scope[scope.type + 'BackState'] = JSON.parse(localStorage.getItem(scope.type + 'BackState'));
                //         scope.restoreState(scope.type + 'BackState');
                //         scope.columnDefs = scope[scope.type + 'BackState'].columns;
                //     } else {
                //         scope[scope.type + 'State'] = JSON.parse(localStorage.getItem(scope.type + 'State'));
                //         scope.restoreState(scope.type + 'State');
                //         scope.columnDefs = scope[scope.type + 'State'].columns;
                //     }
                // });

                // gridApi.colMovable.on.columnPositionChanged(scope, (colDef, originalPosition, newPosition) => {
                //     scope.saveState(scope.type + 'State');
                //     localStorage.setItem(scope.type + 'State', JSON.stringify(scope[scope.type + 'State']));
                // });
                //
                // gridApi.colResizable.on.columnSizeChanged(scope, (colDef, deltaChange) => {
                //     scope.saveState(scope.type + 'State');
                //     localStorage.setItem(scope.type + 'State', JSON.stringify(scope[scope.type + 'State']));
                // });
            },
            rowTemplate: '<div ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" ng-class="{warning:row.entity.isNew}" class="ui-grid-cell" ui-grid-cell></div>',
            columnDefs: this.buildColumn(columnDefs, templateDefine)
        };
        return options;
    }


    buildGridOptionsNoScroll (scope, columnDefs, templateDefine) {
        const uiGridConstants = this.uiGridConstants;
        let options = {
            //行高
            rowHeight: 30,
            //是否显示横向滚动条，只有两个值，默认uiGridConstants.scrollbars.ALWAYS
            enableHorizontalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            enableVerticalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            //是否开启列菜单，默认true
            enableColumnMenus: false,
            //是否显示过滤菜单，默认false
            enableFiltering: false,
            //是否允许排序，默认true
            enableSorting: false,
            enableColumnResizing: true,

            //是否显示grid底边
            // showGridFooter: false,
            //grid底边的高度
            // columnFooterHeight: 50,
            //grid底边的模板
            // gridFooterTemplate: '',
            //是否允许表头全选
            // enableRowHeaderSelection: false,
            //是否允许选取整行
            // enableFullRowSelection: true,

            //是否无限滚动，默认true
            enableInfiniteScroll: false,
            //滚动到距离底部10条时加载数据，默认20
            infiniteScrollRowsFromEnd: 20,
            //是否允许向下无限滚动，默认true
            infiniteScrollDown: false,
            //是否允许向上无限滚动，默认true
            infiniteScrollUp: false,

            //ui-grid回调函数注册
            onRegisterApi: function (gridApi) {
                scope.gridApi = gridApi;
            },
            rowTemplate: '<div ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" ng-class="{warning:row.entity.isNew}" class="ui-grid-cell" ui-grid-cell></div>',
            columnDefs: this.buildColumn(columnDefs, templateDefine)
        };

        options.columnDefs.forEach(e=>{
            e.enableSorting = false;
        });
        return options;
    }

    convertQuotString (item) {
        var str = "";
        if (item) {
            if (item.direction && (item.direction === 'IN' || item.direction === 'OUT')) str += (this.gridDataDefineService.quotString.dir[item.direction] + "  ");
            if (item.billMedium) str += (this.gridDataDefineService.quotString.billMedium[item.billMedium])
            if (item.billType) str += (this.gridDataDefineService.quotString.billType[item.billType] + "  ");
            if (item.quotePriceType) str += (this.gridDataDefineService.quotString.priceType[item.quotePriceType] + "  ");
            if (item.dueDate) str += (this.getRemainingDays(item.dueDate) + "天  ");
            if (item.amount) {
                str += ((item.amount / 10000) + "W  ");
            } else {
                str += ("不限   ");
            }

            if (item.price) {
                str += (item.price.toString() + "%  ");
            } else {
                str += "电议  ";
            }

        }

        return str;
    }

    saveState (scope, state) {
        scope[state] = scope.gridApi.saveState.save();
    }


    restoreState (scope, state) {
        scope.gridApi.saveState.restore(scope, scope[state]);
    }


    [getHeaderCellTemplate] () {
        return require('../common/component/template/header_cell_template.html');
    }

    [getHeaderTemplate] () {
        return require('../common/component/template/grid-header.html');
    }
}
export default ['uiGridConstants', 'httpService', 'userService', 'configConsts', '$interval', '$timeout', 'gridDataDefineService', 'componentCommonService', gridColumnFactoryService];
