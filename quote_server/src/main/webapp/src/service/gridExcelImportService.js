/**
 * Created by jiannan.niu on 2017/2/10.
 */
import gridExcelImportCtrl from './../controller/gridExcelImportCtrl';
import gridColumnPanelCtrl from './../controller/gridColumnPanelCtrl';
const init = Symbol('init');
const getExcelImportPanelTemplate = Symbol('getExcelImportPanelTemplate');
const getGridColumnPanelTemplate = Symbol('getGridColumnPanelTemplate');
const THEME_NAME = "ssAvalonUi";
const directionMap = new Map([
    ['IN', '收'],
    ['OUT', '出']
]);

class gridExcelImportService {

    constructor (uiGridConstants, $mdPanel, httpService, userService, configConsts, gridColumnFactoryService) {
        this.uiGridConstants = uiGridConstants;
        this.httpService = httpService;
        this.userService = userService;
        this.configConsts = configConsts;
        this.$mdPanel = $mdPanel;
        this.gridColumnFactoryService = gridColumnFactoryService;

        this.columnDefs = [
            {
                name: '序号',
                width: 50,
                visible: true,
                SSR: true,
                SSC: true,
                NPC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 0,
                enableCellEdit: false,
                template: '序号'
            },
            {
                name: '报价方',
                width: 200,
                visible: true,
                SSR: true,
                SSC: true,
                NPC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 1,
                template: '报价方',
                editModelField: 'additionalInfo.quoteCompanyName',
            },
            {
                name: '联系人',
                width: 80,
                visible: true,
                SSR: true,
                SSC: true,
                NPC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 2,
                template: '联系人',
                editModelField: 'additionalInfo.contactName',
            },
            {
                name: '联系方式',
                width: 120,
                visible: true,
                SSR: true,
                SSC: true,
                NPC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 3,
                template: '联系方式',
                editModelField: 'additionalInfo.contactTelephone',
            },
            {
                name: '方向',
                width: 50,
                visible: true,
                SSR: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 4,
                template: '方向',
                editModelField: 'direction',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: () => {
                    return this.paramEnum.findItem(e => e.conditionName === '报价方向').conditions;
                }
            },
            {
                name: '票类',
                width: 50,
                visible: true,
                SSR: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 5,
                template: '票类',
                editModelField: 'billMedium',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: (rowEntity, colDef) => {
                    let conditions = angular.copy(this.paramEnum.findItem(e => e.conditionName === '票据类型').conditions);
                    let type = conditions.findWhere(e => {
                        return e.code.indexOf(rowEntity.billType) !== -1;
                    })
                    type.forEach(e => {
                        e.code = e.code.substring(0, 3);
                    });
                    return type;
                }
            },
            {
                name: '票类',
                width: 50,
                visible: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 4,
                template: '票类(转贴)',
                editModelField: 'billMedium',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: (rowEntity, colDef) => {
                    let conditions = angular.copy(this.paramEnum.findItem(e => e.conditionName === '票据类型').conditions);
                    return conditions;
                }
            },
            {
                name: '票类',
                width: 70,
                visible: true,
                SSC: true,
                BKB: true,
                SORT_NUMBER: 12,
                template: '票类(直贴价格)',
                editModelField: 'mediumType',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: (rowEntity, colDef) => {
                    let conditions = angular.copy(this.paramEnum.findItem(e => e.conditionName === '票据类型').conditions);
                    let type = conditions.findWhere(e => {
                        return e.code.indexOf(rowEntity.billType) !== -1;
                    });
                    return type;
                }
            },
            {
                name: '模式',
                width: 50,
                visible: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 5,
                template: '模式',
                editModelField: 'tradeType',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: (rowEntity, colDef) => {
                    let conditions = angular.copy(this.paramEnum.findItem(e => e.conditionName === '交易模式').conditions);
                    return conditions;
                }
            },
            {
                name: '承兑行',
                width: 70,
                visible: true,
                template: '承兑行',
                SSR: true,
                BKB: true,
                SORT_NUMBER: 6,
                editModelField: 'quotePriceType',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: () => {
                    return this.paramEnum.findItem(e => e.conditionName === '承兑行类别').conditions;
                }
            },
            {
                name: '承兑方全称',
                width: 200,
                visible: true,
                template: '承兑方全称',
                SSR: true,
                CMB: true,
                SORT_NUMBER: 6,
                editModelField: 'additionalInfo.acceptingHouseName'
            },
            {
                name: '承兑方类别',
                width: 100,
                visible: true,
                template: '承兑方类别',
                SSR: true,
                CMB: true,
                SORT_NUMBER: 7,
                editModelField: 'additionalInfo.companyType',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: () => {
                    return this.paramEnum.findItem(e => e.conditionName === '承兑方类别').conditions;
                }
            },
            {
                name: '票面金额',
                displayName: '票面金额(元)',
                width: 150,
                visible: true,
                SSR: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 8,
                template: '票面金额(元)',
                editModelField: 'amount',
                type: 'number'
            },
            {
                name: '到期日期',
                width: 150,
                visible: true,
                SSR: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 9,
                template: '到期日期',
                editModelField: 'dueDate',
                // type: 'date'
                editableCellTemplate: '<md-datepicker ng-model="row.entity.dueDate" md-hide-icons="calendar"></md-datepicker>'
            },
            {
                name: '发价日期',
                width: 150,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 13,
                template: '发价日期',
                enableCellEdit: false,
                // editModelField: 'effectiveDate',
                // editableCellTemplate: '<md-datepicker ng-model="row.entity.effectiveDate" md-hide-icons="calendar"></md-datepicker>'
            },
            {
                name: '期望价格',
                displayName: '期望价格(%)',
                width: 120,
                visible: true,
                SSR: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 10,
                template: '期望价格(%)',
                editModelField: 'price'
            },
            {
                name: '国股',
                displayName: '国股(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 14,
                template: '国股(%)',
                editModelField: 'ggPrice'
            },
            {
                name: '城商',
                displayName: '城商(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 15,
                template: '城商(%)',
                editModelField: 'csPrice'
            },
            {
                name: '农商',
                displayName: '农商(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 16,
                template: '农商(%)',
                editModelField: 'nsPrice'
            },
            {
                name: '农信',
                displayName: '农信(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 17,
                template: '农信(%)',
                editModelField: 'nxPrice'
            },
            {
                name: '农合',
                displayName: '农合(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 18,
                template: '农合(%)',
                editModelField: 'nhPrice'
            },
            {
                name: '外资',
                displayName: '外资(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 19,
                template: '外资(%)',
                editModelField: 'wzPrice'
            },
            {
                name: '村镇',
                displayName: '村镇(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 20,
                template: '村镇(%)',
                editModelField: 'czPrice'
            },
            {
                name: '财务公司',
                displayName: '财务公司(%)',
                width: 120,
                visible: true,
                SSC: true,
                NPC: true,
                BKB: true,
                SORT_NUMBER: 21,
                template: '财务公司(%)',
                editModelField: 'cwPrice'
            },
            {
                name: '有保函',
                displayName: '有保函(%)',
                width: 120,
                visible: true,
                SSC: true,
                CMB: true,
                SORT_NUMBER: 22,
                template: '有保函(%)',
                editModelField: 'ybhPrice'
            },
            {
                name: '无保函',
                displayName: '无保函(%)',
                width: 120,
                visible: true,
                SSC: true,
                CMB: true,
                SORT_NUMBER: 23,
                template: '无保函(%)',
                editModelField: 'wbhPrice'
            },
            {
                name: '交易地点',
                width: 100,
                visible: true,
                SSR: true,
                SSC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 11,
                template: '交易地点',
                editModelField: 'quoteProvinces.provinceCode',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editDropdownOptionsFunction: () => {
                    return this.paramEnum.findItem(e => e.conditionName === '交易地点').conditions;
                }
            },
            {
                name: '备注',
                width: '*',
                minWidth: 500,
                visible: true,
                SSR: true,
                SSC: true,
                NPC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 100,
                template: '备注',
                editModelField: 'memo'
            },
            {
                name: '操作',
                width: 50,
                minWidth: 50,
                visible: true,
                SSR: true,
                SSC: true,
                NPC: true,
                BKB: true,
                CMB: true,
                SORT_NUMBER: 1000,
                template: '操作',
                enableCellEdit: false,
                pinnedRight: true
            },
        ];

        this.templateDefine = new Map([
            ['序号', '<div class="ui-grid-cell-contents text-center">{{ row.entity.index }}</div>'],
            ['报价方', '<div class="ui-grid-cell-contents">{{ row.entity.additionalInfo.quoteCompanyName }}</div>'],
            ['联系人', '<div class="ui-grid-cell-contents">{{ row.entity.additionalInfo.contactName }}</div>'],
            ['联系方式', '<div class="ui-grid-cell-contents">{{ row.entity.additionalInfo.contactTelephone }}</div>'],
            ['方向', '<div class="ui-grid-cell-contents">{{ row.entity.direction ? grid.appScope.$ctrl.gridExcelImportService.paramDirection[row.entity.direction] : "未知" }}</div>'],
            ['票类', '<div class="ui-grid-cell-contents">{{ (row.entity.billMedium && row.entity.billType) ? grid.appScope.$ctrl.gridExcelImportService.paramBillType[row.entity.billMedium + "_" + row.entity.billType] : "未知" }}</div>'],
            ['票类(转贴)', '<div class="ui-grid-cell-contents">{{ row.entity.billMedium ? grid.appScope.$ctrl.gridExcelImportService.paramBillType[row.entity.billMedium] : "未知" }}</div>'],
            ['票类(直贴价格)', '<div class="ui-grid-cell-contents">{{ row.entity.billType ? grid.appScope.$ctrl.gridExcelImportService.paramBillType[(row.entity.minor ? "MINOR_" : "") + (row.entity.billMedium ? (row.entity.billMedium + "_") : "") + row.entity.billType] : "未知" }}</div>'],
            ['模式', '<div class="ui-grid-cell-contents">{{ row.entity.tradeType ? grid.appScope.$ctrl.gridExcelImportService.paramTradeType[row.entity.tradeType] : "未知" }}</div>'],
            ['票面金额(元)', '<div class="ui-grid-cell-contents">{{  row.entity.amount | numberFilter: 2}}</div>'],
            ['承兑行', '<div class="ui-grid-cell-contents">{{  row.entity.quotePriceType ? grid.appScope.$ctrl.gridExcelImportService.paramPriceType[row.entity.quotePriceType] : "未知" }}</div>'],
            ['承兑方类别', '<div class="ui-grid-cell-contents">{{ row.entity.additionalInfo.companyType ? grid.appScope.$ctrl.gridExcelImportService.paramCompanyType[row.entity.additionalInfo.companyType] : "未知" }}</div>'],
            ['承兑方全称', '<div class="ui-grid-cell-contents">{{ row.entity.additionalInfo.acceptingHouseName }}</div>'],
            ['到期日期', '<div class="ui-grid-cell-contents">{{ row.entity.dueDate | date:"yyyy-MM-dd" }}</div>'],
            ['发价日期', '<div class="ui-grid-cell-contents">{{ row.entity.effectiveDate | date:"yyyy-MM-dd" }}</div>'],
            ['期望价格(%)', '<div class="ui-grid-cell-contents">{{ row.entity.price | number: 3}}</div>'],
            ['国股(%)', '<div class="ui-grid-cell-contents">{{ row.entity.ggPrice | number: 3}}</div>'],
            ['城商(%)', '<div class="ui-grid-cell-contents">{{ row.entity.csPrice | number: 3}}</div>'],
            ['农商(%)', '<div class="ui-grid-cell-contents">{{ row.entity.nsPrice | number: 3}}</div>'],
            ['农信(%)', '<div class="ui-grid-cell-contents">{{ row.entity.nxPrice | number: 3}}</div>'],
            ['农合(%)', '<div class="ui-grid-cell-contents">{{ row.entity.nhPrice | number: 3}}</div>'],
            ['村镇(%)', '<div class="ui-grid-cell-contents">{{ row.entity.czPrice | number: 3}}</div>'],
            ['外资(%)', '<div class="ui-grid-cell-contents">{{ row.entity.wzPrice | number: 3}}</div>'],
            ['财务公司(%)', '<div class="ui-grid-cell-contents">{{ row.entity.cwPrice | number: 3}}</div>'],
            ['有保函(%)', '<div class="ui-grid-cell-contents">{{ row.entity.ybhPrice | number: 3}}</div>'],
            ['无保函(%)', '<div class="ui-grid-cell-contents">{{ row.entity.wbhPrice | number: 3}}</div>'],
            ['交易地点', '<div class="ui-grid-cell-contents">{{ row.entity.quoteProvinces.provinceName || "不限"}}</div>'],
            ['备注', '<div class="ui-grid-cell-contents">{{ row.entity.memo }}</div>'],
            ['操作', '<div class="ui-grid-cell-contents"><i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" ng-click="grid.appScope.$ctrl.deleteRow($event, grid.appScope, row.entity.index)"><md-tooltip class="tooltip" md-direction="right">删除</md-tooltip></i></div>']
        ])

        this[init]();
    }

