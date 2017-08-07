import mainModule from './mainModule';

import app from './../component/app';
import login from './../component/login';
import board from './../component/board';
import babCalendar from './../component/babCalendar';
import priceDifferenceAnalysis from './../component/priceDifferenceAnalysis/price_difference_analysis';
import home from './../component/home';
import babCalculate from './../component/babCalculate';

// import iconSpritesmithCtrl from './../controller/iconSpritesmithCtrl.js';

const componentMap = new Map([
    ['chartManage', require('./../component/quoteManage/chart_manage')],
    ['matrixTableManage', require('./../component/quoteManage/matrix_table_manage')],
    ['listSsrTicketHall', require('./../component/quoteManage/list_ssr_ticket_hall')],
    ['listSscTicketHall', require('./../component/quoteManage/list_ssc_ticket_hall')],
    ['listNpcTicketHall', require('./../component/quoteManage/list_npc_ticket_hall')],
    ['manageSsrTicketHall', require('./../component/quoteManage/manage_ssr_ticket_hall')],
    ['manageSscTicketHall', require('./../component/quoteManage/manage_ssc_ticket_hall')],
    ['manageNpcTicketHall', require('./../component/quoteManage/manage_npc_ticket_hall')],
    ['gridTicketHallSsr', require('./../component/quoteManage/grid_ticket_hall_ssr')],
    ['gridTicketHallSsc', require('./../component/quoteManage/grid_ticket_hall_ssc')],
    ['gridTicketHallNpc', require('./../component/quoteManage/grid_ticket_hall_npc')],
    ['gridManageSsrTicketHall', require('./../component/quoteManage/grid_manage_ssr_ticket_hall')],
    ['gridManageSscTicketHall', require('./../component/quoteManage/grid_manage_ssc_ticket_hall')],
    ['gridManageNpcTicketHall', require('./../component/quoteManage/grid_manage_npc_ticket_hall')],
    ['searchCriteriaSsr', require('./../component/quoteManage/search_criteria_ssr')],
    ['searchCriteriaCommon', require('./../component/quoteManage/search_criteria_common')],
    ['searchcriteriaAcceptinghouseQuotepricetype', require('./../component/quoteManage/searchcriteria_acceptinghouse_quotepricetype')],

    // ['listBusinessManage', require('./../component/quoteManage/list_business_manage')],
    // ['listTransferPriceManage', require('./../component/quoteManage/list_transfer_price_manage')],
    ['gridPricemanageBatchimport', require('./../component/quoteManage/grid_pricemanage_batchimport')],
    ['inputAcceptinghouseSearchDrop', require('./../component/quoteManage/input_acceptinghouse_search_drop')],
    ['addPricePriceset', require('./../component/quoteManage/add_price_priceset')],
    ['addPriceJoininguser', require('./../component/quoteManage/add_price_joininguser')],
    ['addPriceAcceptinghouseQuotepricetype', require('./../component/quoteManage/add_price_acceptinghouse_quotepricetype')],
    ['panelSearchPricemng', require('./../component/quoteManage/panel_search_pricemng')],

    ['gridCalendarSelect', require('./../component/babCalendar/grid_calendar_select')],    

    ['gridSscPrice', require('./../component/priceDifferenceAnalysis/grid_ssc_price')],
    ['gridNpcPrice', require('./../component/priceDifferenceAnalysis/grid_npc_price')],
    ['gridShiborPrice', require('./../component/priceDifferenceAnalysis/grid_shibor_price')],
    ['panelPriceSpread', require('./../component/priceDifferenceAnalysis/panel_price_spread')],
    ['chartPriceHistory', require('./../component/priceDifferenceAnalysis/chart_price_history')],
    ['chartNetVolumeHistory', require('./../component/priceDifferenceAnalysis/chart_net_volume_history')],

    ['panelAccordion', require('./../common/component/panel_accordion')],
    ['panelTitleContainer', require('./../common/component/panel_title_container')],
    // 提取为父类，只能由子类Controller继承后载入Component
    // ['panelSearchCriteria', require('./../common/component/panel_search_criteria')],   

    ['inputButtonSwitcher', require('./../common/component/input_button_switcher')],
    ['inputButtonSelector', require('./../common/component/input_button_selector')],
    ['inputDropdownChips', require('./../common/component/input_dropdown_chips')],
    ['inputDatePicker', require('./../common/component/input_date_picker')],
    ['inputLabel', require('./../common/component/input_label')],
    ['inputLabelDrop', require('./../common/component/input_label_drop')],
    ['inputRange', require('./../common/component/input_range')],
    ['inputSelectorRange', require('./../common/component/input_selector_range')],
    ['inputDatePickerRange', require('./../common/component/input_date_picker_range')],
    ['dropLabel', require('./../common/component/drop_label')],
    ['inputHidden', require('./../common/component/input_hidden')],
    ['barNav', require('./../common/component/bar_nav')],
    ['inputAutoComplete', require('./../common/component/input_auto_complete')],
    ['inputFileSelect', require('./../common/component/input_file_select')],

    ['manageUserInfo', require('./../component/accountManage/manage_user_info')],
    ['gridUserInfo', require('./../component/accountManage/grid_user_info')],
]);

let appComponent = app(),
    loginComponent = login(),
    homeComponent = home(),
    babCalendarComponent = babCalendar(),
    priceDifferenceAnalysisComponent = priceDifferenceAnalysis(),
    babCalculateComponent = babCalculate(),
    boardComponent = board();


mainModule.value('$routerRootComponent', 'app')

mainModule.component('app', appComponent)
    .component('login', loginComponent)
    .component('babCalendar', babCalendarComponent)
    .component('priceDifferenceAnalysis', priceDifferenceAnalysisComponent)
    .component('babCalculate', babCalculateComponent)
    .component('home', homeComponent)
    .component('board', boardComponent);

componentMap.forEach((value, key, map) => {
    mainModule.component(key, value.default());
});

mainModule.config(['$locationProvider', function ($locationProvider) {
    // $locationProvider.html5Mode(true);
}]);

export default mainModule;