import quotOperConfirmCtrl from './../../controller/quotOperConfirm';

const createAddPriceContent = Symbol('createAddPriceContent');
const createConfirmContent = Symbol('createConfirmContent');

const init = Symbol('init');

// const searchCriteriaTemplateMap = new Map([
//     ['inputButtonSelector', '<input-button-selector></input-button-selector>'],
//     ['inputButtonSelector', '<input-button-selector></input-button-selector>'],
//     ['title', 'Author']
// ]);

class componentCommonService {

    constructor($mdDialog, $mdPanel, $mdCompiler, commonService) {
        this.commonService = commonService;

        this.$mdDialog = $mdDialog;
        this.$mdPanel = $mdPanel;

        this.$mdCompiler = $mdCompiler;

        this[init]();
    };

    setDisplayNameForItems(source, displayPath) {
        if (!source) return;

        if (source && source instanceof Array) {
            source.forEach((item, index) => {
                item.$displayName = displayPath ? this.commonService.getPropertyX(item, displayPath) : item.displayName
            });
        }
    };

    openAddPriceDialog(controller, options) {
        if (!controller) {
            console.error("openAddPriceDialog controller is Null.");
        }
        if (!options) {
            console.error("openAddPriceDialog options is Null.");
        }

        var itemSource = angular.copy(options.itemSource);

        if (itemSource instanceof Array) itemSource.forEach((e, i) => {
            if (!e || !e.attribute) return;

            if (e.attribute.itemSource instanceof Array) e.attribute.itemSource.forEach((e1, i1) => {
                e1.$id = i1;
            })
        })

        // return this.$mdDialog.show({
        //     controller: controller,
        //     controllerAs: '$ctrl',
        //     locals: {
        //         theme: options.theme || 'default',
        //         title: options.title,
        //         itemSource: itemSource,
        //         onClosing: options.onClosing || (() => true),

        //         onDialogVmChanged: options.onVmChanged || undefined
        //     },
        //     bindToController: true,
        //     template: this[createAddPriceContent](),
        //     parent: angular.element(document.body),
        //     targetEvent: event,
        //     clickOutsideToClose: false,
        //     fullscreen: true
        // });

        var position = this.$mdPanel.newPanelPosition()
            .absolute()
            .center();

        var panelAnimation = this.$mdPanel.newPanelAnimation()
            .withAnimation(this.$mdPanel.animation.SCALE)
            .openFrom(options.button)
            .closeTo(options.button);

        var config = {
            attachTo: angular.element(document.body),
            controller: controller,
            controllerAs: '$ctrl',
            // templateUrl: 'panel.tmpl.html',
            locals: {
                theme: options.theme || 'default',
                title: options.title,
                itemSource: itemSource,
                onClosing: options.onClosing || (() => true),

                onDialogVmChanged: options.onVmChanged || undefined
            },
            bindToController: true,
            template: this[createAddPriceContent](),
            hasBackdrop: true,
            position: position,
            // animation: panelAnimation,
            trapFocus: true,
            // zIndex: 1500,
            clickOutsideToClose: false,
            // escapeToClose: true,
            // focusOnOpen: true
        };

        return this.$mdPanel.open(config);
    };