    openImportDialog (controller, params, initData) {

        if (initData.parameterList && angular.isArray(initData.parameterList) && initData.parameterList.length !== 0) {
            this.paramEnum = this.getParamEnum(initData.parameterList);

            if (params.type === 'ssr') {
                this.paramDirection = this.creatEnum('报价方向', this.paramEnum);
                this.paramBillType = this.creatEnum('票据类型', this.paramEnum);
                this.paramPriceType = this.creatEnum('承兑行类别', this.paramEnum);
                this.paramCompanyType = this.creatEnum('承兑方类别', this.paramEnum);
            } else if (params.type === 'ssc') {
                this.paramBillType = this.creatEnum('票据类型', this.paramEnum);
            } else if (params.type === 'npc') {
                this.paramTradeType = this.creatEnum('交易模式', this.paramEnum);
                this.paramBillType = this.creatEnum('票据类型', this.paramEnum);
            }


        }

        var position = this.$mdPanel.newPanelPosition()
            .absolute()
            .center();

        var panelAnimation = this.$mdPanel.newPanelAnimation()
            .withAnimation(this.$mdPanel.animation.SCALE)
            .openFrom('.import')
            .closeTo('.import');

        var config = {
            attachTo: angular.element(document.body),
            controller: gridExcelImportCtrl,
            controllerAs: '$ctrl',
            bindToController: true,
            panelClass: 'import-panel',
            theme: params.theme || THEME_NAME,
            locals: {
                title: params.title || '批量导入',
                image: params.image || undefined,
                textContent: params.message || "",
                quotContent: params.quotContent || "",
                okText: params.okText || "确认",
                cancelText: params.cancelText || "取消",
                onOkCallback: params.okCallback || (() => true),
                onCancelCallback: params.cancelCallback || (() => true),
                type: params.type
            },
            template: this[getExcelImportPanelTemplate](),
            position: position,
            // animation: panelAnimation,
            hasBackdrop: true,
            trapFocus: true,
            fullscreen: false,
            disableParentScroll: false,
            clickOutsideToClose: false
        };

        return this.$mdPanel.open(config);

    }

