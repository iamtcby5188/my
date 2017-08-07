import angular from 'angular';
import ngComponentRouter from 'ngComponentRouter';

// import 'angular-ui-router';

import 'ngAnimate';
import 'ngAria';
import 'ngMd5';
import 'ngSanitize';

import 'amcharts';
import 'amcharts-serial';
import 'amcharts-responsive';
import 'amcharts.css';

import 'ngMaterial';
import 'ngMaterial.css';

import 'angular-ui-grid';
import 'reconnectingwebsocket';
// import 'ng-file-upload';

// Load Avalon.UI
import 'avalon-ui';
import 'avalon-ui.css';
import 'avalon-ui-icon.css';
import 'spin';

export default angular.module('app', [
    'ngAria',
    'ngAnimate',
    'angular-md5',
    'ngMaterial',
    'ngSanitize',
    'avalon.ui',
    'ui.grid',
    'ui.grid.edit',
    'ui.grid.infiniteScroll',
    'ui.grid.cellNav',
    'ui.grid.pinning',
    // 'ui.grid.resizeColumns',
    // 'ui.grid.moveColumns',
    // 'ui.grid.saveState',
    'ngComponentRouter',
    // 'ngFileUpload'
]);