    initEditPriceVm(dialogDataDefine, vmDataDefine, item) {
        dialogDataDefine.forEach(e => {
            // 装填editPriodVmMap 定义通用属性 
            var func = vmDataDefine.editPriod[e.vm];
            if (func && typeof func === 'function') e.attribute.ngModel = func(item);

            // 装填特殊属性 List...AutoComplete...
            switch (e.component) {
                case "inputButtonSelector":
                    if (e.attribute.vmType === 'Object') {
                        if (angular.isString(e.attribute.ngModel)) {
                            e.attribute.ngModel = e.attribute.itemSource.findItem(e1 => e1.vmValue !== undefined && e1.vmValue === e.attribute.ngModel);
                        } else {
                            e.attribute.ngModel = e.attribute.itemSource.findItem(e1 => e1.vmValue !== undefined && angular.equals(e1.vmValue, e.attribute.ngModel));
                        }
                    } else {
                        e.attribute.ngModel = e.attribute.itemSource.findItem(e1 => {
                            if (e.attribute.ngModel instanceof Array)
                                return e1.attribute.ngModel.findItem(e2 => e2.vmValue !== undefined && e1.vmValue === e2.vmValue) !== undefined;
                            else return false;
                        });
                    }
                    break;
                case "inputButtonSwitcher":
                    if (e.attribute.ngModel) {
                        e.attribute.ngModel = e.attribute.itemSource.findItem(e1 => e1.vmValue !== undefined && angular.equals(e1.vmValue, e.attribute.ngModel));
                    }
                    break;
                case "dropLabel":
                    if (e.vm) {
                        if (e.vm === "quoteProvinces") {
                            if (e.attribute.ngModel) {
                                e.attribute.ngModel = e.attribute.itemSource.findItem(e1 => e1.code !== undefined && e1.code === e.attribute.ngModel.provinceCode);
                            }
                        }
                    }
                    break;
                case "addPriceJoininguser":
                    if (item.containsAdditionalInfo) {
                        e.attribute.ngModel = {
                            joiningCompanyDto: { name: item.additionalInfo.quoteCompanyName },
                            joiningUserDto: {
                                name: item.additionalInfo.contactName,
                                mobileTel: item.additionalInfo.contactTelephone
                            }
                        };
                        e.attribute.isReadonly = true;
                    } else {
                        e.attribute.ngModel = e.attribute.itemSource.findItem(
                            e1 => e1.joiningUserDto !== undefined && e1.joiningUserDto.id !== undefined &&
                                e.attribute.ngModel.joiningUserDto !== undefined && e1.joiningUserDto.id === e.attribute.ngModel.joiningUserDto.id);
                    }
                    break;
                case "addPriceAcceptinghouseQuotepricetype":
                    if (item.billType === 'BKB') {
                        e.attribute.ngModel = {
                            quotePriceType: e.attribute.itemSource.findItem(e1 => e1.code !== undefined && e1.code === item.quotePriceType)
                        };
                        e.attribute.vmType = "quotePriceType";
                    } else if (item.billType === 'CMB') {
                        e.attribute.ngModel = {
                            acceptingHouse: item.acceptingHouse
                        };
                        e.attribute.vmType = "acceptingHouse";
                    }
                    break;
                default:
                    break;
            };
        })
    };

    closeDialog() {
        this.$mdDialog.hide();
    };

    openErrorDialog(params) {
        // this.$mdDialog.show({
        //     controller: require('./../controller/errorDialogCtrl').default,
        //     controllerAs: '$ctrl',
        //     locals: {
        //         // theme: params && params.theme ? params.theme : 'default',

        //         theme: "asdgfadg",

        //         title: params && params.title ? params.title : '',
        //         errorMessage: params && params.message ? params.message : '',
        //     },
        //     bindToController: true,
        //     template: this[createAddPriceContent](),
        //     parent: angular.element(document.body),
        //     clickOutsideToClose: true,
        //     fullscreen: true
        // });

        this.$mdDialog.show(
            this.$mdDialog.alert()
                .title(params && params.title ? params.title : '')
                .theme(params && params.theme ? params.theme : 'default')
                .htmlContent(params && params.message ? params.message : '')
                .ok("确定")
        );
    }

    openShowImg(controller, params) {
        var config = {
            attachTo: angular.element(document.body),
            controller: controller,
            controllerAs: '$ctrl',
            bindToController: true,
            theme: params.theme || 'default',
            locals: {
                title: params.title,
                thumb: params.thumb,
            },
            template: require('./../component/template/panel_show_img.html'),
            hasBackdrop: true,
            trapFocus: true,
            clickOutsideToClose: false,
        };

        return this.$mdDialog.show(config);
    };

    openConfirmDialog(controller, params) {
        var config = {
            attachTo: angular.element(document.body),
            controller: controller || quotOperConfirmCtrl,
            controllerAs: '$ctrl',
            bindToController: true,
            theme: params.theme || 'default',
            locals: {
                title: params.title,
                image: params.image || undefined,
                textContent: params.message || "",
                quotContent: params.quotContent || "",
                okText: params.okText || "确认",
                cancelText: params.cancelText || "取消",
                onOkCallback: params.okCallback || (() => true),
                onCancelCallback: params.cancelCallback || (() => true),
            },
            template: this[createConfirmContent](),
            hasBackdrop: true,
            trapFocus: true,
            clickOutsideToClose: false,
        };

        return this.$mdDialog.show(config);
    }

