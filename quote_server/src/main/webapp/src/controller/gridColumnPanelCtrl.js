/**
 * Created by jiannan.niu on 2017/2/27.
 */
const initView = Symbol('initView');
const THEME_NAME = "ssAvalonUi";
class gridColumnPanelCtrl {

    constructor ($scope, $mdDialog, $mdPanel, commonService, gridExcelImportService, gridColumnFactoryService, configConsts, httpService, userService, gridDataDefineService, componentCommonService) {
        this.$scope = $scope;
        this.$mdDialog = $mdDialog;
        this.$mdPanel = $mdPanel;
        this.commonService = commonService;
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridExcelImportService = gridExcelImportService;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this.userService = userService;
        this.componentCommonService = componentCommonService;
        $scope.gridDataDefineService = gridDataDefineService;

        this[initView]();

    }

    [initView] () {
        this.theme = THEME_NAME;
    }

    $onClickOk (event) {
        if (!this.columnDefs || !angular.isArray(this.columnDefs)) return;
        //处理样式，让ui-grid看起来正常
        this.columnDefs.forEach(e => {
            if (e.realWidth) {
                e.width = e.realWidth;
            }
        });
        let cols = this.columnDefs.findWhere(e => e.visible);
        let resize = cols.findItem(e => e.width === '*');
        if (!resize) {
            cols[cols.length - 1].realWidth = cols[cols.length - 1].width;
            cols[cols.length - 1].width = '*';
        }
        this.scope.gridOptions.columnDefs = angular.copy(this.columnDefs);
        this.commonService.safeApply(this.scope);

        this.mdPanelRef && this.mdPanelRef.close().then(() => {
            this.scope.saveState(this.scope.type + 'State');
            localStorage.setItem(this.scope.type + 'State', JSON.stringify(this.scope[this.scope.type + 'State']));
            this.mdPanelRef.destroy();
        });
    }

    $onClickCancel (event) {
        this.mdPanelRef && this.mdPanelRef.close().then(() => {
            this.mdPanelRef.destroy();
        });
    }
}

export default ['$scope', '$mdDialog', '$mdPanel', 'commonService', 'gridExcelImportService', 'gridColumnFactoryService', 'configConsts', 'httpService', 'userService', 'gridDataDefineService', 'componentCommonService', gridColumnPanelCtrl];