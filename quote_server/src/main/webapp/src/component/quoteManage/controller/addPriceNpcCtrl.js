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
        this.quoteType = 'npc-quote';
        this.contentStyle = {
            'width': '640px'
        }
    };

    $getDialogResult() {

    };

    $onDialogVmChanged(result) {

        if (!result) return;

        if (this.oldcontainsAdditionalInfo !== undefined && this.oldcontainsAdditionalInfo !== result.containsAdditionalInfo) {
            if (this.onDialogVmChanged)
                this.onDialogVmChanged(result);
        }

        this.oldcontainsAdditionalInfo = result.containsAdditionalInfo;
    };
};

export default ['$sce', 'commonService', 'configConsts', addPriceSsrCtrl]