    createSearchCriteriaVm(param) {
        if (!param || !param.source || !param.dataDefine || !param.defineMatchFunc) return undefined;

        if (!(param.source instanceof Array)) return;

        param.source.forEach((item, index) => {
            var define = param.dataDefine.findItem(e => param.defineMatchFunc(item, e));

            if (!define) return;

            // if (!searchCriteriaTemplateMap.has(define.component)) {
            //     console.error(`componentCommonService.createSearchCriteriaVm: no template define. ${define.component}`);
            // }

            if (!define.attribute) {
                console.error(`componentCommonService.createSearchCriteriaVm: no 'attribute' property set in dataDefine.`);
                return;
            }

            var setId = (e, i) => {
                e.$parentCriteriaId = define.id;
                e.$id = i;
            };

            switch (define.component) {
                case 'inputButtonSelector':
                    // this.setDisplayNameForItems(item.conditions, define.displayPath);

                    // define.compileOption = {
                    //     template: searchCriteriaTemplateMap.get(define.component),
                    //     templateParam: {
                    //         'item-source': item.conditions
                    //     }
                    // };

                    define.attribute.itemSource = angular.copy(item.conditions);
                    define.attribute.itemSource.forEach((e, i) => {
                        setId(e, i);
                        e.isDefault = define.default && typeof define.default === "function" && define.default(e);
                    });
                    break;
                case 'inputSelectorRange':
                    if (!define.attribute.itemSource) define.attribute.itemSource = {};
                    define.attribute.itemSource.selectorValue = angular.copy(item.conditions);
                    define.attribute.itemSource.selectorValue.forEach(setId);
                    if (!define.attribute.itemSource.rangeValue) define.attribute.itemSource.rangeValue = {};
                    define.attribute.itemSource.rangeValue.$parentCriteriaId = define.id;
                    break;
                case 'inputDropdownChips':
                    define.attribute.itemSource = angular.copy(item.conditions);
                    define.attribute.itemSource.forEach(setId);
                    break;
                case 'barNav':
                    define.attribute.itemSource = angular.copy(item.conditions);
                    define.attribute.itemSource.forEach((e, i) => {
                        setId(e, i);
                        if (define.default && typeof define.default === "function" && define.default(e)) {
                            e.isDefault = true;
                        }
                    });
                    break;
                case 'dropLabel':
                    define.attribute.itemSource = angular.copy(item.conditions);
                    define.attribute.itemSource.forEach((e, i) => {
                        setId(e, i);
                        if (define.default && typeof define.default === "function" && define.default(e)) {
                            e.isDefault = true;
                        }
                    });
                case 'searchcriteriaAcceptinghouseQuotepricetype':
                    if (!define.attribute.itemSource) define.attribute.itemSource = {};
                    if (item.conditionName === "承兑行类别") {
                        define.attribute.itemSource.quotePriceTypeItemSource = angular.copy(item.conditions);
                        define.attribute.itemSource.quotePriceTypeItemSource.forEach(setId);
                    }
                    if (item.conditionName === "承兑方类别") {
                        define.attribute.itemSource.acceptingHouseItemSource = angular.copy(item.conditions);
                        define.attribute.itemSource.acceptingHouseItemSource.forEach(setId);
                    }
                    break;
                default:
                    break;
            };


        });

        return param.dataDefine;
    };

    getSearchCriteriaListByVm(vm, dataDefine) {
        if (!vm || !(vm instanceof Array)) return [];

        var list = [];

        vm.forEach(e => {
            list.push.apply(list, e);
        })

        if (dataDefine instanceof Array) {
            list.forEach(e => {
                if (!e.$parentCriteriaId) return;
                e.$parentCriteria = dataDefine.findItem(e1 => e1.id == e.$parentCriteriaId);
            })
        }

        return list;
    };

    convertDataDefineArrayToVm(arr, dataDefine) {
        if (!(arr instanceof Array) || !(dataDefine instanceof Array)) return;

        var viewModel = {};

        arr.forEach(item => {
            var define = undefined;

            if (item instanceof Array && item.length > 0) {
                define = dataDefine.findItem(e => e.id === item[0].$parentCriteriaId);
                if (!define) return;

                if (define.component === "searchcriteriaAcceptinghouseQuotepricetype") {
                    this.commonService.setPropertyX(viewModel, define.attribute.vmType, item.map(e => e.code));
                } else {
                    this.commonService.setPropertyX(viewModel, define.vm, item.map(e => e.code));
                }
            } else {
                define = dataDefine.findItem(e => e.id === item.$parentCriteriaId);
                if (!define) return;
                this.commonService.setPropertyX(viewModel, define.vm, item.code);
            }
        });

        return viewModel;
    };

    [init]() {
        console.debug('componentCommonService initialized.')
    };

    [createAddPriceContent](dataDefine) {
        return require('./../component/template/panel_base_dialog.html');
    };

    [createConfirmContent]() {
        return require('../../component/quoteManage/template/quot_oper_confirm_dialog.html');
    }
}
;

export default ['$mdDialog', '$mdPanel', '$mdCompiler', 'commonService', componentCommonService];