import mainModule from './mainModule';

import '../common/script/ArrayUtils';
import '../common/script/NumberUtils';
import '../common/script/DateUtils';
import '../common/script/StringUtils';

import './regService.js';
import './regDirective.js';
import './regComponent.js';

import '../style/less/app.less';
import '../style/less/grid.less';
import '../style/less/login.less';
import '../style/less/mdStyle.less';
import '../style/less/panel_accordion.less';
import '../style/less/panel_title_container.less';
import '../style/less/drop_label.less';
import '../style/less/input_date_picker.less';
import '../style/less/input_label.less';
import '../style/less/input_label_drop.less';
import '../style/less/input_button_switcher.less';
import '../style/less/input_button_selector.less';
import '../style/less/input_dropdown_chips.less';

import '../style/less/price_difference_analysis.less';

import '../style/less/grid_ticket_hall_ssr.less';
import '../style/less/panel_search_criteria.less';
import '../style/less/input_selector_range.less';
import '../style/less/input_date_picker_range.less';
import '../style/less/panel_search_pricemng.less';
import '../style/less/list_npc_ticket_hall.less';
import '../style/less/manage_ticket_hall.less';
import '../style/less/add_price_acceptinghouse_quotepricetype.less';
import '../style/less/input_auto_complete.less';
import '../style/less/add_account_associate.less';
import '../style/less/bab_calculate.less';
import '../style/less/input_file_select.less';
import '../style/less/grid_price_margin.less';
import '../style/less/bab_calendar.less';
import '../style/less/grid_calendar_select.less';
import '../style/less/panel_price_spread.less';
import '../style/less/grid_excel_import.less';
import '../style/less/manage_user_info.less';
import '../style/less/grid_column_panel.less';
import '../style/less/home.less';
import '../style/less/md-dialog.less';

// import '../style/inputs.css';
// import '../style/less/icons.less';
// import '../style/hero.css';

const constMap = new Map([
  ['babSsrConsts', require('../const/babSsrConsts')],
  ['babSscConsts', require('../const/babSscConsts')],
  ['babNpcConsts', require('../const/babNpcConsts')]
]);

constMap.forEach((value, key, map) => {
  mainModule.constant(key, value.default);
});

mainModule.config(['$mdDateLocaleProvider', ($mdDateLocaleProvider) => {
    "use strict";

    $mdDateLocaleProvider.months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一', '十二'];
    $mdDateLocaleProvider.shortMonths = $mdDateLocaleProvider.months;
    $mdDateLocaleProvider.days = ['日', '一', '二', '三', '四', '五', '六'];
    $mdDateLocaleProvider.shortDays = $mdDateLocaleProvider.days;

    $mdDateLocaleProvider.formatDate = function(date) {
        return date.format('yyyy-MM-dd');
    };
}]);

// Set config
// mainModule.config([
//     '$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

//         $stateProvider.state('home', {
//             url: '/',
//             templateUrl: 'page/home.html'
//         }).state('inputs', {
//             url: '/inputs',
//             templateUrl: 'page/inputs.html'
//         }).state('icons', {
//             url: '/icons',
//             templateUrl: 'page/icons.html',
//             controller: iconSpritesmithCtrl
//         }).state('component_route', {
//             url: '/component_route',
//             templateUrl: 'page/component_route.html'
//         });

//         // catch all route
//         // send users to the form page 
//         $urlRouterProvider.otherwise('/');
//     }
// ]);

