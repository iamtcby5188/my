const initView = Symbol('initView');
const $closeDialog = Symbol('$closeDialog');
const $dialogResult = Symbol('$dialogResult');

class panelBaseDialogCtrl {

    constructor($sce, commonService, configConsts) {
        this.commonService = commonService;
        this.configConsts = configConsts;
        this.$sce = $sce;

        this[initView]();
    };

    [initView]() {
        console.debug('panelBaseDialogCtrl initView');

        this[$dialogResult] = undefined;

        this.labelFlex = 20;
        this.inputFlex = 80;

        if (this.itemSource && this.itemSource instanceof Array) {
            var item = this.itemSource.findItem(e => e.component === 'labelHtmlContent');

            if (item) item.html = this.$sce.trustAsHtml(item.html);
        }

        this.pickerConfig = {
            class: 'datepicker-no-tip',
            readOnly: true,
            yearMax: +new Date().format('yyyy') + 1,
            yearMin: +new Date().format('yyyy'),
            upperRange: new Date().getTime() + 86400000 * this.configConsts.maxQuoteDays,
            lowerRange: new Date().getTime()
        }
    };

    onVmChanged() {
        if (!this.itemSource || !(this.itemSource instanceof Array)) return;

        if (this.$onDialogVmChanging) this.$onDialogVmChanging(this[$dialogResult]);

        this[$dialogResult] = {}

        this.itemSource.forEach(e => {
            // if (!e.vm) {
            //     console.error('panelBaseDialogCtrl: vm property is required for item in itemSource.');
            //     return;
            // }

            if (!e.vm) return;

            if (e.attribute && e.attribute.ngModel && e.attribute.ngModel.vmValue !== undefined) {
                this.commonService.setPropertyX(this[$dialogResult], e.vm, e.attribute.ngModel.vmValue);
            } else {
                this.commonService.setPropertyX(this[$dialogResult], e.vm, e.attribute.ngModel);
            }
        });

        if (this.$onDialogVmChanged) this.$onDialogVmChanged(this[$dialogResult]);
    };

    $onClickClear(event) {
        if (!this.itemSource || !(this.itemSource instanceof Array)) return;

        this[$dialogResult] = {}

        this.itemSource.forEach(e => {
            // if (e.component === 'addPriceJoininguser') return;
            // if (e.component === 'inputDatePicker') {
            //     this.commonService.setPropertyX(e, "attribute.ngModel", new Date());
            //     return;
            // }

            this.commonService.setPropertyX(e, "attribute.ngModel", undefined);
        });
    };

    $onClickOk(event) {
        if (this.onClosing) {
            var returnValue = this.onClosing(this[$dialogResult]);

            if (returnValue === false) return;

            if (returnValue && returnValue.then && typeof returnValue.then === 'function') {
                returnValue.then(res => {
                    // debugger;
                    // this.$mdDialog.hide(this.$dialogResult);
                    if (res && res === true) this.mdPanelRef.close();
                    return;
                }, res => {
                    console.log(res);
                    return;
                })

                return;
            }
        }

        // debugger;
        // this.$mdDialog.hide(this.$dialogResult);

        this.mdPanelRef.close();
    };

    $onClickCancel(event) {
        this.mdPanelRef && this.mdPanelRef.close().then(() => {
            // angular.element(document.querySelector('.demo-dialog-open-button')).focus();
            this.mdPanelRef.destroy();
        });
    };

    $setDialogResult(result) {
        this[$dialogResult] = result;
    };

    $onInit(){
    };
};

export default panelBaseDialogCtrl;