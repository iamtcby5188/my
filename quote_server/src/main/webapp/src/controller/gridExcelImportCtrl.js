/**
 * Created by jiannan.niu on 2017/2/10.
 */
const initView = Symbol('initView');
const THEME_NAME = "ssAvalonUi";
class gridExcelImportCtrl {

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

        const importAddressDefine = {
            "SSR": this.configConsts.quoteExcel_insertSSR,
            "SSC": this.configConsts.quoteExcel_insertSSC,
            "NPC": this.configConsts.quoteExcel_insertNPC
        };
        const insertAddressDefine = {
            "SSR": this.configConsts.bab_insert_ssr,
            "SSC": this.configConsts.bab_insert_ssc,
            "NPC": this.configConsts.bab_insert_npc
        };
        const emitEvent = {
            "SSR": "gridManageSsrTicketHallCtrl",
            "SSC": "gridManageSscTicketHall",
            "NPC": "gridManageNpcTicketHall"
        };
        const Validation = {
            "ggPrice": "国股价格",
            "csPrice": "城商价格",
            "nsPrice": "农商价格",
            "nxPrice": "农信价格",
            "nhPrice": "农合价格",
            "wzPrice": "外资价格",
            "czPrice": "村镇价格",
            "cwPrice": "财务公司价格",
            "ybhPrice": "有保函价格",
            "wbhPrice": "无保函价格",
            "price": "期望价格"
        };
        this.Validation = Validation;
        this.emitEvent = emitEvent;
        this.insertAddressDefine = insertAddressDefine;
        $scope.handleFiles = (files) => {
            if (!files[0] || !files[0].name) return;
            if (!(/\.(xlsx|xls)$/.test(files[0].name))) {
                this.fileName = '请选择正确的文件格式';
                this.commonService.safeApply($scope);
                return;
            }
            var type = this.type.toUpperCase();

            this.fileName = files[0].name;
            this.commonService.safeApply($scope);

            let reader = new FileReader();
            reader.readAsDataURL(files[0]);
            reader.onload = (e) => {
                let param = {data: e.target.result, fileName: files[0].name}
                let headers = this.userService.getAuthHeaders();
                this.httpService.postService(importAddressDefine[type], param, headers).then(
                    (res) => {
                        if (res.return_code === 0 && angular.isArray(res.result) && res.result.length === 1 && res.result[0].billType && res.result[0].quotes) {

                            let billType = res.result[0].billType;
                            let columnDefs = this.gridExcelImportService.columnDefs.findWhere(e => {
                                return e[type] && e[billType];
                            }).sort((a, b) => {
                                return a.SORT_NUMBER - b.SORT_NUMBER;
                            });
                            let templateDefine = this.gridExcelImportService.templateDefine;
                            $scope.gridOptions = this.gridExcelImportService.buildGridOptions($scope, columnDefs, templateDefine);
                            if (angular.isArray(res.result[0].quotes) && res.result[0].quotes.length !== 0) {
                                res.result[0].quotes.forEach(e => {
                                    if (e.dueDate) {
                                        e.dueDate = new Date(e.dueDate);
                                    }
                                });
                                res.result[0].quotes.forEach(e => {
                                    if (e.effectiveDate) {
                                        e.effectiveDate = new Date(e.effectiveDate);
                                    }
                                });
                                if (type === 'SSC' && billType === 'BKB') {
                                    res.result[0].quotes.forEach(e => {
                                        e.mediumType = (e.minor ? 'MINOR_' : '') + e.billMedium + '_' + e.billType;
                                    });
                                }
                                $scope.gridOptions.data = this.addIndex(res.result[0].quotes, 'index');
                            }
                            if (res.result[0].invalids !== null) {
                                let message = '';
                                if (angular.isArray(res.result[0].invalids) && res.result[0].invalids.length !== 0) {
                                    res.result[0].invalids.forEach((e, index) => {
                                        if (index !== res.result[0].invalids.length - 1) {
                                            message += e + '<br>';
                                        } else {
                                            message += e;
                                        }
                                    });
                                } else {
                                    message = '导入出错，请重试！';
                                }
                                this.componentCommonService.openErrorDialog({
                                    title: '错误提示',
                                    theme: THEME_NAME,
                                    message: message
                                });
                            }
                        } else {
                            let message = '';
                            if (angular.isArray(res.result[0].invalids) && res.result[0].invalids.length !== 0) {
                                res.result[0].invalids.forEach((e, index) => {
                                    if (index !== res.result[0].invalids.length - 1) {
                                        message += e + '<br>';
                                    } else {
                                        message += e;
                                    }
                                });
                            } else {
                                message = '导入出错，请重试！';
                            }
                            this.componentCommonService.openErrorDialog({
                                title: '错误提示',
                                theme: THEME_NAME,
                                message: message
                            });
                        }
                    }, (err) => {
                        let message = '';
                        if (err.return_message && err.return_message.errorDetailsList && angular.isArray(err.return_message.errorDetailsList) && err.return_message.errorDetailsList.length !== 0) {
                            err.return_message.errorDetailsList.forEach((e, index) => {
                                if (index !== err.return_message.errorDetailsList.length - 1) {
                                    message += e.detailMsg + '<br>';
                                } else {
                                    message += e.detailMsg;
                                }
                            });
                        } else if (err.return_message && err.return_message.exceptionCode && err.return_message.exceptionMessage) {
                            message = err.return_message.exceptionName + '，' + err.return_message.exceptionMessage;
                        } else {
                            message = '导入出错，请重试！';
                        }
                        this.componentCommonService.openErrorDialog({
                            title: '错误提示',
                            theme: THEME_NAME,
                            message: message
                        });
                    }
                )
                $('#file').val('');
            }
        };

