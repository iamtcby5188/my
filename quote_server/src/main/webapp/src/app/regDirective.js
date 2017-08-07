import mainModule from './mainModule';
import numberFilter from '../common/filter/numberFilter';
// let app = () => {
//     return {
//         template: require('./app.html'),
//         controller: appCtrl,
//         controllerAs: 'app'
//     }
// };

// class appCtrl {
//     constructor($scope) {
//         this.url = 'https://github.com/preboot/angular-webpack';
//     };
// };

mainModule.filter('numberFilter', numberFilter);

export default mainModule;