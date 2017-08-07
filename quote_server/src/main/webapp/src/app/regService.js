import mainModule from './mainModule';

import commonService from '../common/service/commonService';
import userService from '../common/service/userService';
import websocketService from '../service/websocketService';
import httpService from '../common/service/httpService';
import qbService from '../common/service/qbService';
import componentCommonService from '../common/service/componentCommonService';
import gridDataDefineService from '../service/gridDataDefineService';
import iconSpritesmithService from '../service/iconSpritesmithService';
import gridColDefineService from '../service/gridColDefineService';
import gridColumnFactoryService from '../service/gridColumnFactoryService';
import accountAssociateService from '../service/accountAssociateService';
import gridPriceMarginService from '../service/gridPriceMarginService';
import gridExcelImportService from '../service/gridExcelImportService';

mainModule.service('commonService', commonService)
    .service('userService', userService)
    .service('httpService', httpService)
    .service('qbService', qbService)
    .service('componentCommonService', componentCommonService)

    .service('gridDataDefineService', gridDataDefineService)
    .service('gridColumnFactoryService', gridColumnFactoryService)
    .service('websocketService', websocketService)
    .service('babQuoteService', require('./../service/babQuoteService').default)
    .service('priceDifferenceService', require('./../service/priceDifferenceService').default)

    .service('gridColDefineService', gridColDefineService)
    .service('gridExcelImportService', gridExcelImportService)
    .service('accountAssociateService', accountAssociateService)
    .service('gridPriceMarginService',gridPriceMarginService);
export default mainModule;