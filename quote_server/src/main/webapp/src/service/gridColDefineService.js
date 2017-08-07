/**
 * Created by bianyu on 2016/12/16.
 */

const converColWith = Symbol('converColWith');
const getHeaderCellTemplate = Symbol('getHeaderCellTemplate');

class gridColDefineService {

    constructor (gridColumnFactoryService,gridDataDefineService) {
        this.gridColumnFactoryService = gridColumnFactoryService;
        this.gridDataDefineService = gridDataDefineService;
        this.allColums =
          {
            "col_status" :{name: '状态',  width: 100, isSort: false, template:'col_status'},
            "col_effectivedate" :{name: '生效日期',  width: 100, isSort: false, template:'col_effectivedate'},
            "col_tradetype":{name: '模式',  width: 100, isSort: false, template:'col_tradetype'},
            "col_quoter" :{name: '发价方',   width: 200, isSort: false, template:'col_quoter'},
            "col_contact":{name: '联系人',   width: 150, isSort: false, template:'col_contact'},
            "col_tel"    :{name: '联系方式', width: 150, isSort: false, template:'col_tel'},
            "col_province":{name: '地区',     width: 80, isSort: false, template:'col_province'},
            "col_gg"     :{displayName:'国股(%)', name: '国股',     width: 80,template: 'col_gg', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_cs"     :{displayName:'城商(%)',name: '城商',     width: 80,template:'col_cs', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_ns"     :{displayName:'农商(%)',name: '农商',     width: 80,template: 'col_ns', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_nx"     :{displayName:'农信(%)',name: '农信',     width: 80,template:'col_nx', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_nh"     :{displayName:'农合(%)',name: '农合',     width: 80,template:'col_nh', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_wz"     :{displayName:'外资(%)',name: '外资',     width: 80,template:'col_wz', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_cz"     :{displayName:'村镇(%)',name: '村镇',     width: 80,template:'col_cz', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_cw"     :{displayName:'财务公司(%)',name: '财务公司', width: 100,template:'col_cw', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_ybh"    :{displayName:'有保函(%)',name: '有保函',   width: 100,template:'col_ybh', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_wbh"    :{displayName:'无保函(%)',name: '无保函',   width: 100,template:'col_wbh', sortDirection: 'DA', isSort: true, headerCellTemplate: this[getHeaderCellTemplate](),},
            "col_memo"   :{name: '备注',     width: "*", isSort: false, template: 'col_memo'},
            "col_oper"   :{name: '操作',    width:60, isSort: false, template:'col_oper'}
          };

        this.sortField = {
            "国股": "gg_price",
            "城商": "cs_price",
            "农商": "ns_price",
            "农信": "nx_price",
            "农合": "nh_price",
            "外资": "wz_price",
            "村镇": "cz_price",
            "财务公司": "cw_price",
            "有保函": "ybh_price",
            "无保函": "wbh_price"
        };

        this.templateDefine = new Map([
            ['col_status', '<div class="ui-grid-cell-contents">{{grid.appScope.gridDataDefineService.PriceMngStatus[row.entity.quoteStatus]}}</div>'],
            ['col_effectivedate', '<div class="ui-grid-cell-contents" >{{row.entity.effectiveDate | date:"yyyy-MM-dd"}}</div>'],
            ['col_tradetype', '<div class="ui-grid-cell-contents">{{grid.appScope.gridDataDefineService.tradType[row.entity.tradeType]}}<div>'],
            ['col_quoter', '<div class="ui-grid-cell-contents"><span>{{ row.entity.containsAdditionalInfo ? row.entity.additionalInfo.quoteCompanyName : row.entity.quoteCompanyDto.name }}<md-tooltip class="tooltip" md-direction="right">{{ row.entity.containsAdditionalInfo ? row.entity.additionalInfo.quoteCompanyName : row.entity.quoteCompanyDto.name }}</md-tooltip></span></div>'],
            ['col_contact', '<div class="ui-grid-cell-contents">{{ row.entity.containsAdditionalInfo ? row.entity.additionalInfo.contactName : row.entity.contactDto.name }}</div>'],
            ['col_tel', '<div class="ui-grid-cell-contents">{{ row.entity.containsAdditionalInfo ? row.entity.additionalInfo.contactTelephone : row.entity.contactDto.mobileTel }}</div>'],
            ['col_province', '<div class="ui-grid-cell-contents">{{ row.entity.quoteProvinces.provinceName || "不限" }}</div>'],
            ['col_gg', '<div class="ui-grid-cell-contents">{{ row.entity.ggPrice || "　-"}}</div>'],
            ['col_cs', '<div class="ui-grid-cell-contents">{{ row.entity.csPrice || "　-"}}</div>'],
            ['col_ns', '<div class="ui-grid-cell-contents">{{ row.entity.nsPrice || "　-"}}</div>'],
            ['col_nx', '<div class="ui-grid-cell-contents">{{ row.entity.nxPrice || "　-"}}</div>'],
            ['col_nh', '<div class="ui-grid-cell-contents">{{ row.entity.nhPrice || "　-"}}</div>'],
            ['col_wz', '<div class="ui-grid-cell-contents">{{ row.entity.wzPrice || "　-"}}</div>'],
            ['col_cz','<div class="ui-grid-cell-contents">{{ row.entity.czPrice || "　-"}}</div>'],
            ['col_cw','<div class="ui-grid-cell-contents">{{ row.entity.cwPrice || "　-"}}</div>'],
            ['col_ybh',  '<div class="ui-grid-cell-contents">{{ row.entity.ybhPrice || "　-"}}</div>'],
            ['col_wbh',  '<div class="ui-grid-cell-contents">{{ row.entity.wbhPrice || "　-"}}</div>'],
            ['col_memo', '<div class="ui-grid-cell-contents"><span>{{ row.entity.memo }}<md-tooltip class="tooltip" md-direction="left" ng-if="row.entity.memo">{{ row.entity.memo }}</md-tooltip></span></div>'],
            ['col_oper', '<div class="ui-grid-cell-contents" ng-if="!row.entity.readOnly"><i ng-if="row.entity.quoteStatus===\'DFT\'" class="ss-icon ss-icon-finish-edit-normal ss-icon-highlight-bg pointer" tag="publish"><md-tooltip class="tooltip" md-direction="right">发布</md-tooltip></i> <i class="ss-icon ss-icon-edit-normal ss-icon-highlight-bg pointer" tag="edit"><md-tooltip class="tooltip" md-direction="right">编辑</md-tooltip></i> <i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" tag="del"><md-tooltip class="tooltip" md-direction="right">删除</md-tooltip></i></div>']
        ]);

          //银票
          this.price_BKB_Width = {
                          "col_quoter":{width:200},
                          "col_contact":{width:150},
                          "col_tel":{width:150},
                          "col_province":{width:80},
                          "col_gg":{width:80},
                          "col_cs":{width:80},
                          "col_ns":{width:80},
                          "col_nx":{width:80},
                          "col_nh":{width:80},
                          "col_wz":{width:80},
                          "col_cz":{width:80},
                          "col_cw":{width:100},
                          "col_memo":{width:"*"}
          };
          this.price_BKB = [
                             this.allColums.col_quoter,
                             this.allColums.col_contact,
                             this.allColums.col_tel,
                             this.allColums.col_province,
                             this.allColums.col_gg,
                             this.allColums.col_cs,
                             this.allColums.col_ns,
                             this.allColums.col_nx,
                             this.allColums.col_nh,
                             this.allColums.col_wz,
                             this.allColums.col_cz,
                             this.allColums.col_cw,
                             this.allColums.col_memo
                             ];

         //商票
        this.price_CMB_Width = {
                "col_quoter":{width:200},
                "col_contact":{width:150},
                "col_tel":{width:150},
                "col_province":{width:80},
                "col_ybh":{width:100},
                "col_wbh":{width:100},
                "col_memo":{width:"*"}
                };

          this.price_CMB = [
                             this.allColums.col_quoter,
                             this.allColums.col_contact,
                             this.allColums.col_tel,
                             this.allColums.col_province,
                             this.allColums.col_ybh,
                             this.allColums.col_wbh,
                             this.allColums.col_memo
                             ];
        //管理，银票
        this.price_BKB_mng_Width = {
                "col_status":{width:100},
                "col_effectivedate":{width:100},
                "col_quoter":{width:200},
                "col_contact":{width:150},
                "col_tel":{width:150},
                "col_gg":{width:80},
                "col_cs":{width:80},
                "col_ns":{width:80},
                "col_nx":{width:80},
                "col_nh":{width:80},
                "col_wz":{width:80},
                "col_cz":{width:80},
                "col_cw":{width:100},
                "col_memo":{width:"*"},
                "col_oper":{width:100}
                };

        this.price_BKB_mng = [
                             this.allColums.col_status,
                             this.allColums.col_effectivedate,
                             this.allColums.col_quoter,
                             this.allColums.col_contact,
                             this.allColums.col_tel,
                             this.allColums.col_gg,
                             this.allColums.col_cs,
                             this.allColums.col_ns,
                             this.allColums.col_nx,
                             this.allColums.col_nh,
                             this.allColums.col_wz,
                             this.allColums.col_cz,
                             this.allColums.col_cw,
                             this.allColums.col_memo,
                             this.allColums.col_oper
                             ];
         //管理，商票
        this.price_CMB_mng_Width = {
            "col_status":{width:100},
            "col_effectivedate":{width:100},
            "col_quoter":{width:200},
            "col_contact":{width:150},
            "col_tel":{width:150},
            "col_ybh":{width:100},
            "col_wbh":{width:100},
            "col_memo":{width:"*"},
            "col_oper":{width:80}
                };

          this.price_CMB_mng = [
                             this.allColums.col_status,
                             this.allColums.col_effectivedate,
                             this.allColums.col_quoter,
                             this.allColums.col_contact,
                             this.allColums.col_tel,
                             this.allColums.col_ybh,
                             this.allColums.col_wbh,
                             this.allColums.col_memo,
                             this.allColums.col_oper
                             ];

        //转贴价格管理
        this.price_npc_mng_Width = {
                "col_status":{width:100},
                "col_effectivedate":{width:100},
                "col_quoter":{width:100},
                "col_contact":{width:100},
                "col_tel":{width:150},
                "col_tradetype":{width:100},
                "col_gg":{width:80},
                "col_cs":{width:80},
                "col_ns":{width:80},
                "col_nx":{width:80},
                "col_nh":{width:80},
                "col_wz":{width:80},
                "col_cz":{width:80},
                "col_cw":{width:100},
                "col_memo":{width:"*"},
                "col_oper":{width:100}
                };


        this.pric_npc_mng = [
                             this.allColums.col_status,
                             this.allColums.col_effectivedate,
                             this.allColums.col_quoter,
                             this.allColums.col_contact,
                             this.allColums.col_tel,
                             this.allColums.col_tradetype,
                             this.allColums.col_gg,
                             this.allColums.col_cs,
                             this.allColums.col_ns,
                             this.allColums.col_nx,
                             this.allColums.col_nh,
                             this.allColums.col_wz,
                             this.allColums.col_cz,
                             this.allColums.col_cw,
                             this.allColums.col_memo,
                             this.allColums.col_oper
                            ];
    }

    [converColWith](colArray,widthArray){
        if(!angular.isArray(colArray)) return colArray;
        colArray.forEach(e =>{
            if(e.template && widthArray[e.template])
            {
                e.width = widthArray[e.template].width;
            }
        })

        return colArray;
    }

    getPriceGridCol(tickType){
        switch(tickType){
            case "BKB":
            return this[converColWith](this.price_BKB,this.price_BKB_Width);
            case "CMB":
            return this[converColWith](this.price_CMB,this.price_CMB_Width);
            default:
            return undefined;
        }
    }

    getPriceMngGridCol(tickType){
        switch(tickType){
            case "BKB":
            return this[converColWith](this.price_BKB_mng,this.price_BKB_mng_Width);
            case "CMB":
            return this[converColWith](this.price_CMB_mng,this.price_CMB_mng_Width);
            default:
            return undefined;
        }
    }

    getNpcMngGridCol(){
        return this[converColWith](this.pric_npc_mng,this.price_npc_mng_Width);
    }

    [getHeaderCellTemplate] () {
        return require('../common/component/template/header_cell_template.html');
    }
}

export default ['gridColumnFactoryService','gridDataDefineService', gridColDefineService];