    openGridColumnPanel (event, params) {
        var position = this.$mdPanel.newPanelPosition()
            .absolute()
            .center();

        var config = {
            attachTo: angular.element(document.body),
            controller: gridColumnPanelCtrl,
            controllerAs: '$ctrl',
            bindToController: true,
            panelClass: 'column-panel',
            theme: params.theme || THEME_NAME,
            locals: {
                title: params.title || '自定义列',
                image: params.image || undefined,
                textContent: params.message || "",
                quotContent: params.quotContent || "",
                okText: params.okText || "确认",
                cancelText: params.cancelText || "取消",
                onOkCallback: params.okCallback || (() => true),
                onCancelCallback: params.cancelCallback || (() => true),
                scope: params.scope,
                columnDefs: params.columnDefs
            },
            template: this[getGridColumnPanelTemplate](),
            position: position,
            // animation: panelAnimation,
            hasBackdrop: true,
            trapFocus: true,
            fullscreen: false,
            disableParentScroll: false,
            clickOutsideToClose: false
        };

        return this.$mdPanel.open(config);
    }

    getParamEnum (param) {
        if (!param || !angular.isArray(param) || param.length === 0) return;
        let direction = param.findItem(e => e.conditionName === '报价方向');
        if (direction) {
            direction.conditions.forEach(e => e.name = directionMap.get(e.code));
        }
        let province = param.findItem(e => e.conditionName === '交易地点');
        if (province) {
            province.conditions.unshift({code: null, name: '不限'});
        }
        return param;
    }