mainModule.config(['$mdThemingProvider', function ($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue')
    .accentPalette('light-blue')
    .warnPalette('deep-orange')
    .backgroundPalette('grey');

  var setCustomColor = function () {
    $mdThemingProvider.definePalette('ss-color-bg', {
      '50': '575F62',
      '100': '575F62',
      '200': '4C5356',
      '300': '4C5356',
      '400': '41474A',
      '500': '323739',  // ss_bg_extra
      '600': '323739',
      '700': '232628',  // ss_bg_secondary
      '800': '222527',
      '900': '161819',  // ss_bg_primary
      'A100': '151717',
      'A200': '151717',
      'A400': '151717',
      'A700': '151717',
      'contrastDefaultColor': 'light',    // whether, by default, text (contrast)
      // on this palette should be dark or light

      'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
        '200', '300', '400', 'A100'],
      'contrastLightColors': undefined    // could also specify this if default was 'dark'
    });

    $mdThemingProvider.definePalette('ss-color-pp', {
      '50': '11D3D3',
      '100': '11D3D3',
      '200': '15BBBB',
      '300': '15BBBB',
      '400': '11A7A8',
      '500': '11A7A8',
      '600': '119596',
      '700': '119596',
      '800': '097B7C',
      '900': '097B7C',
      'A100': '096060',
      'A200': '096060',
      'A400': '096060',
      'A700': '096060',
      'contrastDefaultColor': 'light',    // whether, by default, text (contrast)
      // on this palette should be dark or light

      'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
        '200', '300', '400', 'A100'],
      'contrastLightColors': undefined    // could also specify this if default was 'dark'
    });

    // #11DD69
    // #11C25B
    // #11A14C
    // #0C813C
    // #096C32
    // #094F26
    $mdThemingProvider.definePalette('ss-color-ap', {
      '50': '11DD69',
      '100': '11DD69',
      '200': '11C25B',
      '300': '11C25B',
      '400': '11A14C',
      '500': '11A14C',
      '600': '0C813C',
      '700': '0C813C',
      '800': '096C32',
      '900': '096C32',
      'A100': '094F26',
      'A200': '094F26',
      'A400': '094F26',
      'A700': '094F26',
      'contrastDefaultColor': 'light',    // whether, by default, text (contrast)
      // on this palette should be dark or light

      'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
        '200', '300', '400', 'A100'],
      'contrastLightColors': undefined    // could also specify this if default was 'dark'
    });

    // #FFBA4B
    // #FFAD1D
    // #F49C0F
    // #F48D0C
    // #CC7E09
    // #B67009
    $mdThemingProvider.definePalette('ss-color-wp', {
      '50': 'FFBA4B',
      '100': 'FFBA4B',
      '200': 'FFAD1D',
      '300': 'FFAD1D',
      '400': 'F49C0F',
      '500': 'F49C0F',
      '600': 'F48D0C',
      '700': 'F48D0C',
      '800': 'CC7E09',
      '900': 'CC7E09',
      'A100': 'B67009',
      'A200': 'B67009',
      'A400': 'B67009',
      'A700': 'B67009',
      'contrastDefaultColor': 'light',    // whether, by default, text (contrast)
      // on this palette should be dark or light

      'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
        '200', '300', '400', 'A100'],
      'contrastLightColors': undefined    // could also specify this if default was 'dark'
    });
  } ();

  $mdThemingProvider.theme('ssAvalonUi')
    .primaryPalette('ss-color-pp')
    .accentPalette('ss-color-ap')
    .warnPalette('ss-color-wp')
    .backgroundPalette('ss-color-bg');

  $mdThemingProvider.alwaysWatchTheme(true);
}]);

// Http service
mainModule.factory('userInterceptor', [
  '$q', '$injector', 'configConsts', function ($q, $injector, configConsts) {

    return {
      request: function (request) {
        // if (configConsts && configConsts.service_root && request && request.url && request.url.indexOf('.') < 0)
        // request.url = configConsts.service_root + request.url;
        return request;
      },
      requestError: function (request) {
        return request;
      },
      response: function (response) {
        return response;
      },
      responseError: function (response) {
        return $q.reject(response);
      }
    }
  }
]).config([
  '$httpProvider', '$injector', function ($httpProvider, $injector) {
    $httpProvider.interceptors.push('userInterceptor');
  }
]);