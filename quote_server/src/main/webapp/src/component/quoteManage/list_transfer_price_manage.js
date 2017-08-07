const initView = Symbol('initView');

const dataDefine={
    searchPricemngDataDefine:[
        {
            id:"npcMng1",typeName:"npcBillMedium",component:"barNav",containerClass:"inline-block", flex:"20",
            attribute:{
                itemSource:[
                    {parentid:"npcMng1",code:"zp",displayName:"纸票",bDefault:true},
                    {parentid:"npcMng1",code:"dp",displayName:"电票"}
                    ],
            }
        },
        {
            id:"npcMng2",typeName:"npcAddQuot",component:"mdButton",displayName:"新增价格", containerClass:"inline-block",flex:"20",containerAlien:"alien-right",
        },
        {
            id:"npcMng3",typeName:"npcImport",component:"mdButton",displayName:"批量导入" ,containerClass:"inline-block",flex:"20",containerAlien:"alien-right",
        },
        {
            id:"npcMng4",typeName:"npcDateRange",component:"inputDatePickerRange",containerClass:"inline-block", flex:"40",containerAlien:"alien-right",
            attribute:{label:"发布时间"}
        },
        {
            id:"npcMng5",typeName:"npcTradeModel",component:"dropLabel",containerClass:"inline-block",containerAlien:"alien-right", flex:"20",
            attribute:{
                itemSource:[
                    {parentid:"npcMng5",code:"hg",displayName:"回购"},
                    {parentid:"npcMng5",code:"md",displayName:"买断"}
                    ],
                label:"交易模式",
            }
        },
    ],
}
class listTransferPriceManageCtrl {

    constructor($scope, commonService) {
        this.$scope = $scope;
        this.searchPanelData = angular.copy(dataDefine.searchPricemngDataDefine);

        this[initView]();
    };


    [initView]() {
        console.debug('listTransferPriceManageCtrl initView');
    };

    onVmChanged(){
        console.debug(this.searchPanelModel);
    }

    onBtnClick(id){
        console.debug(id);
    }
};

let listTransferPriceManage = () => {
    return {
        template: require('./template/list_transfer_price_manage.html'),
        bindings: {
            theme: '@mdTheme',
        },
        controller: ['$scope', '$mdDialog', 'commonService', listTransferPriceManageCtrl]
    }
};

export default listTransferPriceManage;