    creatEnum (name, param) {
        if (!param || !angular.isArray(param) || param.length === 0) return;
        let item = param.findItem(e => e.conditionName === name);
        if (!item || !item.conditions) return;
        let conditions = item.conditions;
        if (!conditions || !angular.isArray(conditions) || conditions.length === 0) return;
        let enu = {};
        conditions.forEach(e => {
            enu[e.code] = e.name;
        })
        return enu;
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
            //是否显示过滤菜单，默认false
            enableFiltering: false,
            //是否允许排序，默认true
            enableSorting: false,
            enableColumnResizing: false,

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
            // enableInfiniteScroll: false,
            //滚动到距离底部10条时加载数据，默认20
            // infiniteScrollRowsFromEnd: 20,
            //是否允许向下无限滚动，默认true
            // infiniteScrollDown: false,
            //是否允许向上无限滚动，默认true
            // infiniteScrollUp: false,

            //是否允许固定列
            enablePinning: true,

            //是否允许单元格编辑功能，默认undefined
            enableCellEdit: true,
            enableCellEditOnFocus: true,

            //ui-grid回调函数注册
            onRegisterApi: function (gridApi) {
                scope.gridApi = gridApi;
                gridApi.edit.on.beginCellEdit(scope, (rowEntity, colDef, newValue, oldValue) => {
// debugger;
                });
                gridApi.edit.on.afterCellEdit(scope, (rowEntity, colDef, newValue, oldValue) => {
                    if (colDef.name === '交易地点' && newValue) {
                        rowEntity.quoteProvinces.provinceName = scope.$ctrl.gridExcelImportService.paramEnum.findItem(e => e.conditionName === '交易地点').conditions.findItem(e => e.code === newValue).name;
                    }
                    if (colDef.name === '票面金额(元)') {
                        rowEntity.amount = +rowEntity.amount.toFixed(2);
                    }
                    if (colDef.name === '期望价格(%)') {
                        rowEntity.price = '' + ((+rowEntity.price).toFixed(3));
                    }
                    if (colDef.name === '票类' && colDef.editModelField === 'mediumType') {
                        let mediumType = rowEntity.mediumType.split('_');
                        rowEntity.billMedium = mediumType.length === 3 ? mediumType[1] : mediumType[0];
                        rowEntity.minor = mediumType.length === 3 ? true : false;
                    }
                    scope.$ctrl.commonService.safeApply(scope);
                });
            },
            rowTemplate: '<div ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ui-grid-cell></div>',
            columnDefs: this.gridColumnFactoryService.buildColumn(columnDefs, templateDefine)
        };
        return options;
    }

    [init] () {
        console.debug('gridExcelImportService initialized.');
    }

    [getExcelImportPanelTemplate] () {
        return require('../component/template/grid_excel_import.html');
    }

    [getGridColumnPanelTemplate] () {
        return require('../component/template/grid_column_panel.html');
    }
}

export default ['uiGridConstants', '$mdPanel', 'httpService', 'userService', 'configConsts', 'gridColumnFactoryService', gridExcelImportService];