        this[initView]();
    };

    [initView] () {
        this.theme = THEME_NAME;

        this.pickerConfig = {
            class: 'datepicker-no-tip',
            readOnly: true,
            yearMax: +new Date().format('yyyy') + 1,
            yearMin: +new Date().format('yyyy'),
            upperRange: new Date().getTime() + 86400000 * this.configConsts.maxQuoteDays,
            lowerRange: new Date().getTime()
        };

        let type = this.type.toUpperCase();
        let columnDefs = this.gridExcelImportService.columnDefs.findWhere(e => {
            return e.BKB && e[type];
        }).sort((a, b) => {
            return a.SORT_NUMBER - b.SORT_NUMBER;
        });
        let templateDefine = this.gridExcelImportService.templateDefine;
        this.$scope.gridOptions = this.gridExcelImportService.buildGridOptions(this.$scope, columnDefs, templateDefine);
    };

    $onClickOk (event) {
        if (!this.$scope.gridOptions.data || !angular.isArray(this.$scope.gridOptions.data)) return;
        if (this.$scope.gridOptions.data.length === 0) {
            this.componentCommonService.openErrorDialog({
                title: '错误提示',
                theme: THEME_NAME,
                message: '请先导入模板文件'
            });
            return;
        }
        let headers = this.userService.getAuthHeaders();
        let type = this.type.toUpperCase();
        this.httpService.postService(this.insertAddressDefine[type], this.$scope.gridOptions.data, headers).then(res => {
            // console.log(res);
            if (res.return_code === 0 && res.return_message === "Success") {
                this.$scope.$emit(this.emitEvent[type]);
                this.mdPanelRef && this.mdPanelRef.close().then(() => {
                    this.mdPanelRef.destroy();
                });
            }
        }, err => {
            let message = '';
            if (err.return_message && err.return_message.errorDetailsList && angular.isArray(err.return_message.errorDetailsList) && err.return_message.errorDetailsList.length !== 0) {
                err.return_message.errorDetailsList.forEach((e, index) => {
                    if (index !== err.return_message.errorDetailsList.length - 1) {
                        message += (this.Validation[e.sourceFiled]||'') + e.detailMsg + '<br>';
                    } else {
                        message += (this.Validation[e.sourceFiled]||'') + e.detailMsg;
                    }
                });
            } else {
                message = '报价出错！';
            }
            this.componentCommonService.openErrorDialog({
                title: '错误提示',
                theme: THEME_NAME,
                message: message
            });
        })
    }

    $onClickCancel (event) {
        this.mdPanelRef && this.mdPanelRef.close().then(() => {
            this.mdPanelRef.destroy();
        });
    }

    deleteRow ($event, scope, id) {
        if (!scope || !id) return;
        let data = scope.gridOptions.data.findWhere(e => e.index !== id);
        scope.gridOptions.data = this.addIndex(data, 'index');
        this.commonService.safeApply(scope);
    }

    addIndex (data, prop) {
        if (!data || !angular.isArray(data) || !prop || !angular.isString(prop)) return;
        data.forEach((e, index) => {
            e[prop] = index + 1;
        });
        return data;
    }

}

export default ['$scope', '$mdDialog', '$mdPanel', 'commonService', 'gridExcelImportService', 'gridColumnFactoryService', 'configConsts', 'httpService', 'userService', 'gridDataDefineService', 'componentCommonService', gridExcelImportCtrl];