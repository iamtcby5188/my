import panelBaseDialogCtrl from './../../../common/controller/panelBaseDialogCtrl';

const initView = Symbol('initView');

class addPriceSsrCtrl extends panelBaseDialogCtrl {
    constructor($sce, commonService, configConsts) {
        super($sce, commonService, configConsts);

        this[initView]();
    };

    [initView]() {
        console.debug('addPriceSsrCtrl initView');

        this.dialogType = "addPrice";

        this.quoteType = 'ssr-quote';

        this.contentStyle = { 'width': '480px' };
    };

    $getDialogResult() {

    };

    $onDialogVmChanged(result) {

        if (!result) return;

        if (result.billTypeMedium && result.billTypeMedium.billType && this.oldBillType !== result.billTypeMedium.billType && this.itemSource instanceof Array) {

            if (result.billTypeMedium.billType === 'BKB') {
                let inputItem = this.itemSource.findItem(e => e.vm === "qptOrAh");
                if (inputItem) {
                    inputItem.attribute.vmType = 'quotePriceType';
                }
            } else if (result.billTypeMedium.billType === 'CMB') {
                let inputItem = this.itemSource.findItem(e => e.vm === "qptOrAh");
                if (inputItem) {
                    inputItem.attribute.vmType = 'acceptingHouse';
                }
            }

            this.oldBillType = result.billTypeMedium.billType;
        }

        if (result.normalOrQuickBoard !== undefined) {

            if (this.oldNormalOrQuickBoard !== undefined && this.oldNormalOrQuickBoard !== result.normalOrQuickBoard) {
                if (this.onDialogVmChanged)
                    this.onDialogVmChanged(result);
            }

            this.oldNormalOrQuickBoard = result.normalOrQuickBoard;
        }
    };
};

export default ['$sce', 'commonService', 'configConsts', addPriceSsrCtrl]