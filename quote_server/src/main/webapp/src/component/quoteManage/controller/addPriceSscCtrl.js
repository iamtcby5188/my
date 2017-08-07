import panelBaseDialogCtrl from './../../../common/controller/panelBaseDialogCtrl';

const initView = Symbol('initView');
const hideInputItem = Symbol('hideInputItem');
const showInputItem = Symbol('showInputItem');
const removeNgModel = Symbol('removeNgModel');

const dataDefine = {
    CMB: [
        "prices.ybhPrice",
        "prices.wbhPrice"
    ],

    BKB: [
        "prices.ggPrice",
        "prices.csPrice",
        "prices.nsPrice",
        "prices.nxPrice",
        "prices.nhPrice",
        "prices.czPrice",
        "prices.wzPrice",
        "prices.cwPrice"
    ]
};

class addPriceSscCtrl extends panelBaseDialogCtrl {
    constructor($sce, commonService, configConsts) {
        super($sce, commonService, configConsts);

        this[initView]();
    };

    [initView]() {
        console.debug('addPriceSscCtrl initView');

        this.dialogType = "addPrice";
        this.quoteType = 'ssc-quote';
        this.contentStyle = {
            'width': '640px'
        }
    };

    [hideInputItem](vmName) {
        let inputItem = this.itemSource.findItem(e => e.vm && e.vm.startWith(vmName));
        if (inputItem) {
            inputItem.attribute.isHide = true;
        }
    };

    [showInputItem](vmName) {
        let inputItem = this.itemSource.findItem(e => e.vm && e.vm.startWith(vmName));
        if (inputItem) {
            inputItem.attribute.isHide = false;
        }
    };

    [removeNgModel](vmName) {
        let inputItem = this.itemSource.findItem(e => e.vm && e.vm.startWith(vmName));
        if (inputItem) {
            delete inputItem.attribute["ngModel"];
        }
    };

    $onDialogVmChanging(result) {
        if (!result) return; 

        if (result.billTypeMedium) {

            if (result.billTypeMedium.billType === "CMB") {
                dataDefine.BKB.forEach(item => { this[removeNgModel](item); });
            } else if (result.billTypeMedium.billType === "BKB") {
                dataDefine.CMB.forEach(item => { this[removeNgModel](item); });
            }
        }
    };

    $onDialogVmChanged(result) {
        if (!result) return;

        if (result.billTypeMedium) {

            if (result.billTypeMedium.billType === "CMB") {
                dataDefine.BKB.forEach(item => { this[hideInputItem](item); });
                dataDefine.CMB.forEach(item => { this[showInputItem](item); });
            } else if (result.billTypeMedium.billType === "BKB") {
                dataDefine.CMB.forEach(item => { this[hideInputItem](item); });
                dataDefine.BKB.forEach(item => { this[showInputItem](item); });
            }
        }

        ["ggPrice","csPrice","nsPrice","nxPrice","nhPrice","czPrice","wzPrice","cwPrice","ybhPrice","wbhPrice"].forEach(e=>{
            result.prices[e].unit = result.unit
        });
    };
};

export default ['$sce', 'commonService', 'configConsts', addPriceSscCtrl]