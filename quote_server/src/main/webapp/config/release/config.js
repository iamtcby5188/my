﻿(function (angular) {
    angular.module('app').constant('configConsts', {
        tag: 'config.release const',

        DATE_FORMAT: 'yyyy-MM-dd',

        maxQuoteDays: 389,

        service_root: undefined,

        ws_address: 'ws://' + location.host + '/bab_quote/websck/babquote',

        bab_init_login: '/bab_quote/init/login',
        bab_get_token: '/bab_quote/quoteMng/getDataToken',

        bab_init_data: '/bab_quote/init/initData',

        bab_insert_ssr: '/bab_quote/quoteMng/insertSSR',
        bab_insert_ssc: '/bab_quote/quoteMng/insertSSC',
        bab_insert_npc: '/bab_quote/quoteMng/insertNPC',

        acceptinghouse_search: '/bab_quote/AcceptingCompany/searchByNameOrPY',
        acceptinghouse_search_from_yellow: '/bab_quote/AcceptingCompany/searchByNameOrPYFromYellow',

        bab_search_SSR: '/bab_quote/quoteQuery/searchSSRByParam',
        bab_search_SSC: '/bab_quote/quoteQuery/searchSSCByParam',
        bab_search_NPC: '/bab_quote/quoteQuery/searchNPCByParam',

        bab_mng_search_SSR: '/bab_quote/quoteQuery/querySSRManagerQuotesByParam',
        bab_mng_search_SSC: '/bab_quote/quoteQuery/querySSCManagerQuotesByParam',
        bab_mng_search_NPC: '/bab_quote/quoteQuery/queryNPCManagerQuotesByParam',

        bab_ssrmng_init: '/bab_quote/init/SSRInitData',
        bab_sscmng_init: '/bab_quote/init/SSCInitData',
        bab_npcmng_init: '/bab_quote/init/NPCInitData',

        bab_company_ssr: '/bab_quote/quoteQuery/searchSSRCompanies',
        bab_company_ssc: '/bab_quote/quoteQuery/searchSSCCompanies',
        bab_company_npc: '/bab_quote/quoteQuery/searchNPCCompanies',

        bab_update_ssr: '/bab_quote/quoteMng/updateSSR',
        bab_update_ssc: '/bab_quote/quoteMng/updateSSC',
        bab_update_npc: '/bab_quote/quoteMng/updateNPC',

        bab_setstatus_ssr: '/bab_quote/quoteMng/setSSRStatus',
        bab_setstatus_ssc: '/bab_quote/quoteMng/setSSCStatus',
        bab_setstatus_npc: '/bab_quote/quoteMng/setNPCStatus',

        // 直贴价格管理 当日价格概览 近期价格走势
        bab_mng_foot_current: '/bab_quote/trends/searchPriceTrendCalculation',
        bab_mng_foot_tendency: '/bab_quote/trends/searchPriceTrends',

        bab_sscmng_foot_current: '/bab_quote/trends/searchPriceTrendCalculation',
        bab_sscmng_foot_tendency: '/bab_quote/trends/searchPriceTrends',

        bill_calendar_for_date: '/bab_quote/billCalendar/billCalendarForDate',
        bill_calendar_for_month: '/bab_quote/billCalendar/billCalendarForMonth',

        // 价差分析
        price_margin_analysis_init : '/bab_quote/priceMarginAnalysis/init',
        price_margin_analysis_price : '/bab_quote/priceMarginAnalysis/analysisPrice',
        chart_get_net_volume_history: '/bab_quote/priceMarginAnalysis/getNetVolumeHistory',
        chart_get_price_trends_history: '/bab_quote/priceMarginAnalysis/getPriceTrendsHistory',

        //导入excel
        quoteExcel_insertSSR: '/bab_quote/quoteExcel/importExcelSSR',
        quoteExcel_insertSSC: '/bab_quote/quoteExcel/importExcelSSC',
        quoteExcel_insertNPC: '/bab_quote/quoteExcel/importExcelNPC',

        query_user_info: '/bab_quote/joinUser/getUserJoinInfo',
        set_user_join_relation: '/bab_quote/joinUser/setUserJoinRelation'
    });
})(window.angular)